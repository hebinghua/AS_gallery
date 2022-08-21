package com.miui.gallery.editor.photo.core.imports.remover;

import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView;
import com.miui.gallery.editor.photo.utils.parcelable.ParcelableGenericUtils;
import com.miui.gallery.util.parcelable.ParcelableMatrix;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class RemoverPaintData implements Parcelable {
    public static final Parcelable.Creator<RemoverPaintData> CREATOR = new Parcelable.Creator<RemoverPaintData>() { // from class: com.miui.gallery.editor.photo.core.imports.remover.RemoverPaintData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public RemoverPaintData mo849createFromParcel(Parcel parcel) {
            return new RemoverPaintData(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public RemoverPaintData[] mo850newArray(int i) {
            return new RemoverPaintData[i];
        }
    };
    public ParcelableMatrix mApplyDoodleMatrix;
    public RectF mBmpBounds;
    public List<RemoverGestureView.Curve> mCurves;
    public ParcelableMatrix mDrawBitmapMatrix;
    public RectF mDrawableBounds;
    public RectF mExportBounds;
    public ParcelableMatrix mExportMatrix;
    public RemoverNNFData mRemoverNNFData;
    public RectF mViewBounds;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public RemoverPaintData() {
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

    public RemoverPaintData(Parcel parcel) {
        this.mExportBounds = (RectF) parcel.readParcelable(RectF.class.getClassLoader());
        this.mExportMatrix = (ParcelableMatrix) parcel.readParcelable(ParcelableMatrix.class.getClassLoader());
        this.mBmpBounds = (RectF) parcel.readParcelable(RectF.class.getClassLoader());
        this.mViewBounds = (RectF) parcel.readParcelable(RectF.class.getClassLoader());
        this.mDrawableBounds = (RectF) parcel.readParcelable(RectF.class.getClassLoader());
        this.mDrawBitmapMatrix = (ParcelableMatrix) parcel.readParcelable(ParcelableMatrix.class.getClassLoader());
        this.mApplyDoodleMatrix = (ParcelableMatrix) parcel.readParcelable(ParcelableMatrix.class.getClassLoader());
        this.mCurves = ParcelableGenericUtils.readList(parcel);
        this.mRemoverNNFData = (RemoverNNFData) parcel.readParcelable(RemoverNNFData.class.getClassLoader());
    }
}
