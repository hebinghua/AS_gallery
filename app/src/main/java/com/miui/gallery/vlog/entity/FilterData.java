package com.miui.gallery.vlog.entity;

import com.miui.gallery.vlog.R$drawable;
import com.miui.gallery.vlog.filter.FilterResource;

/* loaded from: classes2.dex */
public class FilterData extends FilterResource {
    public String mPath;
    public int mProgress = 80;
    public String mTemplateKey;

    public static FilterData getDefaultItem() {
        FilterData filterData = new FilterData();
        filterData.nameKey = "vlog_filter_default";
        filterData.imageId = R$drawable.vlog_filter_default;
        filterData.type = "type_none";
        filterData.mNameResId = FilterResource.sFilterNames.get("vlog_filter_default").intValue();
        return filterData;
    }

    public boolean isNone() {
        return "type_none".equalsIgnoreCase(this.type);
    }

    public boolean isExtra() {
        return "type_extra".equalsIgnoreCase(this.type);
    }

    public void setProgress(int i) {
        this.mProgress = i;
    }

    public int getProgress() {
        return this.mProgress;
    }

    public String getPath() {
        return this.mPath;
    }

    public void setPath(String str) {
        this.mPath = str;
    }

    public String getTemplateKey() {
        return this.mTemplateKey;
    }

    public void setTemplateKey(String str) {
        this.mTemplateKey = str;
    }
}
