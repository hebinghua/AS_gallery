package com.miui.gallery.editor.photo.sdk;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.ArraySet;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import com.miui.gallery.job.workers.TrackedWorker;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.io.File;
import java.util.List;
import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: CleanWorker.kt */
/* loaded from: classes2.dex */
public final class CleanWorker extends TrackedWorker {
    public static final Companion Companion = new Companion(null);

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CleanWorker(Context context, WorkerParameters workerParams) {
        super(context, workerParams);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(workerParams, "workerParams");
    }

    @Override // com.miui.gallery.job.workers.TrackedWorker
    public ListenableWorker.Result doWork() {
        ListenableWorker.Result success;
        String str;
        Companion companion = Companion;
        Data inputData = getInputData();
        Intrinsics.checkNotNullExpressionValue(inputData, "inputData");
        List<File> parse = companion.parse(inputData);
        DefaultLogger.d("CleanWorker", "start clean files: %s", parse);
        boolean z = false;
        for (File file : parse) {
            if (file.exists()) {
                Companion companion2 = Companion;
                Context applicationContext = getApplicationContext();
                Intrinsics.checkNotNullExpressionValue(applicationContext, "applicationContext");
                z |= !companion2.delete(applicationContext, file);
            }
        }
        DefaultLogger.d("CleanWorker", "job finish, need reschedule? %b", Boolean.valueOf(z));
        if (z) {
            success = ListenableWorker.Result.retry();
            str = "retry()";
        } else {
            success = ListenableWorker.Result.success();
            str = "success()";
        }
        Intrinsics.checkNotNullExpressionValue(success, str);
        return success;
    }

    /* compiled from: CleanWorker.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        /* JADX WARN: Code restructure failed: missing block: B:8:0x0010, code lost:
            if ((r4.length == 0) != false) goto L24;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final java.util.List<java.io.File> parse(androidx.work.Data r4) {
            /*
                r3 = this;
                java.lang.String r0 = "extra_file_paths"
                java.lang.String[] r4 = r4.getStringArray(r0)
                r0 = 0
                r1 = 1
                if (r4 == 0) goto L12
                int r2 = r4.length
                if (r2 != 0) goto Lf
                r2 = r1
                goto L10
            Lf:
                r2 = r0
            L10:
                if (r2 == 0) goto L13
            L12:
                r0 = r1
            L13:
                if (r0 == 0) goto L1a
                java.util.List r4 = kotlin.collections.CollectionsKt__CollectionsKt.emptyList()
                return r4
            L1a:
                java.util.ArrayList r0 = new java.util.ArrayList
                int r2 = r4.length
                int r2 = r2 / 2
                int r2 = r2 + r1
                r0.<init>(r2)
                java.util.Iterator r4 = kotlin.jvm.internal.ArrayIteratorKt.iterator(r4)
            L27:
                boolean r1 = r4.hasNext()
                if (r1 == 0) goto L42
                java.lang.Object r1 = r4.next()
                java.lang.String r1 = (java.lang.String) r1
                java.io.File r2 = new java.io.File
                r2.<init>(r1)
                boolean r1 = r2.exists()
                if (r1 == 0) goto L27
                r0.add(r2)
                goto L27
            L42:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.editor.photo.sdk.CleanWorker.Companion.parse(androidx.work.Data):java.util.List");
        }

        public final boolean delete(Context context, File file) {
            long j;
            boolean delete;
            Objects.requireNonNull(file, "file can't be null");
            int i = 1;
            r5 = true;
            boolean z = true;
            if (file.isFile()) {
                if (file.exists() && !file.delete()) {
                    z = false;
                }
                DefaultLogger.d("CleanWorker", "deleting file: %s, result: %b", file, Boolean.valueOf(z));
                return z;
            } else if (!file.isDirectory()) {
                return false;
            } else {
                Cursor query = context.getContentResolver().query(MediaStore.Files.getContentUri("external"), new String[]{j.c}, "_data=?", new String[]{file.getAbsolutePath()}, null);
                if (query != null) {
                    try {
                        j = query.moveToFirst() ? query.getLong(0) : -1L;
                        query.close();
                    } finally {
                    }
                } else {
                    j = -1;
                }
                ArraySet arraySet = new ArraySet();
                if (j != -1) {
                    DefaultLogger.d("CleanWorker", "cleaning directory(%d)", Long.valueOf(j));
                    Cursor query2 = context.getContentResolver().query(MediaStore.Files.getContentUri("external"), new String[]{"_data"}, "parent=?", new String[]{String.valueOf(j)}, null);
                    DefaultLogger.d("CleanWorker", "mark children of %d, count: %d", Long.valueOf(j), Integer.valueOf(query2 == null ? -1 : query2.getCount()));
                    if (query2 != null) {
                        while (query2.moveToNext()) {
                            try {
                                String string = query2.getString(0);
                                Intrinsics.checkNotNullExpressionValue(string, "cursor.getString(0)");
                                arraySet.add(string);
                            } finally {
                            }
                        }
                    }
                }
                File[] listFiles = file.listFiles();
                if (listFiles == null) {
                    listFiles = new File[0];
                }
                int length = listFiles.length;
                boolean z2 = true;
                int i2 = 0;
                while (i2 < length) {
                    File file2 = listFiles[i2];
                    i2++;
                    String absolutePath = file2.getAbsolutePath();
                    if (arraySet.contains(absolutePath)) {
                        ContentResolver contentResolver = context.getContentResolver();
                        Uri contentUri = MediaStore.Files.getContentUri("external");
                        String[] strArr = new String[i];
                        strArr[0] = absolutePath;
                        int delete2 = contentResolver.delete(contentUri, "_data=?", strArr);
                        DefaultLogger.d("CleanWorker", "deleted %d item from MediaProvider", Integer.valueOf(delete2));
                        if (delete2 > 0) {
                            arraySet.remove(absolutePath);
                            delete = delete(context, file2);
                        } else {
                            z2 = false;
                            i = 1;
                        }
                    } else {
                        delete = delete(context, file2);
                    }
                    z2 = delete & z2;
                    i = 1;
                }
                return z2;
            }
        }
    }
}
