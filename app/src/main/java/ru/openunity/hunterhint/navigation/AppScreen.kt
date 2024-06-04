package ru.openunity.hunterhint.navigation

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import ru.openunity.hunterhint.ui.Success
import ru.openunity.hunterhint.ui.authorization.AuthViewModel
import ru.openunity.hunterhint.ui.authorization.PasswordAuthScreen
import ru.openunity.hunterhint.ui.authorization.PhoneAuthScreen
import ru.openunity.hunterhint.ui.authorization.navigation.authPhoneCodeScreen
import ru.openunity.hunterhint.ui.booking.navigation.bookingScreen
import ru.openunity.hunterhint.ui.bookingInfo.navigation.bookingInfoScreen
import ru.openunity.hunterhint.ui.comment.navigation.commentScreen
import ru.openunity.hunterhint.ui.groundsPage.navigation.groundsPageScreen
import ru.openunity.hunterhint.ui.personal.PersonalAccountScreen
import ru.openunity.hunterhint.ui.registration.Country
import ru.openunity.hunterhint.ui.registration.navigation.regCompletionScreen
import ru.openunity.hunterhint.ui.registration.navigation.regDateScreen
import ru.openunity.hunterhint.ui.registration.navigation.regEmailScreen
import ru.openunity.hunterhint.ui.registration.navigation.regNameScreen
import ru.openunity.hunterhint.ui.registration.navigation.regPassword
import ru.openunity.hunterhint.ui.registration.navigation.regPhoneCodeScreen
import ru.openunity.hunterhint.ui.registration.navigation.regPhoneScreen
import ru.openunity.hunterhint.ui.search.navigation.searchScreen
import ru.openunity.hunterhint.ui.searchFilters.navigation.searchFiltersScreen


enum class TestTag {
    GroundInfo
}

@Preview(
    showBackground = true, showSystemUi = true
)
@Composable
fun HunterHintApp(
    navController: NavHostController = rememberNavController()
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route?.substringBefore('/') ?: AppScreen.Search.name
    )

    Scaffold(topBar = {
        val context = LocalContext.current
        TopAppBar(
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
    }, bottomBar = {
        AppBottomAppBar(currentScreen = currentScreen, navController = navController)
    }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreen.SearchFilters.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            searchScreen(onGroundsCardClick = {
                navController.navigate("${AppScreen.GroundsPage.name}/${it}")
            })
            groundsPageScreen(
                groundsId = backStackEntry?.arguments?.getString("groundsId")?.toIntOrNull() ?: -1,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateToBooking = { offersId ->
                    navController.navigate("${AppScreen.Booking.name}/${offersId}")
                },
                navigateUp = {
                    navController.navigateUp()
                },
                navController = navController
            )
            bookingScreen(
                navigateUp = {
                    navController.navigateUp()
                },
                navigateToBookings = {
                    navController.navigate(AppScreen.Personal.route)
                },
                offersId = backStackEntry?.arguments?.getString("offersId")?.toLongOrNull() ?: -1L
            )
            regPhoneScreen(navigateToAuth = {
                navController.popBackStack()
                navController.navigate("${AppScreen.AuthPhone.name}/${Country.RUSSIAN_FEDERATION.cCode}")
            }, navigateToRegName = {
                navController.navigate(AppScreen.RegName.name)
            }, navigateToRegPhoneCode = {
                navController.navigate(AppScreen.RegPhoneCode.route)
            },
                countryId = backStackEntry?.arguments?.getString("countryId")?.toIntOrNull()
                    ?: Country.RUSSIAN_FEDERATION.cCode
            )
            regPhoneCodeScreen(navigateUp = { countryId ->
                navController.popBackStack()
                navController.popBackStack()
                navController.navigate("${AppScreen.RegPhone}/$countryId")
            })
            regNameScreen(navigateToRegDate = {
                navController.navigate(AppScreen.RegDate.name)
            })
            regDateScreen(navigateToRegEmail = {
                navController.navigate(AppScreen.RegEmail.name)
            })
            regEmailScreen(navigateToRegPassword = {
                navController.navigate(AppScreen.RegPassword.name)
            })
            regPassword(popBackStack = {
                navController.popBackStack(AppScreen.Search.name, false)
            }, navigateToRegCompletion = {
                navController.navigate(AppScreen.RegCompletion.name)
            })
            regCompletionScreen(
                navigateToPersonal = {
                    navController.popBackStack(AppScreen.Search.name, false)
                    navController.navigate(AppScreen.Personal.route)
                }
            )
            composable(route = AppScreen.AuthPhone.route) {
                val authViewModel: AuthViewModel =
                    hiltViewModel(LocalContext.current as ComponentActivity)
                val countryId = backStackEntry?.arguments?.getString("countryId")?.toIntOrNull()
                    ?: Country.RUSSIAN_FEDERATION.cCode
                authViewModel.updateCountry(countryId)
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
                        navController.navigate("${AppScreen.RegPhone.name}/${Country.RUSSIAN_FEDERATION.cCode}")
                    },
                    uiState = authUiState,
                    onCodeChanged = authViewModel::updatePhoneConfirmationCode,
                    requestPhoneConfirmation = authViewModel::requestPhoneConfirmation,
                    onPhoneChanged = authViewModel::changePhone,
                    onPhoneUpdate = authViewModel::updatePhone
                )
            }
            authPhoneCodeScreen(navigateUp = { countryId ->
                navController.popBackStack()
                navController.popBackStack()
                navController.navigate("${AppScreen.AuthPhone.name}/$countryId")
            })
            composable(route = AppScreen.AuthPassword.name) {
                val authViewModel: AuthViewModel =
                    hiltViewModel(LocalContext.current as ComponentActivity)
                val authUiState by authViewModel.authUiState.collectAsState()
                val state = authUiState.state
                if (state is Success && state.result) {
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
                PersonalAccountScreen(
                    navigateToAuth = {
                        navController.popBackStack()
                        navController.navigate("${AppScreen.AuthPhone.name}/${Country.RUSSIAN_FEDERATION.cCode}")
                    },
                    navigateToBookingInfo = { id ->
                        navController.navigate("${AppScreen.BookingInfo.name}/$id")
                    }
                )
            }
            bookingInfoScreen(
                navController = navController,
                navigateUp = { navController.navigateUp() },
                navigateToCreateComment = { bookingId ->
                    navController.navigate("${AppScreen.Comment.name}/$bookingId")
                },
                navigateToGroundsPage = {
                    navController.navigate("${AppScreen.GroundsPage.name}/${it}")
                }
            )
            commentScreen(
                navController = navController,
                navigateUp = {
                    navController.navigateUp()
                }
            )
            searchFiltersScreen(
                navigateUp = {
                    navController.navigateUp()
                }
            )
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
            intent, context.getString(R.string.app_name)
        )
    )
}