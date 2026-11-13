package edu.virginia.cs4720.StepActivityLogger

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LogEntity::class], version = 1, exportSchema = false)
abstract class LogDatabase : RoomDatabase() {
    abstract fun logDAO(): LogDao

    companion object {
        @Volatile private var instance: LogDatabase? = null
        fun get(context: Context): LogDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext, LogDatabase::class.java, "log.db"
            ).build().also { instance = it }
        }
    }
}