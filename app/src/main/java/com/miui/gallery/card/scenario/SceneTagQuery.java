package com.miui.gallery.card.scenario;

import android.text.TextUtils;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.cloudcontrol.strategies.SceneTagStructureStrategy;
import com.miui.gallery.util.BaseMiscUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/* loaded from: classes.dex */
public class SceneTagQuery {
    public List<Integer> mCertificateTags;
    public HashMap<String, Float> mKIdToThresholdMap;
    public HashMap<String, List<String>> mSubToLeafKIdMap;
    public HashMap<String, List<String>> mTopToSubKIdMap;

    /* loaded from: classes.dex */
    public static class SingletonHolder {
        public static final SceneTagQuery INSTANCE = new SceneTagQuery();
    }

    public SceneTagQuery() {
        init();
    }

    public static SceneTagQuery getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init() {
        SceneTagStructureStrategy sceneTagsStructureStrategy = CloudControlStrategyHelper.getSceneTagsStructureStrategy();
        List<SceneTagStructureStrategy.SceneTag> sceneTags = sceneTagsStructureStrategy.getSceneTags();
        if (!BaseMiscUtil.isValid(sceneTags)) {
            return;
        }
        this.mTopToSubKIdMap = new HashMap<>(sceneTags.size());
        this.mSubToLeafKIdMap = new HashMap<>();
        this.mKIdToThresholdMap = new HashMap<>();
        for (SceneTagStructureStrategy.SceneTag sceneTag : sceneTagsStructureStrategy.getSceneTags()) {
            List<SceneTagStructureStrategy.SceneTag> subTags = sceneTag.getSubTags();
            if (BaseMiscUtil.isValid(subTags)) {
                ArrayList arrayList = new ArrayList(subTags.size());
                for (SceneTagStructureStrategy.SceneTag sceneTag2 : subTags) {
                    arrayList.add(sceneTag2.getKgId());
                    List<SceneTagStructureStrategy.SceneTag> subTags2 = sceneTag2.getSubTags();
                    if (BaseMiscUtil.isValid(subTags2)) {
                        ArrayList arrayList2 = new ArrayList(subTags2.size());
                        for (SceneTagStructureStrategy.SceneTag sceneTag3 : sceneTag2.getSubTags()) {
                            arrayList2.add(sceneTag3.getKgId());
                            if (sceneTag3.getThreshold() > 0.0f) {
                                this.mKIdToThresholdMap.put(sceneTag3.getKgId(), Float.valueOf(sceneTag3.getThreshold()));
                            }
                        }
                        this.mSubToLeafKIdMap.put(sceneTag2.getKgId(), arrayList2);
                    }
                }
                this.mTopToSubKIdMap.put(sceneTag.getKgId(), arrayList);
            }
        }
    }

    public List<Integer> queryAllTagIdsByKIds(List<String> list) {
        if (!BaseMiscUtil.isValid(this.mTopToSubKIdMap) || !BaseMiscUtil.isValid(this.mSubToLeafKIdMap) || !BaseMiscUtil.isValid(list)) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        for (String str : list) {
            List<String> list2 = this.mTopToSubKIdMap.get(str);
            List<String> list3 = this.mSubToLeafKIdMap.get(str);
            if (BaseMiscUtil.isValid(list2)) {
                for (String str2 : list2) {
                    List<String> list4 = this.mSubToLeafKIdMap.get(str2);
                    if (list4 != null && list4.size() > 0) {
                        arrayList.addAll(list4);
                    } else {
                        arrayList.add(str2);
                    }
                }
            } else if (BaseMiscUtil.isValid(list3)) {
                arrayList.addAll(list3);
            } else {
                arrayList.add(str);
            }
        }
        final ArrayList arrayList2 = new ArrayList();
        final SceneTagCSVManager sceneTagCSVManager = SceneTagCSVManager.getInstance();
        arrayList.forEach(new Consumer() { // from class: com.miui.gallery.card.scenario.SceneTagQuery$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                SceneTagQuery.lambda$queryAllTagIdsByKIds$0(SceneTagCSVManager.this, arrayList2, (String) obj);
            }
        });
        return (List) arrayList2.stream().distinct().sorted().collect(Collectors.toList());
    }

    public static /* synthetic */ void lambda$queryAllTagIdsByKIds$0(SceneTagCSVManager sceneTagCSVManager, List list, String str) {
        int targetTagId = sceneTagCSVManager.getTargetTagId(str);
        if (targetTagId != -1) {
            list.add(Integer.valueOf(targetTagId));
        }
    }

    public float queryThresholdByTagId(int i) {
        Float f;
        if (!BaseMiscUtil.isValid(this.mKIdToThresholdMap)) {
            return 0.0f;
        }
        String targetKnowledgeId = SceneTagCSVManager.getInstance().getTargetKnowledgeId(i);
        if (TextUtils.isEmpty(targetKnowledgeId) || (f = this.mKIdToThresholdMap.get(targetKnowledgeId)) == null) {
            return 0.0f;
        }
        return f.floatValue();
    }

    public List<Integer> getCertificateTags() {
        if (this.mCertificateTags == null) {
            ArrayList arrayList = new ArrayList(1);
            arrayList.add("/mi/cardcertificatedoc");
            this.mCertificateTags = queryAllTagIdsByKIds(arrayList);
        }
        return this.mCertificateTags;
    }
}
