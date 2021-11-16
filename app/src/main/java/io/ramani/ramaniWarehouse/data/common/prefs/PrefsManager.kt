package io.ramani.ramaniWarehouse.data.common.prefs

import android.content.Context
import android.content.SharedPreferences
import io.ramani.ramaniWarehouse.core.domain.prefs.Prefs

/**
 * Created by Amr on 9/8/17.
 */
open class PrefsManager(context: Context) : Prefs {
    private val sharedPrefs: SharedPreferences = context.getSharedPreferences(
        PrefsConstants.PREF_FILE_NAME,
        Context.MODE_PRIVATE
    )

    override var selectedLanguageId: Int
        get() = sharedPrefs.getInt(PrefsConstants.SELECTED_LANGUAGE_CODE, languageID)
        set(value) {
            sharedPrefs.edit().putInt(PrefsConstants.SELECTED_LANGUAGE_CODE, value).apply()
        }


    override var language: String
        get() = sharedPrefs.getString(PrefsConstants.LANGUAGE, null) ?: "en"
        set(value) {
            sharedPrefs.edit().putString(PrefsConstants.LANGUAGE, value).apply()
        }

    override var languageDisplay: String
        get() = sharedPrefs.getString(PrefsConstants.LANGUAGE_NAME, null) ?: "English"
        set(value) {
            sharedPrefs.edit().putString(PrefsConstants.LANGUAGE_NAME, value).apply()
        }

    override var languageID: Int
        get() = sharedPrefs.getInt(PrefsConstants.LANGUAGE_CODE, 1)
        set(value) {
            sharedPrefs.edit().putInt(PrefsConstants.LANGUAGE_CODE, value).apply()
        }

    override var currentUser: String
        get() = sharedPrefs.getString(PrefsConstants.PREF_CURRENT_USER_ID, null) ?: ""
        set(value) {
            sharedPrefs.edit().putString(PrefsConstants.PREF_CURRENT_USER_ID, value).apply()
        }

    override var currentInstitutionId: String
        get() = sharedPrefs.getString(PrefsConstants.PREF_CURRENT_INST_ID, null) ?: ""
        set(value) {
            sharedPrefs.edit().putString(PrefsConstants.PREF_CURRENT_INST_ID, value).apply()
        }

    override var isCurrentInstituteSelected: Boolean
        get() = sharedPrefs.getBoolean(PrefsConstants.PREF_CURRENT_INST_SELECTED, false)
        set(value) {
            sharedPrefs.edit().putBoolean(PrefsConstants.PREF_CURRENT_INST_SELECTED, value).apply()
        }

    override var accessToken: String
        get() = sharedPrefs.getString(PrefsConstants.PREF_ACCESS_TOKEN, null) ?: ""
        set(value) {
            sharedPrefs.edit().putString(PrefsConstants.PREF_ACCESS_TOKEN, value).apply()
        }

    override var lastUserEmail: String
        get() = sharedPrefs.getString(PrefsConstants.PREF_LAST_USER_EMAIL, null) ?: ""
        set(value) {
            sharedPrefs.edit().putString(PrefsConstants.PREF_LAST_USER_EMAIL, value).apply()
        }

    override val hasAccessToken: Boolean
        get() = contains(PrefsConstants.PREF_ACCESS_TOKEN)

    override var refreshToken: String
        get() = sharedPrefs.getString(PrefsConstants.PREF_REFRESH_TOKEN, null) ?: ""
        set(value) {
            sharedPrefs.edit().putString(PrefsConstants.PREF_REFRESH_TOKEN, value).apply()
        }

    override var notificationToken: String
        get() = sharedPrefs.getString(PrefsConstants.PREF_NOTIFICATION_TOKEN, "") ?: ""
        set(value) {
            sharedPrefs.edit().putString(PrefsConstants.PREF_NOTIFICATION_TOKEN, value).apply()
        }

    override var programsLastUpdateDate: String
        get() = sharedPrefs.getString(PrefsConstants.PREF_PROGRAMS_LAST_UPDATE_DATE, "") ?: ""
        set(value) {
            sharedPrefs.edit().putString(PrefsConstants.PREF_PROGRAMS_LAST_UPDATE_DATE, value)
                .apply()
        }

    override var eyoUpToDate: Boolean
        get() = sharedPrefs.getBoolean(PrefsConstants.PREF_EYO_PROGRAM_UP_TO_DATE, true)
        set(value) {
            sharedPrefs.edit().putBoolean(PrefsConstants.PREF_EYO_PROGRAM_UP_TO_DATE, value).apply()
        }

    override var coelUpToDate: Boolean
        get() = sharedPrefs.getBoolean(PrefsConstants.PREF_COEL_PROGRAM_UP_TO_DATE, true)
        set(value) {
            sharedPrefs.edit().putBoolean(PrefsConstants.PREF_COEL_PROGRAM_UP_TO_DATE, value)
                .apply()
        }
    override var appVersionName: String
        get() = sharedPrefs.getString(PrefsConstants.PREF_APP_VERSION_NAME, "") ?: ""
        set(value) {
            sharedPrefs.edit().putString(PrefsConstants.PREF_APP_VERSION_NAME, value).apply()
        }


    private fun contains(key: String) = sharedPrefs.contains(key)

    private fun containsAndHasValue(key: String) =
        sharedPrefs.getString(key, "")?.isNotEmpty() == true
}