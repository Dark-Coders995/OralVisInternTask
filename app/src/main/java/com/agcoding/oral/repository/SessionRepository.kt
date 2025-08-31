package com.agcoding.oral.repository

import com.agcoding.oral.data.AppDatabaseDao
import com.agcoding.oral.models.Session

class SessionRepository(private val sessionDao: AppDatabaseDao) {
    suspend fun saveSession(
        sessionId: String,
        name: String,
        age: Int,
        createdAtEpochMs: Long = System.currentTimeMillis()
    ) {
        sessionDao.upsertSession(Session(sessionId = sessionId, name = name, age = age, createdAtEpochMs = createdAtEpochMs))
    }

    suspend fun getSession(sessionId: String): Session? = sessionDao.getSessionById(sessionId)
}
