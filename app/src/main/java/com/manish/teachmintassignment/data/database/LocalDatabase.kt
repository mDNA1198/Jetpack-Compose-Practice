package com.manish.teachmintassignment.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.manish.teachmintassignment.data.database.dao.GitRepoListDao
import com.manish.teachmintassignment.domain.enitties.GitRepoItem
import com.manish.teachmintassignment.domain.enitties.License
import com.manish.teachmintassignment.domain.enitties.Owner

@Database(
    entities = [
        GitRepoItem::class
    ],
    version = 1,
    exportSchema = false
)

abstract class LocalDatabase : RoomDatabase() {
    abstract fun getGitRepoDao(): GitRepoListDao

    @TypeConverter
    fun fromLicense(countryLang: License?): String? {
        val type = object : TypeToken<License>() {}.type
        return Gson().toJson(countryLang, type)
    }

    @TypeConverter
    fun fromOwner(countryLang: Owner?): String? {
        val type = object : TypeToken<Owner>() {}.type
        return Gson().toJson(countryLang, type)
    }

    companion object {
        private const val DB_NAME = "teachmint_assignment.db"

        fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, LocalDatabase::class.java, DB_NAME
        ).build()
    }
}