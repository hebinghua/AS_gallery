package com.miui.gallery.util;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.LongSparseArray;
import com.miui.gallery.R;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.cloudcontrol.strategies.AlbumsStrategy;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.ShareAlbumHelper;
import com.xiaomi.mirror.synergy.CallMethod;
import com.xiaomi.stat.a.j;

/* loaded from: classes2.dex */
public class AlbumsCursorHelper {
    public LongSparseArray<String> mAlbumBabyInfo;
    public LongSparseArray<String> mAlbumBabyShareInfo;
    public LongSparseArray<Integer> mAlbumId2CursorPosMapping;
    public LongSparseArray<String> mAlbumPeopleId;
    public LongSparseArray<String> mAlbumThumbnailInfoOfBaby;
    public Context mContext;
    public Cursor mCursor;

    public static boolean isAllPhotoAlbum(long j) {
        return j == 2147483644;
    }

    public static boolean isFavoritesAlbum(long j) {
        return 2147483642 == j;
    }

    public AlbumsCursorHelper(Context context) {
        this.mContext = context;
    }

    public boolean isAlbumDataValid(long j) {
        LongSparseArray<Integer> longSparseArray = this.mAlbumId2CursorPosMapping;
        return (longSparseArray == null || longSparseArray.get(j) == null) ? false : true;
    }

