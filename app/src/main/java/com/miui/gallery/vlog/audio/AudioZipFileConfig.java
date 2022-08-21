package com.miui.gallery.vlog.audio;

import com.miui.gallery.net.downloadqueues.IZipFileConfig;
import com.miui.gallery.vlog.home.VlogConfig;
import java.io.File;

/* loaded from: classes2.dex */
public class AudioZipFileConfig implements IZipFileConfig {
    @Override // com.miui.gallery.net.downloadqueues.IZipFileConfig
    public String getZipPath(long j) {
        return VlogConfig.AUDIO_PATH + File.separator + j + ".zip";
    }

    @Override // com.miui.gallery.net.downloadqueues.IZipFileConfig
    public String getUnzipPath() {
        return VlogConfig.AUDIO_PATH;
    }
}
