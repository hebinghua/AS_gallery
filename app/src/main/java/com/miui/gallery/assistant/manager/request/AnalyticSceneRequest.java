package com.miui.gallery.assistant.manager.request;

import android.text.TextUtils;
import com.miui.gallery.assistant.algorithm.AlgorithmFactroy;
import com.miui.gallery.assistant.algorithm.AnalyticFaceAndSceneAlgorithm;
import com.miui.gallery.assistant.library.LibraryManager;
import com.miui.gallery.assistant.manager.AlgorithmRequest;
import com.miui.gallery.assistant.manager.request.param.AnalyticSceneParam;
import com.miui.gallery.assistant.manager.request.param.PathParam;
import com.miui.gallery.assistant.manager.result.AnalyticSceneResult;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.assistant.model.MediaScene;
import com.miui.gallery.assistant.utils.AnalyticUtils;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.player.videoAnalytic;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class AnalyticSceneRequest extends AlgorithmRequest<PathParam, AnalyticSceneParam, AnalyticSceneResult> {
    public long mFileSize;
    public boolean mIsQuickCalculateVideo;
    public long mMediaId;
    public int mMediaType;
    public String mPath;

    public AnalyticSceneRequest(AlgorithmRequest.Priority priority, AnalyticSceneParam analyticSceneParam) {
        super(priority, analyticSceneParam);
        MediaFeatureItem mediaFeatureItem = analyticSceneParam.getMediaFeatureItem();
        this.mMediaId = mediaFeatureItem.getMediaId();
        this.mMediaType = mediaFeatureItem.isVideo() ? 1 : 0;
        this.mFileSize = mediaFeatureItem.getFileSize();
        this.mIsQuickCalculateVideo = analyticSceneParam.isQuickCalculateVideo();
    }

    @Override // com.miui.gallery.assistant.manager.AlgorithmRequest
    public AnalyticSceneResult process(PathParam pathParam) {
        videoAnalytic.AlbumTag.AlbumTagNode[] albumTagNodeArr = null;
        if (pathParam == null || this.mRequestParams == 0 || TextUtils.isEmpty(pathParam.path)) {
            return new AnalyticSceneResult(3, null);
        }
        if (CloudControlStrategyHelper.getMediaFeatureCalculationDisable()) {
            DefaultLogger.d(this.TAG, "device disable");
            return new AnalyticSceneResult(7);
        } else if (!LibraryManager.getInstance().loadLibrary(3414L)) {
            DefaultLogger.d(this.TAG, "Load library %s failed", "ALGORITHM_ANALYTIC_FACE_AND_SCENE");
            return new AnalyticSceneResult(2);
        } else {
            try {
                try {
                    AnalyticFaceAndSceneAlgorithm analyticFaceAndSceneAlgorithm = (AnalyticFaceAndSceneAlgorithm) AlgorithmFactroy.getAlgorithmByFlag(32);
                    String str = pathParam.path;
                    this.mPath = str;
                    if (pathParam.isVideo) {
                        videoAnalytic.VideoTag.TagNode[] analyticVideo = analyticFaceAndSceneAlgorithm.analyticVideo(str, false, true, this.mIsQuickCalculateVideo);
                        DefaultLogger.d(this.TAG, "analyticVideo %d", Long.valueOf(this.mMediaId));
                        return new AnalyticSceneResult(AnalyticUtils.generateMediaSceneList(analyticVideo, this.mMediaId, this.mMediaType, this.mPath, this.mFileSize, this.mIsQuickCalculateVideo));
                    }
                    videoAnalytic.FaceAndTagNode analyticImage = analyticFaceAndSceneAlgorithm.analyticImage(str, false, true);
                    if (analyticImage != null) {
                        albumTagNodeArr = analyticImage.tagNode;
                    }
                    DefaultLogger.d(this.TAG, "classifyImage %d", Long.valueOf(this.mMediaId));
                    return new AnalyticSceneResult(AnalyticUtils.generateMediaSceneList(albumTagNodeArr, this.mMediaId, this.mMediaType, this.mPath, this.mFileSize, false));
                } catch (Exception e) {
                    e.printStackTrace();
                    AlgorithmFactroy.releaseAlgorithmByFlag(32);
                    return new AnalyticSceneResult(7);
                }
            } finally {
                AlgorithmFactroy.releaseAlgorithmByFlag(32);
            }
        }
    }

    @Override // com.miui.gallery.assistant.manager.AlgorithmRequest
    public void onSaveResult(AnalyticSceneResult analyticSceneResult) {
        if (this.mMediaId < 0 || analyticSceneResult == null) {
            return;
        }
        saveAnalyticResultToDb(analyticSceneResult);
    }

    public final void saveAnalyticResultToDb(AnalyticSceneResult analyticSceneResult) {
        String format;
        if (analyticSceneResult == null || !BaseMiscUtil.isValid(analyticSceneResult.getResult())) {
            return;
        }
        List<MediaScene> result = analyticSceneResult.getResult();
        GalleryEntityManager galleryEntityManager = GalleryEntityManager.getInstance();
        long j = this.mMediaId;
        if (j >= 0) {
            if (this.mIsQuickCalculateVideo) {
                format = String.format(Locale.US, "%s=%d AND %s = %d AND %s != %d", "mediaId", Long.valueOf(j), "fileSize", Long.valueOf(this.mFileSize), "isQuickResult", 0);
            } else {
                format = String.format(Locale.US, "%s=%d AND %s = %d", "mediaId", Long.valueOf(j), "fileSize", Long.valueOf(this.mFileSize));
            }
            galleryEntityManager.delete(MediaScene.class, format, null);
        }
        galleryEntityManager.insert(result);
        DefaultLogger.d(this.TAG, "insert sceneTag, mediaId is %d", Long.valueOf(this.mMediaId));
    }
}
