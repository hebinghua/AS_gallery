package com.miui.gallery.magic.matting.adapter;

import android.text.TextUtils;
import com.miui.gallery.magic.fetch.MattingResourceFetcher;
import java.io.File;

/* loaded from: classes2.dex */
public class BackgroundIconItem extends IconItem {
    public String accessibilityText;
    public String basePath;
    public boolean downLoading;
    public boolean download;
    public long resId;
    public String resKey;
    public String resPath;

    public BackgroundIconItem(String str, boolean z, String str2, long j, String str3, boolean z2, String str4) {
        super(str, z);
        this.basePath = MattingResourceFetcher.INSTANCE.getResourceBasePath();
        this.resKey = str2;
        this.resId = j;
        this.accessibilityText = str3;
        this.download = z2;
        if (!TextUtils.isEmpty(str4)) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.basePath);
            sb.append(str2);
            String str5 = File.separator;
            sb.append(str5);
            sb.append(j);
            sb.append(str5);
            sb.append(str4);
            this.resPath = sb.toString();
        }
    }

    public boolean isDownload() {
        return this.download;
    }

    public void setDownload(boolean z) {
        this.download = z;
    }

    public String getResPath() {
        return TextUtils.isEmpty(this.resPath) ? "" : this.resPath;
    }

    public boolean isDownLoading() {
        return this.downLoading;
    }

    public void setDownLoading(boolean z) {
        this.downLoading = z;
    }

    public String getAccessibilityText() {
        return this.accessibilityText;
    }

    public String getResKey() {
        return this.resKey;
    }

    public long getResId() {
        return this.resId;
    }
}
