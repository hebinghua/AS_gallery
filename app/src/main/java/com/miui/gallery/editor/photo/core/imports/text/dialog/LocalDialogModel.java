package com.miui.gallery.editor.photo.core.imports.text.dialog;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes2.dex */
public class LocalDialogModel extends BaseDialogModel {
    public static final Parcelable.Creator<LocalDialogModel> CREATOR = new Parcelable.Creator<LocalDialogModel>() { // from class: com.miui.gallery.editor.photo.core.imports.text.dialog.LocalDialogModel.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public LocalDialogModel mo886createFromParcel(Parcel parcel) {
            return new LocalDialogModel(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public LocalDialogModel[] mo887newArray(int i) {
            return new LocalDialogModel[i];
        }
    };
    public int mBlackDialogRes;
    public int mDialogRes;

    @Override // com.miui.gallery.editor.photo.core.imports.text.dialog.BaseDialogModel, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public LocalDialogModel(int r15, int r16, int r17, int r18, float r19, float r20, float r21, float r22, boolean r23, int r24, java.lang.String r25, java.lang.String r26, int r27) {
        /*
            r14 = this;
            r13 = r14
            com.miui.gallery.util.Scheme r0 = com.miui.gallery.util.Scheme.DRAWABLE
            java.lang.String r1 = java.lang.Integer.toString(r16)
            java.lang.String r2 = r0.wrap(r1)
            java.lang.String r1 = java.lang.Integer.toString(r17)
            java.lang.String r3 = r0.wrap(r1)
            r0 = r14
            r1 = r15
            r4 = r19
            r5 = r20
            r6 = r21
            r7 = r22
            r8 = r23
            r9 = r24
            r10 = r25
            r11 = r26
            r12 = r27
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12)
            r0 = r17
            r13.mDialogRes = r0
            r0 = r18
            r13.mBlackDialogRes = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.editor.photo.core.imports.text.dialog.LocalDialogModel.<init>(int, int, int, int, float, float, float, float, boolean, int, java.lang.String, java.lang.String, int):void");
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.dialog.BaseDialogModel
    public boolean hasDialog() {
        return this.mDialogRes != 0;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.dialog.BaseDialogModel
    public Drawable readDialogDrawable(Resources resources) {
        if (hasDialog()) {
            return resources.getDrawable(this.mDialogRes);
        }
        return null;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.dialog.BaseDialogModel
    public boolean hasBlackDialog() {
        return this.mBlackDialogRes != 0;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.dialog.BaseDialogModel
    public Drawable readBlackDialogDrawable(Resources resources) {
        if (hasBlackDialog()) {
            return resources.getDrawable(this.mBlackDialogRes);
        }
        return null;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.dialog.BaseDialogModel, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(this.mDialogRes);
        parcel.writeInt(this.mBlackDialogRes);
    }

    public LocalDialogModel(Parcel parcel) {
        super(parcel);
        this.mDialogRes = parcel.readInt();
        this.mBlackDialogRes = parcel.readInt();
    }
}
