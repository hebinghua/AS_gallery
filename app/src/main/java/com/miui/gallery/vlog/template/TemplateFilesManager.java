package com.miui.gallery.vlog.template;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.imodule.loader.ModuleRegistry;
import com.miui.gallery.imodule.modules.VlogDependsModule;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.base.interfaces.OnDurationChangeListener;
import com.miui.gallery.vlog.base.manager.MiVideoSdkManager;
import com.miui.gallery.vlog.entity.VideoClip;
import com.miui.gallery.vlog.home.VlogConfig;
import com.miui.gallery.vlog.home.VlogModel;
import com.miui.gallery.vlog.sdk.interfaces.IAudioManager;
import com.miui.gallery.vlog.sdk.interfaces.IFilterManager;
import com.miui.gallery.vlog.sdk.interfaces.IHeaderTailManager;
import com.miui.gallery.vlog.sdk.interfaces.IRatioManager;
import com.miui.gallery.vlog.sdk.interfaces.ITransManager;
import com.miui.gallery.vlog.sdk.interfaces.IVideoClip;
import com.miui.gallery.vlog.template.bean.ShotInfos;
import com.miui.gallery.vlog.template.bean.Speed;
import com.miui.gallery.vlog.template.bean.SubTrackFilter;
import com.miui.gallery.vlog.template.bean.TemplateJson;
import com.miui.gallery.vlog.template.bean.effects.Filter;
import com.miui.gallery.vlog.template.bean.effects.TemplateResEffect;
import com.miui.gallery.vlog.tools.VlogUtils;
import java.io.File;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes2.dex */
public class TemplateFilesManager implements OnDurationChangeListener {
    public Context mContext;
    public boolean mIsTemplateApplied;
    public volatile List<VideoClip> mMatchedVideoClips;
    public MiVideoSdkManager mNvSdkManager;
    public String mTemplateLabel = "none";
    public TemplateResEffect mTemplateResEffect = new TemplateResEffect();
    public HashMap<String, TemplateJson> mTemplateJsonBeanHashMap = new HashMap<>();

    @Override // com.miui.gallery.vlog.base.interfaces.OnDurationChangeListener
    public void onDurationChanged() {
    }

    public TemplateFilesManager(Context context) {
        this.mContext = context;
        MiVideoSdkManager sdkManager = ((VlogModel) VlogUtils.getViewModel((FragmentActivity) this.mContext, VlogModel.class)).getSdkManager();
        this.mNvSdkManager = sdkManager;
        sdkManager.addDurationChangeListener(this);
    }

    public synchronized boolean loadTemplate(String str, String str2, List<String> list) {
        DefaultLogger.d("TemplateFilesManager", "loadTemplate %s %s", str, str2);
        this.mMatchedVideoClips = null;
        VlogDependsModule vlogDependsModule = (VlogDependsModule) ModuleRegistry.getModule(VlogDependsModule.class);
        if (vlogDependsModule != null && list != null) {
            this.mMatchedVideoClips = vlogDependsModule.matchToTemplate(str2, list);
        }
        TemplateJson templateJson = this.mTemplateJsonBeanHashMap.get(str);
        if (templateJson != null) {
            parseBean(templateJson);
            return true;
        } else if (!parseJson(str)) {
            return false;
        } else {
            parseBean(this.mTemplateJsonBeanHashMap.get(str));
            return true;
        }
    }

    public boolean parseJson(String str) {
        try {
            String str2 = VlogConfig.TEMPALTE_PATH + File.separator + str + "/info.json";
            if (!new File(str2).exists()) {
                return false;
            }
            TemplateJson templateJson = (TemplateJson) VlogUtils.fromJson(VlogUtils.loadJsonFile(str2), TemplateJson.class);
            this.mTemplateJsonBeanHashMap.put(templateJson.getName(), templateJson);
            return true;
        } catch (Exception e) {
            DefaultLogger.w("TemplateFilesManager", "parseJson occurs error.\n", e);
            return false;
        }
    }

    public void parseBean(TemplateJson templateJson) {
        TemplateResEffect templateResEffect;
        if (templateJson == null || (templateResEffect = this.mTemplateResEffect) == null) {
            return;
        }
        templateResEffect.clear();
        this.mTemplateResEffect.parseJsonBean(templateJson);
    }

