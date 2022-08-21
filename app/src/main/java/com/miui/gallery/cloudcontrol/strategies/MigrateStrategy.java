package com.miui.gallery.cloudcontrol.strategies;

import com.google.gson.annotations.SerializedName;
import com.miui.gallery.util.BaseMiscUtil;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.List;

/* loaded from: classes.dex */
public class MigrateStrategy extends BaseStrategy {
    @SerializedName("albums")
    private List<Album> mAlbums;
    @SerializedName("version")
    private int mVersion;

    public List<Album> getAlbums() {
        return this.mAlbums;
    }

    public int getState() {
        return this.mVersion;
    }

    public String toString() {
        return "AdoptScopedStorageStrategy{mVersion=" + this.mVersion + ", mAlbums=" + this.mAlbums + "}";
    }

    @Override // com.miui.gallery.cloudcontrol.strategies.BaseStrategy
    public void doAdditionalProcessing() {
        if (!BaseMiscUtil.isValid(this.mAlbums)) {
            return;
        }
        for (Album album : this.mAlbums) {
            album.doAdditionalProcessing();
        }
    }

    /* loaded from: classes.dex */
    public static class Album {
        @SerializedName("new")
        private String mNewPath;
        @SerializedName("old")
        private String mOldPath;
        @SerializedName(nexExportFormat.TAG_FORMAT_TYPE)
        private String mType;
        @SerializedName("version")
        private int mVersion;

        public void doAdditionalProcessing() {
        }

        public int getVersion() {
            return this.mVersion;
        }

        public String getType() {
            return this.mType;
        }

        public String getOldPath() {
            return this.mOldPath;
        }

        public String getNewPath() {
            return this.mNewPath;
        }

        public String toString() {
            return "[version=" + this.mVersion + ", type=" + this.mType + ", old=" + this.mOldPath + ", new=" + this.mNewPath + "]";
        }
    }
}
