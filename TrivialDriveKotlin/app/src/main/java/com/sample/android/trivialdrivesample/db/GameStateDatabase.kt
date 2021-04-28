package com.sample.android.trivialdrivesample.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GameState::class], version = 1)
abstract class GameStateDatabase : RoomDatabase() {
    abstract fun gameStateDao(): GameStateDao
}