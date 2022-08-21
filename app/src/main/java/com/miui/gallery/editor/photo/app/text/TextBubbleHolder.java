package com.miui.gallery.editor.photo.app.text;

import android.view.View;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.common.model.TextData;
import com.miui.gallery.editor.photo.core.imports.text.TextConfig;
import com.miui.gallery.glide.GlideApp;
import com.miui.gallery.util.anim.FolmeUtil;

/* loaded from: classes2.dex */
public class TextBubbleHolder extends RecyclerView.ViewHolder {
    public ImageView mImageView;

    public TextBubbleHolder(View view) {
        super(view);
        FolmeUtil.setDefaultTouchAnim(view, null, true);
        this.mImageView = (ImageView) view.findViewById(R.id.img);
    }

    public void bind(TextData textData) {
        GlideApp.with(this.itemView).mo990load(textData.iconPath).skipCache().into(this.mImageView);
        if (textData instanceof TextConfig) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) this.itemView.getLayoutParams();
            int dimensionPixelSize = this.itemView.getResources().getDimensionPixelSize(R.dimen.editor_menu_filter_category_gap) - this.itemView.getResources().getDimensionPixelSize(R.dimen.editor_menu_text_view_pager_horizontal_interval);
            TextConfig textConfig = (TextConfig) textData;
            if (!textConfig.isLast()) {
                dimensionPixelSize = 0;
            }
            layoutParams.setMarginEnd(dimensionPixelSize);
            this.itemView.setLayoutParams(layoutParams);
            if (textConfig.getItemType() == 1) {
                this.itemView.setContentDescription(GalleryApp.sGetAndroidContext().getResources().getString(R.string.text_item_type_talkback_bubble_signature));
            } else {
                this.itemView.setContentDescription(textData.talkbackName);
            }
        }
    }
}
