package com.miui.gallery.vlog.sdk.manager;

import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.sdk.callbacks.CompileCallback;
import com.miui.gallery.vlog.sdk.callbacks.PlaybackCallback;
import com.miui.gallery.vlog.sdk.callbacks.ResizeCallback;
import com.miui.gallery.vlog.sdk.callbacks.SeekCallback;
import com.miui.gallery.vlog.tools.DebugLogUtils;
import com.xiaomi.milab.videosdk.interfaces.ExportCallback;
import com.xiaomi.milab.videosdk.interfaces.PlayCallback;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class MiVideoStateController implements PlayCallback, ExportCallback {
    public CompileCallback mCompileCallback;
    public ResizeCallback mResizeCallback;
    public SeekCallback mSeekCallback;
    public int mEngineState = -1;
    public int mTimelineState = -1;
    public List<PlaybackCallback> mPlaybackCallbackList = new ArrayList();
    public long lastpos = 0;

    public void setCompileCallback(CompileCallback compileCallback) {
        this.mCompileCallback = compileCallback;
    }

    public void addPlaybackCallback(PlaybackCallback playbackCallback) {
        this.mPlaybackCallbackList.add(playbackCallback);
    }

    public void removePlaybackCallback(PlaybackCallback playbackCallback) {
        if (this.mPlaybackCallbackList.contains(playbackCallback)) {
            this.mPlaybackCallbackList.remove(playbackCallback);
        }
    }

    public void setSeekCallback(SeekCallback seekCallback) {
        this.mSeekCallback = seekCallback;
    }

    public void setResizeCallback(ResizeCallback resizeCallback) {
        this.mResizeCallback = resizeCallback;
    }

    public final void printLog() {
        if (!DebugLogUtils.IS_FIRST_FRAME_LOADED_INTO_VLOG && DebugLogUtils.HAS_LOADED_TEMPLATE_DEFAULT) {
            DebugLogUtils.IS_FIRST_FRAME_LOADED_INTO_VLOG = true;
            DebugLogUtils.HAS_LOADED_TEMPLATE_DEFAULT = false;
            DebugLogUtils.endDebugLogSpecialTime("MiVideoStateController", "VlogTemplateMatchActivity Create");
        }
        if (!DebugLogUtils.IS_FIRST_FRAME_LOADED_SELECT_TEMPLATE && DebugLogUtils.HAS_LOADED_SELECT_TEMPLATE) {
            DebugLogUtils.IS_FIRST_FRAME_LOADED_SELECT_TEMPLATE = true;
            DebugLogUtils.HAS_LOADED_SELECT_TEMPLATE = false;
            DebugLogUtils.endDebugLogSpecialTime("MiVideoStateController", "vlog select Template");
        }
        if (!DebugLogUtils.IS_FIRST_FRAME_LOADED_SELECT_MUSIC && DebugLogUtils.HAS_LOADED_SELECT_MUSIC) {
            DebugLogUtils.IS_FIRST_FRAME_LOADED_SELECT_MUSIC = true;
            DebugLogUtils.HAS_LOADED_SELECT_MUSIC = false;
            DebugLogUtils.endDebugLogSpecialTime("MiVideoStateController", "vlog applyAudio");
        }
        if (!DebugLogUtils.IS_FIRST_FRAME_LOADED_SELECT_HEADTAIL && DebugLogUtils.HAS_LOADED_SELECT_HEADTAIL) {
            DebugLogUtils.IS_FIRST_FRAME_LOADED_SELECT_HEADTAIL = true;
            DebugLogUtils.HAS_LOADED_SELECT_HEADTAIL = false;
            DebugLogUtils.endDebugLogSpecialTime("MiVideoStateController", "vlog applyHeadTail");
        }
        if (DebugLogUtils.IS_FIRST_FRAME_LOADED_SELECT_TRANS || !DebugLogUtils.HAS_LOADED_SELECT_TRANS) {
            return;
        }
        DebugLogUtils.IS_FIRST_FRAME_LOADED_SELECT_TRANS = true;
        DebugLogUtils.HAS_LOADED_SELECT_TRANS = false;
        DebugLogUtils.endDebugLogSpecialTime("MiVideoStateController", "vlog applyTrans");
    }

    @Override // com.xiaomi.milab.videosdk.interfaces.PlayCallback
    public void onPlayTimelinePosition(long j) {
        printLog();
        DefaultLogger.d("MiVideoStateController", "progress " + j);
        if (this.mPlaybackCallbackList.size() > 0 && this.lastpos != j) {
            this.lastpos = j;
            for (PlaybackCallback playbackCallback : this.mPlaybackCallbackList) {
                long j2 = 1000 * j;
                playbackCallback.onPlaybackTimelinePosition(j2);
                playbackCallback.onPlaybackTimelinePositionMicro(j2);
            }
        }
    }

    @Override // com.xiaomi.milab.videosdk.interfaces.PlayCallback
    public void onPlayEOF() {
        DefaultLogger.d("MiVideoStateController", "progress end");
        for (PlaybackCallback playbackCallback : this.mPlaybackCallbackList) {
            playbackCallback.onPlaybackEOF();
        }
    }

    @Override // com.xiaomi.milab.videosdk.interfaces.PlayCallback
    public void onTimelineStateChanged(int i) {
        if (i == 2 || i == 0) {
            for (PlaybackCallback playbackCallback : this.mPlaybackCallbackList) {
                playbackCallback.onPlaybackStopped();
            }
        }
        DefaultLogger.d("MiVideoStateController", "state " + i);
    }

    @Override // com.xiaomi.milab.videosdk.interfaces.ExportCallback
    public void onExportProgress(int i) {
        CompileCallback compileCallback = this.mCompileCallback;
        if (compileCallback == null) {
            return;
        }
        compileCallback.onCompileProgress(i);
    }

    @Override // com.xiaomi.milab.videosdk.interfaces.ExportCallback
    public void onExportSuccess() {
        CompileCallback compileCallback = this.mCompileCallback;
        if (compileCallback == null) {
            return;
        }
        compileCallback.onCompileCompleted(false);
    }

    @Override // com.xiaomi.milab.videosdk.interfaces.ExportCallback
    public void onExportCancel() {
        CompileCallback compileCallback = this.mCompileCallback;
        if (compileCallback == null) {
            return;
        }
        compileCallback.onCompileCompleted(true);
    }

    @Override // com.xiaomi.milab.videosdk.interfaces.ExportCallback
    public void onExportFail() {
        CompileCallback compileCallback = this.mCompileCallback;
        if (compileCallback == null) {
            return;
        }
        compileCallback.onCompileFailed();
    }

    @Override // com.xiaomi.milab.videosdk.interfaces.PlayCallback
    public void onTimelineSeekComplete(long j) {
        SeekCallback seekCallback = this.mSeekCallback;
        if (seekCallback == null) {
            return;
        }
        seekCallback.seekTime(j);
    }

    @Override // com.xiaomi.milab.videosdk.interfaces.PlayCallback
    public void onTimelineSurfaceChange(int i) {
        ResizeCallback resizeCallback = this.mResizeCallback;
        if (resizeCallback == null) {
            return;
        }
        resizeCallback.resize(i);
    }
}
