package com.miui.gallery.vlog.caption;

import com.miui.gallery.vlog.R$color;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.vlog.base.net.VlogResource;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class HeaderTailResource extends VlogResource {
    public static final int[] sBgColor = {R$color.vlog_bg_color1, R$color.vlog_bg_color2, R$color.vlog_bg_color3, R$color.vlog_bg_color4, R$color.vlog_bg_color5, R$color.vlog_bg_color6};
    public static final HashMap<String, Integer> sTitleStyleNames = new HashMap<String, Integer>() { // from class: com.miui.gallery.vlog.caption.HeaderTailResource.1
        {
            put("vlog_title_style_default", 0);
            put("vlog_title_style_custom", 0);
            put("vlog_template_city", Integer.valueOf(R$string.vlog_template_city));
            int i = R$string.vlog_template_travel;
            put("vlog_template_travel", Integer.valueOf(i));
            put("vlog_template_life", Integer.valueOf(i));
            put("vlog_template_slow", Integer.valueOf(i));
            put("vlog_template_memories", Integer.valueOf(i));
        }
    };
    public volatile int mBgColor;
    public volatile int mNameResId;
    public String mTemplateKey;

    public HeaderTailResource() {
        setTemplate(false);
    }

    public String getTemplateKey() {
        return this.mTemplateKey;
    }

    public void setTemplateKey(String str) {
        this.mTemplateKey = str;
    }

    public void setBgColor(int i) {
        int[] iArr = sBgColor;
        this.mBgColor = iArr[i % iArr.length];
    }

    public boolean isNone() {
        return "type_none".equalsIgnoreCase(this.type);
    }

    public boolean isCustom() {
        return "type_custom".equalsIgnoreCase(this.type);
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
}
