package com.miui.gallery.picker;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.Lifecycle;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.R;
import com.miui.gallery.adapter.AlbumDetailAdapter;
import com.miui.gallery.adapter.RecentDiscoveryAdapter;
import com.miui.gallery.adapter.SyncStateDisplay$DisplayScene;
import com.miui.gallery.picker.albumdetail.AlbumItemCheckListener;
import com.miui.gallery.picker.helper.Picker;
import com.miui.gallery.picker.helper.PickerItemHolder;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.album.AlbumManager;
import com.miui.gallery.ui.Checkable;
import com.miui.gallery.ui.MicroThumbGridItem;
import com.miui.gallery.util.StringUtils;
import com.miui.gallery.widget.recyclerview.AbsSingleImageViewHolder;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.miui.gallery.widget.recyclerview.GridItemSpacingDecoration;
import com.miui.gallery.widget.recyclerview.IrregularSpanSizeLookup;
import com.miui.gallery.widget.recyclerview.SpanSizeProvider;
import com.miui.gallery.widget.recyclerview.grouped.GroupedItemManager;
import java.util.List;

/* loaded from: classes2.dex */
public class PickRecentDiscoveryFragment extends PickAlbumDetailFragmentBase {
    public PickRecentAlbumAdapter mAdapter;
    public AlbumItemCheckListener mAlbumItemCheckListener;
    public GroupedItemManager mGroupedItemManager;
    public RecentDiscoveryLoaderCallback mLoaderCallback;
    public GridItemSpacingDecoration mSpacingDecoration;

    @Override // com.miui.gallery.picker.PickerCompatFragment
    public String getPageName() {
        return "picker_recent_album";
    }

    public final String getSortOrder() {
        return "dateModified DESC";
    }

    public PickRecentDiscoveryFragment() {
        super("recent");
    }

