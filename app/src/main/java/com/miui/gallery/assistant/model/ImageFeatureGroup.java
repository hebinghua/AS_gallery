package com.miui.gallery.assistant.model;

import com.miui.gallery.assistant.model.ImageFeatureItem;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes.dex */
public class ImageFeatureGroup<T extends ImageFeatureItem> {
    public T mBestImage;
    public final List<T> mImageFeatures;

    public ImageFeatureGroup(Collection<T> collection) {
        LinkedList linkedList = new LinkedList();
        this.mImageFeatures = linkedList;
        linkedList.addAll(collection);
        for (T t : collection) {
            T t2 = this.mBestImage;
            if (t2 == null) {
                this.mBestImage = t;
            } else if (t2.getMediaFeature() != null && t.getMediaFeature() != null && this.mBestImage.getMediaFeature().getScore() < t.getMediaFeature().getScore()) {
                this.mBestImage = t;
            }
        }
    }

    public T getBestImage() {
        return this.mBestImage;
    }
}
