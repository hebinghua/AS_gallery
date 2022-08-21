package com.miui.gallery.ui.photoPage.bars.menuitem;

import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.view.menu.IMenuItem;

/* loaded from: classes2.dex */
public class ToPdf extends BaseMenuItemDelegate {
    public static ToPdf instance(IMenuItem iMenuItem) {
        return new ToPdf(iMenuItem);
    }

    public ToPdf(IMenuItem iMenuItem) {
        super(iMenuItem);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void onClick(BaseDataItem baseDataItem) {
        if (!this.isFunctionInit) {
            return;
        }
        IntentUtil.gotoPicToPdfPreviewPage(this.mContext, baseDataItem.getPathDisplayBetter());
        this.mOwner.postRecordCountEvent("pic_to_pdf", "pic_to_pdf_photo_page_click");
        TrackController.trackClick("403.11.5.1.11164", AutoTracking.getRef());
    }
}
