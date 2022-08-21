package com.miui.gallery.editor.photo.core.imports.text;

import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.Metadata;
import com.miui.gallery.util.BaseBuildUtil;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class TextCategoryData extends Metadata {
    public static final Parcelable.Creator<TextCategoryData> CREATOR = new Parcelable.Creator<TextCategoryData>() { // from class: com.miui.gallery.editor.photo.core.imports.text.TextCategoryData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public TextCategoryData mo879createFromParcel(Parcel parcel) {
            return new TextCategoryData(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public TextCategoryData[] mo880newArray(int i) {
            return new TextCategoryData[i];
        }
    };
    public int id;

    public TextCategoryData(String str, int i) {
        super((short) 0, str);
        this.id = i;
    }

    public static List<TextCategoryData> getCategoryData() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new TextCategoryData(GalleryApp.sGetAndroidContext().getString(R.string.text_tab_mark), 1));
        arrayList.add(new TextCategoryData(GalleryApp.sGetAndroidContext().getString(R.string.text_tab_watermark), 2));
        if (!BaseBuildUtil.isInternational()) {
            arrayList.add(new TextCategoryData(GalleryApp.sGetAndroidContext().getString(R.string.text_tab_spot), 3));
            arrayList.add(new TextCategoryData(GalleryApp.sGetAndroidContext().getString(R.string.text_tab_festival), 4));
            arrayList.add(new TextCategoryData(GalleryApp.sGetAndroidContext().getString(R.string.text_tab_scene), 5));
            arrayList.add(new TextCategoryData(GalleryApp.sGetAndroidContext().getString(R.string.text_tab_city), 6));
        }
        return arrayList;
    }

    @Override // com.miui.gallery.editor.photo.core.Metadata, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
    }

    public TextCategoryData(Parcel parcel) {
        super(parcel);
    }
}
