package ru.openunity.hunterhint.ui.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.navigation.AppScreen
import ru.openunity.hunterhint.ui.theme.HunterHintTheme

@Composable
internal fun RegPasswordRoute(
    popBackStack: () -> Unit,
    navigateToRegCompletion: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegViewModel = hiltViewModel()
) {
    val uiState by viewModel.regUiState.collectAsState()
    PasswordScreen(
        regUiState = uiState,
        onPasswordChanged = viewModel::updatePassword,
        onClickShowPassword = viewModel::showPassword,
        onClickNext = {
            if (viewModel.validatePassword()) {
                viewModel.requestRegistration()
                popBackStack()
                navigateToRegCompletion()
            }
        },
        modifier = modifier
    )
}

@Composable
fun PasswordScreen(
    regUiState: RegUiState,
    onPasswordChanged: (String) -> Unit,
    onClickShowPassword: (Boolean) -> Unit,
    onClickNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    val password = regUiState.userRegDto.password
    Column(
        modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(20.dp)
    ) {
        val focusRequester = remember { FocusRequester() }
        PageDescription(
            pageName = stringResource(R.string.create_password),
            pageDesc = stringResource(R.string.password_desc)
        )
        OutlinedTextField(
            textStyle = MaterialTheme.typography.bodyMedium,
            value = password,
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
            visualTransformation = if (regUiState.isPasswordShow) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            onValueChange = onPasswordChanged,
            label = {
                Text(stringResource(R.string.password))
            },
            isError = !regUiState.isPasswordStrong,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { onClickNext() })
        )
        CheckBoxAndLabel(
            label = R.string.show_password,
            selected = regUiState.isPasswordShow,
            onClick = onClickShowPassword
        )
        if (!regUiState.isPasswordStrong) {
            WrongInput(textResourceId = R.string.password_must_be)
        }
        // Spacer to fill up the available space
        Spacer(modifier = Modifier.weight(1f))
        NextButton(onClickNext = onClickNext, modifier = Modifier.align(Alignment.End))
    }
}

@Composable
fun CheckBoxAndLabel(
    label: Int,
    selected: Boolean,
    onClick: (Boolean) -> Unit,
) {
    Row(
        Modifier.padding(0.dp, 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = selected, onCheckedChange = onClick
        )
        Text(text = stringResource(id = label))
    }
}

@Preview(showBackground = true)
@Composable
fun PasswordScreenPreview() {
    HunterHintTheme {
        PasswordScreen(
            onClickShowPassword = {},
            regUiState = RegUiState(),
            onPasswordChanged = {},
            onClickNext = { },
        )
    }

}
