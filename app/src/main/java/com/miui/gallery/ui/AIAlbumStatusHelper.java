package com.miui.gallery.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Build;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.miui.gallery.cloudcontrol.CloudControlManager;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.cloudcontrol.FeatureProfile;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.core.source.server.OpenSearchRequest;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.os.Rom;

/* loaded from: classes2.dex */
public class AIAlbumStatusHelper {
    public static void notifySearchStatusChanged(boolean z) {
    }

    public static boolean useNewAIAlbumSwitch() {
        return !Rom.IS_INTERNATIONAL || isCloudControlSearchAIAlbumOpen();
    }

    public static void setAIAlbumLocalStatus(Context context, boolean z) {
        boolean z2;
        boolean z3 = true;
        if (internalSetLocalSearchStatus(z, true)) {
            notifySearchStatusChanged(z);
            z2 = true;
        } else {
            z2 = false;
        }
        if (internalSetFaceAlbumStatus(z, true)) {
            notifyFaceAlbumStatusChange(context, z);
        } else {
            z3 = z2;
        }
        if (z3) {
            notifyAIAlbumStatusChanged(context, z);
        }
    }

    public static boolean isAIAlbumEnabled() {
        return isFaceAlbumEnabled() || isLocalSearchOpen(true);
    }

    public static void registerAIAlbumStatusReceiver(Context context, BroadcastReceiver broadcastReceiver) {
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, new IntentFilter("com.miui.gallery.action.AI_ALBUM_SWITCH_CHANGE"));
    }

    public static void unregisterAIAlbumStatusReceiver(Context context, BroadcastReceiver broadcastReceiver) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver);
    }

    public static boolean isFaceSwitchSet() {
        return GalleryPreferences.Face.isFaceSwitchSet();
    }

    public static boolean isFaceAlbumEnabled() {
        return GalleryPreferences.Face.getFaceSwitchEnabled();
    }

    public static void setFaceAlbumStatus(Context context, boolean z) {
        setFaceAlbumStatus(context, z, true);
    }

    public static void setFaceAlbumStatus(Context context, boolean z, boolean z2) {
        if (internalSetFaceAlbumStatus(z, z2)) {
            notifyFaceAlbumStatusChange(context, z);
            notifyAIAlbumStatusChanged(context, z);
        }
    }

    public static int getOpenApiSearchStatus() {
        if (!isLocalSearchOpen(true)) {
            return 0;
        }
        if (isCloudControlSearchAIAlbumOpen()) {
            return 1;
        }
        return isLocalSearchOpen(false) ? 2 : 0;
    }

    public static boolean isCloudControlSearchBarOpen() {
        return CloudControlStrategyHelper.getSearchStrategy().isSearchBarEnabled();
    }

    public static boolean isCloudControlSearchAIAlbumOpen() {
        return CloudControlStrategyHelper.getSearchStrategy().isAIAlbumEnabled();
    }

    public static boolean isLocalSearchOpen(boolean z) {
        return GalleryPreferences.Search.isUserSearchSwitchOpen(z);
    }

    public static boolean isLocalSearchSet() {
        return GalleryPreferences.Search.isUserSearchSwitchSet();
    }

    public static void setLocalSearchStatus(Context context, boolean z) {
        setLocalSearchStatus(context, z, true);
    }

    public static void setLocalSearchStatus(Context context, boolean z, boolean z2) {
        if (internalSetLocalSearchStatus(z, z2)) {
            notifySearchStatusChanged(z);
            notifyAIAlbumStatusChanged(context, z);
        }
    }

    public static void requestOpenCloudControlSearch(String str) {
        if (!isLocalSearchOpen(true)) {
            DefaultLogger.w("AIAlbumStatusHelper", "Local search switch is off, no need to request open cloud search.");
            return;
        }
        long userLastRequestOpenTime = GalleryPreferences.Search.getUserLastRequestOpenTime();
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - userLastRequestOpenTime > 600000) {
            ThreadManager.getMiscPool().submit(new ThreadPool.Job<Void>() { // from class: com.miui.gallery.ui.AIAlbumStatusHelper.1
                @Override // com.miui.gallery.concurrent.ThreadPool.Job
                /* renamed from: run  reason: collision with other method in class */
                public Void mo1807run(ThreadPool.JobContext jobContext) {
                    GalleryPreferences.Search.setIsUserSearchSwitchOpen(true);
                    OpenSearchRequest.request();
                    return null;
                }
            });
            GalleryPreferences.Search.setUserLastRequestOpenTime(currentTimeMillis);
        } else {
            DefaultLogger.w("AIAlbumStatusHelper", "Ignore open search request, too frequent.");
        }
        SearchStatUtils.reportEvent(str, "auto_request_open_search");
    }

    public static void doPostCloudControlJob() {
        if (CloudControlStrategyHelper.getSearchStrategy().isAIAlbumEnabled() || CloudControlManager.getInstance().queryFeatureStatus("search-auto-open-search") != FeatureProfile.Status.ENABLE) {
            return;
        }
        SearchStatUtils.reportEvent("from_cloud_control", "auto_request_open_search", "result", SearchConstants.isErrorStatus(OpenSearchRequest.request()) ? "failed" : "succeeded", "device", Build.DEVICE);
    }

    public static boolean internalSetFaceAlbumStatus(boolean z, boolean z2) {
        if (!isFaceSwitchSet() || isFaceAlbumEnabled() != z) {
            GalleryPreferences.Face.setFaceSwitchStatus(z);
            if (z2) {
                GalleryPreferences.SettingsSync.markDirty(true);
            }
            return true;
        }
        return false;
    }

    public static void notifyFaceAlbumStatusChange(Context context, boolean z) {
        context.getContentResolver().notifyChange(GalleryContract.Album.URI, (ContentObserver) null, false);
        context.getContentResolver().notifyChange(GalleryContract.PeopleFace.PERSONS_URI, (ContentObserver) null, false);
    }

    public static boolean internalSetLocalSearchStatus(boolean z, boolean z2) {
        if (!isLocalSearchSet() || z != isLocalSearchOpen(true)) {
            GalleryPreferences.Search.setIsUserSearchSwitchOpen(z);
            if (z2) {
                GalleryPreferences.SettingsSync.markDirty(true);
            }
            return true;
        }
        return false;
    }

    public static void notifyAIAlbumStatusChanged(Context context, boolean z) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("com.miui.gallery.action.AI_ALBUM_SWITCH_CHANGE"));
    }
}
