package ru.openunity.hunterhint.ui.groundsCreationScreen.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.openunity.hunterhint.navigation.AppScreen
import ru.openunity.hunterhint.ui.groundsCreationScreen.GroundsCreationRoute


fun NavGraphBuilder.groundsCreationScreen(
    navController: NavController,
    navigateUp: () -> Unit,
) {
    composable(
        route = AppScreen.GroundsCreation.route,
    ) {
        GroundsCreationRoute(
            navigateUp = navigateUp,
            navController = navController
        )
    }
}