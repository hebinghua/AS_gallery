package com.miui.gallery.vlog.template.bean.effects;

import android.text.TextUtils;
import com.miui.gallery.vlog.home.VlogConfig;
import com.miui.gallery.vlog.template.bean.ShotInfos;
import com.miui.gallery.vlog.template.bean.TemplateJson;
import com.miui.gallery.vlog.template.bean.TimelineFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class TemplateResEffect {
    public List<Caption> mCaptions = new ArrayList();
    public String mMusicPath;
    public List<ShotInfos> mShotInfos;
    public Filter mTimelineEndFilter;
    public Filter mTimelineFilter;
    public Filter mTimelineTitleFilter;

    public void parseJsonBean(TemplateJson templateJson) {
        this.mShotInfos = templateJson.getShotInfos();
        setCaption(templateJson);
        setTimeLineFilter(templateJson);
        setMusicPath(templateJson);
    }

    public List<ShotInfos> getShotInfos() {
        return this.mShotInfos;
    }

    public Filter getTimelineTitleFilter() {
        return this.mTimelineTitleFilter;
    }

    public Filter getTimelineFilter() {
        return this.mTimelineFilter;
    }

    public final void setMusicPath(TemplateJson templateJson) {
        StringBuilder sb = new StringBuilder();
        sb.append(VlogConfig.TEMPALTE_PATH);
        String str = File.separator;
        sb.append(str);
        sb.append(templateJson.getName());
        sb.append(str);
        sb.append(templateJson.getMusic());
        this.mMusicPath = sb.toString();
    }

    public String getMusicPath() {
        return this.mMusicPath;
    }

    public void setMusicPath(String str) {
        this.mMusicPath = str;
    }

    public final void setCaption(TemplateJson templateJson) {
        if (!isEmpty(templateJson.getTitleCaption())) {
            Caption caption = new Caption();
            caption.setCaptionPackageId(templateJson.getTitleCaption());
            caption.setDuration(templateJson.getTitleCaptionDuration());
            addCaption(caption);
        }
    }

    public final void addCaption(Caption caption) {
        if (!this.mCaptions.contains(caption)) {
            this.mCaptions.add(caption);
        }
    }

    public final void setTimeLineFilter(TemplateJson templateJson) {
        TimelineFilter timelineFilter;
        if (!isEmpty(templateJson.getTitleFilter())) {
            Filter filter = new Filter();
            String titleFilter = templateJson.getTitleFilter();
            filter.setDuration(templateJson.getTitleFilterDuration());
            filter.setVideoFxPackageId(titleFilter);
            this.mTimelineTitleFilter = filter;
        }
        if (!isEmpty(templateJson.getEndingFilter())) {
            Filter filter2 = new Filter();
            String endingFilter = templateJson.getEndingFilter();
            filter2.setDuration(templateJson.getEndingFilterLen());
            filter2.setVideoFxPackageId(endingFilter);
            this.mTimelineEndFilter = filter2;
        }
        List<TimelineFilter> timelineFilter2 = templateJson.getTimelineFilter();
        if (timelineFilter2 == null || timelineFilter2.isEmpty() || (timelineFilter = timelineFilter2.get(0)) == null) {
            return;
        }
        Filter filter3 = new Filter();
        filter3.setVideoFxPackageId(timelineFilter.getFilter());
        StringBuilder sb = new StringBuilder();
        sb.append(VlogConfig.TEMPALTE_PATH);
        String str = File.separator;
        sb.append(str);
        sb.append(templateJson.getName());
        sb.append(str);
        sb.append(timelineFilter.getFilterLut());
        filter3.setFilterLut(sb.toString());
        this.mTimelineFilter = filter3;
    }

    public void clear() {
        this.mCaptions.clear();
        this.mTimelineTitleFilter = null;
        this.mTimelineEndFilter = null;
        this.mTimelineFilter = null;
    }

    public final boolean isEmpty(CharSequence charSequence) {
        return TextUtils.isEmpty(charSequence);
    }
}
