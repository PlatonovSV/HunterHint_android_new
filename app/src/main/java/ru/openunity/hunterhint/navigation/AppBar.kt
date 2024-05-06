package ru.openunity.hunterhint.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.ui.bottomAppBar.HuntBottomAppBar
import ru.openunity.hunterhint.ui.search.SearchAppBarTitle

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    currentScreen: AppScreen,
    navigateUp: () -> Unit,
    onClickSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val appBarColors = TopAppBarDefaults.mediumTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    )
    when (currentScreen) {
        AppScreen.Search -> {
            TopAppBar(title = {
                SearchAppBarTitle(modifier = Modifier)
            }, colors = appBarColors, actions = {
                Row {
                    IconButton(onClick = { onClickSearch() }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.search_filter)
                        )
                    }
                }
            }, modifier = modifier
            )
        }

        AppScreen.GroundsPage -> {}
        AppScreen.RegName -> {}
        AppScreen.RegDate -> {}
        AppScreen.RegEmail -> {}
        AppScreen.RegPhone -> {}
        AppScreen.RegPhoneCode -> {
            TopAppBar(title = { Text(text = stringResource(id = R.string.choose_country)) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_up)
                        )
                    }
                })
        }
        AppScreen.RegPassword -> {}
        AppScreen.RegCompletion -> {
            TopAppBar(title = { Text(text = stringResource(R.string.registration_in_progress)) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_up)
                        )
                    }
                })
        }
        AppScreen.AuthPhone -> {}
        AppScreen.AuthPhoneCode -> {
            TopAppBar(title = { Text(text = stringResource(id = R.string.choose_country)) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_up)
                        )
                    }
                })
        }
        AppScreen.AuthPassword -> {}
        AppScreen.Personal -> {}
        AppScreen.Booking -> {}
    }
}

@Composable
fun AppBottomAppBar(
    navController: NavController,
    currentScreen: AppScreen,
    modifier: Modifier = Modifier,
) {
    when (currentScreen) {
        AppScreen.Search -> {
            HuntBottomAppBar(navController, modifier = modifier)
        }

        AppScreen.GroundsPage -> {}
        AppScreen.RegName -> {}
        AppScreen.RegDate -> {}
        AppScreen.RegEmail -> {}
        AppScreen.RegPhone -> {}
        AppScreen.RegPhoneCode -> {}
        AppScreen.RegPassword -> {}
        AppScreen.RegCompletion -> {
            HuntBottomAppBar(navController, modifier = modifier)
        }
        AppScreen.AuthPhone -> {}
        AppScreen.AuthPhoneCode -> {}
        AppScreen.AuthPassword -> {}
        AppScreen.Personal -> {}
        AppScreen.Booking -> {}
    }
}