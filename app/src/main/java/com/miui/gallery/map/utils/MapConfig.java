package com.miui.gallery.map.utils;

import com.miui.gallery.preference.MemoryPreferenceHelper;

/* loaded from: classes2.dex */
public interface MapConfig {
    public static final Float FOCUS_ZOOM_LEVEL = Float.valueOf(19.0f);
    public static final Float OVERVIEW_ZOOM_LEVEL = Float.valueOf(5.15f);
    public static final Float MIN_CLUSTER_ZOOM_LEVEL = Float.valueOf(0.5f);
    public static final Float MAP_MIN_SHOW_LEVEL = Float.valueOf(4.0f);
    public static final Float MAP_MAX_SHOW_LEVEL = Float.valueOf(21.0f);

    static boolean checkMapCustomStyleAvailable(boolean z) {
        if (z) {
            return MemoryPreferenceHelper.getBoolean("is_map_night_custom_style_available", false);
        }
        return MemoryPreferenceHelper.getBoolean("is_map_light_custom_style_available", false);
    }

    static void setMapCustomStyleAvailable(boolean z) {
        if (z) {
            MemoryPreferenceHelper.putBoolean("is_map_night_custom_style_available", true);
        } else {
            MemoryPreferenceHelper.putBoolean("is_map_light_custom_style_available", true);
        }
    }
}
