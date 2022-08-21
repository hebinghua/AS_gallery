package com.miui.gallery.adapter.itemmodel;

import android.widget.ImageView;
import com.miui.gallery.adapter.itemmodel.FourPalaceGridCoverItemModel;
import com.miui.gallery.model.dto.BaseAlbumCover;
import com.miui.gallery.ui.album.main.viewbean.FourPalaceGridCoverViewBean;
import com.miui.gallery.util.imageloader.imageloadiotion.AlbumImageLoadOptions;
import java.util.List;

/* loaded from: classes.dex */
public class OtherAlbumGridCoverItemModel<T extends BaseAlbumCover> extends FourPalaceGridCoverItemModel<T> {
    public OtherAlbumGridCoverItemModel(FourPalaceGridCoverViewBean fourPalaceGridCoverViewBean) {
        super(fourPalaceGridCoverViewBean);
    }

    @Override // com.miui.gallery.adapter.itemmodel.FourPalaceGridCoverItemModel
    public void bindCovers(FourPalaceGridCoverItemModel.VH vh) {
        int size = getCovers() != null ? getCovers().size() : 0;
        if (size == 4) {
            super.bindCovers(vh);
            return;
        }
        for (int i = 0; i < 4; i++) {
            ImageView imageViewById = vh.getImageViewById(FourPalaceGridCoverItemModel.sAlbumCoverImageViewIds.get(i).intValue());
            if (imageViewById != null) {
                gone(imageViewById);
            }
        }
        List<T> covers = getCovers();
        ImageView bigImageView = vh.getBigImageView();
        bigImageView.setForeground(getCoverForegroundDrawable(false));
        if (size < 4 && size > 0) {
            bigImageView.setVisibility(0);
            bindSingleCover(covers.get(0), bigImageView);
            return;
        }
        bindSingleCover(null, bigImageView);
    }

    @Override // com.miui.gallery.adapter.itemmodel.FourPalaceGridCoverItemModel
    public void onNoCovers(FourPalaceGridCoverItemModel.VH vh) {
        for (int i = 0; i < 4; i++) {
            gone(getImageView(i, vh));
        }
        ImageView bigImageView = vh.getBigImageView();
        bigImageView.setForeground(null);
        bigImageView.setVisibility(0);
        bindImage(bigImageView, AlbumImageLoadOptions.getInstance().getOtherAlbumNoCoverOptions());
    }
}
