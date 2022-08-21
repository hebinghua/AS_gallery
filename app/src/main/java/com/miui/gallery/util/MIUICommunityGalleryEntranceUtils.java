package com.miui.gallery.util;

import android.net.Uri;
import com.miui.os.Rom;

/* loaded from: classes2.dex */
public class MIUICommunityGalleryEntranceUtils {
    public static final boolean IS_AVAILABLE = !Rom.IS_INTERNATIONAL;

    public static int getCommunityVersionCode() {
        return MiscUtil.getAppVersionCode("com.xiaomi.vipaccount");
    }

    public static Uri getCommunityUri(int i) {
        if (i >= 20000) {
            return Uri.parse("mio://vipaccount.miui.com/takepicture?ref=picture_discover");
        }
        return Uri.parse("https://web.vip.miui.com/page/info/mio/mio/singleBoard?boardId=17855583&ref=picture_discover");
    }
}
