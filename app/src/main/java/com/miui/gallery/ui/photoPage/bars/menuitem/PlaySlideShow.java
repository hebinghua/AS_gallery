package com.miui.gallery.ui.photoPage.bars.menuitem;

import android.os.Bundle;
import android.text.TextUtils;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.ui.SlideShowFragment;
import com.miui.gallery.view.menu.IMenuItem;

/* loaded from: classes2.dex */
public class PlaySlideShow extends BaseMenuItemDelegate {
    public static PlaySlideShow instance(IMenuItem iMenuItem) {
        return new PlaySlideShow(iMenuItem);
    }

    public PlaySlideShow(IMenuItem iMenuItem) {
        super(iMenuItem);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void onClick(BaseDataItem baseDataItem) {
        String originalPath;
        if (!this.isFunctionInit) {
            return;
        }
        this.mOwner.postRecordCountEvent(getItemClickEventCategory(baseDataItem), "play_slide_show");
        if (this.mMenuItemManager.isNeedProjectEnter()) {
            return;
        }
        Bundle bundle = this.mDataProvider.getFieldData().mArguments;
        bundle.putBoolean(GalleryPreferences.PrefKeys.SLIDESHOW_LOOP, GalleryPreferences.SlideShow.isSlideShowLoop());
        bundle.putInt("photo_init_position", this.mDataProvider.getFieldData().mCurrent.getPosition());
        if (TextUtils.isEmpty(baseDataItem.getOriginalPath())) {
            originalPath = baseDataItem.getThumnailPath();
        } else {
            originalPath = baseDataItem.getOriginalPath();
        }
        bundle.putParcelable("photo_transition_data", new ImageLoadParams.Builder().setKey(baseDataItem.getKey()).setFilePath(originalPath).setInitPosition(this.mDataProvider.getFieldData().mCurrent.getPosition()).setMimeType(baseDataItem.getMimeType()).setSecretKey(baseDataItem.getSecretKey()).setFileLength(baseDataItem.getSize()).setImageWidth(baseDataItem.getWidth()).setImageHeight(baseDataItem.getHeight()).setTargetSize(Config$ThumbConfig.get().sMicroTargetSize).build());
        bundle.putBoolean("is_secret_photo", baseDataItem.isSecret());
        SlideShowFragment.showSlideShowFragment((BaseActivity) this.mContext, bundle);
        this.mDataProvider.getFieldData().isPlaySlideshow = true;
        TrackController.trackClick("403.11.5.1.11169", AutoTracking.getRef());
    }
}
