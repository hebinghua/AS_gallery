package com.miui.gallery.magic.fetch;

import com.miui.gallery.magic.fetch.MattingResourceFetcher;
import com.miui.gallery.net.fetch.Request;
import java.io.File;

/* loaded from: classes2.dex */
public class MattingRequest extends Request {
    public MattingRequest(String str, long j) {
        super(str, j);
    }

    @Override // com.miui.gallery.net.fetch.Request
    public File getDestDir() {
        return MattingResourceFetcher.ResourceFileConfig.resItemDir(this.mName, this.mId);
    }

    @Override // com.miui.gallery.net.fetch.Request
    public File getZipFile() {
        return MattingResourceFetcher.ResourceFileConfig.resItemZipFile(this.mName);
    }

    @Override // com.miui.gallery.net.fetch.Request
    public void deleteHistoricVersion() {
        MattingResourceFetcher.ResourceFileConfig.deleteHistoricVersion(this.mName);
    }
}
