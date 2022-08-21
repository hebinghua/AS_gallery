package com.miui.gallery.picker;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.miui.gallery.adapter.FacePageAdapter;
import com.miui.gallery.picker.albumdetail.AlbumItemCheckListener;
import com.miui.gallery.picker.helper.CursorUtils;
import com.miui.gallery.picker.helper.PickerItemHolder;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.ui.MicroThumbGridItem;
import com.miui.gallery.widget.recyclerview.AbsSingleImageViewHolder;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.miui.gallery.widget.recyclerview.GridItemSpacingDecoration;
import com.miui.gallery.widget.recyclerview.IrregularSpanSizeLookup;
import com.miui.gallery.widget.recyclerview.SpanSizeProvider;
import com.miui.gallery.widget.recyclerview.grouped.GroupedItemManager;
import java.util.List;

/* loaded from: classes2.dex */
public class PickFaceAlbumFragment extends PickAlbumDetailFragmentBase {
    public PickFacePageAdapter mAdapter;
    public AlbumItemCheckListener mAlbumItemCheckListener;
    public FacePagePhotoLoaderCallback mFacePagePhotoLoaderCallback;
    public GroupedItemManager mGroupedItemManager;
    public long mLocalIdOfAlbum;
    public String mServerIdOfAlbum;
    public GridItemSpacingDecoration mSpacingDecoration;

    @Override // com.miui.gallery.picker.PickerCompatFragment
    public String getPageName() {
        return "picker_face_album";
    }

    @Override // com.miui.gallery.picker.PickerFragment
    public String getPreviewOrder() {
        return "dateTaken DESC ";
    }

    @Override // com.miui.gallery.picker.PickerFragment
    public String getPreviewSelection(Cursor cursor) {
        return null;
    }

    @Override // com.miui.gallery.picker.PickerFragment
    public boolean isPreviewFace() {
        return true;
    }

    @Override // com.miui.gallery.picker.PickerFragment
    public boolean supportFoldBurstItems() {
        return false;
    }

    public PickFaceAlbumFragment() {
        super("face-album");
    }

