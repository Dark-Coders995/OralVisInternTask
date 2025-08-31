package com.agcoding.oral.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.agcoding.oral.models.Session


@Database(
    entities = [Session::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDatabaseDao(): AppDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "oral.db"
                        ).fallbackToDestructiveMigration(false).build().also { INSTANCE = it }
        }
    }
}


