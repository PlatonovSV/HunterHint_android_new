package ru.openunity.hunterhint.ui.registration

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.dto.country
import ru.openunity.hunterhint.dto.getCountryByCode
import ru.openunity.hunterhint.ui.Loading
import ru.openunity.hunterhint.ui.State
import ru.openunity.hunterhint.ui.Success
import ru.openunity.hunterhint.ui.authorization.PhoneAndCountry
import ru.openunity.hunterhint.ui.theme.HunterHintTheme

@Composable
internal fun RegPhoneRoute(
    countryId: Int,
    navigateToRegName: () -> Unit,
    navigateToRegPhoneCode: () -> Unit,
    navigateToAuth: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegViewModel = hiltViewModel(LocalContext.current as ComponentActivity)
) {
    viewModel.updateCountry(getCountryByCode(countryId))
    val uiState by viewModel.regUiState.collectAsState()
    PhoneRegScreen(
        onClickNext = {
            if (uiState.phoneConfirmation.isValid()) {
                navigateToRegName()
            }
        },
        onCodeChanged = viewModel::updatePhoneConfirmationCode,
        requestPhoneConfirmation = viewModel::requestPhoneConfirmation,
        onPhoneChanged = viewModel::changePhone,
        chooseCountryCode = { navigateToRegPhoneCode() },
        onAuth = {
            navigateToAuth()
        },
        regUiState = uiState,
        onPhoneUpdate = viewModel::updatePhone,
        modifier = modifier
    )
}



@Composable
fun PhoneRegScreen(
    onClickNext: () -> Unit,
    chooseCountryCode: () -> Unit,
    onAuth: () -> Unit,
    regUiState: RegUiState,
    onCodeChanged: (String) -> Unit,
    requestPhoneConfirmation: () -> Unit,
    onPhoneChanged: () -> Unit,
    onPhoneUpdate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val confirmation = regUiState.phoneConfirmation
    Column(
        modifier = modifier
            .padding(36.dp)
            .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PageDescription(
            pageName = stringResource(R.string.create_account),
            pageDesc = stringResource(R.string.enter_your_phone_number)
        )
        PhoneAndCountry(
            country = regUiState.userRegDto.country,
            onPhoneUpdate = onPhoneUpdate,
            chooseCountryCode = chooseCountryCode,
            phone = regUiState.userRegDto.phoneNumber,
            isBadPhone = regUiState.isPhoneWrong,
            isEnabled = !confirmation.isRequested,
            modifier = Modifier
        )
        VerificationIndicator(
            state = regUiState.state, isValid = !regUiState.isPhoneStored,
            whenNotValid = R.string.phone_number_already_stored
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

        TextButton(onClick = onAuth) {
            Text(
                text = stringResource(id = R.string.already_have_account),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun CountryCodeItem(
    onCountryClick: (Country) -> Unit,
    country: Country,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable { onCountryClick(country) }
            .padding(horizontal = 32.dp, vertical = 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = country.countryName, style = MaterialTheme.typography.labelLarge)
        Text(
            text = "+${country.internationalCountryCode}",
            color = Color.Blue,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun PageDescription(pageName: String, pageDesc: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
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
            text = pageName,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = pageDesc,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun VerificationIndicator(
    state: State,
    isValid: Boolean,
    whenNotValid: Int,
    modifier: Modifier = Modifier
) {
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        when (state) {
            is Loading -> {
                Image(
                    modifier = modifier.size(150.dp),
                    painter = painterResource(R.drawable.loading_img),
                    contentDescription = stringResource(state.message)
                )
            }

            is Success -> {
                if (!isValid) {
                    WrongInput(textResourceId = whenNotValid)
                }
            }

            else -> {
                WrongInput(textResourceId = state.message)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewCountryCodeDialog(modifier: Modifier = Modifier) {
    HunterHintTheme {
        CountryCodeDialog(
            onCountryClick = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPhoneRegScreen() {
    HunterHintTheme {
        PhoneRegScreen(
            onPhoneUpdate = {},
            onClickNext = {},
            onCodeChanged = {},
            onPhoneChanged = {},
            requestPhoneConfirmation = {},
            onAuth = {},
            chooseCountryCode = {},
            regUiState = RegUiState(),
            modifier = Modifier
        )
    }
}