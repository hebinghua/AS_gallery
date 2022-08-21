package com.miui.gallery.vlog.template;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;
import com.miui.gallery.imodule.loader.ModuleRegistry;
import com.miui.gallery.imodule.modules.VlogDependsModule;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.entity.VideoClip;
import com.miui.gallery.vlog.home.VlogConfig;
import com.miui.gallery.vlog.sdk.interfaces.IAudioManager;
import com.miui.gallery.vlog.sdk.interfaces.ICaptionManager;
import com.miui.gallery.vlog.sdk.interfaces.IFilterManager;
import com.miui.gallery.vlog.sdk.interfaces.IHeaderTailManager;
import com.miui.gallery.vlog.sdk.interfaces.IRatioManager;
import com.miui.gallery.vlog.sdk.interfaces.ITransManager;
import com.miui.gallery.vlog.sdk.interfaces.IVideoClip;
import com.miui.gallery.vlog.template.bean.ClipInfo;
import com.miui.gallery.vlog.template.bean.FilterBeanProcessed;
import com.miui.gallery.vlog.template.bean.MiTemplateJson;
import com.miui.gallery.vlog.template.bean.Speed;
import com.miui.gallery.vlog.template.bean.effects.MiTemplateResEffect;
import com.miui.gallery.vlog.tools.VlogUtils;
import java.io.File;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes2.dex */
public class MiVideoTemplateFilesManager extends TemplateFilesManager {
    public HashMap<String, MiTemplateJson> mTemplateJsonBeanHashMap;
    public MiTemplateResEffect mTemplateResEffect;

    @Override // com.miui.gallery.vlog.template.TemplateFilesManager, com.miui.gallery.vlog.base.interfaces.OnDurationChangeListener
    public void onDurationChanged() {
    }

    public MiVideoTemplateFilesManager(Context context) {
        super(context);
        this.mTemplateResEffect = new MiTemplateResEffect();
        this.mTemplateJsonBeanHashMap = new HashMap<>();
    }

    @Override // com.miui.gallery.vlog.template.TemplateFilesManager
    public synchronized boolean loadTemplate(String str, String str2, List<String> list) {
        DefaultLogger.d("TemplateFilesManager", "loadTemplate %s %s", str, str2);
        this.mMatchedVideoClips = null;
        VlogDependsModule vlogDependsModule = (VlogDependsModule) ModuleRegistry.getModule(VlogDependsModule.class);
        if (vlogDependsModule != null && list != null) {
            this.mMatchedVideoClips = vlogDependsModule.matchToTemplate(str2, list);
        }
        MiTemplateJson miTemplateJson = this.mTemplateJsonBeanHashMap.get(str);
        if (miTemplateJson != null) {
            parseBean(miTemplateJson);
            return true;
        } else if (!parseJson(str)) {
            return false;
        } else {
            parseBean(this.mTemplateJsonBeanHashMap.get(str));
            return true;
        }
    }

    @Override // com.miui.gallery.vlog.template.TemplateFilesManager
    public boolean parseJson(String str) {
        try {
            String str2 = VlogConfig.TEMPALTE_PATH + File.separator + str + "/info.json";
            if (!new File(str2).exists()) {
                return false;
            }
            MiTemplateJson miTemplateJson = (MiTemplateJson) VlogUtils.fromJson(VlogUtils.loadJsonFile(str2), MiTemplateJson.class);
            this.mTemplateJsonBeanHashMap.put(miTemplateJson.getName(), miTemplateJson);
            return true;
        } catch (Exception e) {
            DefaultLogger.w("TemplateFilesManager", "parseJson occurs error.\n", e);
            return false;
        }
    }

    public void parseBean(MiTemplateJson miTemplateJson) {
        MiTemplateResEffect miTemplateResEffect;
        if (miTemplateJson == null || (miTemplateResEffect = this.mTemplateResEffect) == null) {
            return;
        }
        miTemplateResEffect.clear();
        this.mTemplateResEffect.parseJsonBean(miTemplateJson);
    }

    @Override // com.miui.gallery.vlog.template.TemplateFilesManager
    public boolean isTemplateApplied() {
        return this.mIsTemplateApplied;
    }

