package com.miui.gallery.magic.util;

import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes2.dex */
public class MagicSampler {
    public String mCategory;
    public String mPage;

    public MagicSampler(String str, String str2) {
        this.mCategory = str;
        this.mPage = str2;
    }

    public void recordCategory(String str, String str2, Map<String, String> map) {
        DefaultLogger.d("MagicSampler", "record category: %s, event: %s, params: %s", str, str2, map);
        MagicLog.INSTANCE.showLog("MagicSampler", String.format("record category: %s, event: %s, params: %s", str, str2, map));
        SamplingStatHelper.recordCountEvent(str, String.format(Locale.US, "%s_%s", this.mCategory, str2), map);
    }

    public void recordCategory(String str, String str2) {
        DefaultLogger.d("MagicSampler", "record category: %s, event: %s", str, str2);
        MagicLog.INSTANCE.showLog("MagicSampler", String.format("record category: %s, event: %s", str, str2));
        SamplingStatHelper.recordCountEvent(str, String.format(Locale.US, "%s_%s", this.mCategory, str2));
    }

    public static MagicSampler getInstance() {
        DefaultLogger.d("MagicSampler", "create sampler");
        return new MagicSampler("magic_editor", "");
    }
}
