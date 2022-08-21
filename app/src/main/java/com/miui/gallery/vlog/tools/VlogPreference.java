package com.miui.gallery.vlog.tools;

import androidx.annotation.Keep;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.preference.PreferenceHelper;

/* loaded from: classes2.dex */
public class VlogPreference extends BaseGalleryPreferences {

    @Keep
    /* loaded from: classes2.dex */
    public interface PrefKeys {
        public static final String VLOG_AUDIO_VERSION = "vlog_audio_version";
        public static final String VLOG_CAPTION_VERSION = "vlog_caption_version";
        public static final String VLOG_FILTER_VERSION = "vlog_filter_version";
        public static final String VLOG_HEADERTAIL_VERSION = "vlog_header_tail_version";
        public static final String VLOG_TEMPLATE_VERSION = "vlog_template_version";
        public static final String VLOG_TRANSITION_VERSION = "vlog_transition_version";
    }

    public static long getVersion(String str, long j) {
        return PreferenceHelper.getLong(str, j);
    }

    public static void setVersion(String str, long j) {
        PreferenceHelper.putLong(str, j);
    }
}
