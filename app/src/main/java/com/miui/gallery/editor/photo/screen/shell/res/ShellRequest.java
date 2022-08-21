package com.miui.gallery.editor.photo.screen.shell.res;

import com.miui.gallery.editor.photo.screen.shell.res.ShellResourceFetcher;
import com.miui.gallery.net.fetch.Request;
import java.io.File;

/* loaded from: classes2.dex */
public class ShellRequest extends Request {
    public ShellRequest(long j) {
        super("", j);
    }

    @Override // com.miui.gallery.net.fetch.Request
    public File getDestDir() {
        return ShellResourceFetcher.ShellResourceFileConfig.resItemDir(this.mId);
    }

    @Override // com.miui.gallery.net.fetch.Request
    public File getZipFile() {
        return ShellResourceFetcher.ShellResourceFileConfig.resItemZipFile();
    }

    @Override // com.miui.gallery.net.fetch.Request
    public void deleteHistoricVersion() {
        ShellResourceFetcher.ShellResourceFileConfig.deleteHistoricVersion();
    }
}
