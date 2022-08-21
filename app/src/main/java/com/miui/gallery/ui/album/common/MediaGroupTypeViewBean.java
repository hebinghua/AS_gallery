package com.miui.gallery.ui.album.common;

import android.net.Uri;
import com.miui.gallery.ui.album.common.base.BaseViewBean;

/* loaded from: classes2.dex */
public class MediaGroupTypeViewBean extends BaseViewBean {
    public Uri mCoverUri;
    public String mGotoLink;
    public String mTitle;

    public MediaGroupTypeViewBean(long j, Uri uri, String str, String str2) {
        super(j);
        this.mCoverUri = uri;
        this.mTitle = str;
        this.mGotoLink = str2;
    }

    public Uri getCoverUri() {
        return this.mCoverUri;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String getGotoLink() {
        return this.mGotoLink;
    }
}
