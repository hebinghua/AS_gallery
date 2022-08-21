package com.miui.gallery.gallerywidget.ui.editor;

import android.content.Context;
import android.graphics.RectF;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.util.SecurityShareHelper;
import java.io.File;

/* loaded from: classes2.dex */
public class WidgetEditorManager {
    public static String getPicPath(Context context, Uri uri) {
        if (uri != null) {
            if (TextUtils.isEmpty(uri.getScheme())) {
                return uri.getPath();
            }
            File uriToFile = SecurityShareHelper.uriToFile(uri, context);
            if (uriToFile == null) {
                return null;
            }
            return uriToFile.getPath();
        }
        return null;
    }

    public static RectF getRegionRect(float[] fArr) {
        if (fArr == null || fArr.length < 4) {
            return new RectF(0.0f, 0.0f, 1.0f, 1.0f);
        }
        return new RectF(fArr[0], fArr[1], fArr[2], fArr[3]);
    }
}
