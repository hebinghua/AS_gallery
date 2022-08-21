package com.miui.gallery.vlog.sdk.manager;

import android.text.TextUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.base.manager.MiVideoSdkManager;
import com.miui.gallery.vlog.entity.AudioClip;
import com.miui.gallery.vlog.sdk.interfaces.BaseVlogManager;
import com.miui.gallery.vlog.sdk.interfaces.IAudioManager;
import com.xiaomi.milab.videosdk.XmsAudioClip;
import com.xiaomi.milab.videosdk.XmsAudioFilter;
import com.xiaomi.milab.videosdk.XmsAudioTrack;
import com.xiaomi.milab.videosdk.XmsTimeline;
import com.xiaomi.milab.videosdk.XmsVideoTrack;

/* loaded from: classes2.dex */
public class MiVideoAudioManager extends BaseVlogManager implements IAudioManager {
    public AudioClip mAudioClip;
    public String mAudioLabel;
    public XmsAudioFilter mXmsAudioFilter;
    public XmsAudioFilter mXmsVideoFilter;

    public MiVideoAudioManager(MiVideoSdkManager miVideoSdkManager, XmsTimeline xmsTimeline) {
        super(miVideoSdkManager, xmsTimeline);
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0036, code lost:
        if (r2 < 0) goto L12;
     */
    @Override // com.miui.gallery.vlog.sdk.interfaces.IAudioManager
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void audioClip(long r11, long r13) {
        /*
            r10 = this;
            r0 = 0
            com.xiaomi.milab.videosdk.XmsAudioTrack r1 = r10.getAudioTrack(r0)
            if (r1 != 0) goto L8
            return
        L8:
            com.xiaomi.milab.videosdk.XmsAudioClip r1 = r1.getAudioClip(r0)
            com.xiaomi.milab.videosdk.XmsTimeline r2 = r10.mXmsTimeline
            com.xiaomi.milab.videosdk.XmsVideoTrack r0 = r2.getVideoTrack(r0)
            r2 = 1000(0x3e8, double:4.94E-321)
            long r2 = r11 / r2
            r4 = 0
            int r6 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r6 >= 0) goto L1d
            r2 = r4
        L1d:
            long r6 = r0.getDuration()
            long r6 = r6 + r2
            long r8 = r1.getsourceDuration()
            int r8 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r8 <= 0) goto L39
            long r6 = r1.getsourceDuration()
            long r2 = r0.getDuration()
            long r2 = r6 - r2
            int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r0 >= 0) goto L39
            goto L3a
        L39:
            r4 = r2
        L3a:
            r1.setInAndOut(r4, r6)
            com.miui.gallery.vlog.entity.AudioClip r0 = r10.mAudioClip
            if (r0 == 0) goto L49
            r0.setTrimIn(r11)
            com.miui.gallery.vlog.entity.AudioClip r11 = r10.mAudioClip
            r11.setTrimOut(r13)
        L49:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.vlog.sdk.manager.MiVideoAudioManager.audioClip(long, long):void");
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IAudioManager
    public void onTimelineDurationChanged() {
        applyLoopMode();
    }

    public final void applyLoopMode() {
        XmsAudioTrack audioTrack;
        XmsAudioClip audioClip;
        AudioClip audioClip2 = this.mAudioClip;
        if (audioClip2 == null || audioClip2.getDuration() <= 0 || (audioTrack = getAudioTrack(0)) == null || (audioClip = audioTrack.getAudioClip(0)) == null) {
            return;
        }
        long duration = this.mSdkManager.getDuration();
        long j = audioClip.getsourceDuration() * 1000;
        if (duration > j) {
            audioClip.setInAndOut(0L, j / 1000);
            while (j < duration) {
                setInout(audioTrack, audioTrack.appendAudioClip(this.mAudioClip.getFilePath()), null);
                j += this.mAudioClip.getDuration();
            }
            return;
        }
        audioClip(0L, duration);
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IAudioManager
    public boolean removeAudio() {
        setAudioLabel("");
        this.mAudioClip = null;
        return this.mSdkManager.removeAudio(0);
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IAudioManager
    public void removeVideoFilter() {
        this.mXmsVideoFilter = null;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IAudioManager
    public AudioClip applyAudio(String str, String str2) {
        XmsAudioFilter xmsAudioFilter = this.mXmsAudioFilter;
        float doubleValue = xmsAudioFilter != null ? (float) xmsAudioFilter.getDoubleValue("volume.percent") : 1.0f;
        this.mAudioClip = appendAudio(str);
        this.mXmsAudioFilter.setDoubleParam("volume.percent", doubleValue);
        applyLoopMode();
        setAudioLabel(str2);
        return this.mAudioClip;
    }

    public final void setAudioLabel(String str) {
        this.mAudioLabel = str;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IAudioManager
    public float getVideoTrackVolumeGain() {
        XmsAudioFilter xmsAudioFilter = this.mXmsVideoFilter;
        if (xmsAudioFilter == null) {
            return 0.0f;
        }
        return (float) (xmsAudioFilter.getDoubleValue("volume.percent") * 100.0d);
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IAudioManager
    public float getAudioTrackVolumeGain() {
        XmsAudioFilter xmsAudioFilter = this.mXmsAudioFilter;
        if (xmsAudioFilter == null) {
            return 0.0f;
        }
        return (float) (xmsAudioFilter.getDoubleValue("volume.percent") * 100.0d);
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IAudioManager
    public void setVideoTrackVolumeGain(float f, float f2) {
        XmsVideoTrack videoTrack = this.mXmsTimeline.getVideoTrack(0);
        if (videoTrack == null) {
            return;
        }
        if (this.mXmsVideoFilter == null) {
            this.mXmsVideoFilter = videoTrack.addAudioEffect("audio.volume", "");
        }
        this.mXmsVideoFilter.setDoubleParam("volume.percent", f / 100.0f);
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IAudioManager
    public void setAudioTrackVolumeGain(float f, float f2) {
        XmsAudioFilter xmsAudioFilter;
        if (this.mXmsTimeline.getAudioTrack(0) == null || (xmsAudioFilter = this.mXmsAudioFilter) == null) {
            return;
        }
        xmsAudioFilter.setDoubleParam("volume.percent", f / 100.0f);
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IAudioManager
    public long getAudioLength() {
        AudioClip audioClip = this.mAudioClip;
        if (audioClip == null) {
            return -1L;
        }
        return audioClip.getDuration();
    }

    public final XmsAudioTrack getAudioTrack(int i) {
        return this.mXmsTimeline.getAudioTrack(i);
    }

    public final AudioClip appendAudio(String str) {
        XmsAudioTrack audioTrack;
        if (!TextUtils.isEmpty(str) && (audioTrack = getAudioTrack(0)) != null) {
            removeAudio();
            audioTrack.removeAllAudioEffect();
            XmsAudioClip appendAudioClip = audioTrack.appendAudioClip(str);
            this.mXmsAudioFilter = audioTrack.addAudioEffect("audio.volume", "");
            if (appendAudioClip == null) {
                return null;
            }
            AudioClip audioClip = new AudioClip();
            audioClip.setFilePath(str);
            setInout(audioTrack, appendAudioClip, audioClip);
            audioClip.setDuration(appendAudioClip.getsourceDuration() * 1000);
            return audioClip;
        }
        return null;
    }

    public final void setInout(XmsAudioTrack xmsAudioTrack, XmsAudioClip xmsAudioClip, AudioClip audioClip) {
        long duration = this.mSdkManager.getDuration();
        long duration2 = xmsAudioTrack.getDuration() * 1000;
        long duration3 = xmsAudioClip.getDuration() * 1000;
        if (duration < duration2) {
            long j = duration2 - duration;
            if (j < duration3) {
                long j2 = duration3 - j;
                xmsAudioClip.setInAndOut(0L, j2 / 1000);
                if (audioClip == null) {
                    return;
                }
                audioClip.setTrimIn(0L);
                audioClip.setTrimOut(j2);
                return;
            }
            DefaultLogger.d("MiVideoAudioManager", "audioLen exceeds before adding audio.");
        }
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IAudioManager
    public String getAudioLabel() {
        return this.mAudioLabel;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IAudioManager
    public String getAudioPath() {
        AudioClip audioClip = this.mAudioClip;
        return audioClip == null ? "" : audioClip.getFilePath();
    }
}
