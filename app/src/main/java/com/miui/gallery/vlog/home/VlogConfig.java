package com.miui.gallery.vlog.home;

import android.content.Context;
import com.miui.gallery.vlog.R$drawable;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.vlog.audio.AudioMenuFragment;
import com.miui.gallery.vlog.caption.CaptionMenuFragment;
import com.miui.gallery.vlog.caption.HeaderTailFragment;
import com.miui.gallery.vlog.clip.ClipEditNavItem;
import com.miui.gallery.vlog.clip.ClipMenuFragment;
import com.miui.gallery.vlog.clip.single.SingleClipMenuFragment;
import com.miui.gallery.vlog.filter.FilterMenuFragment;
import com.miui.gallery.vlog.nav.VlogNavItem;
import com.miui.gallery.vlog.ratio.RatioMenuFragment;
import com.miui.gallery.vlog.template.TemplateMenuFragment;
import com.miui.gallery.vlog.tools.VlogUtils;
import java.io.File;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class VlogConfig {
    public static String AUDIO_PATH;
    public static String CAPTION_ASSET_PATH;
    public static String FILTER_PATH;
    public static String HEADER_TAIL_ASSET_PATH;
    public static String TEMPALTE_PATH;
    public static String TRANS_CODE_PATH;
    public static String TRANS_PATH;
    public static double sMicroSecondPerTwoPixel;
    public static double sPixelPerMicroSecond;
    public static double sPixelPerMicroSecondForClip;
    public static final int TEMPLATE_RES_ID = R$string.vlog_nav_title_template;
    public static final int CLIP_RES_ID = R$string.vlog_nav_title_clip;
    public static final int FILTER_RES_ID = R$string.vlog_nav_title_filter;
    public static final int CAPTION_RES_ID = R$string.vlog_nav_title_caption;
    public static final int AUDIO_RES_ID = R$string.vlog_nav_title_audio;
    public static final int RATIO_RES_ID = R$string.vlog_nav_title_ratio;
    public static float sInitFlag = -1.0f;
    public static double sScaleForPixelPerMicroSecond = 1.0d;

    /* loaded from: classes2.dex */
    public enum VideoSource {
        FORM_INNER_CLIP,
        FROM_OUTER_VIDEO_EDITOR,
        FROM_OUTER_OTHER
    }

    public static void init() {
        if (sInitFlag == 0.0f) {
            return;
        }
        sInitFlag = 0.0f;
        Context galleryApp = VlogUtils.getGalleryApp();
        File externalFilesDir = galleryApp.getExternalFilesDir(null);
        if (externalFilesDir == null) {
            externalFilesDir = galleryApp.getFilesDir();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(externalFilesDir);
        String str = File.separator;
        sb.append(str);
        sb.append("vlog");
        String sb2 = sb.toString();
        AUDIO_PATH = sb2 + str + "vlog_audio_dir";
        TRANS_PATH = sb2 + str + "vlog_trans_dir";
        TEMPALTE_PATH = sb2 + str + "vlog_template_dir";
        FILTER_PATH = sb2 + str + "vlog_filter_dir";
        CAPTION_ASSET_PATH = sb2 + str + "vlog_caption" + str + "asset_store";
        HEADER_TAIL_ASSET_PATH = sb2 + str + "vlog_header_tail" + str + "asset_store";
        StringBuilder sb3 = new StringBuilder();
        sb3.append(sb2);
        sb3.append(str);
        sb3.append("vlog_trans_code");
        TRANS_CODE_PATH = sb3.toString();
    }

    public static String getTransCodePath() {
        init();
        return TRANS_CODE_PATH;
    }

    public static ArrayList<VlogNavItem> getVlogNavData(boolean z) {
        ArrayList<VlogNavItem> arrayList = new ArrayList<>();
        if (!z) {
            int i = TEMPLATE_RES_ID;
            arrayList.add(new VlogNavItem(i, i, TemplateMenuFragment.class.getName()));
            int i2 = CLIP_RES_ID;
            arrayList.add(new VlogNavItem(i2, i2, ClipMenuFragment.class.getName()));
        } else {
            int i3 = CLIP_RES_ID;
            arrayList.add(new VlogNavItem(i3, i3, SingleClipMenuFragment.class.getName()));
        }
        int i4 = FILTER_RES_ID;
        arrayList.add(new VlogNavItem(i4, i4, FilterMenuFragment.class.getName()));
        if (z) {
            int i5 = CAPTION_RES_ID;
            arrayList.add(new VlogNavItem(i5, i5, HeaderTailFragment.class.getName()));
        } else {
            int i6 = CAPTION_RES_ID;
            arrayList.add(new VlogNavItem(i6, i6, CaptionMenuFragment.class.getName()));
        }
        int i7 = AUDIO_RES_ID;
        arrayList.add(new VlogNavItem(i7, i7, AudioMenuFragment.class.getName()));
        int i8 = RATIO_RES_ID;
        arrayList.add(new VlogNavItem(i8, i8, RatioMenuFragment.class.getName()));
        return arrayList;
    }

    public static ArrayList<ClipEditNavItem> getClipEditData() {
        ArrayList<ClipEditNavItem> arrayList = new ArrayList<>();
        arrayList.add(new ClipEditNavItem(R$string.clip_menu_cut, R$drawable.ic_clip_menu_cut, "type_cut"));
        arrayList.add(new ClipEditNavItem(R$string.clip_menu_speedx, R$drawable.ic_clip_menu_speedx, "type_speed"));
        arrayList.add(new ClipEditNavItem(R$string.clip_menu_reveerse, R$drawable.ic_clip_menu_upend, "type_reverse"));
        arrayList.add(new ClipEditNavItem(R$string.clip_menu_remove, R$drawable.ic_clip_menu_remove, "type_delete"));
        return arrayList;
    }
}
