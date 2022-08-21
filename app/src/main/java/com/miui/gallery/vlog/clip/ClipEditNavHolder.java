package com.miui.gallery.vlog.clip;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.widget.recyclerview.SingleChoiceRecyclerView;

/* loaded from: classes2.dex */
public class ClipEditNavHolder extends SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter.SingleChoiceViewHolder {
    public ImageView mImageView;
    public TextView mTextView;

    @Override // com.miui.gallery.widget.recyclerview.SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter.SingleChoiceViewHolder
    public void setSelect(boolean z) {
    }

    public ClipEditNavHolder(View view) {
        super(view);
        FolmeUtil.setCustomTouchAnim(view, new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build(), null, null, true);
        this.mItemView = view;
        this.mImageView = (ImageView) view.findViewById(R$id.img);
        this.mTextView = (TextView) view.findViewById(R$id.title);
    }

    public void setTitle(int i) {
        this.mTextView.setText(i);
    }

    public void setTitle(String str) {
        this.mTextView.setText(str);
    }

    public void setImage(int i) {
        this.mImageView.setImageResource(i);
    }

    public void setItemState(ClipEditNavItem clipEditNavItem, boolean z) {
        if (!z) {
            this.mImageView.setColorFilter(-7829368);
            this.mTextView.setTextColor(-7829368);
            return;
        }
        this.mImageView.setColorFilter(-1);
        this.mTextView.setTextColor(-1);
    }
}
