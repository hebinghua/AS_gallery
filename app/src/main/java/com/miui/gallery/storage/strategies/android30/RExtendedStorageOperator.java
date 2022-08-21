package com.miui.gallery.storage.strategies.android30;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import androidx.documentfile.provider.GalleryRawDocumentFile;
import com.miui.gallery.storage.exceptions.StorageException;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.IStorageStrategyHolder;
import com.miui.gallery.storage.strategies.base.AbsExtendedStorageOperator;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.MediaStoreUtils;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.List;
import kotlin.Triple;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringsJVMKt;

/* compiled from: RExtendedStorageOperator.kt */
/* loaded from: classes2.dex */
public final class RExtendedStorageOperator extends AbsExtendedStorageOperator {
    public static final Companion Companion = new Companion(null);
    public final Context context;
    public final IStorageStrategyHolder holder;
    public final List<Function1<Triple<String, String, String>, Boolean>> moveActions;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public RExtendedStorageOperator(Context context, IStorageStrategyHolder holder) {
        super(context, holder);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(holder, "holder");
        this.context = context;
        this.holder = holder;
        this.moveActions = CollectionsKt__CollectionsKt.listOf((Object[]) new Function1[]{new RExtendedStorageOperator$moveActions$1(this), new RExtendedStorageOperator$moveActions$2(this), new RExtendedStorageOperator$moveActions$3(this), new RExtendedStorageOperator$moveActions$4(this), new RExtendedStorageOperator$moveActions$5(this)});
    }

    @Override // com.miui.gallery.storage.strategies.base.AbsExtendedStorageOperator
    public List<Function1<Triple<String, String, String>, Boolean>> getMoveActions() {
        return this.moveActions;
    }

    public final boolean moveFileInner1(String str, String str2, String str3) {
        File file = new File(str);
        long lastModified = file.lastModified();
        File file2 = new File(str2);
        try {
            boolean renameTo = file.renameTo(file2);
            if (renameTo) {
                file2.setLastModified(lastModified);
            }
            return renameTo;
        } finally {
            apply(new GalleryRawDocumentFile(null, file));
            apply(new GalleryRawDocumentFile(null, file2));
        }
    }

    public final boolean moveFileInner2(String str, String str2, String str3) {
        DocumentFile documentFile;
        if (StringsKt__StringsJVMKt.equals(BaseFileUtils.getParentFolderPath(str), BaseFileUtils.getParentFolderPath(str2), true) && (documentFile = this.holder.getDocumentFile(str, IStoragePermissionStrategy.Permission.UPDATE, str3)) != null && documentFile.exists()) {
            long lastModified = documentFile.lastModified();
            if (documentFile.renameTo(BaseFileUtils.getFileName(str2))) {
                setLastModified(documentFile, lastModified);
                return true;
            }
        }
        return false;
    }

    public final boolean moveFileInner3(String str, String str2, String str3) {
        Uri fileMediaUri;
        String mimeType = BaseFileMimeUtil.getMimeType(str);
        if (BaseFileMimeUtil.isImageFromMimeType(mimeType) || BaseFileMimeUtil.isVideoFromMimeType(mimeType)) {
            String mimeType2 = BaseFileMimeUtil.getMimeType(str2);
            if ((!BaseFileMimeUtil.isImageFromMimeType(mimeType2) && !BaseFileMimeUtil.isVideoFromMimeType(mimeType2)) || !TextUtils.equals(StorageUtils.getMediaStoreVolumeName(this.context, str), StorageUtils.getMediaStoreVolumeName(this.context, str2)) || (fileMediaUri = MediaStoreUtils.getFileMediaUri(str)) == null || MediaStoreUtils.getFileMediaUri(str2) != null) {
                return false;
            }
            String relativePath = StorageUtils.getRelativePath(this.context, BaseFileUtils.getParentFolderPath(str2));
            String fileName = BaseFileUtils.getFileName(str2);
            ContentValues contentValues = new ContentValues();
            contentValues.put("relative_path", relativePath);
            contentValues.put("_display_name", fileName);
            contentValues.put("is_pending", (Integer) 0);
            return 1 == this.context.getContentResolver().update(fileMediaUri, contentValues, null);
        }
        return false;
    }

