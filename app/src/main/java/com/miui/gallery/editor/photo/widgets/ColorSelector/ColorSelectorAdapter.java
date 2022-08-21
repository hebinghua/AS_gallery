package com.miui.gallery.editor.photo.widgets.ColorSelector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.widgets.recyclerview.Selectable;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.widget.recyclerview.Adapter;

/* loaded from: classes2.dex */
public class ColorSelectorAdapter extends Adapter<NormalTextViewHolder> implements Selectable {
    public ViewGroup.MarginLayoutParams mCircularViewLayoutParam;
    public String[] mColorTexts;
    public final LayoutInflater mLayoutInflater;
    public int mSelectPosition;

    public ColorSelectorAdapter(Context context, String[] strArr) {
        this.mColorTexts = strArr;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public NormalTextViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new NormalTextViewHolder(this.mLayoutInflater.inflate(R.layout.color_selector_item, viewGroup, false));
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(NormalTextViewHolder normalTextViewHolder, int i) {
        super.onBindViewHolder((ColorSelectorAdapter) normalTextViewHolder, i);
        normalTextViewHolder.bind(i);
    }

    public void setSelection(int i) {
        this.mSelectPosition = i;
    }

    @Override // com.miui.gallery.editor.photo.widgets.recyclerview.Selectable
    public int getSelection() {
        return this.mSelectPosition;
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        String[] strArr = this.mColorTexts;
        if (strArr == null) {
            return 0;
        }
        return strArr.length;
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, android.view.View.OnClickListener
    public void onClick(View view) {
        super.onClick(view);
        this.mSelectPosition = ((Integer) view.getTag()).intValue();
    }

    public String[] getColorTexts() {
        return this.mColorTexts;
    }

    public void setColorTexts(String[] strArr) {
        this.mColorTexts = strArr;
        notifyDataSetChanged();
    }

    /* loaded from: classes2.dex */
    public class NormalTextViewHolder extends RecyclerView.ViewHolder {
        public CircularView mCircularView;

        public NormalTextViewHolder(View view) {
            super(view);
            FolmeUtil.setDefaultTouchAnim(view, null, true);
            this.mCircularView = (CircularView) view.findViewById(R.id.circular_view);
        }

        public void bind(int i) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mCircularView.getLayoutParams();
            if (ColorSelectorAdapter.this.mCircularViewLayoutParam != null) {
                marginLayoutParams = ColorSelectorAdapter.this.mCircularViewLayoutParam;
            }
            this.mCircularView.setLayoutParams(marginLayoutParams);
            this.mCircularView.setColorTxt(ColorSelectorAdapter.this.mColorTexts[i]);
            this.mCircularView.setIsSelect(ColorSelectorAdapter.this.mSelectPosition == i);
            this.mCircularView.invalidate();
        }
    }

    public void setCircularViewLayoutParam(ViewGroup.MarginLayoutParams marginLayoutParams) {
        this.mCircularViewLayoutParam = marginLayoutParams;
    }
}
