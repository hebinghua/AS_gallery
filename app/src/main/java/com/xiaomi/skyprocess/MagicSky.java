package com.xiaomi.skyprocess;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.Surface;
import com.baidu.platform.comapi.UIMsg;
import com.meicam.sdk.NvsStreamingContext;
import com.nexstreaming.nexeditorsdk.nexClip;
import com.xiaomi.skyprocess.EffectMediaPlayer;
import com.xiaomi.stat.b.h;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes3.dex */
public class MagicSky {
    private static final String TAG = "debug::" + MagicSky.class.getName();
    private String mAudioMaterial;
    private String mBatchCompsePath;
    private String mBatchSavePath;
    private String mBlurLightFilter;
    private String mBlurMaterial;
    private MediaComposeFile mComposeFile;
    private int mComposeGifHeight;
    private int mComposeGifWidth;
    private String mCompseFileName;
    private String mCompseGifFileName;
    private EffectMediaPlayer mEffectPlayer;
    private String mFireLightFilter;
    private GLSurfaceView mGLSurfaceView;
    private MediaEffectGraph mMediaEffectGraph;
    private MiGLSurfaceViewRender mMiGLSurfaceViewRender;
    public EffectNotifier mNotifier;
    private OpenGlRender mOpenGlRender;
    private Bitmap mOriginalImageBitmapScaled;
    private String mOverallFilter;
    private String mParticleMaterial;
    private String mProspectsFilter;
    private RenderPlayerNotifierCallBack mRenderPlayerNotifierCallBack;
    private String mSkyExternMaterial;
    private String mSkyMaterial;
    private String mSkyMaterialPath;
    private String mSubtitleMaterial;
    private String mSubtitleText;
    private int mSurfaceHeight;
    private int mSurfaceWidth;
    private Surface mVideoSurface;
    private WrapSegmentEngine mWrapsegment;
    private Context m_appContext;
    private HashMap<String, String> mTransformModelType = new HashMap<String, String>() { // from class: com.xiaomi.skyprocess.MagicSky.1
        {
            put("0", "3");
            put("1", "0");
            put("2", "1");
            put("3", "2");
            put("4", "5");
            put("5", "4");
            put("6", "6");
            put("7", "7");
            put("8", "9");
            put("9", "8");
        }
    };
    private int mModelType = 0;
    private int mSubtitleType = 0;
    private float mThreshold = 0.0f;
    private int mUiModelTypeIndex = 3;
    private boolean bMute = false;
    private boolean isComposingMp4 = false;
    private boolean isComposingGif = false;
    private boolean retCompose = true;
    private int mTotalBatchCount = 0;
    private int mCurrentBatchNum = 0;
    private boolean bPreviewLoop = false;
    private boolean isClearLastComposePercent = true;
    private int mComposeGifBitRate = 2048;
    private int mComposeGifFrameRate = 10;
    private int mdefaultComposeGifWidth = UIMsg.MsgDefine.RENDER_STATE_FIRST_FRAME;

    public void cancelBatchCompose() {
    }

    public MagicSky(Context context) {
        String str = TAG;
        Log.d(str, "context: " + context);
        this.m_appContext = context;
    }

    public MagicSky(Context context, GLSurfaceView gLSurfaceView) {
        String str = TAG;
        Log.d(str, "context: " + context + ", glSurfaceView: " + gLSurfaceView);
        this.m_appContext = context;
        WrapSegmentEngine wrapSegmentEngine = new WrapSegmentEngine();
        this.mWrapsegment = wrapSegmentEngine;
        wrapSegmentEngine.ConstructWrapSegmentEngine();
        if (gLSurfaceView == null) {
            Log.e(str, "MiGLSurfaceViewRender glSurfaceView == null error");
            return;
        }
        this.mGLSurfaceView = gLSurfaceView;
        this.mOpenGlRender = new OpenGlRender();
        MiGLSurfaceViewRender miGLSurfaceViewRender = new MiGLSurfaceViewRender(this.mGLSurfaceView, this.mOpenGlRender);
        this.mMiGLSurfaceViewRender = miGLSurfaceViewRender;
        this.mGLSurfaceView.setRenderer(miGLSurfaceViewRender);
        this.mGLSurfaceView.setRenderMode(0);
        RenderPlayerNotifierCallBack renderPlayerNotifierCallBack = new RenderPlayerNotifierCallBack();
        this.mRenderPlayerNotifierCallBack = renderPlayerNotifierCallBack;
        renderPlayerNotifierCallBack.SetGLSurfaceViewRender(this.mMiGLSurfaceViewRender);
    }

    public void setGLSurfaceView(GLSurfaceView gLSurfaceView) {
        if (gLSurfaceView == null) {
            Log.e(TAG, "MiGLSurfaceViewRender glSurfaceView == null error");
            return;
        }
        if (this.mGLSurfaceView != null) {
            Log.w(TAG, "MiGLSurfaceViewRender of glSurfaceView is no null");
        }
        this.mGLSurfaceView = gLSurfaceView;
        this.mOpenGlRender = new OpenGlRender();
        MiGLSurfaceViewRender miGLSurfaceViewRender = new MiGLSurfaceViewRender(this.mGLSurfaceView, this.mOpenGlRender);
        this.mMiGLSurfaceViewRender = miGLSurfaceViewRender;
        this.mGLSurfaceView.setRenderer(miGLSurfaceViewRender);
        this.mGLSurfaceView.setRenderMode(0);
        RenderPlayerNotifierCallBack renderPlayerNotifierCallBack = new RenderPlayerNotifierCallBack();
        this.mRenderPlayerNotifierCallBack = renderPlayerNotifierCallBack;
        renderPlayerNotifierCallBack.SetGLSurfaceViewRender(this.mMiGLSurfaceViewRender);
    }

