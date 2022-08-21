package com.miui.gallery.editor.photo.widgets.ColorSelector;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.SimpleLinearRecyclerView;

/* loaded from: classes2.dex */
public class ColorSelectorView extends SimpleLinearRecyclerView {
    public ColorSelectorAdapter mColorSelectorAdapter;
    public OnItemClickListener mOnItemClickListener;
    public OnItemClickListener mOnItemClickWrapperListener;

    public ColorSelectorView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ColorSelectorView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mOnItemClickWrapperListener = new OnItemClickListener() { // from class: com.miui.gallery.editor.photo.widgets.ColorSelector.ColorSelectorView.1
            @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
            public boolean OnItemClick(RecyclerView recyclerView, View view, int i2) {
                ColorSelectorView.this.smoothScrollToPosition(i2);
                ColorSelectorView.this.setSelection(i2, true);
                if (ColorSelectorView.this.mOnItemClickListener != null) {
                    ColorSelectorView.this.mOnItemClickListener.OnItemClick(recyclerView, view, i2);
                    return false;
                }
                return false;
            }
        };
        setAlwaysDisableSpring(true);
        setEnableItemClickWhileSettling(true);
    }

    public void init(String[] strArr) {
        ColorSelectorAdapter colorSelectorAdapter = new ColorSelectorAdapter(getContext(), strArr);
        this.mColorSelectorAdapter = colorSelectorAdapter;
        colorSelectorAdapter.setOnItemClickListener(this.mOnItemClickWrapperListener);
        setAdapter(this.mColorSelectorAdapter);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setSelection(int i, boolean z) {
        refreshSelection(i);
        this.mColorSelectorAdapter.setSelection(i);
        if (z) {
            smoothScrollToPosition(i);
            return;
        }
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
        if (linearLayoutManager == null) {
            return;
        }
        linearLayoutManager.scrollToPositionWithOffset(i, 0);
    }

    public void setSelectionWithoutMove(int i) {
        refreshSelection(i);
        this.mColorSelectorAdapter.setSelection(i);
    }

    public int getSelection() {
        return this.mColorSelectorAdapter.getSelection();
    }

    public void refreshSelection(int i) {
        ColorSelectorAdapter colorSelectorAdapter = this.mColorSelectorAdapter;
        colorSelectorAdapter.notifyItemChanged(colorSelectorAdapter.getSelection());
        this.mColorSelectorAdapter.notifyItemChanged(i);
    }

    public void setColorTexts(String[] strArr) {
        this.mColorSelectorAdapter.setColorTexts(strArr);
    }

    public String[] getColorTexts() {
        return this.mColorSelectorAdapter.getColorTexts();
    }

    public void setItemMargin(ViewGroup.MarginLayoutParams marginLayoutParams) {
        this.mColorSelectorAdapter.setCircularViewLayoutParam(marginLayoutParams);
    }
}
