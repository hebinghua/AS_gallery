package com.miui.gallery.cloud.base;

import android.text.TextUtils;
import com.xiaomi.micloudsdk.data.ExtendedAuthToken;

/* loaded from: classes.dex */
public class GalleryExtendedAuthToken {
    public ExtendedAuthToken mToken;

    public GalleryExtendedAuthToken(ExtendedAuthToken extendedAuthToken) {
        this.mToken = extendedAuthToken;
    }

    public String getSecurity() {
        return this.mToken.security;
    }

    public static GalleryExtendedAuthToken parse(String str) {
        ExtendedAuthToken parse;
        if (!TextUtils.isEmpty(str) && (parse = ExtendedAuthToken.parse(str)) != null) {
            return new GalleryExtendedAuthToken(parse);
        }
        return null;
    }
}
