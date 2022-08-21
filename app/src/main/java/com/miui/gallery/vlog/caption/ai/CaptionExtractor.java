package com.miui.gallery.vlog.caption.ai;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.caption.ai.AiVoiceToTextChannel;
import com.miui.gallery.vlog.entity.Caption;
import com.miui.gallery.vlog.sdk.interfaces.IClipAudioManager;
import com.miui.gallery.vlog.sdk.interfaces.IVideoClip;
import com.xiaomi.stat.d;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/* loaded from: classes2.dex */
public class CaptionExtractor {
    public IClipAudioManager mClipAudioManager;
    public ExtractCallback mExtractCallback;
    public int mProgress;
    public List<IVideoClip> mVideoClips;
    public Disposable sendDisposable;
    public List<Caption> mCaptionList = new ArrayList();
    public Handler mHandler = new Handler(Looper.getMainLooper());
    public AiVoiceToTextChannel mChannel = new AiVoiceToTextChannel(new ConnectionChannelCallback());
    public final ConvertHelper mConvertHelper = new ConvertHelper();

    /* loaded from: classes2.dex */
    public interface ExtractCallback {
        void onConnect();

        void onError(String str);

        void onFinish(List<Caption> list);

        void onProcess(int i);
    }

    public final int calculateProgress(long j, long j2, long j3, int i, int i2) {
        return (int) (((((float) j2) + ((i / i2) * ((float) j3))) * 100.0f) / ((float) j));
    }

    public CaptionExtractor(IClipAudioManager iClipAudioManager, List<IVideoClip> list) {
        this.mClipAudioManager = iClipAudioManager;
        this.mVideoClips = list;
    }

    public void setExtractCallback(ExtractCallback extractCallback) {
        this.mExtractCallback = extractCallback;
    }

    public void extract() {
        if (this.mChannel == null) {
            ExtractCallback extractCallback = this.mExtractCallback;
            if (extractCallback == null) {
                return;
            }
            extractCallback.onError("channel is null");
            this.mExtractCallback.onFinish(null);
        } else if (emptyClips()) {
            ExtractCallback extractCallback2 = this.mExtractCallback;
            if (extractCallback2 == null) {
                return;
            }
            extractCallback2.onFinish(null);
        } else {
            VoiceApiUtils.loadLibrary();
            this.mChannel.connect();
        }
    }

    public void stop() {
        Disposable disposable = this.sendDisposable;
        if (disposable != null && !disposable.isDisposed()) {
            this.sendDisposable.dispose();
        }
        AiVoiceToTextChannel aiVoiceToTextChannel = this.mChannel;
        if (aiVoiceToTextChannel != null) {
            aiVoiceToTextChannel.cancel();
            this.mChannel = null;
        }
        IClipAudioManager iClipAudioManager = this.mClipAudioManager;
        if (iClipAudioManager != null) {
            iClipAudioManager.setAudioConvertCallback(null);
            this.mClipAudioManager = null;
        }
        this.mExtractCallback = null;
    }

    public final boolean emptyClips() {
        List<IVideoClip> list = this.mVideoClips;
        return list == null || list.size() == 0;
    }

    /* loaded from: classes2.dex */
    public class ConvertHelper {
        public CountDownLatch mLatch;
        public String mResult;
        public long mTaskId;

        public ConvertHelper() {
            CaptionExtractor.this.mClipAudioManager.setAudioConvertCallback(new IClipAudioManager.AudioConvertCallback() { // from class: com.miui.gallery.vlog.caption.ai.CaptionExtractor.ConvertHelper.1
                @Override // com.miui.gallery.vlog.sdk.interfaces.IClipAudioManager.AudioConvertCallback
                public void onFinish(long j, String str, String str2) {
                    if (j == ConvertHelper.this.mTaskId) {
                        if (ConvertHelper.this.mLatch != null) {
                            ConvertHelper.this.mLatch.countDown();
                        }
                        ConvertHelper.this.mResult = str2;
                    }
                }
            });
        }

        public final String getAudio(IVideoClip iVideoClip, String str) throws InterruptedException {
            this.mLatch = new CountDownLatch(1);
            long audio16ks16leAsync = CaptionExtractor.this.mClipAudioManager.getAudio16ks16leAsync(iVideoClip, str);
            this.mTaskId = audio16ks16leAsync;
            if (audio16ks16leAsync == -1) {
                return null;
            }
            this.mLatch.await();
            return this.mResult;
        }
    }

    public final void postConnect() {
        this.mHandler.post(new Runnable() { // from class: com.miui.gallery.vlog.caption.ai.CaptionExtractor.1
            @Override // java.lang.Runnable
            public void run() {
                if (CaptionExtractor.this.mExtractCallback != null) {
                    CaptionExtractor.this.mExtractCallback.onConnect();
                }
            }
        });
    }

    public final void postProcess(final int i) {
        this.mHandler.post(new Runnable() { // from class: com.miui.gallery.vlog.caption.ai.CaptionExtractor.2
            @Override // java.lang.Runnable
            public void run() {
                if (CaptionExtractor.this.mExtractCallback != null) {
                    CaptionExtractor.this.mExtractCallback.onProcess(i);
                }
            }
        });
    }

