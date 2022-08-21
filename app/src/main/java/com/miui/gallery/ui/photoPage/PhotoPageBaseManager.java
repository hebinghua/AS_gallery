package com.miui.gallery.ui.photoPage;

import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.ui.PhotoPageFragment;
import com.miui.gallery.ui.photoPage.IPhotoPageManager;
import java.util.Map;

/* loaded from: classes2.dex */
public abstract class PhotoPageBaseManager implements IPhotoPageManager {
    public boolean isFromCamera;
    public boolean isFromScreenRecorder;
    public boolean isInFreeWindowMode;
    public BaseActivity mActivity;
    public PhotoPageFragment mFragment;
    public IPhotoPageManager.IPhotoPageManagerCallback mPhotoPageManagerCallback;

    public PhotoPageBaseManager(PhotoPageFragment photoPageFragment, Map<String, Object> map, IPhotoPageManager.IPhotoPageManagerCallback iPhotoPageManagerCallback) {
        parseConfig(map);
        this.mFragment = photoPageFragment;
        this.mActivity = (BaseActivity) photoPageFragment.getActivity();
        this.mPhotoPageManagerCallback = iPhotoPageManagerCallback;
    }

    public void parseConfig(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return;
        }
        boolean z = false;
        if (map.containsKey("scene")) {
            int intValue = ((Integer) map.get("scene")).intValue();
            this.isFromCamera = intValue == 1;
            this.isFromScreenRecorder = intValue == 2;
        }
        if (!map.containsKey("window_mode")) {
            return;
        }
        if (((Integer) map.get("window_mode")).intValue() == 1) {
            z = true;
        }
        this.isInFreeWindowMode = z;
    }

    public static <C extends IPhotoPageManager.IPhotoPageManagerController, M extends IPhotoPageManager> C registerManager(PhotoPageFragment photoPageFragment, Map<String, Object> map, IPhotoPageManager.IPhotoPageManagerCallback iPhotoPageManagerCallback, Class<M> cls) {
        if (photoPageFragment == null || photoPageFragment.getActivity() == null || photoPageFragment.getActivity().isDestroyed() || cls == null) {
            throw new IllegalStateException("Illegal State!");
        }
        return (C) PhotoPageManagerMediator.getInstance().registerManager(photoPageFragment, map, iPhotoPageManagerCallback, cls);
    }
}
