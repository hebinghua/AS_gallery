package com.miui.gallery.assistant.algorithm;

import com.miui.gallery.GalleryApp;
import com.miui.gallery.assistant.library.LibraryUtils;
import com.miui.gallery.assistant.manager.AnalyticFaceAndSceneManager;
import com.miui.gallery.net.library.LibraryStrategyUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.player.videoAnalytic;
import java.util.Objects;

/* loaded from: classes.dex */
public class AnalyticFaceAndSceneAlgorithm extends Algorithm {
    public static boolean sIsInited;
    public static videoAnalytic sVideoAnalytic;
    public boolean mIsMTK;

    @Override // com.miui.gallery.assistant.algorithm.Algorithm
    public boolean isForegroundUsed() {
        return true;
    }

    @Override // com.miui.gallery.assistant.algorithm.Algorithm
    public void onDestroyAlgorithm() {
    }

    public AnalyticFaceAndSceneAlgorithm() {
        super(3414L);
        this.mIsMTK = LibraryStrategyUtils.isMtk();
    }

    @Override // com.miui.gallery.assistant.algorithm.Algorithm
    public boolean onInitAlgorithm() {
        boolean checkInit;
        synchronized (AnalyticFaceAndSceneAlgorithm.class) {
            if (sVideoAnalytic == null) {
                sVideoAnalytic = new videoAnalytic();
            }
            checkInit = checkInit();
        }
        return checkInit;
    }

