package com.xiaomi.mediatranscode;

import android.media.MediaFormat;
import android.opengl.GLES30;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.xiaomi.mediatranscode.EglBase;
import com.xiaomi.mediatranscode.MoviePlayer;
import com.xiaomi.mediatranscode.VideoEncoder;

/* loaded from: classes3.dex */
public class RecordRenderDrawer extends BaseRenderDrawer implements Runnable, VideoEncoder.VideoEncoderCallBack {
    private static String TAG = "RecordRenderDrawer";
    private int af_Position;
    private int av_Position;
    private int av_format;
    private boolean isRecording;
    private int lut_Texture;
    private int mBitrate;
    private String mCodecName;
    private EglBase mEgl;
    private boolean mError;
    private int mFps;
    private int mLutTextureId;
    private Handler mMsgHandler;
    private String mPath;
    private EglBase.Context mSharedContext;
    private int mTextureId;
    private VideoEncoder mVideoEncoder;
    private MiVideoTranscode mVideoTransfer;
    private int s_Texture;
    private int format = 0;
    private long mStartTime = -1;
    private boolean mReserverResolution = true;
    public long num = 0;
    public int mCaptureOne = 0;

    @Override // com.xiaomi.mediatranscode.BaseRenderDrawer
    public String getFragmentSource() {
        return "precision mediump float;\nvarying vec2 v_texPo;\nuniform sampler2D s_Texture;\nuniform sampler2D lut_Texture; uniform int av_format ;vec4 result;float m1 = 2610.0 / 16384.0;float m2 = 2523.0 / 4096.0 * 128.0;float c1 = 3423.0 / 4096.0;float c2 = 2413.0 / 4096.0 * 32.0;float c3 = 2392.0 / 4096.0 * 32.0;float linearProc(float src) {    float A = max(pow(src, 1.0 / m2) - c1, 0.0);    float B = c2 - c3 * pow(src, 1.0 / m2);    return pow(A / B, (1.0 / m1)) * 10.0;}float gammaProc(float src) {    return min(max(pow(src, 0.45), 0.0), 1.0);}float PQCurl(float src) {   return texture2D(lut_Texture, vec2(src, 0.5)).a;} vec3 applyMat(vec3 incolor) {   mat3 m = mat3(1.3436, -0.2822, -0.0614,                 -0.0653, 1.07578, -0.0105,                 -0.0028, -0.0196, 1.0168);    return (incolor.rgb * m); } void main() {\n   vec4 tc = texture2D(s_Texture, v_texPo);\n   gl_FragColor = texture2D(s_Texture, v_texPo);\n   gl_FragColor.a = 1.0 ;   if(av_format > 0) {        result = gl_FragColor.rgba;        result.r = PQCurl(result.r);        result.g = PQCurl(result.g);        result.b = PQCurl(result.b);        result.rgb = applyMat(result.rgb);        result.r = gammaProc(result.r);        result.g = gammaProc(result.g);        result.b = gammaProc(result.b);        result.a = 1.0;        gl_FragColor = result;    }else {       gl_FragColor = gl_FragColor;   }}";
    }

    @Override // com.xiaomi.mediatranscode.BaseRenderDrawer
    public String getVertexSource() {
        return "attribute vec4 av_Position; attribute vec2 af_Position; varying vec2 v_texPo; void main() {     v_texPo = af_Position;     gl_Position = av_Position; }";
    }

    @Override // com.xiaomi.mediatranscode.BaseRenderDrawer
    public void onChanged(int i, int i2) {
    }

    @Override // com.xiaomi.mediatranscode.BaseRenderDrawer
    public void release() {
    }

    public RecordRenderDrawer(MiVideoTranscode miVideoTranscode) {
        this.mError = false;
        new Thread(this).start();
        this.mVideoTransfer = miVideoTranscode;
        this.mError = false;
    }

    public void setParams(String str, int i, int i2, int i3, int i4, String str2) {
        this.mVideoEncoder = null;
        this.mTextureId = 0;
        this.isRecording = false;
        this.mPath = str2;
        this.width = i;
        this.height = i2;
        this.mFps = i3;
        this.mBitrate = i4;
        this.mCodecName = str;
    }

    @Override // com.xiaomi.mediatranscode.VideoEncoder.VideoEncoderCallBack
    public void onVideoEncoderEOF() {
        Logg.LogI("on recv encoder eof");
        this.mMsgHandler.sendEmptyMessage(7);
    }

    @Override // com.xiaomi.mediatranscode.VideoEncoder.VideoEncoderCallBack
    public void onVideoEncoderError(int i) {
        Logg.LogE("video encoder setup failed ");
        this.mError = true;
        this.mVideoTransfer.onRecoderFailed(1);
    }

    @Override // com.xiaomi.mediatranscode.BaseRenderDrawer
    public void setInputTextureId(int i) {
        this.mTextureId = i;
        Logg.LogI("setInputTextureId: " + i);
    }

    @Override // com.xiaomi.mediatranscode.BaseRenderDrawer
    public int getOutputTextureId() {
        return this.mTextureId;
    }

    @Override // com.xiaomi.mediatranscode.BaseRenderDrawer
    public void setReserverResolution(boolean z) {
        this.mReserverResolution = z;
    }

    @Override // com.xiaomi.mediatranscode.BaseRenderDrawer
    public void create() {
        this.mSharedContext = EglBase.getCurrentContext();
    }

    public void setLutTextureId(int i) {
        this.mLutTextureId = i;
    }

    public void startRecord() {
        this.mMsgHandler.sendMessage(this.mMsgHandler.obtainMessage(1, this.width, this.height, this.mSharedContext));
        this.isRecording = true;
    }

    public void stopRecord() {
        Logg.LogI("stopRecord");
        Handler handler = this.mMsgHandler;
        handler.sendMessage(handler.obtainMessage(2));
    }

