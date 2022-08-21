package com.miui.gallery.editor.photo.core.imports.filter;

import android.os.Parcel;
import android.os.Parcelable;
import com.miui.filtersdk.filter.base.BaseOriginalFilter;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.common.model.RenderMetaData;
import com.miui.gallery.editor.photo.core.imports.filter.render.BaseOriginalFilterGroup;
import com.miui.gallery.editor.photo.core.imports.filter.render.EmptyGPUImageFilter;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class FilterRenderData extends RenderData {
    public static final Parcelable.Creator<FilterRenderData> CREATOR = new Parcelable.Creator<FilterRenderData>() { // from class: com.miui.gallery.editor.photo.core.imports.filter.FilterRenderData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public FilterRenderData mo810createFromParcel(Parcel parcel) {
            return new FilterRenderData(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public FilterRenderData[] mo811newArray(int i) {
            return new FilterRenderData[i];
        }
    };
    public final List<RenderMetaData> mEffects;

    @Override // com.miui.gallery.editor.photo.core.RenderData, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderData
    public boolean isVideo() {
        return false;
    }

    public FilterRenderData(List<RenderMetaData> list) {
        this.mEffects = new ArrayList(list);
    }

    @Override // com.miui.gallery.editor.photo.core.RenderData
    public RenderData onMerge(RenderData renderData) {
        if (!isPortraitColor() && (renderData instanceof FilterRenderData)) {
            FilterRenderData filterRenderData = (FilterRenderData) renderData;
            if (!filterRenderData.isPortraitColor()) {
                ArrayList arrayList = new ArrayList(this.mEffects);
                arrayList.addAll(filterRenderData.mEffects);
                return new FilterRenderData(arrayList);
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return this.mEffects.isEmpty();
    }

    public BaseOriginalFilter instantiate() {
        if (this.mEffects.size() == 0) {
            return new EmptyGPUImageFilter();
        }
        if (this.mEffects.size() == 1) {
            return this.mEffects.get(0).instantiate();
        }
        ArrayList arrayList = new ArrayList(this.mEffects.size());
        for (int i = 0; i < this.mEffects.size(); i++) {
            arrayList.add(this.mEffects.get(i).instantiate());
        }
        return new BaseOriginalFilterGroup(arrayList);
    }

    public boolean isPortraitBeauty() {
        if (this.mEffects.size() != 1 || !(this.mEffects.get(0) instanceof FilterBeautify)) {
            return false;
        }
        return ((FilterBeautify) this.mEffects.get(0)).isPortraitBeauty();
    }

    public boolean isPortraitColor() {
        if (this.mEffects.size() != 1 || !(this.mEffects.get(0) instanceof FilterItem)) {
            return false;
        }
        return ((FilterItem) this.mEffects.get(0)).isPortraitColor();
    }

    public FilterItem getPortraitColorFilterItem() {
        if (this.mEffects.size() != 1 || !(this.mEffects.get(0) instanceof FilterItem)) {
            return null;
        }
        return (FilterItem) this.mEffects.get(0);
    }

    @Override // com.miui.gallery.editor.photo.core.RenderData, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeList(this.mEffects);
    }

    public FilterRenderData(Parcel parcel) {
        super(parcel);
        ArrayList arrayList = new ArrayList();
        this.mEffects = arrayList;
        parcel.readList(arrayList, FilterItem.class.getClassLoader());
    }
}
