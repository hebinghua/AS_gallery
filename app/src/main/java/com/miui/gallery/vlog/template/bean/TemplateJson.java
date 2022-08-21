package com.miui.gallery.vlog.template.bean;

import com.miui.gallery.vlog.tools.VlogUtils;
import java.util.List;

/* loaded from: classes2.dex */
public class TemplateJson {
    public String endingFilter;
    public long endingFilterLen;
    public String music;
    public String name;
    public List<ShotInfos> shotInfos;
    public List<TimelineFilter> timelineFilter;
    public String titleCaption;
    public int titleCaptionDuration;
    public String titleFilter;
    public long titleFilterDuration;

    public String getName() {
        return VlogUtils.getFormatedStr(this.name);
    }

    public String getMusic() {
        return this.music;
    }

    public String getTitleCaption() {
        return this.titleCaption;
    }

    public int getTitleCaptionDuration() {
        return this.titleCaptionDuration;
    }

    public String getEndingFilter() {
        return this.endingFilter;
    }

    public long getEndingFilterLen() {
        return this.endingFilterLen;
    }

    public List<TimelineFilter> getTimelineFilter() {
        return this.timelineFilter;
    }

    public List<ShotInfos> getShotInfos() {
        return this.shotInfos;
    }

    public String getTitleFilter() {
        return this.titleFilter;
    }

    public long getTitleFilterDuration() {
        return this.titleFilterDuration;
    }
}
