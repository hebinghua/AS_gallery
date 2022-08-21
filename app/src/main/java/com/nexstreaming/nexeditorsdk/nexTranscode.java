package com.nexstreaming.nexeditorsdk;

import android.util.Log;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.nexstreaming.kminternal.kinemaster.config.EditorGlobal;
import com.nexstreaming.kminternal.kinemaster.config.NexExportProfile;
import com.nexstreaming.kminternal.kinemaster.mediainfo.MediaInfo;
import com.nexstreaming.kminternal.nexvideoeditor.NexEditor;
import com.nexstreaming.nexeditorsdk.nexEngine;
import java.io.File;

/* loaded from: classes3.dex */
public class nexTranscode {
    private static final String TAG = "nexTranscode";
    private static nexEngine sEngine;
    private static nexEngineListener sEngineListener;
    private static final int sRunMode = 0;
    private Error lastError;
    private OnTransCoderListener listener;
    private NexEditor mVideoEditor;
    private int progressPercent;
    private File source;
    private State state;

    /* loaded from: classes3.dex */
    public enum Error {
        NONE,
        NOTSUPPORTEDFILE,
        ENGINEFAIL,
        SOURCEFAIL,
        BUSY,
        RUNFAIL,
        CANCEL
    }

    /* loaded from: classes3.dex */
    public static abstract class OnTransCoderListener {
        public abstract void onProgress(int i, int i2);

        public abstract void onTransCodeDone(Error error, int i);
    }

    /* loaded from: classes3.dex */
    public enum Rotate {
        BYPASS,
        CW_0,
        CW_90,
        CW_180,
        CW_270
    }

    /* loaded from: classes3.dex */
    public enum State {
        NONE,
        IDLE,
        WAIT,
        RUNNING,
        COMPLETE
    }

    /* loaded from: classes3.dex */
    public static class Option {
        private int labelResource;
        public int outputBitRate;
        public File outputFile;
        public int outputFitMode;
        public int outputHeight;
        public Rotate outputRotate;
        public Rotate outputRotateMeta;
        public int outputSamplingRate;
        public int outputWidth;

        public Option(File file, int i, int i2, int i3, int i4, boolean z) {
            this.outputFile = file;
            this.outputWidth = i;
            this.outputHeight = i2;
            this.outputBitRate = i3;
            this.outputSamplingRate = i4;
            this.labelResource = NexExportProfile.getLabelResource(i, i2);
            if (z) {
                this.outputFitMode = 1;
            } else {
                this.outputFitMode = 0;
            }
            this.outputRotateMeta = Rotate.BYPASS;
        }

        public Option(File file, int i, int i2, int i3, int i4, boolean z, Rotate rotate, Rotate rotate2) {
            this.outputFile = file;
            this.outputWidth = i;
            this.outputHeight = i2;
            this.outputBitRate = i3;
            this.outputSamplingRate = i4;
            this.labelResource = NexExportProfile.getLabelResource(i, i2);
            if (z) {
                this.outputFitMode = 1;
            } else {
                this.outputFitMode = 0;
            }
            this.outputRotateMeta = rotate;
            this.outputRotate = rotate2;
        }

        public void setOutputRotateMeta(Rotate rotate) {
            this.outputRotateMeta = rotate;
        }

        public void setOutputRotate(Rotate rotate) {
            if (rotate == Rotate.BYPASS) {
                this.outputRotate = Rotate.CW_0;
            } else {
                this.outputRotate = rotate;
            }
        }
    }

    public static void init(nexEngine nexengine) {
        sEngine = nexengine;
    }

    public nexTranscode setTransCoderListener(OnTransCoderListener onTransCoderListener) {
        this.listener = onTransCoderListener;
        return this;
    }

    public nexTranscode(String str, boolean z) {
        this.lastError = Error.NONE;
        this.state = State.NONE;
        if (z) {
            MediaInfo a = MediaInfo.a(str);
            if (!a.m()) {
                this.lastError = Error.NOTSUPPORTEDFILE;
            }
            if (a.i()) {
                this.lastError = Error.NOTSUPPORTEDFILE;
            }
        }
        this.source = new File(str);
        this.state = State.IDLE;
    }

    public static nexTranscode getTranscode(String str, OnTransCoderListener onTransCoderListener) {
        return new nexTranscode(str, false).setTransCoderListener(onTransCoderListener);
    }

