package com.miui.gallery.editor.photo.core.imports.text.typeface;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.miui.gallery.widget.recyclerview.SingleChoiceRecyclerView;

/* loaded from: classes2.dex */
public class TypeFaceHolderOld extends SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter.SingleChoiceViewHolder {
    public static RequestOptions sRequestOptions = GlideOptions.formatOf(DecodeFormat.PREFER_ARGB_8888).mo978skipMemoryCache(false).mo950diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).autoClone();
    public DownloadView mDownloadView;
    public ImageLoadingProcess mImageLoadingProcess;
    public ImageView mIvCheck;
    public ImageView mNameImage;
    public TextView mNameText;
    public int mSelectedColor;

    @Override // com.miui.gallery.widget.recyclerview.SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter.SingleChoiceViewHolder
    public void setSelect(boolean z) {
    }

    public TypeFaceHolderOld(View view) {
        super(view);
        this.mNameText = (TextView) view.findViewById(R.id.tv_font_item_name);
        this.mNameImage = (ImageView) view.findViewById(R.id.iv_font_item_name);
        this.mDownloadView = (DownloadView) view.findViewById(R.id.download_view);
        this.mIvCheck = (ImageView) view.findViewById(R.id.font_item_check);
        this.mSelectedColor = view.getResources().getColor(R.color.text_edit_dialog_tab_menu_text_color_pressed_old);
    }

    public void bind(TextStyle textStyle, boolean z) {
        int i = 0;
        if (textStyle.isLocal()) {
            this.mNameText.setText(textStyle.getDefaultNameResId());
            this.mNameImage.setVisibility(8);
            this.mNameText.setVisibility(0);
            this.mDownloadView.setVisibility(8);
            this.mNameText.setSelected(z);
            this.mIvCheck.setSelected(z);
            return;
        }
        this.mNameImage.setVisibility(0);
        this.mNameText.setVisibility(8);
        if (this.mImageLoadingProcess == null) {
            this.mImageLoadingProcess = new ImageLoadingProcess(textStyle, this.mDownloadView);
        }
        Glide.with(this.itemView).mo985asBitmap().mo963load(textStyle.getIconUrl()).mo946apply((BaseRequestOptions<?>) sRequestOptions).mo970override(Integer.MIN_VALUE).mo959listener(this.mImageLoadingProcess).into(this.mNameImage);
        ImageView imageView = this.mNameImage;
        if (z) {
            i = this.mSelectedColor;
        }
        imageView.setColorFilter(i);
        this.mIvCheck.setSelected(z);
    }

    /* loaded from: classes2.dex */
    public static class ImageLoadingProcess implements RequestListener<Bitmap> {
        public DownloadView mDownloadView;
        public TextStyle mTextStyle;

        public ImageLoadingProcess(TextStyle textStyle, DownloadView downloadView) {
            this.mTextStyle = textStyle;
            this.mDownloadView = downloadView;
        }

        @Override // com.bumptech.glide.request.RequestListener
        public boolean onLoadFailed(GlideException glideException, Object obj, Target<Bitmap> target, boolean z) {
            this.mDownloadView.setVisibility(8);
            return false;
        }

        @Override // com.bumptech.glide.request.RequestListener
        public boolean onResourceReady(Bitmap bitmap, Object obj, Target<Bitmap> target, DataSource dataSource, boolean z) {
            if (this.mTextStyle.isLocal()) {
                this.mDownloadView.setVisibility(8);
                return false;
            } else if (!this.mTextStyle.isExtra()) {
                return false;
            } else {
                this.mDownloadView.setStateImage(this.mTextStyle.getState());
                this.mDownloadView.setText(this.mTextStyle.getFontSize());
                return false;
            }
        }
    }
}
