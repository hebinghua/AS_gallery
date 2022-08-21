package com.miui.gallery.magic.matting.bean;

import android.net.Uri;

/* loaded from: classes2.dex */
public class BackgroundItem {
    public String background;
    public int index;
    public Uri originUri;

    public BackgroundItem() {
        this.background = "";
    }

    public BackgroundItem(int i, String str, Uri uri) {
        this.background = "";
        this.index = i;
        this.background = str;
        this.originUri = uri;
    }

    public String getBackground() {
        return this.background;
    }

    public int getBackgroundIndex() {
        return this.index;
    }

    public Uri getOriginUri() {
        return this.originUri;
    }

    public void setOriginUri(Uri uri) {
        this.originUri = uri;
    }
}
