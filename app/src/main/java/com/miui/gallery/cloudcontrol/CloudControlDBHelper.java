package com.miui.gallery.cloudcontrol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.miui.gallery.cloudcontrol.FeatureProfile;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes.dex */
public class CloudControlDBHelper {
    public static int tryInsertToDB(Context context, FeatureProfile featureProfile) {
        FeatureDBItem queryItem = queryItem(context, featureProfile.getName());
        if (featureProfile.getStatus() != null && featureProfile.getStatus().equals(FeatureProfile.Status.REMOVE.getValue())) {
            if (queryItem == null) {
                return 4;
            }
            return deleteItem(context, featureProfile);
        } else if (queryItem != null) {
            if (queryItem.getStatus() != null) {
                String status = queryItem.getStatus();
                FeatureProfile.Status status2 = FeatureProfile.Status.ENABLE;
                if (status.equals(status2.getValue())) {
                    if (FeatureProfile.Status.DISABLE.getValue().equals(featureProfile.getStatus())) {
                        DefaultLogger.d("CloudControlDBHelper", "Status 'disable' can't overwrite 'enable': %s", String.valueOf(featureProfile));
                    }
                    featureProfile.setStatus(status2.getValue());
                }
            }
            if (queryItem.equals(featureProfile)) {
                DefaultLogger.d("CloudControlDBHelper", "Unchanged item: %s, ignore", String.valueOf(featureProfile));
                return 4;
            }
            return updateItem(context, featureProfile);
        } else {
            return insertItem(context, featureProfile);
        }
    }

    public static int deleteItem(Context context, FeatureProfile featureProfile) {
        DefaultLogger.d("CloudControlDBHelper", "deleteItem: %s", String.valueOf(featureProfile));
        return SafeDBUtil.safeDelete(context, GalleryContract.CloudControl.URI, "featureName = ?", new String[]{featureProfile.getName()}) > 0 ? 2 : 0;
    }

    public static FeatureDBItem queryItem(Context context, String str) {
        return (FeatureDBItem) SafeDBUtil.safeQuery(context, GalleryContract.CloudControl.URI, FeatureDBItem.PROJECTION, "featureName = ?", new String[]{str}, (String) null, new SafeDBUtil.QueryHandler<FeatureDBItem>() { // from class: com.miui.gallery.cloudcontrol.CloudControlDBHelper.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle */
            public FeatureDBItem mo1808handle(Cursor cursor) {
                if (cursor == null || !cursor.moveToFirst()) {
                    return null;
                }
                return FeatureDBItem.fromCursor(cursor);
            }
        });
    }

    public static int updateItem(Context context, FeatureProfile featureProfile) {
        DefaultLogger.d("CloudControlDBHelper", "updateItem: %s", String.valueOf(featureProfile));
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", featureProfile.getStatus());
        contentValues.put("strategy", featureProfile.getStrategy());
        return SafeDBUtil.safeUpdate(context, GalleryContract.CloudControl.URI, contentValues, "featureName = ?", new String[]{featureProfile.getName()}) > 0 ? 3 : 0;
    }

    public static int insertItem(Context context, FeatureProfile featureProfile) {
        DefaultLogger.d("CloudControlDBHelper", "insertItem: %s", String.valueOf(featureProfile));
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", featureProfile.getStatus());
        contentValues.put("strategy", featureProfile.getStrategy());
        contentValues.put("featureName", featureProfile.getName());
        return SafeDBUtil.safeInsert(context, GalleryContract.CloudControl.URI, contentValues) != null ? 1 : 0;
    }
}
