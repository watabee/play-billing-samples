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
package com.sample.android.trivialdrivesample;

import android.app.Application;

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

    public LiveData<String> getMessages() {
        return tdr.getMessages();
    }

    public void debugConsumePremium() {
        tdr.debugConsumePremium();
    }

    public LifecycleObserver getBillingLifecycleObserver() {
        return tdr.getBillingLifecycleObserver();
    }
}
