package com.miui.gallery.editor.photo.app;

import android.app.Activity;
import android.os.Bundle;
import com.miui.gallery.sdk.editor.Constants;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes2.dex */
public class Sampler {
    public String mCategory;
    public String mPage;

    public Sampler(String str, String str2) {
        this.mCategory = str;
        this.mPage = str2;
    }

    public void recordEvent(String str, String str2, Map<String, String> map) {
        DefaultLogger.d("Sampler", "record eventPrefix: %s, event: %s, params: %s", str, str2, map);
        SamplingStatHelper.recordCountEvent(this.mCategory, String.format(Locale.US, "%s_%s", str, str2), map);
    }

    public void recordEvent(String str, String str2) {
        DefaultLogger.d("Sampler", "record eventPrefix: %s, event: %s", str, str2);
        SamplingStatHelper.recordCountEvent(this.mCategory, String.format(Locale.US, "%s_%s", str, str2));
    }

    public void recordCategory(String str, String str2, Map<String, String> map) {
        DefaultLogger.d("Sampler", "record category: %s, event: %s, params: %s", str, str2, map);
        SamplingStatHelper.recordCountEvent(str, String.format(Locale.US, "%s_%s", this.mCategory, str2), map);
    }

    public void recordCategory(String str, String str2) {
        DefaultLogger.d("Sampler", "record category: %s, event: %s", str, str2);
        SamplingStatHelper.recordCountEvent(str, String.format(Locale.US, "%s_%s", this.mCategory, str2));
    }

    public void recordPageStart(Activity activity) {
        DefaultLogger.d("Sampler", "record page start: %s", this.mPage);
        SamplingStatHelper.recordPageStart(activity, this.mPage);
    }

    public void recordPageEnd(Activity activity) {
        DefaultLogger.d("Sampler", "record page end");
        SamplingStatHelper.recordPageEnd(activity, this.mPage);
    }

    public static Sampler from(Bundle bundle) {
        if (bundle != null && bundle.getBoolean(Constants.EXTRA_IS_SCREENSHOT)) {
            if (bundle.getBoolean(Constants.EXTRA_IS_LONG_SCREENSHOT)) {
                DefaultLogger.d("Sampler", "create long screenshot's sampler");
                return new Sampler("photo_editor_long_screenshot", "photo_editor_long_screenshot");
            }
            DefaultLogger.d("Sampler", "create screenshot's sampler");
            return new Sampler("photo_editor_screenshot", "photo_editor_screenshot");
        }
        DefaultLogger.d("Sampler", "create sampler");
        return new Sampler("photo_editor", "photo_editor_main");
    }
}
