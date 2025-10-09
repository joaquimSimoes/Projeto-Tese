package com.android.example.teseproject.Criar

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.android.example.teseproject.BasesDados.DataBase_All.AllUserViewModel
import com.android.example.teseproject.BasesDados.DataBase_All.All_Methods_Data
import com.android.example.teseproject.ExtraFunctions.ProgressBarStep
import com.android.example.teseproject.ExtraFunctions.stopTime
import com.android.example.teseproject.ViewModels.CriarContaViewModel
import kotlinx.coroutines.launch

@Composable
fun RequiredFieldLabel(text: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Text(text, style = MaterialTheme.typography.bodyMedium)
        Text(" *", color = MaterialTheme.colorScheme.error)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CriarConta(navController: NavController, viewModel: CriarContaViewModel, allUserViewModel: AllUserViewModel) {
    val firstName by viewModel.firstName.collectAsState()
    val lastName by viewModel.lastName.collectAsState()
    val email by viewModel.email.collectAsState()
    val mobile by viewModel.mobile.collectAsState()

    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$")
    val emailValid = email.matches(emailRegex)

    val encodedEmail = Uri.encode(email)
    var showErrors by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val allFieldsFilled =
        firstName.isNotBlank() && lastName.isNotBlank() && email.isNotBlank() && mobile.isNotBlank()
    val NineDigits = mobile.length == 9 && mobile.all { it.isDigit() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Criar conta na aplicação ") },
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
                ProgressBarStep(progress = 0.25f)
            }

        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 32.dp),
                //verticalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                val focusManager = LocalFocusManager.current
                RequiredFieldLabel(
                    text = "Primeiro Nome",
                    modifier = Modifier.align(Alignment.Start)
                )
                Text(
                    text = "Exemplo: Joaquim",
                    fontSize = 10.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Start)
                )
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { viewModel.putFirstName(it) },
                    isError = showErrors && firstName.isBlank(),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    )
                )
                if (showErrors && firstName.isBlank()) {
                    Text("Campo obrigatório", color = MaterialTheme.colorScheme.error)
                }
                Spacer(modifier = Modifier.height(16.dp))
                RequiredFieldLabel(text = " Apelido", modifier = Modifier.align(Alignment.Start))
                Text(
                    text = "Exemplo:  Simões",
                    fontSize = 10.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Start)
                )
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { viewModel.putLastName(it) },

                    isError = showErrors && lastName.isBlank(),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    )
                )
                if (showErrors && lastName.isBlank()) {
                    Text("Campo obrigatório", color = MaterialTheme.colorScheme.error)
                }
                Spacer(modifier = Modifier.height(16.dp))
                RequiredFieldLabel(
                    text = "Número de Telemóvel",
                    modifier = Modifier.align(Alignment.Start)
                )
                Text(
                    text = "Exemplo:  910910910",
                    fontSize = 10.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Start)
                )
                OutlinedTextField(
                    value = mobile,
                    onValueChange = {
                        if (it.length <= 9 && it.all { char -> char.isDigit() }) {
                            viewModel.putMobile(it)
                        }
                    },

                    isError = showErrors && !NineDigits,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    )
                )
                if (showErrors && mobile.isBlank()) {
                    Text("Campo obrigatório", color = MaterialTheme.colorScheme.error)
                }
                if (showErrors && mobile.isNotBlank() && !NineDigits) {
                    Text(
                        "Insira exatamente 9 dígitos", color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall, modifier = Modifier
                            .padding(start = 16.dp)
                            .align(
                                Alignment.Start
                            )
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                RequiredFieldLabel(text = "E-mail", modifier = Modifier.align(Alignment.Start))
                Text(
                    text = "Exemplo:  joaquim@gmail.com",
                    fontSize = 10.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Start)
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { viewModel.putEmail(it) },
                    isError = showErrors && (email.isBlank() || !emailValid),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    )
                )
                if (showErrors && email.isBlank()) {
                    Text("Campo obrigatório", color = MaterialTheme.colorScheme.error)
                } else if (showErrors && !emailValid) {
                    Text("Email inválido", color = MaterialTheme.colorScheme.error)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "* Campos obrigatórios", color = MaterialTheme.colorScheme.error)

            }

            Button(
                onClick = {
                    showErrors = true
                    if (allFieldsFilled && NineDigits && emailValid) {
                        scope.launch {
                            val existingUserByEmail = allUserViewModel.getAllUserByEmail(email)
                            val existingUserByMobile = allUserViewModel.getAllUserByMobile(mobile)

                            when {
                                existingUserByEmail != null -> {
                                    dialogMessage = "Esse e-mail já está associado a uma conta."
                                    showDialog = true
                                }

                                existingUserByMobile != null -> {
                                    dialogMessage =
                                        "Esse número de telemóvel já está associado a uma conta."
                                    showDialog = true
                                }

                                else -> {
                                    val newUser = All_Methods_Data(
                                        email = email,
                                        firstName = firstName,
                                        lastName = lastName,
                                        mobile = mobile
                                    )
                                    allUserViewModel.insertUser(newUser)
                                    navController.navigate("choose_method/$encodedEmail")


                                    stopTime(
                                        context = context,
                                        taskName = "CriarConta",
                                        finalEmail = email,
                                        method = "Dados"
                                    )
                                }
                            }
                        }
                    }
                },
                enabled = allFieldsFilled,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(50.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Seguinte", color = Color.Black)
            }

            if(showDialog){
                Dialog(onDismissRequest = { showDialog = false }) {
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        tonalElevation = 6.dp,
                        modifier = Modifier.padding(16.dp)
                    ){
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        ) {
                            Text(dialogMessage, color = Color.Red, textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { showDialog = false }) {
                                Text("Compreendi", color = Color.Black)
                            }
                        }
                    }
                }
            }

        }
    }
}
