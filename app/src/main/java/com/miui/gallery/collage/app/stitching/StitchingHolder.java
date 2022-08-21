package com.miui.gallery.collage.app.stitching;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.miui.gallery.R;
import com.miui.gallery.collage.core.stitching.StitchingModel;
import com.miui.gallery.util.Scheme;
import com.miui.gallery.util.anim.FolmeUtil;
import java.io.File;
import java.util.Locale;

/* loaded from: classes.dex */
public class StitchingHolder extends RecyclerView.ViewHolder {
    public ImageView mImageView;
    public ImageView mSelector;

    public StitchingHolder(ViewGroup viewGroup, Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.poster_icon_item, viewGroup, false));
        FolmeUtil.setDefaultTouchAnim(this.itemView, null, true);
        this.mImageView = (ImageView) this.itemView.findViewById(R.id.collage_item_icon);
        this.mSelector = (ImageView) this.itemView.findViewById(R.id.selected_item_icon);
    }

    public void setStitchingModel(Resources resources, StitchingModel stitchingModel, int i) {
        Glide.with(this.mImageView).mo990load(Scheme.ASSETS.wrap(String.format(Locale.US, "%s%s%s%s", stitchingModel.relativePath, File.separator, "preview", ".png"))).mo970override(Integer.MIN_VALUE).mo950diskCacheStrategy(DiskCacheStrategy.NONE).into(this.mImageView);
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
