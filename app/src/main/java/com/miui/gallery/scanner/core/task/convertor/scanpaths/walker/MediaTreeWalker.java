package com.miui.gallery.scanner.core.task.convertor.scanpaths.walker;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import ch.qos.logback.classic.spi.CallerData;
import com.miui.gallery.scanner.core.scanner.MediaScannerHelper;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.DecodeInfoHelper;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.StorageUtils;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.a.j;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/* loaded from: classes2.dex */
public class MediaTreeWalker extends AbsTreeWalker {
    public MediaTreeWalker(Context context, Path path, ScanTaskConfig scanTaskConfig) {
        super(context, path, scanTaskConfig);
    }

    @Override // com.miui.gallery.scanner.core.task.convertor.scanpaths.walker.AbsTreeWalker
    public void walk(TreeWalkListener treeWalkListener) throws IOException {
        BasicFileAttributes basicFileAttributes;
        if (AbsTreeWalker.isInBlackList(this.mRoot.toString())) {
            return;
        }
        String mimeType = BaseFileMimeUtil.getMimeType(this.mRoot.toString());
        if (BaseFileMimeUtil.isImageFromMimeType(mimeType)) {
            walkSingleInternal(MediaStore.Images.Media.getContentUri("external"), treeWalkListener);
        } else if (BaseFileMimeUtil.isVideoFromMimeType(mimeType)) {
            walkSingleInternal(MediaStore.Video.Media.getContentUri("external"), treeWalkListener);
        } else {
            try {
                basicFileAttributes = Files.readAttributes(this.mRoot, BasicFileAttributes.class, new LinkOption[0]);
            } catch (NoSuchFileException unused) {
                basicFileAttributes = null;
            }
            if (basicFileAttributes == null || !basicFileAttributes.isDirectory() || !MediaScannerHelper.isScannableDirectory(this.mRoot.toFile()) || AbsTreeWalker.isInBlackList(this.mRoot.toString()) || FileVisitResult.SKIP_SUBTREE == treeWalkListener.visit(this.mRoot, basicFileAttributes, false)) {
                return;
            }
            walkTreeInternal(MediaStore.Images.Media.getContentUri("external"), treeWalkListener);
            walkTreeInternal(MediaStore.Video.Media.getContentUri("external"), treeWalkListener);
        }
    }

    public final void walkSingleInternal(Uri uri, TreeWalkListener treeWalkListener) {
        MediaStoreEntry mediaStoreEntry = (MediaStoreEntry) SafeDBUtil.safeQuery(this.mContext, uri, MediaStoreEntry.PROJECTION, "_data=?", new String[]{this.mRoot.toString()}, (String) null, new SafeDBUtil.QueryHandler<MediaStoreEntry>() { // from class: com.miui.gallery.scanner.core.task.convertor.scanpaths.walker.MediaTreeWalker.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle */
            public MediaStoreEntry mo1808handle(Cursor cursor) {
                if (cursor == null || cursor.getCount() <= 0) {
                    return null;
                }
                cursor.moveToFirst();
                return new MediaStoreEntry(cursor);
            }
        });
        if (mediaStoreEntry == null || !mediaStoreEntry.isValid()) {
            return;
        }
        treeWalkListener.visit(this.mRoot, mediaStoreEntry.mAttrs, true);
    }

    public final void walkTreeInternal(Uri uri, TreeWalkListener treeWalkListener) {
        String relativePath = StorageUtils.getRelativePath(this.mContext, this.mRoot.toString(), false);
        if (relativePath == null) {
            return;
        }
        String str = File.separator;
        if (!relativePath.endsWith(str)) {
            relativePath = relativePath + str;
        }
        List<MediaStoreEntry> queryChildren = queryChildren(this.mContext, uri, relativePath, this.mConfig.isRecursiveScan());
        if (!BaseMiscUtil.isValid(queryChildren)) {
            return;
        }
        Iterator<MediaStoreEntry> it = queryChildren.iterator();
        while (it.hasNext()) {
            MediaStoreEntry next = it.next();
            it.remove();
            if (next != null && next.isValid()) {
                treeWalkListener.visit(Paths.get(next.mData, new String[0]), next.mAttrs, true);
            }
        }
    }

