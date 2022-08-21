package com.miui.gallery.card.scenario.time.combination;

import android.database.Cursor;
import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.card.Card;
import com.miui.gallery.card.scenario.DateUtils;
import com.miui.gallery.card.scenario.Record;
import com.miui.gallery.card.scenario.time.LocationScenario;
import com.miui.gallery.card.scenario.time.TimeScenario;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* loaded from: classes.dex */
public class PeopleAndTravelScenario extends TimeScenario {
    /* renamed from: $r8$lambda$-7dlsztyz8TJ6K71SyVU97y3l2A */
    public static /* synthetic */ boolean m650$r8$lambda$7dlsztyz8TJ6K71SyVU97y3l2A(List list, Long l) {
        return list.contains(l);
    }

    /* renamed from: $r8$lambda$vkIQjSKF7O--bMYUAk6TRQ0DM44 */
    public static /* synthetic */ boolean m651$r8$lambda$vkIQjSKF7ObMYUAk6TRQ0DM44(List list, Long l) {
        return list.contains(l);
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public boolean onPrepare(List<Record> list, List<Card> list2) {
        List<Long> recordStartTimesFromRecordAndCards = getRecordStartTimesFromRecordAndCards(list, list2, true);
        long currentTimeMillis = DateUtils.getCurrentTimeMillis();
        int month = DateUtils.getMonth(currentTimeMillis);
        long lastDayEndOfMonthTime = DateUtils.getLastDayEndOfMonthTime(currentTimeMillis);
        if ((month == 5 || month == 11) && currentTimeMillis > lastDayEndOfMonthTime - 259200000 && currentTimeMillis < lastDayEndOfMonthTime) {
            long firstDayOfYearTime = DateUtils.getFirstDayOfYearTime(currentTimeMillis);
            if (recordStartTimesFromRecordAndCards.contains(Long.valueOf(firstDayOfYearTime))) {
                return false;
            }
            setStartTime(firstDayOfYearTime);
            setEndTime(lastDayEndOfMonthTime);
            return true;
        }
        return false;
    }

    @Override // com.miui.gallery.card.scenario.time.TimeScenario, com.miui.gallery.card.scenario.Scenario
    public List<Long> loadMediaItem() {
        List<Long> topNumPeopleIdsByTime = getTopNumPeopleIdsByTime(getStartTime(), getEndTime(), 3);
        if (BaseMiscUtil.isValid(topNumPeopleIdsByTime)) {
            List<Long> mediaIdsByTimeGroupPeopleIds = getMediaIdsByTimeGroupPeopleIds(2, 10, getStartTime(), getEndTime(), topNumPeopleIdsByTime);
            if (BaseMiscUtil.isValid(mediaIdsByTimeGroupPeopleIds)) {
                return getTravelAndPeopleMediaIdsByStartEndTimeAndTag(getStartTime(), getEndTime(), mediaIdsByTimeGroupPeopleIds, this.mTagIdList);
            }
        }
        return Collections.emptyList();
    }

    public List<Long> getTravelAndPeopleMediaIdsByStartEndTimeAndTag(long j, long j2, final List<Long> list, List<Integer> list2) {
        if (!BaseMiscUtil.isValid(list) || !BaseMiscUtil.isValid(list2)) {
            return Collections.emptyList();
        }
        List<Long> arrayList = new ArrayList<>();
        List list3 = (List) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.Cloud.CLOUD_URI, LocationScenario.PROJECTION, String.format(Locale.US, TimeScenario.IMAGE_LOCATION_SELECTION, Long.valueOf(j), Long.valueOf(j2)), (String[]) null, "mixedDateTime ASC", new SafeDBUtil.QueryHandler<List<LocationScenario.MediaItem>>() { // from class: com.miui.gallery.card.scenario.time.combination.PeopleAndTravelScenario.1
            {
                PeopleAndTravelScenario.this = this;
            }

            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle */
            public List<LocationScenario.MediaItem> mo1808handle(Cursor cursor) {
                DefaultLogger.d(PeopleAndTravelScenario.this.TAG, "| Recommendation |Scenario.getId()=%d,startTime=%s,endTime=%s", Integer.valueOf(PeopleAndTravelScenario.this.mScenarioId), Long.valueOf(PeopleAndTravelScenario.this.getStartTime()), Long.valueOf(PeopleAndTravelScenario.this.getEndTime()));
                return LocationScenario.getMediaItemsFromCursor(cursor);
            }
        });
        if (BaseMiscUtil.isValid(list3)) {
            LocationScenario.MediaItem mediaItem = null;
            for (int i = 0; i < list3.size(); i++) {
                LocationScenario.MediaItem mediaItem2 = (LocationScenario.MediaItem) list3.get(i);
                if (mediaItem != null) {
                    if (!TextUtils.equals(mediaItem.mCity, mediaItem2.mCity)) {
                        LocationScenario.MediaItem mediaItem3 = (LocationScenario.MediaItem) list3.get(i - 1);
                        List<Long> mediaIdsByStartEndTime = getMediaIdsByStartEndTime(mediaItem.mDateTime, mediaItem3.mDateTime);
                        if (mediaItem3.mDateTime - mediaItem.mDateTime <= 864000000 && mediaIdsByStartEndTime != null && mediaIdsByStartEndTime.size() > getMinImageCount()) {
                            List<Long> mediaIdsByStartEndTime2 = getMediaIdsByStartEndTime(mediaItem.mDateTime, mediaItem3.mDateTime);
                            if (mediaIdsByStartEndTime2.stream().anyMatch(new Predicate() { // from class: com.miui.gallery.card.scenario.time.combination.PeopleAndTravelScenario$$ExternalSyntheticLambda1
                                @Override // java.util.function.Predicate
                                public final boolean test(Object obj) {
                                    return PeopleAndTravelScenario.m651$r8$lambda$vkIQjSKF7ObMYUAk6TRQ0DM44(list, (Long) obj);
                                }
                            })) {
                                arrayList.addAll(mediaIdsByStartEndTime2);
                            }
                        }
                    }
                }
                mediaItem = mediaItem2;
            }
        }
        DefaultLogger.d(this.TAG, "| Recommendation |Scenario.getId()=%d,allTravelImages.size()=%s", Integer.valueOf(this.mScenarioId), Integer.valueOf(arrayList.size()));
        final List<Long> distinctMediaIds = distinctMediaIds(getMediaIdsByTags(list2));
        if (BaseMiscUtil.isValid(distinctMediaIds)) {
            arrayList = (List) arrayList.stream().filter(new Predicate() { // from class: com.miui.gallery.card.scenario.time.combination.PeopleAndTravelScenario$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return PeopleAndTravelScenario.m650$r8$lambda$7dlsztyz8TJ6K71SyVU97y3l2A(distinctMediaIds, (Long) obj);
                }
            }).collect(Collectors.toList());
        }
        DefaultLogger.d(this.TAG, "| Recommendation |Scenario.getId()=%d,getTravelAndPeopleMediaIdsByStartEndTimeAndTag():select tags from allTravelImages.size()=%s", Integer.valueOf(this.mScenarioId), Integer.valueOf(arrayList.size()));
        return arrayList;
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public String generateTitle(Record record, List<MediaFeatureItem> list) {
        return getRandomArrayString(R.array.people_travel_title);
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public String generateDescription(Record record, List<MediaFeatureItem> list) {
        return DateUtils.getMonthPeriodGraceful(record.getStartTime(), record.getEndTime());
    }
}
