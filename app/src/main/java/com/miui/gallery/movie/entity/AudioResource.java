package com.miui.gallery.movie.entity;

import com.miui.gallery.movie.ui.factory.AudioFactory;

/* loaded from: classes2.dex */
public class AudioResource extends MovieResource {
    public int resType;

    @Override // com.miui.gallery.movie.entity.MovieResource
    public String getStatTypeString() {
        return "audio";
    }

    public AudioResource(int i, String str, int i2, int i3) {
        super(str, i2, i3);
        this.resType = i;
        this.isPackageAssets = true;
        this.downloadState = 17;
    }

    public AudioResource() {
    }

    public AudioResource(String str) {
        this.srcPath = str;
        this.nameKey = "local";
        this.resType = 2;
        this.isPackageAssets = true;
    }

    @Override // com.miui.gallery.movie.entity.MovieResource
    public String getDownloadSrcPath() {
        if (this.isPackageAssets) {
            return this.srcPath;
        }
        return AudioFactory.getAudioDownloadPath(this.pathKey);
    }

    public int getResType() {
        return this.resType;
    }

    @Override // com.miui.gallery.movie.entity.MovieResource
    public String getStatNameString() {
        return "audio-" + this.label;
    }
}
