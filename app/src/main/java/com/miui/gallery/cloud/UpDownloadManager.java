package com.miui.gallery.cloud;

import android.content.ContentValues;
import androidx.documentfile.provider.DocumentFile;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.miui.gallery.cloud.thread.BackUploadTask;
import com.miui.gallery.cloud.thread.BaseTask;
import com.miui.gallery.cloud.thread.RequestCommandQueue;
import com.miui.gallery.cloud.thread.TaskFactory;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes.dex */
public class UpDownloadManager {
    public static final int[] DOWNLOAD_FILE_PRIORITY;
    public static final Map<Integer, Integer> PRIORITY_ELEMENTS;
    public static final Map<Integer, ThreadElement> THREAD_ELEMENTS;
    public static final TaskFactory sFactory;
    public static final UpDownloadManager[] sInstances;
    public final RequestItemExecutor mExecutor;
    public final OnItemChangedDecorator mItemChangedListener;

    public static int mapStatusToRequestItem(int i) {
        if (i != 1) {
            if (i == 2) {
                return 1;
            }
            return i != 3 ? -1 : 2;
        }
        return 0;
    }

    /* loaded from: classes.dex */
    public static class ThreadElement {
        public int batchSize;
        public int commandMaxSize;
        public int type;

        public ThreadElement(int i, int i2, int i3) {
            this.type = i;
            this.batchSize = i2;
            this.commandMaxSize = i3;
        }
    }

    static {
        HashMap newHashMap = Maps.newHashMap();
        newHashMap.put(0, new ThreadElement(0, 10, -1));
        newHashMap.put(1, new ThreadElement(1, 2, -1));
        newHashMap.put(2, new ThreadElement(2, 10, -1));
        newHashMap.put(3, new ThreadElement(3, 1, 30));
        newHashMap.put(4, new ThreadElement(4, 10, 100));
        newHashMap.put(5, new ThreadElement(5, 1, -1));
        newHashMap.put(6, new ThreadElement(6, 1, -1));
        newHashMap.put(7, new ThreadElement(7, 1, -1));
        THREAD_ELEMENTS = Collections.unmodifiableMap(newHashMap);
        HashMap newHashMap2 = Maps.newHashMap();
        newHashMap2.put(0, -1);
        newHashMap2.put(1, -1);
        newHashMap2.put(2, 1);
        newHashMap2.put(3, 1);
        newHashMap2.put(4, 0);
        newHashMap2.put(5, 0);
        newHashMap2.put(6, 2);
        newHashMap2.put(8, 2);
        newHashMap2.put(7, 2);
        newHashMap2.put(9, 7);
        newHashMap2.put(10, 5);
        newHashMap2.put(11, 6);
        newHashMap2.put(12, 3);
        newHashMap2.put(13, 4);
        PRIORITY_ELEMENTS = Collections.unmodifiableMap(newHashMap2);
        DOWNLOAD_FILE_PRIORITY = new int[]{9, 10, 11};
        sInstances = new UpDownloadManager[8];
        sFactory = new TaskFactory() { // from class: com.miui.gallery.cloud.UpDownloadManager.1
            @Override // com.miui.gallery.cloud.thread.TaskFactory
            public BaseTask createTask(int i, int i2, int i3, int i4, RequestCommandQueue.OnItemChangedListener onItemChangedListener) {
                switch (i) {
                    case 0:
                    case 1:
                        return new BackUploadTask(i, i2, i3, i4, onItemChangedListener);
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                        throw new UnsupportedOperationException("this api is deprecated");
                    default:
                        return null;
                }
            }
        };
    }

    public static int getBatchLimit(int i) {
        return THREAD_ELEMENTS.get(Integer.valueOf(i)).batchSize;
    }

    public static int getCommandMaxSize(int i) {
        return THREAD_ELEMENTS.get(Integer.valueOf(i)).commandMaxSize;
    }

