package com.miui.gallery.card.scenario.time.annual;

import com.miui.gallery.R;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.card.scenario.Record;
import java.util.List;

/* loaded from: classes.dex */
public class AnnualTravelsScenario extends AnnualScenario {
    @Override // com.miui.gallery.card.scenario.time.annual.AnnualScenario, com.miui.gallery.card.scenario.time.TimeScenario, com.miui.gallery.card.scenario.Scenario
    public List<Long> loadMediaItem() {
        return getTravelMediaIdsByStartEndTimeAndTag(getStartTime(), getEndTime(), this.mTagIdList);
    }

    @Override // com.miui.gallery.card.scenario.time.annual.AnnualScenario, com.miui.gallery.card.scenario.Scenario
    public String generateTitle(Record record, List<MediaFeatureItem> list) {
        return getRandomArrayString(R.array.annual_travel_title);
    }
}
