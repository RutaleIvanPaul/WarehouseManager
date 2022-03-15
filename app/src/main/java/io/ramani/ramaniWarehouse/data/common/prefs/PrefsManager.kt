package io.ramani.ramaniWarehouse.data.common.prefs

import android.content.Context
import android.content.SharedPreferences
import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.model.ProductsUIModel
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

    override var currentWarehouse: String
        get() = sharedPrefs.getString(PrefsConstants.PREF_CURRENT_WAREHOUSE, null) ?: ""
        set(value) {
            sharedPrefs.edit().putString(PrefsConstants.PREF_CURRENT_WAREHOUSE, value).apply()
        }
//    override var currentProducts: List<ProductsUIModel>?
//        get() =  sharedPrefs.getList(PrefsConstants.PREF_CURRENT_PRODUCTS, null) ?: null
//
//        set(value) {}

    override var accountType: String
        get() = sharedPrefs.getString(PrefsConstants.PREF_ACCOUNT_TYPE, null) ?: ""
        set(value) {
            sharedPrefs.edit().putString(PrefsConstants.PREF_ACCOUNT_TYPE, value).apply()
        }

    override var timeZone: String
        get() = sharedPrefs.getString(PrefsConstants.PREF_TIMEZONE, null) ?: ""
        set(value) {
            sharedPrefs.edit().putString(PrefsConstants.PREF_TIMEZONE, value).apply()
        }

    override var invalidate_cache_company_products: Boolean
        get() = sharedPrefs.getBoolean(PrefsConstants.PREF_INVALIDATE_CACHE_COMPANY_PRODUCTS,false)
        set(value) {
            sharedPrefs.edit()
                .putBoolean(PrefsConstants.PREF_INVALIDATE_CACHE_COMPANY_PRODUCTS, value).apply()
        }

    override var invalidate_cache_available_products: Boolean
        get() = sharedPrefs.getBoolean(PrefsConstants.PREF_INVALIDATE_CACHE_AVAILABLE_PRODUCTS,false)
        set(value) {
            sharedPrefs.edit().putBoolean(PrefsConstants.PREF_INVALIDATE_CACHE_AVAILABLE_PRODUCTS,value).apply()
        }

    override var invalidate_cache_assignments_reports: Boolean
        get() = sharedPrefs.getBoolean(PrefsConstants.PREF_INVALIDATE_CACHE_ASSIGNMENTS_REPORTS,false)
        set(value) {
            sharedPrefs.edit().putBoolean(PrefsConstants.PREF_INVALIDATE_CACHE_ASSIGNMENTS_REPORTS,value).apply()
        }


    private fun contains(key: String) = sharedPrefs.contains(key)

    private fun containsAndHasValue(key: String) =
        sharedPrefs.getString(key, "")?.isNotEmpty() == true
}