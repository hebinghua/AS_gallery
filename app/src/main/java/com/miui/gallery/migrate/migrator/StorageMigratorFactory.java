package com.miui.gallery.migrate.migrator;

import android.content.Context;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.cloudcontrol.strategies.MigrateStrategy;
import com.miui.gallery.migrate.migrator.gallery.GalleryMigrator;
import com.miui.gallery.migrate.migrator.gallery.GallerySecretMigrator;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public class StorageMigratorFactory {
    public static List<AbsMigrator> create(Context context, long j) {
        LinkedList linkedList = new LinkedList();
        List<MigrateStrategy.Album> migrateStrategyAlbums = CloudControlStrategyHelper.getMigrateStrategyAlbums();
        if (!BaseMiscUtil.isValid(migrateStrategyAlbums)) {
            return Collections.emptyList();
        }
        for (MigrateStrategy.Album album : migrateStrategyAlbums) {
            long version = album.getVersion();
            if (version != (j & version)) {
                try {
                    AbsMigrator newMigrator = newMigrator(context, album);
                    if (newMigrator != null) {
                        linkedList.add(newMigrator);
                    }
                } catch (RuntimeException e) {
                    DefaultLogger.e("StorageMigratorFactory", e.getMessage());
                }
            }
        }
        return linkedList;
    }

    public static AbsMigrator newMigrator(Context context, MigrateStrategy.Album album) {
        long version = album.getVersion();
        String type = album.getType();
        type.hashCode();
        char c = 65535;
        switch (type.hashCode()) {
            case -991966464:
                if (type.equals("third-party")) {
                    c = 0;
                    break;
                }
                break;
            case -425475701:
                if (type.equals("gallery-secret")) {
                    c = 1;
                    break;
                }
                break;
            case -196315310:
                if (type.equals("gallery")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return new ThirdPartyMigrator(context, version, album.getOldPath(), album.getNewPath());
            case 1:
                return new GallerySecretMigrator(context, version);
            case 2:
                return new GalleryMigrator(context, version);
            default:
                return new EmptyMigrator(context, version);
        }
    }
}
