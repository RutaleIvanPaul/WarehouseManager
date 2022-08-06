package io.ramani.ramaniWarehouse.app.common.debug

import com.google.firebase.crashlytics.FirebaseCrashlytics


/**
 * Created by Amr on 12/26/17.
 */
object DebugHelper {
    private const val KEY_SCREEN = "screen_name"
    private const val KEY_USER_NAME = "user_name"

    fun setCurrentScreen(screenName: String) {
        FirebaseCrashlytics.getInstance().setCustomKey(KEY_SCREEN, screenName)
    }
//
//    fun setCurrentUserName(userName: String) {
//        if (Fabric.isInitialized()) {
//            Crashlytics.setString(KEY_USER_NAME, userName)
//        }
//    }
}