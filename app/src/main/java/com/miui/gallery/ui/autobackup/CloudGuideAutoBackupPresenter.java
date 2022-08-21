package com.miui.gallery.ui.autobackup;

import com.miui.gallery.model.datalayer.config.ModelConfig;
import com.miui.gallery.model.datalayer.config.ModelManager;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.model.dto.ShareAlbum;
import com.miui.gallery.ui.album.common.AlbumConstants;
import com.miui.gallery.ui.album.common.usecase.QueryAlbumsCase;
import com.miui.gallery.ui.album.common.usecase.QueryShareAlbumCase;
import com.miui.gallery.ui.autobackup.viewbean.CloudGuideAutoBackupItemViewBean;
import com.miui.gallery.util.SimpleDisposableSubscriber;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/* loaded from: classes2.dex */
public class CloudGuideAutoBackupPresenter extends CloudGuideAutoBackupContract$P {
    private QueryAlbumsCase mQueryAlbumsCase;
    private QueryShareAlbumCase mQueryShareAlbumCase;

    @Override // com.miui.gallery.base_optimization.mvp.presenter.BasePresenter, com.miui.gallery.base_optimization.mvp.presenter.IPresenter
    public void onAttachView(CloudGuideAutoBackupContract$V cloudGuideAutoBackupContract$V) {
        super.onAttachView((CloudGuideAutoBackupPresenter) cloudGuideAutoBackupContract$V);
        initUseCases((AbstractAlbumRepository) ModelManager.getInstance().getModel(ModelConfig.ModelNames.ALBUM_REPOSITORY));
    }

    private void initUseCases(AbstractAlbumRepository abstractAlbumRepository) {
        this.mQueryAlbumsCase = new QueryAlbumsCase(abstractAlbumRepository);
        this.mQueryShareAlbumCase = new QueryShareAlbumCase(abstractAlbumRepository);
    }

    @Override // com.miui.gallery.ui.autobackup.CloudGuideAutoBackupContract$P
    public void initAll() {
        this.mQueryAlbumsCase.executeWith(new SimpleDisposableSubscriber<PageResults<List<Album>>>() { // from class: com.miui.gallery.ui.autobackup.CloudGuideAutoBackupPresenter.1
            @Override // org.reactivestreams.Subscriber
            public void onNext(PageResults<List<Album>> pageResults) {
                CloudGuideAutoBackupPresenter.this.dispatchDatas(pageResults.getResult());
            }
        }, new QueryAlbumsCase.ParamBean.Builder().queryFlags(AlbumConstants.QueryScene.SCENE_CLOUD_GUIDE_AUTO_BACKUP).build(), ((CloudGuideAutoBackupContract$V) getView()).getLifecycle());
        this.mQueryShareAlbumCase.executeWith(new SimpleDisposableSubscriber<List<ShareAlbum>>() { // from class: com.miui.gallery.ui.autobackup.CloudGuideAutoBackupPresenter.2
            @Override // org.reactivestreams.Subscriber
            public void onNext(List<ShareAlbum> list) {
            }
        }, null, ((CloudGuideAutoBackupContract$V) getView()).getLifecycle());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchDatas(List<Album> list) {
        ((CloudGuideAutoBackupContract$V) getView()).setDatas((List) list.stream().map(new Function<Album, CloudGuideAutoBackupItemViewBean>() { // from class: com.miui.gallery.ui.autobackup.CloudGuideAutoBackupPresenter.3
            @Override // java.util.function.Function
            public CloudGuideAutoBackupItemViewBean apply(Album album) {
                CloudGuideAutoBackupItemViewBean cloudGuideAutoBackupItemViewBean = new CloudGuideAutoBackupItemViewBean();
                cloudGuideAutoBackupItemViewBean.mapping(album);
                return cloudGuideAutoBackupItemViewBean;
            }
        }).collect(Collectors.toList()));
    }
}
