package com.miui.gallery.assistant.manager.request;

import android.content.ContentValues;
import android.text.TextUtils;
import android.util.LongSparseArray;
import com.miui.gallery.assistant.algorithm.AlgorithmFactroy;
import com.miui.gallery.assistant.algorithm.ClusterAlgorithm;
import com.miui.gallery.assistant.cache.MediaFeatureCacheManager;
import com.miui.gallery.assistant.jni.cluster.ClusterNode;
import com.miui.gallery.assistant.library.LibraryManager;
import com.miui.gallery.assistant.manager.AlgorithmRequest;
import com.miui.gallery.assistant.manager.request.param.ClusteGroupRequestParams;
import com.miui.gallery.assistant.manager.request.param.ImageFeatureBgrRequestParams;
import com.miui.gallery.assistant.manager.result.ClusteGroupResult;
import com.miui.gallery.assistant.model.MediaFeature;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class ClusterGroupRequest extends AlgorithmRequest<List<MediaFeatureItem>, ClusteGroupRequestParams<MediaFeatureItem>, ClusteGroupResult> {
    public List<MediaFeatureItem> mImageFeatureItems;
    public MediaFeature[] mImageFeatures;
    public boolean mIsCalculateClusterFeature;
    public final boolean mSaveResult;

    public ClusterGroupRequest(AlgorithmRequest.Priority priority, ClusteGroupRequestParams<MediaFeatureItem> clusteGroupRequestParams, boolean z) {
        super(priority, clusteGroupRequestParams);
        this.mIsCalculateClusterFeature = clusteGroupRequestParams.isCalculateClusterFeature();
        this.mSaveResult = z;
    }

    @Override // com.miui.gallery.assistant.manager.AlgorithmRequest
    public ClusteGroupResult process(List<MediaFeatureItem> list) {
        this.mImageFeatureItems = list;
        if (BaseMiscUtil.isValid(list)) {
            if (this.mIsCalculateClusterFeature && !allImagesHaveClusterFeature()) {
                this.mIsCalculateClusterFeature = false;
                for (MediaFeatureItem mediaFeatureItem : this.mImageFeatureItems) {
                    new ClusterFeatureRequest(getPriority(), new ImageFeatureBgrRequestParams(mediaFeatureItem)).execute();
                }
                execute();
                DefaultLogger.d(this.TAG, "calculate cluster feature before group");
                return new ClusteGroupResult(6);
            }
            DefaultLogger.d(this.TAG, "Process %d images", Integer.valueOf(this.mImageFeatureItems.size()));
            int i = 0;
            for (int i2 = 0; i2 < this.mImageFeatureItems.size(); i2++) {
                MediaFeature mediaFeature = this.mImageFeatureItems.get(i2).getMediaFeature();
                if (mediaFeature != null && mediaFeature.getClusterFeature() != null) {
                    i++;
                }
            }
            DefaultLogger.d(this.TAG, "Valid image count:%d", Integer.valueOf(i));
            ArrayList<ArrayList<Float>> arrayList = new ArrayList<>();
            ArrayList<String> arrayList2 = new ArrayList<>();
            this.mImageFeatures = new MediaFeature[i];
            int i3 = 0;
            for (int i4 = 0; i4 < this.mImageFeatureItems.size() && i3 < i; i4++) {
                MediaFeature mediaFeature2 = this.mImageFeatureItems.get(i4).getMediaFeature();
                if (mediaFeature2 != null && mediaFeature2.getClusterFeature() != null) {
                    this.mImageFeatures[i3] = mediaFeature2;
                    arrayList.add(new ArrayList<>(Arrays.asList(mediaFeature2.getClusterFeature())));
                    arrayList2.add(String.valueOf(mediaFeature2.getMediaId()));
                    i3++;
                }
            }
            if (!LibraryManager.getInstance().loadLibrary(1004003L)) {
                DefaultLogger.d(this.TAG, "Load library %s failed", "ClusterAlgorithm");
                return new ClusteGroupResult(2);
            }
            try {
                try {
                    ArrayList<ClusterNode> clusterResult = ((ClusterAlgorithm) AlgorithmFactroy.getAlgorithmByFlag(8)).getClusterResult(arrayList, arrayList2);
                    if (!BaseMiscUtil.isValid(clusterResult)) {
                        return new ClusteGroupResult(1);
                    }
                    DefaultLogger.d(this.TAG, "ClusterAlgorithm getClusterResult success! ClustersResult.size: %d", Integer.valueOf(clusterResult.size()));
                    return new ClusteGroupResult(clusterResult);
                } catch (Exception e) {
                    e.printStackTrace();
                    AlgorithmFactroy.releaseAlgorithmByFlag(8);
                    return new ClusteGroupResult(7);
                }
            } finally {
                AlgorithmFactroy.releaseAlgorithmByFlag(8);
            }
        }
        return new ClusteGroupResult(3);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final boolean allImagesHaveClusterFeature() {
        /*
            r3 = this;
            java.util.List<com.miui.gallery.assistant.model.MediaFeatureItem> r0 = r3.mImageFeatureItems
            boolean r0 = com.miui.gallery.util.BaseMiscUtil.isValid(r0)
            if (r0 == 0) goto L2c
            java.util.List<com.miui.gallery.assistant.model.MediaFeatureItem> r0 = r3.mImageFeatureItems
            java.util.Iterator r0 = r0.iterator()
        Le:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L2c
            java.lang.Object r1 = r0.next()
            com.miui.gallery.assistant.model.MediaFeatureItem r1 = (com.miui.gallery.assistant.model.MediaFeatureItem) r1
            com.miui.gallery.assistant.model.MediaFeature r2 = r1.getMediaFeature()
            if (r2 == 0) goto L2a
            com.miui.gallery.assistant.model.MediaFeature r1 = r1.getMediaFeature()
            java.lang.Float[] r1 = r1.getClusterFeature()
            if (r1 != 0) goto Le
        L2a:
            r0 = 0
            return r0
        L2c:
            r0 = 1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.assistant.manager.request.ClusterGroupRequest.allImagesHaveClusterFeature():boolean");
    }

    @Override // com.miui.gallery.assistant.manager.AlgorithmRequest
    public void onSaveResult(ClusteGroupResult clusteGroupResult) {
        if (!this.mSaveResult || clusteGroupResult == null || clusteGroupResult.getResultCode() != 0 || !BaseMiscUtil.isValid(clusteGroupResult.getClusters())) {
            return;
        }
        LongSparseArray longSparseArray = new LongSparseArray(this.mImageFeatureItems.size());
        for (MediaFeatureItem mediaFeatureItem : this.mImageFeatureItems) {
            if (mediaFeatureItem.getMediaSha1() != null) {
                longSparseArray.put(mediaFeatureItem.getMediaId(), mediaFeatureItem);
            }
        }
        long nanoTime = System.nanoTime();
        for (ClusterNode clusterNode : clusteGroupResult.getClusters()) {
            ArrayList<String> imgs = clusterNode.getImgs();
            if (imgs != null && imgs.size() != 0) {
                long j = -1;
                Iterator<String> it = imgs.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    MediaFeatureItem mediaFeatureItem2 = (MediaFeatureItem) longSparseArray.get(Long.valueOf(it.next()).longValue());
                    if (mediaFeatureItem2 != null && mediaFeatureItem2.getMediaFeature() != null && mediaFeatureItem2.getMediaFeature().getClusterGroupId() > 0) {
                        j = mediaFeatureItem2.getMediaFeature().getClusterGroupId();
                        break;
                    }
                }
                if (j < 0) {
                    j = generateGroupId(nanoTime);
                }
                GalleryEntityManager galleryEntityManager = GalleryEntityManager.getInstance();
                ContentValues contentValues = new ContentValues();
                contentValues.put("clusterGroupId", Long.valueOf(j));
                galleryEntityManager.update(MediaFeature.class, contentValues, String.format("%s IN (%s)", "mediaId", TextUtils.join(",", imgs)), null);
                MediaFeatureCacheManager.getInstance().onImageFeaturesChanged(imgs);
            }
        }
    }

    public final long generateGroupId(long j) {
        return (System.currentTimeMillis() * 1000000) + ((System.nanoTime() - j) % 1000000);
    }
}
