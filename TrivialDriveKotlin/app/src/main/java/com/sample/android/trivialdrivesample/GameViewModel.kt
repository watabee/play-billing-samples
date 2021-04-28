package com.sample.android.trivialdrivesample

import android.app.Application
import androidx.lifecycle.*
import com.sample.android.trivialdrivesample.TrivialDriveRepository.Companion.getInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

/*
   This is used for any business logic, as well as to echo LiveData from the BillingRepository.
*/
class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val tdr: TrivialDriveRepository
    fun drive() {
        tdr.drive()
    }

    /*
        We can drive if we have at least one unit of gas.
     */
    fun canDrive(): LiveData<Boolean> {
        val gasUnitsRemaining = gasUnitsRemaining
        return Transformations.map(gasUnitsRemaining) { gasUnits: Int -> gasUnits > 0 }
    }

    val isPremium: LiveData<Boolean>
        get() = tdr.isPurchased(TrivialDriveRepository.SKU_PREMIUM).asLiveData()
    val gasUnitsRemaining: LiveData<Int>
        get() = tdr.gasTankLevel().shareIn(viewModelScope, SharingStarted.Lazily).asLiveData()

    fun sendMessage(message: String) {
        tdr.sendMessage(message)
    }

    companion object {
        val TAG = "TrivialDrive:" + GameViewModel::class.java.simpleName
    }

    init {
        tdr = getInstance(application)
    }
}