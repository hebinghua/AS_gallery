package com.miui.gallery.assistant.cache;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import com.miui.gallery.assistant.manager.MediaFeatureManager;
import com.miui.gallery.assistant.model.MediaFeature;
import com.miui.gallery.assistant.model.TinyMediaFeature;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes.dex */
public class MediaFeatureCacheManager {
    public Map<Long, GroupBestImage> mGroupBestMap;
    public volatile boolean mInitialized;
    public volatile boolean mIsFirstShowImageSelection;
    public Map<Long, TinyMediaFeature> mMediaTinyFeatureMap;

    public MediaFeatureCacheManager() {
        this.mInitialized = false;
        this.mIsFirstShowImageSelection = false;
    }

    /* loaded from: classes.dex */
    public static final class MediaFeatureCacheManagerHolder {
        public static final MediaFeatureCacheManager S_MEDIA_FEATURE_CACHE_MANAGER = new MediaFeatureCacheManager();
    }

    public static MediaFeatureCacheManager getInstance() {
        return MediaFeatureCacheManagerHolder.S_MEDIA_FEATURE_CACHE_MANAGER;
    }

    public synchronized void init() {
        if (!this.mInitialized) {
            long currentTimeMillis = System.currentTimeMillis();
            List<TinyMediaFeature> allMediaFeatures = getAllMediaFeatures();
            int i = 16;
            this.mMediaTinyFeatureMap = new ConcurrentHashMap(allMediaFeatures != null ? allMediaFeatures.size() : 16);
            if (allMediaFeatures != null) {
                i = allMediaFeatures.size() / 2;
            }
            this.mGroupBestMap = new ConcurrentHashMap(i);
            if (BaseMiscUtil.isValid(allMediaFeatures)) {
                for (TinyMediaFeature tinyMediaFeature : allMediaFeatures) {
                    this.mMediaTinyFeatureMap.put(Long.valueOf(tinyMediaFeature.getImageId()), tinyMediaFeature);
                }
                allMediaFeatures.clear();
            }
            initGroupBestMap();
            this.mIsFirstShowImageSelection = GalleryPreferences.Assistant.isFirstShowImageSelection();
            this.mInitialized = true;
            DefaultLogger.d("MediaFeatureCacheManager", "Initialize use time: " + (System.currentTimeMillis() - currentTimeMillis) + "ms.");
            DefaultLogger.d("MediaFeatureCacheManager", "MediaFeature count: %d ; Cluster group count: %d ", Integer.valueOf(this.mMediaTinyFeatureMap.size()), Integer.valueOf(this.mGroupBestMap.size()));
        }
    }

    public boolean shouldShowImageSelectionTip() {
        return this.mInitialized && this.mIsFirstShowImageSelection && this.mMediaTinyFeatureMap.size() > 0;
    }

    public void setFirstShowImageSelection(boolean z) {
        this.mIsFirstShowImageSelection = z;
    }

    public TinyMediaFeature getImageFeature(long j) {
        if (this.mInitialized) {
            return this.mMediaTinyFeatureMap.get(Long.valueOf(j));
        }
        return null;
    }

    public void onImageFeaturesChanged(List<String> list) {
        List<TinyMediaFeature> mediaFeaturesById = getMediaFeaturesById(list);
        if (BaseMiscUtil.isValid(mediaFeaturesById)) {
            for (TinyMediaFeature tinyMediaFeature : mediaFeaturesById) {
                addImageFeature(tinyMediaFeature);
            }
        }
    }

