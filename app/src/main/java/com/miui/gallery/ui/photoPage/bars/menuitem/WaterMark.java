package com.miui.gallery.ui.photoPage.bars.menuitem;

import android.view.View;
import android.widget.ImageView;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.ui.PhotoPageFragment;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.view.menu.IMenuItem;

/* loaded from: classes2.dex */
public class WaterMark extends BaseMenuItemDelegate {
    public static WaterMark instance(IMenuItem iMenuItem) {
        return new WaterMark(iMenuItem);
    }

    public WaterMark(IMenuItem iMenuItem) {
        super(iMenuItem);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void onClick(BaseDataItem baseDataItem) {
        View findViewById;
        ImageView photoView;
        View view = this.mFragment.getView();
        if (view == null || (findViewById = view.findViewById(R.id.photo_pager)) == null || (photoView = ((PhotoPageFragment) this.mFragment).getPhotoView()) == null) {
            return;
        }
        TrackController.trackClick("403.11.5.1.16478", AutoTracking.getRef());
        IntentUtil.startWatermarkAction(baseDataItem, this.mContext, (PhotoPageFragment) this.mFragment, findViewById, photoView);
    }
}
