package ru.openunity.hunterhint.ui.authorization.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.openunity.hunterhint.navigation.AppScreen
import ru.openunity.hunterhint.ui.registration.PhoneCodeRoute

fun NavGraphBuilder.authPhoneCodeScreen(
    navigateUp: (Int) -> Unit
) {
    composable(route = AppScreen.AuthPhoneCode.route) {
        PhoneCodeRoute(
            navigateUp = navigateUp
        )
    }
}