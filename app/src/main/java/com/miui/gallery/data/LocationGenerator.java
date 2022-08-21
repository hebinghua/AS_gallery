package com.miui.gallery.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.text.TextUtils;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.util.PackageUtils;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.xiaomi.stat.a.j;

/* loaded from: classes.dex */
public class LocationGenerator {
    public static LocationGenerator sInstance;
    public String[] CLOUD_PROJECTION = {j.c, "exifGPSLatitude", "exifGPSLatitudeRef", "exifGPSLongitude", "exifGPSLongitudeRef", "fileName"};
    public Future mFuture;

    public static synchronized LocationGenerator getInstance() {
        LocationGenerator locationGenerator;
        synchronized (LocationGenerator.class) {
            if (sInstance == null) {
                sInstance = new LocationGenerator();
            }
            locationGenerator = sInstance;
        }
        return locationGenerator;
    }

    public synchronized void generate(final Context context) {
        Future future = this.mFuture;
        if (future == null || future.isDone()) {
            this.mFuture = ThreadManager.getMiscPool().submit(new ThreadPool.Job<Void>() { // from class: com.miui.gallery.data.LocationGenerator.1
                @Override // com.miui.gallery.concurrent.ThreadPool.Job
                /* renamed from: run  reason: collision with other method in class */
                public Void mo1807run(ThreadPool.JobContext jobContext) {
                    SafeDBUtil.safeQuery(context, GalleryContract.Cloud.CLOUD_URI, LocationGenerator.this.CLOUD_PROJECTION, (String) null, (String[]) null, (String) null, new SafeDBUtil.QueryHandler<Void>() { // from class: com.miui.gallery.data.LocationGenerator.1.1
                        @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                        /* renamed from: handle  reason: collision with other method in class */
                        public Void mo1808handle(Cursor cursor) {
                            if (cursor != null) {
                                while (cursor.moveToNext()) {
                                    int i = cursor.getInt(0);
                                    String string = cursor.getString(1);
                                    String string2 = cursor.getString(2);
                                    String string3 = cursor.getString(3);
                                    String string4 = cursor.getString(4);
                                    String appNameForScreenshot = PackageUtils.getAppNameForScreenshot(cursor.getString(5));
                                    if (TextUtils.isEmpty(appNameForScreenshot)) {
                                        if (!TextUtils.isEmpty(string) && !TextUtils.isEmpty(string3)) {
                                            String decode = CitySearcher.getInstance().decode(new Coordinate(string, string2, string3, string4));
                                            if (LocationUtil.isLocationValidate(decode)) {
                                                appNameForScreenshot = LocationUtil.getCityNameFromRes(context, decode);
                                            }
                                        }
                                        appNameForScreenshot = null;
                                    }
                                    AnonymousClass1 anonymousClass1 = AnonymousClass1.this;
                                    SafeDBUtil.safeUpdate(context, GalleryContract.Cloud.CLOUD_URI, LocationGenerator.this.toValues(appNameForScreenshot), "_id=?", new String[]{String.valueOf(i)});
                                }
                            }
                            return null;
                        }
                    });
                    SafeDBUtil.safeQuery(context, GalleryContract.ShareImage.SHARE_URI, LocationGenerator.this.CLOUD_PROJECTION, (String) null, (String[]) null, (String) null, new SafeDBUtil.QueryHandler<Void>() { // from class: com.miui.gallery.data.LocationGenerator.1.2
                        @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                        /* renamed from: handle  reason: collision with other method in class */
                        public Void mo1808handle(Cursor cursor) {
                            if (cursor != null) {
                                while (cursor.moveToNext()) {
                                    int i = cursor.getInt(0);
                                    String string = cursor.getString(1);
                                    String string2 = cursor.getString(2);
                                    String string3 = cursor.getString(3);
                                    String string4 = cursor.getString(4);
                                    String appNameForScreenshot = PackageUtils.getAppNameForScreenshot(cursor.getString(5));
                                    if (TextUtils.isEmpty(appNameForScreenshot)) {
                                        if (!TextUtils.isEmpty(string) && !TextUtils.isEmpty(string3)) {
                                            String decode = CitySearcher.getInstance().decode(new Coordinate(string, string2, string3, string4));
                                            if (LocationUtil.isLocationValidate(decode)) {
                                                appNameForScreenshot = LocationUtil.getCityNameFromRes(context, decode);
                                            }
                                        }
                                        appNameForScreenshot = null;
                                    }
                                    AnonymousClass1 anonymousClass1 = AnonymousClass1.this;
                                    SafeDBUtil.safeUpdate(context, GalleryContract.ShareImage.SHARE_URI, LocationGenerator.this.toValues(appNameForScreenshot), "_id=?", new String[]{String.valueOf(i)});
                                }
                            }
                            return null;
                        }
                    });
                    LocationGenerator.this.release();
                    context.getContentResolver().notifyChange(GalleryContract.Media.URI, (ContentObserver) null, false);
                    return null;
                }
            });
        }
    }

    public final ContentValues toValues(String str) {
        ContentValues contentValues = new ContentValues();
        if (TextUtils.isEmpty(str)) {
            contentValues.putNull("location");
        } else {
            contentValues.put("location", str);
        }
        return contentValues;
    }

    public synchronized void release() {
        Future future = this.mFuture;
        if (future != null) {
            future.cancel();
        }
        sInstance = null;
    }
}
