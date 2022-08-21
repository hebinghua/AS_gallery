package com.miui.gallery.video.editor.manager;

import com.google.common.collect.ImmutableMap;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.video.editor.Filter;
import com.miui.gallery.video.editor.model.FilterAdjustData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes2.dex */
public class FilterAdjustManager {
    public static final HashMap<Integer, String> sAdjustMap = new HashMap<>(ImmutableMap.of(0, "曝光度", 3, "清晰度", 1, "对比度", 2, "饱和度", 4, "暗角"));
    public static int[] filterIcons = {R.drawable.video_editor_filter_origin, R.drawable.video_editor_filter_jiaopian, R.drawable.video_editor_filter_jingmi, R.drawable.video_editor_filter_wangshi, R.drawable.video_editor_filter_nuancha, R.drawable.video_editor_filter_bailu, R.drawable.video_editor_filter_qingse, R.drawable.video_editor_filter_xiaosenlin, R.drawable.video_editor_filter_heibai};
    public static String[] sFilterTypes = {"ve_type_none", "ve_type_local", "ve_type_local", "ve_type_local", "ve_type_local", "ve_type_local", "ve_type_local", "ve_type_local", "ve_type_local"};
    public static String[] sFilterNameKeys = {"video_editor_filter_origin", "video_editor_filter_jiaopian", "video_editor_filter_jingmi", "video_editor_filter_wangshi", "video_editor_filter_nuancha", "video_editor_filter_bailu", "video_editor_filter_qingse", "video_editor_filter_xiaosenlin", "video_editor_filter_heibai"};
    public static String[] sFilterName = {"原图", "胶片", "静谧", "往事", "暖茶", "白露", "青涩", "小森林", "黑白"};

    public static List<FilterAdjustData> getAdjustData() {
        String string = GalleryApp.sGetAndroidContext().getString(R.string.adjust_brightness);
        HashMap<Integer, String> hashMap = sAdjustMap;
        return Arrays.asList(new FilterAdjustData(0, (short) 10, string, R.drawable.adjust_brightness, true, hashMap.get(0)), new FilterAdjustData(3, (short) 10, GalleryApp.sGetAndroidContext().getString(R.string.adjust_sharpen), R.drawable.adjust_sharpen, false, hashMap.get(3)), new FilterAdjustData(1, (short) 10, GalleryApp.sGetAndroidContext().getString(R.string.adjust_contrast), R.drawable.adjust_contrast, true, hashMap.get(1)), new FilterAdjustData(2, (short) 10, GalleryApp.sGetAndroidContext().getString(R.string.adjust_saturation), R.drawable.adjust_saturation, true, hashMap.get(2)), new FilterAdjustData(4, (short) 10, GalleryApp.sGetAndroidContext().getString(R.string.adjust_vignette), R.drawable.adjust_vignette, false, hashMap.get(4)));
    }

    public static ArrayList<Filter> getFilterData() {
        ArrayList<Filter> arrayList = new ArrayList<>();
        for (int i = 0; i < sFilterNameKeys.length; i++) {
            arrayList.add(new Filter(filterIcons[i], sFilterTypes[i], sFilterNameKeys[i], sFilterName[i]));
        }
        return arrayList;
    }
}
