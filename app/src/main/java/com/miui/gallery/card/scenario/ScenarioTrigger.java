package com.miui.gallery.card.scenario;

import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.card.CardManager;
import com.miui.gallery.card.preprocess.ScenarioAlbumTask;
import com.miui.gallery.card.preprocess.ScenarioTask;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.cloudcontrol.strategies.AssistantScenarioStrategy;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes.dex */
public class ScenarioTrigger {
    public final List<Scenario> mNormalScenarios = new LinkedList();
    public final List<Scenario> mGuaranteeScenarios = new LinkedList();

    public ScenarioTrigger() {
        AssistantScenarioStrategy assistantScenarioStrategy = CloudControlStrategyHelper.getAssistantScenarioStrategy();
        if (assistantScenarioStrategy != null) {
            Scenario.setDefaultMinImageCount(assistantScenarioStrategy.getDefaultMinImageCount());
            Scenario.setDefaultMaxImageCount(assistantScenarioStrategy.getDefaultMaxImageCount());
            Scenario.setDefaultSelectedMinImageCount(assistantScenarioStrategy.getDefaultMinSelectedImageCount());
            Scenario.setDefaultSelectedMaxImageCount(assistantScenarioStrategy.getDefaultMaxSelectedImageCount());
            List<AssistantScenarioStrategy.ScenarioRule> localScenarioRules = assistantScenarioStrategy.getLocalScenarioRules();
            DefaultLogger.d("ScenarioTrigger", "| Recommendation |localRules.size()=%d", Integer.valueOf(localScenarioRules.size()));
            localScenarioRules.forEach(ScenarioTrigger$$ExternalSyntheticLambda0.INSTANCE);
            if (BaseMiscUtil.isValid(localScenarioRules)) {
                for (AssistantScenarioStrategy.ScenarioRule scenarioRule : localScenarioRules) {
                    Scenario createLocalScenario = ScenarioFactory.createLocalScenario(scenarioRule);
                    if (createLocalScenario != null) {
                        DefaultLogger.d("ScenarioTrigger", "| Recommendation |scenario=%s", createLocalScenario);
                        if (createLocalScenario.getScenarioId() == 2405) {
                            this.mGuaranteeScenarios.add(createLocalScenario);
                        } else {
                            this.mNormalScenarios.add(createLocalScenario);
                        }
                    }
                    if (scenarioRule.hasPastYear()) {
                        AssistantScenarioStrategy.ScenarioRule scenarioRule2 = new AssistantScenarioStrategy.ScenarioRule();
                        scenarioRule2.setScenarioId(scenarioRule.getScenarioId() + 100);
                        scenarioRule2.setHolidayId(scenarioRule.getHolidayId());
                        scenarioRule2.setMinImageCount(scenarioRule.getMinImageCount());
                        scenarioRule2.setMinSelectedImageCount(scenarioRule.getMinSelectedImageCount());
                        scenarioRule2.setKnowledgeIds(scenarioRule.getKnowledgeIds());
                        Scenario createLocalScenario2 = ScenarioFactory.createLocalScenario(scenarioRule2);
                        if (createLocalScenario2 != null) {
                            DefaultLogger.d("ScenarioTrigger", "| Recommendation |scenario=%s", createLocalScenario2);
                            this.mNormalScenarios.add(createLocalScenario2);
                        }
                    }
                }
            }
            List<AssistantScenarioStrategy.CloudTimeScenarioRule> cloudTimeScenarioRules = assistantScenarioStrategy.getCloudTimeScenarioRules();
            if (BaseMiscUtil.isValid(cloudTimeScenarioRules)) {
                for (AssistantScenarioStrategy.CloudTimeScenarioRule cloudTimeScenarioRule : cloudTimeScenarioRules) {
                    Scenario createCloudTimeScenario = ScenarioFactory.createCloudTimeScenario(cloudTimeScenarioRule);
                    if (createCloudTimeScenario != null) {
                        this.mNormalScenarios.add(createCloudTimeScenario);
                    }
                }
            }
        }
        DefaultLogger.d("ScenarioTrigger", "| Recommendation |mNormalScenarios.size()=%d", Integer.valueOf(this.mNormalScenarios.size()));
        DefaultLogger.d("ScenarioTrigger", "| Recommendation |mGuaranteeScenarios.size()=%d", Integer.valueOf(this.mGuaranteeScenarios.size()));
    }

