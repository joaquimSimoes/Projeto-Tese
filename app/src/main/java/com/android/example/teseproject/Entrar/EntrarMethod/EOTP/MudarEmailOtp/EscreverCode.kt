package com.android.example.teseproject.Entrar.EntrarMethod.EOTP.MudarEmailOtp


import android.content.Context
import android.util.Log
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
import com.android.example.teseproject.Criar.methods.OTP.enviaOtpParaApp2
import com.android.example.teseproject.Criar.methods.OTP.generateOtpCode
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
fun EscreverCode(navController: NavController, method: String, allUserViewModel: AllUserViewModel)
{
    val buttonModifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
    var codigo by remember { mutableStateOf("") }
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("otp_prefs", Context.MODE_PRIVATE)
    var savedCode by remember { mutableStateOf(sharedPreferences.getString("generated_code", null)) }
    var expiryTime by remember { mutableStateOf(sharedPreferences.getLong("expiry_time", 0)) }
    val identifier = sharedPreferences.getString("otp_user_identifier", null)


    var showError by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
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
                val methodText = when (method.lowercase()) {
                    "no seu email", "email" -> "no seu email"
                    "nas suas mensagens", "sms", "telemovel" -> "nas suas mensagens"
                    else -> "na aplicação"
                }

                RequiredFieldLabel1(
                    text = "Por favor insira o código que recebeu $methodText",
                    modifier = Modifier.align(Alignment.Start)
                )

                OutlinedTextField(
                    value = codigo,
                    onValueChange = { InputCode ->
                        if(InputCode.all {it.isDigit()}) {
                            codigo = InputCode
                            showError = false
                        }},
                    isError = showError,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    )
                )

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = {
                        if (savedCode == null || System.currentTimeMillis() > expiryTime) {
                            showError = true
                            Toast.makeText(context, "O código expirou. Gere um novo.", Toast.LENGTH_SHORT).show()
                            sharedPreferences.edit().remove("generated_code").remove("expiry_time").apply()
                            codigo = ""
                            return@Button
                        }

                        if (codigo== savedCode && identifier != null){
                            scope.launch {
                                val user = if(method.lowercase().contains("email")){
                                    allUserViewModel.getAllUserByEmail(identifier)
                                } else {
                                    allUserViewModel.getAllUserByMobile(identifier)
                                }
                                if(user != null && user.hasOtp){
                                    val name = user.firstName
                                    val email = user.email
                                    Log.d("OTP", "Login successful! Nome: $name")

                                    sharedPreferences.edit().remove("generated_code").remove("expiry_time").apply()
                                    navController.currentBackStackEntry?.savedStateHandle?.set("method", method)
                                    navController.navigate("${Routes.sucessoEntrar}/$name/$email")
                                } else{
                                    showError = true
                                    Toast.makeText(context, "Usuário não encontrado.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        else{
                            showError = true
                            Toast.makeText(context, "Código incorreto. Tente novamente.", Toast.LENGTH_SHORT).show()
                            Log.d("Código de uso único", "Código incorreto!")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                ) {
                    Text("Verificar Código", color= Color.Black)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(onClick = { scope.launch {
                    if (identifier != null) {
                        val newCode = generateOtpCode()
                        val newExpiry = System.currentTimeMillis() + 24 * 60 * 60 * 1000
                        sharedPreferences.edit()
                            .putString("generated_code", newCode)
                            .putLong("expiry_time", newExpiry)
                            .apply()

                        savedCode = newCode
                        expiryTime = newExpiry

                        if (method.lowercase().contains("email")) {
                            enviarEmail(identifier, newCode) { success ->
                                if (success) {
                                    Toast.makeText(
                                        context,
                                        "Um novo código foi enviado para o seu email.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Falha ao enviar o código para o email.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            enviaOtpParaApp2(context, newCode)
                            Toast.makeText(
                                context,
                                "Um novo código foi enviado por SMS.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Não foi possível gerar um novo código. Volte à tela anterior.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                },
                    modifier = Modifier.fillMaxWidth()
                        .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                ) {
                    Text("Gerar novo código", color = Color.Black)
                }
            }
        }
    }
}
