package com.example.devstreetask

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [MyPlaces::class], version = 1)
abstract class PlacesDatabase : RoomDatabase() {

    abstract fun getPlacesDao() : PlacesDao

    companion object{
        private var instance: PlacesDatabase? = null

        @Synchronized
        fun getInstance(context: Context): PlacesDatabase? {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        PlacesDatabase::class.java, "places.db"
                    ).allowMainThreadQueries().build()
            }
            return instance
        }

    }

}