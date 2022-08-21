package com.miui.gallery.assistant.model;

import android.database.Cursor;
import android.text.TextUtils;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.card.model.BaseMedia;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class MediaFeatureItem extends BaseMedia implements ImageFeatureItem, Comparable<MediaFeatureItem> {
    public long mDateTime;
    public String mDownloadPath;
    public long mDuration;
    public List<FaceInfo> mFaceResult;
    public long mFileSize;
    public long mId;
    public int mLocalFlag;
    public transient MediaFeature mMediaFeature;
    public List<MediaScene> mMediaSceneResult;
    public String mMicroThumbnailPath;
    public String mOriginPath;
    public List<PeopleEvent> mPeopleEventResult;
    public long mServerId;
    public String mSha1;
    public long mSpecialTypeFlags;
    public String mThumbnailPath;
    public int mType;
    public static final String[] PROJECTION = {j.c, "sha1", "microthumbfile", "thumbnailFile", "localFile", "mixedDateTime", "localFlag", "serverId", "serverType", MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, "specialTypeFlags", "duration"};
    public static final String[] MEDIA_PROJECTION = {j.c, "sha1", "microthumbfile", "thumbnailFile", "localFile", "alias_create_time", "localFlag", "serverId", "serverType", MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, "specialTypeFlags", "duration"};

    public MediaFeatureItem(Cursor cursor) {
        if (cursor != null) {
            this.mId = cursor.getLong(0);
            this.mSha1 = cursor.getString(1);
            this.mMicroThumbnailPath = cursor.getString(2);
            this.mThumbnailPath = cursor.getString(3);
            this.mOriginPath = cursor.getString(4);
            this.mDateTime = cursor.getLong(5);
            this.mLocalFlag = cursor.getInt(6);
            this.mServerId = cursor.getLong(7);
            this.mType = cursor.getInt(8);
            this.mFileSize = cursor.getLong(9);
            this.mSpecialTypeFlags = cursor.getLong(10);
            this.mDuration = cursor.getLong(11);
        }
    }

    public MediaFeatureItem(long j, String str, int i, long j2) {
        this.mId = j;
        this.mOriginPath = str;
        this.mFileSize = j2;
        this.mType = i;
    }

    public long getId() {
        return this.mId;
    }

    public String getSha1() {
        return this.mSha1;
    }

    public String getMicroThumbnailPath() {
        return this.mMicroThumbnailPath;
    }

    public void setMicroThumbnailPath(String str) {
        this.mMicroThumbnailPath = str;
    }

    public String getThumbnailPath() {
        return this.mThumbnailPath;
    }

    public void setThumbnailPath(String str) {
        this.mThumbnailPath = str;
    }

    public String getOriginPath() {
        return this.mOriginPath;
    }

    public void setOriginPath(String str) {
        this.mOriginPath = str;
    }

    public long getDateTime() {
        return this.mDateTime;
    }

    public int getLocalFlag() {
        return this.mLocalFlag;
    }

    public long getFileSize() {
        return this.mFileSize;
    }

    public long getSpecialTypeFlags() {
        return this.mSpecialTypeFlags;
    }

    @Override // com.miui.gallery.assistant.model.ImageFeatureItem
    public MediaFeature getMediaFeature() {
        return this.mMediaFeature;
    }

    @Override // com.miui.gallery.assistant.model.ImageFeatureItem
    public void setMediaFeature(MediaFeature mediaFeature) {
        this.mMediaFeature = mediaFeature;
    }

    public boolean isSelectionFeatureDone() {
        MediaFeature mediaFeature = this.mMediaFeature;
        return mediaFeature != null && mediaFeature.isSelectionFeatureDone();
    }

    public String getImagePath() {
        if (TextUtils.isEmpty(getOriginPath())) {
            return TextUtils.isEmpty(getThumbnailPath()) ? getMicroThumbnailPath() : getThumbnailPath();
        }
        return getOriginPath();
    }

    public boolean isVideo() {
        return this.mType == 2;
    }

    public boolean isImage() {
        return this.mType == 1;
    }

    @Override // com.miui.gallery.assistant.model.ImageFeatureItem
    public String getMediaSha1() {
        return this.mSha1;
    }

    @Override // com.miui.gallery.assistant.model.ImageFeatureItem
    public long getMediaId() {
        return this.mId;
    }

    public long getServerId() {
        return this.mServerId;
    }

    public void setDownloadPath(String str) {
        this.mDownloadPath = str;
    }

    public String getGuaranteePath() {
        return TextUtils.isEmpty(this.mDownloadPath) ? getImagePath() : this.mDownloadPath;
    }

    public void setMediaSceneResult(List<MediaScene> list) {
        this.mMediaSceneResult = list;
    }

    public boolean isSceneCalculated() {
        return BaseMiscUtil.isValid(this.mMediaSceneResult);
    }

    public List<MediaScene> getMediaSceneResult() {
        return this.mMediaSceneResult;
    }

    public List<FaceInfo> getFaceResult() {
        return this.mFaceResult;
    }

    public void setFaceResult(List<FaceInfo> list) {
        this.mFaceResult = list;
    }

    public List<PeopleEvent> getPeopleEventResult() {
        return this.mPeopleEventResult;
    }

    public void setPeopleEventResult(List<PeopleEvent> list) {
        this.mPeopleEventResult = list;
    }

    public long getDuration() {
        return this.mDuration;
    }

    @Override // java.lang.Comparable
    public int compareTo(MediaFeatureItem mediaFeatureItem) {
        MediaFeature mediaFeature = this.mMediaFeature;
        double d = SearchStatUtils.POW;
        double score = mediaFeature != null ? mediaFeature.getScore() : 0.0d;
        MediaFeature mediaFeature2 = mediaFeatureItem.mMediaFeature;
        if (mediaFeature2 != null) {
            d = mediaFeature2.getScore();
        }
        return Double.compare(d, score);
    }

    public static List<MediaFeatureItem> getMediaFeatureItemsFromCursor(Cursor cursor) {
        ArrayList arrayList = new ArrayList();
        if (cursor == null || !cursor.moveToFirst()) {
            return arrayList;
        }
        do {
            arrayList.add(new MediaFeatureItem(cursor));
        } while (cursor.moveToNext());
        return arrayList;
    }

    public boolean isDocumentImage() {
        if (BaseMiscUtil.isValid(this.mMediaSceneResult)) {
            for (MediaScene mediaScene : this.mMediaSceneResult) {
                if (mediaScene.isDocument()) {
                    return true;
                }
            }
        }
        return (this.mSpecialTypeFlags & 65536) != 0;
    }

    @Override // com.miui.gallery.card.model.BaseMedia
    public String getUri() {
        if (!TextUtils.isEmpty(this.mOriginPath)) {
            return this.mOriginPath;
        }
        if (!TextUtils.isEmpty(this.mThumbnailPath)) {
            return this.mThumbnailPath;
        }
        if (TextUtils.isEmpty(this.mMicroThumbnailPath)) {
            return null;
        }
        return this.mMicroThumbnailPath;
    }
}
