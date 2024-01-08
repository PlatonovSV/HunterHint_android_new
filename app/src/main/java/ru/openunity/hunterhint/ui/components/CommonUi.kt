package ru.openunity.hunterhint.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.models.Photo
import ru.openunity.hunterhint.ui.State


@Composable
fun GroundImages(
    images: List<Photo>,
    changeImage: (Boolean) -> Unit,
    numberOfCurrentImage: Int,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    when (images.size) {
        0 -> {
            WithoutImages(modifier = modifier)
        }

        1 -> {
            DrawImage(
                imgSrc = images[numberOfCurrentImage].imgSrc,
                contentScale = contentScale,
                modifier = modifier
            )
        }

        else -> {
            Box(
                modifier = modifier
            ) {
                // Image at the top
                DrawImage(
                    imgSrc = images[numberOfCurrentImage].imgSrc,
                    contentScale = contentScale,
                    modifier = Modifier
                        .align(Alignment.Center)
                )

                // ImageChanger at the bottom
                ImageChanger(
                    changeImage,
                    numberOfCurrentImage,
                    images.size,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                )
            }

        }
    }
}


@Composable
fun DrawImage(
    imgSrc: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    AsyncImage(
        model = ImageRequest.Builder(context = LocalContext.current).data(imgSrc)
            .crossfade(true).build(),
        error = painterResource(R.drawable.ic_broken_image),
        placeholder = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.hunting_grounds_photo),
        contentScale = contentScale,
        modifier = modifier.fillMaxSize(),
    )
}


@Composable
fun WithoutImages(modifier: Modifier = Modifier) {
    Icon(
        painter = painterResource(id = R.drawable.no_photography),
        contentDescription = null,
        modifier = modifier
            .background(MaterialTheme.colorScheme.secondary)
            .padding(60.dp),
        tint = MaterialTheme.colorScheme.onSecondary
    )
}

@Composable
fun ImageChanger(
    changeImage: (Boolean) -> Unit,
    numberOfImage: Int,
    quantity: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .background(Color(0x00000000))
            .fillMaxSize()
            .padding(0.dp, 10.dp),
    )
    {
        Row(
            modifier = Modifier
                .background(Color(0x00000000))
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowLeft,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(0.dp, 50.dp, 50.dp, 0.dp))
                    .clickable { changeImage(false) }
                    .background(
                        Color(0x00000000),
                    )
                    .padding(start = 0.dp, top = 40.dp, end = 30.dp, bottom = 40.dp)
                    .background(
                        Color(0x25000000),
                        shape = RoundedCornerShape(0.dp, 50.dp, 50.dp, 0.dp)
                    )
                    .padding(start = 0.dp, top = 14.dp, end = 14.dp, bottom = 14.dp)
                    .size(35.dp)
            )
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(50.dp, 0.dp, 0.dp, 50.dp))
                    .clickable { changeImage(true) }
                    .background(
                        Color(0x00000000),
                    )
                    .padding(start = 30.dp, top = 40.dp, end = 0.dp, bottom = 40.dp)
                    .background(
                        Color(0x25000000),
                        shape = RoundedCornerShape(50.dp, 0.dp, 0.dp, 50.dp)
                    )
                    .padding(start = 14.dp, top = 14.dp, end = 0.dp, bottom = 14.dp)
                    .size(35.dp)
            )
        }
        NavigationDots(numberOfImage, quantity, Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun NavigationDots(numberOfImage: Int, quantity: Int, modifier: Modifier = Modifier) {
    Row(
        modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val dotsModifier = Modifier
        for (i in 0 until quantity) {
            if (i == numberOfImage) {
                Icon(
                    painter = painterResource(id = R.drawable.dot),
                    contentDescription = null,
                    modifier = dotsModifier,
                    tint = MaterialTheme.colorScheme.onTertiary
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.dot),
                    contentDescription = null,
                    modifier = dotsModifier,
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

@Composable
fun Screen(
    state: State,
    retryAction: () -> Unit,
    composable: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when (state) {
            State.Loading -> LoadingScreen(Modifier.fillMaxSize())
            State.Success -> composable()
            State.Error -> ErrorScreen(retryAction, Modifier.fillMaxSize())
        }
    }
}

/**
 * The home screen displaying the loading message.
 */
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

/**
 * The home screen displaying error message with re-attempt button.
 */
@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}
