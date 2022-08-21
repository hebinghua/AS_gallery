package com.miui.gallery.collage.app.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.collage.core.layout.LayoutModel;
import com.miui.gallery.util.anim.FolmeUtil;

/* loaded from: classes.dex */
public class LayoutHolder extends RecyclerView.ViewHolder {
    public ImageView mImageView;
    public LayoutPreviewDrawable mLayoutPreviewDrawable;
    public ImageView mSelector;

    public LayoutHolder(ViewGroup viewGroup, Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.layout_icon_item, viewGroup, false));
        FolmeUtil.setDefaultTouchAnim(this.itemView, null, true);
        this.mImageView = (ImageView) this.itemView.findViewById(R.id.collage_item_icon);
        this.mLayoutPreviewDrawable = new LayoutPreviewDrawable(context.getResources());
        this.mSelector = (ImageView) this.itemView.findViewById(R.id.selected_item_icon);
    }

    public void setLayoutModel(LayoutModel layoutModel, int i) {
        this.mLayoutPreviewDrawable.setLayoutModel(layoutModel);
        this.mImageView.setImageDrawable(this.mLayoutPreviewDrawable);
        ImageView imageView = this.mImageView;
        imageView.setContentDescription(imageView.getResources().getString(R.string.photo_editor_talkback_effect, Integer.valueOf(i + 1)));
    }

    public void setSelectorState(boolean z) {
        if (z) {
            this.mSelector.setVisibility(0);
        } else {
            this.mSelector.setVisibility(8);
        }
    }
}
