package com.drake.droidblox.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.drake.droidblox.logger.AndroidLogger
import com.drake.droidblox.sharedprefs.scopes.EditFFlagScope
import kotlinx.serialization.json.Json

/*
fun fact that you might know already
fast flags that are labeled FFlag, FInt, DFFlag, DFInt, etc
can be set as strings

example:
FFlagDebugSkyGray can be either set as the following:
1. true
2. "true"
3. "True"
4. false
5. "false"
6. "False"
etc
 */
class FastFlagsManager(
    context: Context
) {
    companion object {
        private const val TAG = "DBFFlagManager"
        private val logger = AndroidLogger
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("fflags", Context.MODE_PRIVATE)

    val rawFFlags: String?
        get() = sharedPreferences.getString("fflags", null)
    var fflags: MutableMap<String, String>
        get() {
            val jsonString = rawFFlags
            var toReturn: MutableMap<String, String> = mutableMapOf()
            if (jsonString != null) {
                try {
                    toReturn = Json.decodeFromString<MutableMap<String, String>>(jsonString)
                } catch (e: Exception) {
                    logger.e(TAG, "Something went wrong while getting fast flags!; ${e.message}")
                    toReturn = mutableMapOf()
                }
            }
            logger.d(TAG, "get = $toReturn")
            return toReturn
        }
        set(value) {
            val jsonString = Json.encodeToString(value)
            logger.d(TAG, "set = $jsonString")
            sharedPreferences.edit { putString("fflags", jsonString) }
        }

    fun getFFlag(key: String): String? = fflags[key]
    fun edit(block: (EditFFlagScope.() -> Unit)) {
        val currentFFlags = fflags
        val scope = EditFFlagScope(currentFFlags)
        scope.block()
        currentFFlags.putAll(scope.fflagsToEdit)
        fflags = currentFFlags
    }
}