    public synchronized void setAlbumsData(Cursor cursor) {
        Album.ExtraInfo newInstance;
        LongSparseArray<Integer> longSparseArray = this.mAlbumId2CursorPosMapping;
        if (longSparseArray != null) {
            longSparseArray.clear();
        }
        if (cursor != null && !cursor.isClosed()) {
            this.mCursor = cursor;
            if (this.mAlbumId2CursorPosMapping == null) {
                this.mAlbumId2CursorPosMapping = new LongSparseArray<>(cursor.getCount());
            }
            if (this.mAlbumPeopleId == null) {
                this.mAlbumPeopleId = new LongSparseArray<>();
            }
            if (this.mAlbumBabyInfo == null) {
                this.mAlbumBabyInfo = new LongSparseArray<>();
            }
            if (this.mAlbumThumbnailInfoOfBaby == null) {
                this.mAlbumThumbnailInfoOfBaby = new LongSparseArray<>();
            }
            if (this.mAlbumBabyShareInfo == null) {
                this.mAlbumBabyShareInfo = new LongSparseArray<>();
            }
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                long albumId = getAlbumId(cursor);
                this.mAlbumId2CursorPosMapping.put(albumId, Integer.valueOf(cursor.getPosition()));
                String string = cursor.getString(cursor.getColumnIndex(CallMethod.ARG_EXTRA_STRING));
                if (!TextUtils.isEmpty(string) && (newInstance = Album.ExtraInfo.newInstance(string)) != null) {
                    String babyInfo = newInstance.getBabyInfo();
                    if (!TextUtils.isEmpty(babyInfo)) {
                        this.mAlbumBabyInfo.put(albumId, babyInfo);
                    }
                    String peopleId = newInstance.getPeopleId();
                    if (!TextUtils.isEmpty(peopleId)) {
                        this.mAlbumPeopleId.put(albumId, peopleId);
                    }
                    String thumbnailInfo = newInstance.getThumbnailInfo();
                    if (!TextUtils.isEmpty(thumbnailInfo)) {
                        this.mAlbumThumbnailInfoOfBaby.put(albumId, thumbnailInfo);
                    }
                    String shareInfo = newInstance.getShareInfo();
                    if (!TextUtils.isEmpty(shareInfo)) {
                        this.mAlbumBabyShareInfo.put(albumId, shareInfo);
                    }
                }
                cursor.moveToNext();
            }
        } else {
            this.mCursor = null;
            this.mAlbumId2CursorPosMapping = null;
        }
    }

    public final boolean moveCursorToPosition(int i) {
        Cursor cursor = this.mCursor;
        return cursor != null && cursor.moveToPosition(i);
    }

    public final long getAlbumId(Cursor cursor) {
        return cursor.getInt(cursor.getColumnIndex(j.c));
    }

    public String getAlbumLocalPath(Long l) {
        return getAlbumLocalPath(this.mAlbumId2CursorPosMapping.get(l.longValue()).intValue());
    }

    public final String getAlbumLocalPath(int i) {
        moveCursorToPosition(i);
        return getAlbumLocalPath(this.mCursor);
    }

    public final String getAlbumLocalPath(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex("localPath"));
    }

    public String getAlbumName(long j) {
        AlbumsStrategy.Album albumByPath = CloudControlStrategyHelper.getAlbumByPath(getAlbumLocalPath(Long.valueOf(j)));
        if (albumByPath != null && albumByPath.getBestName() != null) {
            return albumByPath.getBestName();
        }
        return getAlbumName(this.mAlbumId2CursorPosMapping.get(j).intValue());
    }

    public String getAlbumName(int i) {
        moveCursorToPosition(i);
        return getAlbumName(this.mCursor);
    }

    public final String getAlbumName(Cursor cursor) {
        return getAlbumName(this.mContext, cursor.getLong(cursor.getColumnIndex(j.c)), cursor.getString(cursor.getColumnIndex("serverId")), cursor.getString(cursor.getColumnIndex("name")));
    }

    public static String getAlbumName(Context context, long j, String str, String str2) {
        if (isVideoAlbum(j)) {
            return context.getString(R.string.album_videos_name);
        }
        if (isCameraAlbum(str)) {
            return context.getString(R.string.album_camera_name);
        }
        if (isScreenshotsAlbum(str)) {
            return context.getString(R.string.album_screenshot_name);
        }
        if (isScreenshotsRecordersAlbum(str)) {
            return context.getString(R.string.album_screenshot_and_screen_recorder_name);
        }
        if (isAllPhotoAlbum(j)) {
            return context.getString(R.string.album_name_recent_discovery);
        }
        return isFavoritesAlbum(j) ? context.getString(R.string.album_favorites_name) : str2;
    }

    public long getAttributes(long j) {
        LongSparseArray<Integer> longSparseArray = this.mAlbumId2CursorPosMapping;
        if (longSparseArray != null) {
            return getAttributes(longSparseArray.get(j).intValue());
        }
        return -1L;
    }

    public long getAttributes(int i) {
        moveCursorToPosition(i);
        return getAttributes(this.mCursor);
    }

    public final long getAttributes(Cursor cursor) {
        return cursor.getLong(cursor.getColumnIndex("attributes"));
    }

    public String getServerId(long j) {
        LongSparseArray<Integer> longSparseArray = this.mAlbumId2CursorPosMapping;
        if (longSparseArray != null) {
            return getServerId(longSparseArray.get(j).intValue());
        }
        return null;
    }

    public String getServerId(int i) {
        moveCursorToPosition(i);
        return getServerId(this.mCursor);
    }

    public final String getServerId(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex("serverId"));
    }

    public static boolean isVideoAlbum(long j) {
        return Album.isVideoAlbum(j);
    }

    public static boolean isCameraAlbum(String str) {
        return String.valueOf(1L).equals(str);
    }

    public boolean isOtherShareAlbum(long j) {
        LongSparseArray<Integer> longSparseArray = this.mAlbumId2CursorPosMapping;
        return longSparseArray != null && isOtherShareAlbum(longSparseArray.get(j).intValue());
    }

    public final boolean isOtherShareAlbum(int i) {
        moveCursorToPosition(i);
        return isOtherShareAlbum(this.mCursor);
    }

    public final boolean isOtherShareAlbum(Cursor cursor) {
        return ShareAlbumHelper.isOtherShareAlbumId(cursor.getInt(cursor.getColumnIndex(j.c)));
    }

    public static boolean isScreenshotsAlbum(Cursor cursor) {
        return isScreenshotsAlbum(cursor.getString(cursor.getColumnIndex("serverId")));
    }

    public static boolean isScreenshotsAlbum(String str) {
        return String.valueOf(2L).equals(str);
    }

    public static boolean isScreenshotsRecordersAlbum(Cursor cursor) {
        return isScreenshotsRecordersAlbum(cursor.getString(cursor.getColumnIndex("serverId")));
    }

    public static boolean isScreenshotsRecordersAlbum(String str) {
        return String.valueOf(-2147483645L).equals(str);
    }

    public static boolean isSystemAlbum(Cursor cursor) {
        String string = cursor.getString(cursor.getColumnIndex("serverId"));
        for (Long l : GalleryContract.Album.ALL_SYSTEM_ALBUM_SERVER_IDS) {
            if (String.valueOf(l).equals(string)) {
                return true;
            }
        }
        return false;
    }

    public final boolean isAutoUploadedAlbum(Cursor cursor) {
        return (cursor.getLong(cursor.getColumnIndex("attributes")) & 1) != 0 || (isSystemAlbum(cursor) && !isScreenshotsAlbum(cursor) && !isScreenshotsRecordersAlbum(cursor)) || isOtherShareAlbum(cursor);
    }

    public boolean isLocalAlbum(long j) {
        LongSparseArray<Integer> longSparseArray = this.mAlbumId2CursorPosMapping;
        return longSparseArray != null && isLocalAlbum(longSparseArray.get(j).intValue());
    }

    public final boolean isLocalAlbum(int i) {
        moveCursorToPosition(i);
        return isLocalAlbum(this.mCursor);
    }

    public final boolean isLocalAlbum(Cursor cursor) {
        return !isAutoUploadedAlbum(cursor);
    }

    public boolean isBabyAlbum(long j) {
        LongSparseArray<String> longSparseArray = this.mAlbumBabyInfo;
        return longSparseArray != null && !TextUtils.isEmpty(longSparseArray.get(j));
    }

    public String getBabyAlbumPeopleId(long j) {
        LongSparseArray<String> longSparseArray = this.mAlbumPeopleId;
        if (longSparseArray != null) {
            return longSparseArray.get(j);
        }
        return null;
    }

    public String getThumbnailInfoOfBaby(long j) {
        LongSparseArray<String> longSparseArray = this.mAlbumThumbnailInfoOfBaby;
        if (longSparseArray != null) {
            return longSparseArray.get(j);
        }
        return null;
    }

    public String getBabyInfo(long j) {
        LongSparseArray<String> longSparseArray = this.mAlbumBabyInfo;
        if (longSparseArray != null) {
            return longSparseArray.get(j);
        }
        return null;
    }

    public String getBabySharerInfo(long j) {
        LongSparseArray<String> longSparseArray = this.mAlbumBabyShareInfo;
        if (longSparseArray != null) {
            return longSparseArray.get(j);
        }
        return null;
    }
}