    @Override // com.miui.gallery.vlog.template.TemplateFilesManager
    public void build(List<VideoClip> list, String str) {
        if (this.mMatchedVideoClips != null) {
            list = this.mMatchedVideoClips;
        }
        if (!BaseMiscUtil.isValid(list)) {
            return;
        }
        this.mTemplateLabel = str;
        removeAllTrackContent();
        buildClipEffect(list, true, true);
        buildTrackFilter();
        buildHeaderTail();
        buildAudio();
        buildRatio();
        buildCaption();
        this.mIsTemplateApplied = true;
    }

    @Override // com.miui.gallery.vlog.template.TemplateFilesManager
    public void remove(List<String> list) {
        this.mIsTemplateApplied = false;
        removeAllTrackContent();
        buildDefaultTimeline(list);
        removeAllClipEffect();
        removeTrackFilter();
        removeTitleFilter();
        removeAudio();
        removeRatio();
    }

    @Override // com.miui.gallery.vlog.template.TemplateFilesManager
    public void play() {
        this.mNvSdkManager.play();
    }

    public void removeAllTrackContent() {
        this.mNvSdkManager.removeAllClips();
        removeTitleFilter();
        IAudioManager iAudioManager = (IAudioManager) this.mNvSdkManager.getManagerService(2);
        if (iAudioManager != null) {
            iAudioManager.removeAudio();
            iAudioManager.removeVideoFilter();
        }
    }

