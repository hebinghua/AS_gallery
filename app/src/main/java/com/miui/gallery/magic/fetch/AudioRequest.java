package com.miui.gallery.magic.fetch;

import com.miui.gallery.magic.fetch.AudioResourceFetcher;
import com.miui.gallery.net.fetch.Request;
import java.io.File;

/* loaded from: classes2.dex */
public class AudioRequest extends Request {
    public AudioRequest(String str, long j) {
        super(str, j);
    }

    @Override // com.miui.gallery.net.fetch.Request
    public File getDestDir() {
        return AudioResourceFetcher.ResourceFileConfig.resItemDir(this.mName, this.mId);
    }

    @Override // com.miui.gallery.net.fetch.Request
    public File getZipFile() {
        return AudioResourceFetcher.ResourceFileConfig.resItemZipFile(this.mName);
    }

    @Override // com.miui.gallery.net.fetch.Request
    public void deleteHistoricVersion() {
        AudioResourceFetcher.ResourceFileConfig.deleteHistoricVersion(this.mName);
    }
}
