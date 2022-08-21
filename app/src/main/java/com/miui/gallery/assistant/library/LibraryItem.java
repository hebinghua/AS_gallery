package com.miui.gallery.assistant.library;

import android.content.Context;
import android.text.TextUtils;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.google.gson.annotations.SerializedName;
import com.miui.gallery.GalleryApp;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.io.File;

/* loaded from: classes.dex */
public class LibraryItem {
    @SerializedName("extraInfo")
    private String mExtraInfo;
    @SerializedName("id")
    private long mId;
    @SerializedName("text")
    private String mName;
    @SerializedName("parentId")
    private long mParentId;
    @SerializedName("sha1Base16")
    private String mSha1;
    @SerializedName(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE)
    private long mSize;
    @SerializedName(nexExportFormat.TAG_FORMAT_TYPE)
    private String mType;
    public transient boolean mIsLocal = false;
    public transient boolean mIsLoaded = false;

    public String getName() {
        return this.mName;
    }

    public String getSha1() {
        return this.mSha1;
    }

    public long getId() {
        return this.mId;
    }

    public long getParentId() {
        return this.mParentId;
    }

    public String getExtraInfo() {
        return this.mExtraInfo;
    }

    public boolean isLocal() {
        return this.mIsLocal;
    }

    public boolean isLoaded() {
        return this.mIsLoaded;
    }

    public void setLoaded(boolean z) {
        this.mIsLoaded = z;
    }

    public boolean isExist(long j) {
        return isLocal() || LibraryUtils.isLibraryItemExist(GalleryApp.sGetAndroidContext(), j, this);
    }

    public String getTargetPath(Context context, long j) {
        return LibraryUtils.getLibraryDirPath(context, j) + File.separator + this.mName;
    }

    public boolean isTypeSo() {
        return TextUtils.equals(this.mType, "so");
    }
}
