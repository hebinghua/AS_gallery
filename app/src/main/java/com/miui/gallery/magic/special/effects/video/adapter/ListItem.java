package com.miui.gallery.magic.special.effects.video.adapter;

import com.miui.gallery.magic.fetch.AudioResourceFetcher;
import com.miui.gallery.magic.fetch.VideoResourceFetcher;
import java.io.File;

/* loaded from: classes2.dex */
public class ListItem {
    public String baseMusicPath;
    public String baseVideoPath;
    public boolean downLoaded;
    public boolean downLoading;
    public String icon;
    public boolean isHaveMusic;
    public String music;
    public long resId;
    public String resKey;
    public String resName;
    public String resPath;
    public String title;
    public ItemType type;

    /* loaded from: classes2.dex */
    public enum ItemType {
        NONE,
        EDIT,
        SELECT,
        CLOSE,
        SELECT_MUSIC
    }

    public ListItem(String str, String str2, String str3, long j, boolean z, ItemType itemType) {
        this(str, str2, str3, j, z, itemType, "", false);
    }

    public ListItem(String str, String str2, String str3, long j, boolean z, ItemType itemType, String str4, boolean z2) {
        this.baseVideoPath = VideoResourceFetcher.INSTANCE.getResourceBasePath();
        this.baseMusicPath = AudioResourceFetcher.INSTANCE.getResourceBasePath();
        this.title = "";
        this.music = "";
        this.isHaveMusic = false;
        this.icon = str;
        this.title = str2;
        this.resKey = str3;
        this.resId = j;
        this.downLoaded = z;
        this.type = itemType;
        this.resName = str4;
        if (z2) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.baseMusicPath);
            sb.append(str3);
            String str5 = File.separator;
            sb.append(str5);
            sb.append(j);
            sb.append(str5);
            this.resPath = sb.toString();
            return;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.baseVideoPath);
        sb2.append(str3);
        String str6 = File.separator;
        sb2.append(str6);
        sb2.append(j);
        sb2.append(str6);
        this.resPath = sb2.toString();
    }

    public String getResName() {
        return this.resName;
    }

    public String getResPath() {
        return this.resPath;
    }

    public long getResId() {
        return this.resId;
    }

    public String getResKey() {
        return this.resKey;
    }

    public String getTitle() {
        return this.title;
    }

    public boolean isDownLoaded() {
        return this.downLoaded;
    }

    public void setDownLoaded(boolean z) {
        this.downLoaded = z;
    }

    public String getIcon() {
        return this.icon;
    }

    public ItemType getType() {
        return this.type;
    }

    public void setType(ItemType itemType) {
        this.type = itemType;
    }

    public boolean isDownLoading() {
        return this.downLoading;
    }

    public void setDownLoading(boolean z) {
        this.downLoading = z;
    }
}
