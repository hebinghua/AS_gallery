package com.android.internal.storage;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.storage.DiskInfo;
import android.os.storage.StorageVolume;
import android.os.storage.VolumeInfo;
import android.text.TextUtils;
import com.android.internal.storage.StorageInfo;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes.dex */
public class StorageManager {

    /* loaded from: classes.dex */
    public static class Singleton {
        public static final StorageManager INSTANCE = new StorageManager();
    }

    public static StorageManager getInstance() {
        return Singleton.INSTANCE;
    }

    public StorageManager() {
    }

    public List<StorageInfo> getStorageInfos(Context context) {
        LinkedList linkedList = new LinkedList();
        if (context == null) {
            return linkedList;
        }
        android.os.storage.StorageManager storageManager = (android.os.storage.StorageManager) context.getSystemService("storage");
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                List<VolumeInfo> volumes = storageManager.getVolumes();
                if (volumes != null) {
                    for (VolumeInfo volumeInfo : volumes) {
                        StorageInfo storageInfo = toStorageInfo(volumeInfo);
                        if (storageInfo != null) {
                            linkedList.add(storageInfo);
                        }
                    }
                }
            } else {
                StorageVolume[] volumeList = storageManager.getVolumeList();
                if (volumeList != null) {
                    for (StorageVolume storageVolume : volumeList) {
                        StorageInfo storageInfo2 = toStorageInfo(context, storageVolume);
                        if (storageInfo2 != null) {
                            linkedList.add(storageInfo2);
                        }
                    }
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
            StorageInfo.Builder builder = new StorageInfo.Builder(Environment.getExternalStorageDirectory().getAbsolutePath());
            builder.setMounted(true);
            builder.setPrimary(true);
            linkedList.add(builder.build());
        }
        return linkedList;
    }

    public static StorageInfo toStorageInfo(Context context, StorageVolume storageVolume) {
        String path;
        if (TextUtils.isEmpty(storageVolume.getPath())) {
            return null;
        }
        if (storageVolume.isPrimary() && "mounted".equals(Environment.getExternalStorageState())) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            path = storageVolume.getPath();
        }
        StorageInfo.Builder builder = new StorageInfo.Builder(path);
        builder.setWrapped(storageVolume);
        builder.setDescription(storageVolume.getDescription(context));
        builder.setMounted("mounted".equalsIgnoreCase(storageVolume.getState()));
        boolean z = true;
        builder.setPrimary(storageVolume.isPrimary() && "mounted".equals(Environment.getExternalStorageState()));
        if (!storageVolume.isRemovable() || !storageVolume.getPath().startsWith("/storage/sdcard")) {
            z = false;
        }
        builder.setSd(z);
        builder.setUuid(storageVolume.getUuid());
        return builder.build();
    }

    public static StorageInfo toStorageInfo(VolumeInfo volumeInfo) {
        String absolutePath;
        File path = volumeInfo.getPath();
        if (path == null || TextUtils.isEmpty(path.getAbsolutePath())) {
            return null;
        }
        int type = volumeInfo.getType();
        if (type != 2 && type != 0) {
            return null;
        }
        if (volumeInfo.isPrimary() && "mounted".equals(Environment.getExternalStorageState())) {
            absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            absolutePath = path.getAbsolutePath();
        }
        StorageInfo.Builder builder = new StorageInfo.Builder(absolutePath);
        builder.setWrapped(volumeInfo);
        builder.setDescription(volumeInfo.getDescription());
        boolean z = true;
        builder.setMounted(volumeInfo.getState() == 2);
        builder.setVisible(volumeInfo.isVisible());
        if (!volumeInfo.isPrimary() || !"mounted".equals(Environment.getExternalStorageState())) {
            z = false;
        }
        builder.setPrimary(z);
        DiskInfo disk = volumeInfo.getDisk();
        if (disk != null) {
            builder.setSd(disk.isSd());
            builder.setUsb(disk.isUsb());
        }
        builder.setUuid(volumeInfo.getFsUuid());
        return builder.build();
    }
}
