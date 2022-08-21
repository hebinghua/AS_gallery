package com.miui.gallery.assistant.recommend;

import android.content.Context;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloudcontrol.CloudControlManager;
import com.miui.gallery.cloudcontrol.CloudControlRequestHelper;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.cloudcontrol.observers.FeatureStrategyObserver;
import com.miui.gallery.cloudcontrol.strategies.RecommendStrategy;
import com.miui.gallery.content.ExtendedAsyncTaskLoader;

/* loaded from: classes.dex */
public class RecommendListLoader extends ExtendedAsyncTaskLoader<RecommendStrategy> {
    public FeatureStrategyObserver<RecommendStrategy> mFeatureStrategyObserver;
    public boolean mIsObserverRegistered;

    public RecommendListLoader(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.content.ExtendedAsyncTaskLoader, androidx.loader.content.Loader
    public void onStartLoading() {
        super.onStartLoading();
        forceLoad();
        registerObserver();
    }

    @Override // androidx.loader.content.AsyncTaskLoader
    /* renamed from: loadInBackground */
    public RecommendStrategy mo1444loadInBackground() {
        RecommendStrategy recommendStrategy = CloudControlStrategyHelper.getRecommendStrategy();
        return (recommendStrategy != null || !new CloudControlRequestHelper(GalleryApp.sGetAndroidContext()).execRecommendRequest()) ? recommendStrategy : CloudControlStrategyHelper.getRecommendStrategy();
    }

    @Override // androidx.loader.content.Loader
    public void onReset() {
        super.onReset();
        unregisterObserver();
    }

    @Override // androidx.loader.content.Loader
    public void onAbandon() {
        super.onAbandon();
        unregisterObserver();
    }

    public final void registerObserver() {
        if (!this.mIsObserverRegistered) {
            this.mIsObserverRegistered = true;
            this.mFeatureStrategyObserver = new FeatureStrategyObserver<RecommendStrategy>() { // from class: com.miui.gallery.assistant.recommend.RecommendListLoader.1
                @Override // com.miui.gallery.cloudcontrol.observers.FeatureStrategyObserver
                public void onStrategyChanged(String str, RecommendStrategy recommendStrategy, RecommendStrategy recommendStrategy2) {
                    RecommendListLoader.this.onContentChanged();
                }
            };
            CloudControlManager.getInstance().registerStrategyObserver("recommendation", null, this.mFeatureStrategyObserver);
        }
    }

    public final void unregisterObserver() {
        if (!this.mIsObserverRegistered || this.mFeatureStrategyObserver == null) {
            return;
        }
        CloudControlManager.getInstance().unregisterStrategyObserver(this.mFeatureStrategyObserver);
        this.mIsObserverRegistered = false;
        this.mFeatureStrategyObserver = null;
    }
}
