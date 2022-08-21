package com.xiaomi.mediacodec;

import android.media.MediaFormat;
import android.opengl.GLES30;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import com.xiaomi.mediacodec.EglBase;
import com.xiaomi.mediacodec.MoviePlayer;
import com.xiaomi.mediacodec.VideoEncoder;
import java.io.FileDescriptor;
import java.util.concurrent.Semaphore;

/* loaded from: classes3.dex */
public class MiHWEncoder extends BaseRenderDrawer implements VideoEncoder.VideoEncoderCallBack {
    private static String TAG = "MiHWEncoder";
    private int af_Position;
    private int av_Position;
    public long indexframe;
    private AudioEncoder mAudioEncoder;
    private int mBitrate;
    private String mCodecName;
    private EglBase mEgl;
    private boolean mError;
    private int mFps;
    private Handler mMsgHandler;
    private String mPath;
    private EglBase.Context mSharedContext;
    private int mTextureId;
    private HandlerThread mThread;
    private VideoEncoder mVideoEncoder;
    private int s_Texture;
    private FileDescriptor mFd = null;
    public boolean encoded_end = false;
    public long mJniContext = 0;
    private volatile boolean misStarted = false;
    private volatile boolean mEncodeExit = false;
    private volatile boolean mThreadHandlerExit = false;
    private volatile boolean mThreadHandlerStart = false;
    private int sample_rate = 48000;
    private final Semaphore semp = new Semaphore(2);
    private final Object mWaitEvent = new Object();
    public long num = 0;
    public int mCaptureOne = 0;

    public native void OnVideoEncoderEOFJni(long j);

    public native void OnVideoEncoderErrorJni(long j);

    public native void OnVideoFrameCompeletedJni(long j, long j2);

    @Override // com.xiaomi.mediacodec.BaseRenderDrawer
    public void create() {
    }

    @Override // com.xiaomi.mediacodec.BaseRenderDrawer
    public String getFragmentSource() {
        return "precision mediump float;\nvarying vec2 v_texPo;\nuniform sampler2D s_Texture;\nvoid main() {\n   vec4 tc = texture2D(s_Texture, v_texPo);\n   gl_FragColor = texture2D(s_Texture, v_texPo);\n}";
    }

    @Override // com.xiaomi.mediacodec.BaseRenderDrawer
    public String getVertexSource() {
        return "attribute vec4 av_Position; attribute vec2 af_Position; varying vec2 v_texPo; void main() {     v_texPo = af_Position;     gl_Position = av_Position; }";
    }

    @Override // com.xiaomi.mediacodec.BaseRenderDrawer
    public void onChanged(int i, int i2) {
    }

    @Override // com.xiaomi.mediacodec.BaseRenderDrawer
    public void onCroped(int i, int i2, int i3, int i4, int i5, int i6) {
    }

    @Override // com.xiaomi.mediacodec.BaseRenderDrawer
    public void release() {
    }

    @Override // com.xiaomi.mediacodec.BaseRenderDrawer
    public void setReserverResolution(boolean z) {
    }

    public MiHWEncoder() {
        this.mError = false;
        this.mError = false;
        Logg.LogI("eglGetCurrentContext:" + EglBase.getCurrentContext());
        this.mSharedContext = EglBase.getCurrentContext();
        HandlerThread handlerThread = new HandlerThread("encoder thread");
        this.mThread = handlerThread;
        handlerThread.start();
        this.mMsgHandler = new MsgHandler(this.mThread.getLooper());
    }

    public void setParams(String str, int i, int i2, int i3, int i4, FileDescriptor fileDescriptor, String str2) {
        this.mPath = str2;
        this.width = i;
        this.height = i2;
        this.mFps = i3;
        this.mBitrate = i4;
        this.mFd = fileDescriptor;
        this.mCodecName = str;
    }

    @Override // com.xiaomi.mediacodec.VideoEncoder.VideoEncoderCallBack
    public void onVideoEncoderEOF() {
        Logg.LogI("on recv encoder eof");
        if (!this.encoded_end) {
            this.encoded_end = true;
        } else {
            OnVideoEncoderEOFJni(this.mJniContext);
        }
    }

