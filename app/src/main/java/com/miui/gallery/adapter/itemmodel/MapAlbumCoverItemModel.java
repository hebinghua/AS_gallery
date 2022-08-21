package com.miui.gallery.adapter.itemmodel;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.request.RequestOptions;
import com.miui.epoxy.EpoxyAdapter;
import com.miui.epoxy.EpoxyModel;
import com.miui.epoxy.EpoxyViewHolder;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.base.BaseGalleryItemModel;
import com.miui.gallery.adapter.itemmodel.base.BaseGalleryViewHolder;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.provider.cache.IMedia;
import com.miui.gallery.ui.album.main.viewbean.MapAlbumViewBean;
import com.miui.gallery.util.imageloader.imageloadiotion.AlbumImageLoadOptions;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import miuix.animation.Folme;
import miuix.animation.IHoverStyle;
import miuix.animation.base.AnimConfig;

/* loaded from: classes.dex */
public class MapAlbumCoverItemModel extends BaseGalleryItemModel<MapAlbumViewBean, VH> {
    public static List<Integer> sAlbumCoverImageViewIds;
    public final RequestOptions mRequestOptions;

    @Override // com.miui.epoxy.EpoxyModel
    public int getLayoutRes() {
        return R.layout.ai_map_album_cover;
    }

    @Override // com.miui.epoxy.EpoxyModel
    public /* bridge */ /* synthetic */ void bindPartialData(EpoxyViewHolder epoxyViewHolder, List list) {
        bindPartialData((VH) epoxyViewHolder, (List<Object>) list);
    }

    static {
        ArrayList arrayList = new ArrayList(4);
        sAlbumCoverImageViewIds = arrayList;
        arrayList.add(Integer.valueOf((int) R.id.album_cover_first));
        sAlbumCoverImageViewIds.add(Integer.valueOf((int) R.id.album_cover_second));
        sAlbumCoverImageViewIds.add(Integer.valueOf((int) R.id.album_cover_third));
        sAlbumCoverImageViewIds.add(Integer.valueOf((int) R.id.album_cover_fourth));
    }

    public MapAlbumCoverItemModel(MapAlbumViewBean mapAlbumViewBean) {
        super(mapAlbumViewBean.getId(), mapAlbumViewBean);
        this.mRequestOptions = GlideOptions.microThumbOf().mo973placeholder((Drawable) null).mo954error(R.drawable.map_album_load_fail).mo956fallback(R.drawable.map_album_load_fail).autoClone();
    }

    @Override // com.miui.epoxy.EpoxyModel
    public Object getDiffChangeResult(EpoxyModel epoxyModel) {
        if (epoxyModel instanceof MapAlbumCoverItemModel) {
            MapAlbumViewBean itemData = ((MapAlbumCoverItemModel) epoxyModel).getItemData();
            MapAlbumViewBean mapAlbumViewBean = new MapAlbumViewBean();
            if (!Objects.equals(itemData.getCovers(), getItemData().getCovers())) {
                mapAlbumViewBean.setCovers(itemData.getCovers());
            }
            return mapAlbumViewBean;
        }
        return super.getDiffChangeResult(epoxyModel);
    }

    public void bindPartialData(VH vh, List<Object> list) {
        Object obj = list.get(0);
        if (obj instanceof MapAlbumViewBean) {
            MapAlbumViewBean mapAlbumViewBean = (MapAlbumViewBean) obj;
            if (!Objects.isNull(mapAlbumViewBean.getAlbumName())) {
                setAlbumName(vh, mapAlbumViewBean.getAlbumName());
            }
            if (Objects.isNull(mapAlbumViewBean.getCovers())) {
                return;
            }
            internalBindCovers(vh, mapAlbumViewBean.getCovers());
        }
    }

    @Override // com.miui.epoxy.EpoxyModel
    public void bindData(VH vh) {
        super.bindData((MapAlbumCoverItemModel) vh);
        setAlbumName(vh, getItemData().getAlbumName());
        internalBindCovers(vh, getItemData().getCovers());
        Folme.useAt(vh.itemView).hover().setEffect(IHoverStyle.HoverEffect.FLOATED).handleHoverOf(vh.itemView, new AnimConfig[0]);
    }

    public void setAlbumName(VH vh, String str) {
        setText(vh.mTvAlbumName, str);
    }

    public final void internalBindCovers(VH vh, List<IMedia> list) {
        if ((list != null ? list.size() : 0) > 0) {
            bindCovers(vh);
        } else {
            onNoCovers(vh);
        }
    }

    public static /* synthetic */ VH lambda$getViewHolderCreator$0(View view, View view2) {
        return new VH(view);
    }

    @Override // com.miui.epoxy.EpoxyModel
    /* renamed from: getViewHolderCreator */
    public EpoxyAdapter.IViewHolderCreator<VH> mo541getViewHolderCreator() {
        return MapAlbumCoverItemModel$$ExternalSyntheticLambda0.INSTANCE;
    }

