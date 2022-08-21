package com.miui.gallery.vlog.clip;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.editor.R$id;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.vlog.entity.TransData;

/* loaded from: classes2.dex */
public class TransViewHolder extends RecyclerView.ViewHolder {
    public ImageView mPreviewView;
    public TextView mTitleView;

    public TransViewHolder(View view) {
        super(view);
        FolmeUtil.setDefaultTouchAnim(view, null, true);
        this.mPreviewView = (ImageView) view.findViewById(R$id.icon);
        this.mTitleView = (TextView) view.findViewById(R$id.text);
    }

    public void setName(int i) {
        if (i == 0) {
            return;
        }
        this.mTitleView.setText(i);
    }

    public void setIcon(TransData transData) {
        int iconResId = transData.getIconResId();
        if (transData.isNone()) {
            this.mPreviewView.setImageResource(iconResId);
        } else if (iconResId == 0) {
        } else {
            this.mPreviewView.setImageResource(iconResId);
        }
    }
}
