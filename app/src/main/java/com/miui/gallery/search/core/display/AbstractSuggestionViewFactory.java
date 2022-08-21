package com.miui.gallery.search.core.display;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.R;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.display.icon.IconImageLoader;
import com.miui.gallery.search.core.suggestion.Suggestion;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.glide.BindImageHelper;
import java.util.List;
import miuix.animation.Folme;
import miuix.animation.IHoverStyle;
import miuix.animation.base.AnimConfig;

/* loaded from: classes2.dex */
public abstract class AbstractSuggestionViewFactory implements SuggestionViewFactory {
    public final Context mContext;
    public RequestOptions mDefaultRequestOptions;
    public final LayoutInflater mLayoutInflater;

    public abstract int getLayoutIdForViewType(String str);

    public boolean needSetTouchAnim(String str) {
        return false;
    }

    public AbstractSuggestionViewFactory(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        initDisplayImageOptions(context);
    }

    public void initDisplayImageOptions(Context context) {
        this.mDefaultRequestOptions = GlideOptions.microThumbOf().mo972placeholder(R.drawable.image_default_cover).mo954error(R.drawable.image_default_cover).mo956fallback(R.drawable.image_default_cover).mo964lock();
    }

    public RequestOptions getDefaultRequestOptions() {
        return this.mDefaultRequestOptions.clone();
    }

    public LayoutInflater getBaseLayoutInflater() {
        return this.mLayoutInflater;
    }

    public Context getContext() {
        return this.mContext;
    }

    @Override // com.miui.gallery.search.core.display.SuggestionViewFactory
    public BaseSuggestionViewHolder createViewHolder(ViewGroup viewGroup, String str) {
        View inflate;
        if (viewGroup != null) {
            inflate = LayoutInflater.from(viewGroup.getContext()).inflate(getLayoutIdForViewType(str), viewGroup, false);
        } else {
            inflate = getBaseLayoutInflater().inflate(getLayoutIdForViewType(str), viewGroup, false);
        }
        if (needSetTouchAnim(str)) {
            FolmeUtil.setDefaultTouchAnim(inflate, null, true);
        }
        return createViewHolder(inflate);
    }

    public BaseSuggestionViewHolder createViewHolder(View view) {
        return new BaseSuggestionViewHolder(view);
    }

    @Override // com.miui.gallery.search.core.display.SuggestionViewFactory
    public void bindViewHolder(QueryInfo queryInfo, Suggestion suggestion, int i, BaseSuggestionViewHolder baseSuggestionViewHolder, OnActionClickListener onActionClickListener) {
        if (baseSuggestionViewHolder.getIconView() != null && suggestion.getSuggestionIcon() != null) {
            bindIcon(baseSuggestionViewHolder.getIconView(), suggestion.getSuggestionIcon(), getDisplayImageOptionsForViewType(baseSuggestionViewHolder.getViewType()), suggestion.getBackupIcons());
            Folme.useAt(baseSuggestionViewHolder.getIconView()).hover().setEffect(IHoverStyle.HoverEffect.FLOATED).handleHoverOf(baseSuggestionViewHolder.getIconView(), new AnimConfig[0]);
        }
        if (baseSuggestionViewHolder.getTitle() != null) {
            baseSuggestionViewHolder.getTitle().setText(suggestion.getSuggestionTitle());
        }
        if (baseSuggestionViewHolder.getSubTitle() != null) {
            baseSuggestionViewHolder.getSubTitle().setText(suggestion.getSuggestionSubTitle());
        }
    }

    public RequestOptions getDisplayImageOptionsForViewType(String str) {
        return this.mDefaultRequestOptions.clone();
    }

    public void bindIcon(ImageView imageView, String str, RequestOptions requestOptions, List<String> list) {
        IconImageLoader.getInstance().displayImage(this.mContext, str == null ? null : Uri.parse(str), DownloadType.MICRO, imageView, requestOptions, list);
    }

    public void bindLocalImage(ImageView imageView, String str, RequestOptions requestOptions) {
        if (TextUtils.isEmpty(str)) {
            BindImageHelper.bindDefaultIcon(imageView, requestOptions);
        } else {
            BindImageHelper.bindImage(str, (Uri) null, DownloadType.MICRO, imageView, requestOptions);
        }
    }
}
