package com.miui.gallery.util;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.LogPrinter;
import android.util.PrintStreamPrinter;
import android.util.Printer;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.FragmentActivity;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.google.common.collect.Maps;
import com.miui.backup.SignatureBackupHelper;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.db.sqlite3.GallerySQLiteHelper;
import com.miui.gallery.preference.ThumbnailPreference;
import com.miui.gallery.provider.CloudUtils;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.constants.StorageConstants;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.ui.ProcessTask;
import com.miui.gallery.ui.ProcessTaskForStoragePermissionMiss;
import com.miui.gallery.ui.ProgressDialogFragment;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import miuix.core.util.Pools;
import miuix.pickerwidget.date.DateUtils;

/* loaded from: classes2.dex */
public class DebugUtil {
    public static Pattern sDupPattern;
    public static final AtomicBoolean sDBExporting = new AtomicBoolean(false);
    public static final AtomicBoolean sIsDumpingDBInfo = new AtomicBoolean(false);

    public static /* synthetic */ Void $r8$lambda$3BsNfS3_7UtlqHQa7gnPqH_GNKw(Context context, Void[] voidArr) {
        return doCorrectPhotoTime(context);
    }

    public static String getDebugPath() {
        return StorageUtils.getPathInPrimaryStorage(StorageConstants.RELATIVE_DIRECTORY_DEBUG);
    }

