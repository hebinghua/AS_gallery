package com.miui.gallery.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.SparseBooleanArray;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloudcontrol.CloudControlManager;
import com.miui.gallery.cloudcontrol.FeatureProfile;
import com.miui.gallery.cloudcontrol.observers.FeatureStatusObserver;
import com.xiaomi.stat.MiStat;
import java.lang.ref.WeakReference;
import java.util.Observable;
import java.util.Observer;

/* loaded from: classes2.dex */
public class AIAlbumDisplayHelper {
    public BroadcastReceiver mAIAlbumSwitchObserver;
    public AIAlbumDisplayStatusObservable mObservable;
    public final FeatureStatusObserver mSearchCloudControlStatusObserver;
    public SparseBooleanArray mStatusValueCache;

    /* loaded from: classes2.dex */
    public interface DisplayStatusCallback {
        void onStatusChanged(SparseBooleanArray sparseBooleanArray);
    }

    /* loaded from: classes2.dex */
    public static class WeakReferencedAIAlbumDisplayStatusObserver implements Observer {
        public WeakReference<DisplayStatusCallback> mCallbackRef;

        public WeakReferencedAIAlbumDisplayStatusObserver(DisplayStatusCallback displayStatusCallback) {
            this.mCallbackRef = new WeakReference<>(displayStatusCallback);
        }

        @Override // java.util.Observer
        public void update(Observable observable, Object obj) {
            DisplayStatusCallback displayStatusCallback = this.mCallbackRef.get();
            if (displayStatusCallback == null || !(obj instanceof SparseBooleanArray)) {
                return;
            }
            displayStatusCallback.onStatusChanged((SparseBooleanArray) obj);
        }
    }

    public AIAlbumDisplayHelper() {
        this.mStatusValueCache = null;
        this.mSearchCloudControlStatusObserver = new FeatureStatusObserver() { // from class: com.miui.gallery.ui.AIAlbumDisplayHelper.1
            @Override // com.miui.gallery.cloudcontrol.observers.FeatureStatusObserver
            public void onStatusChanged(String str, FeatureProfile.Status status) {
                if (MiStat.Event.SEARCH.equals(str)) {
                    AIAlbumDisplayHelper.this.requeryStatusAndNotifyStatusChange();
                }
            }
        };
        this.mAIAlbumSwitchObserver = new BroadcastReceiver() { // from class: com.miui.gallery.ui.AIAlbumDisplayHelper.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (intent == null || !"com.miui.gallery.action.AI_ALBUM_SWITCH_CHANGE".equals(intent.getAction())) {
                    return;
                }
                AIAlbumDisplayHelper.this.requeryStatusAndNotifyStatusChange();
            }
        };
    }

    /* loaded from: classes2.dex */
    public static class AIAlbumDisplayHelperHolder {
        public static final AIAlbumDisplayHelper S_INSTANCE = new AIAlbumDisplayHelper();
    }

    public static AIAlbumDisplayHelper getInstance() {
        return AIAlbumDisplayHelperHolder.S_INSTANCE;
    }

    public SparseBooleanArray registerAIAlbumDisplayStatusObserver(WeakReferencedAIAlbumDisplayStatusObserver weakReferencedAIAlbumDisplayStatusObserver) {
        if (this.mObservable == null) {
            this.mObservable = new AIAlbumDisplayStatusObservable();
        }
        requeryStatusAndNotifyStatusChange();
        synchronized (this.mSearchCloudControlStatusObserver) {
            int countObservers = this.mObservable.countObservers();
            this.mObservable.addObserver(weakReferencedAIAlbumDisplayStatusObserver);
            if (countObservers <= 0 && this.mObservable.countObservers() > 0) {
                CloudControlManager.getInstance().registerStatusObserver(MiStat.Event.SEARCH, this.mSearchCloudControlStatusObserver);
                LocalBroadcastManager.getInstance(GalleryApp.sGetAndroidContext()).registerReceiver(this.mAIAlbumSwitchObserver, new IntentFilter("com.miui.gallery.action.AI_ALBUM_SWITCH_CHANGE"));
            }
        }
        return this.mStatusValueCache.clone();
    }

    public void unregisterAIAlbumDisplayStatusObserver(WeakReferencedAIAlbumDisplayStatusObserver weakReferencedAIAlbumDisplayStatusObserver) {
        synchronized (this.mSearchCloudControlStatusObserver) {
            AIAlbumDisplayStatusObservable aIAlbumDisplayStatusObservable = this.mObservable;
            if (aIAlbumDisplayStatusObservable != null) {
                aIAlbumDisplayStatusObservable.deleteObserver(weakReferencedAIAlbumDisplayStatusObserver);
                if (this.mObservable.countObservers() <= 0) {
                    CloudControlManager.getInstance().unregisterStatusObserver(this.mSearchCloudControlStatusObserver);
                    LocalBroadcastManager.getInstance(GalleryApp.sGetAndroidContext()).unregisterReceiver(this.mAIAlbumSwitchObserver);
                }
            }
        }
    }

    public final void requeryStatusAndNotifyStatusChange() {
        SparseBooleanArray sparseBooleanArray = this.mStatusValueCache;
        if (sparseBooleanArray == null) {
            sparseBooleanArray = new SparseBooleanArray();
        }
        this.mStatusValueCache = new SparseBooleanArray();
        boolean z = true;
        updateNewValue(sparseBooleanArray, 1, AIAlbumStatusHelper.isLocalSearchOpen(true) && AIAlbumStatusHelper.isCloudControlSearchBarOpen());
        if (!AIAlbumStatusHelper.useNewAIAlbumSwitch() || (!AIAlbumStatusHelper.isFaceAlbumEnabled() && !AIAlbumStatusHelper.isLocalSearchOpen(true))) {
            z = false;
        }
        updateNewValue(sparseBooleanArray, 2, z);
        if (this.mObservable == null || sparseBooleanArray.size() <= 0) {
            return;
        }
        this.mObservable.onUpdateStatus(sparseBooleanArray);
    }

    public final void updateNewValue(SparseBooleanArray sparseBooleanArray, int i, boolean z) {
        this.mStatusValueCache.put(i, z);
        if (sparseBooleanArray.indexOfKey(i) < 0 || sparseBooleanArray.get(i) != z) {
            sparseBooleanArray.put(i, z);
        } else {
            sparseBooleanArray.delete(i);
        }
    }

    /* loaded from: classes2.dex */
    public static class AIAlbumDisplayStatusObservable extends Observable {
        public AIAlbumDisplayStatusObservable() {
        }

        public void onUpdateStatus(SparseBooleanArray sparseBooleanArray) {
            setChanged();
            notifyObservers(sparseBooleanArray);
        }
    }
}
