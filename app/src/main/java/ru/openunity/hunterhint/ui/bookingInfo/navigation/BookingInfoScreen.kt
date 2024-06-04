package ru.openunity.hunterhint.ui.bookingInfo.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.openunity.hunterhint.navigation.AppScreen
import ru.openunity.hunterhint.ui.bookingInfo.BookingInfoRoute

fun NavGraphBuilder.bookingInfoScreen(
    navController: NavController,
    navigateToGroundsPage: (Int) -> Unit,
    navigateUp: () -> Unit,
    navigateToCreateComment: (Long) -> Unit
) {
    composable(
        route = AppScreen.BookingInfo.route,
        arguments = listOf(navArgument("bookingId") { type = NavType.LongType })
    ) { backStackEntry ->
        BookingInfoRoute(
            navigateToCreateComment,
            navigateToGroundsPage,
            navigateUp,
            bookingId = backStackEntry.arguments?.getLong("bookingId") ?: -1L,
            navController = navController
        )
    }
}

