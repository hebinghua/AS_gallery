package com.miui.gallery.ui.photoPage.bars.view;

import android.view.ViewGroup;
import androidx.lifecycle.Observer;
import com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate;
import com.miui.gallery.video.VideoFrameSeekBar;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public interface IPhotoPageMenu {
    void addRootLayout(ViewGroup viewGroup);

    int getMenuCollapsedHeight();

    Observer<ArrayList<IMenuItemDelegate>> getNonResidentMenuHelper();

    Observer<ArrayList<IMenuItemDelegate>> getResidentMenuHelper();

    void hideMenuView(boolean z);

    void hideMoreActions(boolean z);

    boolean isMoreActionsShown();

    boolean postDelayed(Runnable runnable, long j);

    void reAddResidentMenuItems(ArrayList<IMenuItemDelegate> arrayList);

    void refreshMenuItem(IMenuItemDelegate iMenuItemDelegate);

    void refreshMoreActionsMaxHeight(float f);

    default void relayoutForConfigChanged(float f) {
    }

    void setFrameBar(VideoFrameSeekBar videoFrameSeekBar);

    void setVisibility(int i);

    void showMenuView(boolean z);

    void showMoreActions(boolean z);

    void toggleMoreActions(boolean z);

    void viewRequestFocus(IMenuItemDelegate iMenuItemDelegate);
}
