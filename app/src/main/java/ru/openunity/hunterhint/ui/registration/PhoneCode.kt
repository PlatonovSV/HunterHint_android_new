package ru.openunity.hunterhint.ui.registration

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun PhoneCodeRoute(
    navigateUp: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    CountryCodeDialog(
        onCountryClick = { country ->
            navigateUp(country.cCode)
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