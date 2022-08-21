package com.miui.gallery.ui.photoPage.bars.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuManager;
import com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate;
import com.miui.gallery.ui.photoPage.bars.view.ActionBarCustomViewBuilder;

/* loaded from: classes2.dex */
public interface IViewProvider {
    View getBottomItemView(ViewGroup viewGroup, boolean z);

    View getBottomMenuMoreItemView(ViewGroup viewGroup);

    View getBottomMenuView(ViewGroup viewGroup);

    IPhotoPageMenu getGalleryMenu(IPhotoPageMenuManager iPhotoPageMenuManager, Context context, IMenuItemDelegate.ClickHelper clickHelper, ActionBarCustomViewBuilder.CustomViewType customViewType);

    View getTopBarView(ViewGroup viewGroup, ActionBarCustomViewBuilder.CustomViewType customViewType);

    View getTopItemView(ViewGroup viewGroup);

    View getTopMenuMoreItemView(ViewGroup viewGroup, int i);

    View getTopMenuView(ViewGroup viewGroup);

    View getVideoSeekBarView(ViewGroup viewGroup);

    void prepareActionBarViews();

    void prepareMenuViews();

    void release();

    void startPrepare();
}
