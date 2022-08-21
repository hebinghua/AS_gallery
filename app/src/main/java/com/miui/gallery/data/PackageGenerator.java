package com.miui.gallery.data;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.util.PackageUtils;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.xiaomi.stat.a.j;
import java.util.Locale;

/* loaded from: classes.dex */
public class PackageGenerator {
    public static Future mFuture;
    public static volatile PackageGenerator sGenerator;
    public static String[] PROJECTION = {j.c, "fileName"};
    public static final String SELECTION = String.format(Locale.US, "%s like ? ", "fileName");
    public static final String[] SCREENSHOT_PATTERN = {"Screenshot%"};

    public static /* synthetic */ Object $r8$lambda$z3zkHw0S_HiyrGNY_ioYzKL2ass(PackageGenerator packageGenerator, ThreadPool.JobContext jobContext) {
        return packageGenerator.lambda$generate$0(jobContext);
    }

    public static PackageGenerator getInstance() {
        if (sGenerator == null) {
            synchronized (PackageGenerator.class) {
                sGenerator = new PackageGenerator();
            }
        }
        return sGenerator;
    }

    public synchronized void generate() {
        if (GalleryPreferences.DataBase.getEverUpgradeDBForScreenshots()) {
            return;
        }
        Future future = mFuture;
        if (future == null || future.isDone()) {
            mFuture = ThreadManager.getMiscPool().submit(new ThreadPool.Job() { // from class: com.miui.gallery.data.PackageGenerator$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.concurrent.ThreadPool.Job
                /* renamed from: run */
                public final Object mo1807run(ThreadPool.JobContext jobContext) {
                    return PackageGenerator.$r8$lambda$z3zkHw0S_HiyrGNY_ioYzKL2ass(PackageGenerator.this, jobContext);
                }
            });
        }
    }

    public /* synthetic */ Object lambda$generate$0(ThreadPool.JobContext jobContext) {
        SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.Cloud.CLOUD_URI, PROJECTION, SELECTION, SCREENSHOT_PATTERN, (String) null, new SafeDBUtil.QueryHandler<Void>() { // from class: com.miui.gallery.data.PackageGenerator.1
            {
                PackageGenerator.this = this;
            }

            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle */
            public Void mo1808handle(Cursor cursor) {
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        int i = cursor.getInt(0);
                        String gePackageNameForScreenshot = PackageUtils.gePackageNameForScreenshot(cursor.getString(1));
                        if (!TextUtils.isEmpty(gePackageNameForScreenshot)) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("source_pkg", gePackageNameForScreenshot);
                            SafeDBUtil.safeUpdate(GalleryApp.sGetAndroidContext(), GalleryContract.Cloud.CLOUD_URI, contentValues, "_id=?", new String[]{String.valueOf(i)});
                        }
                    }
                }
                GalleryPreferences.DataBase.setUpgradeDBForScreenshots();
                return null;
            }
        });
        GalleryApp.sGetAndroidContext().getContentResolver().notifyChange(GalleryContract.Media.URI, (ContentObserver) null, false);
        return null;
    }
}
