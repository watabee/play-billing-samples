package com.sample.android.trivialdrivesample;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

/*
    This is used for any business logic, as well as to echo LiveData from the BillingRepository.
 */
public class GameViewModel extends AndroidViewModel {
    static final String TAG = "TrivialDrive:" + GameViewModel.class.getSimpleName();
    private final TrivialDriveRepository tdr;
    public GameViewModel(@NonNull Application application) {
        super(application);
        tdr = TrivialDriveRepository.getInstance(application);
    }

    public void drive(){
        tdr.drive();
    }

    /*
        We can drive if we have at least one unit of gas.
     */
    public LiveData<Boolean> canDrive() {
        LiveData<Integer> gasUnitsRemaining = getGasUnitsRemaining();
        return Transformations.map(gasUnitsRemaining, gasUnits -> gasUnits > 0);
    }

    public LiveData<Boolean> isPremium() {
        return tdr.isPurchased(TrivialDriveRepository.SKU_PREMIUM);
    }
    public LiveData<Integer> getGasUnitsRemaining() {
        return tdr.gasTankLevel();
    }

    public void sendMessage(String message) {
        tdr.sendMessage(message);
    }
}
