package com.miui.gallery.vlog.rule.bean;

import java.util.List;

/* loaded from: classes2.dex */
public class TemplateEvalForm {
    public List<Integer> classificationScoreList;
    public List<ClipEvalForm> clipEvalForms;
    public boolean enable;
    public String name;
    public boolean onlyRelevant;
    public int relevantClassification;
    public List<Integer> shootingScoreList;
    public boolean timeSensitive;

    public int getRelevantClassification() {
        return this.relevantClassification;
    }

    public boolean isOnlyRelevant() {
        return this.onlyRelevant;
    }

    public boolean isEnable() {
        return this.enable;
    }

    public List<Integer> getClassificationScoreList() {
        return this.classificationScoreList;
    }

    public List<Integer> getShootingScoreList() {
        return this.shootingScoreList;
    }

    public String getName() {
        return this.name;
    }

    public boolean isTimeSensitive() {
        return this.timeSensitive;
    }

    public List<ClipEvalForm> getClipEvalForms() {
        return this.clipEvalForms;
    }
}