    public void setGLSurfaceAvalibale(boolean z) {
        WrapSegmentEngine wrapSegmentEngine = this.mWrapsegment;
        if (wrapSegmentEngine != null) {
            wrapSegmentEngine.setGLSurfaceAvalibale(z);
        }
    }

    public boolean createSegmentObjForBitmap() {
        if (this.mOriginalImageBitmapScaled == null) {
            Log.e(TAG, "createSegmentObj mOriginalImageBitmapScaled == null");
            return false;
        }
        String str = TAG;
        Log.d(str, "createSegmentObj: " + this.mOriginalImageBitmapScaled);
        ByteBuffer allocate = ByteBuffer.allocate(this.mOriginalImageBitmapScaled.getByteCount());
        this.mOriginalImageBitmapScaled.copyPixelsToBuffer(allocate);
        this.mWrapsegment.setRGBDataSource(this.mOriginalImageBitmapScaled.getWidth(), this.mOriginalImageBitmapScaled.getHeight(), allocate.array(), this.mOriginalImageBitmapScaled.getWidth());
        return true;
    }

    public Bitmap setScaleforImage(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (height > width) {
            height = bitmap.getWidth();
            width = bitmap.getHeight();
        }
        int i = (width / 2) * 2;
        int i2 = (height / 2) * 2;
        if (height > 1080) {
            i = (((width * 1080) / height) / 2) * 2;
            i2 = 1080;
        }
        if (i > 1920) {
            i2 = (((i2 * 1920) / i) / 2) * 2;
            i = 1920;
        }
        if (bitmap.getWidth() < bitmap.getHeight()) {
            int i3 = i2;
            i2 = i;
            i = i3;
        }
        return Bitmap.createScaledBitmap(bitmap, i, i2, true);
    }

    public void SetDefaultGifWidth(int i) {
        this.mdefaultComposeGifWidth = (i / 2) * 2;
    }

    private void calculate_gif_size() {
        int width = this.mOriginalImageBitmapScaled.getWidth();
        int height = this.mOriginalImageBitmapScaled.getHeight();
        int i = this.mdefaultComposeGifWidth;
        if (i > width && i > height) {
            this.mComposeGifWidth = width;
            this.mComposeGifHeight = height;
        } else if (width > height) {
            this.mComposeGifWidth = i;
            this.mComposeGifHeight = (((i * height) / width) / 2) * 2;
        } else {
            this.mComposeGifHeight = i;
            this.mComposeGifWidth = (((i * width) / height) / 2) * 2;
        }
    }

    private Bitmap createSegmentObjForBitmapByUri(String str) {
        String str2 = TAG;
        Log.d(str2, "createSegmentObjForBitmapByUri: " + str);
        Bitmap scaleforImage = setScaleforImage(getRoateImg(str));
        if (this.mWrapsegment == null) {
            this.mWrapsegment = new WrapSegmentEngine();
        }
        this.mWrapsegment.ConstructWrapSegmentEngine();
        ByteBuffer allocate = ByteBuffer.allocate(scaleforImage.getByteCount());
        scaleforImage.copyPixelsToBuffer(allocate);
        this.mWrapsegment.setRGBDataSource(scaleforImage.getWidth(), scaleforImage.getHeight(), allocate.array(), scaleforImage.getWidth());
        return scaleforImage;
    }

    public boolean getSegmentResult() {
        boolean segmentResult = this.mWrapsegment.getSegmentResult();
        String str = TAG;
        Log.d(str, "getSegmentResult " + segmentResult);
        return segmentResult;
    }

    public boolean getExchangeResult() {
        boolean exchangeResult = this.mWrapsegment.getExchangeResult();
        String str = TAG;
        Log.d(str, "getExchangeResult " + exchangeResult);
        return exchangeResult;
    }

