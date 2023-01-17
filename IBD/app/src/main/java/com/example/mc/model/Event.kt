package com.example.mc.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "event_table")
data class Event (
    @PrimaryKey(autoGenerate = true)
    var reporttId: Long = 0L,

    @ColumnInfo(name = "location")
    val location: String = "",

    @ColumnInfo(name = "data")
    val data: String = "",

    @ColumnInfo(name = "tag")
    var tag: String = "",

    @ColumnInfo(name = "description")
    var description: String = ""
)