package com.android.example.teseproject.Criar.methods.OTP

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.example.teseproject.BasesDados.DataBase_All.AllUserViewModel
import com.android.example.teseproject.BasesDados.DataBase_All.All_Methods_Data
import com.android.example.teseproject.ExtraFunctions.ProgressBarStep
import com.android.example.teseproject.ExtraFunctions.enviarEmail
import com.android.example.teseproject.navegation.Routes
import kotlinx.coroutines.launch

@Composable
fun RequiredFieldLabel1(text: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpCode(navController: NavController,
            email: String,
            allUserViewModel: AllUserViewModel,
            method: String) {
    var user by remember { mutableStateOf<All_Methods_Data?>(null) }

    LaunchedEffect(email) {
        user = allUserViewModel.getAllUserByEmail(email)
    }

    var code by remember { mutableStateOf("") }
    val context = LocalContext.current

    val sharedPrefs = context.getSharedPreferences("otp_prefs", Context.MODE_PRIVATE)
    var storedCode = sharedPrefs.getString("generated_code", "") ?: ""
    var expiryTime = sharedPrefs.getLong("expiry_time", 0)
    val scope = rememberCoroutineScope()

    var showErrors by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var isSubmitting by remember { mutableStateOf(false) }

    val methodDisplayText = when (method) {
        "email" -> "no seu email"
        "sms" -> "nas suas mensagens"
        else -> method
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(buildAnnotatedString {
                        append("Criar conta na aplicação com ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("Código de Uso Único") }
                    })
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.Black
                        )
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
                val focusManager = LocalFocusManager.current
                RequiredFieldLabel1(
                    text = "Por favor insira o código que recebeu $methodDisplayText",
                    modifier = Modifier.align(Alignment.Start)
                )
                OutlinedTextField(
                    value = code,
                    onValueChange = { inputCode ->
                        if (inputCode.all { it.isDigit() }&& !isSubmitting) {
                            code = inputCode
                            showErrors = false
                        }
                    },
                    //label = { Text("Código") },
                    isError = showErrors,
                    enabled =!isSubmitting,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    )
                )
                if (showErrors) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        val currentUser = user ?: return@Button
                        isSubmitting = true

                        if (System.currentTimeMillis() > expiryTime) {
                            showErrors = true
                            errorMessage = "O código expirou. Por favor, gere um novo."
                            code = ""
                            isSubmitting = false
                        }

                        else if (code == storedCode) {
                            val updatedUser = currentUser.copy(
                                hasOtp = true
                            )
                            scope.launch {
                                allUserViewModel.updateUser(updatedUser)
                                sharedPrefs.edit().remove("generated_code").remove("expiry_time").apply()
                                navController.previousBackStackEntry?.savedStateHandle?.set("method", method)
                                navController.navigate("sucessoCriar/${currentUser.firstName}/$email/$method") {
                                    popUpTo(Routes.Home) { inclusive = false }
                                    launchSingleTop = true
                                }
                            }

                        } else {
                            showErrors = true
                            errorMessage = "Código incorreto. Tente novamente."
                            code=""
                            isSubmitting = false
                        }

                    },
                    enabled = !isSubmitting,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding( bottom = 50.dp, start = 50.dp, end = 50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Criar Conta", color= Color.Black)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(onClick = { scope.launch {
                    val currentUser = user
                    if (currentUser != null) {
                        val newCode = generateOtpCode()
                        val newExpiry = System.currentTimeMillis() + 24 * 60 * 60 * 1000
                        sharedPrefs.edit()
                            .putString("generated_code", newCode)
                            .putLong("expiry_time", newExpiry)
                            .apply()
                        storedCode = newCode
                        expiryTime = newExpiry

                        if (method.lowercase() == "email") {
                            enviarEmail(email, newCode) { success ->
                                if (success) {
                                    errorMessage = ""
                                    showErrors = false
                                    code = ""
                                    Toast.makeText(
                                        context,
                                        "Um novo código foi enviado para o seu email.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    errorMessage = "Falha ao enviar o código para o email."
                                    showErrors = true
                                }
                            }
                        } else if (method.lowercase() == "sms") {
                            enviaOtpParaApp2(context, newCode)
                            errorMessage = ""
                            showErrors = false
                            code = ""
                            Toast.makeText(
                                context,
                                "Um novo código foi enviado por SMS.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        errorMessage = "Não foi possível gerar um novo código. Volte atrás e tente novamente."
                        showErrors = true
                    }
                }
                },
                    enabled = !isSubmitting,
                    modifier = Modifier.fillMaxWidth()
                        .padding( bottom = 50.dp, start = 50.dp, end = 50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Gerar novo código", color = Color.Black)
                }




            }
        }
    }
}

