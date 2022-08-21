package com.miui.gallery.ui.photoPage.bars.menuitem;

import android.content.DialogInterface;
import android.net.Uri;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.CloudItem;
import com.miui.gallery.ui.photoPage.bars.menuitem.DownloadOriginal;
import com.miui.gallery.util.DialogUtil;
import com.miui.gallery.util.IncompatibleMediaType;
import com.miui.gallery.util.SlideWallpaperUtils;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.view.menu.IMenuItem;

/* loaded from: classes2.dex */
public class SetSlideWallPaper extends BaseMenuItemDelegate {
    public static /* synthetic */ void $r8$lambda$721PL4W_lvyn1v1Kh_yMGDc9qgw(SetSlideWallPaper setSlideWallPaper, BaseDataItem baseDataItem, DialogInterface dialogInterface, int i) {
        setSlideWallPaper.lambda$onClick$2(baseDataItem, dialogInterface, i);
    }

    public static /* synthetic */ void $r8$lambda$QqvTpmJjxGhpUblUgrR_CeCXRxk(SetSlideWallPaper setSlideWallPaper, BaseDataItem baseDataItem, DialogInterface dialogInterface, int i) {
        setSlideWallPaper.lambda$onClick$1(baseDataItem, dialogInterface, i);
    }

    public static /* synthetic */ void $r8$lambda$V1LpRcqBw0jvCMd2z6FtLakC53w(SetSlideWallPaper setSlideWallPaper, BaseDataItem baseDataItem, String str, BaseDataItem baseDataItem2) {
        setSlideWallPaper.lambda$onClick$0(baseDataItem, str, baseDataItem2);
    }

    public static SetSlideWallPaper instance(IMenuItem iMenuItem) {
        return new SetSlideWallPaper(iMenuItem);
    }

    public SetSlideWallPaper(IMenuItem iMenuItem) {
        super(iMenuItem);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void onClick(final BaseDataItem baseDataItem) {
        if (!this.isFunctionInit) {
            return;
        }
        if (!IncompatibleMediaType.isUnsupportedMediaType(baseDataItem.getMimeType()) && this.mMenuItemManager.isNeedDownloadOriginal()) {
            DialogUtil.showInfoDialog(this.mContext, (int) R.string.set_as_slide_wallpaper_description, (int) R.string.set_as_slide_wallpaper_title, (int) R.string.yes, (int) R.string.no, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.SetSlideWallPaper$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    SetSlideWallPaper.$r8$lambda$QqvTpmJjxGhpUblUgrR_CeCXRxk(SetSlideWallPaper.this, baseDataItem, dialogInterface, i);
                }
            }, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.SetSlideWallPaper$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    SetSlideWallPaper.$r8$lambda$721PL4W_lvyn1v1Kh_yMGDc9qgw(SetSlideWallPaper.this, baseDataItem, dialogInterface, i);
                }
            });
        } else {
            setSlideWallpaper(baseDataItem.getContentUriForExternal(), baseDataItem instanceof CloudItem ? ((CloudItem) baseDataItem).getSha1() : null);
        }
        this.mOwner.postRecordCountEvent(getItemClickEventCategory(baseDataItem), "set_slide_wallpaper_click");
        TrackController.trackClick("403.11.5.1.11168", AutoTracking.getRef());
    }

    public /* synthetic */ void lambda$onClick$1(final BaseDataItem baseDataItem, DialogInterface dialogInterface, int i) {
        this.mMenuItemManager.downloadOrigin(baseDataItem, new DownloadOriginal.DownloadCallback() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.SetSlideWallPaper$$ExternalSyntheticLambda2
            @Override // com.miui.gallery.ui.photoPage.bars.menuitem.DownloadOriginal.DownloadCallback
            public final void downloadSuccess(String str, BaseDataItem baseDataItem2) {
                SetSlideWallPaper.$r8$lambda$V1LpRcqBw0jvCMd2z6FtLakC53w(SetSlideWallPaper.this, baseDataItem, str, baseDataItem2);
            }
        });
    }

    public /* synthetic */ void lambda$onClick$0(BaseDataItem baseDataItem, String str, BaseDataItem baseDataItem2) {
        setSlideWallpaper(baseDataItem.getContentUriForExternal(), baseDataItem instanceof CloudItem ? ((CloudItem) baseDataItem).getSha1() : null);
    }

    public /* synthetic */ void lambda$onClick$2(BaseDataItem baseDataItem, DialogInterface dialogInterface, int i) {
        setSlideWallpaper(baseDataItem.getContentUriForExternal(), baseDataItem instanceof CloudItem ? ((CloudItem) baseDataItem).getSha1() : null);
    }

    public final void setSlideWallpaper(Uri uri, String str) {
        if (uri == null) {
            ToastUtils.makeText(this.mContext, (int) R.string.set_wallpaper_get_image_failed);
        } else {
            SlideWallpaperUtils.setSlideWallpaper(this.mContext, uri, str);
        }
    }
}