    public void removeAllClipEffect() {
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

    @Override // com.miui.gallery.vlog.template.TemplateFilesManager
    public void buildDefaultTimeline(List<String> list) {
        if (!BaseMiscUtil.isValid(list)) {
            return;
        }
        this.mNvSdkManager.removeAllClips();
        for (String str : list) {
            this.mNvSdkManager.appendClip(str);
        }
    }

    public void buildClipEffect(List<VideoClip> list, boolean z, boolean z2) {
        long j;
        long j2;
        boolean z3;
        int i;
        int i2;
        int i3;
        float f;
        char c;
        float f2;
        long j3;
        int i4;
        int i5;
        if (!BaseMiscUtil.isValid(list)) {
            return;
        }
        List<ClipInfo> clipInfoList = this.mTemplateResEffect.getClipInfoList();
        if (!BaseMiscUtil.isValid(clipInfoList)) {
            return;
        }
        this.mNvSdkManager.removeAllClips();
        SparseArray<AppendClipInfo> sparseArray = new SparseArray<>();
        SparseArray<TransitionBuildInfo> sparseArray2 = new SparseArray<>();
        int size = clipInfoList.size();
        int size2 = list.size();
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        while (i6 < size && i7 < size2) {
            ClipInfo clipInfo = clipInfoList.get(i6);
            int i9 = (!z2 || clipInfo.getSubTrackFilter() == null) ? 1 : 2;
            float duration = clipInfo.getDuration() * 1000.0f;
            float oriDuration = clipInfo.getOriDuration() * 1000.0f;
            int i10 = 0;
            while (i10 < i9 && i7 < size2) {
                VideoClip videoClip = list.get(i7);
                int i11 = size;
                int i12 = size2;
                long trimOut = videoClip.getTrimOut() - videoClip.getTrimIn();
                int i13 = i9;
                long trimIn = videoClip.getTrimIn();
                long trimOut2 = videoClip.getTrimOut();
                List<ClipInfo> list2 = clipInfoList;
                float f3 = (float) trimOut;
                float f4 = 1.0f;
                if (f3 < oriDuration - 1000.0f) {
                    if (duration > 0.0f) {
                        f4 = f3 / duration;
                    }
                    j = trimOut;
                    j2 = trimOut2;
                    z3 = false;
                } else {
                    if (f3 > oriDuration) {
                        j = trimOut;
                        j2 = ((float) trimIn) + oriDuration;
                    } else {
                        j = trimOut;
                        j2 = trimOut2;
                    }
                    z3 = true;
                }
                boolean z4 = i10 != 0;
                List<Speed> list3 = null;
                boolean z5 = is4kVideo(videoClip) && clipInfo.getSpeed4k() != null;
                if (z3) {
                    list3 = z5 ? clipInfo.getSpeed4k() : clipInfo.getSpeed();
                }
                List<Speed> list4 = list3;
                if (!z || !BaseMiscUtil.isValid(list4)) {
                    i = i10;
                    i2 = i6;
                    i3 = i7;
                    f = duration;
                    c = 0;
                    if (z4) {
                        this.mNvSdkManager.addSubTrackClip(videoClip.getFilePath(), 0L, 0L, 0L, 0.0f, 0.0f, null);
                    } else {
                        float f5 = (float) trimIn;
                        float f6 = (float) j2;
                        sparseArray.put(i8, new AppendClipInfo(videoClip.getFilePath(), (float) videoClip.getWholeDuration(), f5, f6, f4, f4, this.mTemplateResEffect.getClipFilterList(clipInfo)));
                        sparseArray2.put(i8, new TransitionBuildInfo(f5, f6, f4, f4, this.mTemplateResEffect.getFilterBeanName(clipInfo.getTransition()), this.mTemplateResEffect.getFilterBeanParam(clipInfo.getTransition())));
                        i8++;
                    }
                } else {
                    int size3 = list4.size();
                    int i14 = 0;
                    while (i14 < size3 && j > 0) {
                        Speed speed = list4.get(i14);
                        float speed0 = speed.getSpeed0();
                        float speed1 = speed.getSpeed1();
                        float end = (speed.getEnd() - speed.getStart()) * 1000.0f;
                        List<Speed> list5 = list4;
                        int i15 = i6;
                        int i16 = i7;
                        long j4 = end * ((speed0 + speed1) / 2.0f);
                        int i17 = i14;
                        int i18 = i10;
                        long j5 = trimIn + j4;
                        if (z4) {
                            this.mNvSdkManager.addSubTrackClip(videoClip.getFilePath(), 0L, 0L, 0L, 0.0f, 0.0f, null);
                            i5 = size3;
                            j3 = j4;
                            f2 = duration;
                            i4 = i17;
                        } else {
                            f2 = duration;
                            j3 = j4;
                            float f7 = (float) trimIn;
                            float f8 = (float) j5;
                            sparseArray.put(i8, new AppendClipInfo(videoClip.getFilePath(), (float) videoClip.getWholeDuration(), f7, f8, speed0, speed1, this.mTemplateResEffect.getClipFilterList(clipInfo)));
                            i4 = i17;
                            if (i4 == size3 - 1) {
                                i5 = size3;
                                sparseArray2.put(i8, new TransitionBuildInfo(f7, f8, speed0, speed1, this.mTemplateResEffect.getFilterBeanName(clipInfo.getTransition()), this.mTemplateResEffect.getFilterBeanParam(clipInfo.getTransition())));
                            } else {
                                i5 = size3;
                            }
                            i8++;
                        }
                        j -= j3;
                        trimIn = j5;
                        i6 = i15;
                        i7 = i16;
                        duration = f2;
                        i10 = i18;
                        i14 = i4 + 1;
                        list4 = list5;
                        size3 = i5;
                    }
                    i = i10;
                    i2 = i6;
                    i3 = i7;
                    f = duration;
                    c = 0;
                }
                i10 = i + 1;
                i7 = i3 + 1;
                size = i11;
                i9 = i13;
                clipInfoList = list2;
                size2 = i12;
                i6 = i2;
                duration = f;
            }
            i6++;
            size = size;
            clipInfoList = clipInfoList;
            size2 = size2;
            i7 = i7;
        }
        processTransitionAndAppendClip(sparseArray2, sparseArray);
        appendClip(sparseArray);
        buildTransition(sparseArray2);
    }

    public final void processTransitionAndAppendClip(SparseArray<TransitionBuildInfo> sparseArray, SparseArray<AppendClipInfo> sparseArray2) {
        for (int i = 0; i < sparseArray.size(); i++) {
            int keyAt = sparseArray.keyAt(i);
            TransitionBuildInfo transitionBuildInfo = sparseArray.get(keyAt);
            if (transitionBuildInfo != null && (!TextUtils.isEmpty(transitionBuildInfo.transitionName) || !TextUtils.isEmpty(transitionBuildInfo.transitionParam))) {
                AppendClipInfo appendClipInfo = sparseArray2.get(keyAt);
                AppendClipInfo appendClipInfo2 = sparseArray2.get(keyAt + 1);
                if (appendClipInfo == null || appendClipInfo2 == null) {
                    sparseArray.put(keyAt, null);
                } else {
                    float speedRatioFitTrans = VlogUtils.getSpeedRatioFitTrans(appendClipInfo.duration, appendClipInfo.startSpeed, appendClipInfo.endSpeed, 900000.0f);
                    appendClipInfo.startSpeed *= speedRatioFitTrans;
                    appendClipInfo.endSpeed *= speedRatioFitTrans;
                    float speedRatioFitTrans2 = VlogUtils.getSpeedRatioFitTrans(appendClipInfo2.duration, appendClipInfo2.startSpeed, appendClipInfo2.endSpeed, 900000.0f);
                    appendClipInfo2.startSpeed *= speedRatioFitTrans2;
                    appendClipInfo2.endSpeed *= speedRatioFitTrans2;
                }
            }
        }
    }

    public final void appendClip(SparseArray<AppendClipInfo> sparseArray) {
        for (int i = 0; i < sparseArray.size(); i++) {
            AppendClipInfo appendClipInfo = sparseArray.get(sparseArray.keyAt(i));
            if (appendClipInfo != null) {
                this.mNvSdkManager.appendClip(appendClipInfo.path, appendClipInfo.trimIn, appendClipInfo.trimOut, appendClipInfo.startSpeed, appendClipInfo.endSpeed, appendClipInfo.filterList);
            }
        }
    }

    public void buildTransition(SparseArray<TransitionBuildInfo> sparseArray) {
        ITransManager iTransManager = (ITransManager) this.mNvSdkManager.getManagerService(9);
        for (int i = 0; i < sparseArray.size(); i++) {
            int keyAt = sparseArray.keyAt(i);
            TransitionBuildInfo transitionBuildInfo = sparseArray.get(keyAt);
            if (transitionBuildInfo != null) {
                iTransManager.buildTransitions(keyAt, transitionBuildInfo.transitionName, transitionBuildInfo.transitionParam);
            }
        }
    }

    public void buildCaption() {
        ((ICaptionManager) this.mNvSdkManager.getManagerService(1)).reAddCaptions();
    }

    @Override // com.miui.gallery.vlog.template.TemplateFilesManager
    public boolean is4kVideo(VideoClip videoClip) {
        int width = videoClip.getWidth();
        int height = videoClip.getHeight();
        return width <= 0 || height <= 0 || Math.max(width, height) >= 3800;
    }

    public void buildTrackFilter() {
        removeTrackFilter();
        List<FilterBeanProcessed> trackFilter = this.mTemplateResEffect.getTrackFilter();
        IFilterManager iFilterManager = (IFilterManager) this.mNvSdkManager.getManagerService(0);
        if (trackFilter == null || iFilterManager == null) {
            return;
        }
        for (FilterBeanProcessed filterBeanProcessed : trackFilter) {
            iFilterManager.buildTrackFilter(filterBeanProcessed.paramIsFile, filterBeanProcessed.filterName, filterBeanProcessed.filterParam, 80, this.mTemplateLabel);
        }
    }

    public void buildHeaderTail() {
        removeTitleFilter();
        IHeaderTailManager iHeaderTailManager = (IHeaderTailManager) this.mNvSdkManager.getManagerService(8);
        MiTemplateResEffect miTemplateResEffect = this.mTemplateResEffect;
        if (miTemplateResEffect == null || iHeaderTailManager == null) {
            return;
        }
        iHeaderTailManager.setHeaderTail(true, miTemplateResEffect.getFolderPath(), this.mTemplateResEffect.getHeaderTailInfoEntity(), TemplateProfileUtils.getHeaderTailLabelFromTemplateKey(this.mTemplateLabel));
        if (this.mTemplateResEffect.getPermanentHeaderTailEntityList() == null) {
            return;
        }
        iHeaderTailManager.setPermanentEntityList(this.mTemplateResEffect.getPermanentHeaderTailEntityList());
        for (int i = 0; i < this.mTemplateResEffect.getPermanentHeaderTailEntityList().size(); i++) {
            iHeaderTailManager.setHeaderTail(true, this.mTemplateResEffect.getFolderPath(), this.mTemplateResEffect.getPermanentHeaderTailEntityList().get(i));
        }
    }

    @Override // com.miui.gallery.vlog.template.TemplateFilesManager
    public void buildAudio() {
        String musicPath = this.mTemplateResEffect.getMusicPath();
        if (!TextUtils.isEmpty(musicPath)) {
            IAudioManager iAudioManager = (IAudioManager) this.mNvSdkManager.getManagerService(2);
            iAudioManager.setVideoTrackVolumeGain(0.0f, 0.0f);
            iAudioManager.setAudioTrackVolumeGain(100.0f, 100.0f);
            iAudioManager.applyAudio(musicPath, this.mTemplateLabel);
        }
    }

    @Override // com.miui.gallery.vlog.template.TemplateFilesManager
    public void buildRatio() {
        IRatioManager iRatioManager = (IRatioManager) this.mNvSdkManager.getManagerService(10);
        if (iRatioManager != null) {
            iRatioManager.setLiveWindowRatio(this.mTemplateLabel, 0);
        }
    }

    public void removeTrackFilter() {
        IFilterManager iFilterManager = (IFilterManager) this.mNvSdkManager.getManagerService(0);
        if (iFilterManager != null) {
            iFilterManager.removeTrackFilter();
        }
    }

    @Override // com.miui.gallery.vlog.template.TemplateFilesManager
    public void removeTitleFilter() {
        IHeaderTailManager iHeaderTailManager = (IHeaderTailManager) this.mNvSdkManager.getManagerService(8);
        if (iHeaderTailManager != null) {
            iHeaderTailManager.removeHeaderTail();
            iHeaderTailManager.removePermanentHeaderTail();
        }
    }

    @Override // com.miui.gallery.vlog.template.TemplateFilesManager
    public void removeAudio() {
        MiTemplateResEffect miTemplateResEffect = this.mTemplateResEffect;
        if (miTemplateResEffect != null) {
            miTemplateResEffect.setMusicPath("");
        }
        IAudioManager iAudioManager = (IAudioManager) this.mNvSdkManager.getManagerService(2);
        if (iAudioManager != null) {
            iAudioManager.removeAudio();
            iAudioManager.setVideoTrackVolumeGain(100.0f, 100.0f);
        }
    }

    @Override // com.miui.gallery.vlog.template.TemplateFilesManager
    public void removeRatio() {
        IRatioManager iRatioManager = (IRatioManager) this.mNvSdkManager.getManagerService(10);
        if (iRatioManager != null) {
            iRatioManager.setLiveWindowRatio("", -1);
        }
    }

    /* loaded from: classes2.dex */
    public static class TransitionBuildInfo {
        public float duration;
        public float endSpeed;
        public float startSpeed;
        public String transitionName;
        public String transitionParam;
        public float trimIn;
        public float trimOut;

        public TransitionBuildInfo(float f, float f2, float f3, float f4, String str, String str2) {
            this.trimIn = f;
            this.trimOut = f2;
            this.startSpeed = f3;
            this.endSpeed = f4;
            this.duration = (f2 - f) / ((f3 + f4) / 2.0f);
            this.transitionName = str;
            this.transitionParam = str2;
        }
    }

    /* loaded from: classes2.dex */
    public static class AppendClipInfo {
        public float duration;
        public float endSpeed;
        public List<FilterBeanProcessed> filterList;
        public String path;
        public float startSpeed;
        public float totalDuration;
        public float trimIn;
        public float trimOut;

        public AppendClipInfo(String str, float f, float f2, float f3, float f4, float f5, List<FilterBeanProcessed> list) {
            this.path = str;
            this.totalDuration = f;
            this.trimIn = f2;
            this.trimOut = f3;
            this.duration = f3 - f2;
            this.startSpeed = f4;
            this.endSpeed = f5;
            this.filterList = list;
        }
    }
}
