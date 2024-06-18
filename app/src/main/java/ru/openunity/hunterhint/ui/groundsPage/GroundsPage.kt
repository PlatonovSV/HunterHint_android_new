package ru.openunity.hunterhint.ui.groundsPage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.models.Ground
import ru.openunity.hunterhint.models.HuntingOffer
import ru.openunity.hunterhint.models.Review
import ru.openunity.hunterhint.ui.bottomAppBar.HuntBottomAppBar
import ru.openunity.hunterhint.ui.components.ComponentScreen
import ru.openunity.hunterhint.ui.components.ComponentSuccess
import ru.openunity.hunterhint.ui.components.DataLoading
import ru.openunity.hunterhint.ui.components.EmptyListMessage
import ru.openunity.hunterhint.ui.components.ErrorScreenE
import ru.openunity.hunterhint.ui.components.GroundImages
import ru.openunity.hunterhint.ui.enums.HuntingMethods
import ru.openunity.hunterhint.ui.enums.HuntingResources
import ru.openunity.hunterhint.ui.personal.UserCard
import ru.openunity.hunterhint.ui.search.RatingStars
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
    Scaffold(topBar = {
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
    }, bottomBar = {
        HuntBottomAppBar(navController, modifier = modifier)
    }) { padding ->
        GroundsPage(
            changeImage = { isIncrement: Boolean ->
                viewModel.changeImage(
                    isIncrement
                )
            },
            selectOffer = navigateToBooking,
            requestOffers = viewModel::getOffers,
            uiState = uiState,

            reloadReview = viewModel::downloadReview,
            reloadReviewList = viewModel::downloadReviewList,
            expandComment = viewModel::expandComment,
            changeReviewImage = viewModel::changeReviewImage,

            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun GroundsPage(
    selectOffer: (Long) -> Unit,
    changeImage: (Boolean) -> Unit,
    changeReviewImage: (Long, Boolean) -> Unit,
    requestOffers: () -> Unit,
    reloadReview: () -> Unit,
    reloadReviewList: () -> Unit,
    expandComment: (Long) -> Unit,
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
                ground = uiState.ground, modifier = Modifier
            )
        }
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UserCard(
                    user = UserCard(dto = uiState.groundsOwner, state = ComponentSuccess),
                    reloadUserCards = {},
                    showDetail = false
                )
            }
        }
        when (val offers = uiState.offers) {
            is OffersLoading -> {
                item {
                    DataLoading(
                        messageId = R.string.uploading_hunting_offers,
                        modifier = Modifier
                            .padding(18.dp)
                            .fillMaxWidth()
                            .height(136.dp)
                    )
                }
            }

            is OffersError -> {
                item {
                    ErrorScreenE(
                        retryAction = { requestOffers() },
                        modifier = Modifier
                            .padding(18.dp)
                            .fillMaxWidth()
                    )
                }
            }

            is OffersSuccess -> {
                if (offers.offers.isEmpty()) {
                    item {
                        EmptyListMessage(
                            messageId = R.string.no_offers,
                            modifier = Modifier
                                .padding(18.dp)
                                .fillMaxWidth()
                                .height(136.dp)
                        )
                    }
                } else {
                    items(offers.offers) {
                        HuntingOffer(
                            onClick = { selectOffer(it.id) }, offer = it, Modifier.padding(6.dp)
                        )
                    }
                }

            }
        }
        item {
            ComponentScreen(
                loadingStrResId = R.string.loading,
                waitContent = {},
                successContent = { mod ->
                    if (uiState.reviews.isEmpty()) {
                        EmptyListMessage(
                            messageId = R.string.no_reviews, modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        this@LazyColumn.items(uiState.reviews) { review ->
                            Review(
                                review = review,
                                reloadReview = reloadReview,
                                changeReviewImage = changeReviewImage,
                                expandComment = expandComment,
                                modifier = mod
                            )
                        }
                    }
                },
                retryOnErrorAction = reloadReviewList,
                state = uiState.reviewsListState,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp, 0.dp)
            )
        }

    }
}