    public void quit() {
        Logg.LogI(" to quit Recoder thread ");
        Handler handler = this.mMsgHandler;
        handler.sendMessage(handler.obtainMessage(6));
    }

    @Override // com.xiaomi.mediatranscode.BaseRenderDrawer
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

    @Override // com.xiaomi.mediatranscode.BaseRenderDrawer
    public void draw(long j, float[] fArr) {
        if (this.isRecording) {
            this.mMsgHandler.removeMessages(5);
            this.mMsgHandler.sendMessage(this.mMsgHandler.obtainMessage(5, Long.valueOf(j)));
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        Looper.prepare();
        this.mMsgHandler = new MsgHandler();
        Looper.loop();
    }

    /* loaded from: classes3.dex */
    public class MsgHandler extends Handler {
        public MsgHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    RecordRenderDrawer.this.prepareVideoEncoder((EglBase.Context) message.obj, message.arg1, message.arg2);
                    return;
                case 2:
                    RecordRenderDrawer.this.stopVideoEncoder();
                    return;
                case 3:
                    Logg.LogI(" TO update context");
                    RecordRenderDrawer.this.updateEglContext((EglBase.Context) message.obj);
                    return;
                case 4:
                    RecordRenderDrawer.this.updateChangedSize(message.arg1, message.arg2);
                    return;
                case 5:
                    RecordRenderDrawer.this.drawFrame(((Long) message.obj).longValue());
                    return;
                case 6:
                    RecordRenderDrawer.this.quitLooper();
                    return;
                case 7:
                    RecordRenderDrawer.this.handleEncoderEOF();
                    return;
                case 8:
                    RecordRenderDrawer.this.handleAudioFormat((MediaFormat) message.obj);
                    return;
                case 9:
                    RecordRenderDrawer.this.handleAudioFrame((MoviePlayer.AudioFrame) message.obj);
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
            videoEncoder.addAudioTrack(mediaFormat);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAudioFrame(MoviePlayer.AudioFrame audioFrame) {
        VideoEncoder videoEncoder = this.mVideoEncoder;
        if (videoEncoder != null) {
            videoEncoder.writeAudioSample(audioFrame);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleEncoderEOF() {
        Logg.LogI("handleEncoderEOF ");
        this.isRecording = false;
        this.mVideoTransfer.onRecoderEOF();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void prepareVideoEncoder(EglBase.Context context, int i, int i2) {
        this.mVideoEncoder = new VideoEncoder(i, i2, this.mFps, this.mBitrate, this.mPath, this.mCodecName, this);
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
        Logg.LogI("to signal stop encoder");
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
    public void drawFrame(long j) {
        this.mEgl.makeCurrent();
        VideoEncoder videoEncoder = this.mVideoEncoder;
        if (videoEncoder != null) {
            videoEncoder.drainEncoder(false);
        }
        onDraw();
        this.mEgl.setPresentTime(j);
        this.mEgl.swapBuffers();
        this.mVideoTransfer.getPlayer().getOneFrame();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateChangedSize(int i, int i2) {
        onChanged(i, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void quitLooper() {
        VideoEncoder videoEncoder = this.mVideoEncoder;
        if (videoEncoder != null) {
            videoEncoder.drainEncoder(true);
        }
        EglBase eglBase = this.mEgl;
        if (eglBase != null) {
            eglBase.release();
            VideoEncoder videoEncoder2 = this.mVideoEncoder;
            if (videoEncoder2 != null) {
                videoEncoder2.release();
            }
            this.mVideoEncoder = null;
            this.mEgl = null;
        }
        Looper.myLooper().quit();
        this.mVideoTransfer.onFileComplete();
    }

    @Override // com.xiaomi.mediatranscode.BaseRenderDrawer
    public void onCreated() {
        this.mProgram = GlesUtil.createProgram(getVertexSource(), getFragmentSource());
        initVertexBufferObjects();
        this.av_Position = GLES30.glGetAttribLocation(this.mProgram, "av_Position");
        this.af_Position = GLES30.glGetAttribLocation(this.mProgram, "af_Position");
        this.s_Texture = GLES30.glGetUniformLocation(this.mProgram, "s_Texture");
        this.lut_Texture = GLES30.glGetUniformLocation(this.mProgram, "lut_Texture");
        this.av_format = GLES30.glGetUniformLocation(this.mProgram, "av_format");
        Logg.LogI("onCreated: av_Position " + this.av_Position);
        Logg.LogI("onCreated: af_Position " + this.af_Position);
        Logg.LogI("onCreated: s_Texture " + this.s_Texture);
        Logg.LogI("onCreated: error " + GLES30.glGetError());
    }

    @Override // com.xiaomi.mediatranscode.BaseRenderDrawer
    public void onCroped(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        this.format = i7;
        Logg.LogI(" RecordRenderDrawer onCroped width:" + i + " height :" + i2 + " cropTop:" + i3 + " cropLeft:" + i4 + " cropBottom:" + i5 + " cropRight:" + i6 + " format " + i7);
    }

    @Override // com.xiaomi.mediatranscode.BaseRenderDrawer
    public void onDraw() {
        clear();
        useProgram();
        GLES30.glUniform1i(this.av_format, this.format == 2141391881 ? 1 : 0);
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
        GLES30.glActiveTexture(33985);
        GLES30.glBindTexture(3553, this.mLutTextureId);
        GLES30.glUniform1i(this.lut_Texture, 1);
        GLES30.glDrawArrays(5, 0, this.VertexCount);
        GLES30.glDisableVertexAttribArray(this.av_Position);
        GLES30.glDisableVertexAttribArray(this.af_Position);
        GLES30.glBindTexture(3553, 0);
    }
}
