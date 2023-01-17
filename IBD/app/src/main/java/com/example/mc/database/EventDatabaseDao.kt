package com.example.mc.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mc.model.Event


@Dao
interface EventDatabaseDao {

    @Insert
    suspend fun insert(night: Event)

    @Update
    suspend fun update(night: Event)

    @Query("SELECT * from event_table")
    fun getAllEvents(): LiveData<List<Event>>
}