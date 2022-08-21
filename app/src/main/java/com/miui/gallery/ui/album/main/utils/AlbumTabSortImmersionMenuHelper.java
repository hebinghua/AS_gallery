package com.miui.gallery.ui.album.main.utils;

import android.content.Context;
import android.view.View;
import com.miui.gallery.R;
import com.miui.gallery.util.AlbumSortHelper;
import com.miui.gallery.widget.menu.ImmersionMenu;
import com.miui.gallery.widget.menu.ImmersionMenuItem;
import com.miui.gallery.widget.menu.ImmersionMenuListener;
import com.miui.gallery.widget.menu.PhoneImmersionMenu;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public class AlbumTabSortImmersionMenuHelper implements ImmersionMenuListener {
    public final WeakReference<Context> mContext;
    public final OnItemClickListener mOnItemClickListener;
    public int mOrder;
    public PhoneImmersionMenu mPhoneImmersionMenu;
    public int mSort;

    /* loaded from: classes2.dex */
    public interface OnItemClickListener {
        void onItemClick(int i);
    }

    public final int getOrderIcon(int i) {
        return i == 1073741824 ? R.drawable.menu_sort_asc : R.drawable.menu_sort_desc;
    }

    public final int reverseOrder(int i) {
        return i == Integer.MIN_VALUE ? 1073741824 : Integer.MIN_VALUE;
    }

    public AlbumTabSortImmersionMenuHelper(Context context, OnItemClickListener onItemClickListener) {
        this.mContext = new WeakReference<>(context);
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override // com.miui.gallery.widget.menu.ImmersionMenuListener
    public void onCreateImmersionMenu(ImmersionMenu immersionMenu) {
        if (this.mContext.get() == null) {
            return;
        }
        ImmersionMenuItem checkableWithoutCheckBox = immersionMenu.add(R.id.sort_custom, this.mContext.get().getString(R.string.album_sort_type_custom)).setCheckableWithoutCheckBox(true);
        ImmersionMenuItem checkableWithoutCheckBox2 = immersionMenu.add(R.id.menu_detail_sort_name, this.mContext.get().getString(R.string.menu_item_sort_name)).setCheckableWithoutCheckBox(true);
        ImmersionMenuItem checkableWithoutCheckBox3 = immersionMenu.add(R.id.menu_detail_sort_create_time, this.mContext.get().getString(R.string.menu_detail_sort_create_date)).setCheckableWithoutCheckBox(true);
        ImmersionMenuItem checkableWithoutCheckBox4 = immersionMenu.add(R.id.menu_detail_sort_update_time, this.mContext.get().getString(R.string.menu_detail_sort_update_date)).setCheckableWithoutCheckBox(true);
        checkableWithoutCheckBox.setRemainWhenClick(true);
        checkableWithoutCheckBox2.setRemainWhenClick(true);
        checkableWithoutCheckBox3.setRemainWhenClick(true);
        checkableWithoutCheckBox4.setRemainWhenClick(true);
    }

    @Override // com.miui.gallery.widget.menu.ImmersionMenuListener
    public boolean onPrepareImmersionMenu(ImmersionMenu immersionMenu) {
        if (this.mContext.get() == null) {
            return false;
        }
        initDefaultSortMenu(immersionMenu);
        return true;
    }

    public final void initDefaultSortMenu(ImmersionMenu immersionMenu) {
        resetMenuToDefault(immersionMenu);
        int lastAlbumSortSpec = AlbumSortHelper.getLastAlbumSortSpec();
        this.mOrder = AlbumSortHelper.SortSpec.getOrder(lastAlbumSortSpec);
        int sort = AlbumSortHelper.SortSpec.getSort(lastAlbumSortSpec);
        this.mSort = sort;
        setSortAndOrder(sort, this.mOrder);
        int i = this.mSort;
        if (i == 2) {
            changeMenuItemStatus(immersionMenu, immersionMenu.mo1822findItem(R.id.menu_detail_sort_name), this.mOrder);
        } else if (i == 3) {
            changeMenuItemStatus(immersionMenu, immersionMenu.mo1822findItem(R.id.menu_detail_sort_create_time), this.mOrder);
        } else if (i == 4) {
            changeMenuItemStatus(immersionMenu, immersionMenu.mo1822findItem(R.id.menu_detail_sort_update_time), this.mOrder);
        } else {
            changeMenuItemStatus(immersionMenu, immersionMenu.mo1822findItem(R.id.sort_custom), this.mOrder);
        }
    }

    public final void resetMenuToDefault(ImmersionMenu immersionMenu) {
        for (int i = 0; i < immersionMenu.size(); i++) {
            ImmersionMenuItem mo1823getItem = immersionMenu.mo1823getItem(i);
            mo1823getItem.setChecked(false);
            mo1823getItem.setIconResource(R.drawable.menu_sort_tranparent);
        }
    }

    public final void changeMenuItemStatus(ImmersionMenu immersionMenu, ImmersionMenuItem immersionMenuItem, int i) {
        immersionMenuItem.setChecked(true);
        int orderIcon = getOrderIcon(i);
        if (immersionMenuItem.getItemId() != R.id.sort_custom) {
            immersionMenuItem.setIconResource(orderIcon);
        } else {
            immersionMenuItem.setIconResource(R.drawable.menu_sort_tranparent);
        }
        this.mPhoneImmersionMenu.update(immersionMenu);
    }

    public final void setSortAndOrder(int i, int i2) {
        this.mSort = i;
        this.mOrder = i2;
        AlbumSortHelper.setAlbumSortSpec(i, i2);
    }

    @Override // com.miui.gallery.widget.menu.ImmersionMenuListener
    public void onImmersionMenuSelected(ImmersionMenu immersionMenu, ImmersionMenuItem immersionMenuItem) {
        if (this.mContext.get() == null) {
            return;
        }
        resetMenuToDefault(immersionMenu);
        int i = Integer.MIN_VALUE;
        switch (immersionMenuItem.getItemId()) {
            case R.id.menu_detail_sort_create_time /* 2131362886 */:
                if (this.mSort == 3) {
                    i = reverseOrder(this.mOrder);
                }
                setSortAndOrder(3, i);
                this.mOnItemClickListener.onItemClick(2);
                break;
            case R.id.menu_detail_sort_name /* 2131362887 */:
                if (this.mSort == 2) {
                    i = reverseOrder(this.mOrder);
                }
                setSortAndOrder(2, i);
                this.mOnItemClickListener.onItemClick(1);
                break;
            case R.id.menu_detail_sort_update_time /* 2131362889 */:
                if (this.mSort == 4) {
                    i = reverseOrder(this.mOrder);
                }
                setSortAndOrder(4, i);
                this.mOnItemClickListener.onItemClick(3);
                break;
            case R.id.sort_custom /* 2131363400 */:
                if (this.mSort != 1) {
                    setSortAndOrder(1, Integer.MIN_VALUE);
                    this.mOnItemClickListener.onItemClick(0);
                    break;
                }
                break;
        }
        changeMenuItemStatus(immersionMenu, immersionMenuItem, this.mOrder);
    }

    public void showImmersionMenu(View view) {
        if (this.mContext.get() == null) {
            return;
        }
        if (this.mPhoneImmersionMenu == null) {
            this.mPhoneImmersionMenu = new PhoneImmersionMenu(this.mContext.get(), this);
        }
        this.mPhoneImmersionMenu.show(view, null);
    }
}
