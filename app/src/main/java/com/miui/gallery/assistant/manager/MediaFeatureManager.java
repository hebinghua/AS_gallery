package com.miui.gallery.assistant.manager;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.assistant.algorithm.Algorithm;
import com.miui.gallery.assistant.cache.MediaFeatureCacheManager;
import com.miui.gallery.assistant.library.LibraryConstantsHelper;
import com.miui.gallery.assistant.library.LibraryManager;
import com.miui.gallery.assistant.manager.AlgorithmRequest;
import com.miui.gallery.assistant.manager.request.BatchImageAlgorithmRequest;
import com.miui.gallery.assistant.manager.request.ClusterGroupRequest;
import com.miui.gallery.assistant.manager.request.param.ClusteGroupRequestParams;
import com.miui.gallery.assistant.manager.request.param.ImageFeatureBitmapRequestParams;
import com.miui.gallery.assistant.manager.result.AlgorithmResult;
import com.miui.gallery.assistant.manager.result.BatchAlgorithmResult;
import com.miui.gallery.assistant.manager.result.ClusteGroupResult;
import com.miui.gallery.assistant.model.FaceInfo;
import com.miui.gallery.assistant.model.MediaFeature;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.assistant.model.MediaScene;
import com.miui.gallery.assistant.model.PeopleEvent;
import com.miui.gallery.assistant.process.BaseImageTask;
import com.miui.gallery.card.CardManager;
import com.miui.gallery.card.CardUtil;
import com.miui.gallery.card.scenario.DateUtils;
import com.miui.gallery.card.scenario.ScenarioConstants;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.BuildUtil;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.PhotoMovieEntranceUtils;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.UriUtil;
import com.miui.gallery.util.assistant.FlagUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.os.Rom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class MediaFeatureManager {
    public static boolean sIsSupportMediaFeatureSelection;
    public static final String[] sWhiteList;
    public volatile boolean mHasMoreImageToProcess;
    public final Runnable mImageDeleteRunnable;
    public final ImageFeatureReusedBitCache mImageFeatureReusedBitCache;
    public volatile boolean mIsCaculating;
    public final ThreadPoolExecutor mThreadPoolExecutor;

    static {
        String[] strArr = {"dipper", "ursa", "jason", "wayne", "platina", "chiron", "sagit", "polaris", "perseus", "equuleus", "sirius", "comet", "lavender", "capricorn", "lithium", "natrium", "scorpio", "gemini", "cepheus", "davinci", "davinciin", "grus", "raphael", "raphaelin", "pyxis", "vela", "crux", "begonia", "begoniain", "beryllium", "tucana", "ginkgo", "violet", "laurus", "phoenix", "phoenixin", "andromeda", "cmi", "umi", "lmi", "lmiin", "lmiinpro", "draco", "picasso", "picassoin", "joyeuse", "excalibur", "durandal", "curtanacn", "curtana", "lmipro", "monet", "monetin", "vangogh", "toco", "tocoin", "gram", "apollo", "cas", "gauguin", "surya", "gauguinpro", "karna", "cannon", "cannong", "atom", "bomb", "cezanne", "venus", "gauguininpro", "star", "cetus", "courbet", "courbetin", "sweet", "sweetin", "haydn", "alioth", "rosemary", "maltose", "secret", "mojito", "rainbow", "sunny", "mars", "renoir", "ares", "aresin", "thyme", "haydnin", "aliothin", "chopin", "camellia", "camellian", "vayu", "bhima", "biloba", "odin", "vili", "enuma", "elish", "nabu", "argo", "agate", "agatein", "selene", "eos", "amber", "lisa", "mona", "evergo", "evergreen", "zeus", "cupid", "psyche", "pissarro", "pissarropro", "pissarroin", "pissarroinpro", "angelicain", "ingres", "poussin", "fleur", "miel", "veux", "peux", "spes", "spesn", "viva", "vida", "munch", "rubens", "matisse", "fog", "thor", "loki", "light", "thunder", "taoyao", "opal", "zijin", "zizhan", "xaga", "xagain", "xagapro", "xagaproin", "unicorn", "plato", "daumier", "mayfly", "wind", "rain", "mayfly", "lime", "citrus", "lemon", "pomelo", "diting"};
        sWhiteList = strArr;
        sIsSupportMediaFeatureSelection = false;
        if (BuildUtil.isBlackShark()) {
            sIsSupportMediaFeatureSelection = true;
            return;
        }
        String str = Build.DEVICE;
        if (TextUtils.isEmpty(str)) {
            return;
        }
        String[] split = str.split("_");
        if (split != null && split.length > 0) {
            str = split[0];
        }
        for (String str2 : strArr) {
            if (str2.equalsIgnoreCase(str)) {
                sIsSupportMediaFeatureSelection = true;
            }
        }
    }

    /* loaded from: classes.dex */
    public static final class MediaFeatureMangerHolder {
        public static final MediaFeatureManager INSTANCE = new MediaFeatureManager();
    }

    public static MediaFeatureManager getInstance() {
        return MediaFeatureMangerHolder.INSTANCE;
    }

    public MediaFeatureManager() {
        this.mHasMoreImageToProcess = false;
        this.mImageFeatureReusedBitCache = new ImageFeatureReusedBitCache();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 30L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
        this.mThreadPoolExecutor = threadPoolExecutor;
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        this.mIsCaculating = false;
        this.mImageDeleteRunnable = new Runnable() { // from class: com.miui.gallery.assistant.manager.MediaFeatureManager.1
            @Override // java.lang.Runnable
            public void run() {
                MediaFeatureManager.this.deleteImageFromCard();
            }
        };
    }

    public static boolean isImageFeatureCalculationEnable() {
        return isDeviceSupportMediaFeatureSelection();
    }

    public static boolean isImageFeatureSelectionVisiable() {
        return isImageFeatureCalculationEnable() && GalleryPreferences.Assistant.isImageSelectionFunctionOn();
    }

    public static boolean isStoryGenerateEnable() {
        return isDeviceSupportStoryFunction() && GalleryPreferences.Assistant.isStoryFunctionOn() && PhotoMovieEntranceUtils.isDeviceSupportPhotoMovie();
    }

    public static boolean isDeviceSupportMediaFeatureSelection() {
        return sIsSupportMediaFeatureSelection && Rom.IS_MIUI;
    }

    public static boolean isDeviceSupportStoryFunction() {
        return isImageFeatureCalculationEnable() && !BaseBuildUtil.isInternational() && !BuildUtil.isBlackShark();
    }

    public final void deleteImageFromCard() {
        this.mThreadPoolExecutor.execute(createImagesDeleteRunnable());
    }

    public final Runnable createImagesDeleteRunnable() {
        return new Runnable() { // from class: com.miui.gallery.assistant.manager.MediaFeatureManager.2
            @Override // java.lang.Runnable
            public void run() {
                if (MediaFeatureManager.isDeviceSupportStoryFunction()) {
                    MediaFeatureManager.this.deleteImagesFromCard();
                }
            }
        };
    }

    public final synchronized void deleteImagesFromCard() {
        List<MediaFeature> query = GalleryEntityManager.getInstance().query(MediaFeature.class, "mediaId>0 AND version=2 AND mediaCalculationType=2", null, null, null);
        if (!BaseMiscUtil.isValid(query)) {
            return;
        }
        DefaultLogger.d("MediaFeatureManager", "Delete %d images,search them in cards", Integer.valueOf(query.size()));
        ArrayList arrayList = new ArrayList(query.size());
        ArrayList arrayList2 = new ArrayList(query.size());
        for (MediaFeature mediaFeature : query) {
            arrayList.add(mediaFeature.getSha1());
            arrayList2.add(Long.valueOf(mediaFeature.getMediaId()));
        }
        if (BaseMiscUtil.isValid(arrayList)) {
            CardManager.getInstance().onDeleteImages(arrayList);
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("mediaCalculationType", (Integer) 3);
        GalleryEntityManager.getInstance().update(MediaFeature.class, contentValues, String.format("%s IN ('%s')", "sha1", TextUtils.join("','", arrayList)), null);
        GalleryEntityManager.getInstance().delete(MediaScene.class, String.format("%s IN ('%s')", "mediaId", TextUtils.join("','", arrayList2)), null);
        GalleryEntityManager.getInstance().delete(PeopleEvent.class, String.format("%s IN ('%s')", "mediaId", TextUtils.join("','", arrayList2)), null);
        GalleryEntityManager.getInstance().delete(FaceInfo.class, String.format("%s IN ('%s')", "mediaId", TextUtils.join("','", arrayList2)), null);
    }

    public void onImageDelete(String str) {
        try {
            onImageDelete(Long.parseLong(str));
        } catch (NumberFormatException e) {
            DefaultLogger.e("MediaFeatureManager", e);
        }
    }

    public void onImageDelete(long j) {
        if (MediaFeatureCacheManager.getInstance().onImageDelete(j)) {
            DefaultLogger.d("MediaFeatureManager", "Image %s delete or add to secret, delete them in cards", Long.valueOf(j));
            ThreadManager.getWorkHandler().removeCallbacks(this.mImageDeleteRunnable);
            ThreadManager.getWorkHandler().postDelayed(this.mImageDeleteRunnable, 3000L);
        }
    }

    public void onImageBatchDelete(List<Long> list) {
        if (!BaseMiscUtil.isValid(list) || !MediaFeatureCacheManager.getInstance().onImageDeleteBatch(list)) {
            return;
        }
        DefaultLogger.d("MediaFeatureManager", "%d Images delete or add to secret batch, delete them in cards", Integer.valueOf(list.size()));
        ThreadManager.getWorkHandler().removeCallbacks(this.mImageDeleteRunnable);
        ThreadManager.getWorkHandler().postDelayed(this.mImageDeleteRunnable, 3000L);
    }

    public void triggerNewImageCalculation() {
        if (!isNewImageCalculationEnable()) {
            DefaultLogger.d("MediaFeatureManager", "MediaFeature Selection Disable or Libraries not exist");
        } else if (this.mIsCaculating) {
        } else {
            this.mIsCaculating = true;
            this.mThreadPoolExecutor.execute(createNewImageCalculateRunnable());
        }
    }

    public boolean isNewImageCalculationEnable() {
        return isImageFeatureCalculationEnable() && LibraryManager.getInstance().isLibrarysExist(LibraryConstantsHelper.sImageFeatureSelectionLibraries);
    }

    public final Runnable createNewImageCalculateRunnable() {
        return new Runnable() { // from class: com.miui.gallery.assistant.manager.MediaFeatureManager.3
            @Override // java.lang.Runnable
            public void run() {
                MediaFeatureManager.this.scheduleNewImages();
            }
        };
    }

    public final void scheduleNewImages() {
        boolean z = false;
        if (MiscUtil.isAppProcessInForeground() && GalleryPreferences.Sync.getPowerCanSync()) {
            DefaultLogger.d("MediaFeatureManager", "Status meet,trigger new image feature calculation");
            AlgorithmRequest.Listener listener = new AlgorithmRequest.Listener() { // from class: com.miui.gallery.assistant.manager.MediaFeatureManager.4
                @Override // com.miui.gallery.assistant.manager.AlgorithmRequest.Listener
                public void onComputeComplete(AlgorithmResult algorithmResult) {
                }

                @Override // com.miui.gallery.assistant.manager.AlgorithmRequest.Listener
                public void onStart() {
                }

                @Override // com.miui.gallery.assistant.manager.AlgorithmRequest.Listener
                public void onCancel() {
                    MediaFeatureManager.this.markCaculateState(false);
                }

                @Override // com.miui.gallery.assistant.manager.AlgorithmRequest.Listener
                public void onSaveComplete(AlgorithmResult algorithmResult) {
                    MediaFeatureManager.this.markCaculateState(false);
                    if (!MediaFeatureManager.this.mHasMoreImageToProcess || algorithmResult == null || algorithmResult.getResultCode() != 0) {
                        return;
                    }
                    MediaFeatureManager.this.mHasMoreImageToProcess = false;
                    MediaFeatureManager.this.triggerNewImageCalculation();
                }
            };
            DefaultLogger.d("MediaFeatureManager", "Start process recent %d images for image selection", (Object) 200);
            List<MediaFeatureItem> list = (List) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), UriUtil.appendLimit(GalleryContract.Cloud.CLOUD_URI, 200), MediaFeatureItem.PROJECTION, ScenarioConstants.IMAGE_FEATURE_CALCULATION_SELECTION, (String[]) null, "mixedDateTime DESC", new SafeDBUtil.QueryHandler<List<MediaFeatureItem>>() { // from class: com.miui.gallery.assistant.manager.MediaFeatureManager.5
                @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                /* renamed from: handle  reason: collision with other method in class */
                public List<MediaFeatureItem> mo1808handle(Cursor cursor) {
                    if (cursor == null || !cursor.moveToFirst()) {
                        return null;
                    }
                    ArrayList arrayList = new ArrayList(cursor.getCount());
                    do {
                        arrayList.add(new MediaFeatureItem(cursor));
                    } while (cursor.moveToNext());
                    return arrayList;
                }
            });
            if (BaseMiscUtil.isValid(list)) {
                CardUtil.bindMediaFeatures(list);
                List<MediaFeatureItem> unProcessedImages = getUnProcessedImages(list, 30);
                if (BaseMiscUtil.isValid(unProcessedImages)) {
                    if (unProcessedImages.size() >= 30) {
                        z = true;
                    }
                    this.mHasMoreImageToProcess = z;
                    calculateImageFeatureAndGroup(AlgorithmRequest.Priority.PRIORITY_NORMAL, unProcessedImages, 5, listener);
                    return;
                }
            }
        }
        markCaculateState(false);
    }

    public final List<MediaFeatureItem> getUnProcessedImages(List<MediaFeatureItem> list, int i) {
        ArrayList arrayList = new ArrayList();
        if (BaseMiscUtil.isValid(list)) {
            for (int i2 = 0; i2 < list.size(); i2++) {
                MediaFeatureItem mediaFeatureItem = list.get(i2);
                if (i2 < list.size() - 1 && mediaFeatureItem.isSelectionFeatureDone() && !list.get(i2 + 1).isSelectionFeatureDone()) {
                    arrayList.add(mediaFeatureItem);
                } else if (!mediaFeatureItem.isSelectionFeatureDone() || (!arrayList.isEmpty() && DateUtils.withinTime(((MediaFeatureItem) arrayList.get(arrayList.size() - 1)).getDateTime(), mediaFeatureItem.getDateTime(), 3600000L))) {
                    arrayList.add(mediaFeatureItem);
                }
                if (arrayList.size() >= i) {
                    break;
                }
            }
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            if (!ensureImagePath((MediaFeatureItem) it.next())) {
                it.remove();
            }
        }
        return arrayList;
    }

    public final boolean ensureImagePath(MediaFeatureItem mediaFeatureItem) {
        String imagePath = mediaFeatureItem.getImagePath();
        if (TextUtils.isEmpty(imagePath)) {
            DefaultLogger.d("MediaFeatureManager", "get micro thumb image using time:%d ms", Long.valueOf(System.currentTimeMillis() - System.currentTimeMillis()));
            imagePath = BaseImageTask.getFilePath(mediaFeatureItem, DownloadType.MICRO, true).get();
        }
        return !TextUtils.isEmpty(imagePath);
    }

    public final boolean markCaculateState(boolean z) {
        DefaultLogger.d("MediaFeatureManager", "New image Caculating : %b", Boolean.valueOf(z));
        this.mIsCaculating = z;
        return this.mIsCaculating;
    }

    public static List<MediaFeatureItem> filterNearByImages(List<MediaFeatureItem> list) {
        LinkedList linkedList = new LinkedList();
        if (BaseMiscUtil.isValid(list)) {
            long j = 0;
            for (MediaFeatureItem mediaFeatureItem : list) {
                MediaFeature mediaFeature = mediaFeatureItem.getMediaFeature();
                if (mediaFeature == null) {
                    linkedList.add(mediaFeatureItem);
                } else {
                    long clusterGroupId = mediaFeature.getClusterGroupId();
                    if (clusterGroupId == 0) {
                        linkedList.add(mediaFeatureItem);
                    } else if (j == 0) {
                        linkedList.add(mediaFeatureItem);
                        j = clusterGroupId;
                    } else {
                        if (j != clusterGroupId) {
                            break;
                        }
                        linkedList.add(mediaFeatureItem);
                    }
                }
            }
        }
        return linkedList;
    }

    public static List<MediaFeatureItem> queryNearByMediaItems(long j) {
        return (List) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.Cloud.CLOUD_URI, MediaFeatureItem.PROJECTION, String.format("%s < %s AND %s > %s", "mixedDateTime", Long.valueOf(j), "mixedDateTime", Long.valueOf(j - 3600000)), (String[]) null, "mixedDateTime DESC", new SafeDBUtil.QueryHandler<List<MediaFeatureItem>>() { // from class: com.miui.gallery.assistant.manager.MediaFeatureManager.6
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public List<MediaFeatureItem> mo1808handle(Cursor cursor) {
                return MediaFeatureItem.getMediaFeatureItemsFromCursor(cursor);
            }
        });
    }

    public ClusteGroupResult calculateClusterGroupSync(List<MediaFeatureItem> list, boolean z, boolean z2, AlgorithmRequest.Listener<ClusteGroupResult> listener) {
        if (BaseMiscUtil.isValid(list)) {
            DefaultLogger.d("MediaFeatureManager", "calculateClusterGroupSync,image count:%d", Integer.valueOf(list.size()));
            ClusterGroupRequest clusterGroupRequest = new ClusterGroupRequest(AlgorithmRequest.Priority.PRIORITY_NORMAL, new ClusteGroupRequestParams(list, z), z2);
            clusterGroupRequest.setThreadListener(listener);
            return clusterGroupRequest.executeSync();
        }
        return new ClusteGroupResult(3);
    }

    public void calculateClusterGroupAsync(AlgorithmRequest.Priority priority, List<MediaFeatureItem> list, boolean z, boolean z2, AlgorithmRequest.Listener<ClusteGroupResult> listener, boolean z3) {
        if (BaseMiscUtil.isValid(list)) {
            DefaultLogger.d("MediaFeatureManager", "calculateClusterGroupAsync,image count:%d", Integer.valueOf(list.size()));
            ClusterGroupRequest clusterGroupRequest = new ClusterGroupRequest(priority, new ClusteGroupRequestParams(list, z), z2);
            if (z3) {
                clusterGroupRequest.setMainThreadListener(listener);
            } else {
                clusterGroupRequest.setThreadListener(listener);
            }
            clusterGroupRequest.execute();
        }
    }

    public void calculateImageFeatureAndGroup(AlgorithmRequest.Priority priority, List<MediaFeatureItem> list, int i, AlgorithmRequest.Listener listener) {
        calculateImageFeatureAndGroup(priority, list, i, listener, true);
    }

    public void calculateImageFeatureAndGroup(AlgorithmRequest.Priority priority, List<MediaFeatureItem> list, int i, AlgorithmRequest.Listener listener, boolean z) {
        int[] iArr;
        int i2;
        if (!BaseMiscUtil.isValid(list)) {
            if (listener == null) {
                return;
            }
            listener.onCancel();
            return;
        }
        DefaultLogger.d("MediaFeatureManager", "Calculate imageGroup with algorithm async,image count:%d", Integer.valueOf(list.size()));
        for (MediaFeatureItem mediaFeatureItem : list) {
            if (mediaFeatureItem.getMediaFeature() == null) {
                i2 = i;
            } else {
                int i3 = 0;
                for (int i4 : Algorithm.FLAG_FEATURE_ALL_ARRAY) {
                    if (FlagUtil.hasFlag(i, i4) && !mediaFeatureItem.getMediaFeature().isFeatureDone(i4)) {
                        i3 = FlagUtil.setFlag(i3, i4);
                    }
                }
                i2 = i3;
            }
            if (i2 != 0) {
                handleImageWithAlgorithmAsync(priority, mediaFeatureItem, true, i2, null);
            }
        }
        calculateClusterGroupAsync(priority, list, true, true, listener, z);
    }

    public void recycleBitmap(Bitmap bitmap) {
        this.mImageFeatureReusedBitCache.put(bitmap);
    }

    public Bitmap getReusedBitMap(BitmapFactory.Options options) {
        Bitmap bitmap = this.mImageFeatureReusedBitCache.get(options);
        StringBuilder sb = new StringBuilder();
        sb.append("get reused bitmap :");
        sb.append(bitmap != null);
        DefaultLogger.d("MediaFeatureManager", sb.toString());
        return bitmap;
    }

    public BatchAlgorithmResult handleImageWithAlgorithmSync(AlgorithmRequest.Priority priority, MediaFeatureItem mediaFeatureItem, boolean z, int i) {
        return (BatchAlgorithmResult) new BatchImageAlgorithmRequest(priority, new ImageFeatureBitmapRequestParams(mediaFeatureItem, z), i).executeSync();
    }

    public void handleImageWithAlgorithmAsync(AlgorithmRequest.Priority priority, MediaFeatureItem mediaFeatureItem, boolean z, int i, AlgorithmRequest.Listener<BatchAlgorithmResult> listener) {
        BatchImageAlgorithmRequest batchImageAlgorithmRequest = new BatchImageAlgorithmRequest(priority, new ImageFeatureBitmapRequestParams(mediaFeatureItem, z), i);
        batchImageAlgorithmRequest.setMainThreadListener(listener);
        batchImageAlgorithmRequest.execute();
    }
}