    public final boolean checkInit() {
        synchronized (AnalyticFaceAndSceneAlgorithm.class) {
            if (sIsInited) {
                return true;
            }
            try {
                long currentTimeMillis = System.currentTimeMillis();
                sVideoAnalytic.construct();
                DefaultLogger.d(this.TAG, "construct, cost %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                long currentTimeMillis2 = System.currentTimeMillis();
                String libraryDirPath = LibraryUtils.getLibraryDirPath(GalleryApp.sGetAndroidContext());
                videoAnalytic.FaceCluster faceCluster = sVideoAnalytic.faceCluster;
                Objects.requireNonNull(faceCluster);
                videoAnalytic.FaceCluster.FaceParam faceParam = new videoAnalytic.FaceCluster.FaceParam();
                faceParam.min_cluster_face_num = 1;
                faceParam.face_detect_image_wd = 512;
                faceParam.face_selection_frame_interval = 6;
                videoAnalytic.AlbumTag albumTag = sVideoAnalytic.imageTag;
                Objects.requireNonNull(albumTag);
                videoAnalytic.AlbumTag.AlbumInitConfig albumInitConfig = new videoAnalytic.AlbumTag.AlbumInitConfig();
                albumInitConfig.model_path = libraryDirPath;
                videoAnalytic.FunctionFlag functionFlag = videoAnalytic.FunctionFlag.VIDEO_LABELING;
                albumInitConfig.function_flag = functionFlag.getValue() | videoAnalytic.FunctionFlag.VIDEO_PERSON_COUNT.getValue() | videoAnalytic.FunctionFlag.VIDEO_HEAT_MAP.getValue();
                videoAnalytic.VideoTag videoTag = sVideoAnalytic.videoTag;
                Objects.requireNonNull(videoTag);
                videoAnalytic.VideoTag.InitConfig initConfig = new videoAnalytic.VideoTag.InitConfig();
                initConfig.video_slice_min_time = 1;
                initConfig.model_path = libraryDirPath;
                DefaultLogger.d(this.TAG, "library path: %s", libraryDirPath);
                initConfig.function_flag = functionFlag.getValue() | videoAnalytic.FunctionFlag.VIDEO_INDOOR_OUTDOOR.getValue() | videoAnalytic.FunctionFlag.VIDEO_SHOT_TYPE.getValue();
                sVideoAnalytic.init(true, true, faceParam, albumInitConfig, initConfig);
                DefaultLogger.d(this.TAG, "initParams cost %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis2));
                sIsInited = true;
                return true;
            } catch (Exception e) {
                reportAlgorithmError(e);
                return false;
            }
        }
    }

    public videoAnalytic.VideoTag.TagNode[] analyticVideo(String str, boolean z, boolean z2, boolean z3) {
        videoAnalytic.VideoTag.TagNode[] tagNodeArr = null;
        if (!AnalyticFaceAndSceneManager.isDeviceSupportVideo()) {
            return null;
        }
        synchronized (AnalyticFaceAndSceneAlgorithm.class) {
            try {
                if (checkInit()) {
                    DefaultLogger.d(this.TAG, "analyticVideo start");
                    long currentTimeMillis = System.currentTimeMillis();
                    tagNodeArr = sVideoAnalytic.analyticVideo(str, z, z2, z3, 5L);
                    DefaultLogger.d(this.TAG, "analyticVideo %s, is quick %s, cost %d", str, Boolean.valueOf(z3), Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                }
            } catch (Exception e) {
                reportAlgorithmError(e);
            }
        }
        return tagNodeArr;
    }

    public videoAnalytic.FaceAndTagNode analyticImage(String str, boolean z, boolean z2) {
        videoAnalytic.FaceAndTagNode faceAndTagNode;
        synchronized (AnalyticFaceAndSceneAlgorithm.class) {
            faceAndTagNode = null;
            try {
                if (checkInit()) {
                    DefaultLogger.d(this.TAG, "analyticImage start");
                    long currentTimeMillis = System.currentTimeMillis();
                    faceAndTagNode = sVideoAnalytic.classifyImage(str, z, z2, this.mIsMTK);
                    DefaultLogger.d(this.TAG, "analyticImage %s, cost %d", str, Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                }
            } catch (Exception e) {
                reportAlgorithmError(e);
            }
        }
        return faceAndTagNode;
    }

    public videoAnalytic.FaceCluster.ClusterOutput runIncrementCluster(videoAnalytic.FaceCluster.FaceClusterNode[] faceClusterNodeArr) {
        videoAnalytic.FaceCluster.ClusterOutput clusterOutput;
        synchronized (AnalyticFaceAndSceneAlgorithm.class) {
            clusterOutput = null;
            try {
                if (checkInit()) {
                    DefaultLogger.d(this.TAG, "runIncrementCluster start");
                    long currentTimeMillis = System.currentTimeMillis();
                    clusterOutput = sVideoAnalytic.runIncrementCluster(faceClusterNodeArr);
                    DefaultLogger.d(this.TAG, "runIncrementCluster, cost %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                }
            } catch (Exception e) {
                reportAlgorithmError(e);
            }
        }
        return clusterOutput;
    }

    public videoAnalytic.FaceCluster.ClusterOutput runAllCluster() {
        videoAnalytic.FaceCluster.ClusterOutput clusterOutput;
        synchronized (AnalyticFaceAndSceneAlgorithm.class) {
            clusterOutput = null;
            try {
                if (checkInit()) {
                    DefaultLogger.d(this.TAG, "runAllCluster start");
                    long currentTimeMillis = System.currentTimeMillis();
                    clusterOutput = sVideoAnalytic.runAllCluster();
                    DefaultLogger.d(this.TAG, "runAllCluster, cost %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                }
            } catch (Exception e) {
                reportAlgorithmError(e);
            }
        }
        return clusterOutput;
    }

    public videoAnalytic.FaceCluster.FaceClusterNode getFaceClusterNode(videoAnalytic.FaceCluster.FaceSubClusterNode[] faceSubClusterNodeArr) {
        videoAnalytic.FaceCluster.FaceClusterNode faceClusterNode;
        synchronized (AnalyticFaceAndSceneAlgorithm.class) {
            faceClusterNode = null;
            try {
                if (checkInit()) {
                    videoAnalytic.FaceCluster faceCluster = sVideoAnalytic.faceCluster;
                    Objects.requireNonNull(faceCluster);
                    faceClusterNode = new videoAnalytic.FaceCluster.FaceClusterNode(faceSubClusterNodeArr);
                }
            } catch (Exception e) {
                reportAlgorithmError(e);
            }
        }
        return faceClusterNode;
    }

    @Override // com.miui.gallery.assistant.algorithm.Algorithm
    public void clearAlgorithm() {
        synchronized (AnalyticFaceAndSceneAlgorithm.class) {
            try {
                if (sIsInited) {
                    long currentTimeMillis = System.currentTimeMillis();
                    sVideoAnalytic.destroy();
                    DefaultLogger.d(this.TAG, "destroy cost %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                    long currentTimeMillis2 = System.currentTimeMillis();
                    sVideoAnalytic.destruct();
                    DefaultLogger.d(this.TAG, "destruct cost %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis2));
                    sIsInited = false;
                }
            } catch (Exception e) {
                reportAlgorithmError(e);
            }
        }
    }
}
