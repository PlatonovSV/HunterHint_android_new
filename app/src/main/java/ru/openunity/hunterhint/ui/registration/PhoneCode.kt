package ru.openunity.hunterhint.ui.registration

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun RegPhoneCodeRoute(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegViewModel = hiltViewModel()
) {
    CountryCodeDialog(
        onCountryClick = { country ->
            viewModel.updateCountry(country)
            navigateUp()
        },
        modifier = modifier
    )
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