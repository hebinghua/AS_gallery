package com.miui.gallery.adapter.itemmodel.common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.bumptech.glide.request.RequestOptions;
import com.miui.epoxy.EpoxyModel;
import com.miui.epoxy.EpoxyViewHolder;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.base.BaseGalleryItemModel;
import com.miui.gallery.adapter.itemmodel.base.BaseGalleryViewHolder;
import com.miui.gallery.adapter.itemmodel.common.AlbumDetailInfoProvider;
import com.miui.gallery.adapter.itemmodel.common.CommonAlbumItemModel.ViewHolder;
import com.miui.gallery.adapter.itemmodel.common.config.CommonGridItemViewDisplaySetting;
import com.miui.gallery.assistant.jni.filter.BaiduSceneResult;
import com.miui.gallery.base_optimization.util.GenericUtils;
import com.miui.gallery.ui.album.common.CommonAlbumItemViewBean;
import com.miui.gallery.util.imageloader.imageloadiotion.AlbumImageLoadOptions;
import java.util.List;

/* loaded from: classes.dex */
public abstract class CommonAlbumItemModel<DATA extends AlbumDetailInfoProvider, VH extends ViewHolder> extends BaseGalleryItemModel<DATA, VH> {
    public static final RequestOptions mBindPartialCoverOptions = AlbumImageLoadOptions.getInstance().getDefaultAlbumImageOptions().mo972placeholder(0).mo973placeholder((Drawable) null);
    public CommonGridItemViewDisplaySetting mDisplaySetting;

