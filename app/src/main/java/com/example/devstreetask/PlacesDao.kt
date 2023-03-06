package com.example.devstreetask

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlacesDao {

    @Insert
    fun insertPlace(myPlaces: MyPlaces)
    @Update
    fun updatePlace(myPlaces: MyPlaces)
    @Delete
    fun deletePlace(myPlaces: MyPlaces)
    @Query("select * from MyPlaces")
    fun showAllPlaces() : List<MyPlaces>

}