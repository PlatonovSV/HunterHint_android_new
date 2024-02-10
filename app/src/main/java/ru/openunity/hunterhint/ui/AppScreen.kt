package ru.openunity.hunterhint.ui

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.ui.registration.DateRegScreen
import ru.openunity.hunterhint.ui.registration.NameRegScreen
import ru.openunity.hunterhint.ui.registration.RegViewModel

/**
 * enum values that represent the screens in the app
 */
enum class AppScreen {
    Search,
    Detailed,
    RegName,
    RegDate
}

enum class TestTag {
    GroundInfo
}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun HunterHintApp(
    searchViewModel: SearchViewModel = viewModel(factory = SearchViewModel.Factory),
    groundsPageViewModel: GroundsPageViewModel = viewModel(factory = GroundsPageViewModel.Factory),
    regViewModel: RegViewModel = viewModel(factory = RegViewModel.Factory),
    navController: NavHostController = rememberNavController()
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    var screenTitle by remember { mutableStateOf("HunterHint") }

    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route ?: AppScreen.Search.name
    )


    Scaffold(
        topBar = {
            val context = LocalContext.current
            TopAppBar(
                canNavigateBack = navController.previousBackStackEntry != null,
                currentScreen = currentScreen,
                navigateUp = { navController.navigateUp() },
                onClickSearch = {
                    shareOrder(
                        context = context,
                        subject = context.resources.getString(R.string.app_name),
                        summary = groundsPageViewModel.groundsPageUiState.value.ground.name
                    )
                },
                groundsPageViewModel = groundsPageViewModel,
                modifier = Modifier
            )
        },
        bottomBar = {
            AppBottomAppBar(
                currentScreen = currentScreen,
                onClickAccount = { navController.navigate(AppScreen.RegName.name) })
        }
    ) { innerPadding ->
        val searchUiState by searchViewModel.searchUiState.collectAsState()
        val groundsPageUiState by groundsPageViewModel.groundsPageUiState.collectAsState()
        val regUiState by regViewModel.regUiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = AppScreen.Search.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = AppScreen.Search.name) {
                SearchScreen(
                    retryAction = searchViewModel::getGrounds,
                    searchUiState = searchUiState,
                    changeImage = { groundId: Int, isIncrement: Boolean ->
                        searchViewModel.changeImage(groundId, isIncrement)
                    },
                    onGroundsCardClick = { groundId: Int ->
                        run {
                            groundsPageViewModel.onGroundsCardClick(groundId)
                            navController.navigate(AppScreen.Detailed.name)
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            composable(route = AppScreen.Detailed.name) {
                screenTitle = groundsPageUiState.ground.name
                val scrollState = rememberScrollState()
                GroundsPage(
                    changeImage = { isIncrement: Boolean ->
                        groundsPageViewModel.changeImage(
                            isIncrement
                        )
                    },
                    groundsPageUiState = groundsPageUiState,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(state = scrollState)
                )
            }
            composable(route = AppScreen.RegName.name) {
                NameRegScreen(
                    userName = regViewModel.userName,
                    regUiState = regUiState,
                    onUserNameChanged = { regViewModel.updateUserName(it) },
                    userLastName = regViewModel.userLastName,
                    onClickNext = {
                        if (regViewModel.isNameCorrect()) {
                            navController.navigate(AppScreen.RegDate.name)
                        }
                    },
                    onLastNameChanged = { regViewModel.updateUserLastName(it) },
                    modifier = Modifier
                )
            }
            composable(route = AppScreen.RegDate.name) {
                DateRegScreen(
                    onClickNext = {
                        regViewModel.checkGender()
                        regViewModel.checkBirthday()
                    },
                    onMonthClick = regViewModel::showMonthDialog,
                    onGenderClick = regViewModel::showGenderDialog,
                    onUserDayChanged = regViewModel::updateUserDay,
                    onUserYearChanged = regViewModel::updateUserYear,
                    userMonth = regViewModel.userMonth,
                    regUiState = regUiState,
                    onMonthSelect = regViewModel::updateUserMonth,
                    onGenderSelect = regViewModel::updateUserGender,
                    onDismissRequest = regViewModel::dismissDialogs,
                    userDay = regViewModel.userDay,
                    userYear = regViewModel.userYear,
                    userGender = regViewModel.userGender,
                    modifier = Modifier
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