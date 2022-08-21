package com.miui.gallery.ui.photoPage.bars;

import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.app.fragment.GalleryFragment;
import com.miui.gallery.ui.photoPage.PhotoPageOrientationManager;
import com.miui.gallery.ui.photoPage.PhotoPageThemeManager;
import com.miui.gallery.ui.photoPage.bars.view.ActionBarCustomViewBuilder;

/* loaded from: classes2.dex */
public interface IBarsOwner {
    FragmentActivity getActivity();

    ActionBarCustomViewBuilder.CustomViewType getCustomViewType();

    PhotoPageOrientationManager.IPhotoPageOrientationManagerController getOrientationController();

    GalleryFragment getOwnerImpl();

    PhotoPageThemeManager.IPhotoPageThemeManagerController getThemeController();

    void hideBars(boolean z);

    boolean hideNarBarForFullScreenGesture();

    boolean isInTalkBackModel();

    void postRecordCountEvent(String str, String str2);

    void showBars(boolean z);
}
