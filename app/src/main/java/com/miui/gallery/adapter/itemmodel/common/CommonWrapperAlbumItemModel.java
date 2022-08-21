package com.miui.gallery.adapter.itemmodel.common;

import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import com.miui.epoxy.EpoxyAdapter;
import com.miui.epoxy.EpoxyViewHolder;
import com.miui.epoxy.EpoxyWrapperViewHolder;
import com.miui.epoxy.common.BaseWrapperItemModel;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.base.BaseGalleryWrapperItemModel;
import com.miui.gallery.adapter.itemmodel.common.CommonAlbumItemModel;
import com.miui.gallery.adapter.itemmodel.common.CommonAlbumItemModel.ViewHolder;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.ui.album.common.CommonAlbumItemViewBean;
import java.util.List;

/* loaded from: classes.dex */
public abstract class CommonWrapperAlbumItemModel<DATA extends CommonAlbumItemViewBean, CVH extends CommonAlbumItemModel.ViewHolder, MODEL extends CommonAlbumItemModel<DATA, CVH>> extends BaseGalleryWrapperItemModel<DATA, CVH, VH<CVH>, MODEL> {
    @Override // com.miui.epoxy.EpoxyWrapperModel, com.miui.epoxy.EpoxyModel
    public /* bridge */ /* synthetic */ void bindPartialData(EpoxyViewHolder epoxyViewHolder, List list) {
        bindPartialData((VH) ((VH) epoxyViewHolder), (List<Object>) list);
    }

    @Override // com.miui.epoxy.EpoxyWrapperModel
    public /* bridge */ /* synthetic */ void bindPartialData(EpoxyWrapperViewHolder epoxyWrapperViewHolder, List list) {
        bindPartialData((VH) ((VH) epoxyWrapperViewHolder), (List<Object>) list);
    }

    public CommonWrapperAlbumItemModel(MODEL model) {
        super(model);
    }

    public final void bindAlbumCloudStatusIndicator(VH vh, Object obj) {
        if (AccountCache.getAccount() != null && (obj instanceof Album)) {
            if (!((Album) obj).isAutoUploadedAlbum()) {
                inflateIndicatorViewIfNeed(vh);
                ImageView imageView = (ImageView) vh.mOtherGroupView.findViewById(R.id.album_cloud_status);
                imageView.setImageResource(R.drawable.type_indicator_no_open_cloud_backup_album);
                imageView.setVisibility(0);
                return;
            }
            ((CommonAlbumItemModel) getChildModel()).gone(vh.itemView.findViewById(R.id.album_cloud_status));
        }
    }

    public final void bindAlbumTypeIndicator(VH vh, Object obj) {
        if (!(obj instanceof Album)) {
            return;
        }
        Album album = (Album) obj;
        int i = -1;
        boolean z = true;
        if (album.isVideoAlbum()) {
            i = R.drawable.album_type_indicator_video;
        } else if (album.isFavoritesAlbum()) {
            i = R.drawable.album_type_indicator_favorites;
        } else if (!album.isShareAlbum() && !album.isShareAlbum() && !album.isShareToDevice()) {
            z = false;
        } else {
            i = R.drawable.type_indicator_share;
        }
        if (z) {
            inflateIndicatorViewIfNeed(vh);
            ImageView imageView = (ImageView) vh.mOtherGroupView.findViewById(R.id.album_type_indicator);
            imageView.setVisibility(0);
            imageView.setImageResource(i);
            return;
        }
        ((CommonAlbumItemModel) getChildModel()).gone(vh.itemView.findViewById(R.id.album_type_indicator));
    }

    public final void inflateIndicatorViewIfNeed(VH vh) {
        vh.mOtherGroupViewStub.setLayoutResource(R.layout.album_common_other_view_grid);
        if (vh.mOtherGroupViewStub == null || vh.mOtherGroupView != null) {
            return;
        }
        vh.mOtherGroupView = vh.mOtherGroupViewStub.inflate();
    }

    @Override // com.miui.epoxy.EpoxyWrapperModel
    public void bindData(VH vh) {
        super.bindData((CommonWrapperAlbumItemModel<DATA, CVH, MODEL>) vh);
        bindAlbumTypeIndicator(vh, ((CommonAlbumItemViewBean) getItemData()).getSource());
        bindAlbumCloudStatusIndicator(vh, ((CommonAlbumItemViewBean) getItemData()).getSource());
    }

    public void bindPartialData(VH<CVH> vh, List<Object> list) {
        super.bindPartialData((CommonWrapperAlbumItemModel<DATA, CVH, MODEL>) vh, list);
        CommonAlbumItemViewBean commonAlbumItemViewBean = (CommonAlbumItemViewBean) list.get(0);
        if (commonAlbumItemViewBean == null || commonAlbumItemViewBean.getSource() == 0) {
            return;
        }
        bindAlbumTypeIndicator(vh, commonAlbumItemViewBean.getSource());
        bindAlbumCloudStatusIndicator(vh, commonAlbumItemViewBean.getSource());
    }

    @Override // com.miui.epoxy.EpoxyWrapperModel, com.miui.epoxy.EpoxyModel
    /* renamed from: getViewHolderCreator  reason: collision with other method in class */
    public EpoxyAdapter.WrapperViewHolderCreator<VH<CVH>, CVH> mo541getViewHolderCreator() {
        return (EpoxyAdapter.WrapperViewHolderCreator<VH<CVH>, CVH>) new EpoxyAdapter.WrapperViewHolderCreator<VH<CVH>, CVH>(((CommonAlbumItemModel) getChildModel()).getLayoutRes(), ((CommonAlbumItemModel) getChildModel()).mo541getViewHolderCreator()) { // from class: com.miui.gallery.adapter.itemmodel.common.CommonWrapperAlbumItemModel.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.miui.epoxy.EpoxyAdapter.WrapperViewHolderCreator
            public /* bridge */ /* synthetic */ EpoxyWrapperViewHolder create(View view, EpoxyViewHolder epoxyViewHolder) {
                return create(view, (View) ((CommonAlbumItemModel.ViewHolder) epoxyViewHolder));
            }

            public VH<CVH> create(View view, CVH cvh) {
                return new VH<>(view, cvh);
            }
        };
    }

    /* loaded from: classes.dex */
    public static class VH<CVH extends CommonAlbumItemModel.ViewHolder> extends BaseWrapperItemModel.VH<CVH> {
        public View mOtherGroupView;
        public ViewStub mOtherGroupViewStub;

        public VH(View view, CVH cvh) {
            super(view, cvh);
            this.mOtherGroupViewStub = (ViewStub) view.findViewById(R.id.album_other_view_group);
        }
    }
}
