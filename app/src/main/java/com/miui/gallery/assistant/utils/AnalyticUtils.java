package com.miui.gallery.assistant.utils;

import android.database.Cursor;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.assistant.manager.AnalyticFaceAndSceneManager;
import com.miui.gallery.assistant.manager.result.AnalyticSceneResult;
import com.miui.gallery.assistant.model.FaceInfo;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.assistant.model.MediaScene;
import com.miui.gallery.assistant.model.MediaSceneTagManager;
import com.miui.gallery.assistant.utils.AnalyticUtils;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.GsonUtils;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.player.videoAnalytic;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class AnalyticUtils {
    public static final Map<Integer, Float> sTagThresholdMap;

    /* loaded from: classes.dex */
    public interface DataFetcher {

        /* loaded from: classes.dex */
        public interface DataQueryHandler<T> {
            T handle(Cursor cursor);
        }

        <T> T query(String[] strArr, String str, String[] strArr2, String str2, DataQueryHandler<T> dataQueryHandler);
    }

    /* loaded from: classes.dex */
    public enum DataFetcherType {
        DATA_FETCHER_DB,
        DATA_FETCHER_PROVIDER
    }

    public static List<MediaScene> generateMediaSceneList(videoAnalytic.VideoTag.TagNode[] tagNodeArr, long j, int i, String str, long j2, boolean z) {
        if (tagNodeArr == null || tagNodeArr.length == 0) {
            return generateDefaultMediaSceneList(j, i, str, j2, z);
        }
        ArrayList arrayList = new ArrayList(tagNodeArr.length);
        for (videoAnalytic.VideoTag.TagNode tagNode : tagNodeArr) {
            MediaScene mediaScene = new MediaScene(j, i, str, j2);
            mediaScene.setIsQuickResult(z);
            mediaScene.setSceneTag(tagNode.tag);
            mediaScene.setConfidence(tagNode.confidence);
            mediaScene.setStartTime(tagNode.start_time);
            mediaScene.setEndTime(tagNode.end_time);
            arrayList.add(mediaScene);
        }
        return arrayList;
    }

    public static List<MediaScene> generateMediaSceneList(videoAnalytic.AlbumTag.AlbumTagNode[] albumTagNodeArr, long j, int i, String str, long j2, boolean z) {
        if (albumTagNodeArr == null || albumTagNodeArr.length == 0) {
            return generateDefaultMediaSceneList(j, i, str, j2, z);
        }
        ArrayList arrayList = new ArrayList(albumTagNodeArr.length);
        int i2 = 0;
        int i3 = 0;
        while (i3 < albumTagNodeArr.length) {
            videoAnalytic.AlbumTag.AlbumTagNode albumTagNode = albumTagNodeArr[i3];
            MediaScene mediaScene = new MediaScene(j, i, str, j2);
            mediaScene.setIsQuickResult(z);
            mediaScene.setSceneTag(albumTagNode.tag);
            mediaScene.setConfidence(albumTagNode.confidence);
            float[] fArr = albumTagNode.heatmap_points;
            if (fArr != null && fArr.length >= 4) {
                mediaScene.setHeatMapPos(fArr[i2], fArr[1], fArr[2], fArr[3]);
            } else {
                mediaScene.setHeatMapPos(-1.0f, -1.0f, -1.0f, -1.0f);
            }
            JSONArray jSONArray = new JSONArray();
            float[][] fArr2 = albumTagNode.face_points;
            if (fArr2 != null && fArr2.length > 0) {
                int i4 = i2;
                while (true) {
                    float[][] fArr3 = albumTagNode.face_points;
                    if (i4 < fArr3.length) {
                        float[] fArr4 = fArr3[i4];
                        if (fArr4 != null && fArr4.length >= 4) {
                            jSONArray.put(MediaScene.getPointPositionJSONObj(fArr4[i2], fArr4[1], fArr4[2], fArr4[3]));
                        }
                        i4++;
                        i2 = 0;
                    }
                }
            }
            mediaScene.setPointPosition(jSONArray.toString());
            arrayList.add(mediaScene);
            i3++;
            i2 = 0;
        }
        return arrayList;
    }

    public static List<MediaScene> generateDefaultMediaSceneList(long j, int i, String str, long j2, boolean z) {
        ArrayList arrayList = new ArrayList(1);
        MediaScene mediaScene = new MediaScene(j, i, str, j2);
        mediaScene.setSceneTag(-1);
        mediaScene.setIsQuickResult(z);
        mediaScene.setHeatMapPos(-1.0f, -1.0f, -1.0f, -1.0f);
        mediaScene.setPointPosition("");
        arrayList.add(mediaScene);
        return arrayList;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0105  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x012d  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x0134  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x01b8  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x01d0  */
    /* JADX WARN: Removed duplicated region for block: B:158:0x01d6  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x01e0  */
    /* JADX WARN: Removed duplicated region for block: B:164:0x01eb A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:167:0x021d A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:169:0x0220  */
    /* JADX WARN: Removed duplicated region for block: B:171:0x0237  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.miui.gallery.assistant.utils.AnalyticUtils.CropRegionInfo getImageCropRegion(long r29, int r31, int r32, int r33, com.miui.gallery.assistant.utils.AnalyticUtils.RegionType r34, com.miui.gallery.assistant.utils.AnalyticUtils.DataFetcherType r35) {
        /*
            Method dump skipped, instructions count: 586
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.assistant.utils.AnalyticUtils.getImageCropRegion(long, int, int, int, com.miui.gallery.assistant.utils.AnalyticUtils$RegionType, com.miui.gallery.assistant.utils.AnalyticUtils$DataFetcherType):com.miui.gallery.assistant.utils.AnalyticUtils$CropRegionInfo");
    }

    public static /* synthetic */ List lambda$getImageCropRegion$0(Cursor cursor) {
        ArrayList arrayList = new ArrayList();
        if (cursor == null || !cursor.moveToFirst()) {
            return null;
        }
        arrayList.add(new MediaFeatureItem(cursor));
        return arrayList;
    }

    public static /* synthetic */ List lambda$getImageCropRegion$1(Cursor cursor) {
        if (cursor == null || !cursor.moveToFirst()) {
            return null;
        }
        MediaFeatureItem mediaFeatureItem = new MediaFeatureItem(cursor);
        long currentTimeMillis = System.currentTimeMillis();
        AnalyticSceneResult analyticSceneTagSync = AnalyticFaceAndSceneManager.getInstance().analyticSceneTagSync(mediaFeatureItem, false);
        DefaultLogger.d("AnalyticUtils", "---log---analyticSceneTagSync cost %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        if (analyticSceneTagSync != null && analyticSceneTagSync.getResultCode() == 0) {
            return analyticSceneTagSync.getResult();
        }
        return null;
    }

    public static float getScoreThreshold(int i) {
        Map<Integer, Float> map = sTagThresholdMap;
        if (!map.containsKey(Integer.valueOf(i))) {
            return -1.0f;
        }
        return map.get(Integer.valueOf(i)).floatValue();
    }

    public static RectF getFaceRect(List<FaceInfo> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        float f = 0.0f;
        float f2 = 1.0f;
        float f3 = 1.0f;
        float f4 = 0.0f;
        for (FaceInfo faceInfo : list) {
            f2 = Math.min(f2, faceInfo.getLeftTopX());
            f3 = Math.min(f3, faceInfo.getLeftTopY());
            f = Math.max(f, faceInfo.getRightBottomX());
            f4 = Math.max(f4, faceInfo.getRightBottomY());
        }
        return new RectF(f2, f3, f, f4);
    }

    public static RectF getFaceRect(JSONArray jSONArray) {
        if (jSONArray != null && jSONArray.length() != 0) {
            float f = 0.0f;
            float f2 = 1.0f;
            float f3 = 1.0f;
            float f4 = 0.0f;
            for (int i = 0; i < jSONArray.length(); i++) {
                try {
                    JSONObject jSONObject = jSONArray.getJSONObject(i);
                    if (jSONObject != null) {
                        MediaScene.PointPosition pointPosition = (MediaScene.PointPosition) GsonUtils.fromJson(jSONObject, MediaScene.PointPosition.class);
                        f2 = Math.min(f2, pointPosition.mPointLeftTopX);
                        f3 = Math.min(f3, pointPosition.mPointLeftTopY);
                        f = Math.max(f, pointPosition.mPointRightBottomX);
                        f4 = Math.max(f4, pointPosition.mPointRightBottomY);
                    }
                } catch (Exception e) {
                    DefaultLogger.d("AnalyticUtils", "getFaceRect fail", e);
                }
            }
            return new RectF(f2, f3, f, f4);
        }
        return null;
    }

    public static CropRegionInfo getCropRegion(float f, float f2, int i, int i2, RegionType regionType, float f3, RectF rectF) {
        float typeRatio;
        if (i <= 0 || i2 <= 0) {
            return new CropRegionInfo(-1.0f, -1.0f, -1.0f, -1.0f);
        }
        if (f3 == -1.0f) {
            return new CropRegionInfo(-1.0f, -1.0f, -1.0f, -1.0f);
        }
        float f4 = (i * 1.0f) / i2;
        float f5 = 0.5f;
        if (f4 >= regionType.getTypeRatio()) {
            typeRatio = 0.5f;
            f5 = (regionType.getTypeRatio() / f4) * 0.5f;
        } else {
            typeRatio = (f4 / regionType.getTypeRatio()) * 0.5f;
        }
        float f6 = ((double) (f + f5)) > 1.0d ? 1.0f - f5 : f;
        if (f6 - f5 < 0.0f) {
            f6 = f5;
        }
        float f7 = ((double) (f2 + typeRatio)) > 1.0d ? 1.0f - typeRatio : f2;
        if (f7 - typeRatio < 0.0f) {
            f7 = typeRatio;
        }
        float f8 = f6 - f5;
        float f9 = f7 - typeRatio;
        float f10 = f6 + f5;
        float f11 = f7 + typeRatio;
        CropRegionInfo cropRegionInfo = new CropRegionInfo(f8, f9, f10, f11);
        if (f3 == 0.0f) {
            cropRegionInfo.mScore = 1.0f;
            cropRegionInfo.mIsAccept = true;
            return cropRegionInfo;
        }
        float cropScore = getCropScore(rectF.left, rectF.top, rectF.right, rectF.bottom, f8, f9, f10, f11);
        cropRegionInfo.mScore = cropScore;
        if (cropScore >= f3) {
            cropRegionInfo.mIsAccept = true;
        } else {
            cropRegionInfo.mIsAccept = false;
        }
        return cropRegionInfo;
    }

    public static float getCropScore(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float max = Math.max(f5, f);
        float min = Math.min(f7, f3);
        float max2 = Math.max(f6, f2);
        float min2 = Math.min(f8, f4);
        if (max >= min || max2 >= min2) {
            return 0.0f;
        }
        return ((min - max) * (min2 - max2)) / ((f3 - f) * (f4 - f2));
    }

    /* loaded from: classes.dex */
    public static class DataFetcherFromDb implements DataFetcher {
        @Override // com.miui.gallery.assistant.utils.AnalyticUtils.DataFetcher
        public <T> T query(String[] strArr, String str, String[] strArr2, String str2, DataFetcher.DataQueryHandler<T> dataQueryHandler) {
            Cursor cursor;
            Cursor cursor2 = null;
            try {
                cursor = GalleryDBHelper.getInstance().getReadableDatabase().query(SupportSQLiteQueryBuilder.builder("cloud").columns(strArr).selection(str, strArr2).create());
                if (dataQueryHandler != null) {
                    try {
                        try {
                            T handle = dataQueryHandler.handle(cursor);
                            BaseMiscUtil.closeSilently(cursor);
                            return handle;
                        } catch (Exception e) {
                            e = e;
                            DefaultLogger.w("AnalyticUtils", e);
                            BaseMiscUtil.closeSilently(cursor);
                            return null;
                        }
                    } catch (Throwable th) {
                        th = th;
                        cursor2 = cursor;
                        BaseMiscUtil.closeSilently(cursor2);
                        throw th;
                    }
                }
            } catch (Exception e2) {
                e = e2;
                cursor = null;
            } catch (Throwable th2) {
                th = th2;
                BaseMiscUtil.closeSilently(cursor2);
                throw th;
            }
            BaseMiscUtil.closeSilently(cursor);
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static class DataFetcherFromProvider implements DataFetcher {
        public static /* synthetic */ Object $r8$lambda$ZHK23EIxYqhFNwKXEf4vYmEoX3M(DataFetcher.DataQueryHandler dataQueryHandler, Cursor cursor) {
            return lambda$query$0(dataQueryHandler, cursor);
        }

        @Override // com.miui.gallery.assistant.utils.AnalyticUtils.DataFetcher
        public <T> T query(String[] strArr, String str, String[] strArr2, String str2, final DataFetcher.DataQueryHandler<T> dataQueryHandler) {
            return (T) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.Cloud.CLOUD_URI, strArr, str, (String[]) null, (String) null, new SafeDBUtil.QueryHandler() { // from class: com.miui.gallery.assistant.utils.AnalyticUtils$DataFetcherFromProvider$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                /* renamed from: handle */
                public final Object mo1808handle(Cursor cursor) {
                    return AnalyticUtils.DataFetcherFromProvider.$r8$lambda$ZHK23EIxYqhFNwKXEf4vYmEoX3M(AnalyticUtils.DataFetcher.DataQueryHandler.this, cursor);
                }
            });
        }

        public static /* synthetic */ Object lambda$query$0(DataFetcher.DataQueryHandler dataQueryHandler, Cursor cursor) {
            if (dataQueryHandler != null) {
                return dataQueryHandler.handle(cursor);
            }
            return null;
        }
    }

    /* renamed from: com.miui.gallery.assistant.utils.AnalyticUtils$1 */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$assistant$utils$AnalyticUtils$DataFetcherType;

        static {
            int[] iArr = new int[DataFetcherType.values().length];
            $SwitchMap$com$miui$gallery$assistant$utils$AnalyticUtils$DataFetcherType = iArr;
            try {
                iArr[DataFetcherType.DATA_FETCHER_DB.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$assistant$utils$AnalyticUtils$DataFetcherType[DataFetcherType.DATA_FETCHER_PROVIDER.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    /* loaded from: classes.dex */
    public static class DataFetcherFactory {
        public static DataFetcher createDataFetcher(DataFetcherType dataFetcherType) {
            if (AnonymousClass1.$SwitchMap$com$miui$gallery$assistant$utils$AnalyticUtils$DataFetcherType[dataFetcherType.ordinal()] == 1) {
                return new DataFetcherFromDb();
            }
            return new DataFetcherFromProvider();
        }
    }

    /* loaded from: classes.dex */
    public static class CropRegionInfo implements Parcelable {
        public static final Parcelable.Creator<CropRegionInfo> CREATOR = new Parcelable.Creator<CropRegionInfo>() { // from class: com.miui.gallery.assistant.utils.AnalyticUtils.CropRegionInfo.1
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public CropRegionInfo mo572createFromParcel(Parcel parcel) {
                return new CropRegionInfo(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public CropRegionInfo[] mo573newArray(int i) {
                return new CropRegionInfo[i];
            }
        };
        public float mCropLeftTopX;
        public float mCropLeftTopY;
        public float mCropRightBottomX;
        public float mCropRightBottomY;
        public boolean mIsAccept;
        public float mScore;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public CropRegionInfo(float f, float f2, float f3, float f4) {
            this.mCropLeftTopX = f;
            this.mCropLeftTopY = f2;
            this.mCropRightBottomX = f3;
            this.mCropRightBottomY = f4;
        }

        public CropRegionInfo(Parcel parcel) {
            this.mCropLeftTopX = parcel.readFloat();
            this.mCropLeftTopY = parcel.readFloat();
            this.mCropRightBottomX = parcel.readFloat();
            this.mCropRightBottomY = parcel.readFloat();
            this.mScore = parcel.readFloat();
            this.mIsAccept = parcel.readByte() != 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeFloat(this.mCropLeftTopX);
            parcel.writeFloat(this.mCropLeftTopY);
            parcel.writeFloat(this.mCropRightBottomX);
            parcel.writeFloat(this.mCropRightBottomY);
            parcel.writeFloat(this.mScore);
            parcel.writeByte(this.mIsAccept ? (byte) 1 : (byte) 0);
        }
    }

    /* loaded from: classes.dex */
    public enum RegionType {
        ONE_ONE(0),
        TWO_ONE(1),
        TWO_THREE(2);
        
        public final int mId;

        RegionType(int i) {
            this.mId = i;
        }

        public float getTypeRatio() {
            int i = this.mId;
            if (i != 1) {
                return i != 2 ? 1.0f : 0.6666667f;
            }
            return 2.0f;
        }

        public static RegionType getRegionTypeById(int i) {
            if (i != 0) {
                if (i == 1) {
                    return TWO_ONE;
                }
                if (i == 2) {
                    return TWO_THREE;
                }
                return ONE_ONE;
            }
            return ONE_ONE;
        }
    }

    static {
        HashMap hashMap = new HashMap();
        sTagThresholdMap = hashMap;
        hashMap.put(Integer.valueOf(MediaSceneTagManager.Tag_Version_0.E_STAPLE_FOOD.getTagValue()), Float.valueOf(0.998946f));
        hashMap.put(Integer.valueOf(MediaSceneTagManager.Tag_Version_0.E_ICE_CREAM.getTagValue()), Float.valueOf(0.8333333f));
        Integer valueOf = Integer.valueOf(MediaSceneTagManager.Tag_Version_0.E_DRINK.getTagValue());
        Float valueOf2 = Float.valueOf(0.95f);
        hashMap.put(valueOf, valueOf2);
        hashMap.put(Integer.valueOf(MediaSceneTagManager.Tag_Version_0.E_DESSERT.getTagValue()), Float.valueOf(0.9985864f));
        hashMap.put(Integer.valueOf(MediaSceneTagManager.Tag_Version_0.E_SUSHI.getTagValue()), valueOf2);
        hashMap.put(Integer.valueOf(MediaSceneTagManager.Tag_Version_0.E_WILD_ANIMAL.getTagValue()), valueOf2);
        Integer valueOf3 = Integer.valueOf(MediaSceneTagManager.Tag_Version_0.E_NIGHTSCENE.getTagValue());
        Float valueOf4 = Float.valueOf(0.0f);
        hashMap.put(valueOf3, valueOf4);
        hashMap.put(Integer.valueOf(MediaSceneTagManager.Tag_Version_0.E_SKY.getTagValue()), valueOf4);
        hashMap.put(Integer.valueOf(MediaSceneTagManager.Tag_Version_0.E_TEMPLE.getTagValue()), valueOf2);
        hashMap.put(Integer.valueOf(MediaSceneTagManager.Tag_Version_0.E_BUILDING.getTagValue()), Float.valueOf(0.9984375f));
        hashMap.put(Integer.valueOf(MediaSceneTagManager.Tag_Version_0.E_PALACE.getTagValue()), valueOf2);
        hashMap.put(Integer.valueOf(MediaSceneTagManager.Tag_Version_0.E_MOUNTAIN.getTagValue()), valueOf4);
        hashMap.put(Integer.valueOf(MediaSceneTagManager.Tag_Version_0.E_CASTLE.getTagValue()), valueOf2);
        hashMap.put(Integer.valueOf(MediaSceneTagManager.Tag_Version_0.E_CITY.getTagValue()), valueOf2);
        hashMap.put(Integer.valueOf(MediaSceneTagManager.Tag_Version_0.E_CHURCH.getTagValue()), valueOf2);
        hashMap.put(Integer.valueOf(MediaSceneTagManager.Tag_Version_0.E_SUNRISE_SUNSET.getTagValue()), valueOf4);
        hashMap.put(Integer.valueOf(MediaSceneTagManager.Tag_Version_0.E_STAR_SKY.getTagValue()), valueOf4);
        hashMap.put(Integer.valueOf(MediaSceneTagManager.Tag_Version_0.E_TREE.getTagValue()), valueOf4);
        hashMap.put(Integer.valueOf(MediaSceneTagManager.Tag_Version_0.E_WOODS.getTagValue()), valueOf4);
        hashMap.put(Integer.valueOf(MediaSceneTagManager.Tag_Version_0.E_BRIDGE.getTagValue()), valueOf2);
        hashMap.put(Integer.valueOf(MediaSceneTagManager.Tag_Version_0.E_SEAFOOD.getTagValue()), valueOf2);
        hashMap.put(Integer.valueOf(MediaSceneTagManager.Tag_Version_0.E_WATERFALL.getTagValue()), Float.valueOf(0.8881579f));
        hashMap.put(Integer.valueOf(MediaSceneTagManager.Tag_Version_0.E_HOTPOT.getTagValue()), Float.valueOf(0.8443396f));
        hashMap.put(Integer.valueOf(MediaSceneTagManager.Tag_Version_0.E_COOKED_DISH.getTagValue()), Float.valueOf(0.8508287f));
        hashMap.put(Integer.valueOf(MediaSceneTagManager.Tag_Version_0.E_FRUIT.getTagValue()), valueOf2);
        hashMap.put(Integer.valueOf(MediaSceneTagManager.Tag_Version_0.E_BARBECUE.getTagValue()), valueOf2);
        hashMap.put(Integer.valueOf(MediaSceneTagManager.Tag_Version_0.E_CAT.getTagValue()), valueOf2);
        hashMap.put(Integer.valueOf(MediaSceneTagManager.Tag_Version_0.E_PIG.getTagValue()), valueOf2);
        hashMap.put(Integer.valueOf(MediaSceneTagManager.Tag_Version_0.E_DOG.getTagValue()), valueOf2);
        hashMap.put(Integer.valueOf(MediaSceneTagManager.Tag_Version_0.E_STREET_VIEW.getTagValue()), valueOf4);
        hashMap.put(Integer.valueOf(MediaSceneTagManager.Tag_Version_0.E_BIRD.getTagValue()), Float.valueOf(0.8f));
        hashMap.put(Integer.valueOf(MediaSceneTagManager.Tag_Version_0.E_FLOWER.getTagValue()), valueOf2);
    }
}
