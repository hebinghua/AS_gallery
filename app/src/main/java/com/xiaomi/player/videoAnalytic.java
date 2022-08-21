package com.xiaomi.player;

import android.graphics.Bitmap;
import android.util.Log;
import com.xiaomi.milab.videosdk.message.MsgType;
import com.xiaomi.utils.BitmapUtils;
import com.xiaomi.utils.ImageUtils;
import com.xiaomi.video.FrameInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes3.dex */
public class videoAnalytic {
    private static final int CLASSIFY_IMAGE_SIZE = 720;
    private static String TAG = "videoAnalytic";
    public long videoAnalyticInst = 0;
    private String url = "";
    private boolean isDetect = false;
    private boolean mIsFaceCluster = false;
    private boolean mIsTag = false;
    private boolean isRealT = false;
    public VideoTag videoTag = null;
    public AlbumTag imageTag = null;
    public FaceCluster faceCluster = null;
    private ArrayList<String> mVideoList = new ArrayList<>();

    /* loaded from: classes3.dex */
    public enum FrameFlag {
        START_FRAME,
        NORMAL_FRAME,
        END_FRAME
    }

    private native int cacheFrameJni(FrameInfo frameInfo);

    private native long constructAnalyticJni(String str, int i, long j);

    private native int convertFrameJni(int i, boolean z, boolean z2, boolean z3);

    private native void destructAnalyticJni();

    private native String getMediaNameJni(String str);

    private native String getVersionJni();

    private native FaceAndTagNode handleBitmapJni(String str, Bitmap bitmap, int i, boolean z, boolean z2);

    private native FaceAndTagNode handleBitmapTagJni(String str, Bitmap bitmap, boolean z);

    public void construct() {
        Log.d(TAG, "construct");
        if (this.videoAnalyticInst == 0) {
            this.mVideoList.clear();
            this.videoAnalyticInst = constructAnalyticJni("", 0, 0L);
            this.faceCluster = new FaceCluster();
            this.videoTag = new VideoTag();
            this.imageTag = new AlbumTag();
        }
    }

    public void destruct() {
        if (this.videoAnalyticInst == 0) {
            return;
        }
        Log.d(TAG, "destruct");
        destructAnalyticJni();
        this.videoAnalyticInst = 0L;
        this.mVideoList.clear();
    }

    public String getVersion() {
        return getVersionJni();
    }

    public void enableDetectShake() {
        this.isDetect = true;
    }

    public void disableDetectShake() {
        this.isDetect = false;
    }

    public int convertFrame(FrameInfo frameInfo, boolean z, boolean z2) {
        return convertFrameJni(frameInfo.index, z, z2, this.isRealT);
    }

    public int cacheFrame(FrameInfo frameInfo) {
        return cacheFrameJni(frameInfo);
    }

    public void init(boolean z, boolean z2, FaceCluster.FaceParam faceParam, AlbumTag.AlbumInitConfig albumInitConfig, VideoTag.InitConfig initConfig) {
        if (z) {
            this.mIsFaceCluster = z;
            this.faceCluster.initCluster(faceParam);
        }
        if (z2) {
            this.mIsTag = z2;
            String cpuName = getCpuName();
            initConfig.device_info = cpuName;
            String str = TAG;
            Log.d(str, "cpu name is " + cpuName);
            albumInitConfig.device_info = cpuName;
            this.videoTag.init(initConfig);
            this.imageTag.init(albumInitConfig);
        }
    }

    public String getFaceClusterVersion() {
        FaceCluster faceCluster;
        return (!this.mIsFaceCluster || (faceCluster = this.faceCluster) == null) ? "" : faceCluster.getVersion();
    }

    public String getVideoTagVersion() {
        VideoTag videoTag;
        return (!this.mIsTag || (videoTag = this.videoTag) == null) ? "" : videoTag.getVersion();
    }

    public String getAlbumTagVersion() {
        AlbumTag albumTag;
        return (!this.mIsTag || (albumTag = this.imageTag) == null) ? "" : albumTag.getVersion();
    }

