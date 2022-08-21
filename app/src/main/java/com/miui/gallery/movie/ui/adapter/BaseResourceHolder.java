package com.miui.gallery.movie.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.editor.ui.view.DownloadView;
import com.miui.gallery.movie.R$id;
import com.miui.gallery.movie.R$string;
import com.miui.gallery.movie.entity.MovieResource;
import com.miui.gallery.movie.ui.adapter.BaseAdapter;
import com.miui.gallery.util.anim.FolmeUtil;

/* loaded from: classes2.dex */
public class BaseResourceHolder<T extends MovieResource> extends BaseAdapter.BaseHolder<T> {
    public DownloadView mDownloadView;
    public ImageView mImageView;
    public TextView mTitle;

    /* renamed from: $r8$lambda$xK3CEznzvr2uoA5Dtvp-Qgrma1I */
    public static /* synthetic */ void m1152$r8$lambda$xK3CEznzvr2uoA5DtvpQgrma1I(MovieResource movieResource) {
        movieResource.downloadState = 17;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.miui.gallery.movie.ui.adapter.BaseAdapter.BaseHolder
    public /* bridge */ /* synthetic */ void bindView(Object obj, int i) {
        bindView((BaseResourceHolder<T>) ((MovieResource) obj), i);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.miui.gallery.movie.ui.adapter.BaseAdapter.BaseHolder
    public /* bridge */ /* synthetic */ void bindView(Object obj, int i, Object obj2) {
        bindView((BaseResourceHolder<T>) ((MovieResource) obj), i, obj2);
    }

    public BaseResourceHolder(View view) {
        super(view);
        FolmeUtil.setDefaultTouchAnim(view, null, true);
        this.mTitle = (TextView) view.findViewById(R$id.item_title);
        this.mImageView = (ImageView) view.findViewById(R$id.item_iv);
        this.mDownloadView = (DownloadView) view.findViewById(R$id.item_download);
    }

    public void bindView(T t, int i) {
        if (t.isPackageAssets) {
            Glide.with(this.itemView).clear(this.mImageView);
            this.mTitle.setText(t.stringId);
            this.mImageView.setImageResource(t.imageId);
            int i2 = t.stringId;
            if (i2 == R$string.movie_audio_none || i2 == R$string.movie_audio_custom || i2 == R$string.movie_template_none) {
                this.mTitle.setVisibility(8);
            } else {
                this.mTitle.setVisibility(0);
            }
        } else {
            this.mTitle.setVisibility(0);
            Glide.with(this.itemView).mo985asBitmap().mo963load(t.icon).mo946apply((BaseRequestOptions<?>) RequestOptions.formatOf(DecodeFormat.PREFER_RGB_565).mo950diskCacheStrategy(DiskCacheStrategy.NONE)).into(this.mImageView);
            int nameId = t.getNameId();
            if (nameId != 0) {
                this.mTitle.setText(nameId);
            } else {
                this.mTitle.setText(t.label);
            }
        }
        int downloadState = t.getDownloadState();
        this.mDownloadView.setStateImage(downloadState);
        if (downloadState == 0) {
            t.downloadState = 17;
        }
    }

    public void bindView(final T t, int i, Object obj) {
        super.bindView((BaseResourceHolder<T>) t, i, obj);
        int downloadState = t.getDownloadState();
        this.mDownloadView.setStateImage(downloadState);
        if (downloadState == 0) {
            this.mDownloadView.postDelayed(new Runnable() { // from class: com.miui.gallery.movie.ui.adapter.BaseResourceHolder$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    BaseResourceHolder.m1152$r8$lambda$xK3CEznzvr2uoA5DtvpQgrma1I(MovieResource.this);
                }
            }, 1000L);
        }
    }
}
