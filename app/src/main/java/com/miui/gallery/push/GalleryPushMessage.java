package com.miui.gallery.push;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.nexstreaming.nexeditorsdk.nexExportFormat;

/* loaded from: classes2.dex */
public class GalleryPushMessage implements Parcelable {
    public static final Parcelable.Creator<GalleryPushMessage> CREATOR = new Parcelable.Creator<GalleryPushMessage>() { // from class: com.miui.gallery.push.GalleryPushMessage.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public GalleryPushMessage mo1250createFromParcel(Parcel parcel) {
            return new GalleryPushMessage(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public GalleryPushMessage[] mo1251newArray(int i) {
            return new GalleryPushMessage[i];
        }
    };
    @SerializedName("businessModule")
    private String mBusinessModule;
    @SerializedName("data")
    private String mData;
    @SerializedName("messageScope")
    private String mMessageScope;
    @SerializedName(nexExportFormat.TAG_FORMAT_TAG)
    private long mTag;
    @SerializedName(nexExportFormat.TAG_FORMAT_TYPE)
    private String mType;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public static GalleryPushMessage fromJson(String str) {
        try {
            return (GalleryPushMessage) new Gson().fromJson(str, (Class<Object>) GalleryPushMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getData() {
        return this.mData;
    }

    public long getTag() {
        return this.mTag;
    }

    public String getType() {
        return this.mType;
    }

    public String getBusinessModule() {
        return this.mBusinessModule;
    }

    public String getMessageScope() {
        return this.mMessageScope;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mType);
        parcel.writeString(this.mBusinessModule);
        parcel.writeString(this.mData);
        parcel.writeLong(this.mTag);
        parcel.writeString(this.mMessageScope);
    }

    public GalleryPushMessage() {
    }

    public GalleryPushMessage(Parcel parcel) {
        this.mType = parcel.readString();
        this.mBusinessModule = parcel.readString();
        this.mData = parcel.readString();
        this.mTag = parcel.readLong();
        this.mMessageScope = parcel.readString();
    }
}
