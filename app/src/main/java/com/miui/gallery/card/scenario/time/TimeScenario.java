package com.miui.gallery.card.scenario.time;

import android.database.Cursor;
import android.text.TextUtils;
import ch.qos.logback.core.net.SyslogConstants;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.assistant.jni.filter.BaiduSceneResult;
import com.miui.gallery.assistant.model.FaceInfo;
import com.miui.gallery.assistant.model.MediaScene;
import com.miui.gallery.assistant.model.PeopleEvent;
import com.miui.gallery.card.Card;
import com.miui.gallery.card.scenario.DateUtils;
import com.miui.gallery.card.scenario.Record;
import com.miui.gallery.card.scenario.Scenario;
import com.miui.gallery.card.scenario.ScenarioConstants;
import com.miui.gallery.card.scenario.SceneTagQuery;
import com.miui.gallery.card.scenario.time.LocationScenario;
import com.miui.gallery.cloudcontrol.strategies.AssistantScenarioStrategy;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.dao.base.Entity;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* loaded from: classes.dex */
public abstract class TimeScenario extends Scenario {
    public static final String IMAGE_LOCATION_SELECTION;
    public static final String MEDIA_IDS_SELECTION;
    public static final String[] PROJECTION;
    public static final String TIME_BY_MEDIA_IDS_SELECTION;
    public static final String TIME_SELECTION;
    public long mEndTime;
    public long mStartTime;
    public long mTargetTime;

    /* renamed from: $r8$lambda$AOeso1smhI-eqsn7Qym0VIfe3gM */
    public static /* synthetic */ boolean m644$r8$lambda$AOeso1smhIeqsn7Qym0VIfe3gM(List list, Long l) {
        return lambda$removeCertificateIdsFromMediaIds$3(list, l);
    }

