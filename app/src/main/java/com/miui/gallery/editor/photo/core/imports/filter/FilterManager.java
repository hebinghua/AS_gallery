package com.miui.gallery.editor.photo.core.imports.filter;

import android.os.Build;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.filter.portrait.PortraitColorCheckHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes2.dex */
public class FilterManager {
    public static List<FilterCategoryData> getFilterCategory() {
        ArrayList arrayList = new ArrayList(5);
        arrayList.add(new FilterCategoryData(1, GalleryApp.sGetAndroidContext().getString(R.string.filter_category_popular), GalleryApp.sGetAndroidContext().getResources().getColor(R.color.filter_category_popular)));
        arrayList.add(new FilterCategoryData(2, GalleryApp.sGetAndroidContext().getString(R.string.filter_movie_classic), GalleryApp.sGetAndroidContext().getResources().getColor(R.color.filter_category_classic)));
        arrayList.add(new FilterCategoryData(3, GalleryApp.sGetAndroidContext().getString(R.string.filter_category_portrait), GalleryApp.sGetAndroidContext().getResources().getColor(R.color.filter_category_portrait)));
        arrayList.add(new FilterCategoryData(4, GalleryApp.sGetAndroidContext().getString(R.string.filter_category_nice_food), GalleryApp.sGetAndroidContext().getResources().getColor(R.color.filter_category_movie)));
        arrayList.add(new FilterCategoryData(5, GalleryApp.sGetAndroidContext().getString(R.string.filter_category_movie), GalleryApp.sGetAndroidContext().getResources().getColor(R.color.filter_category_movie)));
        arrayList.add(new FilterCategoryData(6, GalleryApp.sGetAndroidContext().getString(R.string.filter_category_travel), GalleryApp.sGetAndroidContext().getResources().getColor(R.color.filter_category_movie)));
        arrayList.add(new FilterCategoryData(7, GalleryApp.sGetAndroidContext().getString(R.string.filter_category_night_scene), GalleryApp.sGetAndroidContext().getResources().getColor(R.color.filter_category_movie)));
        arrayList.add(new FilterCategoryData(8, GalleryApp.sGetAndroidContext().getString(R.string.filter_category_texture), GalleryApp.sGetAndroidContext().getResources().getColor(R.color.filter_category_movie)));
        arrayList.add(new FilterCategoryData(9, GalleryApp.sGetAndroidContext().getString(R.string.filter_category_black_white), GalleryApp.sGetAndroidContext().getResources().getColor(R.color.filter_category_movie)));
        arrayList.add(new FilterCategoryData(10, GalleryApp.sGetAndroidContext().getString(R.string.filter_category_fresh), GalleryApp.sGetAndroidContext().getResources().getColor(R.color.filter_category_movie)));
        if ("wayne".equals(Build.DEVICE)) {
            arrayList.add(new FilterCategoryData(11, GalleryApp.sGetAndroidContext().getString(R.string.filter_category_master), GalleryApp.sGetAndroidContext().getResources().getColor(R.color.filter_category_master)));
        }
        return arrayList;
    }

    public static List<FilterAdjust> getAdjustData() {
        return Arrays.asList(new FilterAdjust(0, (short) 10, GalleryApp.sGetAndroidContext().getString(R.string.adjust_brightness), R.drawable.photo_editor_adjust_brightness_selector, true, R.raw.brightness), new FilterAdjust(3, (short) 10, GalleryApp.sGetAndroidContext().getString(R.string.adjust_sharpen), R.drawable.photo_editor_adjust_sharpen_selector, false, R.raw.sharpen), new FilterAdjust(1, (short) 10, GalleryApp.sGetAndroidContext().getString(R.string.adjust_contrast), R.drawable.photo_editor_adjust_contrast_selector, true, R.raw.contrast), new FilterAdjust(2, (short) 10, GalleryApp.sGetAndroidContext().getString(R.string.adjust_saturation), R.drawable.photo_editor_adjust_saturation_selector, true, R.raw.saturation), new FilterAdjust(4, (short) 10, GalleryApp.sGetAndroidContext().getString(R.string.adjust_vignette), R.drawable.photo_editor_adjust_vignette_selector, false, R.raw.vignette));
    }

