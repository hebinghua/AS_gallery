package com.miui.gallery.editor.photo.app.sticker;

import android.view.View;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.editor.photo.core.common.model.StickerCategory;

/* loaded from: classes2.dex */
public class HeaderHolder extends RecyclerView.ViewHolder {
    public ImageView mTitle;

    public HeaderHolder(View view) {
        super(view);
        if (view instanceof ImageView) {
            this.mTitle = (ImageView) view;
        }
    }

    public void bind(StickerCategory stickerCategory) {
        ImageView imageView = this.mTitle;
        if (imageView != null) {
            int identifier = imageView.getContext().getResources().getIdentifier(stickerCategory.name, "drawable", "com.miui.gallery");
            if (identifier != 0) {
                this.mTitle.setImageResource(identifier);
            }
            this.itemView.setContentDescription(stickerCategory.talkbackName);
        }
    }
}
