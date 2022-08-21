package com.miui.gallery.storage.constants;

import android.os.Environment;
import java.io.File;

/* loaded from: classes2.dex */
public class MIUIStorageConstants extends AndroidStorageConstants {
    public static final String DIRECTORY_CAMERA_PATH;
    public static final String DIRECTORY_CAMERA_RAW_PATH;
    public static final String DIRECTORY_SCREENRECORDER_PATH;
    public static final String DIRECTORY_SCREENSHOT_PATH = Environment.DIRECTORY_DCIM + File.separator + "Screenshots";

    static {
        String str = Environment.DIRECTORY_DCIM + "/Camera";
        DIRECTORY_CAMERA_PATH = str;
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("/Raw");
        DIRECTORY_CAMERA_RAW_PATH = sb.toString();
        DIRECTORY_SCREENRECORDER_PATH = Environment.DIRECTORY_DCIM + "/screenrecorder";
    }
}
