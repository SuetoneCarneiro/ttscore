package com.example.ttscore.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ttscore.data.local.dao.MatchDao
import com.example.ttscore.data.local.dao.UserDao
import com.example.ttscore.data.local.entity.MatchEntity
import com.example.ttscore.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, MatchEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val matchDao: MatchDao

    companion object {
        const val DATABASE_NAME = "ttscore_db"
    }
}
