package com.miui.gallery.ui.photoPage;

import android.content.res.Configuration;
import android.os.Bundle;
import androidx.tracing.Trace;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.ui.PhotoPageFragment;
import com.miui.gallery.ui.photoPage.IPhotoPageManager;
import com.miui.gallery.ui.photoPage.PhotoPageThemeManager;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class PhotoPageManagerMediator {
    public static volatile PhotoPageManagerMediator instance;
    public Map<String, IPhotoPageManager> mManagers;

    public static PhotoPageManagerMediator getInstance() {
        if (instance == null) {
            synchronized (PhotoPageManagerMediator.class) {
                if (instance == null) {
                    instance = new PhotoPageManagerMediator();
                }
            }
        }
        return instance;
    }

    public <C extends IPhotoPageManager.IPhotoPageManagerController, M extends IPhotoPageManager> C registerManager(PhotoPageFragment photoPageFragment, Map<String, Object> map, IPhotoPageManager.IPhotoPageManagerCallback iPhotoPageManagerCallback, Class<M> cls) {
        PhotoPageThemeManager.IPhotoPageThemeManagerController iPhotoPageThemeManagerController;
        if (this.mManagers == null) {
            this.mManagers = new HashMap();
        }
        Trace.beginSection("OrientationManager");
        if (PhotoPageOrientationManager.class.equals(cls)) {
            PhotoPageOrientationManager photoPageOrientationManager = new PhotoPageOrientationManager(photoPageFragment, map, iPhotoPageManagerCallback);
            this.mManagers.put(cls.getSimpleName(), photoPageOrientationManager);
            iPhotoPageThemeManagerController = photoPageOrientationManager.getManagerController();
        } else {
            iPhotoPageThemeManagerController = null;
        }
        Trace.endSection();
        Trace.beginSection("ThemeManager");
        if (PhotoPageThemeManager.class.equals(cls)) {
            PhotoPageThemeManager photoPageThemeManager = new PhotoPageThemeManager(photoPageFragment, map, iPhotoPageManagerCallback);
            this.mManagers.put(cls.getSimpleName(), photoPageThemeManager);
            iPhotoPageThemeManagerController = photoPageThemeManager.getManagerController();
        }
        Trace.endSection();
        return iPhotoPageThemeManagerController;
    }

    public void dispatchLifeCirclePause() {
        Map<String, IPhotoPageManager> map = this.mManagers;
        if (map == null || map.isEmpty()) {
            return;
        }
        for (IPhotoPageManager iPhotoPageManager : this.mManagers.values()) {
            if (iPhotoPageManager != null) {
                iPhotoPageManager.onPause();
            }
        }
    }

    public void dispatchLifeCircleResume() {
        Map<String, IPhotoPageManager> map = this.mManagers;
        if (map == null || map.isEmpty()) {
            return;
        }
        for (IPhotoPageManager iPhotoPageManager : this.mManagers.values()) {
            if (iPhotoPageManager != null) {
                iPhotoPageManager.onResume();
            }
        }
    }

    public void dispatchLifeCircleOnDestroyView() {
        Map<String, IPhotoPageManager> map = this.mManagers;
        if (map == null || map.isEmpty()) {
            return;
        }
        for (IPhotoPageManager iPhotoPageManager : this.mManagers.values()) {
            if (iPhotoPageManager != null) {
                iPhotoPageManager.onDestroyView();
            }
        }
    }

    public void dispatchLifeCircleSettleItem(BaseDataItem baseDataItem) {
        Map<String, IPhotoPageManager> map = this.mManagers;
        if (map == null || map.isEmpty()) {
            return;
        }
        for (IPhotoPageManager iPhotoPageManager : this.mManagers.values()) {
            if (iPhotoPageManager != null) {
                iPhotoPageManager.onSettleItem(baseDataItem);
            }
        }
    }

    public void dispatchLifeCircleOnViewInflated() {
        Map<String, IPhotoPageManager> map = this.mManagers;
        if (map == null || map.isEmpty()) {
            return;
        }
        for (IPhotoPageManager iPhotoPageManager : this.mManagers.values()) {
            if (iPhotoPageManager != null) {
                iPhotoPageManager.onViewInflated();
            }
        }
    }

    public void dispatchLifeCircleOnConfigurationChanged(Configuration configuration) {
        Map<String, IPhotoPageManager> map = this.mManagers;
        if (map == null || map.isEmpty()) {
            return;
        }
        for (IPhotoPageManager iPhotoPageManager : this.mManagers.values()) {
            if (iPhotoPageManager != null) {
                iPhotoPageManager.onConfigurationChanged(configuration);
            }
        }
    }

    public void dispatchLifeCircleDoExit() {
        Map<String, IPhotoPageManager> map = this.mManagers;
        if (map == null || map.isEmpty()) {
            return;
        }
        for (IPhotoPageManager iPhotoPageManager : this.mManagers.values()) {
            if (iPhotoPageManager != null) {
                iPhotoPageManager.onDoExit();
            }
        }
    }

    public void dispatchLifeCircleOnDestroy() {
        Map<String, IPhotoPageManager> map = this.mManagers;
        if (map == null || map.isEmpty()) {
            return;
        }
        for (IPhotoPageManager iPhotoPageManager : this.mManagers.values()) {
            if (iPhotoPageManager != null) {
                iPhotoPageManager.onDestroy();
            }
        }
        this.mManagers.clear();
    }

    public void dispatchLifeCircleOnCreate(Bundle bundle) {
        Map<String, IPhotoPageManager> map = this.mManagers;
        if (map == null || map.isEmpty()) {
            return;
        }
        for (IPhotoPageManager iPhotoPageManager : this.mManagers.values()) {
            if (iPhotoPageManager != null) {
                iPhotoPageManager.onCreate(bundle);
            }
        }
    }

    public void dispatchLifeCircleOnStart() {
        Map<String, IPhotoPageManager> map = this.mManagers;
        if (map == null || map.isEmpty()) {
            return;
        }
        for (IPhotoPageManager iPhotoPageManager : this.mManagers.values()) {
            if (iPhotoPageManager != null) {
                iPhotoPageManager.onStart();
            }
        }
    }

    public void dispatchLifeCircleOnSaveInstanceState(Bundle bundle) {
        Map<String, IPhotoPageManager> map = this.mManagers;
        if (map == null || map.isEmpty()) {
            return;
        }
        for (IPhotoPageManager iPhotoPageManager : this.mManagers.values()) {
            if (iPhotoPageManager != null) {
                iPhotoPageManager.onSaveInstanceState(bundle);
            }
        }
    }
}
