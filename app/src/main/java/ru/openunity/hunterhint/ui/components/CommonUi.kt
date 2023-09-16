package ru.openunity.hunterhint.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.model.Image


@Composable
fun GroundImages(
    images: List<Image>,
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
                resId = images[numberOfCurrentImage].id,
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
                    resId = images[numberOfCurrentImage].id,
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
    @DrawableRes resId: Int,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    Image(
        painter = painterResource(id = resId),
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale
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
