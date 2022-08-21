package com.miui.gallery.ui.photoPage.bars.menuitem;

import androidx.lifecycle.DefaultLifecycleObserver;
import com.miui.gallery.app.activity.GalleryActivity;
import com.miui.gallery.app.fragment.GalleryFragment;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.FilterResult;
import com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment;
import com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuItemManager;
import com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager;
import com.miui.gallery.ui.photoPage.bars.data.IDataProvider;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.view.menu.IMenuItem;

/* loaded from: classes2.dex */
public abstract class BaseMenuItemDelegate extends AbstractMenuItemDelegate implements DefaultLifecycleObserver {
    public boolean isFunctionInit;
    public BasePhotoPageBarsDelegateFragment.IConfigMenuCallBack mConfigMenuCallBack;
    public GalleryActivity mContext;
    public IDataProvider mDataProvider;
    public FilterResult mFilterResult;
    public GalleryFragment mFragment;
    public IMenuItem mItemDataState;
    public IPhotoPageMenuItemManager mMenuItemManager;
    public int mOrder;
    public PhotoPageMenuManager.IMenuOwner mOwner;

    public static /* synthetic */ void $r8$lambda$R4_5ZdRiSYrejuIeVos1H6ucwMI(BaseMenuItemDelegate baseMenuItemDelegate) {
        baseMenuItemDelegate.lambda$initFunction$0();
    }

    public static /* synthetic */ void $r8$lambda$XGgd87U_woTEzRunVo5iDSBVMAQ(BaseMenuItemDelegate baseMenuItemDelegate, boolean z) {
        baseMenuItemDelegate.lambda$setVisible$2(z);
    }

