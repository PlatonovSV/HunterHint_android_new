package ru.openunity.hunterhint.ui.registration

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.ui.theme.HunterHintTheme


@Composable
fun NameRegScreen(
    userName: String,
    regUiState: RegUiState,
    onUserNameChanged: (String) -> Unit,
    userLastName: String,
    onClickNext: () -> Unit,
    onLastNameChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        val focusRequester = remember { FocusRequester() }
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
            text = stringResource(R.string.create_account),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.enter_your_name),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = userName,
            singleLine = true,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
            ),
            onValueChange = onUserNameChanged,
            label = {
                Text(stringResource(R.string.first_name))
            },
            isError = regUiState.isNameCorrect,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onDone = { focusRequester.requestFocus() })
        )
        Spacer(modifier = Modifier.height(14.dp))
        OutlinedTextField(
            value = userLastName,
            singleLine = true,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
            ),
            onValueChange = onLastNameChanged,
            label = {
                Text(stringResource(R.string.last_name))
            },
            isError = regUiState.isLastNameCorrect,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { onClickNext() })
        )
        if (!regUiState.isNameCorrect) {
            WrongInput(textResourceId = R.string.name_incorrect)
        } else {
            if (!regUiState.isLastNameCorrect) {
                WrongInput(textResourceId = R.string.lastname_incorrect)
            }
        }
        // Spacer to fill up the available space
        Spacer(modifier = Modifier.weight(1f))
        NextButton(onClickNext = onClickNext, modifier = Modifier.align(Alignment.End))
    }
}

@Composable
fun DateRegScreen(
    onClickNext: () -> Unit,
    showMonthDialog: Boolean,
    showGenderDialog: Boolean, modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
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
            text = stringResource(R.string.create_account),
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

        // Spacer to fill up the available space
        Spacer(modifier = Modifier.weight(1f))
        when {
            showMonthDialog -> {
                MonthDialog(onDismissRequest = { /*TODO*/ }, onSelect = {/*TODO*/ })
            }

            showGenderDialog -> {
                GenderDialog(onDismissRequest = { /*TODO*/ }, onSelect = {/*TODO*/ })
            }

            else -> {
                NextButton(onClickNext = onClickNext, modifier = Modifier.align(Alignment.End))
            }
        }

    }
}

@Composable
fun WrongInput(
    textResourceId: Int,
    modifier: Modifier = Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.padding(0.dp,6.dp)) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null, modifier = Modifier
                .size(12.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = stringResource(textResourceId),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.error,
            fontSize = 10.sp
        )
    }
}

@Composable
fun NextButton(onClickNext: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClickNext, modifier = modifier, shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = stringResource(R.string.next), style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
fun GenderDialog(
    onDismissRequest: () -> Unit, onSelect: (Gender) -> Unit, modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = modifier.background(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = MaterialTheme.shapes.large
            )
        ) {
            DialogItem(
                onClick = { onSelect(Gender.MALE) },
                stringResourceId = R.string.male,
                modifier = Modifier
            )
            HorizontalDivider()
            DialogItem(
                onClick = { onSelect(Gender.FEMALE) },
                stringResourceId = R.string.female,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun MonthDialog(
    onDismissRequest: () -> Unit, onSelect: (Month) -> Unit, modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = modifier.background(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = MaterialTheme.shapes.large
            )
        ) {
            DialogItem(
                onClick = { onSelect(Month.JANUARY) },
                stringResourceId = R.string.january,
                modifier = Modifier
            )
            HorizontalDivider()
            DialogItem(
                onClick = { onSelect(Month.FEBRUARY) },
                stringResourceId = R.string.february,
                modifier = Modifier
            )
            HorizontalDivider()
            DialogItem(
                onClick = { onSelect(Month.MARCH) },
                stringResourceId = R.string.march,
                modifier = Modifier
            )
            HorizontalDivider()
            DialogItem(
                onClick = { onSelect(Month.APRIL) },
                stringResourceId = R.string.april,
                modifier = Modifier
            )
            HorizontalDivider()
            DialogItem(
                onClick = { onSelect(Month.MAY) },
                stringResourceId = R.string.may,
                modifier = Modifier
            )
            HorizontalDivider()
            DialogItem(
                onClick = { onSelect(Month.JUNE) },
                stringResourceId = R.string.june,
                modifier = Modifier
            )
            HorizontalDivider()
            DialogItem(
                onClick = { onSelect(Month.JULY) },
                stringResourceId = R.string.july,
                modifier = Modifier
            )
            HorizontalDivider()
            DialogItem(
                onClick = { onSelect(Month.AUGUST) },
                stringResourceId = R.string.august,
                modifier = Modifier
            )
            HorizontalDivider()
            DialogItem(
                onClick = { onSelect(Month.SEPTEMBER) },
                stringResourceId = R.string.september,
                modifier = Modifier
            )
            HorizontalDivider()
            DialogItem(
                onClick = { onSelect(Month.OCTOBER) },
                stringResourceId = R.string.october,
                modifier = Modifier
            )
            HorizontalDivider()
            DialogItem(
                onClick = { onSelect(Month.NOVEMBER) },
                stringResourceId = R.string.november,
                modifier = Modifier
            )
            HorizontalDivider()
            DialogItem(
                onClick = { onSelect(Month.DECEMBER) },
                stringResourceId = R.string.december,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun HorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = DividerDefaults.Thickness,
    color: Color = MaterialTheme.colorScheme.onTertiaryContainer,
) = Canvas(
    modifier
        .fillMaxWidth()
        .height(thickness)
) {
    drawLine(
        color = color,
        strokeWidth = thickness.toPx(),
        start = Offset(0f, thickness.toPx() / 2),
        end = Offset(size.width, thickness.toPx() / 2),
    )
}

@Composable
fun DialogItem(
    stringResourceId: Int, onClick: () -> Unit, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.radio_button_unchecked),
            contentDescription = null,
            modifier = Modifier
                .padding(14.dp)
                .size(20.dp),
            tint = MaterialTheme.colorScheme.onTertiaryContainer
        )
        Text(
            text = stringResource(id = stringResourceId), style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DateRegScreenPreview() {
    HunterHintTheme {
        DateRegScreen(onClickNext = { /*TODO*/ }, showGenderDialog = false, showMonthDialog = false)
    }
}


@Preview(showBackground = true)
@Composable
fun NameRegScreenPreview() {
    HunterHintTheme {
        NameRegScreen("Сергей", RegUiState(), {}, "Платонов", {}, {})
    }

}