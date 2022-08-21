package com.miui.gallery.ui.photoPage.bars;

import android.content.res.Configuration;
import android.view.View;
import androidx.lifecycle.DefaultLifecycleObserver;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment;
import com.miui.gallery.ui.photoPage.bars.menuitem.Cast;
import com.miui.gallery.video.VideoFrameSeekBar;

/* loaded from: classes2.dex */
public interface IPhotoPageMenuManager extends DefaultLifecycleObserver {
    void attach();

    Cast.ProjectionManager checkAndCreateProjectionManager();

    void configMenu(BasePhotoPageBarsDelegateFragment.IConfigMenuCallBack iConfigMenuCallBack);

    int getMenuCollapsedHeight();

    void hideMenuView(boolean z);

    void hideMoreActions(boolean z);

    boolean isActivityActive();

    boolean isInMultiWindowMode();

    boolean isInTalkBackModel();

    boolean isMoreActionsShown();

    boolean isVideoPlayerSupportActionBarAdjust();

    void onConfigurationChanged(Configuration configuration);

    void onMenuActionsShown();

    void prepareViews();

    void refreshMenuItems(BaseDataItem baseDataItem);

    void refreshMenuItemsIfPrepared(BaseDataItem baseDataItem);

    void release();

    void restoreMoreActions(boolean z);

    void setCurrentFocusView(View view);

    void setFrameBar(VideoFrameSeekBar videoFrameSeekBar);

    void showMenuView(boolean z);

    void uninstallFunctions();
}
