package ru.openunity.hunterhint.ui.bookingInfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.models.BookingData
import ru.openunity.hunterhint.models.GroundsCard
import ru.openunity.hunterhint.navigation.HuntTopAppBar
import ru.openunity.hunterhint.ui.bottomAppBar.HuntBottomAppBar
import ru.openunity.hunterhint.ui.components.ComponentScreen
import ru.openunity.hunterhint.ui.components.ComponentSuccess
import ru.openunity.hunterhint.ui.groundsPage.HuntingOffer
import ru.openunity.hunterhint.ui.personal.UserCard
import ru.openunity.hunterhint.ui.search.GroundItem
import java.time.format.DateTimeFormatter

@Composable
internal fun BookingInfoRoute(
    navigateToCreateComment: (Long) -> Unit,
    navigateToGroundsPage: (Int) -> Unit,
    navigateUp: () -> Unit,
    bookingId: Long,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: BookingInfoViewModel = hiltViewModel()
) {
    if (bookingId < 0) {
        navigateUp()
    } else {
        viewModel.setBooking(bookingId)
        Scaffold(topBar = {
            HuntTopAppBar(
                strResId = R.string.booking_info, navigateUp = navController::navigateUp
            )
        }, bottomBar = { HuntBottomAppBar(navController = navController) }, modifier = modifier
        ) {
            BookingInfoScreen(
                viewModel = viewModel,
                navigateToGroundsPage = navigateToGroundsPage,
                navigateToCreateComment = navigateToCreateComment,
                modifier = modifier.padding(it)
            )
        }
    }
}

@Composable
fun BookingInfoScreen(
    modifier: Modifier = Modifier,
    navigateToGroundsPage: (Int) -> Unit,
    navigateToCreateComment: (Long) -> Unit,
    viewModel: BookingInfoViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        ComponentScreen(
            loadingStrResId = R.string.loading_grounds_card,
            waitContent = {},
            successContent = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BookingInfoGroundCard(
                        content = uiState.groundsCard,
                        onClick = navigateToGroundsPage,
                        changeImage = viewModel::changeImage,
                        it
                    )
                    UserCard(
                        user = UserCard(dto = uiState.groundsOwner, state = ComponentSuccess),
                        reloadUserCards = {},
                        showDetail = false
                    )
                }
            },
            retryOnErrorAction = viewModel::downloadBooking,
            state = uiState.groundsCardState
        )
        ComponentScreen(
            loadingStrResId = R.string.offers_loading,
            waitContent = {},
            successContent = {
                HuntingOffer(
                    onClick = {},
                    offer = uiState.offer,
                    it
                )
            },
            retryOnErrorAction = viewModel::downloadBooking,
            state = uiState.offersCardState
        )
        ComponentScreen(
            loadingStrResId = R.string.bookings_loading,
            waitContent = {},
            successContent = {
                BookingInfoCard(
                    content = uiState.booking,
                    navigateToCreateComment = navigateToCreateComment,
                    it
                )
            },
            retryOnErrorAction = viewModel::downloadBooking,
            state = uiState.bookingCardState
        )
    }
}

@Composable
fun BookingInfoGroundCard(
    content: GroundsCard,
    onClick: (Int) -> Unit,
    changeImage: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    GroundItem(
        changeImage = changeImage, groundCard = content, onClick = onClick, modifier = modifier
    )
}

@Composable
fun BookingInfoCard(
    content: BookingData,
    navigateToCreateComment: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Column(
            Modifier
                .padding(18.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.extraLarge
                )
                .padding(dimensionResource(id = R.dimen.padding_medium))
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.hunting_method),
                fontWeight = FontWeight.W700,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = stringResource(id = content.huntingMethod.stringRes),
                style = MaterialTheme.typography.titleMedium
            )


            Text(
                text = stringResource(id = R.string.start_date),
                fontWeight = FontWeight.W700,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = content.startDate.format(DateTimeFormatter.ofPattern("d.M.yyyy")),
                style = MaterialTheme.typography.titleMedium
            )


            Text(
                text = stringResource(id = R.string.leave_date),
                fontWeight = FontWeight.W700,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = content.finalDate.format(DateTimeFormatter.ofPattern("d.M.yyyy")),
                style = MaterialTheme.typography.titleMedium
            )


            Text(
                text = stringResource(id = R.string.bookings_creation_time),
                fontWeight = FontWeight.W700,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = content.bookingTime.format(DateTimeFormatter.ofPattern("d.M.yyyy")),
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = stringResource(id = R.string.your_request),
                fontWeight = FontWeight.W700,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = content.bookingInfo.format(DateTimeFormatter.ofPattern("d.M.yyyy")),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Button(
            onClick = { navigateToCreateComment(content.id) },
            contentPadding = PaddingValues(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            Text(
                text = stringResource(R.string.create_comment),
                modifier = Modifier,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}