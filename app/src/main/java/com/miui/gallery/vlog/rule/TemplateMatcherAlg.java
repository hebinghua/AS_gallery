package com.miui.gallery.vlog.rule;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.text.TextUtils;
import com.miui.gallery.assistant.manager.AnalyticFaceAndSceneManager;
import com.miui.gallery.assistant.model.MediaScene;
import com.miui.gallery.util.GsonUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.entity.VideoClip;
import com.miui.gallery.vlog.rule.bean.ClipEvalForm;
import com.miui.gallery.vlog.rule.bean.TemplateEvalForm;
import com.miui.gallery.vlog.rule.bean.TemplateSelectForm;
import com.miui.mediaeditor.api.GalleryProviderForMediaEditor;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

/* loaded from: classes2.dex */
public class TemplateMatcherAlg implements TemplateMatcher {
    public AssetManager mAssetManager;
    public boolean mCancel;
    public MatchTask mMatchTask;
    public TemplateSelectForm mTemplateSelectForm;
    public boolean mFresh = true;
    public Map<String, VideoInfo> mVideoInfoMap = new HashMap();
    public Random mRandom = new Random();

    public TemplateMatcherAlg(AssetManager assetManager) {
        this.mAssetManager = assetManager;
    }

    @Override // com.miui.gallery.vlog.rule.TemplateMatcher
    public void release() {
        cancel();
        this.mAssetManager = null;
        this.mTemplateSelectForm = null;
    }

    public final void cancel() {
        this.mCancel = true;
        MatchTask matchTask = this.mMatchTask;
        if (matchTask != null) {
            matchTask.removeOnTemplateMatchedCallback();
            this.mMatchTask.cancel(true);
        }
    }

    /* loaded from: classes2.dex */
    public static class MatchTask extends AsyncTask<Void, Void, MatchedTemplate> {
        public OnTemplateMatchedCallback onTemplateMatchedCallback;
        public List<String> paths;
        public String template;
        public TemplateMatcherAlg templateMatcher;

        public MatchTask(TemplateMatcherAlg templateMatcherAlg, String str, List<String> list, OnTemplateMatchedCallback onTemplateMatchedCallback) {
            this.template = str;
            this.paths = list;
            this.templateMatcher = templateMatcherAlg;
            this.onTemplateMatchedCallback = onTemplateMatchedCallback;
        }

        public void removeOnTemplateMatchedCallback() {
            this.onTemplateMatchedCallback = null;
        }

        @Override // android.os.AsyncTask
        public MatchedTemplate doInBackground(Void... voidArr) {
            if (TextUtils.isEmpty(this.template)) {
                return this.templateMatcher.analyticVideo(this.paths);
            }
            return this.templateMatcher.matchToTemplate(this.template, this.paths);
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(MatchedTemplate matchedTemplate) {
            super.onPostExecute((MatchTask) matchedTemplate);
            OnTemplateMatchedCallback onTemplateMatchedCallback = this.onTemplateMatchedCallback;
            if (onTemplateMatchedCallback != null) {
                onTemplateMatchedCallback.onTemplateMatched(matchedTemplate);
            }
        }
    }

    public final TemplateSelectForm parseTemplateSelectForm() {
        InputStreamReader inputStreamReader;
        AssetManager assetManager = this.mAssetManager;
        if (assetManager == null) {
            return null;
        }
        try {
            inputStreamReader = new InputStreamReader(assetManager.open("template_select_form/TemplateSelectForm.json"));
        } catch (IOException e) {
            e.printStackTrace();
            inputStreamReader = null;
        }
        if (inputStreamReader != null) {
            return (TemplateSelectForm) GsonUtils.fromJson(inputStreamReader, TemplateSelectForm.class);
        }
        return null;
    }

    @Override // com.miui.gallery.vlog.rule.TemplateMatcher
    public boolean matchTemplateAsync(String str, List<String> list, OnTemplateMatchedCallback onTemplateMatchedCallback) {
        if (!this.mFresh) {
            return false;
        }
        MatchTask matchTask = new MatchTask(this, str, list, onTemplateMatchedCallback);
        this.mMatchTask = matchTask;
        matchTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        this.mFresh = false;
        return true;
    }

