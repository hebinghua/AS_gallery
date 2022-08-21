package com.miui.gallery.assistant.algorithm;

import com.miui.gallery.assistant.jni.cluster.ClusterNode;
import com.miui.gallery.assistant.jni.cluster.SimilarImgClusterJNI;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class ClusterAlgorithm extends Algorithm {
    public SimilarImgClusterJNI mImageClusterJNI;

    @Override // com.miui.gallery.assistant.algorithm.Algorithm
    public void clearAlgorithm() {
    }

    public ClusterAlgorithm() {
        super(1004003L);
    }

    @Override // com.miui.gallery.assistant.algorithm.Algorithm
    public boolean onInitAlgorithm() {
        try {
            this.mImageClusterJNI = new SimilarImgClusterJNI();
            return true;
        } catch (Exception e) {
            DefaultLogger.e(this.TAG, e);
            return false;
        }
    }

    @Override // com.miui.gallery.assistant.algorithm.Algorithm
    public void onDestroyAlgorithm() {
        try {
            this.mImageClusterJNI.destroyObject();
        } catch (Exception e) {
            DefaultLogger.e(this.TAG, e);
        }
    }

    public synchronized Float[] extractFeature(byte[] bArr, int i, int i2) {
        if (bArr != null) {
            if (this.mIsNativeInitiated) {
                ArrayList<Float> feature = this.mImageClusterJNI.getFeature(bArr, bArr.length, i, i2);
                if (BaseMiscUtil.isValid(feature)) {
                    return (Float[]) feature.toArray(new Float[feature.size()]);
                }
            }
        }
        return null;
    }

    public synchronized ArrayList<ClusterNode> getClusterResult(ArrayList<ArrayList<Float>> arrayList, ArrayList<String> arrayList2) {
        if (this.mIsNativeInitiated) {
            return this.mImageClusterJNI.clusterImgs(arrayList, arrayList2).getSimilarCluster();
        }
        return null;
    }
}
