package ru.openunity.hunterhint.ui.search.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.openunity.hunterhint.navigation.AppScreen
import ru.openunity.hunterhint.ui.search.SearchRoute

fun NavGraphBuilder.searchScreen(
    onGroundsCardClick: (Int) -> Unit,
    navigateToFilters: () -> Unit,
) {
    composable(route = AppScreen.Search.route) {
        SearchRoute(
            onGroundsCardClick,
            navigateToFilters,
            groundsIds = it.arguments?.getString("groundIds")
        )
    }
}
