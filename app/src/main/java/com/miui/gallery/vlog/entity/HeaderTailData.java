package com.miui.gallery.vlog.entity;

import android.text.TextUtils;
import com.miui.gallery.vlog.R$drawable;
import com.miui.gallery.vlog.caption.HeaderTailResource;
import com.miui.gallery.vlog.home.VlogConfig;
import java.io.File;

/* loaded from: classes2.dex */
public class HeaderTailData extends HeaderTailResource {
    public AutoContents mAutoContents;
    public int mainTitleNumber;
    public int subTitleNumber;

    public static HeaderTailData getDefaultItem() {
        HeaderTailData headerTailData = new HeaderTailData();
        headerTailData.nameKey = "vlog_title_style_default";
        headerTailData.imageId = R$drawable.template_menu_default;
        headerTailData.type = "type_none";
        headerTailData.label = "none";
        headerTailData.mNameResId = HeaderTailResource.sTitleStyleNames.get("vlog_title_style_default").intValue();
        return headerTailData;
    }

    public static HeaderTailData getCustomItem() {
        HeaderTailData headerTailData = new HeaderTailData();
        headerTailData.nameKey = "vlog_title_style_custom";
        headerTailData.imageId = R$drawable.vlog_caption_add_custom;
        headerTailData.type = "type_custom";
        headerTailData.label = "custom";
        headerTailData.mNameResId = HeaderTailResource.sTitleStyleNames.get("vlog_title_style_custom").intValue();
        return headerTailData;
    }

    public AutoContents getAutoContents() {
        if (this.mAutoContents == null) {
            this.mAutoContents = new AutoContents();
        }
        return this.mAutoContents;
    }

    public void setAutoContents(String str, String str2) {
        getAutoContents().setContents(str);
        getAutoContents().setSub(str2);
    }

    public String getFileName() {
        return this.key;
    }

    public String getFolderPath() {
        return VlogConfig.HEADER_TAIL_ASSET_PATH + File.separator + this.key;
    }

    /* loaded from: classes2.dex */
    public class AutoContents {
        public String mContents;
        public String mSub;

        public AutoContents() {
        }

        public String getContents() {
            return this.mContents;
        }

        public void setContents(String str) {
            this.mContents = str;
        }

        public String getSub() {
            return this.mSub;
        }

        public void setSub(String str) {
            this.mSub = str;
        }

        public boolean isValid() {
            return !TextUtils.isEmpty(this.mContents) || !TextUtils.isEmpty(this.mSub);
        }
    }
}