    private synchronized boolean createMediaEffectGraph(boolean z, Bitmap bitmap, boolean z2) {
        int i;
        if (!getSegmentResult()) {
            Log.e(TAG, "createMediaEffectGraph getSegmentResult false");
            return false;
        } else if (!getExchangeResult() && (i = this.mModelType) != 4 && i != 5 && i != 6) {
            String str = TAG;
            Log.e(str, "createMediaEffectGraph getExchangeResult = false , mModelType = " + this.mModelType);
            return false;
        } else {
            MediaEffectGraph mediaEffectGraph = new MediaEffectGraph();
            this.mMediaEffectGraph = mediaEffectGraph;
            mediaEffectGraph.ConstructMediaEffectGraph();
            this.mMediaEffectGraph.SetSharedContext();
            long AddVideoSource = this.mMediaEffectGraph.AddVideoSource("/sdcard/voip-data/XXXXxxxxx.png", true);
            if (AddVideoSource == 0) {
                return false;
            }
            if (z2) {
                HashMap hashMap = new HashMap();
                hashMap.put(NvsStreamingContext.COMPILE_FPS, String.valueOf(this.mComposeGifFrameRate));
                this.mMediaEffectGraph.SetParamsForVideoSource(AddVideoSource, hashMap);
            }
            if (bitmap == null) {
                Log.e(TAG, "createMediaEffectGraph ImageBitmapScaled == null");
                return false;
            }
            ByteBuffer allocate = ByteBuffer.allocate(bitmap.getByteCount());
            bitmap.copyPixelsToBuffer(allocate);
            MediaEffect.SetExternalSource(bitmap.getWidth(), bitmap.getHeight(), allocate.array(), bitmap.getWidth(), AddVideoSource);
            if (!z) {
                this.mMediaEffectGraph.AddAudioTrack(this.mAudioMaterial, true);
                if (this.mAudioMaterial == null || !new File(this.mAudioMaterial).exists()) {
                    String str2 = TAG;
                    Log.e(str2, "createMediaEffectGraph exists false: " + this.mAudioMaterial);
                    return false;
                }
            }
            long AddVideoBackGroud = this.mMediaEffectGraph.AddVideoBackGroud(this.mSkyMaterial, false);
            if (AddVideoBackGroud == 0) {
                return false;
            }
            if (this.mSkyMaterial != null && new File(this.mSkyMaterial).exists()) {
                if (z2) {
                    HashMap hashMap2 = new HashMap();
                    hashMap2.put("outfps", String.valueOf(this.mComposeGifFrameRate));
                    hashMap2.put("dropframe", "true");
                    this.mMediaEffectGraph.SetParamsForVideoSource(AddVideoBackGroud, hashMap2);
                }
                int i2 = this.mModelType;
                if (i2 == 4 || i2 == 5 || i2 == 6 || i2 == 9) {
                    long AddVideoBackGroud2 = this.mMediaEffectGraph.AddVideoBackGroud(this.mSkyExternMaterial, false);
                    if (AddVideoBackGroud2 == 0) {
                        return false;
                    }
                    if (this.mSkyExternMaterial != null && new File(this.mSkyExternMaterial).exists()) {
                        if (z2) {
                            HashMap hashMap3 = new HashMap();
                            hashMap3.put("outfps", String.valueOf(this.mComposeGifFrameRate));
                            hashMap3.put("dropframe", "true");
                            this.mMediaEffectGraph.SetParamsForVideoSource(AddVideoBackGroud2, hashMap3);
                        }
                    }
                    String str3 = TAG;
                    Log.e(str3, "createMediaEffectGraph exists false: " + this.mSkyExternMaterial);
                    return false;
                }
                if (this.mProspectsFilter != null && new File(this.mProspectsFilter).exists()) {
                    if (this.mOverallFilter != null && new File(this.mOverallFilter).exists()) {
                        long GetVideoBackGroudMixer = this.mMediaEffectGraph.GetVideoBackGroudMixer();
                        HashMap hashMap4 = new HashMap();
                        hashMap4.put("mixertype", Integer.toString(this.mModelType));
                        hashMap4.put("photopath1", this.mProspectsFilter);
                        hashMap4.put("photopath2", this.mOverallFilter);
                        if (this.mModelType == 9) {
                            if (this.mSubtitleMaterial != null && new File(this.mSubtitleMaterial).exists()) {
                                hashMap4.put("subtitle_material", this.mSubtitleMaterial);
                            }
                            hashMap4.put("subtitle_type", Integer.toString(this.mSubtitleType));
                            hashMap4.put("subtitle_text", this.mSubtitleText);
                            hashMap4.put("blur_material", this.mBlurMaterial);
                            hashMap4.put("blur_brightness", this.mBlurLightFilter);
                            hashMap4.put("fire_brightness", this.mFireLightFilter);
                        }
                        MediaEffect.SetParamsForEffect(EffectType.VideoMixer, GetVideoBackGroudMixer, hashMap4);
                        calculate_gif_size();
                        return true;
                    }
                    String str4 = TAG;
                    Log.e(str4, "createMediaEffectGraph exists false: " + this.mOverallFilter);
                    return false;
                }
                String str5 = TAG;
                Log.e(str5, "createMediaEffectGraph exists false: " + this.mProspectsFilter);
                return false;
            }
            String str6 = TAG;
            Log.e(str6, "createMediaEffectGraph exists false: " + this.mSkyMaterial);
            return false;
        }
    }

    public PreViewStatus getPreViewStatus() {
        EffectMediaPlayer effectMediaPlayer = this.mEffectPlayer;
        if (effectMediaPlayer != null) {
            return effectMediaPlayer.GetPreViewStatus();
        }
        return PreViewStatus.PreViewStopped;
    }

    public boolean startMagicSkyPreview() {
        String str = TAG;
        Log.d(str, "startMagicSkyPreview");
        synchronized (this) {
            releaseSource();
            if (!createMediaEffectGraph(false, this.mOriginalImageBitmapScaled, false)) {
                Log.e(str, "startMagicSkyPreview, createMediaEffectGraph false!");
                return false;
            }
            EffectMediaPlayer effectMediaPlayer = new EffectMediaPlayer(this.mMediaEffectGraph);
            this.mEffectPlayer = effectMediaPlayer;
            effectMediaPlayer.ConstructMediaPlayer();
            EffectNotifier effectNotifier = this.mNotifier;
            if (effectNotifier != null) {
                this.mEffectPlayer.SetPlayerNotify(effectNotifier);
            }
            this.mEffectPlayer.SetMediaFilterRenderPlayerCallback(this.mRenderPlayerNotifierCallBack);
            this.mEffectPlayer.SetGraphLoop(this.bPreviewLoop);
            if (this.bMute) {
                this.mEffectPlayer.mute();
            }
            this.mEffectPlayer.StartPreView();
            return true;
        }
    }

    public void pauseMagicSkyPreview() {
        Log.d(TAG, "pauseMagicSkyPreview");
        synchronized (this) {
            EffectMediaPlayer effectMediaPlayer = this.mEffectPlayer;
            if (effectMediaPlayer != null) {
                effectMediaPlayer.PausePreView();
            }
        }
    }

