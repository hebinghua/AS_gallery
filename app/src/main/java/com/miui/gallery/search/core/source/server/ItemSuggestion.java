package com.miui.gallery.search.core.source.server;

import com.baidu.platform.comapi.map.MapBundleKey;
import com.google.gson.annotations.SerializedName;
import com.xiaomi.mirror.synergy.CallMethod;
import com.xiaomi.stat.MiStat;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class ItemSuggestion {
    @SerializedName("backupIcons")
    public List<String> backupIcons;
    @SerializedName("extraInfo")
    public Map<String, String> extraInfo;
    @SerializedName(CallMethod.RESULT_ICON)
    public String icon;
    @SerializedName(MapBundleKey.MapObjKey.OBJ_URL)
    public String intentActionURI;
    @SerializedName(MiStat.Param.COUNT)
    public int resultCount;
    @SerializedName("title")
    public String title;
}
