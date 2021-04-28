package com.sample.android.trivialdrivesample

import android.app.Application
import androidx.annotation.MainThread
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.sample.android.trivialdrivesample.GameViewModel
import com.sample.android.trivialdrivesample.TrivialDriveRepository.Companion.getInstance

/*
   This is used for any business logic, as well as to echo LiveData from the BillingRepository.
*/
class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val tdr: TrivialDriveRepository

    @get:MainThread
    val messages: LiveData<String>
        get() = tdr.messages.asLiveData()

    @MainThread
    fun debugConsumePremium() {
        tdr.debugConsumePremium()
    }

    @get:MainThread
    val billingLifecycleObserver: LifecycleObserver
        get() = tdr.billingLifecycleObserver

    companion object {
        val TAG = "TrivialDrive:" + GameViewModel::class.simpleName
    }

    init {
        tdr = getInstance(application)
    }
}