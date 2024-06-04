package ru.openunity.hunterhint.ui.authorization

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.ui.Loading
import ru.openunity.hunterhint.ui.components.DataLoading
import ru.openunity.hunterhint.ui.registration.CheckBoxAndLabel
import ru.openunity.hunterhint.ui.registration.NextButton
import ru.openunity.hunterhint.ui.registration.PageDescription


@Composable
fun PasswordAuthScreen(
    uiState: AuthUiState,
    onPasswordChanged: (String) -> Unit,
    onClickShowPassword: (Boolean) -> Unit,
    onAuth: () -> Unit,
    modifier: Modifier = Modifier
) {
    val password = uiState.authRequestDto.password
    Column(
        modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(20.dp)
    ) {
        val focusRequester = remember { FocusRequester() }
        PageDescription(
            pageName = stringResource(R.string.authorization),
            pageDesc = stringResource(R.string.enter_password)
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
            isError = !uiState.isPasswordGood,
            visualTransformation = if (uiState.isPasswordShow) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            onValueChange = onPasswordChanged,
            label = {
                Text(stringResource(R.string.password))
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { onAuth() })
        )
        CheckBoxAndLabel(
            label = R.string.show_password,
            selected = uiState.isPasswordShow,
            onClick = onClickShowPassword
        )
        val state = uiState.state
        if (state is Loading) {
            DataLoading(
                messageId = R.string.authorization,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )
        }

        NextButton(
            onClickNext = onAuth,
            stringResourceId = R.string.auth,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}
