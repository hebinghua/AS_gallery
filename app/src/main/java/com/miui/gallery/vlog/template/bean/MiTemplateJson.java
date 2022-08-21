package com.miui.gallery.vlog.template.bean;

import com.miui.gallery.vlog.caption.entity.HeaderTailInfoEntity;
import com.miui.gallery.vlog.caption.entity.HeaderTailPermanentEntity;
import java.util.List;

/* loaded from: classes2.dex */
public class MiTemplateJson {
    public List<ClipInfo> clipList;
    public HeaderTailInfoEntity headerFilter;
    public List<HeaderTailPermanentEntity> headerPermanentFilters;
    public String music;
    public String name;
    public List<Filter> trackFilter;

    public String getName() {
        return this.name;
    }

    public String getMusic() {
        return this.music;
    }

    public List<Filter> getTrackFilter() {
        return this.trackFilter;
    }

    public List<ClipInfo> getClipList() {
        return this.clipList;
    }

    public HeaderTailInfoEntity getHeaderFilter() {
        return this.headerFilter;
    }

    public List<HeaderTailPermanentEntity> getHeaderPermanentFilters() {
        return this.headerPermanentFilters;
    }
}
