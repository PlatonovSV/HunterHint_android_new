package ru.openunity.hunterhint.ui.groundsPage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.openunity.hunterhint.navigation.AppScreen
import ru.openunity.hunterhint.ui.groundsPage.GroundsPageRoute

fun NavGraphBuilder.groundsPageScreen(
    groundsId: Int,
    canNavigateBack: Boolean,
    navigateToBooking: (Long) -> Unit,
    navigateUp: () -> Unit,
    navController: NavController,
) {
    composable(route = AppScreen.GroundsPage.route) {
        GroundsPageRoute(
            groundsId, canNavigateBack, navigateToBooking, navigateUp, navController
        )
    }
}