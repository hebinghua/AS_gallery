package com.miui.gallery.assistant.model;

import android.database.Cursor;
import com.miui.gallery.dao.base.Entity;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.xiaomi.stat.MiStat;

/* loaded from: classes.dex */
public class TinyMediaFeature implements Comparable<TinyMediaFeature> {
    public static final String[] PROJECTION = {"mediaId", MiStat.Param.SCORE, "clusterGroupId", "iqaExpo", "iqaNois", "iqaBlur", "iqaBlueType", "mediaDatetime"};
    public static final String[] PROJECTION_GROUP = {"mediaId"};
    public final long mClusterGroupId;
    public long mImageDateTime;
    public final long mImageId;
    public boolean mIsPoorImage;
    public final double mScore;

    public TinyMediaFeature(Cursor cursor) {
        this.mImageId = Entity.getLong(cursor, "mediaId");
        this.mScore = Entity.getDoubleDefault(cursor, MiStat.Param.SCORE, SearchStatUtils.POW);
        this.mClusterGroupId = Entity.getLongDefault(cursor, "clusterGroupId", 0L);
        double doubleDefault = Entity.getDoubleDefault(cursor, "iqaExpo", SearchStatUtils.POW);
        this.mIsPoorImage = (Entity.getDoubleDefault(cursor, "iqaBlur", SearchStatUtils.POW) < 82.0d && ((double) Entity.getIntDefault(cursor, "iqaBlueType", -1)) == SearchStatUtils.POW) || Entity.getDoubleDefault(cursor, "iqaNois", SearchStatUtils.POW) < 79.4d || doubleDefault < 62.3d;
        this.mImageDateTime = Entity.getLongDefault(cursor, "mediaDatetime", 0L);
    }

    public long getImageId() {
        return this.mImageId;
    }

    public long getImageDateTime() {
        return this.mImageDateTime;
    }

    public double getScore() {
        return this.mScore;
    }

    public long getClusterGroupId() {
        return this.mClusterGroupId;
    }

    public boolean isPoorImage() {
        return this.mIsPoorImage;
    }

    @Override // java.lang.Comparable
    public int compareTo(TinyMediaFeature tinyMediaFeature) {
        double score = getScore();
        double score2 = tinyMediaFeature.getScore();
        if (!isPoorImage() || tinyMediaFeature.isPoorImage()) {
            if ((!isPoorImage() && tinyMediaFeature.isPoorImage()) || score > score2) {
                return -1;
            }
            return score < score2 ? 1 : 0;
        }
        return 1;
    }
}
