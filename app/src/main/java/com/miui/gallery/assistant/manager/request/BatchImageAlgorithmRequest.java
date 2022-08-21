package com.miui.gallery.assistant.manager.request;

import android.graphics.Bitmap;
import com.miui.gallery.assistant.algorithm.Algorithm;
import com.miui.gallery.assistant.manager.AlgorithmRequest;
import com.miui.gallery.assistant.manager.MediaFeatureManager;
import com.miui.gallery.assistant.manager.request.param.BgrParams;
import com.miui.gallery.assistant.manager.request.param.ImageFeatureBgrRequestParams;
import com.miui.gallery.assistant.manager.request.param.ImageFeatureBitmapRequestParams;
import com.miui.gallery.assistant.manager.result.BatchAlgorithmResult;
import com.miui.gallery.assistant.manager.result.ImageFeatureResult;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.util.assistant.FlagUtil;
import com.miui.gallery.util.assistant.ImageUtil;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes.dex */
public class BatchImageAlgorithmRequest extends ImageFeatureRequest<Bitmap, ImageFeatureBitmapRequestParams, BatchAlgorithmResult> {
    public final int mAlgorithmFlags;
    public Bitmap mBitmap;
    public int mHeight;
    public byte[] mPixs;
    public int mWidth;

    public BatchImageAlgorithmRequest(AlgorithmRequest.Priority priority, ImageFeatureBitmapRequestParams imageFeatureBitmapRequestParams, int i) {
        super(priority, imageFeatureBitmapRequestParams);
        this.mAlgorithmFlags = i;
    }

    @Override // com.miui.gallery.assistant.manager.AlgorithmRequest
    public BatchAlgorithmResult process(Bitmap bitmap) {
        int[] iArr;
        this.mBitmap = bitmap;
        if (bitmap == null) {
            return new BatchAlgorithmResult(3, this.mImageId);
        }
        this.mWidth = bitmap.getWidth();
        this.mHeight = this.mBitmap.getHeight();
        BatchAlgorithmResult batchAlgorithmResult = new BatchAlgorithmResult(0, this.mImageId);
        for (int i : Algorithm.FLAG_FEATURE_ALL_ARRAY) {
            if (FlagUtil.hasFlag(this.mAlgorithmFlags, i)) {
                long currentTimeMillis = System.currentTimeMillis();
                batchAlgorithmResult.add(i, processSingleRequest(i));
                DefaultLogger.d(this.TAG, "Flag %d AlgorithmRequest process using time %d ms", Integer.valueOf(i), Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            }
        }
        MediaFeatureManager.getInstance().recycleBitmap(this.mBitmap);
        return batchAlgorithmResult;
    }

    public final synchronized byte[] getBgrPixs() {
        if (this.mPixs == null) {
            this.mPixs = ImageUtil.getPixelsBGR(this.mBitmap);
        }
        return this.mPixs;
    }

    public final ImageFeatureResult processSingleRequest(int i) {
        MediaFeatureItem mediaFeatureItem = ((ImageFeatureBitmapRequestParams) this.mRequestParams).getMediaFeatureItem();
        if (FlagUtil.hasFlag(i, 1)) {
            return new QualityFeatureRequest(getPriority(), new ImageFeatureBgrRequestParams(mediaFeatureItem, ((ImageFeatureBitmapRequestParams) this.mRequestParams).isShouldDownloadIfNotExist(), ((ImageFeatureBitmapRequestParams) this.mRequestParams).isAllowedOverMetered(), ((ImageFeatureBitmapRequestParams) this.mRequestParams).getDownloadType())).process(new BgrParams(getBgrPixs(), this.mWidth, this.mHeight));
        }
        if (FlagUtil.hasFlag(i, 2)) {
            return new SceneFeatureRequest(getPriority(), new ImageFeatureBgrRequestParams(mediaFeatureItem, ((ImageFeatureBitmapRequestParams) this.mRequestParams).isShouldDownloadIfNotExist(), ((ImageFeatureBitmapRequestParams) this.mRequestParams).isAllowedOverMetered(), ((ImageFeatureBitmapRequestParams) this.mRequestParams).getDownloadType())).process(new BgrParams(getBgrPixs(), this.mWidth, this.mHeight));
        }
        if (!FlagUtil.hasFlag(i, 4)) {
            return null;
        }
        return new ClusterFeatureRequest(getPriority(), new ImageFeatureBgrRequestParams(mediaFeatureItem, ((ImageFeatureBitmapRequestParams) this.mRequestParams).isShouldDownloadIfNotExist(), ((ImageFeatureBitmapRequestParams) this.mRequestParams).isAllowedOverMetered(), ((ImageFeatureBitmapRequestParams) this.mRequestParams).getDownloadType())).process(new BgrParams(getBgrPixs(), this.mWidth, this.mHeight));
    }
}
