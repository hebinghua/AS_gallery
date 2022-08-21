package com.miui.gallery.video.editor;

import android.text.TextUtils;
import com.google.common.collect.ImmutableMap;
import com.miui.gallery.R;
import com.miui.gallery.video.editor.model.VideoEditorBaseModel;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class Filter extends VideoEditorBaseModel {
    public int mIconResId;
    public static final HashMap<String, Integer> filterNames = new HashMap<>(ImmutableMap.builder().put("video_editor_filter_origin", Integer.valueOf((int) R.string.video_editor_filter_origin)).put("video_editor_filter_jiaopian", Integer.valueOf((int) R.string.video_editor_filter_jiaopian)).put("video_editor_filter_jingmi", Integer.valueOf((int) R.string.video_editor_filter_jingmi)).put("video_editor_filter_wangshi", Integer.valueOf((int) R.string.video_editor_filter_wangshi)).put("video_editor_filter_nuancha", Integer.valueOf((int) R.string.video_editor_filter_nuancha)).put("video_editor_filter_bailu", Integer.valueOf((int) R.string.video_editor_filter_bailu)).put("video_editor_filter_qingse", Integer.valueOf((int) R.string.video_editor_filter_qingse)).put("video_editor_filter_xiaosenlin", Integer.valueOf((int) R.string.video_editor_filter_xiaosenlin)).put("video_editor_filter_heibai", Integer.valueOf((int) R.string.video_editor_filter_heibai)).build());
    public static final HashMap<String, String> filterIds = new HashMap<>(ImmutableMap.builder().put("video_editor_filter_origin", "NONE").put("video_editor_filter_jiaopian", "LUT_XIAOMI_OPIAN").put("video_editor_filter_jingmi", "LUT_XIAOMI_GMI").put("video_editor_filter_wangshi", "LUT_XIAOMI_GSHI").put("video_editor_filter_nuancha", "LUT_XIAOMI_NCHA").put("video_editor_filter_bailu", "LUT_XIAOMI_LU").put("video_editor_filter_qingse", "LUT_XIAOMI_GSE").put("video_editor_filter_xiaosenlin", "LUT_XIAOMI_OSENLIN").put("video_editor_filter_heibai", "LUT_XIAOMI_BAI").build());
    public int mNameResId = 0;
    public String mFilterId = "";

    public Filter(int i, String str, String str2, String str3) {
        this.mIconResId = 0;
        this.mIconResId = i;
        this.mType = str;
        this.mExtra = "ve_type_extra".equals(str);
        this.mNameKey = str2;
        this.mLabel = str3;
    }

    public int getNameResId() {
        if (this.mNameResId == 0) {
            HashMap<String, Integer> hashMap = filterNames;
            if (hashMap.containsKey(this.mNameKey)) {
                this.mNameResId = hashMap.get(this.mNameKey).intValue();
            }
        }
        return this.mNameResId;
    }

    public String getFilterId() {
        if (TextUtils.isEmpty(this.mFilterId)) {
            HashMap<String, String> hashMap = filterIds;
            if (hashMap.containsKey(this.mNameKey)) {
                this.mFilterId = hashMap.get(this.mNameKey);
            }
        }
        return this.mFilterId;
    }

    public int getIconResId() {
        return this.mIconResId;
    }
}
