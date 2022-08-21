package com.miui.gallery.storage.strategies.base;

import android.os.ParcelFileDescriptor;
import androidx.documentfile.provider.DocumentFile;
import java.io.InputStream;
import java.io.OutputStream;

/* compiled from: IExtendedStorageOperator.kt */
/* loaded from: classes2.dex */
public interface IExtendedStorageOperator {
    boolean apply(DocumentFile documentFile);

    boolean copyFile(String str, String str2, String str3);

    boolean moveFile(String str, String str2, String str3);

    ParcelFileDescriptor openFileDescriptor(DocumentFile documentFile, String str);

    InputStream openInputStream(DocumentFile documentFile);

    OutputStream openOutputStream(DocumentFile documentFile);

    boolean setLastModified(DocumentFile documentFile, long j);
}
