package center.techostartup.raksadriver.data.local

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import center.techostartup.raksadriver.utils.AppConstants

class AppSharedPreference(application: Application) {

    private var sharedPreferences: SharedPreferences = application.getSharedPreferences(AppConstants.SHARED_PREFERENCE, Context.MODE_PRIVATE)

    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun get(key: String): String {
        return sharedPreferences.getString(key, "") ?: ""
    }

    fun insert(key: String, value: String) {
        editor.putString(key, value)
        editor.apply()
    }

    fun remove(key: String) {
        editor.remove(key)
        editor.apply()
    }
}