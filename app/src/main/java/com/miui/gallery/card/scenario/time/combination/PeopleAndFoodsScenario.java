package com.miui.gallery.card.scenario.time.combination;

import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.card.Card;
import com.miui.gallery.card.scenario.DateUtils;
import com.miui.gallery.card.scenario.Record;
import com.miui.gallery.card.scenario.time.TimeScenario;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/* loaded from: classes.dex */
public class PeopleAndFoodsScenario extends TimeScenario {
    public List<Long> mSelectedIds;

    public static /* synthetic */ void $r8$lambda$ubp0VNM7sB9o3SWl9Awo3ZENneQ(PeopleAndFoodsScenario peopleAndFoodsScenario, List list, Object obj, List list2) {
        peopleAndFoodsScenario.lambda$getGroupPeopleAndFoodsMedias$1(list, obj, list2);
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public boolean onPrepare(List<Record> list, List<Card> list2) {
        long currentTimeMillis = DateUtils.getCurrentTimeMillis();
        long lastDayEndOfMonthTime = DateUtils.getLastDayEndOfMonthTime(currentTimeMillis);
        if (currentTimeMillis <= lastDayEndOfMonthTime - 259200000 || currentTimeMillis >= lastDayEndOfMonthTime) {
            return false;
        }
        List<Long> topNumPeopleIdsByTime = getTopNumPeopleIdsByTime(DateUtils.getFirstDayOfYearTime(currentTimeMillis), currentTimeMillis, 3);
        if (topNumPeopleIdsByTime != null && topNumPeopleIdsByTime.size() >= 2) {
            this.mSelectedIds = getGroupPeopleAndFoodsMedias(2, 10, topNumPeopleIdsByTime);
        }
        List<Long> list3 = this.mSelectedIds;
        return list3 != null && list3.size() >= getMinImageCount();
    }

    @Override // com.miui.gallery.card.scenario.time.TimeScenario, com.miui.gallery.card.scenario.Scenario
    public List<Long> loadMediaItem() {
        return this.mSelectedIds;
    }

    public List<Long> getGroupPeopleAndFoodsMedias(int i, int i2, List<Long> list) {
        if (list == null || list.size() <= 1) {
            return Collections.emptyList();
        }
        List<Long> distinctMediaIds = distinctMediaIds(getIdsFromPeopleEventByTargetGroupPeople(getPeopleEventsByPeopleCount(i, i2), list));
        removeCertificateIdsFromMediaIds(distinctMediaIds);
        if (!BaseMiscUtil.isValid(distinctMediaIds)) {
            return Collections.emptyList();
        }
        final ArrayList arrayList = new ArrayList();
        List<TimeScenario.MediaItem> mediaItemsByMediaIds = getMediaItemsByMediaIds(distinctMediaIds);
        if (BaseMiscUtil.isValid(mediaItemsByMediaIds)) {
            setStartTime(mediaItemsByMediaIds.get(0).mDateTime - 7200000);
            setEndTime(mediaItemsByMediaIds.get(mediaItemsByMediaIds.size() - 1).mDateTime + 7200000);
            Map map = (Map) mediaItemsByMediaIds.stream().collect(Collectors.groupingBy(PeopleAndFoodsScenario$$ExternalSyntheticLambda1.INSTANCE));
            if (BaseMiscUtil.isValid(map)) {
                map.forEach(new BiConsumer() { // from class: com.miui.gallery.card.scenario.time.combination.PeopleAndFoodsScenario$$ExternalSyntheticLambda0
                    @Override // java.util.function.BiConsumer
                    public final void accept(Object obj, Object obj2) {
                        PeopleAndFoodsScenario.$r8$lambda$ubp0VNM7sB9o3SWl9Awo3ZENneQ(PeopleAndFoodsScenario.this, arrayList, obj, (List) obj2);
                    }
                });
            }
        }
        distinctMediaIds.addAll(arrayList);
        DefaultLogger.d(this.TAG, "| Recommendation |Scenario.getId()=%d,getGroupPeopleAndFoodsMedias(), finally remove certificateIds selectMedias.size()=%d", Integer.valueOf(this.mScenarioId), Integer.valueOf(distinctMediaIds.size()));
        return distinctMediaIds;
    }

    public static /* synthetic */ Object lambda$getGroupPeopleAndFoodsMedias$0(TimeScenario.MediaItem mediaItem) {
        return Long.valueOf(DateUtils.getDateTime(mediaItem.mDateTime));
    }

    public /* synthetic */ void lambda$getGroupPeopleAndFoodsMedias$1(List list, Object obj, List list2) {
        if (BaseMiscUtil.isValid(list2)) {
            long j = ((TimeScenario.MediaItem) list2.get(0)).mDateTime - 7200000;
            long j2 = ((TimeScenario.MediaItem) list2.get(list2.size() - 1)).mDateTime + 7200000;
            List<Long> list3 = null;
            if (BaseMiscUtil.isValid(this.mTagIdList)) {
                list3 = getMediaIdsByStartEndTimeTags(this.mTagIdList, j, j2);
            }
            if (!BaseMiscUtil.isValid(list3)) {
                return;
            }
            list.addAll(list3);
        }
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public String generateTitle(Record record, List<MediaFeatureItem> list) {
        return GalleryApp.sGetAndroidContext().getResources().getString(R.string.foods_people_title);
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public String generateDescription(Record record, List<MediaFeatureItem> list) {
        return DateUtils.getDatePeriodGraceful(getStartTime(), getEndTime());
    }
}
