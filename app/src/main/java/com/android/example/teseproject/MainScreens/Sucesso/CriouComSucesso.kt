package com.android.example.teseproject.MainScreens.Sucesso

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.example.teseproject.ExtraFunctions.stopTime
import com.android.example.teseproject.navegation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CriouComSucesso (navController: NavController, firstName: String, email: String, method: String){
    val context = LocalContext.current
    var showConfirmDialog by remember { mutableStateOf(false) }
    var dialogAction: (() -> Unit)? by remember { mutableStateOf(null) }
    var dialogMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit){
        stopTime(
            context=context,
            taskName = "CriarConta",
            finalEmail = email,
            method = method
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bem Vindo $firstName") },
                actions = {
                    Button(
                        onClick = {
                            dialogMessage= "Tem a certeza de que quer sair?"
                            dialogAction={navController.navigate(Routes.Home)}
                            showConfirmDialog= true
                             },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        modifier = Modifier.padding(5.dp)) {
                        Text(text = "Sair", color= Color.Black)

                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    titleContentColor = Color.Black,
                )
            )
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
                Box(modifier = Modifier
                    .width(330.dp)
                    .height(180.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color(0xFFBDFB83))
                    .padding(16.dp)

                ){ Text(text = "Criou e entrou com sucesso na sua conta!",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Black)
                }

                Spacer(modifier = Modifier.height(32.dp))


                Button(
                    onClick = {
                        dialogMessage="Deseja mesmo adicionar outro método?"
                        dialogAction={
                            navController.navigate("choose_method/${email}") {
                                popUpTo(Routes.sucessoCriar) { inclusive = true }
                        }
                        }
                        showConfirmDialog= true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Adicionar outro método", color = Color.Black)
                }
            }
            if (showConfirmDialog) {
                AlertDialog(
                    onDismissRequest = { showConfirmDialog = false },
                    title = { Text("Confirmação") },
                    text = { Column {
                        Text(dialogMessage)
                        Spacer(Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            //modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(onClick = {
                                dialogAction?.invoke()
                                showConfirmDialog = false
                            }, modifier =Modifier.
                            padding(end = 30.dp, start = 20.dp)) { Text("Sim", color = Color.Black) }

                            Button(onClick = { showConfirmDialog = false }) {
                                Text("Não", color = Color.Black)
                            }
                        }
                    }
                    },
                    confirmButton = {},
                    dismissButton = {}

                )
            }
        }
    }
}

