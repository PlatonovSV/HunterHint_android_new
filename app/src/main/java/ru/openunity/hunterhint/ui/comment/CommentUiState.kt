package ru.openunity.hunterhint.ui.comment

import android.net.Uri
import ru.openunity.hunterhint.ui.components.ComponentState
import ru.openunity.hunterhint.ui.components.ComponentWait

data class CommentUiState(
    val usersComment: String = "",
    val selectedImages: List<Uri> = listOf(),
    val bookingId: Long = -1,
    val imageKeys: List<String> = listOf(),
    val maxStars: Int = 5,
    val numberOfStars: Int = 5,
    val imageUploading: ComponentState = ComponentWait,
    val commentUploading: ComponentState = ComponentWait,

) {

}