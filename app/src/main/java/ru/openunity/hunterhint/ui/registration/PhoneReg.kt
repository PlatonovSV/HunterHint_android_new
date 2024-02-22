package ru.openunity.hunterhint.ui.registration

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
import ru.openunity.hunterhint.ui.theme.HunterHintTheme
import ru.openunity.hunterhint.ui.theme.Montserrat

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PhoneRegScreen(
    userPhone: String,
    onClickNext: () -> Unit,
    chooseCountryCode: () -> Unit,
    onClickCancel: () -> Unit,
    country: Country,
    regUiState: RegUiState,
    onPhoneChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester =
        remember { FocusRequester.Companion.FocusRequesterFactory.component1() }
    Column(
        modifier = modifier
            .padding(36.dp)
            .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
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
            text = stringResource(R.string.enter_your_phone_number),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            value = country.countryName,
            onValueChange = {},
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
                value = userPhone,
                onValueChange = onPhoneChange,
                singleLine = true,
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
                isError = regUiState.isPhoneWrong,
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            shape = MaterialTheme.shapes.medium, onClick = onClickNext,
            modifier = Modifier,
        ) {
            Text(
                text = stringResource(id = R.string.next),
                style = MaterialTheme.typography.labelLarge
            )
        }

        TextButton(onClick = onClickCancel) {
            Text(
                text = stringResource(id = R.string.already_have_account),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun CountryCodeDialog(
    onCountryClick: (Country) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier
            .fillMaxSize()
    ) {
        val countries = Country.entries
        val sortedCountries = countries.sortedBy { country -> country.countryName }
        items(sortedCountries) { country ->
            CountryCodeItem(
                country = country,
                onCountryClick = onCountryClick
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
            userPhone = "",
            onPhoneChange = {},
            onClickNext = {},
            onClickCancel = {},
            chooseCountryCode = {},
            regUiState = RegUiState(),
            country = Country.RUSSIAN_FEDERATION,
            modifier = Modifier
        )
    }
}