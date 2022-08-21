package com.miui.gallery.editor.photo.core;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes2.dex */
public abstract class RenderData implements Parcelable {
    public Effect mType;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean isVideo() {
        return false;
    }

    public RenderData onMerge(RenderData renderData) {
        return null;
    }

    public void onRelease() {
    }

    public RenderData() {
    }

    public final Effect getType() {
        return this.mType;
    }

    public final RenderData merge(RenderData renderData) {
        RenderData onMerge = onMerge(renderData);
        if (onMerge != null) {
            onMerge.mType = this.mType;
        }
        return onMerge;
    }

    public final void release() {
        onRelease();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.mType, i);
    }

    public RenderData(Parcel parcel) {
        this.mType = (Effect) parcel.readParcelable(Effect.class.getClassLoader());
    }
}
