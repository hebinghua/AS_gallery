package com.miui.gallery.editor.photo.core.imports.frame;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractFrameFragment;
import com.miui.gallery.editor.photo.core.common.model.FrameData;
import com.miui.gallery.editor.utils.EditorOrientationHelper;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class FrameFragment extends AbstractFrameFragment {
    public int mColor = -16777216;
    public String mFrameDes;
    public FrameEditorView mFrameEditorView;

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public void clear() {
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public int getUnSupportStringRes() {
        return R.string.frame_not_support_image_ratio;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return new FrameRenderView(layoutInflater.getContext());
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, com.miui.gallery.editor.utils.LayoutOrientationTracker.OnLayoutOrientationChangeListener
    public void onLayoutOrientationChange() {
        super.onLayoutOrientationChange();
        EditorOrientationHelper.copyLayoutParams(new FrameRenderView(getContext()), getView(), true);
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        FrameEditorView frameEditorView = (FrameEditorView) view.findViewById(R.id.frame_editor);
        this.mFrameEditorView = frameEditorView;
        frameEditorView.setBitmap(getBitmap());
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public boolean isEmpty() {
        return this.mFrameEditorView.isEmpty();
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public List<String> onSample() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.mFrameDes + "_" + Integer.toHexString(this.mColor));
        return arrayList;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public RenderData onExport() {
        return new FrameRenderData(this.mFrameEditorView.export());
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public boolean isSupportBitmap(Bitmap bitmap) {
        float height = bitmap.getHeight() / bitmap.getWidth();
        if (height < 1.0f) {
            height = 1.0f / height;
        }
        return height <= 3.0f;
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractFrameFragment
    public void setFrameData(FrameData frameData) {
        this.mFrameDes = frameData.toString();
        FrameEditorView frameEditorView = this.mFrameEditorView;
        if (frameEditorView != null) {
            frameEditorView.setFrameData(frameData.width / frameData.height, frameData.cinemaStyle);
        }
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractFrameFragment
    public void setScaleProgress(float f) {
        FrameEditorView frameEditorView = this.mFrameEditorView;
        if (frameEditorView != null) {
            frameEditorView.setScaleProgress(f);
        }
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractFrameFragment
    public void setFrameColor(int i) {
        this.mColor = i;
        FrameEditorView frameEditorView = this.mFrameEditorView;
        if (frameEditorView != null) {
            frameEditorView.setFrameColor(i);
        }
    }
}