    public static void exportFile(File file, File file2) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            FileChannel channel = fileInputStream.getChannel();
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            try {
                FileChannel channel2 = fileOutputStream.getChannel();
                channel2.transferFrom(channel, 0L, channel.size());
                DefaultLogger.d("DebugUtil", "Done exporting file: %s", file2.getPath());
                channel2.close();
                fileOutputStream.close();
                channel.close();
                fileInputStream.close();
            } catch (Throwable th) {
                try {
                    fileOutputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (Exception e) {
            DefaultLogger.e("DebugUtil", e);
        }
    }

    public static void doExportDB() {
        FileOutputStream fileOutputStream;
        AtomicBoolean atomicBoolean = sDBExporting;
        if (atomicBoolean.get()) {
            return;
        }
        atomicBoolean.set(true);
        File file = new File(getDebugPath());
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            try {
                HashMap newHashMap = Maps.newHashMap();
                newHashMap.put("gallery", "DBTest");
                newHashMap.put("gallery_sub", "DBTest_sub");
                newHashMap.put("gallery_lite", "DBTest_lite");
                String[] databaseList = GalleryApp.sGetAndroidContext().databaseList();
                if (databaseList != null && databaseList.length > 0) {
                    for (String str : databaseList) {
                        String fileNameWithoutExtension = BaseFileUtils.getFileNameWithoutExtension(str);
                        String extension = BaseFileUtils.getExtension(str);
                        if (!TextUtils.isEmpty(fileNameWithoutExtension) && !TextUtils.isEmpty(extension) && newHashMap.containsKey(fileNameWithoutExtension)) {
                            exportFile(GalleryApp.sGetAndroidContext().getDatabasePath(str), new File(file, ((String) newHashMap.get(fileNameWithoutExtension)) + "." + extension));
                        }
                    }
                }
                try {
                    fileOutputStream = new FileOutputStream(new File(file, DateUtils.formatDateTime(StaticContext.sGetAndroidContext(), System.currentTimeMillis(), 974).replace(":", "_") + ".txt"));
                } catch (Exception e) {
                    DefaultLogger.w("DebugUtil", "Error occurred while exporting date file, %s", e);
                }
            } catch (Exception e2) {
                DefaultLogger.w("DebugUtil", "Error occurred while exporting db, %s", e2);
            }
            try {
                PrintStream printStream = new PrintStream((OutputStream) fileOutputStream, true);
                doDumpDatabaseInfo(new PrintStreamPrinter(printStream));
                printStream.close();
                fileOutputStream.close();
                SignatureBackupHelper.createZipFile(getDebugPath(), file.getParent(), "Debug.zip");
                DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(getDebugPath(), IStoragePermissionStrategy.Permission.DELETE_DIRECTORY, FileHandleRecordHelper.appendInvokerTag("DebugUtil", "doExportDB"));
                if (documentFile != null) {
                    documentFile.delete();
                }
            } catch (Throwable th) {
                try {
                    fileOutputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } finally {
            sDBExporting.set(false);
        }
    }

    public static void exportDB(boolean z) {
        if (z) {
            ThreadManager.getMiscPool().submit(DebugUtil$$ExternalSyntheticLambda1.INSTANCE);
        } else {
            doExportDB();
        }
    }

    public static void generateDebugLog(FragmentActivity fragmentActivity) {
        new DebugTask(fragmentActivity).execute(new Void[0]);
    }

    /* loaded from: classes2.dex */
    public static class DebugTask extends AsyncTask<Void, Void, Void> {
        public static AtomicBoolean sDebugging = new AtomicBoolean(false);
        public WeakReference<FragmentActivity> mActivityRef;
        public ProgressDialogFragment mProgress;

        public DebugTask(FragmentActivity fragmentActivity) {
            this.mActivityRef = new WeakReference<>(fragmentActivity);
        }

        public final FragmentActivity getActivity() {
            FragmentActivity fragmentActivity;
            WeakReference<FragmentActivity> weakReference = this.mActivityRef;
            if (weakReference == null || (fragmentActivity = weakReference.get()) == null) {
                ProgressDialogFragment progressDialogFragment = this.mProgress;
                if (progressDialogFragment == null) {
                    return null;
                }
                progressDialogFragment.dismissAllowingStateLoss();
                return null;
            }
            return fragmentActivity;
        }

        @Override // android.os.AsyncTask
        public void onPreExecute() {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
                this.mProgress = progressDialogFragment;
                progressDialogFragment.setTitle(activity.getResources().getString(R.string.debugging_tip));
                this.mProgress.setCancelable(false);
                this.mProgress.showAllowingStateLoss(activity.getSupportFragmentManager(), "DebugUtil");
            }
        }

        @Override // android.os.AsyncTask
        public Void doInBackground(Void... voidArr) {
            if (sDebugging.get()) {
                return null;
            }
            sDebugging.set(true);
            DebugUtil.doExportDB();
            sDebugging.set(false);
            return null;
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Void r5) {
            ProgressDialogFragment progressDialogFragment = this.mProgress;
            if (progressDialogFragment == null || progressDialogFragment.getFragmentManager() == null) {
                return;
            }
            this.mProgress.dismissAllowingStateLoss();
            final FragmentActivity activity = getActivity();
            if (activity == null) {
                return;
            }
            DialogUtil.showConfirmAlert(activity, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.util.DebugUtil.DebugTask.1
                {
                    DebugTask.this = this;
                }

                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    String parentFolderPath = BaseFileUtils.getParentFolderPath(DebugUtil.getDebugPath());
                    if (!TextUtils.isEmpty(parentFolderPath)) {
                        IntentUtil.jumpToExplore(activity, parentFolderPath);
                    }
                }
            }, activity.getString(R.string.title_tip), activity.getString(R.string.debugging_info), activity.getString(R.string.ok));
        }
    }

    public static void doCorrectPhotoTime(Context context) {
        GalleryDBHelper.getInstance(context).getWritableDatabase().execSQL("UPDATE cloud SET mixedDateTime=dateTaken WHERE _id IN (SELECT _id FROM cloud WHERE serverType IN (1,2) AND exifGPSDateStamp LIKE '1970%' AND dateTaken > mixedDateTime)");
    }

    public static void correctPhotoTime(FragmentActivity fragmentActivity) {
        final Application application = fragmentActivity.getApplication();
        ProcessTask processTask = new ProcessTask(new ProcessTask.ProcessCallback() { // from class: com.miui.gallery.util.DebugUtil$$ExternalSyntheticLambda2
            @Override // com.miui.gallery.ui.ProcessTask.ProcessCallback
            public final Object doProcess(Object[] objArr) {
                return DebugUtil.$r8$lambda$3BsNfS3_7UtlqHQa7gnPqH_GNKw(application, (Void[]) objArr);
            }
        });
        processTask.setCompleteListener(new ProcessTask.OnCompleteListener() { // from class: com.miui.gallery.util.DebugUtil.1
            @Override // com.miui.gallery.ui.ProcessTask.OnCompleteListener
            public void onCompleteProcess(Object obj) {
                ToastUtils.makeText(application, (int) R.string.correct_time_success);
            }
        });
        processTask.showProgress(fragmentActivity, fragmentActivity.getString(R.string.correct_time_tip));
        processTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public static void deleteDupMedias(final FragmentActivity fragmentActivity) {
        final Application application = fragmentActivity.getApplication();
        ProcessTaskForStoragePermissionMiss processTaskForStoragePermissionMiss = new ProcessTaskForStoragePermissionMiss(new ProcessTask.ProcessCallback() { // from class: com.miui.gallery.util.DebugUtil$$ExternalSyntheticLambda3
            @Override // com.miui.gallery.ui.ProcessTask.ProcessCallback
            public final Object doProcess(Object[] objArr) {
                Object doDeleteDupMedias;
                doDeleteDupMedias = DebugUtil.doDeleteDupMedias(FragmentActivity.this);
                return doDeleteDupMedias;
            }
        });
        processTaskForStoragePermissionMiss.setCompleteListener(new ProcessTask.OnCompleteListener() { // from class: com.miui.gallery.util.DebugUtil.2
            @Override // com.miui.gallery.ui.ProcessTask.OnCompleteListener
            public void onCompleteProcess(Object obj) {
                ToastUtils.makeText(application, (int) R.string.clear_dup_success);
            }
        });
        processTaskForStoragePermissionMiss.setFragmentActivityForStoragePermissionMiss(fragmentActivity);
        processTaskForStoragePermissionMiss.showProgress(fragmentActivity, fragmentActivity.getString(R.string.clear_dup_tip));
        processTaskForStoragePermissionMiss.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public static void doDeleteDupMedias(FragmentActivity fragmentActivity) {
        String[] strArr = {j.c, "fileName", "localGroupId", "localFile", MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE};
        LinkedList linkedList = new LinkedList();
        Cursor cursor = null;
        try {
            Cursor query = fragmentActivity.getContentResolver().query(GalleryContract.Cloud.CLOUD_URI, strArr, "size < 1000000 AND serverType = 1 AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))", null, null);
            if (query != null) {
                while (query.moveToNext()) {
                    String string = query.getString(1);
                    long j = query.getLong(2);
                    String maybeDupMediaName = maybeDupMediaName(string);
                    if (!TextUtils.isEmpty(maybeDupMediaName)) {
                        String hasMedia = hasMedia(fragmentActivity, "title = ? AND localGroupId = ? AND _id != ? AND size >= ? AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))", new String[]{BaseFileUtils.getFileTitle(maybeDupMediaName), String.valueOf(j), String.valueOf(query.getLong(0)), String.valueOf(query.getLong(4))});
                        if (!TextUtils.isEmpty(hasMedia)) {
                            DefaultLogger.d("DebugUtil", "delete dup pair origin: %s, dup: %s, %s", hasMedia, string, Long.valueOf(query.getLong(2)));
                            linkedList.add(Long.valueOf(query.getLong(0)));
                        }
                    } else {
                        String hasMedia2 = hasMedia(fragmentActivity, "title = ? AND localGroupId = ? AND _id != ? AND size >= ? AND mimeType != ? AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))", new String[]{BaseFileUtils.getFileTitle(string), String.valueOf(j), String.valueOf(query.getLong(0)), String.valueOf(query.getLong(4)), "image/jpeg"});
                        if (!TextUtils.isEmpty(hasMedia2)) {
                            DefaultLogger.d("DebugUtil", "delete dup pair origin: %s, dup: %s, %s", hasMedia2, string, Long.valueOf(query.getLong(2)));
                            linkedList.add(Long.valueOf(query.getLong(0)));
                        }
                    }
                }
            }
            if (query != null) {
                query.close();
            }
            DefaultLogger.d("DebugUtil", "delete dup count %s", Integer.valueOf(linkedList.size()));
            try {
                CloudUtils.deleteById(fragmentActivity, -1, MiscUtil.listToArray(linkedList));
            } catch (StoragePermissionMissingException e) {
                e.offer(fragmentActivity);
            }
        } catch (Throwable th) {
            if (0 != 0) {
                cursor.close();
            }
            throw th;
        }
    }

    public static String maybeDupMediaName(String str) {
        int length;
        if (sDupPattern == null) {
            sDupPattern = Pattern.compile("^1[0-9]{12}");
        }
        String[] split = BaseFileUtils.getFileTitle(str).split("_");
        if (split != null && split.length > 1) {
            if (split[split.length - 1].equalsIgnoreCase("_BURST") || split[split.length - 1].equalsIgnoreCase("_TIMEBURST")) {
                length = split.length - 2;
            } else {
                length = split.length - 1;
            }
            String str2 = (length <= -1 || !sDupPattern.matcher(split[length]).matches()) ? null : split[length];
            if (str2 != null && str2.length() >= 13) {
                StringBuilder sb = new StringBuilder();
                int indexOf = str.indexOf(str2);
                sb.append(str.substring(0, indexOf - 1));
                sb.append(str.substring(indexOf + 13));
                return sb.toString();
            }
        }
        return null;
    }

    public static String hasMedia(Context context, String str, String[] strArr) {
        Cursor cursor = null;
        try {
            Cursor query = context.getContentResolver().query(GalleryContract.Cloud.CLOUD_URI, new String[]{"fileName"}, str, strArr, null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        String string = query.getString(0);
                        query.close();
                        return string;
                    }
                } catch (Throwable th) {
                    th = th;
                    cursor = query;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            }
            if (query != null) {
                query.close();
            }
            return null;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static void clearThumbnailErrorLog(Context context) {
        ThumbnailPreference.clear();
        ToastUtils.makeText(context, (int) R.string.thumbnail_error_log_cleared);
    }

    public static void dumpDatabaseInfo() {
        ThreadManager.getMiscPool().submit(DebugUtil$$ExternalSyntheticLambda0.INSTANCE);
    }

    public static /* synthetic */ Object lambda$dumpDatabaseInfo$3(ThreadPool.JobContext jobContext) {
        AtomicBoolean atomicBoolean = sIsDumpingDBInfo;
        if (atomicBoolean.compareAndSet(false, true)) {
            try {
                doDumpDatabaseInfo(new LogPrinter(3, "DebugUtil"));
                atomicBoolean.set(false);
                return null;
            } catch (Throwable th) {
                sIsDumpingDBInfo.set(false);
                throw th;
            }
        }
        return null;
    }

    public static void doDumpDatabaseInfo(Printer printer) {
        try {
            GallerySQLiteHelper.dump(new String[0], printer);
        } catch (Exception e) {
            DefaultLogger.w("DebugUtil", e);
        }
    }

    public static void printThreadPoolStatus(Executor executor, boolean z) {
        StringBuilder acquire = Pools.getStringBuilderPool().acquire();
        try {
            try {
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
                if (threadPoolExecutor != null) {
                    acquire.append("---------------------dump threadPool status---------------------------");
                    acquire.append("\n线程池当前活动的线程数:");
                    acquire.append(threadPoolExecutor.getActiveCount());
                    acquire.append("\n当前任务队列中的任务数:");
                    acquire.append(threadPoolExecutor.getQueue().size());
                    acquire.append("\n当前线程池总执行的任务数(包括已完成的):");
                    acquire.append(threadPoolExecutor.getTaskCount());
                    acquire.append("\n线程池当前已经执行完成了多少任务数:");
                    acquire.append(threadPoolExecutor.getCompletedTaskCount());
                    if (z) {
                        try {
                            acquire.append(threadPoolExecutor.toString());
                            acquire.append("-------------------------stack------------------------");
                            Iterator it = ((HashSet) getFieldValue(threadPoolExecutor, "workers")).iterator();
                            while (it.hasNext()) {
                                acquire.append(TextUtils.join("\n", ((Thread) getFieldValue(it.next(), "thread")).getStackTrace()));
                            }
                            acquire.append("-----------------------------------------------------");
                        } catch (Exception unused) {
                            DefaultLogger.e("DebugUtil", "cant print thread status");
                        }
                    }
                    acquire.append("---------------------dump threadPool status end-------------------------");
                }
                DefaultLogger.fd("DebugUtil", acquire.toString());
            } finally {
                Pools.getStringBuilderPool().release(acquire);
            }
        } catch (Exception e) {
            DefaultLogger.e("DebugUtil", "cant print executor status [%s]", e.getMessage());
        }
    }

    public static Object getFieldValue(Object obj, String str) throws NoSuchFieldException, IllegalAccessException {
        Field declaredField = obj.getClass().getDeclaredField(str);
        declaredField.setAccessible(true);
        return declaredField.get(obj);
    }

    public static void printThreadPoolTaskQueueStatus(Executor executor) {
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
        if (threadPoolExecutor != null) {
            DefaultLogger.d("DebugUtil", "当前任务队列中的任务数:[%s]", Integer.valueOf(threadPoolExecutor.getQueue().size()));
        }
    }

    public static long logEventTime(String str, String str2, boolean z) {
        long currentTimeMillis = System.currentTimeMillis();
        logEventTime(str, str2, currentTimeMillis, z);
        return currentTimeMillis;
    }

    public static void logEventTime(String str, String str2, long j, boolean z) {
        DefaultLogger.v(str, "[%s] event [%s]: [%d]", str2, z ? "end" : "start", Long.valueOf(j));
    }

    public static void logEventTime(String str, String str2, long j) {
        DefaultLogger.v(str, "[%s] event end,cost: [%d]", str2, Long.valueOf(System.currentTimeMillis() - j));
    }
}
