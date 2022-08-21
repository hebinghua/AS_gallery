package com.miui.gallery.scanner.core.task.convertor.scanpaths;

import android.content.Context;
import com.miui.gallery.scanner.core.model.OwnerAlbumEntry;
import com.miui.gallery.scanner.core.model.OwnerEntry;
import com.miui.gallery.scanner.core.model.OwnerItemEntry;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/* loaded from: classes2.dex */
public class DatabaseRecords {
    public final Map<String, OwnerEntry> mRecords;

    public DatabaseRecords(Context context, Path path, ScanTaskConfig scanTaskConfig) {
        if (scanTaskConfig.isCredible()) {
            this.mRecords = Collections.emptyMap();
        } else if (path.toFile().isDirectory()) {
            this.mRecords = new HashMap(OwnerAlbumEntry.fromDirectoryPath(context, path, scanTaskConfig));
        } else if (path.toFile().isFile()) {
            this.mRecords = new HashMap(OwnerItemEntry.fromFilePath(context, path));
        } else {
            HashMap hashMap = new HashMap(OwnerAlbumEntry.fromDirectoryPath(context, path, scanTaskConfig));
            this.mRecords = hashMap;
            hashMap.putAll(new HashMap(OwnerItemEntry.fromFilePath(context, path)));
        }
    }

    public OwnerEntry visit(String str, boolean z) {
        String lowerCase = str.toLowerCase();
        Map<String, OwnerEntry> map = this.mRecords;
        OwnerItemEntry remove = z ? map.remove(lowerCase) : map.get(lowerCase);
        if (remove != null) {
            return remove;
        }
        for (OwnerEntry ownerEntry : this.mRecords.values()) {
            if (ownerEntry instanceof OwnerAlbumEntry) {
                Map<String, OwnerItemEntry> contents = ((OwnerAlbumEntry) ownerEntry).getContents();
                remove = z ? contents.remove(lowerCase) : contents.get(lowerCase);
                if (remove != null) {
                    break;
                }
            }
        }
        return remove;
    }

    public Set<Map.Entry<String, OwnerEntry>> entrySet() {
        return this.mRecords.entrySet();
    }
}
