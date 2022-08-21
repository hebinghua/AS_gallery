package com.miui.gallery.ui.photoPage.bars.menuitem;

import android.content.DialogInterface;
import android.text.TextUtils;
import com.miui.gallery.R;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.ui.photoPage.bars.menuitem.DownloadOriginal;
import com.miui.gallery.util.DialogUtil;
import com.miui.gallery.util.IncompatibleMediaType;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.VideoWallpaperUtils;
import com.miui.gallery.view.menu.IMenuItem;

/* loaded from: classes2.dex */
public class SetVideoWallPaper extends BaseMenuItemDelegate {
    /* renamed from: $r8$lambda$_RJlintKcmwB0ie-c5Pi4RBZheU */
    public static /* synthetic */ void m1634$r8$lambda$_RJlintKcmwB0iec5Pi4RBZheU(SetVideoWallPaper setVideoWallPaper, BaseDataItem baseDataItem, DialogInterface dialogInterface, int i) {
        setVideoWallPaper.lambda$onClick$1(baseDataItem, dialogInterface, i);
    }

    public static /* synthetic */ void $r8$lambda$jNKSPpGdxvP1o2YBUFo2sIIyo4s(SetVideoWallPaper setVideoWallPaper, DialogInterface dialogInterface, int i) {
        setVideoWallPaper.lambda$onClick$2(dialogInterface, i);
    }

    public static /* synthetic */ void $r8$lambda$zwf_WL5AlTqRKorlJxcIcCwJ45o(SetVideoWallPaper setVideoWallPaper, BaseDataItem baseDataItem, String str, BaseDataItem baseDataItem2) {
        setVideoWallPaper.lambda$onClick$0(baseDataItem, str, baseDataItem2);
    }

    public static SetVideoWallPaper instance(IMenuItem iMenuItem) {
        return new SetVideoWallPaper(iMenuItem);
    }

    public SetVideoWallPaper(IMenuItem iMenuItem) {
        super(iMenuItem);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void onClick(final BaseDataItem baseDataItem) {
        if (!this.isFunctionInit) {
            return;
        }
        if (!IncompatibleMediaType.isUnsupportedMediaType(baseDataItem.getMimeType()) && this.mMenuItemManager.isNeedDownloadOriginal()) {
            DialogUtil.showInfoDialog(this.mContext, (int) R.string.set_as_video_wallpaper_description, (int) R.string.set_as_video_wallpaper_title, (int) R.string.yes, (int) R.string.no, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.SetVideoWallPaper$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    SetVideoWallPaper.m1634$r8$lambda$_RJlintKcmwB0iec5Pi4RBZheU(SetVideoWallPaper.this, baseDataItem, dialogInterface, i);
                }
            }, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.SetVideoWallPaper$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    SetVideoWallPaper.$r8$lambda$jNKSPpGdxvP1o2YBUFo2sIIyo4s(SetVideoWallPaper.this, dialogInterface, i);
                }
            });
        } else {
            setVideoWallpaper(baseDataItem.getOriginalPath());
        }
        this.mOwner.postRecordCountEvent("video", "set_slide_wallpaper_click");
    }

    public /* synthetic */ void lambda$onClick$0(BaseDataItem baseDataItem, String str, BaseDataItem baseDataItem2) {
        setVideoWallpaper(baseDataItem.getOriginalPath());
    }

    public /* synthetic */ void lambda$onClick$1(final BaseDataItem baseDataItem, DialogInterface dialogInterface, int i) {
        this.mMenuItemManager.downloadOrigin(baseDataItem, new DownloadOriginal.DownloadCallback() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.SetVideoWallPaper$$ExternalSyntheticLambda2
            @Override // com.miui.gallery.ui.photoPage.bars.menuitem.DownloadOriginal.DownloadCallback
            public final void downloadSuccess(String str, BaseDataItem baseDataItem2) {
                SetVideoWallPaper.$r8$lambda$zwf_WL5AlTqRKorlJxcIcCwJ45o(SetVideoWallPaper.this, baseDataItem, str, baseDataItem2);
            }
        });
    }

    public /* synthetic */ void lambda$onClick$2(DialogInterface dialogInterface, int i) {
        ToastUtils.makeText(this.mContext, (int) R.string.set_video_wallpaper_failed);
        this.mOwner.postRecordCountEvent("video", "set_slide_wallpaper_fail");
    }

    public final void setVideoWallpaper(String str) {
        if (TextUtils.isEmpty(str)) {
            ToastUtils.makeText(this.mContext, (int) R.string.set_video_wallpaper_failed);
            this.mOwner.postRecordCountEvent("video", "set_slide_wallpaper_fail");
            return;
        }
        VideoWallpaperUtils.setVideoWallpaper(this.mContext, str);
    }
}
