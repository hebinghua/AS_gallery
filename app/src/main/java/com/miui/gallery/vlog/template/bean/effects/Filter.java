package com.miui.gallery.vlog.template.bean.effects;

/* loaded from: classes2.dex */
public class Filter {
    public long duration;
    public String filterLut;
    public String videoFxPackageId;

    public String getVideoFxPackageId() {
        return this.videoFxPackageId;
    }

    public void setVideoFxPackageId(String str) {
        this.videoFxPackageId = str;
    }

    public String getFilterLut() {
        return this.filterLut;
    }

    public void setFilterLut(String str) {
        this.filterLut = str;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long j) {
        this.duration = j * 1000;
    }
}
