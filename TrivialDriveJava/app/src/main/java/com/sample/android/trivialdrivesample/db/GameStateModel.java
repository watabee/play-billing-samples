package com.sample.android.trivialdrivesample.db;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameStateModel {
    private final Application application;
    private final GameStateDatabase db;
    final GameStateDao gameStateDao;
    final ExecutorService queryExecutor = Executors.newSingleThreadExecutor();
    final static private String GAS_LEVEL = "gas";
    final LiveData<Integer> gasTankLevel;

    public GameStateModel(@NonNull Application application) {
        this.application = application;
        // This creates our DB and populates our game state database with the initial state of
        // a full tank
        db = Room.databaseBuilder(application,
                GameStateDatabase.class, "GameState.db")
                .createFromAsset("database/initialgamestate.db")
                .build();
        gameStateDao = db.gameStateDao();
        gasTankLevel = gameStateDao.observe(GAS_LEVEL);
    }

    public void decrementGas( int minLevel ) {
        queryExecutor.submit(new Runnable() {
            @Override
            public void run() {
                gameStateDao.decrement(GAS_LEVEL, minLevel);
            }
        });
    }

    public void incrementGas( int maxLevel ) {
        queryExecutor.submit(new Runnable() {
            @Override
            public void run() { gameStateDao.increment(GAS_LEVEL, maxLevel); }
        });
    }

    public LiveData<Integer> gasTankLevel() {
        return gasTankLevel;
    }

    public Integer getCurrentGasTankLevel() { return gameStateDao.get(GAS_LEVEL); }

    private static volatile GameStateModel sInstance;
    /*
        Standard boilerplate double check locking pattern for thread-safe singletons.
     */
    public static GameStateModel getInstance(Application application) {
        if (sInstance == null) {
            synchronized (GameStateModel.class) {
                if (sInstance == null) sInstance = new GameStateModel(application);
            }
        }
        return sInstance;
    }
}
