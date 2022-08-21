package com.miui.gallery.editor.photo.app.sky.sdk;

import com.miui.gallery.editor.photo.app.sky.sdk.IVideoExporter;

/* loaded from: classes2.dex */
public class SkyVideoExporter implements IVideoExporter {
    @Override // com.miui.gallery.editor.photo.app.sky.sdk.IVideoExporter
    public void release() {
    }

    @Override // com.miui.gallery.editor.photo.app.sky.sdk.IVideoExporter
    public int export(String str) {
        return DynamicSky.INSTANCE.export(str);
    }

    @Override // com.miui.gallery.editor.photo.app.sky.sdk.IVideoExporter
    public void cancel() {
        DynamicSky.INSTANCE.cancel();
    }

    @Override // com.miui.gallery.editor.photo.app.sky.sdk.IVideoExporter
    public void setCallback(IVideoExporter.Callback callback) {
        DynamicSky.INSTANCE.setCallback(callback);
    }
}
