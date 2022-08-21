package com.miui.gallery.ui.photoPage.bars.menuitem;

import android.content.DialogInterface;
import android.net.Uri;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.ui.photoPage.bars.menuitem.DownloadOriginal;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.DialogUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.IncompatibleMediaType;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.WallpaperUtils;
import com.miui.gallery.view.menu.IMenuItem;
import java.io.File;

/* loaded from: classes2.dex */
public class SetWallPaper extends BaseMenuItemDelegate {
    /* renamed from: $r8$lambda$5SXFPRIVsZ-1oPO3YdwNdy5kKlA */
    public static /* synthetic */ void m1635$r8$lambda$5SXFPRIVsZ1oPO3YdwNdy5kKlA(SetWallPaper setWallPaper, String str, BaseDataItem baseDataItem) {
        setWallPaper.lambda$onClick$0(str, baseDataItem);
    }

    public static /* synthetic */ void $r8$lambda$Yl1tihUDCCJgQUWJqaO_e14s2fA(SetWallPaper setWallPaper, BaseDataItem baseDataItem, DialogInterface dialogInterface, int i) {
        setWallPaper.lambda$onClick$1(baseDataItem, dialogInterface, i);
    }

    public static /* synthetic */ void $r8$lambda$l0ZusQ6WGvbcmd4ocE2CXLRjtEQ(SetWallPaper setWallPaper, BaseDataItem baseDataItem, DialogInterface dialogInterface, int i) {
        setWallPaper.lambda$onClick$2(baseDataItem, dialogInterface, i);
    }

    public static SetWallPaper instance(IMenuItem iMenuItem) {
        return new SetWallPaper(iMenuItem);
    }

    public SetWallPaper(IMenuItem iMenuItem) {
        super(iMenuItem);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void onClick(final BaseDataItem baseDataItem) {
        if (!this.isFunctionInit) {
            return;
        }
        if (!IncompatibleMediaType.isUnsupportedMediaType(baseDataItem.getMimeType()) && this.mMenuItemManager.isNeedDownloadOriginal()) {
            DialogUtil.showInfoDialog(this.mContext, (int) R.string.set_as_wallpaper_description, (int) R.string.set_as_wallpaper_title, (int) R.string.yes, (int) R.string.no, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.SetWallPaper$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    SetWallPaper.$r8$lambda$Yl1tihUDCCJgQUWJqaO_e14s2fA(SetWallPaper.this, baseDataItem, dialogInterface, i);
                }
            }, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.SetWallPaper$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    SetWallPaper.$r8$lambda$l0ZusQ6WGvbcmd4ocE2CXLRjtEQ(SetWallPaper.this, baseDataItem, dialogInterface, i);
                }
            });
        } else {
            setWallpaper(baseDataItem.getContentUriForExternal(), baseDataItem.getMimeType());
        }
        this.mOwner.postRecordCountEvent(getItemClickEventCategory(baseDataItem), "set_wallpaper_click");
        TrackController.trackClick("403.11.5.1.11167", AutoTracking.getRef());
    }

    public /* synthetic */ void lambda$onClick$0(String str, BaseDataItem baseDataItem) {
        setWallpaper(str);
    }

    public /* synthetic */ void lambda$onClick$1(BaseDataItem baseDataItem, DialogInterface dialogInterface, int i) {
        this.mMenuItemManager.downloadOrigin(baseDataItem, new DownloadOriginal.DownloadCallback() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.SetWallPaper$$ExternalSyntheticLambda2
            @Override // com.miui.gallery.ui.photoPage.bars.menuitem.DownloadOriginal.DownloadCallback
            public final void downloadSuccess(String str, BaseDataItem baseDataItem2) {
                SetWallPaper.m1635$r8$lambda$5SXFPRIVsZ1oPO3YdwNdy5kKlA(SetWallPaper.this, str, baseDataItem2);
            }
        });
    }

    public /* synthetic */ void lambda$onClick$2(BaseDataItem baseDataItem, DialogInterface dialogInterface, int i) {
        setWallpaper(baseDataItem.getContentUriForExternal(), baseDataItem.getMimeType());
    }

    public final void setWallpaper(String str) {
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag("PhotoPageFragment_MenuManager_MenuItem", "setWallpaper"));
        if (documentFile == null || !documentFile.exists()) {
            ToastUtils.makeText(this.mContext, (int) R.string.set_wallpaper_get_image_failed);
            return;
        }
        setWallpaper(Uri.fromFile(new File(str)), BaseFileMimeUtil.getMimeType(str));
    }

    public final void setWallpaper(Uri uri, String str) {
        if (uri == null) {
            ToastUtils.makeText(this.mContext, (int) R.string.set_wallpaper_get_image_failed);
            return;
        }
        WallpaperUtils.setWallPapers(this.mContext, uri, str);
        this.mOwner.postRecordCountEvent("photo", "set_as_wallpaper");
    }
}
