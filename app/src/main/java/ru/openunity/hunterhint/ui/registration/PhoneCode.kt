package ru.openunity.hunterhint.ui.registration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.openunity.hunterhint.R

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
    var countryName by remember { mutableStateOf("") }
    LazyColumn(
        modifier
            .fillMaxSize()
    ) {
        val countries =
            Country.entries.filter { it.countryName.contains(countryName, ignoreCase = true) }
        val sortedCountries = countries.sortedBy { country -> country.countryName }

        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = countryName,
                    onValueChange = { countryName = it },
                    label = { Text(stringResource(R.string.country_name)) },
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(18.dp))
            }

        }
        items(sortedCountries) { country ->
            CountryCodeItem(
                country = country,
                onCountryClick = onCountryClick
            )
        }
    }
}