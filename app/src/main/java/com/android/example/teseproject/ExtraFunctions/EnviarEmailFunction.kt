package com.android.example.teseproject.ExtraFunctions

import android.os.Handler
import android.os.Looper
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

fun enviarEmail(email: String, code: String, onResult:(Boolean) -> Unit){
    val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val json = """
        {"to": "$email",
        "code": "$code"
        }
    """.trimIndent()

    val url = "https://emailtese.onrender.com/send-email"
    val requestBody = json.toRequestBody("application/json".toMediaType())

    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .build()

    println("DEBUG: Sending POST to $url with body: $json")

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException){
            e.printStackTrace()
            onResult(false)
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                if (response.isSuccessful){
                    println("Email envio sucesso código: ${response.code}")
                    Handler(Looper.getMainLooper()).post {
                        onResult(true)
                    }
                }
                else {
                    println("Email failed with status code: ${response.code}")
                    println("Response message: ${response.message}")
                    println("Error body: ${response.body?.string()}")
                    onResult(false)
                }
            }
        }
    })
}