package com.miui.gallery.assistant.process;

import android.database.Cursor;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.Config$ImageDownload;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.assistant.algorithm.Algorithm;
import com.miui.gallery.assistant.library.LibraryConstantsHelper;
import com.miui.gallery.assistant.library.LibraryDownloadTask;
import com.miui.gallery.assistant.library.LibraryManager;
import com.miui.gallery.assistant.manager.AlgorithmRequest;
import com.miui.gallery.assistant.manager.MediaFeatureManager;
import com.miui.gallery.assistant.manager.result.BatchAlgorithmResult;
import com.miui.gallery.assistant.manager.result.ClusteGroupResult;
import com.miui.gallery.assistant.model.ImageFeatureGroupSet;
import com.miui.gallery.assistant.model.MediaFeature;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.card.CardUtil;
import com.miui.gallery.card.scenario.ScenarioConstants;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.dao.base.Entity;
import com.miui.gallery.pendingtask.PendingTaskManager;
import com.miui.gallery.pendingtask.base.PendingTask;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.sdk.download.DownloadOptions;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.sdk.download.ImageDownloader;
import com.miui.gallery.sdk.download.assist.DownloadedItem;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.GsonUtils;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.assistant.FlagUtil;
import com.miui.gallery.util.cloudimageloader.CloudUriAdapter;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public abstract class BaseImageTask extends PendingTask<JSONObject> {
    public final String TAG;

    /* loaded from: classes.dex */
    public enum TriggerTaskType {
        NONE,
        IMAGE_FEATURE,
        IMAGE_FEATURE_CHARGING,
        FACE_AND_SCENE,
        FACE_AND_SCENE_CHARGING
    }

    public abstract DownloadType onGetImageDownloadType();

    public BaseImageTask(int i) {
        super(i);
        this.TAG = getClass().getSimpleName();
    }

    @Override // com.miui.gallery.pendingtask.base.PendingTask
    /* renamed from: parseData  reason: avoid collision after fix types in other method */
    public final JSONObject mo1252parseData(byte[] bArr) throws Exception {
        return bArr == null ? new JSONObject() : new JSONObject(new String(bArr, "utf-8"));
    }

    @Override // com.miui.gallery.pendingtask.base.PendingTask
    public final byte[] wrapData(JSONObject jSONObject) throws Exception {
        if (jSONObject == null) {
            return null;
        }
        return jSONObject.toString().getBytes("utf-8");
    }

    public boolean processItems(JSONObject jSONObject, List<MediaFeatureItem> list, boolean z, boolean z2) {
        ClusteGroupResult calculateClusterGroupSync;
        if (BaseMiscUtil.isValid(list)) {
            CardUtil.bindMediaFeatures(list);
            LibraryManager libraryManager = LibraryManager.getInstance();
            Long[] lArr = LibraryConstantsHelper.sImageProcessLibraries;
            if (libraryManager.loadLibrary(lArr)) {
                DefaultLogger.d(this.TAG, "processing %d images", Integer.valueOf(list.size()));
                if (onGetImageDownloadType() == null) {
                    throw new IllegalStateException("onGetImageDownloadType in Sub class return null!");
                }
                for (int i = 1; i < list.size(); i++) {
                    long currentTimeMillis = System.currentTimeMillis();
                    int i2 = i - 1;
                    calculateAllImageFeature(list.get(i2));
                    DefaultLogger.d(this.TAG, "Image %d ImageFeature Algorithm time: %d", Long.valueOf(list.get(i2).getId()), Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                }
                calculateAllImageFeature(list.get(list.size() - 1));
                return !z || (calculateClusterGroupSync = MediaFeatureManager.getInstance().calculateClusterGroupSync(list, true, true, null)) == null || calculateClusterGroupSync.getResultCode() == 0;
            }
            DefaultLogger.d(this.TAG, "loadLibrary fail, schedule download task!");
            JSONObject jSONObject2 = new JSONObject();
            try {
                jSONObject2.put("originalType", getType());
                jSONObject2.put("originalData", jSONObject);
                jSONObject2.put("libraryIds", GsonUtils.toString(lArr));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PendingTaskManager.getInstance().postTask(7, jSONObject2, LibraryDownloadTask.class.getSimpleName());
            return false;
        }
        return true;
    }

    public static List<MediaFeatureItem> getSelectedImages(List<MediaFeatureItem> list) {
        DefaultLogger.d("Scenario", "MediaItems length is %d", Integer.valueOf(list.size()));
        if (BaseMiscUtil.isValid(list)) {
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = new ArrayList();
            for (MediaFeatureItem mediaFeatureItem : list) {
                boolean isDocumentImage = mediaFeatureItem.isDocumentImage();
                if (isDocumentImage) {
                    DefaultLogger.d("Scenario", "this is a document, the mediaIds is %d", Long.valueOf(mediaFeatureItem.getMediaId()));
                }
                DefaultLogger.d("Scenario", "featureItem. length is %d", Integer.valueOf(arrayList2.size()));
                if (mediaFeatureItem.isVideo()) {
                    arrayList3.add(mediaFeatureItem);
                }
                if (mediaFeatureItem.getMediaFeature() != null) {
                    MediaFeature mediaFeature = mediaFeatureItem.getMediaFeature();
                    if (mediaFeatureItem.isImage() && mediaFeature.hasClusterFeature() && !isDocumentImage && !mediaFeature.isPoorMedia()) {
                        arrayList2.add(mediaFeatureItem);
                    }
                }
            }
            DefaultLogger.d("Scenario", "toProcessedItems length is %d", Integer.valueOf(arrayList2.size()));
            ClusteGroupResult calculateClusterGroupSync = MediaFeatureManager.getInstance().calculateClusterGroupSync(arrayList2, true, false, null);
            if (calculateClusterGroupSync != null && calculateClusterGroupSync.getResultCode() == 0) {
                ImageFeatureGroupSet imageFeatureGroupSet = new ImageFeatureGroupSet(arrayList2, calculateClusterGroupSync.getClusters());
                if (BaseMiscUtil.isValid(imageFeatureGroupSet)) {
                    arrayList.addAll(imageFeatureGroupSet.getSelectedImages());
                }
            }
            DefaultLogger.d("Scenario", "videoItems length is %d", Integer.valueOf(arrayList3.size()));
            arrayList.addAll(arrayList3);
            DefaultLogger.d("Scenario", "selectItems length is %d", Integer.valueOf(arrayList.size()));
            return arrayList;
        }
        return null;
    }

    public final void calculateAllImageFeature(MediaFeatureItem mediaFeatureItem) {
        int i;
        int[] iArr;
        if (mediaFeatureItem == null || !mediaFeatureItem.isImage()) {
            return;
        }
        if (mediaFeatureItem.getMediaFeature() != null) {
            i = 0;
            for (int i2 : Algorithm.FLAG_FEATURE_ALL_ARRAY) {
                if (!mediaFeatureItem.getMediaFeature().isFeatureDone(i2)) {
                    i = FlagUtil.setFlag(i, i2);
                }
            }
        } else {
            i = 5;
        }
        BatchAlgorithmResult handleImageWithAlgorithmSync = MediaFeatureManager.getInstance().handleImageWithAlgorithmSync(AlgorithmRequest.Priority.PRIORITY_NORMAL, mediaFeatureItem, false, i);
        if (handleImageWithAlgorithmSync != null && handleImageWithAlgorithmSync.getResultCode() != 3) {
            return;
        }
        MediaFeature mediaFeature = new MediaFeature(mediaFeatureItem.getId(), mediaFeatureItem.getSha1(), mediaFeatureItem.getDateTime());
        mediaFeature.setMediaCalculationType(-1);
        GalleryEntityManager.getInstance().insert(mediaFeature);
        DefaultLogger.d(this.TAG, "insert image %d feature, image path is null", Long.valueOf(mediaFeature.getMediaId()));
    }

    public static Future<String> getFilePath(MediaFeatureItem mediaFeatureItem, DownloadType downloadType, boolean z) {
        return ThreadManager.getMiscPool().submit(new ImageGuaranteeJob(mediaFeatureItem, downloadType, z));
    }

    public static String downloadImage(long j, DownloadType downloadType) throws Exception {
        if (!BaseNetworkUtils.isNetworkConnected() || (downloadType == DownloadType.ORIGIN && BaseNetworkUtils.isActiveNetworkMetered())) {
            throw new RuntimeException("network invalid.");
        }
        if (AccountCache.getAccount() == null) {
            throw new RuntimeException("no account.");
        }
        DownloadedItem loadSync = ImageDownloader.getInstance().loadSync(CloudUriAdapter.getDownloadUri(j), downloadType, new DownloadOptions.Builder().setRequireWLAN(Config$ImageDownload.requireWLAN(downloadType)).setRequireCharging(false).setRequirePower(true).build(), null);
        if (loadSync == null) {
            return null;
        }
        return loadSync.getFilePath();
    }

    public static List<Long> getAllProcessedSuccessFeatureImages() {
        ArrayList arrayList = new ArrayList();
        Cursor rawQuery = GalleryEntityManager.getInstance().rawQuery(MediaFeature.class, new String[]{"mediaId"}, MediaFeature.ALL_PROCESSED_SELECTION, null, null, null, "mediaId DESC", null);
        if (rawQuery != null) {
            while (rawQuery.moveToNext()) {
                try {
                    arrayList.add(Long.valueOf(Entity.getLong(rawQuery, "mediaId")));
                } finally {
                    rawQuery.close();
                }
            }
        }
        return arrayList;
    }

    public static List<MediaFeatureItem> queryMediaItemByIds(List<Long> list) {
        return queryMediaItem(ScenarioConstants.ALL_MEDIA_SCENARIO_CALCULATION_SELECTION + " AND " + j.c + " IN ('" + TextUtils.join("','", list) + "')");
    }

    public static List<MediaFeatureItem> queryMediaItem(String str) {
        return (List) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.Cloud.CLOUD_URI, MediaFeatureItem.PROJECTION, str, (String[]) null, "mixedDateTime DESC", new SafeDBUtil.QueryHandler<List<MediaFeatureItem>>() { // from class: com.miui.gallery.assistant.process.BaseImageTask.1
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public List<MediaFeatureItem> mo1808handle(Cursor cursor) {
                return MediaFeatureItem.getMediaFeatureItemsFromCursor(cursor);
            }
        });
    }

    /* loaded from: classes.dex */
    public static class ImageGuaranteeJob implements ThreadPool.Job<String> {
        public final DownloadType mDownloadType;
        public final boolean mIsDownloadCloudImage;
        public final MediaFeatureItem mMediaFeatureItem;

        public ImageGuaranteeJob(MediaFeatureItem mediaFeatureItem, DownloadType downloadType, boolean z) {
            this.mMediaFeatureItem = mediaFeatureItem;
            this.mDownloadType = downloadType;
            this.mIsDownloadCloudImage = z;
        }

        @Override // com.miui.gallery.concurrent.ThreadPool.Job
        /* renamed from: run  reason: collision with other method in class */
        public String mo1807run(ThreadPool.JobContext jobContext) {
            String str = null;
            if (this.mMediaFeatureItem != null) {
                String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("ImageGuaranteeJob", "run");
                StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
                String microThumbnailPath = this.mMediaFeatureItem.getMicroThumbnailPath();
                IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.QUERY;
                DocumentFile documentFile = storageStrategyManager.getDocumentFile(microThumbnailPath, permission, appendInvokerTag);
                String microThumbnailPath2 = (documentFile == null || !documentFile.exists()) ? null : this.mMediaFeatureItem.getMicroThumbnailPath();
                DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(this.mMediaFeatureItem.getThumbnailPath(), permission, appendInvokerTag);
                String thumbnailPath = (documentFile2 == null || !documentFile2.exists()) ? null : this.mMediaFeatureItem.getThumbnailPath();
                DocumentFile documentFile3 = StorageSolutionProvider.get().getDocumentFile(this.mMediaFeatureItem.getOriginPath(), permission, appendInvokerTag);
                String originPath = (documentFile3 == null || !documentFile3.exists()) ? null : this.mMediaFeatureItem.getOriginPath();
                try {
                } catch (Exception e) {
                    DefaultLogger.e("ImageGuaranteeJob", "downloadImage occur error.\n", e);
                }
                if (!this.mIsDownloadCloudImage) {
                    return TextUtils.isEmpty(thumbnailPath) ? TextUtils.isEmpty(originPath) ? microThumbnailPath2 : originPath : thumbnailPath;
                }
                DownloadType downloadType = this.mDownloadType;
                if (downloadType == DownloadType.ORIGIN) {
                    if (originPath == null) {
                        str = BaseImageTask.downloadImage(this.mMediaFeatureItem.getId(), this.mDownloadType);
                        this.mMediaFeatureItem.setOriginPath(str);
                        return str;
                    }
                } else if (downloadType != DownloadType.THUMBNAIL) {
                    if (microThumbnailPath2 != null) {
                        return microThumbnailPath2;
                    }
                    if (thumbnailPath == null) {
                        if (originPath == null) {
                            str = BaseImageTask.downloadImage(this.mMediaFeatureItem.getId(), this.mDownloadType);
                            this.mMediaFeatureItem.setMicroThumbnailPath(str);
                            return str;
                        }
                    }
                    return thumbnailPath;
                } else if (thumbnailPath != null) {
                    return thumbnailPath;
                } else {
                    if (originPath == null) {
                        str = BaseImageTask.downloadImage(this.mMediaFeatureItem.getId(), this.mDownloadType);
                        this.mMediaFeatureItem.setThumbnailPath(str);
                        return str;
                    }
                }
                return originPath;
            }
            return null;
        }
    }
}