    @Override // com.xiaomi.mediacodec.VideoEncoder.VideoEncoderCallBack
    public void onVideoEncoderError(int i) {
        Logg.LogE("video encoder setup failed ");
        this.mError = true;
        OnVideoEncoderErrorJni(this.mJniContext);
    }

    @Override // com.xiaomi.mediacodec.VideoEncoder.VideoEncoderCallBack
    public void onVideoEncodedFrame(boolean z) {
        Logg.LogI("video encoded one frame ");
        this.semp.release();
    }

    @Override // com.xiaomi.mediacodec.BaseRenderDrawer
    public void setInputTextureId(int i) {
        this.mTextureId = i;
        Logg.LogI("setInputTextureId: " + i);
    }

    @Override // com.xiaomi.mediacodec.BaseRenderDrawer
    public int getOutputTextureId() {
        return this.mTextureId;
    }

    public boolean CreateEncoder(int i, int i2, int i3, int i4, FileDescriptor fileDescriptor, String str, String str2, long j) {
        Logg.LogI("CreateEncoder: width:" + i + " height:" + i2 + " fps:" + i3 + " bit:" + i4 + " fd: " + fileDescriptor + " path:" + str + " codec name:" + str2);
        this.mJniContext = j;
        setParams(str2, i, i2, i3, i4, fileDescriptor, str);
        AudioEncoder audioEncoder = new AudioEncoder();
        this.mAudioEncoder = audioEncoder;
        audioEncoder.SetEncoderDataCallback(this);
        this.mAudioEncoder.initMediacodec(this.sample_rate);
        return true;
    }

    public boolean EncodeVideoData(int i, long j) {
        Logg.LogI("java EncodeVideoData: texture_id:" + i + " timestamp:" + j);
        drawTexture(j, null, i);
        return true;
    }

    public void Flush() {
        Logg.LogI("java Flush():");
        stopRecord();
        if (this.mEncodeExit) {
            return;
        }
        quit();
        this.mThread.quitSafely();
        this.mEncodeExit = true;
    }

    public void Release() {
        Logg.LogI("java Release()");
        if (!this.mEncodeExit) {
            quit();
            this.mThread.quitSafely();
        }
        if (this.mThreadHandlerStart) {
            long currentTimeMillis = System.currentTimeMillis();
            while (!this.mThreadHandlerExit && System.currentTimeMillis() - currentTimeMillis <= 3000) {
            }
        }
    }

    public boolean EncodeAudioData(byte[] bArr, int i, int i2, int i3, double d) {
        Logg.LogI("jni EncodeAudioData() size: " + i + "timeStamp:" + d);
        return this.mAudioEncoder.encodecPcmToAAc(i, bArr, d);
    }

    public void startRecord() {
        Logg.LogI("java startRecord context : ");
        if (this.mEncodeExit) {
            return;
        }
        this.misStarted = true;
        this.mMsgHandler.sendMessage(this.mMsgHandler.obtainMessage(1, this.width, this.height, this.mSharedContext));
    }

    public void stopRecord() {
        if (!this.misStarted) {
            return;
        }
        Logg.LogI("stopRecord");
        Handler handler = this.mMsgHandler;
        handler.sendMessage(handler.obtainMessage(2));
        while (!this.encoded_end) {
            synchronized (this.mWaitEvent) {
                try {
                    Logg.LogI("wait encoder end callback");
                    this.mWaitEvent.wait(10L);
                } catch (InterruptedException e) {
                    Logg.LogI(" found InterruptedException exception at doExtract " + e);
                }
            }
        }
        this.misStarted = false;
    }

    public void quit() {
        Logg.LogI(" to quit Recoder thread ");
        Handler handler = this.mMsgHandler;
        handler.sendMessage(handler.obtainMessage(6));
    }

    @Override // com.xiaomi.mediacodec.BaseRenderDrawer
    public void surfaceChangedSize(int i, int i2) {
        this.width = i;
        this.height = i2;
    }

