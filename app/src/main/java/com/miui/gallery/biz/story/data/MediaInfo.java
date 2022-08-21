package com.miui.gallery.biz.story.data;

import android.database.Cursor;
import android.text.TextUtils;
import com.miui.gallery.card.model.BaseMedia;
import java.util.Comparator;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: MediaInfo.kt */
/* loaded from: classes.dex */
public final class MediaInfo extends BaseMedia {
    public long createTime;
    public int duration;
    public String filePath;
    public int height;
    public long id;
    public String latitude;
    public String latitudeRef;
    public String location;
    public String longitude;
    public String longitudeRef;
    public String microPath;
    public String microThumb;
    public String mimeType;
    public int orientation;
    public double score;
    public int serverType;
    public String sha1;
    public long size;
    public final long specialTypeFlags;
    public int syncState;
    public String thumbPath;
    public int width;

    public MediaInfo(Cursor cursor) {
        Intrinsics.checkNotNullParameter(cursor, "cursor");
        this.id = cursor.getLong(0);
        this.microPath = cursor.getString(1);
        this.thumbPath = cursor.getString(2);
        this.filePath = cursor.getString(3);
        this.mimeType = cursor.getString(4);
        this.createTime = cursor.getLong(5);
        this.location = cursor.getString(6);
        this.size = cursor.getLong(7);
        this.width = cursor.getInt(8);
        this.height = cursor.getInt(9);
        this.duration = cursor.getInt(10);
        this.latitude = cursor.getString(11);
        this.latitudeRef = cursor.getString(12);
        this.longitude = cursor.getString(13);
        this.longitudeRef = cursor.getString(14);
        this.sha1 = cursor.getString(15);
        this.syncState = cursor.getInt(16);
        this.microThumb = cursor.getString(17);
        this.orientation = cursor.getInt(19);
        this.serverType = cursor.getInt(20);
        this.specialTypeFlags = cursor.getLong(21);
    }

    public final long getId() {
        return this.id;
    }

    public final String getMicroPath() {
        return this.microPath;
    }

    public final String getThumbPath() {
        return this.thumbPath;
    }

    public final void setThumbPath(String str) {
        this.thumbPath = str;
    }

    public final String getFilePath() {
        return this.filePath;
    }

    public final void setFilePath(String str) {
        this.filePath = str;
    }

    public final String getMimeType() {
        return this.mimeType;
    }

    public final long getCreateTime() {
        return this.createTime;
    }

    public final String getLocation() {
        return this.location;
    }

    public final long getSize() {
        return this.size;
    }

    public final int getWidth() {
        return this.width;
    }

    public final int getHeight() {
        return this.height;
    }

    public final int getDuration() {
        return this.duration;
    }

    public final String getSha1() {
        return this.sha1;
    }

    public final int getSyncState() {
        return this.syncState;
    }

    public final int getOrientation() {
        return this.orientation;
    }

    public final double getScore() {
        return this.score;
    }

    public final void setScore(double d) {
        this.score = d;
    }

    public final boolean isVideo() {
        return this.serverType == 2;
    }

    public final long getSpecialTypeFlags() {
        return this.specialTypeFlags;
    }

    @Override // com.miui.gallery.card.model.BaseMedia
    public String getUri() {
        if (!TextUtils.isEmpty(this.filePath)) {
            return this.filePath;
        }
        if (!TextUtils.isEmpty(this.thumbPath)) {
            return this.thumbPath;
        }
        if (TextUtils.isEmpty(this.microPath)) {
            return null;
        }
        return this.microPath;
    }

    /* compiled from: MediaInfo.kt */
    /* loaded from: classes.dex */
    public static final class DurationComparator implements Comparator<MediaInfo> {
        @Override // java.util.Comparator
        public int compare(MediaInfo o1, MediaInfo o2) {
            Intrinsics.checkNotNullParameter(o1, "o1");
            Intrinsics.checkNotNullParameter(o2, "o2");
            return Intrinsics.compare(o2.getDuration(), o1.getDuration());
        }
    }

    /* compiled from: MediaInfo.kt */
    /* loaded from: classes.dex */
    public static final class ScoreComparator implements Comparator<MediaInfo> {
        @Override // java.util.Comparator
        public int compare(MediaInfo o1, MediaInfo o2) {
            Intrinsics.checkNotNullParameter(o1, "o1");
            Intrinsics.checkNotNullParameter(o2, "o2");
            return -Double.compare(o1.getScore(), o2.getScore());
        }
    }

    /* compiled from: MediaInfo.kt */
    /* loaded from: classes.dex */
    public static final class TimeComparator implements Comparator<MediaInfo> {
        @Override // java.util.Comparator
        public int compare(MediaInfo o1, MediaInfo o2) {
            Intrinsics.checkNotNullParameter(o1, "o1");
            Intrinsics.checkNotNullParameter(o2, "o2");
            return -Intrinsics.compare(o1.getCreateTime(), o2.getCreateTime());
        }
    }
}