    public FaceAndTagNode classifyImage(String str, boolean z, boolean z2, boolean z3) {
        try {
            long currentTimeMillis = System.currentTimeMillis();
            int exifRotate = BitmapUtils.getExifRotate(str);
            Bitmap loadBmpFromFile = BitmapUtils.loadBmpFromFile(new File(str), CLASSIFY_IMAGE_SIZE, CLASSIFY_IMAGE_SIZE);
            if (loadBmpFromFile != null) {
                FaceAndTagNode handleBitmapJni = handleBitmapJni(str, loadBmpFromFile, exifRotate, z2, z);
                String str2 = TAG;
                Log.d(str2, "classifyImage time:" + (System.currentTimeMillis() - currentTimeMillis) + ", isTag:" + z2 + ", isFaceCluster:" + z);
                String str3 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("classifyImage FaceNode size is:");
                sb.append(handleBitmapJni.faceNode.length);
                sb.append(", AlbumTagNode size is:");
                sb.append(handleBitmapJni.tagNode.length);
                Log.d(str3, sb.toString());
                return handleBitmapJni;
            }
            String str4 = TAG;
            Log.e(str4, "cannot load image:" + str);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public FaceAndTagNode classifyImageBmp(String str, Bitmap bitmap, boolean z, boolean z2, boolean z3) {
        long currentTimeMillis = System.currentTimeMillis();
        if (bitmap != null) {
            FaceAndTagNode handleBitmapTagJni = handleBitmapTagJni(str, bitmap, z2);
            String str2 = TAG;
            Log.d(str2, "classifyImageBmp time:" + (System.currentTimeMillis() - currentTimeMillis) + ", isTag:" + z2 + ", isFaceCluster:" + z);
            String str3 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("classifyImageBmp FaceNode size is:");
            sb.append(handleBitmapTagJni.faceNode.length);
            sb.append(", AlbumTagNode size is:");
            sb.append(handleBitmapTagJni.tagNode.length);
            Log.d(str3, sb.toString());
            for (int i = 0; i < handleBitmapTagJni.tagNode.length; i++) {
                String str4 = TAG;
                Log.d(str4, "classifyImageBmp AlbumTagNode[" + i + "] " + handleBitmapTagJni.tagNode[i].tag + ",  " + handleBitmapTagJni.tagNode[i].confidence);
            }
            return handleBitmapTagJni;
        }
        Log.e(TAG, "classifyImageBmp bitmap == null");
        return null;
    }

    public FaceCluster.FaceNode[] clusterImage(String str) {
        FaceCluster faceCluster;
        if (!this.mIsFaceCluster || (faceCluster = this.faceCluster) == null) {
            return null;
        }
        return faceCluster.getImageFaceFeature(str);
    }

    public VideoTag.TagNode[] analyticVideo(String str, boolean z, boolean z2, boolean z3, long j) {
        Log.d(TAG, "VideoAnalyticManager start");
        this.isRealT = z3;
        this.mVideoList.add(str);
        return new VideoAnalyticManager(this, str, z, z2, z3, j).analytic();
    }

    public FaceCluster.ClusterOutput runAllCluster() {
        FaceCluster faceCluster;
        if (!this.mIsFaceCluster || (faceCluster = this.faceCluster) == null) {
            return null;
        }
        return faceCluster.runAllCluster(this.mVideoList);
    }

    public FaceCluster.ClusterOutput runIncrementCluster(FaceCluster.FaceClusterNode[] faceClusterNodeArr) {
        FaceCluster faceCluster;
        if (!this.mIsFaceCluster || (faceCluster = this.faceCluster) == null) {
            return null;
        }
        return faceCluster.runIncrementCluster(faceClusterNodeArr);
    }

    public FaceCluster.FaceNode[] runFastIncrementCluster(FaceCluster.FaceClusterNode[] faceClusterNodeArr) {
        FaceCluster faceCluster;
        if (!this.mIsFaceCluster || (faceCluster = this.faceCluster) == null) {
            return null;
        }
        return faceCluster.runFastIncrementCluster(faceClusterNodeArr);
    }

    public String getMediaName(String str) {
        return getMediaNameJni(str);
    }

    public void destroy() {
        if (this.mIsFaceCluster) {
            this.faceCluster.clusterDestroy();
            this.mIsFaceCluster = false;
        }
        if (this.mIsTag) {
            this.imageTag.destroy();
            this.videoTag.destroy();
            this.mIsTag = false;
        }
    }

    private String getCpuName() {
        String readLine;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/cpuinfo"));
            do {
                readLine = bufferedReader.readLine();
                if (readLine == null) {
                    bufferedReader.close();
                    return null;
                }
            } while (!readLine.contains("Hardware"));
            return readLine.split(":")[1];
        } catch (IOException unused) {
            return null;
        }
    }

    /* loaded from: classes3.dex */
    public class FaceCluster {
        private long videoAnalyticInst = 0;
        private boolean isDetect = false;
        private long time = 0;
        private int num = 0;

        private native boolean DrawRectTestJni(String str, float f, float f2, float f3, float f4);

        private native void clusterDestroyJni();

        private native FaceNode[] getImageBitmapFaceFeatureJni(String str, byte[] bArr, int i, int i2, int i3, int i4);

        private native String getVersionJni();

        private native int getVideoFaceFeatureJni(String str, int i, int i2);

        private native int initClusterJni(FaceParam faceParam);

        private native ClusterOutput runAllClusterJni(ArrayList<String> arrayList);

        private native FaceNode[] runFastIncrementClusterJni(FaceClusterNode[] faceClusterNodeArr);

        private native ClusterOutput runIncrementClusterJni(FaceClusterNode[] faceClusterNodeArr);

        public FaceCluster() {
        }

        public String getVersion() {
            return getVersionJni();
        }

        /* loaded from: classes3.dex */
        public class FaceNode implements Cloneable {
            public int age;
            public float[][] angle;
            public int bVideoImage;
            public int cluster_id;
            public float[] confidence;
            public String file_id;
            public int[] frame_index;
            public float[] frame_time;
            public int key_face_idx;
            public float[][] key_point;
            public int[] smile;
            public float[] x1;
            public float[] x2;
            public float[] y1;
            public float[] y2;

            public FaceNode(int i, int i2, String str, float[] fArr, float[] fArr2, float[] fArr3, float[] fArr4, float[] fArr5, int[] iArr, float[][] fArr6, float[][] fArr7, float[] fArr8, int i3, int[] iArr2, int i4) {
                this.bVideoImage = i;
                this.cluster_id = i2;
                this.file_id = str;
                this.x1 = fArr;
                this.y1 = fArr2;
                this.x2 = fArr3;
                this.y2 = fArr4;
                this.frame_time = fArr5;
                this.frame_index = iArr;
                this.key_point = fArr6;
                this.angle = fArr7;
                this.confidence = fArr8;
                this.age = i3;
                this.smile = iArr2;
                this.key_face_idx = i4;
            }

            public Object clone() throws CloneNotSupportedException {
                return super.clone();
            }
        }

        /* loaded from: classes3.dex */
        public class FaceSubClusterNode implements Cloneable {
            public float[] cluster_center;
            public int cluster_num;

            public FaceSubClusterNode(int i, float[] fArr) {
                this.cluster_num = i;
                this.cluster_center = fArr;
            }

            public Object clone() throws CloneNotSupportedException {
                return super.clone();
            }
        }

        /* loaded from: classes3.dex */
        public class FaceClusterNode implements Cloneable {
            public FaceSubClusterNode[] subcluster_list;

            public FaceClusterNode(FaceSubClusterNode[] faceSubClusterNodeArr) throws CloneNotSupportedException {
                this.subcluster_list = new FaceSubClusterNode[faceSubClusterNodeArr.length];
                for (int i = 0; i < faceSubClusterNodeArr.length; i++) {
                    this.subcluster_list[i] = (FaceSubClusterNode) faceSubClusterNodeArr[i].clone();
                }
            }

            public Object clone() throws CloneNotSupportedException {
                return super.clone();
            }
        }

        /* loaded from: classes3.dex */
        public class EventNode implements Cloneable {
            public int[] cluster_id_list;
            public int end_frame_index;
            public float end_time;
            public int event_type;
            public int face_type;
            public String file_id;
            public int key_frame_index;
            public float key_frame_time;
            public int start_frame_index;
            public float start_time;

            public EventNode(int i, int i2, String str, int[] iArr, float f, float f2, float f3, int i3, int i4, int i5) {
                this.event_type = i;
                this.face_type = i2;
                this.file_id = str;
                this.cluster_id_list = iArr;
                this.start_frame_index = i3;
                this.end_frame_index = i4;
                this.key_frame_index = i5;
                this.start_time = f;
                this.key_frame_time = f3;
                this.end_time = f2;
            }

            public Object clone() throws CloneNotSupportedException {
                return super.clone();
            }
        }

        /* loaded from: classes3.dex */
        public class ClusterOutput {
            public FaceClusterNode[] out1;
            public FaceNode[][] out2;
            public EventNode[][] out_event;

            public ClusterOutput(FaceClusterNode[] faceClusterNodeArr, FaceNode[][] faceNodeArr, EventNode[][] eventNodeArr) throws CloneNotSupportedException {
                if (faceClusterNodeArr != null) {
                    this.out1 = new FaceClusterNode[faceClusterNodeArr.length];
                    for (int i = 0; i < faceClusterNodeArr.length; i++) {
                        if (faceClusterNodeArr[i] != null) {
                            this.out1[i] = (FaceClusterNode) faceClusterNodeArr[i].clone();
                        }
                    }
                }
                if (faceNodeArr != null) {
                    this.out2 = new FaceNode[faceNodeArr.length];
                    for (int i2 = 0; i2 < faceNodeArr.length; i2++) {
                        this.out2[i2] = new FaceNode[faceNodeArr[i2].length];
                        for (int i3 = 0; i3 < faceNodeArr[i2].length; i3++) {
                            if (faceNodeArr[i2][i3] != null) {
                                this.out2[i2][i3] = (FaceNode) faceNodeArr[i2][i3].clone();
                            }
                        }
                    }
                }
                if (eventNodeArr != null) {
                    this.out_event = new EventNode[eventNodeArr.length];
                    for (int i4 = 0; i4 < eventNodeArr.length; i4++) {
                        this.out_event[i4] = new EventNode[eventNodeArr[i4].length];
                        for (int i5 = 0; i5 < eventNodeArr[i4].length; i5++) {
                            if (eventNodeArr[i4][i5] != null) {
                                this.out_event[i4][i5] = (EventNode) eventNodeArr[i4][i5].clone();
                            }
                        }
                    }
                }
            }
        }

        public boolean DrawRectTest(String str, float f, float f2, float f3, float f4) {
            return DrawRectTestJni(str, f, f2, f3, f4);
        }

        /* loaded from: classes3.dex */
        public class FaceParam {
            public int face_detect_image_wd = 512;
            public int face_selection_frame_interval = 10;
            public int min_cluster_face_num = 2;
            public float min_sequence_interval = 1.0f;
            public int child_age_thd = 13;

            public FaceParam() {
            }
        }

        public int initCluster(FaceParam faceParam) {
            this.videoAnalyticInst = videoAnalytic.this.videoAnalyticInst;
            return initClusterJni(faceParam);
        }

        public boolean videoFaceFeature(String str) {
            this.isDetect = videoAnalytic.this.isDetect;
            return false;
        }

        public void updateStartFrame(String str, int i) {
            getVideoFaceFeatureJni(str, i, FrameFlag.START_FRAME.ordinal());
        }

        public void updateNormalFrame(String str, int i) {
            getVideoFaceFeatureJni(str, i, FrameFlag.NORMAL_FRAME.ordinal());
        }

        public void updateEndFrame(String str) {
            getVideoFaceFeatureJni(str, 0, FrameFlag.END_FRAME.ordinal());
        }

        public FaceNode[] getImageFaceFeature(String str) {
            try {
                String str2 = videoAnalytic.TAG;
                Log.e(str2, " getImageFaceFeature path " + str);
                System.currentTimeMillis();
                Bitmap loadBmpFromFile = BitmapUtils.loadBmpFromFile(new File(str), 1080, 1080);
                return getImageBitmapFaceFeatureJni(str, ImageUtils.bitmap2BGR(loadBmpFromFile), loadBmpFromFile != null ? loadBmpFromFile.getWidth() : 0, loadBmpFromFile != null ? loadBmpFromFile.getHeight() : 0, 3, BitmapUtils.getExifRotate(str));
            } catch (IOException unused) {
                Log.e(videoAnalytic.TAG, "load exif rotate failed");
                return null;
            }
        }

        public ClusterOutput runAllCluster(ArrayList<String> arrayList) {
            return runAllClusterJni(arrayList);
        }

        public ClusterOutput runIncrementCluster(FaceClusterNode[] faceClusterNodeArr) {
            return runIncrementClusterJni(faceClusterNodeArr);
        }

        public FaceNode[] runFastIncrementCluster(FaceClusterNode[] faceClusterNodeArr) {
            return runFastIncrementClusterJni(faceClusterNodeArr);
        }

        public void clusterDestroy() {
            clusterDestroyJni();
        }
    }

    /* loaded from: classes3.dex */
    public class FaceAndTagNode {
        public FaceCluster.FaceNode[] faceNode;
        public AlbumTag.AlbumTagNode[] tagNode;

        public FaceAndTagNode(AlbumTag.AlbumTagNode[] albumTagNodeArr, FaceCluster.FaceNode[] faceNodeArr) throws CloneNotSupportedException {
            this.tagNode = null;
            this.faceNode = null;
            if (albumTagNodeArr != null) {
                this.tagNode = new AlbumTag.AlbumTagNode[albumTagNodeArr.length];
                for (int i = 0; i < albumTagNodeArr.length; i++) {
                    if (albumTagNodeArr[i] != null) {
                        this.tagNode[i] = albumTagNodeArr[i];
                    }
                }
            }
            if (faceNodeArr != null) {
                this.faceNode = new FaceCluster.FaceNode[faceNodeArr.length];
                for (int i2 = 0; i2 < faceNodeArr.length; i2++) {
                    if (faceNodeArr[i2] != null) {
                        this.faceNode[i2] = faceNodeArr[i2];
                    }
                }
            }
        }
    }

    /* loaded from: classes3.dex */
    public enum Tag {
        E_BABY(0),
        E_KID(1),
        E_CAT(2),
        E_DOG(3),
        E_PIG(4),
        E_BIRD(5),
        E_WILD_ANIMAL(6),
        E_WEDDING(7),
        E_INSTRUMENTAL_PERFORMANCE(8),
        E_SHOW(9),
        E_GROUP_PHOTO(10),
        E_DINNER_PARTY(11),
        E_ICE_CREAM(12),
        E_DESSERT(13),
        E_DRINK(14),
        E_BARBECUE(15),
        E_HOTPOT(16),
        E_SEAFOOD(17),
        E_SUSHI(18),
        E_WESTERN_FOOD(19),
        E_COOKED_DISH(21),
        E_STAPLE_FOOD(22),
        E_CITY(23),
        E_TEMPLE(24),
        E_PALACE(25),
        E_GARDEN(26),
        E_CHURCH(27),
        E_CASTLE(28),
        E_STREET_VIEW(29),
        E_BRIDGE(30),
        E_BUILDING(31),
        E_NIGHTSCENE(32),
        E_SKIING(33),
        E_SWIMMING(34),
        E_BASKETBALL(35),
        E_FOOTBALL(36),
        E_BADMINTON(37),
        E_DANCE(38),
        E_SKATEBOARDING(39),
        E_FITNESS(40),
        E_SEA(41),
        E_BEACH(42),
        E_WOODS(43),
        E_TREE(44),
        E_FLOWER(45),
        E_LAKE_RIVER(46),
        E_SNOW(47),
        E_SKY(48),
        E_STAR_SKY(49),
        E_SUNRISE_SUNSET(50),
        E_GRASSLAND(51),
        E_DESERT(52),
        E_WATERFALL(53),
        E_MOUNTAIN(54),
        E_GORGE(55),
        E_INVOICE(56),
        E_BUSINESS_CARD(57),
        E_EXPRESS_RECEIPT(58),
        E_HUKOU(59),
        E_PASSPORT(60),
        E_TRAIN_TICKET(61),
        E_SOCIAL_SECURITY_CARD(62),
        E_ID_CARD(63),
        E_BANK_CARD(64),
        E_ENTRAINCE_TICKET(65),
        E_SCREEN_WORDS(66),
        E_SPROT_TABLE_TENNIS(67),
        E_SPORT_SURFING(68),
        E_SPORT_BILLIARDS(69),
        E_SPORT_BOXING(70),
        E_SPORT_VOLLEYBALL(71),
        E_SPROT_DIVING(72),
        E_SPORT_RIDING(73),
        E_SPORT_GOLF(74),
        E_FRUIT(75),
        E_IndianAadhaar(76),
        E_IndianBankcard(77),
        E_IndianDriverlicence(78),
        E_IndianPassport(79),
        E_IndianPermanentAccountNumber(80),
        E_IndianRailwayTicket(81),
        E_IndianVoterID(82),
        E_DOCUMENT(83),
        E_SHOT_TYPE_DAQUANJING(MsgType.XMSCONTEXT),
        E_SHOT_TYPE_RENJINZHONGJING(MsgType.XMSEXPORT),
        E_SHOT_TYPE_RENQUANJING(MsgType.XMSTRANSCODE),
        E_SHOT_TYPE_RENTEXIE(MsgType.XMSAUDIOEXTRACT),
        E_SHOT_TYPE_WUJINZHONGJING(MsgType.XMSTIMELINE),
        E_SHOT_TYPE_WUQUANJING(10006),
        E_SHOT_TYPE_WUTEXIE(10007),
        E_INDOOR(20001),
        E_OUTDOOR(20002),
        E_PERSON_COUNT_NONE(30001),
        E_PERSON_COUNT_SINGLE(30002),
        E_PERSON_COUNT_SEVERAL(30003),
        E_PERSON_COUNT_MANY(30004),
        E_NATURAL(40001),
        E_NONE_NATURAL(40002),
        E_NATURAL_AND_NONE_NATURAL(40003);
        
        private final int value;

        Tag(int i) {
            this.value = i;
        }

        public static Tag valueOf(int i) {
            Tag[] values;
            for (Tag tag : values()) {
                if (tag.getValue() == i) {
                    return tag;
                }
            }
            return null;
        }

        public int getValue() {
            return this.value;
        }
    }

    /* loaded from: classes3.dex */
    public enum FunctionFlag {
        VIDEO_LABELING(1),
        VIDEO_SHOT_TYPE(2),
        VIDEO_INDOOR_OUTDOOR(4),
        VIDEO_PERSON_COUNT(8),
        VIDEO_HEAT_MAP(16),
        VIDEO_HIGH_LIGHT(32);
        
        private final int value;

        FunctionFlag(int i) {
            this.value = i;
        }

        public static FunctionFlag valueOf(int i) {
            FunctionFlag[] values;
            for (FunctionFlag functionFlag : values()) {
                if (functionFlag.getValue() == i) {
                    return functionFlag;
                }
            }
            return null;
        }

        public int getValue() {
            return this.value;
        }
    }

    /* loaded from: classes3.dex */
    public class AlbumTag {
        private static final int ALBUM_TAG_IMAGE_MIN_SIZE = 448;
        private long videoAnalyticInst = 0;

        private native AlbumTagNode[] classifyBitmapJni(String str, Bitmap bitmap, int i);

        private native void destroyJni();

        private native String getVersionJni();

        private native int initJni(AlbumInitConfig albumInitConfig);

        public AlbumTag() {
        }

        /* loaded from: classes3.dex */
        public class AlbumTagNode {
            public float confidence;
            public float[][] face_points;
            public String file_id;
            public float[] heatmap_points;
            public int tag;

            public AlbumTagNode(String str, int i, float f, float[] fArr, float[][] fArr2) {
                this.file_id = str;
                this.tag = i;
                this.confidence = f;
                this.heatmap_points = fArr;
                this.face_points = fArr2;
            }

            public Object clone() throws CloneNotSupportedException {
                return super.clone();
            }
        }

        /* loaded from: classes3.dex */
        public class AlbumInitConfig {
            public String device_info;
            public String dsp_path;
            public String model_path;
            public int result_max_size = 4;
            public HashMap<Integer, Float> confidence_threshold = null;
            public long function_flag = FunctionFlag.VIDEO_LABELING.getValue();

            public AlbumInitConfig() {
            }
        }

        public int init(AlbumInitConfig albumInitConfig) {
            this.videoAnalyticInst = videoAnalytic.this.videoAnalyticInst;
            return initJni(albumInitConfig);
        }

        public AlbumTagNode[] classify(String str, Bitmap bitmap, int i) {
            long currentTimeMillis = System.currentTimeMillis();
            if (bitmap != null) {
                String str2 = videoAnalytic.TAG;
                Log.e(str2, " time profile get bitmap " + (System.currentTimeMillis() - currentTimeMillis));
                return classifyBitmapJni(str, bitmap, i);
            }
            String str3 = videoAnalytic.TAG;
            Log.e(str3, " path " + str + " get bitmap null ");
            return null;
        }

        public AlbumTagNode[] classify(String str) {
            try {
                int exifRotate = BitmapUtils.getExifRotate(str);
                long currentTimeMillis = System.currentTimeMillis();
                Bitmap loadBmpFromFile = BitmapUtils.loadBmpFromFile(new File(str), ALBUM_TAG_IMAGE_MIN_SIZE, ALBUM_TAG_IMAGE_MIN_SIZE);
                if (loadBmpFromFile != null) {
                    String str2 = videoAnalytic.TAG;
                    Log.e(str2, " time profile get bitmap " + (System.currentTimeMillis() - currentTimeMillis));
                    return classifyBitmapJni(str, loadBmpFromFile, exifRotate);
                }
                String str3 = videoAnalytic.TAG;
                Log.e(str3, " path " + str + " get bitmap null ");
                return null;
            } catch (IOException unused) {
                Log.e(videoAnalytic.TAG, "load exif rotate failed");
                return null;
            }
        }

        public String getVersion() {
            return getVersionJni();
        }

        public void destroy() {
            destroyJni();
        }
    }

    /* loaded from: classes3.dex */
    public class VideoTag {
        private long videoAnalyticInst = 0;
        private boolean isDetect = false;

        private native void destroyJni();

        private native TagNode[] getTagListJni(String str);

        private native String getVersionJni();

        private native int initJni(InitConfig initConfig);

        private native int updateFrameJni(String str, int i, int i2, boolean z);

        public VideoTag() {
        }

        /* loaded from: classes3.dex */
        public class TagNode {
            public int best_frame;
            public float best_frame_time;
            public float confidence;
            public int end_frame;
            public double end_time;
            public String file_id;
            public int start_frame;
            public double start_time;
            public int tag;

            public TagNode(String str, int i, float f, int i2, int i3, double d, double d2, int i4, float f2) {
                this.file_id = str;
                this.tag = i;
                this.confidence = f;
                this.start_frame = i2;
                this.end_frame = i3;
                this.start_time = d;
                this.end_time = d2;
                this.best_frame = i4;
                this.best_frame_time = f2;
            }
        }

        /* loaded from: classes3.dex */
        public class InitConfig {
            public String device_info;
            public String dsp_path;
            public boolean isRealT;
            public String model_path;
            public HashMap<Integer, Float> confidence_threshold = null;
            public int video_slice_min_time = 3;
            public long function_flag = ((FunctionFlag.VIDEO_LABELING.getValue() | FunctionFlag.VIDEO_INDOOR_OUTDOOR.getValue()) | FunctionFlag.VIDEO_PERSON_COUNT.getValue()) | FunctionFlag.VIDEO_SHOT_TYPE.getValue();

            public InitConfig() {
            }
        }

        public int init(InitConfig initConfig) {
            this.videoAnalyticInst = videoAnalytic.this.videoAnalyticInst;
            return initJni(initConfig);
        }

        public void updateStartFrame(String str, int i) {
            updateFrameJni(str, i, FrameFlag.START_FRAME.ordinal(), videoAnalytic.this.isRealT);
        }

        public void updateNormalFrame(String str, int i) {
            updateFrameJni(str, i, FrameFlag.NORMAL_FRAME.ordinal(), videoAnalytic.this.isRealT);
        }

        public TagNode[] getTagList(String str) {
            return getTagListJni(str);
        }

        public String getVersion() {
            return getVersionJni();
        }

        public void destroy() {
            destroyJni();
        }
    }
}
