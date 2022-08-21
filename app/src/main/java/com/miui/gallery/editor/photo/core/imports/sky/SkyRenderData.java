package com.miui.gallery.editor.photo.core.imports.sky;

import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.editor.photo.app.sky.res.ResourceFetcher;
import com.miui.gallery.editor.photo.app.sky.sdk.SkyTransferTempData;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.common.model.SkyData;

/* loaded from: classes2.dex */
public class SkyRenderData extends RenderData {
    public static final Parcelable.Creator<SkyRenderData> CREATOR = new Parcelable.Creator<SkyRenderData>() { // from class: com.miui.gallery.editor.photo.core.imports.sky.SkyRenderData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public SkyRenderData mo868createFromParcel(Parcel parcel) {
            return new SkyRenderData(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public SkyRenderData[] mo869newArray(int i) {
            return new SkyRenderData[i];
        }
    };
    public boolean mCanAdjustMoment;
    public String mPath;
    public SkyTransferTempData mRenderTempData;
    public SkyData mSkyData;

    public SkyRenderData(SkyData skyData) {
        this.mSkyData = skyData;
        if (!skyData.isNone()) {
            this.mPath = ResourceFetcher.getMaterialPath(skyData);
        }
    }

    public String getBitmapPath() {
        return this.mSkyData.isFromCloud() ? "background_cloud" : "background";
    }

    @Override // com.miui.gallery.editor.photo.core.RenderData
    public boolean isVideo() {
        return this.mSkyData.isDynamic();
    }

    public void setCanAdjustMoment(boolean z) {
        this.mCanAdjustMoment = z;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderData, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeParcelable(this.mSkyData, i);
        parcel.writeByte(this.mCanAdjustMoment ? (byte) 1 : (byte) 0);
    }

    public boolean isNone() {
        return this.mSkyData.isNone();
    }

    public SkyRenderData(Parcel parcel) {
        super(parcel);
        this.mSkyData = (SkyData) parcel.readParcelable(SkyDataImpl.class.getClassLoader());
        this.mCanAdjustMoment = parcel.readByte() != 1 ? false : true;
    }

    public String getMaterialName() {
        return this.mSkyData.getMaterialName();
    }

    public boolean isDynamic() {
        return this.mSkyData.isDynamic();
    }

    public boolean supportRandom() {
        return (this.mSkyData.getParentCategory() != 1 || this.mSkyData.getMaterialName() == "sky_caihong" || this.mSkyData.getMaterialName() == "sky_hongni" || this.mSkyData.getMaterialName() == "sky_xuetian") ? false : true;
    }

    public boolean isDynamicTextYanhua() {
        return !this.mSkyData.isNone() && this.mSkyData.getMaterialName().equals("dynamic_sky_text_yanhua");
    }

    public boolean isNocturne() {
        return this.mSkyData.getParentCategory() == 3;
    }

    public int getMaterialId() {
        int parentCategory = this.mSkyData.getParentCategory();
        if (parentCategory != 1) {
            if (parentCategory == 2) {
                return SkyDataManager.getNightSkyMaterialId(this.mSkyData.getMaterialName());
            }
            if (parentCategory == 3) {
                return SkyDataManager.getNocturneSkyMaterialId(this.mSkyData.getMaterialName());
            }
            if (parentCategory == 4) {
                return SkyDataManager.getDynamicSkyMaterialId(this.mSkyData.getMaterialName());
            }
            return -1;
        }
        return SkyDataManager.getSunnySkyMaterialId(this.mSkyData.getMaterialName());
    }

    public String getPath() {
        return this.mPath;
    }

    public int getProgress() {
        return this.mSkyData.getProgress();
    }

    public void setRenderTempData(SkyTransferTempData skyTransferTempData) {
        this.mRenderTempData = skyTransferTempData;
    }

    public SkyTransferTempData getRenderTempData() {
        return this.mRenderTempData;
    }

    public String toString() {
        return "SkyRenderData{mSkyData=" + this.mSkyData + '}';
    }
}
