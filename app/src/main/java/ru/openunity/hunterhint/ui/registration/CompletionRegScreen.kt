package ru.openunity.hunterhint.ui.registration

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.ui.components.ComponentScreen
import ru.openunity.hunterhint.ui.components.ComponentState

@Composable
internal fun RegCompletionRoute(
    navigateToPersonal: () -> Unit,
    viewModel: RegViewModel = hiltViewModel(LocalContext.current as ComponentActivity)
) {
    val uiState by viewModel.regUiState.collectAsState()
    CompletionRegScreen(
        state = uiState.regState,
        retryAction = viewModel::auth,
        navigateToPersonal = navigateToPersonal)
}

@Composable
fun CompletionRegScreen(
    state: ComponentState,
    retryAction: () -> Unit,
    navigateToPersonal: () -> Unit,
    modifier: Modifier = Modifier
) {
    ComponentScreen(loadingStrResId = R.string.authorization,
        waitContent = {},
        successContent = { navigateToPersonal() },
        retryOnErrorAction = retryAction,
        state = state,
        modifier = modifier.fillMaxSize()
    )
}