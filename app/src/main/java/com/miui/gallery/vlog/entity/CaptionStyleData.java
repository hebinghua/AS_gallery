package com.miui.gallery.vlog.entity;

import com.miui.gallery.vlog.base.manager.MiVideoSdkManager;
import com.miui.gallery.vlog.caption.CaptionStyleResource;

/* loaded from: classes2.dex */
public class CaptionStyleData extends CaptionStyleResource {
    public String assetDoubleId;
    public String assetDoubleName;
    public String assetSingleId;
    public String assetSingleName;
    public String iconColor;

    public static boolean install(MiVideoSdkManager miVideoSdkManager, CaptionStyleData captionStyleData, String str) {
        return false;
    }

    public static CaptionStyleData getDefaultItem() {
        CaptionStyleData captionStyleData = new CaptionStyleData();
        captionStyleData.assetId = "";
        captionStyleData.nameKey = "vlog_caption_style_default";
        captionStyleData.type = "type_extra";
        captionStyleData.mNameResId = CaptionStyleResource.sCaptionStyleNames.get("vlog_caption_style_default").intValue();
        captionStyleData.assetSingleId = "";
        captionStyleData.assetSingleName = "";
        captionStyleData.assetDoubleId = "";
        captionStyleData.assetDoubleName = "";
        captionStyleData.setDownloadState(17);
        return captionStyleData;
    }
}
