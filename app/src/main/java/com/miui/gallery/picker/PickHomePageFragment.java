package com.miui.gallery.picker;

import android.app.Activity;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.R;
import com.miui.gallery.activity.HomePageStartupHelper;
import com.miui.gallery.activity.InternalPhotoPageActivity;
import com.miui.gallery.adapter.ProportionStringTagAdapter;
import com.miui.gallery.adapter.SyncStateDisplay$DisplayScene;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.picker.albumdetail.IPickAlbumDetail;
import com.miui.gallery.picker.helper.CursorUtils;
import com.miui.gallery.picker.helper.PickableBaseTimeLineAdapterWrapper;
import com.miui.gallery.picker.helper.Picker;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.ui.Checkable;
import com.miui.gallery.ui.MicroThumbGridItem;
import com.miui.gallery.util.PhotoPageIntent;
import com.miui.gallery.util.StringUtils;
import com.miui.gallery.widget.EmptyPage;
import com.miui.gallery.widget.recyclerview.FastScrollerCapsule;
import com.miui.gallery.widget.recyclerview.FastScrollerCapsuleViewProvider;
import com.miui.gallery.widget.recyclerview.FastScrollerTimeCapsuleView;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.miui.gallery.widget.recyclerview.GridItemSpacingDecoration;
import com.miui.gallery.widget.recyclerview.IrregularSpanSizeLookup;
import com.miui.gallery.widget.recyclerview.ProportionTagAdapterProvider;
import com.miui.gallery.widget.recyclerview.ProportionTagBaseAdapter;
import com.miui.gallery.widget.recyclerview.ProportionTagModel;
import com.miui.gallery.widget.recyclerview.SpanSizeProvider;
import com.miui.gallery.widget.recyclerview.grouped.GroupedItemManager;
import java.util.List;

/* loaded from: classes2.dex */
public class PickHomePageFragment extends PickerFragment implements IPickAlbumDetail {
    public int mColumns;
    public EmptyPage mEmptyView;
    public GroupedItemManager mGroupedItemManager;
    public PickableBaseTimeLineAdapterWrapper mHomePageAdapter;
    public HomePagePhotoLoaderCallback mHomePagePhotoLoaderCallback;
    public HomePageStartupHelper mHomePageStartHelper;
    public GalleryRecyclerView mRecyclerView;
    public int mSpacing;
    public GridItemSpacingDecoration mSpacingDecoration;
    public boolean mTagProportionChanged = true;
    public ProportionTagBaseAdapter<Integer> mTimeTagAdapter;

    @Override // com.miui.gallery.picker.PickerCompatFragment
    public String getPageName() {
        return "picker_home";
    }

    @Override // com.miui.gallery.picker.PickerCompatFragment
    public boolean recordPageByDefault() {
        return false;
    }

