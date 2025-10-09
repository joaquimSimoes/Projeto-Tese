package com.android.example.teseproject.Criar.methods



import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
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

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun fingerPrint(navController: NavController,
                email: String,
                allUserViewModel: AllUserViewModel
) {
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
            errorMessage = "Impressão Digital não configurada."
        }
    }

    LaunchedEffect(Unit) {
        val bioManager = BiometricManager.from(context)
        when (bioManager.canAuthenticate(getAvailableAuthenticators())) {
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                showEnrollDialog = true
            }

            BiometricManager.BIOMETRIC_SUCCESS -> {
                shouldStartBiometric= true}
            else -> {
                errorMessage = "Erro na verificação da autenticação biométrica."
            }
        }
    }
    LaunchedEffect(shouldStartBiometric) {
        if (shouldStartBiometric && fragmentActivity != null) {
            val prompt = createBiometricPrompt(
                activity = fragmentActivity,
                        onSuccess = {
                            val currentUser = user ?: return@createBiometricPrompt
                            val updatedUser = currentUser.copy(
                                hasFingerprint = true
                            )
                            coroutineScope.launch(Dispatchers.IO) {
                                allUserViewModel.updateUser(updatedUser)
                            }
                            navController.currentBackStackEntry?.savedStateHandle?.set("method", "fingerprint")
                            navController.navigate("sucessoCriar/${currentUser.firstName}/$email/fingerprint")
                        },
                    onError = {
                        errorMessage = it
                    }
                )
                prompt.authenticate(
                    buildPromptInfo(
                        title = "Autenticação por Impressão Digital.",
                        subtitle = "Use a Impressão Digital para criar a conta."
                    )
                )
            }
    }

    if (showEnrollDialog){
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
                        "Impressão Digital",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Ainda não configurou a Impressão Digital. \n Deseja configurar?")

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth())
                    {
                        TextButton(onClick = {
                            showEnrollDialog = false
                            val enrollIntent = if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                                Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                                    putExtra(
                                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                                        BiometricManager.Authenticators.BIOMETRIC_STRONG
                                    )
                                }
                            }else {
                               Intent(Settings.ACTION_SECURITY_SETTINGS)
                            }
                            biometricEnrollerLauncher.launch(enrollIntent)
                        }) {
                            Text("Sim", color = Color.Black)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(onClick = {
                            showEnrollDialog = false
                            errorMessage = "Impressão Digital por configurar"
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
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){append("Impressão Digital")}}) },
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
            Text(text = "Por favor coloque o seu dedo na área designada.")
            errorMessage?.let {
                Spacer(Modifier.height(20.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

