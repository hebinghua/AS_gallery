package com.miui.gallery.adapter.itemmodel;

import android.widget.ImageView;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.model.dto.AIAlbumCover;
import com.miui.gallery.model.dto.BaseAlbumCover;
import com.miui.gallery.model.dto.FaceAlbumCover;
import com.miui.gallery.ui.album.main.viewbean.FourPalaceGridCoverViewBean;
import com.miui.gallery.util.glide.BindImageHelper;
import com.miui.gallery.util.imageloader.imageloadiotion.AlbumImageLoadOptions;
import java.util.List;

/* loaded from: classes.dex */
public class AIAlbumGridCoverItemModel<T extends AIAlbumCover> extends FourPalaceGridCoverItemModel<T> {
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.miui.gallery.adapter.itemmodel.FourPalaceGridCoverItemModel
    public /* bridge */ /* synthetic */ void bindSingleCover(BaseAlbumCover baseAlbumCover, ImageView imageView, RequestOptions requestOptions) {
        bindSingleCover((AIAlbumGridCoverItemModel<T>) ((AIAlbumCover) baseAlbumCover), imageView, requestOptions);
    }

    public AIAlbumGridCoverItemModel(FourPalaceGridCoverViewBean fourPalaceGridCoverViewBean) {
        super(fourPalaceGridCoverViewBean);
    }

    @Override // com.miui.gallery.adapter.itemmodel.FourPalaceGridCoverItemModel
    public RequestOptions getCoverDisplayImageOptionsByPosition(int i) {
        List<T> covers = getCovers();
        if (covers == 0) {
            return super.getCoverDisplayImageOptionsByPosition(i);
        }
        AIAlbumCover aIAlbumCover = i >= covers.size() ? null : (AIAlbumCover) covers.get(i);
        if ((aIAlbumCover instanceof FaceAlbumCover) && aIAlbumCover.isValid()) {
            return AlbumImageLoadOptions.getInstance().buildFaceRequestOptions(((FaceAlbumCover) aIAlbumCover).faceRectF, aIAlbumCover.coverSize).mo955error(this.mErrorBg).mo957fallback(this.mErrorBg);
        }
        return super.getCoverDisplayImageOptionsByPosition(i);
    }

    public void bindSingleCover(T t, ImageView imageView, RequestOptions requestOptions) {
        if (t instanceof FaceAlbumCover) {
            bindFaceImage(imageView, (FaceAlbumCover) t, requestOptions);
        } else {
            super.bindSingleCover((AIAlbumGridCoverItemModel<T>) t, imageView, requestOptions);
        }
    }

    public void bindFaceImage(ImageView imageView, FaceAlbumCover faceAlbumCover, RequestOptions requestOptions) {
        BindImageHelper.bindFaceImage(faceAlbumCover.coverPath, getDownloadUri(faceAlbumCover.coverId, faceAlbumCover.coverSyncState), imageView, requestOptions, false);
    }
}
