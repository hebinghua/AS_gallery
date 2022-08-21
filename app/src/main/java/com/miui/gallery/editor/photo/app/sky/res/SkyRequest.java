package com.miui.gallery.editor.photo.app.sky.res;

import com.miui.gallery.editor.photo.app.sky.res.ResourceFetcher;
import com.miui.gallery.net.fetch.Request;
import java.io.File;

/* loaded from: classes2.dex */
public class SkyRequest extends Request {
    public SkyRequest(String str, long j) {
        super(str, j);
    }

    @Override // com.miui.gallery.net.fetch.Request
    public File getDestDir() {
        return ResourceFetcher.ResourceFileConfig.resItemDir(this.mName, this.mId);
    }

    @Override // com.miui.gallery.net.fetch.Request
    public File getZipFile() {
        return ResourceFetcher.ResourceFileConfig.resItemZipFile(this.mName);
    }

    @Override // com.miui.gallery.net.fetch.Request
    public void deleteHistoricVersion() {
        ResourceFetcher.ResourceFileConfig.deleteHistoricVersion(this.mName);
    }
}