    @Override // com.miui.gallery.picker.PickerFragment, com.miui.gallery.picker.PickerCompatFragment, com.miui.gallery.picker.PickerBaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mAdapter = new PickFacePageAdapter(this.mActivity, getLifecycle());
        if (this.mActivity.getIntent().getBooleanExtra("need_pick_face_id", false)) {
            this.mAdapter.changeDisplayMode();
        }
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.face_page, viewGroup, false);
        GalleryRecyclerView galleryRecyclerView = (GalleryRecyclerView) inflate.findViewById(R.id.grid);
        this.mRecyclerView = galleryRecyclerView;
        galleryRecyclerView.setOnItemClickListener(getGridViewOnItemClickListener());
        this.mRecyclerView.setFastScrollEnabled(true);
        this.mRecyclerView.setFastScrollerTopMargin(this.mFastScrollerMarginTop);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this.mActivity, Config$ThumbConfig.get().sMicroThumbColumnsPortrait);
        gridLayoutManager.setSpanSizeLookup(IrregularSpanSizeLookup.create(new SpanSizeProvider() { // from class: com.miui.gallery.picker.PickFaceAlbumFragment.1
            @Override // com.miui.gallery.widget.recyclerview.SpanSizeProvider
            public int getSpanSize(int i) {
                if (GroupedItemManager.getPackedPositionChild(PickFaceAlbumFragment.this.mGroupedItemManager.getExpandablePosition(i)) == -1) {
                    return gridLayoutManager.getSpanCount();
                }
                return 1;
            }

            @Override // com.miui.gallery.widget.recyclerview.SpanSizeProvider
            public int getSpanIndex(int i, int i2) {
                int packedPositionChild = GroupedItemManager.getPackedPositionChild(PickFaceAlbumFragment.this.mGroupedItemManager.getExpandablePosition(i));
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
        this.mAlbumItemCheckListener = new AlbumItemCheckListener(this, this.mPicker);
        GridItemSpacingDecoration gridItemSpacingDecoration = new GridItemSpacingDecoration(this.mRecyclerView, false, getResources().getDimensionPixelSize(R.dimen.micro_thumb_horizontal_spacing), getResources().getDimensionPixelSize(R.dimen.micro_thumb_vertical_spacing));
        this.mSpacingDecoration = gridItemSpacingDecoration;
        this.mRecyclerView.addItemDecoration(gridItemSpacingDecoration);
        initialSelections();
        updateConfiguration(getResources().getConfiguration());
        return inflate;
    }

    @Override // com.miui.gallery.picker.PickerFragment
    public Uri getUri() {
        return GalleryContract.PeopleFace.ONE_PERSON_URI.buildUpon().appendQueryParameter("generate_headers", String.valueOf(true)).build();
    }

    @Override // com.miui.gallery.picker.PickerFragment
    public long getKey(Cursor cursor) {
        return CursorUtils.getFacePhotoId(cursor);
    }

    @Override // com.miui.gallery.picker.albumdetail.IPickAlbumDetail
    public String genKeyFromCursor(Cursor cursor) {
        return String.valueOf(CursorUtils.getFacePhotoId(cursor));
    }

    @Override // com.miui.gallery.picker.PickerFragment
    public String getLocalPath(Cursor cursor) {
        return this.mAdapter.getBindImagePath(cursor.getPosition());
    }

    @Override // com.miui.gallery.picker.PickerFragment
    public Uri getPreviewUri() {
        return GalleryContract.PeopleFace.RECOMMEND_FACES_OF_ONE_PERSON_URI;
    }

    @Override // com.miui.gallery.picker.PickAlbumDetailFragmentBase, com.miui.gallery.picker.albumdetail.IPickAlbumDetail
    public boolean onPhotoItemClick(Cursor cursor, View view) {
        if (this.mActivity.getIntent().getBooleanExtra("need_pick_face_id", false)) {
            this.mPicker.pick(CursorUtils.getFaceId(cursor));
            this.mPicker.done(-1);
            return true;
        }
        return super.onPhotoItemClick(cursor, view);
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        PickFacePageAdapter pickFacePageAdapter = this.mAdapter;
        if (pickFacePageAdapter != null) {
            pickFacePageAdapter.swapCursor(null);
        }
        super.onDestroy();
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mServerIdOfAlbum = this.mActivity.getIntent().getStringExtra("server_id_of_album");
        this.mLocalIdOfAlbum = Long.parseLong(this.mActivity.getIntent().getStringExtra("local_id_of_album"));
        this.mFacePagePhotoLoaderCallback = new FacePagePhotoLoaderCallback();
        getLoaderManager().initLoader(1, null, this.mFacePagePhotoLoaderCallback);
    }

    @Override // com.miui.gallery.picker.PickAlbumDetailFragmentBase, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int findFirstVisibleItemPosition = this.mRecyclerView.findFirstVisibleItemPosition();
        if (configuration.orientation == 2) {
            ((GridLayoutManager) this.mRecyclerView.getLayoutManager()).setSpanCount(Config$ThumbConfig.get().sMicroThumbColumnsLand);
        } else {
            ((GridLayoutManager) this.mRecyclerView.getLayoutManager()).setSpanCount(Config$ThumbConfig.get().sMicroThumbColumnsPortrait);
        }
        this.mRecyclerView.scrollToPosition(findFirstVisibleItemPosition);
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

    /* loaded from: classes2.dex */
    public class FacePagePhotoLoaderCallback implements LoaderManager.LoaderCallbacks {
        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader loader) {
        }

        public FacePagePhotoLoaderCallback() {
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader onCreateLoader(int i, Bundle bundle) {
            String str;
            String stringExtra = PickFaceAlbumFragment.this.mActivity.getIntent().getStringExtra("pick_face_ids_in");
            if (!TextUtils.isEmpty(stringExtra)) {
                str = "_id in ( " + stringExtra + ")";
            } else {
                str = null;
            }
            CursorLoader cursorLoader = new CursorLoader(PickFaceAlbumFragment.this.mActivity);
            cursorLoader.setUri(PickFaceAlbumFragment.this.getUri());
            cursorLoader.setProjection(FacePageAdapter.PROJECTION);
            cursorLoader.setSelection(str);
            cursorLoader.setSelectionArgs(new String[]{PickFaceAlbumFragment.this.mServerIdOfAlbum, String.valueOf(PickFaceAlbumFragment.this.mLocalIdOfAlbum)});
            cursorLoader.setSortOrder("dateTaken DESC ");
            return cursorLoader;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader loader, Object obj) {
            PickFaceAlbumFragment.this.mAdapter.swapCursor((Cursor) obj);
            PickFaceAlbumFragment.this.copy2Pick();
        }
    }

    /* loaded from: classes2.dex */
    public class PickFacePageAdapter extends FacePageAdapter {
        public PickFacePageAdapter(Context context, Lifecycle lifecycle) {
            super(context, lifecycle);
        }

        @Override // com.miui.gallery.adapter.FacePageAdapter, com.miui.gallery.adapter.MultiViewMediaAdapter
        public AbsSingleImageViewHolder createSingleImageViewHolder(View view, Lifecycle lifecycle) {
            return new SingleImageViewHolder(view, lifecycle);
        }

        /* loaded from: classes2.dex */
        public class SingleImageViewHolder extends FacePageAdapter.BaseSingleImageViewHolder {
            public SingleImageViewHolder(View view, Lifecycle lifecycle) {
                super(view, lifecycle);
            }

            @Override // com.miui.gallery.adapter.FacePageAdapter.BaseSingleImageViewHolder, com.miui.gallery.widget.recyclerview.AbsViewHolder
            public void bindData(int i, int i2, List<Object> list) {
                super.bindData(i, i2, list);
                int packDataPosition = PickFacePageAdapter.this.packDataPosition(i, i2);
                PickFacePageAdapter pickFacePageAdapter = PickFacePageAdapter.this;
                PickFaceAlbumFragment.this.bindCheckState(this.mView, pickFacePageAdapter.mo1558getItem(packDataPosition));
                MicroThumbGridItem microThumbGridItem = this.mView;
                PickFacePageAdapter pickFacePageAdapter2 = PickFacePageAdapter.this;
                PickerItemHolder.bindView(packDataPosition, microThumbGridItem, pickFacePageAdapter2, PickFaceAlbumFragment.this.mAlbumItemCheckListener);
            }
        }
    }
}
