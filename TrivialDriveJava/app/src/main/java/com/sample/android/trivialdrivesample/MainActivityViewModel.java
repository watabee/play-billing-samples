package com.sample.android.trivialdrivesample;

import android.app.Application;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;

/*
    This is used for any business logic, as well as to echo LiveData from the BillingRepository.
 */
public class MainActivityViewModel extends AndroidViewModel {
    static final String TAG = "TrivialDrive:" + GameViewModel.class.getSimpleName();
    private final TrivialDriveRepository tdr;
    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        tdr = TrivialDriveRepository.getInstance(application);
    }

    @MainThread
    public LiveData<String> getMessages() {
        return tdr.getMessages();
    }

    @MainThread
    public void debugConsumePremium() {
        tdr.debugConsumePremium();
    }

    @MainThread
    public LifecycleObserver getBillingLifecycleObserver() {
        return tdr.getBillingLifecycleObserver();
    }
}
