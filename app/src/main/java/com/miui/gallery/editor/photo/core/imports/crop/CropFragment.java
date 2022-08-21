package com.miui.gallery.editor.photo.core.imports.crop;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.crop.AutoCropData;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractCropFragment;
import com.miui.gallery.editor.photo.core.common.model.CropData;
import com.miui.gallery.editor.photo.core.imports.obsoletes.Crop;
import com.miui.gallery.editor.photo.core.imports.obsoletes.EditorView;
import com.miui.gallery.editor.utils.EditorOrientationHelper;
import com.miui.gallery.preference.GalleryPreferences;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public class CropFragment extends AbstractCropFragment {
    public CropData.AspectRatio mAspectRatio;
    public float mAutoCropDegree;
    public int mCanvasRotateTimes;
    public Crop mCrop;
    public EditorView mEditorView;
    public boolean mMirrored;
    public Crop.OnCropChangedListener mOnCropChangedListener = new Crop.OnCropChangedListener() { // from class: com.miui.gallery.editor.photo.core.imports.crop.CropFragment.1
        @Override // com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.OnCropChangedListener
        public void onChanged() {
            CropFragment.this.onChanged();
        }

        @Override // com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.OnCropChangedListener
        public void onRatioChanged() {
            CropFragment.this.notifyCropped();
        }

        @Override // com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.OnCropChangedListener
        public void changeRotationState(boolean z) {
            CropFragment.this.changeRotationState(z);
        }

        @Override // com.miui.gallery.editor.photo.core.imports.obsoletes.Crop.OnCropChangedListener
        public void onRotateChanged() {
            CropFragment.access$108(CropFragment.this);
        }
    };
    public View.OnLayoutChangeListener mOnLayoutChangeListener = new View.OnLayoutChangeListener() { // from class: com.miui.gallery.editor.photo.core.imports.crop.CropFragment.2
        @Override // android.view.View.OnLayoutChangeListener
        public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            CropFragment.this.mCrop.start();
        }
    };
    public float mTuningDegree;

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public boolean isSupportAnimation() {
        return true;
    }

    public static /* synthetic */ int access$108(CropFragment cropFragment) {
        int i = cropFragment.mCanvasRotateTimes;
        cropFragment.mCanvasRotateTimes = i + 1;
        return i;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return new CropRenderView(viewGroup.getContext());
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, com.miui.gallery.editor.utils.LayoutOrientationTracker.OnLayoutOrientationChangeListener
    public void onLayoutOrientationChange() {
        super.onLayoutOrientationChange();
        EditorOrientationHelper.copyLayoutParams(new CropRenderView(getContext()), getView(), true);
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mEditorView = (EditorView) view.findViewById(R.id.editor_view);
        Crop crop = new Crop(getActivity());
        this.mCrop = crop;
        this.mEditorView.install(crop);
        this.mEditorView.setDrawBoundLine(false);
        this.mEditorView.setBitmap(getBitmap());
        this.mEditorView.addOnLayoutChangeListener(this.mOnLayoutChangeListener);
        this.mCrop.setOnCropChangedListener(this.mOnCropChangedListener);
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        this.mEditorView.removeOnLayoutChangeListener(this.mOnLayoutChangeListener);
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public RenderData onExport() {
        return new CropStateData(this.mCrop.export());
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public List<String> onSample() {
        boolean z;
        ArrayList arrayList = new ArrayList();
        if (this.mMirrored) {
            arrayList.add("mirror");
        }
        float f = this.mTuningDegree;
        if (f != 0.0f) {
            if (this.mAutoCropDegree == f) {
                arrayList.add("rotate_auto");
            } else {
                arrayList.add("rotate_manual");
            }
            z = true;
        } else {
            z = false;
        }
        if (this.mCanvasRotateTimes % 4 != 0) {
            arrayList.add("rotate_canvas");
            z = true;
        }
        if (z) {
            arrayList.add("rotate");
        }
        RectF sampleSize = this.mCrop.getSampleSize();
        RectF croppedSize = this.mCrop.getCroppedSize();
        if (Math.round(sampleSize.width()) != Math.round(croppedSize.width()) || Math.round(sampleSize.height()) != Math.round(croppedSize.height())) {
            arrayList.add("crop");
        }
        Locale locale = Locale.US;
        Object[] objArr = new Object[1];
        CropData.AspectRatio aspectRatio = this.mAspectRatio;
        objArr[0] = aspectRatio == null ? "free" : aspectRatio.name;
        arrayList.add(String.format(locale, "aspect_ratio: %s", objArr));
        return arrayList;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public void clear() {
        this.mCrop.reset();
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractCropFragment
    public void restore() {
        super.restore();
        this.mEditorView.setBitmap(getBitmap());
        this.mCrop.reset();
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public void setBitmap(Bitmap bitmap) {
        super.setBitmap(bitmap);
        EditorView editorView = this.mEditorView;
        if (editorView != null) {
            editorView.setBitmap(bitmap);
        }
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public boolean isEmpty() {
        RectF sampleSize = this.mCrop.getSampleSize();
        RectF croppedSize = this.mCrop.getCroppedSize();
        return !this.mMirrored && this.mTuningDegree == 0.0f && this.mCanvasRotateTimes % 4 == 0 && !(Math.round(sampleSize.width()) != Math.round(croppedSize.width()) || Math.round(sampleSize.height()) != Math.round(croppedSize.height()));
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractCropFragment
    public void onSetAspectRatio(CropData.AspectRatio aspectRatio) {
        this.mCrop.setFixedAspectRatio(aspectRatio.width, aspectRatio.height);
        this.mAspectRatio = aspectRatio;
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractCropFragment
    public void onDoMirror() {
        this.mCrop.mirror();
        this.mMirrored = !this.mMirrored;
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractCropFragment
    public void onDoRotate() {
        this.mCrop.rotate();
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractCropFragment
    public void prepareTuning() {
        this.mCrop.beginRotate();
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractCropFragment
    public void finishTuning() {
        this.mCrop.finishRotate();
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractCropFragment
    public void onTurning(float f) {
        this.mCrop.setRotateDegree(f);
        this.mTuningDegree = f;
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractCropFragment
    public void onAutoCrop(AutoCropData autoCropData) {
        this.mCrop.performAutoCrop(autoCropData);
        this.mTuningDegree = autoCropData.getDegree();
        this.mAutoCropDegree = autoCropData.getDegree();
        if (GalleryPreferences.PhotoEditor.isCropTipsShow()) {
            GalleryPreferences.PhotoEditor.addCropTipsShowTimes();
            notifyAutoCropFinished();
        }
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractCropFragment
    public void hideGuideLines() {
        this.mCrop.hideGuideLine();
    }

    public boolean isCanDoSaveOperation() {
        return this.mCrop.isCanDoSaveOperation();
    }
}
