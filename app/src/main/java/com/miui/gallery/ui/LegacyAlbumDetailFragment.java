package com.miui.gallery.ui;

import android.database.Cursor;
import android.os.Bundle;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.R;
import com.miui.gallery.adapter.AlbumDetailAdapter;
import com.miui.gallery.adapter.AlbumType;
import com.miui.gallery.adapter.PreloadItem;
import com.miui.gallery.adapter.SortBy;
import com.miui.gallery.widget.editwrapper.EditableListViewWrapper;
import java.util.Collections;
import java.util.List;

/* loaded from: classes2.dex */
public class LegacyAlbumDetailFragment extends AlbumDetailFragment<AlbumDetailAdapter> {
    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    /* renamed from: getViewAdapter */
    public AlbumDetailAdapter mo1517getViewAdapter() {
        RecyclerView.Adapter adapter = (RecyclerView.Adapter) WrapperAdapterUtils.findWrappedAdapter(this.mRecyclerView.getAdapter(), AlbumDetailAdapter.class);
        if (adapter != null) {
            return (AlbumDetailAdapter) adapter;
        }
        return (AlbumDetailAdapter) super.mo1517getViewAdapter();
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    /* renamed from: getAdapter */
    public AlbumDetailAdapter mo1564getAdapter() {
        if (this.mAlbumDetailAdapter == 0) {
            AlbumDetailAdapter albumDetailAdapter = new AlbumDetailAdapter(this.mActivity, getLifecycle());
            this.mAlbumDetailAdapter = albumDetailAdapter;
            if (this.mIsOtherShareAlbum) {
                albumDetailAdapter.setAlbumType(AlbumType.OTHER_SHARE);
            } else if (this.mIsScreenshotAlbum) {
                albumDetailAdapter.setAlbumType(AlbumType.SCREENSHOT);
            } else if (this.mIsVideoAlbum) {
                albumDetailAdapter.setAlbumType(AlbumType.VIDEO);
            } else if (isSecretAlbum()) {
                ((AlbumDetailAdapter) this.mAlbumDetailAdapter).setAlbumType(AlbumType.SECRET);
            } else if (isFavoritesAlbum()) {
                ((AlbumDetailAdapter) this.mAlbumDetailAdapter).setAlbumType(AlbumType.FAVORITES);
            } else if (isAllPhotosAlbum()) {
                ((AlbumDetailAdapter) this.mAlbumDetailAdapter).setAlbumType(AlbumType.ALL_PHOTOS);
            }
        }
        return (AlbumDetailAdapter) this.mAlbumDetailAdapter;
    }

    @Override // com.bumptech.glide.ListPreloader.PreloadModelProvider
    public List<PreloadItem> getPreloadItems(int i) {
        int[] unpackGroupedPosition = this.mEditableWrapper.unpackGroupedPosition(i);
        if (unpackGroupedPosition[0] == -1 || unpackGroupedPosition[1] == -1) {
            return Collections.emptyList();
        }
        return mo1564getAdapter().getPreloadItems(mo1564getAdapter().packDataPosition(unpackGroupedPosition[0], unpackGroupedPosition[1]));
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public LoaderManager.LoaderCallbacks<Cursor> getLoaderCallback() {
        return new AlbumDetailLoaderCallback();
    }

    @Override // com.miui.gallery.ui.AlbumDetailFragment
    public void onSortByChanged() {
        mo1564getAdapter().setCurrentSortBy(this.mViewModel.getSortBy());
        mo1564getAdapter().setShowTimeLine(needShowTimeLine());
        Loader loader = getLoaderManager().getLoader(1);
        configLoader((CursorLoader) loader, this.mViewModel.getSortBy());
        loader.forceLoad();
    }

    public final void configLoader(CursorLoader cursorLoader, SortBy sortBy) {
        cursorLoader.setUri(getUri(sortBy));
        cursorLoader.setProjection(AlbumDetailAdapter.PROJECTION);
        cursorLoader.setSelection(getSelection());
        cursorLoader.setSelectionArgs(getSelectionArgs());
        cursorLoader.setSortOrder(configOrderBy(sortBy));
    }

    /* loaded from: classes2.dex */
    public class AlbumDetailLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {
        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader<Cursor> loader) {
        }

        public AlbumDetailLoaderCallback() {
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            CursorLoader cursorLoader = new CursorLoader(LegacyAlbumDetailFragment.this.mActivity);
            LegacyAlbumDetailFragment legacyAlbumDetailFragment = LegacyAlbumDetailFragment.this;
            legacyAlbumDetailFragment.configLoader(cursorLoader, legacyAlbumDetailFragment.mViewModel.getSortBy());
            return cursorLoader;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            RecyclerViewPreloader recyclerViewPreloader;
            LegacyAlbumDetailFragment legacyAlbumDetailFragment = LegacyAlbumDetailFragment.this;
            legacyAlbumDetailFragment.mTagProportionChanged = true;
            AlbumDetailAdapter mo1564getAdapter = legacyAlbumDetailFragment.mo1564getAdapter();
            mo1564getAdapter.setShowTimeLine(LegacyAlbumDetailFragment.this.needShowTimeLine());
            mo1564getAdapter.setCurrentSortBy(LegacyAlbumDetailFragment.this.mViewModel.getSortBy());
            mo1564getAdapter.swapCursor(cursor);
            mo1564getAdapter.setSpacing(LegacyAlbumDetailFragment.this.mSpacing);
            mo1564getAdapter.setSpanCount(LegacyAlbumDetailFragment.this.mColumns);
            LegacyAlbumDetailFragment.this.mEditableWrapper.setAdapter(mo1564getAdapter);
            LegacyAlbumDetailFragment.this.mEditableWrapper.setHandleTouchAnimItemType(AlbumDetailGridItem.class.getSimpleName());
            EditableListViewWrapper editableListViewWrapper = LegacyAlbumDetailFragment.this.mEditableWrapper;
            if (Config$ThumbConfig.get().sPreloadNum == 0) {
                recyclerViewPreloader = null;
            } else {
                LegacyAlbumDetailFragment legacyAlbumDetailFragment2 = LegacyAlbumDetailFragment.this;
                recyclerViewPreloader = new RecyclerViewPreloader(legacyAlbumDetailFragment2, legacyAlbumDetailFragment2, mo1564getAdapter, Config$ThumbConfig.get().sPreloadNum);
            }
            editableListViewWrapper.setOnScrollListener(mo1564getAdapter.generateWrapScrollListener(recyclerViewPreloader));
            LegacyAlbumDetailFragment.this.mRecyclerView.setScrollingCalculator(mo1564getAdapter);
            if (LegacyAlbumDetailFragment.this.mViewModel.getSortBy() == SortBy.UPDATE_DATE || LegacyAlbumDetailFragment.this.mViewModel.getSortBy() == SortBy.CREATE_DATE) {
                LegacyAlbumDetailFragment.this.mRecyclerView.setCapsuleCalculator(mo1564getAdapter);
                if (LegacyAlbumDetailFragment.this.mViewModel.isTimeGroup()) {
                    LegacyAlbumDetailFragment legacyAlbumDetailFragment3 = LegacyAlbumDetailFragment.this;
                    legacyAlbumDetailFragment3.mFastScrollerMarginTop = legacyAlbumDetailFragment3.getResources().getDimensionPixelOffset(R.dimen.time_line_header_height) + LegacyAlbumDetailFragment.this.getResources().getDimensionPixelSize(R.dimen.fast_scroller_margin_top_to_time_line);
                } else {
                    LegacyAlbumDetailFragment legacyAlbumDetailFragment4 = LegacyAlbumDetailFragment.this;
                    legacyAlbumDetailFragment4.mFastScrollerMarginTop = legacyAlbumDetailFragment4.getResources().getDimensionPixelSize(R.dimen.fast_scroller_margin_top_to_time_line);
                }
            } else {
                LegacyAlbumDetailFragment.this.mRecyclerView.setCapsuleCalculator(null);
                LegacyAlbumDetailFragment legacyAlbumDetailFragment5 = LegacyAlbumDetailFragment.this;
                legacyAlbumDetailFragment5.mFastScrollerMarginTop = legacyAlbumDetailFragment5.getResources().getDimensionPixelSize(R.dimen.fast_scroller_margin_top_to_time_line);
            }
            LegacyAlbumDetailFragment legacyAlbumDetailFragment6 = LegacyAlbumDetailFragment.this;
            legacyAlbumDetailFragment6.mRecyclerView.setFastScrollerTopMargin(legacyAlbumDetailFragment6.mFastScrollerMarginTop);
        }
    }
}
