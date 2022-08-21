package com.miui.gallery.card.scenario.time.lastsomeday;

import android.content.res.Resources;
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class RecentTravelScenario extends LocationScenario {
    @Override // com.miui.gallery.card.scenario.Scenario
    public boolean onPrepare(List<Record> list, List<Card> list2) {
        List<Long> recordStartTimesFromRecordAndCards = getRecordStartTimesFromRecordAndCards(list, list2, true);
        long currentTimeMillis = DateUtils.getCurrentTimeMillis();
        long dateTime = DateUtils.getDateTime(currentTimeMillis);
        List<LocationScenario.MediaItem> list3 = (List) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.Cloud.CLOUD_URI, LocationScenario.PROJECTION, String.format(Locale.US, TimeScenario.TIME_SELECTION, Long.valueOf(dateTime - 2592000000L), Long.valueOf(dateTime)), (String[]) null, "mixedDateTime DESC", new SafeDBUtil.QueryHandler<List<LocationScenario.MediaItem>>() { // from class: com.miui.gallery.card.scenario.time.lastsomeday.RecentTravelScenario.1
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public List<LocationScenario.MediaItem> mo1808handle(Cursor cursor) {
                return LocationScenario.getMediaItemsFromCursor(cursor);
            }
        });
        if (!BaseMiscUtil.isValid(list3)) {
            return false;
        }
        String str = null;
        LinkedList linkedList = null;
        LocationScenario.MediaItem mediaItem = null;
        for (LocationScenario.MediaItem mediaItem2 : list3) {
            if (str == null) {
                str = mediaItem2.mCity;
            } else if (linkedList != null) {
                if (!TextUtils.equals(mediaItem.mCity, mediaItem2.mCity)) {
                    break;
                }
                linkedList.add(mediaItem2);
            } else if (!TextUtils.isEmpty(mediaItem2.mCity) && !TextUtils.equals(mediaItem2.mCity, str)) {
                if (currentTimeMillis - mediaItem2.mDateTime >= 259200000) {
                    return false;
                }
                linkedList = new LinkedList();
                linkedList.add(mediaItem2);
                mediaItem = mediaItem2;
            }
        }
        if (BaseMiscUtil.isValid(linkedList)) {
            LocationScenario.MediaItem mediaItem3 = (LocationScenario.MediaItem) linkedList.get(linkedList.size() - 1);
            LocationScenario.MediaItem mediaItem4 = (LocationScenario.MediaItem) linkedList.get(0);
            long j = mediaItem4.mDateTime;
            long j2 = mediaItem3.mDateTime;
            if (j - j2 <= 864000000 && !recordStartTimesFromRecordAndCards.contains(Long.valueOf(DateUtils.getDateTime(j2)))) {
                this.mTargetCity = mediaItem.mCity;
                setStartTime(mediaItem3.mDateTime);
                setEndTime(mediaItem4.mDateTime);
                return true;
            }
        }
        return false;
    }

    @Override // com.miui.gallery.card.scenario.time.LocationScenario, com.miui.gallery.card.scenario.time.TimeScenario, com.miui.gallery.card.scenario.Scenario
    public List<Long> loadMediaItem() {
        List<Integer> list = this.mTagIdList;
        if (list != null && list.size() > 0) {
            return getMediaIdsByStartEndTimeTags(this.mTagIdList, getStartTime(), getEndTime());
        }
        return super.loadMediaItem();
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public String generateTitle(Record record, List<MediaFeatureItem> list) {
        Resources resources = GalleryApp.sGetAndroidContext().getResources();
        if (this.mScenarioId == 2403) {
            ArrayList arrayList = new ArrayList(3);
            arrayList.add(resources.getString(R.string.foods_travel_title_one, record.getLocation()));
            arrayList.add(resources.getString(R.string.foods_travel_title_two, record.getLocation()));
            arrayList.add(resources.getString(R.string.foods_travel_title_three, record.getLocation()));
            return getRandomArrayString(arrayList);
        }
        ArrayList arrayList2 = new ArrayList(4);
        arrayList2.add(resources.getString(R.string.last_some_day_travel_title_one, record.getLocation()));
        arrayList2.add(resources.getString(R.string.last_some_day_travel_title_two, record.getLocation()));
        arrayList2.add(resources.getString(R.string.last_some_day_travel_title_three, record.getLocation()));
        arrayList2.add(resources.getString(R.string.last_some_day_travel_title_four, record.getLocation()));
        return getRandomArrayString(arrayList2);
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public String generateDescription(Record record, List<MediaFeatureItem> list) {
        return getDatePeriodFromRecord(record);
    }
}