    public void resumeMagicSkyPreview() {
        Log.d(TAG, "resumeMagicSkyPreview");
        synchronized (this) {
            EffectMediaPlayer effectMediaPlayer = this.mEffectPlayer;
            if (effectMediaPlayer != null) {
                effectMediaPlayer.setGravity(EffectMediaPlayer.SurfaceGravity.SurfaceGravityResizeAspectFit, this.mSurfaceWidth, this.mSurfaceHeight);
                this.mEffectPlayer.ResumePreView();
            }
        }
    }

    public void stopMagicSkyPreview() {
        Log.d(TAG, "stopMagicSkyPreview");
        synchronized (this) {
            EffectMediaPlayer effectMediaPlayer = this.mEffectPlayer;
            if (effectMediaPlayer != null) {
                effectMediaPlayer.StopPreView();
            }
        }
    }

    public boolean getComposeMp4Status() {
        String str = TAG;
        Log.d(str, "getComposeStatus mp4: " + this.isComposingMp4 + " gif:" + this.isComposingGif);
        return this.isComposingMp4;
    }

    public boolean getComposeGifStatus() {
        String str = TAG;
        Log.d(str, "getComposeStatus mp4: " + this.isComposingMp4 + " gif:" + this.isComposingGif);
        return this.isComposingGif;
    }