    public static int getThreadType(int i) {
        return PRIORITY_ELEMENTS.get(Integer.valueOf(i)).intValue();
    }

    public void addOnItemChangedListener(RequestCommandQueue.OnItemChangedListener onItemChangedListener) {
        this.mItemChangedListener.addOnItemChangedListener(onItemChangedListener);
    }

    public int invokeList(List<RequestCloudItem> list, boolean z) {
        return this.mExecutor.invoke(list, z);
    }

    public void cancelAll(int i, boolean z, boolean z2) {
        this.mExecutor.cancelAll(i, z, z2);
    }

    public void cancelAll(boolean z, boolean z2) {
        for (int i = 0; i < 14; i++) {
            if (SyncConditionManager.check(i) != 0 && getThreadType(i) != -1) {
                this.mExecutor.cancelAll(i, z, z2);
            }
        }
    }

    public boolean hasDelayedItem() {
        return this.mExecutor.hasDelayedItem();
    }

    public static int dispatchList(List<RequestCloudItem> list, boolean z) {
        if (list.isEmpty()) {
            return 0;
        }
        return instance(list.get(0).priority).invokeList(list, z);
    }

    public static int dispatchList(List<RequestCloudItem> list) {
        return dispatchList(list, false);
    }

    public static void cancel(int i, boolean z, boolean z2) {
        int threadType = getThreadType(i);
        if (threadType == -1) {
            DefaultLogger.w("UpDownloadManager", "invalid thread type, priority=" + i);
            Thread.dumpStack();
            return;
        }
        UpDownloadManager[] upDownloadManagerArr = sInstances;
        if (upDownloadManagerArr[threadType] == null) {
            return;
        }
        upDownloadManagerArr[threadType].cancelAll(i, z, z2);
    }

    public static void cancelAllBackgroundPriority(boolean z, boolean z2) {
        UpDownloadManager upDownloadManager;
        HashSet hashSet = new HashSet();
        for (Map.Entry<Integer, Integer> entry : PRIORITY_ELEMENTS.entrySet()) {
            if (RequestItemBase.isBackgroundPriority(entry.getKey().intValue()) && entry.getValue().intValue() != -1 && !hashSet.contains(entry.getValue()) && (upDownloadManager = sInstances[entry.getValue().intValue()]) != null) {
                upDownloadManager.cancelAll(z, z2);
                hashSet.add(entry.getValue());
            }
        }
    }

    /* loaded from: classes.dex */
    public static class OnItemChangedDecorator implements RequestCommandQueue.OnItemChangedListener {
        public final List<RequestCommandQueue.OnItemChangedListener> mListeners;

        public OnItemChangedDecorator() {
            this.mListeners = Lists.newArrayList();
        }

        public void addOnItemChangedListener(RequestCommandQueue.OnItemChangedListener onItemChangedListener) {
            synchronized (this.mListeners) {
                this.mListeners.add(onItemChangedListener);
            }
        }

        @Override // com.miui.gallery.cloud.thread.RequestCommandQueue.OnItemChangedListener
        public void onRemoveItem(RequestCloudItem requestCloudItem) {
            UpDownloadManager.endDownload(requestCloudItem);
            synchronized (this.mListeners) {
                for (RequestCommandQueue.OnItemChangedListener onItemChangedListener : this.mListeners) {
                    onItemChangedListener.onRemoveItem(requestCloudItem);
                }
            }
        }

        @Override // com.miui.gallery.cloud.thread.RequestCommandQueue.OnItemChangedListener
        public void onAddItem(RequestCloudItem requestCloudItem) {
            UpDownloadManager.startDownload(requestCloudItem);
            synchronized (this.mListeners) {
                for (RequestCommandQueue.OnItemChangedListener onItemChangedListener : this.mListeners) {
                    onItemChangedListener.onAddItem(requestCloudItem);
                }
            }
        }
    }

