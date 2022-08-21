package com.miui.gallery.adapter.itemmodel;

import android.widget.ImageView;
import com.bumptech.glide.request.RequestOptions;
import com.miui.epoxy.EpoxyModel;
import com.miui.epoxy.EpoxyViewHolder;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.common.CommonAlbumItemModel;
import com.miui.gallery.adapter.itemmodel.common.config.CommonGridItemViewDisplaySetting;
import com.miui.gallery.adapter.itemmodel.common.grid.CommonSimpleGridAlbumItemModel;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.people.PeopleDisplayHelper;
import com.miui.gallery.ui.album.main.viewbean.ai.PeopleFaceAlbumViewBean;
import com.miui.gallery.util.imageloader.imageloadiotion.AlbumImageLoadOptions;
import java.util.List;
import miuix.animation.Folme;
import miuix.animation.IHoverStyle;
import miuix.animation.base.AnimConfig;

/* loaded from: classes.dex */
public class PeopleFaceItemModel extends CommonSimpleGridAlbumItemModel<PeopleFaceAlbumViewBean> {
    public static final CommonGridItemViewDisplaySetting sConfig = new CommonGridItemViewDisplaySetting.DisplayConfig().setTitleViewCenterHorizontal(true).setShowSubTitleView(false).setDefaultAlbumName(R.string.people_page_unname).titleSize(R.dimen.album_ai_item_title_text_size).create();

    @Override // com.miui.gallery.adapter.itemmodel.common.CommonAlbumItemModel, com.miui.epoxy.EpoxyModel
    public /* bridge */ /* synthetic */ void bindPartialData(EpoxyViewHolder epoxyViewHolder, List list) {
        bindPartialData((CommonAlbumItemModel.ViewHolder) epoxyViewHolder, (List<Object>) list);
    }

    public PeopleFaceItemModel(PeopleFaceAlbumViewBean peopleFaceAlbumViewBean) {
        super(peopleFaceAlbumViewBean);
        setDisplaySetting(sConfig);
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.CommonAlbumItemModel
    public void bindCover(ImageView imageView, PeopleFaceAlbumViewBean peopleFaceAlbumViewBean) {
        imageView.setForeground(null);
        if (peopleFaceAlbumViewBean.isMoreStyle()) {
            imageView.setForeground(imageView.getResources().getDrawable(R.drawable.ai_face_album_more_foreground));
        }
        if (peopleFaceAlbumViewBean.getFaceRectF() == null) {
            bindImage(imageView, AlbumImageLoadOptions.getInstance().getFaceLoaderFailedRequestOptions());
        } else {
            PeopleDisplayHelper.bindImage(imageView, peopleFaceAlbumViewBean.getCoverPath(), peopleFaceAlbumViewBean.getAlbumCoverUri(), GlideOptions.peopleFaceOf(peopleFaceAlbumViewBean.getFaceRectF(), 0L).circleCrop(), peopleFaceAlbumViewBean.getDownloadType());
        }
        Folme.useAt(imageView).hover().setEffect(IHoverStyle.HoverEffect.FLOATED).handleHoverOf(imageView, new AnimConfig[0]);
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.CommonAlbumItemModel
    public void bindCover(ImageView imageView, PeopleFaceAlbumViewBean peopleFaceAlbumViewBean, RequestOptions requestOptions) {
        bindCover(imageView, peopleFaceAlbumViewBean);
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.CommonAlbumItemModel
    public void bindPartialData(CommonAlbumItemModel.ViewHolder viewHolder, List<Object> list) {
        super.bindPartialData((PeopleFaceItemModel) viewHolder, list);
        PeopleFaceAlbumViewBean peopleFaceAlbumViewBean = (PeopleFaceAlbumViewBean) list.get(0);
        if (peopleFaceAlbumViewBean == null || peopleFaceAlbumViewBean.getFaceRectF() == null) {
            return;
        }
        bindCover(viewHolder.mAlbumCover, peopleFaceAlbumViewBean);
        bindTitleText(viewHolder.mAlbumTitle, peopleFaceAlbumViewBean);
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.CommonAlbumItemModel, com.miui.epoxy.EpoxyModel
    public Object getDiffChangeResult(EpoxyModel epoxyModel) {
        if (epoxyModel instanceof PeopleFaceItemModel) {
            PeopleFaceAlbumViewBean peopleFaceAlbumViewBean = (PeopleFaceAlbumViewBean) ((PeopleFaceItemModel) epoxyModel).getItemData();
            PeopleFaceAlbumViewBean peopleFaceAlbumViewBean2 = (PeopleFaceAlbumViewBean) getItemData();
            PeopleFaceAlbumViewBean peopleFaceAlbumViewBean3 = new PeopleFaceAlbumViewBean();
            if (!isEquals(peopleFaceAlbumViewBean.getFaceRectF(), peopleFaceAlbumViewBean2.getFaceRectF())) {
                peopleFaceAlbumViewBean3.setFaceRectF(peopleFaceAlbumViewBean.getFaceRectF());
                peopleFaceAlbumViewBean3.setCoverPath(peopleFaceAlbumViewBean.getCoverPath());
                peopleFaceAlbumViewBean3.setAlbumCoverUri(peopleFaceAlbumViewBean.getAlbumCoverUri());
            }
            if (!isEquals(peopleFaceAlbumViewBean.getTitle(), peopleFaceAlbumViewBean2.getTitle())) {
                peopleFaceAlbumViewBean3.setTitle(peopleFaceAlbumViewBean.getTitle());
            }
            peopleFaceAlbumViewBean3.setSource(peopleFaceAlbumViewBean.getSource());
            if (peopleFaceAlbumViewBean2.isMoreStyle() != peopleFaceAlbumViewBean.isMoreStyle()) {
                peopleFaceAlbumViewBean3.setMoreStyle(peopleFaceAlbumViewBean.isMoreStyle());
                peopleFaceAlbumViewBean3.setFaceRectF(peopleFaceAlbumViewBean.getFaceRectF());
                peopleFaceAlbumViewBean3.setCoverPath(peopleFaceAlbumViewBean.getCoverPath());
                peopleFaceAlbumViewBean3.setAlbumCoverUri(peopleFaceAlbumViewBean.getAlbumCoverUri());
                peopleFaceAlbumViewBean3.setTitle(peopleFaceAlbumViewBean.getTitle());
            }
            return peopleFaceAlbumViewBean3;
        }
        return super.getDiffChangeResult(epoxyModel);
    }

    @Override // com.miui.gallery.adapter.itemmodel.common.CommonAlbumItemModel
    /* renamed from: instanceDiffResultBean  reason: collision with other method in class */
    public PeopleFaceAlbumViewBean mo1574instanceDiffResultBean() {
        return new PeopleFaceAlbumViewBean();
    }
}
