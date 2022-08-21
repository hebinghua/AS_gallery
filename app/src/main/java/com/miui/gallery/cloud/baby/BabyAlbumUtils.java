package com.miui.gallery.cloud.baby;

import android.content.Context;
import android.text.TextUtils;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.data.DBAlbum;
import com.miui.gallery.data.DBShareAlbum;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.util.GalleryUtils;
import com.xiaomi.stat.a.j;

/* loaded from: classes.dex */
public class BabyAlbumUtils {
    public static void saveBabyInfo(String str, BabyInfo babyInfo, boolean z) {
        if (z) {
            saveShareAlbumBabyInfo(str, babyInfo);
        } else {
            saveOwnerAlbumBabyInfo(str, babyInfo);
        }
    }

    public static void saveOwnerAlbumBabyInfo(String str, BabyInfo babyInfo) {
        saveBabyInfoJson(str, AlbumDataHelper.genUpdateAlbumExtraInfoSql("babyInfoJson", babyInfo.toJSON(), false), "babyInfoJson", 24, "peopleId", AlbumDataHelper.genUpdateAlbumExtraInfoSql("peopleId", babyInfo.mPeopleId, false), "editedColumns", j.c, "album");
    }

    public static void saveShareAlbumBabyInfo(String str, BabyInfo babyInfo) {
        saveBabyInfoJson(str, babyInfo.toJSON(), "babyInfoJson", 25, "peopleId", babyInfo.mPeopleId, "editedColumns", j.c, "shareAlbum");
    }

    public static void saveBabyInfoJson(String str, String str2, String str3, int i, String str4, String str5, String str6, String str7, String str8) {
        String transformToEditedColumnsElement = GalleryCloudUtils.transformToEditedColumnsElement(i);
        Object[] objArr = new Object[12];
        objArr[0] = str8;
        objArr[1] = str3;
        objArr[2] = str2 == null ? "" : str2;
        objArr[3] = str4;
        if (str2 == null) {
            str5 = "";
        }
        objArr[4] = str5;
        objArr[5] = str6;
        objArr[6] = str6;
        objArr[7] = transformToEditedColumnsElement;
        objArr[8] = transformToEditedColumnsElement;
        objArr[9] = transformToEditedColumnsElement;
        objArr[10] = str7;
        objArr[11] = str;
        GalleryUtils.safeExec(String.format("update %s set %s=%s, %s='%s', %s=coalesce(replace(%s, '%s', '') || '%s', '%s') where %s=%s", objArr));
    }

    public static boolean isBabyAlbumForThisServerId(Context context, String str, boolean z) {
        if (!z) {
            DBAlbum albumByServerID = AlbumDataHelper.getAlbumByServerID(context, str);
            return albumByServerID != null && !TextUtils.isEmpty(albumByServerID.getBabyInfo());
        }
        DBShareAlbum groupItemByColumnnameAndValueFromShare = CloudUtils.getGroupItemByColumnnameAndValueFromShare(context, "serverId", str);
        return groupItemByColumnnameAndValueFromShare != null && !TextUtils.isEmpty(groupItemByColumnnameAndValueFromShare.getBabyInfoJson());
    }
}
