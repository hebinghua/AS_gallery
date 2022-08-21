package com.miui.gallery.vlog.template.bean;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/* loaded from: classes2.dex */
public class ClipInfo {
    public float duration;
    public List<Filter> filterList;
    public float oriDuration;
    public List<Speed> speed;
    @SerializedName("4kspeed")
    private List<Speed> speed4k;
    public List<SubTrackFilter> subTrackFilter;
    public Filter transition;

    public float getDuration() {
        return this.duration;
    }

    public float getOriDuration() {
        float f = this.oriDuration;
        return f > 0.0f ? f : this.duration;
    }

    public void setOriDuration(float f) {
        this.oriDuration = f;
    }

    public Filter getTransition() {
        return this.transition;
    }

    public List<Filter> getFilterList() {
        return this.filterList;
    }

    public List<Speed> getSpeed() {
        return this.speed;
    }

    public List<Speed> getSpeed4k() {
        return this.speed4k;
    }

    public List<SubTrackFilter> getSubTrackFilter() {
        return this.subTrackFilter;
    }
}
