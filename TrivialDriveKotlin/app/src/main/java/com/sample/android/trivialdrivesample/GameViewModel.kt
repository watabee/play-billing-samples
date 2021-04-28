/*
 * Copyright (C) 2021 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sample.android.trivialdrivesample

import android.app.Application
import androidx.lifecycle.*
import com.sample.android.trivialdrivesample.TrivialDriveRepository.Companion.getInstance
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn

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