package com.miui.gallery.editor.photo.core;

import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.editor.photo.core.SdkProvider;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractCropFragment;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractDoodleFragment;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractFrameFragment;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractLongCropFragment;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractMosaicFragment;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractRemover2Fragment;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractRemoverFragment;
import com.miui.gallery.editor.photo.core.common.model.Adjust2Data;
import com.miui.gallery.editor.photo.core.common.model.AdjustData;
import com.miui.gallery.editor.photo.core.common.model.BeautifyData;
import com.miui.gallery.editor.photo.core.common.model.CropData;
import com.miui.gallery.editor.photo.core.common.model.DoodleData;
import com.miui.gallery.editor.photo.core.common.model.FilterCategory;
import com.miui.gallery.editor.photo.core.common.model.FrameData;
import com.miui.gallery.editor.photo.core.common.model.MosaicData;
import com.miui.gallery.editor.photo.core.common.model.Remover2Data;
import com.miui.gallery.editor.photo.core.common.model.RemoverData;
import com.miui.gallery.editor.photo.core.common.model.SkyCategory;
import com.miui.gallery.editor.photo.core.common.provider.AbstractStickerProvider;
import com.miui.gallery.editor.photo.core.common.provider.AbstractTextProvider;
import com.miui.gallery.editor.photo.core.imports.miuibeauty.MiuiBeautifyData;

/* loaded from: classes2.dex */
public final class Effect<T extends SdkProvider> implements Parcelable {
    public String mName;
    public int mOrdinal;
    public static final Effect<SdkProvider<FilterCategory, AbstractEffectFragment>> FILTER = new Effect<>("FILTER", 0);
    public static final Effect<SdkProvider<AdjustData, AbstractEffectFragment>> ADJUST = new Effect<>("ADJUST", 1);
    public static final Effect<SdkProvider<Adjust2Data, AbstractEffectFragment>> ADJUST2 = new Effect<>("ADJUST2", 13);
    public static final Effect<SdkProvider<CropData, AbstractCropFragment>> CROP = new Effect<>("CROP", 2);
    public static final Effect<SdkProvider<DoodleData, AbstractDoodleFragment>> DOODLE = new Effect<>("DOODLE", 3);
    public static final Effect<AbstractStickerProvider> STICKER = new Effect<>("STICKER", 4);
    public static final Effect<SdkProvider<BeautifyData, AbstractEffectFragment>> BEAUTIFY = new Effect<>("BEAUTIFY", 5);
    public static final Effect<SdkProvider<BeautifyData, AbstractEffectFragment>> BEAUTIFY2 = new Effect<>("BEAUTIFY2", 14);
    public static final Effect<SdkProvider<Object, AbstractLongCropFragment>> LONG_CROP = new Effect<>("LONG_CROP", 6);
    public static final Effect<AbstractTextProvider> TEXT = new Effect<>("TEXT", 7);
    public static final Effect<SdkProvider<MosaicData, AbstractMosaicFragment>> MOSAIC = new Effect<>("MOSAIC", 8);
    public static final Effect<SdkProvider<RemoverData, AbstractRemoverFragment>> REMOVER = new Effect<>("REMOVER", 9);
    public static final Effect<SdkProvider<MiuiBeautifyData, AbstractEffectFragment>> MIUIBEAUTIFY = new Effect<>("MIUIBEAUTIFY", 10);
    public static final Effect<SdkProvider<FrameData, AbstractFrameFragment>> FRAME = new Effect<>("FRAME", 11);
    public static final Effect<SdkProvider<SkyCategory, AbstractEffectFragment>> SKY = new Effect<>("SKY", 12);
    public static final Effect<SdkProvider<Remover2Data, AbstractRemover2Fragment>> REMOVER2 = new Effect<>("REMOVER2", 15);
    public static final Parcelable.Creator<Effect> CREATOR = new Parcelable.Creator<Effect>() { // from class: com.miui.gallery.editor.photo.core.Effect.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public Effect[] mo779newArray(int i) {
            return null;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public Effect mo778createFromParcel(Parcel parcel) {
            return new Effect(parcel);
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Effect(String str, int i) {
        this.mOrdinal = i;
        this.mName = str;
    }

    public final String toString() {
        return this.mName;
    }

    public int ordinal() {
        return this.mOrdinal;
    }

    public String name() {
        return this.mName;
    }

    public static Effect[] values() {
        return new Effect[]{FILTER, ADJUST, CROP, DOODLE, STICKER, BEAUTIFY, LONG_CROP, TEXT, MOSAIC, REMOVER, MIUIBEAUTIFY, FRAME, SKY, ADJUST2, BEAUTIFY2, REMOVER2};
    }

    public Effect(Parcel parcel) {
        this.mOrdinal = parcel.readInt();
        this.mName = parcel.readString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mOrdinal);
        parcel.writeString(this.mName);
    }
}
