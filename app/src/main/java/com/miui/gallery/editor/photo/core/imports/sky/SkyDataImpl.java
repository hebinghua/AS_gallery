package com.miui.gallery.editor.photo.core.imports.sky;

import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.editor.photo.core.common.model.SkyData;

/* loaded from: classes2.dex */
public class SkyDataImpl extends SkyData {
    public static final Parcelable.Creator<SkyDataImpl> CREATOR = new Parcelable.Creator<SkyDataImpl>() { // from class: com.miui.gallery.editor.photo.core.imports.sky.SkyDataImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public SkyDataImpl mo866createFromParcel(Parcel parcel) {
            return new SkyDataImpl(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public SkyDataImpl[] mo867newArray(int i) {
            return new SkyDataImpl[i];
        }
    };
    public int mDefaultProgress;
    public boolean mDependOnSegment;
    public int mDownloadState;
    public int mIcon;
    public boolean mIsDynamic;
    public boolean mIsFromCloud;
    public boolean mIsLast;
    public String mMaterialName;
    public int mParentCategory;
    public int mProgress;

    public SkyDataImpl(int i, String str, String str2, int i2, int i3, boolean z) {
        this(i, str, str2, i2, i3);
        this.mIsLast = z;
    }

    public SkyDataImpl(int i, String str, String str2, int i2, int i3) {
        this(i, str, str2, i2, i3, false, false);
    }

    public SkyDataImpl(int i, String str, String str2, int i2, int i3, boolean z, boolean z2) {
        super((short) 0, str2);
        this.mIsFromCloud = false;
        this.mDownloadState = 19;
        this.mParentCategory = i;
        this.mIsDynamic = z;
        this.mMaterialName = str;
        this.mIcon = i2;
        this.mDefaultProgress = i3;
        this.mProgress = i3;
        this.mDependOnSegment = z2;
    }

    public SkyDataImpl(Parcel parcel) {
        super(parcel);
        boolean z = false;
        this.mIsFromCloud = false;
        this.mDownloadState = 19;
        this.mMaterialName = parcel.readString();
        this.mIcon = parcel.readInt();
        this.mDefaultProgress = parcel.readInt();
        this.mProgress = parcel.readInt();
        this.mIsDynamic = parcel.readByte() == 1;
        this.mDependOnSegment = parcel.readByte() == 1;
        this.mIsLast = parcel.readByte() == 1;
        this.mIsFromCloud = parcel.readByte() == 1 ? true : z;
    }

    @Override // com.miui.gallery.editor.photo.core.Metadata, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(this.mMaterialName);
        parcel.writeInt(this.mIcon);
        parcel.writeInt(this.mDefaultProgress);
        parcel.writeInt(this.mProgress);
        parcel.writeByte(this.mIsDynamic ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.mDependOnSegment ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.mIsLast ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.mIsFromCloud ? (byte) 1 : (byte) 0);
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.SkyData
    public boolean isDynamic() {
        return this.mIsDynamic;
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.SkyData
    public int getProgress() {
        return this.mProgress;
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.SkyData
    public void setProgress(int i) {
        this.mProgress = i;
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.SkyData
    public boolean isNone() {
        return this.mMaterialName == null;
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.SkyData
    public int getIcon() {
        return this.mIcon;
    }

    public boolean getLast() {
        return this.mIsLast;
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.SkyData
    public void resetProgress() {
        this.mProgress = this.mDefaultProgress;
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.SkyData
    public int getDownloadState() {
        return this.mDownloadState;
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.SkyData
    public void setDownloadState(int i) {
        this.mDownloadState = i;
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.SkyData
    public String getMaterialName() {
        return this.mMaterialName;
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.SkyData
    public boolean dependOnSegment() {
        return this.mDependOnSegment;
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.SkyData
    public int getParentCategory() {
        return this.mParentCategory;
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.SkyData
    public boolean isFromCloud() {
        return this.mIsFromCloud;
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.SkyData
    public void setFromCloud(boolean z) {
        this.mIsFromCloud = z;
    }
}
