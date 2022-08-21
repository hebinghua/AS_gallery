package com.miui.gallery.migrate.migrator;

import com.miui.gallery.scanner.core.ScannerEngine;
import com.miui.gallery.scanner.utils.ScanCache;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

/* loaded from: classes2.dex */
public class MigrateUtils {
    public static void requestPauseScan(List<String> list) {
        List list2 = (List) ScanCache.getInstance().get("key_migrate_affected_paths");
        if (list2 == null) {
            list2 = Collections.synchronizedList(new LinkedList());
            ScanCache.getInstance().put("key_migrate_affected_paths", list2);
        }
        list2.addAll(list);
    }

    public static void requestResumeScan(List<String> list) {
        List list2 = (List) ScanCache.getInstance().get("key_migrate_affected_paths");
        if (list2 == null) {
            return;
        }
        list2.removeAll(list);
        ScannerEngine.getInstance().scanPathsAsync(list, 10);
    }

    public static void moveDirectory(Path path, Path path2, CopyOption... copyOptionArr) throws IOException {
        moveDirectory(path, path2, null, copyOptionArr);
    }

    public static void moveDirectory(final Path path, final Path path2, final BiConsumer<Path, Path> biConsumer, CopyOption... copyOptionArr) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() { // from class: com.miui.gallery.migrate.migrator.MigrateUtils.1
            @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
            public FileVisitResult preVisitDirectory(Path path3, BasicFileAttributes basicFileAttributes) {
                try {
                    Files.createDirectories(path3.compareTo(path) == 0 ? path2 : path2.resolve(path3.subpath(path.getNameCount(), path3.getNameCount())), new FileAttribute[0]);
                } catch (Exception e) {
                    DefaultLogger.e("MigrateUtils", "preVisitDirectory [%s] error, skip. %s", path3, e);
                }
                return FileVisitResult.CONTINUE;
            }

            /* JADX WARN: Removed duplicated region for block: B:51:0x00df  */
            /* JADX WARN: Removed duplicated region for block: B:52:0x00e1  */
            /* JADX WARN: Removed duplicated region for block: B:59:0x0108  */
            /* JADX WARN: Removed duplicated region for block: B:63:0x0110  */
            @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public java.nio.file.FileVisitResult visitFile(java.nio.file.Path r11, java.nio.file.attribute.BasicFileAttributes r12) {
                /*
                    Method dump skipped, instructions count: 276
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.migrate.migrator.MigrateUtils.AnonymousClass1.visitFile(java.nio.file.Path, java.nio.file.attribute.BasicFileAttributes):java.nio.file.FileVisitResult");
            }

            @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
            public FileVisitResult postVisitDirectory(Path path3, IOException iOException) {
                try {
                    Files.delete(path3);
                } catch (Exception e) {
                    DefaultLogger.e("MigrateUtils", "postVisitDirectory [%s] error, skip. %s", path3, e);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
