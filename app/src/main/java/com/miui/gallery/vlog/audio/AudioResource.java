package com.miui.gallery.vlog.audio;

import com.miui.gallery.vlog.R$drawable;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.vlog.base.net.VlogResource;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class AudioResource extends VlogResource {
    public static final HashMap<String, Integer> sAudioNames = new HashMap<String, Integer>() { // from class: com.miui.gallery.vlog.audio.AudioResource.1
        {
            put("vlog_audio_default", Integer.valueOf(R$string.vlog_none));
            put("vlog_audio_changyang", Integer.valueOf(R$string.vlog_audio_changyang));
            put("vlog_audio_weifeng", Integer.valueOf(R$string.vlog_audio_weifeng));
            put("vlog_audio_rumeng", Integer.valueOf(R$string.vlog_audio_rumeng));
            put("vlog_audio_guiyun", Integer.valueOf(R$string.vlog_audio_guiyun));
            put("vlog_audio_shiyi", Integer.valueOf(R$string.vlog_audio_shiyi));
            put("vlog_audio_mengya", Integer.valueOf(R$string.vlog_audio_mengya));
            put("vlog_audio_wanyan", Integer.valueOf(R$string.vlog_audio_wanyan));
            put("vlog_audio_shenyin", Integer.valueOf(R$string.vlog_audio_shenyin));
            put("vlog_audio_liuying", Integer.valueOf(R$string.vlog_audio_liuying));
            put("vlog_audio_jianyue", Integer.valueOf(R$string.vlog_audio_jianyue));
            put("vlog_audio_mili", Integer.valueOf(R$string.vlog_audio_mili));
            put("vlog_audio_fulan", Integer.valueOf(R$string.vlog_audio_fulan));
        }
    };
    public String mAudioPath;
    public int mBgColor = R$drawable.vlog_common_menu_default_item_bg;
    public String mNameColor;
    public int mNameResId;

    public boolean isNone() {
        return "type_none".equalsIgnoreCase(this.type);
    }

    public boolean isExtra() {
        return "type_extra".equalsIgnoreCase(this.type);
    }

    public boolean isLocal() {
        return "type_local".equalsIgnoreCase(this.type);
    }

    public int getNameResId() {
        HashMap<String, Integer> hashMap;
        if (this.mNameResId == 0 && (hashMap = sAudioNames) != null && hashMap.containsKey(this.nameKey)) {
            this.mNameResId = hashMap.get(this.nameKey).intValue();
        }
        return this.mNameResId;
    }

    public String getFileName() {
        return this.nameKey + ".mp3";
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

    public void setAudioPath(String str) {
        this.mAudioPath = str;
    }

    public String getAudioPath() {
        return this.mAudioPath;
    }

    public void setNameColor(String str) {
        this.mNameColor = str;
    }
}
