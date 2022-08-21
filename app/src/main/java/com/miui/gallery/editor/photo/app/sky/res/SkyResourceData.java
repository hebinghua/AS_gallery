package com.miui.gallery.editor.photo.app.sky.res;

import androidx.annotation.Keep;

@Keep
/* loaded from: classes2.dex */
public class SkyResourceData {
    private SkyRandomResource data;
    private SkyRandomResponse status;

    public SkyRandomResponse getStatus() {
        return this.status;
    }

    public void setStatus(SkyRandomResponse skyRandomResponse) {
        this.status = skyRandomResponse;
    }

    public SkyRandomResource getData() {
        return this.data;
    }

    public void setData(SkyRandomResource skyRandomResource) {
        this.data = skyRandomResource;
    }
}
