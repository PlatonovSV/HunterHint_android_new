package ru.openunity.hunterhint.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.models.GroundsCard
import ru.openunity.hunterhint.navigation.TestTag
import ru.openunity.hunterhint.ui.components.DataLoading
import ru.openunity.hunterhint.ui.components.EmptyListMessage
import ru.openunity.hunterhint.ui.components.ErrorScreenE
import ru.openunity.hunterhint.ui.components.GroundImages
import ru.openunity.hunterhint.ui.groundsPage.GroundsPageTitle
import ru.openunity.hunterhint.ui.theme.uiElements_filledStar
import ru.openunity.hunterhint.ui.theme.uiElements_twoToneStar
import java.util.ArrayList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchRoute(
    onGroundsCardClick: (Int) -> Unit,
    navigateToFilters: () -> Unit,
    groundsIds: String?,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel()
) {
    viewModel.setGroundsIds(groundsIds)
    val uiState by viewModel.searchUiState.collectAsState()
    Scaffold(
        topBar = {TopAppBar(title = { GroundsPageTitle(navigateToFilters = navigateToFilters) },Modifier.padding(30.dp, 0.dp,48.dp,0.dp))},
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        SearchScreen(
            onGroundsCardClick = onGroundsCardClick,
            uiState = uiState,
            viewModel = viewModel,
            modifier = Modifier.padding(it)
        )
    }
}

@Composable
fun SearchScreen(
    onGroundsCardClick: (Int) -> Unit,
    uiState: SearchUiState,
    viewModel: SearchViewModel,
    modifier: Modifier = Modifier
) {
    when (val groundIds = uiState.groundIds) {
        is IdsLoading -> {
            DataLoading(
                messageId = R.string.search_for_hunting_grounds, modifier = modifier.fillMaxSize()
            )
        }

        is IdsError -> {
            ErrorScreenE(
                retryAction = viewModel::getGroundIds, modifier = modifier.fillMaxSize()
            )
        }

        is IdsSuccess -> {
            if (groundIds.ids.isEmpty()) {
                EmptyListMessage(
                    messageId = R.string.no_ground_ids, modifier = modifier.fillMaxSize()
                )
            } else {
                GroundCards(
                    cards = uiState.cards,
                    changeImage = viewModel::changeImage,
                    retryGroundsUpload = viewModel::getGrounds,
                    onClick = onGroundsCardClick,
                    modifier = modifier
                )
            }

        }


    }
}