    public final MatchedTemplate analyticVideo(List<String> list) {
        DefaultLogger.d("TemplateMatcherAlg", "analytic video");
        if (Util.isEmpty(list)) {
            return null;
        }
        DefaultLogger.d("TemplateMatcherAlg", "start video analytic");
        long currentTimeMillis = System.currentTimeMillis();
        for (int i = 0; i < list.size(); i++) {
            if (this.mCancel) {
                return null;
            }
            if (getVideoInfo(list.get(i)).getDurationMilli() <= 480000) {
                AnalyticFaceAndSceneManager.getInstance().getSceneTagListByPath(list.get(i), false, true);
            }
        }
        DefaultLogger.d("TemplateMatcherAlg", "video analytic consume time " + (System.currentTimeMillis() - currentTimeMillis));
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < list.size(); i2++) {
            String str = list.get(i2);
            VideoInfo extractVideoInfo = Util.extractVideoInfo(str);
            if (extractVideoInfo.getWidth() <= 0) {
                DefaultLogger.d("TemplateMatcherAlg", "%s is not valid video", str);
            } else {
                arrayList.add(new VideoClip(str, extractVideoInfo.getDuration(), 0L, extractVideoInfo.getDuration(), extractVideoInfo.getWidth(), extractVideoInfo.getHeight()));
            }
        }
        return new MatchedTemplate("vlog_template_none", arrayList);
    }

    public MatchedTemplate matchToTemplate(String str, List<String> list) {
        return matchToTemplateInner(str, list, false);
    }

    @Override // com.miui.gallery.vlog.rule.TemplateMatcher
    public MatchedTemplate matchToTemplateFromDB(String str, List<String> list) {
        return matchToTemplateInner(str, list, true);
    }

