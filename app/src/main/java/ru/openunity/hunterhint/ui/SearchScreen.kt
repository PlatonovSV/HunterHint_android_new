package ru.openunity.hunterhint.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.models.Rating
import ru.openunity.hunterhint.ui.components.GroundImages
import ru.openunity.hunterhint.ui.theme.uiElements_filledStar
import ru.openunity.hunterhint.ui.theme.uiElements_twoToneStar

@Composable
fun GroundCards(
    groundCards: List<GroundCardUiState>,
    changeImage: (groundId: Int, isIncrement: Boolean) -> Unit,
    onClick: (Int) -> Unit
) {
    LazyColumn {
        items(groundCards) {
            GroundItem(
                groundCard = it,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
                onClick = onClick,
                changeImage = { isIncrement: Boolean -> changeImage(it.id, isIncrement) }
            )
        }
    }
}

@Composable
fun SearchScreen(
    searchUiState: SearchUiState,
    changeImage: (Int, Boolean) -> Unit,
    onGroundsCardClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        GroundCards(searchUiState.cards, { groundId, isIncrement ->
            changeImage(
                groundId,
                isIncrement
            )
        }) { groundId: Int ->
            onGroundsCardClick(groundId)
        }
    }
}

@Composable
fun GroundItem(
    groundCard: GroundCardUiState,
    changeImage: (Boolean) -> Unit,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_small))
        ) {
            GroundImages(
                images = groundCard.images,
                changeImage = changeImage,
                numberOfCurrentImage = groundCard.numberOfCurrentImage,
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.image_size))
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clip(MaterialTheme.shapes.medium),
            )
            GroundInformation(groundCard = groundCard, onClick, modifier = Modifier)
        }
    }
}

@Composable
fun GroundInformation(
    groundCard: GroundCardUiState,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .fillMaxWidth()
        .clickable {
            onClick(groundCard.id)
        }
        .testTag(TestTag.GroundInfo.name)) {
        Text(
            text = groundCard.name,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.displayMedium
        )
        val hotelStatus = if (groundCard.isHotel) " ✓" else " ✘"
        val bathStatus = if (groundCard.isBath) " ✓" else " ✘"
        Text(
            text = "${groundCard.regionName},\n${groundCard.municipalDistrictName}",
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = stringResource(id = R.string.ground_card_square, groundCard.area.toInt()),
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = stringResource(id = R.string.ground_card_hotel, hotelStatus),
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = stringResource(id = R.string.ground_card_bath, bathStatus),
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = stringResource(id = R.string.minimum_price, groundCard.minCost),
            style = MaterialTheme.typography.labelSmall
        )
        GroundRating(rating = groundCard.rating)
    }
}

@Composable
fun GroundRating(rating: Rating, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RatingStars(starsQuantity = rating.rating, modifier = Modifier)
        NumberOfReviews(number = rating.numberOfReview, modifier = Modifier)
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
        style = MaterialTheme.typography.labelSmall
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
fun SearchAppBarTitle(onClickSearch: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(0.dp, 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(Modifier
            .clip(shape = RoundedCornerShape(50.dp, 50.dp, 50.dp, 50.dp))
            .clickable { /*TODO*/ }
            .padding(6.dp)
        ) {
            Text(
                text = stringResource(R.string.sort_popular_first),
                fontSize = 16.sp
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = stringResource(R.string.drop_down)
            )
        }
        IconButton(onClick = { onClickSearch() }) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search_filter)
            )
        }
    }
}

