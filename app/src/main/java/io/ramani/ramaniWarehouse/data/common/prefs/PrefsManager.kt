package io.ramani.ramaniWarehouse.data.common.prefs

import android.content.Context
import android.content.SharedPreferences
import io.ramani.ramaniWarehouse.domainCore.prefs.Prefs

/**
 * Created by Amr on 9/8/17.
 */
open class PrefsManager(context: Context) : Prefs {
    private val sharedPrefs: SharedPreferences = context.getSharedPreferences(
        PrefsConstants.PREF_FILE_NAME,
        Context.MODE_PRIVATE
    )

    override var currentUser: String
        get() = sharedPrefs.getString(PrefsConstants.PREF_CURRENT_USER, null) ?: ""
        set(value) {
            sharedPrefs.edit().putString(PrefsConstants.PREF_CURRENT_USER, value).apply()
        }
    override var accessToken: String
        get() = sharedPrefs.getString(PrefsConstants.PREF_ACCESS_TOKEN, null) ?: ""
        set(value) {
            sharedPrefs.edit().putString(PrefsConstants.PREF_ACCESS_TOKEN, value).apply()
        }


    override val hasAccessToken: Boolean
        get() = contains(PrefsConstants.PREF_ACCESS_TOKEN)

    override var refreshToken: String
        get() = sharedPrefs.getString(PrefsConstants.PREF_REFRESH_TOKEN, null) ?: ""
        set(value) {
            sharedPrefs.edit().putString(PrefsConstants.PREF_REFRESH_TOKEN, value).apply()
        }


    private fun contains(key: String) = sharedPrefs.contains(key)

    private fun containsAndHasValue(key: String) =
        sharedPrefs.getString(key, "")?.isNotEmpty() == true
}