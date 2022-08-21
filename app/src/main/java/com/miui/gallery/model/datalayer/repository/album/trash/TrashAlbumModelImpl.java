package com.miui.gallery.model.datalayer.repository.album.trash;

import android.content.Context;
import android.database.Cursor;
import androidx.loader.content.Loader;
import com.miui.gallery.dao.AlbumTableServices;
import com.miui.gallery.model.datalayer.utils.LoaderFlowableOnSubscribe;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public class TrashAlbumModelImpl implements ITrashAlbumModel {
    public WeakReference<Context> mContext;

    public TrashAlbumModelImpl(Context context) {
        this.mContext = new WeakReference<>(context);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.trash.ITrashAlbumModel
    public Flowable<Integer> queryTrashAlbumCount() {
        return Flowable.create(new LoaderFlowableOnSubscribe<Cursor, Integer>() { // from class: com.miui.gallery.model.datalayer.repository.album.trash.TrashAlbumModelImpl.1
            @Override // com.miui.gallery.model.datalayer.utils.LoaderFlowableOnSubscribe
            public Loader<Cursor> getLoader() {
                return AlbumTableServices.getQueryTrashAlbumPhotoCountLoader((Context) TrashAlbumModelImpl.this.mContext.get());
            }

            @Override // com.miui.gallery.model.datalayer.utils.LoaderFlowableOnSubscribe
            public void subscribe(FlowableEmitter<Integer> flowableEmitter, Cursor cursor) {
                if (cursor == null || cursor.isClosed() || !cursor.moveToFirst()) {
                    return;
                }
                flowableEmitter.onNext(Integer.valueOf(cursor.getInt(0)));
            }
        }, BackpressureStrategy.LATEST);
    }
}
