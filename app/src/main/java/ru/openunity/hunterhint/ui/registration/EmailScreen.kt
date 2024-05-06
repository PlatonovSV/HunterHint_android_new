package ru.openunity.hunterhint.ui.registration

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.ui.theme.HunterHintTheme

@Composable
internal fun RegEmailRoute(
    navigateToRegPassword: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegViewModel = hiltViewModel(),
) {
    val uiState by viewModel.regUiState.collectAsState()
    EmailScreen(
        onClickNext = {
            if (uiState.emailConfirmation.isValid()) {
                navigateToRegPassword()
            }
        },
        onUpdateEmail = viewModel::updateEmail,
        onCodeChanged = viewModel::updateEmailConfirmationCode,
        requestEmailConfirmation = viewModel::requestEmailConfirmation,
        onEmailChanged = viewModel::changeEmail,
        regUiState = uiState,
        modifier = modifier
    )

}

@Composable
fun EmailScreen(
    onClickNext: () -> Unit,
    onCodeChanged: (String) -> Unit,
    requestEmailConfirmation: () -> Unit,
    onEmailChanged: (String) -> Unit,
    onUpdateEmail: () -> Unit,
    regUiState: RegUiState,
    modifier: Modifier = Modifier
) {
    val confirmation = regUiState.emailConfirmation
    val userEmail = regUiState.userRegDto.email
    Column(
        modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(20.dp)
    ) {
        PageDescription(
            pageName = stringResource(R.string.basic_information),
            pageDesc = stringResource(R.string.enter_your_email)
        )
        OutlinedTextField(
            textStyle = MaterialTheme.typography.bodyMedium,
            value = userEmail,
            enabled = !confirmation.isRequested,
            singleLine = true,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
            ),
            onValueChange = onEmailChanged,
            label = {
                Text(stringResource(R.string.email))
            },
            isError = regUiState.isEmailCorrect,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { requestEmailConfirmation() })
        )
        VerificationIndicator(
            state = regUiState.state, isValid = !regUiState.isEmailStored,
            whenNotValid = R.string.phone_number_already_stored
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))

        Confirmation(
            confirmation,
            requestEmailConfirmation,
            onUpdateEmail,
            onClickNext,
            onCodeChanged,
            Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface)
        )
    }
}

@Composable
fun Confirmation(
    confirmation: Confirmation,
    requestEmailConfirmation: () -> Unit,
    onUpdateEmail: () -> Unit,
    onClickNext: () -> Unit,
    onCodeChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        if (!confirmation.isRequested) {
            ConfirmationButton(
                label = R.string.confirm,
                action = requestEmailConfirmation,
            )
        } else {
            ConfirmationButton(
                label = R.string.change,
                action = onUpdateEmail,
                Modifier.align(Alignment.CenterHorizontally)
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
        if (confirmation.isRequested) {
            TextField(
                textStyle = MaterialTheme.typography.bodyLarge,
                value = confirmation.userInput,
                singleLine = true,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .width(110.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                ),
                onValueChange = onCodeChanged,
                label = {
                    Text(
                        stringResource(R.string.code),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                isError = confirmation.isValid(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { onClickNext() })
            )
        }
        if (confirmation.isCompete()) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
            NextButton(onClickNext = onClickNext, Modifier)
        }
    }
}

@Composable
fun ConfirmationButton(@StringRes label: Int, action: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = action,
        modifier = modifier,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EmailScreenPreview() {
    HunterHintTheme {
        EmailScreen(
            onClickNext = { /*TODO*/ },
            requestEmailConfirmation = { /*TODO*/ },
            onEmailChanged = {},
            onCodeChanged = {},
            onUpdateEmail = {},
            regUiState = RegUiState()
        )
    }
}