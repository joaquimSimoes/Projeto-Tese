package com.android.example.teseproject.Entrar

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.android.example.teseproject.navegation.Routes
import com.android.example.teseproject.ui.theme.TeseProjectTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntrarConta(navController: NavController){
    val buttonModifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Entrar na Conta") },
                navigationIcon ={
                    IconButton(onClick = { navController.popBackStack()}) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.Black)
                    } },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    titleContentColor = Color.Black,
                )
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            Button(onClick = { navController.navigate("${Routes.entrarMudarPP}") },
                modifier = buttonModifier,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary))
            {
                Text(text = "Utilizar Palavra-Passe", color = Color.Black)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.navigate("${Routes.entrarMudarIP}") },
                modifier = buttonModifier,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary))
            {
                Text(text = "Utilizar Imagem-Passe", color = Color.Black)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.navigate("${Routes.entrarMudarOTP}") },
                modifier = buttonModifier,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary))
            {
                Text(text = "Utilizar Código de Uso Único", color = Color.Black)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.navigate("${Routes.entrarMudarFP}") },
                modifier = buttonModifier,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary))
            {
                Text(text = "Utilizar Impressão Digital", color = Color.Black)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.navigate("${Routes.entrarMudarRF}") },
                modifier = buttonModifier,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary))
            {
                Text(text = "Utilizar Reconhecimento Facial", color = Color.Black)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview6() {
    TeseProjectTheme {
        EntrarConta(navController = rememberNavController())
    }
}