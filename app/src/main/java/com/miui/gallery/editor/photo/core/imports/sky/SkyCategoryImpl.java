package com.miui.gallery.editor.photo.core.imports.sky;

import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.common.model.SkyCategory;
import com.miui.gallery.editor.photo.core.common.model.SkyData;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class SkyCategoryImpl extends SkyCategory {
    public static final Parcelable.Creator<SkyCategoryImpl> CREATOR = new Parcelable.Creator<SkyCategoryImpl>() { // from class: com.miui.gallery.editor.photo.core.imports.sky.SkyCategoryImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public SkyCategoryImpl mo864createFromParcel(Parcel parcel) {
            return new SkyCategoryImpl(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public SkyCategoryImpl[] mo865newArray(int i) {
            return new SkyCategoryImpl[i];
        }
    };
    public int id;
    public List<SkyData> mDataList;
    public boolean mIsDynamic;

    public SkyCategoryImpl(String str, boolean z, int i, List<SkyData> list) {
        super((short) 0, str);
        this.mIsDynamic = z;
        this.id = i;
        this.mDataList = list;
    }

    public boolean isDynamic() {
        return this.mIsDynamic;
    }

    public List<SkyData> getDataList() {
        return this.mDataList;
    }

    public static List<SkyCategoryImpl> getCategoryData() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new SkyCategoryImpl(GalleryApp.sGetAndroidContext().getString(R.string.sky_sunny), false, 1, SkyDataManager.getSunnySkyItem()));
        arrayList.add(new SkyCategoryImpl(GalleryApp.sGetAndroidContext().getString(R.string.sky_night), false, 2, SkyDataManager.getNightSkyItem()));
        arrayList.add(new SkyCategoryImpl(GalleryApp.sGetAndroidContext().getString(R.string.sky_nocturne), false, 3, SkyDataManager.getNocturneSkyItem()));
        arrayList.add(new SkyCategoryImpl(GalleryApp.sGetAndroidContext().getString(R.string.sky_dynamic), true, 4, SkyDataManager.getDynamicSkyItem()));
        return arrayList;
    }

    @Override // com.miui.gallery.editor.photo.core.Metadata, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeByte(isDynamic() ? (byte) 1 : (byte) 0);
    }

    public SkyCategoryImpl(Parcel parcel) {
        super(parcel);
        this.mIsDynamic = parcel.readByte() != 1 ? false : true;
    }
}