    public synchronized void trigger() {
        long currentTimeMillis = System.currentTimeMillis();
        triggerInternal(this.mNormalScenarios);
        DefaultLogger.d("ScenarioTrigger", "| Recommendation |trigger scenarios and generate cards cost %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
    }

    public void triggerGuaranteeScenario() {
        DefaultLogger.d("ScenarioTrigger", "| Recommendation |Trigger guarantee scenarios");
        triggerInternal(this.mGuaranteeScenarios);
    }

    public final void triggerInternal(Collection<Scenario> collection) {
        long currentTimeMillis = System.currentTimeMillis();
        for (Scenario scenario : collection) {
            DefaultLogger.d("ScenarioTrigger", "| Recommendation |%s,scenario: %s %s start...", DateUtils.getDateFormat(DateUtils.getCurrentTimeMillis()), scenario.getName(), Integer.valueOf(scenario.getScenarioId()));
            int scenarioId = scenario.getScenarioId();
            if (!scenario.prepare(scenario.findRecords(), scenario.findCards())) {
                printLogger("Trigger event failed!", "trigger failed because of not meeting scenario", scenario, null);
                statisticTriggerError(scenarioId, "trigger failed because of not meeting scenario");
            } else {
                List<Long> loadMediaItem = scenario.loadMediaItem();
                Record record = new Record(scenario, loadMediaItem);
                int i = 0;
                boolean z = false;
                if (loadMediaItem == null || loadMediaItem.size() < scenario.getMinImageCount()) {
                    if (loadMediaItem != null) {
                        i = loadMediaItem.size();
                    }
                    String str = "trigger failed because media items are too few,which is just " + i;
                    printLogger("Trigger event failed!", str, scenario, loadMediaItem);
                    statisticTriggerError(scenarioId, str);
                } else if (!addRecord(record)) {
                    printLogger("Trigger event failed!", "trigger failed because of failing to add record", scenario, loadMediaItem);
                    statisticTriggerError(scenarioId, "trigger failed because of failing to add record");
                } else {
                    printLogger("Trigger event success!", null, scenario, loadMediaItem);
                    DefaultLogger.d("ScenarioTrigger", "Scenario %s trigger successfully. Try generate card!", scenario.getClass().getSimpleName());
                    ScenarioAlbumTask scenarioAlbumTask = new ScenarioAlbumTask(2);
                    if (scenario.getScenarioId() == 2405) {
                        z = true;
                    }
                    if (scenarioAlbumTask.generateCard(null, scenario, record, z) == ScenarioAlbumTask.CardResult.HAVE_UNPROCESSED_IMAGES) {
                        ScenarioTask.post(2, String.valueOf(record.getRowId()), record.getRowId());
                    }
                }
            }
        }
        CardManager.getInstance().selectToShowCards(currentTimeMillis);
    }

    public final void printLogger(String str, String str2, Scenario scenario, List<Long> list) {
        Object[] objArr = new Object[7];
        int i = 0;
        objArr[0] = str;
        objArr[1] = Integer.valueOf(scenario.getScenarioId());
        objArr[2] = list;
        if (list != null) {
            i = list.size();
        }
        objArr[3] = Integer.valueOf(i);
        objArr[4] = DateUtils.getDateStamp(scenario.getStartTime());
        objArr[5] = DateUtils.getDateStamp(scenario.getEndTime());
        objArr[6] = str2;
        DefaultLogger.d("ScenarioTrigger", "| Recommendation |%s |scenarioId:%s| |selectIds:%s| |number:%s| |timeStart:%s-timeEnd:%s| |cause:%s|", objArr);
    }

    public final void statisticTriggerError(int i, String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("tip", "403.38.0.1.16471");
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, Integer.valueOf(i));
        hashMap.put("error", str);
        TrackController.trackError(hashMap);
    }

    public static boolean addRecord(Record record) {
        return -1 != GalleryEntityManager.getInstance().insert(record);
    }

    public Scenario getScenarioById(int i) {
        for (Scenario scenario : this.mNormalScenarios) {
            if (scenario.getScenarioId() == i) {
                return scenario;
            }
        }
        for (Scenario scenario2 : this.mGuaranteeScenarios) {
            if (scenario2.getScenarioId() == i) {
                return scenario2;
            }
        }
        return null;
    }
}
