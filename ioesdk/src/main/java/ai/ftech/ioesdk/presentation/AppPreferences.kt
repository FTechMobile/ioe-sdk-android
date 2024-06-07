package ai.ftech.ioesdk.presentation

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

object AppPreferences {
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences
    private lateinit var gson: Gson
    private val TOKEN_KEY = "TOKEN_KEY"
    private val FTECH_KEY = "FTECH_KEY"
    private val APP_ID = "APP_ID"
    private val REQUEST_ID = "REQUEST_ID"

    fun init(context: Context) {
        preferences = context.getSharedPreferences(context.packageName, MODE)
        gson = Gson()
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    private inline fun SharedPreferences.commit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.commit()
    }

    var token: String?
        get() = preferences.getString(TOKEN_KEY, "")
        set(value) = preferences.edit {
            it.putString(TOKEN_KEY, value)
        }

    var ftechKey: String?
        get() = preferences.getString(FTECH_KEY, "")
        set(value) = preferences.edit {
            it.putString(FTECH_KEY, value)
        }
    var appId: String?
        get() = preferences.getString(APP_ID, "")
        set(value) = preferences.edit {
            it.putString(APP_ID, value)
        }

    var requestId: String?
        get() = preferences.getString(REQUEST_ID, "")
        set(value) = preferences.edit {
            it.putString(REQUEST_ID, value)
        }
}
