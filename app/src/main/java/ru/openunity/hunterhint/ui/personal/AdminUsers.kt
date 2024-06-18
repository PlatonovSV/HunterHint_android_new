package ru.openunity.hunterhint.ui.personal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.ui.booking.HuntingMethodItem
import ru.openunity.hunterhint.ui.components.ComponentScreen
import ru.openunity.hunterhint.ui.enums.AccessLevels
import ru.openunity.hunterhint.ui.enums.Status
import ru.openunity.hunterhint.ui.enums.getStatusByCode

@Composable
fun AdminUsersPanel(
    viewModel: PersonalViewModel,
    uiState: PersonalUiState,
    modifier: Modifier = Modifier
) {
    ComponentScreen(
        loadingStrResId = R.string.search,
        waitContent = {
            AdminUsersSearchPanel(uiState = uiState, viewModel = viewModel)
        },
        successContent = {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    AdminUsersSearchPanel(uiState = uiState, viewModel = viewModel)
                }
                items(uiState.foundByAdmin) { card ->
                    UserCard(
                        user = card,
                        reloadUserCards = viewModel::loadUserCards,
                        select = {
                            viewModel.selectUserCardForAdmin(card)
                        }
                    )
                }
            }
        },
        retryOnErrorAction = viewModel::findUsers,
        state = uiState.userSearchState
    )

    UserStatusBottomSheet(
        showBottomSheet = uiState.showBottomSheet,
        user = uiState.bottomSheetUserCard,
        onDismissRequest = viewModel::dismissUserStatusBottomSheet,
        setStatus = viewModel::setStatus,
        saveUserStatus = viewModel::saveAdminChanges,
        isChangeStatus = uiState.editStatus,
        setAccessLevel = viewModel::setAccessLevel,
        changeSection = viewModel::changeAdminEditSection
    )
}

@Composable
private fun AdminUsersSearchPanel(
    uiState: PersonalUiState,
    viewModel: PersonalViewModel
) {
    Column(
        modifier = Modifier
            .padding(0.dp, 8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = uiState.userNameByAdmin,
            onValueChange = viewModel::updateUserNameByAdmin,
            singleLine = true,
            label = {
                Text(text = stringResource(id = R.string.user_name))
            },
            modifier = Modifier
                .padding(32.dp, 0.dp)
                .fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.userLastNameByAdmin,
            onValueChange = viewModel::updateUserLastNameByAdmin,
            singleLine = true,
            label = {
                Text(text = stringResource(id = R.string.user_lastname))
            },
            modifier = Modifier
                .padding(32.dp, 0.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = viewModel::findUsers
        ) {
            Text(
                text = stringResource(id = R.string.find_users),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun UserCard(
    user: UserCard,
    reloadUserCards: () -> Unit,
    modifier: Modifier = Modifier,
    select: () -> Unit = {},
    showDetail: Boolean = true
) {
    Card(
        modifier = modifier
            .clickable {
                select()
            }
            .padding(16.dp, 6.dp)
            .fillMaxWidth()
    ) {
        ComponentScreen(
            loadingStrResId = R.string.downloading_user_information,
            waitContent = {},
            successContent = {
                Column(modifier = Modifier.padding(8.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context = LocalContext.current)
                                .data(user.dto.photoUrl).crossfade(true).build(),
                            error = painterResource(R.drawable.ic_broken_image),
                            placeholder = painterResource(R.drawable.loading_img),
                            contentDescription = stringResource(R.string.user_profile_photo),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .padding(12.dp)
                                .size(78.dp)
                                .clip(CircleShape)

                        )
                        Column {
                            Text(
                                text = user.dto.name,
                                style = MaterialTheme.typography.headlineMedium
                            )
                            Text(
                                text = user.dto.lastName,
                                style = MaterialTheme.typography.headlineMedium
                            )
                            Text(
                                text = "+${user.dto.phoneNumber}",
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    }
                    Text(
                        text = user.dto.email,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    if (showDetail) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(id = getStatusByCode(user.dto.status).strResId),
                                style = MaterialTheme.typography.headlineSmall,
                            )
                            Text(
                                text = getRole(user.dto.accessLevel).name,
                                style = MaterialTheme.typography.headlineSmall,
                            )
                        }
                    }
                }
            },
            retryOnErrorAction = reloadUserCards,
            state = user.state
        )

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserStatusBottomSheet(
    showBottomSheet: Boolean,
    user: UserCard,
    onDismissRequest: () -> Unit,
    isChangeStatus: Boolean,
    changeSection: () -> Unit,
    setStatus: (Int) -> Unit,
    setAccessLevel: (Int) -> Unit,
    saveUserStatus: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UserCard(
                    user = user,
                    reloadUserCards = {}
                )
                if (isChangeStatus) {
                    Status.entries.forEach {
                        HuntingMethodItem(
                            isSelect = user.dto.status == it.code,
                            onClick = { setStatus(it.code) },
                            stringRes = it.strResId
                        )
                    }
                } else {
                    AccessLevels.entries.forEach {
                        HuntingMethodItem(
                            isSelect = user.dto.accessLevel == it.code,
                            onClick = { setAccessLevel(it.code) },
                            stringRes = it.strResId
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    changeSection()
                }) {
                    Text(stringResource(if (isChangeStatus) R.string.to_change_access_level else R.string.to_change_status))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onDismissRequest()
                        }
                        saveUserStatus()
                    }
                }) {
                    Text(stringResource(R.string.save))
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

        }
    }
}