package ru.openunity.hunterhint.ui.registration

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.ui.theme.HunterHintTheme

@Composable
internal fun RegDateRoute(
    navigateToRegEmail: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegViewModel = hiltViewModel(LocalContext.current as ComponentActivity)
) {
    val uiState by viewModel.regUiState.collectAsState()
    DateRegScreen(
        onClickNext = {
            if (viewModel.checkBirthday() && viewModel.checkGender()) {
                navigateToRegEmail()
            }
        },
        onMonthClick = viewModel::showMonthDialog,
        onGenderClick = viewModel::showGenderDialog,
        onUserDayChanged = viewModel::updateUserDay,
        onUserYearChanged = viewModel::updateUserYear,
        userMonth = viewModel.userMonth,
        regUiState = uiState,
        onMonthSelect = viewModel::updateUserMonth,
        onGenderSelect = viewModel::updateUserGender,
        onDismissRequest = viewModel::dismissDialogs,
        userGender = viewModel.userGender,
        modifier = modifier
    )
}

@Composable
fun DateRegScreen(
    onClickNext: () -> Unit,
    onMonthSelect: (Month) -> Unit,
    onGenderClick: () -> Unit,
    onGenderSelect: (Gender) -> Unit,
    onDismissRequest: () -> Unit,
    userGender: Int,
    onMonthClick: () -> Unit,
    userMonth: Int,
    onUserDayChanged: (String) -> Unit,
    onUserYearChanged: (String) -> Unit,
    regUiState: RegUiState,
    modifier: Modifier = Modifier
) {
    val userDay = regUiState.userRegDto.birthDay
    val userYear = regUiState.userRegDto.birthYear
    Column(
        modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(20.dp)
    ) {

        PageDescription(
            pageName = stringResource(R.string.basic_information),
            pageDesc = stringResource(R.string.enter_your_birthday_and_gender)
        )
        SelectDate(
            isDataCorrect = regUiState.isDataCorrect,
            isBirthdayComplete = regUiState.isBirthdayComplete,
            isBirthdayCorrect = regUiState.isBirthdayCorrect,
            userDay = userDay,
            userYear = userYear,
            onMonthClick = onMonthClick,
            userMonth = userMonth,
            onUserDayChanged = onUserDayChanged,
            onUserYearChanged = onUserYearChanged,
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))

        val sourceGender = remember {
            MutableInteractionSource()
        }
        OutlinedTextField(
            textStyle = MaterialTheme.typography.bodyMedium,
            value = stringResource(id = userGender),
            singleLine = true,
            readOnly = true,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
            ),
            trailingIcon = {
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
            },
            interactionSource = sourceGender,
            onValueChange = { },
            isError = regUiState.isDataCorrect,
        )
        if (!regUiState.isGenderSpecified) {
            WrongInput(textResourceId = R.string.please_select_your_gender)
        }
        if (sourceGender.collectIsPressedAsState().value) {
            onGenderClick()
        }

        // Spacer to fill up the available space
        when {
            regUiState.isMonthDialogShow -> {
                MonthDialog(onDismissRequest = onDismissRequest, onSelect = onMonthSelect)
            }

            regUiState.isGenderDialogShow -> {
                GenderDialog(onDismissRequest = onDismissRequest, onSelect = onGenderSelect)
            }

            else -> {
                Spacer(modifier = Modifier.height(14.dp))
                NextButton(onClickNext = onClickNext, modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }

    }
}

@Composable
fun SelectDate(
    isDataCorrect: Boolean,
    isBirthdayComplete: Boolean,
    isBirthdayCorrect: Boolean,
    userDay: Int,
    userYear: Int,
    onMonthClick: () -> Unit,
    userMonth: Int,
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
            value = stringResource(id = userMonth),
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
            isError = isDataCorrect
        )
        if (sourceMonth.collectIsPressedAsState().value) {
            onMonthClick()
        }
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_medium)))
        OutlinedTextField(
            textStyle = MaterialTheme.typography.bodyMedium,
            value = if (userDay == 0) "" else userDay.toString(),
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
            isError = isDataCorrect,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { focusRequester1.requestFocus() })
        )
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_medium)))
        OutlinedTextField(
            textStyle = MaterialTheme.typography.bodyMedium,
            value = if (userYear == 0) "" else userYear.toString(),
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
            isError = isDataCorrect,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {})
        )
    }
    when {
        !isBirthdayComplete -> WrongInput(R.string.please_fill_a_complete_birthday)
        !isBirthdayCorrect -> WrongInput(R.string.please_fill_a_correct_birthday)
    }
}

@Preview(showBackground = true)
@Composable
fun DateRegScreenPreview() {
    HunterHintTheme {
        DateRegScreen(
            onClickNext = { },
            onMonthClick = {},
            onGenderClick = {},
            onUserDayChanged = {},
            onUserYearChanged = {},
            userMonth = Month.SEPTEMBER.stringResourceId,
            regUiState = RegUiState(),
            onDismissRequest = {},
            onGenderSelect = { _ -> },
            onMonthSelect = { _ -> },
            userGender = Gender.MALE.stringResourceId,
            modifier = Modifier
        )
    }
}