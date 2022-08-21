package com.miui.gallery.editor.photo.core.imports.remover2;

import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.editor.photo.core.RenderData;
import java.util.List;

/* loaded from: classes2.dex */
public class Remover2RenderData extends RenderData {
    public static final Parcelable.Creator<Remover2PaintData> CREATOR = new Parcelable.Creator<Remover2PaintData>() { // from class: com.miui.gallery.editor.photo.core.imports.remover2.Remover2RenderData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public Remover2PaintData mo862createFromParcel(Parcel parcel) {
            return new Remover2PaintData(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public Remover2PaintData[] mo863newArray(int i) {
            return new Remover2PaintData[i];
        }
    };
    public final List<Remover2PaintData> mPaintData;

    @Override // com.miui.gallery.editor.photo.core.RenderData, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Remover2RenderData(List<Remover2PaintData> list) {
        this.mPaintData = list;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderData
    public void onRelease() {
        for (Remover2PaintData remover2PaintData : this.mPaintData) {
            Remover2NNFData remover2NNFData = remover2PaintData.mRemoverNNFData;
            if (remover2NNFData != null) {
                remover2NNFData.releaseMemoryFile();
            }
        }
    }

    @Override // com.miui.gallery.editor.photo.core.RenderData, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeList(this.mPaintData);
    }
}
