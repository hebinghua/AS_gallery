package com.miui.gallery.video.editor.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.video.editor.model.MenuFragmentData;

/* loaded from: classes2.dex */
public class VideoNavHolder extends RecyclerView.ViewHolder {
    public ImageView mImageView;
    public TextView mView;

    public VideoNavHolder(View view) {
        super(view);
        FolmeUtil.setCustomTouchAnim(view, new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build(), null, null, true);
        this.mView = (TextView) view.findViewById(R.id.label);
        this.mImageView = (ImageView) view.findViewById(R.id.img_nav);
    }

    public void bind(MenuFragmentData menuFragmentData) {
        this.mView.setText(menuFragmentData.nameId);
        this.mImageView.setImageResource(menuFragmentData.iconId);
    }
}
