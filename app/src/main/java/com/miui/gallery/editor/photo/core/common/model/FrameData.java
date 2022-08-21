package com.miui.gallery.editor.photo.core.common.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.Metadata;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class FrameData extends Metadata {
    public static final Parcelable.Creator<FrameData> CREATOR;
    public static Map<String, Integer> sStringResMap;
    public boolean cinemaStyle;
    public final int height;
    public String icon;
    public int iconHPadding;
    public final float iconRatio;
    public int iconVPadding;
    public final String talkbackName;
    public final int width;

    @Override // com.miui.gallery.editor.photo.core.Metadata, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    static {
        HashMap hashMap = new HashMap();
        sStringResMap = hashMap;
        hashMap.put("cinema.json", Integer.valueOf((int) R.string.filter_category_movie));
        CREATOR = new Parcelable.Creator<FrameData>() { // from class: com.miui.gallery.editor.photo.core.common.model.FrameData.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public FrameData mo780createFromParcel(Parcel parcel) {
                return new FrameData(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public FrameData[] mo781newArray(int i) {
                return new FrameData[i];
            }
        };
    }

    public FrameData(short s, String str, int i, int i2, String str2, float f, int i3, int i4, boolean z, String str3) {
        super(s, str);
        this.width = i;
        this.height = i2;
        this.talkbackName = str2;
        this.iconRatio = f;
        this.iconHPadding = i3;
        this.iconVPadding = i4;
        this.cinemaStyle = z;
        this.icon = str3;
    }

    public String toString() {
        Integer num = sStringResMap.get(this.name);
        return num == null ? String.format("%s:%s", Integer.valueOf(this.width), Integer.valueOf(this.height)) : GalleryApp.sGetAndroidContext().getResources().getString(num.intValue());
    }

    @Override // com.miui.gallery.editor.photo.core.Metadata, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(this.width);
        parcel.writeInt(this.height);
        parcel.writeString(this.talkbackName);
        parcel.writeFloat(this.iconRatio);
        parcel.writeInt(this.iconHPadding);
        parcel.writeInt(this.iconVPadding);
        parcel.writeString(this.icon);
        parcel.writeByte(this.cinemaStyle ? (byte) 1 : (byte) 0);
    }

    public FrameData(Parcel parcel) {
        super(parcel);
        this.width = parcel.readInt();
        this.height = parcel.readInt();
        this.talkbackName = parcel.readString();
        this.iconRatio = parcel.readFloat();
        this.iconHPadding = parcel.readInt();
        this.iconVPadding = parcel.readInt();
        this.icon = parcel.readString();
        this.cinemaStyle = parcel.readByte() != 1 ? false : true;
    }
}
