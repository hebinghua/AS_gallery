package com.miui.gallery.editor.photo.utils;

import android.os.MemoryFile;
import android.os.ParcelFileDescriptor;
import com.miui.gallery.util.ReflectUtils;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

/* loaded from: classes2.dex */
public class MemoryFileUtils {
    public static MemoryFile createMemoryFile(String str, int i) {
        try {
            return new MemoryFile(str, i);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static FileInputStream getInputStream(ParcelFileDescriptor parcelFileDescriptor) {
        if (parcelFileDescriptor == null) {
            throw new IllegalArgumentException("ParcelFileDescriptor can not be null");
        }
        return new FileInputStream(parcelFileDescriptor.getFileDescriptor());
    }

    public static ParcelFileDescriptor getParcelFileDescriptor(MemoryFile memoryFile) {
        if (memoryFile == null) {
            throw new IllegalArgumentException("memoryFile can not be null");
        }
        return (ParcelFileDescriptor) ReflectUtils.getInstance("android.os.ParcelFileDescriptor", getFileDescriptor(memoryFile));
    }

    public static FileDescriptor getFileDescriptor(MemoryFile memoryFile) {
        if (memoryFile == null) {
            throw new IllegalArgumentException("memoryFile can not be null");
        }
        return (FileDescriptor) ReflectUtils.invoke("android.os.MemoryFile", memoryFile, "getFileDescriptor", new Object[0]);
    }
}
