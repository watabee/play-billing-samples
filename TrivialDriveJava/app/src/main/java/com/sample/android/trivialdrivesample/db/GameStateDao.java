package com.sample.android.trivialdrivesample.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface GameStateDao {
    @Query("SELECT `value` FROM GameState WHERE `key` = :key LIMIT 1")
    LiveData<Integer> observe(String key);

    @Query("SELECT `value` FROM GameState WHERE `key` = :key LIMIT 1")
    Integer get(String key);

    @Query("REPLACE INTO GameState VALUES(:key,:value)")
    void put(String key, int value);

    @Query("UPDATE GameState SET `value`=`value`-1 WHERE `key`=:key AND `value` > :minValue")
    int decrement(String key, int minValue);

    @Query("UPDATE GameState SET `value`=`value`+1 WHERE `key`=:key AND `value` < :maxValue")
    int increment(String key, int maxValue);
}
