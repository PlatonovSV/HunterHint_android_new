package ru.openunity.hunterhint.ui.groundsCreationScreen

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.data.ground.GroundRepository
import ru.openunity.hunterhint.data.user.UserRepository
import ru.openunity.hunterhint.di.BucketsFolders
import ru.openunity.hunterhint.di.ObjectStorageConfig
import ru.openunity.hunterhint.dto.NewGroundDto
import ru.openunity.hunterhint.ui.comment.convertUrisToFiles
import ru.openunity.hunterhint.ui.components.ComponentError
import ru.openunity.hunterhint.ui.components.ComponentLoading
import ru.openunity.hunterhint.ui.components.ComponentSuccess
import ru.openunity.hunterhint.ui.components.ComponentWait
import ru.openunity.hunterhint.ui.searchFilters.SearchFiltersHints
import java.io.IOException
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class GroundsCreationViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val groundRepository: GroundRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroundsCreationUiState())
    val uiState = _uiState.asStateFlow()

    fun changeComment(userInput: String) {
        if (userInput.length <= 2048) {
            _uiState.update {
                it.copy(
                    newGroundDto = it.newGroundDto.copy(
                        groundsName = userInput
                    )
                )
            }
        }
    }

    fun setImages(uris: List<Uri>) {
        _uiState.update {
            it.copy(
                selectedImages = it.selectedImages + uris
            )
        }
    }

    fun createGround() {
        val uris = uiState.value.selectedImages
        val files = convertUrisToFiles(context, uris)
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
                imageUploading = ComponentLoading
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            val keys = mutableListOf<String>()
            val result = try {
                files.forEach { image ->

                    val imageId = UUID.randomUUID().toString()
                    val imageExtension = image.extension
                    val key = "${BucketsFolders.COMMENT.name}/$imageId.$imageExtension"

                    amazonS3Client.putObject(
                        PutObjectRequest(bucket, key, image).withCannedAcl(
                            CannedAccessControlList.PublicRead
                        )
                    )
                    keys.add(key.substringAfterLast('/'))
                    _uiState.update {
                        it.copy(
                            newGroundDto = it.newGroundDto.copy(
                                photoUrls = keys
                            )
                        )
                    }
                }
                uploadGround()
                ComponentSuccess
            } catch (e: AmazonServiceException) {
                ComponentError(R.string.server_error)
            } catch (e: AmazonClientException) {
                ComponentError(R.string.no_internet)
            }
            _uiState.update {
                it.copy(
                    imageUploading = result
                )
            }
        }
    }

    fun uploadGround() {
        val dto = _uiState.value.newGroundDto.copy(
            regionCode = _uiState.value.regionHint.current.first,
            municipalDistrict = _uiState.value.districtHint.current.first,
        )
        if (checkNewGroundDto(dto)) {
            viewModelScope.launch {
                _uiState.update {
                    it.copy(
                        groundsUploading = ComponentLoading
                    )
                }

                val user = userRepository.getUser().first()
                val result = try {
                    val jwt = user?.jwt ?: ""

                    val result = groundRepository.createGround(
                        dto.copy(
                            token = jwt,
                        )
                    )
                    if (result >= 0) {
                        _uiState.update {
                            it.copy(
                                newGroundDto = NewGroundDto()

                            )
                        }
                        ComponentSuccess
                    } else {
                        ComponentWait
                    }
                } catch (e: IOException) {
                    ComponentError(R.string.server_error)
                } catch (e: HttpException) {
                    ComponentError(R.string.no_internet)
                }
                _uiState.update {
                    it.copy(
                        groundsUploading = result
                    )
                }
            }
        }
    }

    private fun checkNewGroundDto(dto: NewGroundDto): Boolean {
        if (dto.groundsName.isBlank()) {
            return false
        }
        return true
    }

    fun removeImage(uri: Uri) {
        _uiState.update { state ->
            state.copy(
                selectedImages = state.selectedImages.filter { it != uri }
            )
        }
    }

    fun changeHotelCapacity(userInput: String) {
        val intValue = userInput.toIntOrNull()
        if (intValue != null) {
            _uiState.update {
                it.copy(
                    newGroundDto = it.newGroundDto.copy(
                        hotelCapacity = intValue
                    )
                )
            }
        }

    }

    fun changeMaxNumberHunters(userInput: String) {
        val intValue = userInput.toIntOrNull()
        if (intValue != null) {
            _uiState.update {
                it.copy(
                    newGroundDto = it.newGroundDto.copy(
                        maxNumberHunters = intValue
                    )
                )
            }
        }

    }

    fun changeFirstBaseCoordinate(userInput: String) {
        val doubleValue = userInput.toDoubleOrNull()
        if (doubleValue != null) {
            _uiState.update {
                it.copy(
                    newGroundDto = it.newGroundDto.copy(
                        baseCoordinate = listOf(doubleValue, it.newGroundDto.baseCoordinate.last())
                    )
                )
            }
        }

    }

    fun changeLastBaseCoordinate(userInput: String) {
        val doubleValue = userInput.toDoubleOrNull()
        if (doubleValue != null) {
            _uiState.update {
                it.copy(
                    newGroundDto = it.newGroundDto.copy(
                        baseCoordinate = listOf(it.newGroundDto.baseCoordinate.first(), doubleValue)
                    )
                )
            }
        }

    }

    fun updateThereIsBath() {
        _uiState.update {
            it.copy(
                newGroundDto = it.newGroundDto.copy(
                    informationBath = !it.newGroundDto.informationBath
                )
            )
        }
    }

    fun updateThereIsHotel() {
        _uiState.update {
            it.copy(
                newGroundDto = it.newGroundDto.copy(
                    informationHotel = !it.newGroundDto.informationHotel
                )
            )
        }
    }

    fun selectHint(hint: Pair<Int, String>, key: SearchFiltersHints) {
        _uiState.update {
            it.copy(
                districtHint = it.districtHint.selectHint(hint, key),
                regionHint = it.regionHint.selectHint(hint, key),
            )
        }
        if (key == SearchFiltersHints.REGION) {
            viewModelScope.launch {
                try {
                    val dtoList = groundRepository.getDistricts(hint.first)
                    _uiState.update { state ->
                        state.copy(
                            districtHint = state.districtHint.copy(allLocal = dtoList.map {
                                Pair(it.id, it.name)
                            })
                        )
                    }
                } catch (_: IOException) {

                } catch (_: HttpException) {

                }
            }
        }
    }


    fun setAllLocal(
        allLocalRegions: List<Pair<Int, String>>
    ) {
        _uiState.update {
            it.copy(

                regionHint = it.regionHint.copy(allLocal = allLocalRegions),
            )
        }
    }

    fun updateHint(userInput: String, key: SearchFiltersHints) {
        _uiState.update {
            it.copy(
                districtHint = it.districtHint.updateHints(userInput, key),
                regionHint = it.regionHint.updateHints(userInput, key),
            )
        }
    }

    fun updateGroundsDescription(userInput: String) {
        if (userInput.length <= 2048) {
            _uiState.update {
                it.copy(
                    newGroundDto = it.newGroundDto.copy(
                        groundsDescription = userInput
                    )
                )
            }
        }
    }

    fun updateCompanyName(userInput: String) {
        if (userInput.length <= 2048) {
            _uiState.update {
                it.copy(
                    newGroundDto = it.newGroundDto.copy(
                        companyName = userInput
                    )
                )
            }
        }
    }

    fun updateAccommodationCost(userInput: String) {
        val intValue = userInput.toIntOrNull()
        if (intValue != null) {
            _uiState.update {
                it.copy(
                    newGroundDto = it.newGroundDto.copy(
                        accommodationCost = intValue
                    )
                )
            }
        }
    }

    fun updateGroundsArea(userInput: String) {
        val doubleValue = userInput.toDoubleOrNull()
        if (doubleValue != null) {
            _uiState.update {
                it.copy(
                    newGroundDto = it.newGroundDto.copy(
                        groundsArea = doubleValue
                    )
                )
            }
        }
    }

}