    /* renamed from: $r8$lambda$YraMREpsqeEJe-El8hyNPZKUeHg */
    public static /* synthetic */ void m1624$r8$lambda$YraMREpsqeEJeEl8hyNPZKUeHg(BaseMenuItemDelegate baseMenuItemDelegate) {
        baseMenuItemDelegate.lambda$invalidate$1();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void prepareMenuData(BaseDataItem baseDataItem, FilterResult filterResult) {
    }

    public BaseMenuItemDelegate(IMenuItem iMenuItem) {
        this.mItemDataState = iMenuItem;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.AbstractMenuItemDelegate
    public void cacheFilterResult(FilterResult filterResult) {
        this.mFilterResult = filterResult;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void setConfigMenuCallBack(BasePhotoPageBarsDelegateFragment.IConfigMenuCallBack iConfigMenuCallBack) {
        this.mConfigMenuCallBack = iConfigMenuCallBack;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public final void initFunction(IDataProvider iDataProvider, IPhotoPageMenuItemManager iPhotoPageMenuItemManager) {
        if (this.isFunctionInit) {
            return;
        }
        DefaultLogger.d("PhotoPageFragment_MenuManager_MenuItem", ((Object) getItemName()) + " initFunction =>");
        this.mDataProvider = iDataProvider;
        this.mMenuItemManager = iPhotoPageMenuItemManager;
        PhotoPageMenuManager.IMenuOwner menuOwner = iPhotoPageMenuItemManager.getMenuOwner();
        this.mOwner = menuOwner;
        this.mFragment = menuOwner.getOwnerImpl();
        GalleryActivity galleryActivity = (GalleryActivity) this.mOwner.getActivity();
        this.mContext = galleryActivity;
        if (galleryActivity == null) {
            return;
        }
        galleryActivity.runOnUiThread(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.BaseMenuItemDelegate$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                BaseMenuItemDelegate.$r8$lambda$R4_5ZdRiSYrejuIeVos1H6ucwMI(BaseMenuItemDelegate.this);
            }
        });
    }

    public /* synthetic */ void lambda$initFunction$0() {
        if (this.mContext == null) {
            return;
        }
        doInitFunction();
        this.mContext.getLifecycle().addObserver(this);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void uninstallFunction() {
        this.isFunctionInit = false;
        DefaultLogger.d("PhotoPageFragment_MenuManager_MenuItem", ((Object) getItemName()) + " uninstallFunction =>");
    }

    public CharSequence getItemName() {
        return this.mItemDataState.getTitle();
    }

    public void doInitFunction() {
        this.isFunctionInit = true;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public IMenuItem getItemDataState() {
        return this.mItemDataState;
    }

    public void invalidate() {
        GalleryActivity galleryActivity = this.mContext;
        if (galleryActivity == null || this.mMenuItemManager == null) {
            return;
        }
        galleryActivity.runOnUiThread(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.BaseMenuItemDelegate$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                BaseMenuItemDelegate.m1624$r8$lambda$YraMREpsqeEJeEl8hyNPZKUeHg(BaseMenuItemDelegate.this);
            }
        });
    }

    public /* synthetic */ void lambda$invalidate$1() {
        this.mMenuItemManager.refreshMenuItem(this);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public final boolean isResident() {
        return this.mItemDataState.isResident();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void setResident(boolean z) {
        this.mItemDataState.setResident(z);
        FilterResult filterResult = this.mFilterResult;
        if (filterResult != null) {
            filterResult.setResident(z);
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public int getOrder() {
        return this.mOrder;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void setOrder(int i) {
        this.mOrder = i;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public int getTitleId() {
        return this.mItemDataState.getTitleId();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void setTitleId(int i) {
        this.mItemDataState.setTitle(i);
        FilterResult filterResult = this.mFilterResult;
        if (filterResult != null) {
            filterResult.setTitleId(i);
        }
        if (isResident()) {
            invalidate();
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void setTitle(CharSequence charSequence) {
        this.mItemDataState.setTitle(charSequence);
        FilterResult filterResult = this.mFilterResult;
        if (filterResult != null) {
            filterResult.setTitleStr(charSequence);
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public CharSequence getTitle() {
        return this.mItemDataState.getTitle();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public int getIconId() {
        return this.mItemDataState.getIconRes();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void setIconId(int i) {
        this.mItemDataState.setIcon(i);
        FilterResult filterResult = this.mFilterResult;
        if (filterResult != null) {
            filterResult.setIconId(i);
        }
        if (isResident()) {
            invalidate();
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public boolean isSupport() {
        return this.mItemDataState.isSupport();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void setSupport(boolean z) {
        this.mItemDataState.setSupport(z);
        FilterResult filterResult = this.mFilterResult;
        if (filterResult != null) {
            filterResult.setSupport(z);
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public boolean isVisible() {
        return this.mItemDataState.isVisible();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void setVisible(final boolean z) {
        this.mItemDataState.setVisible(z);
        FilterResult filterResult = this.mFilterResult;
        if (filterResult != null) {
            filterResult.setVisible(z);
        }
        if (this.mContext == null || this.mMenuItemManager == null || isResident()) {
            return;
        }
        this.mContext.runOnUiThread(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.BaseMenuItemDelegate$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                BaseMenuItemDelegate.$r8$lambda$XGgd87U_woTEzRunVo5iDSBVMAQ(BaseMenuItemDelegate.this, z);
            }
        });
    }

    public /* synthetic */ void lambda$setVisible$2(boolean z) {
        this.mMenuItemManager.refreshNonResidentData(this, z);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public boolean isEnable() {
        return this.mItemDataState.isEnabled();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void setEnable(boolean z) {
        this.mItemDataState.setEnabled(z);
        FilterResult filterResult = this.mFilterResult;
        if (filterResult != null) {
            filterResult.setEnable(z);
        }
        invalidate();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public boolean isCheckable() {
        return this.mItemDataState.isCheckable();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void setCheckable(boolean z) {
        this.mItemDataState.setCheckable(z);
        FilterResult filterResult = this.mFilterResult;
        if (filterResult != null) {
            filterResult.setCheckable(true);
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public boolean isChecked() {
        return this.mItemDataState.isChecked();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void setChecked(boolean z) {
        this.mItemDataState.setCheckable(true);
        this.mItemDataState.setChecked(z);
        FilterResult filterResult = this.mFilterResult;
        if (filterResult != null) {
            filterResult.setCheckable(true);
            this.mFilterResult.setChecked(z);
        }
    }

    public String getItemClickEventCategory(BaseDataItem baseDataItem) {
        return baseDataItem.isSecret() ? "photo_secret" : "photo";
    }
}
