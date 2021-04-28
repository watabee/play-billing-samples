package com.sample.android.trivialdrivesample.db

import android.app.Application
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class GameStateModel(private val application: Application) {
    private val db: GameStateDatabase
    val gameStateDao: GameStateDao
    val gasTankLevel: Flow<Int>

    suspend fun decrementGas(minLevel: Int) : Int {
        return CoroutineScope(Dispatchers.IO).async {
             gameStateDao.decrement(GAS_LEVEL, minLevel)
        }.await()
    }

    suspend fun incrementGas(maxLevel: Int) : Int {
        return CoroutineScope(Dispatchers.IO).async {
            gameStateDao.increment(GAS_LEVEL, maxLevel)
        }.await()
    }

    fun gasTankLevel(): Flow<Int> {
        return gasTankLevel
    }

    companion object {
        private const val GAS_LEVEL = "gas"

        @Volatile
        private var sInstance: GameStateModel? = null

        // Standard boilerplate double check locking pattern for thread-safe singletons.
        @JvmStatic
        fun getInstance(application: Application) =
                sInstance ?: synchronized( this ) {
                    sInstance ?: GameStateModel(application).
                    also { sInstance = it }
                }
    }

    init {
        // This creates our DB and populates our game state database with the initial state of
        // a full tank
        db = Room.databaseBuilder(application,
                GameStateDatabase::class.java, "GameState.db")
                .createFromAsset("database/initialgamestate.db")
                .build()
        gameStateDao = db.gameStateDao()
        // this causes the gasTankLevel from our Room database to behave more like LiveData
        gasTankLevel = gameStateDao[GAS_LEVEL].distinctUntilChanged().shareIn(CoroutineScope(Dispatchers.Main), SharingStarted.Lazily, 1)
    }
}