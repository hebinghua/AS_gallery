package com.miui.gallery.adapter.itemmodel;

import android.content.ContentUris;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
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
import com.miui.gallery.model.dto.BaseAlbumCover;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.ui.album.main.base.config.AlbumPageConfig;
import com.miui.gallery.ui.album.main.viewbean.FourPalaceGridCoverViewBean;
import com.miui.gallery.util.DimensionUtils;
import com.miui.gallery.util.ResourceUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import miuix.animation.Folme;
import miuix.animation.IHoverStyle;
import miuix.animation.base.AnimConfig;

/* loaded from: classes.dex */
public class FourPalaceGridCoverItemModel<T extends BaseAlbumCover> extends BaseGalleryItemModel<FourPalaceGridCoverViewBean<T>, VH> {
    public static List<Integer> sAlbumCoverImageViewIds;
    public final Drawable mErrorBg;
    public final RequestOptions mRequestOptions;

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

    public FourPalaceGridCoverItemModel(FourPalaceGridCoverViewBean fourPalaceGridCoverViewBean) {
        super(fourPalaceGridCoverViewBean.getId(), fourPalaceGridCoverViewBean);
        Drawable coverForegroundDrawable = getCoverForegroundDrawable(true);
        this.mErrorBg = coverForegroundDrawable;
        this.mRequestOptions = GlideOptions.microThumbOf().mo973placeholder((Drawable) null).mo955error(coverForegroundDrawable).mo957fallback(coverForegroundDrawable).autoClone();
    }

    @Override // com.miui.epoxy.EpoxyModel
    public int getLayoutRes() {
        return AlbumPageConfig.getInstance().isGridPageMode() ? R.layout.album_coveraggregation_item_grid : R.layout.album_coveraggregation_item_linear;
    }

    @Override // com.miui.epoxy.EpoxyModel
    public void bindData(VH vh) {
        super.bindData((FourPalaceGridCoverItemModel<T>) vh);
        setAlbumName(vh, getItemData().getAlbumName());
        setAlbumDescription(vh, getItemData().getAlbumDescription());
        internalBindCovers(vh, getItemData().getCovers());
        Folme.useAt(vh.itemView).hover().setEffect(IHoverStyle.HoverEffect.FLOATED).handleHoverOf(vh.itemView, new AnimConfig[0]);
    }

    public final void internalBindCovers(VH vh, List<T> list) {
        if ((list != null ? list.size() : 0) > 0) {
            bindCovers(vh);
        } else {
            onNoCovers(vh);
        }
    }

    public void setAlbumName(VH vh, String str) {
        setText(vh.mTvAlbumName, str);
    }

    public void setAlbumDescription(VH vh, String str) {
        setText(vh.mTvAlbumPhotoNumber, str);
    }

    public void bindCovers(VH vh) {
        vh.goneBigImageViewIfNeed();
        List<T> covers = getCovers();
        if (covers == null || covers.isEmpty()) {
            return;
        }
        int size = getCovers().size();
        int i = 0;
        while (i < sAlbumCoverImageViewIds.size()) {
            ImageView imageView = getImageView(i, vh);
            T t = null;
            T t2 = i >= size ? null : getCovers().get(i);
            if (imageView == null) {
                return;
            }
            imageView.setForeground(getCoverForegroundDrawable(false));
            if (t2 != null) {
                t = t2;
            }
            bindSingleCover(t, imageView, getCoverDisplayImageOptionsByPosition(i));
            i++;
        }
    }

    public void bindPartialData(VH vh, List<Object> list) {
        Object obj = list.get(0);
        if (obj instanceof FourPalaceGridCoverViewBean) {
            FourPalaceGridCoverViewBean fourPalaceGridCoverViewBean = (FourPalaceGridCoverViewBean) obj;
            if (!Objects.isNull(fourPalaceGridCoverViewBean.getAlbumName())) {
                setAlbumName(vh, fourPalaceGridCoverViewBean.getAlbumName());
            }
            if (!Objects.isNull(fourPalaceGridCoverViewBean.getAlbumDescription())) {
                setAlbumDescription(vh, fourPalaceGridCoverViewBean.getAlbumDescription());
            }
            if (Objects.isNull(fourPalaceGridCoverViewBean.getCovers())) {
                return;
            }
            internalBindCovers(vh, fourPalaceGridCoverViewBean.getCovers());
        }
    }