    public synchronized boolean onImageDelete(long j) {
        TinyMediaFeature imageFeature = getImageFeature(j);
        if (!this.mInitialized || imageFeature == null || imageFeature.getClusterGroupId() <= 0) {
            return false;
        }
        long currentTimeMillis = System.currentTimeMillis();
        this.mMediaTinyFeatureMap.remove(Long.valueOf(j));
        ContentValues contentValues = new ContentValues();
        contentValues.put("mediaCalculationType", (Integer) 2);
        GalleryEntityManager.getInstance().update(MediaFeature.class, contentValues, String.format("%s = %s", "mediaId", Long.valueOf(j)), null);
        refreshGroupBestMapbyGroupId(imageFeature.getClusterGroupId());
        DefaultLogger.d("MediaFeatureCacheManager", "Delete image %d using %d ms!", Long.valueOf(j), Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        return true;
    }

    public final synchronized void refreshGroupBestMapbyGroupId(long j) {
        GroupBestImage groupBestImage = this.mGroupBestMap.get(Long.valueOf(j));
        if (groupBestImage != null) {
            if (groupBestImage.mImageCount.get() == 1) {
                this.mGroupBestMap.remove(Long.valueOf(j));
            } else {
                groupBestImage.clear();
                List<TinyMediaFeature> imageFeaturesByGroup = getImageFeaturesByGroup(j);
                if (BaseMiscUtil.isValid(imageFeaturesByGroup)) {
                    for (TinyMediaFeature tinyMediaFeature : imageFeaturesByGroup) {
                        refreshGroupBestMap(tinyMediaFeature, true);
                    }
                }
            }
        }
    }

    public synchronized boolean onImageDeleteBatch(List<Long> list) {
        if (this.mInitialized && BaseMiscUtil.isValid(list)) {
            long currentTimeMillis = System.currentTimeMillis();
            ArrayList arrayList = new ArrayList();
            for (Long l : list) {
                TinyMediaFeature imageFeature = getImageFeature(l.longValue());
                if (imageFeature != null && imageFeature.getClusterGroupId() > 0) {
                    arrayList.add(l);
                    this.mMediaTinyFeatureMap.remove(l);
                    refreshGroupBestMapbyGroupId(imageFeature.getClusterGroupId());
                }
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put("mediaCalculationType", (Integer) 2);
            GalleryEntityManager.getInstance().update(MediaFeature.class, contentValues, String.format("%s IN (%s)", "mediaId", TextUtils.join(",", arrayList)), null);
            DefaultLogger.d("MediaFeatureCacheManager", "Delete %d images batch using %d ms!", Integer.valueOf(list.size()), Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            return true;
        }
        return false;
    }

    public final synchronized List<TinyMediaFeature> getAllMediaFeatures() {
        ArrayList arrayList;
        arrayList = new ArrayList();
        fillTinyFeaturesFromCursor(arrayList, GalleryEntityManager.getInstance().rawQuery(MediaFeature.class, TinyMediaFeature.PROJECTION, MediaFeature.ALL_IQA_CLUSTER_SELECTION, null, null, null, "mediaId ASC", null));
        return arrayList;
    }

    public final synchronized List<TinyMediaFeature> getMediaFeaturesById(List<String> list) {
        ArrayList arrayList;
        arrayList = new ArrayList();
        if (BaseMiscUtil.isValid(list)) {
            fillTinyFeaturesFromCursor(arrayList, GalleryEntityManager.getInstance().rawQuery(MediaFeature.class, TinyMediaFeature.PROJECTION, MediaFeature.ALL_IQA_CLUSTER_SELECTION + " AND " + String.format("%s IN (%s)", "mediaId", TextUtils.join(",", list)), null, null, null, "mediaId ASC", null));
        }
        return arrayList;
    }

    public final void fillTinyFeaturesFromCursor(List<TinyMediaFeature> list, Cursor cursor) {
        if (list == null || cursor == null) {
            return;
        }
        while (cursor.moveToNext()) {
            try {
                list.add(new TinyMediaFeature(cursor));
            } finally {
                cursor.close();
            }
        }
    }

    public List<TinyMediaFeature> getImageFeaturesByGroup(long j) {
        if (this.mInitialized) {
            ArrayList arrayList = new ArrayList();
            for (TinyMediaFeature tinyMediaFeature : this.mMediaTinyFeatureMap.values()) {
                if (tinyMediaFeature.getClusterGroupId() == j) {
                    arrayList.add(tinyMediaFeature);
                }
            }
            return arrayList;
        }
        return null;
    }

    public final void addImageFeature(TinyMediaFeature tinyMediaFeature) {
        if (!this.mInitialized || tinyMediaFeature == null) {
            return;
        }
        refreshGroupBestMap(tinyMediaFeature, !this.mMediaTinyFeatureMap.containsKey(Long.valueOf(tinyMediaFeature.getImageId())));
        this.mMediaTinyFeatureMap.put(Long.valueOf(tinyMediaFeature.getImageId()), tinyMediaFeature);
    }

    public int getBestImageCount(boolean z) {
        int i = 0;
        if (this.mInitialized) {
            for (Map.Entry<Long, GroupBestImage> entry : this.mGroupBestMap.entrySet()) {
                GroupBestImage value = entry.getValue();
                if (value != null && (z || value.mImageCount.get() > 1)) {
                    if (value.mBestImage != null) {
                        i++;
                    }
                }
            }
        }
        return i;
    }

    public boolean shouldShowSelectionStar(long j, boolean z, boolean z2) {
        return MediaFeatureManager.isImageFeatureSelectionVisiable() && isBestImage(j, z, z2, null);
    }

    public boolean shouldShowSelectionStar(long j, boolean z, boolean z2, List<Long> list) {
        return MediaFeatureManager.isImageFeatureSelectionVisiable() && isBestImage(j, z, z2, list);
    }

    public boolean isBestImage(long j, boolean z, boolean z2, List<Long> list) {
        GroupBestImage groupBestImage;
        boolean z3;
        TinyMediaFeature imageFeature = getImageFeature(j);
        if (!this.mInitialized || imageFeature == null || (groupBestImage = this.mGroupBestMap.get(Long.valueOf(imageFeature.getClusterGroupId()))) == null) {
            return false;
        }
        TinyMediaFeature tinyMediaFeature = groupBestImage.mBestImage;
        TreeSet<TinyMediaFeature> imageFeatureInGroup = groupBestImage.getImageFeatureInGroup();
        if (list == null) {
            z3 = false;
            break;
        }
        Iterator<TinyMediaFeature> it = imageFeatureInGroup.iterator();
        while (it.hasNext()) {
            if (!list.contains(Long.valueOf(it.next().getImageId()))) {
                z3 = false;
                break;
            }
        }
        z3 = true;
        if (tinyMediaFeature == null && z2) {
            tinyMediaFeature = imageFeatureInGroup.first();
        }
        return (z || groupBestImage.getImageCount() > 1) && tinyMediaFeature != null && tinyMediaFeature.getImageId() == j && !z3;
    }

    public List<GroupBestImage> getAllGroups(boolean z) {
        ArrayList arrayList = new ArrayList();
        if (this.mInitialized) {
            for (Map.Entry<Long, GroupBestImage> entry : this.mGroupBestMap.entrySet()) {
                GroupBestImage value = entry.getValue();
                if (value != null && (z || value.getImageCount() > 1)) {
                    if (value.mBestImage != null) {
                        arrayList.add(value);
                    }
                }
            }
        }
        return arrayList;
    }

    public final synchronized void initGroupBestMap() {
        Collection<TinyMediaFeature> values = this.mMediaTinyFeatureMap.values();
        if (BaseMiscUtil.isValid(values)) {
            for (TinyMediaFeature tinyMediaFeature : values) {
                refreshGroupBestMap(tinyMediaFeature, true);
            }
        }
    }

    public final synchronized void refreshGroupBestMap(TinyMediaFeature tinyMediaFeature, boolean z) {
        if (tinyMediaFeature == null) {
            return;
        }
        if (this.mGroupBestMap.containsKey(Long.valueOf(tinyMediaFeature.getClusterGroupId()))) {
            GroupBestImage groupBestImage = this.mGroupBestMap.get(Long.valueOf(tinyMediaFeature.getClusterGroupId()));
            if (z) {
                groupBestImage.increase();
            }
            groupBestImage.tryReplace(tinyMediaFeature);
        } else {
            this.mGroupBestMap.put(Long.valueOf(tinyMediaFeature.getClusterGroupId()), new GroupBestImage(tinyMediaFeature.getClusterGroupId(), 1, tinyMediaFeature));
        }
    }

    /* loaded from: classes.dex */
    public static class GroupBestImage {
        public TinyMediaFeature mBestImage;
        public volatile long mGroupId;
        public AtomicInteger mImageCount;
        public TreeSet<TinyMediaFeature> mImageFeatureInGroup = new TreeSet<>();

        public GroupBestImage(long j, int i, TinyMediaFeature tinyMediaFeature) {
            this.mGroupId = j;
            this.mImageCount = new AtomicInteger(i);
            if (tinyMediaFeature != null) {
                this.mImageFeatureInGroup.add(tinyMediaFeature);
                if (tinyMediaFeature.isPoorImage()) {
                    return;
                }
                this.mBestImage = tinyMediaFeature;
            }
        }

        public TinyMediaFeature getBestImage() {
            return this.mBestImage;
        }

        public long getGroupId() {
            return this.mGroupId;
        }

        public int getImageCount() {
            return this.mImageCount.get();
        }

        public int increase() {
            return this.mImageCount.incrementAndGet();
        }

        public void tryReplace(TinyMediaFeature tinyMediaFeature) {
            if (tinyMediaFeature != null) {
                this.mImageFeatureInGroup.add(tinyMediaFeature);
                if (tinyMediaFeature.isPoorImage()) {
                    return;
                }
                TinyMediaFeature tinyMediaFeature2 = this.mBestImage;
                if (tinyMediaFeature2 != null && tinyMediaFeature2.getScore() >= tinyMediaFeature.getScore() && this.mBestImage.getImageId() != tinyMediaFeature.getImageId()) {
                    return;
                }
                this.mBestImage = tinyMediaFeature;
            }
        }

        public TreeSet<TinyMediaFeature> getImageFeatureInGroup() {
            return this.mImageFeatureInGroup;
        }

        public void clear() {
            AtomicInteger atomicInteger = this.mImageCount;
            atomicInteger.compareAndSet(atomicInteger.get(), 0);
            this.mBestImage = null;
            this.mImageFeatureInGroup.clear();
        }
    }
}
