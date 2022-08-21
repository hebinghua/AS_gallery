package com.miui.gallery.model.datalayer.repository.album.common;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import androidx.loader.content.Loader;
import com.miui.gallery.dao.AlbumTableServices;
import com.miui.gallery.model.datalayer.utils.AlbumCursorConvert;
import com.miui.gallery.model.datalayer.utils.LoaderFlowableOnSubscribe;
import com.miui.gallery.model.dto.Album;
import io.reactivex.FlowableEmitter;
import java.lang.ref.WeakReference;
import java.util.List;

/* loaded from: classes2.dex */
public class AlbumLoaderFlowableOnSubscribe extends LoaderFlowableOnSubscribe<Cursor, List<Album>> {
    public final WeakReference<Context> mContext;
    public final long mQueryFlags;
    public final QueryParam mQueryParam;
    public final Uri mUri;

    public AlbumLoaderFlowableOnSubscribe(Context context, Uri uri, QueryParam queryParam, long j) {
        this.mContext = new WeakReference<>(context);
        this.mUri = uri;
        this.mQueryFlags = j;
        this.mQueryParam = queryParam;
    }

    @Override // com.miui.gallery.model.datalayer.utils.LoaderFlowableOnSubscribe
    public Loader<Cursor> getLoader() {
        return AlbumTableServices.getQueryAlbumsLoader(this.mContext.get(), this.mUri, this.mQueryParam, this.mQueryFlags);
    }

    @Override // com.miui.gallery.model.datalayer.utils.LoaderFlowableOnSubscribe
    public void subscribe(FlowableEmitter<List<Album>> flowableEmitter, Cursor cursor) {
        if (cursor == null || cursor.isClosed()) {
            return;
        }
        List<Album> mo1129convert = AlbumCursorConvert.getInstance().mo1129convert(cursor);
        if (cursor.isClosed()) {
            return;
        }
        flowableEmitter.onNext(mo1129convert);
    }
}
