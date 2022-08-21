package com.miui.gallery.vlog.caption;

import com.miui.gallery.net.downloadqueues.IZipFileConfig;
import com.miui.gallery.vlog.home.VlogConfig;
import java.io.File;

/* loaded from: classes2.dex */
public class CaptionStyleZipFileConfig implements IZipFileConfig {
    @Override // com.miui.gallery.net.downloadqueues.IZipFileConfig
    public String getZipPath(long j) {
        return VlogConfig.CAPTION_ASSET_PATH + File.separator + j + ".zip";
    }

    @Override // com.miui.gallery.net.downloadqueues.IZipFileConfig
    public String getUnzipPath() {
        return VlogConfig.CAPTION_ASSET_PATH;
    }
}
