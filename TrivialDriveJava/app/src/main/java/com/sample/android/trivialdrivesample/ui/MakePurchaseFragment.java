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
package com.sample.android.trivialdrivesample.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.sample.android.trivialdrivesample.MakePurchaseViewModel;
import com.sample.android.trivialdrivesample.R;
import com.sample.android.trivialdrivesample.TrivialDriveApplication;
import com.sample.android.trivialdrivesample.TrivialDriveRepository;
import com.sample.android.trivialdrivesample.databinding.FragmentMakePurchaseBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * This Fragment is simply a wrapper for the inventory (i.e. items for sale). Here again there is
 * no complicated billing logic. All the billing logic reside inside the [BillingRepository].
 * The [BillingRepository] provides a so-called [AugmentedSkuDetails] object that shows what
 * is for sale and whether the user is allowed to buy the item at this moment. E.g. if the user
 * already has a full tank of gas, then they cannot buy gas at this moment.
 */
public class MakePurchaseFragment extends Fragment {
    String LOG_TAG = "MakePurchaseFragment";

    private MakePurchaseViewModel makePurchaseViewModel;
    private FragmentMakePurchaseBinding binding;
    private final List<MakePurchaseAdapter.Item> inventoryList = new ArrayList<>();

    /**
     * While this list is hard-coded here, it could just as easily come from a server, allowing
     * you to add new SKUs to your app without having to update your app.
     */
    void makeInventoryList() {
        inventoryList.add(new MakePurchaseAdapter.Item(
                getString(R.string.header_fuel_your_ride), MakePurchaseAdapter.VIEW_TYPE_HEADER
        ));
        inventoryList.add(new MakePurchaseAdapter.Item(
                TrivialDriveRepository.SKU_GAS, MakePurchaseAdapter.VIEW_TYPE_ITEM
        ));
        inventoryList.add(new MakePurchaseAdapter.Item(
                getString(R.string.header_go_premium), MakePurchaseAdapter.VIEW_TYPE_HEADER
        ));
        inventoryList.add(new MakePurchaseAdapter.Item(
                TrivialDriveRepository.SKU_PREMIUM, MakePurchaseAdapter.VIEW_TYPE_ITEM
        ));
        inventoryList.add(new MakePurchaseAdapter.Item(
                getString(R.string.header_subscribe), MakePurchaseAdapter.VIEW_TYPE_HEADER
        ));
        inventoryList.add(new MakePurchaseAdapter.Item(
                TrivialDriveRepository.SKU_INFINITE_GAS_MONTHLY, MakePurchaseAdapter.VIEW_TYPE_ITEM
        ));
        inventoryList.add(new MakePurchaseAdapter.Item(
                TrivialDriveRepository.SKU_INFINITE_GAS_YEARLY, MakePurchaseAdapter.VIEW_TYPE_ITEM
        ));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_make_purchase, container, false);
        // This allows data binding to automatically observe any LiveData we pass in
        binding.setLifecycleOwner(this);
        makeInventoryList();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MakePurchaseViewModel.MakePurchaseViewModelFactory makePurchaseViewModelFactory =
                new MakePurchaseViewModel.MakePurchaseViewModelFactory(
                        ((TrivialDriveApplication)getActivity().getApplication())
                                .appContainer.trivialDriveRepository);
        makePurchaseViewModel = new ViewModelProvider( this, makePurchaseViewModelFactory).
                get(MakePurchaseViewModel.class);
        
        binding.setMpvm(makePurchaseViewModel);
        binding.inappInventory.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.inappInventory.setAdapter(new MakePurchaseAdapter(inventoryList, makePurchaseViewModel, this));
    }

    public void makePurchase(String sku) {
        if (!makePurchaseViewModel.buySku(getActivity(), sku)) {
            makePurchaseViewModel.sendMessage(R.string.error_unable_to_make_purchase);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public LiveData<Boolean> canBuySku(String sku) {
        return makePurchaseViewModel.canBuySku(sku);
    }
}
