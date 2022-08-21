package com.miui.gallery.vlog.filter;

import com.miui.gallery.vlog.R$drawable;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.vlog.base.net.VlogResource;
import com.miui.gallery.vlog.tools.VlogUtils;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class FilterResource extends VlogResource {
    public static final HashMap<String, Integer> sFilterNames = new HashMap<String, Integer>() { // from class: com.miui.gallery.vlog.filter.FilterResource.1
        {
            put("vlog_filter_default", Integer.valueOf(R$string.vlog_filter_origin));
            put("vlog_filter_bbp", Integer.valueOf(R$string.vlog_filter_bbp));
            put("vlog_filter_shenmi", Integer.valueOf(R$string.vlog_filter_shenmi));
            put("vlog_filter_qingcheng", Integer.valueOf(R$string.vlog_filter_qingcheng));
            put("vlog_filter_lading", Integer.valueOf(R$string.vlog_filter_lading));
            put("vlog_filter_qihuan", Integer.valueOf(R$string.vlog_filter_qihuan));
            put("vlog_filter_chenmeng", Integer.valueOf(R$string.vlog_filter_chenmeng));
            put("vlog_filter_wangjiao", Integer.valueOf(R$string.vlog_filter_wangjiao));
            put("vlog_filter_xiari", Integer.valueOf(R$string.vlog_filter_xiari));
            put("vlog_filter_heijin", Integer.valueOf(R$string.vlog_filter_heijin));
            put("vlog_filter_xiehou", Integer.valueOf(R$string.vlog_filter_xiehou));
            put("vlog_filter_beiou", Integer.valueOf(R$string.vlog_filter_beiou));
            put("vlog_filter_luoma", Integer.valueOf(R$string.vlog_filter_luoma));
            put("vlog_filter_rixi", Integer.valueOf(R$string.vlog_filter_rixi));
            put("vlog_filter_zhaomu", Integer.valueOf(R$string.vlog_filter_zhaomu));
            put("vlog_filter_xiayang", Integer.valueOf(R$string.vlog_filter_xiayang));
            put("vlog_filter_nuanyang", Integer.valueOf(R$string.vlog_filter_nuanyang));
            put("vlog_filter_qianyin", Integer.valueOf(R$string.vlog_filter_qianyin));
            put("vlog_filter_chunlu", Integer.valueOf(R$string.vlog_filter_chunlu));
            put("vlog_filter_huadeng", Integer.valueOf(R$string.vlog_filter_huadeng));
            put("vlog_filter_liulan", Integer.valueOf(R$string.vlog_filter_liulan));
            put("vlog_filter_lanmei", Integer.valueOf(R$string.vlog_filter_lanmei));
            put("vlog_filter_changfeng", Integer.valueOf(R$string.vlog_filter_changfeng));
            put("vlog_filter_chujing", Integer.valueOf(R$string.vlog_filter_chujing));
        }
    };
    public int mBgColor = R$drawable.vlog_common_menu_default_item_bg;
    public int mNameResId;

    public int getNameResId() {
        HashMap<String, Integer> hashMap;
        if (this.mNameResId == 0 && (hashMap = sFilterNames) != null && hashMap.containsKey(this.nameKey)) {
            this.mNameResId = hashMap.get(this.nameKey).intValue();
        }
        return this.mNameResId;
    }

    public String getFileName() {
        return VlogUtils.getFormatedStr(this.key) + ".png";
    }

    @Override // com.miui.gallery.vlog.base.net.VlogResource, com.miui.gallery.net.resource.LocalResource
    public String getLabel() {
        return this.label;
    }

    public String getIconUrl() {
        return this.icon;
    }

    public int getIconResId() {
        return this.imageId;
    }

    public int getBgColor() {
        return this.mBgColor;
    }

    @Override // com.miui.gallery.vlog.base.net.VlogResource
    public int getDownloadState() {
        return this.mDownloadState;
    }
}