    public boolean startComposeMp4() {
        String str = TAG;
        Log.d(str, "startComposeMp4");
        synchronized (this) {
            if (this.isComposingMp4) {
                Log.d(str, "hava startComposeMp4!");
                return false;
            }
            this.retCompose = false;
            this.isComposingMp4 = true;
            releaseSource();
            this.isClearLastComposePercent = true;
            if (!createMediaEffectGraph(this.bMute, this.mOriginalImageBitmapScaled, false)) {
                Log.d(str, "startComposeMp4, createMediaEffectGraph false!");
                this.isComposingMp4 = false;
                return false;
            }
            MediaComposeFile mediaComposeFile = new MediaComposeFile(this.mMediaEffectGraph);
            this.mComposeFile = mediaComposeFile;
            mediaComposeFile.ConstructMediaComposeFile(this.mOriginalImageBitmapScaled.getWidth(), this.mOriginalImageBitmapScaled.getHeight(), 20971520, 25);
            this.mComposeFile.SetComposeNotify(new EffectNotifier() { // from class: com.xiaomi.skyprocess.MagicSky.2
                @Override // com.xiaomi.skyprocess.EffectNotifier
                public void OnReceiveFinish() {
                    Log.d(MagicSky.TAG, "startComposeMp4 OnReceiveFinish");
                    MagicSky.this.isComposingMp4 = false;
                    MagicSky.this.retCompose = true;
                }

                @Override // com.xiaomi.skyprocess.EffectNotifier
                public void OnReceiveFailed() {
                    Log.d(MagicSky.TAG, "startComposeMp4 OnReceiveFailed");
                    MagicSky.this.isComposingMp4 = false;
                    MagicSky.this.retCompose = false;
                }

                @Override // com.xiaomi.skyprocess.EffectNotifier
                public void OnReceiveFrameInfo(int i, int i2) {
                    String str2 = MagicSky.TAG;
                    Log.d(str2, "startComposeMp4 OnReceiveFrameInfo width " + i + "x height " + i2);
                }

                @Override // com.xiaomi.skyprocess.EffectNotifier
                public void OnReceiveCacheFinished() {
                    Log.d(MagicSky.TAG, "startComposeMp4 OnReceiveCacheFinished");
                }

                @Override // com.xiaomi.skyprocess.EffectNotifier
                public void onReceiveProgressPercent(int i) {
                    String str2 = MagicSky.TAG;
                    Log.d(str2, "startComposeMp4 onReceiveProgressPercent: " + i);
                }
            });
            Log.d(str, "startComposeMp4 mCompseFileName: " + this.mCompseFileName);
            this.isComposingMp4 = true;
            setThreshold(this.mThreshold);
            this.mComposeFile.SetComposeFileFd(null, this.mCompseFileName);
            this.mComposeFile.BeginComposeFile();
            while (this.isComposingMp4) {
                try {
                    Thread.sleep(100L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return this.retCompose;
        }
    }

    public boolean startComposeGif() {
        String str = TAG;
        Log.d(str, "startComposeGif");
        synchronized (this) {
            if (this.isComposingGif) {
                Log.d(str, "hava startComposeGif!");
                return false;
            }
            this.retCompose = false;
            this.isComposingGif = true;
            releaseSource();
            this.isClearLastComposePercent = true;
            if (!createMediaEffectGraph(this.bMute, this.mOriginalImageBitmapScaled, true)) {
                Log.d(str, "startComposeGif, createMediaEffectGraph false!");
                this.isComposingGif = false;
                return false;
            }
            MediaComposeFile mediaComposeFile = new MediaComposeFile(this.mMediaEffectGraph);
            this.mComposeFile = mediaComposeFile;
            mediaComposeFile.ConstructMediaComposeFile(this.mOriginalImageBitmapScaled.getWidth(), this.mOriginalImageBitmapScaled.getHeight(), 20971520, 25);
            this.mComposeFile.SetComposeNotify(new EffectNotifier() { // from class: com.xiaomi.skyprocess.MagicSky.3
                @Override // com.xiaomi.skyprocess.EffectNotifier
                public void OnReceiveFinish() {
                    Log.d(MagicSky.TAG, "startComposeGif OnReceiveFinish");
                    MagicSky.this.isComposingGif = false;
                    MagicSky.this.retCompose = true;
                }

                @Override // com.xiaomi.skyprocess.EffectNotifier
                public void OnReceiveFailed() {
                    Log.d(MagicSky.TAG, "startComposeGif OnReceiveFailed");
                    MagicSky.this.isComposingGif = false;
                    MagicSky.this.retCompose = false;
                }

                @Override // com.xiaomi.skyprocess.EffectNotifier
                public void OnReceiveFrameInfo(int i, int i2) {
                    String str2 = MagicSky.TAG;
                    Log.d(str2, "startComposeGif OnReceiveFrameInfo width " + i + "x height " + i2);
                }

                @Override // com.xiaomi.skyprocess.EffectNotifier
                public void OnReceiveCacheFinished() {
                    Log.d(MagicSky.TAG, "startComposeGif OnReceiveCacheFinished");
                }

                @Override // com.xiaomi.skyprocess.EffectNotifier
                public void onReceiveProgressPercent(int i) {
                    String str2 = MagicSky.TAG;
                    Log.d(str2, "startComposeGif onReceiveProgressPercent: " + i);
                }
            });
            Log.d(str, "startComposeGif mCompseFileName: " + this.mCompseFileName);
            this.isComposingGif = true;
            setThreshold(this.mThreshold);
            this.mComposeFile.EnableComposeFile2Gif();
            this.mComposeFile.SetParamentForGif(this.mComposeGifWidth, this.mComposeGifHeight, this.mComposeGifFrameRate, this.mComposeGifBitRate, this.mCompseGifFileName);
            this.mComposeFile.SetComposeFileName(this.mCompseFileName);
            this.mComposeFile.BeginComposeFile();
            while (this.isComposingGif) {
                try {
                    Thread.sleep(100L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return this.retCompose;
        }
    }

    public void cancelCompose() {
        Log.d(TAG, "cancelCompose");
        synchronized (this) {
            MediaComposeFile mediaComposeFile = this.mComposeFile;
            if (mediaComposeFile != null) {
                mediaComposeFile.CancelComposeFile();
                this.retCompose = false;
                this.isComposingMp4 = false;
                this.isComposingGif = false;
            }
        }
    }

    public void clearLastComposePercent() {
        this.isClearLastComposePercent = false;
    }

    public int getCurrentComposePercent() {
        MediaComposeFile mediaComposeFile = this.mComposeFile;
        if (mediaComposeFile == null || !this.isClearLastComposePercent) {
            return 0;
        }
        return mediaComposeFile.getCurrentComposePercent();
    }

    public int getCurrentBatchPercent(int[] iArr) {
        if (iArr.length > 1) {
            iArr[0] = this.mTotalBatchCount;
            iArr[1] = this.mCurrentBatchNum;
        }
        MediaComposeFile mediaComposeFile = this.mComposeFile;
        if (mediaComposeFile != null) {
            return mediaComposeFile.getCurrentComposePercent();
        }
        return 0;
    }

    public boolean startBatchCompose() {
        String str = TAG;
        Log.d(str, "startBatchCompose");
        File file = new File(this.mBatchCompsePath);
        File file2 = new File(this.mBatchSavePath);
        if (!file.exists() || !file.isDirectory() || !file2.exists() || !file2.isDirectory()) {
            Log.e(str, "startBatchCompose error, dir not find: " + this.mBatchCompsePath + ", " + this.mBatchSavePath);
            return false;
        }
        File[] listFiles = file.listFiles();
        if (listFiles == null) {
            Log.e(str, "startBatchCompose error, dir is null: " + this.mBatchCompsePath);
            return false;
        }
        ArrayList arrayList = new ArrayList();
        for (File file3 : listFiles) {
            arrayList.add(file3.getAbsolutePath());
        }
        if (arrayList.isEmpty()) {
            Log.e(TAG, "startBatchCompose error, dir no image: " + this.mBatchCompsePath);
            return false;
        }
        initSegment();
        doBatchImages(arrayList);
        createSegmentObjForBitmap();
        releaseSegment();
        return true;
    }

    private void doBatchImages(List<String> list) {
        this.mTotalBatchCount = list.size();
        Bitmap bitmap = this.mOriginalImageBitmapScaled;
        int i = 0;
        while (i < this.mTotalBatchCount) {
            int i2 = i + 1;
            this.mCurrentBatchNum = i2;
            final String str = list.get(i);
            String substring = str.substring(str.lastIndexOf(h.g) + 1);
            String substring2 = substring.substring(0, substring.lastIndexOf("."));
            String str2 = TAG;
            Log.d(str2, "doBatchImages, image: " + str);
            releaseSource();
            Bitmap createSegmentObjForBitmapByUri = createSegmentObjForBitmapByUri(str);
            this.mOriginalImageBitmapScaled = createSegmentObjForBitmapByUri;
            setSkyModel(this.mUiModelTypeIndex, this.mSkyMaterialPath);
            if (!createMediaEffectGraph(false, createSegmentObjForBitmapByUri, true)) {
                Log.d(str2, "doBatchImages, createMediaEffectGraph false, image: " + str);
            } else {
                String str3 = this.mBatchSavePath + h.g + substring2 + "_" + Integer.toString(this.mModelType) + "_" + Integer.toString(i) + ".mp4";
                String str4 = this.mBatchSavePath + h.g + substring2 + "_" + Integer.toString(this.mModelType) + "_" + Integer.toString(i) + ".gif";
                MediaComposeFile mediaComposeFile = new MediaComposeFile(this.mMediaEffectGraph);
                this.mComposeFile = mediaComposeFile;
                mediaComposeFile.ConstructMediaComposeFile(createSegmentObjForBitmapByUri.getWidth(), createSegmentObjForBitmapByUri.getHeight(), 20971520, 25);
                this.mComposeFile.SetComposeNotify(new EffectNotifier() { // from class: com.xiaomi.skyprocess.MagicSky.4
                    @Override // com.xiaomi.skyprocess.EffectNotifier
                    public void OnReceiveFinish() {
                        String str5 = MagicSky.TAG;
                        Log.d(str5, "doBatchImages OnReceiveFinish: " + str);
                        MagicSky.this.isComposingMp4 = false;
                    }

                    @Override // com.xiaomi.skyprocess.EffectNotifier
                    public void OnReceiveFailed() {
                        String str5 = MagicSky.TAG;
                        Log.d(str5, "doBatchImages OnReceiveFailed: " + str);
                        MagicSky.this.isComposingMp4 = false;
                    }

                    @Override // com.xiaomi.skyprocess.EffectNotifier
                    public void OnReceiveFrameInfo(int i3, int i4) {
                        String str5 = MagicSky.TAG;
                        Log.d(str5, "doBatchImages OnReceiveFrameInfo width " + i3 + "x height " + i4);
                    }

                    @Override // com.xiaomi.skyprocess.EffectNotifier
                    public void OnReceiveCacheFinished() {
                        Log.d(MagicSky.TAG, "doBatchImages OnReceiveCacheFinished");
                    }

                    @Override // com.xiaomi.skyprocess.EffectNotifier
                    public void onReceiveProgressPercent(int i3) {
                        String str5 = MagicSky.TAG;
                        Log.d(str5, "doBatchImages onReceiveProgressPercent: " + i3);
                    }
                });
                long currentTimeMillis = System.currentTimeMillis();
                this.isComposingMp4 = true;
                this.mComposeFile.SetComposeFileName(str3);
                this.mComposeFile.BeginComposeFile();
                while (this.isComposingMp4) {
                    try {
                        Thread.sleep(100L);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                long currentTimeMillis2 = System.currentTimeMillis();
                String str5 = TAG;
                Log.d(str5, "MagicSky compose mp4 in batch last duration is: " + (currentTimeMillis2 - currentTimeMillis) + ", uri: " + str);
                releaseSource();
                if (!createMediaEffectGraph(false, createSegmentObjForBitmapByUri, false)) {
                    Log.d(str5, "doBatchImages, createMediaEffectGraph false, image: " + str);
                } else {
                    MediaComposeFile mediaComposeFile2 = new MediaComposeFile(this.mMediaEffectGraph);
                    this.mComposeFile = mediaComposeFile2;
                    mediaComposeFile2.ConstructMediaComposeFile(createSegmentObjForBitmapByUri.getWidth(), createSegmentObjForBitmapByUri.getHeight(), 20971520, 25);
                    this.mComposeFile.SetComposeNotify(new EffectNotifier() { // from class: com.xiaomi.skyprocess.MagicSky.5
                        @Override // com.xiaomi.skyprocess.EffectNotifier
                        public void OnReceiveFinish() {
                            String str6 = MagicSky.TAG;
                            Log.d(str6, "doBatchImages OnReceiveFinish: " + str);
                            MagicSky.this.isComposingGif = false;
                        }

                        @Override // com.xiaomi.skyprocess.EffectNotifier
                        public void OnReceiveFailed() {
                            String str6 = MagicSky.TAG;
                            Log.d(str6, "doBatchImages OnReceiveFailed: " + str);
                            MagicSky.this.isComposingGif = false;
                        }

                        @Override // com.xiaomi.skyprocess.EffectNotifier
                        public void OnReceiveFrameInfo(int i3, int i4) {
                            String str6 = MagicSky.TAG;
                            Log.d(str6, "doBatchImages OnReceiveFrameInfo width " + i3 + "x height " + i4);
                        }

                        @Override // com.xiaomi.skyprocess.EffectNotifier
                        public void OnReceiveCacheFinished() {
                            Log.d(MagicSky.TAG, "doBatchImages OnReceiveCacheFinished");
                        }

                        @Override // com.xiaomi.skyprocess.EffectNotifier
                        public void onReceiveProgressPercent(int i3) {
                            String str6 = MagicSky.TAG;
                            Log.d(str6, "doBatchImages onReceiveProgressPercent: " + i3);
                        }
                    });
                    long currentTimeMillis3 = System.currentTimeMillis();
                    this.isComposingGif = true;
                    this.mComposeFile.EnableComposeFile2Gif();
                    setCompseGifParament(str4, 2048, 10);
                    this.mComposeFile.SetParamentForGif(this.mComposeGifWidth, this.mComposeGifHeight, this.mComposeGifFrameRate, this.mComposeGifBitRate, this.mCompseGifFileName);
                    this.mComposeFile.SetComposeFileName("/sdcard/voip-data/temp-huantian.mp4");
                    this.mComposeFile.BeginComposeFile();
                    while (this.isComposingGif) {
                        try {
                            Thread.sleep(100L);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                    long currentTimeMillis4 = System.currentTimeMillis();
                    Log.d(TAG, "MagicSky compose gif in batch last duration is: " + (currentTimeMillis4 - currentTimeMillis3) + ", uri: " + str);
                }
            }
            i = i2;
        }
        this.mOriginalImageBitmapScaled = bitmap;
    }

    public int getBitMapScaledWidth() {
        return this.mOriginalImageBitmapScaled.getWidth();
    }

    public int getBitMapScaledHeight() {
        return this.mOriginalImageBitmapScaled.getHeight();
    }

    public void setOriginalImageBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            Log.e(TAG, "setOriginalImageBitmap: OriginalImageBitmap == null");
            return;
        }
        this.mOriginalImageBitmapScaled = setScaleforImage(bitmap);
        String str = TAG;
        Log.d(str, "wangqm setOriginalImageBitmap, original W x H: " + bitmap.getWidth() + " X " + bitmap.getHeight() + ", scaled W x H: " + this.mOriginalImageBitmapScaled.getWidth() + " X " + this.mOriginalImageBitmapScaled.getHeight());
    }

    public boolean setSkyModel(int i, String str) {
        if (this.mOriginalImageBitmapScaled == null) {
            Log.e(TAG, "setSkyModel mOriginalImageBitmapScaled == null");
            return false;
        }
        this.mUiModelTypeIndex = i;
        this.mModelType = transformModelType(i);
        this.mSkyMaterialPath = str;
        this.mAudioMaterial = this.mSkyMaterialPath + "/audio.mp3";
        this.mProspectsFilter = this.mSkyMaterialPath + "/prospectsFilters.png";
        this.mOverallFilter = this.mSkyMaterialPath + "/overallFilters.png";
        int i2 = this.mModelType;
        if (i2 == 4 || i2 == 5) {
            this.mSkyExternMaterial = this.mSkyMaterialPath + "/external.mp4";
        } else if (i2 == 9) {
            this.mSubtitleMaterial = this.mSkyMaterialPath + "/MiLanProVF.ttf";
            this.mBlurMaterial = this.mSkyMaterialPath + "/blurMaterial.png";
            this.mFireLightFilter = this.mSkyMaterialPath + "/fireLight.png";
            this.mBlurLightFilter = this.mSkyMaterialPath + "/blurLight.png";
            if (this.mOriginalImageBitmapScaled.getWidth() > this.mOriginalImageBitmapScaled.getHeight()) {
                this.mSkyExternMaterial = this.mSkyMaterialPath + "/landscape_particle.mp4";
            } else {
                this.mSkyExternMaterial = this.mSkyMaterialPath + "/portrait_particle.mp4";
            }
        } else {
            this.mSkyExternMaterial = null;
        }
        if (this.mOriginalImageBitmapScaled.getWidth() > this.mOriginalImageBitmapScaled.getHeight()) {
            int i3 = this.mModelType;
            if (i3 == 4 || i3 == 5 || i3 == 6) {
                this.mSkyMaterial = this.mSkyMaterialPath + "/landscape.jpg";
            } else {
                this.mSkyMaterial = this.mSkyMaterialPath + "/landscape.mp4";
            }
            if (this.mModelType == 6) {
                this.mSkyExternMaterial = this.mSkyMaterialPath + "/externallandscape.mp4";
            }
        } else {
            int i4 = this.mModelType;
            if (i4 == 4 || i4 == 5 || i4 == 6) {
                this.mSkyMaterial = this.mSkyMaterialPath + "/portrait.jpg";
            } else {
                this.mSkyMaterial = this.mSkyMaterialPath + "/portrait.mp4";
            }
            if (this.mModelType == 6) {
                this.mSkyExternMaterial = this.mSkyMaterialPath + "/externalportrait.mp4";
            }
        }
        String str2 = TAG;
        Log.d(str2, "setModelIndex, mModelType: " + this.mModelType + ", mSkyMaterialPath: " + this.mSkyMaterialPath + ", mSkyMaterial: " + this.mSkyMaterial + ", mSkyExternMaterial: " + this.mSkyExternMaterial + ", mAudioMaterial: " + this.mAudioMaterial + ", mProspectsFilter: " + this.mProspectsFilter + ", mOverallFilter:" + this.mOverallFilter);
        return true;
    }

    public void setSubtitleModel(int i, String str) {
        this.mSubtitleType = i;
        this.mSubtitleText = str;
        String str2 = TAG;
        Log.d(str2, "setSubtitleModel, mSubtitleType: " + this.mSubtitleType + ", mSubtitleText: " + this.mSubtitleText);
    }

    public void setVideoSurface(Surface surface, int i, int i2) {
        String str = TAG;
        Log.d(str, "setVideoSurface: " + surface);
        this.mVideoSurface = surface;
        this.mSurfaceWidth = i;
        this.mSurfaceHeight = i2;
    }

    public void setThreshold(float f) {
        String str = TAG;
        Log.d(str, "setThreshold: " + f);
        if (f < -1.0f) {
            this.mThreshold = -1.0f;
        } else if (f > 1.0f) {
            this.mThreshold = 1.0f;
        } else {
            this.mThreshold = f;
        }
        MediaEffectGraph mediaEffectGraph = this.mMediaEffectGraph;
        if (mediaEffectGraph != null) {
            long GetVideoBackGroudMixer = mediaEffectGraph.GetVideoBackGroudMixer();
            if (GetVideoBackGroudMixer == 0) {
                return;
            }
            HashMap hashMap = new HashMap();
            hashMap.put("brightness", Float.toString(this.mThreshold));
            MediaEffect.SetParamsForEffect(EffectType.VideoMixer, GetVideoBackGroudMixer, hashMap);
        }
    }

    public boolean setCompseFileName(String str) {
        this.mCompseFileName = str;
        String str2 = TAG;
        Log.d(str2, "setCompseFileName: " + str);
        return true;
    }

    public boolean setCompseGifParament(String str, int i, int i2) {
        this.mCompseGifFileName = str;
        this.mComposeGifBitRate = i;
        this.mComposeGifFrameRate = i2;
        String str2 = TAG;
        Log.d(str2, "setCompseGifParament: " + this.mCompseGifFileName);
        return true;
    }

    public boolean setBatchCompsePath(String str) {
        if (str == null || !new File(str).exists()) {
            String str2 = TAG;
            Log.e(str2, "setBatchCompsePath BatchCompsePath exists false: " + str);
            return false;
        }
        this.mBatchCompsePath = str;
        String str3 = TAG;
        Log.d(str3, "setBatchCompsePath: " + str);
        return true;
    }

    public boolean setmBatchSavePath(String str) {
        if (str == null || !new File(str).exists()) {
            String str2 = TAG;
            Log.e(str2, "setmBatchSavePath BatchSavePath exists false: " + str);
            return false;
        }
        this.mBatchSavePath = str;
        String str3 = TAG;
        Log.d(str3, "setmBatchSavePath: " + str);
        return true;
    }

    public void setCallbackNotify(EffectNotifier effectNotifier) {
        String str = TAG;
        Log.d(str, "SetComposeNotify: " + effectNotifier);
        this.mNotifier = effectNotifier;
    }

    public void setPriewLoop(boolean z) {
        String str = TAG;
        Log.d(str, "setPriewLoop: " + z);
        this.bPreviewLoop = z;
        EffectMediaPlayer effectMediaPlayer = this.mEffectPlayer;
        if (effectMediaPlayer != null) {
            effectMediaPlayer.SetPlayLoop(z);
        }
    }

    public void mute() {
        Log.d(TAG, "mutePreview");
        this.bMute = true;
        EffectMediaPlayer effectMediaPlayer = this.mEffectPlayer;
        if (effectMediaPlayer != null) {
            effectMediaPlayer.mute();
        }
    }

    public void unmute() {
        Log.d(TAG, "unMutePreview");
        this.bMute = false;
        EffectMediaPlayer effectMediaPlayer = this.mEffectPlayer;
        if (effectMediaPlayer != null) {
            effectMediaPlayer.unmute();
        }
    }

    private int transformModelType(int i) {
        String str = this.mTransformModelType.get(String.valueOf(i));
        if (str == null) {
            str = "0";
        }
        int parseInt = Integer.parseInt(str);
        String str2 = TAG;
        Log.d(str2, "transformModelType, input modelindex: " + i + "output modelindex: " + parseInt);
        return parseInt;
    }

    private Bitmap getRoateImg(String str) {
        ExifInterface exifInterface;
        try {
            exifInterface = new ExifInterface(str);
        } catch (IOException e) {
            e.printStackTrace();
            exifInterface = null;
        }
        int attributeInt = exifInterface.getAttributeInt("Orientation", -1);
        int i = 0;
        if (attributeInt == 6) {
            i = 90;
        } else if (attributeInt == 3) {
            i = nexClip.kClip_Rotate_180;
        } else if (attributeInt == 8) {
            i = nexClip.kClip_Rotate_270;
        }
        Matrix matrix = new Matrix();
        Bitmap decodeFile = BitmapFactory.decodeFile(str);
        int width = decodeFile.getWidth();
        int height = decodeFile.getHeight();
        matrix.setRotate(i);
        String str2 = TAG;
        Log.d(str2, "BitmapFactory orientation:" + i + " width:" + width + " height:" + height);
        return Bitmap.createBitmap(decodeFile, 0, 0, width, height, matrix, true);
    }

    public void initSegment() {
        WrapSegmentEngine wrapSegmentEngine = this.mWrapsegment;
        if (wrapSegmentEngine != null) {
            wrapSegmentEngine.initSegment();
        }
    }

    public void releaseSegment() {
        WrapSegmentEngine wrapSegmentEngine = this.mWrapsegment;
        if (wrapSegmentEngine != null) {
            wrapSegmentEngine.releaseSegment();
        }
    }

    public void setExchangeResult(int i) {
        WrapSegmentEngine wrapSegmentEngine = this.mWrapsegment;
        if (wrapSegmentEngine != null) {
            boolean z = true;
            if (i != 1) {
                z = false;
            }
            wrapSegmentEngine.setExchangeResult(z);
        }
    }

    public void setExchangeResult(boolean z) {
        WrapSegmentEngine wrapSegmentEngine = this.mWrapsegment;
        if (wrapSegmentEngine != null) {
            wrapSegmentEngine.setExchangeResult(z);
        }
    }

    public void releaseSource() {
        synchronized (this) {
            this.isComposingGif = false;
            this.isComposingMp4 = false;
            EffectMediaPlayer effectMediaPlayer = this.mEffectPlayer;
            if (effectMediaPlayer != null) {
                effectMediaPlayer.StopPreView();
                this.mEffectPlayer.DestructMediaPlayer();
                this.mEffectPlayer = null;
            }
            MediaComposeFile mediaComposeFile = this.mComposeFile;
            if (mediaComposeFile != null) {
                mediaComposeFile.CancelComposeFile();
                this.mComposeFile.DestructMediaComposeFile();
                this.mComposeFile = null;
            }
            MediaEffectGraph mediaEffectGraph = this.mMediaEffectGraph;
            if (mediaEffectGraph != null) {
                mediaEffectGraph.DestructMediaEffectGraph();
                this.mMediaEffectGraph = null;
            }
        }
    }

    public void releaseRender() {
        synchronized (this) {
            MiGLSurfaceViewRender miGLSurfaceViewRender = this.mMiGLSurfaceViewRender;
            if (miGLSurfaceViewRender != null) {
                miGLSurfaceViewRender.onDestroy();
                this.mMiGLSurfaceViewRender = null;
            }
        }
    }
}
