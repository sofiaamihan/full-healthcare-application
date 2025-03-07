package com.example.fullhealthcareapplication.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fullhealthcareapplication.ui.components.NavigationDrawer
import com.example.fullhealthcareapplication.R
import com.example.fullhealthcareapplication.data.factory.HealthServiceViewModelFactory
import com.example.fullhealthcareapplication.data.preferences.TokenDataStore
import com.example.fullhealthcareapplication.data.viewmodel.ChangePasswordViewModel
import com.example.fullhealthcareapplication.data.viewmodel.DeleteAccountViewModel
import com.example.fullhealthcareapplication.data.viewmodel.UpdateProfileViewModel
import com.example.fullhealthcareapplication.data.factory.UserInfoViewModelFactory
import com.example.fullhealthcareapplication.data.viewmodel.EditUserMeasurementsViewModel
import com.example.fullhealthcareapplication.data.viewmodel.GetUserMeasurementsViewModel
import com.example.fullhealthcareapplication.ui.components.BlackText
import com.example.fullhealthcareapplication.ui.components.ChangePasswordDialog
import com.example.fullhealthcareapplication.ui.components.DeleteAccountDialog
import com.example.fullhealthcareapplication.ui.components.EditMeasurementsDialog
import com.example.fullhealthcareapplication.ui.components.EditProfileDialog
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage

