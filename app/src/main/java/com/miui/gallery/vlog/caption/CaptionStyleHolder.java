package com.miui.gallery.vlog.caption;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.miui.gallery.editor.ui.view.DownloadView;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.vlog.entity.CaptionStyleData;
import com.miui.gallery.vlog.tools.VlogUtils;

/* loaded from: classes2.dex */
public class CaptionStyleHolder extends RecyclerView.ViewHolder {
    public DownloadView mDownloadView;
    public TextView mTitle;
    public ImageView mView;

    public CaptionStyleHolder(View view) {
        super(view);
        FolmeUtil.setDefaultTouchAnim(view, null, true);
        this.mView = (ImageView) view.findViewById(R$id.preview);
        this.mTitle = (TextView) view.findViewById(R$id.title);
        this.mDownloadView = (DownloadView) view.findViewById(R$id.item_download);
    }

    public void bind(Context context, CaptionStyleData captionStyleData, boolean z) {
        if (captionStyleData != null && !TextUtils.isEmpty(captionStyleData.getLabel())) {
            this.mTitle.setVisibility(8);
        } else {
            this.mTitle.setVisibility(0);
        }
        if (!TextUtils.isEmpty(captionStyleData.getIconUrl())) {
            Glide.with(this.itemView).mo990load(captionStyleData.getIconUrl()).into(this.mView);
        }
    }

    public void setDownloadViewState(CaptionStyleData captionStyleData) {
        if (captionStyleData.type.equals("type_none")) {
            VlogUtils.hideViews(this.mDownloadView);
        } else {
            this.mDownloadView.setStateImage(captionStyleData.getDownloadState());
        }
    }
}
