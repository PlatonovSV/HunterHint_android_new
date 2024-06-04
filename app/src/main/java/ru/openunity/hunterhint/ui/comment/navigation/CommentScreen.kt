package ru.openunity.hunterhint.ui.comment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.openunity.hunterhint.navigation.AppScreen
import ru.openunity.hunterhint.ui.comment.CommentRoute


fun NavGraphBuilder.commentScreen(
    navController: NavController,
    navigateUp: () -> Unit,
) {
    composable(
        route = AppScreen.Comment.route,
        arguments = listOf(navArgument("bookingId") { type = NavType.LongType })
    ) { backStackEntry ->
        CommentRoute(
            navigateUp = navigateUp,
            bookingId = backStackEntry.arguments?.getLong("bookingId") ?: -1L,
            navController = navController
        )
    }
}