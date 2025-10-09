package com.android.example.teseproject.Entrar.EntrarMethod.EOTP.MudarEmailOtp

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.android.example.teseproject.Criar.methods.OTP.RequiredFieldLabel1
import com.android.example.teseproject.Criar.methods.OTP.enviaOtpParaApp2
import com.android.example.teseproject.Criar.methods.OTP.generateOtpCode
import com.android.example.teseproject.ExtraFunctions.enviarEmail
import com.android.example.teseproject.navegation.Routes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeEmailOtp(navController: NavController, method: String, allUserViewModel: AllUserViewModel) {
    var email by remember { mutableStateOf("") }
    var telemovel by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var codigo by remember { mutableStateOf("") }
    val context = navController.context
    val scope = rememberCoroutineScope()
    val isEmailMethod = method.lowercase() in listOf("no seu email", "email")
    val isMobileMethod = method.lowercase() in listOf("nas suas mensagens", "sms", "telemóvel", "telemovel")
    val sharedPreferences = context.getSharedPreferences("otp_prefs", Context.MODE_PRIVATE)
    val NineDigits = telemovel.length== 9 && telemovel.all{it.isDigit()}

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(buildAnnotatedString { append("Entrar na aplicação com ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){append("Código de uso Único")}}) },
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
            Button(
                onClick = {
                    errorMessage = null
                    scope.launch {
                        if(isEmailMethod) {
                            if (email.isBlank()) {
                                errorMessage = "Por favor, insira o seu email"
                                return@launch
                            }
                                val user = allUserViewModel.getAllUserByEmail(email)
                                if (user != null && user.hasOtp) {
                                    val otpCode = generateOtpCode()
                                    sharedPreferences
                                        .edit()
                                        .putString("generated_code", otpCode)
                                        .putString("otp_user_identifier", email)
                                        .apply()
                                    enviarEmail(email, otpCode){success ->
                                        if (success){
                                            navController.navigate("${Routes.escreverCodeVerify}/no seu email")
                                        } else{
                                            errorMessage = "Falha ao enviar o código para o email."
                                        }
                                    }
                                } else {
                                    errorMessage = "Email não encontrado no sistema."
                                }
                            }
                        else if (isMobileMethod){
                            if (telemovel.isBlank()) {
                                errorMessage = "Por favor, insira o seu número de telemóvel."
                                return@launch
                            }
                            if(!telemovel.matches(Regex("^9\\d{8}\$")) && !NineDigits){
                                errorMessage = "Número de telemóvel inválido."+
                                        " Deve começar por 9 e ter 9 dígitos."
                                return@launch
                            }
                            val user = allUserViewModel.getAllUserByMobile(telemovel)

                            if (user != null && user.hasOtp) {
                                val otpCode = generateOtpCode()
                                val expiryTime = System.currentTimeMillis() + 24 * 60 * 60 * 1000
                                sharedPreferences
                                    .edit()
                                    .putString("generated_code", otpCode)
                                    .putString("otp_user_identifier", telemovel)
                                    .putLong("expiry_time", expiryTime)
                                    .apply()
                                enviaOtpParaApp2(context,otpCode)
                                navController.navigate("${Routes.escreverCodeVerify}/nas suas mensagens")
                            } else {
                                errorMessage = "Número de telemóvel não encontrado no sistema."
                            }
                        }
                        else {
                            errorMessage = "Método inválido ou não reconhecido."
                        }
                        }

                },
                enabled = when{
                    isEmailMethod -> email.isNotBlank()
                    isMobileMethod -> telemovel.isNotBlank()
                    else ->false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text("Enviar Código", color = Color.Black)
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            val focusManager = LocalFocusManager.current
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                   if(isEmailMethod) {
                        RequiredFieldLabel1(
                            text = "O seu email",
                            modifier = Modifier.align(Alignment.Start))
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                       Spacer(modifier = Modifier.height(16.dp))
                       if (errorMessage != null) {
                           Text(
                               text = errorMessage ?: "",
                               color = MaterialTheme.colorScheme.error,
                               style = MaterialTheme.typography.bodySmall,
                               modifier = Modifier
                                   .padding(start = 16.dp)
                                   .align(Alignment.Start)
                           )
                       }
                    }
                else if ( isMobileMethod) {
                       RequiredFieldLabel1(
                           text = "O seu nº de telemóvel",
                           modifier = Modifier.align(Alignment.Start)
                       )
                       OutlinedTextField(
                           value = telemovel,
                           onValueChange = {
                               if (it.length <= 9 && it.all { char -> char.isDigit() }) {
                                   telemovel = it} },

                           modifier = Modifier.fillMaxWidth(),
                           keyboardOptions = KeyboardOptions(
                               keyboardType = KeyboardType.Number,
                               imeAction = ImeAction.Done
                           ),
                           keyboardActions = KeyboardActions(
                               onDone = { focusManager.clearFocus() }
                           )
                       )
                       Spacer(modifier = Modifier.height(16.dp))
                       if (errorMessage != null) {
                           Text(
                               text = errorMessage ?: "",
                               color = MaterialTheme.colorScheme.error,
                               style = MaterialTheme.typography.bodySmall,
                               modifier = Modifier
                                   .padding(start = 16.dp)
                                   .align(Alignment.Start)
                           )
                       }
                   }
                    else {
                        Text("Método inválido ou não reconhecido.")
                    }
            }
        }
    }
}