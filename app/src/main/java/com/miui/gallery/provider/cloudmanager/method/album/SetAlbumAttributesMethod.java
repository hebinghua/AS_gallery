package com.miui.gallery.provider.cloudmanager.method.album;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.provider.CloudUtils;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.RecentDiscoveryMediaManager;
import com.miui.gallery.provider.ShareAlbumHelper;
import com.miui.gallery.provider.album.AlbumManager;
import com.miui.gallery.provider.cache.AlbumCacheManager;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.util.BaseMiscUtil;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.stream.LongStream;

/* loaded from: classes2.dex */
public class SetAlbumAttributesMethod implements IAlbumMethod {
    public static /* synthetic */ boolean lambda$doExecute$0(long j) {
        return j == 2147483645;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.IMethod
    public String getInvokerTag() {
        return "galleryAction_Method_SetAlbumAttributesMethod";
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.IMethod
    public void doExecute(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, String str, Bundle bundle, Bundle bundle2, ArrayList<Long> arrayList) {
        long[] longArray = bundle.getLongArray("album_id");
        long j = bundle.getLong("attributes_bit");
        boolean z = bundle.getBoolean("set");
        boolean z2 = bundle.getBoolean("manual");
        if (Arrays.stream(longArray).anyMatch(SetAlbumAttributesMethod$$ExternalSyntheticLambda0.INSTANCE)) {
            longArray = LongStream.concat(Arrays.stream(longArray), LongStream.of(AlbumCacheManager.getInstance().getScreenshotsAlbumId(), AlbumCacheManager.getInstance().getScreenRecordersAlbumId())).toArray();
        }
        setAlbumAttributes(supportSQLiteDatabase, mediaManager, longArray, j, z, z2);
        int length = longArray.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            } else if (!ShareAlbumHelper.isOtherShareAlbumId(longArray[i])) {
                List<Long> albumSyncAttributes = AlbumManager.getAlbumSyncAttributes();
                if (BaseMiscUtil.isValid(albumSyncAttributes) && albumSyncAttributes.contains(Long.valueOf(j))) {
                    bundle2.putBoolean("should_request_sync", true);
                }
            } else {
                i++;
            }
        }
        CloudUtils.parceNotifyUri(bundle2, GalleryContract.Media.URI_ALL, GalleryContract.Media.URI);
    }

    public static void setAlbumAttributes(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, long[] jArr, long j, boolean z, boolean z2) {
        char c;
        long packageAttributeBit = AlbumManager.packageAttributeBit(j, z, z2);
        long packageAttributeBit2 = AlbumManager.packageAttributeBit(j, true, true);
        int length = jArr.length / 2;
        ArrayList<Long> arrayList = new ArrayList(length);
        ArrayList arrayList2 = new ArrayList(length);
        for (long j2 : jArr) {
            if (ShareAlbumHelper.isOtherShareAlbumId(j2)) {
                arrayList.add(Long.valueOf(ShareAlbumHelper.getOriginalAlbumId(j2)));
            } else {
                arrayList2.add(Long.valueOf(j2));
            }
        }
        if (Album.isRubbishAlbum(j)) {
            Bundle updateAttributesIfIsRubbishTag = CloudUtils.updateAttributesIfIsRubbishTag(j, packageAttributeBit, z);
            packageAttributeBit = updateAttributesIfIsRubbishTag.getLong(" packageAttibuteBit", packageAttributeBit);
            packageAttributeBit2 = updateAttributesIfIsRubbishTag.getLong("attributeBitMask", packageAttributeBit2);
            if (z) {
                RecentDiscoveryMediaManager.deleteGroupByAlbumIds(supportSQLiteDatabase, jArr);
            }
        }
        long j3 = packageAttributeBit;
        long j4 = packageAttributeBit2;
        if (!arrayList.isEmpty()) {
            supportSQLiteDatabase.execSQL(String.format(Locale.US, "UPDATE %s SET %s = (%s & ~%d) | %d WHERE %s IN (%s)", "shareAlbum", "attributes", "attributes", Long.valueOf(j4), Long.valueOf(j3), j.c, TextUtils.join(",", arrayList)));
            LinkedList linkedList = new LinkedList();
            for (Long l : arrayList) {
                linkedList.add(Long.valueOf(ShareAlbumHelper.getUniformAlbumId(l.longValue())));
            }
            c = 7;
            AlbumCacheManager.getInstance().updateAttributes(j, z, z2, linkedList);
        } else {
            c = 7;
        }
        if (!arrayList2.isEmpty()) {
            List<Long> albumSyncAttributes = AlbumManager.getAlbumSyncAttributes();
            String transformToEditedColumnsElement = (!BaseMiscUtil.isValid(albumSyncAttributes) || !albumSyncAttributes.contains(Long.valueOf(j))) ? "" : GalleryCloudUtils.transformToEditedColumnsElement(22);
            Locale locale = Locale.US;
            Object[] objArr = new Object[12];
            objArr[0] = "album";
            objArr[1] = "attributes";
            objArr[2] = "attributes";
            objArr[3] = Long.valueOf(j4);
            objArr[4] = Long.valueOf(j3);
            objArr[5] = "editedColumns";
            objArr[6] = "editedColumns";
            objArr[c] = transformToEditedColumnsElement;
            objArr[8] = transformToEditedColumnsElement;
            objArr[9] = transformToEditedColumnsElement;
            objArr[10] = j.c;
            objArr[11] = TextUtils.join(",", arrayList2);
            supportSQLiteDatabase.execSQL(String.format(locale, "UPDATE %s SET %s = (%s & ~%d) | %d, %s=coalesce(replace(%s, '%s', '') || '%s', '%s') WHERE %s IN (%s)", objArr));
            AlbumCacheManager.getInstance().updateAttributes(j, z, z2, arrayList2);
        }
    }
}
