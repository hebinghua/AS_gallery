package com.miui.gallery.assistant.manager.request.param;

import com.miui.gallery.assistant.model.ImageFeatureItem;
import com.miui.gallery.assistant.utils.ImageFeatureItemUtils;
import java.util.List;

/* loaded from: classes.dex */
public class ClusteGroupRequestParams<E extends ImageFeatureItem> implements RequestParams<List<E>> {
    public List<E> mImageFeatureItems;
    public boolean mIsCalculateClusterFeature;

    public ClusteGroupRequestParams(List<E> list, boolean z) {
        this.mImageFeatureItems = list;
        this.mIsCalculateClusterFeature = z;
    }

    public boolean isCalculateClusterFeature() {
        return this.mIsCalculateClusterFeature;
    }

    @Override // com.miui.gallery.assistant.manager.request.param.RequestParams
    /* renamed from: getAlgorithmRequestInputs  reason: collision with other method in class */
    public List<E> mo564getAlgorithmRequestInputs() {
        new ImageFeatureItemUtils().bindMediaFeatures(this.mImageFeatureItems);
        return this.mImageFeatureItems;
    }
}