    @Override // com.miui.gallery.picker.PickerFragment, com.miui.gallery.picker.PickerCompatFragment, com.miui.gallery.picker.PickerBaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        PickRecentAlbumAdapter pickRecentAlbumAdapter = new PickRecentAlbumAdapter(this.mActivity, getLifecycle());
        this.mAdapter = pickRecentAlbumAdapter;
        pickRecentAlbumAdapter.setShareAlbums(null);
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.album_recent, viewGroup, false);
        GalleryRecyclerView galleryRecyclerView = (GalleryRecyclerView) inflate.findViewById(R.id.grid);
        this.mRecyclerView = galleryRecyclerView;
        galleryRecyclerView.setOnItemClickListener(getGridViewOnItemClickListener());
        this.mRecyclerView.setFastScrollEnabled(true);
        this.mRecyclerView.setFastScrollerTopMargin(this.mFastScrollerMarginTop);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this.mActivity, Config$ThumbConfig.get().sMicroThumbColumnsPortrait);
        gridLayoutManager.setSpanSizeLookup(IrregularSpanSizeLookup.create(new SpanSizeProvider() { // from class: com.miui.gallery.picker.PickRecentDiscoveryFragment.1
            @Override // com.miui.gallery.widget.recyclerview.SpanSizeProvider
            public int getSpanSize(int i) {
                if (GroupedItemManager.getPackedPositionChild(PickRecentDiscoveryFragment.this.mGroupedItemManager.getExpandablePosition(i)) == -1) {
                    return gridLayoutManager.getSpanCount();
                }
                return 1;
            }

            @Override // com.miui.gallery.widget.recyclerview.SpanSizeProvider
            public int getSpanIndex(int i, int i2) {
                int packedPositionChild = GroupedItemManager.getPackedPositionChild(PickRecentDiscoveryFragment.this.mGroupedItemManager.getExpandablePosition(i));
                if (packedPositionChild == -1) {
                    return 0;
                }
                return packedPositionChild % i2;
            }
        }));
        this.mRecyclerView.setLayoutManager(gridLayoutManager);
        GroupedItemManager groupedItemManager = new GroupedItemManager();
        this.mGroupedItemManager = groupedItemManager;
        this.mRecyclerView.setAdapter(groupedItemManager.createWrappedAdapter(this.mAdapter));
        GridItemSpacingDecoration gridItemSpacingDecoration = new GridItemSpacingDecoration(this.mRecyclerView, false, getResources().getDimensionPixelSize(R.dimen.micro_thumb_horizontal_spacing), getResources().getDimensionPixelSize(R.dimen.micro_thumb_vertical_spacing));
        this.mSpacingDecoration = gridItemSpacingDecoration;
        this.mRecyclerView.addItemDecoration(gridItemSpacingDecoration);
        initialSelections();
        updateConfiguration(getResources().getConfiguration());
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mLoaderCallback = new RecentDiscoveryLoaderCallback();
        getLoaderManager().initLoader(1, null, this.mLoaderCallback);
        getLoaderManager().initLoader(2, null, this.mLoaderCallback);
    }

    @Override // com.miui.gallery.picker.PickAlbumDetailFragmentBase, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateConfiguration(configuration);
    }

    @Override // com.miui.gallery.picker.PickAlbumDetailFragmentBase
    public void updateConfiguration(Configuration configuration) {
        int i;
        int findFirstVisibleItemPosition = this.mRecyclerView.findFirstVisibleItemPosition();
        if (configuration.orientation == 2) {
            i = Config$ThumbConfig.get().sMicroThumbRecentColumnsLand;
        } else {
            i = Config$ThumbConfig.get().sMicroThumbRecentColumnsPortrait;
        }
        ((GridLayoutManager) this.mRecyclerView.getLayoutManager()).setSpanCount(i);
        this.mRecyclerView.scrollToPosition(findFirstVisibleItemPosition);
    }

    public String getSelection() {
        StringBuilder sb = new StringBuilder();
        if (getPicker().getMediaType() != Picker.MediaType.ALL) {
            sb.append("serverType");
            sb.append(" =? ");
        }
        if (StringUtils.isValid(getPicker().getFilterMimeTypes())) {
            sb.append(" AND ");
            sb.append(getFilterSelectionWithMimeType(getPicker().getFilterMimeTypes()));
        }
        return sb.toString();
    }

    public String[] getSelectionArgs() {
        if (getPicker().getMediaType() == Picker.MediaType.IMAGE) {
            return new String[]{String.valueOf(1)};
        }
        if (getPicker().getMediaType() != Picker.MediaType.VIDEO) {
            return null;
        }
        return new String[]{String.valueOf(2)};
    }

    @Override // com.miui.gallery.picker.PickerFragment
    public Uri getUri() {
        return GalleryContract.Media.URI_RECENT_MEDIA.buildUpon().appendQueryParameter("generate_headers", String.valueOf(true)).appendQueryParameter("remove_processing_items", String.valueOf(true)).build();
    }

    /* loaded from: classes2.dex */
    public class RecentDiscoveryLoaderCallback implements LoaderManager.LoaderCallbacks {
        public RecentDiscoveryLoaderCallback() {
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader onCreateLoader(int i, Bundle bundle) {
            CursorLoader cursorLoader = new CursorLoader(PickRecentDiscoveryFragment.this.mActivity);
            if (i == 1) {
                cursorLoader.setUri(PickRecentDiscoveryFragment.this.getUri());
                cursorLoader.setProjection(RecentDiscoveryAdapter.PROJECTION);
                cursorLoader.setSelection(PickRecentDiscoveryFragment.this.getSelection());
                cursorLoader.setSelectionArgs(PickRecentDiscoveryFragment.this.getSelectionArgs());
                cursorLoader.setSortOrder(PickRecentDiscoveryFragment.this.getSortOrder());
            } else if (i == 2) {
                cursorLoader.setUri(GalleryContract.Album.URI_QUERY_ALL_MODE);
                cursorLoader.setProjection(AlbumManager.QUERY_ALBUM_PROJECTION);
                cursorLoader.setSortOrder("sortBy ASC ");
            }
            return cursorLoader;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader loader, Object obj) {
            int id = loader.getId();
            if (id == 1) {
                PickRecentDiscoveryFragment.this.mAdapter.swapCursor((Cursor) obj);
                PickRecentDiscoveryFragment.this.copy2Pick();
            } else if (id != 2) {
            } else {
                PickRecentDiscoveryFragment.this.mAdapter.setAllAlbums((Cursor) obj);
            }
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader loader) {
            int id = loader.getId();
            if (id == 1) {
                PickRecentDiscoveryFragment.this.mAdapter.changeCursor(null);
            } else if (id != 2) {
            } else {
                PickRecentDiscoveryFragment.this.mAdapter.setAllAlbums(null);
            }
        }
    }

    /* loaded from: classes2.dex */
    public class PickRecentAlbumAdapter extends RecentDiscoveryAdapter {
        public PickRecentAlbumAdapter(Context context, Lifecycle lifecycle) {
            super(context, SyncStateDisplay$DisplayScene.SCENE_NONE, lifecycle);
        }

        @Override // com.miui.gallery.adapter.AlbumDetailAdapter, com.miui.gallery.adapter.MultiViewMediaAdapter
        public AbsSingleImageViewHolder createSingleImageViewHolder(View view, Lifecycle lifecycle) {
            return new SingleImageViewHolder(view, lifecycle);
        }

        /* loaded from: classes2.dex */
        public class SingleImageViewHolder extends AlbumDetailAdapter.BaseSingleImageViewHolder {
            public SingleImageViewHolder(View view, Lifecycle lifecycle) {
                super(view, lifecycle);
            }

            @Override // com.miui.gallery.adapter.AlbumDetailAdapter.BaseSingleImageViewHolder, com.miui.gallery.widget.recyclerview.AbsViewHolder
            public void bindData(int i, int i2, List<Object> list) {
                super.bindData(i, i2, list);
                int packDataPosition = PickRecentAlbumAdapter.this.packDataPosition(i, i2);
                PickRecentAlbumAdapter pickRecentAlbumAdapter = PickRecentAlbumAdapter.this;
                PickRecentDiscoveryFragment.this.bindCheckState(this.mView, pickRecentAlbumAdapter.mo1558getItem(packDataPosition));
                MicroThumbGridItem microThumbGridItem = this.mView;
                PickRecentAlbumAdapter pickRecentAlbumAdapter2 = PickRecentAlbumAdapter.this;
                PickerItemHolder.bindView(packDataPosition, microThumbGridItem, pickRecentAlbumAdapter2, PickRecentDiscoveryFragment.this.mAlbumItemCheckListener);
            }
        }
    }

    @Override // com.miui.gallery.picker.PickAlbumDetailFragmentBase, com.miui.gallery.picker.albumdetail.IPickAlbumDetail
    public boolean bindCheckState(View view, Cursor cursor) {
        if (this.mPicker.getMode() == Picker.Mode.SINGLE) {
            if (view instanceof MicroThumbGridItem) {
                ((MicroThumbGridItem) view).setSimilarMarkEnable(true);
            }
            return true;
        }
        Checkable checkable = (Checkable) view;
        checkable.setCheckable(true);
        checkable.setChecked(this.mPicker.contains(genKeyFromCursor(cursor)));
        return true;
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        PickRecentAlbumAdapter pickRecentAlbumAdapter = this.mAdapter;
        if (pickRecentAlbumAdapter != null) {
            pickRecentAlbumAdapter.swapCursor(null);
        }
        super.onDestroy();
    }

    @Override // com.miui.gallery.picker.PickerFragment
    public boolean supportFoldBurstItems() {
        return this.mAdapter.supportFoldBurstItems();
    }

    @Override // com.miui.gallery.picker.albumdetail.IPickAlbumDetail
    public int unwrapPosition(int i) {
        long expandablePosition = this.mGroupedItemManager.getExpandablePosition(i);
        int packedPositionGroup = GroupedItemManager.getPackedPositionGroup(expandablePosition);
        int packedPositionChild = GroupedItemManager.getPackedPositionChild(expandablePosition);
        if (packedPositionChild == -1) {
            return -1;
        }
        return this.mAdapter.packDataPosition(packedPositionGroup, packedPositionChild);
    }
}
