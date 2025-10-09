package com.android.example.teseproject.Criar.methods

import android.app.Activity
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.android.example.teseproject.BasesDados.DataBase_All.AllUserViewModel
import com.android.example.teseproject.BasesDados.DataBase_All.All_Methods_Data
import com.android.example.teseproject.ExtraFunctions.ProgressBarStep
import com.android.example.teseproject.ExtraFunctions.buildPromptInfo
import com.android.example.teseproject.ExtraFunctions.createBiometricPrompt
import com.android.example.teseproject.ExtraFunctions.getAvailableAuthenticators
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun recFacial(navController: NavController,
              email: String,
              allUserViewModel: AllUserViewModel)
{
    var user by remember { mutableStateOf<All_Methods_Data?>(null) }

    LaunchedEffect(email) {
        user = allUserViewModel.getAllUserByEmail(email)
    }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val fragmentActivity = context as? FragmentActivity

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showEnrollDialog by remember { mutableStateOf(false) }
    var shouldStartBiometric by remember { mutableStateOf(false) }

    val biometricEnrollerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            shouldStartBiometric = true
        } else {
            errorMessage = "Reconhecimento Facial não configurado."
        }
    }


    LaunchedEffect(Unit) {
        val bioManager = BiometricManager.from(context)
        val canAuth = bioManager.canAuthenticate(getAvailableAuthenticators())
        when (canAuth) {
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->{
                showEnrollDialog = true
            }

            BiometricManager.BIOMETRIC_SUCCESS -> {
                shouldStartBiometric= true}
            else -> {
                errorMessage = "Erro na verificação da autenticação biométrica."
            }
        }
    }

    LaunchedEffect(shouldStartBiometric){
        if(shouldStartBiometric && fragmentActivity != null){

                val prompt = createBiometricPrompt(
                    activity = fragmentActivity,
                    onSuccess = {
                        val currentUser = user ?: return@createBiometricPrompt
                        val updatedUser = currentUser.copy(
                            hasFacial = true
                        )
                        coroutineScope.launch(Dispatchers.IO) {
                            allUserViewModel.updateUser(updatedUser)
                        }
                        navController.currentBackStackEntry?.savedStateHandle?.set("method", "recFacial")
                        navController.navigate("sucessoCriar/${currentUser.firstName}/$email/recFacial")
                    },
                    onError = {
                        errorMessage = it
                    }
                )
                prompt.authenticate(
                    buildPromptInfo(
                    title = "Autenticação por Reconhecimento Facial.",
                    subtitle = "Use o Reconhecimento Facial para criar a conta.")
                )
        }
    }

    if (showEnrollDialog) {
        Dialog(onDismissRequest = { }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 6.dp,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                )
                {
                    Text(
                        "Reconhecimento Facial",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Ainda não configurou o Reconhecimento Facial. \n Deseja configurar?")

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth())
                    {
                        TextButton(onClick = {
                            showEnrollDialog = false
                            val enrollIntent = if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R){
                                Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                                putExtra(
                                    Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                                    BiometricManager.Authenticators.BIOMETRIC_STRONG or
                                            BiometricManager.Authenticators.BIOMETRIC_WEAK or
                                            BiometricManager.Authenticators.DEVICE_CREDENTIAL
                                )
                            }
                            }else{
                                Intent(Settings.ACTION_SECURITY_SETTINGS)
                            }
                            biometricEnrollerLauncher.launch(enrollIntent)
                        }) {
                            Text("Sim", color= Color.Black)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(onClick = {
                            showEnrollDialog = false
                            showEnrollDialog = false
                            errorMessage = "Reconhecimento Facial por configurar."
                        }) {
                            Text("Cancelar", color= Color.Black)
                        }

                    }
                }
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(buildAnnotatedString { append("Criar conta na aplicação com ")
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                ProgressBarStep(progress = 0.25f)
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
            Text(text = "Acerte a sua cabeça com a área designada e não mova a cabeça.")
            errorMessage?.let {
                Spacer(Modifier.height(20.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
