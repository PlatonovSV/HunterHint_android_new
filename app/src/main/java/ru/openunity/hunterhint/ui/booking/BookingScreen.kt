package ru.openunity.hunterhint.ui.booking

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.models.HuntingOffer
import ru.openunity.hunterhint.models.typeStringRes
import ru.openunity.hunterhint.ui.components.ErrorScreenE
import ru.openunity.hunterhint.ui.groundsPage.DataLoading
import ru.openunity.hunterhint.ui.groundsPage.EmptyListMessage
import ru.openunity.hunterhint.ui.personal.HorizontalMenuElement
import ru.openunity.hunterhint.ui.registration.MonthDialog
import ru.openunity.hunterhint.ui.registration.NextButton
import ru.openunity.hunterhint.ui.registration.WrongInput

@Composable
internal fun BookingRoute(
    offersId: Long,
    modifier: Modifier = Modifier, viewModel: BookingViewModel = hiltViewModel()
) {
    viewModel.getOffer(offersId)
    BookingScreen(
        viewModel, modifier
    )
}

@Composable
fun BookingScreen(
    viewModel: BookingViewModel, modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            when (val offer = uiState.offer) {
                is OfferLoading -> {
                    DataLoading(
                        messageId = R.string.uploading, modifier = Modifier
                    )
                }

                is OfferError -> {
                    ErrorScreenE(
                        retryAction = viewModel::getOffer, modifier = Modifier
                    )
                }

                is OfferSuccess -> {
                    if (offer.offer.id < 0) {
                        EmptyListMessage(
                            messageId = R.string.no_offer, modifier = Modifier
                        )
                    } else {
                        BookingMenu(
                            uiState,
                            currentSection = uiState.currentSection,
                            changeSection = viewModel::changeSection,
                            Modifier
                        )
                        BookingSections(
                            offer.offer,
                            uiState, viewModel, Modifier.fillMaxSize()
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun BookingSections(
    offer: HuntingOffer,
    uiState: BookingUiState,
    viewModel: BookingViewModel,
    modifier: Modifier = Modifier
) {
    when (uiState.currentSection) {
        BookingSections.DATE -> {
            ChoseDate(uiState = uiState, viewModel = viewModel, modifier)
        }

        BookingSections.HUNTER_RESOURCE -> {
            Text(text = stringResource(id = offer.typeStringRes))
        }
    }
}

@Composable
fun BookingMenu(
    uiState: BookingUiState,
    currentSection: BookingSections,
    changeSection: (BookingSections) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        BookingSections.entries.forEach {
            HorizontalMenuElement(
                isActive = it == currentSection,
                changeSection = { changeSection(it) },
                sectionNameId = it.sectionNameId,
                isWrong = when (it) {
                    BookingSections.DATE -> {
                        uiState.isDateWrong
                    }

                    BookingSections.HUNTER_RESOURCE -> {
                        false
                    }
                },
                modifier = Modifier
            )
        }
    }
}

@Composable
fun ChoseDate(
    uiState: BookingUiState, viewModel: BookingViewModel, modifier: Modifier = Modifier
) {
    Column(
        modifier
            .padding(18.dp)
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.start_date),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        SelectDate(
            isShowError = uiState.isShowStartError,
            userDate = uiState.startDay,
            onMonthClick = viewModel::showStartMonthDialog,
            onUserDayChanged = viewModel::updateStartDay,
            onUserYearChanged = viewModel::updateStartYear,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.leave_date),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        SelectDate(
            isShowError = uiState.isShowFinalError,
            userDate = uiState.finalDay,
            onMonthClick = viewModel::showFinalMonthDialog,
            onUserDayChanged = viewModel::updateFinalDay,
            onUserYearChanged = viewModel::updateFinalYear,
            modifier = Modifier
        )
        if (uiState.startDay.isCorrect && uiState.finalDay.isCorrect) {
            Spacer(modifier = Modifier.padding(16.dp))
            NextButton(
                onClickNext = viewModel::nextSection,
                modifier.align(Alignment.CenterHorizontally)
            )
        }
    }

    if (uiState.isStartMonthDialogShow) {
        MonthDialog(
            onDismissRequest = viewModel::dismissDialogs, onSelect = viewModel::updateStartMonth
        )
    }
    if (uiState.isFinalMonthDialogShow) {
        MonthDialog(
            onDismissRequest = viewModel::dismissDialogs, onSelect = viewModel::updateFinalMonth
        )
    }
}

@Composable
fun SelectDate(
    isShowError: Boolean,
    userDate: UserDate,
    onMonthClick: () -> Unit,
    onUserDayChanged: (String) -> Unit,
    onUserYearChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester1 = FocusRequester()
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
        val sourceMonth = remember {
            MutableInteractionSource()
        }
        OutlinedTextField(
            textStyle = MaterialTheme.typography.bodyMedium,
            value = stringResource(id = userDate.month.stringResourceId),
            singleLine = true,
            readOnly = true,
            interactionSource = sourceMonth,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
            ),
            trailingIcon = {
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
            },
            onValueChange = {},
            isError = isShowError && userDate.isMonthCorrect
        )
        if (sourceMonth.collectIsPressedAsState().value) {
            onMonthClick()
        }
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_medium)))
        OutlinedTextField(
            textStyle = MaterialTheme.typography.bodyMedium,
            value = if (userDate.day == 0) "" else userDate.day.toString(),
            singleLine = true,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
            ),
            onValueChange = onUserDayChanged,
            label = {
                Text(stringResource(R.string.day))
            },
            isError = isShowError && userDate.isDayCorrect,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { focusRequester1.requestFocus() })
        )
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_medium)))
        OutlinedTextField(
            textStyle = MaterialTheme.typography.bodyMedium,
            value = if (userDate.year == 0) "" else userDate.year.toString(),
            singleLine = true,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .focusRequester(focusRequester1)
                .weight(1f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
            ),
            onValueChange = onUserYearChanged,
            label = {
                Text(stringResource(R.string.year))
            },
            isError = isShowError && userDate.isYearCorrect,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {})
        )
    }
    if (isShowError && !userDate.isCorrect) {
        WrongInput(R.string.please_fill_a_correct_date)
    }
}