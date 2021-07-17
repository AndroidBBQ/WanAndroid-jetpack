package com.bbq.home.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bbq.home.bean.ArticleBean

@Database(
    entities = [ArticleBean::class],
    version = 3,
    exportSchema = false
)
abstract class HomeDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao

    companion object {
        private const val DB_NAME = "app.db"

        @Volatile
        private var instance: HomeDatabase? = null

        fun get(context: Context): HomeDatabase {
            return instance ?: Room.databaseBuilder(
                context, HomeDatabase::class.java,
                DB_NAME
            )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
                .also {
                    instance = it
                }
        }
    }
}