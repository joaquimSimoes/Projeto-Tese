package com.android.example.teseproject.Criar.methods.OTP

import android.content.Context
import android.content.Intent
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.example.teseproject.BasesDados.DataBase_All.AllUserViewModel
import com.android.example.teseproject.BasesDados.DataBase_All.All_Methods_Data
import com.android.example.teseproject.ExtraFunctions.ProgressBarStep
import com.android.example.teseproject.ExtraFunctions.enviarEmail


fun generateOtpCode(): String {
    return (100000..999999).random().toString()
}
fun enviaOtpParaApp2(context: Context, code:String){
    val intent = Intent("com.android.example.sms_simulator.RECEIVE_CODE").apply{
        setPackage("com.android.example.sms_simulator")
        putExtra("otp_code", code)
    }
    context.sendBroadcast(intent)
    Log.d("OtpSender", "Sending OTP: $code to App2")
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Otp(navController: NavController,
        email: String,
        allUserViewModel: AllUserViewModel
        )
{
    var user by remember { mutableStateOf<All_Methods_Data?>(null) }

    LaunchedEffect(email ){
        user = allUserViewModel.getAllUserByEmail(email)
    }
    val buttonModifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(buildAnnotatedString { append("Criar conta na aplicação com ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){append("Código de Uso Único")}}) },
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                ProgressBarStep(progress = 0.75f)
            }

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
                Button(
                    onClick = {
                        val otpCode = generateOtpCode()
                        val expiryTime = System.currentTimeMillis() + 24 * 60 * 60 * 1000

                        context.getSharedPreferences("otp_prefs", Context.MODE_PRIVATE)
                            .edit()
                            .putString("generated_code", otpCode)
                            .putLong("expiry_time", expiryTime)
                            .apply()

                        enviarEmail(email, otpCode){success ->
                            if ( success ){
                                navController.navigate("otp_code/$email/email")
                            }else{
                                Log.e("Otp", "Erro ao enviar para email")
                            }

                        }
                    },
                    modifier = buttonModifier,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(text = "Quero receber o código no meu email", color = Color.Black, textAlign = TextAlign.Center)

                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val otpCode = generateOtpCode()
                        val expiryTime = System.currentTimeMillis() + 24 * 60 * 60 * 1000
                        context.getSharedPreferences("otp_prefs", Context.MODE_PRIVATE)
                            .edit()
                            .putString("generated_code", otpCode)
                            .putLong("expiry_time", expiryTime)
                            .apply()

                        enviaOtpParaApp2(context, otpCode)
                        navController.navigate("otp_code/$email/sms")
                    },
                    modifier = buttonModifier,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(text = "Quero receber o código no meu telemóvel", color = Color.Black, textAlign = TextAlign.Center)
                }

            }
        }
    }
}
