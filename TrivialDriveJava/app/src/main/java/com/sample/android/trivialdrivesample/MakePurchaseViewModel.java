package com.sample.android.trivialdrivesample;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.HashMap;
import java.util.Map;

import static com.sample.android.trivialdrivesample.TrivialDriveRepository.SKU_GAS;
import static com.sample.android.trivialdrivesample.TrivialDriveRepository.SKU_INFINITE_GAS_MONTHLY;
import static com.sample.android.trivialdrivesample.TrivialDriveRepository.SKU_INFINITE_GAS_YEARLY;
import static com.sample.android.trivialdrivesample.TrivialDriveRepository.SKU_PREMIUM;

/*
    This is used for any business logic, as well as to echo LiveData from the BillingRepository.
 */
public class MakePurchaseViewModel extends AndroidViewModel {
    static final String TAG = "TrivialDrive:" + MakePurchaseViewModel.class.getSimpleName();
    private final TrivialDriveRepository tdr;
    static private Map<String, Integer> skuToResourceIdMap = new HashMap<>();
    static {
        skuToResourceIdMap.put(SKU_GAS, R.drawable.buy_gas);
        skuToResourceIdMap.put(SKU_PREMIUM, R.drawable.upgrade_app);
        skuToResourceIdMap.put(SKU_INFINITE_GAS_MONTHLY, R.drawable.get_infinite_gas);
        skuToResourceIdMap.put(SKU_INFINITE_GAS_YEARLY, R.drawable.get_infinite_gas);
    }
    public MakePurchaseViewModel(@NonNull Application application) {
        super(application);
        tdr = TrivialDriveRepository.getInstance(application);
    }

    static public class SkuDetails {
        final public String sku;
        final public LiveData<String> title;
        final public LiveData<String> description;
        final public LiveData<String> price;
        final public int iconDrawableId;
        SkuDetails(@NonNull String sku, TrivialDriveRepository tdr) {
            this.sku = sku;
            title = tdr.getSkuTitle(sku);
            description = tdr.getSkuDescription(sku);
            price = tdr.getSkuPrice(sku);
            iconDrawableId = skuToResourceIdMap.get(sku);
        }
    }

    public SkuDetails getSkuDetails(String sku) {
        return new SkuDetails(sku, tdr);
    }

    public LiveData<Boolean> canBuySku(String sku) {
        return tdr.canPurchase(sku);
    }

    /**
     * Starts a billing flow for purchasing gas.
     * @param activity
     * @return whether or not we were able to start the flow
     */
    public boolean buySku(Activity activity, String sku){
        return tdr.buySku(activity, sku);
    }

    public LiveData<Boolean> getBillingFlowInProcess() { return tdr.getBillingFlowInProcess(); }

    public void sendMessage(String message) {
        tdr.sendMessage(message);
    }
}