    public final void postFinish(final List<Caption> list) {
        this.mHandler.post(new Runnable() { // from class: com.miui.gallery.vlog.caption.ai.CaptionExtractor.3
            @Override // java.lang.Runnable
            public void run() {
                if (CaptionExtractor.this.mExtractCallback != null) {
                    CaptionExtractor.this.mExtractCallback.onFinish(list);
                }
            }
        });
    }

    public final void postError(final String str) {
        this.mHandler.post(new Runnable() { // from class: com.miui.gallery.vlog.caption.ai.CaptionExtractor.4
            @Override // java.lang.Runnable
            public void run() {
                if (CaptionExtractor.this.mExtractCallback != null) {
                    CaptionExtractor.this.mExtractCallback.onError(str);
                }
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:52:0x0111  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void sendAudio(com.miui.gallery.vlog.caption.ai.AiVoiceToTextChannel r27) {
        /*
            Method dump skipped, instructions count: 299
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.vlog.caption.ai.CaptionExtractor.sendAudio(com.miui.gallery.vlog.caption.ai.AiVoiceToTextChannel):void");
    }

    public final String getTempPcmFile() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "DCIM/Camera");
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(file, ".16ks16le.pcm");
        if (file2.exists()) {
            file2.delete();
        }
        return file2.getPath();
    }

    /* loaded from: classes2.dex */
    public class ConnectionChannelCallback implements AiVoiceToTextChannel.ChannelCallback {
        public ConnectionChannelCallback() {
        }

        @Override // com.miui.gallery.vlog.caption.ai.AiVoiceToTextChannel.ChannelCallback
        public void onOpen(final AiVoiceToTextChannel aiVoiceToTextChannel) {
            CaptionExtractor.this.postConnect();
            CaptionExtractor.this.sendDisposable = Schedulers.io().createWorker().schedule(new Runnable() { // from class: com.miui.gallery.vlog.caption.ai.CaptionExtractor.ConnectionChannelCallback.1
                @Override // java.lang.Runnable
                public void run() {
                    CaptionExtractor.this.sendAudio(aiVoiceToTextChannel);
                }
            });
        }

        @Override // com.miui.gallery.vlog.caption.ai.AiVoiceToTextChannel.ChannelCallback
        public void onReceiveData(JsonObject jsonObject) {
            Caption parseData = CaptionExtractor.this.parseData(jsonObject);
            if (parseData == null || TextUtils.isEmpty(parseData.text)) {
                return;
            }
            String str = parseData.text;
            str.replaceAll("[^a-z^A-Z^0-9]", "").length();
            String trim = str.replaceAll("[.。]", "").trim();
            int length = trim.length();
            int i = 0;
            while (i < length) {
                int i2 = i + 30;
                int min = Math.min(i2, length);
                String substring = trim.substring(i, min);
                Caption caption = new Caption();
                caption.text = substring;
                long j = parseData.outPoint;
                long j2 = parseData.inPoint;
                long j3 = ((float) (j - j2)) / length;
                caption.inPoint = (j2 + (i * j3)) * 1000;
                caption.outPoint = (parseData.inPoint + (min * j3)) * 1000;
                CaptionExtractor.this.mCaptionList.add(caption);
                i = i2;
            }
        }

        @Override // com.miui.gallery.vlog.caption.ai.AiVoiceToTextChannel.ChannelCallback
        public void onError(String str) {
            CaptionExtractor.this.postError(str);
        }

        @Override // com.miui.gallery.vlog.caption.ai.AiVoiceToTextChannel.ChannelCallback
        public void onClose() {
            CaptionExtractor captionExtractor = CaptionExtractor.this;
            captionExtractor.postFinish(captionExtractor.mCaptionList);
        }
    }

    public final Caption parseData(JsonObject jsonObject) {
        JsonObject asJsonObject;
        JsonObject asJsonObject2;
        if (jsonObject == null) {
            return null;
        }
        try {
            JsonObject asJsonObject3 = jsonObject.getAsJsonObject("result");
            if (asJsonObject3 == null || (asJsonObject = asJsonObject3.getAsJsonObject("cn")) == null || (asJsonObject2 = asJsonObject.getAsJsonObject(d.n)) == null) {
                return null;
            }
            Caption caption = new Caption();
            caption.inPoint = asJsonObject2.getAsJsonPrimitive("bg").getAsLong();
            caption.outPoint = asJsonObject2.getAsJsonPrimitive("ed").getAsLong();
            JsonArray asJsonArray = asJsonObject2.getAsJsonArray("rt").get(0).getAsJsonObject().getAsJsonArray("ws");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < asJsonArray.size(); i++) {
                sb.append(asJsonArray.get(i).getAsJsonObject().getAsJsonArray("cw").get(0).getAsJsonObject().getAsJsonPrimitive("w").getAsString());
            }
            caption.text = sb.toString();
            return caption;
        } catch (Exception e) {
            DefaultLogger.d("CaptionExtractor", "parse data exception %s", e.getMessage());
            return null;
        }
    }
}
