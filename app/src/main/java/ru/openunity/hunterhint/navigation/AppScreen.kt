package ru.openunity.hunterhint.navigation

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.ui.authorization.AuthViewModel
import ru.openunity.hunterhint.ui.authorization.PasswordAuthScreen
import ru.openunity.hunterhint.ui.authorization.PhoneAuthScreen
import ru.openunity.hunterhint.ui.groundsPage.GroundsPage
import ru.openunity.hunterhint.ui.groundsPage.GroundsPageViewModel
import ru.openunity.hunterhint.ui.personal.PersonalAccountScreen
import ru.openunity.hunterhint.ui.personal.PersonalViewModel
import ru.openunity.hunterhint.ui.registration.CompletionRegScreen
import ru.openunity.hunterhint.ui.registration.CountryCodeDialog
import ru.openunity.hunterhint.ui.registration.DateRegScreen
import ru.openunity.hunterhint.ui.registration.EmailScreen
import ru.openunity.hunterhint.ui.registration.NameRegScreen
import ru.openunity.hunterhint.ui.registration.PasswordScreen
import ru.openunity.hunterhint.ui.registration.PhoneRegScreen
import ru.openunity.hunterhint.ui.registration.RegViewModel
import ru.openunity.hunterhint.ui.search.SearchScreen
import ru.openunity.hunterhint.ui.search.SearchViewModel


