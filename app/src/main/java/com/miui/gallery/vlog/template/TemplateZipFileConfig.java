package com.miui.gallery.vlog.template;

import com.miui.gallery.net.downloadqueues.IZipFileConfig;
import com.miui.gallery.vlog.home.VlogConfig;
import java.io.File;

/* loaded from: classes2.dex */
public class TemplateZipFileConfig implements IZipFileConfig {
    @Override // com.miui.gallery.net.downloadqueues.IZipFileConfig
    public String getZipPath(long j) {
        return VlogConfig.TEMPALTE_PATH + File.separator + j + ".zip";
    }

    @Override // com.miui.gallery.net.downloadqueues.IZipFileConfig
    public String getUnzipPath() {
        return VlogConfig.TEMPALTE_PATH;
    }
}
