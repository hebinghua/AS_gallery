package com.miui.gallery.editor.photo.core.imports.adjust2;

import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.common.model.Adjust2Data;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/* loaded from: classes2.dex */
public class Adjust2RenderData extends RenderData {
    public static final Parcelable.Creator<Adjust2RenderData> CREATOR = new Parcelable.Creator<Adjust2RenderData>() { // from class: com.miui.gallery.editor.photo.core.imports.adjust2.Adjust2RenderData.1
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public Adjust2RenderData mo782createFromParcel(Parcel parcel) {
            return new Adjust2RenderData(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public Adjust2RenderData[] mo783newArray(int i) {
            return new Adjust2RenderData[i];
        }
    };
    public List<Adjust2Data> mEffects;

    public static /* synthetic */ void $r8$lambda$arvEP48EO9ZXdsht8CdYHGzWM9E(StringBuilder sb, Adjust2Data adjust2Data) {
        lambda$getFilterEffect$0(sb, adjust2Data);
    }

    @Override // com.miui.gallery.editor.photo.core.RenderData, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderData
    public void onRelease() {
    }

    public Adjust2RenderData(List<Adjust2Data> list) {
        this.mEffects = new ArrayList(list);
    }

    @Override // com.miui.gallery.editor.photo.core.RenderData
    public RenderData onMerge(RenderData renderData) {
        if (renderData instanceof Adjust2RenderData) {
            ArrayList arrayList = new ArrayList(this.mEffects);
            arrayList.addAll(((Adjust2RenderData) renderData).mEffects);
            return new Adjust2RenderData(arrayList);
        }
        return null;
    }

    public String getFilterEffect() {
        final StringBuilder sb = new StringBuilder();
        this.mEffects.forEach(new Consumer() { // from class: com.miui.gallery.editor.photo.core.imports.adjust2.Adjust2RenderData$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Adjust2RenderData.$r8$lambda$arvEP48EO9ZXdsht8CdYHGzWM9E(sb, (Adjust2Data) obj);
            }
        });
        return sb.toString();
    }

    public static /* synthetic */ void lambda$getFilterEffect$0(StringBuilder sb, Adjust2Data adjust2Data) {
        if (!sb.toString().equals("")) {
            sb.append("@");
        }
        sb.append(((Adjust2DataImpl) adjust2Data).getEffect());
    }

    @Override // com.miui.gallery.editor.photo.core.RenderData, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeList(this.mEffects);
    }

    public Adjust2RenderData(Parcel parcel) {
        super(parcel);
        ArrayList arrayList = new ArrayList();
        this.mEffects = arrayList;
        parcel.readList(arrayList, Adjust2Data.class.getClassLoader());
    }
}
