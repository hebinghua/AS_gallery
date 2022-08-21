package com.miui.gallery.editor.photo.screen.home;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.miui.gallery.R;
import com.miui.gallery.app.fragment.AndroidFragment;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.paintbrush.DoodlePen;
import com.miui.gallery.editor.photo.core.imports.longcrop.LongScreenshotCropEditorView;
import com.miui.gallery.editor.photo.screen.base.IScreenOperation;
import com.miui.gallery.editor.photo.screen.core.ScreenRenderData;

/* loaded from: classes2.dex */
public class ScreenEditorFragment extends AndroidFragment implements IScreenEditorController {
    public ScreenEditorView mEditorView;
    public boolean mIsLongCrop;
    public OnScreenCropStatusChangeListener mOnScreenCropStatusChangeListener;
    public OperationUpdateListener mOperationUpdateListener;
    public Bitmap mOrigin;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.screen_editor_fragment, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ScreenEditorView screenEditorView = (ScreenEditorView) view.findViewById(R.id.screen_editor_view);
        this.mEditorView = screenEditorView;
        screenEditorView.setOperationUpdateListener(this.mOperationUpdateListener);
        this.mEditorView.setOnCropStatusChangeListener(this.mOnScreenCropStatusChangeListener);
        this.mEditorView.setLongCrop(this.mIsLongCrop);
        this.mEditorView.init();
    }

    @Override // com.miui.gallery.app.fragment.AndroidFragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.mEditorView != null) {
            int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.screen_editor_view_horizontal_margin);
            this.mEditorView.setPadding(dimensionPixelSize, getResources().getDimensionPixelSize(R.dimen.screen_editor_view_top_margin), dimensionPixelSize, getResources().getDimensionPixelSize(R.dimen.screen_editor_view_bottom_margin));
        }
    }

    @Override // com.miui.gallery.editor.photo.screen.home.IScreenEditorController
    public void setPreviewBitmap(Bitmap bitmap) {
        ScreenEditorView screenEditorView = this.mEditorView;
        if (screenEditorView != null) {
            screenEditorView.setPreviewBitmap(bitmap);
        }
    }

    @Override // com.miui.gallery.editor.photo.screen.home.IScreenEditorController
    public void setLongCropEntry(LongScreenshotCropEditorView.Entry entry) {
        ScreenEditorView screenEditorView = this.mEditorView;
        if (screenEditorView != null) {
            screenEditorView.setLongCropEntry(entry);
        }
    }

    @Override // com.miui.gallery.editor.photo.screen.home.IScreenEditorController
    public boolean setCurrentScreenEditor(int i) {
        ScreenEditorView screenEditorView = this.mEditorView;
        if (screenEditorView == null) {
            return false;
        }
        return screenEditorView.setCurrentScreenEditor(i);
    }

    public void setOriginBitmap(Bitmap bitmap) {
        this.mOrigin = bitmap;
        if (getView() != null) {
            this.mEditorView.setOriginalBitmap(this.mOrigin, 0.0f, 1.0f);
        }
    }

    @Override // com.miui.gallery.editor.photo.screen.home.IScreenEditorController
    public void setDoodlePen(DoodlePen doodlePen) {
        ScreenEditorView screenEditorView = this.mEditorView;
        if (screenEditorView != null) {
            screenEditorView.setDoodlePen(doodlePen);
        }
    }

    @Override // com.miui.gallery.editor.photo.screen.home.IScreenEditorController
    public ScreenRenderData onExport() {
        ScreenEditorView screenEditorView = this.mEditorView;
        if (screenEditorView == null) {
            return null;
        }
        return screenEditorView.onExport();
    }

    @Override // com.miui.gallery.editor.photo.screen.home.IScreenEditorController
    public void doRevoke() {
        ScreenEditorView screenEditorView = this.mEditorView;
        if (screenEditorView == null || !screenEditorView.canRevoke()) {
            return;
        }
        this.mEditorView.doRevoke();
    }

    @Override // com.miui.gallery.editor.photo.screen.home.IScreenEditorController
    public void doRevert() {
        ScreenEditorView screenEditorView = this.mEditorView;
        if (screenEditorView == null || !screenEditorView.canRevert()) {
            return;
        }
        this.mEditorView.doRevert();
    }

    @Override // com.miui.gallery.editor.photo.screen.home.IScreenEditorController
    public void setOperationUpdateListener(OperationUpdateListener operationUpdateListener) {
        this.mOperationUpdateListener = operationUpdateListener;
    }

    @Override // com.miui.gallery.editor.photo.screen.home.IScreenEditorController
    public void setOnCropStatusChangeListener(OnScreenCropStatusChangeListener onScreenCropStatusChangeListener) {
        this.mOnScreenCropStatusChangeListener = onScreenCropStatusChangeListener;
    }

    @Override // com.miui.gallery.editor.photo.screen.home.IScreenEditorController
    public void setLongCrop(boolean z) {
        this.mIsLongCrop = z;
        ScreenEditorView screenEditorView = this.mEditorView;
        if (screenEditorView != null) {
            screenEditorView.setLongCrop(z);
        }
    }

    @Override // com.miui.gallery.editor.photo.screen.home.IScreenEditorController
    public void startScreenThumbnailAnimate(ThumbnailAnimatorCallback thumbnailAnimatorCallback) {
        ScreenEditorView screenEditorView = this.mEditorView;
        if (screenEditorView != null) {
            screenEditorView.startScreenThumbnailAnimate(thumbnailAnimatorCallback);
        }
    }

    @Override // com.miui.gallery.editor.photo.screen.home.IScreenEditorController
    public boolean isModified() {
        ScreenEditorView screenEditorView = this.mEditorView;
        if (screenEditorView == null) {
            return false;
        }
        return screenEditorView.isModified();
    }

    @Override // com.miui.gallery.editor.photo.screen.home.IScreenEditorController
    public void checkTextEditor(boolean z) {
        ScreenEditorView screenEditorView = this.mEditorView;
        if (screenEditorView != null) {
            screenEditorView.checkTextEditor(z);
        }
    }

    @Override // com.miui.gallery.editor.photo.screen.home.IScreenEditorController
    public void export() {
        ScreenEditorView screenEditorView = this.mEditorView;
        if (screenEditorView != null) {
            screenEditorView.export();
        }
    }

    public boolean isCanDoSaveOperation() {
        ScreenEditorView screenEditorView = this.mEditorView;
        if (screenEditorView == null) {
            return false;
        }
        return screenEditorView.isCanDoSaveOperation();
    }

    @Override // com.miui.gallery.editor.photo.screen.home.IScreenEditorController
    public boolean isModifiedBaseLast() {
        ScreenEditorView screenEditorView = this.mEditorView;
        if (screenEditorView == null) {
            return false;
        }
        return screenEditorView.isModifiedBaseLast();
    }

    @Override // com.miui.gallery.editor.photo.screen.home.IScreenEditorController
    public <T extends IScreenOperation> T getScreenOperation(Class<T> cls) {
        ScreenEditorView screenEditorView = this.mEditorView;
        if (screenEditorView == null) {
            return null;
        }
        return (T) screenEditorView.getScreenOperation(cls);
    }
}
