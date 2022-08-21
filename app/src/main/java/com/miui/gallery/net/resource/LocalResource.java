package com.miui.gallery.net.resource;

/* loaded from: classes2.dex */
public class LocalResource extends Resource {
    public String assetId;
    public String assetName;
    public int imageId;
    public int index;
    public int isInternational;
    public String nameKey;
    public long size;

    public boolean isTemplate() {
        return false;
    }

    public void setUnZipPath(String str) {
    }

    public LocalResource(int i, String str) {
        this.type = str;
        this.imageId = i;
        this.isInternational = 0;
    }

    public LocalResource() {
    }

    public int getIndex() {
        return this.index;
    }

    public boolean isInternational() {
        return this.isInternational == 0;
    }

    public long getId() {
        return this.id;
    }

    public String getLabel() {
        return this.label;
    }

    public String getNameKey() {
        return this.nameKey;
    }
}
