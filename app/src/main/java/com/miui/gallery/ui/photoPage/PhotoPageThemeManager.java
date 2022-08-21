package com.miui.gallery.ui.photoPage;

import android.view.View;
import com.miui.gallery.R;
import com.miui.gallery.ui.PhotoPageFragment;
import com.miui.gallery.ui.photoPage.IPhotoPageManager;
import com.miui.gallery.ui.photoPage.PhotoPageOrientationManager;
import com.miui.gallery.widget.IMultiThemeView;
import java.util.Map;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes2.dex */
public class PhotoPageThemeManager extends PhotoPageBaseManager {
    public boolean isInDarkTheme;
    public IMultiThemeView mHostView;

    /* loaded from: classes2.dex */
    public interface IPhotoPageThemeManagerController extends IPhotoPageManager.IPhotoPageManagerController {
        void setBackgroundAlpha(float f);

        void setDarkTheme(boolean z, boolean z2);

        void setHostView(IMultiThemeView iMultiThemeView);

        void setLightTheme(boolean z, boolean z2);
    }

    public PhotoPageThemeManager(PhotoPageFragment photoPageFragment, Map<String, Object> map, IPhotoPageManager.IPhotoPageManagerCallback iPhotoPageManagerCallback) {
        super(photoPageFragment, map, iPhotoPageManagerCallback);
    }

    public IPhotoPageThemeManagerController getManagerController() {
        return new PhotoPageThemeManagerController();
    }

    /* loaded from: classes2.dex */
    public class PhotoPageThemeManagerController implements IPhotoPageThemeManagerController {
        public PhotoPageThemeManagerController() {
        }

        @Override // com.miui.gallery.ui.photoPage.PhotoPageThemeManager.IPhotoPageThemeManagerController
        public void setHostView(IMultiThemeView iMultiThemeView) {
            PhotoPageThemeManager.this.setHostView(iMultiThemeView);
        }

        @Override // com.miui.gallery.ui.photoPage.PhotoPageThemeManager.IPhotoPageThemeManagerController
        public void setDarkTheme(boolean z, boolean z2) {
            PhotoPageThemeManager.this.setDarkTheme(z, z2);
        }

        @Override // com.miui.gallery.ui.photoPage.PhotoPageThemeManager.IPhotoPageThemeManagerController
        public void setLightTheme(boolean z, boolean z2) {
            PhotoPageThemeManager.this.setLightTheme(z, z2);
        }

        @Override // com.miui.gallery.ui.photoPage.PhotoPageThemeManager.IPhotoPageThemeManagerController
        public void setBackgroundAlpha(float f) {
            PhotoPageThemeManager.this.setBackgroundAlpha(f);
        }
    }

    public final void setHostView(IMultiThemeView iMultiThemeView) {
        this.mHostView = iMultiThemeView;
    }

    public final void setLightTheme(boolean z, boolean z2) {
        IMultiThemeView iMultiThemeView = this.mHostView;
        if (iMultiThemeView != null) {
            this.isInDarkTheme = false;
            if (z2) {
                iMultiThemeView.setTheme(IMultiThemeView.Theme.LIGHT, z ? IMultiThemeView.ThemeTransition.FADE_IN : IMultiThemeView.ThemeTransition.NONE, new CubicEaseOutInterpolator(), ((View) this.mHostView).getContext().getResources().getInteger(R.integer.photo_background_in_quick_duration));
            } else {
                iMultiThemeView.setTheme(IMultiThemeView.Theme.LIGHT, z ? IMultiThemeView.ThemeTransition.FADE_IN : IMultiThemeView.ThemeTransition.NONE);
            }
        }
    }

    public final void setDarkTheme(boolean z, boolean z2) {
        IMultiThemeView iMultiThemeView = this.mHostView;
        if (iMultiThemeView != null) {
            this.isInDarkTheme = true;
            if (z2) {
                iMultiThemeView.setTheme(IMultiThemeView.Theme.DARK, z ? IMultiThemeView.ThemeTransition.FADE_OUT : IMultiThemeView.ThemeTransition.NONE, new CubicEaseOutInterpolator(), ((View) this.mHostView).getContext().getResources().getInteger(R.integer.photo_background_out_quick_duration));
            } else {
                iMultiThemeView.setTheme(IMultiThemeView.Theme.DARK, z ? IMultiThemeView.ThemeTransition.FADE_OUT : IMultiThemeView.ThemeTransition.NONE);
            }
        }
    }

    public final void setBackgroundAlpha(float f) {
        PhotoPageFragment photoPageFragment;
        if (this.mHostView == null || (photoPageFragment = this.mFragment) == null) {
            return;
        }
        PhotoPageOrientationManager.IPhotoPageOrientationManagerController orientationController = photoPageFragment.getOrientationController();
        float f2 = 1.0f;
        if (orientationController != null && orientationController.isOrientationChanged()) {
            f = 1.0f;
        }
        if (f == 0.0f && this.mFragment.isPlaySlideshow()) {
            this.mFragment.setPlaySlideshow(false);
            f = 1.0f;
        }
        PhotoPageFragment.CameraAnimManager cameraAnimManager = this.mFragment.getCameraAnimManager();
        if (cameraAnimManager == null || !cameraAnimManager.isLocked()) {
            f2 = f;
        }
        this.mHostView.setBackgroundAlpha(f2);
    }
}
