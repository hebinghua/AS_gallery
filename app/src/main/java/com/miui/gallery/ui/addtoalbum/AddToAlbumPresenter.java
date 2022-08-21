package com.miui.gallery.ui.addtoalbum;

import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import com.miui.gallery.app.base.BaseUseCase;
import com.miui.gallery.base_optimization.clean.LifecycleUseCase;
import com.miui.gallery.model.datalayer.config.ModelConfig;
import com.miui.gallery.model.datalayer.config.ModelManager;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.model.datalayer.repository.AbstractCloudRepository;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.ui.addtoalbum.usecase.QueryAddAlbumPageDatasCase;
import com.miui.gallery.ui.album.common.usecase.QueryItemFilePath;
import com.miui.gallery.ui.album.common.usecase.QueryShareAlbumCase;
import com.miui.gallery.util.DebugUtil;
import com.miui.gallery.util.SimpleDisposableSubscriber;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class AddToAlbumPresenter extends AddToAlbumContract$P {
    private static final String TAG = "AddToAlbumPresenter";
    private boolean isFirstLoadData = true;
    private boolean isShowSecretAlbum;
    private LifecycleUseCase mQueryAlbumsCase;
    private BaseUseCase mQueryPhotoPathCase;
    private LifecycleUseCase mQueryShareAlbumCase;

    @Override // com.miui.gallery.ui.addtoalbum.AddToAlbumContract$P
    public void initUsecase() {
        AbstractAlbumRepository abstractAlbumRepository = (AbstractAlbumRepository) ModelManager.getInstance().getModel(ModelConfig.ModelNames.ALBUM_REPOSITORY);
        this.mQueryAlbumsCase = new QueryAddAlbumPageDatasCase(abstractAlbumRepository);
        this.mQueryShareAlbumCase = new QueryShareAlbumCase(abstractAlbumRepository);
        this.mQueryPhotoPathCase = new QueryItemFilePath((AbstractCloudRepository) ModelManager.getInstance().getModel(ModelConfig.ModelNames.CLOUD_REPOSITORY));
    }

    @Override // com.miui.gallery.ui.addtoalbum.AddToAlbumContract$P
    public void onInitData() {
        final int i;
        boolean z;
        boolean z2;
        final long currentTimeMillis = System.currentTimeMillis();
        QueryItemFilePath.RequestParam requestParam = null;
        Bundle extras = getView().getIntent() != null ? getView().getIntent().getExtras() : null;
        if (extras != null) {
            boolean z3 = extras.getBoolean("show_share_album", true);
            this.isShowSecretAlbum = extras.getBoolean("show_add_secret", true);
            boolean z4 = extras.getInt("extra_from_type", -1) != 1019;
            long[] longArray = extras.getLongArray("media_id_array");
            ArrayList parcelableArrayList = extras.getParcelableArrayList("media_uri_array");
            if (longArray != null) {
                i = longArray.length;
            } else {
                i = parcelableArrayList != null ? parcelableArrayList.size() : 0;
            }
            if (longArray != null) {
                requestParam = new QueryItemFilePath.RequestParam(Long.valueOf(longArray[0]));
            } else if (parcelableArrayList != null) {
                requestParam = new QueryItemFilePath.RequestParam((Uri) parcelableArrayList.get(0));
            }
            this.mQueryPhotoPathCase.executeWith(new SimpleDisposableSubscriber<List<Pair<String, Byte[]>>>() { // from class: com.miui.gallery.ui.addtoalbum.AddToAlbumPresenter.1
                @Override // org.reactivestreams.Subscriber
                public void onNext(List<Pair<String, Byte[]>> list) {
                    AddToAlbumPresenter.this.getView().bindHeaderInfo(list.isEmpty() ? null : list.get(0), i);
                }
            }, requestParam, getView().getLifecycle());
            z = z3;
            z2 = z4;
        } else {
            i = 0;
            z = true;
            z2 = true;
        }
        boolean isAddPicToPdf = getView().isAddPicToPdf();
        DefaultLogger.d(TAG, "start query addToAlbumPage Datas,param:isShowSecret=[%s],isAddPicToPdfButton=[%s],isShowOtherShare=[%s],selectedPhotoCount:[%s]", Boolean.valueOf(this.isShowSecretAlbum), Boolean.valueOf(isAddPicToPdf), Boolean.valueOf(z), String.valueOf(i));
        this.mQueryAlbumsCase.executeWith(new SimpleDisposableSubscriber<AddToAlbumGroupResult>() { // from class: com.miui.gallery.ui.addtoalbum.AddToAlbumPresenter.2
            @Override // org.reactivestreams.Subscriber
            public void onNext(AddToAlbumGroupResult addToAlbumGroupResult) {
                DefaultLogger.d(AddToAlbumPresenter.TAG, "end query addToAlbumPage Datas,cost:[%s],size:[%s]", String.valueOf(System.currentTimeMillis() - currentTimeMillis), String.valueOf(addToAlbumGroupResult.getDataSize()));
                AddToAlbumPresenter.this.dispatchAlbums(addToAlbumGroupResult);
            }
        }, new QueryAddAlbumPageDatasCase.ParamBean(this.isShowSecretAlbum, z, true, isAddPicToPdf, z2), getView().getLifecycle());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchAlbums(AddToAlbumGroupResult addToAlbumGroupResult) {
        getView().dispatchAlbums(addToAlbumGroupResult.getDatas(), addToAlbumGroupResult.getModels());
        if (this.isFirstLoadData) {
            this.isFirstLoadData = false;
            DebugUtil.logEventTime("operationTrace", "show_add_album_page", true);
        }
    }

    @Override // com.miui.gallery.ui.addtoalbum.AddToAlbumContract$P
    public void onRecordLastSelectedAlbum(long j) {
        if (j == -1000 || j == 1000) {
            return;
        }
        GalleryPreferences.Album.setAddToAlbumPageLastSelectedAlbumId(j);
    }
}
