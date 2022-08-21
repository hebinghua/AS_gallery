package com.miui.gallery.search.core.source.server;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/* loaded from: classes2.dex */
public class SectionedResponseData implements Cacheable {
    @SerializedName("expireMs")
    public long dataExpireMills;
    @SerializedName("sections")
    public List<SectionedSuggestion> sections;

    @Override // com.miui.gallery.search.core.source.server.Cacheable
    public long getExpireMills() {
        return this.dataExpireMills;
    }
}