    public ImageView getImageView(int i, VH vh) {
        if (i >= sAlbumCoverImageViewIds.size()) {
            return null;
        }
        int intValue = sAlbumCoverImageViewIds.get(i).intValue();
        vh.initImageViewById(intValue);
        ImageView imageViewById = vh.getImageViewById(intValue);
        imageViewById.setVisibility(0);
        return imageViewById;
    }

    public void bindCovers(VH vh) {
        int i = 0;
        int size = getCovers() != null ? getCovers().size() : 0;
        if (size >= 4) {
            vh.goneBigImageViewIfNeed();
            while (i < sAlbumCoverImageViewIds.size()) {
                ImageView imageView = getImageView(i, vh);
                IMedia iMedia = i >= size ? null : getCovers().get(i);
                if (imageView == null) {
                    return;
                }
                bindSingleCover(iMedia, imageView, getCoverDisplayImageOptionsByPosition(i));
                i++;
            }
            return;
        }
        for (int i2 = 0; i2 < 4; i2++) {
            ImageView imageViewById = vh.getImageViewById(sAlbumCoverImageViewIds.get(i2).intValue());
            if (imageViewById != null) {
                gone(imageViewById);
            }
        }
        List<IMedia> covers = getCovers();
        ImageView bigImageView = vh.getBigImageView();
        if (size > 0) {
            bigImageView.setVisibility(0);
            bindSingleCover(covers.get(0), bigImageView);
            return;
        }
        bindSingleCover(null, bigImageView);
    }

    public void bindSingleCover(IMedia iMedia, ImageView imageView) {
        bindSingleCover(iMedia, imageView, this.mRequestOptions);
    }

    public void bindSingleCover(IMedia iMedia, ImageView imageView, RequestOptions requestOptions) {
        if (iMedia == null) {
            bindImage(imageView, requestOptions);
        } else {
            bindImage(imageView, iMedia.getSmallSizeThumb(), null, requestOptions);
        }
    }

    public RequestOptions getCoverDisplayImageOptionsByPosition(int i) {
        return this.mRequestOptions;
    }

    public void onNoCovers(VH vh) {
        for (int i = 0; i < 4; i++) {
            gone(getImageView(i, vh));
        }
        ImageView bigImageView = vh.getBigImageView();
        bigImageView.setForeground(null);
        bigImageView.setVisibility(0);
        bindImage(bigImageView, AlbumImageLoadOptions.getInstance().getMapAlbumNoCoverOptions());
    }

    public List<IMedia> getCovers() {
        return getItemData().getCovers();
    }

    /* loaded from: classes.dex */
    public static class VH extends BaseGalleryViewHolder {
        public ImageView mAlbumCoverBig;
        public ImageView mAlbumCoverFirst;
        public ImageView mAlbumCoverFourth;
        public ImageView mAlbumCoverSecond;
        public ImageView mAlbumCoverThird;
        public ViewGroup mCoverParentView;
        public final TextView mTvAlbumName;

        public VH(View view) {
            super(view);
            this.mCoverParentView = (ViewGroup) findViewById(R.id.album_covers);
            this.mTvAlbumName = (TextView) findViewById(R.id.title);
        }

        public void initImageViewById(int i) {
            switch (i) {
                case R.id.album_cover_big /* 2131361939 */:
                    if (this.mAlbumCoverBig != null) {
                        return;
                    }
                    this.mAlbumCoverBig = (ImageView) findViewById(i);
                    return;
                case R.id.album_cover_first /* 2131361940 */:
                    if (this.mAlbumCoverFirst != null) {
                        return;
                    }
                    this.mAlbumCoverFirst = (ImageView) findViewById(i);
                    return;
                case R.id.album_cover_fourth /* 2131361941 */:
                    if (this.mAlbumCoverFourth != null) {
                        return;
                    }
                    this.mAlbumCoverFourth = (ImageView) findViewById(i);
                    return;
                case R.id.album_cover_second /* 2131361942 */:
                    if (this.mAlbumCoverSecond != null) {
                        return;
                    }
                    this.mAlbumCoverSecond = (ImageView) findViewById(i);
                    return;
                case R.id.album_cover_third /* 2131361943 */:
                    if (this.mAlbumCoverThird != null) {
                        return;
                    }
                    this.mAlbumCoverThird = (ImageView) findViewById(i);
                    return;
                default:
                    return;
            }
        }

        public ImageView getImageViewById(int i) {
            switch (i) {
                case R.id.album_cover_big /* 2131361939 */:
                    return this.mAlbumCoverBig;
                case R.id.album_cover_first /* 2131361940 */:
                    return this.mAlbumCoverFirst;
                case R.id.album_cover_fourth /* 2131361941 */:
                    return this.mAlbumCoverFourth;
                case R.id.album_cover_second /* 2131361942 */:
                    return this.mAlbumCoverSecond;
                case R.id.album_cover_third /* 2131361943 */:
                    return this.mAlbumCoverThird;
                default:
                    return null;
            }
        }

        public ImageView getBigImageView() {
            initImageViewById(R.id.album_cover_big);
            return getImageViewById(R.id.album_cover_big);
        }

        public void goneBigImageViewIfNeed() {
            ImageView imageView = this.mAlbumCoverBig;
            if (imageView != null) {
                imageView.setVisibility(8);
            }
        }
    }
}
