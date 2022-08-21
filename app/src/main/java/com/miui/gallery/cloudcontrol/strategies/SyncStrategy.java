package com.miui.gallery.cloudcontrol.strategies;

import ch.qos.logback.core.spi.ComponentTracker;
import com.google.gson.annotations.SerializedName;
import com.miui.gallery.preference.GalleryPreferences;

/* loaded from: classes.dex */
public class SyncStrategy extends BaseStrategy {
    @SerializedName("total-owner-save-thumbnail-count-limit")
    private int mTotalOwnerSaveThumbnailCountLimit = 10000;
    @SerializedName("total-sharer-save-thumbnail-count-limit")
    private int mTotalSharerSaveThumbnailCountLimit = 0;
    @SerializedName("delay-upload-time")
    private long mDelayUploadTime = 0;
    @SerializedName("only-screen-off-acquire-wakelock")
    private boolean mOnlyScreenOffAcquireWakelock = true;
    @SerializedName("only-charging-acquire-wakelock")
    private boolean mOnlyChargingAcquireWakelock = true;
    @SerializedName("max-capacity-temp-operation-server-tag")
    private int mMaxTempOperationServerTagCapacity = 30;
    @SerializedName("auto-download-space-limit")
    private float mAutoDownloadSpaceLimit = 0.2f;
    @SerializedName("auto-download-num-in-foreground")
    private int mAutoDownloadNumInForeground = 500;
    @SerializedName("auto-download-owner-synced-image")
    private boolean mAutoDownloadOwnerSyncedImage = true;
    @SerializedName("auto-download-share-synced-image")
    private boolean mAutoDownloadShareSyncedImage = false;
    @SerializedName("auto-download-in-back-periodic")
    private boolean isAutoDownloadBackPeriodic = false;
    @SerializedName("auto-download-in-fore-periodic")
    private boolean isAutoDownloadForePeriodic = false;
    @SerializedName("auto-download-require-device-idle")
    private boolean isAutoDownloadRequireDeviceIdle = true;
    @SerializedName("guide-cloud-for-international-build")
    private boolean isGuideCloudInternational = false;
    @SerializedName("max-download-times-when-timeout")
    private int mMaxDownloadTimes = 1;
    @SerializedName(GalleryPreferences.PrefKeys.SYNC_AUTO_DOWNLOAD)
    private boolean isAutoDownload = true;
    @SerializedName(GalleryPreferences.PrefKeys.SYNC_DOWNLOAD_TYPE)
    private int mAutoDownloadType = 0;
    @SerializedName("auto_download_share")
    private boolean isAutoDownloadShare = false;
    @SerializedName("support_backup_only_wifi")
    private boolean isSupportBackupOnlyWifi = true;
    @SerializedName("monitor_enable")
    private boolean isMonitorEnable = true;
    @SerializedName("monitor_check_interval")
    private long mMonitorCheckInterval = 0;
    @SerializedName("monitor_background")
    private boolean isMonitorBackground = true;
    @SerializedName("monitor_background_buffer_time")
    private long mMonitorBackBufferTime = 15000;
    @SerializedName("monitor_background_upper_time")
    private long mMonitorBackUpperTime = 120000;
    @SerializedName("monitor_traffic")
    private boolean isMonitorTraffic = false;
    @SerializedName("monitor_traffic_step")
    private long mMonitorTrafficStep = 104857600;
    @SerializedName("monitor_sync_time")
    private boolean isMonitorSyncTime = false;
    @SerializedName("monitor_sync_upper_time")
    private long mMonitorSyncUpperTime = ComponentTracker.DEFAULT_TIMEOUT;
    @SerializedName("front_for_manual_download")
    private boolean isFrontForManualDownload = true;
    @SerializedName("interval_for_active_pull")
    private long mIntervalForActivePull = 10800000;

    public static SyncStrategy createDefault() {
        return new SyncStrategy();
    }

    public long getDelayUploadTime() {
        return this.mDelayUploadTime;
    }

    public boolean isOnlyScreenOffAcquireWakelock() {
        return this.mOnlyScreenOffAcquireWakelock;
    }

    public boolean isOnlyChargingAcquireWakelock() {
        return this.mOnlyChargingAcquireWakelock;
    }

    public int getMaxTempOperationServerTagCapacity() {
        return this.mMaxTempOperationServerTagCapacity;
    }

