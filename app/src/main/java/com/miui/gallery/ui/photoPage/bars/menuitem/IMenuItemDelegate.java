package com.miui.gallery.ui.photoPage.bars.menuitem;

import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.FilterResult;
import com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment;
import com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuItemManager;
import com.miui.gallery.ui.photoPage.bars.data.IDataProvider;
import com.miui.gallery.ui.photoPage.bars.menuitem.AbstractMenuItemDelegate;
import com.miui.gallery.view.menu.IMenuItem;

/* loaded from: classes2.dex */
public interface IMenuItemDelegate {

    /* loaded from: classes2.dex */
    public interface ClickHelper {
        boolean onMenuItemClick(IMenuItemDelegate iMenuItemDelegate);
    }

    void applyFilterResult(FilterResult filterResult);

    AbstractMenuItemDelegate.ItemDataStateCache getDefaultState();

    int getIconId();

    IMenuItem getItemDataState();

    int getOrder();

    CharSequence getTitle();

    int getTitleId();

    void initFunction(IDataProvider iDataProvider, IPhotoPageMenuItemManager iPhotoPageMenuItemManager);

    boolean isCheckable();

    boolean isChecked();

    boolean isEnable();

    boolean isResident();

    boolean isSupport();

    boolean isVisible();

    void onClick(BaseDataItem baseDataItem);

    void prepareMenuData(BaseDataItem baseDataItem, FilterResult filterResult);

    void saveDefaultState();

    void setCheckable(boolean z);

    void setChecked(boolean z);

    void setConfigMenuCallBack(BasePhotoPageBarsDelegateFragment.IConfigMenuCallBack iConfigMenuCallBack);

    void setEnable(boolean z);

    void setIconId(int i);

    void setOrder(int i);

    void setResident(boolean z);

    void setSupport(boolean z);

    void setTitle(CharSequence charSequence);

    void setTitleId(int i);

    void setVisible(boolean z);

    void uninstallFunction();
}
