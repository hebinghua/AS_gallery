package com.miui.gallery.ui;

import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GalleryGridLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.R;
import com.miui.gallery.activity.InternalPhotoPageActivity;
import com.miui.gallery.adapter.AlbumDetailAdapter;
import com.miui.gallery.adapter.IAlbumAdapter;
import com.miui.gallery.adapter.ICursorAdapter;
import com.miui.gallery.adapter.IMediaAdapter;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.album.main.utils.factory.GalleryViewCreator;
import com.miui.gallery.util.PhotoPageIntent;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.miui.gallery.widget.recyclerview.GridItemSpacingDecoration;
import com.miui.gallery.widget.recyclerview.ItemClickSupport;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class PhotoListFragmentBase<ADAPTER extends IMediaAdapter> extends BaseMediaFragment {
    public LoaderManager.LoaderCallbacks<Cursor> mAlbumDetailLoaderCallback;
    public long mAlbumId = -1;
    public String mAlbumName;
    public int mColumns;
    public View mEmptyView;
    public GalleryRecyclerView mRecyclerView;
    public int mSpacing;
    public GridItemSpacingDecoration mSpacingDecoration;

    /* renamed from: getAdapter */
    public abstract ADAPTER mo1564getAdapter();

    public String getCurrentSortOrder() {
        return "alias_sort_time DESC ";
    }

    public abstract int getLayoutSource();

    public abstract String getSelection();

    public abstract String[] getSelectionArgs();

    public long getSupportOperationMask() {
        return -1L;
    }

    public abstract Uri getUri();

    public void onDataLoaded(int i) {
    }

    public void onEmptyViewVisibilityChanged(int i) {
    }

    /* renamed from: getViewAdapter */
    public ADAPTER mo1517getViewAdapter() {
        return mo1564getAdapter();
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        if (layoutInflater.getFactory2() != null) {
            layoutInflater = layoutInflater.cloneInContext(getContext());
        }
        layoutInflater.setFactory2(GalleryViewCreator.getViewFactory());
        View inflate = layoutInflater.inflate(getLayoutSource(), viewGroup, false);
        GalleryRecyclerView galleryRecyclerView = (GalleryRecyclerView) inflate.findViewById(R.id.grid);
        this.mRecyclerView = galleryRecyclerView;
        galleryRecyclerView.setItemAnimator(null);
        if (getResources().getConfiguration().orientation == 2) {
            this.mColumns = Config$ThumbConfig.get().sMicroThumbColumnsLand;
        } else {
            this.mColumns = Config$ThumbConfig.get().sMicroThumbColumnsPortrait;
        }
        this.mRecyclerView.setLayoutManager(new GalleryGridLayoutManager(this.mActivity, this.mColumns));
        this.mSpacing = getResources().getDimensionPixelSize(R.dimen.micro_thumb_vertical_spacing);
        GridItemSpacingDecoration gridItemSpacingDecoration = new GridItemSpacingDecoration(this.mRecyclerView, false, getResources().getDimensionPixelSize(R.dimen.micro_thumb_horizontal_spacing), this.mSpacing);
        this.mSpacingDecoration = gridItemSpacingDecoration;
        this.mRecyclerView.addItemDecoration(gridItemSpacingDecoration);
        setEmptyViewVisibility(8);
        return inflate;
    }

    public View getEmptyView() {
        return getView().findViewById(16908292);
    }

    public void setEmptyViewVisibility(int i) {
        if (this.mEmptyView == null && i == 0) {
            this.mEmptyView = getEmptyView();
        }
        View view = this.mEmptyView;
        if (view == null || view.getVisibility() == i) {
            return;
        }
        this.mEmptyView.setVisibility(i);
        onEmptyViewVisibilityChanged(i);
    }

    public void stopAndHideScroller() {
        GalleryRecyclerView galleryRecyclerView = this.mRecyclerView;
        if (galleryRecyclerView != null) {
            galleryRecyclerView.stopScroll();
            this.mRecyclerView.hideScrollerBar();
        }
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        mo1564getAdapter().updateGalleryCloudSyncableState();
    }

    public ItemClickSupport.OnItemClickListener getGridViewOnItemClickListener() {
        return new ItemClickSupport.OnItemClickListener() { // from class: com.miui.gallery.ui.PhotoListFragmentBase.1
            @Override // com.miui.gallery.widget.recyclerview.ItemClickSupport.OnItemClickListener
            public boolean onItemClick(RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
                IMediaAdapter mo1517getViewAdapter = PhotoListFragmentBase.this.mo1517getViewAdapter();
                new PhotoPageIntent.Builder(PhotoListFragmentBase.this, InternalPhotoPageActivity.class).setAdapterView(recyclerView).setUri(PhotoListFragmentBase.this.getUri()).setInitPosition(i).setCount(mo1517getViewAdapter.getItemCount()).setSelection(PhotoListFragmentBase.this.getSelection()).setSelectionArgs(PhotoListFragmentBase.this.getSelectionArgs()).setOrderBy(PhotoListFragmentBase.this.getCurrentSortOrder()).setImageLoadParams(new ImageLoadParams.Builder().setKey(mo1517getViewAdapter.getItemKey(i)).setFilePath(mo1517getViewAdapter.getBindImagePath(i)).setTargetSize(Config$ThumbConfig.get().sMicroTargetSize).setRegionRect(mo1517getViewAdapter.getItemDecodeRectF(i)).setInitPosition(i).setMimeType(mo1517getViewAdapter.getMimeType(i)).setSecretKey(mo1517getViewAdapter.getItemSecretKey(i)).setFileLength(mo1517getViewAdapter.getFileLength(i)).setImageWidth(mo1517getViewAdapter.getImageWidth(i)).setImageHeight(mo1517getViewAdapter.getImageHeight(i)).setCreateTime(mo1517getViewAdapter.getCreateTime(i)).setLocation(mo1517getViewAdapter.getLocation(i)).build()).setAlbumId(PhotoListFragmentBase.this.mAlbumId).setAlbumName(PhotoListFragmentBase.this.mAlbumName).setOperationMask(PhotoListFragmentBase.this.getSupportOperationMask()).setUnfoldBurst(!PhotoListFragmentBase.this.mo1564getAdapter().supportFoldBurstItems()).setPreview(PhotoListFragmentBase.this.isPreviewMode()).build().gotoPhotoPage();
                HashMap hashMap = new HashMap();
                hashMap.put("from", PhotoListFragmentBase.this.getPageName());
                hashMap.put("position", String.valueOf(i));
                SamplingStatHelper.recordCountEvent("photo", "click_micro_thumb", hashMap);
                boolean z = mo1517getViewAdapter instanceof IAlbumAdapter;
                boolean z2 = false;
                boolean z3 = z && ((IAlbumAdapter) mo1517getViewAdapter).isAllPhotosAlbum();
                if (z && ((IAlbumAdapter) mo1517getViewAdapter).isBabyAlbum()) {
                    z2 = true;
                }
                HashMap hashMap2 = new HashMap();
                String str = "403.42.1.1.11291";
                hashMap2.put("tip", z3 ? "403.44.1.1.11211" : z2 ? str : "403.15.1.1.11177");
                if (z3) {
                    str = "403.44.0.1.11210";
                } else if (!z2) {
                    str = "403.15.1.1.11176";
                }
                hashMap2.put("ref_tip", str);
                hashMap2.put("position", String.valueOf(i));
                TrackController.trackClick(hashMap2);
                return true;
            }
        };
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        startDataLoading();
    }

    public void startDataLoading() {
        getLoaderManager().initLoader(1, null, new LoaderCallbackWrapper(getLoaderCallback()));
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment
    public List<Loader<?>> getLoaders() {
        return Collections.singletonList(LoaderManager.getInstance(this).getLoader(1));
    }

    public LoaderManager.LoaderCallbacks<Cursor> getLoaderCallback() {
        if (this.mAlbumDetailLoaderCallback == null) {
            this.mAlbumDetailLoaderCallback = new PhotoListLoaderCallback();
        }
        return this.mAlbumDetailLoaderCallback;
    }

    @Override // com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int findFirstVisibleItemPosition = this.mRecyclerView.findFirstVisibleItemPosition();
        updateConfiguration(configuration);
        ((GridLayoutManager) this.mRecyclerView.getLayoutManager()).setSpanCount(this.mColumns);
        this.mRecyclerView.scrollToPosition(findFirstVisibleItemPosition);
    }

    public void updateConfiguration(Configuration configuration) {
        Config$ThumbConfig.get().updateConfig();
        if (configuration.orientation == 2) {
            this.mColumns = Config$ThumbConfig.get().sMicroThumbColumnsLand;
        } else {
            this.mColumns = Config$ThumbConfig.get().sMicroThumbColumnsPortrait;
        }
    }

    public String[] getProjection() {
        return AlbumDetailAdapter.PROJECTION;
    }

    /* loaded from: classes2.dex */
    public class PhotoListLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {
        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader<Cursor> loader) {
        }

        public PhotoListLoaderCallback() {
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            CursorLoader cursorLoader = new CursorLoader(PhotoListFragmentBase.this.mActivity);
            PhotoListFragmentBase.this.configLoader(cursorLoader);
            return cursorLoader;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            IMediaAdapter mo1564getAdapter = PhotoListFragmentBase.this.mo1564getAdapter();
            if (mo1564getAdapter instanceof ICursorAdapter) {
                ((ICursorAdapter) mo1564getAdapter).swapCursor(cursor);
            }
        }
    }

    public void configLoader(CursorLoader cursorLoader) {
        cursorLoader.setUri(getUri());
        cursorLoader.setProjection(getProjection());
        cursorLoader.setSelection(getSelection());
        cursorLoader.setSelectionArgs(getSelectionArgs());
        cursorLoader.setSortOrder(getCurrentSortOrder());
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        ADAPTER mo1517getViewAdapter = mo1517getViewAdapter();
        if (mo1517getViewAdapter instanceof ICursorAdapter) {
            ((ICursorAdapter) mo1517getViewAdapter).swapCursor(null);
        }
        super.onDestroy();
    }

    public boolean isPreviewMode() {
        return getActivity() != null && getActivity().getIntent().getBooleanExtra("photo_preview_mode", false);
    }

    /* loaded from: classes2.dex */
    public class LoaderCallbackWrapper implements LoaderManager.LoaderCallbacks<Cursor> {
        public final LoaderManager.LoaderCallbacks<Cursor> mWrapped;

        public LoaderCallbackWrapper(LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks) {
            this.mWrapped = loaderCallbacks;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            return this.mWrapped.onCreateLoader(i, bundle);
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            this.mWrapped.onLoadFinished(loader, cursor);
            int i = 0;
            if (cursor == null || cursor.getCount() == 0) {
                PhotoListFragmentBase.this.setEmptyViewVisibility(0);
            } else {
                PhotoListFragmentBase.this.setEmptyViewVisibility(8);
            }
            PhotoListFragmentBase photoListFragmentBase = PhotoListFragmentBase.this;
            if (cursor != null) {
                i = cursor.getCount();
            }
            photoListFragmentBase.onDataLoaded(i);
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader<Cursor> loader) {
            this.mWrapped.onLoaderReset(loader);
        }
    }
}