    public void onNoCovers(VH vh) {
        vh.goneBigImageViewIfNeed();
        for (int i = 0; i < 4; i++) {
            ImageView imageView = getImageView(i, vh);
            imageView.setForeground(null);
            bindImage(imageView, getCoverDisplayImageOptionsByPosition(i));
        }
    }

    public void bindSingleCover(T t, ImageView imageView) {
        bindSingleCover(t, imageView, this.mRequestOptions);
    }

    public void bindSingleCover(T t, ImageView imageView, RequestOptions requestOptions) {
        if (t == null) {
            bindImage(imageView, requestOptions);
        } else {
            bindImage(imageView, t.coverPath, t.coverUri, requestOptions);
        }
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

    public List<T> getCovers() {
        return getItemData().getCovers();
    }

    public RequestOptions getCoverDisplayImageOptionsByPosition(int i) {
        return this.mRequestOptions;
    }

    public Drawable getCoverForegroundDrawable(boolean z) {
        ShapeDrawable shapeDrawable = new ShapeDrawable();
        Paint paint = shapeDrawable.getPaint();
        paint.setStyle(z ? Paint.Style.FILL_AND_STROKE : Paint.Style.STROKE);
        paint.setColor(ResourceUtils.getColor(R.color.album_empty_cover_background));
        paint.setStrokeWidth(DimensionUtils.getDimensionPixelSize(R.dimen.album_cover_stroke_size));
        shapeDrawable.setIntrinsicHeight(-1);
        shapeDrawable.setIntrinsicWidth(-1);
        return shapeDrawable;
    }

    public Uri getDownloadUri(long j, int i) {
        if (i == 0) {
            return ContentUris.withAppendedId(GalleryContract.Cloud.CLOUD_URI, j);
        }
        return null;
    }

    @Override // com.miui.epoxy.EpoxyModel
    public Object getDiffChangeResult(EpoxyModel epoxyModel) {
        if (epoxyModel instanceof FourPalaceGridCoverItemModel) {
            FourPalaceGridCoverViewBean itemData = ((FourPalaceGridCoverItemModel) epoxyModel).getItemData();
            FourPalaceGridCoverViewBean itemData2 = getItemData();
            FourPalaceGridCoverViewBean fourPalaceGridCoverViewBean = new FourPalaceGridCoverViewBean();
            if (!Objects.equals(itemData.getCovers(), itemData2.getCovers())) {
                fourPalaceGridCoverViewBean.setCovers(itemData.getCovers());
            }
            if (!Objects.equals(itemData.getAlbumName(), itemData2.getAlbumName())) {
                fourPalaceGridCoverViewBean.setAlbumName(itemData.getAlbumName());
            }
            if (!Objects.equals(itemData.getAlbumDescription(), itemData2.getAlbumDescription())) {
                fourPalaceGridCoverViewBean.setAlbumDescription(itemData.getAlbumDescription());
            }
            return fourPalaceGridCoverViewBean;
        }
        return super.getDiffChangeResult(epoxyModel);
    }

    @Override // com.miui.epoxy.EpoxyModel
    /* renamed from: getViewHolderCreator */
    public EpoxyAdapter.IViewHolderCreator<VH> mo541getViewHolderCreator() {
        return new EpoxyAdapter.IViewHolderCreator<VH>() { // from class: com.miui.gallery.adapter.itemmodel.FourPalaceGridCoverItemModel.1
            @Override // com.miui.epoxy.EpoxyAdapter.IViewHolderCreator
            /* renamed from: create  reason: collision with other method in class */
            public VH mo1603create(View view, View view2) {
                return new VH(view);
            }
        };
    }

    /* loaded from: classes.dex */
    public static class VH extends BaseGalleryViewHolder {
        public ImageView mAlbumCoverBig;
        public ImageView mAlbumCoverFirst;
        public ImageView mAlbumCoverFourth;
        public ImageView mAlbumCoverSecond;
        public ImageView mAlbumCoverThird;
        public ViewGroup mCoverParentView;
        public TextView mTvAlbumName;
        public TextView mTvAlbumPhotoNumber;

        public VH(View view) {
            super(view);
            this.mCoverParentView = (ViewGroup) findViewById(R.id.album_covers);
            this.mTvAlbumPhotoNumber = (TextView) findViewById(R.id.album_common_sub_title);
            TextView textView = (TextView) findViewById(R.id.album_common_title);
            this.mTvAlbumName = textView;
            textView.getPaint().setFakeBoldText(true);
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
