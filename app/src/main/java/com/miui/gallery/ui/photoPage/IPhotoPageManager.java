package com.miui.gallery.ui.photoPage;

import android.content.res.Configuration;
import android.os.Bundle;
import com.miui.gallery.model.BaseDataItem;

/* loaded from: classes2.dex */
public interface IPhotoPageManager {

    /* loaded from: classes2.dex */
    public interface IPhotoPageManagerCallback {
        void onAccelerometerRotationChange();

        void onOrientationChanged(int i, int i2);
    }

    /* loaded from: classes2.dex */
    public interface IPhotoPageManagerController {
    }

    default void onConfigurationChanged(Configuration configuration) {
    }

    default void onCreate(Bundle bundle) {
    }

    default void onDestroy() {
    }

    default void onDestroyView() {
    }

    default void onDoExit() {
    }

    default void onPause() {
    }

    default void onResume() {
    }

    default void onSaveInstanceState(Bundle bundle) {
    }

    default void onSettleItem(BaseDataItem baseDataItem) {
    }

    default void onStart() {
    }

    default void onViewInflated() {
    }
}
