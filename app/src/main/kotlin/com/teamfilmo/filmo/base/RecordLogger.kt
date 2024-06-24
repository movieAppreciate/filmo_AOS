package com.teamfilmo.filmo.base

import androidx.lifecycle.LifecycleOwner

interface RecordLogger {
    fun registerActivity(owner: LifecycleOwner)
}
