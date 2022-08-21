package com.nexstreaming.nexeditorsdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import ch.qos.logback.core.CoreConstants;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.nexstreaming.app.common.task.Task;
import com.nexstreaming.kminternal.kinemaster.config.EditorGlobal;
import com.nexstreaming.kminternal.kinemaster.mediainfo.MediaInfo;
import com.nexstreaming.kminternal.nexvideoeditor.LayerRenderer;
import com.nexstreaming.kminternal.nexvideoeditor.NexAudioClip;
import com.nexstreaming.kminternal.nexvideoeditor.NexDrawInfo;
import com.nexstreaming.kminternal.nexvideoeditor.NexEditor;
import com.nexstreaming.kminternal.nexvideoeditor.NexThemeView;
import com.nexstreaming.kminternal.nexvideoeditor.NexVisualClip;
import com.nexstreaming.nexeditorsdk.exception.InvalidRangeException;
import com.nexstreaming.nexeditorsdk.exception.ProjectNotAttachedException;
import com.nexstreaming.nexeditorsdk.module.nexExternalExportProvider;
import com.nexstreaming.nexeditorsdk.module.nexFaceDetectionProvider;
import com.nexstreaming.nexeditorsdk.nexOverlayItem;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.keyczar.Keyczar;

/* loaded from: classes3.dex */
public final class nexEngine implements SurfaceHolder.Callback {
    public static final int DirectExportOption_AudioEncode = 1;
    public static final int DirectExportOption_None = 0;
    public static final int ExportAVCLevel1 = 1;
    public static final int ExportAVCLevel11 = 4;
    public static final int ExportAVCLevel12 = 8;
    public static final int ExportAVCLevel13 = 16;
    public static final int ExportAVCLevel1b = 2;
    public static final int ExportAVCLevel2 = 32;
    public static final int ExportAVCLevel21 = 64;
    public static final int ExportAVCLevel22 = 128;
    public static final int ExportAVCLevel3 = 256;
    public static final int ExportAVCLevel31 = 512;
    public static final int ExportAVCLevel32 = 1024;
    public static final int ExportAVCLevel4 = 2048;
    public static final int ExportAVCLevel41 = 4096;
    public static final int ExportAVCLevel42 = 8192;
    public static final int ExportAVCLevel5 = 16384;
    public static final int ExportAVCLevel51 = 32768;
    public static final int ExportAVCLevel52 = 65536;
    public static final int ExportCodec_AVC = 268501760;
    public static final int ExportCodec_HEVC = 268502016;
    public static final int ExportCodec_MPEG4V = 268566784;
    public static final int ExportCropMode_Enhanced = 1;
    public static final int ExportCropMode_Fill = 2;
    public static final int ExportCropMode_None = 0;
    public static final int ExportHEVCHighTierLevel1 = 2;
    public static final int ExportHEVCHighTierLevel2 = 8;
    public static final int ExportHEVCHighTierLevel21 = 32;
    public static final int ExportHEVCHighTierLevel3 = 128;
    public static final int ExportHEVCHighTierLevel31 = 512;
    public static final int ExportHEVCHighTierLevel4 = 2048;
    public static final int ExportHEVCHighTierLevel41 = 8192;
    public static final int ExportHEVCHighTierLevel5 = 32768;
    public static final int ExportHEVCHighTierLevel51 = 131072;
    public static final int ExportHEVCHighTierLevel52 = 524288;
    public static final int ExportHEVCHighTierLevel6 = 2097152;
    public static final int ExportHEVCHighTierLevel61 = 8388608;
    public static final int ExportHEVCHighTierLevel62 = 33554432;
    public static final int ExportHEVCMainTierLevel1 = 1;
    public static final int ExportHEVCMainTierLevel2 = 4;
    public static final int ExportHEVCMainTierLevel21 = 16;
    public static final int ExportHEVCMainTierLevel3 = 64;
    public static final int ExportHEVCMainTierLevel31 = 256;
    public static final int ExportHEVCMainTierLevel4 = 1024;
    public static final int ExportHEVCMainTierLevel41 = 4096;
    public static final int ExportHEVCMainTierLevel5 = 16384;
    public static final int ExportHEVCMainTierLevel51 = 65536;
    public static final int ExportHEVCMainTierLevel52 = 262144;
    public static final int ExportHEVCMainTierLevel6 = 1048576;
    public static final int ExportHEVCMainTierLevel61 = 4194304;
    public static final int ExportHEVCMainTierLevel62 = 16777216;
    public static final int ExportMPEG4Level0 = 1;
    public static final int ExportMPEG4Level0b = 2;
    public static final int ExportMPEG4Level1 = 4;
    public static final int ExportMPEG4Level2 = 8;
    public static final int ExportMPEG4Level3 = 16;
    public static final int ExportMPEG4Level4 = 32;
    public static final int ExportMPEG4Level4a = 64;
    public static final int ExportMPEG4Level5 = 128;
    public static final int ExportProfile_AVCBaseline = 1;
    public static final int ExportProfile_AVCExtended = 3;
    public static final int ExportProfile_AVCHigh = 4;
    public static final int ExportProfile_AVCHigh10 = 5;
    public static final int ExportProfile_AVCHigh422 = 6;
    public static final int ExportProfile_AVCHigh444 = 7;
    public static final int ExportProfile_AVCMain = 2;
    public static final int ExportProfile_HEVCMain = 1;
    public static final int ExportProfile_HEVCMain10 = 2;
    public static final int ExportProfile_MPEG4VASP = 32768;
    public static final int ExportProfile_MPEG4VSimple = 1;
    private static final int PERSIST_INTERVAL = 500;
    private static final int PREWAKE_INTERVAL = 2000;
    private static final String TAG = "nexEngine";
    private static final int export_audio_sampling_rate = 44100;
    private static final int export_fps = 3000;
    private static final int kState_export = 2;
    private static final int kState_idle = 0;
    private static final int kState_load = 1;
    private static Comparator<nexOverlayItem> layerZOrderComparator = new Comparator<nexOverlayItem>() { // from class: com.nexstreaming.nexeditorsdk.nexEngine.5
        @Override // java.util.Comparator
        /* renamed from: a */
        public int compare(nexOverlayItem nexoverlayitem, nexOverlayItem nexoverlayitem2) {
            int zOrder = nexoverlayitem.getZOrder() - nexoverlayitem2.getZOrder();
            if (zOrder < 0) {
                return -1;
            }
            return zOrder > 0 ? 1 : 0;
        }
    };
    public static final int retCheckDirectExport_ClipCountZero = 3;
    public static final int retCheckDirectExport_EncoderDSIMismatch = 11;
    public static final int retCheckDirectExport_FilterApplied = 15;
    public static final int retCheckDirectExport_HasImageClip = 7;
    public static final int retCheckDirectExport_HasSpeedControl = 12;
    public static final int retCheckDirectExport_HasVideoLayer = 8;
    public static final int retCheckDirectExport_InvalidClipList = 2;
    public static final int retCheckDirectExport_InvalidHandle = 1;
    public static final int retCheckDirectExport_InvalidRotate = 14;
    public static final int retCheckDirectExport_InvalidVideoInfo = 4;
    public static final int retCheckDirectExport_NotStartIFrame = 6;
    public static final int retCheckDirectExport_OK = 0;
    public static final int retCheckDirectExport_SetClipEffect = 10;
    public static final int retCheckDirectExport_SetTransitionEffect = 9;
    public static final int retCheckDirectExport_UnmatchedVideoCodec = 5;
    public static final int retCheckDirectExport_UnsupportedCodec = 13;
    private static int sExportVideoTrackUUIDMode = 0;
    private static boolean sLoadListAsync = true;
    private static int sNextId = 1;
    private static nexEngineListener sTranscodeListener = null;
    private static boolean sTranscodeMode = false;
    private static nexProject sTranscodeProject;
    private static ExportProfile[] s_exportProfiles;
    private Context mAppContext;
    private List<NexAudioClip> mCachedNexAudioClips;
    private List<NexVisualClip> mCachedNexVisualClips;
    private int mCurrentPlayTime;
    private int mEncodeBitrate;
    private int mEncodeHeight;
    private long mEncodeMaxFileSize;
    private int mEncodeWidth;
    private int mEnhancedCropMode;
    private int mEnhancedCropOutputHeight;
    private int mEnhancedCropOutputWidth;
    private String mExportFilePath;
    private int mExportTotalTime;
    private boolean mForceMixExportMode;
    private int mId;
    private Surface mSurface;
    private SurfaceView mSurfaceView;
    private NexEditor mVideoEditor;
    private nexProject mProject = null;
    private com.nexstreaming.kminternal.nexvideoeditor.c mEditorListener = null;
    private OnSurfaceChangeListener m_onSurfaceChangeListener = null;
    private int mState = 0;
    private nexEngineListener mEventListener = null;
    private NexEditor.PlayState mPlayState = NexEditor.PlayState.IDLE;
    private Set<nexOverlayItem> mActiveRenderLayers = new HashSet();
    private List<nexOverlayItem> mRenderInCurrentPass = new ArrayList();
    private int lastCheckDirectExportOption = 0;
    private int lastSeekTime = 0;
    private boolean bLetterBox = false;
    private boolean cacheSeekMode = false;
    private boolean externalImageExportProcessing = false;
    public boolean bNeedScaling = false;
    private int mLastProjectFadeIn = -1;
    private int mLastProjectFadeOut = -1;
    private int mLastProjectVolume = -1;
    private int mLastManualVolCtl = -1;
    private Object m_layerRenderLock = new Object();
    private boolean m_layerLock = false;
    private NexEditor.c m_layerRenderCallback = new NexEditor.c() { // from class: com.nexstreaming.nexeditorsdk.nexEngine.6
        @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.c
        public void a(LayerRenderer layerRenderer) {
            synchronized (nexEngine.this.m_layerRenderLock) {
                if (nexEngine.this.m_layerLock) {
                    return;
                }
                nexEngine.this.mRenderInCurrentPass.clear();
                if (nexEngine.this.mProject == null) {
                    return;
                }
                if (nexEngine.this.mProject.getOverlayItems() == null) {
                    return;
                }
                if (nexEngine.this.mProject.getOverlayItems().size() > 0) {
                    int g = layerRenderer.g();
                    for (nexOverlayItem nexoverlayitem : nexEngine.this.mProject.getOverlayItems()) {
                        int startTime = nexoverlayitem.getStartTime();
                        int endTime = nexoverlayitem.getEndTime();
                        boolean contains = nexEngine.this.mActiveRenderLayers.contains(nexoverlayitem);
                        if (startTime <= g && endTime > g) {
                            if (!contains) {
                                nexEngine.this.mActiveRenderLayers.add(nexoverlayitem);
                                nexoverlayitem.onRenderAwake(layerRenderer);
                            }
                            nexEngine.this.mRenderInCurrentPass.add(nexoverlayitem);
                        } else if (contains) {
                            nexEngine.this.mActiveRenderLayers.remove(nexoverlayitem);
                            nexoverlayitem.onRenderAsleep(layerRenderer);
                        }
                    }
                }
                Collections.sort(nexEngine.this.mRenderInCurrentPass, nexEngine.layerZOrderComparator);
                int size = nexEngine.this.mRenderInCurrentPass.size();
                for (int i = 0; i < size; i++) {
                    ((nexOverlayItem) nexEngine.this.mRenderInCurrentPass.get(i)).onRender(layerRenderer);
                }
            }
        }
    };
    private boolean facedetect_asyncmode = true;
    private int facedetect_syncclip_count = 1;
    private int facedetect_undetected_clip_cropping_mode = 0;
    public ArrayList<AsyncTask<String, Void, com.nexstreaming.kminternal.kinemaster.utils.facedetect.a>> async_facedetect_worker_list_ = new ArrayList<>();

    /* loaded from: classes3.dex */
    public enum FastPreviewOption {
        normal,
        brightness,
        contrast,
        saturation,
        adj_brightness,
        adj_contrast,
        adj_saturation,
        tintColor,
        cts,
        adj_vignette,
        adj_vignetteRange,
        adj_sharpness,
        customlut_clip,
        customlut_power
    }

    /* loaded from: classes3.dex */
    public static abstract class OnAutoTrimRatioResultListener {
        public abstract void onAutoTrimRatioResult(int i, int[] iArr, int[] iArr2);
    }

    /* loaded from: classes3.dex */
    public static abstract class OnAutoTrimResultListener {
        public abstract void onAutoTrimResult(int i, int[] iArr);
    }

    /* loaded from: classes3.dex */
    public static abstract class OnCaptureListener {
        public abstract void onCapture(Bitmap bitmap);

        public abstract void onCaptureFail(nexErrorCode nexerrorcode);
    }

    /* loaded from: classes3.dex */
    public static abstract class OnCompletionListener {
        public abstract void onComplete(int i);
    }

    /* loaded from: classes3.dex */
    public static abstract class OnSeekCompletionListener {
        public abstract void onSeekComplete(int i, int i2, int i3);
    }

    /* loaded from: classes3.dex */
    public static abstract class OnSurfaceChangeListener {
        public abstract void onSurfaceChanged();
    }

    /* loaded from: classes3.dex */
    public enum OverlayCommand {
        clear,
        upload,
        lock,
        unlock
    }

    private void applyCropSpeed() {
    }

    @Deprecated
    public boolean KineMixExport(String str) {
        return false;
    }

    @Deprecated
    public void cancelKineMixExport() {
    }

    @Deprecated
    public int checkKineMixExport(boolean z) {
        return 2;
    }

    @Deprecated
    public boolean checkKineMixExport() {
        return false;
    }

    @Deprecated
    public boolean checkKineMixExport(String str, String str2) {
        return false;
    }

    public void setBrightness(int i) {
        NexEditor nexEditor = this.mVideoEditor;
        if (nexEditor != null) {
            nexEditor.setBrightness(i);
        }
    }

    public void setContrast(int i) {
        NexEditor nexEditor = this.mVideoEditor;
        if (nexEditor != null) {
            nexEditor.setContrast(i);
        }
    }

    public void setSaturation(int i) {
        NexEditor nexEditor = this.mVideoEditor;
        if (nexEditor != null) {
            nexEditor.setSaturation(i);
        }
    }

    public int getBrightness() {
        NexEditor nexEditor = this.mVideoEditor;
        if (nexEditor != null) {
            return nexEditor.getBrightness();
        }
        return 0;
    }

    public int getContrast() {
        NexEditor nexEditor = this.mVideoEditor;
        if (nexEditor != null) {
            return nexEditor.getContrast();
        }
        return 0;
    }

    public int getSaturation() {
        NexEditor nexEditor = this.mVideoEditor;
        if (nexEditor != null) {
            return nexEditor.getSaturation();
        }
        return 0;
    }

    public void setVignette(int i) {
        NexEditor nexEditor = this.mVideoEditor;
        if (nexEditor != null) {
            nexEditor.setVignette(i);
        }
    }

    public int getVignette() {
        NexEditor nexEditor = this.mVideoEditor;
        if (nexEditor != null) {
            return nexEditor.getVignette();
        }
        return 0;
    }

    public int getVignetteRange() {
        NexEditor nexEditor = this.mVideoEditor;
        if (nexEditor != null) {
            return nexEditor.getVignetteRange();
        }
        return 0;
    }

    public void setVignetteRange(int i) {
        NexEditor nexEditor = this.mVideoEditor;
        if (nexEditor != null) {
            nexEditor.setVignetteRange(i);
        }
    }

    public void setSharpness(int i) {
        NexEditor nexEditor = this.mVideoEditor;
        if (nexEditor != null) {
            nexEditor.setSharpness(i);
        }
    }

    public int getSharpness() {
        NexEditor nexEditor = this.mVideoEditor;
        if (nexEditor != null) {
            return nexEditor.getSharpness();
        }
        return 0;
    }

    public void setDeviceLightLevel(int i) {
        NexEditor nexEditor = this.mVideoEditor;
        if (nexEditor != null) {
            nexEditor.setDeviceLightLevel(i);
        }
    }

    public void setDeviceGamma(float f) {
        NexEditor nexEditor = this.mVideoEditor;
        if (nexEditor != null) {
            nexEditor.setDeviceGamma(f);
        }
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.v(TAG, "surfaceCreated called()");
        if (this.mSurfaceView == null) {
            return;
        }
        Surface surface = surfaceHolder.getSurface();
        this.mSurface = surface;
        NexEditor nexEditor = this.mVideoEditor;
        if (nexEditor == null) {
            return;
        }
        nexEditor.a(surface);
        this.mVideoEditor.w();
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        int i4;
        int i5;
        Log.v(TAG, "surfaceChanged called(" + i2 + "," + i3 + ")");
        if (i2 == 0 || i3 == 0) {
            Log.e(TAG, "invalid video width(" + i2 + ") or height(" + i3 + ")");
            return;
        }
        float aspectRatio = nexApplicationConfig.getAspectRatio();
        if (aspectRatio > 0.0f) {
            float f = i3 * aspectRatio;
            float f2 = i2;
            if (f > f2) {
                i5 = Math.round(f2 / aspectRatio);
                i4 = i2;
                Log.d(TAG, "surfaceChanged aspect view size " + i4 + "x" + i5);
                if (i4 == i2 || i5 != i3) {
                    Log.d(TAG, "surfaceChanged new view size " + i4 + "x" + i5);
                    surfaceHolder.setFixedSize(i4, i5);
                }
                Surface surface = surfaceHolder.getSurface();
                this.mSurface = surface;
                NexEditor nexEditor = this.mVideoEditor;
                if (nexEditor == null) {
                    return;
                }
                nexEditor.a(surface);
                this.mVideoEditor.w();
                return;
            }
            i4 = Math.round(f);
        } else {
            i4 = i2;
        }
        i5 = i3;
        Log.d(TAG, "surfaceChanged aspect view size " + i4 + "x" + i5);
        if (i4 == i2) {
        }
        Log.d(TAG, "surfaceChanged new view size " + i4 + "x" + i5);
        surfaceHolder.setFixedSize(i4, i5);
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.v(TAG, "surfaceDestroyed called()");
        if (surfaceHolder != null && this.mSurface == surfaceHolder.getSurface()) {
            this.mSurface = null;
        }
        NexEditor nexEditor = this.mVideoEditor;
        if (nexEditor != null) {
            nexEditor.a((Surface) null);
        }
    }

