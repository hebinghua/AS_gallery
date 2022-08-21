package com.miui.gallery.model.datalayer.repository.album.share;

import android.content.Context;
import android.database.Cursor;
import androidx.loader.content.Loader;
import com.miui.epoxy.common.CollectionUtils;
import com.miui.gallery.dao.AlbumTableServices;
import com.miui.gallery.model.datalayer.utils.LoaderFlowableOnSubscribe;
import com.miui.gallery.model.dto.ShareAlbum;
import com.miui.gallery.share.ShareAlbumCacheManager;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/* loaded from: classes2.dex */
public class ShareAlbumModelImpl implements IShareAlbumModel {
    public WeakReference<Context> mContext;

    public ShareAlbumModelImpl(Context context) {
        this.mContext = new WeakReference<>(context);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.share.IShareAlbumModel
    public Flowable<List<ShareAlbum>> queryAlbumListShareInfo() {
        return Flowable.create(new LoaderFlowableOnSubscribe<Cursor, List<ShareAlbum>>() { // from class: com.miui.gallery.model.datalayer.repository.album.share.ShareAlbumModelImpl.1
            @Override // com.miui.gallery.model.datalayer.utils.LoaderFlowableOnSubscribe
            public Loader<Cursor> getLoader() {
                return AlbumTableServices.getQueryShareAlbumLoader((Context) ShareAlbumModelImpl.this.mContext.get());
            }

            @Override // com.miui.gallery.model.datalayer.utils.LoaderFlowableOnSubscribe
            public void subscribe(FlowableEmitter<List<ShareAlbum>> flowableEmitter, Cursor cursor) {
                ShareAlbumCacheManager.getInstance().putSharedAlbums(cursor);
                if (cursor == null || !cursor.moveToFirst()) {
                    flowableEmitter.onNext(CollectionUtils.emptyList());
                    return;
                }
                Collection<ShareAlbum> shareAlbumList = ShareAlbumCacheManager.getInstance().getShareAlbumList();
                ArrayList arrayList = new ArrayList(shareAlbumList.size());
                arrayList.addAll(shareAlbumList);
                flowableEmitter.onNext(arrayList);
            }
        }, BackpressureStrategy.LATEST);
    }
}
