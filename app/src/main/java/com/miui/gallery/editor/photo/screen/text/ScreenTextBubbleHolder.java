package com.miui.gallery.editor.photo.screen.text;

import android.view.View;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.common.model.TextData;
import com.miui.gallery.util.anim.FolmeUtil;

/* loaded from: classes2.dex */
public class ScreenTextBubbleHolder extends RecyclerView.ViewHolder {
    public ImageView mImageView;

    public ScreenTextBubbleHolder(View view) {
        super(view);
        FolmeUtil.setDefaultTouchAnim(view, null, true);
        this.mImageView = (ImageView) view.findViewById(R.id.img);
    }

    public void bind(TextData textData, int i) {
        Glide.with(this.itemView).mo990load(textData.iconPath).mo950diskCacheStrategy(DiskCacheStrategy.NONE).mo978skipMemoryCache(true).into(this.mImageView);
        ImageView imageView = this.mImageView;
        imageView.setContentDescription(imageView.getResources().getString(R.string.photo_editor_talkback_effect, Integer.valueOf(i + 1)));
    }
}
