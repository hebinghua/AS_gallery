package com.miui.gallery.vlog.rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes2.dex */
public class VideoTagClipEvaluator {
    public List<Integer> mClassificationCandidateList;
    public List<Integer> mClassificationScoreList;
    public int mDefaultClassificationScore = 0;
    public int mDefaultShootingScore = 0;
    public List<Integer> mShootingCandidateList;
    public List<Integer> mShootingScoreList;

    public VideoTagClipEvaluator(List<Integer> list, List<Integer> list2, List<Integer> list3, List<Integer> list4) {
        this.mClassificationCandidateList = list;
        this.mClassificationScoreList = list2;
        this.mShootingCandidateList = list3;
        this.mShootingScoreList = list4;
        findDefaultClassificationScore();
        findDefaultShootingScore();
    }

    public final void findDefaultClassificationScore() {
        for (int i = 0; i < this.mClassificationCandidateList.size(); i++) {
            if (this.mClassificationCandidateList.get(i).intValue() == 0 && this.mClassificationScoreList.size() > i) {
                this.mDefaultClassificationScore = this.mClassificationScoreList.get(i).intValue();
                return;
            }
        }
    }

    public final void findDefaultShootingScore() {
        for (int i = 0; i < this.mShootingCandidateList.size(); i++) {
            if (this.mShootingCandidateList.get(i).intValue() == 0 && this.mShootingScoreList.size() > i) {
                this.mDefaultShootingScore = this.mShootingScoreList.get(i).intValue();
                return;
            }
        }
    }

    public int evaluate(VideoTagClip videoTagClip) {
        return evaluateShooting(videoTagClip) + evaluateClassification(videoTagClip);
    }

    public final int evaluateShooting(VideoTagClip videoTagClip) {
        if (videoTagClip != null && !Util.isEmpty(this.mShootingCandidateList) && !Util.isEmpty(this.mShootingScoreList) && videoTagClip.getShootingScene() != null) {
            int min = Math.min(this.mShootingCandidateList.size(), this.mShootingScoreList.size());
            for (int i = 0; i < min; i++) {
                if (this.mShootingCandidateList.get(i).intValue() == videoTagClip.getShooting()) {
                    return this.mShootingScoreList.get(i).intValue();
                }
            }
            return this.mDefaultShootingScore;
        }
        return 0;
    }

    public final int evaluateClassification(VideoTagClip videoTagClip) {
        if (videoTagClip != null && !Util.isEmpty(this.mClassificationCandidateList) && !Util.isEmpty(this.mClassificationScoreList) && videoTagClip.getClassificationScene() != null) {
            int min = Math.min(this.mClassificationCandidateList.size(), this.mClassificationScoreList.size());
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < min; i++) {
                if (Classification.belongTo(videoTagClip.getClassification(), this.mClassificationCandidateList.get(i).intValue())) {
                    arrayList.add(this.mClassificationCandidateList.get(i));
                }
            }
            if (arrayList.size() == 0) {
                return this.mDefaultClassificationScore;
            }
            return this.mClassificationScoreList.get(this.mClassificationCandidateList.indexOf(Integer.valueOf(((Integer) Collections.max(arrayList, Classification$$ExternalSyntheticLambda0.INSTANCE)).intValue()))).intValue();
        }
        return 0;
    }
}
