package ru.openunity.hunterhint.ui.searchFilters.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.openunity.hunterhint.navigation.AppScreen
import ru.openunity.hunterhint.ui.searchFilters.SearchFiltersRoute

fun NavGraphBuilder.searchFiltersScreen(
    navigateUp: () -> Unit,
    navigateToSearchScreen: (List<Int>) -> Unit,
) {
    composable(route = AppScreen.SearchFilters.route) {
        SearchFiltersRoute(navigateUp, navigateToSearchScreen)
    }
}