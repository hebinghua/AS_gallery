package com.miui.gallery.widget;

import android.app.Activity;
import android.view.DragEvent;
import androidx.core.app.ActivityCompat;
import androidx.core.view.DragAndDropPermissionsCompat;

/* loaded from: classes2.dex */
public class ViewDragPermissionManager {
    public DragAndDropPermissionsCompat mDragPermissionsCompat;

    /* loaded from: classes2.dex */
    public static class Singleton {
        public static final ViewDragPermissionManager INSTANCE = new ViewDragPermissionManager();
    }

    public ViewDragPermissionManager() {
    }

    public static ViewDragPermissionManager getInstance() {
        return Singleton.INSTANCE;
    }

    public boolean requestDragPermission(Activity activity, DragEvent dragEvent) {
        if (activity != null) {
            this.mDragPermissionsCompat = ActivityCompat.requestDragAndDropPermissions(activity, dragEvent);
        }
        return this.mDragPermissionsCompat != null;
    }

    public void releasePermission() {
        DragAndDropPermissionsCompat dragAndDropPermissionsCompat = this.mDragPermissionsCompat;
        if (dragAndDropPermissionsCompat != null) {
            dragAndDropPermissionsCompat.release();
            this.mDragPermissionsCompat = null;
        }
    }
}