    public final boolean moveFileInner4(String str, String str2, String str3) {
        if (copyFile(str, str2, str3)) {
            DocumentFile documentFile = this.holder.getDocumentFile(str, IStoragePermissionStrategy.Permission.DELETE, str3);
            if (documentFile == null) {
                return true;
            }
            documentFile.delete();
            apply(documentFile);
            return true;
        }
        DocumentFile documentFile2 = this.holder.getDocumentFile(str2, IStoragePermissionStrategy.Permission.DELETE, str3);
        if (documentFile2 == null) {
            return false;
        }
        documentFile2.delete();
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final boolean moveFileInner5(String str, String str2, String str3) {
        FileChannel fileChannel;
        ParcelFileDescriptor parcelFileDescriptor;
        FileInputStream fileInputStream;
        FileInputStream fileInputStream2;
        FileInputStream fileInputStream3;
        FileInputStream fileInputStream4;
        FileInputStream fileInputStream5;
        FileInputStream fileInputStream6;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return false;
        }
        FileChannel fileChannel2 = null;
        try {
            DocumentFile documentFile = this.holder.getDocumentFile(str, IStoragePermissionStrategy.Permission.QUERY, str3);
            if (documentFile == null || !documentFile.exists()) {
                throw new StorageException("in file is null", new Object[0]);
            }
            long lastModified = documentFile.lastModified();
            parcelFileDescriptor = openFileDescriptor(documentFile, "r");
            try {
                if (parcelFileDescriptor == null) {
                    throw new StorageException("in pfd is null", new Object[0]);
                }
                fileInputStream4 = new FileInputStream(parcelFileDescriptor.getFileDescriptor());
                try {
                    FileChannel channel = fileInputStream4.getChannel();
                    try {
                        DocumentFile documentFile2 = this.holder.getDocumentFile(str2, IStoragePermissionStrategy.Permission.INSERT, str3);
                        if (documentFile2 == null || !documentFile2.exists()) {
                            throw new StorageException("create out file failed", new Object[0]);
                        }
                        fileInputStream3 = openFileDescriptor(documentFile2, "w");
                        try {
                            if (fileInputStream3 == null) {
                                throw new StorageException("out pfd is null", new Object[0]);
                            }
                            FileOutputStream fileOutputStream = new FileOutputStream(fileInputStream3.getFileDescriptor());
                            try {
                                fileChannel2 = fileOutputStream.getChannel();
                                long size = channel.size();
                                fileInputStream5 = fileOutputStream;
                                try {
                                    long transferTo = channel.transferTo(0L, size, fileChannel2);
                                    if (size != transferTo) {
                                        throw new StorageException("transfer error, expect count %s, actual count %s", Long.valueOf(size), Long.valueOf(transferTo));
                                    }
                                    setLastModified(documentFile2, lastModified);
                                    if (!documentFile.delete()) {
                                        DefaultLogger.w("RExtendedStorageOperator", "transfer success but delete src file error.");
                                    }
                                    if ((!apply(documentFile2)) | (!apply(documentFile))) {
                                        DefaultLogger.w("RExtendedStorageOperator", "transfer success but apply error.");
                                    }
                                    BaseMiscUtil.closeSilently(channel);
                                    BaseMiscUtil.closeSilently(fileChannel2);
                                    BaseMiscUtil.closeSilently(fileInputStream4);
                                    BaseMiscUtil.closeSilently(fileInputStream5);
                                    BaseMiscUtil.closeSilently(parcelFileDescriptor);
                                    BaseMiscUtil.closeSilently(fileInputStream3);
                                    return true;
                                } catch (Exception e) {
                                    e = e;
                                    fileChannel = fileChannel2;
                                    fileChannel2 = channel;
                                    fileInputStream4 = fileInputStream4;
                                    try {
                                        DefaultLogger.e("RExtendedStorageOperator", "move file [%s] -> [%s] by channel failed, error: %s", str, str2, e);
                                        BaseMiscUtil.closeSilently(fileChannel2);
                                        BaseMiscUtil.closeSilently(fileChannel);
                                        BaseMiscUtil.closeSilently(fileInputStream4);
                                        BaseMiscUtil.closeSilently(fileInputStream5);
                                        BaseMiscUtil.closeSilently(parcelFileDescriptor);
                                        BaseMiscUtil.closeSilently(fileInputStream3);
                                        return false;
                                    } catch (Throwable th) {
                                        th = th;
                                        BaseMiscUtil.closeSilently(fileChannel2);
                                        BaseMiscUtil.closeSilently(fileChannel);
                                        BaseMiscUtil.closeSilently(fileInputStream4);
                                        BaseMiscUtil.closeSilently(fileInputStream5);
                                        BaseMiscUtil.closeSilently(parcelFileDescriptor);
                                        BaseMiscUtil.closeSilently(fileInputStream3);
                                        throw th;
                                    }
                                } catch (Throwable th2) {
                                    th = th2;
                                    fileChannel = fileChannel2;
                                    fileChannel2 = channel;
                                    BaseMiscUtil.closeSilently(fileChannel2);
                                    BaseMiscUtil.closeSilently(fileChannel);
                                    BaseMiscUtil.closeSilently(fileInputStream4);
                                    BaseMiscUtil.closeSilently(fileInputStream5);
                                    BaseMiscUtil.closeSilently(parcelFileDescriptor);
                                    BaseMiscUtil.closeSilently(fileInputStream3);
                                    throw th;
                                }
                            } catch (Exception e2) {
                                e = e2;
                                fileInputStream5 = fileOutputStream;
                            } catch (Throwable th3) {
                                th = th3;
                                fileInputStream5 = fileOutputStream;
                            }
                        } catch (Exception e3) {
                            e = e3;
                            fileChannel = null;
                            fileInputStream5 = null;
                        } catch (Throwable th4) {
                            th = th4;
                            fileChannel = null;
                            fileInputStream5 = null;
                        }
                    } catch (Exception e4) {
                        e = e4;
                        fileChannel = null;
                        fileInputStream3 = null;
                        fileInputStream5 = null;
                    } catch (Throwable th5) {
                        th = th5;
                        fileChannel = null;
                        fileInputStream3 = null;
                        fileInputStream5 = null;
                    }
                } catch (Exception e5) {
                    e = e5;
                    fileChannel = null;
                    fileInputStream3 = null;
                    fileInputStream6 = fileInputStream4;
                    fileInputStream5 = fileInputStream3;
                    fileInputStream4 = fileInputStream6;
                    DefaultLogger.e("RExtendedStorageOperator", "move file [%s] -> [%s] by channel failed, error: %s", str, str2, e);
                    BaseMiscUtil.closeSilently(fileChannel2);
                    BaseMiscUtil.closeSilently(fileChannel);
                    BaseMiscUtil.closeSilently(fileInputStream4);
                    BaseMiscUtil.closeSilently(fileInputStream5);
                    BaseMiscUtil.closeSilently(parcelFileDescriptor);
                    BaseMiscUtil.closeSilently(fileInputStream3);
                    return false;
                } catch (Throwable th6) {
                    th = th6;
                    fileChannel = null;
                    fileInputStream3 = null;
                    fileInputStream4 = fileInputStream4;
                    fileInputStream5 = fileInputStream3;
                    BaseMiscUtil.closeSilently(fileChannel2);
                    BaseMiscUtil.closeSilently(fileChannel);
                    BaseMiscUtil.closeSilently(fileInputStream4);
                    BaseMiscUtil.closeSilently(fileInputStream5);
                    BaseMiscUtil.closeSilently(parcelFileDescriptor);
                    BaseMiscUtil.closeSilently(fileInputStream3);
                    throw th;
                }
            } catch (Exception e6) {
                e = e6;
                fileChannel = null;
                fileInputStream2 = null;
                fileInputStream3 = fileInputStream2;
                fileInputStream6 = fileInputStream2;
                fileInputStream5 = fileInputStream3;
                fileInputStream4 = fileInputStream6;
                DefaultLogger.e("RExtendedStorageOperator", "move file [%s] -> [%s] by channel failed, error: %s", str, str2, e);
                BaseMiscUtil.closeSilently(fileChannel2);
                BaseMiscUtil.closeSilently(fileChannel);
                BaseMiscUtil.closeSilently(fileInputStream4);
                BaseMiscUtil.closeSilently(fileInputStream5);
                BaseMiscUtil.closeSilently(parcelFileDescriptor);
                BaseMiscUtil.closeSilently(fileInputStream3);
                return false;
            } catch (Throwable th7) {
                th = th7;
                fileChannel = null;
                fileInputStream = null;
                fileInputStream3 = fileInputStream;
                fileInputStream4 = fileInputStream;
                fileInputStream5 = fileInputStream3;
                BaseMiscUtil.closeSilently(fileChannel2);
                BaseMiscUtil.closeSilently(fileChannel);
                BaseMiscUtil.closeSilently(fileInputStream4);
                BaseMiscUtil.closeSilently(fileInputStream5);
                BaseMiscUtil.closeSilently(parcelFileDescriptor);
                BaseMiscUtil.closeSilently(fileInputStream3);
                throw th;
            }
        } catch (Exception e7) {
            e = e7;
            fileChannel = null;
            parcelFileDescriptor = null;
            fileInputStream2 = null;
        } catch (Throwable th8) {
            th = th8;
            fileChannel = null;
            parcelFileDescriptor = null;
            fileInputStream = null;
        }
    }

    /* compiled from: RExtendedStorageOperator.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
