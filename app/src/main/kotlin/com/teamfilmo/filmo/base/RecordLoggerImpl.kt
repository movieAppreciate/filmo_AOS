package com.teamfilmo.filmo.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class RecordLoggerImpl(
    private val screenName: String,
) : RecordLogger,
    LifecycleEventObserver {
    override fun registerActivity(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }

    override fun onStateChanged(
        source: LifecycleOwner,
        event: Lifecycle.Event,
    ) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                Timber.tag(screenName).d("ON_CREATE")
                FirebaseCrashlytics.getInstance().setCustomKey("ScreenName", "$screenName ON_CREATE")
            }

            Lifecycle.Event.ON_START -> TODO()
            Lifecycle.Event.ON_RESUME -> TODO()
            Lifecycle.Event.ON_PAUSE -> TODO()
            Lifecycle.Event.ON_STOP -> TODO()
            Lifecycle.Event.ON_DESTROY -> TODO()
            Lifecycle.Event.ON_ANY -> TODO()
        }
    }
}
