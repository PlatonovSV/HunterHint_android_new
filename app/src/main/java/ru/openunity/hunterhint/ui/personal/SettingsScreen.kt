package ru.openunity.hunterhint.ui.personal

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.dto.getCountryByCode
import ru.openunity.hunterhint.ui.authorization.PhoneAndCountry
import ru.openunity.hunterhint.ui.registration.CheckBoxAndLabel

@Composable
fun SettingsScreen(
    uiState: PersonalUiState,
    setProfilePhoto: (Uri?) -> Unit,
    removeImage: () -> Unit,
    updateProfile: () -> Unit,
    onClickLogOut: () -> Unit,

    updateNewName: (String) -> Unit,
    updateNewLastName: (String) -> Unit,
    updatePhoneNumber: (String) -> Unit,
    updateNewEmail: (String) -> Unit,
    updateNewPassword: (String) -> Unit,
    updateOldPassword: (String) -> Unit,
    onClickShowOldPassword: (Boolean) -> Unit,
    onClickShowNewPassword: (Boolean) -> Unit,
    deleteAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        setProfilePhoto(it)
    }
    Column(modifier = modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(24.dp))
        if (uiState.newUserProfilePicture != null) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                Image(painter = rememberAsyncImagePainter(uiState.newUserProfilePicture),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(16.dp, 8.dp)
                        .size(100.dp)
                        .clickable {
                            removeImage()
                        })
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { galleryLauncher.launch("image/*") },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.add_photo),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null
            )
            Text(
                text = stringResource(R.string.select_a_new_profile_picture),
                style = MaterialTheme.typography.titleMedium
            )
        }
        UpdatedFields(
            uiState = uiState,
            updateNewName = updateNewName,
            updateNewLastName = updateNewLastName,
            updatePhoneNumber = updatePhoneNumber,
            updateNewEmail = updateNewEmail ,
            updateNewPassword = updateNewPassword,
            updateOldPassword = updateOldPassword,
            onClickShowOldPassword = onClickShowOldPassword,
            onClickShowNewPassword = onClickShowNewPassword,
            modifier = Modifier.padding(32.dp)
        )

        Button(onClick = updateProfile) {
            Row {
                Text(
                    text = stringResource(R.string.update_your_profile),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }


        Spacer(modifier = Modifier.weight(1f))
        LogoutButton(
            onClickLogOut,
            Modifier
                .padding(22.dp)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(36.dp))
        TextButton(onClick = deleteAccount, modifier = modifier) {
            Text(
                text = stringResource(id = R.string.delete_account), style = MaterialTheme.typography.labelLarge
            )
        }
        Spacer(modifier = Modifier.height(36.dp))
    }
}

@Composable
fun UpdatedFields(
    uiState: PersonalUiState,
    updateNewName: (String) -> Unit,
    updateNewLastName: (String) -> Unit,
    updatePhoneNumber: (String) -> Unit,
    updateNewEmail: (String) -> Unit,
    updateNewPassword: (String) -> Unit,
    updateOldPassword: (String) -> Unit,
    onClickShowOldPassword: (Boolean) -> Unit,
    onClickShowNewPassword: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(value = uiState.newName,
            onValueChange = updateNewName,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(),
            label = {
                Text(text = stringResource(R.string.update_user_name))
            })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = uiState.newLastName,
            onValueChange = updateNewLastName,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(),
            label = {
                Text(text = stringResource(R.string.update_user_lastname))
            })
        Spacer(modifier = Modifier.height(8.dp))
        PhoneAndCountry(
            country = getCountryByCode(uiState.user.countryCode),
            onPhoneUpdate = updatePhoneNumber,
            chooseCountryCode = {},
            phone = uiState.newPhoneNumber,
            isBadPhone = false,
            isEnabled = true,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            textStyle = MaterialTheme.typography.bodyMedium,
            value = uiState.newEmail,
            enabled = true,
            singleLine = true,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
            ),
            onValueChange = updateNewEmail,
            label = {
                Text(stringResource(R.string.email))
            },
            isError = false,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email, imeAction = ImeAction.Done
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            textStyle = MaterialTheme.typography.bodyMedium,
            value = uiState.oldPassword,
            singleLine = true,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
            ),
            visualTransformation = if (uiState.isOldPasswordShow) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            onValueChange = updateOldPassword,
            label = {
                Text(stringResource(R.string.old_password))
            },
            isError = false,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            )
        )
        CheckBoxAndLabel(
            label = R.string.show_password,
            selected = uiState.isOldPasswordShow,
            onClick = onClickShowOldPassword
        )

        OutlinedTextField(
            textStyle = MaterialTheme.typography.bodyMedium,
            value = uiState.newPassword,
            singleLine = true,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
            ),
            visualTransformation = if (uiState.isNewPasswordShow) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            onValueChange = updateNewPassword,
            label = {
                Text(stringResource(R.string.new_password))
            },
            isError = false,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            )
        )
        CheckBoxAndLabel(
            label = R.string.show_password,
            selected = uiState.isNewPasswordShow,
            onClick = onClickShowNewPassword
        )
    }
}