    public static List<FilterBeautify> getBeautifyData() {
        return Arrays.asList(new FilterBeautify(0, GalleryApp.sGetAndroidContext().getString(R.string.filter_original), R.drawable.photo_editor_beautify_none_selector), new FilterBeautify(1, GalleryApp.sGetAndroidContext().getString(R.string.onekey_auto), R.drawable.photo_editor_beautify_auto_selector), new FilterBeautify(3, GalleryApp.sGetAndroidContext().getString(R.string.onekey_food), R.drawable.photo_editor_beautify_food_selector), new FilterBeautify(4, GalleryApp.sGetAndroidContext().getString(R.string.onekey_scene), R.drawable.photo_editor_beautify_scene_selector), new FilterBeautify(2, GalleryApp.sGetAndroidContext().getString(R.string.onekey_portrait), R.drawable.photo_editor_beautify_portrait_selector));
    }

    public static FilterBeautify getAutoBeautifyData() {
        return new FilterBeautify(1, GalleryApp.sGetAndroidContext().getString(R.string.onekey_auto), R.drawable.beautify_none_sel);
    }

    public static List<FilterItem> getFiltersByCategory(int i) {
        switch (i) {
            case 1:
                return getPopularFilterItem();
            case 2:
                return getClassicFilterItem();
            case 3:
                return getPortraitFilterItem();
            case 4:
                return getFoodFilterItem();
            case 5:
                return getMovieFilterItem();
            case 6:
                return getTravelFilterItem();
            case 7:
                return getNightFilterItem();
            case 8:
                return getTextureFilterItem();
            case 9:
                return getBlackWhiteFilterItem();
            case 10:
                return getFreshFilterItem();
            case 11:
                return getMasterFilterItem();
            default:
                throw new IllegalArgumentException("not support filterCategory:" + i);
        }
    }

