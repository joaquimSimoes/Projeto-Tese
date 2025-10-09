package com.android.example.teseproject

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.fragment.app.FragmentActivity
import com.android.example.teseproject.ExtraFunctions.TouchCounter
import com.android.example.teseproject.navegation.AppNavigation
import com.android.example.teseproject.ui.theme.TeseProjectTheme


class MainActivity : FragmentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (applicationContext.applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE != 0) {
            deleteDatabase("finger_user_database")
            deleteDatabase("facial_user_database")
            deleteDatabase("otp_user_database")
            deleteDatabase("user-database")
            deleteDatabase("image_user_database")

        }
        setContent {
            TeseProjectTheme { // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize()
                        .pointerInteropFilter {event ->
                            if(event.action == android.view.MotionEvent.ACTION_DOWN){
                                TouchCounter.increment()
                            }
                            false
                        },
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}
