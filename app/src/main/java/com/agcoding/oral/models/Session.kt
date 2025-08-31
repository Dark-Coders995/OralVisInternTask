package com.agcoding.oral.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "sessions")
data class Session(
    @PrimaryKey val sessionId: String,
    val name: String,
    val age: Int,
    val createdAtEpochMs: Long,
)


