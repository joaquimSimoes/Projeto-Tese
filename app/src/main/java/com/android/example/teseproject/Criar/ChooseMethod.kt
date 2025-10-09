package com.android.example.teseproject.Criar

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.android.example.teseproject.BasesDados.DataBase_All.AllUserViewModel
import com.android.example.teseproject.BasesDados.DataBase_All.All_Methods_Data
import com.android.example.teseproject.ExtraFunctions.ProgressBarStep
import com.android.example.teseproject.ExtraFunctions.saveTime
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseMethod(navController: NavController,
                 email:String,
                 allUserViewModel: AllUserViewModel
){
    val buttonModifier = Modifier
        .fillMaxWidth()
        .height(56.dp)

    var user by remember { mutableStateOf<All_Methods_Data?>(null) }

    LaunchedEffect(email) {
        user = allUserViewModel.getAllUserByEmail(email)
        if (user != null) {
            Log.d("ChooseMethod", "User found in DB: $user")
        } else {
            Log.w("ChooseMethod", "User NOT found for email: $email")
        }
    }

    LaunchedEffect(Unit) {
        val allUsers = allUserViewModel.getAllUsers() // make this suspend in your ViewModel
        Log.d("DB_CONTENT", "All users: $allUsers")
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Criar conta na aplicação ") },
                navigationIcon ={
                    IconButton(onClick = { navController.popBackStack()}) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.Black)
                    } },
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
                ProgressBarStep(progress = 0.5f)
            }

        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()){
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 32.dp),
                //verticalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally)
            {
                Button(onClick = {
                    scope.launch{user?.let {
                    val (used, field) = allUserViewModel.isPasswordUsed(it.email, it.mobile)
                    if (used) {
                        dialogMessage = if (field == "email") {
                            "Este email já está associado a uma conta neste método."
                        } else {
                            "Este número de telemóvel já está associado a uma conta neste método."
                        }
                        showDialog = true
                    } else {
                        navController.navigate("password_page/${Uri.encode(email)}")
                        saveTime(context)
                    }
                }}

                },
                    modifier = buttonModifier,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary))
                {
                    Text(text = "Palavra-Passe", color = Color.Black)
                }
                Text(text = "Criar uma palavra chave para entrar na conta", textAlign = TextAlign.Center)

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    scope.launch{user?.let {
                        val (used, field) = allUserViewModel.isImagePasswordUsed(it.email, it.mobile)
                        if (used) {
                            dialogMessage = if (field == "email") {
                                "Este email já está associado a uma conta neste método."
                            } else {
                                "Este número de telemóvel já está associado a uma conta neste método."
                            }
                            showDialog = true
                        } else {
                            navController.navigate("image_password_page/${Uri.encode(email)}")
                            saveTime(context)
                        }
                    }
                    }

                },
                    modifier = buttonModifier,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary))
                {
                    Text(text = "Imagem-Passe", color = Color.Black)
                }
                Text(text = "Escolher duas imagens para entrar na conta", textAlign = TextAlign.Center)

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { scope.launch{user?.let {
                        val (used, field) = allUserViewModel.isOTPUsed(it.email, it.mobile)
                        if (used) {
                            dialogMessage = if (field == "email") {
                                "Este email já está associado a uma conta neste método."
                            } else {
                                "Este número de telemóvel já está associado a uma conta neste método."
                            }
                            showDialog = true
                        } else {
                            navController.navigate("otp/${Uri.encode(email)}")
                            saveTime(context)
                        }
                    }}

                },
                    modifier = buttonModifier,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary))
                {
                    Text(text = "Código de Uso Único", color = Color.Black)
                }
                Text(text = "Códigos enviados para telemóvel ou e-mail", textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { scope.launch{user?.let {
                        val (used, field) = allUserViewModel.isFingerPrintUsed(it.email, it.mobile)
                        if (used) {
                            dialogMessage = if (field == "email") {
                                "Este email já está associado a uma conta neste método."
                            } else {
                                "Este número de telemóvel já está associado a uma conta neste método."
                            }
                            showDialog = true
                        } else {
                            navController.navigate("fingerPrint/${Uri.encode(email)}")
                            saveTime(context)
                        }
                    }}

                },
                    modifier = buttonModifier,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary))
                {
                    Text(text = "Impressão Digital", color = Color.Black)
                }
                Text(text = "Uso de impressão digital para entrar na conta", textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {scope.launch{user?.let {
                    val (used, field) = allUserViewModel.isFacialUsed(it.email, it.mobile)
                    if (used) {
                        dialogMessage = if (field == "email") {
                            "Este email já está associado a uma conta neste método."
                        } else {
                            "Este número de telemóvel já está associado a uma conta neste método."
                        }
                        showDialog = true
                    } else {
                        navController.navigate("recFacial/${Uri.encode(email)}")
                        saveTime(context)
                    }
                }}

                },
                    modifier = buttonModifier,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary))
                {
                    Text(text = "Reconhecimento Facial", color = Color.Black)
                }
                Text(text = "Uso da sua cara para entrar na conta", textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(16.dp))

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
                                    Text("Compreendi", color= Color.Black)
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}
