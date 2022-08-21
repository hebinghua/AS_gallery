package com.miui.gallery.magic.fetch;

import com.miui.gallery.magic.fetch.ArtResourceFetcher;
import com.miui.gallery.net.fetch.Request;
import java.io.File;

/* loaded from: classes2.dex */
public class ArtRequest extends Request {
    public ArtRequest(String str, long j) {
        super(str, j);
    }

    @Override // com.miui.gallery.net.fetch.Request
    public File getDestDir() {
        return ArtResourceFetcher.ResourceFileConfig.resItemDir(this.mName, this.mId);
    }

    @Override // com.miui.gallery.net.fetch.Request
    public File getZipFile() {
        return ArtResourceFetcher.ResourceFileConfig.resItemZipFile(this.mName);
    }

    @Override // com.miui.gallery.net.fetch.Request
    public void deleteHistoricVersion() {
        ArtResourceFetcher.ResourceFileConfig.deleteHistoricVersion(this.mName);
    }
}
