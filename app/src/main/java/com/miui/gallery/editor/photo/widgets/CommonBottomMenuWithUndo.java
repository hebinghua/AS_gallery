package com.miui.gallery.editor.photo.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.RenderRecord;

/* loaded from: classes2.dex */
public class CommonBottomMenuWithUndo extends LinearLayout {
    public ImageView mCancel;
    public ImageView mOk;
    public ImageView mRedo;
    public RenderRecord mRenderRecordListener;
    public boolean mShowTitle;
    public TextView mTitle;
    public ImageView mUndo;

    public CommonBottomMenuWithUndo(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mShowTitle = true;
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mUndo = (ImageView) findViewById(R.id.undo);
        this.mRedo = (ImageView) findViewById(R.id.redo);
        this.mTitle = (TextView) findViewById(R.id.title);
        this.mCancel = (ImageView) findViewById(R.id.cancel);
        this.mOk = (ImageView) findViewById(R.id.ok);
        init();
    }

    public final void init() {
        this.mUndo.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.widgets.CommonBottomMenuWithUndo.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (CommonBottomMenuWithUndo.this.mRenderRecordListener != null) {
                    CommonBottomMenuWithUndo.this.mRenderRecordListener.previousRecord();
                }
            }
        });
        this.mRedo.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.widgets.CommonBottomMenuWithUndo.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (CommonBottomMenuWithUndo.this.mRenderRecordListener != null) {
                    CommonBottomMenuWithUndo.this.mRenderRecordListener.nextRecord();
                }
            }
        });
    }

    public void setTitle(int i) {
        this.mTitle.setText(i);
    }

    public TextView getTitle() {
        return this.mTitle;
    }

    public void setRenderRecordListener(RenderRecord renderRecord) {
        this.mRenderRecordListener = renderRecord;
    }

    public void updateBottomBar(boolean z) {
        if (z) {
            this.mTitle.setVisibility(0);
            this.mUndo.setVisibility(8);
            this.mRedo.setVisibility(8);
            return;
        }
        this.mTitle.setVisibility(8);
        this.mUndo.setVisibility(0);
        this.mRedo.setVisibility(0);
    }

    public void onMenuUpdated(boolean z, boolean z2) {
        if (this.mShowTitle) {
            updateBottomBar(false);
            this.mShowTitle = false;
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

    public boolean isShowTitle() {
        return this.mShowTitle;
    }
}
