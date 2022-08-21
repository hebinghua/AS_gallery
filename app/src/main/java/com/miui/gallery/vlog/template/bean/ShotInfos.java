package com.miui.gallery.vlog.template.bean;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/* loaded from: classes2.dex */
public class ShotInfos {
    public int duration;
    public String filter;
    public String fullTrackFilter;
    public String mainTrackFilter;
    public float oriDuration;
    public List<Speed> speed;
    @SerializedName("4kspeed")
    private List<Speed> speed4k;
    public List<SubTrackFilter> subTrackFilter;
    public String trans;

    public String getTrans() {
        return this.trans;
    }

    public int getDuration() {
        return this.duration;
    }

    public float getOriDuration() {
        float f = this.oriDuration;
        return f > 0.0f ? f : this.duration;
    }

    public String getFilter() {
        return this.filter;
    }

    public String getMainTrackFilter() {
        return this.mainTrackFilter;
    }

    public List<SubTrackFilter> getSubTrackFilter() {
        return this.subTrackFilter;
    }

    public String getFullTrackFilter() {
        return this.fullTrackFilter;
    }

    public List<Speed> getSpeed() {
        return this.speed;
    }

    public List<Speed> getSpeed4k() {
        return this.speed4k;
    }
}