    public static ArrayList<FilterItem> getPopularFilterItem() {
        ArrayList<FilterItem> arrayList = new ArrayList<>();
        String string = GalleryApp.sGetAndroidContext().getString(R.string.filter_original);
        int i = FilterItem.FILTER_POPULAR_COLOR;
        arrayList.add(new FilterItem(string, R.drawable.filter_popular_original, i));
        arrayList.add(new FilterItem("filter/popular/sunset_gold.png", GalleryApp.sGetAndroidContext().getString(R.string.filter_popular_sunset_gold), R.drawable.filter_popular_sunset_gold, 80, i));
        arrayList.add(new FilterItem("filter/popular/blues.png", GalleryApp.sGetAndroidContext().getString(R.string.filter_popular_blues), R.drawable.filter_popular_blues, 80, i));
        arrayList.add(new FilterItem("filter/popular/agave.png", GalleryApp.sGetAndroidContext().getString(R.string.filter_popular_agave), R.drawable.filter_popular_agave, 80, i));
        arrayList.add(new FilterItem("filter/popular/retro_powder.png", GalleryApp.sGetAndroidContext().getString(R.string.filter_popular_retro_powder), R.drawable.filter_popular_retro_powder, 80, i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_popular_pearl), (int) R.drawable.filter_popular_pearl, 80, 19, "popular_pearl", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_popular_navy), (int) R.drawable.filter_popular_navy, 80, 19, "popular_navy", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_popular_french), (int) R.drawable.filter_popular_french, 80, 19, "popular_french", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_popular_polaroid), (int) R.drawable.filter_popular_polaroid, 80, 19, "popular_polaroid", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_popular_cyan), (int) R.drawable.filter_popular_cyan, 80, 19, "popular_cyan", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_popular_pinellia), (int) R.drawable.filter_popular_pinellia, 70, 19, "popular_pinellia", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_popular_wakakusa), (int) R.drawable.filter_popular_wakakusa, 70, 19, "popular_wakakusa", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_popular_sunset), (int) R.drawable.filter_popular_sunset, 80, 19, "popular_sunset", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_popular_freshfood), (int) R.drawable.filter_popular_freshfood, 80, 19, "popular_freshfood", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_popular_summercloud), (int) R.drawable.filter_popular_summercloud, 80, 19, "popular_summercloud", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_popular_youth), (int) R.drawable.filter_popular_youth, 100, 19, "popular_youth", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_popular_windchime), (int) R.drawable.filter_popular_windchime, 100, 19, "popular_windchime", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_popular_gingertea), (int) R.drawable.filter_popular_gingertea, 100, 19, "popular_gingertea", i));
        return arrayList;
    }

    public static ArrayList<FilterItem> getClassicFilterItem() {
        ArrayList<FilterItem> arrayList = new ArrayList<>();
        String string = GalleryApp.sGetAndroidContext().getString(R.string.filter_general_vivid);
        int i = FilterItem.FILTER_CLASSIC_COLOR;
        arrayList.add(new FilterItem("type_cloud", string, (int) R.drawable.filter_classic_vivid, 100, 19, "classic_vivid", i));
        String string2 = GalleryApp.sGetAndroidContext().getString(R.string.filter_popular_blackred);
        int i2 = FilterItem.FILTER_POPULAR_COLOR;
        arrayList.add(new FilterItem("type_cloud", string2, (int) R.drawable.filter_popular_blackred, 100, 19, "popular_blackred", i2));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_movie_blackglod), (int) R.drawable.filter_popular_blackglod, 100, 19, "popular_blackglod", i2));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_general_greenorange), (int) R.drawable.filter_general_greenorange, 100, 19, "popular_greenorange", i2));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_general_past), (int) R.drawable.filter_general_past, 100, 19, "classic_past", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_movie_film), (int) R.drawable.filter_classic_film, 100, 19, "classic_film", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_movie_koizora), (int) R.drawable.filter_classic_koizora, 100, 19, "general_koizora", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_popular_purple), (int) R.drawable.filter_classic_purple, 100, 19, "popular_purple", i2));
        return arrayList;
    }

    public static ArrayList<FilterItem> getPortraitFilterItem() {
        ArrayList<FilterItem> arrayList = new ArrayList<>();
        if (PortraitColorCheckHelper.isPortraitEnable()) {
            arrayList.add(new FilterItem("type_portrait_color", "filter/general/vivid.png", GalleryApp.sGetAndroidContext().getString(R.string.filter_movie_portrait), (int) R.drawable.filter_portrait_portrait, 100, 19, FilterItem.FILTER_PORTRAIT_COLOR));
        }
        String string = GalleryApp.sGetAndroidContext().getString(R.string.filter_portrait_protist);
        int i = FilterItem.FILTER_PORTRAIT_COLOR;
        arrayList.add(new FilterItem("type_cloud", string, (int) R.drawable.filter_portrait_protist, 80, 19, "portrait_protist", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_portrait_holiday), (int) R.drawable.filter_portrait_holiday, 80, 19, "portrait_holiday", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_portrait_light_oxygen), (int) R.drawable.filter_portrait_light_oxygen, 80, 19, "portrait_light_oxygen", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_portrait_mint), (int) R.drawable.filter_portrait_mint, 80, 19, "portrait_mint", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_portrait_pink_orange), (int) R.drawable.filter_portrait_pink_orange, 80, 19, "portrait_pink_orange", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_general_nature), (int) R.drawable.filter_portrait_nature, 100, 19, "general_nature", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_general_pink), (int) R.drawable.filter_portrait_pink, 100, 19, "general_pink", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_general_japanese), (int) R.drawable.filter_portrait_japanese, 100, 19, "general_japanese", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_portrait_painting), (int) R.drawable.filter_portrait_painting, 100, 19, "portrait_painting", i));
        return arrayList;
    }

    public static ArrayList<FilterItem> getMovieFilterItem() {
        ArrayList<FilterItem> arrayList = new ArrayList<>();
        String string = GalleryApp.sGetAndroidContext().getString(R.string.filter_movie_bbp);
        int i = FilterItem.FILTER_MOVIE_COLOR;
        arrayList.add(new FilterItem("type_cloud", string, (int) R.drawable.filter_movie_bbp, 100, 19, "movie_bbp", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_movie_mystery), (int) R.drawable.filter_movie_mystery, 100, 19, "movie_mystery", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_movie_to), (int) R.drawable.filter_movie_to, 100, 19, "movie_greenoranget&o", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_movie_latin), (int) R.drawable.filter_movie_latin, 100, 19, "movie_latin", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_movie_fantasy), (int) R.drawable.filter_movie_fantasy, 100, 19, "movie_fantasy", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_movie_dustdream), (int) R.drawable.filter_movie_dustdream, 100, 19, "movie_dustdream", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_movie_prosperousangle), (int) R.drawable.filter_movie_prosperousangle, 100, 19, "movie_carmen", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_movie_summer), (int) R.drawable.filter_movie_summer, 100, 19, "movie_summer", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_movie_encounter), (int) R.drawable.filter_movie_encounter, 100, 19, "movie_encounter", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_movie_nordic), (int) R.drawable.filter_movie_nordic, 100, 19, "movie_nordic", i));
        if ("wayne".equals(Build.DEVICE)) {
            arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_movie_rome), (int) R.drawable.filter_movie_rome, 100, 19, "movie_rome", i));
        } else {
            arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_movie_rome), (int) R.drawable.filter_movie_rome, 100, 19, "movie_rome", i));
        }
        return arrayList;
    }

    public static ArrayList<FilterItem> getMasterFilterItem() {
        ArrayList<FilterItem> arrayList = new ArrayList<>();
        arrayList.add(new FilterItem("filter/master/memoire.png", GalleryApp.sGetAndroidContext().getString(R.string.filter_master_memoire), R.drawable.filter_master_memoire, 100));
        arrayList.add(new FilterItem("filter/master/mellow.png", GalleryApp.sGetAndroidContext().getString(R.string.filter_master_mellow), R.drawable.filter_master_mellow, 100));
        arrayList.add(new FilterItem("filter/master/somber.png", GalleryApp.sGetAndroidContext().getString(R.string.filter_master_somber), R.drawable.filter_master_somber, 100));
        arrayList.add(new FilterItem("filter/master/rise.png", GalleryApp.sGetAndroidContext().getString(R.string.filter_master_rise), R.drawable.filter_master_rise, 100));
        arrayList.add(new FilterItem("filter/master/hazy.png", GalleryApp.sGetAndroidContext().getString(R.string.filter_master_hazy), R.drawable.filter_master_hazy, 100));
        return arrayList;
    }

    public static ArrayList<FilterItem> getFoodFilterItem() {
        ArrayList<FilterItem> arrayList = new ArrayList<>();
        String string = GalleryApp.sGetAndroidContext().getString(R.string.filter_food_orangeflavor);
        int i = FilterItem.FILTER_MOVIE_COLOR;
        arrayList.add(new FilterItem("type_cloud", string, (int) R.drawable.filter_food_orangeflavor, 100, 19, "food_orangeflavor", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_general_delicious), (int) R.drawable.filter_classic_delicious, 100, 19, "classic_delicious", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_general_softdrink), (int) R.drawable.filter_classic_softdrink, 100, 19, "general_softdrink", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_general_warm), (int) R.drawable.filter_classic_warm, 100, 19, "general_warm", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_popular_mango), (int) R.drawable.filter_popular_mango, 70, 19, "popular_mango", i));
        return arrayList;
    }

    public static ArrayList<FilterItem> getTravelFilterItem() {
        ArrayList<FilterItem> arrayList = new ArrayList<>();
        String string = GalleryApp.sGetAndroidContext().getString(R.string.filter_travel_charlotte);
        int i = FilterItem.FILTER_MOVIE_COLOR;
        arrayList.add(new FilterItem("type_cloud", string, (int) R.drawable.filter_travel_charlotte, 70, 19, "travel_charlotte", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_popular_hill), (int) R.drawable.filter_popular_hill, 100, 19, "travel_hill", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_popular_kamakura), (int) R.drawable.filter_popular_kamakura, 100, 19, "popular_kamakura", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_popular_autumn), (int) R.drawable.filter_popular_autumn, 100, 19, "popular_autumn", FilterItem.FILTER_POPULAR_COLOR));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_popular_icesnow), (int) R.drawable.filter_popular_icesnow, 100, 19, "popular_icesnow", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_popular_wintersun), (int) R.drawable.filter_popular_wintersun, 100, 19, "popular_wintersum", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_popular_dream), (int) R.drawable.filter_popular_dream, 100, 19, "popular_dream", i));
        return arrayList;
    }

    public static ArrayList<FilterItem> getNightFilterItem() {
        ArrayList<FilterItem> arrayList = new ArrayList<>();
        String string = GalleryApp.sGetAndroidContext().getString(R.string.filter_popular_bluedream);
        int i = FilterItem.FILTER_POPULAR_COLOR;
        arrayList.add(new FilterItem("type_cloud", string, (int) R.drawable.filter_popular_bluedream, 100, 19, "popular_bluedream", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_movie_cyberpunk), (int) R.drawable.filter_movie_cyberpunk, 100, 19, "popular_cyberpunk", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_general_blackice), (int) R.drawable.filter_popular_blackice, 100, 19, "general_blackice", i));
        return arrayList;
    }

    public static ArrayList<FilterItem> getTextureFilterItem() {
        ArrayList<FilterItem> arrayList = new ArrayList<>();
        String string = GalleryApp.sGetAndroidContext().getString(R.string.filter_texture_shadow);
        int i = FilterItem.FILTER_MOVIE_COLOR;
        arrayList.add(new FilterItem("type_cloud", string, (int) R.drawable.filter_texture_shadow, 100, 19, "texture_shadow", i));
        String string2 = GalleryApp.sGetAndroidContext().getString(R.string.filter_general_lilt);
        int i2 = FilterItem.FILTER_PORTRAIT_COLOR;
        arrayList.add(new FilterItem("type_cloud", string2, (int) R.drawable.filter_portrait_lilt, 100, 19, "general_lilt", i2));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_portrait_mojito), (int) R.drawable.filter_portrait_mojito, 100, 19, "portrait_mojito", i2));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_movie_quiet), (int) R.drawable.filter_movie_quiet, 100, 19, "movie_quiet", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_general_df), (int) R.drawable.filter_classic_df, 100, 19, "general_df", FilterItem.FILTER_CLASSIC_COLOR));
        return arrayList;
    }

    public static ArrayList<FilterItem> getBlackWhiteFilterItem() {
        ArrayList<FilterItem> arrayList = new ArrayList<>();
        String string = GalleryApp.sGetAndroidContext().getString(R.string.filter_general_blackwhite);
        int i = FilterItem.FILTER_MOVIE_COLOR;
        arrayList.add(new FilterItem("type_cloud", string, (int) R.drawable.filter_classic_blackwhite, 100, 19, "general_blackwhite", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_movie_classic), (int) R.drawable.filter_classic_classic, 100, 19, "general_classic", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_popular_humanity), (int) R.drawable.filter_popular_humanity, 100, 19, "popular_humanity", i));
        return arrayList;
    }

    public static ArrayList<FilterItem> getFreshFilterItem() {
        ArrayList<FilterItem> arrayList = new ArrayList<>();
        String string = GalleryApp.sGetAndroidContext().getString(R.string.filter_general_rise);
        int i = FilterItem.FILTER_MOVIE_COLOR;
        arrayList.add(new FilterItem("type_cloud", string, (int) R.drawable.filter_classic_rise, 100, 19, "general_rise", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_popular_shallow_summer), (int) R.drawable.filter_popular_shallow_summer, 100, 19, "popular_shallow_summer", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_general_fairytale), (int) R.drawable.filter_portrait_fairytale, 100, 19, "general_fairytale", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_portrait_mist), (int) R.drawable.filter_portrait_mist, 100, 19, "portrait_mist", i));
        arrayList.add(new FilterItem("type_cloud", GalleryApp.sGetAndroidContext().getString(R.string.filter_popular_coldsmoke), (int) R.drawable.filter_popular_coldsmoke, 100, 19, "popular_coldsmoke", FilterItem.FILTER_POPULAR_COLOR));
        return arrayList;
    }
}
