package com.miui.gallery.assistant.model;

import ch.qos.logback.core.net.SyslogConstants;
import com.miui.gallery.assistant.jni.filter.BaiduSceneResult;
import com.xiaomi.milab.videosdk.message.MsgType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
public class MediaSceneTagManager {
    public static int UNUSUAL_TAG_START_VALUE = 10000;
    public static Set<Integer> sDocumentList = new HashSet();
    public static Map<Integer, Integer> sTransToOldVersionTagMap;

    static {
        HashMap hashMap = new HashMap();
        sTransToOldVersionTagMap = hashMap;
        Integer valueOf = Integer.valueOf(Tag_Version_1.N_FOOD_BAOZI.getTagValue());
        Tag_Version_0 tag_Version_0 = Tag_Version_0.E_STAPLE_FOOD;
        hashMap.put(valueOf, Integer.valueOf(tag_Version_0.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_FOOD_YOUTIAO.getTagValue()), Integer.valueOf(tag_Version_0.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_FOOD_RICE.getTagValue()), Integer.valueOf(tag_Version_0.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_FOOD_WHEAT.getTagValue()), Integer.valueOf(tag_Version_0.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_FOOD_CONGEE.getTagValue()), Integer.valueOf(tag_Version_0.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_FOOD_BREAD.getTagValue()), Integer.valueOf(tag_Version_0.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_FOOD_DUMPLING.getTagValue()), Integer.valueOf(tag_Version_0.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_FOOD_CAKE.getTagValue()), Integer.valueOf(tag_Version_0.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_FOOD_MANTOU.getTagValue()), Integer.valueOf(tag_Version_0.getTagValue()));
        Map<Integer, Integer> map = sTransToOldVersionTagMap;
        Integer valueOf2 = Integer.valueOf(Tag_Version_1.N_INSTRUMENTAL_PERFORMANCE_1.getTagValue());
        Tag_Version_0 tag_Version_02 = Tag_Version_0.E_INSTRUMENTAL_PERFORMANCE;
        map.put(valueOf2, Integer.valueOf(tag_Version_02.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_INSTRUMENTAL_PERFORMANCE_2.getTagValue()), Integer.valueOf(tag_Version_02.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_INSTRUMENTAL_PERFORMANCE_3.getTagValue()), Integer.valueOf(tag_Version_02.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_INSTRUMENTAL_PERFORMANCE_4.getTagValue()), Integer.valueOf(tag_Version_02.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_INSTRUMENTAL_PERFORMANCE_5.getTagValue()), Integer.valueOf(tag_Version_02.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_INSTRUMENTAL_PERFORMANCE_6.getTagValue()), Integer.valueOf(tag_Version_02.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_INSTRUMENTAL_PERFORMANCE_7.getTagValue()), Integer.valueOf(tag_Version_02.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_INSTRUMENTAL_PERFORMANCE_8.getTagValue()), Integer.valueOf(tag_Version_02.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_INSTRUMENTAL_PERFORMANCE_9.getTagValue()), Integer.valueOf(tag_Version_02.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_INSTRUMENTAL_PERFORMANCE_10.getTagValue()), Integer.valueOf(tag_Version_02.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_INSTRUMENTAL_PERFORMANCE_11.getTagValue()), Integer.valueOf(tag_Version_02.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_INSTRUMENTAL_PERFORMANCE_12.getTagValue()), Integer.valueOf(tag_Version_02.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_INSTRUMENTAL_PERFORMANCE_13.getTagValue()), Integer.valueOf(tag_Version_02.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_INSTRUMENTAL_PERFORMANCE_14.getTagValue()), Integer.valueOf(tag_Version_02.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_FITNESS.getTagValue()), Integer.valueOf(Tag_Version_0.E_FITNESS.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_ICE_CREAM.getTagValue()), Integer.valueOf(Tag_Version_0.E_ICE_CREAM.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_SUSHI.getTagValue()), Integer.valueOf(Tag_Version_0.E_SUSHI.getTagValue()));
        Map<Integer, Integer> map2 = sTransToOldVersionTagMap;
        Integer valueOf3 = Integer.valueOf(Tag_Version_1.N_WILD_ANIMAL_1.getTagValue());
        Tag_Version_0 tag_Version_03 = Tag_Version_0.E_WILD_ANIMAL;
        map2.put(valueOf3, Integer.valueOf(tag_Version_03.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WILD_ANIMAL_2.getTagValue()), Integer.valueOf(tag_Version_03.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WILD_ANIMAL_3.getTagValue()), Integer.valueOf(tag_Version_03.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WILD_ANIMAL_4.getTagValue()), Integer.valueOf(tag_Version_03.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WILD_ANIMAL_5.getTagValue()), Integer.valueOf(tag_Version_03.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WILD_ANIMAL_6.getTagValue()), Integer.valueOf(tag_Version_03.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WILD_ANIMAL_7.getTagValue()), Integer.valueOf(tag_Version_03.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WILD_ANIMAL_8.getTagValue()), Integer.valueOf(tag_Version_03.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WILD_ANIMAL_9.getTagValue()), Integer.valueOf(tag_Version_03.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WILD_ANIMAL_10.getTagValue()), Integer.valueOf(tag_Version_03.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WILD_ANIMAL_11.getTagValue()), Integer.valueOf(tag_Version_03.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WILD_ANIMAL_12.getTagValue()), Integer.valueOf(tag_Version_03.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WILD_ANIMAL_13.getTagValue()), Integer.valueOf(tag_Version_03.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WILD_ANIMAL_14.getTagValue()), Integer.valueOf(tag_Version_03.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WILD_ANIMAL_15.getTagValue()), Integer.valueOf(tag_Version_03.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WILD_ANIMAL_16.getTagValue()), Integer.valueOf(tag_Version_03.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WILD_ANIMAL_17.getTagValue()), Integer.valueOf(tag_Version_03.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WILD_ANIMAL_18.getTagValue()), Integer.valueOf(tag_Version_03.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WILD_ANIMAL_19.getTagValue()), Integer.valueOf(tag_Version_03.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WILD_ANIMAL_20.getTagValue()), Integer.valueOf(tag_Version_03.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WILD_ANIMAL_21.getTagValue()), Integer.valueOf(tag_Version_03.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WILD_ANIMAL_22.getTagValue()), Integer.valueOf(tag_Version_03.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WILD_ANIMAL_23.getTagValue()), Integer.valueOf(tag_Version_03.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WILD_ANIMAL_24.getTagValue()), Integer.valueOf(tag_Version_03.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WILD_ANIMAL_25.getTagValue()), Integer.valueOf(tag_Version_03.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WILD_ANIMAL_26.getTagValue()), Integer.valueOf(tag_Version_03.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WILD_ANIMAL_27.getTagValue()), Integer.valueOf(tag_Version_03.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WILD_ANIMAL_28.getTagValue()), Integer.valueOf(tag_Version_03.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WILD_ANIMAL_29.getTagValue()), Integer.valueOf(tag_Version_03.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WILD_ANIMAL_30.getTagValue()), Integer.valueOf(tag_Version_03.getTagValue()));
        Map<Integer, Integer> map3 = sTransToOldVersionTagMap;
        Integer valueOf4 = Integer.valueOf(Tag_Version_1.N_BIRD_1.getTagValue());
        Tag_Version_0 tag_Version_04 = Tag_Version_0.E_BIRD;
        map3.put(valueOf4, Integer.valueOf(tag_Version_04.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_BIRD_2.getTagValue()), Integer.valueOf(tag_Version_04.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_BIRD_3.getTagValue()), Integer.valueOf(tag_Version_04.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_BIRD_4.getTagValue()), Integer.valueOf(tag_Version_04.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_BIRD_5.getTagValue()), Integer.valueOf(tag_Version_04.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_BIRD_6.getTagValue()), Integer.valueOf(tag_Version_04.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_BIRD_7.getTagValue()), Integer.valueOf(tag_Version_04.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_PIG.getTagValue()), Integer.valueOf(Tag_Version_0.E_PIG.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_GROUP_PHOTO.getTagValue()), Integer.valueOf(Tag_Version_0.E_GROUP_PHOTO.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_GARDEN.getTagValue()), Integer.valueOf(Tag_Version_0.E_GARDEN.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_CASTLE.getTagValue()), Integer.valueOf(Tag_Version_0.E_CASTLE.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_CITY.getTagValue()), Integer.valueOf(Tag_Version_0.E_CITY.getTagValue()));
        Map<Integer, Integer> map4 = sTransToOldVersionTagMap;
        Integer valueOf5 = Integer.valueOf(Tag_Version_1.N_NIGHTSCENE.getTagValue());
        Tag_Version_0 tag_Version_05 = Tag_Version_0.E_NIGHTSCENE;
        map4.put(valueOf5, Integer.valueOf(tag_Version_05.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_SKY.getTagValue()), Integer.valueOf(Tag_Version_0.E_SKY.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WEDDING.getTagValue()), Integer.valueOf(Tag_Version_0.E_WEDDING.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_BABY.getTagValue()), Integer.valueOf(Tag_Version_0.E_BABY.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_PALACE.getTagValue()), Integer.valueOf(Tag_Version_0.E_PALACE.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_TEMPLE.getTagValue()), Integer.valueOf(Tag_Version_0.E_TEMPLE.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_KID.getTagValue()), Integer.valueOf(Tag_Version_0.E_KID.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_MOUNTAIN.getTagValue()), Integer.valueOf(Tag_Version_0.E_MOUNTAIN.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_GORGE.getTagValue()), Integer.valueOf(Tag_Version_0.E_GORGE.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_BUILDING.getTagValue()), Integer.valueOf(Tag_Version_0.E_BUILDING.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_CHURCH.getTagValue()), Integer.valueOf(Tag_Version_0.E_CHURCH.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_SUNRISE_SUNSET.getTagValue()), Integer.valueOf(Tag_Version_0.E_SUNRISE_SUNSET.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_STAR_SKY.getTagValue()), Integer.valueOf(Tag_Version_0.E_STAR_SKY.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_TREE.getTagValue()), Integer.valueOf(Tag_Version_0.E_TREE.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WOODS.getTagValue()), Integer.valueOf(Tag_Version_0.E_WOODS.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_BRIDGE.getTagValue()), Integer.valueOf(Tag_Version_0.E_BRIDGE.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_FRUIT.getTagValue()), Integer.valueOf(Tag_Version_0.E_FRUIT.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_DESERT.getTagValue()), Integer.valueOf(Tag_Version_0.E_DESERT.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_SEA.getTagValue()), Integer.valueOf(Tag_Version_0.E_SEA.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_BEACH.getTagValue()), Integer.valueOf(Tag_Version_0.E_BEACH.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_SEAFOOD.getTagValue()), Integer.valueOf(Tag_Version_0.E_SEAFOOD.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_SWIMMING.getTagValue()), Integer.valueOf(Tag_Version_0.E_SWIMMING.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_LAKE_RIVER.getTagValue()), Integer.valueOf(Tag_Version_0.E_LAKE_RIVER.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_SKATEBOARDING.getTagValue()), Integer.valueOf(Tag_Version_0.E_SKATEBOARDING.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_SKIING.getTagValue()), Integer.valueOf(Tag_Version_0.E_SKIING.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_SHOW.getTagValue()), Integer.valueOf(Tag_Version_0.E_SHOW.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WATERFALL.getTagValue()), Integer.valueOf(Tag_Version_0.E_WATERFALL.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_HOTPOT.getTagValue()), Integer.valueOf(Tag_Version_0.E_HOTPOT.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_COOKED_DISH.getTagValue()), Integer.valueOf(Tag_Version_0.E_COOKED_DISH.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_FIRWORKS.getTagValue()), Integer.valueOf(tag_Version_05.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_BARBECUE.getTagValue()), Integer.valueOf(Tag_Version_0.E_BARBECUE.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_DOG.getTagValue()), Integer.valueOf(Tag_Version_0.E_DOG.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_CAT.getTagValue()), Integer.valueOf(Tag_Version_0.E_CAT.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_DESSERT.getTagValue()), Integer.valueOf(Tag_Version_0.E_DESSERT.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_BASKETBALL.getTagValue()), Integer.valueOf(Tag_Version_0.E_BASKETBALL.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_BADMINTON.getTagValue()), Integer.valueOf(Tag_Version_0.E_BADMINTON.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_DINNER_PARTY.getTagValue()), Integer.valueOf(Tag_Version_0.E_DINNER_PARTY.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_DANCE.getTagValue()), Integer.valueOf(Tag_Version_0.E_DANCE.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_FLOWER.getTagValue()), Integer.valueOf(Tag_Version_0.E_FLOWER.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_GRASSLAND.getTagValue()), Integer.valueOf(Tag_Version_0.E_GRASSLAND.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_STREET_VIEW.getTagValue()), Integer.valueOf(Tag_Version_0.E_STREET_VIEW.getTagValue()));
        Map<Integer, Integer> map5 = sTransToOldVersionTagMap;
        Integer valueOf6 = Integer.valueOf(Tag_Version_1.N_WESTERN_FOOD_1.getTagValue());
        Tag_Version_0 tag_Version_06 = Tag_Version_0.E_WESTERN_FOOD;
        map5.put(valueOf6, Integer.valueOf(tag_Version_06.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WESTERN_FOOD_2.getTagValue()), Integer.valueOf(tag_Version_06.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_WESTERN_FOOD_3.getTagValue()), Integer.valueOf(tag_Version_06.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_FOOTBALL.getTagValue()), Integer.valueOf(Tag_Version_0.E_FOOTBALL.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_SPROT_TABLE_TENNIS.getTagValue()), Integer.valueOf(Tag_Version_0.E_SPROT_TABLE_TENNIS.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_SPORT_SURFING.getTagValue()), Integer.valueOf(Tag_Version_0.E_SPORT_SURFING.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_SPORT_BILLIARDS.getTagValue()), Integer.valueOf(Tag_Version_0.E_SPORT_BILLIARDS.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_SPORT_BOXING.getTagValue()), Integer.valueOf(Tag_Version_0.E_SPORT_BOXING.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_SPORT_VOLLEYBALL.getTagValue()), Integer.valueOf(Tag_Version_0.E_SPORT_VOLLEYBALL.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_SPROT_DIVING.getTagValue()), Integer.valueOf(Tag_Version_0.E_SPROT_DIVING.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_SPORT_RIDING.getTagValue()), Integer.valueOf(Tag_Version_0.E_SPORT_RIDING.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_SPORT_GOLF.getTagValue()), Integer.valueOf(Tag_Version_0.E_SPORT_GOLF.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_SNOW.getTagValue()), Integer.valueOf(Tag_Version_0.E_SNOW.getTagValue()));
        sTransToOldVersionTagMap.put(Integer.valueOf(Tag_Version_1.N_DRINK.getTagValue()), Integer.valueOf(Tag_Version_0.E_DRINK.getTagValue()));
        Map<Integer, Integer> map6 = sTransToOldVersionTagMap;
        Tag_Version_1 tag_Version_1 = Tag_Version_1.N_INVOICE;
        map6.put(Integer.valueOf(tag_Version_1.getTagValue()), Integer.valueOf(Tag_Version_0.E_INVOICE.getTagValue()));
        Map<Integer, Integer> map7 = sTransToOldVersionTagMap;
        Tag_Version_1 tag_Version_12 = Tag_Version_1.N_BUSINESS_CARD;
        map7.put(Integer.valueOf(tag_Version_12.getTagValue()), Integer.valueOf(Tag_Version_0.E_BUSINESS_CARD.getTagValue()));
        Map<Integer, Integer> map8 = sTransToOldVersionTagMap;
        Tag_Version_1 tag_Version_13 = Tag_Version_1.N_EXPRESS_RECEIPT;
        map8.put(Integer.valueOf(tag_Version_13.getTagValue()), Integer.valueOf(Tag_Version_0.E_EXPRESS_RECEIPT.getTagValue()));
        Map<Integer, Integer> map9 = sTransToOldVersionTagMap;
        Tag_Version_1 tag_Version_14 = Tag_Version_1.N_HUKOU;
        map9.put(Integer.valueOf(tag_Version_14.getTagValue()), Integer.valueOf(Tag_Version_0.E_HUKOU.getTagValue()));
        Map<Integer, Integer> map10 = sTransToOldVersionTagMap;
        Tag_Version_1 tag_Version_15 = Tag_Version_1.N_PASSPORT;
        map10.put(Integer.valueOf(tag_Version_15.getTagValue()), Integer.valueOf(Tag_Version_0.E_PASSPORT.getTagValue()));
        Map<Integer, Integer> map11 = sTransToOldVersionTagMap;
        Tag_Version_1 tag_Version_16 = Tag_Version_1.N_TRAIN_TICKET;
        map11.put(Integer.valueOf(tag_Version_16.getTagValue()), Integer.valueOf(Tag_Version_0.E_TRAIN_TICKET.getTagValue()));
        Map<Integer, Integer> map12 = sTransToOldVersionTagMap;
        Tag_Version_1 tag_Version_17 = Tag_Version_1.N_SOCIAL_SECURITY_CARD;
        map12.put(Integer.valueOf(tag_Version_17.getTagValue()), Integer.valueOf(Tag_Version_0.E_SOCIAL_SECURITY_CARD.getTagValue()));
        Map<Integer, Integer> map13 = sTransToOldVersionTagMap;
        Tag_Version_1 tag_Version_18 = Tag_Version_1.N_ID_CARD;
        map13.put(Integer.valueOf(tag_Version_18.getTagValue()), Integer.valueOf(Tag_Version_0.E_ID_CARD.getTagValue()));
        Map<Integer, Integer> map14 = sTransToOldVersionTagMap;
        Tag_Version_1 tag_Version_19 = Tag_Version_1.N_BANK_CARD;
        map14.put(Integer.valueOf(tag_Version_19.getTagValue()), Integer.valueOf(Tag_Version_0.E_BANK_CARD.getTagValue()));
        Map<Integer, Integer> map15 = sTransToOldVersionTagMap;
        Tag_Version_1 tag_Version_110 = Tag_Version_1.N_ENTRAINCE_TICKET;
        map15.put(Integer.valueOf(tag_Version_110.getTagValue()), Integer.valueOf(Tag_Version_0.E_ENTRAINCE_TICKET.getTagValue()));
        Map<Integer, Integer> map16 = sTransToOldVersionTagMap;
        Tag_Version_1 tag_Version_111 = Tag_Version_1.N_SCREEN_WORDS;
        Integer valueOf7 = Integer.valueOf(tag_Version_111.getTagValue());
        Tag_Version_0 tag_Version_07 = Tag_Version_0.E_SCREEN_WORDS;
        map16.put(valueOf7, Integer.valueOf(tag_Version_07.getTagValue()));
        Map<Integer, Integer> map17 = sTransToOldVersionTagMap;
        Tag_Version_1 tag_Version_112 = Tag_Version_1.N_SCREEN_PPT;
        map17.put(Integer.valueOf(tag_Version_112.getTagValue()), Integer.valueOf(tag_Version_07.getTagValue()));
        sDocumentList.add(Integer.valueOf(tag_Version_1.getTagValue()));
        sDocumentList.add(Integer.valueOf(tag_Version_12.getTagValue()));
        sDocumentList.add(Integer.valueOf(tag_Version_13.getTagValue()));
        sDocumentList.add(Integer.valueOf(tag_Version_14.getTagValue()));
        sDocumentList.add(Integer.valueOf(tag_Version_15.getTagValue()));
        sDocumentList.add(Integer.valueOf(tag_Version_16.getTagValue()));
        sDocumentList.add(Integer.valueOf(tag_Version_17.getTagValue()));
        sDocumentList.add(Integer.valueOf(tag_Version_18.getTagValue()));
        sDocumentList.add(Integer.valueOf(tag_Version_19.getTagValue()));
        sDocumentList.add(Integer.valueOf(tag_Version_110.getTagValue()));
        sDocumentList.add(Integer.valueOf(tag_Version_111.getTagValue()));
        sDocumentList.add(Integer.valueOf(tag_Version_112.getTagValue()));
        sDocumentList.add(Integer.valueOf(Tag_Version_1.N_DOCUMENT_CARD_1.getTagValue()));
        sDocumentList.add(Integer.valueOf(Tag_Version_1.N_DOCUMENT_CARD_2.getTagValue()));
        sDocumentList.add(Integer.valueOf(Tag_Version_1.N_DOCUMENT_CARD_3.getTagValue()));
        sDocumentList.add(Integer.valueOf(Tag_Version_1.N_DOCUMENT_CARD_4.getTagValue()));
        sDocumentList.add(Integer.valueOf(Tag_Version_1.N_DOCUMENT_CARD_5.getTagValue()));
        sDocumentList.add(Integer.valueOf(Tag_Version_1.N_DOCUMENT_CARD_6.getTagValue()));
        sDocumentList.add(Integer.valueOf(Tag_Version_1.N_DOCUMENT_CARD_7.getTagValue()));
        sDocumentList.add(Integer.valueOf(Tag_Version_1.N_DOCUMENT_CARD_8.getTagValue()));
        sDocumentList.add(Integer.valueOf(Tag_Version_1.N_DOCUMENT_CARD_9.getTagValue()));
        sDocumentList.add(Integer.valueOf(Tag_Version_1.N_DOCUMENT_CARD_10.getTagValue()));
        sDocumentList.add(Integer.valueOf(Tag_Version_1.N_DOCUMENT_CARD_11.getTagValue()));
        sDocumentList.add(Integer.valueOf(Tag_Version_1.N_DOCUMENT_CARD_12.getTagValue()));
        sDocumentList.add(Integer.valueOf(Tag_Version_1.N_DOCUMENT_CARD_13.getTagValue()));
        sDocumentList.add(Integer.valueOf(Tag_Version_1.N_DOCUMENT_CARD_14.getTagValue()));
        sDocumentList.add(Integer.valueOf(Tag_Version_1.N_DOCUMENT_CARD_15.getTagValue()));
        sDocumentList.add(Integer.valueOf(Tag_Version_1.N_DOCUMENT_CARD_16.getTagValue()));
        sDocumentList.add(Integer.valueOf(Tag_Version_1.N_DOCUMENT_CARD_17.getTagValue()));
        sDocumentList.add(Integer.valueOf(Tag_Version_1.N_DOCUMENT_CARD_18.getTagValue()));
        sDocumentList.add(Integer.valueOf(Tag_Version_1.N_DOCUMENT_CARD_19.getTagValue()));
        sDocumentList.add(Integer.valueOf(Tag_Version_1.N_DOCUMENT_CARD_20.getTagValue()));
        sDocumentList.add(Integer.valueOf(Tag_Version_1.N_DOCUMENT_CARD_21.getTagValue()));
    }

