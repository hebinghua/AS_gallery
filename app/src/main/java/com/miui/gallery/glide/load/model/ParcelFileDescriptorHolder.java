package com.miui.gallery.glide.load.model;

import android.os.ParcelFileDescriptor;
import androidx.documentfile.provider.DocumentFile;
import java.io.IOException;

/* loaded from: classes2.dex */
public class ParcelFileDescriptorHolder extends DataHolder<ParcelFileDescriptor> {
    public String linkedFilePath;
    public DocumentFile tempFile;

    public ParcelFileDescriptorHolder(ParcelFileDescriptor parcelFileDescriptor, String str) {
        super(parcelFileDescriptor);
        this.linkedFilePath = str;
    }

    public ParcelFileDescriptorHolder(ParcelFileDescriptor parcelFileDescriptor, int i, DocumentFile documentFile) {
        super(parcelFileDescriptor, i);
        this.tempFile = documentFile;
        this.linkedFilePath = documentFile.getUri().toString();
    }

    @Override // com.miui.gallery.glide.load.model.DataHolder
    public void close() throws IOException {
        DocumentFile documentFile;
        ((ParcelFileDescriptor) this.data).close();
        int i = this.requestCode;
        if (i == -1 || (documentFile = this.tempFile) == null) {
            return;
        }
        ThumbFetcherManager.release(DocumentFile.class, documentFile, i);
    }
}
