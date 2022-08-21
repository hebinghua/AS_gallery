package com.miui.gallery.editor.photo.core.imports.sky;

import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.sky.sdk.SkyCheckHelper;
import com.miui.gallery.editor.photo.core.common.model.SkyData;
import com.miui.gallery.util.BaseBuildUtil;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class SkyDataManager {
    public static ArrayList<SkyData> getSunnySkyItem() {
        ArrayList<SkyData> arrayList = new ArrayList<>();
        arrayList.add(new SkyDataImpl(1, null, GalleryApp.sGetAndroidContext().getString(R.string.filter_original), R.drawable.filter_sky_original, 0));
        arrayList.add(new SkyDataImpl(1, "sky_qingtian", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_clear_sky), R.drawable.filter_sky_clear_sky, 100));
        arrayList.add(new SkyDataImpl(1, "sky_qingkong", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_sunny), R.drawable.filter_sky_sunny, 100));
        arrayList.add(new SkyDataImpl(1, "sky_bikong", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_bikong), R.drawable.filter_sky_bikong, 100));
        arrayList.add(new SkyDataImpl(1, "sky_duoyun", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_cloudy), R.drawable.filter_sky_cloudy, 100));
        arrayList.add(new SkyDataImpl(1, "sky_boyun", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_cumulus), R.drawable.filter_sky_cumulus, 100));
        arrayList.add(new SkyDataImpl(1, "sky_yunxu", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_yunxu), R.drawable.filter_sky_yunxu, 100));
        arrayList.add(new SkyDataImpl(1, "sky_cengyun", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_stratus), R.drawable.filter_sky_stratus, 100));
        arrayList.add(new SkyDataImpl(1, "sky_yuyun", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_nimbus), R.drawable.filter_sky_nimbus, 100));
        arrayList.add(new SkyDataImpl(1, "sky_caihong", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_rainbow), R.drawable.filter_sky_rainbow, 100));
        arrayList.add(new SkyDataImpl(1, "sky_hongni", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_double_rainbow), R.drawable.filter_sky_double_rainbow, 100));
        arrayList.add(new SkyDataImpl(1, "sky_xuetian", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_snowy), R.drawable.filter_sky_snowy, 100, true));
        return arrayList;
    }

    public static ArrayList<SkyData> getNightSkyItem() {
        ArrayList<SkyData> arrayList = new ArrayList<>();
        arrayList.add(new SkyDataImpl(2, "sky_xizhao", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_xizhao), R.drawable.filter_sky_xizhao, 100));
        arrayList.add(new SkyDataImpl(2, "sky_qixia", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_qixia), R.drawable.filter_sky_qixia, 100));
        arrayList.add(new SkyDataImpl(2, "sky_zhaoxia", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_dawn), R.drawable.filter_sky_dawn, 100));
        arrayList.add(new SkyDataImpl(2, "sky_luoxia", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_twilight), R.drawable.filter_sky_twilight, 100));
        arrayList.add(new SkyDataImpl(2, "sky_wanxia", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_glow), R.drawable.filter_sky_glow, 100));
        arrayList.add(new SkyDataImpl(2, "sky_yingxia", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_yingxia), R.drawable.filter_sky_yingxia, 100));
        arrayList.add(new SkyDataImpl(2, "sky_yuhui", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_yuhui), R.drawable.filter_sky_yuhui, 100));
        arrayList.add(new SkyDataImpl(2, "sky_xiyang", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_sunset), R.drawable.filter_sky_sunset, 100));
        arrayList.add(new SkyDataImpl(2, "sky_muguang", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_dusk), R.drawable.filter_sky_dusk, 100, true));
        return arrayList;
    }

    public static ArrayList<SkyData> getNocturneSkyItem() {
        ArrayList<SkyData> arrayList = new ArrayList<>();
        arrayList.add(new SkyDataImpl(3, "sky_xingchen", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_xingchen), R.drawable.filter_sky_xingchen, 100));
        arrayList.add(new SkyDataImpl(3, "sky_xingji", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_xingji), R.drawable.filter_sky_xingji, 100));
        arrayList.add(new SkyDataImpl(3, "sky_xingui", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_xinggui), R.drawable.filter_sky_xinggui, 100));
        arrayList.add(new SkyDataImpl(3, "sky_xuanyue", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_xuanyue), R.drawable.filter_sky_xuanyue, 100));
        arrayList.add(new SkyDataImpl(3, "sky_haoyue", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_haoyue), R.drawable.filter_sky_haoyue, 100));
        arrayList.add(new SkyDataImpl(3, "sky_yingyue", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_yingyue), R.drawable.filter_sky_yingyue, 100));
        arrayList.add(new SkyDataImpl(3, "sky_shandian", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_shandian), R.drawable.filter_sky_shandian, 100));
        arrayList.add(new SkyDataImpl(3, "sky_chenguang", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_chenguang), R.drawable.filter_sky_chenguang, 100));
        arrayList.add(new SkyDataImpl(3, "sky_xuanguang", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_xuanguang), R.drawable.filter_sky_xuanguang, 95));
        arrayList.add(new SkyDataImpl(3, "sky_shenkong", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_shenkong), R.drawable.filter_sky_shenkong, 100));
        arrayList.add(new SkyDataImpl(3, "sky_xinghe", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_xinghe), R.drawable.filter_sky_xinghe, 95));
        arrayList.add(new SkyDataImpl(3, "sky_yinhe", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_yinhe), R.drawable.filter_sky_yinhe, 95, true));
        return arrayList;
    }

    public static ArrayList<SkyData> getDynamicSkyItem() {
        ArrayList<SkyData> arrayList = new ArrayList<>();
        arrayList.add(new SkyDataImpl(4, "dynamic_sky_qingtian", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_dynamic_qingtian), R.drawable.filter_dynamic_sky_qingtian, 60, true, true));
        arrayList.add(new SkyDataImpl(4, "dynamic_sky_duoyun", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_dynamic_duoyun), R.drawable.filter_dynamic_sky_duoyun, 60, true, true));
        arrayList.add(new SkyDataImpl(4, "dynamic_sky_jiyun", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_dynamic_jiyun), R.drawable.filter_dynamic_sky_jiyun, 60, true, true));
        arrayList.add(new SkyDataImpl(4, "dynamic_sky_muguang", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_dynamic_muguang), R.drawable.filter_dynamic_sky_muguang, 60, true, true));
        arrayList.add(new SkyDataImpl(4, "dynamic_sky_chuangyu", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_dynamic_rain), R.drawable.filter_dynamic_sky_chuangyu, 60, true, false));
        arrayList.add(new SkyDataImpl(4, "dynamic_sky_xuxue", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_dynamic_snow), R.drawable.filter_dynamic_sky_xuxue, 60, true, false));
        arrayList.add(new SkyDataImpl(4, "dynamic_sky_shuyu", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_dynamic_shuyu), R.drawable.filter_dynamic_sky_shuyu, 60, true, false));
        arrayList.add(new SkyDataImpl(4, "dynamic_sky_yinhe", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_dynamic_yinhe), R.drawable.filter_dynamic_sky_yinhe, 60, true, true));
        arrayList.add(new SkyDataImpl(4, "dynamic_sky_yanhua", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_dynamic_yanhua), R.drawable.filter_dynamic_sky_yanhua, 60, true, true));
        if (!BaseBuildUtil.isInternational() && SkyCheckHelper.isSupportTextYanhua()) {
            arrayList.add(new SkyDataImpl(4, "dynamic_sky_text_yanhua", GalleryApp.sGetAndroidContext().getString(R.string.filter_sky_dynamic_text_yanhua), R.drawable.filter_dynamic_sky_text_yanhua, 60, true, true));
        }
        return arrayList;
    }

    public static int getSunnySkyMaterialId(String str) {
        if (str == null) {
            return 0;
        }
        char c = 65535;
        switch (str.hashCode()) {
            case -1369223993:
                if (str.equals("sky_boyun")) {
                    c = 0;
                    break;
                }
                break;
            case -1347814735:
                if (str.equals("sky_yunxu")) {
                    c = 1;
                    break;
                }
                break;
            case -1347804264:
                if (str.equals("sky_yuyun")) {
                    c = 2;
                    break;
                }
                break;
            case -1092728275:
                if (str.equals("sky_caihong")) {
                    c = 3;
                    break;
                }
                break;
            case -973614023:
                if (str.equals("sky_cengyun")) {
                    c = 4;
                    break;
                }
                break;
            case 497765314:
                if (str.equals("sky_bikong")) {
                    c = 5;
                    break;
                }
                break;
            case 566234866:
                if (str.equals("sky_duoyun")) {
                    c = 6;
                    break;
                }
                break;
            case 675163033:
                if (str.equals("sky_hongni")) {
                    c = 7;
                    break;
                }
                break;
            case 934220108:
                if (str.equals("sky_xuetian")) {
                    c = '\b';
                    break;
                }
                break;
            case 2061869260:
                if (str.equals("sky_qingkong")) {
                    c = '\t';
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return 4;
            case 1:
                return 5;
            case 2:
                return 16;
            case 3:
                return 17;
            case 4:
                return 15;
            case 5:
                return 2;
            case 6:
                return 3;
            case 7:
                return 18;
            case '\b':
                return 19;
            case '\t':
                return 1;
            default:
                return 0;
        }
    }

    public static int getNightSkyMaterialId(String str) {
        if (str == null) {
            return 0;
        }
        char c = 65535;
        switch (str.hashCode()) {
            case -1961747178:
                if (str.equals("sky_zhaoxia")) {
                    c = 0;
                    break;
                }
                break;
            case -1355551270:
                if (str.equals("sky_qixia")) {
                    c = 1;
                    break;
                }
                break;
            case -1347820606:
                if (str.equals("sky_yuhui")) {
                    c = 2;
                    break;
                }
                break;
            case -236516250:
                if (str.equals("sky_muguang")) {
                    c = 3;
                    break;
                }
                break;
            case 795266728:
                if (str.equals("sky_luoxia")) {
                    c = 4;
                    break;
                }
                break;
            case 1091687178:
                if (str.equals("sky_wanxia")) {
                    c = 5;
                    break;
                }
                break;
            case 1128010256:
                if (str.equals("sky_xiyang")) {
                    c = 6;
                    break;
                }
                break;
            case 1128046379:
                if (str.equals("sky_xizhao")) {
                    c = 7;
                    break;
                }
                break;
            case 1486113033:
                if (str.equals("sky_yingxia")) {
                    c = '\b';
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return 8;
            case 1:
                return 7;
            case 2:
                return 12;
            case 3:
                return 14;
            case 4:
                return 9;
            case 5:
                return 10;
            case 6:
                return 13;
            case 7:
                return 6;
            case '\b':
                return 11;
            default:
                return 0;
        }
    }

    public static int getNocturneSkyMaterialId(String str) {
        if (str == null) {
            return 0;
        }
        char c = 65535;
        switch (str.hashCode()) {
            case -1915536000:
                if (str.equals("sky_chenguang")) {
                    c = 0;
                    break;
                }
                break;
            case -1348172739:
                if (str.equals("sky_yinhe")) {
                    c = 1;
                    break;
                }
                break;
            case -910062:
                if (str.equals("sky_shandian")) {
                    c = 2;
                    break;
                }
                break;
            case 113821241:
                if (str.equals("sky_shenkong")) {
                    c = 3;
                    break;
                }
                break;
            case 662281041:
                if (str.equals("sky_haoyue")) {
                    c = 4;
                    break;
                }
                break;
            case 709276388:
                if (str.equals("sky_xuanguang")) {
                    c = 5;
                    break;
                }
                break;
            case 930363265:
                if (str.equals("sky_xuanyue")) {
                    c = 6;
                    break;
                }
                break;
            case 1127688133:
                if (str.equals("sky_xinghe")) {
                    c = 7;
                    break;
                }
                break;
            case 1127688199:
                if (str.equals("sky_xingji")) {
                    c = '\b';
                    break;
                }
                break;
            case 1127688540:
                if (str.equals("sky_xingui")) {
                    c = '\t';
                    break;
                }
                break;
            case 1376394390:
                if (str.equals("sky_xingchen")) {
                    c = '\n';
                    break;
                }
                break;
            case 1486114370:
                if (str.equals("sky_yingyue")) {
                    c = 11;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return 23;
            case 1:
                return 29;
            case 2:
                return 25;
            case 3:
                return 31;
            case 4:
                return SkyCheckHelper.isLargeType() ? 33 : 21;
            case 5:
                return 24;
            case 6:
                return SkyCheckHelper.isLargeType() ? 32 : 20;
            case 7:
                return 30;
            case '\b':
                return 27;
            case '\t':
                return 28;
            case '\n':
                return 26;
            case 11:
                return SkyCheckHelper.isLargeType() ? 34 : 22;
            default:
                return 0;
        }
    }

    public static int getDynamicSkyMaterialId(String str) {
        if (str == null) {
            return 0;
        }
        char c = 65535;
        switch (str.hashCode()) {
            case -238573931:
                if (str.equals("dynamic_sky_jiyun")) {
                    c = 0;
                    break;
                }
                break;
            case -230295746:
                if (str.equals("dynamic_sky_shuyu")) {
                    c = 1;
                    break;
                }
                break;
            case -225288115:
                if (str.equals("dynamic_sky_xuxue")) {
                    c = 2;
                    break;
                }
                break;
            case -224732099:
                if (str.equals("dynamic_sky_yinhe")) {
                    c = 3;
                    break;
                }
                break;
            case 320775682:
                if (str.equals("dynamic_sky_text_yanhua")) {
                    c = 4;
                    break;
                }
                break;
            case 479738148:
                if (str.equals("dynamic_sky_chuangyu")) {
                    c = 5;
                    break;
                }
                break;
            case 1033156338:
                if (str.equals("dynamic_sky_duoyun")) {
                    c = 6;
                    break;
                }
                break;
            case 1353147494:
                if (str.equals("dynamic_sky_muguang")) {
                    c = 7;
                    break;
                }
                break;
            case 1615851948:
                if (str.equals("dynamic_sky_yanhua")) {
                    c = '\b';
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return 2;
            case 1:
                return 6;
            case 2:
                return 5;
            case 3:
                return 9;
            case 4:
                return 8;
            case 5:
                return 4;
            case 6:
                return 1;
            case 7:
                return 3;
            case '\b':
                return 7;
            default:
                return 0;
        }
    }
}
