package com.miui.gallery.card.scenario.time.guarantee;

import android.database.Cursor;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.card.Card;
import com.miui.gallery.card.scenario.DateUtils;
import com.miui.gallery.card.scenario.Record;
import com.miui.gallery.card.scenario.Scenario;
import com.miui.gallery.card.scenario.ScenarioConstants;
import com.miui.gallery.card.scenario.time.TimeScenario;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.UriUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class RecentMemoryScenario extends TimeScenario {
    public List<Long> mRecentImages = new ArrayList(30);

    @Override // com.miui.gallery.card.scenario.Scenario
    public boolean onPrepare(List<Record> list, List<Card> list2) {
        this.mRecentImages.clear();
        if (BaseMiscUtil.isValid(list2) || BaseMiscUtil.isValid(list)) {
            return false;
        }
        long currentTimeMillis = DateUtils.getCurrentTimeMillis();
        List list3 = (List) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), UriUtil.appendLimit(GalleryContract.Cloud.CLOUD_URI, 30), Scenario.BASE_PROJECTION, String.format(Locale.US, ScenarioConstants.MEDIA_SCENARIO_SELECTION + " AND mixedDateTime < %s", Long.valueOf(currentTimeMillis)), (String[]) null, "mixedDateTime DESC", new SafeDBUtil.QueryHandler<List<Long>>() { // from class: com.miui.gallery.card.scenario.time.guarantee.RecentMemoryScenario.1
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public List<Long> mo1808handle(Cursor cursor) {
                return RecentMemoryScenario.this.getMediaIdsFromCursor(cursor);
            }
        });
        if (!BaseMiscUtil.isValid(list3)) {
            return false;
        }
        this.mRecentImages.addAll(list3);
        return true;
    }

    @Override // com.miui.gallery.card.scenario.time.TimeScenario, com.miui.gallery.card.scenario.Scenario
    public List<Long> loadMediaItem() {
        return this.mRecentImages;
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public String generateTitle(Record record, List<MediaFeatureItem> list) {
        return GalleryApp.sGetAndroidContext().getResources().getString(R.string.card_title_recent_memory);
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public String generateDescription(Record record, List<MediaFeatureItem> list) {
        long j = Long.MAX_VALUE;
        long j2 = 0;
        if (BaseMiscUtil.isValid(list)) {
            for (MediaFeatureItem mediaFeatureItem : list) {
                long dateTime = mediaFeatureItem.getDateTime();
                if (j > dateTime) {
                    j = dateTime;
                }
                if (j2 < dateTime) {
                    j2 = dateTime;
                }
            }
        }
        return DateUtils.getDatePeriodGraceful(j, j2);
    }
}
