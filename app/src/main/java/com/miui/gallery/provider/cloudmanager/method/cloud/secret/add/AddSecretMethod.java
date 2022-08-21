package com.miui.gallery.provider.cloudmanager.method.cloud.secret.add;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.method.cloud.ICLoudMethod;
import com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.AddSecretById2;
import com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.uri.AddSecretByUriFactory;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.LinkedList;

/* loaded from: classes2.dex */
public class AddSecretMethod implements ICLoudMethod {
    @Override // com.miui.gallery.provider.cloudmanager.method.IMethod
    public String getInvokerTag() {
        return "galleryAction_Method_AddSecretMethod";
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.IMethod
    public void doExecute(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, String str, Bundle bundle, Bundle bundle2, ArrayList<Long> arrayList) throws StoragePermissionMissingException {
        addSecret(context, supportSQLiteDatabase, mediaManager, bundle, bundle2, arrayList);
        requestSyncIfNeed(bundle2);
    }

    public static void addSecret(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, Bundle bundle, Bundle bundle2, ArrayList<Long> arrayList) throws StoragePermissionMissingException {
        if (bundle.containsKey("extra_src_media_ids")) {
            addToSecretByIds(context, supportSQLiteDatabase, mediaManager, bundle, bundle2, arrayList);
        } else if (!bundle.containsKey("extra_src_uris")) {
        } else {
            addToSecretByUris(context, supportSQLiteDatabase, mediaManager, bundle, bundle2, arrayList);
        }
    }

    public static void requestSyncIfNeed(Bundle bundle) {
        long[] longArray = bundle.getLongArray("ids");
        int length = longArray.length;
        boolean z = false;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            } else if (longArray[i] > 0) {
                z = true;
                break;
            } else {
                i++;
            }
        }
        bundle.putBoolean("should_request_sync", z);
    }

    public static void addToSecretByIds(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, Bundle bundle, Bundle bundle2, ArrayList<Long> arrayList) throws StoragePermissionMissingException {
        long[] longArray = bundle.getLongArray("extra_src_media_ids");
        LinkedList linkedList = new LinkedList();
        for (int i = 0; i < longArray.length; i++) {
            try {
                longArray[i] = new AddSecretById2(context, arrayList, supportSQLiteDatabase, longArray[i]).run(supportSQLiteDatabase, mediaManager);
            } catch (StoragePermissionMissingException e) {
                linkedList.addAll(e.getPermissionResultList());
            } catch (Exception e2) {
                DefaultLogger.e("galleryAction_Method_AddSecretMethod", "add to secret error %d, %s", Long.valueOf(longArray[i]), e2);
                longArray[i] = -100;
            }
        }
        if (BaseMiscUtil.isValid(linkedList)) {
            throw new StoragePermissionMissingException(linkedList);
        }
        bundle2.putLongArray("ids", longArray);
    }

    public static void addToSecretByUris(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, Bundle bundle, Bundle bundle2, ArrayList<Long> arrayList) throws StoragePermissionMissingException {
        ArrayList parcelableArrayList = bundle.getParcelableArrayList("extra_src_uris");
        long[] jArr = new long[parcelableArrayList.size()];
        LinkedList linkedList = new LinkedList();
        for (int i = 0; i < parcelableArrayList.size(); i++) {
            try {
                jArr[i] = AddSecretByUriFactory.create(context, arrayList, (Uri) parcelableArrayList.get(i), supportSQLiteDatabase).run(supportSQLiteDatabase, mediaManager);
            } catch (StoragePermissionMissingException e) {
                linkedList.addAll(e.getPermissionResultList());
            } catch (Exception unused) {
                DefaultLogger.e("galleryAction_Method_AddSecretMethod", "add to secret error %s", parcelableArrayList.get(i));
                jArr[i] = -100;
            }
        }
        if (BaseMiscUtil.isValid(linkedList)) {
            throw new StoragePermissionMissingException(linkedList);
        }
        bundle2.putLongArray("ids", jArr);
    }
}
