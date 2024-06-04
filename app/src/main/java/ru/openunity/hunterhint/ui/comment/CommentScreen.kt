package ru.openunity.hunterhint.ui.comment

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.navigation.HuntTopAppBar
import ru.openunity.hunterhint.ui.bottomAppBar.HuntBottomAppBar
import ru.openunity.hunterhint.ui.components.ComponentScreen
import ru.openunity.hunterhint.ui.components.ComponentState
import ru.openunity.hunterhint.ui.components.ComponentWait
import ru.openunity.hunterhint.ui.search.RatingStar

@Composable
internal fun CommentRoute(
    bookingId: Long,
    navigateUp: () -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: CommentViewModel = hiltViewModel()
) {
    if (bookingId > 0) {
        viewModel.setBookingId(bookingId)
    }
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(topBar = {
        HuntTopAppBar(
            strResId = R.string.create_comment, navigateUp = navigateUp
        )
    }, bottomBar = { HuntBottomAppBar(navController = navController) }) { padding ->
        ComponentScreen(
            loadingStrResId = R.string.creating_review,
            waitContent = {
                CommentScreen(
                    comment = uiState.usersComment,
                    setImages = viewModel::setImages,
                    createComment = viewModel::createComment,
                    selectedImages = uiState.selectedImages,
                    onCommentChange = viewModel::changeComment,
                    imagesSent = uiState.imageKeys.size,
                    removeImage = viewModel::removeImage,
                    maxStars = uiState.maxStars,
                    numberOfStars = uiState.numberOfStars,
                    selectRating = viewModel::selectRating,
                    imageSentState = uiState.imageUploading,
                    modifier = it.padding(padding)
                )
            },
            successContent = {
                navigateUp()
            },
            retryOnErrorAction = viewModel::uploadComment,
            state = uiState.commentUploading,
            modifier = modifier.fillMaxSize()
        )
    }
}


@Composable
fun CommentScreen(
    comment: String,
    imagesSent: Int,
    maxStars: Int,
    numberOfStars: Int,
    selectRating: (Int) -> Unit,
    removeImage: (Uri) -> Unit,
    imageSentState: ComponentState,
    createComment: () -> Unit,
    selectedImages: List<Uri>,
    setImages: (List<Uri>) -> Unit,
    onCommentChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) {
            setImages(it)
        }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ComponentScreen(
            loadingMessage = stringResource(
                id = R.string.uploaded_out_of,
                imagesSent,
                selectedImages.size
            ),
            waitContent = {
                LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                    items(selectedImages) { uri ->
                        Image(painter = rememberAsyncImagePainter(uri),
                            contentScale = ContentScale.FillWidth,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(16.dp, 8.dp)
                                .size(100.dp)
                                .clickable {
                                    removeImage(uri)
                                })
                    }
                }
            },
            successContent = {},
            retryOnErrorAction = createComment,
            state = imageSentState,
            loadingStrResId = R.string.empty
        )


        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            repeat(numberOfStars) {
                RatingStar(isFilled = true, modifier = Modifier.clickable { selectRating(it) })
            }
            repeat(maxStars - numberOfStars) {
                RatingStar(modifier = Modifier.clickable { selectRating(numberOfStars + it) })
            }
        }

        OutlinedTextField(value = comment,
            onValueChange = onCommentChange,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            enabled = imageSentState == ComponentWait,
            shape = RoundedCornerShape(42.dp),
            label = {
                Text(
                    text = stringResource(id = R.string.comment),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            suffix = {
                ImageSelectionButton(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier.wrapContentSize()
                )
            })
        if (comment.isNotBlank()) {
            Button(
                onClick = createComment,
                enabled = imageSentState == ComponentWait,
                modifier = Modifier.padding(
                    dimensionResource(id = R.dimen.padding_medium)
                ), shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = stringResource(R.string.create_comment),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

    }
}

@Composable
fun ImageSelectionButton(
    onClick: () -> Unit, modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            painter = painterResource(id = R.drawable.add_photo),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = null
        )
    }
}
