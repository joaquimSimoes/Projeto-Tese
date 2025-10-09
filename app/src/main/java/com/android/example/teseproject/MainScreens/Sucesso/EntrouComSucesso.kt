package com.android.example.teseproject.MainScreens.Sucesso

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun EntrouComSucesso (navController: NavController, userName: String, email: String, method: String){
    val context = LocalContext.current
    LaunchedEffect(Unit ){
        stopTime(
            context=context,
            taskName = "EntrarConta",
            finalEmail = email,
            method = method
            )

    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bem Vindo $userName") },
                actions = {
                    Button(onClick = {navController.navigate(Routes.Home) },
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

                ){ Text(text = "Entrou com sucesso na sua conta!",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Black)}
            }
        }
    }
}