    public nexEngine(Context context) {
        int i = sNextId;
        sNextId = i + 1;
        this.mId = i;
        Log.d(TAG, "[" + this.mId + "] nexEngine create");
        this.mAppContext = context;
        this.mVideoEditor = EditorGlobal.a();
        NexEditor.a(nexApplicationConfig.getAspectProfile().getWidth(), nexApplicationConfig.getAspectProfile().getHeight(), nexApplicationConfig.getOverlayCoordinateMode());
        this.mVideoEditor.a(nexApplicationConfig.getScreenMode());
        this.mVideoEditor.a(this.m_layerRenderCallback);
        if (this.mVideoEditor.y() == 1) {
            this.mVideoEditor.e(true);
        } else {
            this.mVideoEditor.e(false);
        }
    }

    public nexEngine(Context context, boolean z) {
        int i = sNextId;
        sNextId = i + 1;
        this.mId = i;
        Log.d(TAG, "[" + this.mId + "] nexEngine create internal");
        this.mAppContext = context;
        this.mVideoEditor = EditorGlobal.a();
        NexEditor.a(nexApplicationConfig.getAspectProfile().getWidth(), nexApplicationConfig.getAspectProfile().getHeight(), nexApplicationConfig.getOverlayCoordinateMode());
        this.mVideoEditor.a(nexApplicationConfig.getScreenMode());
        this.mVideoEditor.a(this.m_layerRenderCallback);
        if (z) {
            setMark();
        }
    }

    public void setMark() {
        if (this.mVideoEditor.y() == 1) {
            this.mVideoEditor.e(true);
        } else {
            this.mVideoEditor.e(false);
        }
    }

    public void setEventHandler(nexEngineListener nexenginelistener) {
        Log.d(TAG, "[" + this.mId + "] setEventHandler =" + nexenginelistener);
        this.mEventListener = nexenginelistener;
        setEditorListener();
    }

    public static void setLoadListAsync(boolean z) {
        sLoadListAsync = z;
    }

