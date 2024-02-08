package ru.openunity.hunterhint.ui.registration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.ui.theme.HunterHintTheme


@Composable
fun NameRegScreen(
    userName: String,
    isNameWrong: Boolean,
    onKeyboardNext: () -> Unit,
    onUserNameChanged: (String) -> Unit,
    userLastName: String,
    isLastNameWrong: Boolean,
    onKeyboardDone: () -> Unit,
    onClickNext: () -> Unit,
    onLastNameChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_medium))
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
            text = stringResource(R.string.enter_your_name),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = userName,
            singleLine = true,
            shape = MaterialTheme.shapes.large,
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
            isError = isNameWrong,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onDone = { onKeyboardNext() }
            )
        )
        Spacer(modifier = Modifier.height(14.dp))
        OutlinedTextField(
            value = userLastName,
            singleLine = true,
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
            ),
            onValueChange = onLastNameChanged,
            label = {
                Text(stringResource(R.string.last_name))
            },
            isError = isLastNameWrong,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onKeyboardDone() }
            )
        )
        // Spacer to fill up the available space
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onClickNext,
            modifier = Modifier.align(Alignment.End),
            shape = MaterialTheme.shapes.small
        ) {
            Text(
                text = stringResource(R.string.next),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NameRegScreenPreview() {
    HunterHintTheme {
        NameRegScreen(
            "Сергей",
            false,
            {},
            {},
            "Платонов",
            false,
            {},
            {},
            {}
        )
    }

}