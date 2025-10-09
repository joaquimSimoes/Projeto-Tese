package com.android.example.teseproject.navegation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.example.teseproject.BasesDados.DataBase_All.AllDataBaseProvider
import com.android.example.teseproject.BasesDados.DataBase_All.AllUserViewModel
import com.android.example.teseproject.BasesDados.DataBase_All.AllUserViewModelFactory
import com.android.example.teseproject.Criar.ChooseMethod
import com.android.example.teseproject.Criar.CriarConta
import com.android.example.teseproject.Criar.methods.OTP.Otp
import com.android.example.teseproject.Criar.methods.OTP.OtpCode
import com.android.example.teseproject.Criar.methods.PasswordPage
import com.android.example.teseproject.Criar.methods.fingerPrint
import com.android.example.teseproject.Criar.methods.imagePass.ConfirmImagePass
import com.android.example.teseproject.Criar.methods.imagePass.ImagePass
import com.android.example.teseproject.Criar.methods.recFacial
import com.android.example.teseproject.Entrar.EntrarConta
import com.android.example.teseproject.Entrar.EntrarMethod.EOTP.MudarEmailOtp.ChangeEmailOtp
import com.android.example.teseproject.Entrar.EntrarMethod.EOTP.MudarEmailOtp.EscolherMudarParaOndeCode
import com.android.example.teseproject.Entrar.EntrarMethod.EOTP.MudarEmailOtp.EscreverCode
import com.android.example.teseproject.Entrar.EntrarMethod.MudarEmailFP
import com.android.example.teseproject.Entrar.EntrarMethod.MudarEmailImagePass
import com.android.example.teseproject.Entrar.EntrarMethod.MudarEmailPP
import com.android.example.teseproject.Entrar.EntrarMethod.MudarEmailRF
import com.android.example.teseproject.MainScreens.FalhaNasCredenciais
import com.android.example.teseproject.MainScreens.HomeScreen
import com.android.example.teseproject.MainScreens.Sucesso.CriouComSucesso
import com.android.example.teseproject.MainScreens.Sucesso.EntrouComSucesso
import com.android.example.teseproject.ViewModels.CriarContaViewModel
import com.android.example.teseproject.ViewModels.ImageShuffleViewModel


@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    val context = LocalContext.current
    val db = AllDataBaseProvider.getDatabase(context)
    val allDao = db.allDao()
    val allUserViewModel: AllUserViewModel = viewModel(factory = AllUserViewModelFactory(allDao))

    NavHost(navController = navController, startDestination = Routes.Home){
        composable(Routes.Home) { HomeScreen(navController) }
        composable(Routes.CriarConta) {
            val viewModel: CriarContaViewModel = viewModel()
            CriarConta(navController = navController, viewModel = viewModel, allUserViewModel = allUserViewModel) }

        composable( route = "${Routes.ChooseMethod}/{email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType }))
        { backStackEntry ->
            val email = Uri.decode(backStackEntry.arguments?.getString("email") ?: return@composable)
            ChooseMethod(navController = navController, email = email, allUserViewModel = allUserViewModel)
        }

        composable(Routes.EntrarConta) { EntrarConta(navController)}

        //////////////////////////////////////////////////////////////////////
        composable("password_page/{email}"){ backStackEntry ->
            val email = Uri.decode(backStackEntry.arguments?.getString("email") ?: return@composable)
            PasswordPage(navController = navController,
                email = email,
                allUserViewModel = allUserViewModel)
        }

        composable(route ="image_password_page/{email}"){ backStackEntry ->
            val email = Uri.decode(backStackEntry.arguments?.getString("email") ?: return@composable)
            val imageviewModel: ImageShuffleViewModel = viewModel()
            ImagePass(navController = navController, email = email, allUserViewModel= allUserViewModel ,imageviewModel = imageviewModel)
        }

        composable("confirm_image_pass/{image1}/{image2}/{email}"){backStackEntry ->
            val email = Uri.decode(backStackEntry.arguments?.getString("email") ?: return@composable)
            val shuffleViewModel: ImageShuffleViewModel = viewModel()
            val image1 = backStackEntry.arguments?.getString("image1")?.toIntOrNull() ?: return@composable
            val image2 = backStackEntry.arguments?.getString("image2")?.toIntOrNull() ?: return@composable
            ConfirmImagePass(
                navController = navController,
                email = email,
                selectedImages = listOf(image1, image2),
                allUserViewModel= allUserViewModel,
                shuffleViewModel = shuffleViewModel
            )
        }

        composable("fingerPrint/{email}"){ backStackEntry->
            val email = Uri.decode(backStackEntry.arguments?.getString("email") ?: return@composable)
           fingerPrint(navController = navController, email=email, allUserViewModel = allUserViewModel)
        }

        composable("recFacial/{email}"){ backStackEntry->
            val email = Uri.decode(backStackEntry.arguments?.getString("email") ?: return@composable)
            recFacial(navController = navController, email=email, allUserViewModel = allUserViewModel)
        }

        composable("otp/{email}"){ backStackEntry->
            val email = Uri.decode(backStackEntry.arguments?.getString("email") ?: return@composable)
            Otp(navController = navController,email=email, allUserViewModel= allUserViewModel)
        }


        composable("otp_code/{email}/{method}") { backStackEntry ->
            val method = backStackEntry.arguments?.getString("method") ?: ""
            val email = Uri.decode(backStackEntry.arguments?.getString("email") ?: return@composable)
            OtpCode(navController = navController, email = email, allUserViewModel = allUserViewModel ,method = method)
        }

        ///////////////////////////////////////////////////////////////////////////////////
        composable(
            route = "${Routes.escreverCodeInput}/{method}",
            arguments = listOf(navArgument("method"){type= NavType.StringType})) {
                backStackEntry ->
            val method = backStackEntry.arguments?.getString("method")?: ""

            ChangeEmailOtp(navController = navController, method = method, allUserViewModel = allUserViewModel )
        }

        composable(
            route = "${Routes.escreverCodeVerify}/{method}",
            arguments = listOf(navArgument("method") { type = NavType.StringType })
        ) { backStackEntry ->
            val method = backStackEntry.arguments?.getString("method") ?: ""
            EscreverCode(navController = navController, method = method, allUserViewModel = allUserViewModel)

        }

        composable(Routes.entrarMudarFP) {
            MudarEmailFP(navController)
        }

        composable(Routes.entrarMudarRF) {
            MudarEmailRF(navController)
        }



        composable(Routes.entrarMudarPP){ MudarEmailPP(navController) }
        composable(Routes.entrarMudarIP){ MudarEmailImagePass(navController) }
        composable(Routes.entrarMudarOTP){ EscolherMudarParaOndeCode(navController)}



        ///////////////////////////////////////////////////////////////////////////////////
        composable("sucessoCriar/{firstName}/{email}/{method}") { backStackEntry ->
            val firstName = backStackEntry.arguments?.getString("firstName") ?: ""
            val email = backStackEntry.arguments?.getString("email")?:"Email"
            val method = backStackEntry.arguments?.getString("method") ?: "unknown"

            CriouComSucesso(navController, firstName, email, method)
        }

        composable("${Routes.sucessoEntrar}/{userName}/{email}") { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: "Utilizador"
            val email = backStackEntry.arguments?.getString("email")?: "Email"

            val method = navController.previousBackStackEntry
                ?.savedStateHandle?.get<String>("method") ?: "unknown"
            EntrouComSucesso(navController, userName, email, method)
        }
        composable(Routes.falhaEntrar) { FalhaNasCredenciais(navController) }
    }
}