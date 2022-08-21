package com.miui.gallery.editor.photo.core.imports.text.typeface;

import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.miui.gallery.R;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.widget.recyclerview.SingleChoiceRecyclerView;

/* loaded from: classes2.dex */
public class TypeFaceHolder extends SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter.SingleChoiceViewHolder {
    public static RequestOptions sRequestOptions = GlideOptions.formatOf(DecodeFormat.PREFER_ARGB_8888).mo978skipMemoryCache(false).mo950diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).autoClone();
    public com.miui.gallery.editor.ui.view.DownloadView mDownloadView;
    public ImageLoadingProcess mImageLoadingProcess;
    public ImageView mItemBackgroundIv;
    public RelativeLayout mRelativeLayout;

    @Override // com.miui.gallery.widget.recyclerview.SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter.SingleChoiceViewHolder
    public void setSelect(boolean z) {
    }

    public TypeFaceHolder(View view) {
        super(view);
        FolmeUtil.setDefaultTouchAnim(view, null, true);
        this.mRelativeLayout = (RelativeLayout) view.findViewById(R.id.text_edit_dialog_options_font_panel_item);
        this.mItemBackgroundIv = (ImageView) view.findViewById(R.id.iv_item_background);
        this.mDownloadView = (com.miui.gallery.editor.ui.view.DownloadView) view.findViewById(R.id.item_download);
    }

    public void bind(TextStyle textStyle, boolean z) {
        if (textStyle.isLocal()) {
            textStyle.getDefaultNameResId();
            this.mRelativeLayout.setVisibility(0);
            this.mItemBackgroundIv.setBackgroundResource(R.drawable.text_edit_font_fangzheng_xmlt);
            this.mItemBackgroundIv.setSelected(z);
        } else {
            this.mRelativeLayout.setVisibility(0);
            this.mDownloadView.setBackground(this.itemView.getResources().getDrawable(R.drawable.filter_menu_item_download_bg));
            if (this.mImageLoadingProcess == null) {
                this.mImageLoadingProcess = new ImageLoadingProcess(textStyle, this.mDownloadView);
            }
            Glide.with(this.itemView).mo985asBitmap().mo963load(textStyle.getIconUrl()).mo946apply((BaseRequestOptions<?>) sRequestOptions).mo970override(Integer.MIN_VALUE).mo959listener(this.mImageLoadingProcess).into(this.mItemBackgroundIv);
        }
        this.mItemBackgroundIv.setColorFilter(z ? -16777216 : -1, PorterDuff.Mode.SRC_IN);
        this.mRelativeLayout.setSelected(z);
    }

    /* loaded from: classes2.dex */
    public static class ImageLoadingProcess implements RequestListener<Bitmap> {
        public com.miui.gallery.editor.ui.view.DownloadView mDownloadView;
        public TextStyle mTextStyle;

        @Override // com.bumptech.glide.request.RequestListener
        public boolean onLoadFailed(GlideException glideException, Object obj, Target<Bitmap> target, boolean z) {
            return false;
        }

        public ImageLoadingProcess(TextStyle textStyle, com.miui.gallery.editor.ui.view.DownloadView downloadView) {
            this.mTextStyle = textStyle;
            this.mDownloadView = downloadView;
        }

        @Override // com.bumptech.glide.request.RequestListener
        public boolean onResourceReady(Bitmap bitmap, Object obj, Target<Bitmap> target, DataSource dataSource, boolean z) {
            if (!this.mTextStyle.isLocal() && this.mTextStyle.isExtra()) {
                this.mDownloadView.setStateImage(this.mTextStyle.getState());
                return false;
            }
            return false;
        }
    }
}
