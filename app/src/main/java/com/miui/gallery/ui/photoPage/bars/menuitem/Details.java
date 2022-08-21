package com.miui.gallery.ui.photoPage.bars.menuitem;

import androidx.loader.content.Loader;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.loader.CloudSetLoader;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.view.menu.IMenuItem;

/* loaded from: classes2.dex */
public class Details extends BaseMenuItemDelegate {
    public static Details instance(IMenuItem iMenuItem) {
        return new Details(iMenuItem);
    }

    public Details(IMenuItem iMenuItem) {
        super(iMenuItem);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void onClick(BaseDataItem baseDataItem) {
        Loader currentPhotoLoader;
        if (this.isFunctionInit && (currentPhotoLoader = this.mDataProvider.getCurrentPhotoLoader()) != null) {
            IntentUtil.gotoPhotoDetailPage(this.mContext, baseDataItem, this.mDataProvider.getFieldData().isStartWhenLocked, (currentPhotoLoader instanceof CloudSetLoader) && isCanEditPhotoDate(), this.mMenuItemManager.isNeedDownloadOriginal(), this.mMenuItemManager.isSupportPhotoRename());
            this.mOwner.postRecordCountEvent(getItemClickEventCategory(baseDataItem), "view_detail");
            TrackController.trackClick("403.11.5.1.11170", AutoTracking.getRef());
        }
    }

    public final boolean isCanEditPhotoDate() {
        if (this.mDataProvider.getFieldData().mArguments == null) {
            return false;
        }
        return this.mDataProvider.getFieldData().mArguments.getBoolean("photodetail_is_photo_datetime_editable", true);
    }
}
