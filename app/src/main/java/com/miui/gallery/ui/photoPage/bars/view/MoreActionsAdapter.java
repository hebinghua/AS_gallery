package com.miui.gallery.ui.photoPage.bars.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.ui.photoPage.bars.menuitem.Favorite;
import com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate;
import com.miui.gallery.view.menu.IMenuItem;
import com.miui.gallery.view.menu.MenuView$ItemView;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class MoreActionsAdapter extends BaseAdapter {
    public final ArrayList<IMenuItemDelegate> mItems;
    public final IViewProvider mViewProvider;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getViewTypeCount() {
        return 2;
    }

    public MoreActionsAdapter(ArrayList<IMenuItemDelegate> arrayList, IViewProvider iViewProvider) {
        this.mItems = arrayList;
        this.mViewProvider = iViewProvider;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mItems.size();
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getItemViewType(int i) {
        return this.mItems.get(i) instanceof Favorite ? 1 : 0;
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = this.mViewProvider.getTopMenuMoreItemView(viewGroup, getItemViewType(i));
        }
        ((MenuView$ItemView) view).initialize(mo1640getItem(i), 0);
        setPadding(view, i);
        return view;
    }

    @Override // android.widget.Adapter
    /* renamed from: getItem */
    public IMenuItem mo1640getItem(int i) {
        return this.mItems.get(i).getItemDataState();
    }

    public final void setPadding(View view, int i) {
        int dimensionPixelSize;
        int dimensionPixelSize2;
        int paddingStart = view.getPaddingStart();
        view.getPaddingTop();
        int paddingEnd = view.getPaddingEnd();
        view.getPaddingBottom();
        if (getCount() == 1) {
            dimensionPixelSize = GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(R.dimen.drop_down_menu_padding_small);
            dimensionPixelSize2 = GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(R.dimen.drop_down_menu_padding_small);
        } else if (i == 0) {
            dimensionPixelSize = GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(R.dimen.drop_down_menu_padding_large);
            dimensionPixelSize2 = GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(R.dimen.drop_down_menu_padding_small);
        } else if (i == getCount() - 1) {
            dimensionPixelSize = GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(R.dimen.drop_down_menu_padding_small);
            dimensionPixelSize2 = GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(R.dimen.drop_down_menu_padding_large);
        } else {
            dimensionPixelSize = GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(R.dimen.drop_down_menu_padding_small);
            dimensionPixelSize2 = GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(R.dimen.drop_down_menu_padding_small);
        }
        view.setPaddingRelative(paddingStart, dimensionPixelSize, paddingEnd, dimensionPixelSize2);
    }
}
