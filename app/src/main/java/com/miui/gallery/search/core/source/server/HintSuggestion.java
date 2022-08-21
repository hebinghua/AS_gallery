package com.miui.gallery.search.core.source.server;

import com.google.gson.annotations.SerializedName;

/* loaded from: classes2.dex */
public class HintSuggestion {
    @SerializedName("durationMs")
    public int displayDurationMs;
    @SerializedName("text")
    public String hintText;
    @SerializedName("query")
    public String queryText;
}
