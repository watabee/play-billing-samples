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

    val messages: LiveData<String>
        get() = tdr.messages.asLiveData()

    fun debugConsumePremium() {
        tdr.debugConsumePremium()
    }

    val billingLifecycleObserver: LifecycleObserver
        get() = tdr.billingLifecycleObserver

    companion object {
        val TAG = "TrivialDrive:" + GameViewModel::class.simpleName
    }

    init {
        tdr = getInstance(application)
    }
}