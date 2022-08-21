package com.miui.gallery.vlog.caption;

import com.miui.gallery.vlog.R$color;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.vlog.base.net.VlogResource;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class CaptionStyleResource extends VlogResource {
    public static final int[] sBgColor = {R$color.vlog_bg_color1, R$color.vlog_bg_color2, R$color.vlog_bg_color3, R$color.vlog_bg_color4, R$color.vlog_bg_color5, R$color.vlog_bg_color6};
    public static final HashMap<String, Integer> sCaptionStyleNames = new HashMap<String, Integer>() { // from class: com.miui.gallery.vlog.caption.CaptionStyleResource.1
        {
            put("vlog_caption_style_default", 0);
            put("vlog_template_city", Integer.valueOf(R$string.vlog_template_city));
            int i = R$string.vlog_template_travel;
            put("vlog_template_travel", Integer.valueOf(i));
            put("vlog_template_life", Integer.valueOf(i));
            put("vlog_template_slow", Integer.valueOf(i));
            put("vlog_template_memories", Integer.valueOf(i));
        }
    };
    public int mNameResId;

    public CaptionStyleResource() {
        setTemplate(false);
    }

    @Override // com.miui.gallery.vlog.base.net.VlogResource, com.miui.gallery.net.resource.LocalResource
    public String getLabel() {
        return this.label;
    }

    public String getIconUrl() {
        return this.icon;
    }
}
