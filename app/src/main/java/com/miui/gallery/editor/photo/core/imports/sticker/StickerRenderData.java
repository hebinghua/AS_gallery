package com.miui.gallery.editor.photo.core.imports.sticker;

import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.imports.sticker.StickerEditorView;

/* loaded from: classes2.dex */
public class StickerRenderData extends RenderData {
    public static final Parcelable.Creator<StickerRenderData> CREATOR = new Parcelable.Creator<StickerRenderData>() { // from class: com.miui.gallery.editor.photo.core.imports.sticker.StickerRenderData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public StickerRenderData mo875createFromParcel(Parcel parcel) {
            return new StickerRenderData(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public StickerRenderData[] mo876newArray(int i) {
            return new StickerRenderData[i];
        }
    };
    public StickerEditorView.StickerEntry mEntry;

    @Override // com.miui.gallery.editor.photo.core.RenderData, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public StickerRenderData(StickerEditorView.StickerEntry stickerEntry) {
        this.mEntry = stickerEntry;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderData, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeParcelable(this.mEntry, i);
    }

    public StickerRenderData(Parcel parcel) {
        super(parcel);
        this.mEntry = (StickerEditorView.StickerEntry) parcel.readParcelable(StickerEditorView.StickerEntry.class.getClassLoader());
    }
}
