package com.miui.gallery.ui.photoPage.bars.view;

import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.ui.photoPage.bars.PhotoPageActionBarManager;

/* loaded from: classes2.dex */
public interface IPhotoPageTopBar {
    void dismissPopMenu();

    View getView();

    void hide();

    void onActivityConfigurationChanged(Configuration configuration);

    void onOrientationChanged(int i, int i2);

    void playLockButtonAnimation(boolean z);

    void setCacheHolder(PhotoPageActionBarManager.BarSelector.CacheHolder cacheHolder);

    void setLocation(Context context, BaseDataItem baseDataItem, boolean z);

    void setLockButtonLock(boolean z);

    void setLockButtonVisible(boolean z);

    void setOperationViewClickListener(View.OnClickListener onClickListener);

    void setOperationViewVisibility(int i);

    void setRotateButtonVisible(boolean z);

    void setSpecialTypeEnterClickListener(View.OnClickListener onClickListener);

    void setSpecialTypeEnterVisible(boolean z);

    void setSubTitle(String str);

    void setTitle(String str);

    void show();

    void showLockButtonGuide();

    void updateSpecialTypeEnter(int i, int i2);
}