enum class TestTag {
    GroundInfo
}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun HunterHintApp(
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
                        summary = context.resources.getString(R.string.app_name)
                    )
                },
                modifier = Modifier
            )
        },
        bottomBar = {
            AppBottomAppBar(
                currentScreen = currentScreen,
                onClickAccount = {
                    navController.navigate(AppScreen.RegPhone.name)
                    ///
                })
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreen.Search.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = AppScreen.Search.name) {
                val searchViewModel: SearchViewModel = hiltViewModel<SearchViewModel>()
                val searchUiState by searchViewModel.searchUiState.collectAsState()
                SearchScreen(
                    retryAction = searchViewModel::getGrounds,
                    searchUiState = searchUiState,
                    changeImage = { groundId: Int, isIncrement: Boolean ->
                        searchViewModel.changeImage(groundId, isIncrement)
                    },
                    onGroundsCardClick = { groundId: Int ->
                        run {
                            navController.navigate(AppScreen.Detailed.name)
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            composable(route = AppScreen.Detailed.name) {
                val groundsPageViewModel: GroundsPageViewModel = hiltViewModel()
                val groundsPageUiState by groundsPageViewModel.groundsPageUiState.collectAsState()
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
            composable(route = AppScreen.RegPhone.name) {
                val regViewModel: RegViewModel = hiltViewModel()
                val regUiState by regViewModel.regUiState.collectAsState()
                PhoneRegScreen(
                    onClickNext = {
                        if (regUiState.phoneConfirmation.isValid()) {
                            navController.navigate(AppScreen.RegName.name)
                        }
                    },
                    onCodeChanged = regViewModel::updatePhoneConfirmationCode,
                    requestPhoneConfirmation = regViewModel::requestPhoneConfirmation,
                    onPhoneChanged = regViewModel::changePhone,
                    chooseCountryCode = { navController.navigate(AppScreen.RegPhoneCode.name) },
                    onAuth = {
                        navController.popBackStack()
                        navController.navigate(AppScreen.AuthPhone.name) },
                    regUiState = regUiState,
                    onPhoneUpdate = regViewModel::updatePhone
                )
            }
            composable(route = AppScreen.RegPhoneCode.name) {
                val regViewModel: RegViewModel = hiltViewModel()
                val regUiState by regViewModel.regUiState.collectAsState()
                CountryCodeDialog(onCountryClick = { country ->
                    regViewModel.updateCountry(country)
                    navController.navigateUp()
                })
            }
            composable(route = AppScreen.RegName.name) {
                val regViewModel: RegViewModel = hiltViewModel()
                val regUiState by regViewModel.regUiState.collectAsState()
                NameRegScreen(
                    regUiState = regUiState,
                    onUserNameChanged = { regViewModel.updateUserName(it) },
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
                val regViewModel: RegViewModel = hiltViewModel()
                val regUiState by regViewModel.regUiState.collectAsState()
                DateRegScreen(
                    onClickNext = {
                        if (regViewModel.checkBirthday() && regViewModel.checkGender()) {
                            navController.navigate(AppScreen.RegEmail.name)
                        }
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
                    userGender = regViewModel.userGender,
                    modifier = Modifier
                )
            }
            composable(route = AppScreen.RegEmail.name) {
                val regViewModel: RegViewModel = hiltViewModel()
                val regUiState by regViewModel.regUiState.collectAsState()
                EmailScreen(
                    onClickNext = {
                        if (regUiState.emailConfirmation.isValid()) {
                            navController.navigate(AppScreen.RegPassword.name)
                        }
                    },
                    onUpdateEmail = regViewModel::updateEmail,
                    onCodeChanged = regViewModel::updateEmailConfirmationCode,
                    requestEmailConfirmation = regViewModel::requestEmailConfirmation,
                    onEmailChanged = regViewModel::changeEmail,
                    regUiState = regUiState
                )
            }
            composable(route = AppScreen.RegPassword.name) {
                val regViewModel: RegViewModel = hiltViewModel()
                val regUiState by regViewModel.regUiState.collectAsState()
                PasswordScreen(
                    regUiState = regUiState,
                    onPasswordChanged = regViewModel::updatePassword,
                    onClickShowPassword = regViewModel::showPassword,
                    onClickNext = {
                        if (regViewModel.validatePassword()) {
                            regViewModel.requestRegistration()
                            navController.popBackStack(AppScreen.Search.name, false)
                            navController.navigate(AppScreen.RegCompletion.name)
                        }
                    })
            }
            composable(route = AppScreen.RegCompletion.name) {
                val regViewModel: RegViewModel = hiltViewModel()
                val regUiState by regViewModel.regUiState.collectAsState()
                CompletionRegScreen(
                    state = regUiState.state,
                    retryAction = regViewModel::requestRegistration,
                    cancelAction = {
                        regViewModel.onRegCompete()
                        navController.navigate(AppScreen.Search.name)
                    },
                    onComplete = {
                        regViewModel.onRegCompete()
                        navController.navigate(AppScreen.Search.name)
                    }
                )
            }
            composable(route = AppScreen.AuthPhone.name) {
                val authViewModel: AuthViewModel = hiltViewModel()
                val authUiState by authViewModel.authUiState.collectAsState()
                PhoneAuthScreen(
                    onClickNext = {
                        if (authViewModel.checkConfirmation()) {
                            navController.navigate(AppScreen.AuthPassword.name)
                        }
                    },
                    chooseCountryCode = { navController.navigate(AppScreen.AuthPhoneCode.name) },
                    onRegistration = {
                        navController.popBackStack()
                        navController.navigate(AppScreen.RegPhone.name) },
                    uiState = authUiState,
                    onCodeChanged = authViewModel::updatePhoneConfirmationCode,
                    requestPhoneConfirmation = authViewModel::requestPhoneConfirmation,
                    onPhoneChanged = authViewModel::changePhone,
                    onPhoneUpdate = authViewModel::updatePhone
                )
            }
            composable(route = AppScreen.AuthPhoneCode.name) {
                val authViewModel: AuthViewModel = hiltViewModel()
                val authUiState by authViewModel.authUiState.collectAsState()
                CountryCodeDialog(onCountryClick = { country ->
                    authViewModel.updateCountry(country)
                    navController.navigateUp()
                })
            }
            composable(route = AppScreen.AuthPassword.name) {
                val authViewModel: AuthViewModel = hiltViewModel()
                val authUiState by authViewModel.authUiState.collectAsState()
                if (authUiState.isAuthSuccess) {
                    navController.popBackStack(AppScreen.Search.name, false)
                    navController.navigate(AppScreen.Personal.name)
                }
                PasswordAuthScreen(
                    uiState = authUiState,
                    onPasswordChanged = authViewModel::changePassword,
                    onClickShowPassword = authViewModel::showPassword,
                    onAuth = authViewModel::onClickAuth
                )
            }
            composable(route = AppScreen.Personal.name) {
                val personalViewModel: PersonalViewModel = hiltViewModel()
                val personalUiState by personalViewModel.uiState.collectAsState()
                PersonalAccountScreen(uiState = personalUiState,
                    changeSection = personalViewModel::changeSection
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