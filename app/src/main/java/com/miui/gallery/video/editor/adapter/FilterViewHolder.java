package com.miui.gallery.video.editor.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.video.editor.widget.SingleChoiceRecyclerView;

/* loaded from: classes2.dex */
public class FilterViewHolder extends SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter.SingleChoiceViewHolder {
    public ImageView mIcon;
    public TextView mNameTextView;
    public View mSelector;

    public FilterViewHolder(View view) {
        super(view);
        FolmeUtil.setDefaultTouchAnim(view, null, true);
        this.mIcon = (ImageView) view.findViewById(R.id.icon);
        this.mNameTextView = (TextView) view.findViewById(R.id.name);
        this.mSelector = view.findViewById(R.id.selector);
    }

    @Override // com.miui.gallery.video.editor.widget.SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter.SingleChoiceViewHolder
    public void setSelect(boolean z) {
        this.itemView.setSelected(z);
        View view = this.mSelector;
        if (view != null) {
            view.setVisibility(z ? 0 : 8);
            if (z) {
                this.mNameTextView.setBackground(null);
            } else {
                this.mNameTextView.setBackgroundResource(R.drawable.video_editor_filter_text_background_shape);
            }
        }
    }

    public void setName(int i) {
        if (i != 0) {
            this.mNameTextView.setText(i);
        }
    }

    public void setIcon(int i) {
        if (i != 0) {
            this.mIcon.setImageResource(i);
        }
    }
}
