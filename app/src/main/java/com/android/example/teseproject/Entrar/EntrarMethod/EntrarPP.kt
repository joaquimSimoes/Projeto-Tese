package com.android.example.teseproject.Entrar.EntrarMethod

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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.example.teseproject.BasesDados.DataBase_All.AllDataBaseProvider
import com.android.example.teseproject.Criar.methods.PasswordField
import com.android.example.teseproject.Criar.methods.RequiredFieldLabel
import com.android.example.teseproject.ExtraFunctions.hashPassword
import com.android.example.teseproject.navegation.Routes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MudarEmailPP(navController: NavController){
    val context = LocalContext.current
    val db = AllDataBaseProvider.getDatabase(context)
    val userDao = db.allDao()

    val coroutineScope = rememberCoroutineScope()


    var passwordVisible by remember { mutableStateOf(false) }
    var passwordconf by remember { mutableStateOf("") }

    var mudaremail by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    var showErrors by remember { mutableStateOf(false) }
    var loginError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(buildAnnotatedString { append("Entrar na aplicação com ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){append("Palavra-Passe")}}) },

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
                    val inputHash = hashPassword(passwordconf)
                    val allFieldsFilled = passwordconf.isNotBlank() && mudaremail.isNotBlank()
                    if (allFieldsFilled){
                        coroutineScope.launch {
                            val user = withContext(Dispatchers.IO) {
                                userDao.getMethodByEmail(mudaremail)
                            }

                            if (user != null && user.hasPassword && user.passwordHash == inputHash) {
                                val userName = user.firstName
                                val email = user.email
                                navController.currentBackStackEntry?.savedStateHandle?.set("method", "password")
                                navController.navigate("${Routes.sucessoEntrar}/$userName/$email")
                            } else {
                                loginError = "Email ou palavra-passe incorretos."
                            }
                        }

                    }
                    else {
                        showErrors = true
                    }
                },
                enabled = passwordconf.isNotBlank() && mudaremail.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),

                ) {
                Text("Entrar na aplicação", color= Color.Black)
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
                onValueChange = { mudaremail = it },
                isError = showErrors && mudaremail.isBlank(),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            RequiredFieldLabel(text = "A sua Palavra-Passe",modifier = Modifier.align(Alignment.Start))
            PasswordField(
                label = "",
                password = passwordconf,
                onPasswordChange = { passwordconf = it },
                passwordVisible = passwordVisible,
                onPasswordVisibilityChange = { passwordVisible = it },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.clearFocus() }
                )
            )
            if (loginError != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(loginError!!, color = MaterialTheme.colorScheme.error)
            }

        }
    }
}