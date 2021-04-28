package com.sample.android.trivialdrivesample.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
    A very simple key-value store for integers built using Room, since we're storing just a tiny
    bit of data.
 */
@Entity
class GameState(@field:PrimaryKey var key: String, var value: Int?)