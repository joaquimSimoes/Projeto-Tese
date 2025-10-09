package com.android.example.teseproject.Criar.methods.imagePass

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.android.example.teseproject.BasesDados.DataBase_All.AllUserViewModel
import com.android.example.teseproject.BasesDados.DataBase_All.All_Methods_Data
import com.android.example.teseproject.ExtraFunctions.ProgressBarStep
import com.android.example.teseproject.ExtraFunctions.hashImageId
import com.android.example.teseproject.ViewModels.ImageShuffleViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmImagePass(
    navController: NavController,
    email: String,
    selectedImages: List<Int>,
    allUserViewModel: AllUserViewModel,
    shuffleViewModel: ImageShuffleViewModel
) {

    var user by remember { mutableStateOf<All_Methods_Data?>(null) }
    val imageList =shuffleViewModel.imageList
    LaunchedEffect(email) {
        user = allUserViewModel.getAllUserByEmail(email)
    }

    val reselectedImages = remember { mutableStateListOf<Int>() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val isValid = reselectedImages.toSet() == selectedImages.toSet()
    var showError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(buildAnnotatedString { append("Criar conta na aplicação com ")
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        val currentUser = user ?: return@Button
                        if (isValid){
                            val updatedUser = currentUser.copy(
                                image1 = hashImageId(selectedImages[0]),
                                image2 = hashImageId(selectedImages[1]),
                                hasImagePassword = true
                            )
                            scope.launch {
                                allUserViewModel.updateUser(updatedUser)
                                navController.currentBackStackEntry?.savedStateHandle?.set("method", "imagePass")
                                navController.navigate("sucessoCriar/${currentUser.firstName}/$email/imagePass")
                            }
                        }else{
                            showError = true
                        }
                    },
                    enabled = reselectedImages.size == 2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                ) {
                    Text("Criar Conta", color= Color.Black)
                }
                Spacer(modifier = Modifier.height(5.dp))
                ProgressBarStep(progress = 1f)
            }

        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text("Selecione as mesmas imagens para confirmar.")
            Spacer(modifier = Modifier.height(10.dp))

            if (showError) {
                Dialog(onDismissRequest = { }){
                    Surface (
                        shape = MaterialTheme.shapes.medium,
                        tonalElevation = 6.dp,
                        modifier = Modifier.padding(16.dp)
                    ){
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        ) {
                            Text(text = "As imagens não correspondem. Tente novamente.",
                                color = Color.Red,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()){
                                Spacer(modifier = Modifier.height(8.dp))
                                TextButton( onClick = {
                                    showError = false
                                },
                                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                                ){ Text(text = "Tentar outra vez.", color= Color.Black)}
                            }
                        }
                    }
                }
            }

            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val columns = 4
                val rows = 5
                val spacing = 6.dp
                val horizontalSpacing = spacing * (columns - 1)
                val verticalSpacing = spacing * (rows - 1)
                val imageSize = ((maxWidth - horizontalSpacing) / columns)
                    .coerceAtMost((maxHeight - verticalSpacing) / rows)

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    contentPadding = PaddingValues(spacing),
                    verticalArrangement = Arrangement.spacedBy(spacing),
                    horizontalArrangement = Arrangement.spacedBy(spacing),
                    modifier = Modifier.fillMaxSize()
                ) {
                            items(imageList) { imageRes ->
                                val isSelected = imageRes in reselectedImages

                                AsyncImage(
                                    model = imageRes,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(imageSize)
                                        .border(
                                            width = if (isSelected) 3.dp else 1.dp,
                                            color = if (isSelected) Color.Green else Color.Black
                                        )
                                        .clickable {
                                            if (isSelected) {
                                                reselectedImages.remove(imageRes)
                                            } else if (reselectedImages.size < 2) {
                                                reselectedImages.add(imageRes)
                                            }
                                        }
                                )
                            }
                        }
                    }
                }
            }
        }



