package com.teamfilmo.filmo.app

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.navercorp.nid.NaverIdLoginSDK
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class FilmoApp : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.d("App onCreate")

        NaverIdLoginSDK.showDevelopersLog(true)

        FirebaseCrashlytics.getInstance().setUserId("user_id")
        FirebaseCrashlytics.getInstance().setCustomKey("screen_name", "main_screen")
    }
}
