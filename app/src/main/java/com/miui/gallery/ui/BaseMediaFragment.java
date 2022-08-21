package com.miui.gallery.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.loader.content.Loader;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.miui.gallery.R;
import com.miui.gallery.util.logger.DefaultLogger;
import java.lang.ref.WeakReference;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class BaseMediaFragment extends BaseFragment {
    public int mFastScrollerMarginTop;
    public boolean mInPhotoPage;
    public PhotoPageReceiver mPhotoPageReceiver;

    public abstract List<Loader<?>> getLoaders();

    @Override // com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mFastScrollerMarginTop = getResources().getDimensionPixelOffset(R.dimen.time_line_header_height) + getResources().getDimensionPixelSize(R.dimen.fast_scroller_margin_top_to_time_line);
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mPhotoPageReceiver = new PhotoPageReceiver(this);
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.miu.gallery.action.ENTER_PHOTO_PAGE");
        intentFilter.addAction("com.miui.gallery.action.EXIT_PHOTO_PAGE");
        LocalBroadcastManager.getInstance(this.mActivity).registerReceiver(this.mPhotoPageReceiver, intentFilter);
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this.mActivity).unregisterReceiver(this.mPhotoPageReceiver);
    }

    @Override // com.miui.gallery.ui.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
    }

    @Override // com.miui.gallery.ui.BaseFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
    }

    public void onPhotoPageCreate(Intent intent) {
        if (isAdded()) {
            pauseDataLoading();
        }
        this.mInPhotoPage = true;
    }

    public void pauseDataLoading() {
        List<Loader<?>> loaders = getLoaders();
        if (loaders == null || loaders.size() <= 0) {
            return;
        }
        for (Loader<?> loader : loaders) {
            if (loader != null) {
                loader.stopLoading();
            }
        }
    }

    public void onPhotoPageDestroy(Intent intent) {
        if (isAdded()) {
            if (intent.getIntExtra("photo_result_code", -1) == -1) {
                resumeDataLoading();
            } else {
                finish();
            }
        }
        this.mInPhotoPage = false;
    }

    public void resumeDataLoading() {
        List<Loader<?>> loaders = getLoaders();
        if (loaders == null || loaders.size() <= 0) {
            return;
        }
        for (Loader<?> loader : loaders) {
            if (loader != null) {
                loader.startLoading();
            }
        }
    }

    public boolean isInPhotoPage() {
        return this.mInPhotoPage;
    }

    /* loaded from: classes2.dex */
    public static class PhotoPageReceiver extends BroadcastReceiver {
        public WeakReference<BaseMediaFragment> mFragmentRef;

        public PhotoPageReceiver(BaseMediaFragment baseMediaFragment) {
            this.mFragmentRef = new WeakReference<>(baseMediaFragment);
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            BaseMediaFragment baseMediaFragment = this.mFragmentRef.get();
            if (baseMediaFragment != null) {
                String action = intent.getAction();
                if ("com.miu.gallery.action.ENTER_PHOTO_PAGE".equals(action)) {
                    DefaultLogger.d("BaseMediaFragment", "ACTION_ENTER_PHOTO_PAGE");
                    baseMediaFragment.onPhotoPageCreate(intent);
                } else if (!"com.miui.gallery.action.EXIT_PHOTO_PAGE".equals(action)) {
                } else {
                    DefaultLogger.d("BaseMediaFragment", "ACTION_EXIT_PHOTO_PAGE");
                    baseMediaFragment.onPhotoPageDestroy(intent);
                }
            }
        }
    }
}
