package com.android.example.teseproject.Criar.methods

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.example.teseproject.BasesDados.DataBase_All.AllUserViewModel
import com.android.example.teseproject.BasesDados.DataBase_All.All_Methods_Data
import com.android.example.teseproject.ExtraFunctions.ProgressBarStep
import com.android.example.teseproject.ExtraFunctions.hashPassword
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordPage(
    navController: NavController,
    email: String,
    allUserViewModel: AllUserViewModel
) {
    var user by remember { mutableStateOf<All_Methods_Data?>(null) }

    var passwordVisible by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val isPasswordStrong = password.length >= 8
    val passwordsMatch = password == confirmPassword
    val allFieldsFilled = password.isNotBlank() && confirmPassword.isNotBlank()
    val isvalid = allFieldsFilled && passwordsMatch && isPasswordStrong


    var showErrors by remember { mutableStateOf(false) }
    var containPersonalData by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(email) {
        val decodedEmail = Uri.decode(email)
        val allUsers = allUserViewModel.getAllUsers()
        Log.d("DB_CONTENT", "All users in DB: $allUsers")
        user = allUserViewModel.getAllUserByEmail(decodedEmail)
        if (user != null) {
            Log.d("PasswordPage", "Fetched user: $user")
        } else {
            Log.w("PasswordPage", "User NOT found for email: $email")
        }
    }

    LaunchedEffect(password, confirmPassword) {
        showErrors = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    buildAnnotatedString {
                        append("Criar conta na aplicação com ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Palavra-Passe")
                        }
                    }
                )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    titleContentColor = Color.Black,
                )
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                ProgressBarStep(progress = 1f)
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                RequiredFieldLabel(text = "Escreva a sua Palavra-Passe. Deve ter no mínimo 8 caracteres."
                    ,modifier = Modifier.align(Alignment.Start))
                PasswordField(
                    label = "",
                    password = password,
                    onPasswordChange = { password = it.trim() },
                    passwordVisible = passwordVisible,
                    onPasswordVisibilityChange = { passwordVisible = it }
                )
                if (showErrors && !isPasswordStrong) {
                    Text("A palavra-passe deve ter no mínimo 8 caracteres."
                        , color = MaterialTheme.colorScheme.error)
                }


                Spacer(modifier = Modifier.height(16.dp))
                RequiredFieldLabel(text = "Confirme a sua Palavra-Passe."
                    ,modifier = Modifier.align(Alignment.Start))

                PasswordField(
                    label = "",
                    password = confirmPassword,
                    onPasswordChange = { confirmPassword = it },
                    passwordVisible = passwordVisible,
                    onPasswordVisibilityChange = { passwordVisible = it },

                )


                if (showErrors && password != confirmPassword) {
                    Text("As palavras-passe não coincidem.", color = MaterialTheme.colorScheme.error)
                }

                if (showErrors && containPersonalData) {
                    Text("A palavra-passe não pode conter o seu nome, email ou número de telemóvel.", color = MaterialTheme.colorScheme.error)
                }


                Spacer(modifier = Modifier.height(50.dp))
                Row(verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 30.dp))
                {
                    Button(
                        onClick = {
                            showErrors = true
                            containPersonalData = false
                            if (!isvalid) return@Button

                            user?.let { currentUser ->
                                val passwordLower = password.lowercase()
                                containPersonalData = listOf(
                                    currentUser.firstName.lowercase(),
                                    currentUser.lastName.lowercase(),
                                    currentUser.email.lowercase(),
                                    currentUser.mobile.lowercase()
                                ).any {personalData ->
                                    personalData.isNotBlank() && passwordLower.contains(personalData)
                                }
                                if(containPersonalData) return@Button
                                val hashedPassword = hashPassword(password)
                                scope.launch {
                                    val updatedUser = currentUser.copy(
                                        passwordHash = hashedPassword,
                                        hasPassword = true
                                    )
                                    allUserViewModel.updateUser(updatedUser)
                                    Log.d("PasswordPage", "Updated user password: $updatedUser")
                                    val encodedEmail = Uri.encode(currentUser.email)
                                    navController.currentBackStackEntry?.savedStateHandle?.set("method", "password")
                                    navController.navigate("sucessoCriar/${currentUser.firstName}/$encodedEmail/password")
                                }
                            }
                        },
                        enabled = true,
                        modifier = Modifier
                            .padding(bottom = 50.dp)
                            .fillMaxWidth(),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Criar Conta", color = Color.Black)
                    }
                }



            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(
    label: String,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text(label) },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
            IconButton(onClick = { onPasswordVisibilityChange(!passwordVisible) }) {
                Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
            }
        },
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedLabelColor = Color.Gray,
            unfocusedLabelColor = Color.Gray
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )


}

@Composable
fun RequiredFieldLabel(text: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Text(text, style = MaterialTheme.typography.bodyMedium)

    }
}

