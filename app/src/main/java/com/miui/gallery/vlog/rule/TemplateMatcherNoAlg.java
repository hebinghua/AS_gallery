package com.miui.gallery.vlog.rule;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.text.TextUtils;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.spi.AbstractComponentTracker;
import com.miui.gallery.util.GsonUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.entity.VideoClip;
import com.miui.gallery.vlog.rule.bean.TemplateEvalForm;
import com.miui.gallery.vlog.rule.bean.TemplateSelectForm;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/* loaded from: classes2.dex */
public class TemplateMatcherNoAlg implements TemplateMatcher {
    public static final List<String> sRandomTemplates;
    public AssetManager mAssetManager;
    public MatchTask mMatchTask;
    public TemplateSelectForm mTemplateSelectForm;
    public boolean mFresh = true;
    public Random mRandom = new Random();

    static {
        ArrayList arrayList = new ArrayList();
        sRandomTemplates = arrayList;
        arrayList.add("vlog_template_memory");
        arrayList.add("vlog_template_mandiao");
        arrayList.add("vlog_template_rixi");
        arrayList.add("vlog_template_summer");
        arrayList.add("vlog_template_fugu");
        arrayList.add("vlog_template_city");
        arrayList.add("vlog_template_kuxuan");
        arrayList.add("vlog_template_travel");
    }

    public TemplateMatcherNoAlg(AssetManager assetManager) {
        this.mAssetManager = assetManager;
    }

    @Override // com.miui.gallery.vlog.rule.TemplateMatcher
    public void release() {
        cancel();
        this.mAssetManager = null;
        this.mTemplateSelectForm = null;
    }

