package com.miui.gallery.app;

import com.miui.gallery.analytics.TrackController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class AutoTracking {
    public static List<String> mAutoTrackList = null;
    public static Map<String, String> mNavMap = null;
    public static String mRef = "";
    public static Map<String, String> mViewMap;

    static {
        HashMap hashMap = new HashMap(26);
        mNavMap = hashMap;
        hashMap.put("com.miui.gallery.share.NormalShareAlbumSharerFragment", "403.23.0.1.11527");
        mNavMap.put("com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumFragment", "403.39.0.1.11539");
        mNavMap.put("com.miui.gallery.ui.album.main.AlbumTabFragment", "403.7.0.1.11543");
        mNavMap.put("com.miui.gallery.ui.album.otheralbum.OtherAlbumFragment", "403.40.0.1.11540");
        mNavMap.put("com.miui.gallery.ui.PhotoPreviewSelectFragment", "403.37.0.1.11533");
        mNavMap.put("com.miui.gallery.ui.BabyAlbumDetailFragment", "403.42.0.1.11528");
        mNavMap.put("com.miui.gallery.ui.BackupDetailFragment", "403.28.0.1.11529");
        mNavMap.put("com.miui.gallery.share.NormalShareAlbumOwnerFragment", "403.23.0.1.11527");
        mNavMap.put("com.miui.gallery.search.resultpage.SearchResultFragment", "403.50.0.1.11531");
        mNavMap.put("com.miui.gallery.ui.HomePageFragment", "403.1.0.1.11542");
        mNavMap.put("com.miui.gallery.ui.PhotoPageFragment", "403.11.0.1.11536");
        mNavMap.put("com.miui.gallery.ui.TrashFragment", "403.21.0.1.11530");
        mNavMap.put("com.miui.gallery.ui.PicToPdfPreviewFragment", "403.71.1.1.16988");
        mNavMap.put("com.miui.gallery.search.resultpage.SearchImageResultFragment", "403.50.0.1.11531");
        mNavMap.put("com.miui.gallery.ui.ModernAlbumDetailFragment", "403.15.0.1.11535");
        mNavMap.put("com.miui.gallery.ui.album.aialbum.AIAlbumPageFragment", "403.16.0.1.12189");
        mNavMap.put("com.miui.gallery.search.SearchFragment", "403.20.0.1.11532");
        mNavMap.put("com.miui.gallery.ui.LegacyAlbumDetailFragment", "403.15.0.1.11535");
        mNavMap.put("com.miui.gallery.biz.story.StoryAlbumFragment", "403.38.0.1.11537");
        mNavMap.put("com.miui.gallery.ui.MapFragment", "403.61.0.1.15375");
        mNavMap.put("com.miui.gallery.ui.GallerySettingsFragment", "403.52.0.1.11525");
        mNavMap.put("com.miui.gallery.ui.SecretAlbumDetailFragment", "403.51.0.1.11524");
        mNavMap.put("com.miui.gallery.ui.CleanerFragment", "403.27.0.1.11526");
        mNavMap.put("com.miui.gallery.card.ui.cardlist.AssistantPageFragment", "403.8.0.1.11538");
        mNavMap.put("com.miui.gallery.ui.TextEditFragment", "403.43.0.1.12030");
        mNavMap.put("com.miui.gallery.ui.BackupSettingsFragment", "403.22.1.1.16891");
        HashMap hashMap2 = new HashMap(26);
        mViewMap = hashMap2;
        hashMap2.put("com.miui.gallery.share.NormalShareAlbumSharerFragment", "403.23.0.1.11308");
        mViewMap.put("com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumFragment", "403.39.0.1.11132");
        mViewMap.put("com.miui.gallery.ui.album.main.AlbumTabFragment", "403.7.0.1.10328");
        mViewMap.put("com.miui.gallery.ui.album.otheralbum.OtherAlbumFragment", "403.40.0.1.11123");
        mViewMap.put("com.miui.gallery.ui.PhotoPreviewSelectFragment", "403.37.0.1.11230");
        mViewMap.put("com.miui.gallery.ui.BabyAlbumDetailFragment", "403.42.1.1.11290");
        mViewMap.put("com.miui.gallery.ui.BackupDetailFragment", "403.28.2.1.11289");
        mViewMap.put("com.miui.gallery.share.NormalShareAlbumOwnerFragment", "403.23.0.1.11308");
        mViewMap.put("com.miui.gallery.search.resultpage.SearchResultFragment", "403.50.0.1.11273");
        mViewMap.put("com.miui.gallery.ui.HomePageFragment", "403.1.2.1.9881");
        mViewMap.put("com.miui.gallery.ui.PhotoPageFragment", "403.11.0.1.11151");
        mViewMap.put("com.miui.gallery.ui.TrashFragment", "403.21.0.1.11274");
        mViewMap.put("com.miui.gallery.ui.PicToPdfPreviewFragment", "403.71.1.1.16987");
        mViewMap.put("com.miui.gallery.search.resultpage.SearchImageResultFragment", "403.50.0.1.11273");
        mViewMap.put("com.miui.gallery.ui.ModernAlbumDetailFragment", "403.15.1.1.11176");
        mViewMap.put("com.miui.gallery.ui.album.aialbum.AIAlbumPageFragment", "403.16.0.1.12191");
        mViewMap.put("com.miui.gallery.search.SearchFragment", "403.20.0.1.11265");
        mViewMap.put("com.miui.gallery.ui.LegacyAlbumDetailFragment", "403.15.1.1.11176");
        mViewMap.put("com.miui.gallery.biz.story.StoryAlbumFragment", "403.38.0.1.11150");
        mViewMap.put("com.miui.gallery.ui.MapFragment", "403.61.0.1.15327");
        mViewMap.put("com.miui.gallery.ui.GallerySettingsFragment", "403.22.0.1.11353");
        mViewMap.put("com.miui.gallery.ui.SecretAlbumDetailFragment", "403.51.0.1.11419");
        mViewMap.put("com.miui.gallery.ui.CleanerFragment", "403.27.0.1.11312");
        mViewMap.put("com.miui.gallery.card.ui.cardlist.AssistantPageFragment", "403.8.0.1.11136");
        mViewMap.put("com.miui.gallery.ui.TextEditFragment", "403.43.0.1.11171");
        mViewMap.put("com.miui.gallery.ui.BackupSettingsFragment", "403.22.1.1.16835");
        ArrayList arrayList = new ArrayList(14);
        mAutoTrackList = arrayList;
        arrayList.add("com.miui.gallery.search.SearchFragment");
        mAutoTrackList.add("com.miui.gallery.search.resultpage.SearchResultFragment");
        mAutoTrackList.add("com.miui.gallery.search.resultpage.SearchImageResultFragment");
        mAutoTrackList.add("com.miui.gallery.ui.PhotoPreviewSelectFragment");
        mAutoTrackList.add("com.miui.gallery.ui.GallerySettingsFragment");
        mAutoTrackList.add("com.miui.gallery.ui.BackupSettingsFragment");
        mAutoTrackList.add("com.miui.gallery.ui.BabyAlbumDetailFragment");
        mAutoTrackList.add("com.miui.gallery.ui.PhotoPageFragment");
        mAutoTrackList.add("com.miui.gallery.ui.TrashFragment");
        mAutoTrackList.add("com.miui.gallery.ui.PicToPdfPreviewFragment");
        mAutoTrackList.add("com.miui.gallery.ui.album.aialbum.AIAlbumPageFragment");
        mAutoTrackList.add("com.miui.gallery.ui.album.otheralbum.OtherAlbumFragment");
        mAutoTrackList.add("com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumFragment");
        mAutoTrackList.add("com.miui.gallery.ui.TextEditFragment");
    }

    public static boolean contains(String str) {
        return mNavMap.containsKey(str) && mViewMap.containsKey(str);
    }

    public static boolean track(String str) {
        if (!contains(str) || mViewMap.get(str).equals(mRef)) {
            return false;
        }
        TrackController.trackNav(mNavMap.get(str), mRef);
        if (!mAutoTrackList.contains(str)) {
            return true;
        }
        TrackController.trackView(TrackController.buildViewParams(mViewMap.get(str), mRef));
        mRef = mViewMap.get(str);
        return true;
    }

    public static boolean trackNav(String str) {
        TrackController.trackNav(str, mRef);
        return true;
    }

    public static boolean trackNavAndView(String str, String str2) {
        if (str2.equals(mRef)) {
            return false;
        }
        TrackController.trackNav(str, mRef);
        TrackController.trackView(TrackController.buildViewParams(str2, mRef));
        mRef = str2;
        return true;
    }

    public static void trackView(String str, String str2) {
        trackView(TrackController.buildViewParams(str, str2));
    }

    public static void trackView(String str, String str2, int i) {
        trackView(TrackController.buildViewParams(str, str2, i));
    }

    public static void trackView(String str, String str2, String str3) {
        trackView(TrackController.buildViewParams(str, str2, str3));
    }

    public static void trackView(Map map) {
        String valueOf = String.valueOf(map.get("tip"));
        if (valueOf.equals(mRef)) {
            return;
        }
        TrackController.trackView(map);
        if (!mViewMap.containsValue(valueOf)) {
            return;
        }
        mRef = valueOf;
    }

    public static String getRef() {
        return mRef;
    }
}