    public static List<MediaStoreEntry> queryChildren(Context context, Uri uri, String str, boolean z) {
        String[] strArr = MediaStoreEntry.PROJECTION;
        StringBuilder sb = new StringBuilder();
        sb.append("relative_path");
        sb.append(z ? " like " : "=");
        sb.append(CallerData.NA);
        String sb2 = sb.toString();
        String[] strArr2 = new String[1];
        if (z) {
            str = str + "%";
        }
        strArr2[0] = str;
        return (List) SafeDBUtil.safeQuery(context, uri, strArr, sb2, strArr2, (String) null, new SafeDBUtil.QueryHandler<List<MediaStoreEntry>>() { // from class: com.miui.gallery.scanner.core.task.convertor.scanpaths.walker.MediaTreeWalker.2
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public List<MediaStoreEntry> mo1808handle(Cursor cursor) {
                if (cursor == null || cursor.getCount() <= 0) {
                    return Collections.emptyList();
                }
                LinkedList linkedList = new LinkedList();
                while (cursor.moveToNext()) {
                    linkedList.add(new MediaStoreEntry(cursor));
                }
                return linkedList;
            }
        });
    }

    /* loaded from: classes2.dex */
    public static class MediaStoreEntry {
        public static final String[] PROJECTION = {"_data", "_size", "date_modified", nexExportFormat.TAG_FORMAT_WIDTH, nexExportFormat.TAG_FORMAT_HEIGHT, j.c};
        public final BasicFileAttributes mAttrs;
        public final String mData;
        public final long mDateModified;
        public final long mSize;

        public MediaStoreEntry(Cursor cursor) {
            String string = cursor.getString(0);
            this.mData = string;
            long j = cursor.getLong(1);
            this.mSize = j;
            long j2 = cursor.getLong(2);
            this.mDateModified = j2;
            this.mAttrs = new MediaStoreAttrs(j2, j);
            if (BaseFileMimeUtil.isImageFromMimeType(BaseFileMimeUtil.getMimeType(string))) {
                DecodeInfoHelper.DecodeInfo decodeInfo = new DecodeInfoHelper.DecodeInfo();
                decodeInfo.lastModified = j2;
                decodeInfo.fileUri = Uri.fromFile(new File(string));
                decodeInfo.mediaUri = ContentUris.withAppendedId(MediaStore.Files.getContentUri("external"), cursor.getLong(5));
                decodeInfo.width = cursor.getInt(3);
                decodeInfo.height = cursor.getInt(4);
                DecodeInfoHelper.getInstance().put(decodeInfo);
            }
        }

        public boolean isValid() {
            return !this.mData.startsWith(".") && MediaScannerHelper.isScannableDirectory(new File(BaseFileUtils.getParentFolderPath(this.mData)));
        }

        /* loaded from: classes2.dex */
        public static class MediaStoreAttrs implements BasicFileAttributes {
            public final long mDateModified;
            public final long mSize;

            @Override // java.nio.file.attribute.BasicFileAttributes
            public FileTime creationTime() {
                return null;
            }

            @Override // java.nio.file.attribute.BasicFileAttributes
            public Object fileKey() {
                return null;
            }

            @Override // java.nio.file.attribute.BasicFileAttributes
            public boolean isDirectory() {
                return false;
            }

            @Override // java.nio.file.attribute.BasicFileAttributes
            public boolean isOther() {
                return false;
            }

            @Override // java.nio.file.attribute.BasicFileAttributes
            public boolean isRegularFile() {
                return true;
            }

            @Override // java.nio.file.attribute.BasicFileAttributes
            public boolean isSymbolicLink() {
                return false;
            }

            @Override // java.nio.file.attribute.BasicFileAttributes
            public FileTime lastAccessTime() {
                return null;
            }

            public MediaStoreAttrs(long j, long j2) {
                this.mDateModified = j;
                this.mSize = j2;
            }

            @Override // java.nio.file.attribute.BasicFileAttributes
            public FileTime lastModifiedTime() {
                return FileTime.from(this.mDateModified, TimeUnit.SECONDS);
            }

            @Override // java.nio.file.attribute.BasicFileAttributes
            public long size() {
                return this.mSize;
            }
        }
    }
}
