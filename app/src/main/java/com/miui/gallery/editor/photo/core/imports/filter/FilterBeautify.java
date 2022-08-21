package com.miui.gallery.editor.photo.core.imports.filter;

import android.os.Parcel;
import android.os.Parcelable;
import com.miui.filtersdk.filter.base.BaseOriginalFilter;
import com.miui.filtersdk.filter.base.ColorLookupFilter;
import com.miui.gallery.editor.photo.core.common.model.BeautifyData;
import com.miui.gallery.editor.photo.core.imports.filter.render.EmptyGPUImageFilter;

/* loaded from: classes2.dex */
public class FilterBeautify extends BeautifyData {
    public static short BEAUTIFY_ITEM_PRIORITY = 10;
    public static final Parcelable.Creator<FilterBeautify> CREATOR = new Parcelable.Creator<FilterBeautify>() { // from class: com.miui.gallery.editor.photo.core.imports.filter.FilterBeautify.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public FilterBeautify mo804createFromParcel(Parcel parcel) {
            return new FilterBeautify(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public FilterBeautify[] mo805newArray(int i) {
            return new FilterBeautify[i];
        }
    };
    public int mId;

    @Override // com.miui.gallery.editor.photo.core.common.model.BeautifyData, com.miui.gallery.editor.photo.core.Metadata, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public FilterBeautify(int i, String str, int i2) {
        super(BEAUTIFY_ITEM_PRIORITY, str, i2);
        this.mId = i;
    }

    public final String getTablePath() {
        int i = this.mId;
        if (i != 1) {
            if (i == 2) {
                return "filter/onekey/portrait.png";
            }
            if (i == 3) {
                return "filter/onekey/food.png";
            }
            if (i == 4) {
                return "filter/onekey/scene.png";
            }
            throw new IllegalArgumentException("not table for this id: " + this.mId);
        }
        return "filter/onekey/auto.png";
    }

    @Override // com.miui.gallery.editor.photo.core.imports.filter.Renderable
    public BaseOriginalFilter instantiate() {
        if (this.mId == 0) {
            return new EmptyGPUImageFilter();
        }
        ColorLookupFilter colorLookupFilter = new ColorLookupFilter(getTablePath());
        if (!colorLookupFilter.isDegreeAdjustSupported()) {
            return colorLookupFilter;
        }
        colorLookupFilter.setDegree(100);
        return colorLookupFilter;
    }

    public boolean isPortraitBeauty() {
        return this.mId == 2;
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.BeautifyData, com.miui.gallery.editor.photo.core.Metadata, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(this.mId);
    }

    public FilterBeautify(Parcel parcel) {
        super(parcel);
        this.mId = parcel.readInt();
    }
}
