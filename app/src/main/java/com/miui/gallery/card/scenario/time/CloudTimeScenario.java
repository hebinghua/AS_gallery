package com.miui.gallery.card.scenario.time;

import android.text.TextUtils;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.card.Card;
import com.miui.gallery.card.scenario.Record;
import com.miui.gallery.cloudcontrol.strategies.AssistantScenarioStrategy;
import com.miui.gallery.util.BaseMiscUtil;
import java.util.List;

/* loaded from: classes.dex */
public class CloudTimeScenario extends TimeScenario {
    public String mDescription;
    public long mEndTime;
    public long mStartTime;
    public String mTitle;

    @Override // com.miui.gallery.card.scenario.time.TimeScenario, com.miui.gallery.card.scenario.Scenario
    public void onFillScenarioRule(AssistantScenarioStrategy.ScenarioRule scenarioRule) {
        super.onFillScenarioRule(scenarioRule);
        if (scenarioRule == null || !(scenarioRule instanceof AssistantScenarioStrategy.CloudTimeScenarioRule)) {
            return;
        }
        AssistantScenarioStrategy.CloudTimeScenarioRule cloudTimeScenarioRule = (AssistantScenarioStrategy.CloudTimeScenarioRule) scenarioRule;
        this.mTitle = cloudTimeScenarioRule.getTitle();
        this.mDescription = cloudTimeScenarioRule.getDescription();
        this.mStartTime = cloudTimeScenarioRule.getStartTime();
        this.mEndTime = cloudTimeScenarioRule.getEndTime();
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public boolean onPrepare(List<Record> list, List<Card> list2) {
        if (!BaseMiscUtil.isValid(list) && !BaseMiscUtil.isValid(list2)) {
            long j = this.mStartTime;
            if (j > 0 && this.mEndTime > j && !TextUtils.isEmpty(this.mTitle) && !TextUtils.isEmpty(this.mDescription)) {
                setStartTime(this.mStartTime);
                setEndTime(this.mEndTime);
                return true;
            }
        }
        return false;
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public String generateTitle(Record record, List<MediaFeatureItem> list) {
        return this.mTitle;
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public String generateDescription(Record record, List<MediaFeatureItem> list) {
        return this.mDescription;
    }
}