    /* loaded from: classes.dex */
    public enum Tag_Version_0 {
        E_BABY(0),
        E_KID(1),
        E_CAT(2),
        E_DOG(3),
        E_PIG(4),
        E_BIRD(5),
        E_WILD_ANIMAL(6),
        E_WEDDING(7),
        E_INSTRUMENTAL_PERFORMANCE(8),
        E_SHOW(9),
        E_GROUP_PHOTO(10),
        E_DINNER_PARTY(11),
        E_ICE_CREAM(12),
        E_DESSERT(13),
        E_DRINK(14),
        E_BARBECUE(15),
        E_HOTPOT(16),
        E_SEAFOOD(17),
        E_SUSHI(18),
        E_WESTERN_FOOD(19),
        E_COOKED_DISH(21),
        E_STAPLE_FOOD(22),
        E_CITY(23),
        E_TEMPLE(24),
        E_PALACE(25),
        E_GARDEN(26),
        E_CHURCH(27),
        E_CASTLE(28),
        E_STREET_VIEW(29),
        E_BRIDGE(30),
        E_BUILDING(31),
        E_NIGHTSCENE(32),
        E_SKIING(33),
        E_SWIMMING(34),
        E_BASKETBALL(35),
        E_FOOTBALL(36),
        E_BADMINTON(37),
        E_DANCE(38),
        E_SKATEBOARDING(39),
        E_FITNESS(40),
        E_SEA(41),
        E_BEACH(42),
        E_WOODS(43),
        E_TREE(44),
        E_FLOWER(45),
        E_LAKE_RIVER(46),
        E_SNOW(47),
        E_SKY(48),
        E_STAR_SKY(49),
        E_SUNRISE_SUNSET(50),
        E_GRASSLAND(51),
        E_DESERT(52),
        E_WATERFALL(53),
        E_MOUNTAIN(54),
        E_GORGE(55),
        E_INVOICE(56),
        E_BUSINESS_CARD(57),
        E_EXPRESS_RECEIPT(58),
        E_HUKOU(59),
        E_PASSPORT(60),
        E_TRAIN_TICKET(61),
        E_SOCIAL_SECURITY_CARD(62),
        E_ID_CARD(63),
        E_BANK_CARD(64),
        E_ENTRAINCE_TICKET(65),
        E_SCREEN_WORDS(66),
        E_SPROT_TABLE_TENNIS(67),
        E_SPORT_SURFING(68),
        E_SPORT_BILLIARDS(69),
        E_SPORT_BOXING(70),
        E_SPORT_VOLLEYBALL(71),
        E_SPROT_DIVING(72),
        E_SPORT_RIDING(73),
        E_SPORT_GOLF(74),
        E_FRUIT(75),
        E_IndianAadhaar(76),
        E_IndianBankcard(77),
        E_IndianDriverlicence(78),
        E_IndianPassport(79),
        E_IndianPermanentAccountNumber(80),
        E_IndianRailwayTicket(81),
        E_IndianVoterID(82),
        E_DOCUMENT(83),
        E_SHOT_TYPE_DAQUANJING(MsgType.XMSCONTEXT),
        E_SHOT_TYPE_RENJINZHONGJING(MsgType.XMSEXPORT),
        E_SHOT_TYPE_RENQUANJING(MsgType.XMSTRANSCODE),
        E_SHOT_TYPE_RENTEXIE(MsgType.XMSAUDIOEXTRACT),
        E_SHOT_TYPE_WUJINZHONGJING(MsgType.XMSTIMELINE),
        E_SHOT_TYPE_WUQUANJING(10006),
        E_SHOT_TYPE_WUTEXIE(10007),
        E_INDOOR(20001),
        E_OUTDOOR(20002),
        E_PERSON_COUNT_NONE(30001),
        E_PERSON_COUNT_SINGLE(30002),
        E_PERSON_COUNT_SEVERAL(30003),
        E_PERSON_COUNT_MANY(30004),
        E_NATURAL(40001),
        E_NONE_NATURAL(40002),
        E_NATURAL_AND_NONE_NATURAL(40003);
        
