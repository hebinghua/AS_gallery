package com.miui.gallery.model.dto;

import android.text.TextUtils;

/* loaded from: classes2.dex */
public class ShareAlbum {
    public String mAlbumId;
    public String mCreatorId;
    public String mOwnerNickName;
    public int mUserCount;

    public String getOwnerName() {
        return TextUtils.isEmpty(this.mOwnerNickName) ? this.mCreatorId : this.mOwnerNickName;
    }
}
