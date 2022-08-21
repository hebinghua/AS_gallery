package com.miui.gallery.editor.photo.core.imports.text.watermark;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.editor.photo.utils.parcelable.ParcelableGenericUtils;
import java.util.List;

/* loaded from: classes2.dex */
public class WatermarkInfo implements Parcelable {
    public static final Parcelable.Creator<WatermarkInfo> CREATOR = new Parcelable.Creator<WatermarkInfo>() { // from class: com.miui.gallery.editor.photo.core.imports.text.watermark.WatermarkInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public WatermarkInfo mo910createFromParcel(Parcel parcel) {
            return new WatermarkInfo(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public WatermarkInfo[] mo911newArray(int i) {
            return new WatermarkInfo[i];
        }
    };
    public String bgPost;
    public float[] bgPostRect;
    public float height;
    public String icon;
    public String name;
    public float offsetX;
    public float offsetY;
    public String talkbackName;
    public List<TextPieceInfo> textPieceList;
    public int version;
    public float width;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String getTalkbackName() {
        int identifier;
        Resources resources = GalleryApp.sGetAndroidContext().getResources();
        if (this.talkbackName.startsWith("@string/") && (identifier = resources.getIdentifier(this.talkbackName, "string", GalleryApp.sGetAndroidContext().getPackageName())) != 0) {
            return resources.getString(identifier);
        }
        return this.talkbackName;
    }

    /* loaded from: classes2.dex */
    public static class TextPieceInfo implements Parcelable {
        public static final Parcelable.Creator<TextPieceInfo> CREATOR = new Parcelable.Creator<TextPieceInfo>() { // from class: com.miui.gallery.editor.photo.core.imports.text.watermark.WatermarkInfo.TextPieceInfo.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public TextPieceInfo mo912createFromParcel(Parcel parcel) {
                return new TextPieceInfo(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public TextPieceInfo[] mo913newArray(int i) {
                return new TextPieceInfo[i];
            }
        };
        public int align;
        public String font;
        public float[] frameRect;
        public boolean isBold;
        public int isVerticalText;
        public float letterSpacing;
        public int lines;
        public float minSize;
        public float size;
        public String text;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeFloatArray(this.frameRect);
            parcel.writeString(this.text);
            parcel.writeFloat(this.size);
            parcel.writeFloat(this.minSize);
            parcel.writeInt(this.isVerticalText);
            parcel.writeString(this.font);
            parcel.writeInt(this.align);
            parcel.writeFloat(this.letterSpacing);
            parcel.writeByte(this.isBold ? (byte) 1 : (byte) 0);
            parcel.writeInt(this.lines);
        }

        public TextPieceInfo() {
        }

        public TextPieceInfo(Parcel parcel) {
            this.frameRect = parcel.createFloatArray();
            this.text = parcel.readString();
            this.size = parcel.readFloat();
            this.minSize = parcel.readFloat();
            this.isVerticalText = parcel.readInt();
            this.font = parcel.readString();
            this.align = parcel.readInt();
            this.letterSpacing = parcel.readFloat();
            this.isBold = parcel.readByte() != 0;
            this.lines = parcel.readInt();
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(this.width);
        parcel.writeFloat(this.height);
        parcel.writeInt(this.version);
        parcel.writeFloat(this.offsetX);
        parcel.writeFloat(this.offsetY);
        parcel.writeString(this.icon);
        parcel.writeString(this.bgPost);
        parcel.writeFloatArray(this.bgPostRect);
        parcel.writeString(this.name);
        parcel.writeString(this.talkbackName);
        ParcelableGenericUtils.writeList(parcel, i, this.textPieceList);
    }

    public WatermarkInfo(Parcel parcel) {
        this.width = parcel.readFloat();
        this.height = parcel.readFloat();
        this.version = parcel.readInt();
        this.offsetX = parcel.readFloat();
        this.offsetY = parcel.readFloat();
        this.icon = parcel.readString();
        this.bgPost = parcel.readString();
        this.bgPostRect = parcel.createFloatArray();
        this.name = parcel.readString();
        this.talkbackName = parcel.readString();
        this.textPieceList = ParcelableGenericUtils.readList(parcel);
    }
}
