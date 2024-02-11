package ru.openunity.hunterhint.ui.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.ui.theme.HunterHintTheme

@Composable
fun EmailScreen(
    onClickNext: () -> Unit,
    userEmail: String,
    confirmationCode: String,
    onCodeChanged: (String) -> Unit,
    requestEmailConfirmation: () -> Unit,
    onEmailChanged: (String) -> Unit,
    regUiState: RegUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
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
            text = stringResource(R.string.basic_information),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.enter_your_email),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            textStyle = MaterialTheme.typography.bodyMedium,
            value = userEmail,
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
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
        Button(
            onClick = requestEmailConfirmation,
            modifier = modifier.align(Alignment.CenterHorizontally),
            shape = MaterialTheme.shapes.small
        ) {
            Text(
                text = stringResource(R.string.confirm),
                style = MaterialTheme.typography.labelMedium
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
        if (regUiState.isConfirmationRequested) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                TextField(
                    textStyle = MaterialTheme.typography.bodyLarge,
                    value = confirmationCode,
                    singleLine = true,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.width(110.dp).align(Alignment.CenterVertically),
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
                    isError = regUiState.isCodeCorrect,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { onClickNext() })
                )
            }
        }

        // Spacer to fill up the available space
        Spacer(modifier = Modifier.weight(1f))
        NextButton(onClickNext = onClickNext, modifier = Modifier.align(Alignment.End))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EmailScreenPreview() {
    HunterHintTheme {
        EmailScreen(
            onClickNext = { /*TODO*/ },
            userEmail = "",
            requestEmailConfirmation = { /*TODO*/ },
            onEmailChanged = {},
            confirmationCode = "1234567",
            onCodeChanged = {},
            regUiState = RegUiState(isConfirmationRequested = true)
        )
    }
}