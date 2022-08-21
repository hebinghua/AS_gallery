package com.qiniu.pili.droid.shortvideo.g;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import com.qiniu.pili.droid.shortvideo.PLVideoFrame;
import java.io.IOException;
import java.nio.ByteBuffer;

/* compiled from: MediaFile.java */
/* loaded from: classes3.dex */
public class f {
    public String a;
    public MediaExtractor b;
    public MediaExtractor c;
    public MediaFormat d;
    public MediaFormat e;

    public f(String str) {
        this(str, true, true);
    }

    public f(String str, boolean z, boolean z2) {
        if (str == null) {
            e.w.e("MediaFile", "Create MediaFile failed, empty path");
            return;
        }
        this.a = str;
        if (z) {
            a(str);
        }
        if (!z2) {
            return;
        }
        b(str);
    }

    public void a() {
        MediaExtractor mediaExtractor = this.b;
        if (mediaExtractor != null) {
            mediaExtractor.release();
            this.b = null;
        }
        MediaExtractor mediaExtractor2 = this.c;
        if (mediaExtractor2 != null) {
            mediaExtractor2.release();
            this.c = null;
        }
    }

    public final boolean a(String str) {
        MediaExtractor mediaExtractor = new MediaExtractor();
        this.b = mediaExtractor;
        try {
            mediaExtractor.setDataSource(str);
            int a = a(this.b, "video/");
            if (a >= 0) {
                this.b.selectTrack(a);
                this.d = this.b.getTrackFormat(a);
                return true;
            }
            e eVar = e.w;
            eVar.e("MediaFile", "failed to select video track: " + this.a);
            return false;
        } catch (IOException e) {
            e.w.e("MediaFile", e.getMessage());
            return false;
        }
    }

    public final boolean b(String str) {
        MediaExtractor mediaExtractor = new MediaExtractor();
        this.c = mediaExtractor;
        try {
            mediaExtractor.setDataSource(str);
            int a = a(this.c, "audio/");
            if (a >= 0) {
                this.c.selectTrack(a);
                this.e = this.c.getTrackFormat(a);
                return true;
            }
            e eVar = e.w;
            eVar.e("MediaFile", "failed to select audio track: " + this.a);
            return false;
        } catch (IOException e) {
            e.w.e("MediaFile", e.getMessage());
            return false;
        }
    }

    public final int a(MediaExtractor mediaExtractor, String str) {
        int trackCount = mediaExtractor.getTrackCount();
        for (int i = 0; i < trackCount; i++) {
            MediaFormat trackFormat = mediaExtractor.getTrackFormat(i);
            String string = trackFormat.getString("mime");
            if (string.startsWith(str)) {
                e eVar = e.w;
                eVar.b("MediaFile", "Extractor selected track " + i + " (" + string + "): " + trackFormat);
                return i;
            }
        }
        return -1;
    }

    public PLVideoFrame a(long j, boolean z, int i, int i2) {
        return b(j * 1000, z, i, i2);
    }

    public PLVideoFrame a(long j, boolean z) {
        return a(j, z, 0, 0);
    }

    @TargetApi(21)
    public final PLVideoFrame b(long j, boolean z, int i, int i2) {
        PLVideoFrame.a aVar;
        e eVar = e.w;
        eVar.c("MediaFile", "getVideoFrame at in Us: " + j + " is key frame: " + z);
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            mediaMetadataRetriever.setDataSource(this.a);
            Bitmap frameAtTime = mediaMetadataRetriever.getFrameAtTime(j, z ? 2 : 3);
            if (frameAtTime != null) {
                Bitmap.Config config = frameAtTime.getConfig();
                if (config == Bitmap.Config.RGB_565) {
                    aVar = PLVideoFrame.a.RGB_565;
                } else if (config == Bitmap.Config.ARGB_8888) {
                    aVar = PLVideoFrame.a.ARGB_8888;
                } else {
                    eVar.d("MediaFile", config + " config not supported");
                }
                if (i != 0 && i2 != 0) {
                    frameAtTime = ThumbnailUtils.extractThumbnail(frameAtTime, i, i2);
                }
                ByteBuffer allocate = ByteBuffer.allocate(frameAtTime.getByteCount());
                frameAtTime.copyPixelsToBuffer(allocate);
                PLVideoFrame pLVideoFrame = new PLVideoFrame();
                pLVideoFrame.setTimestampMs(j / 1000);
                pLVideoFrame.setData(allocate.array());
                pLVideoFrame.setDataFormat(aVar);
                pLVideoFrame.setIsKeyFrame(z);
                pLVideoFrame.setWidth(frameAtTime.getWidth());
                pLVideoFrame.setHeight(frameAtTime.getHeight());
                pLVideoFrame.setRotation(0);
                return pLVideoFrame;
            }
            return null;
        } catch (RuntimeException unused) {
            e.w.e("MediaFile", "Illegal file path for MediaMetadataRetriever");
            return null;
        }
    }
}
