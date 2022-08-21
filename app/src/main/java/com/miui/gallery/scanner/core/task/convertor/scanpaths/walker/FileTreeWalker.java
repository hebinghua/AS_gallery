package com.miui.gallery.scanner.core.task.convertor.scanpaths.walker;

import android.content.Context;
import com.miui.gallery.scanner.core.scanner.MediaScannerHelper;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/* loaded from: classes2.dex */
public class FileTreeWalker extends AbsTreeWalker {
    public FileTreeWalker(Context context, Path path, ScanTaskConfig scanTaskConfig) {
        super(context, path, scanTaskConfig);
    }

    @Override // com.miui.gallery.scanner.core.task.convertor.scanpaths.walker.AbsTreeWalker
    public void walk(final TreeWalkListener treeWalkListener) throws IOException {
        if (!this.mRoot.toFile().exists()) {
            return;
        }
        if (this.mRoot.toFile().isFile() && !MediaScannerHelper.isScannableDirectory(this.mRoot.getParent().toFile())) {
            return;
        }
        Files.walkFileTree(this.mRoot, new SimpleFileVisitor<Path>() { // from class: com.miui.gallery.scanner.core.task.convertor.scanpaths.walker.FileTreeWalker.1
            @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
            public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes basicFileAttributes) {
                if (!FileTreeWalker.this.mConfig.isRecursiveScan() && !path.toAbsolutePath().equals(FileTreeWalker.this.mRoot)) {
                    return FileVisitResult.SKIP_SUBTREE;
                }
                if (!MediaScannerHelper.isScannableDirectory(path.toFile())) {
                    return FileVisitResult.SKIP_SUBTREE;
                }
                if (AbsTreeWalker.isInBlackList(path.toString())) {
                    return FileVisitResult.SKIP_SUBTREE;
                }
                return visitFile(path, basicFileAttributes);
            }

            @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
            public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) {
                if (!basicFileAttributes.isRegularFile() && !basicFileAttributes.isDirectory()) {
                    return FileVisitResult.SKIP_SUBTREE;
                }
                if (basicFileAttributes.isRegularFile() && (!FileTreeWalker.isMediaFile(path.toString()) || path.toFile().isHidden())) {
                    return FileVisitResult.SKIP_SUBTREE;
                }
                if (AbsTreeWalker.isInBlackList(path.getParent().toString())) {
                    return FileVisitResult.SKIP_SUBTREE;
                }
                return treeWalkListener.visit(path, basicFileAttributes, !basicFileAttributes.isDirectory());
            }

            @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
            public FileVisitResult postVisitDirectory(Path path, IOException iOException) {
                treeWalkListener.postVisitDirectory(path);
                return FileVisitResult.CONTINUE;
            }

            @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
            public FileVisitResult visitFileFailed(Path path, IOException iOException) {
                DefaultLogger.e("FileTreeWalker", "visit [%s] failed, error: [%s].", path.toString(), iOException.toString());
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static boolean isMediaFile(String str) {
        String mimeType = BaseFileMimeUtil.getMimeType(str);
        return BaseFileMimeUtil.isImageFromMimeType(mimeType) || BaseFileMimeUtil.isVideoFromMimeType(mimeType);
    }
}
