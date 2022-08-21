package com.miui.gallery.activity;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.documentfile.provider.DocumentFile;
import com.google.common.collect.EvictingQueue;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.activity.StorageTestActivity;
import com.miui.gallery.app.activity.AndroidActivity;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.constants.MIUIStorageConstants;
import com.miui.gallery.storage.exceptions.StorageException;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.android30.PermissionUtils;
import com.miui.gallery.util.ArrayUtils;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.function.Predicate;

/* loaded from: classes.dex */
public class StorageTestActivity extends AndroidActivity {
    public final InputRecorder mInputRecorder = new InputRecorder();

    @Override // com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        TextView textView = new TextView(this);
        setContentView(textView);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        textView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        IStoragePermissionStrategy.PermissionResult permissionResult = new IStoragePermissionStrategy.PermissionResult("/storage/emulated/0/browser/test.txt", IStoragePermissionStrategy.Permission.INSERT);
        permissionResult.applicable = true;
        new StoragePermissionMissingException(Collections.singletonList(permissionResult)).offer(this);
        new Thread(new Test(textView)).start();
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i != 25 && i != 24) {
            return super.onKeyDown(i, keyEvent);
        }
        if (this.mInputRecorder.onKeyDown(i)) {
            IntentUtil.gotoAlbumPermissionActivity(this);
        }
        return super.onKeyDown(i, keyEvent);
    }

    /* loaded from: classes.dex */
    public static class InputRecorder {
        public final Boolean[] code;
        public final Queue<Boolean> mQueue;

        public InputRecorder() {
            Boolean bool = Boolean.TRUE;
            Boolean bool2 = Boolean.FALSE;
            Boolean[] boolArr = {bool, bool, bool2, bool2, bool, bool2};
            this.code = boolArr;
            this.mQueue = EvictingQueue.create(boolArr.length);
        }

        public boolean onKeyDown(int i) {
            if (i == 24) {
                this.mQueue.offer(Boolean.TRUE);
            } else if (i == 25) {
                this.mQueue.offer(Boolean.FALSE);
            }
            return check();
        }

        public final boolean check() {
            boolean z = false;
            int i = 0;
            for (Boolean bool : this.mQueue) {
                int i2 = i + 1;
                z = bool.equals(this.code[i]);
                if (!z) {
                    return false;
                }
                i = i2;
            }
            return z && i == this.code.length;
        }
    }

    /* loaded from: classes.dex */
    public static class Test implements Runnable {
        public final WeakReference<TextView> mRef;
        public final Map<String, Boolean> results = new HashMap();

        public static /* synthetic */ boolean $r8$lambda$Wf7EmY3HAF5M9bOgV9Y21UKh4MU(Test test, String str) {
            return test.lambda$testFile$0(str);
        }

        public static /* synthetic */ void $r8$lambda$lp_8AAZDz91Yys316zLG18A37cY(Test test, String str, Object[] objArr) {
            test.lambda$append$4(str, objArr);
        }

        /* renamed from: $r8$lambda$zcV5JCwqv3f1lK2ofkM7-Bfk2e8 */
        public static /* synthetic */ boolean m476$r8$lambda$zcV5JCwqv3f1lK2ofkM7Bfk2e8(Test test, String str) {
            return test.lambda$testHolder$2(str);
        }

        public Test(TextView textView) {
            this.mRef = new WeakReference<>(textView);
        }

        @Override // java.lang.Runnable
        public void run() {
            printDeviceInfo();
            printSystemGalleryInfo();
            if (!triggerGenerate()) {
                return;
            }
            testDirectory();
            testFile();
            testHolder();
        }

        public final void printDeviceInfo() {
            append("Android Sdk Int [%d]", Integer.valueOf(Build.VERSION.SDK_INT));
            append("with external sdcard [%b]", Boolean.valueOf(StorageUtils.hasExternalSDCard(GalleryApp.sGetAndroidContext())));
        }

        public final void printSystemGalleryInfo() {
            append("[%s] is system gallery: [%b]", "com.miui.gallery", Boolean.valueOf(PermissionUtils.checkWriteImagesOrVideoAppOps(GalleryApp.sGetAndroidContext(), Process.myUid(), "com.miui.gallery")));
            append("------------------------------------------------------------------------------", new Object[0]);
        }

        public final void testDirectory() {
            Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
            String[] strArr = {sGetAndroidContext.getCacheDir().getAbsolutePath(), sGetAndroidContext.getExternalCacheDir().getAbsolutePath()};
            StringBuilder sb = new StringBuilder();
            sb.append(MIUIStorageConstants.DIRECTORY_SCREENRECORDER_PATH);
            String str = File.separator;
            sb.append(str);
            sb.append("storagetest");
            String[] strArr2 = (String[]) ArrayUtils.concat(strArr, StorageUtils.getAbsolutePath(sGetAndroidContext, sb.toString()), StorageUtils.getAbsolutePath(sGetAndroidContext, Environment.DIRECTORY_DOWNLOADS + str + "storagetest"), StorageUtils.getAbsolutePath(sGetAndroidContext, "MIUI" + str + "storagetest"), StorageUtils.getAbsolutePath(sGetAndroidContext, "storagetest"));
            int length = strArr2.length;
            for (int i = 0; i < length; i++) {
                String str2 = strArr2[i];
                triggerGenerate();
                IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.QUERY_DIRECTORY;
                IStoragePermissionStrategy.Permission permission2 = IStoragePermissionStrategy.Permission.UPDATE_DIRECTORY;
                IStoragePermissionStrategy.Permission permission3 = IStoragePermissionStrategy.Permission.DELETE_DIRECTORY;
                doTestDirectory(str2, new IStoragePermissionStrategy.Permission[]{permission, permission2, permission3});
                doTestDirectory(str2 + File.separator + "storagetest_insert", new IStoragePermissionStrategy.Permission[]{IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, permission, permission2, permission3});
            }
        }

        public final void doTestDirectory(String str, IStoragePermissionStrategy.Permission[] permissionArr) {
            append("check permission - [%s]", str);
            for (IStoragePermissionStrategy.Permission permission : permissionArr) {
                IStoragePermissionStrategy.PermissionResult checkPermission = StorageSolutionProvider.get().checkPermission(str, permission);
                if (!checkPermission.granted) {
                    append("                 - %s: [false]", permission);
                    append("                 - %s: applicable [%b]", str, Boolean.valueOf(checkPermission.applicable));
                }
            }
            append("get document file - [%s]", str);
            String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("StorageTestActivity", "doTestDirectory");
            for (IStoragePermissionStrategy.Permission permission2 : permissionArr) {
                DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str, permission2, appendInvokerTag);
                if (documentFile == null) {
                    append("                  - %s : null", permission2);
                } else {
                    append("                  - %s: class: [%s], Uri: [%s]", permission2, documentFile.getClass().getSimpleName(), documentFile.getUri());
                    if (!StorageSolutionProvider.get().checkPermission(str, permission2).granted) {
                        return;
                    }
                    int i = AnonymousClass1.$SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[permission2.ordinal()];
                    if (i != 1) {
                        if (i != 2) {
                            if (i != 3) {
                                if (i == 4) {
                                    if (!documentFile.renameTo("storagetest_rename")) {
                                        append("                      rename failed", new Object[0]);
                                    } else {
                                        str = BaseFileUtils.getParentFolderPath(str) + File.separator + "storagetest_rename";
                                    }
                                }
                            } else if (!documentFile.delete()) {
                                append("                      delete failed", new Object[0]);
                            }
                        } else if (StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, appendInvokerTag) == null) {
                            append("                      insert directory failed", new Object[0]);
                        }
                    } else if (!documentFile.exists()) {
                        append("                      not exists", new Object[0]);
                    } else if (!documentFile.isDirectory()) {
                        append("                      is not a directory", new Object[0]);
                    } else if (0 == documentFile.lastModified()) {
                        append("                      last modified return 0", new Object[0]);
                    } else {
                        try {
                            documentFile.listFiles();
                        } catch (Exception e) {
                            append("                      list children error : %s", e);
                        }
                    }
                }
            }
        }

        public final void testFile() {
            String[] strArr;
            Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
            for (String str : (String[]) this.results.keySet().stream().filter(new Predicate() { // from class: com.miui.gallery.activity.StorageTestActivity$Test$$ExternalSyntheticLambda3
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return StorageTestActivity.Test.$r8$lambda$Wf7EmY3HAF5M9bOgV9Y21UKh4MU(StorageTestActivity.Test.this, (String) obj);
                }
            }).toArray(StorageTestActivity$Test$$ExternalSyntheticLambda2.INSTANCE)) {
                triggerGenerate();
                String parentFolderPath = BaseFileUtils.getParentFolderPath(str);
                try {
                    doTestFile(sGetAndroidContext, parentFolderPath + File.separator + (BaseFileUtils.getFileNameWithoutExtension(str) + "_insert." + BaseFileUtils.getExtension(str)), new IStoragePermissionStrategy.Permission[]{IStoragePermissionStrategy.Permission.INSERT, IStoragePermissionStrategy.Permission.QUERY, IStoragePermissionStrategy.Permission.UPDATE, IStoragePermissionStrategy.Permission.DELETE}, null);
                } catch (InterruptedException unused) {
                }
                String str2 = BaseFileUtils.getFileNameWithoutExtension(str) + "_append." + BaseFileUtils.getExtension(str);
                Bundle bundle = new Bundle();
                long mediaStoreId = getMediaStoreId(sGetAndroidContext, str);
                if (mediaStoreId >= 0) {
                    bundle.putParcelable("android:query-arg-related-uri", ContentUris.withAppendedId(getUri(sGetAndroidContext, str), mediaStoreId));
                    try {
                        doTestFile(sGetAndroidContext, parentFolderPath + File.separator + str2, new IStoragePermissionStrategy.Permission[]{IStoragePermissionStrategy.Permission.APPEND, IStoragePermissionStrategy.Permission.QUERY, IStoragePermissionStrategy.Permission.UPDATE, IStoragePermissionStrategy.Permission.DELETE}, bundle);
                    } catch (InterruptedException unused2) {
                    }
                }
                try {
                    doTestFile(sGetAndroidContext, str, new IStoragePermissionStrategy.Permission[]{IStoragePermissionStrategy.Permission.QUERY, IStoragePermissionStrategy.Permission.UPDATE, IStoragePermissionStrategy.Permission.DELETE}, null);
                } catch (InterruptedException unused3) {
                }
            }
        }

        public /* synthetic */ boolean lambda$testFile$0(String str) {
            return this.results.get(str) != null && this.results.get(str).booleanValue();
        }

        public static /* synthetic */ String[] lambda$testFile$1(int i) {
            return new String[i];
        }

        /* JADX WARN: Removed duplicated region for block: B:272:0x01bd A[Catch: all -> 0x01c1, TRY_ENTER, TRY_LEAVE, TryCatch #10 {Exception -> 0x01c9, blocks: (B:261:0x01af, B:277:0x01c6, B:272:0x01bd), top: B:352:0x01af }] */
        /* JADX WARN: Removed duplicated region for block: B:288:0x01e7  */
        /* JADX WARN: Removed duplicated region for block: B:365:0x02b3 A[SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final void doTestFile(android.content.Context r23, java.lang.String r24, com.miui.gallery.storage.strategies.IStoragePermissionStrategy.Permission[] r25, android.os.Bundle r26) throws java.lang.InterruptedException {
            /*
                Method dump skipped, instructions count: 714
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.activity.StorageTestActivity.Test.doTestFile(android.content.Context, java.lang.String, com.miui.gallery.storage.strategies.IStoragePermissionStrategy$Permission[], android.os.Bundle):void");
        }

        public final void testHolder() {
            Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
            for (String str : (String[]) this.results.keySet().stream().filter(new Predicate() { // from class: com.miui.gallery.activity.StorageTestActivity$Test$$ExternalSyntheticLambda4
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return StorageTestActivity.Test.m476$r8$lambda$zcV5JCwqv3f1lK2ofkM7Bfk2e8(StorageTestActivity.Test.this, (String) obj);
                }
            }).toArray(StorageTestActivity$Test$$ExternalSyntheticLambda1.INSTANCE)) {
                try {
                    doTestHolder(sGetAndroidContext, str);
                } catch (InterruptedException unused) {
                }
            }
        }

        public /* synthetic */ boolean lambda$testHolder$2(String str) {
            return this.results.get(str) != null && this.results.get(str).booleanValue();
        }

        public static /* synthetic */ String[] lambda$testHolder$3(int i) {
            return new String[i];
        }

        /* JADX WARN: Removed duplicated region for block: B:130:0x00fc  */
        /* JADX WARN: Removed duplicated region for block: B:149:0x01a8  */
        /* JADX WARN: Removed duplicated region for block: B:166:0x0234  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final void doTestHolder(android.content.Context r14, java.lang.String r15) throws java.lang.InterruptedException {
            /*
                Method dump skipped, instructions count: 626
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.activity.StorageTestActivity.Test.doTestHolder(android.content.Context, java.lang.String):void");
        }

        public final boolean triggerGenerate() {
            Context context;
            TextView textView = this.mRef.get();
            if (textView == null || (context = textView.getContext()) == null) {
                return false;
            }
            try {
                context.getContentResolver().call(Uri.parse("content://com.miui.gallery.storagetest.storage_test_result"), "null", (String) null, (Bundle) null);
                return readResult();
            } catch (Exception unused) {
                return false;
            }
        }

        public final boolean readResult() {
            Context context;
            this.results.clear();
            TextView textView = this.mRef.get();
            if (textView == null || (context = textView.getContext()) == null) {
                return false;
            }
            try {
                Cursor query = context.getContentResolver().query(Uri.parse("content://com.miui.gallery.storagetest.storage_test_result"), null, null, null, null);
                if (query != null && query.getCount() > 0) {
                    while (query.moveToNext()) {
                        this.results.put(query.getString(0), Boolean.valueOf(Boolean.parseBoolean(query.getString(1))));
                    }
                    append("------------------------------------------------------------------------------", new Object[0]);
                    query.close();
                    return true;
                }
                append("empty cursor", new Object[0]);
                if (query != null) {
                    query.close();
                }
                return false;
            } catch (Exception e) {
                append("load error: %s", e);
                return false;
            }
        }

        public final void append(final String str, final Object... objArr) {
            if (this.mRef.get() == null) {
                return;
            }
            this.mRef.get().post(new Runnable() { // from class: com.miui.gallery.activity.StorageTestActivity$Test$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    StorageTestActivity.Test.$r8$lambda$lp_8AAZDz91Yys316zLG18A37cY(StorageTestActivity.Test.this, str, objArr);
                }
            });
        }

        public /* synthetic */ void lambda$append$4(String str, Object[] objArr) {
            TextView textView = this.mRef.get();
            if (textView == null) {
                return;
            }
            textView.append(String.format(str, objArr) + "\n");
            DefaultLogger.e("StorageTestActivity", String.format(str, objArr));
        }

        public final long getMediaStoreId(Context context, String str) {
            Uri uri = getUri(context, str);
            if (uri == null) {
                return -1L;
            }
            Cursor query = context.getContentResolver().query(uri, new String[]{j.c, "is_pending"}, getQuerySelection(str), null, null, null);
            try {
                if (query == null) {
                    throw new StorageException("invalid cursor.", new Object[0]);
                }
                if (query.getCount() > 0) {
                    if (!query.moveToFirst()) {
                        query.close();
                        return -1L;
                    } else if (1 == query.getInt(1)) {
                        append("is pending", new Object[0]);
                        query.close();
                        return -1L;
                    } else {
                        long j = query.getLong(0);
                        query.close();
                        return j;
                    }
                }
                query.close();
                return -1L;
            } catch (Throwable th) {
                if (query != null) {
                    try {
                        query.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public static Uri getUri(Context context, String str) {
            String mimeType = BaseFileMimeUtil.getMimeType(str);
            if (BaseFileMimeUtil.isImageFromMimeType(mimeType) || BaseFileMimeUtil.isVideoFromMimeType(mimeType)) {
                String mediaStoreVolumeName = StorageUtils.getMediaStoreVolumeName(context, str);
                return BaseFileMimeUtil.isImageFromMimeType(mimeType) ? MediaStore.Images.Media.getContentUri(mediaStoreVolumeName) : MediaStore.Video.Media.getContentUri(mediaStoreVolumeName);
            }
            return null;
        }

        public static String getQuerySelection(String str) {
            return String.format(Locale.US, "bucket_id=%d AND _display_name='%s'", Integer.valueOf(BaseFileUtils.getBucketID(BaseFileUtils.getParentFolderPath(str))), BaseFileUtils.getFileName(str));
        }
    }

    /* renamed from: com.miui.gallery.activity.StorageTestActivity$1 */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission;

        static {
            int[] iArr = new int[IStoragePermissionStrategy.Permission.values().length];
            $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission = iArr;
            try {
                iArr[IStoragePermissionStrategy.Permission.QUERY_DIRECTORY.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.INSERT_DIRECTORY.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.DELETE_DIRECTORY.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.UPDATE_DIRECTORY.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.DELETE.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.QUERY.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.APPEND.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.INSERT.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.UPDATE.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
        }
    }
}
