package com.miui.gallery.provider;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.cloud.RequestCloudItem;
import com.miui.gallery.cloud.peopleface.FaceDataManager;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.data.DBAlbum;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.data.DBShareAlbum;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.util.ExifUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.a.j;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

/* loaded from: classes2.dex */
public class BabyLockWallpaperDataManager {
    public static String[] BABY_ALBUM_INFO_PROJECTION = {j.c, "name"};
    public static String[] BABY_PHOTO_INFO_PROJECTION = {j.c, "fileName"};
    public static BabyLockWallpaperDataManager sInstance;
    public ArrayList<BabyAlbumInfo> mAlbums;
    public ArrayList<BabyPhotoInfo> mAllPhotos;

    /* loaded from: classes2.dex */
    public static class BabyAlbumInfo {
        public boolean isOtherShared;
        public long localId;
        public String name;
    }

    /* loaded from: classes2.dex */
    public static class BabyPhotoInfo {
        public long id;
        public boolean isOtherShared;
    }

    public static synchronized BabyLockWallpaperDataManager getInstance() {
        BabyLockWallpaperDataManager babyLockWallpaperDataManager;
        synchronized (BabyLockWallpaperDataManager.class) {
            if (sInstance == null) {
                sInstance = new BabyLockWallpaperDataManager();
            }
            babyLockWallpaperDataManager = sInstance;
        }
        return babyLockWallpaperDataManager;
    }

    /* loaded from: classes2.dex */
    public class PhotoObserver extends ContentObserver {
        public PhotoObserver(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            super.onChange(z);
            BabyLockWallpaperDataManager.this.refresh();
        }
    }

    public BabyLockWallpaperDataManager() {
        intialAlbumAndPhotos();
        GalleryApp.sGetAndroidContext().getContentResolver().registerContentObserver(GalleryContract.Cloud.CLOUD_URI, true, new PhotoObserver(ThreadManager.getMainHandler()));
    }

    public final synchronized void intialAlbumAndPhotos() {
        ArrayList<BabyAlbumInfo> queryAllBabyAlbums = FaceManager.queryAllBabyAlbums();
        this.mAlbums = queryAllBabyAlbums;
        if (queryAllBabyAlbums != null && queryAllBabyAlbums.size() > 0) {
            ArrayList arrayList = new ArrayList();
            Iterator<BabyAlbumInfo> it = this.mAlbums.iterator();
            while (it.hasNext()) {
                BabyAlbumInfo next = it.next();
                if (GalleryPreferences.BabyLock.isBabyAlbumForLockWallpaper(next.localId, next.isOtherShared)) {
                    arrayList.add(next);
                }
            }
            this.mAllPhotos = FaceManager.queryAllBabyAlbumPhotos(arrayList);
        }
    }

    public void refresh() {
        ThreadManager.getMiscPool().submit(new ThreadPool.Job<Void>() { // from class: com.miui.gallery.provider.BabyLockWallpaperDataManager.1
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run  reason: collision with other method in class */
            public Void mo1807run(ThreadPool.JobContext jobContext) {
                BabyLockWallpaperDataManager.this.intialAlbumAndPhotos();
                return null;
            }
        });
    }

    public synchronized ArrayList<BabyAlbumInfo> getAllBabyAlbums() {
        return (ArrayList) this.mAlbums.clone();
    }

    public synchronized boolean existBabyAlbum() {
        boolean z;
        ArrayList<BabyAlbumInfo> arrayList = this.mAlbums;
        if (arrayList != null) {
            if (arrayList.size() > 0) {
                z = true;
            }
        }
        z = false;
        return z;
    }

    public synchronized String getRandomUri() {
        ArrayList<BabyPhotoInfo> arrayList = this.mAllPhotos;
        if (arrayList != null && arrayList.size() != 0) {
            int[] iArr = {3};
            for (int i = 0; i < 5; i++) {
                int random = (int) (Math.random() * this.mAllPhotos.size());
                Uri uri = GalleryCloudUtils.CLOUD_URI;
                if (this.mAllPhotos.get(random).isOtherShared) {
                    uri = GalleryCloudUtils.SHARE_IMAGE_URI;
                }
                DBImage item = com.miui.gallery.cloud.CloudUtils.getItem(uri, GalleryApp.sGetAndroidContext(), j.c, String.valueOf(this.mAllPhotos.get(random).id));
                if (isLegal(item, iArr)) {
                    return createUri(item);
                }
            }
            return null;
        }
        return null;
    }

