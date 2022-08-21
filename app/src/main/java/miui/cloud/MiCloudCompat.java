package miui.cloud;

import com.miui.gallery.storage.constants.StorageConstants;
import com.miui.gallery.util.StorageUtils;
import java.io.File;

/* loaded from: classes3.dex */
public class MiCloudCompat {
    public static final String FACE_HOST;
    public static final String GALLERY_ANONYMOUS_HOST;
    public static final String GALLERY_H5;
    public static final String GALLERY_HOST;
    public static final String SEARCH_ANONYMOUS_HOST;
    public static final String SEARCH_HOST;
    public static final String URL_ACCOUNT_API_BASE_SECURE;
    public static final boolean USE_PREVIEW;
    public static final boolean USE_TEST;
    public static final String VIP_STATUS_HOST;

    public static String getQuantityStringWithUnit(long j) {
        float f = (float) j;
        return f > 1.07374184E8f ? String.format("%1$.2fGB", Float.valueOf(((f / 1024.0f) / 1024.0f) / 1024.0f)) : f > 104857.6f ? String.format("%1$.2fMB", Float.valueOf((f / 1024.0f) / 1024.0f)) : f > 0.0f ? "0.1MB" : "0MB";
    }

    static {
        boolean exists = new File("/data/system/xiaomi_account_preview").exists();
        USE_PREVIEW = exists;
        boolean exists2 = new File(StorageUtils.getPathInPrimaryStorage(StorageConstants.RELATIVE_DIRECTORY_GALLERY_ALBUM + "/url_daily")).exists();
        USE_TEST = exists2;
        if (exists) {
            GALLERY_HOST = "http://galleryapi.micloud.preview.n.xiaomi.net";
            GALLERY_ANONYMOUS_HOST = "http://galleryapi.micloud.preview.n.xiaomi.net";
            URL_ACCOUNT_API_BASE_SECURE = "http://api.account.preview.n.xiaomi.net/pass";
            FACE_HOST = "http://galleryfaceapi.micloud.preview.n.xiaomi.net";
            SEARCH_HOST = "http://gallerysearchapi.micloud.preview.n.xiaomi.net";
            SEARCH_ANONYMOUS_HOST = "http://gallerysearchapi.micloud.preview.n.xiaomi.nett";
            VIP_STATUS_HOST = "http://statusapi.micloud.preview.n.xiaomi.net";
        } else {
            GALLERY_HOST = "http://galleryapi.micloud.xiaomi.net";
            GALLERY_ANONYMOUS_HOST = "http://g.galleryapi.micloud.xiaomi.net";
            URL_ACCOUNT_API_BASE_SECURE = "https://api.account.xiaomi.com/pass";
            FACE_HOST = "http://galleryfaceapi.micloud.xiaomi.net";
            SEARCH_HOST = "http://gallerysearchapi.micloud.xiaomi.net";
            SEARCH_ANONYMOUS_HOST = "http://g.gallerysearchapi.micloud.xiaomi.net";
            VIP_STATUS_HOST = "http://statusapi.micloud.xiaomi.net";
        }
        if (exists2) {
            GALLERY_H5 = "https://daily.i.mi.com";
        } else {
            GALLERY_H5 = "https://i.mi.com";
        }
    }
}
