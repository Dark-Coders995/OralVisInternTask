package com.agcoding.oral.di

import android.content.Context
import com.agcoding.oral.data.AppDatabase
import com.agcoding.oral.repository.SessionRepository
import com.agcoding.oral.utils.MediaStoreStorage


object AppContainer {
    lateinit var appContext: Context

    val database: AppDatabase by lazy { AppDatabase.getInstance(appContext) }
    val sessionRepository: SessionRepository by lazy { SessionRepository(database.appDatabaseDao()) }
    val mediaStoreStorage: MediaStoreStorage by lazy { MediaStoreStorage(appContext) }
}


