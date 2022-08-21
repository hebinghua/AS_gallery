package com.miui.gallery.ui;

import android.net.Uri;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.share.PrepareItem;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.List;

/* loaded from: classes2.dex */
public class PhotoPagerSamplingStatHelper {
    public static Uri mEditorSavedUri;

    public static void onImageShared(List<? extends PrepareItem> list) {
        Uri uri;
        if (!BaseMiscUtil.isValid(list)) {
            return;
        }
        if (list.size() == 1 && (uri = mEditorSavedUri) != null && uri.equals(list.get(0).getSrcUri())) {
            DefaultLogger.d("PhotoPagerSamplingStatHelper", "User share the photo after edit.");
            SamplingStatHelper.recordCountEvent("photo", "photo_share_after_edit");
        }
        mEditorSavedUri = null;
    }

    public static void onEditorSaved(Uri uri) {
        mEditorSavedUri = uri;
    }

    public static void onDestroy() {
        mEditorSavedUri = null;
    }
}
