package com.miui.gallery.vlog.rule.bean;

import java.util.HashMap;
import java.util.List;

/* loaded from: classes2.dex */
public class TemplateSelectForm {
    public List<List<Integer>> categoryCandidates;
    public HashMap<String, List<Integer>> shootingCandidates;
    public List<TemplateEvalForm> templateEvalForms;

    public List<List<Integer>> getCategoryCandidates() {
        return this.categoryCandidates;
    }

    public HashMap<String, List<Integer>> getShootingCandidates() {
        return this.shootingCandidates;
    }

    public List<TemplateEvalForm> getTemplateEvalForms() {
        return this.templateEvalForms;
    }
}
