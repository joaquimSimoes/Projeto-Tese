package com.android.example.teseproject.Criar.methods.imagePass

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.android.example.teseproject.BasesDados.DataBase_All.AllUserViewModel
import com.android.example.teseproject.BasesDados.DataBase_All.All_Methods_Data
import com.android.example.teseproject.ExtraFunctions.ProgressBarStep
import com.android.example.teseproject.ViewModels.ImageShuffleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePass(
    navController: NavController,
    email: String,
    allUserViewModel: AllUserViewModel,
    imageviewModel: ImageShuffleViewModel
){
    var user by remember { mutableStateOf<All_Methods_Data?>(null) }

    LaunchedEffect(email ){
        user = allUserViewModel.getAllUserByEmail(email)
    }
    val imageList = imageviewModel.imageList
    val selectedImages = remember { mutableStateListOf<Int>() }

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
                        if (selectedImages.size == 2){
                            navController.navigate(
                                "confirm_image_pass/${selectedImages[0]}/${selectedImages[1]}/$email"
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    enabled = selectedImages.size == 2
                ) {
                    Text("Seguinte", color= Color.Black)
                }
                Spacer(modifier = Modifier.height(5.dp))
                ProgressBarStep(progress = 0.75f)
            }

        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Spacer(Modifier.height(10.dp))

            AlinharTexto(text = "Escolha duas imagens de entre as imagens abaixo. Toque uma vez para selecionar," +
                    " toque outra vez para retirar a seleção. ", modifier = Modifier.align(Alignment.Start))

            Spacer(Modifier.height(10.dp))

            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val columns = 4
                val rows = 5
                val spacing = 6.dp
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
                            items(imageList) { imageRes ->
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

@Composable
fun AlinharTexto(text: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Text(text, style = MaterialTheme.typography.bodyMedium,
            fontSize = MaterialTheme.typography.titleMedium.fontSize)
    }
}