    public void addAudioFormat(Object obj) {
        if (obj == null) {
            return;
        }
        this.mMsgHandler.sendMessage(this.mMsgHandler.obtainMessage(8, obj));
    }

    public void addAudioFrame(Object obj) {
        this.mMsgHandler.sendMessage(this.mMsgHandler.obtainMessage(9, obj));
    }

    @Override // com.xiaomi.mediacodec.BaseRenderDrawer
    public void draw(long j, float[] fArr) {
        this.mMsgHandler.sendMessage(this.mMsgHandler.obtainMessage(5, (int) j, this.mTextureId));
    }

    public void drawTexture(long j, float[] fArr, int i) {
        try {
            this.semp.acquire();
            StringBuilder sb = new StringBuilder();
            sb.append("drawTexture timestamp :");
            sb.append(j);
            sb.append(" drawTexture indexframe:");
            long j2 = this.indexframe;
            this.indexframe = j2 + 1;
            sb.append(j2);
            Logg.LogI(sb.toString());
            this.mMsgHandler.sendMessage(this.mMsgHandler.obtainMessage(5, (int) j, i));
            this.indexframe++;
        } catch (InterruptedException unused) {
            Logg.LogI("drawTexture timestamp faild:" + j);
        }
    }

    /* loaded from: classes3.dex */
    public class MsgHandler extends Handler {
        public static final int MSG_AUDIO_FORMAT = 8;
        public static final int MSG_AUDIO_FRAME = 9;
        public static final int MSG_ENCODER_EOF = 7;
        public static final int MSG_FRAME = 5;
        public static final int MSG_QUIT = 6;
        public static final int MSG_START_RECORD = 1;
        public static final int MSG_STOP_RECORD = 2;
        public static final int MSG_UPDATE_CONTEXT = 3;
        public static final int MSG_UPDATE_SIZE = 4;

