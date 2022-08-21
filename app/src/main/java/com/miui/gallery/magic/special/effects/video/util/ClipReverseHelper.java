package com.miui.gallery.magic.special.effects.video.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.miui.gallery.magic.util.MagicFileUtil;
import com.miui.gallery.magic.util.MagicLog;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.milab.videosdk.FrameRetriever;
import com.xiaomi.milab.videosdk.MediaTranscode;
import com.xiaomi.milab.videosdk.message.TranscodeCallback;
import com.xiaomi.stat.b.h;
import java.io.File;
import java.util.Hashtable;

/* loaded from: classes2.dex */
public class ClipReverseHelper {
    public Callback mCallback;
    public Context mContext;
    public String mDstPath;
    public int mIndex;
    public Boolean mIsCanceled;
    public MediaTranscode mMediaTranscode = new MediaTranscode();
    public Handler mMainHandler = new Handler(Looper.getMainLooper());

    /* loaded from: classes2.dex */
    public interface Callback {
        void onSuccess(String str, String str2, int i);

        void onTranscodeProgress(int i);
    }

    public ClipReverseHelper(Context context, Callback callback) {
        this.mContext = context;
        this.mCallback = callback;
    }

    public boolean reverseFile(int i, String str, int i2, long j, long j2) {
        this.mIndex = i;
        if (TextUtils.isEmpty(str) || !new File(str).exists()) {
            DefaultLogger.e("MagicLogger ClipReverseHelper", "convert file is fail, the src file is not exists. ");
            return false;
        }
        String str2 = MagicFileUtil.getTempVideoPath() + h.g + System.currentTimeMillis() + "_" + i2 + "_covert.mp4";
        this.mDstPath = str2;
        return convertMediaFile(str, str2, true, j, j2, null) == 0;
    }

    public final long convertMediaFile(String str, String str2, boolean z, long j, long j2, Hashtable<String, Object> hashtable) {
        this.mIsCanceled = Boolean.FALSE;
        FrameRetriever frameRetriever = new FrameRetriever();
        frameRetriever.setDataSource(str);
        MediaTranscode.EncodeParams encodeParams = new MediaTranscode.EncodeParams();
        encodeParams.audioBitrate = 48000;
        encodeParams.frequency = 44100;
        encodeParams.reverse = false;
        encodeParams.channels = 2;
        encodeParams.fps = 30.0d;
        encodeParams.from = j;
        encodeParams.to = j2;
        encodeParams.interval = 1;
        encodeParams.videoBitrate = (int) frameRetriever.getBitrate();
        int height = frameRetriever.getHeight();
        int width = frameRetriever.getWidth();
        if (height == 7680 || height == 4320 || width == 7680 || width == 4320) {
            encodeParams.height = height / 4;
            encodeParams.width = width / 4;
        } else {
            encodeParams.height = height;
            encodeParams.width = width;
        }
        this.mMediaTranscode.setDecoderType(MediaTranscode.DecoderType.HARDWARE_DECODER);
        MagicLog magicLog = MagicLog.INSTANCE;
        magicLog.showLog("MagicLogger ClipReverseHelper", "convert Thread.currentThread().getName " + Thread.currentThread().getName() + "\n Looper.getMainLooper().getName " + Looper.getMainLooper().getThread().getName());
        return this.mMediaTranscode.convert(str, str2, encodeParams, new TranscodeCallbackImpl(str, str2));
    }

    public void release() {
        MediaTranscode mediaTranscode = this.mMediaTranscode;
        if (mediaTranscode != null) {
            mediaTranscode.stop();
            this.mMediaTranscode.release();
            this.mMediaTranscode = null;
        }
    }

    /* loaded from: classes2.dex */
    public class TranscodeCallbackImpl implements TranscodeCallback {
        public String destFile;
        public String srcFile;

        public TranscodeCallbackImpl(String str, String str2) {
            this.srcFile = str;
            this.destFile = str2;
        }

        @Override // com.xiaomi.milab.videosdk.message.TranscodeCallback
        public void onTranscodeProgress(int i) {
            MagicLog magicLog = MagicLog.INSTANCE;
            magicLog.showLog("MagicLogger ClipReverseHelper", "onTranscodeProgress:  " + i);
            ClipReverseHelper.this.mCallback.onTranscodeProgress(i);
        }

        @Override // com.xiaomi.milab.videosdk.message.TranscodeCallback
        public void onTranscodeSuccess() {
            MagicLog.INSTANCE.showLog("MagicLogger ClipReverseHelper", "onTranscodeSuccess  ");
            ClipReverseHelper.this.mCallback.onSuccess(this.srcFile, this.destFile, 0);
        }

        @Override // com.xiaomi.milab.videosdk.message.TranscodeCallback
        public void onTranscodeCancel() {
            MagicLog.INSTANCE.showLog("MagicLogger ClipReverseHelper", "onTranscodeCancel  ");
        }

        @Override // com.xiaomi.milab.videosdk.message.TranscodeCallback
        public void onTranscodeFail() {
            MagicLog.INSTANCE.showLog("MagicLogger ClipReverseHelper", "onTranscodeFail");
        }
    }
}
