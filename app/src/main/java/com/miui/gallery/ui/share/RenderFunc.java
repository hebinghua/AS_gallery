package com.miui.gallery.ui.share;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

/* loaded from: classes2.dex */
public class RenderFunc implements PrepareFunc<PrepareItem> {
    public File mCacheFolder;
    public SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SSS", Locale.US);

    @Override // com.miui.gallery.ui.share.PrepareFunc
    public void release() {
    }

    public RenderFunc(File file) {
        this.mCacheFolder = file;
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x0094  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0099  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x009e  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00b2  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00b7  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00bc  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x00c7  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x00cc  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x00d1  */
    @Override // com.miui.gallery.ui.share.PrepareFunc
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public android.net.Uri prepare(com.miui.gallery.ui.share.PrepareItem r19, com.miui.gallery.ui.share.PrepareProgressCallback<com.miui.gallery.ui.share.PrepareItem> r20) {
        /*
            Method dump skipped, instructions count: 215
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.ui.share.RenderFunc.prepare(com.miui.gallery.ui.share.PrepareItem, com.miui.gallery.ui.share.PrepareProgressCallback):android.net.Uri");
    }
}
