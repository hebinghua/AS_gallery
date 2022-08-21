package com.xiaomi.milab.videosdk.message;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.xiaomi.milab.videosdk.interfaces.AudioExtractCallback;
import com.xiaomi.milab.videosdk.interfaces.ExportCallback;
import com.xiaomi.milab.videosdk.interfaces.PlayCallback;
import com.xiaomi.milab.videosdk.interfaces.TimelineCallback;
import com.xiaomi.milab.videosdk.message.MsgType;
import java.lang.ref.WeakReference;

/* loaded from: classes3.dex */
public class EventHandler extends Handler {
    public final String TAG;
    private AudioExtractCallback mAudioExtractCallback;
    private ExportCallback mExportCallback;
    private PlayCallback mPlayCallback;
    private TimelineCallback mTimelineCacllback;
    private TranscodeCallback mTranscodeCallback;
    private final WeakReference<EventHandler> mWeakContext;

    public EventHandler(Looper looper) {
        super(looper);
        this.TAG = "EventHandler";
        this.mPlayCallback = null;
        this.mExportCallback = null;
        this.mTranscodeCallback = null;
        this.mAudioExtractCallback = null;
        this.mTimelineCacllback = null;
        this.mWeakContext = new WeakReference<>(this);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        AudioExtractCallback audioExtractCallback;
        TimelineCallback timelineCallback;
        if (this.mWeakContext.get() == null) {
            Log.w("EventHandler", "XmsContext went away with unhandled events");
            return;
        }
        Log.d("EventHandler", "get msgType:" + message.what + "msgEvent:" + message.arg1 + "msgValue" + message.arg2);
        switch (message.what) {
            case MsgType.XMSCONTEXT /* 10001 */:
                switch (message.arg1) {
                    case 100:
                        PlayCallback playCallback = this.mPlayCallback;
                        if (playCallback == null) {
                            return;
                        }
                        playCallback.onTimelineStateChanged(message.arg2);
                        return;
                    case 101:
                        PlayCallback playCallback2 = this.mPlayCallback;
                        if (playCallback2 == null) {
                            return;
                        }
                        playCallback2.onPlayTimelinePosition(message.arg2);
                        return;
                    case 102:
                        PlayCallback playCallback3 = this.mPlayCallback;
                        if (playCallback3 == null) {
                            return;
                        }
                        playCallback3.onPlayEOF();
                        return;
                    case 103:
                        PlayCallback playCallback4 = this.mPlayCallback;
                        if (playCallback4 == null) {
                            return;
                        }
                        playCallback4.onTimelineSeekComplete(message.arg2);
                        return;
                    case 104:
                        PlayCallback playCallback5 = this.mPlayCallback;
                        if (playCallback5 != null) {
                            playCallback5.onTimelineSurfaceChange(message.arg2);
                            XmsMessage.OnUserCommand(110, 0, 0, 0);
                            return;
                        }
                        XmsMessage.OnUserCommand(110, 0, 0, 0);
                        return;
                    default:
                        return;
                }
            case MsgType.XMSEXPORT /* 10002 */:
                switch (message.arg1) {
                    case 201:
                        ExportCallback exportCallback = this.mExportCallback;
                        if (exportCallback == null) {
                            return;
                        }
                        exportCallback.onExportProgress(message.arg2);
                        return;
                    case 202:
                        ExportCallback exportCallback2 = this.mExportCallback;
                        if (exportCallback2 == null) {
                            return;
                        }
                        exportCallback2.onExportSuccess();
                        return;
                    case 203:
                        ExportCallback exportCallback3 = this.mExportCallback;
                        if (exportCallback3 == null) {
                            return;
                        }
                        exportCallback3.onExportCancel();
                        return;
                    case 204:
                        ExportCallback exportCallback4 = this.mExportCallback;
                        if (exportCallback4 == null) {
                            return;
                        }
                        exportCallback4.onExportFail();
                        return;
                    default:
                        return;
                }
            case MsgType.XMSTRANSCODE /* 10003 */:
                switch (message.arg1) {
                    case MsgType.MsgEvent.PLAYER_EVENT_TRANSCODE_PROGRESS /* 301 */:
                        TranscodeCallback transcodeCallback = this.mTranscodeCallback;
                        if (transcodeCallback == null) {
                            return;
                        }
                        transcodeCallback.onTranscodeProgress(message.arg2);
                        return;
                    case MsgType.MsgEvent.PLAYER_EVENT_TRANSCODE_SUCCESS /* 302 */:
                        if (this.mExportCallback == null) {
                            return;
                        }
                        this.mTranscodeCallback.onTranscodeSuccess();
                        return;
                    case MsgType.MsgEvent.PLAYER_EVENT_TRANSCODE_CANCEL /* 303 */:
                        if (this.mExportCallback == null) {
                            return;
                        }
                        this.mTranscodeCallback.onTranscodeCancel();
                        return;
                    case MsgType.MsgEvent.PLAYER_EVENT_TRANSCODE_FAIL /* 304 */:
                        if (this.mExportCallback == null) {
                            return;
                        }
                        this.mTranscodeCallback.onTranscodeFail();
                        return;
                    default:
                        return;
                }
            case MsgType.XMSAUDIOEXTRACT /* 10004 */:
                int i = message.arg1;
                if (i != 401) {
                    if (i != 402 || (audioExtractCallback = this.mAudioExtractCallback) == null) {
                        return;
                    }
                    audioExtractCallback.onExtractFinished();
                    return;
                }
                AudioExtractCallback audioExtractCallback2 = this.mAudioExtractCallback;
                if (audioExtractCallback2 == null) {
                    return;
                }
                audioExtractCallback2.onExtractProgress(message.arg2);
                return;
            case MsgType.XMSTIMELINE /* 10005 */:
                if (message.arg1 != 501 || (timelineCallback = this.mTimelineCacllback) == null) {
                    return;
                }
                timelineCallback.onTimelineStarted();
                return;
            default:
                Log.e("EventHandler", "Unknown message type " + message.what);
                return;
        }
    }

    public void setPlayCallback(PlayCallback playCallback) {
        this.mPlayCallback = playCallback;
    }

    public void setExportCallback(ExportCallback exportCallback) {
        this.mExportCallback = exportCallback;
    }

    public void setTranscodeCallback(TranscodeCallback transcodeCallback) {
        this.mTranscodeCallback = transcodeCallback;
    }

    public void setAudioExtractCallback(AudioExtractCallback audioExtractCallback) {
        this.mAudioExtractCallback = audioExtractCallback;
    }

    public void setTimelineCallback(TimelineCallback timelineCallback) {
        this.mTimelineCacllback = timelineCallback;
    }
}
