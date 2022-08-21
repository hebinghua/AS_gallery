package com.miui.gallery.editor.photo.app.sticker;

import android.util.Size;
import android.view.View;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.common.model.StickerData;
import com.miui.gallery.util.anim.FolmeUtil;

/* loaded from: classes2.dex */
public class StickerHolder extends RecyclerView.ViewHolder {
    public RequestOptions mOptions;
    public Size mSize;
    public ImageView mView;

    public StickerHolder(View view, RequestOptions requestOptions, Size size) {
        super(view);
        FolmeUtil.setDefaultTouchAnim(view, null, true);
        this.mView = (ImageView) view.findViewById(R.id.img);
        this.mOptions = requestOptions;
        this.mSize = size;
    }

    public void bind(StickerData stickerData, int i) {
        RequestManager with = Glide.with(this.itemView);
        with.mo990load("file://" + stickerData.icon).mo946apply((BaseRequestOptions<?>) this.mOptions).mo971override(this.mSize.getWidth(), this.mSize.getHeight()).into(this.mView);
        ImageView imageView = this.mView;
        imageView.setContentDescription(imageView.getResources().getString(R.string.photo_editor_talkback_effect, Integer.valueOf(i + 1)));
    }
}
