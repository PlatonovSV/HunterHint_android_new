package ru.openunity.hunterhint.ui.personal

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.ui.theme.HunterHintTheme
import kotlin.math.max

enum class PersonalSections(@StringRes val sectionNameId: Int) {
    SETTINGS(R.string.settings), BOOKINGS(R.string.bookings), FAVORITES(R.string.favorites)
}

@Composable
fun PersonalAccountScreen(
    uiState: PersonalUiState,
    changeSection: (PersonalSections) -> Unit,
    imgUrl: String = "", modifier: Modifier = Modifier
) {
    val currentSection = uiState.currentSection
    /*
           Фотография
           пользователя

    Список охот
    Любимые хозяйства
    Настройки
     */
    Column(modifier = modifier, verticalArrangement = Arrangement.Top) {
        ProfilePhoto(imgUrl = imgUrl)
        PersonalMenu(
            currentSection = currentSection,
            changeSection = changeSection
        )
        when (currentSection) {
            PersonalSections.BOOKINGS -> {
                BookingScreen()
            }

            PersonalSections.FAVORITES -> {
                FavoritesScreen()
            }

            PersonalSections.SETTINGS -> {
                SettingsScreen(uiState, onClickLogOut = {})
            }
        }
    }
}

@Composable
fun ProfilePhoto(
    imgUrl: String, modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current).data(imgUrl)
                .crossfade(true).build(),
            error = painterResource(R.drawable.ic_broken_image),
            placeholder = painterResource(R.drawable.loading_img),
            contentDescription = stringResource(R.string.user_profile_photo),
            contentScale = ContentScale.Crop,
            modifier = modifier.size(270.dp),
        )
    }
}

@Composable
fun PersonalMenu(
    currentSection: PersonalSections,
    changeSection: (PersonalSections) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        PersonalSections.entries.forEach {
            HorizontalMenuElement(
                isActive = it == currentSection,
                changeSection = { changeSection(it) },
                sectionNameId = it.sectionNameId
            )
        }
    }
}

@Composable
fun HorizontalMenuElement(
    isActive: Boolean,
    @StringRes sectionNameId: Int,
    changeSection: () -> Unit,
    modifier: Modifier = Modifier,
    isWrong: Boolean = false
) {
    val hexagon = remember {
        RoundedPolygon(
            6, rounding = CornerRounding(0.2f)
        )
    }
    val clip = remember(hexagon) {
        RoundedPolygonShape(polygon = hexagon)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier
            .padding(
                horizontal = 24.dp
            )
            .clickable { changeSection() }
    ) {
        Text(
            text = stringResource(id = sectionNameId),
            maxLines = 1,
            style = MaterialTheme.typography.titleLarge,
            color = if (isActive) MaterialTheme.colorScheme.secondary else Color.Unspecified
        )
        when {
            isActive -> {
                Box(
                    modifier = Modifier
                        .clip(clip)
                        .background(MaterialTheme.colorScheme.secondary)
                        .size(24.dp)
                ) {}
            }
            isWrong -> {
                Box(
                    modifier = Modifier
                        .clip(clip)
                        .background(MaterialTheme.colorScheme.error)
                        .size(18.dp)
                ) {}
            }
        }
    }
}


@Composable
fun SettingsScreen(
    uiState: PersonalUiState,
    onClickLogOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(text = uiState.user.toString())
        Spacer(modifier = Modifier.weight(1f))
        Button(
            shape = MaterialTheme.shapes.medium, onClick = onClickLogOut,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .background(color = MaterialTheme.colorScheme.error),
        ) {
            Text(
                text = stringResource(id = R.string.logout),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun BookingScreen(modifier: Modifier = Modifier) {
    Column {

    }
}

@Composable
fun FavoritesScreen(modifier: Modifier = Modifier) {
    Column {

    }
}


@Composable
fun PersonalButton(
    onClick: () -> Unit, @DrawableRes iconPainterResourceId: Int, modifier: Modifier = Modifier
) {
    Button(onClick = onClick, modifier = modifier) {
        Row(modifier = Modifier) {
            Icon(painter = painterResource(id = iconPainterResourceId), contentDescription = "")
        }
    }
}

fun RoundedPolygon.getBounds() = calculateBounds().let { Rect(it[0], it[1], it[2], it[3]) }
class RoundedPolygonShape(
    private val polygon: RoundedPolygon, private var matrix: Matrix = Matrix()
) : Shape {
    private var path = Path()
    override fun createOutline(
        size: Size, layoutDirection: LayoutDirection, density: Density
    ): Outline {
        path.rewind()
        path = polygon.toPath().asComposePath()
        matrix.reset()
        val bounds = polygon.getBounds()
        val maxDimension = max(bounds.width, bounds.height)
        matrix.scale(size.width / maxDimension, size.height / maxDimension)
        matrix.translate(-bounds.left, -bounds.top)

        path.transform(matrix)
        return Outline.Generic(path)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PersonalAccountScreenPreview(modifier: Modifier = Modifier) {
    HunterHintTheme {
        PersonalAccountScreen(
            uiState = PersonalUiState(),
            changeSection = {},
            imgUrl = ""
        )
    }
}
