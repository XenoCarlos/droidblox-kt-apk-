package com.drake.droidblox.backend.sharedprefs.playsession

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.drake.droidblox.backend.sharedprefs.playsession.models.PlaySession
import kotlinx.serialization.json.Json

private const val TAG = "DBPlaySessionsManager"

class PlaySessionsManager(
    context: Context
) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("playsessions", Context.MODE_PRIVATE)

    var recentGamesPlayed: List<PlaySession>
        get() {
            val jsonString = sharedPreferences.getString("recentGamesPlayed", null)
            if (jsonString != null) {
                try {
                    return Json.decodeFromString<List<PlaySession>>(jsonString)
                } catch (e: Exception) {
                    Log.e(TAG, "Something went wrong while getting a list of games played!; ${e.message}")
                    return emptyList()
                }
            } else {
               return emptyList()
            }
        }
        set(value) {
            val jsonString = Json.encodeToString(value)
            sharedPreferences.edit { putString("recentGamesPlayed", jsonString) }
        }

    fun addPlaySession(playSession: PlaySession) {
        Log.d(TAG, "Adding play session: $playSession")
        val currentGames = recentGamesPlayed.toMutableList()
        currentGames.add(playSession)
        recentGamesPlayed = currentGames.toList()
    }
}