    public final MatchedTemplate matchToTemplateInner(String str, List<String> list, boolean z) {
        DefaultLogger.d("TemplateMatcherAlg", "match to template %s", str);
        TemplateEvalForm templateEvalForm = null;
        if (list == null || list.size() == 0) {
            return null;
        }
        DefaultLogger.d("TemplateMatcherAlg", "start video analytic");
        long currentTimeMillis = System.currentTimeMillis();
        List<List<MediaScene>> arrayList = new ArrayList<>();
        int i = 0;
        if (z) {
            arrayList = AnalyticFaceAndSceneManager.getInstance().getSceneTagListByPathInBacth(list, z, true);
            if (arrayList == null) {
                return null;
            }
            while (i < arrayList.size()) {
                if (GalleryProviderForMediaEditor.transferToOld((ArrayList) arrayList.get(i)) == null) {
                    arrayList.set(i, new ArrayList());
                }
                i++;
            }
        } else {
            while (i < list.size()) {
                if (this.mCancel) {
                    return null;
                }
                List<MediaScene> sceneTagListByPath = AnalyticFaceAndSceneManager.getInstance().getSceneTagListByPath(list.get(i), z, true);
                if (sceneTagListByPath == null) {
                    sceneTagListByPath = new ArrayList<>();
                }
                arrayList.add(GalleryProviderForMediaEditor.transferToOld((ArrayList) sceneTagListByPath));
                i++;
            }
        }
        DefaultLogger.d("TemplateMatcherAlg", "video analytic consume time " + (System.currentTimeMillis() - currentTimeMillis));
        List<Video> convertToVideo = convertToVideo(list, arrayList);
        if (Util.isEmpty(convertToVideo)) {
            DefaultLogger.d("TemplateMatcherAlg", "no valid video source");
            return createNoMatchTemplate(str, convertToVideo);
        }
        if (this.mTemplateSelectForm == null) {
            this.mTemplateSelectForm = parseTemplateSelectForm();
        }
        TemplateSelectForm templateSelectForm = this.mTemplateSelectForm;
        if (templateSelectForm == null) {
            DefaultLogger.d("TemplateMatcherAlg", "json parse error");
            return createNoMatchTemplate(str, convertToVideo);
        }
        for (TemplateEvalForm templateEvalForm2 : templateSelectForm.getTemplateEvalForms()) {
            if (templateEvalForm2.isEnable() && templateEvalForm2.getName().equals(str)) {
                templateEvalForm = templateEvalForm2;
            }
        }
        if (templateEvalForm == null) {
            return createNoMatchTemplate(str, convertToVideo);
        }
        EvaluatedTemplate matchAndEvaluateTemplate = matchAndEvaluateTemplate(templateEvalForm, convertToVideo, this.mTemplateSelectForm.getCategoryCandidates(), this.mTemplateSelectForm.getShootingCandidates());
        applyOutputRule(matchAndEvaluateTemplate);
        DefaultLogger.d("TemplateMatcherAlg", "match to template consume %d ,video count %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis), Integer.valueOf(list.size()));
        return convertToMatchedTemplate(matchAndEvaluateTemplate);
    }

    public final void applyOutputRule(EvaluatedTemplate evaluatedTemplate) {
        ArrayList arrayList = evaluatedTemplate.mMatchClips;
        if (evaluatedTemplate.mTemplateEvalForm.isOnlyRelevant()) {
            ArrayList arrayList2 = new ArrayList();
            for (VideoClipLocalVariable videoClipLocalVariable : arrayList) {
                if (videoClipLocalVariable.mVideoTagClip.getClassification() == evaluatedTemplate.mTemplateEvalForm.getRelevantClassification()) {
                    arrayList2.add(videoClipLocalVariable);
                }
            }
            if (arrayList2.size() > 0) {
                arrayList = arrayList2;
            }
        }
        int size = evaluatedTemplate.mTemplateEvalForm.getClipEvalForms().size();
        if (arrayList.size() < 8) {
            ArrayList arrayList3 = new ArrayList(arrayList);
            int i = 0;
            while (arrayList3.size() < Math.min(8, size)) {
                arrayList3.add((VideoClipLocalVariable) arrayList.get(i).clone());
                i = (i + 1) % arrayList.size();
            }
            arrayList = arrayList3;
        }
        if (arrayList.size() > size) {
            ArrayList arrayList4 = new ArrayList();
            for (int i2 = 0; i2 < size; i2++) {
                arrayList4.add(arrayList.get(i2));
            }
            arrayList = arrayList4;
        }
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            long duration = evaluatedTemplate.mTemplateEvalForm.getClipEvalForms().get(i3).getDuration();
            VideoClipLocalVariable videoClipLocalVariable2 = arrayList.get(i3);
            long j = videoClipLocalVariable2.mParent.mEndPoint;
            if (videoClipLocalVariable2.mVideoTagClip.getDuration() < duration) {
                long min = Math.min(videoClipLocalVariable2.mVideoTagClip.getInPoint() + duration, j);
                long max = Math.max(0L, min - duration);
                videoClipLocalVariable2.mVideoTagClip.setOutPoint(min);
                videoClipLocalVariable2.mVideoTagClip.setInPoint(max);
            } else if (videoClipLocalVariable2.mVideoTagClip.getDuration() > duration) {
                videoClipLocalVariable2.mVideoTagClip.setOutPoint(videoClipLocalVariable2.mVideoTagClip.getInPoint() + duration);
            }
        }
        evaluatedTemplate.mMatchClips = arrayList;
    }