fun createFile(context: Context): Uri {
    val file = File.createTempFile("image.png", context.cacheDir.name)
    return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ProfileScreen(
    toProfile: () -> Unit,
    toHealthLogs: () -> Unit,
    toHealthReport: () -> Unit,
    toHome: () -> Unit,
    toSignOut: () -> Unit,
    tokenDataStore: TokenDataStore,
    viewModelFactory: UserInfoViewModelFactory,
    healthServiceViewModelFactory: HealthServiceViewModelFactory
){
    val nric = remember { mutableStateOf("") }
    val role = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val fullName = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val newPassword = remember { mutableStateOf("") }
    val confirmNewPassword = remember { mutableStateOf("") }
    var showModal = remember { mutableStateOf(false) }
    var showPasswordModal = remember { mutableStateOf(false) }
    var deleteAccountModal = remember { mutableStateOf(false) }
    var showMeasurementsModal = remember { mutableStateOf(false) }
    val editUserMeasurementsViewModel: EditUserMeasurementsViewModel = viewModel(factory = healthServiceViewModelFactory)
    val updateProfileViewModel: UpdateProfileViewModel = viewModel(factory = viewModelFactory)
    val changePasswordViewModel: ChangePasswordViewModel = viewModel(factory = viewModelFactory)
    val deleteAccountViewModel: DeleteAccountViewModel = viewModel(factory = viewModelFactory)
    val getUserMeasurements: GetUserMeasurementsViewModel = viewModel(factory = healthServiceViewModelFactory)
    val state = getUserMeasurements.state

    val scope = rememberCoroutineScope()

    val id = remember { mutableIntStateOf(0) }
    val age = remember { mutableIntStateOf(0) }
    val gender = remember { mutableStateOf("") }
    val weight = remember { mutableDoubleStateOf(0.0) }
    val height = remember { mutableDoubleStateOf(0.0) }

    val scrollState = rememberScrollState()

    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var permission by remember { mutableStateOf(context.checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) }
    val permissionActivity = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { permission = it }
    var currentUri by remember { mutableStateOf<String?>(null) }
    var photo by remember { mutableStateOf<String?>(null) }
    val cameraActivity = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            photo = currentUri
            showDialog = false
        }
    }
    val galleryActivity = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        photo = it.toString()
        showDialog = false
    }

    LaunchedEffect(Unit) {
        id.intValue = tokenDataStore.getId.first()?.toInt()!!
        nric.value = tokenDataStore.getNric.first().toString()
        role.value = tokenDataStore.getRole.first().toString()
        email.value = tokenDataStore.getEmail.first().toString()
        fullName.value = tokenDataStore.getFullName.first().toString()

        getUserMeasurements.getUserMeasurements(id.intValue)
    }
    scope.launch{
        state.userMeasurements.let {
            age.intValue = it.age
            gender.value = it.gender
            weight.doubleValue = it.weight
            height.doubleValue = it.height
        }
    }
    NavigationDrawer(
        title = "Profile",
        toProfile = {toProfile()},
        toHome = {toHome()}
    ){ padding ->
        Box(
            modifier = Modifier
                .padding(top = padding.calculateTopPadding())
                .clip(RoundedCornerShape(bottomStart = 38.dp, bottomEnd = 38.dp))
                .height(20.dp)
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.secondaryContainer)
        )
        Column(
            modifier = Modifier
                .padding(top = 240.dp)
                .verticalScroll(state = scrollState)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clickable{ }
//                    .padding(top = 20.dp),
//            ){
//                Image(
//                    painter = painterResource(R.drawable.profile),
//                    contentDescription = "Temporary Profile Photo",
//                    contentScale = ContentScale.FillBounds,
//                    modifier = Modifier
//                        .size(160.dp)
//                        .align(Alignment.Center)
//                        .clip(shape = CircleShape),
//                )
//            }
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)
                    .clickable {
                        showDialog = true
                    },
            ) {
                // Background image
                Box (
                    modifier = Modifier
                        .matchParentSize() // Make the foreground image fill the Box
                        .clip(CircleShape), // Clip to circle shape
                    contentAlignment = Alignment.Center
                ){
                    Image(
                        painter = painterResource(R.drawable.upload),
                        contentDescription = "Upload image",
                        modifier = Modifier.size(100.dp)
                    )

                }

                // Foreground image
                AsyncImage(
                    model = photo,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .matchParentSize()
                        .clip(CircleShape)
                )
            }
            BlackText(
                text = "Profile",
                onClick = {
                    showModal.value = true
                }
            )
            Row (
                modifier = Modifier
                    .padding(start = 24.dp, end = 24.dp, bottom = 5.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text("NRIC")
                Text(nric.value)
            }
            Row (
                modifier = Modifier
                    .padding(start = 24.dp, end = 24.dp, bottom = 5.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text("Email")
                Text(email.value)
            }
            Row (
                modifier = Modifier
                    .padding(start = 24.dp, end = 24.dp, bottom = 5.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text("Password")
                TextButton(
                    onClick = {
                        showPasswordModal.value = true
                    }
                ) {
                    Text(
                        text = "Change Password",
                        style = TextStyle(textDecoration = TextDecoration.Underline)
                    )
                }
            }
            BlackText("Measurements", {
                showMeasurementsModal.value = true
            })
            if (state.loadingState) {
                Column (
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    CircularProgressIndicator()
                }
            } else if (state.errorState) {
                Column (
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text("Error: ${state.errorMessage}")
                }
            } else {
                Row (
                    modifier = Modifier
                        .padding(start = 24.dp, end = 24.dp, bottom = 5.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text("Age")
                    Text("${state.userMeasurements.age}")
                }
                Row (
                    modifier = Modifier
                        .padding(start = 24.dp, end = 24.dp, bottom = 5.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text("Gender")
                    Text(state.userMeasurements.gender)
                }
                Row (
                    modifier = Modifier
                        .padding(start = 24.dp, end = 24.dp, bottom = 5.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text("Weight")
                    Text("${state.userMeasurements.weight} kg")
                }
                Row (
                    modifier = Modifier
                        .padding(start = 24.dp, end = 24.dp, bottom = 5.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text("Height")
                    Text("${state.userMeasurements.height} cm")
                }
            }
            Button(
                onClick = {toSignOut()},
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 24.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
            ) {
                Text("Sign Out")
            }
            Button(
                onClick = {
                    deleteAccountModal.value = true
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 16.dp, bottom = 64.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary)
            ) {
                Text("Delete Account !!!")
            }
        }

        if(showModal.value){
            EditProfileDialog(
                onDismiss = { showModal.value = false },
                onEditProfile = { nric, role, email, fullName ->
                    updateProfileViewModel.updateProfile(nric, role, email, fullName)
                    showModal.value = false
                },
                nric = nric.value,
                onNricChange = { nric.value = it },
                role = role.value,
                onRoleChange = { role.value = it },
                email = email.value,
                onEmailChange = { email.value = it },
                fullName = fullName.value,
                onFullNameChange = { fullName.value = it },
            )
        }

        if(showMeasurementsModal.value){
            EditMeasurementsDialog(
                onDismiss = { showMeasurementsModal.value = false },
                onEditMeasurements = { id, nric, role, age, gender, weight, height ->
                    editUserMeasurementsViewModel.editUserMeasurements(id, nric, role, age, gender, weight, height)
                    showMeasurementsModal.value = false
                    toHome()
                },
                id = id.intValue,
                nric = nric.value,
                onNricChange = { nric.value = it },
                role = role.value,
                onRoleChange = { role.value = it },
                age = age.intValue,
                onAgeChange = { age.intValue = it },
                gender = gender.value,
                onGenderChange = { gender.value = it },
                weight = weight.doubleValue,
                onWeightChange = { weight.doubleValue = it },
                height = height.doubleValue,
                onHeightChange = { height.doubleValue = it }
            )
        }

        if(showPasswordModal.value){
            ChangePasswordDialog(
                onDismiss = { showPasswordModal.value = false },
                onChangePassword = { nric, role, password, newPassword ->
                    changePasswordViewModel.changePassword(nric, role, password, newPassword)
                    showPasswordModal.value = false
                },
                nric = nric.value,
                role = role.value,
                password = password.value,
                onPasswordChange = { password.value = it },
                newPassword = newPassword.value,
                onNewPasswordChange = { newPassword.value = it },
                confirmNewPassword = confirmNewPassword.value,
                onConfirmNewPasswordChange = { confirmNewPassword.value = it }
            )
        }

        if(deleteAccountModal.value){
            DeleteAccountDialog (
                onDismiss = { deleteAccountModal.value = false },
                onDeleteAccount = { nric, role, password ->
                    deleteAccountViewModel.deleteAccount(nric, role, password)
                    deleteAccountModal.value = false
                },
                nric = nric.value,
                role = role.value,
                password = password.value,
                onPasswordChange = { password.value = it },
                toSignOut = toSignOut
            )
        }

        if (showDialog) {
            Dialog (
                onDismissRequest = {
                    showDialog = false
                }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Select a picture from the gallery or take a picture")
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(
                                onClick = {
                                    galleryActivity.launch(PickVisualMediaRequest(
                                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                    ))
                                }
                            ) {
                                Text("Gallery")
                            }
                            TextButton(
                                onClick = {
                                    if (permission) {
                                        val uri = createFile(context)
                                        currentUri = uri.toString()
                                        cameraActivity.launch(uri)
                                    } else {
                                        permissionActivity.launch(android.Manifest.permission.CAMERA)
                                    }
                                }
                            ) {
                                Text("Take Picture")
                            }
                            TextButton(
                                onClick = {
                                    showDialog = false
                                }
                            ) {
                                Text("Cancel")
                            }
                        }
                    }
                }
            }
        }
    }
}