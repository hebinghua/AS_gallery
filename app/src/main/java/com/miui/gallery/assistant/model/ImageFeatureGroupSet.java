package com.miui.gallery.assistant.model;

import android.util.LongSparseArray;
import com.miui.gallery.assistant.jni.cluster.ClusterNode;
import com.miui.gallery.assistant.model.ImageFeatureItem;
import com.miui.gallery.util.BaseMiscUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class ImageFeatureGroupSet<T extends ImageFeatureItem> extends HashSet<ImageFeatureGroup<T>> {
    public ImageFeatureGroupSet(List<T> list, List<ClusterNode> list2) {
        if (!BaseMiscUtil.isValid(list2) || !BaseMiscUtil.isValid(list)) {
            return;
        }
        LongSparseArray longSparseArray = new LongSparseArray();
        for (T t : list) {
            if (t.getMediaSha1() != null) {
                longSparseArray.put(t.getMediaId(), t);
            }
        }
        for (ClusterNode clusterNode : list2) {
            ArrayList arrayList = new ArrayList();
            Iterator<String> it = clusterNode.getImgs().iterator();
            while (it.hasNext()) {
                ImageFeatureItem imageFeatureItem = (ImageFeatureItem) longSparseArray.get(Long.valueOf(it.next()).longValue());
                if (imageFeatureItem != null) {
                    arrayList.add(imageFeatureItem);
                }
            }
            if (BaseMiscUtil.isValid(arrayList)) {
                add(new ImageFeatureGroup(arrayList));
            }
        }
    }

    public List<T> getSelectedImages() {
        ArrayList arrayList = new ArrayList();
        Iterator<ImageFeatureGroup<T>> it = iterator();
        while (it.hasNext()) {
            arrayList.add(((ImageFeatureGroup) it.next()).getBestImage());
        }
        return arrayList;
    }
}
