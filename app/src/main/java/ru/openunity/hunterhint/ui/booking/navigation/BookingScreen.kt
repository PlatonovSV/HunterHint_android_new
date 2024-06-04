package ru.openunity.hunterhint.ui.booking.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.openunity.hunterhint.navigation.AppScreen
import ru.openunity.hunterhint.ui.booking.BookingRoute

fun NavGraphBuilder.bookingScreen(
    navigateUp: () -> Unit, navigateToBookings: () -> Unit, offersId: Long
) {
    composable(route = AppScreen.Booking.route) {
        BookingRoute(
            navigateToBookings, navigateUp,
            offersId
        )
    }
}