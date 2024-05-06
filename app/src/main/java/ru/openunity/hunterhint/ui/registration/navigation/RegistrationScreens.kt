package ru.openunity.hunterhint.ui.registration.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.openunity.hunterhint.navigation.AppScreen
import ru.openunity.hunterhint.ui.registration.RegCompletionRoute
import ru.openunity.hunterhint.ui.registration.RegDateRoute
import ru.openunity.hunterhint.ui.registration.RegEmailRoute
import ru.openunity.hunterhint.ui.registration.RegNameRoute
import ru.openunity.hunterhint.ui.registration.RegPasswordRoute
import ru.openunity.hunterhint.ui.registration.RegPhoneCodeRoute
import ru.openunity.hunterhint.ui.registration.RegPhoneRoute

fun NavGraphBuilder.regPhoneScreen(
    navigateToRegName: () -> Unit,
    navigateToRegPhoneCode: () -> Unit,
    navigateToAuth: () -> Unit
) {
    composable(route = AppScreen.RegPhone.name) {
        RegPhoneRoute(
            navigateToRegName,
            navigateToRegPhoneCode,
            navigateToAuth
        )
    }

}

fun NavGraphBuilder.regPhoneCodeScreen(
    navigateUp: () -> Unit
) {
    composable(route = AppScreen.RegPhoneCode.name) {
        RegPhoneCodeRoute(
            navigateUp = navigateUp
        )
    }
}

fun NavGraphBuilder.regNameScreen(
    navigateToRegDate: () -> Unit
) {
    composable(route = AppScreen.RegName.name) {
        RegNameRoute(
            navigateToRegDate
        )
    }
}

fun NavGraphBuilder.regDateScreen(
    navigateToRegEmail: () -> Unit
) {
    composable(route = AppScreen.RegDate.name) {
        RegDateRoute(
            navigateToRegEmail
        )
    }
}

fun NavGraphBuilder.regPassword(
    popBackStack: () -> Unit,
    navigateToRegCompletion: () -> Unit,
) {
    composable(route = AppScreen.RegPassword.name) {
        RegPasswordRoute(
            popBackStack,
            navigateToRegCompletion
        )
    }
}

fun NavGraphBuilder.regEmailScreen(
    navigateToRegPassword: () -> Unit
) {
    composable(route = AppScreen.RegEmail.name) {
        RegEmailRoute(
            navigateToRegPassword
        )
    }
}

fun NavGraphBuilder.regCompletionScreen(
    navigateOnCancel: () -> Unit,
    navigateOnComplete: () -> Unit,
) {
    composable(route = AppScreen.RegCompletion.name) {
        RegCompletionRoute(
            navigateOnCancel,
            navigateOnComplete
        )
    }
}






