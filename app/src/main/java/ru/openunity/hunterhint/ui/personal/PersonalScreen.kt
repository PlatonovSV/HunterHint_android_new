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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.models.BookingCard
import ru.openunity.hunterhint.ui.components.EmptyListMessage
import ru.openunity.hunterhint.ui.theme.HunterHintTheme
import kotlin.math.max

enum class PersonalSections(@StringRes val sectionNameId: Int) {
    SETTINGS(R.string.settings), BOOKINGS(R.string.bookings), FAVORITES(R.string.favorites)
}

@Composable
fun PersonalAccountScreen(
    navigateToAuth: () -> Unit,
    navigateToBookingInfo: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PersonalViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val imgUrl = ""
    val currentSection = uiState.currentSection
    Column(modifier = modifier, verticalArrangement = Arrangement.Top) {
        ProfilePhoto(imgUrl = imgUrl)
        PersonalMenu(
            currentSection = currentSection,
            changeSection = viewModel::changeSection
        )
        when (currentSection) {
            PersonalSections.BOOKINGS -> {
                UserBookings(
                    navigateToBookingInfo = navigateToBookingInfo,
                    userBookings = uiState.bookingCards,
                    Modifier.fillMaxSize()
                )
            }

            PersonalSections.FAVORITES -> {
                FavoritesScreen()
            }

            PersonalSections.SETTINGS -> {
                SettingsScreen(uiState, onClickLogOut = {
                    viewModel.logout()
                    navigateToAuth()
                })
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
        Spacer(modifier = Modifier.weight(1f))
        LogoutButton(
            onClickLogOut,
            Modifier
                .padding(22.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun LogoutButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(onClick = onClick, modifier = modifier) {
        Text(
            text = stringResource(id = R.string.logout), style = MaterialTheme.typography.labelLarge
        )
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

@Composable
fun PersonalAccountScreenPreview(modifier: Modifier = Modifier) {
    HunterHintTheme {
        PersonalAccountScreen({ },{})
    }
}


@Composable
fun UserBookings(
    navigateToBookingInfo: (Long) -> Unit,
    userBookings: List<BookingCard>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (userBookings.isEmpty()) {
            item {
                EmptyListMessage(
                    messageId = R.string.no_bookings,
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else {
            items(userBookings) {
                UsersBookingCard(it,navigateToBookingInfo)
            }
        }
    }
}

@Composable
fun UsersBookingCard(
    content: BookingCard,
    navigateToBookingInfo: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = MaterialTheme.shapes.extraLarge,
        elevation = CardDefaults.elevatedCardElevation(),
        modifier = modifier
            .padding(18.dp, 8.dp)
            .fillMaxWidth()
            .clickable { navigateToBookingInfo(content.id) }
    ) {
        UsersBookingCardContent(content = content)
    }
}

@Composable
fun UsersBookingCardContent(
    content: BookingCard,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = content.typeStrRes),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = content.groundsName,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "${content.startDate} — ${content.finalDate}",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.headlineSmall
        )
    }
}


