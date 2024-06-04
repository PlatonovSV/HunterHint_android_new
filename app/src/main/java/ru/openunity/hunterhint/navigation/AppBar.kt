package ru.openunity.hunterhint.navigation

import androidx.annotation.StringRes
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
import androidx.compose.ui.res.painterResource
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

        AppScreen.RegPhoneCode -> {
            TopAppBar(title = { Text(text = stringResource(id = R.string.choose_country)) },
                navigationIcon = {
                    NavigateUpButton(navigateUp)
                })
        }

        AppScreen.RegCompletion -> {
            TopAppBar(title = { Text(text = stringResource(R.string.registration_in_progress)) },
                navigationIcon = {
                    NavigateUpButton(navigateUp)
                })
        }

        AppScreen.AuthPhoneCode -> {
            TopAppBar(title = { Text(text = stringResource(id = R.string.choose_country)) },
                navigationIcon = {
                    NavigateUpButton(navigateUp)
                })
        }

        else -> {

        }
    }
}

@Composable
fun NavigateUpButton(
    onClick: () -> Unit, modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.navigate_up)
        )
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

        AppScreen.RegCompletion -> {
            HuntBottomAppBar(navController, modifier = modifier)
        }

        else -> {

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HuntTopAppBar(@StringRes strResId: Int,
                  navigateUp: () -> Unit,
                  modifier: Modifier = Modifier
) {
    TopAppBar(title = { HuntTopAppBarTitle(strResId = strResId) },
        navigationIcon = { NavigateUpButton(onClick = navigateUp)},
        modifier = modifier)
}

@Composable
fun HuntTopAppBarTitle(
    @StringRes strResId: Int,
    modifier: Modifier = Modifier) {
    Text(text = stringResource(id = strResId), modifier = modifier)
}

