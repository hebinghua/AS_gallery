package com.miui.gallery.cloudcontrol.strategies;

import com.google.common.collect.ImmutableMap;
import com.google.gson.annotations.SerializedName;
import com.miui.extraphoto.sdk.ExtraPhotoSDK;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.assistant.manager.MediaFeatureManager;
import com.miui.gallery.cloudcontrol.RecommendItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/* loaded from: classes.dex */
public class RecommendStrategy extends BaseStrategy {
    public static final ImmutableMap<String, Boolean> sFeatureList;
    @SerializedName("sublist")
    private ArrayList<RecommendItem> mCandidateList;

    static {
        ImmutableMap.Builder builder = ImmutableMap.builder();
        Boolean bool = Boolean.FALSE;
        ImmutableMap.Builder put = builder.put("slowAdjustment", bool).put("smartSoundtrack", bool).put("dynamicSpot", Boolean.valueOf(ExtraPhotoSDK.isDeviceSupportRefocus(GalleryApp.sGetAndroidContext())));
        Boolean bool2 = Boolean.TRUE;
        sFeatureList = put.put("puzzle", bool2).put("studioLightEffect", Boolean.valueOf(ExtraPhotoSDK.isDeviceSupportRefocus(GalleryApp.sGetAndroidContext()))).put("photoMovie", bool2).put("storyAlbum", Boolean.valueOf(MediaFeatureManager.isDeviceSupportStoryFunction())).put("banner", bool2).build();
    }

    public final ArrayList<RecommendItem> filter() {
        ArrayList<RecommendItem> arrayList = new ArrayList<>();
        Iterator<RecommendItem> it = this.mCandidateList.iterator();
        while (it.hasNext()) {
            RecommendItem next = it.next();
            ImmutableMap<String, Boolean> immutableMap = sFeatureList;
            if (immutableMap.containsKey(next.getKey()) && immutableMap.get(next.getKey()).booleanValue()) {
                arrayList.add(next);
            }
        }
        Collections.sort(arrayList, new Comparator<RecommendItem>() { // from class: com.miui.gallery.cloudcontrol.strategies.RecommendStrategy.1
            @Override // java.util.Comparator
            public int compare(RecommendItem recommendItem, RecommendItem recommendItem2) {
                if (recommendItem.getKey().equals("banner")) {
                    return -1;
                }
                if (!recommendItem2.getKey().equals("banner")) {
                    return Integer.compare(Integer.parseInt(recommendItem2.getSeqId()), Integer.parseInt(recommendItem.getSeqId()));
                }
                return 1;
            }
        });
        return arrayList;
    }

    public ArrayList<RecommendItem> getRecommendItems() {
        return filter();
    }

    public int getMaxSeqId() {
        Iterator<RecommendItem> it = this.mCandidateList.iterator();
        int i = 0;
        while (it.hasNext()) {
            int parseInt = Integer.parseInt(it.next().getSeqId());
            if (parseInt > i) {
                i = parseInt;
            }
        }
        return i;
    }
}
