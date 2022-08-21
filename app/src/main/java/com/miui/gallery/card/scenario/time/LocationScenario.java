package com.miui.gallery.card.scenario.time;

import android.database.Cursor;
import android.text.TextUtils;
import com.miui.gallery.card.scenario.DateUtils;
import com.miui.gallery.card.scenario.Record;
import com.miui.gallery.card.scenario.ScenarioConstants;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.data.LocationManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import com.xiaomi.stat.b;
import com.xiaomi.stat.b.h;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public abstract class LocationScenario extends TimeScenario {
    public static final String ALL_IMAGE_SELECTION = ScenarioConstants.MEDIA_SCENARIO_SELECTION + " AND location is not null";
    public static final String[] PROJECTION = {"DISTINCT sha1", j.c, "location", "mixedDateTime", "source_pkg"};
    public String mTargetCity;

    /* loaded from: classes.dex */
    public static class MediaItem {
        public String mCity;
        public long mDateTime;
        public long mId;
        public String mLocation;
        public String mSha1;
        public String mSourcePkg;
    }

    @Override // com.miui.gallery.card.scenario.time.TimeScenario, com.miui.gallery.card.scenario.Scenario
    public String getPeopleId() {
        return null;
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public List<Record> findRecords() {
        this.mTargetCity = null;
        return GalleryEntityManager.getInstance().query(Record.class, String.format("%s IN (%s) AND %s > %s", "scenarioId", TextUtils.join(",", new Integer[]{114, 201}), b.j, String.valueOf(DateUtils.getCurrentTimeMillis() - 15552000000L)), null, "time ASC", null);
    }

    @Override // com.miui.gallery.card.scenario.time.TimeScenario, com.miui.gallery.card.scenario.Scenario
    public List<Long> loadMediaItem() {
        return super.loadMediaItem();
    }

    @Override // com.miui.gallery.card.scenario.time.TimeScenario, com.miui.gallery.card.scenario.Scenario
    public String getLocation() {
        return this.mTargetCity;
    }

    public static List<MediaItem> getMediaItemsFromCursor(Cursor cursor) {
        ArrayList arrayList = new ArrayList();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                try {
                    MediaItem mediaItem = new MediaItem();
                    mediaItem.mSha1 = cursor.getString(0);
                    mediaItem.mId = cursor.getLong(1);
                    mediaItem.mLocation = cursor.getString(2);
                    mediaItem.mDateTime = cursor.getLong(3);
                    mediaItem.mSourcePkg = cursor.getString(4);
                    mediaItem.mCity = getCityNameFromLoaction(mediaItem.mLocation);
                    if (TextUtils.isEmpty(mediaItem.mSourcePkg) && !TextUtils.isEmpty(mediaItem.mCity)) {
                        arrayList.add(mediaItem);
                    }
                } catch (Throwable th) {
                    cursor.close();
                    throw th;
                }
            }
            cursor.close();
            DefaultLogger.d("LocationScenario", "cursor.size()=%d,getMediaItemsFromCursor(),resultIds.size()=%d", Integer.valueOf(cursor.getCount()), Integer.valueOf(arrayList.size()));
        }
        return arrayList;
    }

    public static String getCityNameFromLoaction(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        if (str.indexOf(h.g) == -1) {
            return str;
        }
        String[] segmentLocation = LocationManager.segmentLocation(str);
        if (segmentLocation != null) {
            if (!TextUtils.isEmpty(segmentLocation[2])) {
                return segmentLocation[2];
            }
            if (!TextUtils.isEmpty(segmentLocation[3])) {
                return segmentLocation[3];
            }
            if (!TextUtils.isEmpty(segmentLocation[1])) {
                return segmentLocation[1];
            }
            if (!TextUtils.isEmpty(segmentLocation[0])) {
                return segmentLocation[0];
            }
        }
        return "";
    }

    public String getDatePeriodFromRecord(Record record) {
        return DateUtils.getDatePeriodGraceful(getRecordStartTime(record), getRecordEndTime(record));
    }
}
