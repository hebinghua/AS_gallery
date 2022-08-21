package com.miui.gallery.vlog.clip;

import com.miui.gallery.net.downloadqueues.IZipFileConfig;
import com.miui.gallery.vlog.home.VlogConfig;
import java.io.File;

/* loaded from: classes2.dex */
public class TransZipFileConfig implements IZipFileConfig {
    @Override // com.miui.gallery.net.downloadqueues.IZipFileConfig
    public String getZipPath(long j) {
        return VlogConfig.TRANS_PATH + File.separator + j + ".zip";
    }

    @Override // com.miui.gallery.net.downloadqueues.IZipFileConfig
    public String getUnzipPath() {
        return VlogConfig.TRANS_PATH;
    }
}
