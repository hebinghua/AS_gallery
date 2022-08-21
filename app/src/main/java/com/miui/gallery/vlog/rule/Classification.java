package com.miui.gallery.vlog.rule;

import com.miui.gallery.assistant.model.MediaScene;
import com.miui.gallery.assistant.model.MediaSceneTagManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class Classification {
    public static List<Integer> sClassifications;
    public static final int[] sImportanceSeq = {4, 1, 203, 3, 2, 103};
    public static List<MediaSceneTagManager.Tag_Version_0> sSceneryList = new ArrayList();
    public static List<MediaSceneTagManager.Tag_Version_0> sObjectList = new ArrayList();
    public static List<MediaSceneTagManager.Tag_Version_0> sHumanList = new ArrayList();
    public static List<MediaSceneTagManager.Tag_Version_0> sPetList = new ArrayList();
    public static List<MediaSceneTagManager.Tag_Version_0> sFoodList = new ArrayList();
    public static List<MediaSceneTagManager.Tag_Version_0> sBabyList = new ArrayList();
    public static Map<Integer, List<MediaSceneTagManager.Tag_Version_0>> sClassificationTagListMap = new HashMap();

    static {
        ArrayList arrayList = new ArrayList();
        sClassifications = arrayList;
        arrayList.add(1);
        sClassifications.add(2);
        sClassifications.add(3);
        sClassifications.add(4);
        sClassifications.add(103);
        sClassifications.add(203);
        sClassificationTagListMap.put(1, sHumanList);
        sClassificationTagListMap.put(2, sSceneryList);
        sClassificationTagListMap.put(3, sObjectList);
        sClassificationTagListMap.put(4, sBabyList);
        sClassificationTagListMap.put(103, sPetList);
        sClassificationTagListMap.put(203, sFoodList);
        sSceneryList.add(MediaSceneTagManager.Tag_Version_0.E_SNOW);
        sSceneryList.add(MediaSceneTagManager.Tag_Version_0.E_NIGHTSCENE);
        sSceneryList.add(MediaSceneTagManager.Tag_Version_0.E_STAR_SKY);
        sSceneryList.add(MediaSceneTagManager.Tag_Version_0.E_SUNRISE_SUNSET);
        sSceneryList.add(MediaSceneTagManager.Tag_Version_0.E_SKY);
        sSceneryList.add(MediaSceneTagManager.Tag_Version_0.E_CITY);
        sSceneryList.add(MediaSceneTagManager.Tag_Version_0.E_GARDEN);
        sSceneryList.add(MediaSceneTagManager.Tag_Version_0.E_BUILDING);
        sSceneryList.add(MediaSceneTagManager.Tag_Version_0.E_CASTLE);
        sSceneryList.add(MediaSceneTagManager.Tag_Version_0.E_CHURCH);
        sSceneryList.add(MediaSceneTagManager.Tag_Version_0.E_TEMPLE);
        sSceneryList.add(MediaSceneTagManager.Tag_Version_0.E_PALACE);
        sSceneryList.add(MediaSceneTagManager.Tag_Version_0.E_BRIDGE);
        sSceneryList.add(MediaSceneTagManager.Tag_Version_0.E_STREET_VIEW);
        sSceneryList.add(MediaSceneTagManager.Tag_Version_0.E_SEA);
        sSceneryList.add(MediaSceneTagManager.Tag_Version_0.E_BEACH);
        sSceneryList.add(MediaSceneTagManager.Tag_Version_0.E_WOODS);
        sSceneryList.add(MediaSceneTagManager.Tag_Version_0.E_LAKE_RIVER);
        sSceneryList.add(MediaSceneTagManager.Tag_Version_0.E_GRASSLAND);
        sSceneryList.add(MediaSceneTagManager.Tag_Version_0.E_DESERT);
        sSceneryList.add(MediaSceneTagManager.Tag_Version_0.E_GORGE);
        sSceneryList.add(MediaSceneTagManager.Tag_Version_0.E_MOUNTAIN);
        sSceneryList.add(MediaSceneTagManager.Tag_Version_0.E_WATERFALL);
        sObjectList.add(MediaSceneTagManager.Tag_Version_0.E_TREE);
        sObjectList.add(MediaSceneTagManager.Tag_Version_0.E_FLOWER);
        List<MediaSceneTagManager.Tag_Version_0> list = sObjectList;
        MediaSceneTagManager.Tag_Version_0 tag_Version_0 = MediaSceneTagManager.Tag_Version_0.E_CAT;
        list.add(tag_Version_0);
        List<MediaSceneTagManager.Tag_Version_0> list2 = sObjectList;
        MediaSceneTagManager.Tag_Version_0 tag_Version_02 = MediaSceneTagManager.Tag_Version_0.E_DOG;
        list2.add(tag_Version_02);
        sObjectList.add(MediaSceneTagManager.Tag_Version_0.E_PIG);
        sObjectList.add(MediaSceneTagManager.Tag_Version_0.E_BIRD);
        sObjectList.add(MediaSceneTagManager.Tag_Version_0.E_WILD_ANIMAL);
        List<MediaSceneTagManager.Tag_Version_0> list3 = sObjectList;
        MediaSceneTagManager.Tag_Version_0 tag_Version_03 = MediaSceneTagManager.Tag_Version_0.E_FRUIT;
        list3.add(tag_Version_03);
        List<MediaSceneTagManager.Tag_Version_0> list4 = sObjectList;
        MediaSceneTagManager.Tag_Version_0 tag_Version_04 = MediaSceneTagManager.Tag_Version_0.E_DESSERT;
        list4.add(tag_Version_04);
        List<MediaSceneTagManager.Tag_Version_0> list5 = sObjectList;
        MediaSceneTagManager.Tag_Version_0 tag_Version_05 = MediaSceneTagManager.Tag_Version_0.E_DRINK;
        list5.add(tag_Version_05);
        List<MediaSceneTagManager.Tag_Version_0> list6 = sObjectList;
        MediaSceneTagManager.Tag_Version_0 tag_Version_06 = MediaSceneTagManager.Tag_Version_0.E_BARBECUE;
        list6.add(tag_Version_06);
        List<MediaSceneTagManager.Tag_Version_0> list7 = sObjectList;
        MediaSceneTagManager.Tag_Version_0 tag_Version_07 = MediaSceneTagManager.Tag_Version_0.E_HOTPOT;
        list7.add(tag_Version_07);
        List<MediaSceneTagManager.Tag_Version_0> list8 = sObjectList;
        MediaSceneTagManager.Tag_Version_0 tag_Version_08 = MediaSceneTagManager.Tag_Version_0.E_SEAFOOD;
        list8.add(tag_Version_08);
        List<MediaSceneTagManager.Tag_Version_0> list9 = sObjectList;
        MediaSceneTagManager.Tag_Version_0 tag_Version_09 = MediaSceneTagManager.Tag_Version_0.E_SUSHI;
        list9.add(tag_Version_09);
        List<MediaSceneTagManager.Tag_Version_0> list10 = sObjectList;
        MediaSceneTagManager.Tag_Version_0 tag_Version_010 = MediaSceneTagManager.Tag_Version_0.E_WESTERN_FOOD;
        list10.add(tag_Version_010);
        List<MediaSceneTagManager.Tag_Version_0> list11 = sObjectList;
        MediaSceneTagManager.Tag_Version_0 tag_Version_011 = MediaSceneTagManager.Tag_Version_0.E_COOKED_DISH;
        list11.add(tag_Version_011);
        List<MediaSceneTagManager.Tag_Version_0> list12 = sObjectList;
        MediaSceneTagManager.Tag_Version_0 tag_Version_012 = MediaSceneTagManager.Tag_Version_0.E_STAPLE_FOOD;
        list12.add(tag_Version_012);
        List<MediaSceneTagManager.Tag_Version_0> list13 = sObjectList;
        MediaSceneTagManager.Tag_Version_0 tag_Version_013 = MediaSceneTagManager.Tag_Version_0.E_ICE_CREAM;
        list13.add(tag_Version_013);
        sObjectList.add(MediaSceneTagManager.Tag_Version_0.E_WEDDING);
        sObjectList.add(MediaSceneTagManager.Tag_Version_0.E_DINNER_PARTY);
        sObjectList.add(MediaSceneTagManager.Tag_Version_0.E_GROUP_PHOTO);
        sObjectList.add(MediaSceneTagManager.Tag_Version_0.E_SHOW);
        sObjectList.add(MediaSceneTagManager.Tag_Version_0.E_INSTRUMENTAL_PERFORMANCE);
        sObjectList.add(MediaSceneTagManager.Tag_Version_0.E_DANCE);
        sObjectList.add(MediaSceneTagManager.Tag_Version_0.E_SPORT_BOXING);
        sObjectList.add(MediaSceneTagManager.Tag_Version_0.E_FITNESS);
        sObjectList.add(MediaSceneTagManager.Tag_Version_0.E_BASKETBALL);
        sObjectList.add(MediaSceneTagManager.Tag_Version_0.E_FOOTBALL);
        sObjectList.add(MediaSceneTagManager.Tag_Version_0.E_BADMINTON);
        sObjectList.add(MediaSceneTagManager.Tag_Version_0.E_SPORT_BILLIARDS);
        sObjectList.add(MediaSceneTagManager.Tag_Version_0.E_SPORT_VOLLEYBALL);
        sObjectList.add(MediaSceneTagManager.Tag_Version_0.E_SPROT_TABLE_TENNIS);
        sObjectList.add(MediaSceneTagManager.Tag_Version_0.E_SPORT_GOLF);
        sObjectList.add(MediaSceneTagManager.Tag_Version_0.E_SKIING);
        sObjectList.add(MediaSceneTagManager.Tag_Version_0.E_SKATEBOARDING);
        sObjectList.add(MediaSceneTagManager.Tag_Version_0.E_SPORT_RIDING);
        sObjectList.add(MediaSceneTagManager.Tag_Version_0.E_SPROT_DIVING);
        sObjectList.add(MediaSceneTagManager.Tag_Version_0.E_SWIMMING);
        sObjectList.add(MediaSceneTagManager.Tag_Version_0.E_SPORT_SURFING);
        sHumanList.add(MediaSceneTagManager.Tag_Version_0.E_PERSON_COUNT_SINGLE);
        sHumanList.add(MediaSceneTagManager.Tag_Version_0.E_PERSON_COUNT_MANY);
        sHumanList.add(MediaSceneTagManager.Tag_Version_0.E_PERSON_COUNT_SEVERAL);
        sPetList.add(tag_Version_0);
        sPetList.add(tag_Version_02);
        sFoodList.add(tag_Version_03);
        sFoodList.add(tag_Version_04);
        sFoodList.add(tag_Version_05);
        sFoodList.add(tag_Version_06);
        sFoodList.add(tag_Version_07);
        sFoodList.add(tag_Version_08);
        sFoodList.add(tag_Version_09);
        sFoodList.add(tag_Version_010);
        sFoodList.add(tag_Version_011);
        sFoodList.add(tag_Version_012);
        sFoodList.add(tag_Version_013);
        sBabyList.add(MediaSceneTagManager.Tag_Version_0.E_BABY);
        sBabyList.add(MediaSceneTagManager.Tag_Version_0.E_KID);
    }

    public static int supperCategory(int i) {
        if (i < 100) {
            return -1;
        }
        return i % 100;
    }

    public static int compareImportance(int i, int i2) {
        int i3 = -1;
        int i4 = 0;
        int i5 = -1;
        while (true) {
            int[] iArr = sImportanceSeq;
            if (i4 < iArr.length) {
                int i6 = iArr[i4];
                if (i6 == i) {
                    i5 = i4;
                }
                if (i6 == i2) {
                    i3 = i4;
                }
                i4++;
            } else {
                return i3 - i5;
            }
        }
    }

    public static boolean belongTo(int i, int i2) {
        if (i == i2) {
            return true;
        }
        while (i > i2) {
            if (supperCategory(i) == i2) {
                return true;
            }
            i = supperCategory(i);
        }
        return false;
    }

    public static boolean isClassificationScene(MediaScene mediaScene) {
        return getClassification(mediaScene) > 0;
    }

    public static int getClassification(MediaScene mediaScene) {
        if (mediaScene == null) {
            return -1;
        }
        ArrayList arrayList = new ArrayList();
        for (Map.Entry<Integer, List<MediaSceneTagManager.Tag_Version_0>> entry : sClassificationTagListMap.entrySet()) {
            Iterator<MediaSceneTagManager.Tag_Version_0> it = entry.getValue().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                } else if (it.next().getTagValue() == mediaScene.getSceneTag()) {
                    arrayList.add(entry.getKey());
                    break;
                }
            }
        }
        if (arrayList.size() != 0) {
            return ((Integer) Collections.max(arrayList, Classification$$ExternalSyntheticLambda0.INSTANCE)).intValue();
        }
        return -1;
    }
}