    public nexTranscode run(Option option) {
        if (sEngine == null) {
            Log.e(TAG, "must call setEngin()!");
            Error error = Error.ENGINEFAIL;
            this.lastError = error;
            OnTransCoderListener onTransCoderListener = this.listener;
            if (onTransCoderListener != null) {
                onTransCoderListener.onTransCodeDone(error, 1);
            }
            return this;
        } else if (this.source == null) {
            Log.e(TAG, "source is null!");
            Error error2 = Error.SOURCEFAIL;
            this.lastError = error2;
            OnTransCoderListener onTransCoderListener2 = this.listener;
            if (onTransCoderListener2 != null) {
                onTransCoderListener2.onTransCodeDone(error2, 1);
            }
            return this;
        } else if (option == null) {
            Log.e(TAG, "option is null!");
            Error error3 = Error.RUNFAIL;
            this.lastError = error3;
            OnTransCoderListener onTransCoderListener3 = this.listener;
            if (onTransCoderListener3 != null) {
                onTransCoderListener3.onTransCodeDone(error3, 2);
            }
            return this;
        } else {
            sEngineListener = new nexEngineListener() { // from class: com.nexstreaming.nexeditorsdk.nexTranscode.1
                @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
                public void onCheckDirectExport(int i) {
                }

                @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
                public void onClipInfoDone() {
                }

                @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
                public void onFastPreviewStartDone(int i, int i2, int i3) {
                }

                @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
                public void onFastPreviewStopDone(int i) {
                }

                @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
                public void onFastPreviewTimeDone(int i) {
                }

                @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
                public void onPlayEnd() {
                }

                @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
                public void onPlayFail(int i, int i2) {
                }

                @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
                public void onPlayStart() {
                }

                @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
                public void onPreviewPeakMeter(int i, int i2) {
                }

                @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
                public void onProgressThumbnailCaching(int i, int i2) {
                }

                @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
                public void onSeekStateChanged(boolean z) {
                }

                @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
                public void onSetTimeDone(int i) {
                }

                @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
                public void onSetTimeFail(int i) {
                }

                @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
                public void onSetTimeIgnored() {
                }

                @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
                public void onStateChange(int i, int i2) {
                }

                @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
                public void onTimeChange(int i) {
                    nexTranscode.this.state = State.RUNNING;
                }

                @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
                public void onEncodingDone(boolean z, int i) {
                    nexTranscode.this.state = State.COMPLETE;
                    nexTranscode.this.lastError = Error.NONE;
                    if (nexTranscode.this.mVideoEditor != null) {
                        nexTranscode.this.mVideoEditor.setProjectEffect(EditorGlobal.b("std"));
                    }
                    if (nexTranscode.sEngine != null) {
                        nexTranscode.sEngine.setScalingFlag2Export(false);
                    }
                    Log.d(nexTranscode.TAG, "onEncodingDone()=" + i);
                    if (i == nexEngine.nexErrorCode.EXPORT_USER_CANCEL.getValue()) {
                        nexTranscode.this.lastError = Error.CANCEL;
                    } else if (i == nexEngine.nexErrorCode.TRANSCODING_BUSY.getValue()) {
                        nexTranscode.this.lastError = Error.BUSY;
                    } else if (i == nexEngine.nexErrorCode.TRANSCODING_NOT_SUPPORTED_FORMAT.getValue()) {
                        nexTranscode.this.lastError = Error.NOTSUPPORTEDFILE;
                    } else if (z) {
                        nexTranscode.this.lastError = Error.RUNFAIL;
                    }
                    if (nexTranscode.this.listener != null) {
                        nexTranscode.this.listener.onTransCodeDone(nexTranscode.this.lastError, i);
                    }
                }

                @Override // com.nexstreaming.nexeditorsdk.nexEngineListener
                public void onEncodingProgress(int i) {
                    nexTranscode.this.state = State.RUNNING;
                    nexTranscode.this.progressPercent = i;
                    if (nexTranscode.this.listener != null) {
                        nexTranscode.this.listener.onProgress(i, 100);
                    }
                }
            };
            Log.d(TAG, "( " + option.outputWidth + " X " + option.outputHeight + " ) bitrate=" + option.outputBitRate + ", resource=" + option.labelResource + sEngine);
            nexEngine nexengine = sEngine;
            if (nexengine != null) {
                nexengine.setScalingFlag2Export(true);
            }
            NexEditor a = EditorGlobal.a();
            this.mVideoEditor = a;
            a.setProjectEffect(EditorGlobal.b(MapBundleKey.OfflineMapKey.OFFLINE_UPDATE));
            sEngine.runTranscodeMode(option, this.source.getAbsolutePath(), sEngineListener);
            return this;
        }
    }

    public boolean cancel() {
        NexEditor nexEditor = this.mVideoEditor;
        if (nexEditor != null) {
            nexEditor.setProjectEffect(EditorGlobal.b("std"));
        }
        nexEngine nexengine = sEngine;
        if (nexengine != null) {
            nexengine.setScalingFlag2Export(false);
        }
        sEngine.stopTranscode();
        return true;
    }

    public int getProgress() {
        return this.progressPercent;
    }

    public State getCurrentState() {
        return this.state;
    }

    public Error getLastError() {
        NexEditor nexEditor = this.mVideoEditor;
        if (nexEditor != null) {
            nexEditor.setProjectEffect(EditorGlobal.b("std"));
        }
        nexEngine nexengine = sEngine;
        if (nexengine != null) {
            nexengine.setScalingFlag2Export(false);
        }
        return this.lastError;
    }
}
