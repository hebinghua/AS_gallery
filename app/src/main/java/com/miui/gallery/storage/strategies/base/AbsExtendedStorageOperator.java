package com.miui.gallery.storage.strategies.base;

import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import androidx.documentfile.provider.DocumentFile;
import androidx.documentfile.provider.DocumentFileWrapper;
import com.miui.gallery.storage.exceptions.StorageException;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.IStorageStrategy;
import com.miui.gallery.storage.strategies.IStorageStrategyHolder;
import com.miui.gallery.storage.utils.Utils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.MediaStoreUtils;
import com.miui.gallery.util.Scheme;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import kotlin.Triple;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringsJVMKt;
import kotlin.text.StringsKt__StringsKt;

/* compiled from: AbsExtendedStorageOperator.kt */
/* loaded from: classes2.dex */
public abstract class AbsExtendedStorageOperator implements IExtendedStorageOperator {
    public static final Companion Companion = new Companion(null);
    public final Context context;
    public final List<Function1<Triple<String, String, String>, Boolean>> copyActions;
    public final IStorageStrategyHolder holder;
    public final List<Function1<Triple<String, String, String>, Boolean>> moveActions;

    /* compiled from: AbsExtendedStorageOperator.kt */
    /* loaded from: classes2.dex */
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[Scheme.values().length];
            iArr[Scheme.FILE.ordinal()] = 1;
            iArr[Scheme.CONTENT.ordinal()] = 2;
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public abstract List<Function1<Triple<String, String, String>, Boolean>> getMoveActions();

