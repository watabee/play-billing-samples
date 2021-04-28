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

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.sample.android.trivialdrivesample.TrivialDriveRepository.Companion.getInstance
import java.util.*

/*
   This is used for any business logic, as well as to echo LiveData from the BillingRepository.
*/
class MakePurchaseViewModel(application: Application) : AndroidViewModel(application) {
    private val tdr: TrivialDriveRepository

    companion object {
        val TAG = "TrivialDrive:" + MakePurchaseViewModel::class.java.simpleName
        private val skuToResourceIdMap: MutableMap<String, Int> = HashMap()

        init {
            skuToResourceIdMap[TrivialDriveRepository.SKU_GAS] = R.drawable.buy_gas
            skuToResourceIdMap[TrivialDriveRepository.SKU_PREMIUM] = R.drawable.upgrade_app
            skuToResourceIdMap[TrivialDriveRepository.SKU_INFINITE_GAS_MONTHLY] = R.drawable.get_infinite_gas
            skuToResourceIdMap[TrivialDriveRepository.SKU_INFINITE_GAS_YEARLY] = R.drawable.get_infinite_gas
        }
    }

    class SkuDetails internal constructor(val sku: String, tdr: TrivialDriveRepository) {
        @JvmField
        val title: LiveData<String>
        @JvmField
        val description: LiveData<String>
        @JvmField
        val price: LiveData<String>
        @JvmField
        val iconDrawableId: Int

        init {
            title = tdr.getSkuTitle(sku).asLiveData()
            description = tdr.getSkuDescription(sku).asLiveData()
            price = tdr.getSkuPrice(sku).asLiveData()
            iconDrawableId = skuToResourceIdMap[sku]!!
        }
    }

    fun getSkuDetails(sku: String): SkuDetails {
        return SkuDetails(sku, tdr)
    }

    fun canBuySku(sku: String): LiveData<Boolean> {
        return tdr.canPurchase(sku).asLiveData()
    }

    /**
     * Starts a billing flow for purchasing gas.
     * @param activity
     * @return whether or not we were able to start the flow
     */
    fun buySku(activity: Activity?, sku: String?): Boolean {
        return tdr.buySku(activity!!, sku!!)
    }

    val billingFlowInProcess: LiveData<Boolean>
        get() = tdr.billingFlowInProcess.asLiveData()

    fun sendMessage(message: String) {
        tdr.sendMessage(message)
    }

    init {
        tdr = getInstance(application)
    }
}