    public boolean isTemplateApplied() {
        return this.mIsTemplateApplied;
    }

    public void build(List<VideoClip> list, String str) {
        if (this.mMatchedVideoClips != null) {
            list = this.mMatchedVideoClips;
        }
        if (!BaseMiscUtil.isValid(list)) {
            return;
        }
        this.mTemplateLabel = str;
        buildShotEffect(list, true, true);
        buildLutFilter();
        buildTitleFilter();
        buildAudio();
        buildRatio();
        this.mIsTemplateApplied = true;
    }

    public void remove(List<String> list) {
        this.mIsTemplateApplied = false;
        buildDefaultTimeline(list);
        removeAllShotEffect();
        removeLutFilter();
        removeTitleFilter();
        removeAudio();
        removeRatio();
    }

    public void play() {
        this.mNvSdkManager.play();
    }

    public void removeAllShotEffect() {
        List<IVideoClip> videoClips = this.mNvSdkManager.getVideoClips();
        if (videoClips == null) {
            return;
        }
        ITransManager iTransManager = (ITransManager) this.mNvSdkManager.getManagerService(9);
        int size = videoClips.size();
        for (int i = 0; i < size; i++) {
            IVideoClip iVideoClip = videoClips.get(i);
            iVideoClip.setTrans(null, null);
            iVideoClip.removeAllFx();
            iVideoClip.changeSpeed(1.0d);
            if (iTransManager != null) {
                iTransManager.buildTransitions(i, null, null);
            }
        }
        this.mNvSdkManager.removeSubTrack();
    }

    public void buildDefaultTimeline(List<String> list) {
        if (!BaseMiscUtil.isValid(list)) {
            return;
        }
        this.mNvSdkManager.removeAllClips();
        for (String str : list) {
            this.mNvSdkManager.appendClip(str);
        }
    }

