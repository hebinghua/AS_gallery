package com.xiaomi.video;

import android.media.MediaFormat;
import com.nexstreaming.nexeditorsdk.nexExportFormat;

/* loaded from: classes3.dex */
class MediaInfo {
    public int frameRate;
    public int height;
    public int rotation;
    public int width;

    public MediaInfo(MediaFormat mediaFormat) {
        if (mediaFormat.containsKey(nexExportFormat.TAG_FORMAT_WIDTH)) {
            this.width = mediaFormat.getInteger(nexExportFormat.TAG_FORMAT_WIDTH);
        }
        if (mediaFormat.containsKey(nexExportFormat.TAG_FORMAT_HEIGHT)) {
            this.height = mediaFormat.getInteger(nexExportFormat.TAG_FORMAT_HEIGHT);
        }
        if (mediaFormat.containsKey("frame-rate")) {
            this.frameRate = mediaFormat.getInteger("frame-rate");
        }
        if (mediaFormat.containsKey("rotation-degrees")) {
            this.rotation = mediaFormat.getInteger("rotation-degrees");
        }
    }
}
