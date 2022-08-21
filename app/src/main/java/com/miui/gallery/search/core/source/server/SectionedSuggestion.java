package com.miui.gallery.search.core.source.server;

import com.google.gson.annotations.SerializedName;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.List;

/* loaded from: classes2.dex */
public class SectionedSuggestion {
    @SerializedName("dataUrl")
    public String dataUrl;
    @SerializedName("items")
    public List<ItemSuggestion> items;
    @SerializedName("more")
    public ItemSuggestion moreItem;
    @SerializedName("rankInfos")
    public List<ItemRankInfo> rankInfos;
    @SerializedName(nexExportFormat.TAG_FORMAT_TYPE)
    public String sectionType;
    @SerializedName("title")
    public String title;
}
