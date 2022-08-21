package com.miui.gallery.model.datalayer.repository.album.cloud;

import android.content.Context;
import com.miui.gallery.model.datalayer.repository.album.common.CommonAlbumModelImpl;
import com.miui.gallery.model.datalayer.repository.album.common.ICommonAlbumModel;
import com.miui.gallery.model.datalayer.repository.album.common.QueryParam;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.provider.album.config.QueryFlagsBuilder;
import com.miui.gallery.util.MediaAndAlbumOperations;
import io.reactivex.Flowable;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Callable;

/* loaded from: classes2.dex */
public class CloudAlbumModelImpl implements ICloudAlbumModel {
    public final WeakReference<Context> mContext;
    public final ICommonAlbumModel mModelSource;

    public CloudAlbumModelImpl(Context context) {
        this.mContext = new WeakReference<>(context);
        this.mModelSource = new CommonAlbumModelImpl(context);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.cloud.ICloudAlbumModel
    public Flowable<PageResults<List<Album>>> queryCloudAlbums() {
        return this.mModelSource.queryAlbums(new QueryFlagsBuilder().joinVirtualScreenshotsRecorders().excludeRealScreenshotsAndRecorders().joinOtherShareAlbums().excludeEmptyAlbum().build(), new QueryParam.Builder().selection("(serverId IS NULL OR serverId NOT IN( 1))", null).build());
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.cloud.ICloudAlbumModel
    public Flowable<Boolean> doChangeAlbumBackupStatus(final boolean z, final long j) {
        return Flowable.fromCallable(new Callable<Boolean>() { // from class: com.miui.gallery.model.datalayer.repository.album.cloud.CloudAlbumModelImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public Boolean mo1117call() throws Exception {
                return Boolean.valueOf(MediaAndAlbumOperations.doChangeAutoUpload((Context) CloudAlbumModelImpl.this.mContext.get(), j, z, false));
            }
        });
    }
}
