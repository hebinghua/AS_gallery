package com.miui.gallery.cloudcontrol.strategies;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class MediaFeatureCalculationStrategy extends BaseStrategy {
    @SerializedName("device_strategy")
    private List<DeviceStrategy> mDevicesStrategy = new ArrayList();

    public DeviceStrategy getStrategy(String str) {
        for (DeviceStrategy deviceStrategy : this.mDevicesStrategy) {
            if (deviceStrategy.getDevice().equals(str)) {
                return deviceStrategy;
            }
        }
        return null;
    }

    /* loaded from: classes.dex */
    public static class DeviceStrategy {
        @SerializedName("device_feature")
        private String mDeviceFeature;
        @SerializedName("disable")
        private boolean mDisable;

        public String getDevice() {
            return this.mDeviceFeature;
        }

        public boolean isDisable() {
            return this.mDisable;
        }
    }
}
