package com.miui.gallery.editor.photo.app;

import android.content.Context;
import com.miui.filtersdk.BeautificationSDK;
import com.miui.gallery.cloudcontrol.CloudControlManager;
import com.miui.gallery.stat.StatHelper;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class EditorInitializer {
    public void initialize(Context context) {
        DefaultLogger.d("EditorInitializer", "edit progress start");
        CloudControlManager.getInstance().init(context);
        StatHelper.init(context);
        BeautificationSDK.init(context);
        StorageSolutionProvider.init(context);
    }
}
