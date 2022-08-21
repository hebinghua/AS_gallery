package com.miui.gallery.editor.photo.core.imports.text;

import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.imports.text.TextEditorView;

/* loaded from: classes2.dex */
public class TextRenderData extends RenderData {
    public static final Parcelable.Creator<TextRenderData> CREATOR = new Parcelable.Creator<TextRenderData>() { // from class: com.miui.gallery.editor.photo.core.imports.text.TextRenderData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public TextRenderData mo884createFromParcel(Parcel parcel) {
            return new TextRenderData(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public TextRenderData[] mo885newArray(int i) {
            return new TextRenderData[i];
        }
    };
    public TextEditorView.TextEntry mTextEntry;

    @Override // com.miui.gallery.editor.photo.core.RenderData, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public TextRenderData(TextEditorView.TextEntry textEntry) {
        this.mTextEntry = textEntry;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderData, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeParcelable(this.mTextEntry, i);
    }

    public TextRenderData(Parcel parcel) {
        super(parcel);
        this.mTextEntry = (TextEditorView.TextEntry) parcel.readParcelable(TextEditorView.TextEntry.class.getClassLoader());
    }
}
