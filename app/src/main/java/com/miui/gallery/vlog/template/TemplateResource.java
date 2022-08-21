package com.miui.gallery.vlog.template;

import com.miui.gallery.vlog.R$drawable;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.vlog.base.net.VlogResource;
import com.miui.gallery.vlog.tools.VlogUtils;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class TemplateResource extends VlogResource {
    public static final HashMap<String, Integer> sTemplateNames = new HashMap<String, Integer>() { // from class: com.miui.gallery.vlog.template.TemplateResource.1
        {
            put("vlog_template_default", Integer.valueOf(R$string.vlog_none));
            put("vlog_template_city", Integer.valueOf(R$string.vlog_template_city));
            put("vlog_template_kuxuan", Integer.valueOf(R$string.vlog_template_kuxuan));
            put("vlog_template_shikong", Integer.valueOf(R$string.vlog_template_shikong));
            put("vlog_template_rixi", Integer.valueOf(R$string.vlog_template_rixi));
            put("vlog_template_travel", Integer.valueOf(R$string.vlog_template_travel));
            put("vlog_template_fugu", Integer.valueOf(R$string.vlog_template_fugu));
            put("vlog_template_baobao", Integer.valueOf(R$string.vlog_template_baobao));
            put("vlog_template_mengchong", Integer.valueOf(R$string.vlog_template_mengchong));
            put("vlog_template_food", Integer.valueOf(R$string.vlog_template_food));
            put("vlog_template_mandiao", Integer.valueOf(R$string.vlog_template_mandiao));
            put("vlog_template_memory", Integer.valueOf(R$string.vlog_template_memory));
            put("vlog_template_summer", Integer.valueOf(R$string.vlog_template_summer));
        }
    };
    public int mBgColor;
    public String mFilePath;
    public String mNameColor;
    public int mNameResId;

    public boolean isHighTemplate() {
        return "vlog_template_travel".equals(this.nameKey) || "vlog_template_kuxuan".equals(this.nameKey);
    }

    public TemplateResource() {
        setTemplate(false);
        this.mBgColor = R$drawable.vlog_common_menu_default_item_bg;
    }

    public static TemplateResource getDefaultItem() {
        TemplateResource templateResource = new TemplateResource();
        templateResource.nameKey = "vlog_template_default";
        templateResource.imageId = R$drawable.template_menu_default;
        templateResource.type = "type_none";
        templateResource.mDownloadState = 17;
        templateResource.mNameResId = sTemplateNames.get("vlog_template_default").intValue();
        templateResource.mNameColor = "#B0BFF1";
        return templateResource;
    }

    @Override // com.miui.gallery.net.resource.LocalResource
    public String getNameKey() {
        return this.nameKey;
    }

    public boolean isNone() {
        return "type_none".equalsIgnoreCase(this.type);
    }

    public boolean isExtra() {
        return "type_extra".equalsIgnoreCase(this.type);
    }

    public int getNameResId() {
        HashMap<String, Integer> hashMap;
        if (this.mNameResId == 0 && (hashMap = sTemplateNames) != null && hashMap.containsKey(this.nameKey)) {
            this.mNameResId = hashMap.get(this.nameKey).intValue();
        }
        return this.mNameResId;
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

    public void setFilePath(String str) {
        this.mFilePath = str;
    }

    public String getAssetName() {
        return VlogUtils.getFormatedStr(this.key);
    }

    public void setNameColor(String str) {
        this.mNameColor = str;
    }
}