    public void removeEditorListener() {
        if (this.mEditorListener != null) {
            this.mEditorListener = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setEditorListener() {
        if (this.mEditorListener != null) {
            return;
        }
        com.nexstreaming.kminternal.nexvideoeditor.c cVar = new com.nexstreaming.kminternal.nexvideoeditor.c() { // from class: com.nexstreaming.nexeditorsdk.nexEngine.1
            @Override // com.nexstreaming.kminternal.nexvideoeditor.c
            public void a(NexEditor.PlayState playState, NexEditor.PlayState playState2) {
                NexEditor.PlayState playState3;
                NexEditor.PlayState playState4;
                Log.i(nexEngine.TAG, "[" + nexEngine.this.mId + "]onStateChange() oldState=" + playState + ", newState=" + playState2 + ",curState=" + nexEngine.this.mState + ", trans=" + nexEngine.sTranscodeMode);
                if (playState != NexEditor.PlayState.NONE || playState2 != NexEditor.PlayState.IDLE) {
                    nexEngine.this.cacheSeekMode = false;
                } else {
                    nexEngine.this.mState = 1;
                }
                if (nexEngine.sTranscodeMode) {
                    if (nexEngine.this.mState != 2 || playState != (playState4 = NexEditor.PlayState.RECORD) || playState2 == playState4 || nexEngine.sTranscodeListener == null) {
                        return;
                    }
                    nexEngine.sTranscodeListener.onStateChange(playState.getValue(), playState2.getValue());
                    return;
                }
                nexEngine.this.mPlayState = playState2;
                if (nexEngine.this.mState == 2 && playState == (playState3 = NexEditor.PlayState.RECORD) && playState2 != playState3) {
                    if (playState2 == NexEditor.PlayState.PAUSE) {
                        Log.d(nexEngine.TAG, "[" + nexEngine.this.mId + "]new State is " + playState2);
                    } else if (playState2 != NexEditor.PlayState.RESUME) {
                        nexEngine.this.mState = 1;
                    } else {
                        Log.d(nexEngine.TAG, "[" + nexEngine.this.mId + "]new State is " + playState2);
                    }
                }
                if (nexEngine.this.mEventListener == null) {
                    return;
                }
                nexEngine.this.mEventListener.onStateChange(playState.getValue(), playState2.getValue());
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.c
            public void a(int i) {
                int i2;
                if (nexEngine.this.externalImageExportProcessing) {
                    return;
                }
                if (nexEngine.sTranscodeMode) {
                    if (nexEngine.sTranscodeListener == null) {
                        return;
                    }
                    int i3 = nexEngine.this.mExportTotalTime;
                    if (i3 <= 0) {
                        i3 = nexEngine.sTranscodeProject.getTotalTime();
                    }
                    if (i3 <= 0) {
                        i2 = i / 10;
                    } else {
                        i2 = (i * 100) / i3;
                    }
                    if (i2 < 0 || i2 > 100) {
                        return;
                    }
                    nexEngine.sTranscodeListener.onEncodingProgress(i2);
                    return;
                }
                nexEngine.this.mCurrentPlayTime = i;
                if (nexEngine.this.mEventListener == null) {
                    return;
                }
                if (nexEngine.this.mState == 2 || nexEngine.this.mPlayState == NexEditor.PlayState.RECORD) {
                    int i4 = nexEngine.this.mExportTotalTime;
                    if (i4 <= 0) {
                        i4 = nexEngine.this.mProject.getTotalTime();
                    }
                    int i5 = i4 <= 0 ? 0 : (i * 100) / i4;
                    Log.d(nexEngine.TAG, "[" + nexEngine.this.mId + "]export progress = " + i5 + ", duration=" + i4 + ", currentTime=" + i);
                    if (i5 < 0 || i5 > 100) {
                        return;
                    }
                    nexEngine.this.mEventListener.onEncodingProgress(i5);
                    return;
                }
                nexEngine.this.mEventListener.onTimeChange(i);
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.c
            public void b(int i) {
                if (nexEngine.this.externalImageExportProcessing) {
                    return;
                }
                if (nexEngine.this.mState != 2 && nexEngine.this.mEventListener != null) {
                    nexEngine.this.mEventListener.onSetTimeDone(i);
                }
                nexEngine.this.mCurrentPlayTime = i;
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.c
            public void a(NexEditor.ErrorCode errorCode) {
                if (!nexEngine.this.externalImageExportProcessing && nexEngine.this.mEventListener != null) {
                    nexEngine.this.mEventListener.onSetTimeFail(errorCode.getValue());
                }
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.c
            public void a() {
                if (!nexEngine.this.externalImageExportProcessing && nexEngine.this.mEventListener != null) {
                    nexEngine.this.mEventListener.onSetTimeIgnored();
                }
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.c
            public void b(NexEditor.ErrorCode errorCode) {
                nexEngine.this.mState = 1;
                Log.i(nexEngine.TAG, "[" + nexEngine.this.mId + "]onEncodingDone() =" + errorCode + ", trans=" + nexEngine.sTranscodeMode + ", forceMix=" + nexEngine.this.mForceMixExportMode);
                if (nexEngine.sTranscodeMode) {
                    if (nexEngine.sTranscodeListener != null) {
                        if (!errorCode.isError()) {
                            nexEngine.sTranscodeListener.onEncodingProgress(100);
                        }
                        nexEngine.sTranscodeListener.onEncodingDone(errorCode.isError(), errorCode.getValue());
                    }
                    boolean unused = nexEngine.sTranscodeMode = false;
                    nexEngine.this.resolveProject(true, true);
                } else if (nexEngine.this.mEventListener != null) {
                    if (!errorCode.isError()) {
                        nexEngine.this.mEventListener.onEncodingProgress(100);
                    }
                    nexEngine.this.mEventListener.onEncodingDone(errorCode.isError(), errorCode.getValue());
                }
                nexEngine.this.mExportTotalTime = 0;
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.c
            public void b() {
                nexEngine.this.mState = 1;
                if (nexEngine.this.mEventListener != null) {
                    nexEngine.this.mEventListener.onPlayEnd();
                }
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.c
            public void a(NexEditor.ErrorCode errorCode, int i) {
                if (nexEngine.this.mEventListener != null) {
                    nexEngine.this.mEventListener.onPlayFail(errorCode.getValue(), i);
                }
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.c
            public void c() {
                if (nexEngine.this.mEventListener != null) {
                    nexEngine.this.mEventListener.onPlayStart();
                }
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.c
            public void d() {
                if (nexEngine.this.mEventListener != null) {
                    nexEngine.this.mEventListener.onClipInfoDone();
                }
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.c
            public void a(boolean z) {
                if (nexEngine.this.mEventListener != null) {
                    nexEngine.this.mEventListener.onSeekStateChanged(z);
                }
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.c
            public void b(NexEditor.ErrorCode errorCode, int i) {
                if (nexEngine.this.mEventListener != null) {
                    nexEngine.this.mEventListener.onCheckDirectExport(i);
                }
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.c
            public void a(int i, int i2) {
                if (nexEngine.this.mEventListener != null) {
                    if (i > i2) {
                        i = 100;
                    }
                    nexEngine.this.mEventListener.onProgressThumbnailCaching(i, i2);
                }
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.c
            public void b(int i, int i2) {
                if (nexEngine.this.mEventListener != null) {
                    if (i > i2) {
                        i = 100;
                    }
                    nexEngine.this.mEventListener.onEncodingProgress(i);
                }
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.c
            public void c(NexEditor.ErrorCode errorCode) {
                if (nexEngine.this.mEventListener != null) {
                    nexEngine.this.mEventListener.onEncodingDone(errorCode.isError(), errorCode.getValue());
                }
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.c
            public void a(NexEditor.ErrorCode errorCode, int i, int i2) {
                if (nexEngine.this.mEventListener != null) {
                    nexEngine.this.mEventListener.onFastPreviewStartDone(errorCode.getValue(), i, i2);
                }
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.c
            public void d(NexEditor.ErrorCode errorCode) {
                if (nexEngine.this.mEventListener != null) {
                    nexEngine.this.mEventListener.onFastPreviewStopDone(errorCode.getValue());
                }
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.c
            public void e(NexEditor.ErrorCode errorCode) {
                if (nexEngine.this.mEventListener != null) {
                    nexEngine.this.mEventListener.onFastPreviewTimeDone(errorCode.getValue());
                }
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.c
            public void c(int i, int i2) {
                if (nexEngine.this.mEventListener != null) {
                    nexEngine.this.mEventListener.onPreviewPeakMeter(i, i2);
                }
            }
        };
        this.mEditorListener = cVar;
        this.mVideoEditor.a(cVar);
    }

    public void setOnSurfaceChangeListener(OnSurfaceChangeListener onSurfaceChangeListener) {
        this.m_onSurfaceChangeListener = onSurfaceChangeListener;
        this.mVideoEditor.a(new NexEditor.q() { // from class: com.nexstreaming.nexeditorsdk.nexEngine.12
            @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.q
            public void a() {
                if (nexEngine.this.m_onSurfaceChangeListener != null) {
                    nexEngine.this.m_onSurfaceChangeListener.onSurfaceChanged();
                }
            }
        });
    }

    public int setView(nexEngineView nexengineview) {
        Log.d(TAG, "[" + this.mId + "] setView nexEngineView=" + nexengineview);
        SurfaceView surfaceView = this.mSurfaceView;
        if (surfaceView != null) {
            surfaceView.getHolder().removeCallback(this);
            this.mSurfaceView = null;
        }
        this.mVideoEditor.a(nexengineview);
        return 0;
    }

    public int setView(SurfaceView surfaceView) {
        Log.d(TAG, "[" + this.mId + "] setView SurfaceView=" + surfaceView);
        this.mVideoEditor.a((NexThemeView) null);
        SurfaceView surfaceView2 = this.mSurfaceView;
        if (surfaceView != surfaceView2 && surfaceView2 != null) {
            surfaceView2.getHolder().removeCallback(this);
        }
        this.mSurfaceView = surfaceView;
        if (surfaceView != null) {
            surfaceView.getHolder().addCallback(this);
            return 0;
        }
        return 0;
    }

    public nexEngineView getView() {
        return (nexEngineView) this.mVideoEditor.k();
    }

    public void setProject(nexProject nexproject) {
        Log.d(TAG, "[" + this.mId + "] setProject");
        this.mProject = nexproject;
        defaultFaceDetectSetting();
    }

    public void clearFaceDetectInfo() {
        nexProject nexproject = this.mProject;
        if (nexproject != null) {
            nexproject.clearFaceDetectInfo();
        }
    }

    public nexProject getProject() {
        return this.mProject;
    }

    private boolean isSetProject(boolean z) {
        nexProject nexproject;
        nexProject nexproject2 = this.mProject;
        if (nexproject2 == null || nexproject2.getTotalClipCount(true) <= 0) {
            if (sTranscodeMode && (nexproject = sTranscodeProject) != null && nexproject.getTotalClipCount(true) > 0) {
                return true;
            }
            if (z) {
                throw new ProjectNotAttachedException();
            }
            return false;
        }
        return true;
    }

    public void play() {
        this.cacheSeekMode = false;
        if (isSetProject(false)) {
            if (this.mState == 2) {
                Log.w(TAG, "[" + this.mId + "]export state");
                return;
            }
            stopAsyncFaceDetect();
            setOverlays(OverlayCommand.upload);
            resolveProject(sLoadListAsync, true);
            faceDetect_internal(this.facedetect_asyncmode, this.facedetect_syncclip_count, this.facedetect_undetected_clip_cropping_mode);
            loadEffectsInEditor(false);
            setEditorListener();
            this.mVideoEditor.t();
        }
    }

    public boolean play(boolean z) {
        this.cacheSeekMode = false;
        if (z) {
            try {
                play();
                return true;
            } catch (Exception unused) {
                return false;
            }
        }
        play();
        return true;
    }

    public void stop() {
        if (this.externalImageExportProcessing) {
            this.mVideoEditor.u();
            this.mState = 1;
            return;
        }
        this.cacheSeekMode = false;
        if (!isSetProject(false)) {
            return;
        }
        if (this.mState == 2) {
            this.mState = 1;
            this.mVideoEditor.s();
            return;
        }
        this.mState = 1;
        this.mVideoEditor.s();
    }

    public void exportSaveStop(final OnCompletionListener onCompletionListener) {
        if (this.externalImageExportProcessing) {
            return;
        }
        if (isSetProject(false)) {
            this.mState = 1;
            this.mVideoEditor.a(1, new NexEditor.f() { // from class: com.nexstreaming.nexeditorsdk.nexEngine.14
                @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.f
                public void a(NexEditor.ErrorCode errorCode) {
                    onCompletionListener.onComplete(errorCode.getValue());
                    nexEngine.this.mState = 1;
                }
            });
            return;
        }
        onCompletionListener.onComplete(21);
    }

    public void stop(final OnCompletionListener onCompletionListener) {
        if (this.externalImageExportProcessing) {
            this.mVideoEditor.u();
            if (onCompletionListener != null) {
                onCompletionListener.onComplete(0);
            }
            this.mState = 1;
            return;
        }
        this.cacheSeekMode = false;
        if (isSetProject(false)) {
            this.mState = 1;
            this.mVideoEditor.a(new NexEditor.f() { // from class: com.nexstreaming.nexeditorsdk.nexEngine.15
                @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.f
                public void a(NexEditor.ErrorCode errorCode) {
                    onCompletionListener.onComplete(errorCode.getValue());
                }
            });
            return;
        }
        onCompletionListener.onComplete(21);
    }

    @Deprecated
    public void stopSync() {
        if (isSetProject(false)) {
            Log.i(TAG, "[" + this.mId + "]stopSync start");
            NexEditor.PlayState playState = this.mPlayState;
            if (playState == NexEditor.PlayState.NONE || playState == NexEditor.PlayState.IDLE) {
                Log.i(TAG, "[" + this.mId + "]stopSync IDLE state");
                this.mState = 1;
                return;
            }
            this.mVideoEditor.b(true);
            this.mVideoEditor.s();
            while (this.mPlayState != NexEditor.PlayState.IDLE) {
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.mState = 1;
            this.mVideoEditor.b(false);
            Log.i(TAG, "[" + this.mId + "]stopSync End!");
        }
    }

    public void pause() {
        if (isSetProject(false)) {
            this.mVideoEditor.s();
        }
    }

    public void resume() {
        this.cacheSeekMode = false;
        if (isSetProject(false)) {
            undoForceMixProject();
            setEditorListener();
            setOverlays(OverlayCommand.upload);
            faceDetect_internal(this.facedetect_asyncmode, this.facedetect_syncclip_count, this.facedetect_undetected_clip_cropping_mode);
            this.mVideoEditor.t();
        }
    }

    public void seek(int i) {
        if (this.cacheSeekMode) {
            Log.d(TAG, "[" + this.mId + "] seek fail! cacheSeekMode");
        } else if (!isSetProject(false)) {
        } else {
            undoForceMixProject();
            setEditorListener();
            setOverlays(OverlayCommand.upload);
            stopAsyncFaceDetect();
            faceDetect_for_seek(i, this.facedetect_undetected_clip_cropping_mode);
            this.lastSeekTime = i;
            this.mVideoEditor.j(i);
        }
    }

    public void seekIDRorI(int i) {
        if (this.cacheSeekMode) {
            Log.d(TAG, "[" + this.mId + "] seekIDRorI fail! cacheSeekMode");
        } else if (!isSetProject(false)) {
        } else {
            undoForceMixProject();
            setEditorListener();
            setOverlays(OverlayCommand.upload);
            this.lastSeekTime = i;
            this.mVideoEditor.d(i, (NexEditor.p) null);
        }
    }

    public void seekIDROnly(int i) {
        if (this.cacheSeekMode) {
            Log.d(TAG, "[" + this.mId + "] seekIDROnly fail! cacheSeekMode");
        } else if (!isSetProject(false)) {
        } else {
            undoForceMixProject();
            setEditorListener();
            setOverlays(OverlayCommand.upload);
            this.lastSeekTime = i;
            this.mVideoEditor.e(i, (NexEditor.p) null);
        }
    }

    public void seekIDROnly(int i, final OnSeekCompletionListener onSeekCompletionListener) {
        if (this.cacheSeekMode) {
            Log.d(TAG, "[" + this.mId + "] seekIDROnly fail! cacheSeekMode");
        } else if (!isSetProject(false)) {
        } else {
            undoForceMixProject();
            setEditorListener();
            setOverlays(OverlayCommand.upload);
            this.lastSeekTime = i;
            this.mVideoEditor.e(i, new NexEditor.p() { // from class: com.nexstreaming.nexeditorsdk.nexEngine.16
                @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.p
                public String a() {
                    return "seekIDROnly";
                }

                @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.p
                public void a(int i2, int i3) {
                    OnSeekCompletionListener onSeekCompletionListener2 = onSeekCompletionListener;
                    if (onSeekCompletionListener2 != null) {
                        onSeekCompletionListener2.onSeekComplete(0, i2, i3);
                    }
                }

                @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.p
                public void a(NexEditor.ErrorCode errorCode) {
                    OnSeekCompletionListener onSeekCompletionListener2 = onSeekCompletionListener;
                    if (onSeekCompletionListener2 != null) {
                        onSeekCompletionListener2.onSeekComplete(errorCode.getValue(), 0, 0);
                    }
                }
            });
        }
    }

    public boolean isCacheSeekMode() {
        return this.cacheSeekMode;
    }

    public boolean startCollectCache(int i, final OnCompletionListener onCompletionListener) {
        if (isSetProject(false)) {
            if (MediaInfo.k()) {
                if (onCompletionListener != null) {
                    onCompletionListener.onComplete(NexEditor.ErrorCode.THUMBNAIL_BUSY.getValue());
                }
                return false;
            }
            this.cacheSeekMode = true;
            undoForceMixProject();
            setEditorListener();
            setOverlays(OverlayCommand.upload);
            this.mVideoEditor.a(i, new NexEditor.p() { // from class: com.nexstreaming.nexeditorsdk.nexEngine.17
                @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.p
                public String a() {
                    return null;
                }

                @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.p
                public void a(int i2, int i3) {
                    OnCompletionListener onCompletionListener2 = onCompletionListener;
                    if (onCompletionListener2 != null) {
                        onCompletionListener2.onComplete(0);
                    }
                }

                @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.p
                public void a(NexEditor.ErrorCode errorCode) {
                    nexEngine.this.cacheSeekMode = false;
                    OnCompletionListener onCompletionListener2 = onCompletionListener;
                    if (onCompletionListener2 != null) {
                        onCompletionListener2.onComplete(errorCode.getValue());
                    }
                }
            });
            return true;
        }
        return false;
    }

    public boolean seekFromCache(int i) {
        if (this.cacheSeekMode && isSetProject(false)) {
            undoForceMixProject();
            setEditorListener();
            setOverlays(OverlayCommand.upload);
            this.mVideoEditor.b(i, (NexEditor.p) null);
            return true;
        }
        return false;
    }

    public Bitmap getThumbnailCache(int i, int i2) {
        Bitmap n;
        if (this.cacheSeekMode && (n = this.mVideoEditor.n(i)) != null) {
            int width = n.getWidth();
            int height = n.getHeight();
            Rect rect = new Rect(0, 0, width, height);
            if (i2 == 180) {
                Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(createBitmap);
                canvas.rotate(180.0f, width / 2, height / 2);
                canvas.drawBitmap(n, rect, new Rect(0, 0, width, height), (Paint) null);
                return createBitmap;
            } else if (i2 == 0) {
                Bitmap createBitmap2 = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                new Canvas(createBitmap2).drawBitmap(n, rect, new Rect(0, 0, width, height), (Paint) null);
                return createBitmap2;
            } else if (i2 == 270) {
                Bitmap createBitmap3 = Bitmap.createBitmap(height, width, Bitmap.Config.RGB_565);
                Canvas canvas2 = new Canvas(createBitmap3);
                canvas2.rotate(90.0f, 0.0f, 0.0f);
                canvas2.drawBitmap(n, rect, new Rect(0, -height, width, 0), (Paint) null);
                return createBitmap3;
            } else if (i2 != 90) {
                return null;
            } else {
                Bitmap createBitmap4 = Bitmap.createBitmap(height, width, Bitmap.Config.RGB_565);
                Canvas canvas3 = new Canvas(createBitmap4);
                canvas3.rotate(270.0f, 0.0f, 0.0f);
                canvas3.drawBitmap(n, rect, new Rect(-width, 0, 0, height), (Paint) null);
                return createBitmap4;
            }
        }
        return null;
    }

    public void stopCollectCache(int i) {
        this.cacheSeekMode = false;
        seek(i);
    }

    public int export(String str, int i, int i2, int i3, long j, int i4, int i5) {
        return export(str, i, i2, i3, j, i4, i5, 0, 0, export_fps, ExportCodec_AVC);
    }

    public int export(String str, int i, int i2, int i3, long j, int i4, int i5, int i6) {
        return export(str, i, i2, i3, j, i4, i5, 0, 0, i6, ExportCodec_AVC);
    }

    public int export(String str, int i, int i2, int i3, long j, final int i4, final int i5, final int i6, final int i7, final int i8, final int i9) {
        if (this.mState == 2) {
            Log.w(TAG, "[" + this.mId + "]already export state");
            return -1;
        } else if (!isSetProject(true)) {
            return -1;
        } else {
            stopAsyncFaceDetect();
            setOverlays(OverlayCommand.upload);
            resolveProject(sLoadListAsync, true);
            faceDetect_internal(this.facedetect_asyncmode, this.facedetect_syncclip_count, this.facedetect_undetected_clip_cropping_mode);
            this.mState = 2;
            setThumbnailRoutine(2);
            this.mExportTotalTime = this.mProject.getTotalTime();
            this.mExportFilePath = str;
            this.mEncodeWidth = i;
            this.mEncodeHeight = i2;
            this.mEncodeBitrate = i3;
            this.mEncodeMaxFileSize = j;
            this.mVideoEditor.a(this.mAppContext).mo1850onComplete(new Task.OnTaskEventListener() { // from class: com.nexstreaming.nexeditorsdk.nexEngine.18
                @Override // com.nexstreaming.app.common.task.Task.OnTaskEventListener
                public void onTaskEvent(Task task, Task.Event event) {
                    nexEngine.this.loadEffectsInEditor(true);
                    nexEngine.this.setEditorListener();
                    int i10 = i4;
                    int i11 = i10 != 90 ? i10 != 180 ? i10 != 270 ? 0 : 64 : 32 : 16;
                    nexEngine nexengine = nexEngine.this;
                    if (nexengine.bNeedScaling) {
                        nexengine.bNeedScaling = false;
                        i11 |= nexEngine.ExportHEVCMainTierLevel6;
                    }
                    nexengine.mVideoEditor.a((NexEditor.i) null);
                    nexEngine.this.mVideoEditor.a(nexEngine.sExportVideoTrackUUIDMode, (byte[]) null);
                    nexEngine.this.mVideoEditor.a(nexEngine.this.mExportFilePath, nexEngine.this.mEncodeWidth, nexEngine.this.mEncodeHeight, nexEngine.this.mEncodeBitrate, nexEngine.this.mEncodeMaxFileSize, 0, false, i5, i6, i7, i8, i9, i11).mo1851onFailure(new Task.OnFailListener() { // from class: com.nexstreaming.nexeditorsdk.nexEngine.18.1
                        @Override // com.nexstreaming.app.common.task.Task.OnFailListener
                        public void onFail(Task task2, Task.Event event2, Task.TaskError taskError) {
                            Log.e(nexEngine.TAG, "[" + nexEngine.this.mId + "]export fail!=" + taskError);
                        }
                    });
                }
            });
            return 0;
        }
    }

    public int exportNoException(String str, int i, int i2, int i3, long j, int i4, int i5, int i6, int i7, int i8, int i9) {
        try {
            return export(str, i, i2, i3, j, i4, i5, i6, i7, i8, i9);
        } catch (Exception unused) {
            return -2;
        }
    }

    public int exportNoException(String str, int i, int i2, int i3, long j, int i4, int i5, int i6, int i7, int i8) {
        try {
            return export(str, i, i2, i3, j, i4, i5, i6, i7, i8, ExportCodec_AVC);
        } catch (Exception unused) {
            return -2;
        }
    }

    public int export(String str, int i, int i2, int i3, long j, int i4) {
        return export(str, i, i2, i3, j, i4, export_audio_sampling_rate, 0, 0, export_fps, ExportCodec_AVC);
    }

    public nexErrorCode exportJpeg(String str, int i, int i2, int i3, final OnCaptureListener onCaptureListener) {
        if (onCaptureListener == null) {
            return nexErrorCode.ARGUMENT_FAILED;
        }
        return nexErrorCode.fromValue(this.mVideoEditor.a(i, i2, 0, new NexEditor.e() { // from class: com.nexstreaming.nexeditorsdk.nexEngine.19
            @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.e
            public void a(Bitmap bitmap) {
                onCaptureListener.onCapture(bitmap);
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.e
            public void a(NexEditor.ErrorCode errorCode) {
                onCaptureListener.onCaptureFail(nexErrorCode.fromValue(errorCode.getValue()));
            }
        }).getValue());
    }

    /* loaded from: classes3.dex */
    public class a extends NexEditor.v {
        private nexExternalExportProvider b;
        private nexExportListener c;

        public a(nexExternalExportProvider nexexternalexportprovider, nexExportListener nexexportlistener) {
            this.b = nexexternalexportprovider;
            this.c = nexexportlistener;
        }

        @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.v
        public boolean a(byte[] bArr, int i) {
            return this.b.OnPushData(i, bArr);
        }

        @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.v
        public void a() {
            this.b.OnLastProcess();
        }

        @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.v
        public boolean a(int i) {
            nexExportListener nexexportlistener;
            nexErrorCode nexerrorcode = nexErrorCode.EXPORT_WRITER_START_FAIL;
            if (i == -3) {
                nexerrorcode = nexErrorCode.EXPORT_WRITER_INIT_FAIL;
            } else if (i == -2) {
                nexerrorcode = nexErrorCode.EXPORT_USER_CANCEL;
            } else if (i == -1) {
                nexerrorcode = nexErrorCode.INVALID_STATE;
            } else if (i == 0) {
                nexerrorcode = nexErrorCode.NONE;
            }
            boolean OnEnd = this.b.OnEnd(i);
            if (i < 0 && (nexexportlistener = this.c) != null) {
                nexexportlistener.onExportFail(nexerrorcode);
            }
            nexExportListener nexexportlistener2 = this.c;
            if (nexexportlistener2 == null) {
                if (nexEngine.this.mEventListener != null) {
                    nexEngine.this.mEventListener.onEncodingDone(i != 0, nexerrorcode.getValue());
                }
            } else {
                nexexportlistener2.onExportDone(null);
            }
            nexEngine.this.externalImageExportProcessing = false;
            nexEngine.this.mState = 1;
            return OnEnd;
        }

        @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.v
        public void b(int i) {
            nexExportListener nexexportlistener = this.c;
            if (nexexportlistener == null) {
                if (nexEngine.this.mEventListener == null) {
                    return;
                }
                nexEngine.this.mEventListener.onEncodingProgress(i);
                return;
            }
            nexexportlistener.onExportProgress(i);
        }

        @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.v
        public int b() {
            return nexEngine.this.async_facedetect_worker_list_.size();
        }
    }

    private NexEditor.v getExternalExport(nexExportFormat nexexportformat, String str, String str2, nexExportListener nexexportlistener) {
        if (str2 == null) {
            nexExternalExportProvider nexexternalexportprovider = (nexExternalExportProvider) nexExternalModuleManager.getInstance().getModule(str, nexExternalExportProvider.class);
            if (nexexternalexportprovider != null && nexexternalexportprovider.OnPrepare(nexexportformat)) {
                return new a(nexexternalexportprovider, nexexportlistener);
            }
            return null;
        }
        Object module = nexExternalModuleManager.getInstance().getModule(str2);
        if (module != null && nexExternalExportProvider.class.isInstance(module)) {
            nexExternalExportProvider nexexternalexportprovider2 = (nexExternalExportProvider) module;
            if (nexexternalexportprovider2.OnPrepare(nexexportformat)) {
                return new a(nexexternalexportprovider2, nexexportlistener);
            }
        }
        return null;
    }

    public nexErrorCode export(nexExportFormat nexexportformat, final nexExportListener nexexportlistener) {
        String string = nexexportformat.getString(nexExportFormat.TAG_FORMAT_TYPE);
        if (string == null) {
            return nexErrorCode.ARGUMENT_FAILED;
        }
        if (string.startsWith("external-")) {
            if (this.mState == 2) {
                Log.w(TAG, "[" + this.mId + "]already external export state");
                return nexErrorCode.INVALID_STATE;
            }
            int parseInt = Integer.parseInt(nexexportformat.formats.get(nexExportFormat.TAG_FORMAT_WIDTH));
            int parseInt2 = Integer.parseInt(nexexportformat.formats.get(nexExportFormat.TAG_FORMAT_HEIGHT));
            int parseInt3 = Integer.parseInt(nexexportformat.formats.get(nexExportFormat.TAG_FORMAT_INTERVAL_TIME));
            int parseInt4 = Integer.parseInt(nexexportformat.formats.get(nexExportFormat.TAG_FORMAT_START_TIME));
            int parseInt5 = Integer.parseInt(nexexportformat.formats.get(nexExportFormat.TAG_FORMAT_END_TIME));
            if (parseInt5 > this.mProject.getTotalTime()) {
                parseInt5 = this.mProject.getTotalTime();
            }
            if (parseInt4 > parseInt5) {
                return nexErrorCode.ARGUMENT_FAILED;
            }
            NexEditor.v externalExport = getExternalExport(nexexportformat, string.substring(9), nexexportformat.formats.get(nexExportFormat.TAG_FORMAT_UUID), nexexportlistener);
            if (externalExport == null) {
                return nexErrorCode.DIRECTEXPORT_ENC_ENCODE_ERROR;
            }
            this.externalImageExportProcessing = true;
            if (NexEditor.ErrorCode.NONE == this.mVideoEditor.a(parseInt, parseInt2, parseInt3, parseInt4, parseInt5, externalExport)) {
                this.mState = 2;
                return nexErrorCode.NONE;
            }
            this.externalImageExportProcessing = false;
            externalExport.a();
            externalExport.a(-1);
            if (nexexportlistener != null) {
                nexexportlistener.onExportFail(nexErrorCode.INVALID_STATE);
            }
        }
        if (string.compareTo("bitmap") == 0) {
            int parseInt6 = Integer.parseInt(nexexportformat.formats.get(nexExportFormat.TAG_FORMAT_WIDTH));
            int parseInt7 = Integer.parseInt(nexexportformat.formats.get(nexExportFormat.TAG_FORMAT_HEIGHT));
            int parseInt8 = Integer.parseInt(nexexportformat.formats.get(nexExportFormat.TAG_FORMAT_TAG));
            if (nexexportlistener == null) {
                return nexErrorCode.ARGUMENT_FAILED;
            }
            return nexErrorCode.fromValue(this.mVideoEditor.a(parseInt6, parseInt7, parseInt8, new NexEditor.e() { // from class: com.nexstreaming.nexeditorsdk.nexEngine.20
                @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.e
                public void a(Bitmap bitmap) {
                    nexexportlistener.onExportDone(bitmap);
                }

                @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.e
                public void a(NexEditor.ErrorCode errorCode) {
                    nexexportlistener.onExportFail(nexErrorCode.fromValue(errorCode.getValue()));
                }
            }).getValue());
        } else if (string.compareTo("jpeg") == 0) {
            final String str = nexexportformat.formats.get(nexExportFormat.TAG_FORMAT_PATH);
            int parseInt9 = Integer.parseInt(nexexportformat.formats.get(nexExportFormat.TAG_FORMAT_WIDTH));
            int parseInt10 = Integer.parseInt(nexexportformat.formats.get(nexExportFormat.TAG_FORMAT_HEIGHT));
            final int parseInt11 = Integer.parseInt(nexexportformat.formats.get(nexExportFormat.TAG_FORMAT_QUALITY));
            if (str == null || str.length() <= 0 || parseInt9 <= 0 || parseInt10 <= 0 || parseInt11 <= 0 || parseInt11 > 100) {
                return nexErrorCode.ARGUMENT_FAILED;
            }
            return nexErrorCode.fromValue(this.mVideoEditor.a(parseInt9, parseInt10, 0, new NexEditor.e() { // from class: com.nexstreaming.nexeditorsdk.nexEngine.2
                @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.e
                public void a(Bitmap bitmap) {
                    NexEditor.ErrorCode errorCode = NexEditor.ErrorCode.NONE;
                    File file = new File(str);
                    if (file.exists()) {
                        file.delete();
                    }
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, parseInt11, fileOutputStream);
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        errorCode = NexEditor.ErrorCode.EXPORT_WRITER_INIT_FAIL;
                    }
                    nexExportListener nexexportlistener2 = nexexportlistener;
                    if (nexexportlistener2 == null) {
                        if (nexEngine.this.mEditorListener == null) {
                            return;
                        }
                        nexEngine.this.mEditorListener.b(errorCode);
                        return;
                    }
                    nexexportlistener2.onExportDone(null);
                }

                @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.e
                public void a(NexEditor.ErrorCode errorCode) {
                    nexExportListener nexexportlistener2 = nexexportlistener;
                    if (nexexportlistener2 == null) {
                        if (nexEngine.this.mEditorListener == null) {
                            return;
                        }
                        nexEngine.this.mEditorListener.b(errorCode);
                        return;
                    }
                    nexexportlistener2.onExportFail(nexErrorCode.fromValue(errorCode.getValue()));
                }
            }).getValue());
        } else if (string.compareTo("mp4") == 0) {
            try {
                String str2 = nexexportformat.formats.get(nexExportFormat.TAG_FORMAT_PATH);
                int parseInt12 = Integer.parseInt(nexexportformat.formats.get(nexExportFormat.TAG_FORMAT_WIDTH));
                int parseInt13 = Integer.parseInt(nexexportformat.formats.get(nexExportFormat.TAG_FORMAT_HEIGHT));
                int parseInt14 = Integer.parseInt(nexexportformat.formats.get(nexExportFormat.TAG_FORMAT_VIDEO_CODEC));
                int parseInt15 = Integer.parseInt(nexexportformat.formats.get(nexExportFormat.TAG_FORMAT_VIDEO_BITRATE));
                int parseInt16 = Integer.parseInt(nexexportformat.formats.get(nexExportFormat.TAG_FORMAT_VIDEO_PROFILE));
                int parseInt17 = Integer.parseInt(nexexportformat.formats.get(nexExportFormat.TAG_FORMAT_VIDEO_LEVEL));
                int parseInt18 = Integer.parseInt(nexexportformat.formats.get(nexExportFormat.TAG_FORMAT_VIDEO_ROTATE));
                int parseInt19 = Integer.parseInt(nexexportformat.formats.get(nexExportFormat.TAG_FORMAT_VIDEO_FPS));
                try {
                    if (export(str2, parseInt12, parseInt13, parseInt15, Long.parseLong(nexexportformat.formats.get(nexExportFormat.TAG_FORMAT_MAX_FILESIZE)), parseInt18, Integer.parseInt(nexexportformat.formats.get(nexExportFormat.TAG_FORMAT_AUDIO_SAMPLERATE)), parseInt16, parseInt17, parseInt19, parseInt14) == 0) {
                        this.mVideoEditor.a(new NexEditor.i() { // from class: com.nexstreaming.nexeditorsdk.nexEngine.3
                            @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.i
                            public void a(NexEditor.ErrorCode errorCode) {
                                nexexportlistener.onExportFail(nexErrorCode.fromValue(errorCode.getValue()));
                            }

                            @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.i
                            public void a(int i) {
                                nexexportlistener.onExportProgress((i * 100) / nexEngine.this.mProject.getTotalTime());
                            }

                            @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.i
                            public void a() {
                                nexexportlistener.onExportDone(null);
                            }
                        });
                        return nexErrorCode.NONE;
                    }
                    return nexErrorCode.UNKNOWN;
                } catch (Exception unused) {
                    return nexErrorCode.UNKNOWN;
                }
            } catch (Exception unused2) {
                return nexErrorCode.ARGUMENT_FAILED;
            }
        } else {
            return nexErrorCode.UNSUPPORT_FORMAT;
        }
    }

    private int transcode(String str, int i, int i2, int i3, long j, final int i4, final int i5) {
        if (this.mState == 2) {
            Log.w(TAG, "[" + this.mId + "]already export state");
            return -1;
        } else if (!isSetProject(true)) {
            return -1;
        } else {
            setOverlays(OverlayCommand.upload);
            resolveProject(sLoadListAsync, true);
            this.mState = 2;
            this.mExportTotalTime = sTranscodeProject.getTotalTime();
            this.mExportFilePath = str;
            this.mEncodeWidth = i;
            this.mEncodeHeight = i2;
            this.mEncodeBitrate = i3;
            this.mEncodeMaxFileSize = j;
            this.mVideoEditor.a(this.mAppContext).mo1850onComplete(new Task.OnTaskEventListener() { // from class: com.nexstreaming.nexeditorsdk.nexEngine.4
                @Override // com.nexstreaming.app.common.task.Task.OnTaskEventListener
                public void onTaskEvent(Task task, Task.Event event) {
                    nexEngine.this.loadEffectsInEditor(true);
                    nexEngine.this.setEditorListener();
                    int i6 = i5;
                    int i7 = i6 != 90 ? i6 != 180 ? i6 != 270 ? 0 : 64 : 32 : 16;
                    nexEngine nexengine = nexEngine.this;
                    if (nexengine.bNeedScaling) {
                        nexengine.bNeedScaling = false;
                        i7 |= nexEngine.ExportHEVCMainTierLevel6;
                    }
                    nexengine.mVideoEditor.a(nexEngine.sExportVideoTrackUUIDMode, (byte[]) null);
                    nexEngine.this.mVideoEditor.a(nexEngine.this.mExportFilePath, nexEngine.this.mEncodeWidth, nexEngine.this.mEncodeHeight, nexEngine.this.mEncodeBitrate, nexEngine.this.mEncodeMaxFileSize, 0, false, i4, i7 | 4096).mo1851onFailure(new Task.OnFailListener() { // from class: com.nexstreaming.nexeditorsdk.nexEngine.4.1
                        @Override // com.nexstreaming.app.common.task.Task.OnFailListener
                        public void onFail(Task task2, Task.Event event2, Task.TaskError taskError) {
                            Log.e(nexEngine.TAG, "[" + nexEngine.this.mId + "]transcode fail!=" + taskError);
                        }
                    });
                }
            });
            return 0;
        }
    }

    public void setScalingFlag2Export(boolean z) {
        this.bNeedScaling = z;
    }

    public int exportPause() {
        if (this.externalImageExportProcessing) {
            return -1;
        }
        return this.mVideoEditor.encodePause();
    }

    public int exportResume() {
        if (this.externalImageExportProcessing) {
            return -1;
        }
        return this.mVideoEditor.encodeResume();
    }

    public int checkDirectExport(int i) {
        if (!this.mProject.getOverlayItems().isEmpty()) {
            return 8;
        }
        setEditorListener();
        this.lastCheckDirectExportOption = i;
        return this.mVideoEditor.checkDirectExport(i);
    }

    public int checkDirectExport() {
        return checkDirectExport(0);
    }

    public boolean directExport(String str, long j, long j2, int i) {
        if (isSetProject(true)) {
            this.mState = 2;
            if (str == null) {
                return false;
            }
            this.mExportTotalTime = this.mProject.getTotalTime();
            this.mExportFilePath = str;
            this.mEncodeMaxFileSize = j;
            setEditorListener();
            this.mVideoEditor.a(sExportVideoTrackUUIDMode, (byte[]) null);
            this.mVideoEditor.directExport(this.mExportFilePath, this.mEncodeMaxFileSize, j2, EditorGlobal.b(MapBundleKey.OfflineMapKey.OFFLINE_UPDATE), i);
            return true;
        }
        return false;
    }

    public boolean directExport(String str, long j, long j2) {
        return directExport(str, j, j2, this.lastCheckDirectExportOption);
    }

    public boolean checkPFrameDirectExportSync(String str) {
        return this.mVideoEditor.checkPFrameDirectExportSync(str) == 0;
    }

    @Deprecated
    private boolean fastPreviewStart(int i, int i2, int i3, int i4) {
        this.mVideoEditor.fastPreviewStart(i, i2, i3, i4);
        return true;
    }

    @Deprecated
    private boolean fastPreviewTime(int i) {
        this.mVideoEditor.fastPreviewTime(i);
        return true;
    }

    @Deprecated
    private boolean fastPreviewStop() {
        this.mVideoEditor.fastPreviewStop();
        return true;
    }

    public boolean reverseStart(String str, String str2, String str3, int i, int i2, int i3, long j, int i4, int i5, int i6) {
        int i7 = i5 - i4;
        if (i7 >= 500) {
            return this.mVideoEditor.reverseStart(str, str2, str3, i, i2, i3, j, i4, i5, i6) == 0;
        }
        throw new InvalidRangeException(500, i7, true);
    }

    public boolean reverseStop() {
        return this.mVideoEditor.reverseStop() == 0;
    }

    public boolean transcodingStart(String str, String str2, int i, int i2, int i3, int i4, int i5, long j, int i6, int i7) {
        return !this.mVideoEditor.a(str, str2, i, i2, i3, i4, i5, j, i6, i7, EditorGlobal.b(MapBundleKey.OfflineMapKey.OFFLINE_UPDATE)).isError();
    }

    public boolean transcodingStop() {
        return !this.mVideoEditor.p().isError();
    }

    public boolean setTotalAudioVolumeWhilePlay(int i, int i2) {
        if (i == 100) {
            i = 101;
        }
        if (i2 == 100) {
            i2 = 101;
        }
        return i >= 0 && i <= 200 && i2 >= 0 && i2 <= 200 && this.mVideoEditor.setVolumeWhilePlay(i, i2) == 0;
    }

    public boolean setTotalAudioVolumeResetWhilePlay() {
        return this.mVideoEditor.setVolumeWhilePlay(100, 100) == 0;
    }

    public void setTotalAudioVolumeProject(int i, int i2) {
        for (nexClip nexclip : this.mProject.getPrimaryItems()) {
            nexclip.setClipVolume(i);
        }
        for (nexAudioItem nexaudioitem : this.mProject.getAudioItems()) {
            nexaudioitem.getClip().setClipVolume(i2);
        }
        this.mProject.setBGMMasterVolumeScale(i2 / 200.0f);
    }

    private static int bsearch(int[] iArr, int i) {
        int length = iArr.length - 1;
        int i2 = 0;
        while (i2 <= length) {
            int i3 = (i2 + length) / 2;
            if (iArr[i3] == i) {
                return i3;
            }
            if (iArr[i3] < i) {
                i2 = i3 + 1;
            } else {
                length = i3 - 1;
            }
        }
        if (i2 > 0) {
            return i2 - 1;
        }
        return 0;
    }

    public boolean forceMixExport(String str) {
        if (isSetProject(true)) {
            if (this.mProject.getClip(0, true).getClipType() != 4) {
                Log.d(TAG, "[" + this.mId + "]forceMixExport: no video clip.");
                return false;
            }
            nexProject nexproject = new nexProject();
            if (this.mProject.getClip(0, true).hasAudio() && !this.mProject.getClip(0, true).getAudioCodecType().contains("aac")) {
                Log.d(TAG, "[" + this.mId + "]forceMixExport: audio is not aac");
                return false;
            }
            String realPath = this.mProject.getClip(0, true).getRealPath();
            if (this.mVideoEditor.checkIDRStart(realPath) != 0) {
                Log.d(TAG, "[" + this.mId + "]forceMixExport: idr finder start fail!");
                return false;
            }
            int i = 0;
            for (int i2 = 0; i2 < this.mProject.getTotalClipCount(true); i2++) {
                if (realPath.compareTo(this.mProject.getClip(i2, true).getRealPath()) == 0) {
                    nexproject.add(nexClip.dup(this.mProject.getClip(i2, true)));
                    int startTrimTime = this.mProject.getClip(i2, true).getVideoClipEdit().getStartTrimTime();
                    int endTrimTime = this.mProject.getClip(i2, true).getVideoClipEdit().getEndTrimTime();
                    if (this.mProject.getClip(i2, true).getVideoClipEdit().getSpeedControl() != 100) {
                        Log.d(TAG, "[" + this.mId + "]forceMixExport: set speed clip index=" + i2);
                        i = 1;
                    }
                    int checkIDRTime = this.mVideoEditor.checkIDRTime(startTrimTime);
                    if (checkIDRTime == -1) {
                        Log.d(TAG, "[" + this.mId + "]forceMixExport: idr finder fail startTrimTime=" + startTrimTime);
                        this.mVideoEditor.checkIDREnd();
                        return false;
                    }
                    Log.d(TAG, "[" + this.mId + "]forceMixExport: startTrimTime=" + startTrimTime + ", endTrimTime=" + endTrimTime + ", new idrTime=" + checkIDRTime);
                    nexproject.getClip(i2, true).getVideoClipEdit().setTrim(checkIDRTime, endTrimTime);
                } else {
                    Log.d(TAG, "[" + this.mId + "]forceMixExport: [" + i2 + "] clip invaild path=" + realPath);
                    this.mVideoEditor.checkIDREnd();
                    return false;
                }
            }
            this.mVideoEditor.checkIDREnd();
            nexProject nexproject2 = this.mProject;
            this.mProject = nexproject;
            try {
                resolveProject(sLoadListAsync, true);
                this.mForceMixExportMode = true;
                this.mExportTotalTime = nexproject.getTotalTime();
                this.mState = 2;
                this.mExportFilePath = str;
                this.mEncodeMaxFileSize = Long.MAX_VALUE;
                setEditorListener();
                this.mVideoEditor.a(sExportVideoTrackUUIDMode, (byte[]) null);
                this.mVideoEditor.directExport(this.mExportFilePath, this.mEncodeMaxFileSize, this.mProject.getTotalTime(), EditorGlobal.b(MapBundleKey.OfflineMapKey.OFFLINE_UPDATE), i);
                this.mProject = nexproject2;
                return true;
            } catch (Exception unused) {
                this.mProject = nexproject2;
            }
        }
        return false;
    }

    public int getDuration() {
        if (isSetProject(false)) {
            return this.mVideoEditor.getDuration() * 1000;
        }
        return 0;
    }

    public int getCurrentPlayTimeTime() {
        return this.mCurrentPlayTime;
    }

    public void updateProject() {
        if (this.mProject != null) {
            setOverlays(OverlayCommand.upload);
            int resolveProject = resolveProject(sLoadListAsync, false);
            if (this.mState == 2 || resolveProject != 1 || this.mProject.getTotalClipCount(true) <= 0) {
                return;
            }
            loadEffectsInEditor(false);
        }
    }

    public boolean updateProject(boolean z) {
        if (z) {
            try {
                updateProject();
                return true;
            } catch (Exception unused) {
                return false;
            }
        }
        updateProject();
        return true;
    }

    private void clearList() {
        Log.i(TAG, "[" + this.mId + "]clearList()");
        if (sLoadListAsync) {
            this.mVideoEditor.b((NexVisualClip[]) null, (NexAudioClip[]) null, 0);
            this.mVideoEditor.asyncDrawInfoList(null, null);
            return;
        }
        this.mVideoEditor.a((NexVisualClip[]) null, (NexAudioClip[]) null, 0);
        this.mVideoEditor.asyncDrawInfoList(null, null);
    }

    private void encodeEffectOptions(StringBuilder sb, Map<String, String> map) {
        try {
            boolean z = true;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (z) {
                    z = false;
                } else {
                    sb.append('&');
                }
                sb.append(URLEncoder.encode(entry.getKey(), Keyczar.DEFAULT_ENCODING));
                sb.append("=");
                sb.append(URLEncoder.encode(entry.getValue(), Keyczar.DEFAULT_ENCODING));
            }
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    public String getEncodedEffectOptions(nexClip nexclip, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(nexclip.getClipEffect(true).getShowStartTime());
        sb.append(CoreConstants.COMMA_CHAR);
        sb.append(nexclip.getClipEffect(true).getShowEndTime());
        sb.append('?');
        encodeEffectOptions(sb, nexclip.getTransitionEffect(true).getEffectOptions(str));
        sb.append('?');
        encodeEffectOptions(sb, nexclip.getClipEffect(true).getEffectOptions(str));
        return sb.toString();
    }

    public void clearProject() {
        clearList();
        this.mProject = null;
    }

    public int clearScreen() {
        NexEditor nexEditor = this.mVideoEditor;
        if (nexEditor != null) {
            nexEditor.r();
            return 0;
        }
        return 1;
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x003e, code lost:
        if (r8.size() > 0) goto L12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x007e, code lost:
        if (r9.size() > 0) goto L25;
     */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0084  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean checkUpdateProject(java.util.List<com.nexstreaming.kminternal.nexvideoeditor.NexVisualClip> r8, java.util.List<com.nexstreaming.kminternal.nexvideoeditor.NexAudioClip> r9) {
        /*
            r7 = this;
            java.util.List<com.nexstreaming.kminternal.nexvideoeditor.NexVisualClip> r0 = r7.mCachedNexVisualClips
            r1 = 0
            java.lang.String r2 = "nexEngine"
            r3 = 1
            if (r0 == 0) goto L3a
            int r0 = r0.size()
            int r4 = r8.size()
            if (r0 != r4) goto L34
            int r0 = r8.size()
            r4 = r1
        L17:
            if (r4 >= r0) goto L42
            java.util.List<com.nexstreaming.kminternal.nexvideoeditor.NexVisualClip> r5 = r7.mCachedNexVisualClips
            java.lang.Object r5 = r5.get(r4)
            com.nexstreaming.kminternal.nexvideoeditor.NexVisualClip r5 = (com.nexstreaming.kminternal.nexvideoeditor.NexVisualClip) r5
            java.lang.Object r6 = r8.get(r4)
            boolean r5 = r5.equals(r6)
            if (r5 != 0) goto L31
            java.lang.String r0 = "checkUpdateProject video not equals"
            android.util.Log.d(r2, r0)
            goto L40
        L31:
            int r4 = r4 + 1
            goto L17
        L34:
            java.lang.String r0 = "checkUpdateProject video diff size"
            android.util.Log.d(r2, r0)
            goto L40
        L3a:
            int r0 = r8.size()
            if (r0 <= 0) goto L42
        L40:
            r0 = r3
            goto L43
        L42:
            r0 = r1
        L43:
            if (r0 != 0) goto L81
            java.util.List<com.nexstreaming.kminternal.nexvideoeditor.NexAudioClip> r4 = r7.mCachedNexAudioClips
            if (r4 == 0) goto L7a
            int r4 = r4.size()
            int r5 = r9.size()
            if (r4 != r5) goto L74
            int r4 = r9.size()
        L57:
            if (r1 >= r4) goto L81
            java.util.List<com.nexstreaming.kminternal.nexvideoeditor.NexAudioClip> r5 = r7.mCachedNexAudioClips
            java.lang.Object r5 = r5.get(r1)
            com.nexstreaming.kminternal.nexvideoeditor.NexAudioClip r5 = (com.nexstreaming.kminternal.nexvideoeditor.NexAudioClip) r5
            java.lang.Object r6 = r9.get(r1)
            boolean r5 = r5.equals(r6)
            if (r5 != 0) goto L71
            java.lang.String r0 = "checkUpdateProject audio not equals"
            android.util.Log.d(r2, r0)
            goto L82
        L71:
            int r1 = r1 + 1
            goto L57
        L74:
            java.lang.String r0 = "checkUpdateProject audio diff size"
            android.util.Log.d(r2, r0)
            goto L82
        L7a:
            int r1 = r9.size()
            if (r1 <= 0) goto L81
            goto L82
        L81:
            r3 = r0
        L82:
            if (r3 == 0) goto L88
            r7.mCachedNexVisualClips = r8
            r7.mCachedNexAudioClips = r9
        L88:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.nexeditorsdk.nexEngine.checkUpdateProject(java.util.List, java.util.List):boolean");
    }

    private boolean loadClipToEngine(List<NexVisualClip> list, List<NexAudioClip> list2, boolean z, boolean z2, int i) {
        boolean z3 = true;
        if (z2) {
            this.mCachedNexVisualClips = list;
            this.mCachedNexAudioClips = list2;
            Log.d(TAG, "loadClipToEngine update force");
        } else if (checkUpdateProject(list, list2)) {
            Log.d(TAG, "loadClipToEngine update loadlist call");
        } else {
            Log.d(TAG, "loadClipToEngine No update");
            z3 = false;
        }
        if (z3) {
            NexVisualClip[] nexVisualClipArr = new NexVisualClip[this.mCachedNexVisualClips.size()];
            int size = this.mCachedNexAudioClips.size();
            if (size != 0) {
                NexAudioClip[] nexAudioClipArr = new NexAudioClip[size];
                if (z) {
                    this.mVideoEditor.b((NexVisualClip[]) this.mCachedNexVisualClips.toArray(nexVisualClipArr), (NexAudioClip[]) this.mCachedNexAudioClips.toArray(nexAudioClipArr), i);
                } else {
                    this.mVideoEditor.a((NexVisualClip[]) this.mCachedNexVisualClips.toArray(nexVisualClipArr), (NexAudioClip[]) this.mCachedNexAudioClips.toArray(nexAudioClipArr), i);
                }
            } else if (z) {
                this.mVideoEditor.b((NexVisualClip[]) this.mCachedNexVisualClips.toArray(nexVisualClipArr), (NexAudioClip[]) null, i);
            } else {
                this.mVideoEditor.a((NexVisualClip[]) this.mCachedNexVisualClips.toArray(nexVisualClipArr), (NexAudioClip[]) null, i);
            }
        } else {
            this.mVideoEditor.clearProject();
        }
        return z3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:188:0x049d A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x018d  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x019f  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x025d  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x028d  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x02ed  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x03d6  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x03e1  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x03fb  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0406  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x041d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int resolveProject(boolean r26, boolean r27) {
        /*
            Method dump skipped, instructions count: 2402
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.nexeditorsdk.nexEngine.resolveProject(boolean, boolean):int");
    }

    public void overlayLock(boolean z) {
        if (z) {
            setOverlays(OverlayCommand.lock);
        } else {
            setOverlays(OverlayCommand.unlock);
        }
    }

    /* loaded from: classes3.dex */
    public class OverlayPreviewBuilder {
        private nexOverlayItem mItem;

        private OverlayPreviewBuilder(int i) {
            for (nexOverlayItem nexoverlayitem : nexEngine.this.mProject.getOverlayItems()) {
                if (nexoverlayitem.getId() == i) {
                    this.mItem = nexoverlayitem;
                }
            }
        }

        public OverlayPreviewBuilder setOutline(boolean z) {
            nexOverlayItem nexoverlayitem = this.mItem;
            if (nexoverlayitem != null) {
                nexoverlayitem.showOutline(z);
            }
            return this;
        }

        public OverlayPreviewBuilder setPositionX(int i) {
            nexOverlayItem nexoverlayitem = this.mItem;
            if (nexoverlayitem != null) {
                nexoverlayitem.setPosition(i, nexoverlayitem.getPositionY());
            }
            return this;
        }

        public OverlayPreviewBuilder setPositionY(int i) {
            nexOverlayItem nexoverlayitem = this.mItem;
            if (nexoverlayitem != null) {
                nexoverlayitem.setPosition(nexoverlayitem.getPositionX(), i);
            }
            return this;
        }

        public OverlayPreviewBuilder setRotateX(int i) {
            nexOverlayItem nexoverlayitem = this.mItem;
            if (nexoverlayitem != null) {
                nexoverlayitem.setRotate(i, nexoverlayitem.getRotateY(), this.mItem.getRotateZ());
            }
            return this;
        }

        public OverlayPreviewBuilder setRotateY(int i) {
            nexOverlayItem nexoverlayitem = this.mItem;
            if (nexoverlayitem != null) {
                nexoverlayitem.setRotate(nexoverlayitem.getRotateX(), i, this.mItem.getRotateZ());
            }
            return this;
        }

        public OverlayPreviewBuilder setRotateZ(int i) {
            nexOverlayItem nexoverlayitem = this.mItem;
            if (nexoverlayitem != null) {
                nexoverlayitem.setRotate(nexoverlayitem.getRotateX(), this.mItem.getRotateY(), i);
            }
            return this;
        }

        public OverlayPreviewBuilder setScaleX(float f) {
            nexOverlayItem nexoverlayitem = this.mItem;
            if (nexoverlayitem != null) {
                nexoverlayitem.setScale(f, nexoverlayitem.getScaledY());
            }
            return this;
        }

        public OverlayPreviewBuilder setScaleY(float f) {
            nexOverlayItem nexoverlayitem = this.mItem;
            if (nexoverlayitem != null) {
                nexoverlayitem.setScale(nexoverlayitem.getScaledX(), f);
            }
            return this;
        }

        public OverlayPreviewBuilder setAlpha(float f) {
            nexOverlayItem nexoverlayitem = this.mItem;
            if (nexoverlayitem != null) {
                nexoverlayitem.setAlpha(f);
            }
            return this;
        }

        public void display() {
            if (this.mItem != null) {
                nexEngine.this.fastPreview(FastPreviewOption.normal, 0);
            }
        }

        public void clear() {
            if (this.mItem != null) {
                this.mItem = null;
            }
        }
    }

    public boolean getOverlayHitPoint(nexOverlayItem.HitPoint hitPoint) {
        for (nexOverlayItem nexoverlayitem : this.mProject.getOverlayItems()) {
            if (nexoverlayitem.getStartTime() <= hitPoint.mTime && nexoverlayitem.getEndTime() > hitPoint.mTime) {
                return nexoverlayitem.isPointInOverlayItem(hitPoint);
            }
        }
        return false;
    }

    public OverlayPreviewBuilder buildOverlayPreview(int i) {
        return new OverlayPreviewBuilder(i);
    }

    private void setOverlays(OverlayCommand overlayCommand) {
        synchronized (this.m_layerRenderLock) {
            int i = AnonymousClass13.a[overlayCommand.ordinal()];
            if (i == 1) {
                this.mActiveRenderLayers.clear();
                com.nexstreaming.kminternal.nexvideoeditor.b.a().b();
            } else if (i != 2) {
                if (i == 3) {
                    this.m_layerLock = true;
                } else if (i == 4) {
                    this.m_layerLock = false;
                }
            } else if (this.mProject == null) {
            } else {
                this.mActiveRenderLayers.clear();
            }
        }
    }

    private int getOverlayVideoCount() {
        nexProject nexproject = this.mProject;
        int i = 0;
        if (nexproject == null) {
            return 0;
        }
        for (nexOverlayItem nexoverlayitem : nexproject.getOverlayItems()) {
            if (nexoverlayitem.isVideo()) {
                i++;
            }
        }
        return i;
    }

    public void fastPreview(FastPreviewOption fastPreviewOption, int i) {
        NexEditor.FastPreviewOption fastPreviewOption2 = NexEditor.FastPreviewOption.normal;
        switch (AnonymousClass13.b[fastPreviewOption.ordinal()]) {
            case 2:
                fastPreviewOption2 = NexEditor.FastPreviewOption.brightness;
                break;
            case 3:
                fastPreviewOption2 = NexEditor.FastPreviewOption.contrast;
                break;
            case 4:
                fastPreviewOption2 = NexEditor.FastPreviewOption.saturation;
                break;
            case 5:
                fastPreviewOption2 = NexEditor.FastPreviewOption.adj_brightness;
                break;
            case 6:
                fastPreviewOption2 = NexEditor.FastPreviewOption.adj_contrast;
                break;
            case 7:
                fastPreviewOption2 = NexEditor.FastPreviewOption.adj_saturation;
                break;
            case 8:
                fastPreviewOption2 = NexEditor.FastPreviewOption.tintColor;
                break;
            case 9:
                fastPreviewOption2 = NexEditor.FastPreviewOption.cts;
                break;
            case 10:
                fastPreviewOption2 = NexEditor.FastPreviewOption.adj_vignette;
                break;
            case 11:
                fastPreviewOption2 = NexEditor.FastPreviewOption.adj_vignetteRange;
                break;
            case 12:
                fastPreviewOption2 = NexEditor.FastPreviewOption.adj_sharpness;
                break;
            case 13:
                fastPreviewOption2 = NexEditor.FastPreviewOption.customlut_clip;
                break;
            case 14:
                fastPreviewOption2 = NexEditor.FastPreviewOption.customlut_power;
                break;
        }
        this.mVideoEditor.a(fastPreviewOption2, i);
    }

    /* renamed from: com.nexstreaming.nexeditorsdk.nexEngine$13  reason: invalid class name */
    /* loaded from: classes3.dex */
    public static /* synthetic */ class AnonymousClass13 {
        public static final /* synthetic */ int[] a;
        public static final /* synthetic */ int[] b;

        static {
            int[] iArr = new int[FastPreviewOption.values().length];
            b = iArr;
            try {
                iArr[FastPreviewOption.normal.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                b[FastPreviewOption.brightness.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                b[FastPreviewOption.contrast.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                b[FastPreviewOption.saturation.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                b[FastPreviewOption.adj_brightness.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                b[FastPreviewOption.adj_contrast.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                b[FastPreviewOption.adj_saturation.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                b[FastPreviewOption.tintColor.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                b[FastPreviewOption.cts.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                b[FastPreviewOption.adj_vignette.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                b[FastPreviewOption.adj_vignetteRange.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                b[FastPreviewOption.adj_sharpness.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                b[FastPreviewOption.customlut_clip.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                b[FastPreviewOption.customlut_power.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            int[] iArr2 = new int[OverlayCommand.values().length];
            a = iArr2;
            try {
                iArr2[OverlayCommand.clear.ordinal()] = 1;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                a[OverlayCommand.upload.ordinal()] = 2;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                a[OverlayCommand.lock.ordinal()] = 3;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                a[OverlayCommand.unlock.ordinal()] = 4;
            } catch (NoSuchFieldError unused18) {
            }
        }
    }

    public void fastPreviewCustomlut(int i) {
        nexProject nexproject = this.mProject;
        if (nexproject == null) {
            return;
        }
        fastPreviewCustomlut(nexproject.getClipPosition(this.mCurrentPlayTime) + 1, i);
    }

    public void fastPreviewCustomlut(int i, int i2) {
        this.mVideoEditor.q().a(NexEditor.FastPreviewOption.customlut_clip, i).a(NexEditor.FastPreviewOption.customlut_power, i2).a();
    }

    public void fastPreviewCrop(Rect rect) {
        this.mVideoEditor.q().a(NexEditor.FastPreviewOption.nofx, 1).a(rect).a();
    }

    public void fastPreviewEffect(int i, boolean z) {
        this.mVideoEditor.q().a(i).a(z).a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadEffectsInEditor(boolean z) {
        loadEffectsInEditor_usingAssetPackageManager(z);
    }

    private void loadEffectsInEditor_usingAssetPackageManager(boolean z) {
        HashSet hashSet = new HashSet();
        Log.d(TAG, "TranscoderMode =" + sTranscodeMode);
        r2 = false;
        r2 = false;
        r2 = false;
        boolean z2 = false;
        if (sTranscodeMode) {
            nexProject nexproject = this.mProject;
        } else if (this.mProject.getTemplateApplyMode() == 3) {
            List<nexDrawInfo> topDrawInfo = this.mProject.getTopDrawInfo();
            if (topDrawInfo != null && topDrawInfo.size() > 0) {
                for (nexDrawInfo nexdrawinfo : topDrawInfo) {
                    hashSet.add(nexdrawinfo.getEffectID());
                }
            }
        } else {
            boolean templageOverlappedTransitionMode = this.mProject.getTemplageOverlappedTransitionMode();
            List<nexClip> primaryItems = this.mProject.getPrimaryItems();
            for (int i = 0; i < this.mProject.getTotalClipCount(true); i++) {
                String id = primaryItems.get(i).getClipEffect(true).getId();
                if (id != null && id.compareToIgnoreCase("none") != 0 && !hashSet.contains(id)) {
                    hashSet.add(id);
                }
                String id2 = primaryItems.get(i).getTransitionEffect(true).getId();
                if (id2 != null && id2.compareToIgnoreCase("none") != 0 && !hashSet.contains(id2)) {
                    hashSet.add(id2);
                }
            }
            z2 = templageOverlappedTransitionMode;
        }
        com.nexstreaming.app.common.nexasset.assetpackage.c.a().a(hashSet, this.mVideoEditor, z, z2);
        com.nexstreaming.app.common.nexasset.assetpackage.c.a().a(hashSet, this.mVideoEditor, z);
    }

    public void clearTrackCache() {
        NexEditor nexEditor = this.mVideoEditor;
        if (nexEditor != null) {
            nexEditor.clearTrackCache();
            setOverlays(OverlayCommand.clear);
        }
    }

    public void clearOverlayCache() {
        setOverlays(OverlayCommand.clear);
    }

    public void setProperty(String str, String str2) {
        NexEditor nexEditor = this.mVideoEditor;
        if (nexEditor != null) {
            nexEditor.setProperty(str, str2);
        }
    }

    public void updateDrawInfo(nexDrawInfo nexdrawinfo) {
        if (this.mVideoEditor != null) {
            NexDrawInfo nexDrawInfo = new NexDrawInfo();
            nexDrawInfo.mID = nexdrawinfo.getID();
            nexDrawInfo.mTrackID = nexdrawinfo.getClipID();
            nexDrawInfo.mSubEffectID = nexdrawinfo.getSubEffectID();
            nexDrawInfo.mEffectID = nexdrawinfo.getEffectID();
            nexDrawInfo.mTitle = nexdrawinfo.getTitle();
            nexDrawInfo.mIsTransition = nexdrawinfo.getIsTransition();
            nexDrawInfo.mStartTime = nexdrawinfo.getStartTime();
            nexDrawInfo.mEndTime = nexdrawinfo.getEndTime();
            nexDrawInfo.mRotateState = nexdrawinfo.getRotateState();
            nexDrawInfo.mUserRotateState = nexdrawinfo.getUserRotateState();
            nexDrawInfo.mTranslateX = nexdrawinfo.getUserTranslateX();
            nexDrawInfo.mTranslateY = nexdrawinfo.getUserTranslateY();
            nexDrawInfo.mLUT = nexdrawinfo.getLUT();
            nexDrawInfo.mCustomLUT_A = nexdrawinfo.getCustomLUTA();
            nexDrawInfo.mCustomLUT_B = nexdrawinfo.getCustomLUTB();
            nexDrawInfo.mCustomLUT_Power = nexdrawinfo.getCustomLUTPower();
            nexDrawInfo.mBrightness = nexdrawinfo.getBrightness();
            nexDrawInfo.mContrast = nexdrawinfo.getContrast();
            nexDrawInfo.mSaturation = nexdrawinfo.getSaturation();
            nexDrawInfo.mTintcolor = nexdrawinfo.getTintcolor();
            nexDrawInfo.mStartRect.setRect(nexdrawinfo.getStartRect().left, nexdrawinfo.getStartRect().top, nexdrawinfo.getStartRect().right, nexdrawinfo.getStartRect().bottom);
            nexDrawInfo.mEndRect.setRect(nexdrawinfo.getEndRect().left, nexdrawinfo.getEndRect().top, nexdrawinfo.getEndRect().right, nexdrawinfo.getEndRect().bottom);
            nexDrawInfo.mFaceRect.setRect(nexdrawinfo.getFaceRect().left, nexdrawinfo.getFaceRect().top, nexdrawinfo.getFaceRect().right, nexdrawinfo.getFaceRect().bottom);
            this.mVideoEditor.updateDrawInfo(nexDrawInfo);
        }
    }

    public void removeClip(int i) {
        NexEditor nexEditor = this.mVideoEditor;
        if (nexEditor != null) {
            nexEditor.a(i, (NexEditor.g) null);
        }
    }

    /* loaded from: classes3.dex */
    public enum nexPlayState {
        NONE(0),
        IDLE(1),
        RUN(2),
        RECORD(3);
        
        private int mValue;

        nexPlayState(int i) {
            this.mValue = i;
        }

        public int getValue() {
            return this.mValue;
        }

        public static nexPlayState fromValue(int i) {
            nexPlayState[] values;
            for (nexPlayState nexplaystate : values()) {
                if (nexplaystate.getValue() == i) {
                    return nexplaystate;
                }
            }
            return null;
        }
    }

    /* loaded from: classes3.dex */
    public enum nexErrorCode {
        NONE(0),
        GENERAL(1),
        UNKNOWN(2),
        NO_ACTION(3),
        INVALID_INFO(4),
        INVALID_STATE(5),
        VERSION_MISMATCH(6),
        CREATE_FAILED(7),
        MEMALLOC_FAILED(8),
        ARGUMENT_FAILED(9),
        NOT_ENOUGH_NEMORY(10),
        EVENTHANDLER(11),
        FILE_IO_FAILED(12),
        FILE_INVALID_SYNTAX(13),
        FILEREADER_CREATE_FAIL(14),
        FILEWRITER_CREATE_FAIL(15),
        AUDIORESAMPLER_CREATE_FAIL(16),
        UNSUPPORT_FORMAT(17),
        FILEREADER_FAILED(18),
        PLAYSTART_FAILED(19),
        PLAYSTOP_FAILED(20),
        PROJECT_NOT_CREATE(21),
        PROJECT_NOT_OPEN(22),
        CODEC_INIT(23),
        RENDERER_INIT(24),
        THEMESET_CREATE_FAIL(25),
        ADD_CLIP_FAIL(26),
        ENCODE_VIDEO_FAIL(27),
        INPROGRESS_GETCLIPINFO(28),
        THUMBNAIL_BUSY(29),
        UNSUPPORT_MIN_DURATION(30),
        UNSUPPORT_MAX_RESOLUTION(31),
        UNSUPPORT_MIN_RESOLUTION(32),
        UNSUPPORT_VIDEIO_PROFILE(33),
        UNSUPPORT_VIDEO_LEVEL(34),
        UNSUPPORT_VIDEO_FPS(35),
        TRANSCODING_BUSY(36),
        TRANSCODING_NOT_SUPPORTED_FORMAT(37),
        TRANSCODING_USER_CANCEL(38),
        TRANSCODING_NOT_ENOUGHT_DISK_SPACE(39),
        TRANSCODING_CODEC_FAILED(40),
        EXPORT_WRITER_INVAILED_HANDLE(41),
        EXPORT_WRITER_INIT_FAIL(42),
        EXPORT_WRITER_START_FAIL(43),
        EXPORT_AUDIO_DEC_INIT_FAIL(44),
        EXPORT_VIDEO_DEC_INIT_FAIL(45),
        EXPORT_VIDEO_ENC_FAIL(46),
        EXPORT_VIDEO_RENDER_INIT_FAIL(47),
        EXPORT_NOT_ENOUGHT_DISK_SPACE(48),
        UNSUPPORT_AUDIO_PROFILE(49),
        THUMBNAIL_INIT_FAIL(50),
        UNSUPPORT_AUDIO_CODEC(51),
        UNSUPPORT_VIDEO_CODEC(52),
        HIGHLIGHT_FILEREADER_INIT_ERROR(53),
        HIGHLIGHT_TOO_SHORT_CONTENTS(54),
        HIGHLIGHT_CODEC_INIT_ERROR(55),
        HIGHLIGHT_CODEC_DECODE_ERROR(56),
        HIGHLIGHT_RENDER_INIT_ERROR(57),
        HIGHLIGHT_WRITER_INIT_ERROR(58),
        HIGHLIGHT_WRITER_WRITE_ERROR(59),
        HIGHLIGHT_GET_INDEX_ERROR(60),
        HIGHLIGHT_USER_CANCEL(61),
        GETCLIPINFO_USER_CANCEL(62),
        DIRECTEXPORT_CLIPLIST_ERROR(63),
        DIRECTEXPORT_CHECK_ERROR(64),
        DIRECTEXPORT_FILEREADER_INIT_ERROR(65),
        DIRECTEXPORT_FILEWRITER_INIT_ERROR(66),
        DIRECTEXPORT_DEC_INIT_ERROR(67),
        DIRECTEXPORT_DEC_INIT_SURFACE_ERROR(68),
        DIRECTEXPORT_DEC_DECODE_ERROR(69),
        DIRECTEXPORT_ENC_INIT_ERROR(70),
        DIRECTEXPORT_ENC_ENCODE_ERROR(71),
        DIRECTEXPORT_ENC_INPUT_SURFACE_ERROR(72),
        DIRECTEXPORT_ENC_FUNCTION_ERROR(73),
        DIRECTEXPORT_ENC_DSI_DIFF_ERROR(74),
        DIRECTEXPORT_ENC_FRAME_CONVERT_ERROR(75),
        DIRECTEXPORT_RENDER_INIT_ERROR(76),
        DIRECTEXPORT_WRITER_WRITE_ERROR(77),
        DIRECTEXPORT_WRITER_UNKNOWN_ERROR(78),
        FASTPREVIEW_USER_CANCEL(79),
        FASTPREVIEW_CLIPLIST_ERROR(80),
        FASTPREVIEW_FIND_CLIP_ERROR(81),
        FASTPREVIEW_FIND_READER_ERROR(82),
        FASTPREVIEW_VIDEO_RENDERER_ERROR(83),
        FASTPREVIEW_DEC_INIT_SURFACE_ERROR(84),
        HW_NOT_ENOUGH_MEMORY(85),
        EXPORT_USER_CANCEL(86),
        FASTPREVIEW_DEC_INIT_ERROR(87),
        FASTPREVIEW_FILEREADER_INIT_ERROR(88),
        FASTPREVIEW_TIME_ERROR(89),
        FASTPREVIEW_RENDER_INIT_ERROR(90),
        FASTPREVIEW_OUTPUTSURFACE_INIT_ERROR(91),
        FASTPREVIEW_BUSY(92),
        CODEC_DECODE(93),
        RENDERER_AUDIO(94);
        
        private final int errno;

        nexErrorCode(int i) {
            this.errno = i;
        }

        public int getValue() {
            return this.errno;
        }

        public static nexErrorCode fromValue(int i) {
            nexErrorCode[] values;
            for (nexErrorCode nexerrorcode : values()) {
                if (nexerrorcode.getValue() == i) {
                    return nexerrorcode;
                }
            }
            return null;
        }
    }

    public int[] getIDRSeekTabSync(nexClip nexclip) {
        ArrayList arrayList = new ArrayList();
        MediaInfo mediaInfo = nexclip.getMediaInfo();
        if (mediaInfo == null) {
            Log.d(TAG, "[" + this.mId + "]getIDRSeekTabSync() getinfo fail!");
            return null;
        }
        int[] d = mediaInfo.d();
        int i = -1;
        if (this.mVideoEditor.checkIDRStart(nexclip.getRealPath()) != 0) {
            Log.d(TAG, "[" + this.mId + "]getIDRSeekTabSync() checkIDRStart fail!");
            return null;
        }
        for (int i2 = 0; i2 < d.length; i2++) {
            int checkIDRTime = this.mVideoEditor.checkIDRTime(d[i2]);
            Log.d(TAG, "[" + this.mId + "]getIDRSeekTabSync() : seektab=" + d[i2] + ", idrTime=" + checkIDRTime);
            if (checkIDRTime < 0) {
                Log.d(TAG, "[" + this.mId + "]getIDRSeekTabSync() idrTime fail! seekTime=" + d[i2]);
                this.mVideoEditor.checkIDREnd();
                return null;
            }
            if (i != checkIDRTime) {
                arrayList.add(Integer.valueOf(checkIDRTime));
                i = checkIDRTime;
            }
        }
        this.mVideoEditor.checkIDREnd();
        int[] iArr = new int[arrayList.size()];
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            iArr[i3] = ((Integer) arrayList.get(i3)).intValue();
            Log.d(TAG, "[" + this.mId + "]getIDRSeekTabSync() : new seektab=" + iArr[i3]);
        }
        return iArr;
    }

    public int addUdta(int i, String str) {
        return this.mVideoEditor.addUDTA(i, str);
    }

    public int clearUdta() {
        return this.mVideoEditor.clearUDTA();
    }

    public void updateScreenMode() {
        if (this.mVideoEditor != null) {
            NexEditor.a(nexApplicationConfig.getAspectProfile().getWidth(), nexApplicationConfig.getAspectProfile().getHeight(), nexApplicationConfig.getOverlayCoordinateMode());
            this.mVideoEditor.a(nexApplicationConfig.getScreenMode());
            NexThemeView.setAspectRatio(nexApplicationConfig.getAspectRatio());
            setMark();
        }
    }

    public int autoTrim(String str, boolean z, int i, int i2, int i3, final OnAutoTrimResultListener onAutoTrimResultListener) {
        if (onAutoTrimResultListener != null) {
            this.mVideoEditor.a(new NexEditor.l() { // from class: com.nexstreaming.nexeditorsdk.nexEngine.7
                @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.l
                public int a(int i4, int[] iArr) {
                    onAutoTrimResultListener.onAutoTrimResult(0, iArr);
                    return 0;
                }

                @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.l
                public int a(int i4) {
                    onAutoTrimResultListener.onAutoTrimResult(i4, null);
                    return 0;
                }
            });
        }
        this.mVideoEditor.a(str, z ? 1 : 0, i, i2, i3, 0, 0);
        return 0;
    }

    public int autoTrim(String str, int i, int i2, final OnAutoTrimRatioResultListener onAutoTrimRatioResultListener) {
        if (onAutoTrimRatioResultListener != null) {
            this.mVideoEditor.a(new NexEditor.s() { // from class: com.nexstreaming.nexeditorsdk.nexEngine.8
                @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.s
                public int a(int i3, int[] iArr, int[] iArr2) {
                    onAutoTrimRatioResultListener.onAutoTrimRatioResult(0, iArr, iArr2);
                    return 0;
                }

                @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.s
                public int a(int i3) {
                    onAutoTrimRatioResultListener.onAutoTrimRatioResult(i3, null, null);
                    return 0;
                }
            });
        }
        this.mVideoEditor.a(str, 1, i, 1, 0, i2, 1);
        return 0;
    }

    public int autoTrimStop() {
        return this.mVideoEditor.z();
    }

    public static void setExportVideoTrackUUID(boolean z) {
        Log.i(TAG, "setExportVideoTrackUUID()=" + z);
        if (z) {
            sExportVideoTrackUUIDMode = 1;
        } else {
            sExportVideoTrackUUIDMode = 0;
        }
    }

    public Context getAppContext() {
        return this.mAppContext;
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x005e  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x006b  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0079  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0093  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00a0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void runTranscodeMode(com.nexstreaming.nexeditorsdk.nexTranscode.Option r12, java.lang.String r13, com.nexstreaming.nexeditorsdk.nexEngineListener r14) {
        /*
            r11 = this;
            boolean r2 = com.nexstreaming.nexeditorsdk.nexEngine.sTranscodeMode
            r3 = 1
            if (r2 == 0) goto Lf
            com.nexstreaming.nexeditorsdk.nexEngine$nexErrorCode r0 = com.nexstreaming.nexeditorsdk.nexEngine.nexErrorCode.TRANSCODING_BUSY
            int r0 = r0.getValue()
            r14.onEncodingDone(r3, r0)
            return
        Lf:
            int r2 = r11.mState
            r4 = 2
            if (r2 != r4) goto L1e
            com.nexstreaming.nexeditorsdk.nexEngine$nexErrorCode r0 = com.nexstreaming.nexeditorsdk.nexEngine.nexErrorCode.TRANSCODING_BUSY
            int r0 = r0.getValue()
            r14.onEncodingDone(r3, r0)
            return
        L1e:
            com.nexstreaming.nexeditorsdk.nexClip r2 = com.nexstreaming.nexeditorsdk.nexClip.getSupportedClip(r13)
            if (r2 != 0) goto L2e
            com.nexstreaming.nexeditorsdk.nexEngine$nexErrorCode r0 = com.nexstreaming.nexeditorsdk.nexEngine.nexErrorCode.TRANSCODING_NOT_SUPPORTED_FORMAT
            int r0 = r0.getValue()
            r14.onEncodingDone(r3, r0)
            return
        L2e:
            com.nexstreaming.nexeditorsdk.nexEngine.sTranscodeMode = r3
            com.nexstreaming.nexeditorsdk.nexProject r4 = new com.nexstreaming.nexeditorsdk.nexProject
            r4.<init>()
            com.nexstreaming.nexeditorsdk.nexEngine.sTranscodeProject = r4
            r4.add(r2)
            com.nexstreaming.nexeditorsdk.nexEngine.sTranscodeListener = r14
            com.nexstreaming.nexeditorsdk.nexTranscode$Rotate r1 = r12.outputRotate
            com.nexstreaming.nexeditorsdk.nexTranscode$Rotate r4 = com.nexstreaming.nexeditorsdk.nexTranscode.Rotate.CW_0
            r5 = 90
            r6 = 180(0xb4, float:2.52E-43)
            r7 = 270(0x10e, float:3.78E-43)
            r8 = 0
            if (r1 != r4) goto L4b
        L49:
            r1 = r8
            goto L5c
        L4b:
            com.nexstreaming.nexeditorsdk.nexTranscode$Rotate r9 = com.nexstreaming.nexeditorsdk.nexTranscode.Rotate.CW_90
            if (r1 != r9) goto L51
            r1 = r7
            goto L5c
        L51:
            com.nexstreaming.nexeditorsdk.nexTranscode$Rotate r9 = com.nexstreaming.nexeditorsdk.nexTranscode.Rotate.CW_180
            if (r1 != r9) goto L57
            r1 = r6
            goto L5c
        L57:
            com.nexstreaming.nexeditorsdk.nexTranscode$Rotate r9 = com.nexstreaming.nexeditorsdk.nexTranscode.Rotate.CW_270
            if (r1 != r9) goto L49
            r1 = r5
        L5c:
            if (r1 == 0) goto L67
            com.nexstreaming.nexeditorsdk.nexProject r9 = com.nexstreaming.nexeditorsdk.nexEngine.sTranscodeProject
            com.nexstreaming.nexeditorsdk.nexClip r9 = r9.getClip(r8, r3)
            r9.setRotateDegree(r1)
        L67:
            int r1 = r12.outputFitMode
            if (r1 != 0) goto L79
            com.nexstreaming.nexeditorsdk.nexProject r1 = com.nexstreaming.nexeditorsdk.nexEngine.sTranscodeProject
            com.nexstreaming.nexeditorsdk.nexClip r1 = r1.getClip(r8, r3)
            com.nexstreaming.nexeditorsdk.nexCrop r1 = r1.getCrop()
            r1.resetStartEndPosition()
            goto L8a
        L79:
            com.nexstreaming.nexeditorsdk.nexProject r1 = com.nexstreaming.nexeditorsdk.nexEngine.sTranscodeProject
            com.nexstreaming.nexeditorsdk.nexClip r1 = r1.getClip(r8, r3)
            com.nexstreaming.nexeditorsdk.nexCrop r1 = r1.getCrop()
            int r9 = r12.outputWidth
            int r10 = r12.outputHeight
            r1.fitStartEndPosition(r9, r10)
        L8a:
            r11.resolveProject(r3, r3)
            com.nexstreaming.nexeditorsdk.nexTranscode$Rotate r1 = r12.outputRotateMeta
            com.nexstreaming.nexeditorsdk.nexTranscode$Rotate r3 = com.nexstreaming.nexeditorsdk.nexTranscode.Rotate.BYPASS
            if (r1 != r3) goto La0
            int r1 = r2.getRotateInMeta()
            r2 = 360(0x168, float:5.04E-43)
            int r1 = 360 - r1
            if (r1 != r2) goto L9e
            goto Lb4
        L9e:
            r8 = r1
            goto Lb4
        La0:
            if (r1 != r4) goto La3
            goto Lb4
        La3:
            com.nexstreaming.nexeditorsdk.nexTranscode$Rotate r2 = com.nexstreaming.nexeditorsdk.nexTranscode.Rotate.CW_90
            if (r1 != r2) goto La9
            r8 = r5
            goto Lb4
        La9:
            com.nexstreaming.nexeditorsdk.nexTranscode$Rotate r2 = com.nexstreaming.nexeditorsdk.nexTranscode.Rotate.CW_180
            if (r1 != r2) goto Laf
            r8 = r6
            goto Lb4
        Laf:
            com.nexstreaming.nexeditorsdk.nexTranscode$Rotate r2 = com.nexstreaming.nexeditorsdk.nexTranscode.Rotate.CW_270
            if (r1 != r2) goto Lb4
            r8 = r7
        Lb4:
            java.io.File r1 = r12.outputFile
            java.lang.String r1 = r1.getAbsolutePath()
            int r2 = r12.outputWidth
            int r3 = r12.outputHeight
            int r4 = r12.outputBitRate
            r5 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            int r7 = r12.outputSamplingRate
            r0 = r11
            r0.transcode(r1, r2, r3, r4, r5, r7, r8)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.nexeditorsdk.nexEngine.runTranscodeMode(com.nexstreaming.nexeditorsdk.nexTranscode$Option, java.lang.String, com.nexstreaming.nexeditorsdk.nexEngineListener):void");
    }

    public void stopTranscode() {
        if (sTranscodeMode) {
            if (this.mState == 2) {
                stop();
                return;
            }
            sTranscodeMode = false;
            resolveProject(true, true);
        }
    }

    @Deprecated
    public static void prepareSurfaceSetToNull(boolean z) {
        NexEditor.c(z);
    }

    @Deprecated
    public int getAudioSessionID() {
        return this.mVideoEditor.d(true);
    }

    public boolean set360VideoViewPosition(int i, int i2) {
        return this.mVideoEditor.e(i, i2);
    }

    public void set360VideoViewStopPosition(int i, int i2) {
        this.mVideoEditor.q().a(NexEditor.FastPreviewOption.nofx, 1).a(i, i2).a();
    }

    public void set360VideoForceNormalView() {
        this.mVideoEditor.A();
    }

    public int getLayerWidth() {
        return nexApplicationConfig.getAspectProfile().getWidth();
    }

    public int getLayerHeight() {
        return nexApplicationConfig.getAspectProfile().getHeight();
    }

    public void setExportCrop(int i, int i2, int i3) {
        Log.d(TAG, "[" + this.mId + "] setExportCrop mode=" + i + ", (" + i2 + "X" + i3 + ")");
        this.mEnhancedCropMode = i;
        this.mEnhancedCropOutputWidth = i2;
        this.mEnhancedCropOutputHeight = i3;
    }

    public void setTaskSleep(boolean z) {
        this.mVideoEditor.setTaskSleep(z ? 1 : 0);
    }

    public boolean setPreviewScaleFactor(float f) {
        return this.mVideoEditor.a(f);
    }

    public nexErrorCode captureCurrentFrame(final OnCaptureListener onCaptureListener) {
        if (onCaptureListener == null) {
            return nexErrorCode.ARGUMENT_FAILED;
        }
        if (this.mVideoEditor.x()) {
            return nexErrorCode.fromValue(this.mVideoEditor.a(this.lastSeekTime, new NexEditor.e() { // from class: com.nexstreaming.nexeditorsdk.nexEngine.9
                @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.e
                public void a(Bitmap bitmap) {
                    onCaptureListener.onCapture(bitmap);
                }

                @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.e
                public void a(NexEditor.ErrorCode errorCode) {
                    onCaptureListener.onCaptureFail(nexErrorCode.fromValue(errorCode.getValue()));
                }
            }).getValue());
        }
        return nexErrorCode.fromValue(this.mVideoEditor.a(new NexEditor.e() { // from class: com.nexstreaming.nexeditorsdk.nexEngine.10
            @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.e
            public void a(Bitmap bitmap) {
                onCaptureListener.onCapture(bitmap);
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.e
            public void a(NexEditor.ErrorCode errorCode) {
                onCaptureListener.onCaptureFail(nexErrorCode.fromValue(errorCode.getValue()));
            }
        }).getValue());
    }

    /* loaded from: classes3.dex */
    public enum nexUndetectedFaceCrop {
        NONE(0),
        ZOOM(1);
        
        private int mValue;

        nexUndetectedFaceCrop(int i) {
            this.mValue = i;
        }

        public int getValue() {
            return this.mValue;
        }

        public static nexUndetectedFaceCrop fromValue(int i) {
            nexUndetectedFaceCrop[] values;
            for (nexUndetectedFaceCrop nexundetectedfacecrop : values()) {
                if (nexundetectedfacecrop.getValue() == i) {
                    return nexundetectedfacecrop;
                }
            }
            return null;
        }
    }

    private void defaultFaceDetectSetting() {
        this.facedetect_asyncmode = true;
        this.facedetect_syncclip_count = 1;
        this.facedetect_undetected_clip_cropping_mode = 0;
    }

    public void stopAsyncFaceDetect() {
        Iterator<AsyncTask<String, Void, com.nexstreaming.kminternal.kinemaster.utils.facedetect.a>> it = this.async_facedetect_worker_list_.iterator();
        while (it.hasNext()) {
            it.next().cancel(true);
        }
        this.async_facedetect_worker_list_.clear();
    }

    @Deprecated
    public int faceDetect(boolean z, int i, nexUndetectedFaceCrop nexundetectedfacecrop) {
        this.facedetect_asyncmode = true;
        this.facedetect_syncclip_count = 0;
        this.facedetect_undetected_clip_cropping_mode = nexundetectedfacecrop.getValue();
        return (com.nexstreaming.kminternal.kinemaster.utils.facedetect.a.a() == null || this.facedetect_undetected_clip_cropping_mode == 0) ? 0 : 1;
    }

    public void setFaceModule(String str) {
        stopAsyncFaceDetect();
        if (str == null) {
            com.nexstreaming.kminternal.kinemaster.utils.facedetect.a.a((nexFaceDetectionProvider) null);
            return;
        }
        nexFaceDetectionProvider a2 = com.nexstreaming.kminternal.kinemaster.utils.facedetect.a.a();
        if (a2 != null && a2.uuid().compareTo(str) == 0) {
            return;
        }
        if (this.mProject != null) {
            for (int i = 0; i < this.mProject.getTotalClipCount(true); i++) {
                nexClip clip = this.mProject.getClip(i, true);
                if (clip.getClipType() == 1) {
                    clip.resetFaceDetectProcessed();
                }
            }
        }
        Object module = nexApplicationConfig.getExternalModuleManager().getModule(str);
        if (module == null) {
            com.nexstreaming.kminternal.kinemaster.utils.facedetect.a.a((nexFaceDetectionProvider) null);
        } else if (nexFaceDetectionProvider.class.isInstance(module)) {
            com.nexstreaming.kminternal.kinemaster.utils.facedetect.a.a((nexFaceDetectionProvider) module);
        }
    }

    public void setLetterBox(boolean z) {
        this.bLetterBox = z;
    }

    public boolean getLetterBox() {
        return this.bLetterBox;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFaceInfo2Clip(nexClip nexclip, int i, RectF rectF, int i2) {
        boolean z;
        nexClip nexclip2;
        Rect rect;
        String str;
        nexDrawInfo nexdrawinfo;
        Rect rect2 = new Rect();
        Rect rect3 = new Rect();
        Rect rect4 = new Rect();
        int width = nexclip.getCrop().getWidth();
        int height = nexclip.getCrop().getHeight();
        int faceDetectSpeed = nexclip.getCrop().getFaceDetectSpeed();
        Log.d(TAG, "Face Detection speed: " + faceDetectSpeed);
        rect4.set(0, 0, width, height);
        float f = (float) width;
        float f2 = (float) height;
        rect3.set((int) (rectF.left * f), (int) (rectF.top * f2), (int) (rectF.right * f), (int) (rectF.bottom * f2));
        rect2.set((int) (rectF.left * f), (int) (rectF.top * f2), (int) (rectF.right * f), (int) (rectF.bottom * f2));
        char c = 2;
        if (rect3.isEmpty()) {
            rect3.set(0, 0, width, height);
            rect3.offset((int) ((width * Math.random()) / 4.0d), (int) ((height * Math.random()) / 4.0d));
            Log.d(TAG, "Face Detection Empty ");
            z = false;
        } else {
            int width2 = width - rect3.width();
            if (width2 >= 2) {
                int i3 = width2 / 8;
                rect3.left -= i3;
                rect3.right += i3;
                Log.d(TAG, "Face Detection width addSpace > 0");
            }
            int height2 = height - rect3.height();
            if (height2 >= 2) {
                int i4 = height2 / 8;
                rect3.top -= i4;
                rect3.bottom += i4;
                Log.d(TAG, "Face Detection height addSpace>0");
            }
            z = true;
        }
        String str2 = "Face Detection false  ";
        String str3 = "Face Detection true";
        if (nexclip.getDrawInfos() == null || nexclip.getDrawInfos().size() <= 0) {
            Rect rect5 = new Rect(rect2);
            Rect rect6 = new Rect(rect3);
            Rect rect7 = new Rect(rect4);
            if (rectF.isEmpty() || nexclip.getCrop().getEndPosionLock()) {
                nexclip.getCrop().getStartPositionRaw(rect3);
                nexclip.getCrop().getEndPositionRaw(rect4);
                this.mVideoEditor.a(i, nexclip.isFaceDetected() ? 1 : 0, rect3, rect4, rect4);
                nexclip.setFaceDetectProcessed(false, rect4);
                return;
            }
            nexclip.getCrop().growToAspect(rect6, nexApplicationConfig.getAspectRatio());
            nexclip.getCrop().applyCropSpeed(rect7, rect6, width, height, faceDetectSpeed, nexclip.getProjectDuration());
            if (z) {
                nexclip.getCrop().shrinkToAspect(rect6, nexApplicationConfig.getAspectRatio());
                nexclip.getCrop().shrinkToAspect(rect7, nexApplicationConfig.getAspectRatio());
                Log.d(TAG, str3);
                int i5 = rect5.top;
                int i6 = rect7.top;
                if (i5 < i6) {
                    int i7 = i6 - i5;
                    rect7.top = i6 - i7;
                    rect7.bottom -= i7;
                }
                if (this.bLetterBox) {
                    int i8 = rect6.right;
                    int i9 = rect6.left;
                    int i10 = (i8 - i9) / 4;
                    rect6.left = i9 - i10;
                    rect6.right = i8 + i10;
                    int i11 = rect6.bottom;
                    int i12 = rect6.top;
                    int i13 = (i11 - i12) / 4;
                    rect6.top = i12 - i13;
                    rect6.bottom = i11 + i13;
                    nexclip.getCrop().growToAspect(rect6, nexApplicationConfig.getAspectRatio());
                }
                nexclip.getCrop().setStartPosition(rect7);
                nexclip.getCrop().setEndPosition(rect6);
                nexclip.getCrop().setFacePosition(rect5);
                nexclip.getCrop().getStartPositionRaw(rect7);
                nexclip.getCrop().getEndPositionRaw(rect6);
                nexclip.getCrop().getFacePositionRaw(rect5);
            } else {
                if (i2 == 2) {
                    nexclip.getCrop().shrinkToAspect(rect6, nexApplicationConfig.getAspectRatio());
                    nexclip.getCrop().growToAspect(rect7, nexApplicationConfig.getAspectRatio());
                    nexclip.getCrop().setStartPosition(rect7);
                    nexclip.getCrop().setEndPosition(rect6);
                    nexclip.getCrop().setFacePosition(rect5);
                    nexclip.getCrop().getStartPositionRaw(rect7);
                    nexclip.getCrop().getEndPositionRaw(rect6);
                    nexclip.getCrop().getFacePositionRaw(rect5);
                } else {
                    nexclip.getCrop().shrinkToAspect(rect6, nexApplicationConfig.getAspectRatio());
                    nexclip.getCrop().shrinkToAspect(rect7, nexApplicationConfig.getAspectRatio());
                    nexclip.getCrop().setStartPosition(rect6);
                    nexclip.getCrop().setEndPosition(rect7);
                    nexclip.getCrop().setFacePosition(rect5);
                    nexclip.getCrop().getStartPositionRaw(rect6);
                    nexclip.getCrop().getEndPositionRaw(rect7);
                    nexclip.getCrop().getFacePositionRaw(rect5);
                }
                Log.d(TAG, str2);
            }
            this.mVideoEditor.a(i, z ? 1 : 0, rect7, rect6, rect5);
            nexclip2 = nexclip;
            rect = rect2;
        } else {
            for (nexDrawInfo nexdrawinfo2 : nexclip.getDrawInfos()) {
                if (nexdrawinfo2.getFaceRect().isEmpty()) {
                    Rect rect8 = new Rect(rect2);
                    Rect rect9 = new Rect(rect3);
                    Rect rect10 = rect2;
                    Rect rect11 = new Rect(rect4);
                    if (rectF.isEmpty() || nexclip.getCrop().getEndPosionLock()) {
                        Rect rect12 = rect3;
                        Rect rect13 = rect4;
                        nexclip.getCrop().getStartPositionRaw(rect12);
                        nexclip.getCrop().getEndPositionRaw(rect13);
                        nexdrawinfo2.setStartRect(rect12);
                        nexdrawinfo2.setEndRect(rect13);
                        nexdrawinfo2.setFaceRect(rect13);
                        updateDrawInfo(nexdrawinfo2);
                        nexclip.setFaceDetectProcessed(false, rect13);
                        str3 = str3;
                        rect4 = rect13;
                        str2 = str2;
                        c = c;
                        rect3 = rect12;
                        rect2 = rect10;
                    } else {
                        String str4 = str3;
                        nexclip.getCrop().growToAspect(rect9, nexdrawinfo2.getRatio());
                        Rect rect14 = rect4;
                        Rect rect15 = rect3;
                        String str5 = str2;
                        nexclip.getCrop().applyCropSpeed(rect11, rect9, width, height, faceDetectSpeed, nexclip.getProjectDuration());
                        if (z) {
                            nexclip.getCrop().shrinkToAspect(rect9, nexdrawinfo2.getRatio());
                            nexclip.getCrop().shrinkToAspect(rect11, nexdrawinfo2.getRatio());
                            Log.d(TAG, str4);
                            int i14 = rect8.top;
                            int i15 = rect11.top;
                            if (i14 < i15) {
                                int i16 = i15 - i14;
                                rect11.top = i15 - i16;
                                rect11.bottom -= i16;
                            }
                            if (this.bLetterBox) {
                                int i17 = rect9.right;
                                int i18 = rect9.left;
                                int i19 = (i17 - i18) / 4;
                                rect9.left = i18 - i19;
                                rect9.right = i17 + i19;
                                int i20 = rect9.bottom;
                                int i21 = rect9.top;
                                int i22 = (i20 - i21) / 4;
                                rect9.top = i21 - i22;
                                rect9.bottom = i20 + i22;
                                nexclip.getCrop().growToAspect(rect9, nexdrawinfo2.getRatio());
                            }
                            nexclip.getCrop().setStartPosition(rect11);
                            nexclip.getCrop().setEndPosition(rect9);
                            nexclip.getCrop().setFacePosition(rect8);
                            nexclip.getCrop().getStartPositionRaw(rect11);
                            nexclip.getCrop().getEndPositionRaw(rect9);
                            nexclip.getCrop().getFacePositionRaw(rect8);
                            nexdrawinfo = nexdrawinfo2;
                            str = str5;
                        } else {
                            if (i2 == 2) {
                                nexclip.getCrop().shrinkToAspect(rect9, nexdrawinfo2.getRatio());
                                nexclip.getCrop().growToAspect(rect11, nexdrawinfo2.getRatio());
                                nexclip.getCrop().setStartPosition(rect11);
                                nexclip.getCrop().setEndPosition(rect9);
                                nexclip.getCrop().setFacePosition(rect8);
                                nexclip.getCrop().getStartPositionRaw(rect11);
                                nexclip.getCrop().getEndPositionRaw(rect9);
                                nexclip.getCrop().getFacePositionRaw(rect8);
                            } else {
                                nexclip.getCrop().shrinkToAspect(rect9, nexdrawinfo2.getRatio());
                                nexclip.getCrop().shrinkToAspect(rect11, nexdrawinfo2.getRatio());
                                nexclip.getCrop().setStartPosition(rect9);
                                nexclip.getCrop().setEndPosition(rect11);
                                nexclip.getCrop().setFacePosition(rect8);
                                nexclip.getCrop().getStartPositionRaw(rect9);
                                nexclip.getCrop().getEndPositionRaw(rect11);
                                nexclip.getCrop().getFacePositionRaw(rect8);
                            }
                            rect8.set(0, 0, 1, 1);
                            str = str5;
                            Log.d(TAG, str);
                            nexdrawinfo = nexdrawinfo2;
                        }
                        nexdrawinfo.setStartRect(rect9);
                        nexdrawinfo.setEndRect(rect11);
                        nexdrawinfo.setFaceRect(rect8);
                        updateDrawInfo(nexdrawinfo);
                        str3 = str4;
                        str2 = str;
                        rect4 = rect14;
                        rect2 = rect10;
                        c = 2;
                        rect3 = rect15;
                    }
                }
            }
            nexclip2 = nexclip;
            Rect rect16 = rect2;
            nexclip.getCrop().setFacePosition(rect16);
            nexclip.getCrop().getFacePositionRaw(rect16);
            rect = rect16;
        }
        nexclip2.setFaceDetectProcessed(z, rect);
    }

    public int faceDetect_for_seek(int i, int i2) {
        Log.d(TAG, String.format("Face Detection Mode = %d", Integer.valueOf(i2)));
        if (i2 == 0) {
            return -1;
        }
        int totalClipCount = this.mProject.getTotalClipCount(true);
        for (int i3 = 0; i3 < totalClipCount; i3++) {
            nexClip clip = this.mProject.getClip(i3, true);
            if (clip.getClipType() == 1 && clip.mStartTime <= i && i <= clip.mEndTime && !clip.isAssetResource()) {
                com.nexstreaming.kminternal.kinemaster.utils.facedetect.a a2 = com.nexstreaming.kminternal.kinemaster.utils.facedetect.a.a(clip.getPath());
                if (a2 == null) {
                    a2 = new com.nexstreaming.kminternal.kinemaster.utils.facedetect.a(new File(clip.getPath()), true, getAppContext());
                    com.nexstreaming.kminternal.kinemaster.utils.facedetect.a.a(clip.getPath(), a2);
                }
                RectF rectF = new RectF();
                a2.a(rectF);
                updateFaceInfo2Clip(clip, i3 + 1, rectF, i2);
            }
        }
        return 0;
    }

    public int faceDetect_internal(boolean z, int i, final int i2) {
        int i3;
        Log.d(TAG, String.format("Face Detection Mode = %d", Integer.valueOf(i2)));
        if (i2 == 0) {
            return -1;
        }
        int totalClipCount = this.mProject.getTotalClipCount(true);
        int i4 = 0;
        for (int i5 = 0; i5 < totalClipCount; i5++) {
            if (this.mProject.getClip(i5, true).getClipType() == 1) {
                i4++;
            }
        }
        if (true == z) {
            i4 = i;
        }
        int i6 = 0;
        while (i6 < totalClipCount) {
            int i7 = i6 + 1;
            Log.d(TAG, String.format("Face Detection ResetInfo clip ID = %d", Integer.valueOf(i7)));
            if (this.mProject.getClip(i6, true).getClipType() == 1) {
                this.mVideoEditor.b(i7);
            }
            i6 = i7;
        }
        int i8 = 0;
        final int i9 = 0;
        while (i8 < totalClipCount) {
            RectF rectF = new RectF();
            int i10 = i8 + 1;
            Log.d(TAG, String.format("Face Detection sync clip ID = %d", Integer.valueOf(i10)));
            nexClip clip = this.mProject.getClip(i8, true);
            if (clip.getClipType() != 1 || clip.isFaceDetectProcessed() || clip.isAssetResource()) {
                if (clip.getClipType() == 1) {
                    Rect rect = new Rect();
                    Rect rect2 = new Rect();
                    Rect rect3 = new Rect();
                    Log.d(TAG, String.format("Face Detection skip clip ID = %d", Integer.valueOf(i10)));
                    if (clip.getDrawInfos() != null && clip.getDrawInfos().size() > 0) {
                        for (nexDrawInfo nexdrawinfo : clip.getDrawInfos()) {
                            if (nexdrawinfo.getFaceRect().isEmpty()) {
                                clip.getCrop().getStartPositionRaw(rect);
                                clip.getCrop().getEndPositionRaw(rect3);
                                clip.getCrop().getFacePositionRaw(rect2);
                                nexdrawinfo.setStartRect(rect);
                                nexdrawinfo.setEndRect(rect3);
                                nexdrawinfo.setFaceRect(rect2);
                                updateDrawInfo(nexdrawinfo);
                                Log.d(TAG, String.format("Face Detection info update for draw info(clip ID = %d)", Integer.valueOf(i10)));
                            }
                        }
                        i3 = i10;
                    } else {
                        clip.getCrop().getStartPositionRaw(rect);
                        clip.getCrop().getEndPositionRaw(rect3);
                        clip.getCrop().getFacePositionRaw(rect2);
                        i3 = i10;
                        this.mVideoEditor.a(i10, clip.isFaceDetected() ? 1 : 0, rect, rect3, rect2);
                    }
                    i9++;
                    i8 = i3;
                }
            } else {
                com.nexstreaming.kminternal.kinemaster.utils.facedetect.a a2 = com.nexstreaming.kminternal.kinemaster.utils.facedetect.a.a(clip.getPath());
                Log.d(TAG, "Face Detection sync total num =" + i4 + " continueClip num= " + i9);
                if (a2 == null) {
                    if (i8 > i4 - 1) {
                        break;
                    }
                    a2 = new com.nexstreaming.kminternal.kinemaster.utils.facedetect.a(new File(clip.getPath()), true, getAppContext());
                    com.nexstreaming.kminternal.kinemaster.utils.facedetect.a.a(clip.getPath(), a2);
                }
                a2.a(rectF);
                updateFaceInfo2Clip(clip, i10, rectF, i2);
                i9++;
            }
            i3 = i10;
            i8 = i3;
        }
        while (i9 < totalClipCount) {
            final nexClip clip2 = this.mProject.getClip(i9, true);
            if (clip2.getClipType() != 1 || clip2.isFaceDetectProcessed() || clip2.isAssetResource()) {
                if (clip2.getClipType() == 1) {
                    Rect rect4 = new Rect();
                    Rect rect5 = new Rect();
                    Rect rect6 = new Rect();
                    int i11 = i9 + 1;
                    Log.d(TAG, String.format("Face Detection skip clip ID = %d", Integer.valueOf(i11)));
                    if (clip2.getDrawInfos() != null && clip2.getDrawInfos().size() > 0) {
                        for (nexDrawInfo nexdrawinfo2 : clip2.getDrawInfos()) {
                            if (nexdrawinfo2.getFaceRect().isEmpty()) {
                                clip2.getCrop().getStartPositionRaw(rect4);
                                clip2.getCrop().getEndPositionRaw(rect5);
                                clip2.getCrop().getFacePositionRaw(rect6);
                                nexdrawinfo2.setStartRect(rect4);
                                nexdrawinfo2.setEndRect(rect5);
                                nexdrawinfo2.setFaceRect(rect6);
                                updateDrawInfo(nexdrawinfo2);
                            }
                        }
                    } else {
                        clip2.getCrop().getStartPositionRaw(rect4);
                        clip2.getCrop().getEndPositionRaw(rect5);
                        clip2.getCrop().getFacePositionRaw(rect6);
                        this.mVideoEditor.a(i11, clip2.isFaceDetected() ? 1 : 0, rect4, rect5, rect6);
                    }
                }
            } else {
                Log.d(TAG, String.format("Face Detection async clip ID = %d", Integer.valueOf(i9 + 1)));
                AsyncTask<String, Void, com.nexstreaming.kminternal.kinemaster.utils.facedetect.a> asyncTask = new AsyncTask<String, Void, com.nexstreaming.kminternal.kinemaster.utils.facedetect.a>() { // from class: com.nexstreaming.nexeditorsdk.nexEngine.11
                    public Task.TaskError a = null;

                    @Override // android.os.AsyncTask
                    public void onCancelled() {
                        Log.d(nexEngine.TAG, "Face Detection was cancelled");
                    }

                    @Override // android.os.AsyncTask
                    /* renamed from: a */
                    public com.nexstreaming.kminternal.kinemaster.utils.facedetect.a doInBackground(String... strArr) {
                        Log.d(nexEngine.TAG, "Face Detection async thread start =" + strArr[0]);
                        if (i9 == 0) {
                            nexEngine.this.mVideoEditor.e();
                        }
                        com.nexstreaming.kminternal.kinemaster.utils.facedetect.a a3 = com.nexstreaming.kminternal.kinemaster.utils.facedetect.a.a(strArr[0]);
                        if (a3 == null) {
                            com.nexstreaming.kminternal.kinemaster.utils.facedetect.a aVar = new com.nexstreaming.kminternal.kinemaster.utils.facedetect.a(new File(strArr[0]), 1, nexEngine.this.getAppContext());
                            com.nexstreaming.kminternal.kinemaster.utils.facedetect.a.a(strArr[0], aVar);
                            return aVar;
                        }
                        return a3;
                    }

                    @Override // android.os.AsyncTask
                    /* renamed from: a */
                    public void onPostExecute(com.nexstreaming.kminternal.kinemaster.utils.facedetect.a aVar) {
                        nexEngine.this.async_facedetect_worker_list_.remove(this);
                        Log.d(nexEngine.TAG, "Face Detection worker list size:" + nexEngine.this.async_facedetect_worker_list_.size());
                        RectF rectF2 = new RectF();
                        aVar.a(rectF2);
                        nexEngine.this.updateFaceInfo2Clip(clip2, i9 + 1, rectF2, i2);
                        Log.d(nexEngine.TAG, "Face Detection aync thread end =" + clip2.getPath());
                    }
                };
                this.async_facedetect_worker_list_.add(asyncTask);
                asyncTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, clip2.getPath());
            }
            i9++;
        }
        return 0;
    }

    private void undoForceMixProject() {
        if (this.mForceMixExportMode) {
            if (isSetProject(false)) {
                Log.d(TAG, "[" + this.mId + "]undoForceMixProject");
                resolveProject(true, true);
            }
            this.mForceMixExportMode = false;
        }
    }

    /* loaded from: classes3.dex */
    public static class ProfileAndLevel implements Cloneable {
        private int level;
        private int profile;

        private ProfileAndLevel(int i, int i2) {
            this.profile = i;
            this.level = i2;
        }

        public int getProfile() {
            return this.profile;
        }

        public int getLevel() {
            return this.level;
        }

        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public String toString() {
            return "ProfileAndLevel{level=" + this.level + ", profile=" + this.profile + '}';
        }
    }

    /* loaded from: classes3.dex */
    public static class ExportProfile {
        private int mimeType;
        private ProfileAndLevel[] proFileAndLevel;

        public int getMimeType() {
            return this.mimeType;
        }

        public ProfileAndLevel[] getProFileAndLevel() {
            return (ProfileAndLevel[]) this.proFileAndLevel.clone();
        }

        public boolean isSupported(int i, int i2) {
            ProfileAndLevel[] profileAndLevelArr;
            for (ProfileAndLevel profileAndLevel : this.proFileAndLevel) {
                if (profileAndLevel.getProfile() == i && profileAndLevel.getLevel() <= i2) {
                    return true;
                }
            }
            return false;
        }

        public String toString() {
            return "ExportProfile{mimeType=" + this.mimeType + ", proFileAndLevel=" + Arrays.toString(this.proFileAndLevel) + '}';
        }
    }

    public static ExportProfile[] getExportProfile() {
        MediaCodecInfo[] mediaCodecInfoArr;
        int i;
        MediaCodecInfo mediaCodecInfo;
        char c;
        int i2;
        int i3 = 0;
        if (Build.VERSION.SDK_INT < 21) {
            return new ExportProfile[0];
        }
        if (s_exportProfiles == null) {
            MediaCodecList mediaCodecList = new MediaCodecList(0);
            ArrayList arrayList = new ArrayList();
            MediaCodecInfo[] codecInfos = mediaCodecList.getCodecInfos();
            int length = codecInfos.length;
            int i4 = 0;
            while (i4 < length) {
                MediaCodecInfo mediaCodecInfo2 = codecInfos[i4];
                if (mediaCodecInfo2.isEncoder()) {
                    String[] supportedTypes = mediaCodecInfo2.getSupportedTypes();
                    int i5 = i3;
                    while (i5 < supportedTypes.length) {
                        if (supportedTypes[i5].equalsIgnoreCase("video/avc") || supportedTypes[i5].equalsIgnoreCase("video/hevc") || supportedTypes[i5].equalsIgnoreCase("video/mp4v-es")) {
                            ExportProfile exportProfile = new ExportProfile();
                            boolean equalsIgnoreCase = supportedTypes[i5].equalsIgnoreCase("video/avc");
                            int i6 = ExportCodec_AVC;
                            if (equalsIgnoreCase) {
                                exportProfile.mimeType = ExportCodec_AVC;
                            } else if (supportedTypes[i5].equalsIgnoreCase("video/hevc")) {
                                exportProfile.mimeType = ExportCodec_HEVC;
                            } else if (supportedTypes[i5].equalsIgnoreCase("video/mp4v-es")) {
                                exportProfile.mimeType = ExportCodec_MPEG4V;
                            }
                            Log.d(TAG, "codec name=" + mediaCodecInfo2.getName());
                            try {
                                MediaCodecInfo.CodecCapabilities capabilitiesForType = mediaCodecInfo2.getCapabilitiesForType(supportedTypes[i5]);
                                SparseIntArray sparseIntArray = new SparseIntArray();
                                MediaCodecInfo.CodecProfileLevel[] codecProfileLevelArr = capabilitiesForType.profileLevels;
                                int length2 = codecProfileLevelArr.length;
                                while (i3 < length2) {
                                    MediaCodecInfo.CodecProfileLevel codecProfileLevel = codecProfileLevelArr[i3];
                                    mediaCodecInfoArr = codecInfos;
                                    i = length;
                                    if (exportProfile.mimeType != i6) {
                                        mediaCodecInfo = mediaCodecInfo2;
                                        if (exportProfile.mimeType != 268502016) {
                                            c = 256;
                                            if (exportProfile.mimeType == 268566784 && (i2 = codecProfileLevel.profile) != 0) {
                                                Log.d(TAG, "codec profile=" + i2 + ", level=" + codecProfileLevel.level);
                                                int i7 = sparseIntArray.get(i2);
                                                int i8 = codecProfileLevel.level;
                                                if (i7 < i8) {
                                                    sparseIntArray.put(i2, i8);
                                                    continue;
                                                } else {
                                                    continue;
                                                }
                                            }
                                            i3++;
                                            codecInfos = mediaCodecInfoArr;
                                            length = i;
                                            mediaCodecInfo2 = mediaCodecInfo;
                                            i6 = ExportCodec_AVC;
                                        } else {
                                            int i9 = codecProfileLevel.profile;
                                            if (i9 != 0) {
                                                Log.d(TAG, "codec profile=" + i9 + ", level=" + codecProfileLevel.level);
                                                int i10 = sparseIntArray.get(i9);
                                                int i11 = codecProfileLevel.level;
                                                if (i10 < i11) {
                                                    sparseIntArray.put(i9, i11);
                                                }
                                            }
                                        }
                                    } else {
                                        try {
                                            int i12 = codecProfileLevel.profile;
                                            int i13 = 2;
                                            mediaCodecInfo = mediaCodecInfo2;
                                            if (i12 == 1) {
                                                i13 = 1;
                                            } else if (i12 != 2) {
                                                i13 = (i12 == 8 || i12 == 16 || i12 == 32 || i12 == 64) ? 4 : 0;
                                            }
                                            if (i13 != 0) {
                                                try {
                                                    Log.d(TAG, "codec profile=" + i13 + ", level=" + codecProfileLevel.level);
                                                    int i14 = sparseIntArray.get(i13);
                                                    int i15 = codecProfileLevel.level;
                                                    if (i14 < i15) {
                                                        sparseIntArray.put(i13, i15);
                                                    }
                                                } catch (Exception e) {
                                                    e = e;
                                                    Log.wtf(TAG, e);
                                                    i5++;
                                                    codecInfos = mediaCodecInfoArr;
                                                    length = i;
                                                    mediaCodecInfo2 = mediaCodecInfo;
                                                    i3 = 0;
                                                }
                                            }
                                        } catch (Exception e2) {
                                            e = e2;
                                            mediaCodecInfo = mediaCodecInfo2;
                                            Log.wtf(TAG, e);
                                            i5++;
                                            codecInfos = mediaCodecInfoArr;
                                            length = i;
                                            mediaCodecInfo2 = mediaCodecInfo;
                                            i3 = 0;
                                        }
                                    }
                                    c = 256;
                                    continue;
                                    i3++;
                                    codecInfos = mediaCodecInfoArr;
                                    length = i;
                                    mediaCodecInfo2 = mediaCodecInfo;
                                    i6 = ExportCodec_AVC;
                                }
                                mediaCodecInfoArr = codecInfos;
                                i = length;
                                mediaCodecInfo = mediaCodecInfo2;
                                exportProfile.proFileAndLevel = new ProfileAndLevel[sparseIntArray.size()];
                                for (int i16 = 0; i16 < sparseIntArray.size(); i16++) {
                                    exportProfile.proFileAndLevel[i16] = new ProfileAndLevel(sparseIntArray.keyAt(i16), sparseIntArray.valueAt(i16));
                                }
                                arrayList.add(exportProfile);
                            } catch (Exception e3) {
                                e = e3;
                                mediaCodecInfoArr = codecInfos;
                                i = length;
                            }
                        } else {
                            mediaCodecInfoArr = codecInfos;
                            i = length;
                            mediaCodecInfo = mediaCodecInfo2;
                        }
                        i5++;
                        codecInfos = mediaCodecInfoArr;
                        length = i;
                        mediaCodecInfo2 = mediaCodecInfo;
                        i3 = 0;
                    }
                }
                i4++;
                codecInfos = codecInfos;
                length = length;
                i3 = 0;
            }
            s_exportProfiles = new ExportProfile[arrayList.size()];
            for (int i17 = 0; i17 < arrayList.size(); i17++) {
                s_exportProfiles[i17] = (ExportProfile) arrayList.get(i17);
            }
        }
        return s_exportProfiles;
    }

    public void setHDRtoSDR(boolean z) {
        this.mVideoEditor.setProperty("HDR2SDR", z ? "1" : "0");
    }

    public void setThumbnailRoutine(int i) {
        this.mVideoEditor.o(i);
    }
}
