package com.miui.gallery.editor.photo.app.filter.res;

import com.miui.gallery.editor.photo.app.filter.res.FilterResourceFetcher;
import com.miui.gallery.net.fetch.Request;
import java.io.File;

/* loaded from: classes2.dex */
public class FilterRequest extends Request {
    public FilterRequest(String str, long j) {
        super(str, j);
    }

    @Override // com.miui.gallery.net.fetch.Request
    public File getDestDir() {
        return FilterResourceFetcher.ResourceFileConfig.resItemDir(this.mName, this.mId);
    }

    @Override // com.miui.gallery.net.fetch.Request
    public File getZipFile() {
        return FilterResourceFetcher.ResourceFileConfig.resItemZipFile(this.mName);
    }

    @Override // com.miui.gallery.net.fetch.Request
    public void deleteHistoricVersion() {
        FilterResourceFetcher.ResourceFileConfig.deleteHistoricVersion(this.mName);
    }
}
