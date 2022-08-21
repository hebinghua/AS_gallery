package com.miui.gallery.vlog.home;

import android.view.View;
import com.miui.gallery.vlog.base.widget.VlogPlayView;
import com.miui.gallery.vlog.view.VlogMenuTopView;

/* loaded from: classes2.dex */
public interface VlogContract$IVlogView {
    void changeVoiceState(boolean z);

    void dismissProgressBar();

    View getLiveWindow();

    VlogMenuTopView getTopView();

    VlogPlayView getVlogPlayView();

    void hideApplyView();

    void hideCustomTitleView();

    void hideProgressView();

    boolean isClickRightTab();

    void setPlayProgressEnable(boolean z);

    void setPlayViewProgress(long j);

    void setSeparatedView(View view);

    void setTopView(View view);

    void showApplyView();

    void showCustomTitleView(View view);

    void showEffectMenuFragment(int i, String str, String str2);

    void showProgressBar();

    void showProgressView();

    void showToast(String str);

    void updateCaptionClearView(View view, boolean z);

    void updateClipList();

    void updateDisplayOperationView(View view, boolean z);

    void updateEffectMenuView(boolean z);

    void updateNavViewAfterExitClipSortView();

    void updateTextEditorView(View view, boolean z);

    void updateTimeView(boolean z);

    void updateVlogBottomNaviView(boolean z);
}
