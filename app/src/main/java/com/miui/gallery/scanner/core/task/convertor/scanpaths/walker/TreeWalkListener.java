package com.miui.gallery.scanner.core.task.convertor.scanpaths.walker;

import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/* loaded from: classes2.dex */
public interface TreeWalkListener {
    void postVisitDirectory(Path path);

    FileVisitResult visit(Path path, BasicFileAttributes basicFileAttributes, boolean z);
}
