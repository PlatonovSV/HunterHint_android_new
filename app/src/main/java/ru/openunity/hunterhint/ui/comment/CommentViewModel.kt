package ru.openunity.hunterhint.ui.comment

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
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
import ru.openunity.hunterhint.data.comment.CommentRepository
import ru.openunity.hunterhint.data.user.UserRepository
import ru.openunity.hunterhint.di.BucketsFolders
import ru.openunity.hunterhint.di.ObjectStorageConfig
import ru.openunity.hunterhint.dto.NewReviewDto
import ru.openunity.hunterhint.ui.components.ComponentError
import ru.openunity.hunterhint.ui.components.ComponentLoading
import ru.openunity.hunterhint.ui.components.ComponentSuccess
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class CommentViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CommentUiState())
    val uiState = _uiState.asStateFlow()

    fun changeComment(userInput: String) {
        if (userInput.length <= 2048) {
            _uiState.update {
                it.copy(
                    usersComment = userInput
                )
            }
        }
    }

    fun setBookingId(bookingId: Long) {
        _uiState.update {
            CommentUiState(bookingId = bookingId)
        }
    }

    fun setImages(uris: List<Uri>) {
        _uiState.update {
            it.copy(
                selectedImages = it.selectedImages + uris
            )
        }
    }

    fun createComment() {
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
                            imageKeys = keys
                        )
                    }
                }
                uploadComment()
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

    fun uploadComment() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    commentUploading = ComponentLoading
                )
            }
            val dto = NewReviewDto(
                feedback = uiState.value.usersComment,
                rating = uiState.value.numberOfStars,
                images = uiState.value.imageKeys,
                bookingId = uiState.value.bookingId
            )
            val user = userRepository.getUser().first()
            val result = try {
                val jwt = user?.jwt ?: ""
                commentRepository.create(jwt, dto)
                ComponentSuccess
            } catch (e: IOException) {
                ComponentError(R.string.server_error)
            } catch (e: HttpException) {
                ComponentError(R.string.no_internet)
            }
            _uiState.update {
                it.copy(
                    commentUploading = result
                )
            }
        }
    }

    fun removeImage(uri: Uri) {
        _uiState.update { state ->
            state.copy(
                selectedImages = state.selectedImages.filter { it != uri }
            )
        }
    }

    fun selectRating(numberOfStars: Int) {
        _uiState.update {
            it.copy(
                numberOfStars = numberOfStars + 1
            )
        }
    }

}

private fun convertUrisToFiles(context: Context, uris: List<Uri>): List<File> {
    return uris.mapNotNull { uri ->
        // Check if the URI is from a file provider
        if (uri.scheme == "content") {
            // Handle content URIs (likely from media or file providers)
            val contentResolver = context.contentResolver
            val cursor = contentResolver.query(uri, null, null, null, null)

            cursor?.use {
                if (it.moveToFirst()) {
                    val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    val fileName = if (nameIndex != -1) it.getString(nameIndex) else "temp_file"
                    val file = File(context.cacheDir, fileName)

                    // Copy the content to a temporary file
                    contentResolver.openInputStream(uri)?.use { inputStream ->
                        FileOutputStream(file).use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }

                    return@mapNotNull file
                }
            }
        } else if (uri.scheme == "file") {
            // Handle file URIs directly
            return@mapNotNull File(uri.path!!)
        }

        // If the URI scheme is not supported, return null for this item
        null
    }
}