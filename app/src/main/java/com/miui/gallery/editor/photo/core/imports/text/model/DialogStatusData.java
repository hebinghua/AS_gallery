package com.miui.gallery.editor.photo.core.imports.text.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.miui.gallery.editor.photo.core.imports.text.dialog.BaseDialogModel;
import com.miui.gallery.editor.photo.core.imports.text.signature.SignatureInfo;
import com.miui.gallery.editor.photo.core.imports.text.utils.AutoLineLayout;
import com.miui.gallery.editor.photo.core.imports.text.watermark.WatermarkInfo;
import com.miui.gallery.editor.photo.utils.parcelable.ParcelableGenericUtils;
import java.util.List;

/* loaded from: classes2.dex */
public class DialogStatusData extends TextStatusData {
    public static final Parcelable.Creator<DialogStatusData> CREATOR = new Parcelable.Creator<DialogStatusData>() { // from class: com.miui.gallery.editor.photo.core.imports.text.model.DialogStatusData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public DialogStatusData mo893createFromParcel(Parcel parcel) {
            return new DialogStatusData(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public DialogStatusData[] mo894newArray(int i) {
            return new DialogStatusData[i];
        }
    };
    public boolean isReverseColor;
    public float itemDegree;
    public float itemPositionX;
    public float itemPositionY;
    public float itemScale;
    public boolean mIsWatermark;
    public String mName;
    public String mSignaturePath;
    public int[] mSubstrateColors;
    public TextStatusData[] textStatusDatas;

    @Override // com.miui.gallery.editor.photo.core.imports.text.model.TextStatusData, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public void configSignatureSelfByInit(SignatureInfo signatureInfo) {
        if (signatureInfo == null || TextUtils.isEmpty(signatureInfo.path)) {
            setEmpty();
            return;
        }
        this.mSignaturePath = signatureInfo.path;
        this.transparentProgress = 0.0f;
        this.textStyle = null;
        this.textBold = false;
        this.itemScale = 1.0f;
        this.itemDegree = 0.0f;
        this.itemPositionX = 0.0f;
        this.itemPositionY = 0.0f;
    }

    public void configSelfByInit(BaseDialogModel baseDialogModel) {
        if (baseDialogModel != null && baseDialogModel.hasDialog()) {
            if (baseDialogModel.isCorner) {
                this.color = -1;
                this.textAlignment = AutoLineLayout.TextAlignment.LEFT;
                this.textShadow = true;
            } else {
                this.color = -16777216;
                this.textAlignment = AutoLineLayout.TextAlignment.CENTER;
                this.textShadow = false;
            }
        } else {
            this.color = -1;
            this.textAlignment = AutoLineLayout.TextAlignment.LEFT;
            this.textShadow = true;
        }
        this.transparentProgress = 0.0f;
        this.textStyle = null;
        this.textBold = false;
        this.itemScale = 1.0f;
        this.itemDegree = 0.0f;
        this.itemPositionX = 0.0f;
        this.itemPositionY = 0.0f;
    }

    public void setEmpty() {
        this.color = -1;
        this.textAlignment = AutoLineLayout.TextAlignment.LEFT;
        this.transparentProgress = 0.0f;
        this.textStyle = null;
        this.textBold = false;
        this.textShadow = false;
        this.itemScale = 1.0f;
        this.itemDegree = 0.0f;
        this.itemPositionX = 0.0f;
        this.itemPositionY = 0.0f;
        this.textStatusDatas = new TextStatusData[0];
    }

    public void watermarkInitSelf(WatermarkInfo watermarkInfo) {
        List<WatermarkInfo.TextPieceInfo> list;
        if (watermarkInfo == null || (list = watermarkInfo.textPieceList) == null) {
            setEmpty();
            return;
        }
        this.textStatusDatas = new TextStatusData[list.size()];
        for (int i = 0; i < this.textStatusDatas.length; i++) {
            TextStatusData textStatusData = new TextStatusData();
            textStatusData.textBold = watermarkInfo.textPieceList.get(i).isBold;
            textStatusData.color = -1;
            textStatusData.textAlignment = AutoLineLayout.TextAlignment.LEFT;
            textStatusData.transparentProgress = 0.0f;
            textStatusData.textStyle = null;
            textStatusData.textShadow = false;
            this.textStatusDatas[i] = textStatusData;
        }
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.model.TextStatusData, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(this.itemPositionX);
        parcel.writeFloat(this.itemPositionY);
        parcel.writeFloat(this.itemScale);
        parcel.writeFloat(this.itemDegree);
        parcel.writeByte(this.isReverseColor ? (byte) 1 : (byte) 0);
        parcel.writeString(this.mSignaturePath);
        parcel.writeIntArray(this.mSubstrateColors);
        parcel.writeString(this.mName);
        ParcelableGenericUtils.writeArray(parcel, i, this.textStatusDatas, TextStatusData.class);
    }

    public DialogStatusData() {
    }

    public DialogStatusData(Parcel parcel) {
        this.itemPositionX = parcel.readFloat();
        this.itemPositionY = parcel.readFloat();
        this.itemScale = parcel.readFloat();
        this.itemDegree = parcel.readFloat();
        this.isReverseColor = parcel.readByte() != 0;
        this.mSignaturePath = parcel.readString();
        this.mSubstrateColors = parcel.createIntArray();
        this.textStatusDatas = (TextStatusData[]) ParcelableGenericUtils.readArray(parcel);
        this.mName = parcel.readString();
    }
}
