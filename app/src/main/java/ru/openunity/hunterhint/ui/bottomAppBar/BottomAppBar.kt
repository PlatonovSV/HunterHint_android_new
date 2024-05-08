package ru.openunity.hunterhint.ui.bottomAppBar

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ru.openunity.hunterhint.navigation.AppScreen
import ru.openunity.hunterhint.ui.registration.Country

@Composable
fun HuntBottomAppBar(
    navController: NavController,
    viewModel: BottomAppBarViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        actions = {
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "Localized description",
                )
            }
            IconButton(onClick = { navController.navigate("${AppScreen.RegPhone.name}/${Country.RUSSIAN_FEDERATION.cCode}") }) {
                Icon(
                    Icons.Filled.AccountCircle,
                    contentDescription = "Localized description",
                )
            }
        }, modifier = modifier.height(106.dp)
    )
}