    public float getAutoDownloadSpaceLimit() {
        return this.mAutoDownloadSpaceLimit;
    }

    @Deprecated
    public boolean isAutoDownloadRequireDeviceIdle() {
        return this.isAutoDownloadRequireDeviceIdle;
    }

    public int getMaxDownloadTimes() {
        return this.mMaxDownloadTimes;
    }

    public boolean isAutoDownload() {
        return this.isAutoDownload;
    }

    public int getAutoDownloadType() {
        return this.mAutoDownloadType;
    }

    public boolean isAutoDownloadShare() {
        return this.isAutoDownloadShare;
    }

    public boolean isSupportBackupOnlyWifi() {
        return this.isSupportBackupOnlyWifi;
    }

    public boolean isMonitorEnable() {
        return this.isMonitorEnable;
    }

    public long getMonitorCheckInterval() {
        return this.mMonitorCheckInterval;
    }

    public boolean isMonitorBackground() {
        return this.isMonitorBackground;
    }

    public long getMonitorBackBufferTime() {
        return this.mMonitorBackBufferTime;
    }

    public long getMonitorBackUpperTime() {
        return this.mMonitorBackUpperTime;
    }

    public boolean isMonitorTraffic() {
        return this.isMonitorTraffic;
    }

    public long getMonitorTrafficStep() {
        return this.mMonitorTrafficStep;
    }

    public boolean isMonitorSyncTime() {
        return this.isMonitorSyncTime;
    }

    public long getMonitorSyncUpperTime() {
        return this.mMonitorSyncUpperTime;
    }

    public boolean isFrontForManualDownload() {
        return this.isFrontForManualDownload;
    }

    public long getIntervalForActivePull() {
        return this.mIntervalForActivePull;
    }

    public String toString() {
        return "SyncStrategy{mTotalOwnerSaveThumbnailCountLimit=" + this.mTotalOwnerSaveThumbnailCountLimit + ", mTotalSharerSaveThumbnailCountLimit=" + this.mTotalSharerSaveThumbnailCountLimit + ", mDelayUploadTime=" + this.mDelayUploadTime + ", mOnlyScreenOffAcquireWakelock=" + this.mOnlyScreenOffAcquireWakelock + ", mOnlyChargingAcquireWakelock=" + this.mOnlyChargingAcquireWakelock + ", mMaxTempOperationServerTagCapacity=" + this.mMaxTempOperationServerTagCapacity + ", mAutoDownloadSpaceLimit=" + this.mAutoDownloadSpaceLimit + ", mAutoDownloadNumInForeground=" + this.mAutoDownloadNumInForeground + ", mAutoDownloadOwnerSyncedImage=" + this.mAutoDownloadOwnerSyncedImage + ", mAutoDownloadShareSyncedImage=" + this.mAutoDownloadShareSyncedImage + ", isAutoDownloadBackPeriodic=" + this.isAutoDownloadBackPeriodic + ", isAutoDownloadForePeriodic=" + this.isAutoDownloadForePeriodic + ", isAutoDownloadRequireDeviceIdle=" + this.isAutoDownloadRequireDeviceIdle + ", isGuideCloudInternational=" + this.isGuideCloudInternational + ", isAutoDownload=" + this.isAutoDownload + ", mAutoDownloadType=" + this.mAutoDownloadType + ", isAutoDownloadShare=" + this.isAutoDownloadShare + ", isSupportBackupOnlyWifi=" + this.isSupportBackupOnlyWifi + ", isMonitorEnable=" + this.isMonitorEnable + ", mMonitorCheckInterval=" + this.mMonitorCheckInterval + ", isMonitorBackground=" + this.isMonitorBackground + ", mMonitorBackBufferTime=" + this.mMonitorBackBufferTime + ", mMonitorBackUpperTime=" + this.mMonitorBackUpperTime + ", isMonitorTraffic=" + this.isMonitorTraffic + ", mMonitorTrafficStep=" + this.mMonitorTrafficStep + ", isMonitorSyncTime=" + this.isMonitorSyncTime + ", mMonitorSyncUpperTime=" + this.mMonitorSyncUpperTime + ", isFrontForManualDownload=" + this.isFrontForManualDownload + ", mIntervalForActivePull=" + this.mIntervalForActivePull + '}';
    }
}
