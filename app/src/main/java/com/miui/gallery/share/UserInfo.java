package com.miui.gallery.share;

import android.text.TextUtils;

/* loaded from: classes2.dex */
public class UserInfo {
    public String mAliasNick;
    public final String mId;
    public String mMiliaoIconUrl;
    public String mMiliaoNick;

    public UserInfo(String str) {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("Bad id");
        }
        this.mId = str;
    }

    public String getUserId() {
        return this.mId;
    }

    public String getAliasNick() {
        return this.mAliasNick;
    }

    public String getMiliaoNick() {
        return this.mMiliaoNick;
    }

    public String getMiliaoIconUrl() {
        return this.mMiliaoIconUrl;
    }

    public String getMiliaoIconUrl150() {
        return convertUrl("150");
    }

    public final String convertUrl(String str) {
        String str2 = this.mMiliaoIconUrl;
        if (str2 == null) {
            return null;
        }
        int lastIndexOf = str2.lastIndexOf(".");
        return lastIndexOf > 0 ? String.format("%s_%s%s", str2.substring(0, lastIndexOf), str, str2.substring(lastIndexOf)) : str2;
    }

    public String getDisplayName() {
        String readableName = getReadableName();
        return !TextUtils.isEmpty(readableName) ? readableName : this.mId;
    }

    public String getReadableName() {
        if (!TextUtils.isEmpty(this.mMiliaoNick)) {
            return this.mMiliaoNick;
        }
        if (TextUtils.isEmpty(this.mAliasNick)) {
            return null;
        }
        return this.mAliasNick;
    }

    public void setAliasNick(String str) {
        if (str != null) {
            this.mAliasNick = str.trim();
        } else {
            this.mAliasNick = null;
        }
    }

    public void setMiliaoNick(String str) {
        if (str != null) {
            this.mMiliaoNick = str.trim();
        } else {
            this.mMiliaoNick = null;
        }
    }

    public void setMiliaoIconUrl(String str) {
        if (str != null) {
            this.mMiliaoIconUrl = str.trim();
        } else {
            this.mMiliaoIconUrl = null;
        }
    }
}
