package ru.openunity.hunterhint.ui.personal

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amazonaws.AmazonClientException
import com.amazonaws.AmazonServiceException
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.PutObjectRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.data.booking.BookingRepository
import ru.openunity.hunterhint.data.ground.GroundRepository
import ru.openunity.hunterhint.data.user.UserRepository
import ru.openunity.hunterhint.di.BucketsFolders
import ru.openunity.hunterhint.di.ObjectStorageConfig
import ru.openunity.hunterhint.dto.UserCardDto
import ru.openunity.hunterhint.models.BookingCard
import ru.openunity.hunterhint.models.GroundsCard
import ru.openunity.hunterhint.models.database.User
import ru.openunity.hunterhint.ui.comment.convertUrisToFiles
import ru.openunity.hunterhint.ui.components.ComponentError
import ru.openunity.hunterhint.ui.components.ComponentLoading
import ru.openunity.hunterhint.ui.components.ComponentState
import ru.openunity.hunterhint.ui.components.ComponentSuccess
import ru.openunity.hunterhint.ui.components.ComponentWait
import ru.openunity.hunterhint.ui.enums.AccessLevels
import ru.openunity.hunterhint.ui.enums.Status
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PersonalViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userRepository: UserRepository,
    private val bookingRepository: BookingRepository,
    private val groundRepository: GroundRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PersonalUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.getUser().collect {
                _uiState.update { ui ->
                    if (it == null) {
                        ui.copy(
                            isLoggedIn = false
                        )
                    } else {
                        if (it != ui.user) {
                            updateSettingHints(it)
                            ui.copy(
                                user = it
                            )
                        } else {
                            ui
                        }
                    }
                }
                if (it != null && _uiState.value.user.photoUrl.isBlank()) {
                    reload()
                }
            }

        }
        viewModelScope.launch {
            while (true) {
                val user = _uiState.value.user
                if (user.jwt.isNotEmpty()) {
                    val bookingCards = try {
                        bookingRepository.getUsersBooking(
                            token = user.jwt
                        ).sortedBy { it.id }
                    } catch (e: IOException) {
                        listOf()
                    } catch (e: HttpException) {
                        listOf()
                    }
                    if (bookingCards.isNotEmpty() && _uiState.value.bookingCards != bookingCards) {
                        _uiState.update {
                            it.copy(
                                bookingCards = BookingCard.fromDtoList(bookingCards)
                            )
                        }
                    }
                }
                delay(3000)
            }
        }

        viewModelScope.launch {
            while (true) {
                val user = _uiState.value.user
                if (user.jwt.isNotEmpty()) {
                    if (user.roleCode == AccessLevels.OWNER.code) {

                        try {
                            val groundsId =
                                groundRepository.getGroundsByOwnerId(_uiState.value.user.id)
                            val groundCards =
                                groundRepository.getListOfGroundsPreview(listOf(groundsId))
                            val card = if (groundCards.isEmpty()) {
                                GroundsCard(id = Int.MIN_VALUE)
                            } else {
                                groundCards.first()
                            }

                            val usersCards = bookingRepository.getAllClientsList(
                                user.jwt
                            ).map {
                                UserCard(
                                    dto = UserCardDto(),
                                    state = ComponentSuccess
                                )
                            }

                            _uiState.update {
                                it.copy(
                                    ownersClients = usersCards,
                                    ownersGround = card
                                )
                            }

                        } catch (_: IOException) {
                        } catch (_: HttpException) {
                        }
                    }
                }
                delay(3000)
            }
        }

    }

    fun changeSection(personalSections: PersonalSections) {
        _uiState.update {
            it.copy(
                currentSection = personalSections
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.delete()
        }
    }

    fun setProfilePhoto(uri: Uri?) {
        if (uri != null) {
            _uiState.update {
                it.copy(
                    newUserProfilePicture = uri
                )
            }
        }
    }

    fun removeImage() {
        _uiState.update {
            it.copy(
                newUserProfilePicture = null
            )
        }
    }

    fun updateProfile() {
        val dtoMap = getChangedAttributes()
        dtoMap[UserAttributes.JWT.id] = _uiState.value.user.jwt
        _uiState.update {
            it.copy(
                updateUploading = ComponentLoading
            )
        }
        viewModelScope.launch {
            val uri = _uiState.value.newUserProfilePicture
            if (uri != null) {
                val urlDeferred = uploadImage(uri)
                val url = urlDeferred.await()
                if (url.isNotBlank()) {
                    dtoMap[UserAttributes.PHOTO_URL.id] = url
                } else {
                    _uiState.update {
                        it.copy(
                            updateUploading = ComponentError(R.string.no_internet)
                        )
                    }
                    return@launch
                }
            }
            val result = try {
                val id = userRepository.updateRemote(dtoMap)
                if (id == -1L) {
                    ComponentError(R.string.server_error)
                } else {
                    ComponentSuccess
                }

            } catch (e: IOException) {
                ComponentError(R.string.server_error)
            } catch (e: HttpException) {
                ComponentError(R.string.no_internet)
            }
            _uiState.update {
                it.copy(
                    updateUploading = result
                )
            }

        }


    }

    fun changeImage(isIncrement: Boolean) {
        val images = uiState.value.ownersGround.images
        var numberOfCurrentImage = uiState.value.ownersGround.numberOfCurrentImage
        if (isIncrement) {
            if (numberOfCurrentImage == images.size - 1) {
                numberOfCurrentImage = 0
            } else {
                numberOfCurrentImage++
            }
        } else {
            if (numberOfCurrentImage == 0) {
                numberOfCurrentImage = images.size - 1
            } else {
                numberOfCurrentImage--
            }
        }
        _uiState.update {
            it.copy(
                ownersGround = it.ownersGround.copy(
                    numberOfCurrentImage = numberOfCurrentImage
                )
            )
        }

    }

    private fun getChangedAttributes(): MutableMap<String, String> {
        val dtoMap = mutableMapOf<String, String>()
        val ui = _uiState.value

        if (ui.newName != ui.user.name && ui.newName.length >= 2) {
            dtoMap[UserAttributes.NAME.id] = ui.newName
        }

        if (ui.newLastName != ui.user.lastName && ui.newLastName.length >= 2) {
            dtoMap[UserAttributes.LAST_NAME.id] = ui.newLastName
        }

        if (ui.newEmail != ui.user.email && ui.newEmail.length >= 5) {
            dtoMap[UserAttributes.EMAIL.id] = ui.newEmail
        }

        if (ui.newPhoneNumber != ui.user.phoneNumber && ui.newPhoneNumber.length >= 5) {
            dtoMap[UserAttributes.PHONE_NUMBER.id] = ui.newPhoneNumber
        }

        if (ui.newPassword.length >= 8 && ui.oldPassword.length >= 8) {
            dtoMap[UserAttributes.NEW_PASSWORD.id] = ui.newPassword
            dtoMap[UserAttributes.OLD_PASSWORD.id] = ui.oldPassword
        }
        return dtoMap
    }

    fun updateNewName(userInput: String) {
        _uiState.update {
            it.copy(
                newName = userInput
            )
        }
    }

    fun updateNewLastName(userInput: String) {
        _uiState.update {
            it.copy(
                newLastName = userInput
            )
        }
    }

    fun updatePhoneNumber(userInput: String) {
        _uiState.update {
            it.copy(
                newPhoneNumber = userInput
            )
        }
    }

    fun updateNewEmail(userInput: String) {
        _uiState.update {
            it.copy(
                newEmail = userInput
            )
        }
    }

    fun updateNewPassword(userInput: String) {
        _uiState.update {
            it.copy(
                newPassword = userInput
            )
        }
    }

    fun updateOldPassword(userInput: String) {
        _uiState.update {
            it.copy(
                oldPassword = userInput
            )
        }
    }

    private fun uploadImage(uri: Uri): Deferred<String> {
        return viewModelScope.async(Dispatchers.IO) {
            val image = convertUrisToFiles(context, listOf(uri)).first()

            val objectStorageConfig = ObjectStorageConfig()
            val amazonS3Client = AmazonS3Client(
                BasicAWSCredentials(
                    objectStorageConfig.accessKeyId, objectStorageConfig.secretAccessKey
                )
            )
            amazonS3Client.setEndpoint(objectStorageConfig.endpoint)
            val bucket = objectStorageConfig.bucketName

            _uiState.update {
                it.copy(
                    updateUploading = ComponentLoading
                )
            }

            val imageId = UUID.randomUUID().toString()
            val imageExtension = image.extension
            val key = "${BucketsFolders.USER.name}/$imageId.$imageExtension"
            return@async try {
                amazonS3Client.putObject(
                    PutObjectRequest(bucket, key, image).withCannedAcl(
                        CannedAccessControlList.PublicRead
                    )
                )
                key.substringAfterLast('/')
            } catch (e: AmazonServiceException) {
                ""
            } catch (e: AmazonClientException) {
                ""
            }

        }
    }

    fun reload() {
        _uiState.update {
            it.copy(
                newUserProfilePicture = null, updateUploading = ComponentLoading
            )
        }
        viewModelScope.launch {
            val result = try {
                val dto = userRepository.getUsersData(_uiState.value.user.jwt)
                _uiState.update {
                    it.copy(
                        user = User.updateWithDto(it.user, dto)
                    )
                }
                ComponentWait
            } catch (e: IOException) {
                ComponentError(R.string.server_error)
            } catch (e: HttpException) {
                ComponentError(R.string.no_internet)
            }
            _uiState.update {
                it.copy(
                    updateUploading = result
                )
            }
        }
    }

    fun findUsers() {
        _uiState.update {
            it.copy(
                userSearchState = ComponentLoading
            )
        }
        viewModelScope.launch {
            val resultState = try {
                val userIds = userRepository.findUsers(
                    _uiState.value.user.jwt,
                    name = _uiState.value.userNameByAdmin,
                    lastName = _uiState.value.userLastNameByAdmin,
                )
                val emptyCards = mutableListOf<UserCard>()
                userIds.forEach { id ->
                    emptyCards.add(UserCard(userId = id))
                }
                _uiState.update {
                    it.copy(
                        foundByAdmin = emptyCards
                    )
                }
                ComponentSuccess
            } catch (e: IOException) {
                ComponentError(R.string.server_error)
            } catch (e: HttpException) {
                ComponentError(R.string.no_internet)
            }
            _uiState.update {
                it.copy(
                    userSearchState = resultState
                )
            }
            if (resultState is ComponentSuccess) {
                loadUserCards()
            }
        }

    }

    fun loadUserCards() {
        val cards = _uiState.value.foundByAdmin
        cards.map {
            if (it.state !is ComponentSuccess) {
                it.copy(
                    state = ComponentLoading
                )
            } else {
                it
            }
        }
        _uiState.update {
            it.copy(
                foundByAdmin = cards
            )
        }

        cards.forEach { userCard ->
            if (userCard.state !is ComponentSuccess) {
                viewModelScope.launch {
                    var newCard = userCard

                    val resultState = try {
                        val dto: UserCardDto = userRepository.getUserCard(
                            userId = userCard.userId, token = _uiState.value.user.jwt
                        )
                        newCard = newCard.copy(
                            dto = dto
                        )
                        ComponentSuccess
                    } catch (e: IOException) {
                        ComponentError(R.string.server_error)
                    } catch (e: HttpException) {
                        ComponentError(R.string.no_internet)
                    }

                    newCard = newCard.copy(
                        state = resultState
                    )
                    updateUserCard(newCard)
                }
            }

        }

    }

    private fun updateUserCard(newValue: UserCard) {
        _uiState.update { uiState ->
            uiState.copy(foundByAdmin = _uiState.value.foundByAdmin.map {
                if (it.userId == newValue.userId) {
                    newValue
                } else {
                    it
                }
            })
        }
    }

    fun dismissUserStatusBottomSheet() {
        _uiState.update {
            it.copy(
                showBottomSheet = false
            )
        }
    }

    fun setStatus(status: Int) {
        _uiState.update {
            it.copy(
                bottomSheetUserCard = it.bottomSheetUserCard.copy(
                    dto = it.bottomSheetUserCard.dto.copy(
                        status = status
                    )
                )
            )
        }

    }

    private fun saveUserStatus() {
        viewModelScope.launch {
            val currentCard = _uiState.value.bottomSheetUserCard
            var newCardDto = UserCardDto()
            val result = try {
                val updatedUserId = userRepository.setUserStatus(
                    _uiState.value.user.jwt, currentCard.userId, currentCard.dto.status
                )
                if (updatedUserId != -1L) {
                    newCardDto = userRepository.getUserCard(
                        _uiState.value.user.jwt, updatedUserId
                    )
                    ComponentSuccess
                } else {
                    ComponentError(R.string.server_error)
                }
            } catch (e: IOException) {
                ComponentError(R.string.server_error)
            } catch (e: HttpException) {
                ComponentError(R.string.no_internet)
            }
            updateCards(currentCard.userId, newCardDto, result)
        }
    }

    fun saveAdminChanges() {
        if (_uiState.value.editStatus) {
            saveUserStatus()
        } else {
            saveUserAccessLevel()
        }
    }

    fun selectUserCardForAdmin(userCard: UserCard) {
        _uiState.update {
            it.copy(
                bottomSheetUserCard = userCard, showBottomSheet = true
            )
        }
    }

    fun changeOldPasswordVisibility(isShow: Boolean) {
        _uiState.update {
            it.copy(
                isOldPasswordShow = isShow
            )
        }
    }

    fun changeNewPasswordVisibility(isShow: Boolean) {
        _uiState.update {
            it.copy(
                isNewPasswordShow = isShow
            )
        }
    }

    fun updateUserLastNameByAdmin(lastName: String) {
        _uiState.update {
            it.copy(
                userLastNameByAdmin = lastName
            )
        }
    }

    fun updateUserNameByAdmin(name: String) {
        _uiState.update {
            it.copy(
                userNameByAdmin = name
            )
        }
    }

    fun changeAdminEditSection() {
        _uiState.update {
            it.copy(
                editStatus = !it.editStatus
            )
        }
    }

    fun setAccessLevel(level: Int) {
        _uiState.update {
            it.copy(
                bottomSheetUserCard = it.bottomSheetUserCard.copy(
                    dto = it.bottomSheetUserCard.dto.copy(
                        accessLevel = level
                    )
                )
            )
        }

    }

    private fun saveUserAccessLevel() {
        viewModelScope.launch {
            val currentCard = _uiState.value.bottomSheetUserCard
            var newCardDto = UserCardDto()
            val result = try {
                val updatedUserId = userRepository.setUserAccessLevel(
                    _uiState.value.user.jwt, currentCard.userId, currentCard.dto.accessLevel
                )
                if (updatedUserId != -1L) {
                    newCardDto = userRepository.getUserCard(
                        _uiState.value.user.jwt, updatedUserId
                    )
                    ComponentSuccess
                } else {
                    ComponentError(R.string.server_error)
                }
            } catch (e: IOException) {
                ComponentError(R.string.server_error)
            } catch (e: HttpException) {
                ComponentError(R.string.no_internet)
            }
            updateCards(currentCard.userId, newCardDto, result)
        }
    }

    private fun updateCards(
        userId: Long, newCardDto: UserCardDto, result: ComponentState
    ) {
        _uiState.update { state ->
            state.copy(foundByAdmin = state.foundByAdmin.map {
                if (it.userId == userId) {
                    it.copy(
                        dto = newCardDto, state = result
                    )
                } else {
                    it
                }
            })
        }
    }


    private fun updateSettingHints(user: User) {
        _uiState.update {
            it.copy(
                newName = user.name,
                newLastName = user.lastName,
                newPhoneNumber = user.phoneNumber,
                newEmail = user.email,
            )
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            try {
                val updatedUserId = userRepository.setUserStatus(
                    _uiState.value.user.jwt, _uiState.value.user.id, Status.DELETED.code
                )
                if (updatedUserId != -1L) {
                    logout()
                }
            } catch (_: IOException) {
            } catch (_: HttpException) {
            }
        }
    }

}


enum class UserAttributes(val id: String) {
    JWT("0"), PHOTO_URL("1"), NAME("2"), LAST_NAME("3"), PHONE_NUMBER("4"), EMAIL("5"), OLD_PASSWORD(
        "6"
    ),
    NEW_PASSWORD("7"),
}