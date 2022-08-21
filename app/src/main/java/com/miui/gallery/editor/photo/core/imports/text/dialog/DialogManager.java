package com.miui.gallery.editor.photo.core.imports.text.dialog;

import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class DialogManager {
    public List<BaseDialogModel> mDialogModelList = new ArrayList();
    public List<BaseDialogModel> mSpotModelList = new ArrayList();
    public List<BaseDialogModel> mFestivalModelList = new ArrayList();
    public List<BaseDialogModel> mSceneModelList = new ArrayList();
    public List<BaseDialogModel> mCityModelList = new ArrayList();

    public DialogManager() {
        LocalDialog[] values = LocalDialog.values();
        int length = values.length;
        int i = 0;
        while (i < length) {
            LocalDialog localDialog = values[i];
            LocalDialog[] localDialogArr = values;
            int i2 = length;
            int i3 = i;
            LocalDialogModel localDialogModel = new LocalDialogModel(localDialog.mBackGroundColor, localDialog.mSmallIcon, localDialog.mGraphics, localDialog.mBlackGraphics, localDialog.mLeftPercent, localDialog.mTopPercent, localDialog.mRightPercent, localDialog.mBottomPercent, localDialog.mIsCorner, localDialog.mCornerPosition, localDialog.name(), localDialog.mTalkbackName, localDialog.mType);
            int i4 = localDialog.mType;
            if (i4 == 0) {
                this.mDialogModelList.add(localDialogModel);
            } else if (i4 == 1) {
                this.mSpotModelList.add(localDialogModel);
            } else if (i4 == 2) {
                this.mFestivalModelList.add(localDialogModel);
            } else if (i4 == 3) {
                this.mSceneModelList.add(localDialogModel);
            } else if (i4 == 4) {
                this.mCityModelList.add(localDialogModel);
            }
            i = i3 + 1;
            values = localDialogArr;
            length = i2;
        }
    }

    /* loaded from: classes2.dex */
    public enum LocalDialog {
        SIGNATURE(R.drawable.text_edit_signature_none_icon, 0, R.color.text_menu_item_background_none, 0.0f, 0.0f, 0.0f, 0.0f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_bubble_signature), 0),
        NONE(R.drawable.text_edit_bubble_none_icon, 0, R.color.text_menu_item_background_none, 0.0f, 0.0f, 0.0f, 0.0f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_bubble_none), 0),
        CIRCULAR(R.drawable.text_edit_bubble_circular_icon, R.drawable.text_edit_bubble_circular, R.color.text_menu_item_background_blue, 0.15f, 0.22f, 0.15f, 0.23f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_bubble_circular), 0),
        RECTANGLE(R.drawable.text_edit_bubble_rectangle_icon, R.drawable.text_edit_bubble_rectangle, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_bubble_rectangle), 0),
        RECT_HORIZONTAL(R.drawable.text_edit_bubble_rect_horizontal_icon, R.drawable.text_edit_bubble_rect_horizontal, R.color.text_menu_item_background_blue, 0.15f, 0.2f, 0.1f, 0.2f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_bubble_rectangle2), 0),
        OVAL(R.drawable.text_edit_bubble_oval_icon, R.drawable.text_edit_bubble_oval, R.color.text_menu_item_background_purple, 0.15f, 0.17f, 0.15f, 0.32f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_bubble_oval), 0),
        ARROW_1(R.drawable.text_edit_bubble_arrow_2_icon, R.drawable.text_edit_bubble_arrow_1, R.color.text_menu_item_background_blue, 0, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_bubble_arrow1), 0),
        ARROW_2(R.drawable.text_edit_bubble_arrow_1_icon, R.drawable.text_edit_bubble_arrow_2, R.color.text_menu_item_background_purple, 0, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_bubble_arrow2), 0),
        GUGONG(R.drawable.text_editor_spot_gugong_icon, R.drawable.text_editor_spot_gugong, R.drawable.text_editor_spot_gugong_black, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_spot_gugong), 1),
        CHANGCHENG(R.drawable.text_editor_spot_changcheng_icon, R.drawable.text_editor_spot_changcheng, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_spot_changcheng), 1),
        HUANGHELOU(R.drawable.text_editor_spot_huanghelou_icon, R.drawable.text_editor_spot_huanghelou, R.drawable.text_editor_spot_huanghelou_black, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_spot_huanghelou), 1),
        JIUZHAIGOU(R.drawable.text_editor_spot_jiuzhaigou_icon, R.drawable.text_editor_spot_jiuzhaigou, R.drawable.text_editor_spot_jiuzhaigou_black, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_spot_jiuzhaigou), 1),
        TAISHAN(R.drawable.text_editor_spot_taishan_icon, R.drawable.text_editor_spot_taishan, R.drawable.text_editor_spot_taishan_black, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_spot_taishan), 1),
        HUASHAN(R.drawable.text_editor_spot_huashan_icon, R.drawable.text_editor_spot_huashan, R.drawable.text_editor_spot_huashan_black, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_spot_huashan), 1),
        WUZHEN(R.drawable.text_editor_spot_wuzhen_icon, R.drawable.text_editor_spot_wuzhen, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_spot_wuzhen), 1),
        DAOCHENYADING(R.drawable.text_editor_spot_daochengyading_icon, R.drawable.text_editor_spot_daochengyading, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_spot_daocheng), 1),
        LIJIANG(R.drawable.text_editor_spot_lijiang_icon, R.drawable.text_editor_spot_lijiang, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_spot_lijiang), 1),
        YIHEYUAN(R.drawable.text_editor_spot_yiheyuan_icon, R.drawable.text_editor_spot_yiheyuan, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_spot_yiheyuan), 1),
        XIHU(R.drawable.text_editor_spot_xihu_icon, R.drawable.text_editor_spot_xihu, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_spot_xihu), 1),
        ZHANGJIAJIE(R.drawable.text_editor_spot_zhangjiajie_icon, R.drawable.text_editor_spot_zhangjiajie, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_spot_zhangjiajie), 1),
        HUANGSHAN(R.drawable.text_editor_spot_huangshan_icon, R.drawable.text_editor_spot_huangshan, R.drawable.text_editor_spot_huangshan_black, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_spot_huangshan), 1),
        BINGMAYONG(R.drawable.text_editor_spot_bingmayong_icon, R.drawable.text_editor_spot_bingmayong, R.drawable.text_editor_spot_bingmayong_black, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_spot_bingmayong), 1),
        HONGYADONG(R.drawable.text_editor_spot_hongyadong_icon, R.drawable.text_editor_spot_hongyadong, R.drawable.text_editor_spot_hongyadong_black, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_spot_hongyadong), 1),
        TIANTAN(R.drawable.text_editor_spot_tiantan_icon, R.drawable.text_editor_spot_tiantan, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_spot_tiantan), 1),
        SUZHOUYUANLIN(R.drawable.text_editor_spot_suzhouyuanlin_icon, R.drawable.text_editor_spot_suzhouyuanlin, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_spot_suzhouyuanlin), 1),
        GULANGYU(R.drawable.text_editor_spot_gulangyu_icon, R.drawable.text_editor_spot_gulangyu, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_spot_gulangyu), 1),
        VICTORIA(R.drawable.text_editor_spot_weiduoliya_icon, R.drawable.text_editor_spot_weiduoliya, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_spot_victoria), 1),
        POTALA(R.drawable.text_editor_spot_budalagong_icon, R.drawable.text_editor_spot_budalagong, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_spot_budalagong), 1),
        CHUNJIE(R.drawable.text_editor_festival_chunjie_icon, R.drawable.text_editor_festival_chunjie, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_festival_chunjie), 2),
        CHUXI(R.drawable.text_editor_festival_chuxi_icon, R.drawable.text_editor_festival_chuxi, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_festival_chuxi), 2),
        ZHONGQIU(R.drawable.text_editor_festival_zhongqiu_icon, R.drawable.text_editor_festival_zhongqiu, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_festival_zhongqiujie), 2),
        SUPERMOON1(R.drawable.text_edit_super_moon_city_icon, R.drawable.text_edit_super_moon_city, R.drawable.text_edit_super_moon_city_black, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_festival_chaojiyueliang), 2),
        SUPERMOON2(R.drawable.text_edit_super_moon_wine_icon, R.drawable.text_edit_super_moon_wine, R.drawable.text_edit_super_moon_wine_black, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_festival_chaojieyueliang2), 2),
        SUPERMOON3(R.drawable.text_edit_super_moon_icon, R.drawable.text_edit_super_moon, R.drawable.text_edit_super_moon_black, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_festival_chaojiyueliang3), 2),
        SUPERMOON4(R.drawable.text_edit_super_moon_poem_icon, R.drawable.text_edit_super_moon_poem, R.drawable.text_edit_super_moon_poem_black, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_festival_chaojiyueliang4), 2),
        SUPERMOON5(R.drawable.text_edit_super_moon_rabbit_icon, R.drawable.text_edit_super_moon_rabbit, R.drawable.text_edit_super_moon_rabbit_black, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_festival_chaojiyueliang5), 2),
        ERTONGJIE(R.drawable.text_editor_festival_ertongjie_icon, R.drawable.text_editor_festival_ertongjie, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_festival_ertongjie), 2),
        LABA(R.drawable.text_editor_festival_laba_icon, R.drawable.text_editor_festival_laba, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_festival_laba), 2),
        YUANDAN(R.drawable.text_editor_festival_yuandan_icon, R.drawable.text_editor_festival_yuandan, R.drawable.text_editor_festival_yuandan_black, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_festival_yuandan), 2),
        QINGRENJIE(R.drawable.text_editor_festival_qingren_icon, R.drawable.text_editor_festival_qingren, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_festival_qingrenjie), 2),
        SHENGDAN(R.drawable.text_editor_festival_shengdan_icon, R.drawable.text_editor_festival_shengdan, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_festival_shegndanjie), 2),
        LANTIAN(R.drawable.text_editor_scene_lantian_icon, R.drawable.text_editor_scene_lantian, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_scene_lantian), 3),
        DUOYUN(R.drawable.text_editor_scene_duoyun_icon, R.drawable.text_editor_scene_duoyun, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_scene_duoyun), 3),
        YINTIAN(R.drawable.text_editor_scene_yintian_icon, R.drawable.text_editor_scene_yintian, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_scene_yintian), 3),
        HUA(R.drawable.text_editor_scene_hua_icon, R.drawable.text_editor_scene_hua, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_scene_hua), 3),
        DUOROU(R.drawable.text_editor_scene_duorou_icon, R.drawable.text_editor_scene_duorou, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_scene_duorou), 3),
        FENGYE(R.drawable.text_editor_scene_fengye_icon, R.drawable.text_editor_scene_fengye, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_scene_fengye), 3),
        CAODI(R.drawable.text_editor_scene_caodi_icon, R.drawable.text_editor_scene_caodi, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_scene_caodi), 3),
        LVZHI(R.drawable.text_editor_scene_lvzhi_icon, R.drawable.text_editor_scene_lvzhi, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_scene_lvzhi), 3),
        SHUIXIA(R.drawable.text_editor_scene_shuixia_icon, R.drawable.text_editor_scene_shuixia, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_scene_shuixia), 3),
        SHIWU(R.drawable.text_editor_scene_shiwu_icon, R.drawable.text_editor_scene_shiwu, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_scene_shiwu), 3),
        HUOGUO(R.drawable.text_editor_scene_huoguo_icon, R.drawable.text_editor_scene_huoguo, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_scene_huoguo), 3),
        KAFEI(R.drawable.text_editor_scene_kafei_icon, R.drawable.text_editor_scene_kafei, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_scene_kafei), 3),
        RICHURILUO(R.drawable.text_editor_scene_richuriluo_icon, R.drawable.text_editor_scene_richuriluo, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_scene_richuriluo), 3),
        YEJING(R.drawable.text_editor_scene_yejing_icon, R.drawable.text_editor_scene_yejing, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_scene_yejing), 3),
        DOG(R.drawable.text_editor_scene_dog_icon, R.drawable.text_editor_scene_dog, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_scene_dog), 3),
        CAT(R.drawable.text_editor_scene_cat_icon, R.drawable.text_editor_scene_cat, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_scene_cat), 3),
        BABY(R.drawable.text_editor_scene_baby_icon, R.drawable.text_editor_scene_baby, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_scene_baby), 3),
        BEIJING(R.drawable.text_editor_city_beijing_icon, R.drawable.text_editor_city_beijing, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_city_beijing), 4),
        SHANGHAI(R.drawable.text_editor_city_shanghai_icon, R.drawable.text_editor_city_shanghai, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_city_shanghai), 4),
        GUANGZHOU(R.drawable.text_editor_city_guangzhou_icon, R.drawable.text_editor_city_guangzhou, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_city_guangzhou), 4),
        CHONGQING(R.drawable.text_editor_city_chongqing_icon, R.drawable.text_editor_city_chongqing, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_city_chongqing), 4),
        TIANJIN(R.drawable.text_editor_city_tianjin_icon, R.drawable.text_editor_city_tianjin, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_city_tianjin), 4),
        HANGZHOU(R.drawable.text_editor_city_hangzhou_icon, R.drawable.text_editor_city_hangzhou, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_city_hangzhou), 4),
        WUHAN(R.drawable.text_editor_city_wuhan_icon, R.drawable.text_editor_city_wuhan, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_city_wuhan), 4),
        CHENGDU(R.drawable.text_editor_city_chengdu_icon, R.drawable.text_editor_city_chengdu, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_city_chengdu), 4),
        NANJING(R.drawable.text_editor_city_nanjing_icon, R.drawable.text_editor_city_nanjing, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_city_nanjing), 4),
        SUZHOU(R.drawable.text_editor_city_suzhou_icon, R.drawable.text_editor_city_suzhou, R.color.text_menu_item_background_green, 0.15f, 0.15f, 0.15f, 0.25f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_city_suzhou), 4);
        
        public final int mBackGroundColor;
        public final int mBlackGraphics;
        public final float mBottomPercent;
        public final int mCornerPosition;
        public final int mGraphics;
        public final boolean mIsCorner;
        public final float mLeftPercent;
        public final float mRightPercent;
        public final int mSmallIcon;
        public final String mTalkbackName;
        public final float mTopPercent;
        public final int mType;

        LocalDialog(int i, int i2, int i3, float f, float f2, float f3, float f4, String str, int i4) {
            this.mSmallIcon = i;
            this.mGraphics = i2;
            this.mBlackGraphics = 0;
            this.mBackGroundColor = i3;
            this.mLeftPercent = f;
            this.mTopPercent = f2;
            this.mRightPercent = f3;
            this.mBottomPercent = f4;
            this.mIsCorner = false;
            this.mCornerPosition = 0;
            this.mTalkbackName = str;
            this.mType = i4;
        }

        LocalDialog(int i, int i2, int i3, int i4, String str, int i5) {
            this.mSmallIcon = i;
            this.mGraphics = i2;
            this.mBlackGraphics = 0;
            this.mBackGroundColor = i3;
            this.mLeftPercent = 0.0f;
            this.mTopPercent = 0.0f;
            this.mRightPercent = 0.0f;
            this.mBottomPercent = 0.0f;
            this.mIsCorner = true;
            this.mCornerPosition = i4;
            this.mTalkbackName = str;
            this.mType = i5;
        }

        LocalDialog(int i, int i2, int i3, String str, int i4) {
            this.mSmallIcon = i;
            this.mGraphics = i2;
            this.mBlackGraphics = i3;
            this.mBackGroundColor = 0;
            this.mLeftPercent = 0.0f;
            this.mTopPercent = 0.0f;
            this.mRightPercent = 0.0f;
            this.mBottomPercent = 0.0f;
            this.mIsCorner = false;
            this.mCornerPosition = 0;
            this.mTalkbackName = str;
            this.mType = i4;
        }
    }

    public List<BaseDialogModel> getDialogModelList() {
        return this.mDialogModelList;
    }

    public List<BaseDialogModel> getSpotModelList() {
        return this.mSpotModelList;
    }

    public List<BaseDialogModel> getFestivalModelList() {
        return this.mFestivalModelList;
    }

    public List<BaseDialogModel> getSceneModelList() {
        return this.mSceneModelList;
    }

    public List<BaseDialogModel> getCityModelList() {
        return this.mCityModelList;
    }
}