    @Override // com.miui.gallery.picker.PickerFragment
    public boolean supportFoldBurstItems() {
        return true;
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.picker_home_page, viewGroup, false);
        this.mRecyclerView = (GalleryRecyclerView) inflate.findViewById(R.id.grid);
        this.mGroupedItemManager = new GroupedItemManager();
        this.mColumns = Config$ThumbConfig.get().sMicroThumbColumnsPortrait;
        this.mSpacing = getResources().getDimensionPixelSize(R.dimen.micro_thumb_vertical_spacing);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this.mActivity, this.mColumns);
        gridLayoutManager.setSpanSizeLookup(IrregularSpanSizeLookup.create(new SpanSizeProvider() { // from class: com.miui.gallery.picker.PickHomePageFragment.1
            @Override // com.miui.gallery.widget.recyclerview.SpanSizeProvider
            public int getSpanSize(int i) {
                if (GroupedItemManager.getPackedPositionChild(PickHomePageFragment.this.mGroupedItemManager.getExpandablePosition(i)) == -1) {
                    return gridLayoutManager.getSpanCount();
                }
                return 1;
            }

            @Override // com.miui.gallery.widget.recyclerview.SpanSizeProvider
            public int getSpanIndex(int i, int i2) {
                int packedPositionChild = GroupedItemManager.getPackedPositionChild(PickHomePageFragment.this.mGroupedItemManager.getExpandablePosition(i));
                if (packedPositionChild == -1) {
                    return 0;
                }
                return packedPositionChild % i2;
            }
        }));
        this.mRecyclerView.setLayoutManager(gridLayoutManager);
        GridItemSpacingDecoration gridItemSpacingDecoration = new GridItemSpacingDecoration(this.mRecyclerView, false, getResources().getDimensionPixelSize(R.dimen.micro_thumb_horizontal_spacing), this.mSpacing);
        this.mSpacingDecoration = gridItemSpacingDecoration;
        this.mRecyclerView.addItemDecoration(gridItemSpacingDecoration);
        this.mRecyclerView.setAdapter(this.mGroupedItemManager.createWrappedAdapter(this.mHomePageAdapter));
        this.mRecyclerView.setOnItemClickListener(getGridViewOnItemClickListener());
        final PickerHomePageAdapter pickerHomePageAdapter = (PickerHomePageAdapter) this.mHomePageAdapter.getBaseAdapter();
        this.mRecyclerView.setFastScrollEnabled(true);
        this.mRecyclerView.setFastScrollerTopMargin(this.mFastScrollerMarginTop);
        this.mRecyclerView.setScrollingCalculator(pickerHomePageAdapter);
        this.mRecyclerView.setCapsuleCalculator(pickerHomePageAdapter);
        this.mRecyclerView.setFastScrollerCapsuleViewProvider(new FastScrollerCapsuleViewProvider() { // from class: com.miui.gallery.picker.PickHomePageFragment.2
            @Override // com.miui.gallery.widget.recyclerview.FastScrollerCapsuleViewProvider
            public boolean isShowCapsule() {
                return true;
            }

            @Override // com.miui.gallery.widget.recyclerview.FastScrollerCapsuleViewProvider
            public FastScrollerCapsule createFastScrollerCapsule() {
                FastScrollerTimeCapsuleView fastScrollerTimeCapsuleView = new FastScrollerTimeCapsuleView(PickHomePageFragment.this.getContext());
                fastScrollerTimeCapsuleView.setStyle(R.style.FastScrollTimeCapsule);
                return fastScrollerTimeCapsuleView;
            }
        });
        this.mRecyclerView.setProportionTagAdapterProvider(new ProportionTagAdapterProvider<Integer>() { // from class: com.miui.gallery.picker.PickHomePageFragment.3
            @Override // com.miui.gallery.widget.recyclerview.ProportionTagAdapterProvider
            public boolean isShowProportionTag() {
                return PickHomePageFragment.this.getResources().getConfiguration().orientation != 2;
            }

            @Override // com.miui.gallery.widget.recyclerview.ProportionTagAdapterProvider
            public ProportionTagBaseAdapter<Integer> createTagAdapter() {
                if (PickHomePageFragment.this.mTimeTagAdapter == null) {
                    PickHomePageFragment pickHomePageFragment = PickHomePageFragment.this;
                    pickHomePageFragment.mTimeTagAdapter = new ProportionStringTagAdapter(pickHomePageFragment.getContext());
                }
                return PickHomePageFragment.this.mTimeTagAdapter;
            }

            @Override // com.miui.gallery.widget.recyclerview.ProportionTagAdapterProvider
            public boolean isProportionTagChanged() {
                return PickHomePageFragment.this.mTagProportionChanged;
            }

            @Override // com.miui.gallery.widget.recyclerview.ProportionTagAdapterProvider
            public List<ProportionTagModel<Integer>> getProportionTagModel() {
                PickHomePageFragment.this.mTagProportionChanged = false;
                return pickerHomePageAdapter.calculateTagProportionList();
            }
        });
        this.mRecyclerView.setOnTouchListener(new View.OnTouchListener() { // from class: com.miui.gallery.picker.PickHomePageFragment.4
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getActionMasked() != 2 || !PickHomePageFragment.this.mRecyclerView.isFastScrollerPressed()) {
                    return false;
                }
                PickHomePageFragment.this.mRecyclerView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        this.mRecyclerView.setItemAnimator(null);
        EmptyPage emptyPage = (EmptyPage) inflate.findViewById(16908292);
        this.mEmptyView = emptyPage;
        this.mRecyclerView.setEmptyView(emptyPage);
        this.mEmptyView.setVisibility(8);
        updateConfiguration(getResources().getConfiguration());
        HomePageStartupHelper startupHelper = ((HomePageStartupHelper.Attacher) getActivity()).getStartupHelper();
        this.mHomePageStartHelper = startupHelper;
        if (startupHelper != null) {
            startupHelper.setDataLoaderListener(new HomePageStartupHelper.DataLoadListener() { // from class: com.miui.gallery.picker.PickHomePageFragment.5
                @Override // com.miui.gallery.activity.HomePageStartupHelper.DataLoadListener
                public void onDataLoadFinish(Cursor cursor) {
                    PickHomePageFragment pickHomePageFragment = PickHomePageFragment.this;
                    if (pickHomePageFragment.mActivity == null) {
                        return;
                    }
                    pickHomePageFragment.mHomePageStartHelper.setDataLoaderListener(null);
                    if (cursor == null) {
                        return;
                    }
                    PickHomePageFragment.this.onLoaderFinished(cursor);
                    PickHomePageFragment.this.mHomePageStartHelper = null;
                }
            });
        }
        return inflate;
    }

    public void onPermissionsChecked() {
        this.mHomePageAdapter.notifyDataSetChanged();
    }

    @Override // com.miui.gallery.picker.PickerFragment, com.miui.gallery.picker.PickerCompatFragment, com.miui.gallery.picker.PickerBaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mHomePageAdapter = new PickableBaseTimeLineAdapterWrapper(this, this.mPicker, new PickerHomePageAdapter(this.mActivity, SyncStateDisplay$DisplayScene.SCENE_NONE, getLifecycle()));
    }

    @Override // com.miui.gallery.picker.PickerCompatFragment, androidx.fragment.app.Fragment
    public void setUserVisibleHint(boolean z) {
        super.setUserVisibleHint(z);
        if (!z || !isResumed()) {
            return;
        }
        refreshPickState();
    }

    @Override // com.miui.gallery.picker.PickerCompatFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        EmptyPage emptyPage = this.mEmptyView;
        if (emptyPage != null) {
            emptyPage.resumeMaml();
        }
    }

    @Override // com.miui.gallery.picker.PickerCompatFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        EmptyPage emptyPage = this.mEmptyView;
        if (emptyPage != null) {
            emptyPage.pauseMaml();
        }
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        EmptyPage emptyPage = this.mEmptyView;
        if (emptyPage != null) {
            emptyPage.destroyMaml();
        }
        super.onDestroyView();
    }

    @Override // com.miui.gallery.picker.PickerFragment
    public Uri getUri() {
        return GalleryContract.Media.URI.buildUpon().appendQueryParameter("generate_headers", String.valueOf(true)).appendQueryParameter("remove_processing_items", String.valueOf(true)).build();
    }

    @Override // com.miui.gallery.picker.albumdetail.IPickAlbumDetail
    public boolean onPhotoItemClick(Cursor cursor, View view) {
        if (this.mPicker.getMode() == Picker.Mode.SINGLE) {
            this.mPicker.pick(genKeyFromCursor(cursor));
            this.mPicker.done(-1);
            return true;
        }
        return false;
    }

    @Override // com.miui.gallery.picker.albumdetail.IPickAlbumDetail
    public int unwrapPosition(int i) {
        long expandablePosition = this.mGroupedItemManager.getExpandablePosition(i);
        int packedPositionGroup = GroupedItemManager.getPackedPositionGroup(expandablePosition);
        int packedPositionChild = GroupedItemManager.getPackedPositionChild(expandablePosition);
        if (packedPositionChild == -1) {
            return -1;
        }
        return this.mHomePageAdapter.packDataPosition(packedPositionGroup, packedPositionChild);
    }

    @Override // com.miui.gallery.picker.albumdetail.IPickAlbumDetail
    public boolean bindCheckState(View view, Cursor cursor) {
        if (view instanceof MicroThumbGridItem) {
            MicroThumbGridItem microThumbGridItem = (MicroThumbGridItem) view;
            microThumbGridItem.setCheckable(true);
            microThumbGridItem.setIsOpenCheckBoxAnim(false);
        }
        return false;
    }

    public final void refreshPickState() {
        int unwrapPosition;
        int findFirstVisibleItemPosition = this.mRecyclerView.findFirstVisibleItemPosition();
        for (int i = 0; i < this.mRecyclerView.getChildCount(); i++) {
            View childAt = this.mRecyclerView.getChildAt(i);
            if ((childAt instanceof Checkable) && (unwrapPosition = unwrapPosition(findFirstVisibleItemPosition + i)) != -1) {
                ((Checkable) childAt).setChecked(this.mPicker.contains(genKeyFromCursor(this.mHomePageAdapter.mo1558getItem(unwrapPosition))));
            }
        }
    }

    public final void onLoaderFinished(Cursor cursor) {
        this.mTagProportionChanged = true;
        this.mHomePageAdapter.swapCursor(cursor);
        this.mRecyclerView.setClipToPadding(false);
        GalleryRecyclerView galleryRecyclerView = this.mRecyclerView;
        galleryRecyclerView.setPadding(galleryRecyclerView.getPaddingLeft(), this.mRecyclerView.getTop(), this.mRecyclerView.getPaddingRight(), getResources().getDimensionPixelOffset(R.dimen.safe_distance_bottom));
    }

    public String getSelection() {
        StringBuilder sb = new StringBuilder();
        sb.append("sha1 NOT NULL AND alias_hidden = 0 AND alias_rubbish = 0 AND localGroupId != -1000");
        if (this.mPicker.getMediaType() != Picker.MediaType.ALL) {
            sb.append(" AND ");
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

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mHomePagePhotoLoaderCallback = new HomePagePhotoLoaderCallback();
        getLoaderManager().initLoader(1, null, this.mHomePagePhotoLoaderCallback);
    }

    @Override // com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateConfiguration(configuration);
    }

    public final void updateConfiguration(Configuration configuration) {
        int findFirstVisibleItemPosition = this.mRecyclerView.findFirstVisibleItemPosition();
        if (configuration.orientation == 2) {
            this.mColumns = Config$ThumbConfig.get().sMicroThumbColumnsLand;
        } else {
            this.mColumns = Config$ThumbConfig.get().sMicroThumbColumnsPortrait;
        }
        PickableBaseTimeLineAdapterWrapper pickableBaseTimeLineAdapterWrapper = this.mHomePageAdapter;
        if (pickableBaseTimeLineAdapterWrapper != null && pickableBaseTimeLineAdapterWrapper.getBaseAdapter() != null && (this.mHomePageAdapter.getBaseAdapter() instanceof PickerHomePageAdapter)) {
            PickerHomePageAdapter pickerHomePageAdapter = (PickerHomePageAdapter) this.mHomePageAdapter.getBaseAdapter();
            pickerHomePageAdapter.setSpacing(this.mSpacing);
            pickerHomePageAdapter.setSpanCount(this.mColumns);
        }
        ((GridLayoutManager) this.mRecyclerView.getLayoutManager()).setSpanCount(this.mColumns);
        this.mRecyclerView.scrollToPosition(findFirstVisibleItemPosition);
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        PickableBaseTimeLineAdapterWrapper pickableBaseTimeLineAdapterWrapper = this.mHomePageAdapter;
        if (pickableBaseTimeLineAdapterWrapper != null) {
            pickableBaseTimeLineAdapterWrapper.swapCursor(null);
        }
        super.onDestroy();
    }

    @Override // com.miui.gallery.picker.albumdetail.IPickAlbumDetail
    public Activity getPickerActivity() {
        return getActivity();
    }

    @Override // com.miui.gallery.picker.albumdetail.IPickAlbumDetail
    public void gotoPhotoPageFromPicker(RecyclerView recyclerView, View view, int i) {
        Cursor mo1558getItem = this.mHomePageAdapter.mo1558getItem(i);
        new PhotoPageIntent.Builder(getActivity(), InternalPhotoPageActivity.class).setAdapterView(this.mRecyclerView).setUri(getPreviewUri()).setSelection(getPreviewSelection(mo1558getItem)).setSelectionArgs(getPreviewSelectionArgs(mo1558getItem)).setOrderBy(getPreviewOrder()).setImageLoadParams(new ImageLoadParams.Builder().setKey(getKey(mo1558getItem)).setFilePath(getLocalPath(mo1558getItem)).setTargetSize(Config$ThumbConfig.get().sMicroTargetSize).setInitPosition(0).setMimeType(CursorUtils.getMimeType(mo1558getItem)).fromFace(isPreviewFace()).setFileLength(getFileLength(mo1558getItem)).setImageWidth(CursorUtils.getWidth(mo1558getItem)).setImageHeight(CursorUtils.getHeight(mo1558getItem)).setCreateTime(CursorUtils.getCreateTime(mo1558getItem)).setLocation(CursorUtils.getLocation(mo1558getItem)).build()).setIdForPicker(getKey(mo1558getItem)).setUnfoldBurst(!supportFoldBurstItems()).setPreview(true).build().gotoPhotoPage();
    }

    /* loaded from: classes2.dex */
    public class HomePagePhotoLoaderCallback implements LoaderManager.LoaderCallbacks {
        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader loader) {
        }

        public HomePagePhotoLoaderCallback() {
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader onCreateLoader(int i, Bundle bundle) {
            CursorLoader cursorLoader = new CursorLoader(PickHomePageFragment.this.mActivity);
            cursorLoader.setUri(PickHomePageFragment.this.getUri());
            cursorLoader.setProjection(PickerHomePageAdapter.PROJECTION);
            cursorLoader.setSortOrder("alias_sort_time DESC ");
            cursorLoader.setSelection(PickHomePageFragment.this.getSelection());
            cursorLoader.setSelectionArgs(PickHomePageFragment.this.getSelectionArgs());
            return cursorLoader;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader loader, Object obj) {
            PickHomePageFragment.this.onLoaderFinished((Cursor) obj);
        }
    }
}
