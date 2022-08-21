package com.miui.gallery.reddot;

import com.google.common.collect.ImmutableMap;
import com.miui.gallery.assistant.manager.MediaFeatureManager;
import com.miui.gallery.cloudcontrol.CloudControlManager;
import com.miui.gallery.cloudcontrol.observers.FeatureStrategyObserver;
import com.miui.gallery.cloudcontrol.strategies.RecommendStrategy;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.util.FeatureUtil;
import com.miui.gallery.util.PhotoMovieEntranceUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.market.MacaronInstaller;
import com.miui.gallery.util.market.PrintInstaller;
import com.miui.gallery.vlog.VlogEntranceUtils;
import com.nexstreaming.nexeditorsdk.nexEngine;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class DisplayStatusManager {
    public static int sDisplayStatus;
    public static final Map<String, RedDot> sRedDotMap = new HashMap();
    public static final ImmutableMap<String, Integer> sKeyPositionMap = ImmutableMap.builder().put("collage", 2).put("photo_movie", 4).put("photo_cleaner", 8).put("vlog", 1024).put("photo_print", 32).put("settings", 64).put("story_album", 128).put("recommendation", 256).put("macarons", 2048).put("filter_sky", 512).put("magic_matting", 4096).put("id_photo", 8192).put("art_still", 16384).put("video_post", 32768).put("map_album", 65536).put("action_bar_more", Integer.valueOf((int) nexEngine.ExportHEVCMainTierLevel6)).put("assistant_tab", Integer.valueOf((int) nexEngine.ExportHEVCHighTierLevel6)).put("root", Integer.MIN_VALUE).build();

    public static boolean isRedDotEnabled() {
        return true;
    }

    static {
        CloudControlManager.getInstance().registerStrategyObserver("recommendation", null, new FeatureStrategyObserver<RecommendStrategy>() { // from class: com.miui.gallery.reddot.DisplayStatusManager.1
            @Override // com.miui.gallery.cloudcontrol.observers.FeatureStrategyObserver
            public void onStrategyChanged(String str, RecommendStrategy recommendStrategy, RecommendStrategy recommendStrategy2) {
                if (recommendStrategy == null || (recommendStrategy2 != null && recommendStrategy2.getMaxSeqId() > recommendStrategy.getMaxSeqId())) {
                    DisplayStatusManager.updateFeature("recommendation");
                }
            }
        });
    }

    public static void setRedDotClicked(String str) {
        RedDot redDot = getRedDotMap().get(str);
        if (redDot != null) {
            redDot.onClick();
        } else {
            DefaultLogger.e("DisplayStatusManager", "get a null red dot from red dot map");
        }
    }

    public static boolean getRedDotStatus(String str) {
        return getRedDotStatus(str, true);
    }

    public static void updateFeature(String str) {
        getRedDotMap().get(str).onUpdate();
    }

    public static void regenerateRedDotMap() {
        if (sRedDotMap != null) {
            generateRedDotMap();
        }
    }

    public static void generateRedDotMap() {
        RedDotAtom redDotAtom;
        RedDotAtom redDotAtom2;
        RedDotAtom redDotAtom3 = new RedDotAtom("collage", false, false);
        RedDotAtom redDotAtom4 = new RedDotAtom("macarons", false, false);
        RedDotAtom redDotAtom5 = new RedDotAtom("photo_movie", false, false);
        RedDotAtom redDotAtom6 = new RedDotAtom("photo_cleaner", false, false);
        RedDotAtom redDotAtom7 = new RedDotAtom("vlog", false, false);
        RedDotAtom redDotAtom8 = new RedDotAtom("photo_print", false, true);
        RedDotAtom redDotAtom9 = new RedDotAtom("settings", true, false);
        if (FeatureUtil.isReplaceAssistantPageRecommend()) {
            redDotAtom = new RedDotAtom("recommendation", false, false);
            redDotAtom2 = new RedDotAtom("filter_sky", false, false);
        } else {
            redDotAtom = new RedDotAtom("recommendation", false, false);
            redDotAtom2 = new RedDotAtom("filter_sky", false, false);
        }
        RedDotAtom redDotAtom10 = new RedDotAtom("story_album", false, false);
        ArrayList arrayList = new ArrayList();
        if (MediaFeatureManager.isDeviceSupportStoryFunction()) {
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(redDotAtom2);
            arrayList2.add(redDotAtom5);
            arrayList2.add(redDotAtom3);
            arrayList2.add(redDotAtom4);
            arrayList2.add(redDotAtom);
            arrayList2.add(redDotAtom10);
            ArrayList arrayList3 = new ArrayList();
            if (VlogEntranceUtils.isAvailable()) {
                arrayList3.add(redDotAtom7);
            }
            arrayList3.add(redDotAtom6);
            arrayList3.add(redDotAtom9);
            if (PrintInstaller.getInstance().isPhotoPrintEnable()) {
                arrayList3.add(redDotAtom8);
            }
            RedDotGroup redDotGroup = new RedDotGroup("action_bar_more", arrayList3, 1);
            arrayList.add(new RedDotGroup("assistant_tab", arrayList2, 2));
            arrayList.add(redDotGroup);
        } else {
            ArrayList arrayList4 = new ArrayList();
            if (PhotoMovieEntranceUtils.isPhotoMovieUseMiSDK()) {
                arrayList4.add(redDotAtom5);
            }
            if (VlogEntranceUtils.isAvailable()) {
                arrayList4.add(redDotAtom7);
            }
            arrayList4.add(redDotAtom3);
            if (MacaronInstaller.isFunctionOn()) {
                arrayList4.add(redDotAtom4);
            }
            arrayList4.add(redDotAtom6);
            if (PrintInstaller.getInstance().isPhotoPrintEnable()) {
                arrayList4.add(redDotAtom8);
            }
            arrayList4.add(redDotAtom9);
            arrayList.add(new RedDotGroup("action_bar_more", arrayList4, 4));
        }
        flatMap(new RedDotGroup("root", arrayList, 1));
    }

    public static Map<String, RedDot> getRedDotMap() {
        Map<String, RedDot> map = sRedDotMap;
        if (map == null || map.size() == 0) {
            generateRedDotMap();
            return map;
        }
        return map;
    }

    public static void flatMap(RedDot redDot) {
        sRedDotMap.put(redDot.getKey(), redDot);
        if (!(redDot instanceof RedDotAtom) && (redDot instanceof RedDotGroup)) {
            for (RedDot redDot2 : ((RedDotGroup) redDot).mPriorityGroup) {
                flatMap(redDot2);
            }
        }
    }

    public static boolean getRedDotStatus(String str, boolean z) {
        return (keyToPosition(str) & getDisplayStatus(z)) != 0;
    }

    public static int keyToPosition(String str) {
        ImmutableMap<String, Integer> immutableMap = sKeyPositionMap;
        if (immutableMap.get(str) == null) {
            return 0;
        }
        return immutableMap.get(str).intValue();
    }

    public static boolean isRedDotShown(String str) {
        Map<String, RedDot> map = sRedDotMap;
        if (map == null) {
            return false;
        }
        RedDot redDot = map.get(str);
        if (redDot instanceof RedDotAtom) {
            return GalleryPreferences.FeatureRedDot.getRedDotSawTime(str) > 0;
        }
        if (redDot instanceof RedDotGroup) {
            for (RedDot redDot2 : ((RedDotGroup) redDot).mPriorityGroup) {
                if (isRedDotShown(redDot2.getKey())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean reddenDot(String str) {
        int keyToPosition = keyToPosition(str);
        if ((sDisplayStatus & keyToPosition) == 0) {
            if (isRedDotShown(str)) {
                sDisplayStatus |= keyToPosition;
                return true;
            }
            long currentTimeMillis = System.currentTimeMillis() - GalleryPreferences.FeatureRedDot.getLastUnreddenTime();
            if (currentTimeMillis <= 7200000 && currentTimeMillis >= 0) {
                return false;
            }
            sDisplayStatus |= keyToPosition;
            GalleryPreferences.FeatureRedDot.setLastUnreddenTime(0L);
        }
        return true;
    }

    public static void unreddenDot(String str) {
        int keyToPosition = keyToPosition(str);
        int i = sDisplayStatus;
        if ((i & keyToPosition) != 0) {
            sDisplayStatus = (~keyToPosition) & i;
            if (GalleryPreferences.FeatureRedDot.getLastUnreddenTime() != 0) {
                return;
            }
            GalleryPreferences.FeatureRedDot.setLastUnreddenTime(System.currentTimeMillis());
        }
    }

    public static int getDisplayStatus(boolean z) {
        if (z) {
            getRedDotMap().get("root").processDisplayStatus();
        }
        return sDisplayStatus;
    }
}
