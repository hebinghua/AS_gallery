package com.miui.gallery.provider.cloudmanager.method.cloud.addTo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.provider.cache.AlbumCacheManager;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.method.cloud.ICLoudMethod;
import com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.CopyAndMoveByIdFactory;
import com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.uri.CopyAndMoveByUriFactory;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.Numbers;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/* loaded from: classes2.dex */
public class AddToAlbumMethod implements ICLoudMethod {
    @Override // com.miui.gallery.provider.cloudmanager.method.IMethod
    public String getInvokerTag() {
        return "galleryAction_Method_AddToAlbum";
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.IMethod
    public void doExecute(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, String str, Bundle bundle, Bundle bundle2, ArrayList<Long> arrayList) throws StoragePermissionMissingException {
        long longValue = Numbers.parse(str, -1L).longValue();
        if (longValue == 2147483645) {
            longValue = AlbumCacheManager.getInstance().getScreenshotsAlbumId();
        }
        long j = longValue;
        int i = bundle.getInt("extra_src_type", 0);
        int i2 = bundle.getInt("extra_type", 0);
        if (i == 1) {
            copyOrMoveUriMedia(bundle, j, str, i2, context, arrayList, supportSQLiteDatabase, mediaManager, bundle2);
        }
        if (i == 0) {
            copyOrMoveMedia(bundle, j, str, i2, context, arrayList, supportSQLiteDatabase, mediaManager, bundle2);
        }
    }

    public final void copyOrMoveMedia(Bundle bundle, long j, String str, int i, Context context, ArrayList<Long> arrayList, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, Bundle bundle2) throws StoragePermissionMissingException {
        long[] longArray = bundle.getLongArray("extra_src_media_ids");
        if (longArray == null || ((i != 0 && i != 1) || Album.isVirtualAlbum(j))) {
            DefaultLogger.e("galleryAction_Method_AddToAlbum", "error, albumId is %s, mediaIds is %s", str, longArray);
            return;
        }
        LinkedList linkedList = new LinkedList();
        long[] jArr = new long[longArray.length];
        int i2 = 0;
        for (long j2 : longArray) {
            try {
            } catch (StoragePermissionMissingException e) {
                e = e;
            } catch (Exception e2) {
                e = e2;
            }
            try {
                jArr[i2] = CopyAndMoveByIdFactory.create(i, context, arrayList, j2, j, supportSQLiteDatabase).run(supportSQLiteDatabase, mediaManager);
            } catch (StoragePermissionMissingException e3) {
                e = e3;
                linkedList.addAll(e.getPermissionResultList());
                i2++;
            } catch (Exception e4) {
                e = e4;
                DefaultLogger.e("galleryAction_Method_AddToAlbum", "copy or move error [%d], %s", Long.valueOf(jArr[i2]), e);
                jArr[i2] = -100;
                i2++;
            }
            i2++;
        }
        if (BaseMiscUtil.isValid(linkedList)) {
            throw new StoragePermissionMissingException(linkedList);
        }
        bundle2.putLongArray("ids", jArr);
    }

    public final void copyOrMoveUriMedia(Bundle bundle, long j, String str, int i, Context context, ArrayList<Long> arrayList, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, Bundle bundle2) throws StoragePermissionMissingException {
        ArrayList parcelableArrayList = bundle.getParcelableArrayList("extra_src_uris");
        if (parcelableArrayList == null || Album.isVirtualAlbum(j)) {
            DefaultLogger.e("galleryAction_Method_AddToAlbum", "error, albumId is %s, uris is %s", str, parcelableArrayList);
            return;
        }
        LinkedList linkedList = new LinkedList();
        long[] jArr = new long[parcelableArrayList.size()];
        int i2 = 0;
        Iterator it = parcelableArrayList.iterator();
        while (it.hasNext()) {
            try {
            } catch (StoragePermissionMissingException e) {
                e = e;
            } catch (Exception e2) {
                e = e2;
            }
            try {
                jArr[i2] = CopyAndMoveByUriFactory.create(i, context, arrayList, j, (Uri) it.next(), supportSQLiteDatabase).run(supportSQLiteDatabase, mediaManager);
            } catch (StoragePermissionMissingException e3) {
                e = e3;
                linkedList.addAll(e.getPermissionResultList());
                i2++;
            } catch (Exception e4) {
                e = e4;
                DefaultLogger.e("galleryAction_Method_AddToAlbum", "copy or move error [%d], %s", Long.valueOf(jArr[i2]), e);
                jArr[i2] = -100;
                i2++;
            }
            i2++;
        }
        if (BaseMiscUtil.isValid(linkedList)) {
            throw new StoragePermissionMissingException(linkedList);
        }
        bundle2.putLongArray("ids", jArr);
    }
}
