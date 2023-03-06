package com.example.devstreetask

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class MyPlaces (
    @ColumnInfo(name = "name")
    var name : String = "",
    @ColumnInfo(name = "address")
    var address: String = "",
    @ColumnInfo(name = "lat")
    var lat : Double = 0.0,
    @ColumnInfo(name = "long")
    var long : Double = 0.0,
    @PrimaryKey(autoGenerate = true)
    var id : Int=0) : java.io.Serializable