    public UpDownloadManager(int i) {
        OnItemChangedDecorator onItemChangedDecorator = new OnItemChangedDecorator();
        this.mItemChangedListener = onItemChangedDecorator;
        this.mExecutor = new RequestItemExecutor(sFactory.createTask(i, PRIORITY_ELEMENTS.size(), getBatchLimit(i), getCommandMaxSize(i), onItemChangedDecorator));
    }

    public static UpDownloadManager instance(int i) {
        return instanceInternel(getThreadType(i));
    }

    public static synchronized UpDownloadManager instanceInternel(int i) {
        UpDownloadManager upDownloadManager;
        synchronized (UpDownloadManager.class) {
            UpDownloadManager[] upDownloadManagerArr = sInstances;
            if (upDownloadManagerArr[i] == null) {
                upDownloadManagerArr[i] = new UpDownloadManager(i);
            }
            upDownloadManager = upDownloadManagerArr[i];
        }
        return upDownloadManager;
    }

    public static void startDownload(RequestCloudItem requestCloudItem) {
        if (!needPersisit(requestCloudItem)) {
            return;
        }
        DefaultLogger.d("UpDownloadManager", requestCloudItem.getIdentity() + " start download");
        toDatabase(requestCloudItem.dbCloud, 1);
    }

    public static void endDownload(RequestCloudItem requestCloudItem) {
        if (!needPersisit(requestCloudItem)) {
            return;
        }
        DefaultLogger.d("UpDownloadManager", requestCloudItem.getIdentity() + " end download, status=" + requestCloudItem.getStatus());
        int status = requestCloudItem.getStatus();
        DBImage dBImage = requestCloudItem.dbCloud;
        if (status == -1 || status == 0) {
            DefaultLogger.e("UpDownloadManager", "bad status: status=" + status + ", file name=" + requestCloudItem.getFileName());
        } else if (status == 1) {
            DefaultLogger.e("UpDownloadManager", "bad status: status=" + status + ", file name=" + requestCloudItem.getFileName());
            toDatabase(dBImage, 2);
        } else if (status == 2) {
            toDatabase(dBImage, 3);
        } else if (status != 3) {
        } else {
            DefaultLogger.e("UpDownloadManager", "bad status: status=" + status + ", file name=" + requestCloudItem.getFileName());
            toDatabase(dBImage, 0);
            deleteTempFile(requestCloudItem);
        }
    }

    public static void deleteTempFile(RequestCloudItem requestCloudItem) {
        String downloadTempFilePath = requestCloudItem.getDownloadTempFilePath();
        String str = BaseFileUtils.getParentFolderPath(downloadTempFilePath) + "/." + BaseFileUtils.getFileName(downloadTempFilePath) + ".kinfo";
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("UpDownloadManager", "deleteTempFile");
        StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
        IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.DELETE;
        DocumentFile documentFile = storageStrategyManager.getDocumentFile(downloadTempFilePath, permission, appendInvokerTag);
        if (documentFile != null) {
            documentFile.delete();
        }
        DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(str, permission, appendInvokerTag);
        if (documentFile2 != null) {
            documentFile2.delete();
        }
    }

    public static boolean needPersisit(RequestCloudItem requestCloudItem) {
        int i = requestCloudItem.priority;
        for (int i2 : DOWNLOAD_FILE_PRIORITY) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }

    public static void toDatabase(DBImage dBImage, int i) {
        if (i == 1) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("downloadFileTime", Long.valueOf(System.currentTimeMillis()));
            GalleryUtils.safeUpdate(dBImage.getBaseUri(), contentValues, String.format(Locale.US, "(%s) AND (%s is null OR %s=%d)", "_id=?", "downloadFileStatus", "downloadFileStatus", 0), new String[]{dBImage.getId()});
        }
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("downloadFileStatus", Integer.valueOf(i));
        if (i != 3) {
            contentValues2.put("localFile", "");
        }
        GalleryUtils.safeUpdate(dBImage.getBaseUri(), contentValues2, "_id=?", new String[]{dBImage.getId()});
    }
}