    public static /* synthetic */ boolean $r8$lambda$WJ8HBCqqQZwbZAAf6klltCqZOvM(List list, Long l) {
        return list.contains(l);
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public String getLocation() {
        return null;
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public String getPeopleId() {
        return null;
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public String getPrimaryKey() {
        return null;
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public String getSecondary() {
        return null;
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public String getTertiaryKey() {
        return null;
    }

    static {
        StringBuilder sb = new StringBuilder();
        String str = ScenarioConstants.MEDIA_SCENARIO_SELECTION;
        sb.append(str);
        sb.append(" AND ");
        sb.append("mixedDateTime");
        sb.append(" > %s AND ");
        sb.append("mixedDateTime");
        sb.append(" < %s");
        String sb2 = sb.toString();
        TIME_SELECTION = sb2;
        MEDIA_IDS_SELECTION = str + " AND " + j.c + " IN (%s)";
        TIME_BY_MEDIA_IDS_SELECTION = sb2 + " AND " + j.c + " IN (%s)";
        IMAGE_LOCATION_SELECTION = str + " AND location is not null AND mixedDateTime > %s AND mixedDateTime < %s";
        PROJECTION = new String[]{"cloud._id", "cloud.mixedDateTime"};
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public void onFillScenarioRule(AssistantScenarioStrategy.ScenarioRule scenarioRule) {
        if (scenarioRule != null) {
            this.mScenarioId = scenarioRule.getScenarioId();
            this.mHolidayId = scenarioRule.getHolidayId();
            this.mMinImageCount = scenarioRule.getMinImageCount();
            this.mMaxImageCount = scenarioRule.getMaxImageCount();
            this.mMinSelectedImageCount = scenarioRule.getMinSelectedImageCount();
            this.mMaxSelectedImageCount = scenarioRule.getMaxSelectedImageCount();
            this.mTagIdList = SceneTagQuery.getInstance().queryAllTagIdsByKIds(scenarioRule.getKnowledgeIds());
            int timeType = getTimeType();
            if (timeType == 1000) {
                this.mPriority = 200;
            } else if (timeType == 1100) {
                this.mPriority = 190;
            } else if (timeType == 1400) {
                this.mPriority = 170;
            } else if (timeType == 1500) {
                this.mPriority = BaiduSceneResult.VISA;
            } else if (timeType == 1700) {
                this.mPriority = 150;
            } else if (timeType == 1800) {
                this.mPriority = 110;
            } else if (timeType == 1900) {
                this.mPriority = SyslogConstants.LOG_LOCAL4;
            } else if (timeType == 2000) {
                this.mPriority = 120;
            } else if (timeType == 2100) {
                this.mPriority = 179;
            } else if (timeType != 2200) {
            } else {
                this.mPriority = BaiduSceneResult.FASHION_OTHER;
            }
        }
    }

    public void setStartTime(long j) {
        this.mStartTime = j;
    }

    public void setEndTime(long j) {
        this.mEndTime = j;
    }

    public void setTargetTime(long j) {
        this.mTargetTime = j;
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public List<Long> loadMediaItem() {
        return getMediaIdsByStartEndTime(this.mStartTime, this.mEndTime);
    }

    public List<Long> getMediaIdsByStartEndTime(long j, long j2) {
        List<Long> list = (List) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.Cloud.CLOUD_URI, Scenario.BASE_PROJECTION, String.format(Locale.US, TIME_SELECTION, Long.valueOf(j), Long.valueOf(j2)), (String[]) null, (String) null, new SafeDBUtil.QueryHandler<List<Long>>() { // from class: com.miui.gallery.card.scenario.time.TimeScenario.1
            {
                TimeScenario.this = this;
            }

            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle */
            public List<Long> mo1808handle(Cursor cursor) {
                return TimeScenario.this.getMediaIdsFromCursor(cursor);
            }
        });
        if (list != null) {
            DefaultLogger.d(this.TAG, "| Recommendation |Scenario.getId()=%d,getMediaIdsByStartEndTime(),startTime =%s,endTime=%s,resultIds.size()=%d,resultIds is %s", Integer.valueOf(this.mScenarioId), Long.valueOf(j), Long.valueOf(j2), Integer.valueOf(list.size()), TextUtils.join(",", list));
        }
        return list;
    }

    public List<MediaItem> getMediaItemsByMediaIds(List<Long> list) {
        if (!BaseMiscUtil.isValid(list)) {
            return Collections.emptyList();
        }
        List<MediaItem> list2 = (List) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.Cloud.CLOUD_URI, PROJECTION, String.format(Locale.US, MEDIA_IDS_SELECTION, TextUtils.join(",", list)), (String[]) null, "mixedDateTime asc", new SafeDBUtil.QueryHandler<List<MediaItem>>() { // from class: com.miui.gallery.card.scenario.time.TimeScenario.2
            {
                TimeScenario.this = this;
            }

            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle */
            public List<MediaItem> mo1808handle(Cursor cursor) {
                if (cursor == null || cursor.getCount() == 0) {
                    return Collections.emptyList();
                }
                ArrayList arrayList = new ArrayList(cursor.getCount());
                while (cursor.moveToNext()) {
                    MediaItem mediaItem = new MediaItem();
                    mediaItem.mId = cursor.getLong(0);
                    mediaItem.mDateTime = cursor.getLong(1);
                    arrayList.add(mediaItem);
                }
                return arrayList;
            }
        });
        if (list2 != null) {
            DefaultLogger.d(this.TAG, "| Recommendation |Scenario.getId()=%d,getMediaItemsByMediaIds(),resultItems.size()=%d,resultItems is %s", Integer.valueOf(this.mScenarioId), Integer.valueOf(list2.size()), TextUtils.join(",", list2));
        }
        return list2;
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public long getStartTime() {
        return this.mStartTime;
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public long getEndTime() {
        return this.mEndTime;
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public long getTargetTime() {
        return this.mTargetTime;
    }

    public long getRecordStartTime(Record record) {
        if (record != null) {
            return record.getStartTime();
        }
        return -1L;
    }

    public long getRecordEndTime(Record record) {
        if (record != null) {
            return record.getEndTime();
        }
        return -1L;
    }

    public int getTimeType() {
        return (this.mScenarioId / 100) * 100;
    }

    public int getScenarioType() {
        return this.mScenarioId % 100;
    }

    public List<Long> getMediaIdsByStartEndTimeTags(List<Integer> list, long j, long j2) {
        return getMediaIdsByStartEndTimeAndMediaIds(j, j2, getMediaIdsByTags(list));
    }

    public List<Long> getTopNumPeopleIdsByTime(long j, long j2, int i) {
        List<Long> columnFromCursor = getColumnFromCursor(GalleryEntityManager.getInstance().rawQuery(FaceInfo.class, new String[]{"COUNT(DISTINCT mediaId) NUM", "groupId"}, String.format("mediaId >0  AND version = 1 AND faceId != -1 AND mediaId IN (%s) ", TextUtils.join(",", getMediaIdsByStartEndTime(j, j2))), null, "groupId", null, "NUM DESC", String.format(Locale.US, "%s,%s", 0, Integer.valueOf(i))), "groupId");
        DefaultLogger.d(this.TAG, "| Recommendation |Scenario.getId()=%d,getTopNumPeopleIdsByTime(),peopleId=%s", Integer.valueOf(this.mScenarioId), columnFromCursor);
        return columnFromCursor;
    }

    public List<Long> getMediaIdsByStartEndTimeAndAge(long j, long j2, int i, int i2) {
        List<Long> mediaIdsByStartEndTime = getMediaIdsByStartEndTime(j, j2);
        if (!BaseMiscUtil.isValid(mediaIdsByStartEndTime)) {
            return Collections.emptyList();
        }
        List<Long> columnFromCursor = getColumnFromCursor(GalleryEntityManager.getInstance().rawQuery(FaceInfo.class, new String[]{"DISTINCT mediaId"}, String.format("mediaId >0  AND version = 1 AND faceId != -1 AND age >=?  AND age <=?  AND mediaId IN (%s) ", TextUtils.join(",", mediaIdsByStartEndTime)), new String[]{Integer.toString(i), Integer.toString(i2)}, null, null, null, null), "mediaId");
        if (columnFromCursor != null) {
            DefaultLogger.d(this.TAG, "| Recommendation |Scenario.getId()=%d,getMediaIdsByStartEndTimeAndAge(),mediaIds=%s", Integer.valueOf(this.mScenarioId), columnFromCursor);
        }
        return columnFromCursor;
    }

    public List<Long> getMediaIdsByTimeGroupPeopleIds(int i, int i2, long j, long j2, List<Long> list) {
        if (list == null || list.size() <= 1) {
            return Collections.emptyList();
        }
        List<Long> distinctMediaIds = distinctMediaIds(getIdsFromPeopleEventByTargetGroupPeople(getPeopleEventsByPeopleCount(i, i2), list));
        removeCertificateIdsFromMediaIds(distinctMediaIds);
        if (distinctMediaIds != null) {
            DefaultLogger.d(this.TAG, "| Recommendation |Scenario.getId()=%d,getMediaIdsByTimePeopleCountPeopleIds(), finally remove certificateIds selectMedias.size()=%d", Integer.valueOf(this.mScenarioId), Integer.valueOf(distinctMediaIds.size()));
        }
        return getMediaIdsByStartEndTimeAndMediaIds(j, j2, distinctMediaIds);
    }

    public List<Long> getIdsFromPeopleEventByTargetGroupPeople(List<PeopleEvent> list, List<Long> list2) {
        ArrayList arrayList;
        if (BaseMiscUtil.isValid(list)) {
            int size = list2.size();
            arrayList = new ArrayList();
            for (PeopleEvent peopleEvent : list) {
                String[] split = peopleEvent.getPeopleList().split(",");
                ArrayList arrayList2 = new ArrayList(split.length);
                Collections.addAll(arrayList2, split);
                if (arrayList2.contains(String.valueOf(list2.get(0)))) {
                    if (arrayList2.contains(String.valueOf(list2.get(1)))) {
                        arrayList.add(Long.valueOf(peopleEvent.getMediaId()));
                    } else if (size == 3 && arrayList2.contains(String.valueOf(list2.get(2)))) {
                        arrayList.add(Long.valueOf(peopleEvent.getMediaId()));
                    }
                }
            }
        } else {
            arrayList = null;
        }
        if (arrayList != null) {
            DefaultLogger.d(this.TAG, "| Recommendation |Scenario.getId()=%d,getIdsFromPeopleEventByTargetGroupPeople,selectMedias.size()=%d", Integer.valueOf(this.mScenarioId), Integer.valueOf(arrayList.size()));
        }
        return arrayList;
    }

    public List<Long> getMediaIdsByStartEndTimeAndMediaIds(long j, long j2, List<Long> list) {
        if (BaseMiscUtil.isValid(list)) {
            List<Long> list2 = (List) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.Cloud.CLOUD_URI, Scenario.BASE_PROJECTION, String.format(Locale.US, TIME_BY_MEDIA_IDS_SELECTION, Long.valueOf(j), Long.valueOf(j2), TextUtils.join(",", list)), (String[]) null, "mixedDateTime asc", new SafeDBUtil.QueryHandler<List<Long>>() { // from class: com.miui.gallery.card.scenario.time.TimeScenario.3
                {
                    TimeScenario.this = this;
                }

                @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                /* renamed from: handle */
                public List<Long> mo1808handle(Cursor cursor) {
                    return TimeScenario.this.getMediaIdsFromCursor(cursor);
                }
            });
            if (list2 != null) {
                DefaultLogger.d(this.TAG, "| Recommendation |Scenario.getId()=%d,getMediaIdsByStartEndTimeAndMediaIds(),startTime =%s,endTime=%s,resultIds.toString is %s,resultIds.size()=%d", Integer.valueOf(this.mScenarioId), Long.valueOf(j), Long.valueOf(j2), TextUtils.join(",", list2), Integer.valueOf(list2.size()));
            }
            return list2;
        }
        return null;
    }

    public List<Long> getMediaIdsByTags(List<Integer> list) {
        Cursor rawQuery = GalleryEntityManager.getInstance().rawQuery(MediaScene.class, new String[]{"mediaId", "sceneTag", "confidence"}, String.format("mediaId > 0 AND version = 1 AND sceneTag != -1 AND sceneTag IN (%s)", TextUtils.join(",", list)), null, null, null, null, null);
        ArrayList arrayList = new ArrayList();
        if (rawQuery != null) {
            while (rawQuery.moveToNext()) {
                try {
                    if (Entity.getFloat(rawQuery, "confidence") >= SceneTagQuery.getInstance().queryThresholdByTagId(Entity.getInt(rawQuery, "sceneTag"))) {
                        arrayList.add(Long.valueOf(Entity.getLong(rawQuery, "mediaId")));
                    }
                } finally {
                    BaseMiscUtil.closeSilently(rawQuery);
                }
            }
        }
        List<Long> distinctMediaIds = distinctMediaIds(arrayList);
        if (distinctMediaIds != null) {
            DefaultLogger.d(this.TAG, "| Recommendation |Scenario.getId()=%d,getMediaIdsByTags(),resultIds.size()=%d", Integer.valueOf(this.mScenarioId), Integer.valueOf(distinctMediaIds.size()));
        }
        return distinctMediaIds;
    }

    public List<Long> getMediaIdsByTagsAndMediaIds(List<Integer> list, List<Long> list2, String str) {
        if (!BaseMiscUtil.isValid(list2) || !BaseMiscUtil.isValid(list)) {
            return null;
        }
        Cursor rawQuery = GalleryEntityManager.getInstance().rawQuery(MediaScene.class, new String[]{"mediaId", "sceneTag", "confidence"}, String.format(str, TextUtils.join(",", list), TextUtils.join(",", list2)), null, null, null, null, null);
        ArrayList arrayList = new ArrayList();
        if (rawQuery != null) {
            while (rawQuery.moveToNext()) {
                try {
                    if (Entity.getFloat(rawQuery, "confidence") >= SceneTagQuery.getInstance().queryThresholdByTagId(Entity.getInt(rawQuery, "sceneTag"))) {
                        arrayList.add(Long.valueOf(Entity.getLong(rawQuery, "mediaId")));
                    }
                } finally {
                    BaseMiscUtil.closeSilently(rawQuery);
                }
            }
        }
        List<Long> distinctMediaIds = distinctMediaIds(arrayList);
        if (distinctMediaIds != null) {
            DefaultLogger.d(this.TAG, "| Recommendation |Scenario.getId()=%d,getMediaIdsByTagsAndMediaIds(),resultIds.size()=%d", Integer.valueOf(this.mScenarioId), Integer.valueOf(distinctMediaIds.size()));
        }
        return distinctMediaIds;
    }

    public List<Long> getMediaIdsByAges(int i, int i2) {
        List<Long> columnFromCursor = getColumnFromCursor(GalleryEntityManager.getInstance().rawQuery(FaceInfo.class, new String[]{"DISTINCT mediaId"}, "mediaId >0  AND version = 1 AND faceId != -1 AND age >=?  AND age <=? ", new String[]{Integer.toString(i), Integer.toString(i2)}, null, null, null, null), "mediaId");
        if (columnFromCursor != null) {
            DefaultLogger.d(this.TAG, "| Recommendation |Scenario.getId()=%d,getMediaIdsByAges(),resultIds.size()=%d", Integer.valueOf(this.mScenarioId), Integer.valueOf(columnFromCursor.size()));
        }
        return columnFromCursor;
    }

    public List<PeopleEvent> getPeopleEventsByPeopleCount(int i, int i2) {
        List<PeopleEvent> query = GalleryEntityManager.getInstance().query(PeopleEvent.class, "mediaId >0  AND peopleCount >=?  AND peopleCount <=? ", new String[]{Integer.toString(i), Integer.toString(i2)}, null, null);
        if (query != null) {
            DefaultLogger.d(this.TAG, "| Recommendation |Scenario.getId()=%d,getPeopleEventsByPeopleCount(),resultIds.size()=%d", Integer.valueOf(this.mScenarioId), Integer.valueOf(query.size()));
        }
        return query;
    }

    public List<Long> getTravelMediaIdsByStartEndTimeAndTag(long j, long j2, List<Integer> list) {
        if (!BaseMiscUtil.isValid(list)) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        List list2 = (List) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.Cloud.CLOUD_URI, LocationScenario.PROJECTION, String.format(Locale.US, IMAGE_LOCATION_SELECTION, Long.valueOf(j), Long.valueOf(j2)), (String[]) null, "mixedDateTime ASC", new SafeDBUtil.QueryHandler<List<LocationScenario.MediaItem>>() { // from class: com.miui.gallery.card.scenario.time.TimeScenario.5
            {
                TimeScenario.this = this;
            }

            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle */
            public List<LocationScenario.MediaItem> mo1808handle(Cursor cursor) {
                DefaultLogger.d(TimeScenario.this.TAG, "| Recommendation |Scenario.getId()=%d,startTime=%s,endTime=%s", Integer.valueOf(TimeScenario.this.mScenarioId), Long.valueOf(TimeScenario.this.getStartTime()), Long.valueOf(TimeScenario.this.getEndTime()));
                return LocationScenario.getMediaItemsFromCursor(cursor);
            }
        });
        List<Long> list3 = null;
        if (BaseMiscUtil.isValid(list2)) {
            final LocationScenario.MediaItem mediaItem = null;
            for (int i = 0; i < list2.size(); i++) {
                LocationScenario.MediaItem mediaItem2 = (LocationScenario.MediaItem) list2.get(i);
                if (mediaItem != null) {
                    if (!TextUtils.equals(mediaItem.mCity, mediaItem2.mCity)) {
                        final LocationScenario.MediaItem mediaItem3 = (LocationScenario.MediaItem) list2.get(i - 1);
                        List list4 = (List) list2.stream().filter(new Predicate() { // from class: com.miui.gallery.card.scenario.time.TimeScenario$$ExternalSyntheticLambda1
                            @Override // java.util.function.Predicate
                            public final boolean test(Object obj) {
                                boolean lambda$getTravelMediaIdsByStartEndTimeAndTag$0;
                                lambda$getTravelMediaIdsByStartEndTimeAndTag$0 = TimeScenario.lambda$getTravelMediaIdsByStartEndTimeAndTag$0(LocationScenario.MediaItem.this, mediaItem3, (LocationScenario.MediaItem) obj);
                                return lambda$getTravelMediaIdsByStartEndTimeAndTag$0;
                            }
                        }).map(TimeScenario$$ExternalSyntheticLambda0.INSTANCE).collect(Collectors.toList());
                        if (mediaItem3.mDateTime - mediaItem.mDateTime <= 864000000 && BaseMiscUtil.isValid(list4)) {
                            arrayList.addAll(list4);
                        }
                    }
                }
                mediaItem = mediaItem2;
            }
        }
        DefaultLogger.d(this.TAG, "| Recommendation |Scenario.getId()=%d,allTravelImages.size()=%s", Integer.valueOf(this.mScenarioId), Integer.valueOf(arrayList.size()));
        final List<Long> distinctMediaIds = distinctMediaIds(getMediaIdsByTags(list));
        if (BaseMiscUtil.isValid(distinctMediaIds)) {
            list3 = (List) arrayList.stream().filter(new Predicate() { // from class: com.miui.gallery.card.scenario.time.TimeScenario$$ExternalSyntheticLambda3
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return TimeScenario.$r8$lambda$WJ8HBCqqQZwbZAAf6klltCqZOvM(distinctMediaIds, (Long) obj);
                }
            }).collect(Collectors.toList());
        }
        if (list3 != null) {
            DefaultLogger.d(this.TAG, "| Recommendation |Scenario.getId()=%d,getTravelMediaIdsByStartEndTimeAndTag():select tags from resultTravelImages.size()=%s", Integer.valueOf(this.mScenarioId), Integer.valueOf(list3.size()));
        }
        return list3;
    }

    public static /* synthetic */ boolean lambda$getTravelMediaIdsByStartEndTimeAndTag$0(LocationScenario.MediaItem mediaItem, LocationScenario.MediaItem mediaItem2, LocationScenario.MediaItem mediaItem3) {
        long j = mediaItem3.mDateTime;
        return j >= mediaItem.mDateTime && j <= mediaItem2.mDateTime;
    }

    public static /* synthetic */ Long lambda$getTravelMediaIdsByStartEndTimeAndTag$1(LocationScenario.MediaItem mediaItem) {
        return Long.valueOf(mediaItem.mId);
    }

    public void removeCertificateIdsFromMediaIds(List<Long> list) {
        if (BaseMiscUtil.isValid(list)) {
            final List<Long> mediaIdsByTagsAndMediaIds = getMediaIdsByTagsAndMediaIds(SceneTagQuery.getInstance().getCertificateTags(), list, "mediaId > 0 AND version = 1 AND sceneTag != -1 AND mediaType = 0 AND sceneTag IN (%s) AND mediaId IN (%s)");
            if (BaseMiscUtil.isValid(mediaIdsByTagsAndMediaIds)) {
                list = (List) list.stream().filter(new Predicate() { // from class: com.miui.gallery.card.scenario.time.TimeScenario$$ExternalSyntheticLambda2
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return TimeScenario.m644$r8$lambda$AOeso1smhIeqsn7Qym0VIfe3gM(mediaIdsByTagsAndMediaIds, (Long) obj);
                    }
                }).collect(Collectors.toList());
            }
            if (!BaseMiscUtil.isValid(list)) {
                return;
            }
            DefaultLogger.d(this.TAG, "| Recommendation |Scenario.getId()=%d,removeCertificateIdsFromMediaIds, finally remove certificateIds selectMedias.size()=%d", Integer.valueOf(this.mScenarioId), Integer.valueOf(list.size()));
        }
    }

    public static /* synthetic */ boolean lambda$removeCertificateIdsFromMediaIds$3(List list, Long l) {
        return !list.contains(l);
    }

    public List<Long> distinctMediaIds(List<Long> list) {
        if (BaseMiscUtil.isValid(list)) {
            return (List) list.stream().distinct().collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public List<Long> getColumnFromCursor(Cursor cursor, String str) {
        ArrayList arrayList = new ArrayList();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                try {
                    arrayList.add(Long.valueOf(Entity.getLong(cursor, str)));
                } finally {
                    cursor.close();
                }
            }
        }
        return arrayList;
    }

    public List<Long> getRecordStartTimesFromRecordAndCards(List<Record> list, List<Card> list2, boolean z) {
        ArrayList arrayList = new ArrayList();
        if (BaseMiscUtil.isValid(list)) {
            for (Record record : list) {
                arrayList.add(Long.valueOf(getRecordStartTime(record)));
            }
        }
        if (BaseMiscUtil.isValid(list2)) {
            for (Card card : list2) {
                long recordStartTime = card.getRecordStartTime();
                if (z) {
                    recordStartTime = DateUtils.getDateTime(recordStartTime);
                }
                if (!arrayList.contains(Long.valueOf(recordStartTime))) {
                    arrayList.add(Long.valueOf(recordStartTime));
                }
            }
        }
        return arrayList;
    }

    public String getRandomArrayString(int i) {
        String[] stringArray = GalleryApp.sGetAndroidContext().getResources().getStringArray(i);
        return stringArray.length > 0 ? stringArray[new Random().nextInt(stringArray.length)] : "";
    }

    public String getRandomArrayString(List<String> list) {
        return (list == null || list.size() <= 0) ? "" : list.get(new Random().nextInt(list.size()));
    }

    /* loaded from: classes.dex */
    public static class MediaItem {
        public long mDateTime;
        public long mId;

        public String toString() {
            return "MediaItem{mId=" + this.mId + ", mDateTime=" + this.mDateTime + '}';
        }
    }
}
