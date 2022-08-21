package com.miui.gallery.editor.photo.origin;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.utils.parcelable.ParcelableGenericUtils;
import java.util.List;

/* loaded from: classes2.dex */
public class OriginRenderData implements Parcelable {
    public static final Parcelable.Creator<OriginRenderData> CREATOR = new Parcelable.Creator<OriginRenderData>() { // from class: com.miui.gallery.editor.photo.origin.OriginRenderData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public OriginRenderData mo914createFromParcel(Parcel parcel) {
            return new OriginRenderData(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public OriginRenderData[] mo915newArray(int i) {
            return new OriginRenderData[i];
        }
    };
    public Bundle mBundle;
    public Uri mOut;
    public List<RenderData> mRenderDataList;
    public Uri mSource;
    public boolean mWithWatermark;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public OriginRenderData(List<RenderData> list, Uri uri, Uri uri2, Bundle bundle, boolean z) {
        this.mRenderDataList = list;
        this.mSource = uri;
        this.mOut = uri2;
        this.mBundle = bundle;
        this.mWithWatermark = z;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        ParcelableGenericUtils.writeList(parcel, i, this.mRenderDataList);
        parcel.writeParcelable(this.mSource, i);
        parcel.writeParcelable(this.mOut, i);
        parcel.writeParcelable(this.mBundle, i);
        parcel.writeByte(this.mWithWatermark ? (byte) 1 : (byte) 0);
    }

    public OriginRenderData(Parcel parcel) {
        this.mRenderDataList = ParcelableGenericUtils.readList(parcel);
        this.mSource = (Uri) parcel.readParcelable(Uri.class.getClassLoader());
        this.mOut = (Uri) parcel.readParcelable(Uri.class.getClassLoader());
        this.mBundle = (Bundle) parcel.readParcelable(Bundle.class.getClassLoader());
        this.mWithWatermark = parcel.readByte() != 0;
    }
}
