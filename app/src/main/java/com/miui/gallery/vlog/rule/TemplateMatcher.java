package com.miui.gallery.vlog.rule;

import java.util.List;

/* loaded from: classes2.dex */
public interface TemplateMatcher {
    boolean matchTemplateAsync(String str, List<String> list, OnTemplateMatchedCallback onTemplateMatchedCallback);

    MatchedTemplate matchToTemplateFromDB(String str, List<String> list);

    void release();
}
