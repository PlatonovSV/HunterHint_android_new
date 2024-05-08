package ru.openunity.hunterhint.ui.registration

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.ui.State
import ru.openunity.hunterhint.ui.components.Screen

@Composable
internal fun RegCompletionRoute(
    navigateOnCancel: () -> Unit,
    navigateOnComplete: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegViewModel = hiltViewModel(LocalContext.current as ComponentActivity)
) {
    val uiState by viewModel.regUiState.collectAsState()
    CompletionRegScreen(state = uiState.state,
        retryAction = viewModel::requestRegistration,
        cancelAction = {
            viewModel.onRegCompete()
            navigateOnCancel()
        },
        onComplete = {
            viewModel.onRegCompete()
            navigateOnComplete()
        },
        modifier = modifier
    )
}

@Composable
fun CompletionRegScreen(
    state: State,
    retryAction: () -> Unit,
    cancelAction: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Screen(state = state, retryAction = retryAction, cancelAction = cancelAction, composable = {
        RegistrationSuccessful(
            imageId = R.drawable.task_alt,
            action = onComplete,
            modifier = modifier.fillMaxSize()
        )
    })
}

@Composable
fun RegistrationSuccessful(
    action: () -> Unit, imageId: Int, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = imageId), contentDescription = ""
        )
        Text(
            text = stringResource(R.string.registration_completed_successfully),
            modifier = Modifier.padding(16.dp)
        )
        Button(onClick = action) {
            Text(stringResource(R.string.contin))
        }
    }
}