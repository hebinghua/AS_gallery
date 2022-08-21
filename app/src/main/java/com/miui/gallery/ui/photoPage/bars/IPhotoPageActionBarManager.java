package com.miui.gallery.ui.photoPage.bars;

import android.content.res.Configuration;
import android.view.View;
import androidx.lifecycle.DefaultLifecycleObserver;
import com.miui.gallery.model.BaseDataItem;
import miuix.appcompat.app.ActionBar;

/* loaded from: classes2.dex */
public interface IPhotoPageActionBarManager extends DefaultLifecycleObserver {
    void attach();

    void delegate(ActionBar actionBar);

    int getActionBarHeight();

    void hide(boolean z);

    void inflateActionBarCustomView();

    boolean isShowing();

    boolean isVideoPlayerSupportActionBarAdjust();

    void onConfigurationChanged(Configuration configuration);

    void prepareViews();

    void refreshTopBarAllElements();

    void refreshTopBarInfo(BaseDataItem baseDataItem);

    void refreshTopBarLocation(int i, int i2);

    void refreshTopBarLockEnter(boolean z, boolean z2);

    void refreshTopBarMotionPhotoEnter(boolean z, View.OnClickListener onClickListener);

    void refreshTopBarSpecialTypeEnter(BaseDataItem baseDataItem, View.OnClickListener onClickListener);

    void release();

    void setAccessibilityDelegateFor(View view);

    void setTopBarContentVisibility(boolean z);

    void show(boolean z);

    void showLockButtonGuide();
}
