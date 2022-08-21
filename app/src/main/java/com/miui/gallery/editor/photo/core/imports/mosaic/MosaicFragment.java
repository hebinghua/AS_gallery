package com.miui.gallery.editor.photo.core.imports.mosaic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.OperationUpdateListener;
import com.miui.gallery.editor.photo.app.RenderRecord;
import com.miui.gallery.editor.photo.app.RenderRecordAdapter;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractMosaicFragment;
import com.miui.gallery.editor.photo.core.common.model.MosaicData;
import com.miui.gallery.editor.photo.widgets.UndoRedoView;
import com.miui.gallery.editor.utils.EditorOrientationHelper;
import java.util.List;

/* loaded from: classes2.dex */
public class MosaicFragment extends AbstractMosaicFragment implements SurfaceHolder.Callback {
    public MosaicGLView mMosaicView;
    public MosaicGLEntity mNextEntity;
    public ImageView mPlaceholderView;
    public TextView mTitleView;
    public UndoRedoView mUndoRedoView;
    public int mNextPaintSize = -1;
    public final RenderRecord mRenderRecordAdapter = new RenderRecordAdapter() { // from class: com.miui.gallery.editor.photo.core.imports.mosaic.MosaicFragment.1
        {
            MosaicFragment.this = this;
        }

        @Override // com.miui.gallery.editor.photo.app.RenderRecord
        public void previousRecord() {
            MosaicFragment.this.doRevoke();
        }

        @Override // com.miui.gallery.editor.photo.app.RenderRecord
        public void nextRecord() {
            MosaicFragment.this.doRevert();
        }
    };
    public OperationUpdateListener mOperationUpdateListener = new OperationUpdateListener() { // from class: com.miui.gallery.editor.photo.core.imports.mosaic.MosaicFragment.2
        {
            MosaicFragment.this = this;
        }

        @Override // com.miui.gallery.editor.photo.app.OperationUpdateListener
        public void onOperationUpdate() {
            MosaicFragment.this.refreshOperationPanel();
        }
    };

    public static /* synthetic */ void $r8$lambda$pn5yDXuixZ3S0Lc9NCH0kguEMAY(MosaicFragment mosaicFragment) {
        mosaicFragment.lambda$surfaceCreated$0();
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return new MosaicRenderView(layoutInflater.getContext());
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, com.miui.gallery.editor.utils.LayoutOrientationTracker.OnLayoutOrientationChangeListener
    public void onLayoutOrientationChange() {
        super.onLayoutOrientationChange();
        EditorOrientationHelper.copyLayoutParams(new MosaicRenderView(getContext()), getView(), true);
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        MosaicGLView mosaicGLView = (MosaicGLView) view.findViewById(R.id.mosaic_view);
        this.mMosaicView = mosaicGLView;
        mosaicGLView.setZOrderOnTop(false);
        this.mMosaicView.setBitmap(getBitmap());
        MosaicGLEntity mosaicGLEntity = this.mNextEntity;
        if (mosaicGLEntity != null) {
            this.mMosaicView.setCurrentEntity(mosaicGLEntity);
            this.mNextEntity = null;
        }
        int i = this.mNextPaintSize;
        if (i != 1) {
            this.mMosaicView.setMosaicPaintSize(i);
            this.mNextPaintSize = -1;
        }
        OperationUpdateListener operationUpdateListener = this.mOperationUpdateListener;
        if (operationUpdateListener != null) {
            this.mMosaicView.setOperationUpdateListener(operationUpdateListener);
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.image_placeholder);
        this.mPlaceholderView = imageView;
        imageView.setImageBitmap(getBitmap());
        this.mMosaicView.setEnabled(false);
        this.mMosaicView.getHolder().addCallback(this);
        UndoRedoView undoRedoView = (UndoRedoView) view.findViewById(R.id.undo_redo);
        this.mUndoRedoView = undoRedoView;
        undoRedoView.setRenderRecordListener(this.mRenderRecordAdapter);
        this.mTitleView = getTitleView();
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public boolean isEmpty() {
        return this.mMosaicView.isEmpty();
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public List<String> onSample() {
        return this.mMosaicView.generateSample();
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public RenderData onExport() {
        return new MosaicRenderData(this.mMosaicView.export());
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public void clear() {
        this.mMosaicView.onClear();
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractMosaicFragment
    public void setMosaicData(MosaicData mosaicData) {
        MosaicGLEntity mosaicGLEntity = (MosaicGLEntity) mosaicData;
        MosaicGLView mosaicGLView = this.mMosaicView;
        if (mosaicGLView != null) {
            mosaicGLView.setCurrentEntity(mosaicGLEntity);
        } else {
            this.mNextEntity = mosaicGLEntity;
        }
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractMosaicFragment
    public void setMosaicPaintSize(int i) {
        MosaicGLView mosaicGLView = this.mMosaicView;
        if (mosaicGLView != null) {
            mosaicGLView.setMosaicPaintSize(i);
        } else {
            this.mNextPaintSize = i;
        }
    }

    public boolean canRevoke() {
        return this.mMosaicView.canRevoke();
    }

    public boolean canRevert() {
        return this.mMosaicView.canRevert();
    }

    public void doRevoke() {
        this.mMosaicView.doRevoke();
    }

    public void doRevert() {
        this.mMosaicView.doRevert();
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractMosaicFragment
    public boolean isDrawingMosaic() {
        return this.mMosaicView.isDrawingMosaic();
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.mMosaicView.postDelayed(new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.mosaic.MosaicFragment$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                MosaicFragment.$r8$lambda$pn5yDXuixZ3S0Lc9NCH0kguEMAY(MosaicFragment.this);
            }
        }, 200L);
    }

    public /* synthetic */ void lambda$surfaceCreated$0() {
        ImageView imageView = this.mPlaceholderView;
        if (imageView != null) {
            imageView.setVisibility(8);
            this.mMosaicView.setEnabled(true);
        }
    }

    public final void refreshOperationPanel() {
        if (canRevoke() || canRevert()) {
            this.mUndoRedoView.onMenuUpdated(canRevoke(), canRevert());
            this.mTitleView.setVisibility(8);
        }
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        MosaicGLView mosaicGLView = this.mMosaicView;
        if (mosaicGLView != null) {
            mosaicGLView.setVisibility(8);
        }
    }
}
