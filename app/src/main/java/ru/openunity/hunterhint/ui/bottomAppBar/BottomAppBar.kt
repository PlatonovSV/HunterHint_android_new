package ru.openunity.hunterhint.ui.bottomAppBar

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.navigation.AppScreen
import ru.openunity.hunterhint.ui.registration.Country

@Composable
fun HuntBottomAppBar(
    navController: NavController,
    viewModel: BottomAppBarViewModel = hiltViewModel(LocalContext.current as ComponentActivity),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    BottomAppBar(
        actions = {
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "Localized description",
                )
            }
            if (uiState.isAuthorized) {
                IconButton(onClick = { navController.navigate(AppScreen.Personal.route) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.person),
                        contentDescription = stringResource(R.string.the_button_to_go_to_your_personal_account),
                    )
                }
            } else {
                IconButton(onClick = { navController.navigate("${AppScreen.RegPhone.name}/${Country.RUSSIAN_FEDERATION.cCode}") }) {
                    Icon(
                        Icons.Filled.AccountCircle,
                        contentDescription = stringResource(R.string.registration_button),
                    )
                }
            }

        }, modifier = modifier.height(106.dp)
    )
}
