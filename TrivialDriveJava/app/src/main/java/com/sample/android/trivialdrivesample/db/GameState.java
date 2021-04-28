package com.sample.android.trivialdrivesample.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/*
    A very simple key-value store for integers built using Room, since we're storing just a tiny
    bit of data.
 */
@Entity
public class GameState {
    public GameState(String key, Integer value) {this.key = key; this.value = value;}
    @PrimaryKey
    @NonNull
    public String key;
    public Integer value;
}
