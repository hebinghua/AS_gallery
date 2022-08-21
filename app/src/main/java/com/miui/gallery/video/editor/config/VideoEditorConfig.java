package com.miui.gallery.video.editor.config;

import android.content.Context;
import java.io.File;

/* loaded from: classes2.dex */
public class VideoEditorConfig {
    public static String ASSET_INSTALL_ROOT_PATH;
    public static String ASSET_STORE_PATH;
    public static String AUDIO_PATH;
    public static String SMART_EFFECT_PATH;
    public static String WATER_MARK_PATH;

    public static void init(Context context) {
        File externalFilesDir = context.getExternalFilesDir(null);
        if (externalFilesDir == null) {
            externalFilesDir = context.getFilesDir();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(externalFilesDir);
        String str = File.separator;
        sb.append(str);
        sb.append("video_editor");
        AUDIO_PATH = sb.toString() + str + "video_editor_audio_dir";
        String str2 = context.getFilesDir() + str + "video_editor_template";
        ASSET_STORE_PATH = str2 + str + "video_editor_asset_store" + str + "asset_store";
        ASSET_INSTALL_ROOT_PATH = str2 + str + "video_editor_asset_store" + str + "assets";
        String str3 = ASSET_STORE_PATH;
        SMART_EFFECT_PATH = str3;
        WATER_MARK_PATH = str3;
    }
}
