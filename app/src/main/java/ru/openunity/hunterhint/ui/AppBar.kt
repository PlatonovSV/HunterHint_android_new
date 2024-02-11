package ru.openunity.hunterhint.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.openunity.hunterhint.R

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    canNavigateBack: Boolean,
    currentScreen: AppScreen,
    navigateUp: () -> Unit,
    onClickSearch: () -> Unit,
    groundsPageViewModel: GroundsPageViewModel,
    modifier: Modifier = Modifier,
) {
    val appBarColors = TopAppBarDefaults.mediumTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    )
    when (currentScreen) {
        AppScreen.Search -> {
            TopAppBar(
                title = {
                    SearchAppBarTitle(modifier = Modifier)
                },
                colors = appBarColors,
                actions = {
                    Row {
                        IconButton(onClick = { onClickSearch() }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = stringResource(R.string.search_filter)
                            )
                        }
                    }
                },
                modifier = modifier
            )
        }

        AppScreen.Detailed -> {
            TopAppBar(
                title = {
                    GroundsPageTitle()
                },
                colors = appBarColors,
                actions = {
                    Row {
                        IconButton(onClick = { onClickSearch() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ios_share),
                                contentDescription = stringResource(
                                    id = R.string.share
                                ),
                                modifier = Modifier.padding(8.dp, 6.dp)
                            )
                        }
                        //Add ground to favorite button
                        IconButton(onClick = groundsPageViewModel::addToFavorite) {
                            Icon(
                                painter = painterResource(id = R.drawable.favorite),
                                contentDescription = stringResource(
                                    id = R.string.to_favorite
                                ),
                                modifier = Modifier.padding(0.dp, 4.dp, 8.dp, 4.dp)
                            )
                        }
                    }
                },
                modifier = modifier,
                navigationIcon = {
                    if (canNavigateBack) {
                        IconButton(onClick = navigateUp) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back_button)
                            )
                        }
                    }
                }
            )
        }
        AppScreen.RegName -> {}
        AppScreen.RegDate -> {}
        AppScreen.RegEmail -> {}
    }
}

@Composable
fun AppBottomAppBar(
    currentScreen: AppScreen,
    onClickAccount: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when(currentScreen) {
        AppScreen.Search -> {
            HuntBottomAppBar(onClickAccount, modifier = modifier)
        }
        AppScreen.Detailed -> {
            HuntBottomAppBar(onClickAccount, modifier = modifier)
        }
        AppScreen.RegName -> {}
        AppScreen.RegDate -> {}
        AppScreen.RegEmail -> {}
    }
}

@Composable
fun HuntBottomAppBar(
    onClickAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        actions = {
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "Localized description",
                )
            }
            IconButton(onClick = onClickAccount) {
                Icon(
                    Icons.Filled.AccountCircle,
                    contentDescription = "Localized description",
                )
            }
        },
        modifier = modifier.height(106.dp)
    )
}
