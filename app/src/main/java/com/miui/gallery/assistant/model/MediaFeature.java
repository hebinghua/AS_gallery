package com.miui.gallery.assistant.model;

import android.content.ContentValues;
import android.database.Cursor;
import com.miui.gallery.assistant.algorithm.Algorithm;
import com.miui.gallery.assistant.jni.score.QualityScore;
import com.miui.gallery.dao.base.Entity;
import com.miui.gallery.dao.base.TableColumn;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.util.GsonUtils;
import com.xiaomi.stat.MiStat;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class MediaFeature extends Entity implements Comparable<MediaFeature> {
    public static final String ALL_FEATURE_PROCESSED_SELECTION;
    public static final String ALL_IQA_CLUSTER_SELECTION = "mediaId>0 AND version = 2 AND (mediaCalculationType = 0 OR mediaCalculationType = 1) AND resultFlag & " + Integer.toString(1) + " > 0 AND clusterGroupId>0";
    public static final String ALL_PROCESSED_SELECTION;
    public Float[] mClusterFeature;
    public long mClusterGroupId;
    public long mCreateTime;
    public double mIqaBala;
    public double mIqaBlur;
    public int mIqaBlurType;
    public double mIqaComp;
    public double mIqaExpo;
    public double mIqaHaze;
    public double mIqaNima;
    public double mIqaNois;
    public double mIqaSatu;
    public int mMediaCalculationType;
    public long mMediaDateTime;
    public long mMediaId;
    public int mResultFlag;
    public double mScore;
    public String mSha1;
    public int mVersion;

    static {
        int[] iArr;
        StringBuilder sb = new StringBuilder();
        sb.append("mediaId>0 AND version = 2 AND (mediaCalculationType = 0 OR mediaCalculationType = 1) AND ");
        sb.append(getFlagsMatchSql(Algorithm.FLAG_FEATURE_ALL_ARRAY));
        ALL_FEATURE_PROCESSED_SELECTION = sb.toString();
        ALL_PROCESSED_SELECTION = "mediaId>0 AND version = 2 AND (mediaCalculationType = 0 OR mediaCalculationType = 1) AND " + getFlagsMatchSql(iArr) + " AND clusterGroupId>0";
    }

    public MediaFeature() {
        this.mVersion = 2;
    }

    public MediaFeature(long j, String str, long j2) {
        this.mMediaId = j;
        this.mSha1 = str;
        this.mVersion = 2;
        this.mIqaBlurType = -1;
        this.mMediaCalculationType = 0;
        this.mCreateTime = System.currentTimeMillis();
        this.mMediaDateTime = j2;
    }

    public void setFeatureFlag(int i) {
        this.mResultFlag = i | this.mResultFlag;
    }

    public void setVersion(int i) {
        this.mVersion = i;
    }

    public boolean isFeatureDone(int i) {
        return (i & this.mResultFlag) > 0 && this.mVersion == 2;
    }

    public int getFeatureFlag() {
        return this.mResultFlag;
    }

    public double getScore() {
        return this.mScore;
    }

    public long getMediaId() {
        return this.mMediaId;
    }

    public String getSha1() {
        return this.mSha1;
    }

    public Float[] getClusterFeature() {
        return this.mClusterFeature;
    }

    public boolean hasClusterFeature() {
        Float[] fArr = this.mClusterFeature;
        return fArr != null && fArr.length == 1024;
    }

    public long getClusterGroupId() {
        return this.mClusterGroupId;
    }

    public void setQualityScore(QualityScore qualityScore) {
        if (qualityScore != null) {
            this.mIqaExpo = qualityScore.getIqaExpo();
            this.mIqaSatu = qualityScore.getIqaSatu();
            this.mIqaBala = qualityScore.getIqaBala();
            this.mIqaHaze = qualityScore.getIqaHaze();
            this.mIqaNois = qualityScore.getIqaNois();
            this.mIqaBlur = qualityScore.getIqaBlur();
            this.mIqaComp = qualityScore.getIqaComp();
            this.mIqaNima = qualityScore.getIqaNima();
            this.mIqaBlurType = (int) qualityScore.getIqaBlurType();
            this.mScore = this.mIqaNima;
            setFeatureFlag(1);
        }
    }

    public void setClusterFeature(Float[] fArr) {
        if (fArr != null) {
            this.mClusterFeature = fArr;
            setFeatureFlag(4);
        }
    }

    public void setMediaCalculationType(int i) {
        this.mMediaCalculationType = i;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public List<TableColumn> getTableColumns() {
        ArrayList arrayList = new ArrayList();
        Entity.addColumn((List<TableColumn>) arrayList, "mediaId", "INTEGER", true);
        Entity.addColumn(arrayList, "sha1", "TEXT");
        Entity.addColumn(arrayList, "version", "INTEGER");
        Entity.addColumn(arrayList, MiStat.Param.SCORE, "REAL");
        Entity.addColumn(arrayList, "iqaExpo", "REAL");
        Entity.addColumn(arrayList, "iqaSatu", "REAL");
        Entity.addColumn(arrayList, "iqaBala", "REAL");
        Entity.addColumn(arrayList, "iqaHaze", "REAL");
        Entity.addColumn(arrayList, "iqaNois", "REAL");
        Entity.addColumn(arrayList, "iqaBlur", "REAL");
        Entity.addColumn(arrayList, "iqaComP", "REAL");
        Entity.addColumn(arrayList, "iqaNima", "REAL");
        Entity.addColumn(arrayList, "iqaBlueType", "INTEGER");
        Entity.addColumn(arrayList, "clusterFeature", "TEXT");
        Entity.addColumn(arrayList, "resultFlag", "INTEGER");
        Entity.addColumn(arrayList, "clusterGroupId", "INTEGER");
        Entity.addColumn(arrayList, "mediaCalculationType", "INTEGER");
        Entity.addColumn(arrayList, "createTime", "INTEGER");
        Entity.addColumn(arrayList, "mediaDatetime", "INTEGER");
        return arrayList;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onInitFromCursor(Cursor cursor) {
        this.mMediaId = Entity.getLong(cursor, "mediaId");
        this.mSha1 = Entity.getStringDefault(cursor, "sha1", "");
        this.mVersion = Entity.getIntDefault(cursor, "version", 0);
        this.mScore = Entity.getDoubleDefault(cursor, MiStat.Param.SCORE, SearchStatUtils.POW);
        this.mIqaExpo = Entity.getDoubleDefault(cursor, "iqaExpo", SearchStatUtils.POW);
        this.mIqaSatu = Entity.getDoubleDefault(cursor, "iqaSatu", SearchStatUtils.POW);
        this.mIqaBala = Entity.getDoubleDefault(cursor, "iqaBala", SearchStatUtils.POW);
        this.mIqaHaze = Entity.getDoubleDefault(cursor, "iqaHaze", SearchStatUtils.POW);
        this.mIqaNois = Entity.getDoubleDefault(cursor, "iqaNois", SearchStatUtils.POW);
        this.mIqaBlur = Entity.getDoubleDefault(cursor, "iqaBlur", SearchStatUtils.POW);
        this.mIqaBlurType = Entity.getIntDefault(cursor, "iqaBlueType", -1);
        this.mIqaComp = Entity.getDoubleDefault(cursor, "iqaComP", SearchStatUtils.POW);
        this.mIqaNima = Entity.getDoubleDefault(cursor, "iqaNima", SearchStatUtils.POW);
        this.mClusterFeature = (Float[]) GsonUtils.fromJson(Entity.getStringDefault(cursor, "clusterFeature", ""), (Class<Object>) Float[].class);
        this.mResultFlag = Entity.getIntDefault(cursor, "resultFlag", 0);
        this.mClusterGroupId = Entity.getLongDefault(cursor, "clusterGroupId", 0L);
        this.mMediaCalculationType = Entity.getIntDefault(cursor, "mediaCalculationType", 0);
        this.mCreateTime = Entity.getLongDefault(cursor, "createTime", 0L);
        this.mMediaDateTime = Entity.getLongDefault(cursor, "mediaDatetime", 0L);
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onConvertToContents(ContentValues contentValues) {
        contentValues.put("mediaId", Long.valueOf(this.mMediaId));
        contentValues.put("sha1", this.mSha1);
        contentValues.put("version", Integer.valueOf(this.mVersion));
        contentValues.put(MiStat.Param.SCORE, Double.valueOf(this.mScore));
        contentValues.put("iqaExpo", Double.valueOf(this.mIqaExpo));
        contentValues.put("iqaSatu", Double.valueOf(this.mIqaSatu));
        contentValues.put("iqaBala", Double.valueOf(this.mIqaBala));
        contentValues.put("iqaHaze", Double.valueOf(this.mIqaHaze));
        contentValues.put("iqaNois", Double.valueOf(this.mIqaNois));
        contentValues.put("iqaBlur", Double.valueOf(this.mIqaBlur));
        contentValues.put("iqaBlueType", Integer.valueOf(this.mIqaBlurType));
        contentValues.put("iqaComP", Double.valueOf(this.mIqaComp));
        contentValues.put("iqaNima", Double.valueOf(this.mIqaNima));
        contentValues.put("clusterFeature", GsonUtils.toString(this.mClusterFeature));
        contentValues.put("resultFlag", Integer.valueOf(this.mResultFlag));
        contentValues.put("clusterGroupId", Long.valueOf(this.mClusterGroupId));
        contentValues.put("mediaCalculationType", Integer.valueOf(this.mMediaCalculationType));
        contentValues.put("createTime", Long.valueOf(this.mCreateTime));
        contentValues.put("mediaDatetime", Long.valueOf(this.mMediaDateTime));
    }

    public String toString() {
        return "MediaFeature{mMediaId=" + this.mMediaId + ", mVersion=" + this.mVersion + ", mScore=" + this.mScore + ", mIqaExpo=" + this.mIqaExpo + ", mIqaSatu=" + this.mIqaSatu + ", mIqaBala=" + this.mIqaBala + ", mIqaHaze=" + this.mIqaHaze + ", mIqaNois=" + this.mIqaNois + ", mIqaBlur=" + this.mIqaBlur + ", mIqaBlurType=" + this.mIqaBlurType + ", mIqaComp=" + this.mIqaComp + ", mIqaNima=" + this.mIqaNima + ", mResultFlag=" + this.mResultFlag + ", mClusterGroupId=" + this.mClusterGroupId + ", mMediaType=" + this.mMediaCalculationType + ", mCreateTime=" + this.mCreateTime + '}';
    }

    public boolean isPoorMedia() {
        return (this.mIqaBlur < 82.0d && this.mIqaBlurType == 0) || this.mIqaNois < 79.4d || this.mIqaExpo < 62.3d;
    }

    @Override // java.lang.Comparable
    public int compareTo(MediaFeature mediaFeature) {
        return Double.compare(this.mScore, mediaFeature.mScore);
    }

    public static String getFlagsMatchSql(int[] iArr) {
        if (iArr == null || iArr.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < iArr.length; i++) {
            sb.append("resultFlag");
            sb.append(" & ");
            sb.append(Integer.toString(iArr[i]));
            sb.append(" > 0");
            if (i != iArr.length - 1) {
                sb.append(" AND ");
            }
        }
        return sb.toString();
    }

    public boolean isSelectionFeatureDone() {
        return isFeatureDone(1) && this.mClusterGroupId > 0;
    }
}
