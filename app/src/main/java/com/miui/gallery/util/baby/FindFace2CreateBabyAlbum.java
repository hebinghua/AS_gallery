package com.miui.gallery.util.baby;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.miui.gallery.activity.facebaby.BabyAlbumSettingsFragment;
import com.miui.gallery.activity.facebaby.OwnerBabyAlbumSettingActivity;
import com.miui.gallery.activity.facebaby.OwnerBabyAlbumSettingsFragment;
import com.miui.gallery.activity.facebaby.SharerBabyAlbumSettingActivity;
import com.miui.gallery.cloud.baby.BabyInfo;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.deprecated.NormalPeopleFaceMediaSet;
import com.miui.gallery.provider.deprecated.ThumbnailInfo;

/* loaded from: classes2.dex */
public class FindFace2CreateBabyAlbum {
    public static void gotoFillBabyAlbumInfo(Activity activity, NormalPeopleFaceMediaSet normalPeopleFaceMediaSet, String str) {
        Intent intent = new Intent(activity, OwnerBabyAlbumSettingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong("faceAlbumLocalId", normalPeopleFaceMediaSet.getBucketId());
        if (!TextUtils.isEmpty(str)) {
            bundle.putString("faceAlbumCoverFaceServerId", str);
        }
        bundle.putString("faceAlbumServerId", normalPeopleFaceMediaSet.getServerId());
        bundle.putString("name", normalPeopleFaceMediaSet.getName());
        int[] currentYearMonthDay = BabyAlbumSettingsFragment.getCurrentYearMonthDay(System.currentTimeMillis());
        bundle.putString("birthday", BabyAlbumSettingsFragment.combine2Birthday(currentYearMonthDay[0], currentYearMonthDay[1], currentYearMonthDay[2]));
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, 13);
    }

    public static void gotoBabyAlbumInfoPage(Activity activity, ThumbnailInfo thumbnailInfo, BabyInfo babyInfo, long j, String str, String str2, boolean z) {
        Intent intent = new Intent(activity, z ? SharerBabyAlbumSettingActivity.class : OwnerBabyAlbumSettingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("baby_info", babyInfo);
        bundle.putString("album_name", str);
        bundle.putLong("babyAlbumLocalId", j);
        if (thumbnailInfo != null) {
            intent.putExtra("thumbnail_info_of_baby", thumbnailInfo.toString());
        }
        if (!TextUtils.isEmpty(str2)) {
            bundle.putString("face_path", str2);
        }
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, 12);
    }

    public static void createBabyAlbumAndSaveBabyInfo(String str, BabyInfo babyInfo, NormalPeopleFaceMediaSet normalPeopleFaceMediaSet, Activity activity, String str2) {
        String createBabyAlbumAndAddItems = normalPeopleFaceMediaSet.createBabyAlbumAndAddItems(str, babyInfo, activity);
        ThumbnailInfo thumbnailInfo = new ThumbnailInfo(Integer.parseInt(createBabyAlbumAndAddItems), false, null);
        thumbnailInfo.setFaceId(str2);
        OwnerBabyAlbumSettingsFragment.saveInfo2Db(babyInfo, normalPeopleFaceMediaSet.getServerId(), Long.valueOf(normalPeopleFaceMediaSet.getBucketId()), createBabyAlbumAndAddItems, thumbnailInfo);
        GalleryPreferences.Baby.recordBabyAlbumHasShortcut(str);
    }
}
