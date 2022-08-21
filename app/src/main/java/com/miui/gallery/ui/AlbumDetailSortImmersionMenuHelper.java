package com.miui.gallery.ui;

import android.content.Context;
import android.view.View;
import com.miui.gallery.R;
import com.miui.gallery.adapter.SortBy;
import com.miui.gallery.widget.menu.ImmersionMenu;
import com.miui.gallery.widget.menu.ImmersionMenuItem;
import com.miui.gallery.widget.menu.ImmersionMenuListener;
import com.miui.gallery.widget.menu.PhoneImmersionMenu;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class AlbumDetailSortImmersionMenuHelper implements ImmersionMenuListener {
    public Context mContext;
    public boolean mCurrentIsAscOrder;
    public SortBy mCurrentSortBy;
    public OnItemClickListener mOnItemClickListener;
    public PhoneImmersionMenu mPhoneImmersionMenu;
    public boolean mIsRefreshSortToDefault = true;
    public final HashMap<Integer, SortBy> mSortByMap = new HashMap<>();

    /* loaded from: classes2.dex */
    public interface OnItemClickListener {
        void onItemClick(SortBy sortBy);
    }

    public AlbumDetailSortImmersionMenuHelper(Context context, SortBy sortBy, boolean z) {
        this.mContext = context;
        this.mCurrentSortBy = sortBy;
        this.mCurrentIsAscOrder = z;
        initSortByMap();
    }

    public void setSortOrder(SortBy sortBy, boolean z) {
        this.mCurrentSortBy = sortBy;
        this.mCurrentIsAscOrder = z;
    }

    public void setRefreshSortToDefault() {
        this.mIsRefreshSortToDefault = true;
    }

    public final void initSortByMap() {
        this.mSortByMap.put(Integer.valueOf((int) R.id.menu_detail_sort_update_time), SortBy.UPDATE_DATE);
        this.mSortByMap.put(Integer.valueOf((int) R.id.menu_detail_sort_create_time), SortBy.CREATE_DATE);
        this.mSortByMap.put(Integer.valueOf((int) R.id.menu_detail_sort_name), SortBy.NAME);
        this.mSortByMap.put(Integer.valueOf((int) R.id.menu_detail_sort_size), SortBy.SIZE);
    }

    @Override // com.miui.gallery.widget.menu.ImmersionMenuListener
    public void onCreateImmersionMenu(ImmersionMenu immersionMenu) {
        Context context = this.mContext;
        if (context == null) {
            return;
        }
        ImmersionMenuItem checkableWithoutCheckBox = immersionMenu.add(R.id.menu_detail_sort_update_time, context.getString(R.string.menu_detail_sort_update_date)).setCheckableWithoutCheckBox(true);
        ImmersionMenuItem checkableWithoutCheckBox2 = immersionMenu.add(R.id.menu_detail_sort_create_time, this.mContext.getString(R.string.menu_detail_sort_create_date)).setCheckableWithoutCheckBox(true);
        ImmersionMenuItem checkableWithoutCheckBox3 = immersionMenu.add(R.id.menu_detail_sort_name, this.mContext.getString(R.string.menu_detail_sort_name)).setCheckableWithoutCheckBox(true);
        ImmersionMenuItem checkableWithoutCheckBox4 = immersionMenu.add(R.id.menu_detail_sort_size, this.mContext.getString(R.string.menu_detail_sort_size)).setCheckableWithoutCheckBox(true);
        checkableWithoutCheckBox.setRemainWhenClick(true);
        checkableWithoutCheckBox2.setRemainWhenClick(true);
        checkableWithoutCheckBox3.setRemainWhenClick(true);
        checkableWithoutCheckBox4.setRemainWhenClick(true);
    }

    @Override // com.miui.gallery.widget.menu.ImmersionMenuListener
    public boolean onPrepareImmersionMenu(ImmersionMenu immersionMenu) {
        if (this.mContext == null) {
            return false;
        }
        if (this.mIsRefreshSortToDefault) {
            initSortMenu(immersionMenu);
        }
        this.mIsRefreshSortToDefault = false;
        return true;
    }

    public final void initSortMenu(ImmersionMenu immersionMenu) {
        for (int i = 0; i < immersionMenu.size(); i++) {
            ImmersionMenuItem mo1823getItem = immersionMenu.mo1823getItem(i);
            mo1823getItem.setChecked(false);
            mo1823getItem.setIconResource(R.drawable.menu_sort_tranparent);
        }
        ImmersionMenuItem mo1823getItem2 = this.mCurrentSortBy == SortBy.UPDATE_DATE ? immersionMenu.mo1823getItem(0) : null;
        if (this.mCurrentSortBy == SortBy.CREATE_DATE) {
            mo1823getItem2 = immersionMenu.mo1823getItem(1);
        }
        if (this.mCurrentSortBy == SortBy.NAME) {
            mo1823getItem2 = immersionMenu.mo1823getItem(2);
        }
        if (this.mCurrentSortBy == SortBy.SIZE) {
            mo1823getItem2 = immersionMenu.mo1823getItem(3);
        }
        if (mo1823getItem2 != null) {
            mo1823getItem2.setChecked(true);
            if (this.mCurrentIsAscOrder) {
                mo1823getItem2.setIconResource(R.drawable.menu_sort_asc);
            } else {
                mo1823getItem2.setIconResource(R.drawable.menu_sort_desc);
            }
        }
    }

    @Override // com.miui.gallery.widget.menu.ImmersionMenuListener
    public void onImmersionMenuSelected(ImmersionMenu immersionMenu, ImmersionMenuItem immersionMenuItem) {
        OnItemClickListener onItemClickListener;
        if (this.mContext == null || (onItemClickListener = this.mOnItemClickListener) == null) {
            return;
        }
        onItemClickListener.onItemClick(this.mSortByMap.get(Integer.valueOf(immersionMenuItem.getItemId())));
    }

    public void updateMenuItem() {
        PhoneImmersionMenu phoneImmersionMenu = this.mPhoneImmersionMenu;
        if (phoneImmersionMenu != null) {
            ImmersionMenu immersionMenu = phoneImmersionMenu.getImmersionMenu();
            for (int i = 0; i < immersionMenu.size(); i++) {
                ImmersionMenuItem mo1823getItem = immersionMenu.mo1823getItem(i);
                if (this.mCurrentSortBy == this.mSortByMap.get(Integer.valueOf(mo1823getItem.getItemId()))) {
                    mo1823getItem.setChecked(true);
                    if (this.mCurrentIsAscOrder) {
                        mo1823getItem.setIconResource(R.drawable.menu_sort_asc);
                    } else {
                        mo1823getItem.setIconResource(R.drawable.menu_sort_desc);
                    }
                } else {
                    mo1823getItem.setChecked(false);
                    mo1823getItem.setIconResource(R.drawable.menu_sort_tranparent);
                }
            }
            this.mPhoneImmersionMenu.update(immersionMenu);
        }
    }

    public void showImmersionMenu(View view) {
        if (this.mPhoneImmersionMenu == null) {
            this.mPhoneImmersionMenu = new PhoneImmersionMenu(this.mContext, this);
        }
        this.mPhoneImmersionMenu.show(view, null);
    }

    public void dismiss() {
        PhoneImmersionMenu phoneImmersionMenu = this.mPhoneImmersionMenu;
        if (phoneImmersionMenu == null || !phoneImmersionMenu.isShowing()) {
            return;
        }
        this.mPhoneImmersionMenu.dismiss();
    }

    public boolean isShowing() {
        PhoneImmersionMenu phoneImmersionMenu = this.mPhoneImmersionMenu;
        if (phoneImmersionMenu != null) {
            return phoneImmersionMenu.isShowing();
        }
        return false;
    }

    public void setOnClickItemListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
