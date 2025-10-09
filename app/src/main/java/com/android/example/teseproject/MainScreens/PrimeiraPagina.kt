package com.android.example.teseproject.MainScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.example.teseproject.ExtraFunctions.saveTime
import com.android.example.teseproject.navegation.Routes


@Composable
fun HomeScreen(navController: NavController){
    val buttonModifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
    val context = LocalContext.current
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = {
            saveTime(context)
            navController.navigate(Routes.CriarConta) },
            modifier = buttonModifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary))
        {
            Text(text = "Criar Conta", color = Color.Black)
        }
        Text(text = "Caso ainda não tenha uma conta crie uma", textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            saveTime(context)
            navController.navigate(Routes.EntrarConta) },
            modifier = buttonModifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary))
        {
            Text(text = "Entrar na Minha Conta", color = Color.Black)
            
        }
        Text(text = "Caso já tenha uma conta entre nela", textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(32.dp))


    }
}

