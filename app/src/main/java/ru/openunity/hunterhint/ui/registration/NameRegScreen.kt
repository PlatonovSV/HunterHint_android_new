package ru.openunity.hunterhint.ui.registration

import androidx.activity.ComponentActivity
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.ui.theme.HunterHintTheme

@Composable
internal fun RegNameRoute(
    navigateToRegDate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegViewModel = hiltViewModel(LocalContext.current as ComponentActivity),
) {
    val uiState by viewModel.regUiState.collectAsState()
    NameRegScreen(regUiState = uiState,
        onUserNameChanged = { viewModel.updateUserName(it) },
        onClickNext = {
            if (viewModel.isNameCorrect()) {
                navigateToRegDate()
            }
        },
        onLastNameChanged = { viewModel.updateUserLastName(it) },
        modifier = modifier
    )
}

@Composable
fun NameRegScreen(
    regUiState: RegUiState,
    onUserNameChanged: (String) -> Unit,
    onClickNext: () -> Unit,
    onLastNameChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val userName = regUiState.userRegDto.name
    val userLastName = regUiState.userRegDto.lastName
    Column(
        modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(20.dp)
    ) {
        val focusRequester = remember { FocusRequester() }
        PageDescription(
            pageName = stringResource(R.string.create_account),
            pageDesc = stringResource(R.string.enter_your_name)
        )
        OutlinedTextField(
            textStyle = MaterialTheme.typography.bodyMedium,
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
        if (!regUiState.isNameCorrect) {
            WrongInput(textResourceId = R.string.name_incorrect)
        }
        Spacer(modifier = Modifier.height(14.dp))
        OutlinedTextField(
            textStyle = MaterialTheme.typography.bodyMedium,
            value = userLastName,
            singleLine = true,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
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
        if (!regUiState.isLastNameCorrect) {
            WrongInput(textResourceId = R.string.lastname_incorrect)
        }
        // Spacer to fill up the available space
        Spacer(modifier = Modifier.height(14.dp))
        NextButton(onClickNext = onClickNext, modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}


@Composable
fun WrongInput(
    textResourceId: Int, modifier: Modifier = Modifier
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.padding(0.dp, 6.dp)) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            modifier = Modifier.size(12.dp),
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
fun NextButton(
    onClickNext: () -> Unit, modifier: Modifier = Modifier, stringResourceId: Int = R.string.next
) {
    Button(
        onClick = onClickNext, modifier = modifier, shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = stringResource(stringResourceId), style = MaterialTheme.typography.labelLarge
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
            GenderDialogItem(
                onClick = onSelect, gender = Gender.MALE, modifier = Modifier
            )
            HorizontalDivider()
            GenderDialogItem(
                onClick = onSelect, gender = Gender.FEMALE, modifier = Modifier
            )
        }
    }
}

@Composable
fun GenderDialogItem(onClick: (Gender) -> Unit, gender: Gender, modifier: Modifier = Modifier) {
    DialogItem(
        stringResourceId = gender.stringResourceId,
        onClick = { onClick(gender) },
        modifier = modifier
    )
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
            Month.entries.forEach {
                MonthDialogItem(
                    onClick = onSelect, it, modifier = Modifier
                )
                if (it != Month.DECEMBER) {
                    HorizontalDivider()
                }
            }
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
fun MonthDialogItem(onClick: (Month) -> Unit, month: Month, modifier: Modifier = Modifier) {
    DialogItem(
        stringResourceId = month.stringResourceId, onClick = { onClick(month) }, modifier = modifier
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
fun NameRegScreenPreview() {
    HunterHintTheme {
        NameRegScreen(RegUiState(), {}, {}, {})
    }

}