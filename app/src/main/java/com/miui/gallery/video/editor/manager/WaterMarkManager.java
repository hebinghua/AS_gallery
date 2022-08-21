package com.miui.gallery.video.editor.manager;

import android.text.TextUtils;
import com.miui.gallery.R;
import com.miui.gallery.net.resource.LocalResource;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.video.editor.TextStyle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class WaterMarkManager {
    public static final int[] sBgColor = {R.drawable.video_editor_water_mark_color_1, R.drawable.video_editor_water_mark_color_2, R.drawable.video_editor_water_mark_color_3, R.drawable.video_editor_water_mark_color_4, R.drawable.video_editor_water_mark_color_5, R.drawable.video_editor_water_mark_color_6, R.drawable.video_editor_water_mark_color_7, R.drawable.video_editor_water_mark_color_8};

    public static ArrayList<TextStyle> initDataWithBgColor() {
        int[] iArr;
        ArrayList<TextStyle> arrayList = new ArrayList<>();
        arrayList.add(getLocalNoneTextStyle());
        arrayList.add(getLocalCustomTextStyle());
        for (int i : sBgColor) {
            TextStyle textStyle = new TextStyle();
            textStyle.setIconResId(i);
            arrayList.add(textStyle);
        }
        return arrayList;
    }

    public static TextStyle getLocalNoneTextStyle() {
        TextStyle textStyle = new TextStyle(R.drawable.video_editor_icon_water_mark_none, "ve_type_none");
        textStyle.setDownloadState(17);
        return textStyle;
    }

    public static TextStyle getLocalCustomTextStyle() {
        TextStyle textStyle = new TextStyle(R.drawable.video_editor_water_mark_text, "ve_type_local");
        textStyle.setDownloadState(17);
        return textStyle;
    }

    public static ArrayList<TextStyle> loadWaterMarks(List<LocalResource> list) {
        int i;
        ArrayList<TextStyle> arrayList = new ArrayList<>();
        if (!BaseMiscUtil.isValid(list)) {
            return arrayList;
        }
        for (int i2 = 0; i2 < list.size(); i2++) {
            LocalResource localResource = list.get(i2);
            if (localResource != null) {
                TextStyle textStyle = new TextStyle(localResource);
                if (textStyle.isNone()) {
                    textStyle.setIconResId(R.drawable.video_editor_icon_water_mark_none);
                } else if (textStyle.isLocal()) {
                    textStyle.setIconResId(R.drawable.video_editor_water_mark_text);
                } else if (textStyle.isExtra()) {
                    int[] iArr = sBgColor;
                    if (i2 < 2 || i2 - 2 >= iArr.length) {
                        i = 0;
                    }
                    textStyle.setBgColor(iArr[i]);
                }
                if (!BaseBuildUtil.isInternational() || localResource.isInternational()) {
                    arrayList.add(textStyle);
                }
            }
        }
        return arrayList;
    }

    public void initDataWithTemplate(String[] strArr, ArrayList<TextStyle> arrayList) {
        boolean z;
        if (strArr == null || arrayList == null) {
            return;
        }
        for (String str : strArr) {
            if (!TextUtils.isEmpty(str)) {
                Iterator<TextStyle> it = arrayList.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        z = true;
                        break;
                    }
                    TextStyle next = it.next();
                    if (next != null && !TextUtils.isEmpty(next.getAssetName()) && str.contains(next.getAssetName())) {
                        next.setDownloadState(17);
                        next.setTemplateId(str);
                        z = false;
                        break;
                    }
                }
                if (z) {
                    NexAssetTemplateManager.getInstance().uninstallPackageById(str);
                }
            }
        }
    }

    public static void updateDataWithTemplate(String[] strArr, TextStyle textStyle) {
        if (strArr == null || textStyle == null) {
            return;
        }
        for (String str : strArr) {
            if (!TextUtils.isEmpty(textStyle.getAssetName()) && str.contains(textStyle.getAssetName())) {
                textStyle.setDownloadState(0);
                textStyle.setTemplateId(str);
                return;
            }
        }
    }
}