@Composable
fun GroundsDescription(
    ground: Ground, modifier: Modifier = Modifier
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
                    shape = RoundedCornerShape(22.dp), color = MaterialTheme.colorScheme.tertiary
                )
                .padding(20.dp)
        ) {
            val textStyle = MaterialTheme.typography.titleMedium
            val textColor = MaterialTheme.colorScheme.onTertiary
            Text(
                text = ground.regionName, style = textStyle, color = textColor
            )
            Text(
                text = ground.municipalDistrictName, style = textStyle, color = textColor
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()
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
            val lastIndex =
                if (ground.area.toString().lastIndex > 6) 6 else ground.area.toString().lastIndex
            Text(
                text = stringResource(
                    R.string.grounds_area,
                    ground.area.toString().substring(0, lastIndex)
                ),
                style = textStyle,
                color = textColor
            )

        }
        Text(
            text = ground.description,
            modifier = Modifier.padding(18.dp, 0.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(
    showSystemUi = true, showBackground = true
)
@Composable
fun GroundsDescriptionPreview(
    modifier: Modifier = Modifier, ground: Ground = Ground(
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
        description = "The difference between subclasses and subtypes becomes especially important when\n" + "we start talking about generic types. The question from the previous section of\n" + "whether it’s safe to pass a variable of type List<String> to a function expecting\n" + "List<Any> now can be reformulated in terms of subtyping: is List<String> a subtype of\n" + "List<Any>? You’ve seen why it’s not safe to treat MutableList<String> as a subtype of\n" + "MutableList<Any>. Clearly, the reverse isn’t true either: MutableList<Any> isn’t a subtype\n" + "of MutableList<String>."
    )
) {
    HunterHintTheme {
        GroundsDescription(ground = ground)
    }
}


@Composable
fun HuntingOffer(
    onClick: () -> Unit, offer: HuntingOffer, modifier: Modifier = Modifier
) {
    Column(modifier = modifier
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
        .clickable { onClick() }) {
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
                    text = stringResource(R.string.opening_date),
                    fontWeight = FontWeight.W500,
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
                    text = stringResource(R.string.closing_date),
                    fontWeight = FontWeight.W500,
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
                    text = stringResource(R.string.hunt_price),
                    fontWeight = FontWeight.W500,
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
            text = offer.description,
            modifier = Modifier
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
            onClick = {}, offer = HuntingOffer(
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


@Composable
fun GroundsPageTitle(
    modifier: Modifier = Modifier,
    navigateToFilters: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = RoundedCornerShape(60.dp),
            )
            .fillMaxWidth()
            .clickable { navigateToFilters() }, verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = stringResource(R.string.search_filter),
            Modifier.padding(5.dp),
            tint = MaterialTheme.colorScheme.tertiary
        )
        Text(
            text = stringResource(R.string.find_in_hunter_hint),
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = 16.sp
        )
    }
}

@Composable
fun Review(
    review: Review,
    reloadReview: () -> Unit,
    changeReviewImage: (Long, Boolean) -> Unit,
    expandComment: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(), shape = MaterialTheme.shapes.extraLarge
    ) {
        ComponentScreen(
            loadingStrResId = R.string.loading,
            waitContent = {},
            successContent = {
                ReviewContent(
                    review = review,
                    changeReviewImage = changeReviewImage,
                    expandComment = expandComment
                )
            },
            retryOnErrorAction = reloadReview,
            state = review.state,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ReviewContent(
    review: Review,
    changeReviewImage: (Long, Boolean) -> Unit,
    expandComment: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.extraLarge
            ),
    ) {
        GroundImages(
            images = review.images,
            changeImage = { isIncrement -> changeReviewImage(review.id, isIncrement) },
            numberOfCurrentImage = review.numberOfCurrentImage,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Row {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(review.usersPhoto.imgSrc).crossfade(true).build(),
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = stringResource(R.string.user_profile_photo),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(12.dp)
                    .size(78.dp)
                    .clip(CircleShape)

            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    text = review.dateOfCreation,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.align(Alignment.End)
                )
                Text(
                    text = review.userName,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.align(Alignment.Start)
                )
                Text(
                    text = review.userLastName,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Start date ${review.startDate}",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.align(Alignment.Start)
                )
            }
        }
        RatingStars(
            review.starsQuantity,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    shape = MaterialTheme.shapes.large
                )
        ) {
            if (review.review.length > 80) {
                Text(
                    text = if (review.isExpanded) review.review else review.review.substring(
                        0, 64
                    ) + "...", style = MaterialTheme.typography.bodyLarge
                )
                TextButton(
                    onClick = { expandComment(review.id) }, modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Show more", style = MaterialTheme.typography.labelLarge)
                }
            } else {
                Text(
                    text = review.review, style = MaterialTheme.typography.bodyLarge
                )
            }

        }
    }
}