@Composable
fun GroundCards(
    cards: CardsState,
    changeImage: (groundId: Int, isIncrement: Boolean) -> Unit,
    onClick: (Int) -> Unit,
    retryGroundsUpload: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        when (cards) {
            is CardsLoading -> {
                items(cards.cards) { card ->
                    GroundItem(groundCard = card,
                        changeImage = { isIncrement: Boolean -> changeImage(card.id, isIncrement) },
                        onClick = { onClick(card.id) })
                }
                item {
                    Card(
                        shape = MaterialTheme.shapes.extraLarge,
                        modifier = Modifier
                            .padding(dimensionResource(id = R.dimen.padding_small))
                            .fillMaxWidth()
                            .height(248.dp)
                    ) {
                        DataLoading(
                            messageId = R.string.search_for_hunting_grounds,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            is CardsError -> {
                items(cards.cards) { card ->
                    GroundItem(groundCard = card,
                        changeImage = { isIncrement: Boolean -> changeImage(card.id, isIncrement) },
                        onClick = { onClick(card.id) })
                }
                item {
                    Card(
                        shape = MaterialTheme.shapes.extraLarge,
                        modifier = Modifier
                            .padding(dimensionResource(id = R.dimen.padding_small))
                            .fillMaxWidth()
                            .height(248.dp)
                    ) {
                        ErrorScreenE(
                            retryAction = retryGroundsUpload, modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            is CardsSuccess -> {
                if (cards.cards.isEmpty()) {
                    item {
                        Card(
                            shape = MaterialTheme.shapes.extraLarge,
                            modifier = Modifier
                                .padding(dimensionResource(id = R.dimen.padding_small))
                                .fillMaxWidth()
                                .height(248.dp)
                        ) {
                            EmptyListMessage(
                                messageId = R.string.no_ground_ids,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                } else {
                    items(cards.cards) { card ->
                        GroundItem(groundCard = card, changeImage = { isIncrement: Boolean ->
                            changeImage(
                                card.id, isIncrement
                            )
                        }, onClick = { onClick(card.id) })
                    }
                }
            }
        }
    }

}

@Composable
fun GroundItem(
    groundCard: GroundsCard,
    changeImage: (Boolean) -> Unit,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = MaterialTheme.shapes.extraLarge,
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_small))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_small))
        ) {
            GroundName(groundCard = groundCard, onClick, modifier = Modifier)
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(
                    192.dp
                )
            ) {
                GroundImages(
                    images = groundCard.images,
                    changeImage = changeImage,
                    numberOfCurrentImage = groundCard.numberOfCurrentImage,
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.padding_small))
                        .weight(1f)
                        .clip(MaterialTheme.shapes.extraLarge),
                )
                GroundInformation(groundCard = groundCard, onClick, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun GroundName(groundCard: GroundsCard, onClick: (Int) -> Unit, modifier: Modifier) {
    Text(text = groundCard.name,
        style = MaterialTheme.typography.titleLarge,
        modifier = modifier
            .clickable {
                onClick(groundCard.id)
            }
            .testTag(TestTag.GroundInfo.name)
            .fillMaxWidth(),
        textAlign = TextAlign.Center)
}

@Composable
fun GroundInformation(
    groundCard: GroundsCard, onClick: (Int) -> Unit, modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .clickable {
            onClick(groundCard.id)
        }
        .testTag(TestTag.GroundInfo.name)) {
        val hotelStatus = if (groundCard.isHotel) " ✓" else " ✘"
        val bathStatus = if (groundCard.isBath) " ✓" else " ✘"
        Text(
            text = "${groundCard.regionName},\n${groundCard.municipalDistrictName}",
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = stringResource(id = R.string.ground_card_square, groundCard.area.toInt()),
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = stringResource(id = R.string.ground_card_hotel, hotelStatus),
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = stringResource(id = R.string.ground_card_bath, bathStatus),
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = stringResource(id = R.string.minimum_price, groundCard.minCost),
            style = MaterialTheme.typography.titleSmall
        )
        GroundRating(
            rating = groundCard.rating, reviewsQuantity = groundCard.reviewsQuantity
        )
    }
}

@Composable
fun GroundRating(rating: Double, reviewsQuantity: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RatingStars(starsQuantity = rating.toInt(), modifier = Modifier)
        NumberOfReviews(number = reviewsQuantity, modifier = Modifier)
    }
}

@Composable
fun RatingStars(starsQuantity: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val maxNumberOfStars = 5
        repeat(starsQuantity) {
            RatingStar(modifier = Modifier, isFilled = true)
        }
        repeat(maxNumberOfStars - starsQuantity) {
            RatingStar(modifier = Modifier, isFilled = false)
        }
    }
}

@Composable
fun NumberOfReviews(number: Int, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = R.string.number_of_reviews, number),
        modifier = modifier,
        style = MaterialTheme.typography.titleSmall
    )
}

@Composable
fun RatingStar(modifier: Modifier = Modifier, isFilled: Boolean = false) {
    modifier.size(14.dp)
    if (isFilled) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = stringResource(R.string.filled_star),
            tint = uiElements_filledStar,
            modifier = modifier
        )
    } else {
        Icon(
            imageVector = Icons.TwoTone.Star,
            contentDescription = null,
            tint = uiElements_twoToneStar,
            modifier = modifier
        )
    }
}

/*Sorting And Filters */
@Composable
fun SearchAppBarTitle(modifier: Modifier = Modifier) {
    Row(
        modifier
            .clip(shape = RoundedCornerShape(50.dp, 50.dp, 50.dp, 50.dp))
            .clickable { /*TODO*/ }
            .padding(6.dp)) {
        Text(
            text = stringResource(R.string.sort_popular_first), fontSize = 16.sp
        )
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = stringResource(R.string.drop_down)
        )
    }
}

