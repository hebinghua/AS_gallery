package com.miui.gallery.editor.photo.screen.stat;

import com.miui.gallery.editor.photo.screen.core.ScreenRenderData;
import com.miui.gallery.editor.photo.screen.entity.ScreenNavigatorData;
import com.miui.gallery.stat.SamplingStatHelper;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.mirror.synergy.CallMethod;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class ScreenEditorStatUtils {
    public static String getNavName(int i) {
        switch (i) {
            case 1:
                return "send";
            case 2:
                return "doodle";
            case 3:
                return "text";
            case 4:
                return "mosaic";
            case 5:
                return "crop";
            case 6:
            case 7:
            default:
                return "no_define";
            case 8:
                return "shape";
            case 9:
                return "pen";
            case 10:
                return "mark";
            case 11:
                return "eraser";
        }
    }

    public static void statBtnDeletelClick(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, z ? "long_screen_editor" : "normal_editor");
        SamplingStatHelper.recordCountEvent("ScreenEditor", "btn_delete_click", hashMap);
    }

    public static void statBtnSavelClick(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, z ? "long_screen_editor" : "normal_editor");
        SamplingStatHelper.recordCountEvent("ScreenEditor", "btn_save_click", hashMap);
    }

    public static void statRevokeClick(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, z ? "long_screen_editor" : "normal_editor");
        SamplingStatHelper.recordCountEvent("ScreenEditor", "revoke_click", hashMap);
    }

    public static void statRevertClick(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, z ? "long_screen_editor" : "normal_editor");
        SamplingStatHelper.recordCountEvent("ScreenEditor", "revert_click", hashMap);
    }

    public static void statNavItemClick(boolean z, ScreenNavigatorData screenNavigatorData, boolean z2) {
        statNavItemClick(z, screenNavigatorData.id, z2);
    }

    public static void statNavItemClick(boolean z, int i, boolean z2) {
        HashMap hashMap = new HashMap();
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, z ? "long_screen_editor" : "normal_editor");
        hashMap.put("name", getNavName(i));
        hashMap.put(CallMethod.ARG_EXTRA_STRING, z2 ? "true" : "false");
        SamplingStatHelper.recordCountEvent("ScreenEditor", "nav_item_click", hashMap);
    }

    public static void statDoodleMenuItemClick(int i) {
        HashMap hashMap = new HashMap();
        hashMap.put("position", String.valueOf(i));
        SamplingStatHelper.recordCountEvent("ScreenEditor", "doodle_menu_item_click", hashMap);
    }

    public static void statTextMenuItemClick(int i) {
        HashMap hashMap = new HashMap();
        hashMap.put("position", String.valueOf(i));
        SamplingStatHelper.recordCountEvent("ScreenEditor", "text_menu_item_click", hashMap);
    }

    public static void statMosaicMenuItemClick(int i) {
        HashMap hashMap = new HashMap();
        hashMap.put("position", String.valueOf(i));
        SamplingStatHelper.recordCountEvent("ScreenEditor", "mosaic_menu_item_click", hashMap);
    }

    public static void statMosaicSizeChoose(int i) {
        HashMap hashMap = new HashMap();
        hashMap.put("percentage", String.valueOf(i));
        SamplingStatHelper.recordCountEvent("ScreenEditor", "mosaic_size_choose", hashMap);
    }

    public static void statShowExportFragment(ScreenRenderData screenRenderData, boolean z) {
        HashMap<String, String> hashMap = new HashMap<>();
        screenRenderData.putStat(hashMap);
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, z ? "long_screen_editor" : "normal_editor");
        SamplingStatHelper.recordCountEvent("ScreenEditor", "export_saving", hashMap);
    }
}
