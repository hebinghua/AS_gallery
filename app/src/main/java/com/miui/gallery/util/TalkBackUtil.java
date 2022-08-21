package com.miui.gallery.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.miui.gallery.R;
import com.miui.gallery.data.LocationManager;
import miuix.pickerwidget.date.DateUtils;

/* loaded from: classes2.dex */
public class TalkBackUtil {
    public static String getContentDescriptionForImage(Context context, long j, String str, String str2) {
        if (context == null || str2 == null) {
            return null;
        }
        return getContentDescriptionForImage(context, j, str, BaseFileMimeUtil.isVideoFromMimeType(str2), BaseFileMimeUtil.isImageFromMimeType(str2));
    }

    public static String getContentDescriptionForImage(Context context, long j, String str, boolean z, boolean z2) {
        if (context == null) {
            return null;
        }
        String generateTitleLine = LocationManager.getInstance().generateTitleLine(str);
        Object formatDateTime = DateUtils.formatDateTime(StaticContext.sGetAndroidContext(), j, 942);
        if (!TextUtils.isEmpty(generateTitleLine)) {
            return context.getString(z ? R.string.talkback_video_with_time_and_location : !z2 ? R.string.talkback_screenshot_with_time_and_location : R.string.talkback_image_with_time_and_location, formatDateTime, generateTitleLine);
        }
        return context.getString(z ? R.string.talkback_video_with_time : !z2 ? R.string.talkback_screenshot_with_time : R.string.talkback_image_with_time, formatDateTime);
    }

    public static void requestAnnouncementEvent(View view, String str) {
        if (view == null || TextUtils.isEmpty(str)) {
            return;
        }
        view.announceForAccessibility(str);
    }
}
