package com.miui.gallery.provider;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.epoxy.common.CollectionUtils;
import com.miui.gallery.dao.GalleryLiteEntityManager;
import com.miui.gallery.dao.base.EntityTransaction;
import com.miui.gallery.model.PeopleContactInfo;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.face.FaceRegionRectF;
import com.miui.gallery.util.face.PeopleCursorHelper;
import com.miui.gallery.util.face.PeopleItem;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.MiStat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class PeopleFaceSnapshotHelper {
    public static List<PeopleItem> cursor2Entities(Cursor cursor) {
        if (cursor != null && !cursor.isClosed() && cursor.getCount() > 0 && cursor.moveToFirst()) {
            ArrayList arrayList = new ArrayList(cursor.getCount());
            int i = 0;
            do {
                PeopleItem fromCursor = PeopleItem.fromCursor(cursor);
                if (fromCursor != null) {
                    arrayList.add(fromCursor);
                    if (PeopleContactInfo.isUnKnownRelation(fromCursor.getRelationType()) && TextUtils.isEmpty(fromCursor.getName())) {
                        i++;
                    }
                }
                if (!cursor.moveToNext()) {
                    break;
                }
            } while (i < 18);
            return arrayList;
        }
        return CollectionUtils.emptyList();
    }

    public static FaceRegionRectF getFaceRegionRectF(PeopleItem peopleItem) {
        if (peopleItem == null) {
            return new FaceRegionRectF(0.0f, 0.0f, 0.0f, 0.0f, 0);
        }
        return new FaceRegionRectF(peopleItem.getFaceXScale(), peopleItem.getFaceYScale(), peopleItem.getFaceXScale() + peopleItem.getFaceWScale(), peopleItem.getFaceYScale() + peopleItem.getFaceHScale(), peopleItem.getExifOrientation());
    }

    public static Uri getThumbnailDownloadUri(PeopleItem peopleItem) {
        return ContentUris.withAppendedId(GalleryContract.Cloud.CLOUD_URI, peopleItem.getCloudId());
    }

    public static String getThumbnailPath(PeopleItem peopleItem) {
        String thumbFile = peopleItem.getThumbFile();
        if (TextUtils.isEmpty(thumbFile)) {
            thumbFile = peopleItem.getLocalFile();
        }
        return TextUtils.isEmpty(thumbFile) ? peopleItem.getMicroThumbFile() : thumbFile;
    }

    public static DownloadType getThumbnailDownloadType(PeopleItem peopleItem) {
        return (!TextUtils.isEmpty(peopleItem.getThumbFile()) || !TextUtils.isEmpty(peopleItem.getLocalFile())) ? DownloadType.THUMBNAIL : DownloadType.MICRO;
    }

    public static void queryAndPersist(Context context) {
        DefaultLogger.d("PeopleFaceSnapshotHelper", "queryAndPersist on thread: %s", Thread.currentThread().getName());
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(GalleryContract.PeopleFace.PERSONS_URI, PeopleCursorHelper.PROJECTION, null, null, null);
            persist(cursor2Entities(cursor));
        } finally {
            BaseMiscUtil.closeSilently(cursor);
        }
    }

    public static synchronized int persist(List<PeopleItem> list) {
        int i;
        synchronized (PeopleFaceSnapshotHelper.class) {
            i = 0;
            if (BaseMiscUtil.isValid(list)) {
                long currentTimeMillis = System.currentTimeMillis();
                EntityTransaction transaction = GalleryLiteEntityManager.getInstance().getTransaction();
                transaction.begin();
                GalleryLiteEntityManager.getInstance().deleteAll(PeopleItem.class);
                Iterator<PeopleItem> it = list.iterator();
                int i2 = 0;
                while (it.hasNext()) {
                    i2 += -1 != GalleryLiteEntityManager.getInstance().insert(it.next()) ? 1 : 0;
                }
                transaction.commit();
                transaction.end();
                long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
                DefaultLogger.d("PeopleFaceSnapshotHelper", "save people count %d, cost %d", Integer.valueOf(i2), Long.valueOf(currentTimeMillis2));
                HashMap hashMap = new HashMap();
                hashMap.put("cost_time", String.valueOf(currentTimeMillis2));
                hashMap.put(MiStat.Param.COUNT, String.valueOf(i2));
                SamplingStatHelper.recordCountEvent("people", "people_snapshot_save_time", hashMap);
                i = i2;
            } else {
                GalleryLiteEntityManager.getInstance().deleteAll(PeopleItem.class);
            }
        }
        return i;
    }
}
