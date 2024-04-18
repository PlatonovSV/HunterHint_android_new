package ru.openunity.hunterhint.ui.authorization

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.dto.country
import ru.openunity.hunterhint.ui.registration.Confirmation
import ru.openunity.hunterhint.ui.registration.Country
import ru.openunity.hunterhint.ui.registration.PageDescription
import ru.openunity.hunterhint.ui.registration.VerificationIndicator
import ru.openunity.hunterhint.ui.theme.HunterHintTheme
import ru.openunity.hunterhint.ui.theme.Montserrat

@Composable
fun PhoneAuthScreen(
    onClickNext: () -> Unit,
    chooseCountryCode: () -> Unit,
    onRegistration: () -> Unit,
    uiState: AuthUiState,
    onCodeChanged: (String) -> Unit,
    requestPhoneConfirmation: () -> Unit,
    onPhoneChanged: () -> Unit,
    onPhoneUpdate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val confirmation = uiState.phoneConfirmation
    Column(
        modifier = modifier
            .padding(36.dp)
            .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PageDescription(
            pageName = stringResource(R.string.authorization),
            pageDesc = stringResource(R.string.enter_your_phone_number)
        )
        PhoneAndCountry(
            country = uiState.authRequestDto.country,
            onPhoneUpdate = onPhoneUpdate,
            chooseCountryCode = chooseCountryCode,
            phone = uiState.authRequestDto.phoneNumber,
            isBadPhone = !uiState.isPhoneStored,
            isEnabled = !confirmation.isRequested,
            modifier = Modifier
        )
        VerificationIndicator(
            state = uiState.state, isValid = uiState.isPhoneStored,
            whenNotValid = R.string.account_not_yet_been_created
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))

        Confirmation(
            confirmation,
            requestPhoneConfirmation,
            onPhoneChanged,
            onClickNext,
            onCodeChanged,
            Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface)
        )

        TextButton(onClick = onRegistration) {
            Text(
                text = stringResource(id = R.string.create_new_account),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun PhoneAndCountry(
    country: Country,
    onPhoneUpdate: (String) -> Unit,
    chooseCountryCode: () -> Unit,
    phone: String, isBadPhone: Boolean, isEnabled: Boolean, modifier: Modifier
) {
    val focusRequester =
        remember { FocusRequester() }
    Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(
            value = country.countryName,
            onValueChange = {},
            enabled = isEnabled,
            readOnly = true,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center),
            interactionSource = remember { MutableInteractionSource() }
                .also { interactionSource ->
                    LaunchedEffect(interactionSource) {
                        interactionSource.interactions.collect {
                            if (it is PressInteraction.Release) {
                                chooseCountryCode()
                            }
                        }
                    }
                },

            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            OutlinedTextField(
                value = "+${country.internationalCountryCode}",
                onValueChange = {},
                enabled = isEnabled,
                shape = RoundedCornerShape(
                    topStart = 20.dp, topEnd = 0.dp, bottomEnd = 0.dp, bottomStart = 20.dp
                ),
                interactionSource = remember { MutableInteractionSource() }
                    .also { interactionSource ->
                        LaunchedEffect(interactionSource) {
                            interactionSource.interactions.collect {
                                if (it is PressInteraction.Release) {
                                    chooseCountryCode()
                                }
                            }
                        }
                    },
                readOnly = true,
                singleLine = true,
                textStyle = TextStyle(
                    textAlign = TextAlign.Right,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                ),
                modifier = Modifier
                    .size(90.dp, 56.dp)
            )
            OutlinedTextField(
                value = phone,
                onValueChange = onPhoneUpdate,
                singleLine = true,
                enabled = isEnabled,
                shape = RoundedCornerShape(
                    topStart = 0.dp, topEnd = 20.dp, bottomEnd = 20.dp, bottomStart = 0.dp
                ),
                textStyle = TextStyle(
                    textAlign = TextAlign.Left,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                isError = isBadPhone,
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPhoneRegScreen() {
    HunterHintTheme {

    }
}

