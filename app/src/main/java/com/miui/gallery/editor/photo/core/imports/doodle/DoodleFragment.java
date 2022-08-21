package com.miui.gallery.editor.photo.core.imports.doodle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.OperationUpdateListener;
import com.miui.gallery.editor.photo.app.RenderRecord;
import com.miui.gallery.editor.photo.app.RenderRecordAdapter;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractDoodleFragment;
import com.miui.gallery.editor.photo.core.common.model.DoodleData;
import com.miui.gallery.editor.photo.core.imports.doodle.DoodleEditorView;
import com.miui.gallery.editor.photo.widgets.UndoRedoView;
import com.miui.gallery.editor.utils.EditorOrientationHelper;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class DoodleFragment extends AbstractDoodleFragment {
    public DoodleEditorView mDoodleEditorView;
    public TextView mTitleView;
    public UndoRedoView mUndoRedoView;
    public int mCurrentColor = -16777216;
    public List<String> mStats = new ArrayList();
    public final RenderRecord mRenderRecordAdapter = new RenderRecordAdapter() { // from class: com.miui.gallery.editor.photo.core.imports.doodle.DoodleFragment.1
        {
            DoodleFragment.this = this;
        }

        @Override // com.miui.gallery.editor.photo.app.RenderRecord
        public void previousRecord() {
            DoodleFragment.this.doRevoke();
        }

        @Override // com.miui.gallery.editor.photo.app.RenderRecord
        public void nextRecord() {
            DoodleFragment.this.doRevert();
        }
    };
    public View.OnTouchListener mCompareTouchListener = new View.OnTouchListener() { // from class: com.miui.gallery.editor.photo.core.imports.doodle.DoodleFragment.2
        {
            DoodleFragment.this = this;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0) {
                DoodleFragment.this.mDoodleEditorView.setRenderOriginOnly(true);
                DoodleFragment.this.mDoodleEditorView.invalidate();
            } else if (1 == motionEvent.getAction() || 3 == motionEvent.getAction()) {
                DoodleFragment.this.mDoodleEditorView.setRenderOriginOnly(false);
                DoodleFragment.this.mDoodleEditorView.invalidate();
            }
            return true;
        }
    };
    public OperationUpdateListener mOperationUpdateListener = new OperationUpdateListener() { // from class: com.miui.gallery.editor.photo.core.imports.doodle.DoodleFragment$$ExternalSyntheticLambda0
        @Override // com.miui.gallery.editor.photo.app.OperationUpdateListener
        public final void onOperationUpdate() {
            DoodleFragment.$r8$lambda$JUfh4o4Z3Kdu3qDGJZrOyOWnatU(DoodleFragment.this);
        }
    };
    public DoodleEditorView.DoodleCallback mDoodleCallback = new DoodleEditorView.DoodleCallback() { // from class: com.miui.gallery.editor.photo.core.imports.doodle.DoodleFragment.3
        {
            DoodleFragment.this = this;
        }

        @Override // com.miui.gallery.editor.photo.core.imports.doodle.DoodleEditorView.DoodleCallback
        public void onDoodleGenerate(String str, int i) {
            List list = DoodleFragment.this.mStats;
            list.add(str + "_" + Integer.toHexString(i));
        }
    };

    public static /* synthetic */ void $r8$lambda$JUfh4o4Z3Kdu3qDGJZrOyOWnatU(DoodleFragment doodleFragment) {
        doodleFragment.lambda$new$0();
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return new DoodleRenderView(viewGroup.getContext());
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, com.miui.gallery.editor.utils.LayoutOrientationTracker.OnLayoutOrientationChangeListener
    public void onLayoutOrientationChange() {
        super.onLayoutOrientationChange();
        EditorOrientationHelper.copyLayoutParams(new DoodleRenderView(getContext()), getView(), true);
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        DoodleEditorView doodleEditorView = (DoodleEditorView) view.findViewById(R.id.doodle_editor_view);
        this.mDoodleEditorView = doodleEditorView;
        doodleEditorView.setBitmap(getBitmap());
        this.mDoodleEditorView.setColor(this.mCurrentColor);
        this.mDoodleEditorView.setDoodleCallback(this.mDoodleCallback);
        this.mDoodleEditorView.setOperationUpdateListener(this.mOperationUpdateListener);
        this.mTitleView = (TextView) view.findViewById(R.id.effect_title);
        UndoRedoView undoRedoView = (UndoRedoView) view.findViewById(R.id.undo_redo);
        this.mUndoRedoView = undoRedoView;
        undoRedoView.setRenderRecordListener(this.mRenderRecordAdapter);
        setCompareTouchListener(this.mCompareTouchListener);
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        hideCompareButton();
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public boolean isEmpty() {
        return this.mDoodleEditorView.isEmpty();
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public List<String> onSample() {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.mStats);
        return arrayList;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public RenderData onExport() {
        return new DoodleRenderData(this.mDoodleEditorView.export());
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public void clear() {
        this.mDoodleEditorView.onClear();
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractDoodleFragment
    public void setDoodleData(DoodleData doodleData) {
        DoodleEditorView doodleEditorView = this.mDoodleEditorView;
        if (doodleEditorView != null) {
            doodleEditorView.setCurrentDoodleItem(((DoodleConfig) doodleData).getDoodleItem());
            this.mDoodleEditorView.clearActivation();
        }
    }

    /* renamed from: refreshOperationPanel */
    public final void lambda$new$0() {
        this.mUndoRedoView.onMenuUpdated(canRevoke(), canRevert());
        this.mTitleView.setVisibility(8);
        if (canRevoke()) {
            showCompareButton();
        } else {
            hideCompareButton();
        }
    }

    public boolean canRevoke() {
        return this.mDoodleEditorView.canRevoke();
    }

    public boolean canRevert() {
        return this.mDoodleEditorView.canRevert();
    }

    public void doRevoke() {
        this.mDoodleEditorView.doRevoke();
    }

    public void doRevert() {
        this.mDoodleEditorView.doRevert();
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractDoodleFragment
    public void setColor(int i) {
        this.mCurrentColor = i;
        DoodleEditorView doodleEditorView = this.mDoodleEditorView;
        if (doodleEditorView != null) {
            doodleEditorView.setColor(i);
        }
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractDoodleFragment
    public void setPaintSize(float f) {
        this.mDoodleEditorView.setPaintSize(f);
    }
}
