package com.miui.gallery.editor.photo.core.imports.mosaic;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.LinkedList;

/* loaded from: classes2.dex */
public class MosaicOperationItem implements Parcelable {
    public static final Parcelable.Creator<MosaicOperationItem> CREATOR = new Parcelable.Creator<MosaicOperationItem>() { // from class: com.miui.gallery.editor.photo.core.imports.mosaic.MosaicOperationItem.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public MosaicOperationItem mo831createFromParcel(Parcel parcel) {
            return new MosaicOperationItem(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public MosaicOperationItem[] mo832newArray(int i) {
            return new MosaicOperationItem[i];
        }
    };
    public final MosaicGLEntity mosaicGLEntity;
    public final LinkedList<PaintingItem> paintingItems;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public MosaicOperationItem(MosaicGLEntity mosaicGLEntity) {
        this.mosaicGLEntity = mosaicGLEntity;
        this.paintingItems = new LinkedList<>();
    }

    public void add(PaintingItem paintingItem) {
        this.paintingItems.add(paintingItem);
    }

    public boolean isEmpty() {
        return this.paintingItems.isEmpty();
    }

    public PaintingItem removeLast() {
        return this.paintingItems.removeLast();
    }

    /* loaded from: classes2.dex */
    public static class PaintingItem implements Parcelable {
        public static final Parcelable.Creator<PaintingItem> CREATOR = new Parcelable.Creator<PaintingItem>() { // from class: com.miui.gallery.editor.photo.core.imports.mosaic.MosaicOperationItem.PaintingItem.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public PaintingItem mo833createFromParcel(Parcel parcel) {
                return new PaintingItem(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public PaintingItem[] mo834newArray(int i) {
                return new PaintingItem[i];
            }
        };
        public final LinkedList<GLRectF> points;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public PaintingItem() {
            this.points = new LinkedList<>();
        }

        public void add(GLRectF gLRectF) {
            this.points.add(gLRectF);
        }

        public boolean isEmpty() {
            return this.points.isEmpty();
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeTypedList(this.points);
        }

        public PaintingItem(Parcel parcel) {
            LinkedList<GLRectF> linkedList = new LinkedList<>();
            this.points = linkedList;
            parcel.readTypedList(linkedList, GLRectF.CREATOR);
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.mosaicGLEntity, i);
        parcel.writeTypedList(this.paintingItems);
    }

    public MosaicOperationItem(Parcel parcel) {
        this.mosaicGLEntity = (MosaicGLEntity) parcel.readParcelable(MosaicGLEntity.class.getClassLoader());
        LinkedList<PaintingItem> linkedList = new LinkedList<>();
        this.paintingItems = linkedList;
        parcel.readTypedList(linkedList, PaintingItem.CREATOR);
    }
}
