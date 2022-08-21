package com.miui.gallery.editor.photo.core.common.model;

import android.os.Parcel;
import com.miui.gallery.editor.photo.core.Metadata;

/* loaded from: classes2.dex */
public abstract class SkyData extends Metadata {
    public abstract boolean dependOnSegment();

    public abstract int getDownloadState();

    public abstract int getIcon();

    public abstract String getMaterialName();

    public abstract int getParentCategory();

    public abstract int getProgress();

    public abstract boolean isDynamic();

    public abstract boolean isFromCloud();

    public abstract boolean isNone();

    public abstract void resetProgress();

    public abstract void setDownloadState(int i);

    public abstract void setFromCloud(boolean z);

    public abstract void setProgress(int i);

    public SkyData(short s, String str) {
        super(s, str);
    }

    public SkyData(Parcel parcel) {
        super(parcel);
    }
}
