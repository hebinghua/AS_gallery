package com.miui.gallery.vlog.entity;

import com.miui.gallery.vlog.R$drawable;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.vlog.base.net.VlogResource;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class TransData extends VlogResource {
    public static final HashMap<String, Integer> sTransNames = new HashMap<String, Integer>() { // from class: com.miui.gallery.vlog.entity.TransData.1
        {
            put("vlog_trans_none", Integer.valueOf(R$string.vlog_trans_none));
            put("vlog_trans_heichang", Integer.valueOf(R$string.vlog_trans_heichang));
            put("vlog_trans_diehua", Integer.valueOf(R$string.vlog_trans_diehua));
            put("vlog_trans_fangda", Integer.valueOf(R$string.vlog_trans_fangda));
            put("vlog_trans_suoxiao", Integer.valueOf(R$string.vlog_trans_suoxiao));
            put("vlog_trans_fangda_rotate", Integer.valueOf(R$string.vlog_trans_fangda_rotate));
            put("vlog_trans_suoxiao_rotate", Integer.valueOf(R$string.vlog_trans_suoxiao_rotate));
            put("vlog_trans_mohu", Integer.valueOf(R$string.vlog_trans_mohu));
            put("vlog_trans_right", Integer.valueOf(R$string.vlog_trans_right));
            put("vlog_trans_left", Integer.valueOf(R$string.vlog_trans_left));
            put("vlog_trans_up", Integer.valueOf(R$string.vlog_trans_up));
            put("vlog_trans_down", Integer.valueOf(R$string.vlog_trans_down));
        }
    };
    public int mNameResId;
    public String mTemplateKey;
    public String mTransName;
    public String mTransPath;

    public String getTemplateKey() {
        return this.mTemplateKey;
    }

    public void setTemplateKey(String str) {
        this.mTemplateKey = str;
    }

    public static TransData getDefaultItem() {
        TransData transData = new TransData();
        transData.nameKey = "vlog_trans_none";
        transData.imageId = R$drawable.vlog_tans_icon_none;
        transData.type = "type_none";
        transData.setDownloadState(17);
        return transData;
    }

    public boolean isNone() {
        return "type_none".equalsIgnoreCase(this.type);
    }

    public boolean isExtra() {
        return "type_extra".equalsIgnoreCase(this.type);
    }

    public int getNameResId() {
        HashMap<String, Integer> hashMap;
        if (this.mNameResId == 0 && (hashMap = sTransNames) != null && hashMap.containsKey(this.nameKey)) {
            this.mNameResId = hashMap.get(this.nameKey).intValue();
        }
        return this.mNameResId;
    }

    public String getFileName() {
        return this.key;
    }

    @Override // com.miui.gallery.vlog.base.net.VlogResource, com.miui.gallery.net.resource.LocalResource
    public String getLabel() {
        return this.label;
    }

    public int getIconResId() {
        return this.imageId;
    }

    public void setTransPath(String str) {
        this.mTransPath = str;
    }

    public String getTransPath() {
        return this.mTransPath;
    }

    public void setTransName(String str) {
        this.mTransName = str;
    }

    public String getTransName() {
        return this.mTransName;
    }
}
