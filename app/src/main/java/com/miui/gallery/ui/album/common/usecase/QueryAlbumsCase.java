package com.miui.gallery.ui.album.common.usecase;

import android.os.Bundle;
import com.miui.gallery.base_optimization.clean.HotUseCase;
import com.miui.gallery.base_optimization.clean.thread.ObserveThreadExecutor;
import com.miui.gallery.base_optimization.clean.thread.SubScribeThreadExecutor;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.model.datalayer.repository.album.common.QueryParam;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.util.thread.RxGalleryExecutors;
import io.reactivex.Flowable;
import java.util.List;

/* loaded from: classes2.dex */
public class QueryAlbumsCase extends HotUseCase<PageResults<List<Album>>, ParamBean> {
    public AbstractAlbumRepository mAlbumRepository;

    public QueryAlbumsCase(AbstractAlbumRepository abstractAlbumRepository) {
        this(RxGalleryExecutors.getInstance().getUserThreadExecutor(), RxGalleryExecutors.getInstance().getUiThreadExecutor(), abstractAlbumRepository);
    }

    public QueryAlbumsCase(SubScribeThreadExecutor subScribeThreadExecutor, ObserveThreadExecutor observeThreadExecutor, AbstractAlbumRepository abstractAlbumRepository) {
        super(subScribeThreadExecutor, observeThreadExecutor);
        this.mAlbumRepository = abstractAlbumRepository;
    }

    @Override // com.miui.gallery.base_optimization.clean.HotUseCase
    public Flowable<PageResults<List<Album>>> buildFlowable(ParamBean paramBean) {
        if (paramBean == null) {
            return Flowable.error(new IllegalArgumentException("param cant null!"));
        }
        Bundle bundle = null;
        if (paramBean.isQuerySnapSource()) {
            Bundle bundle2 = new Bundle(1);
            bundle2.putString("query_snap_source_key", null);
            bundle = bundle2;
        }
        return this.mAlbumRepository.queryAlbums(paramBean.getQueryFlags(), new QueryParam.Builder().selection(paramBean.getExtraSelection(), paramBean.getExtraSelectionArgs()).extra(bundle).build());
    }

    /* loaded from: classes2.dex */
    public static class ParamBean {
        public boolean isQuerySnapSource;
        public String mExtraSelection;
        public String[] mExtraSelectionArgs;
        public final long mQueryFlags;

        public ParamBean(long j, String str, String[] strArr, boolean z) {
            this.mQueryFlags = j;
            this.mExtraSelectionArgs = strArr;
            this.mExtraSelection = str;
            this.isQuerySnapSource = z;
        }

        public ParamBean(long j) {
            this.mQueryFlags = j;
        }

        public String getExtraSelection() {
            return this.mExtraSelection;
        }

        public String[] getExtraSelectionArgs() {
            return this.mExtraSelectionArgs;
        }

        public long getQueryFlags() {
            return this.mQueryFlags;
        }

        public boolean isQuerySnapSource() {
            return this.isQuerySnapSource;
        }

        /* loaded from: classes2.dex */
        public static class Builder {
            public boolean isQuerySnapSource;
            public String[] mExtraSelectionArgs;
            public long mQueryFlags;
            public String mSelection;

            public Builder queryFlags(long j) {
                this.mQueryFlags = j;
                return this;
            }

            public Builder selection(String str) {
                this.mSelection = str;
                return this;
            }

            public Builder queryFromSnapSource() {
                this.isQuerySnapSource = true;
                return this;
            }

            public ParamBean build() {
                return new ParamBean(this.mQueryFlags, this.mSelection, this.mExtraSelectionArgs, this.isQuerySnapSource);
            }
        }
    }
}
