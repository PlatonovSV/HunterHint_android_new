package ru.openunity.hunterhint.ui

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.openunity.hunterhint.R

/**
 * enum values that represent the screens in the app
 */
enum class HunterHintScreen {
    Search,
    Detailed,
}

enum class TestTag {
    GroundInfo
}



/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    canNavigateBack: Boolean,
    currentScreen: HunterHintScreen,
    navigateUp: () -> Unit,
    onClickSearch: () -> Unit,
    modifier: Modifier = Modifier,
    onShareButtonClicked: () -> Unit = {},
) {
    TopAppBar(
        title = {
            if (currentScreen == HunterHintScreen.Search) {
                SearchAppBarTitle(onClickSearch = onClickSearch, modifier = Modifier)
            } else {
                Row(
                    modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = title)
                    IconButton(
                        onClick = onShareButtonClicked,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = stringResource(R.string.share),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true,
    showSystemUi = true)
@Composable
fun HunterHintApp(
    searchViewModel: SearchViewModel = viewModel(factory = SearchViewModel.Factory),
    groundsPageViewModel: GroundsPageViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    var screenTitle = stringResource(R.string.app_name)
    val currentScreen = HunterHintScreen.valueOf(
        backStackEntry?.destination?.route ?: HunterHintScreen.Search.name
    )


    Scaffold(
        topBar = {
            val context = LocalContext.current
            AppBar(
                title = screenTitle,
                onShareButtonClicked = {
                    shareOrder(
                        context = context,
                        subject = context.resources.getString(R.string.app_name),
                        summary = groundsPageViewModel.groundsUiState.value.ground.name
                    )
                },
                onClickSearch = {
                    shareOrder(
                        context = context,
                        subject = context.resources.getString(R.string.app_name),
                        summary = groundsPageViewModel.groundsUiState.value.ground.name
                    )
                },
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        val searchUiState by searchViewModel.searchUiState.collectAsState()
        val groundsPageUiState by groundsPageViewModel.groundsUiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = HunterHintScreen.Search.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = HunterHintScreen.Search.name) {
                SearchScreen(
                    retryAction = searchViewModel::getGrounds,
                    searchUiState = searchUiState,
                    changeImage = { groundId: Int, isIncrement: Boolean ->
                        searchViewModel.changeImage(groundId, isIncrement)
                    },
                    onGroundsCardClick = { groundId: Int ->
                        run {
                            groundsPageViewModel.onGroundsCardClick(groundId)
                            navController.navigate(HunterHintScreen.Detailed.name)
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp, dimensionResource(R.dimen.padding_medium))
                )
            }
            composable(route = HunterHintScreen.Detailed.name) {
                screenTitle = groundsPageUiState.ground.name
                val scrollState = rememberScrollState()
                GroundsPage(
                    changeImage = { isIncrement: Boolean -> groundsPageViewModel.changeImage(isIncrement) },
                    groundsUiState = groundsPageUiState,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(state = scrollState)
                )
            }
        }
    }
}


private fun shareOrder(context: Context, subject: String, summary: String) {
    // Create an ACTION_SEND implicit intent with order details in the intent extras
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, summary)
    }
    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.app_name)
        )
    )
}