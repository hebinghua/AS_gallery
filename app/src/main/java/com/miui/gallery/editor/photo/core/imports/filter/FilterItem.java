package com.miui.gallery.editor.photo.core.imports.filter;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.miui.filtersdk.filter.base.BaseOriginalFilter;
import com.miui.filtersdk.filter.base.ColorLookupFilter;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.filter.res.FilterResourceFetcher;
import com.miui.gallery.editor.photo.core.common.model.FilterData;
import com.miui.gallery.editor.photo.core.imports.filter.render.EmptyGPUImageFilter;
import com.miui.gallery.editor.photo.core.imports.filter.render.PathLUTFilter;

/* loaded from: classes2.dex */
public class FilterItem extends FilterData {
    public static short FILTER_ITEM_PRIORITY = 10;
    public int mDefault;
    public int mHighLightColor;
    public boolean mIsFilePath;
    public boolean mIsLast;
    public String mMaterialName;
    public String mTablePath;
    public String mType;
    public static final int FILTER_POPULAR_COLOR = GalleryApp.sGetAndroidContext().getResources().getColor(R.color.filter_category_popular);
    public static final int FILTER_CLASSIC_COLOR = GalleryApp.sGetAndroidContext().getResources().getColor(R.color.filter_category_classic);
    public static final int FILTER_PORTRAIT_COLOR = GalleryApp.sGetAndroidContext().getResources().getColor(R.color.filter_category_portrait);
    public static final int FILTER_MOVIE_COLOR = GalleryApp.sGetAndroidContext().getResources().getColor(R.color.filter_category_movie);
    public static final Parcelable.Creator<FilterItem> CREATOR = new Parcelable.Creator<FilterItem>() { // from class: com.miui.gallery.editor.photo.core.imports.filter.FilterItem.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public FilterItem mo808createFromParcel(Parcel parcel) {
            return new FilterItem(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public FilterItem[] mo809newArray(int i) {
            return new FilterItem[i];
        }
    };

    @Override // com.miui.gallery.editor.photo.core.common.model.FilterData, com.miui.gallery.editor.photo.core.Metadata, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public FilterItem(String str, int i, int i2) {
        this(str, i);
        this.mHighLightColor = i2;
    }

    public FilterItem(String str, int i) {
        super(FILTER_ITEM_PRIORITY, str, i);
        this.mType = "default";
    }

    public FilterItem(String str, String str2, int i, int i2, int i3) {
        this(str, str2, i, i2);
        this.mHighLightColor = i3;
    }

    public FilterItem(String str, String str2, int i, int i2) {
        super(FILTER_ITEM_PRIORITY, str2, i);
        this.mType = "default";
        this.mTablePath = str;
        this.mDefault = i2;
        this.progress = getDefault();
    }

    public FilterItem(String str, String str2, String str3, int i, int i2, int i3, int i4) {
        this(str, str2, str3, i, i2, i3);
        this.mHighLightColor = i4;
    }

    public FilterItem(String str, String str2, String str3, int i, int i2, int i3) {
        super(FILTER_ITEM_PRIORITY, str3, i);
        this.mType = "default";
        this.mTablePath = str2;
        this.mDefault = i2;
        this.progress = getDefault();
        this.state = i3;
        this.mType = str;
    }

    public FilterItem(String str, String str2, int i, int i2, int i3, String str3, int i4) {
        this(str, str2, i, i2, i3, str3);
        this.mHighLightColor = i4;
    }

    public FilterItem(String str, String str2, int i, int i2, int i3, String str3) {
        super(FILTER_ITEM_PRIORITY, str2, i);
        this.mType = "default";
        this.mDefault = i2;
        this.progress = getDefault();
        this.state = i3;
        this.mType = str;
        this.mMaterialName = str3;
        this.mTablePath = FilterResourceFetcher.getMaterialPath(this);
        this.mIsFilePath = true;
    }

    public int getDefault() {
        return this.mDefault;
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.FilterData
    public String getMaterialName() {
        return this.mMaterialName;
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.FilterData
    public boolean isNone() {
        return TextUtils.isEmpty(this.mTablePath);
    }

    public boolean getLast() {
        return this.mIsLast;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.filter.Renderable
    public BaseOriginalFilter instantiate() {
        BaseOriginalFilter colorLookupFilter;
        if (TextUtils.isEmpty(this.mTablePath)) {
            return new EmptyGPUImageFilter();
        }
        if (this.mIsFilePath) {
            colorLookupFilter = new PathLUTFilter(this.mTablePath);
        } else {
            colorLookupFilter = new ColorLookupFilter(this.mTablePath);
        }
        if (colorLookupFilter.isDegreeAdjustSupported()) {
            colorLookupFilter.setDegree(this.progress);
        }
        return colorLookupFilter;
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.FilterData
    public boolean isPortraitColor() {
        return "type_portrait_color".equals(this.mType);
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.FilterData
    public boolean isBuiltIn() {
        return "default".equals(this.mType);
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.FilterData, com.miui.gallery.editor.photo.core.Metadata, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(this.mTablePath);
        parcel.writeInt(this.mDefault);
        parcel.writeByte(this.mIsFilePath ? (byte) 1 : (byte) 0);
        parcel.writeString(this.mMaterialName);
        parcel.writeByte(this.mIsLast ? (byte) 1 : (byte) 0);
        parcel.writeInt(this.mHighLightColor);
    }

    public FilterItem(Parcel parcel) {
        super(parcel);
        this.mType = "default";
        this.mTablePath = parcel.readString();
        this.mDefault = parcel.readInt();
        boolean z = true;
        this.mIsFilePath = parcel.readByte() != 0;
        this.mMaterialName = parcel.readString();
        this.mIsLast = parcel.readByte() == 0 ? false : z;
        this.mHighLightColor = parcel.readInt();
    }
}
