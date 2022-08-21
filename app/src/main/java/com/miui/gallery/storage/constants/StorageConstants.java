package com.miui.gallery.storage.constants;

import android.os.Environment;
import com.miui.gallery.util.StaticContext;

/* loaded from: classes2.dex */
public final class StorageConstants extends GalleryStorageConstants {
    public static final String ABSOLUTE_DIRECTORY_DEBUG_LOG;
    public static final String ABSOLUTE_DIRECTORY_NETWORK_CACHE_INTERNAL;
    public static final String INTERNAL_CACHE_ABSOLUTE_PATH;
    public static final String INTERNAL_FILE_ABSOLUTE_PATH;
    public static final String RELATIVE_DIRECTORY_CREATIVE;
    public static final String RELATIVE_DIRECTORY_DEBUG;
    public static final String RELATIVE_DIRECTORY_GALLERY_ALBUM;
    public static final String RELATIVE_DIRECTORY_OWNER_ALBUM;
    public static final String RELATIVE_DIRECTORY_SHARER_ALBUM;

    static {
        String absolutePath = StaticContext.sGetAndroidContext().getCacheDir().getAbsolutePath();
        INTERNAL_CACHE_ABSOLUTE_PATH = absolutePath;
        String absolutePath2 = StaticContext.sGetAndroidContext().getFilesDir().getAbsolutePath();
        INTERNAL_FILE_ABSOLUTE_PATH = absolutePath2;
        ABSOLUTE_DIRECTORY_NETWORK_CACHE_INTERNAL = absolutePath + "/request";
        ABSOLUTE_DIRECTORY_DEBUG_LOG = absolutePath2 + "/debug_log";
        RELATIVE_DIRECTORY_CREATIVE = Environment.DIRECTORY_DCIM + "/Creative";
        String str = Environment.DIRECTORY_PICTURES + "/Gallery";
        RELATIVE_DIRECTORY_GALLERY_ALBUM = str;
        RELATIVE_DIRECTORY_OWNER_ALBUM = str + "/owner";
        RELATIVE_DIRECTORY_SHARER_ALBUM = str + "/sharer";
        RELATIVE_DIRECTORY_DEBUG = Environment.DIRECTORY_DOWNLOADS + "/Debug";
    }
}
