package com.miui.gallery.editor.photo.core.imports.text.typeface;

import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.imports.text.TextFontConfig;
import com.miui.gallery.net.resource.LocalResource;
import java.io.File;
import java.text.DecimalFormat;

/* loaded from: classes2.dex */
public class TextStyle extends LocalResource implements Parcelable, Comparable<TextStyle> {
    public static final Parcelable.Creator<TextStyle> CREATOR = new Parcelable.Creator<TextStyle>() { // from class: com.miui.gallery.editor.photo.core.imports.text.typeface.TextStyle.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public TextStyle mo904createFromParcel(Parcel parcel) {
            return new TextStyle(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public TextStyle[] mo905newArray(int i) {
            return new TextStyle[i];
        }
    };
    public int darkmode;
    public int defaultNameResId;
    public int index;
    public int state;
    public Typeface typeFace;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public TextStyle() {
        this.defaultNameResId = R.string.photo_editor_font_default;
        this.state = 19;
    }

    public TextStyle(Parcel parcel) {
        this.defaultNameResId = R.string.photo_editor_font_default;
        this.state = 19;
        this.defaultNameResId = parcel.readInt();
        this.state = parcel.readInt();
    }

    public static TextStyle getLocalTextStyle() {
        TextStyle textStyle = new TextStyle();
        textStyle.state = 17;
        textStyle.type = "type_local";
        textStyle.defaultNameResId = R.string.photo_editor_font_default;
        textStyle.typeFace = Typeface.create("mipro-medium", 0);
        return textStyle;
    }

    public void setState(int i) {
        this.state = i;
    }

    public void setIndex(int i) {
        this.index = i;
    }

    public void setDarkmode(int i) {
        this.darkmode = i;
    }

    public boolean isDarkModeData() {
        return this.darkmode == 1;
    }

    public void setTypeFace(Typeface typeface) {
        this.typeFace = typeface;
    }

    public Typeface getTypeFace() {
        return this.typeFace;
    }

    public boolean isDownloaded() {
        return this.state == 17;
    }

    public boolean isDownloading() {
        return this.state == 18;
    }

    public boolean isNeedDownload() {
        int i = this.state;
        return i == 19 || i == 20;
    }

    public boolean isDownloadSuccess() {
        return this.state == 0;
    }

    public boolean isExtra() {
        return "type_extra".equals(this.type);
    }

    public boolean isLocal() {
        return "type_local".equals(this.type);
    }

    public int getState() {
        return this.state;
    }

    public String getDownloadFilePath() {
        return getDownloadSrcPath() + ".zip";
    }

    public String getFilePath() {
        return getDownloadSrcPath() + ".ttf";
    }

    public final String getDownloadSrcPath() {
        return TextFontConfig.FONT_PATH + File.separator + this.id;
    }

    public String getIconUrl() {
        return this.icon;
    }

    public String getFontSize() {
        DecimalFormat decimalFormat = new DecimalFormat(".0");
        return decimalFormat.format(((float) this.size) / 1000000.0f) + "M";
    }

    public String getName() {
        return this.label;
    }

    public int getDefaultNameResId() {
        return this.defaultNameResId;
    }

    @Override // java.lang.Comparable
    public int compareTo(TextStyle textStyle) {
        return this.index - textStyle.index;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.defaultNameResId);
        parcel.writeInt(this.state);
    }
}