    public AbsExtendedStorageOperator(Context context, IStorageStrategyHolder holder) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(holder, "holder");
        this.context = context;
        this.holder = holder;
        this.copyActions = CollectionsKt__CollectionsKt.listOf((Object[]) new Function1[]{new AbsExtendedStorageOperator$copyActions$1(this), new AbsExtendedStorageOperator$copyActions$2(this)});
        this.moveActions = CollectionsKt__CollectionsKt.emptyList();
    }

    public List<Function1<Triple<String, String, String>, Boolean>> getCopyActions() {
        return this.copyActions;
    }

    @Override // com.miui.gallery.storage.strategies.base.IExtendedStorageOperator
    public final boolean copyFile(String str, String str2, String invokerTag) {
        Intrinsics.checkNotNullParameter(invokerTag, "invokerTag");
        boolean z = true;
        if (!(str == null || str.length() == 0)) {
            if (str2 != null && str2.length() != 0) {
                z = false;
            }
            if (!z) {
                String str3 = "src: [" + ((Object) str) + "],  dst: [" + ((Object) str2) + ']';
                Triple<String, String, String> triple = new Triple<>(str, str2, invokerTag);
                List<Function1<Triple<String, String, String>, Boolean>> copyActions = getCopyActions();
                Boolean bool = Boolean.FALSE;
                if (IStorageStrategy.DEBUG) {
                    DefaultLogger.v("ExtendedStorageOperatorImpl", "[copyFile] description:[" + str3 + ']');
                    DefaultLogger.verbosePrintStackMsg("ExtendedStorageOperatorImpl");
                }
                LinkedList linkedList = new LinkedList();
                for (Function1<Triple<String, String, String>, Boolean> function1 : copyActions) {
                    try {
                        bool = function1.mo2577invoke(triple);
                    } catch (Throwable th) {
                        linkedList.add(th);
                    }
                    if (bool.booleanValue()) {
                        break;
                    }
                }
                if (IStorageStrategy.DEBUG && !bool.booleanValue()) {
                    Iterator it = linkedList.iterator();
                    while (it.hasNext()) {
                        DefaultLogger.e("ExtendedStorageOperatorImpl", (Throwable) it.next());
                    }
                }
                return FileHandleRecordHelper.recordFileCopy(str, str2, invokerTag, bool.booleanValue());
            }
        }
        DefaultLogger.w("ExtendedStorageOperatorImpl", "[copyFile] with illegal params: src[" + ((Object) str) + "], dst [" + ((Object) str2) + ']');
        if (IStorageStrategy.DEBUG) {
            DefaultLogger.verbosePrintStackMsg("ExtendedStorageOperatorImpl");
        }
        return false;
    }

    public final boolean copyFileInner1(String str, String str2, String str3) {
        Closeable closeable;
        InputStream inputStream = null;
        try {
            DocumentFile documentFile = this.holder.getDocumentFile(str, IStoragePermissionStrategy.Permission.QUERY, str3);
            if (documentFile == null || !documentFile.exists()) {
                throw new StorageException("src file is null", new Object[0]);
            }
            long lastModified = documentFile.lastModified();
            InputStream openInputStream = openInputStream(documentFile);
            try {
                if (openInputStream == null) {
                    throw new StorageException("input stream is null", new Object[0]);
                }
                DocumentFile documentFile2 = this.holder.getDocumentFile(str2, IStoragePermissionStrategy.Permission.INSERT, str3);
                if (documentFile2 == null) {
                    throw new StorageException("dst file is null", new Object[0]);
                }
                OutputStream openOutputStream = openOutputStream(documentFile2);
                if (openOutputStream == null) {
                    throw new StorageException("output stream is null", new Object[0]);
                }
                byte[] bArr = new byte[4096];
                while (true) {
                    int read = openInputStream.read(bArr);
                    if (read < 0) {
                        break;
                    }
                    openOutputStream.write(bArr, 0, read);
                }
                openOutputStream.flush();
                setLastModified(documentFile2, lastModified);
                if (!apply(documentFile2)) {
                    DefaultLogger.w("ExtendedStorageOperatorImpl", "copy success but apply error.");
                }
                BaseMiscUtil.closeSilently(openInputStream);
                BaseMiscUtil.closeSilently(openOutputStream);
                return true;
            } catch (Exception e) {
                e = e;
                closeable = null;
                inputStream = openInputStream;
                try {
                    DefaultLogger.e("ExtendedStorageOperatorImpl", "failed to copy file [%s] to [%s], error: %s", str, str2, e);
                    BaseMiscUtil.closeSilently(inputStream);
                    BaseMiscUtil.closeSilently(closeable);
                    return false;
                } catch (Throwable th) {
                    th = th;
                    BaseMiscUtil.closeSilently(inputStream);
                    BaseMiscUtil.closeSilently(closeable);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                closeable = null;
                inputStream = openInputStream;
                BaseMiscUtil.closeSilently(inputStream);
                BaseMiscUtil.closeSilently(closeable);
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            closeable = null;
        } catch (Throwable th3) {
            th = th3;
            closeable = null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r15v0, types: [java.io.FileOutputStream] */
    /* JADX WARN: Type inference failed for: r6v5 */
    /* JADX WARN: Type inference failed for: r6v6 */
    /* JADX WARN: Type inference failed for: r6v7 */
    /* JADX WARN: Type inference failed for: r6v8 */
    public final boolean copyFileInner2(String str, String str2, String str3) {
        WritableByteChannel writableByteChannel;
        ParcelFileDescriptor parcelFileDescriptor;
        FileInputStream fileInputStream;
        FileChannel fileChannel;
        WritableByteChannel writableByteChannel2;
        FileChannel fileChannel2;
        ParcelFileDescriptor parcelFileDescriptor2;
        FileChannel fileChannel3 = 0;
        try {
            DocumentFile documentFile = this.holder.getDocumentFile(str, IStoragePermissionStrategy.Permission.QUERY, str3);
            if (documentFile == null || !documentFile.exists()) {
                throw new StorageException("src file is null", new Object[0]);
            }
            long lastModified = documentFile.lastModified();
            ParcelFileDescriptor openFileDescriptor = openFileDescriptor(documentFile, "r");
            try {
                if (openFileDescriptor == null) {
                    throw new StorageException("in pfd is null", new Object[0]);
                }
                fileInputStream = new FileInputStream(openFileDescriptor.getFileDescriptor());
                try {
                    FileChannel channel = fileInputStream.getChannel();
                    try {
                        DocumentFile documentFile2 = this.holder.getDocumentFile(str2, IStoragePermissionStrategy.Permission.INSERT, str3);
                        if (documentFile2 == null || !documentFile2.exists()) {
                            throw new StorageException("dst file is null", new Object[0]);
                        }
                        fileChannel = openFileDescriptor(documentFile2, "w");
                        try {
                            if (fileChannel == null) {
                                throw new StorageException("out pfd is null", new Object[0]);
                            }
                            ?? fileOutputStream = new FileOutputStream(fileChannel.getFileDescriptor());
                            try {
                                fileChannel3 = fileOutputStream.getChannel();
                                long size = channel.size();
                                writableByteChannel2 = fileOutputStream;
                                try {
                                    long transferTo = channel.transferTo(0L, size, fileChannel3);
                                    if (size != transferTo) {
                                        documentFile2.delete();
                                        throw new StorageException("transfer error, expect count %s, actual count %s", Long.valueOf(size), Long.valueOf(transferTo));
                                    }
                                    setLastModified(documentFile2, lastModified);
                                    if (!apply(documentFile2)) {
                                        DefaultLogger.w("ExtendedStorageOperatorImpl", "transfer success but apply error.");
                                    }
                                    BaseMiscUtil.closeSilently(channel);
                                    BaseMiscUtil.closeSilently(fileChannel3);
                                    BaseMiscUtil.closeSilently(fileInputStream);
                                    BaseMiscUtil.closeSilently(writableByteChannel2);
                                    BaseMiscUtil.closeSilently(openFileDescriptor);
                                    BaseMiscUtil.closeSilently(fileChannel);
                                    return true;
                                } catch (Exception e) {
                                    e = e;
                                    parcelFileDescriptor = openFileDescriptor;
                                    parcelFileDescriptor2 = fileChannel3;
                                    fileChannel3 = channel;
                                    writableByteChannel = parcelFileDescriptor2;
                                    fileChannel = fileChannel;
                                    writableByteChannel2 = writableByteChannel2;
                                    try {
                                        DefaultLogger.e("ExtendedStorageOperatorImpl", "failed to copy file [%s] to [%s], error: %s", str, str2, e);
                                        BaseMiscUtil.closeSilently(fileChannel3);
                                        BaseMiscUtil.closeSilently(writableByteChannel);
                                        BaseMiscUtil.closeSilently(fileInputStream);
                                        BaseMiscUtil.closeSilently(writableByteChannel2);
                                        BaseMiscUtil.closeSilently(parcelFileDescriptor);
                                        BaseMiscUtil.closeSilently(fileChannel);
                                        return false;
                                    } catch (Throwable th) {
                                        th = th;
                                        BaseMiscUtil.closeSilently(fileChannel3);
                                        BaseMiscUtil.closeSilently(writableByteChannel);
                                        BaseMiscUtil.closeSilently(fileInputStream);
                                        BaseMiscUtil.closeSilently(writableByteChannel2);
                                        BaseMiscUtil.closeSilently(parcelFileDescriptor);
                                        BaseMiscUtil.closeSilently(fileChannel);
                                        throw th;
                                    }
                                } catch (Throwable th2) {
                                    th = th2;
                                    parcelFileDescriptor = openFileDescriptor;
                                    writableByteChannel = fileChannel3;
                                    fileChannel3 = channel;
                                    BaseMiscUtil.closeSilently(fileChannel3);
                                    BaseMiscUtil.closeSilently(writableByteChannel);
                                    BaseMiscUtil.closeSilently(fileInputStream);
                                    BaseMiscUtil.closeSilently(writableByteChannel2);
                                    BaseMiscUtil.closeSilently(parcelFileDescriptor);
                                    BaseMiscUtil.closeSilently(fileChannel);
                                    throw th;
                                }
                            } catch (Exception e2) {
                                e = e2;
                                writableByteChannel2 = fileOutputStream;
                            } catch (Throwable th3) {
                                th = th3;
                                writableByteChannel2 = fileOutputStream;
                            }
                        } catch (Exception e3) {
                            e = e3;
                            writableByteChannel2 = null;
                            parcelFileDescriptor = openFileDescriptor;
                            parcelFileDescriptor2 = writableByteChannel2;
                            fileChannel3 = channel;
                            writableByteChannel = parcelFileDescriptor2;
                            fileChannel = fileChannel;
                            writableByteChannel2 = writableByteChannel2;
                            DefaultLogger.e("ExtendedStorageOperatorImpl", "failed to copy file [%s] to [%s], error: %s", str, str2, e);
                            BaseMiscUtil.closeSilently(fileChannel3);
                            BaseMiscUtil.closeSilently(writableByteChannel);
                            BaseMiscUtil.closeSilently(fileInputStream);
                            BaseMiscUtil.closeSilently(writableByteChannel2);
                            BaseMiscUtil.closeSilently(parcelFileDescriptor);
                            BaseMiscUtil.closeSilently(fileChannel);
                            return false;
                        } catch (Throwable th4) {
                            th = th4;
                            writableByteChannel2 = null;
                            parcelFileDescriptor = openFileDescriptor;
                            writableByteChannel = writableByteChannel2;
                            fileChannel3 = channel;
                            BaseMiscUtil.closeSilently(fileChannel3);
                            BaseMiscUtil.closeSilently(writableByteChannel);
                            BaseMiscUtil.closeSilently(fileInputStream);
                            BaseMiscUtil.closeSilently(writableByteChannel2);
                            BaseMiscUtil.closeSilently(parcelFileDescriptor);
                            BaseMiscUtil.closeSilently(fileChannel);
                            throw th;
                        }
                    } catch (Exception e4) {
                        e = e4;
                        fileChannel = null;
                        writableByteChannel2 = null;
                    } catch (Throwable th5) {
                        th = th5;
                        fileChannel = null;
                        writableByteChannel2 = null;
                    }
                } catch (Exception e5) {
                    e = e5;
                    fileChannel2 = null;
                    FileChannel fileChannel4 = fileChannel2;
                    parcelFileDescriptor = openFileDescriptor;
                    writableByteChannel = fileChannel4;
                    fileChannel = fileChannel2;
                    writableByteChannel2 = fileChannel4;
                    DefaultLogger.e("ExtendedStorageOperatorImpl", "failed to copy file [%s] to [%s], error: %s", str, str2, e);
                    BaseMiscUtil.closeSilently(fileChannel3);
                    BaseMiscUtil.closeSilently(writableByteChannel);
                    BaseMiscUtil.closeSilently(fileInputStream);
                    BaseMiscUtil.closeSilently(writableByteChannel2);
                    BaseMiscUtil.closeSilently(parcelFileDescriptor);
                    BaseMiscUtil.closeSilently(fileChannel);
                    return false;
                } catch (Throwable th6) {
                    th = th6;
                    fileChannel = null;
                    writableByteChannel2 = fileChannel;
                    parcelFileDescriptor = openFileDescriptor;
                    writableByteChannel = writableByteChannel2;
                    BaseMiscUtil.closeSilently(fileChannel3);
                    BaseMiscUtil.closeSilently(writableByteChannel);
                    BaseMiscUtil.closeSilently(fileInputStream);
                    BaseMiscUtil.closeSilently(writableByteChannel2);
                    BaseMiscUtil.closeSilently(parcelFileDescriptor);
                    BaseMiscUtil.closeSilently(fileChannel);
                    throw th;
                }
            } catch (Exception e6) {
                e = e6;
                fileInputStream = null;
                fileChannel2 = null;
            } catch (Throwable th7) {
                th = th7;
                fileInputStream = null;
                fileChannel = null;
            }
        } catch (Exception e7) {
            e = e7;
            writableByteChannel = null;
            parcelFileDescriptor = null;
            fileInputStream = null;
            fileChannel = null;
            writableByteChannel2 = null;
        } catch (Throwable th8) {
            th = th8;
            writableByteChannel = null;
            parcelFileDescriptor = null;
            fileInputStream = null;
            fileChannel = null;
            writableByteChannel2 = null;
        }
    }

    @Override // com.miui.gallery.storage.strategies.base.IExtendedStorageOperator
    public final boolean moveFile(String str, String str2, String invokerTag) {
        Intrinsics.checkNotNullParameter(invokerTag, "invokerTag");
        boolean z = true;
        if (!(str == null || str.length() == 0)) {
            if (str2 != null && str2.length() != 0) {
                z = false;
            }
            if (!z) {
                String str3 = "src: [" + ((Object) str) + "],  dst: [" + ((Object) str2) + ']';
                Triple<String, String, String> triple = new Triple<>(str, str2, invokerTag);
                List<Function1<Triple<String, String, String>, Boolean>> moveActions = getMoveActions();
                Boolean bool = Boolean.FALSE;
                if (IStorageStrategy.DEBUG) {
                    DefaultLogger.v("ExtendedStorageOperatorImpl", "[moveFile] description:[" + str3 + ']');
                    DefaultLogger.verbosePrintStackMsg("ExtendedStorageOperatorImpl");
                }
                LinkedList linkedList = new LinkedList();
                for (Function1<Triple<String, String, String>, Boolean> function1 : moveActions) {
                    try {
                        bool = function1.mo2577invoke(triple);
                    } catch (Throwable th) {
                        linkedList.add(th);
                    }
                    if (bool.booleanValue()) {
                        break;
                    }
                }
                if (IStorageStrategy.DEBUG && !bool.booleanValue()) {
                    Iterator it = linkedList.iterator();
                    while (it.hasNext()) {
                        DefaultLogger.e("ExtendedStorageOperatorImpl", (Throwable) it.next());
                    }
                }
                return FileHandleRecordHelper.recordFileMove(str, str2, invokerTag, bool.booleanValue());
            }
        }
        DefaultLogger.w("ExtendedStorageOperatorImpl", "[moveFile] with illegal params: src[" + ((Object) str) + "], dst [" + ((Object) str2) + ']');
        if (IStorageStrategy.DEBUG) {
            DefaultLogger.verbosePrintStackMsg("ExtendedStorageOperatorImpl");
        }
        return false;
    }

    @Override // com.miui.gallery.storage.strategies.base.IExtendedStorageOperator
    public boolean apply(DocumentFile documentFile) {
        Intrinsics.checkNotNullParameter(documentFile, "documentFile");
        return Utils.triggerMediaScan(this.context, documentFile);
    }

    @Override // com.miui.gallery.storage.strategies.base.IExtendedStorageOperator
    public boolean setLastModified(DocumentFile documentFile, long j) {
        String decode;
        Intrinsics.checkNotNullParameter(documentFile, "documentFile");
        if (documentFile instanceof DocumentFileWrapper) {
            documentFile = ((DocumentFileWrapper) documentFile).get();
        }
        if (!documentFile.exists() || !documentFile.isFile()) {
            DefaultLogger.e("ExtendedStorageOperatorImpl", "setLastModified => file does not exit!");
            return false;
        }
        String uri = documentFile.getUri().toString();
        Intrinsics.checkNotNullExpressionValue(uri, "file.uri.toString()");
        Scheme ofUri = Scheme.ofUri(uri);
        int i = ofUri == null ? -1 : WhenMappings.$EnumSwitchMapping$0[ofUri.ordinal()];
        boolean z = true;
        if (i == 1) {
            decode = Uri.decode(Scheme.FILE.crop(uri));
        } else if (i != 2) {
            return false;
        } else {
            String authority = documentFile.getUri().getAuthority();
            if (authority != null) {
                int hashCode = authority.hashCode();
                if (hashCode != 103772132) {
                    if (hashCode == 596745902 && authority.equals("com.android.externalstorage.documents")) {
                        String lastPathSegment = documentFile.getUri().getLastPathSegment();
                        Intrinsics.checkNotNull(lastPathSegment);
                        String substring = lastPathSegment.substring(0, StringsKt__StringsKt.indexOf$default((CharSequence) lastPathSegment, ":", 0, false, 6, (Object) null));
                        Intrinsics.checkNotNullExpressionValue(substring, "this as java.lang.String…ing(startIndex, endIndex)");
                        String substring2 = lastPathSegment.substring(StringsKt__StringsKt.indexOf$default((CharSequence) lastPathSegment, ":", 0, false, 6, (Object) null) + 1);
                        Intrinsics.checkNotNullExpressionValue(substring2, "this as java.lang.String).substring(startIndex)");
                        if (StringsKt__StringsJVMKt.equals(substring, "primary", true)) {
                            decode = StorageUtils.getPathInPrimaryStorage(substring2);
                        } else {
                            decode = StorageUtils.getPathInSecondaryStorage(substring2);
                        }
                    }
                } else if (authority.equals("media")) {
                    decode = MediaStoreUtils.getMediaFilePath(uri);
                }
            }
            decode = null;
        }
        if (decode != null && decode.length() != 0) {
            z = false;
        }
        if (z) {
            DefaultLogger.e("ExtendedStorageOperatorImpl", "setLastModified => fail: path is null!");
            return false;
        }
        try {
            boolean lastModified = new File(decode).setLastModified(j);
            if (lastModified) {
                DefaultLogger.d("ExtendedStorageOperatorImpl", "setLastModified => [%s] success", decode);
                Utils.triggerMediaScan(this.context, documentFile);
            }
            return lastModified;
        } finally {
            DefaultLogger.e("ExtendedStorageOperatorImpl", "setLastModified => [%s] fail", decode);
        }
    }

    @Override // com.miui.gallery.storage.strategies.base.IExtendedStorageOperator
    public InputStream openInputStream(DocumentFile documentFile) {
        if (documentFile == null) {
            return null;
        }
        try {
            return this.context.getContentResolver().openInputStream(documentFile.getUri());
        } catch (Throwable th) {
            DefaultLogger.e("ExtendedStorageOperatorImpl", th);
            return null;
        }
    }

    @Override // com.miui.gallery.storage.strategies.base.IExtendedStorageOperator
    public OutputStream openOutputStream(DocumentFile documentFile) {
        if (documentFile == null) {
            return null;
        }
        try {
            return this.context.getContentResolver().openOutputStream(documentFile.getUri());
        } catch (Throwable th) {
            DefaultLogger.e("ExtendedStorageOperatorImpl", th);
            return null;
        }
    }

    @Override // com.miui.gallery.storage.strategies.base.IExtendedStorageOperator
    public ParcelFileDescriptor openFileDescriptor(DocumentFile documentFile, String mode) {
        Intrinsics.checkNotNullParameter(mode, "mode");
        if (documentFile == null) {
            return null;
        }
        try {
            return this.context.getContentResolver().openFileDescriptor(documentFile.getUri(), mode);
        } catch (Throwable th) {
            DefaultLogger.e("ExtendedStorageOperatorImpl", th);
            return null;
        }
    }

    /* compiled from: AbsExtendedStorageOperator.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
