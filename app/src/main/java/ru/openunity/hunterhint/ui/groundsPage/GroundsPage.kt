package ru.openunity.hunterhint.ui.groundsPage

import androidx.annotation.StringRes
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.models.Ground
import ru.openunity.hunterhint.models.HuntingOffer
import ru.openunity.hunterhint.ui.bottomAppBar.HuntBottomAppBar
import ru.openunity.hunterhint.ui.components.ErrorScreenE
import ru.openunity.hunterhint.ui.components.GroundImages
import ru.openunity.hunterhint.ui.enums.HuntingMethods
import ru.openunity.hunterhint.ui.enums.HuntingResources
import ru.openunity.hunterhint.ui.theme.HunterHintTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GroundsPageRoute(
    groundsId: Int,
    canNavigateBack: Boolean,
    navigateToBooking: (Long) -> Unit,
    navigateUp: () -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: GroundsPageViewModel = hiltViewModel()
) {
    viewModel.onGroundsCardClick(groundsId)
    val uiState by viewModel.uiState.collectAsState()
    val appBarColors = TopAppBarDefaults.mediumTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    )
    Scaffold(
        topBar = {
            TopAppBar(title = {
                GroundsPageTitle()
            }, colors = appBarColors, actions = {
                Row {
                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(id = R.drawable.ios_share),
                            contentDescription = stringResource(
                                id = R.string.share
                            ),
                            modifier = Modifier.padding(8.dp, 6.dp)
                        )
                    }
                    //Add ground to favorite button
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.favorite),
                            contentDescription = stringResource(
                                id = R.string.to_favorite
                            ),
                            modifier = Modifier.padding(0.dp, 4.dp, 8.dp, 4.dp)
                        )
                    }
                }
            }, modifier = modifier, navigationIcon = {
                if (canNavigateBack) {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                }
            })
        },
        bottomBar = {
            HuntBottomAppBar(navController, modifier = modifier)
        }
    ) { padding ->
        GroundsPage(
            changeImage = { isIncrement: Boolean ->
                viewModel.changeImage(
                    isIncrement
                )
            },
            selectOffer = navigateToBooking,
            requestOffers = viewModel::getOffers,
            uiState = uiState,
            modifier = Modifier
                .padding(padding)
        )
    }
}

@Composable
fun GroundsPage(
    selectOffer: (Long) -> Unit,
    changeImage: (Boolean) -> Unit,
    requestOffers: () -> Unit,
    uiState: GroundsPageUiState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        item {
            GroundImages(
                images = uiState.ground.images,
                changeImage = changeImage,
                numberOfCurrentImage = uiState.numberOfCurrentImage,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                ContentScale.Fit
            )
        }
        item {
            GroundsDescription(
                ground = uiState.ground,
                modifier = Modifier
            )
        }
        when (val offers = uiState.offers) {
            is OffersLoading -> {
                item {
                    DataLoading(
                        messageId = R.string.uploading_hunting_offers, modifier = Modifier
                            .padding(18.dp)
                            .fillMaxWidth()
                            .height(136.dp)
                    )
                }
            }

            is OffersError -> {
                item {
                    ErrorScreenE(
                        retryAction = { requestOffers() }, modifier = Modifier
                            .padding(18.dp)
                            .fillMaxWidth()
                    )
                }
            }

            is OffersSuccess -> {
                if (offers.offers.isEmpty()) {
                    item {
                        EmptyListMessage(
                            messageId = R.string.no_offers, modifier = Modifier
                                .padding(18.dp)
                                .fillMaxWidth()
                                .height(136.dp)
                        )
                    }
                } else {
                    items(offers.offers) {
                        HuntingOffer(
                            onClick = { selectOffer(it.id) },
                            offer = it,
                            Modifier.padding(6.dp)
                        )
                    }
                }

            }
        }

    }
}


