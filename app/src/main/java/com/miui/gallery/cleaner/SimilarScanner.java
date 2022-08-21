package com.miui.gallery.cleaner;

import android.database.Cursor;
import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.assistant.cache.MediaFeatureCacheManager;
import com.miui.gallery.assistant.manager.AlgorithmRequest;
import com.miui.gallery.assistant.manager.MediaFeatureManager;
import com.miui.gallery.assistant.manager.result.AlgorithmResult;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.assistant.model.TinyMediaFeature;
import com.miui.gallery.card.CardUtil;
import com.miui.gallery.card.scenario.DateUtils;
import com.miui.gallery.card.scenario.ScenarioConstants;
import com.miui.gallery.cleaner.BaseScanner;
import com.miui.gallery.cleaner.ScanResult;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.UriUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.mirror.synergy.CallMethod;
import com.xiaomi.stat.a.j;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/* loaded from: classes.dex */
public class SimilarScanner extends BaseScanner {
    public static final String SQL_IMAGES_SELECTION;
    public static final String SQL_IMAGES_SELECTION_BASE;
    public static final String SQL_IMAGES_SELECTION_IN_SAME;
    public List<Long> mExcludedAlbumIds;
    public long mFeatureSqlStartId;
    public long mFeatureSqlStartTime;
    public Future mFurtherLoadingTask;
    public HashMap<Long, BaseScanner.MediaItem> mId2Item;
    public volatile boolean mIsLoadingValid;
    public final Object mScanLock;
    public boolean mScanLockCompleted;
    public long mScreenShotId;
    public final ArrayList<SimilarGroup> mSimilarGroupList;

    /* loaded from: classes.dex */
    public interface OnClusterCompletedListener {
        void onAllCompleted();

        void onCompleteInBatch();
    }

    @Override // com.miui.gallery.cleaner.BaseScanner
    public String getSelection() {
        return null;
    }

    static {
        String str = ScenarioConstants.IMAGE_FEATURE_CALCULATION_SELECTION + " AND localGroupId not in (%s)";
        SQL_IMAGES_SELECTION_BASE = str;
        SQL_IMAGES_SELECTION = str + " AND mixedDateTime <= %s";
        SQL_IMAGES_SELECTION_IN_SAME = str + " AND mixedDateTime = %s AND " + j.c + " <= %d";
    }

    public SimilarScanner() {
        super(3);
        this.mScanLock = new Object();
        this.mId2Item = new HashMap<>();
        this.mSimilarGroupList = new ArrayList<>();
        this.mFeatureSqlStartTime = Long.MAX_VALUE;
        this.mIsLoadingValid = false;
        this.mScanLockCompleted = false;
        this.mScreenShotId = -1L;
        this.mFeatureSqlStartId = Long.MAX_VALUE;
    }