    public void buildShotEffect(List<VideoClip> list, boolean z, boolean z2) {
        SparseArray<String> sparseArray;
        long j;
        int i;
        boolean z3;
        long j2;
        float f;
        int i2;
        String str;
        float f2;
        int i3;
        int i4;
        List<SubTrackFilter> list2;
        List<Speed> list3;
        float f3;
        SubTrackFilter subTrackFilter;
        if (!BaseMiscUtil.isValid(list)) {
            return;
        }
        List<ShotInfos> shotInfos = this.mTemplateResEffect.getShotInfos();
        if (!BaseMiscUtil.isValid(shotInfos)) {
            return;
        }
        this.mNvSdkManager.removeAllClips();
        SparseArray<String> sparseArray2 = new SparseArray<>();
        int size = shotInfos.size();
        int size2 = list.size();
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        while (i5 < size && i6 < size2) {
            ShotInfos shotInfos2 = shotInfos.get(i5);
            String mainTrackFilter = shotInfos2.getMainTrackFilter();
            List<SubTrackFilter> subTrackFilter2 = shotInfos2.getSubTrackFilter();
            String fullTrackFilter = shotInfos2.getFullTrackFilter();
            if (mainTrackFilter == null) {
                mainTrackFilter = shotInfos2.getFilter();
            }
            int i9 = (!z2 || subTrackFilter2 == null) ? 1 : 2;
            float duration = shotInfos2.getDuration() * 1000.0f;
            float oriDuration = shotInfos2.getOriDuration() * 1000.0f;
            if (i9 <= 1 || i6 != size2 - 1 || fullTrackFilter == null) {
                fullTrackFilter = mainTrackFilter;
            }
            int i10 = i8;
            int i11 = 0;
            while (i11 < i9 && i6 < size2) {
                List<ShotInfos> list4 = shotInfos;
                VideoClip videoClip = list.get(i6);
                int i12 = size;
                int i13 = size2;
                long trimOut = videoClip.getTrimOut() - videoClip.getTrimIn();
                int i14 = i7;
                int i15 = i8;
                long trimIn = videoClip.getTrimIn();
                long trimOut2 = videoClip.getTrimOut();
                int i16 = i9;
                float f4 = (float) trimOut;
                float f5 = 1.0f;
                if (f4 < oriDuration - 1000.0f) {
                    sparseArray = sparseArray2;
                    i = 1;
                    j = trimOut;
                    DefaultLogger.d("TemplateFilesManager", "video clip is too short %d, need %f, shot %d, video %d", Long.valueOf(trimOut), Float.valueOf(oriDuration), Integer.valueOf(i5), Integer.valueOf(i6));
                    if (duration > 0.0f) {
                        f5 = f4 / duration;
                    }
                    j2 = trimOut2;
                    f = f5;
                    z3 = false;
                } else {
                    sparseArray = sparseArray2;
                    j = trimOut;
                    i = 1;
                    if (f4 > oriDuration) {
                        j2 = ((float) trimIn) + oriDuration;
                        z3 = true;
                    } else {
                        z3 = true;
                        j2 = trimOut2;
                    }
                    f = 1.0f;
                }
                int i17 = i11 != 0 ? i : 0;
                List<Speed> list5 = null;
                if (i17 != 0) {
                    if (subTrackFilter2 == null || subTrackFilter2.size() == 0) {
                        i2 = 0;
                        subTrackFilter = null;
                    } else {
                        i2 = 0;
                        subTrackFilter = subTrackFilter2.get(0);
                    }
                    str = subTrackFilter == null ? null : subTrackFilter.getFilterName();
                } else {
                    i2 = 0;
                    str = null;
                }
                int i18 = (!is4kVideo(videoClip) || shotInfos2.getSpeed4k() == null) ? i2 : i;
                if (z3) {
                    list5 = i18 != 0 ? shotInfos2.getSpeed4k() : shotInfos2.getSpeed();
                }
                if (!z || !BaseMiscUtil.isValid(list5)) {
                    f2 = duration;
                    i3 = i5;
                    i4 = i6;
                    list2 = subTrackFilter2;
                    if (i17 != 0) {
                        this.mNvSdkManager.addSubTrackClip(videoClip.getFilePath(), i10, trimIn, j2, f, f, str);
                        i10 = (int) (i10 + f2);
                        i7 = i14;
                        i8 = i15;
                    } else {
                        this.mNvSdkManager.appendClip(videoClip.getFilePath(), trimIn, j2, f, f, fullTrackFilter);
                        i7 = i14 + 1;
                        i8 = (int) (i15 + f2);
                    }
                } else {
                    int size3 = list5.size();
                    long j3 = trimIn;
                    list2 = subTrackFilter2;
                    int i19 = i2;
                    long j4 = j;
                    int i20 = i10;
                    int i21 = i15;
                    while (i19 < size3 && j4 > 0) {
                        Speed speed = list5.get(i19);
                        int i22 = size3;
                        int i23 = i5;
                        int i24 = i6;
                        long end = (speed.getEnd() - speed.getStart()) * 1000.0f * ((speed.getSpeed0() + speed.getSpeed1()) / 2.0f);
                        long j5 = j3 + end;
                        if (i17 != 0) {
                            list3 = list5;
                            f3 = duration;
                            this.mNvSdkManager.addSubTrackClip(videoClip.getFilePath(), i20, j3, j5, speed.getSpeed0(), speed.getSpeed1(), str);
                            i20 = (int) (i20 + ((speed.getEnd() - speed.getStart()) * 1000.0f));
                        } else {
                            list3 = list5;
                            f3 = duration;
                            this.mNvSdkManager.appendClip(videoClip.getFilePath(), j3, j5, speed.getSpeed0(), speed.getSpeed1(), fullTrackFilter);
                            i14++;
                            i21 = (int) (i21 + ((speed.getEnd() - speed.getStart()) * 1000.0f));
                        }
                        j4 -= end;
                        i19++;
                        size3 = i22;
                        j3 = j5;
                        list5 = list3;
                        i5 = i23;
                        i6 = i24;
                        duration = f3;
                    }
                    f2 = duration;
                    i3 = i5;
                    i4 = i6;
                    i8 = i21;
                    i10 = i20;
                    i7 = i14;
                }
                i11++;
                i6 = i4 + 1;
                shotInfos = list4;
                size = i12;
                size2 = i13;
                i9 = i16;
                sparseArray2 = sparseArray;
                subTrackFilter2 = list2;
                i5 = i3;
                duration = f2;
            }
            int i25 = i7;
            sparseArray2 = sparseArray2;
            sparseArray2.put(i25 - 1, shotInfos2.getTrans());
            i5++;
            shotInfos = shotInfos;
            size = size;
            size2 = size2;
            i7 = i25;
            i6 = i6;
        }
        buildTransition(sparseArray2, i7);
    }

