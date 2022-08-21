package com.miui.gallery.editor.photo.core.imports.remover2;

import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.editor.photo.core.imports.remover2.Remover2GestureView;
import com.miui.gallery.editor.photo.utils.parcelable.ParcelableGenericUtils;
import com.miui.gallery.util.parcelable.ParcelableMatrix;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class Remover2PaintData implements Parcelable {
    public static final Parcelable.Creator<Remover2PaintData> CREATOR = new Parcelable.Creator<Remover2PaintData>() { // from class: com.miui.gallery.editor.photo.core.imports.remover2.Remover2PaintData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public Remover2PaintData mo860createFromParcel(Parcel parcel) {
            return new Remover2PaintData(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public Remover2PaintData[] mo861newArray(int i) {
            return new Remover2PaintData[i];
        }
    };
    public ParcelableMatrix mApplyDoodleMatrix;
    public RectF mBmpBounds;
    public List<Remover2GestureView.Curve> mCurves;
    public ParcelableMatrix mDrawBitmapMatrix;
    public RectF mDrawableBounds;
    public RectF mExportBounds;
    public ParcelableMatrix mExportMatrix;
    public Remover2NNFData mRemoverNNFData;
    public RectF mViewBounds;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Remover2PaintData() {
        this.mExportBounds = new RectF();
        this.mExportMatrix = new ParcelableMatrix();
        this.mBmpBounds = new RectF();
        this.mViewBounds = new RectF();
        this.mDrawableBounds = new RectF();
        this.mDrawBitmapMatrix = new ParcelableMatrix();
        this.mApplyDoodleMatrix = new ParcelableMatrix();
        this.mCurves = new ArrayList();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.mExportBounds, i);
        parcel.writeParcelable(this.mExportMatrix, i);
        parcel.writeParcelable(this.mBmpBounds, i);
        parcel.writeParcelable(this.mViewBounds, i);
        parcel.writeParcelable(this.mDrawableBounds, i);
        parcel.writeParcelable(this.mDrawBitmapMatrix, i);
        parcel.writeParcelable(this.mApplyDoodleMatrix, i);
        ParcelableGenericUtils.writeList(parcel, i, this.mCurves);
        parcel.writeParcelable(this.mRemoverNNFData, i);
    }

    public Remover2PaintData(Parcel parcel) {
        this.mExportBounds = (RectF) parcel.readParcelable(RectF.class.getClassLoader());
        this.mExportMatrix = (ParcelableMatrix) parcel.readParcelable(ParcelableMatrix.class.getClassLoader());
        this.mBmpBounds = (RectF) parcel.readParcelable(RectF.class.getClassLoader());
        this.mViewBounds = (RectF) parcel.readParcelable(RectF.class.getClassLoader());
        this.mDrawableBounds = (RectF) parcel.readParcelable(RectF.class.getClassLoader());
        this.mDrawBitmapMatrix = (ParcelableMatrix) parcel.readParcelable(ParcelableMatrix.class.getClassLoader());
        this.mApplyDoodleMatrix = (ParcelableMatrix) parcel.readParcelable(ParcelableMatrix.class.getClassLoader());
        this.mCurves = ParcelableGenericUtils.readList(parcel);
        this.mRemoverNNFData = (Remover2NNFData) parcel.readParcelable(Remover2NNFData.class.getClassLoader());
    }
}
