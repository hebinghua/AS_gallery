package com.miui.gallery.assistant.manager.request;

import android.text.TextUtils;
import com.miui.gallery.assistant.algorithm.AlgorithmFactroy;
import com.miui.gallery.assistant.algorithm.AnalyticFaceAndSceneAlgorithm;
import com.miui.gallery.assistant.library.LibraryManager;
import com.miui.gallery.assistant.manager.AlgorithmRequest;
import com.miui.gallery.assistant.manager.request.param.AnalyticFaceAndSceneParam;
import com.miui.gallery.assistant.manager.result.AnalyticFaceAndSceneResult;
import com.miui.gallery.assistant.model.FaceClusterInfo;
import com.miui.gallery.assistant.model.FaceInfo;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.assistant.model.MediaScene;
import com.miui.gallery.assistant.model.PeopleEvent;
import com.miui.gallery.assistant.utils.AnalyticUtils;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.player.videoAnalytic;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class AnalyticFaceAndSceneRequest extends AlgorithmRequest<List<MediaFeatureItem>, AnalyticFaceAndSceneParam, AnalyticFaceAndSceneResult> {
    public boolean mIsSceneCalculate;

    public AnalyticFaceAndSceneRequest(AlgorithmRequest.Priority priority, AnalyticFaceAndSceneParam analyticFaceAndSceneParam) {
        super(priority, analyticFaceAndSceneParam);
        this.mIsSceneCalculate = analyticFaceAndSceneParam.isSceneTagCalculate();
    }

    @Override // com.miui.gallery.assistant.manager.AlgorithmRequest
    public AnalyticFaceAndSceneResult process(List<MediaFeatureItem> list) {
        videoAnalytic.FaceCluster.ClusterOutput runAllCluster;
        AnalyticFaceAndSceneResult generateResult;
        ArrayList arrayList = null;
        if (!BaseMiscUtil.isValid(list)) {
            return null;
        }
        if (CloudControlStrategyHelper.getMediaFeatureCalculationDisable()) {
            DefaultLogger.d(this.TAG, "device disable");
            return new AnalyticFaceAndSceneResult(7);
        } else if (LibraryManager.getInstance().loadLibrary(3414L)) {
            synchronized (AnalyticFaceAndSceneAlgorithm.class) {
                try {
                    try {
                        AnalyticFaceAndSceneAlgorithm analyticFaceAndSceneAlgorithm = (AnalyticFaceAndSceneAlgorithm) AlgorithmFactroy.getAlgorithmByFlag(32);
                        ArrayList arrayList2 = new ArrayList();
                        int i = 0;
                        for (MediaFeatureItem mediaFeatureItem : list) {
                            boolean z = this.mIsSceneCalculate && !mediaFeatureItem.isSceneCalculated();
                            if (mediaFeatureItem.isVideo()) {
                                videoAnalytic.VideoTag.TagNode[] analyticVideo = analyticFaceAndSceneAlgorithm.analyticVideo(mediaFeatureItem.getGuaranteePath(), true, z, false);
                                if (z) {
                                    arrayList2.add(Integer.valueOf(i));
                                    mediaFeatureItem.setMediaSceneResult(AnalyticUtils.generateMediaSceneList(analyticVideo, mediaFeatureItem.getMediaId(), 1, mediaFeatureItem.getGuaranteePath(), mediaFeatureItem.getFileSize(), false));
                                }
                            } else {
                                videoAnalytic.FaceAndTagNode analyticImage = analyticFaceAndSceneAlgorithm.analyticImage(mediaFeatureItem.getGuaranteePath(), true, z);
                                if (z) {
                                    videoAnalytic.AlbumTag.AlbumTagNode[] albumTagNodeArr = analyticImage == null ? null : analyticImage.tagNode;
                                    arrayList2.add(Integer.valueOf(i));
                                    mediaFeatureItem.setMediaSceneResult(AnalyticUtils.generateMediaSceneList(albumTagNodeArr, mediaFeatureItem.getMediaId(), 0, mediaFeatureItem.getGuaranteePath(), mediaFeatureItem.getFileSize(), false));
                                }
                            }
                            i++;
                        }
                        List query = GalleryEntityManager.getInstance().query(FaceClusterInfo.class, String.format(Locale.US, "%s=%d", "version", 0), null);
                        if (query != null) {
                            arrayList = new ArrayList(query);
                        }
                        if (BaseMiscUtil.isValid(arrayList)) {
                            int size = arrayList.size();
                            videoAnalytic.FaceCluster.FaceClusterNode[] faceClusterNodeArr = new videoAnalytic.FaceCluster.FaceClusterNode[size];
                            for (int i2 = 0; i2 < size; i2++) {
                                faceClusterNodeArr[i2] = analyticFaceAndSceneAlgorithm.getFaceClusterNode(arrayList.get(i2).getClusterCenter());
                            }
                            runAllCluster = analyticFaceAndSceneAlgorithm.runIncrementCluster(faceClusterNodeArr);
                        } else {
                            runAllCluster = analyticFaceAndSceneAlgorithm.runAllCluster();
                        }
                        generateResult = generateResult(runAllCluster, arrayList2, list, arrayList);
                        AlgorithmFactroy.releaseAlgorithmByFlag(32);
                    } catch (Exception e) {
                        e.printStackTrace();
                        AlgorithmFactroy.releaseAlgorithmByFlag(32);
                        return new AnalyticFaceAndSceneResult(7);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            return generateResult;
        } else {
            DefaultLogger.d(this.TAG, "Load library %s failed", "ALGORITHM_ANALYTIC_FACE_AND_SCENE");
            return new AnalyticFaceAndSceneResult(2);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r10v2, types: [com.miui.gallery.assistant.model.MediaFeatureItem] */
    /* JADX WARN: Type inference failed for: r14v1 */
    /* JADX WARN: Type inference failed for: r14v2 */
    /* JADX WARN: Type inference failed for: r14v3, types: [int] */
    /* JADX WARN: Type inference failed for: r23v10 */
    /* JADX WARN: Type inference failed for: r23v15 */
    /* JADX WARN: Type inference failed for: r23v9 */
    public final AnalyticFaceAndSceneResult generateResult(videoAnalytic.FaceCluster.ClusterOutput clusterOutput, List<Integer> list, List<MediaFeatureItem> list2, List<FaceClusterInfo> list3) {
        Iterator<MediaFeatureItem> it;
        videoAnalytic.FaceCluster.FaceNode[][] faceNodeArr;
        videoAnalytic.FaceCluster.EventNode[][] eventNodeArr;
        long j;
        videoAnalytic.FaceCluster.EventNode[] eventNodeArr2;
        boolean z;
        int i;
        ?? r14;
        int[] iArr;
        videoAnalytic.FaceCluster.FaceNode[][] faceNodeArr2;
        videoAnalytic.FaceCluster.EventNode[][] eventNodeArr3;
        long j2;
        videoAnalytic.FaceCluster.FaceNode[] faceNodeArr3;
        videoAnalytic.FaceCluster.EventNode[] eventNodeArr4;
        boolean z2;
        float[] fArr;
        FaceInfo faceInfo;
        if (clusterOutput == null) {
            return new AnalyticFaceAndSceneResult(1);
        }
        videoAnalytic.FaceCluster.FaceNode[][] faceNodeArr4 = clusterOutput.out2;
        videoAnalytic.FaceCluster.EventNode[][] eventNodeArr5 = clusterOutput.out_event;
        videoAnalytic.FaceCluster.FaceClusterNode[] faceClusterNodeArr = clusterOutput.out1;
        long nanoTime = System.nanoTime();
        List<FaceClusterInfo> arrayList = list3 == null ? new ArrayList<>() : list3;
        if (faceClusterNodeArr != null) {
            for (int i2 = 0; i2 < faceClusterNodeArr.length; i2++) {
                if (i2 < arrayList.size()) {
                    arrayList.get(i2).setClusterCenter(faceClusterNodeArr[i2].subcluster_list);
                } else {
                    arrayList.add(new FaceClusterInfo(generateId(nanoTime), faceClusterNodeArr[i2].subcluster_list));
                }
            }
        }
        if (faceNodeArr4 != null) {
            Iterator<MediaFeatureItem> it2 = list2.iterator();
            int i3 = 0;
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                MediaFeatureItem next = it2.next();
                if (i3 >= faceNodeArr4.length) {
                    DefaultLogger.d(this.TAG, "faceNodesOutput do not match");
                    break;
                }
                String guaranteePath = next.getGuaranteePath();
                boolean isVideo = next.isVideo();
                videoAnalytic.FaceCluster.FaceNode[] faceNodeArr5 = faceNodeArr4[i3];
                videoAnalytic.FaceCluster.EventNode[] eventNodeArr6 = eventNodeArr5[i3];
                if (faceNodeArr5 != null && faceNodeArr5.length > 0) {
                    ArrayList arrayList2 = null;
                    int i4 = 0;
                    while (i4 < faceNodeArr5.length) {
                        videoAnalytic.FaceCluster.FaceNode faceNode = faceNodeArr5[i4];
                        Iterator<MediaFeatureItem> it3 = it2;
                        if (faceNode == null || !TextUtils.equals(faceNode.file_id, guaranteePath) || (fArr = faceNode.frame_time) == null || fArr.length == 0) {
                            faceNodeArr2 = faceNodeArr4;
                            eventNodeArr3 = eventNodeArr5;
                            j2 = nanoTime;
                            faceNodeArr3 = faceNodeArr5;
                            eventNodeArr4 = eventNodeArr6;
                            z2 = isVideo;
                            DefaultLogger.d(this.TAG, "face result do not match");
                        } else {
                            if (arrayList2 == null) {
                                arrayList2 = new ArrayList(faceNodeArr5.length);
                            }
                            ArrayList arrayList3 = arrayList2;
                            long mediaId = next.getMediaId();
                            long generateId = generateId(nanoTime);
                            faceNodeArr2 = faceNodeArr4;
                            faceNodeArr3 = faceNodeArr5;
                            eventNodeArr4 = eventNodeArr6;
                            z2 = isVideo;
                            int i5 = z2 ? 1 : 0;
                            int i6 = z2 ? 1 : 0;
                            FaceInfo faceInfo2 = new FaceInfo(mediaId, generateId, i5);
                            int i7 = next.isVideo() ? faceNode.key_face_idx : 0;
                            if (next.isVideo()) {
                                faceInfo = faceInfo2;
                                faceInfo.setStartTime(faceNode.frame_time[0]);
                                float[] fArr2 = faceNode.frame_time;
                                faceInfo.setEndTime(fArr2[fArr2.length - 1]);
                                faceInfo.setKeyFaceFrameIndex(faceNode.frame_index[i7]);
                            } else {
                                faceInfo = faceInfo2;
                            }
                            faceInfo.setKeyPoint(faceNode.key_point[i7]);
                            faceInfo.setAangle(faceNode.angle[i7]);
                            faceInfo.setConfidence(faceNode.confidence[i7]);
                            faceInfo.setAge(faceNode.age);
                            faceInfo.setSmile(faceNode.smile[i7]);
                            eventNodeArr3 = eventNodeArr5;
                            j2 = nanoTime;
                            faceInfo.setFacePos(faceNode.x1[i7], faceNode.y1[i7], faceNode.x2[i7], faceNode.y2[i7]);
                            int i8 = faceNode.cluster_id;
                            if (i8 >= 0 && i8 < arrayList.size()) {
                                faceInfo.setGroupId(arrayList.get(faceNode.cluster_id).getClusterId());
                            }
                            arrayList3.add(faceInfo);
                            arrayList2 = arrayList3;
                        }
                        i4++;
                        it2 = it3;
                        eventNodeArr6 = eventNodeArr4;
                        eventNodeArr5 = eventNodeArr3;
                        faceNodeArr5 = faceNodeArr3;
                        faceNodeArr4 = faceNodeArr2;
                        boolean z3 = z2 ? 1 : 0;
                        ?? r23 = z2 ? 1 : 0;
                        isVideo = z3;
                        nanoTime = j2;
                    }
                    it = it2;
                    faceNodeArr = faceNodeArr4;
                    eventNodeArr = eventNodeArr5;
                    j = nanoTime;
                    eventNodeArr2 = eventNodeArr6;
                    z = isVideo;
                    i = 0;
                    next.setFaceResult(arrayList2);
                } else {
                    it = it2;
                    faceNodeArr = faceNodeArr4;
                    eventNodeArr = eventNodeArr5;
                    j = nanoTime;
                    eventNodeArr2 = eventNodeArr6;
                    z = isVideo;
                    i = 0;
                    ArrayList arrayList4 = new ArrayList(1);
                    long mediaId2 = next.getMediaId();
                    int i9 = z ? 1 : 0;
                    int i10 = z ? 1 : 0;
                    FaceInfo faceInfo3 = new FaceInfo(mediaId2, -1L, i9);
                    faceInfo3.setGroupId(-1L);
                    arrayList4.add(faceInfo3);
                    next.setFaceResult(arrayList4);
                }
                if (eventNodeArr2 != null && eventNodeArr2.length > 0) {
                    ArrayList arrayList5 = new ArrayList(eventNodeArr2.length);
                    int i11 = i;
                    while (i11 < eventNodeArr2.length) {
                        videoAnalytic.FaceCluster.EventNode eventNode = eventNodeArr2[i11];
                        if (eventNode == null || !TextUtils.equals(eventNode.file_id, guaranteePath)) {
                            ?? r232 = z ? 1 : 0;
                            ?? r233 = z ? 1 : 0;
                            r14 = r232;
                            DefaultLogger.d(this.TAG, "event result do not match");
                        } else {
                            r14 = z;
                            PeopleEvent peopleEvent = new PeopleEvent(next.getMediaId(), r14, eventNode.event_type);
                            peopleEvent.setStartTime(eventNode.start_time);
                            peopleEvent.setEndTime(eventNode.end_time);
                            if (eventNode.cluster_id_list != null) {
                                ArrayList arrayList6 = new ArrayList(eventNode.cluster_id_list.length);
                                int i12 = i;
                                while (true) {
                                    iArr = eventNode.cluster_id_list;
                                    if (i12 >= iArr.length) {
                                        break;
                                    }
                                    arrayList6.add(Long.valueOf(arrayList.get(iArr[i12]).getClusterId()));
                                    i12++;
                                }
                                peopleEvent.setPeopleCount(iArr.length);
                                peopleEvent.setPeopleList(TextUtils.join(",", arrayList6));
                            }
                            arrayList5.add(peopleEvent);
                        }
                        i11++;
                        z = r14;
                    }
                    next.setPeopleEventResult(arrayList5);
                }
                i3++;
                it2 = it;
                eventNodeArr5 = eventNodeArr;
                faceNodeArr4 = faceNodeArr;
                nanoTime = j;
            }
        } else {
            DefaultLogger.d(this.TAG, "faceNodesOutput empty");
        }
        return new AnalyticFaceAndSceneResult(list2, list, arrayList);
    }

    public final long generateId(long j) {
        return (System.currentTimeMillis() * 1000000) + ((System.nanoTime() - j) % 1000000);
    }

    @Override // com.miui.gallery.assistant.manager.AlgorithmRequest
    public void onSaveResult(AnalyticFaceAndSceneResult analyticFaceAndSceneResult) {
        if (analyticFaceAndSceneResult == null || !BaseMiscUtil.isValid(analyticFaceAndSceneResult.getFaceResult()) || analyticFaceAndSceneResult.getResultCode() != 0) {
            return;
        }
        List<Integer> sceneUpdateItem = analyticFaceAndSceneResult.getSceneUpdateItem();
        List<FaceClusterInfo> clusterInfo = analyticFaceAndSceneResult.getClusterInfo();
        List<MediaFeatureItem> faceResult = analyticFaceAndSceneResult.getFaceResult();
        GalleryEntityManager galleryEntityManager = GalleryEntityManager.getInstance();
        if (BaseMiscUtil.isValid(sceneUpdateItem)) {
            HashSet hashSet = new HashSet();
            ArrayList arrayList = new ArrayList(sceneUpdateItem.size());
            for (Integer num : sceneUpdateItem) {
                MediaFeatureItem mediaFeatureItem = faceResult.get(num.intValue());
                hashSet.add(Long.valueOf(mediaFeatureItem.getMediaId()));
                arrayList.addAll(mediaFeatureItem.getMediaSceneResult());
            }
            if (BaseMiscUtil.isValid(hashSet)) {
                galleryEntityManager.delete(MediaScene.class, String.format(Locale.US, "%s IN (%s)", "mediaId", TextUtils.join(",", hashSet)), null);
            }
            galleryEntityManager.insert(arrayList);
        }
        if (BaseMiscUtil.isValid(clusterInfo)) {
            galleryEntityManager.delete(FaceClusterInfo.class, String.format(Locale.US, "%s = %d", "version", 0), null);
            galleryEntityManager.insert(clusterInfo);
        }
        HashSet hashSet2 = new HashSet();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        for (MediaFeatureItem mediaFeatureItem2 : faceResult) {
            hashSet2.add(Long.valueOf(mediaFeatureItem2.getMediaId()));
            if (BaseMiscUtil.isValid(mediaFeatureItem2.getFaceResult())) {
                arrayList3.addAll(mediaFeatureItem2.getFaceResult());
            }
            if (BaseMiscUtil.isValid(mediaFeatureItem2.getPeopleEventResult())) {
                arrayList2.addAll(mediaFeatureItem2.getPeopleEventResult());
            }
        }
        if (BaseMiscUtil.isValid(hashSet2)) {
            Locale locale = Locale.US;
            galleryEntityManager.delete(FaceInfo.class, String.format(locale, "%s IN (%s)", "mediaId", TextUtils.join(",", hashSet2)), null);
            galleryEntityManager.delete(PeopleEvent.class, String.format(locale, "%s IN (%s)", "mediaId", TextUtils.join(",", hashSet2)), null);
        }
        galleryEntityManager.insert(arrayList3);
        galleryEntityManager.insert(arrayList2);
    }
}
