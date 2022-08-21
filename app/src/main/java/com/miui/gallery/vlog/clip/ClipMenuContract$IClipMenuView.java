package com.miui.gallery.vlog.clip;

import androidx.fragment.app.FragmentManager;
import com.miui.gallery.vlog.clip.widget.MultiVideoEditView;
import com.miui.gallery.vlog.home.VlogContract$IVlogView;
import com.miui.gallery.vlog.sdk.interfaces.IVideoClip;

/* loaded from: classes2.dex */
public interface ClipMenuContract$IClipMenuView {
    void enterEditState();

    void exitEditMode();

    ClipEditNavView getClipEditNavView();

    FragmentManager getClipFragmentManager();

    VlogContract$IVlogView getIVlogView();

    MultiVideoEditView getMultiVideoEditView();

    ClipMenuPresenter getPresenter();

    boolean isSpeedViewVisible();

    void onChangeSpeed(IVideoClip iVideoClip, long j);

    void onCuted(IVideoClip iVideoClip, long j);

    void onDeleted(IVideoClip iVideoClip, long j);

    void onInsertVideoCliped(long j);

    void onSingleVideoInit();

    void seekMultiVideoEditView(long j);

    void setSpeed(double d);

    void setSpeedItemEnable(double d);

    void setSpeedViewVisible(boolean z);

    void showTimeView(boolean z);

    void updateCurrentTime(long j);

    void updateMultiVideoEditView();
}
