package com.xiaomi.milab.videosdk;

import android.graphics.Bitmap;
import com.xiaomi.milab.videosdk.message.TranscodeCallback;

/* loaded from: classes3.dex */
public class MediaTranscode extends XmsNativeObject {

    /* loaded from: classes3.dex */
    public enum DecoderType {
        SOFTWARE_DECODER,
        HARDWARE_DECODER
    }

    /* loaded from: classes3.dex */
    public static class EncodeParams {
        public int audioBitrate;
        public int channels;
        public double fps;
        public int frequency;
        public long from;
        public int height;
        public int interval;
        public boolean reverse;
        public long to;
        public int videoBitrate;
        public int width;
    }

    private native int convertMediaFile(long j, String str, String str2, EncodeParams encodeParams, TranscodeCallback transcodeCallback);

    private native long creatMediaTranscode();

    private native void release(long j);

    private native void setDecoderType(long j, int i);

    private native void setThumbBitmap(long j, Bitmap bitmap);

    private native int stop(long j);

    public MediaTranscode() {
        this.mNativePtr = creatMediaTranscode();
    }

    /* renamed from: com.xiaomi.milab.videosdk.MediaTranscode$1  reason: invalid class name */
    /* loaded from: classes3.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$xiaomi$milab$videosdk$MediaTranscode$DecoderType;

        static {
            int[] iArr = new int[DecoderType.values().length];
            $SwitchMap$com$xiaomi$milab$videosdk$MediaTranscode$DecoderType = iArr;
            try {
                iArr[DecoderType.SOFTWARE_DECODER.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$xiaomi$milab$videosdk$MediaTranscode$DecoderType[DecoderType.HARDWARE_DECODER.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    public void setDecoderType(DecoderType decoderType) {
        int i = AnonymousClass1.$SwitchMap$com$xiaomi$milab$videosdk$MediaTranscode$DecoderType[decoderType.ordinal()];
        int i2 = 1;
        if (i == 1 || i != 2) {
            i2 = 0;
        }
        setDecoderType(this.mNativePtr, i2);
    }

    public void setThumbBitmap(Bitmap bitmap) {
        setThumbBitmap(this.mNativePtr, bitmap);
    }

    public int convert(String str, String str2, EncodeParams encodeParams, TranscodeCallback transcodeCallback) {
        return convertMediaFile(this.mNativePtr, str, str2, encodeParams, transcodeCallback);
    }

    public int stop() {
        return stop(this.mNativePtr);
    }

    public void release() {
        release(this.mNativePtr);
    }
}