    public void configDisplaySetting(CommonGridItemViewDisplaySetting commonGridItemViewDisplaySetting, VH vh, DATA data) {
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.miui.epoxy.EpoxyModel
    public /* bridge */ /* synthetic */ void bindData(EpoxyViewHolder epoxyViewHolder) {
        bindData((CommonAlbumItemModel<DATA, VH>) ((ViewHolder) epoxyViewHolder));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.miui.epoxy.EpoxyModel
    public /* bridge */ /* synthetic */ void bindPartialData(EpoxyViewHolder epoxyViewHolder, List list) {
        bindPartialData((CommonAlbumItemModel<DATA, VH>) ((ViewHolder) epoxyViewHolder), (List<Object>) list);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.miui.epoxy.EpoxyModel
    public /* bridge */ /* synthetic */ void unbind(EpoxyViewHolder epoxyViewHolder) {
        unbind((CommonAlbumItemModel<DATA, VH>) ((ViewHolder) epoxyViewHolder));
    }

    public CommonAlbumItemModel(long j, DATA data) {
        super(j, data);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void bindData(VH vh) {
        super.bindData((CommonAlbumItemModel<DATA, VH>) vh);
        AlbumDetailInfoProvider albumDetailInfoProvider = (AlbumDetailInfoProvider) getItemData();
        if (vh.itemView instanceof ConstraintLayout) {
            configDisplaySetting(this.mDisplaySetting, vh, albumDetailInfoProvider);
        }
        bindTitleText(vh.mAlbumTitle, albumDetailInfoProvider);
        bindSubTitleText(vh.mAlbumSubTitle, albumDetailInfoProvider);
        bindContentDescription(vh, albumDetailInfoProvider);
        bindCover(vh.mAlbumCover, albumDetailInfoProvider);
    }

    public void bindContentDescription(VH vh, DATA data) {
        if (data != null) {
            vh.itemView.setContentDescription(data.getContentDescription());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void bindPartialData(VH vh, List<Object> list) {
        super.bindPartialData((CommonAlbumItemModel<DATA, VH>) vh, list);
        boolean z = false;
        AlbumDetailInfoProvider albumDetailInfoProvider = (AlbumDetailInfoProvider) list.get(0);
        if (!isEmpty(albumDetailInfoProvider.getCoverUri()) || !isEmpty((CharSequence) albumDetailInfoProvider.getCoverPath())) {
            ImageView imageView = vh.mAlbumCover;
            bindCover(imageView, albumDetailInfoProvider, mBindPartialCoverOptions.mo973placeholder(imageView.getDrawable()));
        }
        boolean z2 = true;
        if (!isEmpty((CharSequence) albumDetailInfoProvider.getTitle())) {
            bindTitleText(vh.mAlbumTitle, albumDetailInfoProvider);
            z = true;
        }
        if (!isEmpty(albumDetailInfoProvider.getSubTitle())) {
            bindSubTitleText(vh.mAlbumSubTitle, albumDetailInfoProvider);
        } else {
            z2 = z;
        }
        if (z2) {
            bindContentDescription(vh, albumDetailInfoProvider);
        }
    }

    public void unbind(VH vh) {
        super.unbind((CommonAlbumItemModel<DATA, VH>) vh);
        unbindCover(vh.mAlbumCover);
    }

    public void bindCover(ImageView imageView, DATA data) {
        if (isEmpty(imageView) || isEmpty(data)) {
            return;
        }
        bindCover(imageView, data, getImageLoaderOptions(imageView.getContext(), data));
    }

    public void bindCover(ImageView imageView, DATA data, RequestOptions requestOptions) {
        if (isEmpty(imageView) || isEmpty(data)) {
            return;
        }
        Drawable drawable = null;
        imageView.setForeground(null);
        boolean z = (!isEmpty((CharSequence) data.getCoverPath()) && !data.getCoverPath().equals("empty_cover")) || !isEmpty(data.getCoverUri());
        CommonGridItemViewDisplaySetting commonGridItemViewDisplaySetting = this.mDisplaySetting;
        if (commonGridItemViewDisplaySetting == null || commonGridItemViewDisplaySetting.getForegroundResource() == 0) {
            imageView.setForeground(imageView.getResources().getDrawable(z ? R.drawable.album_common_grid_item_rounded_selector : R.drawable.album_common_grid_item_empty_cover_rounded_selector));
        } else {
            if (this.mDisplaySetting.getEmptyCoverForegroundRes() != 0 || z) {
                drawable = imageView.getResources().getDrawable(this.mDisplaySetting.getForegroundResource());
            }
            imageView.setForeground(drawable);
        }
        if (data.isMoreStyle()) {
            imageView.setForeground(imageView.getResources().getDrawable(R.drawable.ai_album_more_foreground));
        }
        bindImage(imageView, data.getCoverPath(), data.getCoverUri(), requestOptions);
    }

    public void unbindCover(ImageView imageView) {
        if (isEmpty(imageView)) {
            return;
        }
        unbindImage(imageView);
    }

    public void bindTitleText(TextView textView, DATA data) {
        CommonGridItemViewDisplaySetting commonGridItemViewDisplaySetting;
        if (isEmpty(textView) || isEmpty(data)) {
            return;
        }
        if (data.isMoreStyle()) {
            setText(textView, textView.getResources().getString(R.string.more));
        } else if (TextUtils.isEmpty(data.getTitle()) && (commonGridItemViewDisplaySetting = this.mDisplaySetting) != null && commonGridItemViewDisplaySetting.isHaveDefaultAlbumName()) {
            setText(textView, this.mDisplaySetting.getDefaultAlbumNameResource() != 0 ? textView.getResources().getString(this.mDisplaySetting.getDefaultAlbumNameResource()) : this.mDisplaySetting.getDefaultAlbumName());
        } else {
            setText(textView, data.getTitle());
        }
    }

    public void bindSubTitleText(TextView textView, DATA data) {
        if (isEmpty(textView) || isEmpty(data)) {
            return;
        }
        setText(textView, data.getSubTitle());
    }

    public RequestOptions getImageLoaderOptions(Context context, DATA data) {
        return AlbumImageLoadOptions.getInstance().getDefaultAlbumImageOptions(data.getCoverSize());
    }

    public void setDisplaySetting(CommonGridItemViewDisplaySetting commonGridItemViewDisplaySetting) {
        this.mDisplaySetting = commonGridItemViewDisplaySetting;
    }

    @Override // com.miui.epoxy.EpoxyModel
    public Object getDiffChangeResult(EpoxyModel epoxyModel) {
        if (epoxyModel instanceof CommonAlbumItemModel) {
            AlbumDetailInfoProvider albumDetailInfoProvider = (AlbumDetailInfoProvider) getItemData();
            AlbumDetailInfoProvider albumDetailInfoProvider2 = (AlbumDetailInfoProvider) ((CommonAlbumItemModel) epoxyModel).getItemData();
            DATA mo1574instanceDiffResultBean = mo1574instanceDiffResultBean();
            boolean z = false;
            if (mo1574instanceDiffResultBean == null) {
                mo1574instanceDiffResultBean = (DATA) GenericUtils.getSelfClassT(this, 0);
            }
            boolean z2 = true;
            if (!isEquals(albumDetailInfoProvider.getTitle(), albumDetailInfoProvider2.getTitle())) {
                mo1574instanceDiffResultBean.set(47, albumDetailInfoProvider2.getTitle());
                z = true;
            }
            if (!isEquals(albumDetailInfoProvider.getSubTitle(), albumDetailInfoProvider2.getSubTitle())) {
                mo1574instanceDiffResultBean.set(63, albumDetailInfoProvider2.getSubTitle());
                z = true;
            }
            if (albumDetailInfoProvider.getCoverUri() != null && albumDetailInfoProvider2.getCoverUri() != null && !isEquals(albumDetailInfoProvider.getCoverUri(), albumDetailInfoProvider2.getCoverUri())) {
                mo1574instanceDiffResultBean.set(111, albumDetailInfoProvider2.getCoverUri());
                albumDetailInfoProvider.set(95, null);
                z = true;
            }
            if (!isEquals(albumDetailInfoProvider.getCoverPath(), albumDetailInfoProvider2.getCoverPath())) {
                mo1574instanceDiffResultBean.set(95, albumDetailInfoProvider2.getCoverPath() == null ? "empty_cover" : albumDetailInfoProvider2.getCoverPath());
                mo1574instanceDiffResultBean.set(BaiduSceneResult.BANK_CARD, Long.valueOf(albumDetailInfoProvider2.getCoverSize()));
                z = true;
            }
            if (albumDetailInfoProvider.getSource().equals(albumDetailInfoProvider2.getSource())) {
                z2 = z;
            }
            if (z2) {
                mo1574instanceDiffResultBean.set(79, null);
                mo1574instanceDiffResultBean.set(BaiduSceneResult.BLACK_WHITE, albumDetailInfoProvider2.getSource());
                return mo1574instanceDiffResultBean;
            }
        }
        return super.getDiffChangeResult(epoxyModel);
    }

    /* renamed from: instanceDiffResultBean */
    public DATA mo1574instanceDiffResultBean() {
        return new CommonAlbumItemViewBean();
    }

    /* loaded from: classes.dex */
    public static class ViewHolder extends BaseGalleryViewHolder {
        public ImageView mAlbumCover;
        public TextView mAlbumSubTitle;
        public TextView mAlbumTitle;
        public LinearLayout mTitleContainer;

        public static int getCoverViewId() {
            return R.id.album_common_cover;
        }

        public ViewHolder(View view) {
            super(view);
            this.mAlbumTitle = (TextView) view.findViewById(R.id.album_common_title);
            this.mAlbumSubTitle = (TextView) view.findViewById(R.id.album_common_sub_title);
            this.mAlbumCover = (ImageView) view.findViewById(R.id.album_common_cover);
            this.mTitleContainer = (LinearLayout) view.findViewById(R.id.album_common_title_container);
            this.mAlbumTitle.getPaint().setFakeBoldText(true);
        }
    }
}
