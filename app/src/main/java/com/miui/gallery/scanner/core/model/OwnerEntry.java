package com.miui.gallery.scanner.core.model;

import android.text.TextUtils;
import java.nio.file.attribute.BasicFileAttributes;

/* loaded from: classes2.dex */
public abstract class OwnerEntry extends Entry {
    public long mDateModified;
    public int mLocalFlag;
    public String mServerStatus;

    public boolean isLatest(BasicFileAttributes basicFileAttributes) {
        return this.mDateModified / 1000 == basicFileAttributes.lastModifiedTime().toMillis() / 1000;
    }

    public boolean hasSynced() {
        return this.mLocalFlag == 0 || (!TextUtils.isEmpty(this.mServerStatus) && !this.mServerStatus.equals("temp"));
    }
}
