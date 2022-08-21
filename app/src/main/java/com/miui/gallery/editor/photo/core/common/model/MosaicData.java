package com.miui.gallery.editor.photo.core.common.model;

import android.content.res.Resources;
import android.os.Parcel;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.editor.photo.core.Metadata;

/* loaded from: classes2.dex */
public abstract class MosaicData extends Metadata {
    public final String iconPath;
    public final String talkbackName;

    @Override // com.miui.gallery.editor.photo.core.Metadata, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public MosaicData(short s, String str, String str2, String str3) {
        super(s, str);
        this.iconPath = str2;
        this.talkbackName = str3;
    }

    public String getTalkbackName() {
        int identifier;
        Resources resources = GalleryApp.sGetAndroidContext().getResources();
        if (this.talkbackName.startsWith("@string/") && (identifier = resources.getIdentifier(this.talkbackName, "string", GalleryApp.sGetAndroidContext().getPackageName())) != 0) {
            return resources.getString(identifier);
        }
        return this.talkbackName;
    }

    @Override // com.miui.gallery.editor.photo.core.Metadata, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(this.iconPath);
        parcel.writeString(this.talkbackName);
    }

    public MosaicData(Parcel parcel) {
        super(parcel);
        this.iconPath = parcel.readString();
        this.talkbackName = parcel.readString();
    }
}
