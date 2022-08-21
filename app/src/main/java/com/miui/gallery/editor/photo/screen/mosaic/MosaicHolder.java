package com.miui.gallery.editor.photo.screen.mosaic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.miui.gallery.R;
import com.miui.gallery.util.anim.FolmeUtil;

/* loaded from: classes2.dex */
public class MosaicHolder extends RecyclerView.ViewHolder {
    public ImageView mImageView;

    public MosaicHolder(Context context, ViewGroup viewGroup) {
        super(LayoutInflater.from(context).inflate(R.layout.screen_mosaic_menu_item, viewGroup, false));
        FolmeUtil.setDefaultTouchAnim(this.itemView, null, true);
        this.mImageView = (ImageView) this.itemView.findViewById(R.id.img);
    }

    public void setIconPath(String str, int i) {
        Glide.with(this.itemView).mo990load(str).mo950diskCacheStrategy(DiskCacheStrategy.NONE).into(this.mImageView);
        ImageView imageView = this.mImageView;
        imageView.setContentDescription(imageView.getResources().getString(R.string.photo_editor_talkback_effect, Integer.valueOf(i + 1)));
    }
}
