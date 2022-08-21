package com.miui.gallery.assistant.manager.request.param;

import android.text.TextUtils;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.assistant.model.MediaScene;
import com.miui.gallery.assistant.process.BaseImageTask;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.util.BaseMiscUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class AnalyticFaceAndSceneParam implements RequestParams<List<MediaFeatureItem>> {
    public List<MediaFeatureItem> mClusterItems;
    public boolean mIsDownloadImage;
    public boolean mIsDownloadVideo;
    public boolean mIsSceneTagCalculate;

    public AnalyticFaceAndSceneParam(List<MediaFeatureItem> list, boolean z, boolean z2, boolean z3) {
        this.mClusterItems = list;
        this.mIsSceneTagCalculate = z;
        this.mIsDownloadImage = z2;
        this.mIsDownloadVideo = z3;
    }

    public boolean isSceneTagCalculate() {
        return this.mIsSceneTagCalculate;
    }

    @Override // com.miui.gallery.assistant.manager.request.param.RequestParams
    /* renamed from: getAlgorithmRequestInputs  reason: avoid collision after fix types in other method */
    public List<MediaFeatureItem> mo564getAlgorithmRequestInputs() {
        if (this.mIsSceneTagCalculate) {
            bindAnalyticSceneResult();
        }
        if (this.mIsDownloadImage || this.mIsDownloadVideo) {
            bindDownloadPath();
        }
        return this.mClusterItems;
    }

    public final void bindDownloadPath() {
        for (MediaFeatureItem mediaFeatureItem : this.mClusterItems) {
            if (this.mIsDownloadImage && mediaFeatureItem.isImage()) {
                mediaFeatureItem.setDownloadPath(BaseImageTask.getFilePath(mediaFeatureItem, DownloadType.THUMBNAIL, true).get());
            } else if (this.mIsDownloadVideo && mediaFeatureItem.isVideo()) {
                mediaFeatureItem.setDownloadPath(BaseImageTask.getFilePath(mediaFeatureItem, DownloadType.ORIGIN, true).get());
            }
        }
    }

    public final void bindAnalyticSceneResult() {
        HashSet hashSet = new HashSet();
        for (MediaFeatureItem mediaFeatureItem : this.mClusterItems) {
            hashSet.add(Long.valueOf(mediaFeatureItem.getMediaId()));
        }
        if (BaseMiscUtil.isValid(hashSet)) {
            List query = GalleryEntityManager.getInstance().query(MediaScene.class, String.format(Locale.US, "%s IN (%s) AND %s = %d AND %s = %d", "mediaId", TextUtils.join(",", hashSet), "version", 1, "isQuickResult", 0), null);
            if (!BaseMiscUtil.isValid(query)) {
                return;
            }
            for (MediaFeatureItem mediaFeatureItem2 : this.mClusterItems) {
                ArrayList arrayList = new ArrayList();
                Iterator it = query.iterator();
                while (it.hasNext()) {
                    MediaScene mediaScene = (MediaScene) it.next();
                    if (mediaFeatureItem2.getMediaId() == mediaScene.getMediaId()) {
                        arrayList.add(mediaScene);
                        it.remove();
                    }
                }
                if (BaseMiscUtil.isValid(arrayList)) {
                    mediaFeatureItem2.setMediaSceneResult(arrayList);
                }
            }
        }
    }
}