    public void buildTransition(SparseArray<String> sparseArray, int i) {
        ITransManager iTransManager = (ITransManager) this.mNvSdkManager.getManagerService(9);
        for (int i2 = 0; iTransManager != null && i2 < i; i2++) {
            iTransManager.buildTransitions(i2, sparseArray.get(i2), null);
        }
    }

    public boolean is4kVideo(VideoClip videoClip) {
        int width = videoClip.getWidth();
        int height = videoClip.getHeight();
        return width <= 0 || height <= 0 || Math.max(width, height) >= 3800;
    }

    public void buildLutFilter() {
        IFilterManager iFilterManager;
        removeLutFilter();
        Filter timelineFilter = this.mTemplateResEffect.getTimelineFilter();
        if (timelineFilter == null || (iFilterManager = (IFilterManager) this.mNvSdkManager.getManagerService(0)) == null) {
            return;
        }
        iFilterManager.buildTrackFilter(timelineFilter.getFilterLut(), 80, this.mTemplateLabel, true);
    }

    public void buildTitleFilter() {
        removeTitleFilter();
        IHeaderTailManager iHeaderTailManager = (IHeaderTailManager) this.mNvSdkManager.getManagerService(8);
        Filter timelineTitleFilter = this.mTemplateResEffect.getTimelineTitleFilter();
        if (timelineTitleFilter == null || iHeaderTailManager == null) {
            return;
        }
        iHeaderTailManager.setHeaderTail(true, timelineTitleFilter.getVideoFxPackageId(), 3, timelineTitleFilter.getDuration(), this.mTemplateLabel);
    }

    public void buildAudio() {
        String musicPath = this.mTemplateResEffect.getMusicPath();
        if (!TextUtils.isEmpty(musicPath)) {
            IAudioManager iAudioManager = (IAudioManager) this.mNvSdkManager.getManagerService(2);
            iAudioManager.setVideoTrackVolumeGain(0.0f, 0.0f);
            iAudioManager.setAudioTrackVolumeGain(100.0f, 100.0f);
            iAudioManager.applyAudio(musicPath, this.mTemplateLabel);
        }
    }

    public void buildRatio() {
        IRatioManager iRatioManager = (IRatioManager) this.mNvSdkManager.getManagerService(10);
        if (iRatioManager != null) {
            iRatioManager.setLiveWindowRatio(this.mTemplateLabel, 0);
        }
    }

    public void removeLutFilter() {
        IFilterManager iFilterManager = (IFilterManager) this.mNvSdkManager.getManagerService(0);
        if (iFilterManager != null) {
            iFilterManager.removeTrackFilter();
        }
    }

    public void removeTitleFilter() {
        IHeaderTailManager iHeaderTailManager = (IHeaderTailManager) this.mNvSdkManager.getManagerService(8);
        if (iHeaderTailManager != null) {
            iHeaderTailManager.removeHeaderTail();
        }
    }

    public void removeAudio() {
        TemplateResEffect templateResEffect = this.mTemplateResEffect;
        if (templateResEffect != null) {
            templateResEffect.setMusicPath("");
        }
        IAudioManager iAudioManager = (IAudioManager) this.mNvSdkManager.getManagerService(2);
        if (iAudioManager != null) {
            iAudioManager.removeAudio();
            iAudioManager.setVideoTrackVolumeGain(100.0f, 100.0f);
        }
    }

    public void removeRatio() {
        IRatioManager iRatioManager = (IRatioManager) this.mNvSdkManager.getManagerService(10);
        if (iRatioManager != null) {
            iRatioManager.setLiveWindowRatio("", -1);
        }
    }
}
