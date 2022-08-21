package com.miui.gallery.provider.cloudmanager.method.album;

import android.content.Context;
import android.os.Bundle;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.album.DeleteAlbum;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.Numbers;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.LinkedList;

/* loaded from: classes2.dex */
public class DeleteAlbumMethod implements IAlbumMethod {
    @Override // com.miui.gallery.provider.cloudmanager.method.IMethod
    public String getInvokerTag() {
        return "galleryAction_Method_DeleteAlbum";
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.album.IAlbumMethod, com.miui.gallery.provider.cloudmanager.method.IMethod
    public boolean isNeedFileHandle() {
        return true;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.IMethod
    public void doExecute(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, String str, Bundle bundle, Bundle bundle2, ArrayList<Long> arrayList) throws StoragePermissionMissingException {
        ArrayList arrayList2 = new ArrayList();
        deleteAlbum(context, supportSQLiteDatabase, mediaManager, arrayList, bundle.getLongArray("extra_album_ids"), bundle.getInt("extra_delete_options", 0), arrayList2, bundle.getInt("extra_delete_reason", 21));
        bundle2.putLongArray("ids", MiscUtil.ListToArray(arrayList2));
    }

    public static void deleteAlbum(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, ArrayList<Long> arrayList, long[] jArr, int i, ArrayList<Long> arrayList2, int i2) throws StoragePermissionMissingException {
        try {
            try {
                Numbers.ensurePositive(jArr);
                LinkedList linkedList = new LinkedList();
                for (int i3 = 0; i3 < jArr.length; i3++) {
                    try {
                        try {
                        } catch (StoragePermissionMissingException e) {
                            e = e;
                        }
                        try {
                            if (new DeleteAlbum(context, supportSQLiteDatabase, arrayList, jArr[i3], i2, i == 1).run(supportSQLiteDatabase, mediaManager) > 0) {
                                try {
                                    arrayList2.add(Long.valueOf(jArr[i3]));
                                } catch (StoragePermissionMissingException e2) {
                                    e = e2;
                                    linkedList.addAll(e.getPermissionResultList());
                                }
                            }
                        } catch (StoragePermissionMissingException e3) {
                            e = e3;
                            linkedList.addAll(e.getPermissionResultList());
                        }
                    } catch (StoragePermissionMissingException e4) {
                        e = e4;
                    }
                }
                if (!BaseMiscUtil.isValid(linkedList)) {
                    return;
                }
                throw new StoragePermissionMissingException(linkedList);
            } catch (StoragePermissionMissingException e5) {
                throw e5;
            }
        } catch (Exception e6) {
            DefaultLogger.w("galleryAction_Method_DeleteAlbum", e6);
        }
    }
}
