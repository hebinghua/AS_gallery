package com.miui.gallery.editor.photo.app.doodle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.util.anim.FolmeUtil;

/* loaded from: classes2.dex */
public class DoodleHolder extends RecyclerView.ViewHolder {
    public ImageView mImageView;
    public View mSelectorView;

    public DoodleHolder(Context context, ViewGroup viewGroup) {
        super(LayoutInflater.from(context).inflate(R.layout.doodle_menu_item, viewGroup, false));
        FolmeUtil.setDefaultTouchAnim(this.itemView, null, true);
        this.mImageView = (ImageView) this.itemView.findViewById(R.id.img);
        this.mSelectorView = this.itemView.findViewById(R.id.selector_view);
    }

    public void setIconPath(int i, int i2, boolean z) {
        this.mSelectorView.setVisibility(z ? 0 : 8);
        this.mImageView.setImageResource(i);
    }
}
