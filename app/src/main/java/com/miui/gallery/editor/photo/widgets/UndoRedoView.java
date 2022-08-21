package com.miui.gallery.editor.photo.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.RenderRecord;

/* loaded from: classes2.dex */
public class UndoRedoView extends ConstraintLayout {
    public ImageView mRedo;
    public RenderRecord mRenderRecordListener;
    public ImageView mUndo;

    public UndoRedoView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mUndo = (ImageView) findViewById(R.id.operation_revoke);
        this.mRedo = (ImageView) findViewById(R.id.operation_revert);
        init();
    }

    public final void init() {
        this.mUndo.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.widgets.UndoRedoView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (UndoRedoView.this.mRenderRecordListener != null) {
                    UndoRedoView.this.mRenderRecordListener.previousRecord();
                }
            }
        });
        this.mRedo.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.widgets.UndoRedoView.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (UndoRedoView.this.mRenderRecordListener != null) {
                    UndoRedoView.this.mRenderRecordListener.nextRecord();
                }
            }
        });
    }

    public void setRenderRecordListener(RenderRecord renderRecord) {
        this.mRenderRecordListener = renderRecord;
    }

    public void updateBottomBar(boolean z) {
        if (z) {
            this.mUndo.setVisibility(0);
            this.mRedo.setVisibility(0);
            return;
        }
        this.mUndo.setVisibility(8);
        this.mRedo.setVisibility(8);
    }

    public void onMenuUpdated(boolean z, boolean z2) {
        if (this.mUndo.getVisibility() != 0) {
            updateBottomBar(true);
        }
        this.mUndo.setEnabled(z);
        this.mRedo.setEnabled(z2);
        if (z) {
            this.mUndo.setAlpha(1.0f);
        } else {
            this.mUndo.setAlpha(0.3f);
        }
        if (z2) {
            this.mRedo.setAlpha(1.0f);
        } else {
            this.mRedo.setAlpha(0.3f);
        }
    }

    public void onMenuEnabled(boolean z) {
        this.mUndo.setClickable(z);
        this.mRedo.setClickable(z);
    }
}
