package com.miui.gallery.scanner.core.task.convertor.internal.base;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.provider.MediaStore;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.scanner.core.ScanContracts$StatusReason;
import com.miui.gallery.scanner.core.task.BaseScanTaskStateListener;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.core.task.ScanTaskConfigFactory;
import com.miui.gallery.scanner.core.task.semi.ImprintedScanPathsTask;
import com.miui.gallery.scanner.core.task.semi.ScanPathsTask;
import com.miui.gallery.scanner.core.task.semi.SemiScanTask;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.UriUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.Locale;

/* loaded from: classes2.dex */
public class MediaProviderScanner implements IScanner {
    public static final String FIND_NEW_MEDIA_ORDER_BY;
    public static final String[] FIND_NEW_MEDIA_PROJECTION;
    public static final boolean IS_SUPPORT_GENERATION_MODIFIED;

    static {
        String[] strArr;
        boolean z = Build.VERSION.SDK_INT > 29;
        IS_SUPPORT_GENERATION_MODIFIED = z;
        if (z) {
            strArr = new String[]{"_data", "generation_modified"};
        } else {
            strArr = new String[]{"_data"};
        }
        FIND_NEW_MEDIA_PROJECTION = strArr;
        FIND_NEW_MEDIA_ORDER_BY = z ? "generation_modified DESC " : "date_modified DESC ";
    }

    @Override // com.miui.gallery.scanner.core.task.convertor.internal.base.IScanner
    public SemiScanTask[] createTasks(Context context) {
        long currentTimeMillis = System.currentTimeMillis();
        return new SemiScanTask[]{scanMediaProviderImages(context, currentTimeMillis, ScanTaskConfigFactory.get(3)), scanMediaProviderVideos(context, currentTimeMillis, ScanTaskConfigFactory.get(4))};
    }

    public static SemiScanTask scanMediaProviderImages(Context context, final long j, ScanTaskConfig scanTaskConfig) {
        String format;
        ScanPathsTask create;
        long lastImagesScanTime = GalleryPreferences.MediaScanner.getLastImagesScanTime();
        long lastImagesGeneration = GalleryPreferences.MediaScanner.getLastImagesGeneration();
        if (IS_SUPPORT_GENERATION_MODIFIED) {
            format = String.format(Locale.US, "date_added >= %s / 1000 or generation_modified > %s ", String.valueOf(lastImagesScanTime), String.valueOf(lastImagesGeneration));
        } else {
            format = String.format(Locale.US, "date_added >= %s / 1000", String.valueOf(lastImagesScanTime));
        }
        String str = format;
        DefaultLogger.fd("MediaProviderScanner", "  findNewMediaWhere is: " + str);
        ArrayList arrayList = (ArrayList) SafeDBUtil.safeQuery(context, UriUtil.appendLimit(MediaStore.Images.Media.getContentUri("external"), lastImagesScanTime == 0 ? Integer.MAX_VALUE : 2000), FIND_NEW_MEDIA_PROJECTION, str, (String[]) null, FIND_NEW_MEDIA_ORDER_BY, MediaProviderScanner$$ExternalSyntheticLambda0.INSTANCE);
        if (arrayList == null || arrayList.size() <= 0 || (create = ImprintedScanPathsTask.create(context, arrayList, new ScanTaskConfig.Builder().cloneFrom(scanTaskConfig).build(), "scanMediaProviderImages")) == null) {
            return null;
        }
        create.addStateListener(new BaseScanTaskStateListener<ScanPathsTask>() { // from class: com.miui.gallery.scanner.core.task.convertor.internal.base.MediaProviderScanner.1
            @Override // com.miui.gallery.scanner.core.task.BaseScanTaskStateListener
            public void onDone(ScanPathsTask scanPathsTask, ScanContracts$StatusReason scanContracts$StatusReason) {
                GalleryPreferences.MediaScanner.setLastImagesScanTime(j);
            }
        });
        return create;
    }

    public static /* synthetic */ ArrayList lambda$scanMediaProviderImages$0(Cursor cursor) {
        StringBuilder sb = new StringBuilder();
        sb.append("Imagescursor Count is: ");
        sb.append(cursor != null ? cursor.getCount() : 0);
        DefaultLogger.fd("MediaProviderScanner", sb.toString());
        if (cursor == null || cursor.getCount() <= 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList(cursor.getCount());
        long j = 0;
        while (cursor.moveToNext()) {
            arrayList.add(cursor.getString(0));
            if (IS_SUPPORT_GENERATION_MODIFIED && j < cursor.getLong(1)) {
                j = cursor.getLong(1);
            }
        }
        GalleryPreferences.MediaScanner.setLastImagesGeneration(j);
        return arrayList;
    }

    public static SemiScanTask scanMediaProviderVideos(Context context, final long j, ScanTaskConfig scanTaskConfig) {
        String format;
        ScanPathsTask create;
        long lastVideosScanTime = GalleryPreferences.MediaScanner.getLastVideosScanTime();
        long lastVideosGeneration = GalleryPreferences.MediaScanner.getLastVideosGeneration();
        if (IS_SUPPORT_GENERATION_MODIFIED) {
            format = String.format(Locale.US, "date_added >= %s / 1000 or generation_modified > %s ", String.valueOf(lastVideosScanTime), String.valueOf(lastVideosGeneration));
        } else {
            format = String.format(Locale.US, "date_added >= %s / 1000", String.valueOf(lastVideosScanTime));
        }
        String str = format;
        DefaultLogger.fd("MediaProviderScanner", "  findNewMediaWhere is: " + str);
        ArrayList arrayList = (ArrayList) SafeDBUtil.safeQuery(context, UriUtil.appendLimit(MediaStore.Video.Media.getContentUri("external"), lastVideosScanTime == 0 ? Integer.MAX_VALUE : 2000), FIND_NEW_MEDIA_PROJECTION, str, (String[]) null, FIND_NEW_MEDIA_ORDER_BY, MediaProviderScanner$$ExternalSyntheticLambda1.INSTANCE);
        if (arrayList == null || arrayList.size() <= 0 || (create = ImprintedScanPathsTask.create(context, arrayList, new ScanTaskConfig.Builder().cloneFrom(scanTaskConfig).build(), "scanMediaProviderVideos")) == null) {
            return null;
        }
        create.addStateListener(new BaseScanTaskStateListener<ScanPathsTask>() { // from class: com.miui.gallery.scanner.core.task.convertor.internal.base.MediaProviderScanner.2
            @Override // com.miui.gallery.scanner.core.task.BaseScanTaskStateListener
            public void onDone(ScanPathsTask scanPathsTask, ScanContracts$StatusReason scanContracts$StatusReason) {
                GalleryPreferences.MediaScanner.setLastVideosScanTime(j);
            }
        });
        return create;
    }

    public static /* synthetic */ ArrayList lambda$scanMediaProviderVideos$1(Cursor cursor) {
        StringBuilder sb = new StringBuilder();
        sb.append("Videocursor Count is: ");
        sb.append(cursor != null ? cursor.getCount() : 0);
        DefaultLogger.fd("MediaProviderScanner", sb.toString());
        if (cursor == null || cursor.getCount() <= 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList(cursor.getCount());
        long j = 0;
        while (cursor.moveToNext()) {
            arrayList.add(cursor.getString(0));
            if (IS_SUPPORT_GENERATION_MODIFIED && j < cursor.getLong(1)) {
                j = cursor.getLong(1);
            }
        }
        GalleryPreferences.MediaScanner.setLastVideosGeneration(j);
        return arrayList;
    }
}