    public List<Long> getExcludedAlbumIds() {
        List<Long> list = (List) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryCloudUtils.ALBUM_URI, new String[]{j.c, CallMethod.ARG_EXTRA_STRING}, (String) null, (String[]) null, (String) null, SimilarScanner$$ExternalSyntheticLambda0.INSTANCE);
        long j = this.mScreenShotId;
        if (j == -1) {
            long queryScreenshotAlbumId = AlbumDataHelper.queryScreenshotAlbumId(GalleryApp.sGetAndroidContext());
            this.mScreenShotId = queryScreenshotAlbumId;
            if (queryScreenshotAlbumId != -1) {
                list.add(Long.valueOf(queryScreenshotAlbumId));
            }
        } else {
            list.add(Long.valueOf(j));
        }
        return list == null ? new ArrayList() : list;
    }

    public static /* synthetic */ List lambda$getExcludedAlbumIds$0(Cursor cursor) {
        ArrayList arrayList = new ArrayList();
        if (cursor == null || !cursor.moveToFirst()) {
            return arrayList;
        }
        do {
            Album.ExtraInfo parseExtraInfo = AlbumDataHelper.parseExtraInfo(cursor.getString(1));
            if (parseExtraInfo != null && !TextUtils.isEmpty(parseExtraInfo.getBabyInfo())) {
                arrayList.add(Long.valueOf(cursor.getLong(0)));
            }
        } while (cursor.moveToNext());
        return arrayList;
    }

    @Override // com.miui.gallery.cleaner.BaseScanner
    public ScanResult scan() {
        this.mExcludedAlbumIds = getExcludedAlbumIds();
        this.mIsLoadingValid = true;
        this.mFeatureSqlStartTime = Long.MAX_VALUE;
        this.mFeatureSqlStartId = Long.MAX_VALUE;
        do {
            resetSimilarGroupList();
            if (this.mSimilarGroupList.size() <= 0 && MediaFeatureManager.getInstance().isNewImageCalculationEnable()) {
                this.mScanLockCompleted = false;
                DefaultLogger.d("SimilarScanner", "handle images by limit");
                handleImagesByLimit(30, 30, new OnClusterCompletedListener() { // from class: com.miui.gallery.cleaner.SimilarScanner.1
                    @Override // com.miui.gallery.cleaner.SimilarScanner.OnClusterCompletedListener
                    public void onCompleteInBatch() {
                    }

                    @Override // com.miui.gallery.cleaner.SimilarScanner.OnClusterCompletedListener
                    public void onAllCompleted() {
                        synchronized (SimilarScanner.this.mScanLock) {
                            SimilarScanner.this.mScanLockCompleted = true;
                            DefaultLogger.d("SimilarScanner", "unlock for similar cluster result");
                            SimilarScanner.this.mScanLock.notifyAll();
                        }
                    }
                });
                synchronized (this.mScanLock) {
                    while (!this.mScanLockCompleted && this.mIsLoadingValid) {
                        try {
                            DefaultLogger.d("SimilarScanner", "lock for similar cluster result");
                            this.mScanLock.wait();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (this.mSimilarGroupList.size() > 0 || this.mFeatureSqlStartTime <= 0 || !this.mIsLoadingValid) {
                break;
            }
        } while (MediaFeatureManager.getInstance().isNewImageCalculationEnable());
        if (this.mIsLoadingValid && this.mFeatureSqlStartTime > 0 && MediaFeatureManager.getInstance().isNewImageCalculationEnable()) {
            handleRemainImages();
        }
        DefaultLogger.d("SimilarScanner", "scan finish.");
        return buildScanResult();
    }

    @Override // com.miui.gallery.cleaner.BaseScanner
    public ScanResult buildScanResult() {
        long j;
        int i;
        int i2 = 10;
        ScanResult.ResultImage[] resultImageArr = new ScanResult.ResultImage[10];
        synchronized (this.mSimilarGroupList) {
            Iterator<SimilarGroup> it = this.mSimilarGroupList.iterator();
            j = 0;
            i = 0;
            int i3 = 0;
            while (it.hasNext()) {
                SimilarGroup next = it.next();
                Long[] lArr = next.mImageIdsInGroup;
                next.mStartPosIndex = i3;
                i3 += lArr.length;
                int i4 = 0;
                while (i4 < lArr.length) {
                    BaseScanner.MediaItem mediaItem = this.mId2Item.get(lArr[i4]);
                    if (i < i2) {
                        resultImageArr[i] = new ScanResult.ResultImage(mediaItem.mId, mediaItem.mPath);
                    }
                    i++;
                    j += mediaItem.mSize;
                    i4++;
                    i2 = 10;
                }
            }
        }
        return new ScanResult.Builder().setType(this.mType).setImages(resultImageArr).setSize(j).setCount(i).setOnScanResultClickListener(this.mOnScanResultClickListener).build();
    }

    @Override // com.miui.gallery.cleaner.BaseScanner
    public void recordClickScanResultEvent() {
        HashMap hashMap = new HashMap();
        hashMap.put("result", String.valueOf(this.mMediaItems.size()));
        SamplingStatHelper.recordCountEvent("cleaner", "cleaner_result_similar_click", hashMap);
        TrackController.trackClick("403.27.1.1.11318", AutoTracking.getRef());
    }

    @Override // com.miui.gallery.cleaner.BaseScanner
    public void onReset() {
        synchronized (this.mSimilarGroupList) {
            super.onReset();
            this.mSimilarGroupList.clear();
            this.mId2Item.clear();
            this.mFeatureSqlStartTime = Long.MAX_VALUE;
            this.mIsLoadingValid = false;
            this.mScanLockCompleted = false;
            Future future = this.mFurtherLoadingTask;
            if (future != null) {
                future.cancel();
                this.mFurtherLoadingTask = null;
            }
        }
    }

    @Override // com.miui.gallery.cleaner.BaseScanner
    public boolean removeItem(long j) {
        boolean z;
        synchronized (this.mSimilarGroupList) {
            Iterator<SimilarGroup> it = this.mSimilarGroupList.iterator();
            z = false;
            while (it.hasNext()) {
                SimilarGroup next = it.next();
                Long[] lArr = next.mImageIdsInGroup;
                int i = 0;
                while (true) {
                    if (i >= lArr.length) {
                        break;
                    } else if (lArr[i].longValue() == j) {
                        if (lArr.length == 1) {
                            it.remove();
                        } else {
                            Long[] lArr2 = new Long[lArr.length - 1];
                            System.arraycopy(lArr, 0, lArr2, 0, i);
                            if (i < lArr.length - 1) {
                                System.arraycopy(lArr, i + 1, lArr2, i, (lArr.length - i) - 1);
                            }
                            next.mImageIdsInGroup = lArr2;
                        }
                        z = true;
                        continue;
                    } else {
                        i++;
                    }
                }
                if (z) {
                    break;
                }
            }
        }
        return z;
    }

    @Override // com.miui.gallery.cleaner.BaseScanner
    public List<Long> getScanResultIds() {
        ArrayList arrayList;
        synchronized (this.mSimilarGroupList) {
            arrayList = new ArrayList();
            int size = this.mSimilarGroupList.size();
            for (int i = 0; i < size; i++) {
                for (Long l : this.mSimilarGroupList.get(i).mImageIdsInGroup) {
                    arrayList.add(l);
                }
            }
        }
        return arrayList;
    }

    public ArrayList<Integer> getGroupItemCount() {
        ArrayList<Integer> arrayList;
        synchronized (this.mSimilarGroupList) {
            arrayList = new ArrayList<>(this.mSimilarGroupList.size());
            Iterator<SimilarGroup> it = this.mSimilarGroupList.iterator();
            while (it.hasNext()) {
                arrayList.add(Integer.valueOf(it.next().mImageIdsInGroup.length));
            }
        }
        return arrayList;
    }

    public ArrayList<Integer> getGroupStartPos() {
        ArrayList<Integer> arrayList;
        synchronized (this.mSimilarGroupList) {
            arrayList = new ArrayList<>(this.mSimilarGroupList.size());
            Iterator<SimilarGroup> it = this.mSimilarGroupList.iterator();
            while (it.hasNext()) {
                arrayList.add(Integer.valueOf(it.next().mStartPosIndex));
            }
        }
        return arrayList;
    }

    public ArrayList<Long> getClusterGroupId() {
        ArrayList<Long> arrayList;
        synchronized (this.mSimilarGroupList) {
            arrayList = new ArrayList<>(this.mSimilarGroupList.size());
            Iterator<SimilarGroup> it = this.mSimilarGroupList.iterator();
            while (it.hasNext()) {
                arrayList.add(Long.valueOf(it.next().mGroupId));
            }
        }
        return arrayList;
    }

    public void removeSingleItemGroups() {
        boolean z;
        synchronized (this.mSimilarGroupList) {
            Iterator<SimilarGroup> it = this.mSimilarGroupList.iterator();
            z = false;
            while (it.hasNext()) {
                if (it.next().mImageIdsInGroup.length <= 1) {
                    it.remove();
                    z = true;
                }
            }
        }
        if (z) {
            updateScanResult(buildScanResult());
        }
    }

    @Override // com.miui.gallery.cleaner.BaseScanner
    public boolean isLoadingValid() {
        return this.mIsLoadingValid;
    }

    public final void handleRemainImages() {
        this.mFurtherLoadingTask = ThreadManager.getMiscPool().submit(new ThreadPool.Job<Object>() { // from class: com.miui.gallery.cleaner.SimilarScanner.2
            public OnClusterCompletedListener mOnCompleteListener = new OnClusterCompletedListener() { // from class: com.miui.gallery.cleaner.SimilarScanner.2.1
                @Override // com.miui.gallery.cleaner.SimilarScanner.OnClusterCompletedListener
                public void onCompleteInBatch() {
                    if (SimilarScanner.this.mIsLoadingValid) {
                        SimilarScanner.this.resetSimilarGroupList();
                        SimilarScanner similarScanner = SimilarScanner.this;
                        similarScanner.updateScanResult(similarScanner.buildScanResult());
                        DefaultLogger.d("SimilarScanner", "update scan result.");
                    }
                }

                @Override // com.miui.gallery.cleaner.SimilarScanner.OnClusterCompletedListener
                public void onAllCompleted() {
                    if (SimilarScanner.this.mFeatureSqlStartTime <= 0 || !SimilarScanner.this.mIsLoadingValid) {
                        SimilarScanner.this.mIsLoadingValid = false;
                        SimilarScanner.this.resetSimilarGroupList();
                        SimilarScanner similarScanner = SimilarScanner.this;
                        similarScanner.updateScanResult(similarScanner.buildScanResult());
                        DefaultLogger.d("SimilarScanner", "finish handle all images.");
                        return;
                    }
                    SimilarScanner.this.handleImagesByLimit(1000, 30, this);
                }
            };

            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public Object mo1807run(ThreadPool.JobContext jobContext) {
                if (SimilarScanner.this.mIsLoadingValid) {
                    DefaultLogger.d("SimilarScanner", "handle remain images");
                    SimilarScanner.this.handleImagesByLimit(1000, 30, this.mOnCompleteListener);
                    return null;
                }
                return null;
            }
        });
    }

    public final boolean handleImagesByLimit(final int i, int i2, OnClusterCompletedListener onClusterCompletedListener) {
        String format;
        if (this.mFeatureSqlStartId != Long.MAX_VALUE) {
            format = String.format(Locale.US, SQL_IMAGES_SELECTION_IN_SAME, TextUtils.join(", ", this.mExcludedAlbumIds), Long.valueOf(this.mFeatureSqlStartTime), Long.valueOf(this.mFeatureSqlStartId));
        } else {
            format = String.format(Locale.US, SQL_IMAGES_SELECTION, TextUtils.join(", ", this.mExcludedAlbumIds), Long.valueOf(this.mFeatureSqlStartTime));
        }
        List<MediaFeatureItem> list = (List) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), UriUtil.appendLimit(GalleryContract.Cloud.CLOUD_URI, i), MediaFeatureItem.PROJECTION, format, (String[]) null, "mixedDateTime DESC, _id DESC", new SafeDBUtil.QueryHandler<List<MediaFeatureItem>>() { // from class: com.miui.gallery.cleaner.SimilarScanner.3
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public List<MediaFeatureItem> mo1808handle(Cursor cursor) {
                long j = 0;
                if (cursor == null || !cursor.moveToFirst() || !SimilarScanner.this.mIsLoadingValid) {
                    SimilarScanner.this.mFeatureSqlStartId = Long.MAX_VALUE;
                    if (!SimilarScanner.this.isLoadingValid()) {
                        return null;
                    }
                    SimilarScanner.this.mFeatureSqlStartTime = 0L;
                    return null;
                }
                ArrayList arrayList = new ArrayList(cursor.getCount());
                do {
                    arrayList.add(new MediaFeatureItem(cursor));
                } while (cursor.moveToNext());
                if (arrayList.size() >= i) {
                    if (SimilarScanner.this.mFeatureSqlStartTime == ((MediaFeatureItem) arrayList.get(i - 1)).getDateTime()) {
                        SimilarScanner.this.mFeatureSqlStartId = ((MediaFeatureItem) arrayList.get(i - 1)).getId();
                        DefaultLogger.d("SimilarScanner", "SAME!! sqlStartTime: %d, sqlStartId: %d", Long.valueOf(SimilarScanner.this.mFeatureSqlStartTime), Long.valueOf(SimilarScanner.this.mFeatureSqlStartId));
                    } else {
                        SimilarScanner.this.mFeatureSqlStartTime = ((MediaFeatureItem) arrayList.get(i - 1)).getDateTime();
                        SimilarScanner.this.mFeatureSqlStartId = Long.MAX_VALUE;
                    }
                } else {
                    SimilarScanner similarScanner = SimilarScanner.this;
                    if (similarScanner.mFeatureSqlStartId != Long.MAX_VALUE) {
                        j = ((MediaFeatureItem) arrayList.get(arrayList.size() - 1)).getDateTime() - 1;
                    }
                    similarScanner.mFeatureSqlStartTime = j;
                    SimilarScanner.this.mFeatureSqlStartId = Long.MAX_VALUE;
                }
                return arrayList;
            }
        });
        if (BaseMiscUtil.isValid(list) && this.mIsLoadingValid) {
            CardUtil.bindMediaFeatures(list);
        }
        if (this.mIsLoadingValid) {
            calculateImageFeatureAndCluster(list, i2, onClusterCompletedListener);
        }
        return list != null && list.size() >= i;
    }

    public final void calculateImageFeatureAndCluster(List<MediaFeatureItem> list, int i, OnClusterCompletedListener onClusterCompletedListener) {
        if (this.mIsLoadingValid) {
            calculateImageFeatureAndClusterInBatch(list, 0, i, onClusterCompletedListener);
        }
    }

    public final void calculateImageFeatureAndClusterInBatch(final List<MediaFeatureItem> list, int i, final int i2, final OnClusterCompletedListener onClusterCompletedListener) {
        ArrayList arrayList = new ArrayList();
        if (BaseMiscUtil.isValid(list) && this.mIsLoadingValid) {
            int i3 = i;
            final int i4 = i3;
            while (true) {
                if (i3 >= list.size()) {
                    break;
                }
                MediaFeatureItem mediaFeatureItem = list.get(i3);
                if (i3 < list.size() - 1 && mediaFeatureItem.isSelectionFeatureDone() && !list.get(i3 + 1).isSelectionFeatureDone()) {
                    arrayList.add(mediaFeatureItem);
                } else if (!mediaFeatureItem.isSelectionFeatureDone() || (!arrayList.isEmpty() && DateUtils.withinTime(list.get(list.size() - 1).getDateTime(), mediaFeatureItem.getDateTime(), 3600000L))) {
                    arrayList.add(mediaFeatureItem);
                }
                if (arrayList.size() >= i2) {
                    i4 = i3;
                    break;
                } else {
                    i4 = i3;
                    i3++;
                }
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                if (TextUtils.isEmpty(((MediaFeatureItem) it.next()).getImagePath())) {
                    it.remove();
                }
            }
            DefaultLogger.d("SimilarScanner", "images.size: %d, start: %d, nextStart: %d, imagesToHandled.size: %d", Integer.valueOf(list.size()), Integer.valueOf(i), Integer.valueOf(i4), Integer.valueOf(arrayList.size()));
            if (this.mIsLoadingValid && BaseMiscUtil.isValid(arrayList) && arrayList.size() > 1) {
                MediaFeatureManager.getInstance().calculateImageFeatureAndGroup(AlgorithmRequest.Priority.PRIORITY_HIGH, new ArrayList(arrayList), 5, new AlgorithmRequest.Listener() { // from class: com.miui.gallery.cleaner.SimilarScanner.4
                    @Override // com.miui.gallery.assistant.manager.AlgorithmRequest.Listener
                    public void onCancel() {
                    }

                    @Override // com.miui.gallery.assistant.manager.AlgorithmRequest.Listener
                    public void onComputeComplete(AlgorithmResult algorithmResult) {
                    }

                    @Override // com.miui.gallery.assistant.manager.AlgorithmRequest.Listener
                    public void onStart() {
                    }

                    @Override // com.miui.gallery.assistant.manager.AlgorithmRequest.Listener
                    public void onSaveComplete(AlgorithmResult algorithmResult) {
                        if (SimilarScanner.this.mIsLoadingValid) {
                            SimilarScanner.this.calculateImageFeatureAndClusterInBatch(list, i4, i2, onClusterCompletedListener);
                        }
                        onClusterCompletedListener.onCompleteInBatch();
                    }
                }, false);
                return;
            } else {
                onClusterCompletedListener.onAllCompleted();
                return;
            }
        }
        onClusterCompletedListener.onAllCompleted();
    }

    public final void resetSimilarGroupList() {
        List<MediaFeatureCacheManager.GroupBestImage> similarGroups = getSimilarGroups();
        synchronized (this.mSimilarGroupList) {
            this.mSimilarGroupList.clear();
            HashSet hashSet = new HashSet();
            HashSet hashSet2 = new HashSet();
            for (MediaFeatureCacheManager.GroupBestImage groupBestImage : similarGroups) {
                Long valueOf = Long.valueOf(groupBestImage.getGroupId());
                if (hashSet.add(valueOf)) {
                    List<Long> imagesIdsByGroup = getImagesIdsByGroup(valueOf.longValue());
                    Iterator<Long> it = imagesIdsByGroup.iterator();
                    while (it.hasNext()) {
                        if (!hashSet2.add(Long.valueOf(it.next().longValue()))) {
                            it.remove();
                        }
                    }
                    if (imagesIdsByGroup.size() > 1) {
                        this.mSimilarGroupList.add(new SimilarGroup(valueOf.longValue(), (Long[]) imagesIdsByGroup.toArray(new Long[imagesIdsByGroup.size()])));
                    }
                }
            }
            resetIdToItemList();
            Iterator<SimilarGroup> it2 = this.mSimilarGroupList.iterator();
            Set<Long> keySet = this.mId2Item.keySet();
            while (it2.hasNext()) {
                SimilarGroup next = it2.next();
                Long[] lArr = next.mImageIdsInGroup;
                int i = 0;
                int i2 = 0;
                while (true) {
                    if (i >= lArr.length) {
                        break;
                    }
                    if (!keySet.contains(lArr[i])) {
                        Long[] lArr2 = next.mImageIdsInGroup;
                        if (lArr2.length <= 2) {
                            it2.remove();
                            break;
                        }
                        Long[] lArr3 = new Long[lArr2.length - 1];
                        System.arraycopy(lArr2, 0, lArr3, 0, i2);
                        Long[] lArr4 = next.mImageIdsInGroup;
                        if (i2 < lArr4.length - 1) {
                            System.arraycopy(lArr4, i2 + 1, lArr3, i2, (lArr4.length - i2) - 1);
                        }
                        next.mImageIdsInGroup = lArr3;
                    } else {
                        i2++;
                    }
                    i++;
                }
            }
        }
    }

    public final List<MediaFeatureCacheManager.GroupBestImage> getSimilarGroups() {
        List<MediaFeatureCacheManager.GroupBestImage> allGroups = MediaFeatureCacheManager.getInstance().getAllGroups(false);
        if (BaseMiscUtil.isValid(allGroups)) {
            Collections.sort(allGroups, new Comparator<MediaFeatureCacheManager.GroupBestImage>() { // from class: com.miui.gallery.cleaner.SimilarScanner.5
                @Override // java.util.Comparator
                public int compare(MediaFeatureCacheManager.GroupBestImage groupBestImage, MediaFeatureCacheManager.GroupBestImage groupBestImage2) {
                    TinyMediaFeature bestImage = groupBestImage.getBestImage();
                    TinyMediaFeature bestImage2 = groupBestImage2.getBestImage();
                    long j = 0;
                    long imageDateTime = bestImage == null ? 0L : bestImage.getImageDateTime();
                    if (bestImage2 != null) {
                        j = bestImage2.getImageDateTime();
                    }
                    int i = (imageDateTime > j ? 1 : (imageDateTime == j ? 0 : -1));
                    if (i > 0) {
                        return -1;
                    }
                    return i < 0 ? 1 : 0;
                }
            });
        }
        return allGroups;
    }

    public final List<Long> getImagesIdsByGroup(long j) {
        List<TinyMediaFeature> imageFeaturesByGroup = MediaFeatureCacheManager.getInstance().getImageFeaturesByGroup(j);
        Collections.sort(imageFeaturesByGroup);
        ArrayList arrayList = new ArrayList();
        for (TinyMediaFeature tinyMediaFeature : imageFeaturesByGroup) {
            arrayList.add(Long.valueOf(tinyMediaFeature.getImageId()));
        }
        return arrayList;
    }

    public final void resetIdToItemList() {
        Set<Long> keySet = this.mId2Item.keySet();
        List<Long> scanResultIds = getScanResultIds();
        ArrayList arrayList = new ArrayList();
        for (Long l : scanResultIds) {
            if (!keySet.contains(l)) {
                arrayList.add(l);
            }
        }
        if (BaseMiscUtil.isValid(arrayList)) {
            SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.Media.URI_OWNER_ALBUM_DETAIL_MEDIA, CleanerContract$Projection.SIMILAR_SCAN_PROJECTION, String.format(Locale.US, "%s IN (%s) AND %s not in (%s)", j.c, TextUtils.join(",", arrayList), "localGroupId", TextUtils.join(", ", this.mExcludedAlbumIds)), (String[]) null, (String) null, new SafeDBUtil.QueryHandler<Object>() { // from class: com.miui.gallery.cleaner.SimilarScanner.6
                @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                /* renamed from: handle */
                public Object mo1808handle(Cursor cursor) {
                    boolean z;
                    if (cursor == null || !cursor.moveToFirst()) {
                        return null;
                    }
                    HashMap hashMap = new HashMap();
                    do {
                        BaseScanner.MediaItem mediaItem = new BaseScanner.MediaItem();
                        mediaItem.mId = cursor.getLong(0);
                        mediaItem.mSha1 = cursor.getString(5);
                        String string = cursor.getString(3);
                        mediaItem.mPath = string;
                        if (TextUtils.isEmpty(string)) {
                            mediaItem.mPath = cursor.getString(2);
                        }
                        if (TextUtils.isEmpty(mediaItem.mPath)) {
                            mediaItem.mPath = cursor.getString(4);
                        }
                        mediaItem.mSize = cursor.getLong(1);
                        mediaItem.mCreateTime = cursor.getLong(6);
                        if (TextUtils.isEmpty(mediaItem.mPath)) {
                            SimilarScanner.this.mId2Item.put(Long.valueOf(mediaItem.mId), mediaItem);
                        } else {
                            String str = BaseFileUtils.getParentFolderPath(mediaItem.mPath) + File.separator + "1.jpg";
                            if (hashMap.containsKey(str)) {
                                z = ((Boolean) hashMap.get(str)).booleanValue();
                            } else {
                                boolean checkStoragePermission = SimilarScanner.this.checkStoragePermission(str);
                                hashMap.put(str, Boolean.valueOf(checkStoragePermission));
                                z = checkStoragePermission;
                            }
                            if (z) {
                                SimilarScanner.this.mId2Item.put(Long.valueOf(mediaItem.mId), mediaItem);
                            }
                        }
                    } while (cursor.moveToNext());
                    return null;
                }
            });
        }
    }

    /* loaded from: classes.dex */
    public static class SimilarGroup {
        public long mGroupId;
        public Long[] mImageIdsInGroup;
        public int mStartPosIndex;

        public SimilarGroup(long j, Long[] lArr) {
            this.mImageIdsInGroup = lArr;
            this.mGroupId = j;
        }
    }
}
