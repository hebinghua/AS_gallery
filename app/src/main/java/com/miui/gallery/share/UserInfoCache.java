package com.miui.gallery.share;

import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;

/* loaded from: classes2.dex */
public class UserInfoCache extends DBCache<String, UserInfo> {
    public static final UserInfoCache sInstance = new UserInfoCache();

    public static UserInfoCache getInstance() {
        return sInstance;
    }

    @Override // com.miui.gallery.share.DBCache
    public Uri getUri() {
        return GalleryCloudUtils.USER_INFO_URI;
    }

    @Override // com.miui.gallery.share.DBCache
    public String newKey(Cursor cursor) {
        return cursor.getString(2);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.share.DBCache
    /* renamed from: newValue */
    public UserInfo mo1388newValue(Cursor cursor) {
        String string = cursor.getString(2);
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        UserInfo userInfo = new UserInfo(string);
        userInfo.setAliasNick(cursor.getString(3));
        userInfo.setMiliaoNick(cursor.getString(4));
        userInfo.setMiliaoIconUrl(cursor.getString(5));
        return userInfo;
    }
}
