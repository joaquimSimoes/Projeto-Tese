package com.android.example.teseproject.Entrar.EntrarMethod

import androidx.biometric.BiometricManager
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.android.example.teseproject.BasesDados.DataBase_All.AllDataBaseProvider
import com.android.example.teseproject.Criar.methods.RequiredFieldLabel
import com.android.example.teseproject.ExtraFunctions.buildPromptInfo
import com.android.example.teseproject.ExtraFunctions.createBiometricPrompt
import com.android.example.teseproject.navegation.Routes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MudarEmailRF(navController: NavController){
    val context = LocalContext.current
    val activity = context as FragmentActivity
    val biometricManager = BiometricManager.from(context)

    val db = AllDataBaseProvider.getDatabase(context)
    val userDao = db.allDao()
    val coroutineScope = rememberCoroutineScope()

    var mudaremail by remember { mutableStateOf("") }
    val allFieldsFilled = mudaremail.isNotBlank()
    var showErrors by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(buildAnnotatedString { append("Entrar na aplicação com ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){append("Reconhecimento Facial")}}) },
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
                    showErrors = false
                    errorMessage = null

                    coroutineScope.launch {val user = withContext(Dispatchers.IO){
                        userDao.getMethodByEmail(mudaremail)
                    }
                        if (user != null && user.hasFacial) {
                            val userName = user.firstName
                            val email = user.email
                            val prompt = createBiometricPrompt(
                                activity = activity,
                                onSuccess = {
                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        "method",
                                        "recFacial"
                                    )
                                    navController.navigate("${Routes.sucessoEntrar}/$userName/$email") },
                                    onError = {authError ->
                                        errorMessage = "Erro de autenticação: $authError"
                                    }
                            )
                            prompt.authenticate(
                                buildPromptInfo(
                                    title = "Autenticação por Reconhecimento Facial",
                                    subtitle = "Use o reconhecimento facial para entrar"
                                )
                            )
                        }
                        else {
                            showErrors = true
                            errorMessage = "Email não encontrado."
                        }
                    } },

                    enabled = allFieldsFilled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),

                ) {
                Text("Proceder para a leitura da expressão facial", color = Color.Black)
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Spacer(modifier = Modifier.height(16.dp))
            RequiredFieldLabel(
                text = "O seu email",
                modifier = Modifier.align(Alignment.Start)
            )
            OutlinedTextField(
                value = mudaremail,
                onValueChange = { mudaremail = it
                                if (showErrors){
                                    showErrors = false
                                    errorMessage = null}
                                },
                isError = showErrors && mudaremail.isBlank(),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )
            if (errorMessage != null) {
                Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}


