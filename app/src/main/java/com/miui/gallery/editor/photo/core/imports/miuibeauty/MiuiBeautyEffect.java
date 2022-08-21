package com.miui.gallery.editor.photo.core.imports.miuibeauty;

import android.os.Parcel;
import android.os.Parcelable;
import com.miui.filtersdk.beauty.BeautyParameterType;

/* loaded from: classes2.dex */
public class MiuiBeautyEffect implements Parcelable {
    public static final Parcelable.Creator<MiuiBeautyEffect> CREATOR = new Parcelable.Creator<MiuiBeautyEffect>() { // from class: com.miui.gallery.editor.photo.core.imports.miuibeauty.MiuiBeautyEffect.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public MiuiBeautyEffect mo818createFromParcel(Parcel parcel) {
            return new MiuiBeautyEffect(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public MiuiBeautyEffect[] mo819newArray(int i) {
            return new MiuiBeautyEffect[i];
        }
    };
    public BeautyParameterType mBeautyType;
    public String mName;
    public int mOrdinal;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean needFace() {
        int i;
        BeautyParameterType beautyParameterType = this.mBeautyType;
        return (beautyParameterType == null || (i = AnonymousClass2.$SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[beautyParameterType.ordinal()]) == 1 || i == 2) ? false : true;
    }

    /* renamed from: com.miui.gallery.editor.photo.core.imports.miuibeauty.MiuiBeautyEffect$2  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass2 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType;

        static {
            int[] iArr = new int[BeautyParameterType.values().length];
            $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType = iArr;
            try {
                iArr[BeautyParameterType.WHITEN_STRENGTH.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[BeautyParameterType.SMOOTH_STRENGTH.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    public MiuiBeautyEffect(Parcel parcel) {
        this.mOrdinal = parcel.readInt();
        this.mName = parcel.readString();
        this.mBeautyType = BeautyParameterType.valueOf(parcel.readString());
    }

    public MiuiBeautyEffect(String str, int i, BeautyParameterType beautyParameterType) {
        this.mOrdinal = i;
        this.mName = str;
        this.mBeautyType = beautyParameterType;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mOrdinal);
        parcel.writeString(this.mName);
        parcel.writeString(this.mBeautyType.name());
    }
}
