package com.android.example.teseproject.Entrar.EntrarMethod

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.android.example.teseproject.BasesDados.DataBase_All.AllDataBaseProvider
import com.android.example.teseproject.Criar.methods.RequiredFieldLabel
import com.android.example.teseproject.Criar.methods.imagePass.AlinharTexto
import com.android.example.teseproject.ExtraFunctions.hashImageId
import com.android.example.teseproject.ViewModels.ImageShuffleViewModel
import com.android.example.teseproject.navegation.Routes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MudarEmailImagePass(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ImageShuffleViewModel = viewModel()
){
    val imageList = viewModel.imageList

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val db = AllDataBaseProvider.getDatabase(context)
    val imageDao = db.allDao()

    val selectedImages = remember { mutableStateListOf<Int>() }
    var mudaremail by remember { mutableStateOf("") }
    val allFieldsFilled = mudaremail.isNotBlank() && selectedImages.size == 2
    var showErrors by remember { mutableStateOf(false) }
    var loginFailed by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(buildAnnotatedString { append("Entrar na aplicação com ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){append("Imagem-Passe")}}) },

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

                    if (allFieldsFilled) {
                        coroutineScope.launch {
                            withContext(Dispatchers.IO) {
                                val user = imageDao.getMethodByEmail(mudaremail)
                                val hashedSelection = setOf(
                                    hashImageId(selectedImages[0]),
                                    hashImageId(selectedImages[1])
                                )

                                val storedHashes = user?.let { setOf(it.image1, it.image2) }

                                withContext(Dispatchers.Main) {
                                    if (storedHashes != null && user.hasImagePassword && storedHashes == hashedSelection) {
                                        val userName = user.firstName
                                        val email = user.email
                                        navController.currentBackStackEntry?.savedStateHandle?.set("method", "imagePass")
                                        navController.navigate("${Routes.sucessoEntrar}/$userName/$email")
                                    } else {
                                        loginFailed = true
                                    }
                                }
                            }
                        }
                    } else {
                    showErrors = true
                }
            },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = allFieldsFilled
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
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Spacer(Modifier.height(16.dp))

            RequiredFieldLabel(
                text = "O seu email",
                modifier = Modifier.align(Alignment.Start)
            )
            OutlinedTextField(
                value = mudaremail,
                onValueChange = {
                    mudaremail = it
                    showErrors = false
                    loginFailed = false },

                isError = showErrors && mudaremail.isBlank(),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )

            Spacer(modifier = Modifier.height(13.dp))

            AlinharTexto(text = "Escolha as suas imagens-passe", modifier = Modifier.align(Alignment.Start))
            if (loginFailed){
                Dialog(onDismissRequest = { /*TODO*/ }) {
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        tonalElevation = 6.dp,
                        modifier = Modifier.padding(16.dp)
                        ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        ) {
                            Text(text = "Email ou imagens incorretas! Tente novamente.",
                                color = Color.Red,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()){
                                Spacer(modifier = Modifier.height(8.dp))
                                TextButton(onClick = {
                                    loginFailed = false },
                                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                                ) {
                                    Text(text = "Tentar outra vez.", color = Color.Black)
                                }
                            }
                        }
                    }
                }
            }


            Spacer(modifier = Modifier.height(10.dp))
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val columns = 4
                val rows = 5
                val spacing = 8.dp
                val horizontalspacing = spacing * ( columns - 1 )
                val verticalspacing = spacing * (rows - 1 )
                val imageSizes = ((maxWidth - horizontalspacing) /columns)
                    .coerceAtMost((maxHeight - verticalspacing)/ rows)

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    contentPadding = PaddingValues(spacing),
                    verticalArrangement = Arrangement.spacedBy(spacing),
                    horizontalArrangement = Arrangement.spacedBy(spacing),
                    modifier = Modifier.fillMaxSize()
            ){

                        items(imageList){imageRes ->
                            val isSelected = imageRes in selectedImages

                            AsyncImage(
                                model = imageRes,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(imageSizes)
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) Color.Green else Color.Black
                                    )
                                    .clickable {
                                        loginFailed = false
                                        if (isSelected) {
                                            selectedImages.remove(imageRes)
                                        } else if (selectedImages.size < 2) {
                                            selectedImages.add(imageRes)
                                        }
                                    }
                            )


                        }
                    }
                }
            }

        }
    }


