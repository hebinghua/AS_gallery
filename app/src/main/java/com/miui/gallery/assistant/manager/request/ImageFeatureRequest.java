package com.miui.gallery.assistant.manager.request;

import android.text.TextUtils;
import com.miui.gallery.assistant.manager.AlgorithmRequest;
import com.miui.gallery.assistant.manager.request.param.ImageFeatureRequestParams;
import com.miui.gallery.assistant.manager.result.AlgorithmResult;
import com.miui.gallery.assistant.manager.result.ImageFeatureResult;
import com.miui.gallery.assistant.model.MediaFeature;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.List;

/* loaded from: classes.dex */
public abstract class ImageFeatureRequest<I, P extends ImageFeatureRequestParams<I>, R extends ImageFeatureResult> extends AlgorithmRequest<I, P, R> {
    public long mImageDateTime;
    public long mImageId;
    public String mSha1;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.miui.gallery.assistant.manager.AlgorithmRequest
    public /* bridge */ /* synthetic */ void onSaveResult(AlgorithmResult algorithmResult) {
        onSaveResult((ImageFeatureRequest<I, P, R>) ((ImageFeatureResult) algorithmResult));
    }

    public ImageFeatureRequest(AlgorithmRequest.Priority priority, P p) {
        super(priority, p);
        if (p == null || p.getMediaFeatureItem() == null) {
            return;
        }
        this.mImageId = p.getMediaFeatureItem().getMediaId();
        this.mSha1 = p.getMediaFeatureItem().getSha1();
        this.mImageDateTime = p.getMediaFeatureItem().getDateTime();
    }

    public boolean updateFeatureToDb(R r) {
        if (r.getResultCode() == 3) {
            DefaultLogger.e(this.TAG, "Input Bitmap Error, do not update database.");
            return false;
        }
        GalleryEntityManager galleryEntityManager = GalleryEntityManager.getInstance();
        List query = galleryEntityManager.query(MediaFeature.class, String.format("%s=%s", "mediaId", Long.valueOf(this.mImageId)), null);
        if (BaseMiscUtil.isValid(query)) {
            MediaFeature mediaFeature = (MediaFeature) query.get(0);
            updateMediaFeature(mediaFeature, r);
            DefaultLogger.d(this.TAG, "update image %d feature,flag:%d", Long.valueOf(mediaFeature.getMediaId()), Integer.valueOf(mediaFeature.getFeatureFlag()));
            return galleryEntityManager.update(mediaFeature) > 0;
        }
        MediaFeature mediaFeature2 = new MediaFeature(this.mImageId, this.mSha1, this.mImageDateTime);
        updateMediaFeature(mediaFeature2, r);
        DefaultLogger.d(this.TAG, "insert image %d feature,flag:%d", Long.valueOf(mediaFeature2.getMediaId()), Integer.valueOf(mediaFeature2.getFeatureFlag()));
        return -1 != galleryEntityManager.insert(mediaFeature2);
    }

    public void onSaveResult(R r) {
        if (r == null || this.mImageId <= 0 || TextUtils.isEmpty(this.mSha1)) {
            return;
        }
        updateFeatureToDb(r);
    }

    public void updateMediaFeature(MediaFeature mediaFeature, R r) {
        if (r == null || mediaFeature == null) {
            return;
        }
        mediaFeature.setVersion(2);
        r.updateMediaFeature(mediaFeature);
    }
}
