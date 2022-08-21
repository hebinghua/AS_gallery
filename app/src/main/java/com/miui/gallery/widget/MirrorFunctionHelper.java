package com.miui.gallery.widget;

import android.net.Uri;
import android.view.InputEvent;
import android.view.View;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.provider.GalleryOpenProvider;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.mirror.MirrorManagerImpl;
import com.xiaomi.mirror.MirrorMenu;
import com.xiaomi.mirror.OnMirrorMenuQueryListener;
import com.xiaomi.mirror.synergy.MiuiSynergySdk;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class MirrorFunctionHelper {
    public static boolean IS_MIRROR_MANAGER_AVAILABLE = true;

    public static void registerPCRightClick(View view, final String str) {
        if (view == null || str == null) {
            DefaultLogger.i("MirrorFunctionHelper", "registerPCRightClick itemView is null or file is invalid.");
        } else if (!IS_MIRROR_MANAGER_AVAILABLE) {
        } else {
            MirrorManagerImpl mirrorManagerImpl = null;
            try {
                mirrorManagerImpl = MirrorManagerImpl.get(view.getContext());
            } catch (NoSuchMethodError e) {
                IS_MIRROR_MANAGER_AVAILABLE = false;
                DefaultLogger.i("MirrorFunctionHelper", "registerPCRightClick get MirrorManagerImpl error: " + e.getMessage());
            }
            if (mirrorManagerImpl == null) {
                return;
            }
            mirrorManagerImpl.setOnMirrorMenuQueryListener(view, new OnMirrorMenuQueryListener() { // from class: com.miui.gallery.widget.MirrorFunctionHelper.1
                @Override // com.xiaomi.mirror.OnMirrorMenuQueryListener
                public void onMirrorMenuShow(View view2, boolean z) {
                }

                @Override // com.xiaomi.mirror.OnMirrorMenuQueryListener
                public ArrayList<MirrorMenu> onMirrorMenuQuery(View view2) {
                    ArrayList<MirrorMenu> arrayList = new ArrayList<>();
                    Uri translateToContent = GalleryOpenProvider.translateToContent(str);
                    GalleryApp.sGetAndroidContext().grantUriPermission("com.miui.mishare", translateToContent, 1);
                    arrayList.add(new MirrorMenu.PcOpenBuilder().setUri(translateToContent).setExtra("custom extra").build());
                    return arrayList;
                }
            });
        }
    }

    public static boolean isEventFromMirror(InputEvent inputEvent) {
        if (!IS_MIRROR_MANAGER_AVAILABLE) {
            return false;
        }
        try {
            MirrorManagerImpl mirrorManagerImpl = MirrorManagerImpl.get(GalleryApp.sGetAndroidContext());
            if (mirrorManagerImpl == null) {
                return false;
            }
            boolean isFloatWindowShow = MiuiSynergySdk.getInstance().isFloatWindowShow(GalleryApp.sGetAndroidContext());
            DefaultLogger.i("MirrorFunctionHelper", "isFloatingWindow" + isFloatWindowShow);
            return isFloatWindowShow || mirrorManagerImpl.isEventFromMirror(inputEvent);
        } catch (NoSuchMethodError e) {
            IS_MIRROR_MANAGER_AVAILABLE = false;
            DefaultLogger.i("MirrorFunctionHelper", "registerPCRightClick get MirrorManagerImpl error: " + e.getMessage());
            return false;
        }
    }
}
