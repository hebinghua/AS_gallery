package com.miui.gallery.video.editor.manager;

import android.text.TextUtils;
import com.miui.gallery.R;
import com.miui.gallery.net.resource.LocalResource;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.video.editor.SmartEffect;
import com.nexstreaming.nexeditorsdk.nexTemplateManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class SmartEffectManager {
    public static final int[] sBgColor = {R.drawable.video_editor_smart_effect_color_1, R.drawable.video_editor_smart_effect_color_2, R.drawable.video_editor_smart_effect_color_3, R.drawable.video_editor_smart_effect_color_4, R.drawable.video_editor_smart_effect_color_5, R.drawable.video_editor_smart_effect_color_6, R.drawable.video_editor_smart_effect_color_7};

    public static ArrayList<SmartEffect> initDataWithBgColor() {
        int[] iArr;
        ArrayList<SmartEffect> arrayList = new ArrayList<>();
        arrayList.add(getLocalNoneSmartEffect());
        for (int i : sBgColor) {
            SmartEffect smartEffect = new SmartEffect();
            smartEffect.setIconResId(i);
            arrayList.add(smartEffect);
        }
        return arrayList;
    }

    public static SmartEffect getLocalNoneSmartEffect() {
        SmartEffect smartEffect = new SmartEffect();
        smartEffect.setIconResId(R.drawable.video_editor_icon_smart_effect_none);
        smartEffect.setDownloadState(17);
        return smartEffect;
    }

    public static ArrayList<SmartEffect> loadSmartEffects(List<LocalResource> list) {
        int i;
        ArrayList<SmartEffect> arrayList = new ArrayList<>();
        if (!BaseMiscUtil.isValid(list)) {
            return arrayList;
        }
        for (LocalResource localResource : list) {
            if (localResource != null) {
                SmartEffect smartEffect = new SmartEffect(localResource);
                if (smartEffect.isNone()) {
                    smartEffect.setIconResId(R.drawable.video_editor_icon_smart_effect_none);
                } else if (smartEffect.isExtra()) {
                    int indexOf = list.indexOf(localResource);
                    int[] iArr = sBgColor;
                    if (indexOf < 1 || indexOf - 1 >= iArr.length) {
                        i = 0;
                    }
                    smartEffect.setBgColor(iArr[i]);
                }
                arrayList.add(smartEffect);
            }
        }
        return arrayList;
    }

    public void initDataWithTemplate(List<nexTemplateManager.Template> list, ArrayList<SmartEffect> arrayList) {
        for (nexTemplateManager.Template template : list) {
            if (template != null) {
                int assetIdx = template.packageInfo().assetIdx();
                String assetId = template.packageInfo().assetId();
                String id = template.id();
                boolean z = true;
                if (!TextUtils.isEmpty(id)) {
                    Iterator<SmartEffect> it = arrayList.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        SmartEffect next = it.next();
                        if (next != null && !TextUtils.isEmpty(next.getAssetName()) && id.contains(next.getAssetName()) && assetId.equals(next.getAssetName()) && assetIdx == next.getAssetId()) {
                            next.setDownloadState(17);
                            next.setTemplate(template);
                            z = false;
                            break;
                        }
                    }
                    if (z) {
                        NexAssetTemplateManager.getInstance().uninstallPackageById(id);
                    }
                }
            }
        }
    }

    public void updateDataWithTemplate(List<nexTemplateManager.Template> list, SmartEffect smartEffect) {
        nexTemplateManager.Template next;
        if (smartEffect == null || list == null) {
            return;
        }
        Iterator<nexTemplateManager.Template> it = list.iterator();
        while (it.hasNext() && (next = it.next()) != null) {
            int assetIdx = next.packageInfo().assetIdx();
            String assetId = next.packageInfo().assetId();
            String id = next.id();
            if (!TextUtils.isEmpty(smartEffect.getAssetName()) && id.contains(smartEffect.getAssetName()) && assetId.equals(smartEffect.getAssetName()) && assetIdx == smartEffect.getAssetId()) {
                smartEffect.setDownloadState(0);
                smartEffect.setTemplate(next);
                return;
            }
        }
    }
}
