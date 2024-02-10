package ru.openunity.hunterhint.ui.registration

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
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.ui.theme.HunterHintTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DateRegScreen(
    onClickNext: () -> Unit,
    onMonthClick: () -> Unit,
    onMonthSelect: (Month) -> Unit,
    onGenderClick: () -> Unit,
    onGenderSelect: (Gender) -> Unit,
    onDismissRequest: () -> Unit,
    userDay: String,
    userYear: String,
    userMonth: Int,
    userGender: Int,
    onUserDayChanged: (String) -> Unit,
    onUserYearChanged: (String) -> Unit,
    regUiState: RegUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(20.dp)
    ) {
        val focusRequester1 =
            remember { FocusRequester.Companion.FocusRequesterFactory.component1() }
        Spacer(modifier = Modifier.height(36.dp))
        Icon(
            painter = painterResource(id = R.drawable.account_circle),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(36.dp),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.basic_information),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.enter_your_birthday_and_gender),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
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
                isError = regUiState.isDataCorrect
            )
            if (sourceMonth.collectIsPressedAsState().value) {
                onMonthClick()
            }
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_medium)))
            OutlinedTextField(
                textStyle = MaterialTheme.typography.bodyMedium,
                value = userDay,
                singleLine = true,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                ),
                onValueChange = onUserDayChanged,
                label = {
                    Text(stringResource(R.string.day))
                },
                isError = regUiState.isDataCorrect,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusRequester1.requestFocus() })
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_medium)))
            OutlinedTextField(
                textStyle = MaterialTheme.typography.bodyMedium,
                value = userYear,
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
                isError = regUiState.isDataCorrect,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = {})
            )
        }
        when {
            !regUiState.isBirthdayComplete -> WrongInput(R.string.please_fill_a_complete_birthday)
            !regUiState.isBirthdayCorrect -> WrongInput(R.string.please_fill_a_correct_birthday)
        }

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
            modifier = Modifier
                .fillMaxWidth(),
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
        Spacer(modifier = Modifier.weight(1f))
        when {
            regUiState.isMonthDialogShow -> {
                MonthDialog(onDismissRequest = onDismissRequest, onSelect = onMonthSelect)
            }

            regUiState.isGenderDialogShow -> {
                GenderDialog(onDismissRequest = onDismissRequest, onSelect = onGenderSelect)
            }

            else -> {
                NextButton(onClickNext = onClickNext, modifier = Modifier.align(Alignment.End))
            }
        }

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
            userDay = "23",
            userYear = "",
            userGender = Gender.MALE.stringResourceId,
            modifier = Modifier
        )
    }
}