        public MsgHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    MiHWEncoder.this.prepareVideoEncoder((EglBase.Context) message.obj, message.arg1, message.arg2);
                    return;
                case 2:
                    MiHWEncoder.this.stopVideoEncoder();
                    return;
                case 3:
                    Logg.LogI(" TO update context");
                    MiHWEncoder.this.updateEglContext((EglBase.Context) message.obj);
                    return;
                case 4:
                    MiHWEncoder.this.updateChangedSize(message.arg1, message.arg2);
                    return;
                case 5:
                    MiHWEncoder.this.drawFrame(message.arg1, message.arg2);
                    return;
                case 6:
                    MiHWEncoder.this.quitLooper();
                    return;
                case 7:
                    MiHWEncoder.this.handleEncoderEOF();
                    return;
                case 8:
                    MiHWEncoder.this.handleAudioFormat((MediaFormat) message.obj);
                    return;
                case 9:
                    MiHWEncoder.this.handleAudioFrame((MoviePlayer.MediaFrame) message.obj);
                    return;
                default:
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAudioFormat(MediaFormat mediaFormat) {
        VideoEncoder videoEncoder = this.mVideoEncoder;
        if (videoEncoder != null) {
            videoEncoder.addMediaTrack(mediaFormat);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAudioFrame(MoviePlayer.MediaFrame mediaFrame) {
        VideoEncoder videoEncoder = this.mVideoEncoder;
        if (videoEncoder != null) {
            videoEncoder.writeAudioSample(mediaFrame);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleEncoderEOF() {
        Logg.LogI("handleEncoderEOF ");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void prepareVideoEncoder(EglBase.Context context, int i, int i2) {
        this.mThreadHandlerStart = true;
        this.mVideoEncoder = new VideoEncoder(i, i2, this.mFps, this.mBitrate, this.mFd, this.mPath, this.mCodecName, this);
        if (this.mError) {
            this.mVideoEncoder = null;
        }
        EglBase create = EglBase.create(context);
        this.mEgl = create;
        try {
            create.createSurface(this.mVideoEncoder.getInputSurface());
            this.mEgl.makeCurrent();
            onCreated();
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopVideoEncoder() {
        Logg.LogI("to signal stop encoder:" + this.mVideoEncoder);
        VideoEncoder videoEncoder = this.mVideoEncoder;
        if (videoEncoder != null) {
            videoEncoder.stopEncoder();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateEglContext(EglBase.Context context) {
        this.mEgl.release();
        EglBase create = EglBase.create(context);
        this.mEgl = create;
        create.createSurface(this.mVideoEncoder.getInputSurface());
        this.mEgl.makeCurrent();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void drawFrame(long j, int i) {
        Logg.LogI("drawFrame timetamp:" + j + " texture id:" + i);
        setInputTextureId(i);
        this.mEgl.makeCurrent();
        VideoEncoder videoEncoder = this.mVideoEncoder;
        if (videoEncoder != null) {
            videoEncoder.drainEncoder(false);
        }
        onDraw();
        this.mEgl.setPresentTime(j);
        this.mEgl.swapBuffers();
        OnVideoFrameCompeletedJni(this.mJniContext, j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateChangedSize(int i, int i2) {
        onChanged(i, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void quitLooper() {
        Logg.LogI("to quitLooper encoder:" + this.mVideoEncoder + " mEgl:" + this.mEgl);
        VideoEncoder videoEncoder = this.mVideoEncoder;
        if (videoEncoder != null) {
            videoEncoder.drainEncoder(true);
            this.mVideoEncoder.release();
            this.mVideoEncoder = null;
            Logg.LogI("to quitLooper encoder222:" + this.mVideoEncoder + " mEgl:" + this.mEgl);
        }
        AudioEncoder audioEncoder = this.mAudioEncoder;
        if (audioEncoder != null) {
            audioEncoder.releaseMedicacodec();
        }
        destroy();
        EglBase eglBase = this.mEgl;
        if (eglBase != null) {
            eglBase.release();
            this.mEgl = null;
        }
        this.mThreadHandlerExit = true;
    }

    @Override // com.xiaomi.mediacodec.BaseRenderDrawer
    public void onCreated() {
        this.mProgram = GlesUtil.createProgram(getVertexSource(), getFragmentSource());
        initVertexBufferObjects();
        this.av_Position = GLES30.glGetAttribLocation(this.mProgram, "av_Position");
        this.af_Position = GLES30.glGetAttribLocation(this.mProgram, "af_Position");
        this.s_Texture = GLES30.glGetUniformLocation(this.mProgram, "s_Texture");
        Logg.LogI("onCreated: av_Position " + this.av_Position);
        Logg.LogI("onCreated: af_Position " + this.af_Position);
        Logg.LogI("onCreated: s_Texture " + this.s_Texture);
        Logg.LogI("onCreated: error " + GLES30.glGetError());
    }

    @Override // com.xiaomi.mediacodec.BaseRenderDrawer
    public void onDraw() {
        clear();
        useProgram();
        viewPort(0, 0, this.width, this.height);
        GLES30.glEnableVertexAttribArray(this.av_Position);
        GLES30.glEnableVertexAttribArray(this.af_Position);
        GLES30.glBindBuffer(34962, this.mVertexBufferId);
        GLES30.glVertexAttribPointer(this.av_Position, 2, 5126, false, 0, 0);
        GLES30.glBindBuffer(34962, this.mDisplayTextureBufferId);
        GLES30.glVertexAttribPointer(this.af_Position, 2, 5126, false, 0, 0);
        GLES30.glBindBuffer(34962, 0);
        GLES30.glGenerateMipmap(3553);
        GLES30.glActiveTexture(33984);
        GLES30.glBindTexture(3553, this.mTextureId);
        GLES30.glUniform1i(this.s_Texture, 0);
        GLES30.glDrawArrays(5, 0, this.VertexCount);
        GLES30.glFlush();
        GLES30.glDisableVertexAttribArray(this.av_Position);
        GLES30.glDisableVertexAttribArray(this.af_Position);
        GLES30.glBindTexture(3553, 0);
    }
}
