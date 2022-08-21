package com.miui.gallery.cloudcontrol.strategies;

import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;
import com.miui.gallery.request.PicToPdfHelper;

/* loaded from: classes.dex */
public class CreationStrategy extends BaseStrategy {
    @SerializedName("collage_entry")
    private boolean mIsCollageEntryEnable;
    @SerializedName("photo_movie_entry")
    private boolean mIsPhotoMovieEntryEnable;
    @SerializedName("print_entry")
    private boolean mIsPrintEntryEnable;
    @SerializedName("print_intent_uri")
    private String mPrintIntentUri = "xiaomiprint://start.uri.activity?action=createWork";
    @SerializedName("min_print_version_code")
    private int mMinPrintVersionCode = 34;
    @SerializedName("print_max_image_count")
    private int mPrintMaxImageCount = 200;

    public boolean isVlogEntryEnable() {
        return true;
    }

    public CreationStrategy(boolean z, boolean z2, boolean z3) {
        this.mIsCollageEntryEnable = z;
        this.mIsPhotoMovieEntryEnable = z2;
        this.mIsPrintEntryEnable = z3;
    }

    public String getPrintIntentUri() {
        return this.mPrintIntentUri;
    }

    public boolean isCollageEntryEnable() {
        return this.mIsCollageEntryEnable;
    }

    public boolean isPhotoMovieEntryEnable() {
        return this.mIsPhotoMovieEntryEnable;
    }

    public boolean isCreatePdfEnable() {
        return PicToPdfHelper.isPicToPdfSupport();
    }

    public boolean isPrintEntryEnable() {
        return this.mIsPrintEntryEnable;
    }

    public int getMinPrintVersionCode() {
        return this.mMinPrintVersionCode;
    }

    public int getPrintMaxImageCount() {
        int i = this.mPrintMaxImageCount;
        if (i > 0) {
            return i;
        }
        return 200;
    }

    public static CreationStrategy createDefault() {
        return new CreationStrategy(true, true, false);
    }

    @Override // com.miui.gallery.cloudcontrol.strategies.BaseStrategy
    public void doAdditionalProcessing() {
        if (TextUtils.isEmpty(this.mPrintIntentUri)) {
            this.mPrintIntentUri = "xiaomiprint://start.uri.activity?action=createWork";
        }
        if (this.mMinPrintVersionCode <= 0) {
            this.mMinPrintVersionCode = 34;
        }
        if (this.mPrintMaxImageCount <= 0) {
            this.mPrintMaxImageCount = 200;
        }
    }
}