    public final String createUri(DBImage dBImage) {
        if (dBImage == null) {
            return null;
        }
        return BabyLockWallpaperProvider.BASE_URI.buildUpon().appendPath(dBImage.isShareItem() ? "sharer_album" : "owner_album").appendPath(dBImage.getId()).build().toString();
    }

    public final boolean isLegal(DBImage dBImage, int[] iArr) {
        String wallpaperFilePath = getWallpaperFilePath(dBImage);
        if (TextUtils.isEmpty(wallpaperFilePath) || !new File(wallpaperFilePath).isFile()) {
            return false;
        }
        int i = iArr[0];
        iArr[0] = i - 1;
        if (i <= 0) {
            return true;
        }
        int i2 = ExifUtil.parseRotationInfo(dBImage.getJsonExifInteger("orientation", 0).intValue()).rotation;
        boolean z = i2 == 90 || i2 == 270;
        int intValue = dBImage.getJsonExifInteger("imageWidth", 0).intValue();
        int intValue2 = dBImage.getJsonExifInteger("imageLength", 0).intValue();
        if (z) {
            intValue2 = intValue;
            intValue = intValue2;
        }
        return intValue < intValue2 || getFacePos(dBImage) != null;
    }

    public static String getWallpaperFilePath(DBImage dBImage) {
        String str = null;
        if (dBImage != null) {
            if (!dBImage.isVideoType()) {
                str = RequestCloudItem.getDownloadOriginalFilePath(dBImage);
            }
            return TextUtils.isEmpty(str) ? dBImage.getThumbnailFile() : str;
        }
        return null;
    }

    public static RectF getFacePos(DBImage dBImage) {
        DBAlbum albumByServerID;
        String peopleId;
        Cursor cursor = null;
        if (TextUtils.isEmpty(dBImage.getSha1())) {
            return null;
        }
        Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
        if (dBImage.isShareItem()) {
            DBShareAlbum dBShareAlbumBySharedId = com.miui.gallery.cloud.CloudUtils.getDBShareAlbumBySharedId(dBImage.getAlbumId());
            if (dBShareAlbumBySharedId != null) {
                peopleId = dBShareAlbumBySharedId.getPeopleId();
            }
            peopleId = null;
        } else {
            if (!TextUtils.isEmpty(dBImage.getLocalGroupId())) {
                albumByServerID = AlbumDataHelper.getAlbumById(sGetAndroidContext, dBImage.getLocalGroupId(), null);
            } else {
                albumByServerID = AlbumDataHelper.getAlbumByServerID(sGetAndroidContext, String.valueOf(dBImage.getGroupId()));
            }
            if (albumByServerID != null) {
                peopleId = albumByServerID.getPeopleId();
            }
            peopleId = null;
        }
        if (TextUtils.isEmpty(peopleId)) {
            return null;
        }
        try {
            Cursor queryJoinTable = FaceDataManager.queryJoinTable(new String[]{"peopleFace.faceXScale", "peopleFace.faceYScale", "peopleFace.faceWScale", "peopleFace.faceHScale"}, String.format(Locale.US, "%s.%s=%s.%s AND %s.%s=%s.%s  AND %s.%s=? AND %s.%s=? AND %s.%s=?", "peopleFace", "serverId", "faceToImages", "faceId", "faceToImages", "imageServerId", "cloud", "serverId", "peopleFace", nexExportFormat.TAG_FORMAT_TYPE, "peopleFace", "groupId", "cloud", "sha1"), new String[]{"FACE", peopleId, dBImage.getSha1()}, null, null);
            if (queryJoinTable != null) {
                try {
                    if (queryJoinTable.moveToNext()) {
                        float f = (float) queryJoinTable.getDouble(0);
                        float f2 = (float) queryJoinTable.getDouble(1);
                        RectF rectF = new RectF(f, f2, ((float) queryJoinTable.getDouble(2)) + f, ((float) queryJoinTable.getDouble(3)) + f2);
                        queryJoinTable.close();
                        return rectF;
                    }
                } catch (Throwable th) {
                    th = th;
                    cursor = queryJoinTable;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            }
            if (queryJoinTable != null) {
                queryJoinTable.close();
            }
            return null;
        } catch (Throwable th2) {
            th = th2;
        }
    }
}