@Composable
fun GroundsDescription(
    ground: Ground,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = ground.name,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Column(
            modifier = Modifier
                .padding(6.dp, 18.dp)
                .fillMaxWidth()
                .background(
                    shape = RoundedCornerShape(22.dp),
                    color = MaterialTheme.colorScheme.tertiary
                )
                .padding(20.dp)
        ) {
            val textStyle = MaterialTheme.typography.titleMedium
            val textColor = MaterialTheme.colorScheme.onTertiary
            Text(
                text = ground.regionName,
                style = textStyle,
                color = textColor
            )
            Text(
                text = ground.municipalDistrictName,
                style = textStyle,
                color = textColor
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                val hotelStatus = if (ground.isHotel) " ✓" else " ✘"
                val bathStatus = if (ground.isBath) " ✓" else " ✘"
                Text(
                    text = stringResource(id = R.string.ground_card_hotel, hotelStatus),
                    style = textStyle,
                    color = textColor
                )
                Text(
                    text = stringResource(id = R.string.ground_card_bath, bathStatus),
                    style = textStyle,
                    color = textColor
                )
            }
            Text(
                text = stringResource(R.string.accommodation_cost, ground.accommodationCost),
                style = textStyle,
                color = textColor
            )
            Text(
                text = stringResource(R.string.grounds_area, ground.area),
                style = textStyle,
                color = textColor
            )

        }
        Text(
            text = ground.description, modifier = Modifier.padding(18.dp, 0.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun GroundsDescriptionPreview(
    modifier: Modifier = Modifier,
    ground: Ground = Ground(
        name = "Beutyful place",
        companyName = "Strong hunt team",
        area = 2334.3,
        isBath = true,
        isHotel = true,
        maxHunters = 10,
        hotelCapacity = 12,
        accommodationCost = 3000,
        regionName = "Кировская область",
        municipalDistrictName = "Зуевский район",
        baseCoordinate = "123.11 1234.4",
        minCost = 35000,
        rating = 4.35,
        reviewsQuantity = 15,
        description = "The difference between subclasses and subtypes becomes especially important when\n" +
                "we start talking about generic types. The question from the previous section of\n" +
                "whether it’s safe to pass a variable of type List<String> to a function expecting\n" +
                "List<Any> now can be reformulated in terms of subtyping: is List<String> a subtype of\n" +
                "List<Any>? You’ve seen why it’s not safe to treat MutableList<String> as a subtype of\n" +
                "MutableList<Any>. Clearly, the reverse isn’t true either: MutableList<Any> isn’t a subtype\n" +
                "of MutableList<String>."
    )
) {
    HunterHintTheme {
        GroundsDescription(ground = ground)
    }
}


@Composable
fun HuntingOffer(
    onClick: () -> Unit,
    offer: HuntingOffer,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(0.dp, 36.dp)
            .background(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            .padding(4.dp)
            .background(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.primaryContainer
            )
            .padding(16.dp)
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Text(
            text = stringResource(id = offer.typeStringRes),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row {
            Column(
                modifier = Modifier
                    .padding(8.dp, 0.dp)
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.opening_date), fontWeight = FontWeight.W500,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.End)
                )
                Text(
                    text = offer.openingDate.format(DateTimeFormatter.ofPattern("dd.MM.yy")),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.closing_date), fontWeight = FontWeight.W500,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.End)
                )
                Text(
                    text = offer.closingDate.format(DateTimeFormatter.ofPattern("dd.MM.yy")),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Start)
                )
            }
            Column(
                modifier = Modifier
                    .padding(8.dp, 0.dp)
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.hunt_price), fontWeight = FontWeight.W500,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.End)
                )
                Text(
                    text = stringResource(R.string.rubles, offer.eventCost),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.guiding_preference),
                    fontWeight = FontWeight.W500,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.End,
                    modifier = Modifier.align(Alignment.End)
                )
                Text(
                    text = stringResource(id = offer.guidingStringRes),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Start)
                )
            }
        }
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = offer.description, modifier = Modifier
                .background(
                    shape = MaterialTheme.shapes.large,
                    color = MaterialTheme.colorScheme.tertiaryContainer
                )
                .padding(20.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )
    }

}

@Preview(showBackground = true)
@Composable
fun HuntingOfferPreview(modifier: Modifier = Modifier) {
    HunterHintTheme {
        HuntingOffer(
            onClick = {},
            offer = HuntingOffer(
                resourcesTypeId = HuntingResources.BEARS.id,
                openingDate = LocalDateTime.now(),
                closingDate = LocalDateTime.now().plusDays(99),
                methodIds = listOf(
                    HuntingMethods.BY_PEN.id,
                    HuntingMethods.FROM_THE_APPROACH.id,
                    HuntingMethods.FROM_THE_ENTRANCE_BY_HORSE_DRAWN_TRANSPORT.id,
                    HuntingMethods.FROM_HIDING.id
                ),
                eventCost = 120000,
                guidingPreferenceId = 1,
                description = "В нашем хозяйстве из года в год успешные охоты. Медведей нынче особенно много. Наши специалисты сделают вашу охоту результативной и увлекательной."
            )
        )
    }
}
/*
Select hunt

Вид дичи
Колличество

Колличество дней
Потребность в гостинице и в бане

Колличество охотниуков

Способ охоты


 */
/*

    val companyName: String = "",
    val maxHunters: Int = 0,
    val hotelCapacity: Int = 0,
    val accommodationCost: Int = 0,
    val baseCoordinate: String = "",
    val minCost: Int = 0,
    val rating: Double = .0,
    val reviewsQuantity: Int = 0,
 */

//description

//comment

//share button
@Composable
fun DataLoading(
    @StringRes messageId: Int,
    modifier: Modifier = Modifier
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = modifier) {
        Text(text = stringResource(id = messageId), style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.height(16.dp))
        AnimationOfLoading()
    }
}

@Composable
fun AnimationOfLoading() {
    val infiniteAnimation = rememberInfiniteTransition(label = "infinite animation")
    val morphProgress = infiniteAnimation.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "morph"
    )
    val color = MaterialTheme.colorScheme.onTertiaryContainer
    Box(
        modifier = Modifier
            .drawWithCache {
                val triangle = RoundedPolygon(
                    numVertices = 7,
                    radius = size.minDimension / 2f,
                    centerX = size.width / 2f,
                    centerY = size.height / 2f,
                    rounding = CornerRounding(
                        size.minDimension / 10f,
                        smoothing = 0.1f
                    )
                )
                val square = RoundedPolygon(
                    numVertices = 4,
                    radius = size.minDimension / 2f,
                    centerX = size.width / 2f,
                    centerY = size.height / 2f,
                    rounding = CornerRounding(
                        size.minDimension / 10f,
                        smoothing = 0.1f
                    )
                )

                val morph = Morph(start = triangle, end = square)
                val morphPath = morph
                    .toPath(progress = morphProgress.value)
                    .asComposePath()

                onDrawBehind {
                    drawPath(morphPath, color = color)
                }
            }
            .size(278.dp)
    )

}

@Composable
fun EmptyListMessage(
    @StringRes messageId: Int,
    modifier: Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = messageId),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun GroundsPageTitle(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = RoundedCornerShape(60.dp),
            )
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = stringResource(R.string.search_filter),
            Modifier.padding(5.dp), tint = MaterialTheme.colorScheme.tertiary
        )
        Text(
            text = stringResource(R.string.find_in_hunter_hint),
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = 16.sp
        )
    }
}