        private final int mTag;

        Tag_Version_0(int i) {
            this.mTag = i;
        }

        public int getTagValue() {
            return this.mTag;
        }
    }

    /* loaded from: classes.dex */
    public enum Tag_Version_1 {
        N_FOOD_BAOZI(15),
        N_FOOD_YOUTIAO(16),
        N_FOOD_RICE(17),
        N_FOOD_WHEAT(18),
        N_FOOD_CONGEE(19),
        N_FOOD_BREAD(20),
        N_FOOD_DUMPLING(21),
        N_FOOD_CAKE(22),
        N_FOOD_MANTOU(23),
        N_INSTRUMENTAL_PERFORMANCE_1(24),
        N_FITNESS(26),
        N_ICE_CREAM(27),
        N_SUSHI(28),
        N_BIRD_1(32),
        N_BIRD_2(33),
        N_BIRD_3(59),
        N_BIRD_4(60),
        N_BIRD_5(62),
        N_BIRD_6(63),
        N_BIRD_7(BaiduSceneResult.SCREEN_TEXT),
        N_WILD_ANIMAL_1(29),
        N_WILD_ANIMAL_2(30),
        N_WILD_ANIMAL_3(31),
        N_WILD_ANIMAL_4(34),
        N_WILD_ANIMAL_5(35),
        N_WILD_ANIMAL_6(36),
        N_WILD_ANIMAL_7(37),
        N_WILD_ANIMAL_8(38),
        N_WILD_ANIMAL_9(39),
        N_WILD_ANIMAL_10(40),
        N_WILD_ANIMAL_11(41),
        N_WILD_ANIMAL_12(42),
        N_WILD_ANIMAL_13(43),
        N_WILD_ANIMAL_14(44),
        N_WILD_ANIMAL_15(45),
        N_WILD_ANIMAL_16(47),
        N_WILD_ANIMAL_17(48),
        N_WILD_ANIMAL_18(49),
        N_WILD_ANIMAL_19(50),
        N_WILD_ANIMAL_20(51),
        N_WILD_ANIMAL_21(52),
        N_WILD_ANIMAL_22(53),
        N_WILD_ANIMAL_23(54),
        N_WILD_ANIMAL_24(55),
        N_WILD_ANIMAL_25(56),
        N_WILD_ANIMAL_26(57),
        N_WILD_ANIMAL_27(58),
        N_WILD_ANIMAL_28(61),
        N_WILD_ANIMAL_29(64),
        N_WILD_ANIMAL_30(65),
        N_PIG(46),
        N_GROUP_PHOTO(66),
        N_GARDEN(68),
        N_CASTLE(69),
        N_CITY(70),
        N_NIGHTSCENE(72),
        N_SKY(73),
        N_WEDDING(74),
        N_BABY(75),
        N_PALACE(76),
        N_TEMPLE(77),
        N_KID(78),
        N_MOUNTAIN(79),
        N_GORGE(80),
        N_BUILDING(81),
        N_CHURCH(83),
        N_SUNRISE_SUNSET(84),
        N_STAR_SKY(85),
        N_TREE(86),
        N_WOODS(87),
        N_BRIDGE(88),
        N_FRUIT(90),
        N_DESERT(91),
        N_SEA(92),
        N_BEACH(93),
        N_SEAFOOD(94),
        N_SWIMMING(97),
        N_LAKE_RIVER(98),
        N_SKATEBOARDING(99),
        N_SKIING(100),
        N_SHOW(101),
        N_WATERFALL(102),
        N_HOTPOT(103),
        N_COOKED_DISH(104),
        N_FIRWORKS(106),
        N_BARBECUE(107),
        N_DOG(108),
        N_CAT(109),
        N_DESSERT(110),
        N_BASKETBALL(111),
        N_BADMINTON(112),
        N_DINNER_PARTY(114),
        N_DANCE(115),
        N_FLOWER(116),
        N_GRASSLAND(117),
        N_STREET_VIEW(118),
        N_WESTERN_FOOD_1(119),
        N_WESTERN_FOOD_2(120),
        N_WESTERN_FOOD_3(121),
        N_FOOTBALL(123),
        N_SPROT_TABLE_TENNIS(124),
        N_SPORT_SURFING(125),
        N_SPORT_BILLIARDS(126),
        N_SPORT_BOXING(BaiduSceneResult.BANK_CARD),
        N_SPORT_VOLLEYBALL(128),
        N_SPROT_DIVING(BaiduSceneResult.ACCOUNT_BOOK),
        N_SPORT_RIDING(BaiduSceneResult.VISA),
        N_SPORT_GOLF(BaiduSceneResult.INVOICE),
        N_SNOW(BaiduSceneResult.VARIOUS_TICKETS),
        N_DRINK(BaiduSceneResult.EXPRESS_ORDER),
        N_INSTRUMENTAL_PERFORMANCE_2(SyslogConstants.LOG_LOCAL7),
        N_INSTRUMENTAL_PERFORMANCE_3(185),
        N_INSTRUMENTAL_PERFORMANCE_4(186),
        N_INSTRUMENTAL_PERFORMANCE_5(187),
        N_INSTRUMENTAL_PERFORMANCE_6(188),
        N_INSTRUMENTAL_PERFORMANCE_7(189),
        N_INSTRUMENTAL_PERFORMANCE_8(190),
        N_INSTRUMENTAL_PERFORMANCE_9(191),
        N_INSTRUMENTAL_PERFORMANCE_10(192),
        N_INSTRUMENTAL_PERFORMANCE_11(193),
        N_INSTRUMENTAL_PERFORMANCE_12(194),
        N_INSTRUMENTAL_PERFORMANCE_13(195),
        N_INSTRUMENTAL_PERFORMANCE_14(196),
        N_INVOICE(BaiduSceneResult.COSMETICS),
        N_BUSINESS_CARD(BaiduSceneResult.FASHION_OTHER),
        N_EXPRESS_RECEIPT(BaiduSceneResult.CARTOON),
        N_HUKOU(BaiduSceneResult.GAME),
        N_PASSPORT(BaiduSceneResult.DIGITAL_PRODUCT),
        N_TRAIN_TICKET(BaiduSceneResult.BLACK_WHITE),
        N_SOCIAL_SECURITY_CARD(144),
        N_ID_CARD(145),
        N_BANK_CARD(146),
        N_ENTRAINCE_TICKET(147),
        N_SCREEN_WORDS(148),
        N_SCREEN_PPT(199),
        N_DOCUMENT_CARD_1(952),
        N_DOCUMENT_CARD_2(953),
        N_DOCUMENT_CARD_3(954),
        N_DOCUMENT_CARD_4(955),
        N_DOCUMENT_CARD_5(956),
        N_DOCUMENT_CARD_6(957),
        N_DOCUMENT_CARD_7(958),
        N_DOCUMENT_CARD_8(959),
        N_DOCUMENT_CARD_9(960),
        N_DOCUMENT_CARD_10(961),
        N_DOCUMENT_CARD_11(962),
        N_DOCUMENT_CARD_12(963),
        N_DOCUMENT_CARD_13(964),
        N_DOCUMENT_CARD_14(965),
        N_DOCUMENT_CARD_15(966),
        N_DOCUMENT_CARD_16(967),
        N_DOCUMENT_CARD_17(968),
        N_DOCUMENT_CARD_18(969),
        N_DOCUMENT_CARD_19(970),
        N_DOCUMENT_CARD_20(971),
        N_DOCUMENT_CARD_21(972);
        
        private final int mTag;

        Tag_Version_1(int i) {
            this.mTag = i;
        }

        public int getTagValue() {
            return this.mTag;
        }
    }

    public static int transferToOldTagValue(int i) {
        return sTransToOldVersionTagMap.containsKey(Integer.valueOf(i)) ? sTransToOldVersionTagMap.get(Integer.valueOf(i)).intValue() : i;
    }
}
