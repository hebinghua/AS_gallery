package com.miui.gallery.editor.photo.core.imports.text;

import com.miui.gallery.editor.photo.core.common.model.TextData;
import com.miui.gallery.editor.photo.core.imports.text.dialog.BaseDialogModel;
import com.miui.gallery.editor.photo.core.imports.text.signature.SignatureInfo;
import com.miui.gallery.editor.photo.core.imports.text.watermark.WatermarkInfo;

/* loaded from: classes2.dex */
public class TextConfig extends TextData {
    public BaseDialogModel mBaseDialogModel;
    public int mItemType;
    public SignatureInfo mSignatureInfo;
    public WatermarkInfo mWatermarkInfo;

    public TextConfig(short s, String str, String str2, String str3, int i) {
        super(s, str, str2, str3);
        this.mItemType = i;
    }

    public int getItemType() {
        return this.mItemType;
    }

    public TextConfig(short s, String str, BaseDialogModel baseDialogModel) {
        super(s, str, baseDialogModel.talkbackName, baseDialogModel.dialogSmallIconPath, baseDialogModel.backgroundColor);
        this.mBaseDialogModel = baseDialogModel;
    }

    public TextConfig(short s, WatermarkInfo watermarkInfo) {
        super(s, watermarkInfo.name, watermarkInfo.getTalkbackName(), watermarkInfo.icon);
        this.mWatermarkInfo = watermarkInfo;
    }

    public TextConfig(short s, SignatureInfo signatureInfo, String str, BaseDialogModel baseDialogModel) {
        super(s, str, baseDialogModel.talkbackName, baseDialogModel.dialogSmallIconPath, baseDialogModel.backgroundColor);
        this.mSignatureInfo = signatureInfo;
        this.mBaseDialogModel = baseDialogModel;
    }

    public BaseDialogModel getBaseDialogModel() {
        return this.mBaseDialogModel;
    }

    public WatermarkInfo getWatermarkInfo() {
        return this.mWatermarkInfo;
    }

    public SignatureInfo getSignatureInfo() {
        return this.mSignatureInfo;
    }

    public boolean isWatermark() {
        return this.mWatermarkInfo != null;
    }

    public boolean isSignature() {
        return this.mSignatureInfo != null;
    }
}