    public final void cancel() {
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
        public TemplateMatcherNoAlg templateMatcher;

        public MatchTask(TemplateMatcherNoAlg templateMatcherNoAlg, String str, List<String> list, OnTemplateMatchedCallback onTemplateMatchedCallback) {
            this.template = str;
            this.paths = list;
            this.templateMatcher = templateMatcherNoAlg;
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
        DefaultLogger.d("TemplateMatcherNoAlg", "analytic video");
        if (Util.isEmpty(list)) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (String str : list) {
            VideoInfo extractVideoInfo = Util.extractVideoInfo(str);
            if (extractVideoInfo.getWidth() <= 0 || extractVideoInfo.getHeight() <= 0) {
                DefaultLogger.d("TemplateMatcherNoAlg", "%s is not valid video", str);
            } else {
                arrayList.add(new VideoClip(str, extractVideoInfo.getDuration(), 0L, extractVideoInfo.getDuration(), extractVideoInfo.getWidth(), extractVideoInfo.getHeight()));
            }
        }
        return new MatchedTemplate("vlog_template_none", arrayList);
    }

    public final List<VideoClip> convert(List<VideoClipForNoAlg> list) {
        ArrayList arrayList = new ArrayList();
        for (VideoClipForNoAlg videoClipForNoAlg : list) {
            arrayList.add(VideoClipForNoAlg.convert(videoClipForNoAlg));
        }
        return arrayList;
    }

    public MatchedTemplate matchToTemplate(String str, List<String> list) {
        return matchToTemplateInner(str, list);
    }

    @Override // com.miui.gallery.vlog.rule.TemplateMatcher
    public MatchedTemplate matchToTemplateFromDB(String str, List<String> list) {
        return matchToTemplateInner(str, list);
    }

    public final MatchedTemplate matchToTemplateInner(String str, List<String> list) {
        DefaultLogger.d("TemplateMatcherNoAlg", "match to template %s", str);
        if (list == null || list.size() == 0) {
            return null;
        }
        if (this.mTemplateSelectForm == null) {
            this.mTemplateSelectForm = parseTemplateSelectForm();
        }
        if (this.mTemplateSelectForm == null) {
            DefaultLogger.d("TemplateMatcherNoAlg", "json parse error");
            return null;
        }
        List<Video> convertToVideo = convertToVideo(list);
        if (Util.isEmpty(convertToVideo)) {
            DefaultLogger.d("TemplateMatcherNoAlg", "no valid video source");
            return null;
        }
        TemplateEvalForm templateEvalForm = null;
        for (TemplateEvalForm templateEvalForm2 : this.mTemplateSelectForm.getTemplateEvalForms()) {
            if (templateEvalForm2.isEnable() && templateEvalForm2.getName().equals(str)) {
                templateEvalForm = templateEvalForm2;
            }
        }
        if (templateEvalForm == null) {
            return null;
        }
        long currentTimeMillis = System.currentTimeMillis();
        List<VideoClipForNoAlg> outputClips = getOutputClips(convertToVideo, templateEvalForm);
        DefaultLogger.d("TemplateMatcherNoAlg", "apply rule consume time %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        return new MatchedTemplate(templateEvalForm.getName(), convert(outputClips));
    }

    public final List<VideoClipForNoAlg> getOutputClips(List<Video> list, TemplateEvalForm templateEvalForm) {
        int i;
        ArrayList arrayList = new ArrayList();
        Iterator<Video> it = list.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            arrayList.add(it.next().getClip(0));
        }
        for (Video video : list) {
            VideoClipForNoAlg clip = video.getClip(1);
            if (clip != null) {
                arrayList.add(clip);
            }
        }
        for (Video video2 : list) {
            VideoClipForNoAlg clip2 = video2.getClip(2);
            if (clip2 != null) {
                arrayList.add(clip2);
            }
        }
        int size = templateEvalForm.getClipEvalForms().size();
        if (arrayList.size() < 8) {
            ArrayList arrayList2 = new ArrayList(arrayList);
            int i2 = 0;
            while (arrayList2.size() < Math.min(8, size)) {
                arrayList2.add((VideoClipForNoAlg) arrayList.get(i2));
                i2 = (i2 + 1) % arrayList.size();
            }
            arrayList = arrayList2;
        }
        if (arrayList.size() > size) {
            ArrayList arrayList3 = new ArrayList();
            for (int i3 = 0; i3 < size; i3++) {
                arrayList3.add((VideoClipForNoAlg) arrayList.get(i3));
            }
            arrayList = arrayList3;
        }
        for (i = 0; i < arrayList.size(); i++) {
            long duration = templateEvalForm.getClipEvalForms().get(i).getDuration();
            VideoClipForNoAlg videoClipForNoAlg = (VideoClipForNoAlg) arrayList.get(i);
            if (videoClipForNoAlg.getDuration() < duration) {
                long min = Math.min(videoClipForNoAlg.getInPoint() + duration, videoClipForNoAlg.getOutPoint());
                long max = Math.max(0L, min - duration);
                videoClipForNoAlg.setOutPoint(min);
                videoClipForNoAlg.setInPoint(max);
            } else if (videoClipForNoAlg.getDuration() > duration) {
                videoClipForNoAlg.setOutPoint(videoClipForNoAlg.getInPoint() + duration);
            }
        }
        return arrayList;
    }

    public final List<Video> convertToVideo(List<String> list) {
        if (list == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (String str : list) {
            Video create = Video.create(str);
            if (create != null) {
                arrayList.add(create);
            }
        }
        return arrayList;
    }

    /* loaded from: classes2.dex */
    public static class Video {
        public List<VideoClipForNoAlg> mClips;
        public long mDuration;
        public int mHeight;
        public String mPath;
        public int mWidth;

        public static Video create(String str) {
            if (str == null || str.equals("")) {
                return null;
            }
            VideoInfo extractVideoInfo = Util.extractVideoInfo(str);
            if (extractVideoInfo.getWidth() > 0) {
                return new Video(str, extractVideoInfo.getWidth(), extractVideoInfo.getHeight(), extractVideoInfo.getDurationMilli());
            }
            return null;
        }

        public Video(String str, int i, int i2, long j) {
            ArrayList arrayList = new ArrayList();
            this.mClips = arrayList;
            this.mPath = str;
            this.mWidth = i;
            this.mHeight = i2;
            this.mDuration = j;
            if (j < 1000) {
                arrayList.add(new VideoClipForNoAlg(str, i, i2, j, 0L, j));
            } else if (j < 5000) {
                arrayList.add(new VideoClipForNoAlg(str, i, i2, j, 500L, j));
            } else if (j < AbstractComponentTracker.LINGERING_TIMEOUT) {
                long j2 = j / 2;
                arrayList.add(new VideoClipForNoAlg(str, i, i2, j, 500L, j2));
                List<VideoClipForNoAlg> list = this.mClips;
                int i3 = this.mWidth;
                int i4 = this.mHeight;
                long j3 = this.mDuration;
                list.add(new VideoClipForNoAlg(str, i3, i4, j3, j2, j3));
            } else {
                arrayList.add(new VideoClipForNoAlg(str, i, i2, j, 500L, j / 3));
                List<VideoClipForNoAlg> list2 = this.mClips;
                int i5 = this.mWidth;
                int i6 = this.mHeight;
                long j4 = this.mDuration;
                list2.add(new VideoClipForNoAlg(str, i5, i6, j4, j4 / 3, (j4 * 2) / 3));
                List<VideoClipForNoAlg> list3 = this.mClips;
                int i7 = this.mWidth;
                int i8 = this.mHeight;
                long j5 = this.mDuration;
                list3.add(new VideoClipForNoAlg(str, i7, i8, j5, (2 * j5) / 3, j5));
            }
        }

        public VideoClipForNoAlg getClip(int i) {
            if (i < 0 || i >= this.mClips.size()) {
                return null;
            }
            return this.mClips.get(i);
        }
    }

    /* loaded from: classes2.dex */
    public static class VideoClipForNoAlg {
        public int mHeight;
        public long mInPoint;
        public long mOutPoint;
        public String mPath;
        public long mWholeDuration;
        public int mWidth;

        public long getInPoint() {
            return this.mInPoint;
        }

        public long getOutPoint() {
            return this.mOutPoint;
        }

        public String getPath() {
            return this.mPath;
        }

        public int getWidth() {
            return this.mWidth;
        }

        public int getHeight() {
            return this.mHeight;
        }

        public long getDuration() {
            return this.mOutPoint - this.mInPoint;
        }

        public void setInPoint(long j) {
            this.mInPoint = j;
        }

        public void setOutPoint(long j) {
            this.mOutPoint = j;
        }

        public long getTotalDuration() {
            return this.mWholeDuration;
        }

        public VideoClipForNoAlg(String str, int i, int i2, long j, long j2, long j3) {
            this.mPath = str;
            this.mWidth = i;
            this.mHeight = i2;
            this.mWholeDuration = j;
            this.mInPoint = j2;
            this.mOutPoint = j3;
        }

        public static VideoClip convert(VideoClipForNoAlg videoClipForNoAlg) {
            return new VideoClip(videoClipForNoAlg.getPath(), videoClipForNoAlg.getTotalDuration() * 1000, videoClipForNoAlg.getInPoint() * 1000, videoClipForNoAlg.getOutPoint() * 1000, videoClipForNoAlg.getWidth(), videoClipForNoAlg.getHeight());
        }

        public String toString() {
            return "VideoClip{mTotalDuration=" + this.mWholeDuration + "mInPoint=" + this.mInPoint + ", mOutPoint=" + this.mOutPoint + ", mPath='" + this.mPath + CoreConstants.SINGLE_QUOTE_CHAR + ", mWidth=" + this.mWidth + ", mHeight=" + this.mHeight + '}';
        }
    }
}
