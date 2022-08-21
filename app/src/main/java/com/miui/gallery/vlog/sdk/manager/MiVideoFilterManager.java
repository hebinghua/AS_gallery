package com.miui.gallery.vlog.sdk.manager;

import android.text.TextUtils;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.vlog.base.interfaces.OnDurationChangeListener;
import com.miui.gallery.vlog.base.manager.MiVideoSdkManager;
import com.miui.gallery.vlog.sdk.interfaces.BaseVlogManager;
import com.miui.gallery.vlog.sdk.interfaces.IFilterManager;
import com.xiaomi.milab.videosdk.XmsTimeline;
import com.xiaomi.milab.videosdk.XmsVideoFilter;
import com.xiaomi.milab.videosdk.XmsVideoTrack;
import java.io.File;

/* loaded from: classes2.dex */
public class MiVideoFilterManager extends BaseVlogManager implements IFilterManager, OnDurationChangeListener {
    public final float MASTER_NOISE_STRENGTH_DEFAULT;
    public final float MASTER_VIGNETTE_STRENGTH_DEFAULT;
    public int mCurrentFilterStrength;
    public String mFilterLabel;
    public boolean mIsMasterOpen;
    public XmsVideoFilter mXmsVideoFilter;

    @Override // com.miui.gallery.vlog.base.interfaces.OnDurationChangeListener
    public void onDurationChanged() {
    }

    public MiVideoFilterManager(MiVideoSdkManager miVideoSdkManager, XmsTimeline xmsTimeline) {
        super(miVideoSdkManager, xmsTimeline);
        this.MASTER_VIGNETTE_STRENGTH_DEFAULT = 0.3f;
        this.MASTER_NOISE_STRENGTH_DEFAULT = 0.25f;
        this.mIsMasterOpen = true;
        this.mCurrentFilterStrength = 80;
        miVideoSdkManager.addDurationChangeListener(this);
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IFilterManager
    public void buildTrackFilter(boolean z, String str, String str2, int i, String str3) {
        if (!TextUtils.isEmpty(str)) {
            if (z && (TextUtils.isEmpty(str2) || !new File(str2).exists())) {
                return;
            }
            XmsVideoTrack videoTrack = this.mXmsTimeline.getVideoTrack(0);
            XmsVideoFilter addVideoEffect = videoTrack.addVideoEffect(str, str2);
            if ("movit.filter.track_lut".equals(str)) {
                this.mXmsVideoFilter = addVideoEffect;
            }
            updateTrackFilterIntensity(i);
            buildTrackMasterFilter(videoTrack, true);
            setFilterLabel(str3);
        }
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IFilterManager
    public void buildTrackFilter(String str, int i, String str2, boolean z) {
        if (TextUtils.isEmpty(str) || !new File(str).exists()) {
            return;
        }
        XmsVideoTrack videoTrack = this.mXmsTimeline.getVideoTrack(0);
        videoTrack.removeVideoEffectByName("movit.filter.track_lut");
        this.mXmsVideoFilter = videoTrack.addVideoEffect("movit.filter.track_lut", str);
        updateTrackFilterIntensity(i);
        buildTrackMasterFilter(videoTrack, z);
        setFilterLabel(str2);
    }

    public final void buildTrackMasterFilter(XmsVideoTrack xmsVideoTrack, boolean z) {
        if (xmsVideoTrack == null) {
            return;
        }
        xmsVideoTrack.removeVideoEffectByName("movit.filter.track.master");
        XmsVideoFilter addVideoEffect = xmsVideoTrack.addVideoEffect("movit.filter.track.master", "");
        if (addVideoEffect == null) {
            return;
        }
        double d = SearchStatUtils.POW;
        addVideoEffect.setDoubleParam("filter.master.vignette", z ? 0.30000001192092896d : 0.0d);
        if (z) {
            d = 0.25d;
        }
        addVideoEffect.setDoubleParam("filter.master.noise", d);
        this.mIsMasterOpen = z;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IFilterManager
    public void removeTrackFilter() {
        this.mXmsVideoFilter = null;
        XmsVideoTrack videoTrack = this.mXmsTimeline.getVideoTrack(0);
        if (videoTrack != null) {
            videoTrack.removeAllVideoEffect();
        }
        setFilterLabel("");
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IFilterManager
    public void updateTrackFilterIntensity(int i) {
        XmsVideoFilter xmsVideoFilter = this.mXmsVideoFilter;
        if (xmsVideoFilter != null) {
            xmsVideoFilter.setDoubleParam("lut.strength", i / 100.0f);
            this.mCurrentFilterStrength = i;
        }
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IFilterManager
    public String getFilterLabel() {
        return this.mFilterLabel;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IFilterManager
    public int getCurrentFilterStrength() {
        return this.mCurrentFilterStrength;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IFilterManager
    public boolean isMasterFilterOpen() {
        return this.mIsMasterOpen;
    }

    public void setFilterLabel(String str) {
        this.mFilterLabel = str;
    }
}
