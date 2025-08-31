package com.agcoding.oral.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.agcoding.oral.models.Session

@Dao
interface AppDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSession(session: Session)

    @Query("SELECT * FROM sessions WHERE sessionId = :id LIMIT 1")
    suspend fun getSessionById(id: String): Session?

}
