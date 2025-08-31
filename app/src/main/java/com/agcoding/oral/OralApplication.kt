package com.agcoding.oral

import android.app.Application
import com.agcoding.oral.di.AppContainer


class OralApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContainer.appContext = applicationContext
    }
}
