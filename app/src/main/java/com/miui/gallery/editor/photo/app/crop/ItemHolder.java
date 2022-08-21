package com.miui.gallery.editor.photo.app.crop;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.common.model.CropData;
import com.miui.gallery.util.anim.FolmeUtil;

/* loaded from: classes2.dex */
public class ItemHolder extends RecyclerView.ViewHolder {
    public TextView mTextView;
    public ImageView mView;

    public ItemHolder(View view) {
        super(view);
        FolmeUtil.setDefaultTouchAnim(view, null, true);
        this.mView = (ImageView) view.findViewById(R.id.icon);
        this.mTextView = (TextView) view.findViewById(R.id.icon_name);
    }

    public void bind(CropData cropData) {
        this.mView.setImageResource(cropData.icon);
        ImageView imageView = this.mView;
        imageView.setContentDescription(imageView.getResources().getString(cropData.talkbackName));
        this.mTextView.setText(cropData.name);
    }
}
