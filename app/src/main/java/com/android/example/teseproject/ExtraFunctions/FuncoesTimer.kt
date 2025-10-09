package com.android.example.teseproject.ExtraFunctions

import android.content.Context
import java.io.File

fun saveTime(context: Context){
    val prefs = context.getSharedPreferences("task_time", Context.MODE_PRIVATE)
    prefs.edit()
        .putLong("start_time", System.currentTimeMillis())
        .putString("participanteID", "P001")
        .apply()

    TouchCounter.reset()
}

fun stopTime(context: Context, taskName: String, finalEmail: String?=null, method: String){
    val prefs = context.getSharedPreferences("task_time", Context.MODE_PRIVATE)
    val startTime = prefs.getLong("start_time", -1)
    var participantID = prefs.getString("participantID", "P001") ?: "P001"

    if (finalEmail != null) {
        participantID = finalEmail // replace P001 with actual email
        prefs.edit().putString("participant_id", finalEmail).apply()
    }

    if (startTime != -1L) {
        val duration = System.currentTimeMillis() - startTime
        val touches = TouchCounter.getCount()
        saveLog(context, participantID, taskName, method ,duration, touches)
    }
}

fun saveLog(context: Context, participantID: String, taskName: String, method: String ,durationMs: Long, touches: Int){
    val file = File(context.getExternalFilesDir(null), "resultados.csv")
    if (!file.exists()) {
        file.appendText("participantID, taskName, method, durationMs, touches, timestamp\n")
    }

    val timestamp = System.currentTimeMillis()
    val line = "$participantID, $taskName, $method, $durationMs, $touches, $timestamp\n"
    file.appendText(line)
}