    public final List<VideoTagClip> getClassificationVideoClips(List<MediaScene> list, long j) {
        if (list == null || list.size() == 0) {
            return new ArrayList();
        }
        ArrayList<VideoTagClip> arrayList = new ArrayList();
        for (MediaScene mediaScene : list) {
            VideoTagClip singleClip = VideoTagClip.getSingleClip(mediaScene, j);
            if (singleClip != null) {
                arrayList.add(singleClip);
            }
        }
        ArrayList arrayList2 = new ArrayList();
        for (VideoTagClip videoTagClip : arrayList) {
            VideoTagClip videoTagClip2 = null;
            Iterator it = arrayList2.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                VideoTagClip videoTagClip3 = (VideoTagClip) it.next();
                if (VideoTagClip.sameVideoContent(videoTagClip, videoTagClip3)) {
                    videoTagClip2 = videoTagClip3;
                    break;
                }
            }
            if (videoTagClip2 == null) {
                arrayList2.add(videoTagClip);
            } else if (Classification.compareImportance(videoTagClip.getClassification(), videoTagClip2.getClassification()) > 0) {
                arrayList2.remove(videoTagClip2);
                arrayList2.add(videoTagClip);
            }
        }
        return arrayList2;
    }

    public final List<VideoTagClip> getShootingVideoClips(List<MediaScene> list, long j) {
        if (list == null || list.size() == 0) {
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList();
        for (MediaScene mediaScene : list) {
            VideoTagClip singleClip = VideoTagClip.getSingleClip(mediaScene, j);
            if (singleClip != null) {
                arrayList.add(singleClip);
            }
        }
        return arrayList;
    }

    public final List<Video> convertToVideo(List<String> list, List<List<MediaScene>> list2) {
        VideoTagClip artificialVideoClip;
        TemplateMatcherAlg templateMatcherAlg = this;
        int min = Math.min(list2.size(), list.size());
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (i < min) {
            Video video = new Video();
            video.mPath = list.get(i);
            video.mVideoInfo = templateMatcherAlg.getVideoInfo(video.mPath);
            long durationMilli = video.mVideoInfo.getDurationMilli();
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = new ArrayList();
            for (MediaScene mediaScene : list2.get(i)) {
                if (Shooting.isShootingScene(mediaScene)) {
                    arrayList3.add(mediaScene);
                } else if (Classification.isClassificationScene(mediaScene)) {
                    arrayList2.add(mediaScene);
                }
            }
            List<VideoTagClip> classificationVideoClips = templateMatcherAlg.getClassificationVideoClips(arrayList2, durationMilli);
            List<VideoTagClip> shootingVideoClips = templateMatcherAlg.getShootingVideoClips(arrayList3, durationMilli);
            ArrayList<VideoTagClip> arrayList4 = new ArrayList();
            for (int i2 = 0; i2 < classificationVideoClips.size(); i2++) {
                int i3 = 0;
                while (i3 < shootingVideoClips.size()) {
                    VideoTagClip videoTagClip = classificationVideoClips.get(i2);
                    VideoTagClip videoTagClip2 = shootingVideoClips.get(i3);
                    int i4 = min;
                    VideoTagClip crossClip = VideoTagClip.getCrossClip(videoTagClip.getClassificationScene(), videoTagClip2.getShootingScene(), durationMilli);
                    if (crossClip != null) {
                        video.mCrossClips.add(crossClip);
                        arrayList4.add(videoTagClip);
                        arrayList4.add(videoTagClip2);
                    }
                    i3++;
                    min = i4;
                }
            }
            int i5 = min;
            for (VideoTagClip videoTagClip3 : arrayList4) {
                classificationVideoClips.remove(videoTagClip3);
                shootingVideoClips.remove(videoTagClip3);
            }
            video.mSingleClips.addAll(classificationVideoClips);
            video.mSingleClips.addAll(shootingVideoClips);
            if (video.mCrossClips.size() == 0 && video.mSingleClips.size() == 0 && (artificialVideoClip = VideoTagClip.getArtificialVideoClip(video.mPath, video.mVideoInfo, durationMilli)) != null) {
                video.mArtificialClips.add(artificialVideoClip);
            }
            for (VideoTagClip videoTagClip4 : video.mArtificialClips) {
                videoTagClip4.setWidth(video.mVideoInfo.getWidth());
                videoTagClip4.setHeight(video.mVideoInfo.getHeight());
            }
            for (VideoTagClip videoTagClip5 : video.mSingleClips) {
                videoTagClip5.setWidth(video.mVideoInfo.getWidth());
                videoTagClip5.setHeight(video.mVideoInfo.getHeight());
            }
            for (VideoTagClip videoTagClip6 : video.mCrossClips) {
                videoTagClip6.setWidth(video.mVideoInfo.getWidth());
                videoTagClip6.setHeight(video.mVideoInfo.getHeight());
            }
            if (video.mCrossClips.size() > 0 || video.mSingleClips.size() > 0 || video.mArtificialClips.size() > 0) {
                arrayList.add(video);
            }
            i++;
            templateMatcherAlg = this;
            min = i5;
        }
        return arrayList;
    }

    public final EvaluatedTemplate matchAndEvaluateTemplate(TemplateEvalForm templateEvalForm, List<Video> list, List<List<Integer>> list2, Map<String, List<Integer>> map) {
        List<ClipEvalForm> clipEvalForms = templateEvalForm.getClipEvalForms();
        ArrayList<VideoLocalVariable> arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            arrayList.add(new VideoLocalVariable(list.get(i)));
        }
        ArrayList arrayList2 = new ArrayList();
        for (int i2 = 0; i2 < clipEvalForms.size(); i2++) {
            ClipEvalForm clipEvalForm = clipEvalForms.get(i2);
            arrayList2.add(new VideoTagClipEvaluator(list2.get(clipEvalForm.getClassificationCandidate()), templateEvalForm.getClassificationScoreList(), map.get(clipEvalForm.getShootingCandidate()), templateEvalForm.getShootingScoreList()));
        }
        Comparator<VideoClipLocalVariable> timeScoreComparator = templateEvalForm.isTimeSensitive() ? new TimeScoreComparator() : new ScoreComparator();
        List<VideoClipLocalVariable> matchSlot = matchSlot(arrayList2, arrayList, timeScoreComparator);
        for (int i3 = 0; i3 < matchSlot.size(); i3++) {
            matchSlot.get(i3).mMatchedPosition = i3;
        }
        for (VideoLocalVariable videoLocalVariable : arrayList) {
            videoLocalVariable.buildRemainingAndMatched();
        }
        ArrayList arrayList3 = new ArrayList();
        PriorityQueue priorityQueue = new PriorityQueue(new Comparator<VideoLocalVariable>() { // from class: com.miui.gallery.vlog.rule.TemplateMatcherAlg.1
            @Override // java.util.Comparator
            public int compare(VideoLocalVariable videoLocalVariable2, VideoLocalVariable videoLocalVariable3) {
                VideoClipLocalVariable videoClipLocalVariable = (VideoClipLocalVariable) videoLocalVariable2.mMatchedClipQueue.peek();
                VideoClipLocalVariable videoClipLocalVariable2 = (VideoClipLocalVariable) videoLocalVariable3.mMatchedClipQueue.peek();
                int i4 = 0;
                int i5 = videoClipLocalVariable == null ? 0 : videoClipLocalVariable.mScore;
                if (videoClipLocalVariable2 != null) {
                    i4 = videoClipLocalVariable2.mScore;
                }
                return i5 - i4;
            }
        });
        for (int i4 = 0; i4 < arrayList.size(); i4++) {
            if (arrayList.get(i4).mMatchedClipQueue.size() == 0) {
                arrayList3.add(arrayList.get(i4));
            } else if (arrayList.get(i4).mMatchedClipQueue.size() > 1) {
                priorityQueue.add(arrayList.get(i4));
            }
        }
        while (arrayList3.size() > 0 && priorityQueue.size() > 0) {
            VideoLocalVariable videoLocalVariable2 = (VideoLocalVariable) arrayList3.remove(0);
            if (videoLocalVariable2.mRemainingClips.size() != 0) {
                VideoLocalVariable videoLocalVariable3 = (VideoLocalVariable) priorityQueue.poll();
                if (videoLocalVariable3 == null) {
                    break;
                }
                VideoClipLocalVariable videoClipLocalVariable = (VideoClipLocalVariable) videoLocalVariable3.mMatchedClipQueue.poll();
                if (videoClipLocalVariable != null) {
                    int i5 = videoClipLocalVariable.mMatchedPosition;
                    VideoTagClipEvaluator videoTagClipEvaluator = arrayList2.get(videoClipLocalVariable.mMatchedPosition);
                    for (VideoClipLocalVariable videoClipLocalVariable2 : videoLocalVariable2.mRemainingClips) {
                        videoClipLocalVariable2.mScore = videoTagClipEvaluator.evaluate(videoClipLocalVariable2.mVideoTagClip);
                    }
                    VideoClipLocalVariable videoClipLocalVariable3 = (VideoClipLocalVariable) Collections.max(videoLocalVariable2.mRemainingClips, timeScoreComparator);
                    videoClipLocalVariable3.mParent.addMatch(i5, videoClipLocalVariable3);
                    videoClipLocalVariable.mParent.removeMatch(videoClipLocalVariable);
                    matchSlot.set(i5, videoClipLocalVariable3);
                }
            }
        }
        if (templateEvalForm.isTimeSensitive()) {
            Collections.sort(matchSlot, new TimeComparator());
        }
        EvaluatedTemplate evaluatedTemplate = new EvaluatedTemplate();
        evaluatedTemplate.mScore = computeScore(matchSlot);
        evaluatedTemplate.mTemplateEvalForm = templateEvalForm;
        evaluatedTemplate.mMatchClips = matchSlot;
        return evaluatedTemplate;
    }

    public final List<VideoClipLocalVariable> matchSlot(List<VideoTagClipEvaluator> list, List<VideoLocalVariable> list2, Comparator<VideoClipLocalVariable> comparator) {
        ArrayList arrayList = new ArrayList();
        ArrayList<VideoClipLocalVariable> arrayList2 = new ArrayList();
        for (int i = 0; i < list2.size(); i++) {
            arrayList2.addAll(list2.get(i).mAllClips);
        }
        if (arrayList2.size() == 0) {
            return arrayList;
        }
        int size = list.size();
        for (int i2 = 0; i2 < size && arrayList2.size() != 0; i2++) {
            VideoTagClipEvaluator videoTagClipEvaluator = list.get(i2);
            for (VideoClipLocalVariable videoClipLocalVariable : arrayList2) {
                videoClipLocalVariable.mScore = videoTagClipEvaluator.evaluate(videoClipLocalVariable.mVideoTagClip);
            }
            VideoClipLocalVariable videoClipLocalVariable2 = (VideoClipLocalVariable) Collections.max(arrayList2, comparator);
            arrayList.add(videoClipLocalVariable2);
            arrayList2.remove(videoClipLocalVariable2);
        }
        return arrayList;
    }

    public final VideoInfo getVideoInfo(String str) {
        VideoInfo videoInfo = this.mVideoInfoMap.get(str);
        if (videoInfo == null) {
            VideoInfo extractVideoInfo = Util.extractVideoInfo(str);
            this.mVideoInfoMap.put(str, extractVideoInfo);
            return extractVideoInfo;
        }
        return videoInfo;
    }

    public final int computeScore(List<VideoClipLocalVariable> list) {
        int i = 0;
        for (int i2 = 0; i2 < list.size(); i2++) {
            i += list.get(i2).mScore;
        }
        return i;
    }

    public final List<VideoClip> convert(List<VideoTagClip> list) {
        ArrayList arrayList = new ArrayList();
        for (VideoTagClip videoTagClip : list) {
            arrayList.add(VideoTagClip.convert(videoTagClip));
        }
        return arrayList;
    }

    public final MatchedTemplate createNoMatchTemplate(String str, List<Video> list) {
        ArrayList arrayList = new ArrayList();
        for (Video video : list) {
            if (video.mCrossClips.size() > 0) {
                arrayList.addAll(video.mCrossClips);
            }
            if (video.mSingleClips.size() > 0) {
                arrayList.addAll(video.mSingleClips);
            }
            if (video.mArtificialClips.size() > 0) {
                arrayList.addAll(video.mArtificialClips);
            }
        }
        return new MatchedTemplate(str, convert(arrayList));
    }

    public final MatchedTemplate convertToMatchedTemplate(EvaluatedTemplate evaluatedTemplate) {
        ArrayList arrayList = new ArrayList();
        for (VideoClipLocalVariable videoClipLocalVariable : evaluatedTemplate.mMatchClips) {
            arrayList.add(videoClipLocalVariable.mVideoTagClip);
        }
        return new MatchedTemplate(evaluatedTemplate.mTemplateEvalForm.getName(), convert(arrayList));
    }

    /* loaded from: classes2.dex */
    public static class EvaluatedTemplate {
        public List<VideoClipLocalVariable> mMatchClips;
        public int mScore;
        public TemplateEvalForm mTemplateEvalForm;

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("template:");
            sb.append(this.mTemplateEvalForm.getName());
            sb.append(",score:");
            sb.append(this.mScore);
            sb.append(",video:[");
            if (!Util.isEmpty(this.mMatchClips)) {
                sb.append("\n");
                for (int i = 0; i < this.mMatchClips.size(); i++) {
                    sb.append(this.mMatchClips.get(i).toString());
                    sb.append("\n");
                }
            }
            sb.append("]");
            return sb.toString();
        }
    }

    /* loaded from: classes2.dex */
    public static class VideoLocalVariable {
        public List<VideoClipLocalVariable> mAllClips = new ArrayList();
        public long mEndPoint;
        public PriorityQueue<VideoClipLocalVariable> mMatchedClipQueue;
        public List<VideoClipLocalVariable> mRemainingClips;
        public Video mVideo;

        public VideoClipLocalVariable getVideoClipLocalVariable(VideoTagClip videoTagClip) {
            return new VideoClipLocalVariable(this, videoTagClip);
        }

        public VideoLocalVariable(Video video) {
            this.mVideo = video;
            this.mEndPoint = video.mVideoInfo.getDurationMilli();
            for (int i = 0; i < this.mVideo.mCrossClips.size(); i++) {
                this.mAllClips.add(getVideoClipLocalVariable((VideoTagClip) this.mVideo.mCrossClips.get(i)));
            }
            for (int i2 = 0; i2 < this.mVideo.mSingleClips.size(); i2++) {
                this.mAllClips.add(getVideoClipLocalVariable((VideoTagClip) this.mVideo.mSingleClips.get(i2)));
            }
            for (int i3 = 0; i3 < this.mVideo.mArtificialClips.size(); i3++) {
                this.mAllClips.add(getVideoClipLocalVariable((VideoTagClip) this.mVideo.mArtificialClips.get(i3)));
            }
            this.mRemainingClips = new ArrayList();
            this.mMatchedClipQueue = new PriorityQueue<>(new ScoreComparator());
        }

        public final void buildRemainingAndMatched() {
            this.mRemainingClips.clear();
            this.mMatchedClipQueue.clear();
            for (VideoClipLocalVariable videoClipLocalVariable : this.mAllClips) {
                if (videoClipLocalVariable.mMatchedPosition == -1) {
                    this.mRemainingClips.add(videoClipLocalVariable);
                } else {
                    this.mMatchedClipQueue.add(videoClipLocalVariable);
                }
            }
        }

        public void addMatch(int i, VideoClipLocalVariable videoClipLocalVariable) {
            videoClipLocalVariable.mMatchedPosition = i;
            this.mMatchedClipQueue.add(videoClipLocalVariable);
            this.mRemainingClips.remove(videoClipLocalVariable);
        }

        public void removeMatch(VideoClipLocalVariable videoClipLocalVariable) {
            videoClipLocalVariable.mMatchedPosition = -1;
            this.mMatchedClipQueue.remove(videoClipLocalVariable);
        }
    }

    /* loaded from: classes2.dex */
    public static class VideoClipLocalVariable implements Cloneable {
        public int mMatchedPosition;
        public VideoLocalVariable mParent;
        public int mScore;
        public VideoTagClip mVideoTagClip;

        public VideoClipLocalVariable(VideoLocalVariable videoLocalVariable, VideoTagClip videoTagClip) {
            this.mMatchedPosition = -1;
            this.mParent = videoLocalVariable;
            this.mVideoTagClip = videoTagClip;
        }

        public Object clone() {
            VideoClipLocalVariable videoClipLocalVariable;
            CloneNotSupportedException e;
            try {
                videoClipLocalVariable = (VideoClipLocalVariable) super.clone();
                try {
                    videoClipLocalVariable.mVideoTagClip = (VideoTagClip) videoClipLocalVariable.mVideoTagClip.clone();
                } catch (CloneNotSupportedException e2) {
                    e = e2;
                    e.printStackTrace();
                    return videoClipLocalVariable;
                }
            } catch (CloneNotSupportedException e3) {
                videoClipLocalVariable = null;
                e = e3;
            }
            return videoClipLocalVariable;
        }

        public String toString() {
            return String.format(Locale.US, "[video clip - path:%s,classification:%d,classificationTag:%d,shooting:%d,shootingTag:%d,inPoint:%d,outPoint:%d,score:%d]", this.mParent.mVideo.mPath, Integer.valueOf(this.mVideoTagClip.getClassification()), Integer.valueOf(this.mVideoTagClip.getClassificationTag()), Integer.valueOf(this.mVideoTagClip.getShooting()), Integer.valueOf(this.mVideoTagClip.getShootingTag()), Long.valueOf(this.mVideoTagClip.getInPoint()), Long.valueOf(this.mVideoTagClip.getOutPoint()), Integer.valueOf(this.mScore));
        }
    }

    /* loaded from: classes2.dex */
    public static class Video {
        public final List<VideoTagClip> mArtificialClips;
        public final List<VideoTagClip> mCrossClips;
        public String mPath;
        public final List<VideoTagClip> mSingleClips;
        public VideoInfo mVideoInfo;

        public Video() {
            this.mCrossClips = new ArrayList();
            this.mSingleClips = new ArrayList();
            this.mArtificialClips = new ArrayList();
        }
    }

    /* loaded from: classes2.dex */
    public static class ScoreComparator implements Comparator<VideoClipLocalVariable> {
        public ScoreComparator() {
        }

        @Override // java.util.Comparator
        public int compare(VideoClipLocalVariable videoClipLocalVariable, VideoClipLocalVariable videoClipLocalVariable2) {
            return videoClipLocalVariable.mScore != videoClipLocalVariable2.mScore ? videoClipLocalVariable.mScore - videoClipLocalVariable2.mScore : Long.compare(videoClipLocalVariable.mVideoTagClip.getDuration(), videoClipLocalVariable2.mVideoTagClip.getDuration());
        }
    }

    /* loaded from: classes2.dex */
    public static class TimeComparator implements Comparator<VideoClipLocalVariable> {
        public TimeComparator() {
        }

        @Override // java.util.Comparator
        public int compare(VideoClipLocalVariable videoClipLocalVariable, VideoClipLocalVariable videoClipLocalVariable2) {
            if (videoClipLocalVariable.mVideoTagClip.getTakenTime() == videoClipLocalVariable2.mVideoTagClip.getTakenTime()) {
                return 0;
            }
            return videoClipLocalVariable.mVideoTagClip.getTakenTime() - videoClipLocalVariable2.mVideoTagClip.getTakenTime() > 0 ? 1 : -1;
        }
    }

    /* loaded from: classes2.dex */
    public static class TimeScoreComparator implements Comparator<VideoClipLocalVariable> {
        public TimeScoreComparator() {
        }

        @Override // java.util.Comparator
        public int compare(VideoClipLocalVariable videoClipLocalVariable, VideoClipLocalVariable videoClipLocalVariable2) {
            return videoClipLocalVariable.mVideoTagClip.getTakenTime() != videoClipLocalVariable2.mVideoTagClip.getTakenTime() ? videoClipLocalVariable.mVideoTagClip.getTakenTime() > videoClipLocalVariable2.mVideoTagClip.getTakenTime() ? 1 : -1 : videoClipLocalVariable.mScore != videoClipLocalVariable2.mScore ? videoClipLocalVariable.mScore - videoClipLocalVariable2.mScore : Long.compare(videoClipLocalVariable.mVideoTagClip.getDuration(), videoClipLocalVariable2.mVideoTagClip.getDuration());
        }
    }
}
