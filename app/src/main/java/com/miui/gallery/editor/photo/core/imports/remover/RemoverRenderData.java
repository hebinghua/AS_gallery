package com.miui.gallery.editor.photo.core.imports.remover;

import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.editor.photo.core.RenderData;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class RemoverRenderData extends RenderData {
    public static final Parcelable.Creator<RemoverRenderData> CREATOR = new Parcelable.Creator<RemoverRenderData>() { // from class: com.miui.gallery.editor.photo.core.imports.remover.RemoverRenderData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public RemoverRenderData mo851createFromParcel(Parcel parcel) {
            return new RemoverRenderData(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public RemoverRenderData[] mo852newArray(int i) {
            return new RemoverRenderData[i];
        }
    };
    public final List<RemoverPaintData> mPaintData;

    @Override // com.miui.gallery.editor.photo.core.RenderData, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public RemoverRenderData(List<RemoverPaintData> list) {
        this.mPaintData = list;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderData
    public void onRelease() {
        for (RemoverPaintData removerPaintData : this.mPaintData) {
            RemoverNNFData removerNNFData = removerPaintData.mRemoverNNFData;
            if (removerNNFData != null) {
                removerNNFData.releaseMemoryFile();
            }
        }
    }

    @Override // com.miui.gallery.editor.photo.core.RenderData, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeList(this.mPaintData);
    }

    public RemoverRenderData(Parcel parcel) {
        super(parcel);
        ArrayList arrayList = new ArrayList();
        this.mPaintData = arrayList;
        parcel.readList(arrayList, RemoverPaintData.class.getClassLoader());
    }
}
