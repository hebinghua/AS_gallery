package com.miui.gallery.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.R;
import com.miui.gallery.provider.GalleryOpenProvider;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.List;

/* loaded from: classes2.dex */
public class ShareUtil {
    public static Uri getSharedUri(String str) {
        if (!TextUtils.isEmpty(str)) {
            return GalleryOpenProvider.translateToContent(str);
        }
        return null;
    }

    public static void share(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            DefaultLogger.e("ShareUtil", "share outFilePath is null");
            return;
        }
        Uri sharedUri = getSharedUri(str);
        String mimeType = BaseFileMimeUtil.getMimeType(str);
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.setType(mimeType);
        intent.putExtra("android.intent.extra.STREAM", sharedUri);
        intent.addFlags(1);
        List<ResolveInfo> queryIntentActivities = context.getPackageManager().queryIntentActivities(intent, 65536);
        if (!BaseMiscUtil.isValid(queryIntentActivities)) {
            DefaultLogger.e("ShareUtil", "doShare: resInfoList is invalid.");
            return;
        }
        for (ResolveInfo resolveInfo : queryIntentActivities) {
            context.grantUriPermission(resolveInfo.activityInfo.packageName, sharedUri, 1);
        }
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.send_multiple_files_hint_format, FormatUtil.formatFileSize(context, BaseFileUtils.getFileSize(str)))));
    }
}
