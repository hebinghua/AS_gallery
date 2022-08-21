package com.miui.gallery.picker;

import android.content.ContentUris;
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
import androidx.recyclerview.widget.RecyclerView;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.R;
import com.miui.gallery.adapter.AlbumDetailAdapter;
import com.miui.gallery.adapter.AlbumType;
import com.miui.gallery.adapter.ProportionStringTagAdapter;
import com.miui.gallery.adapter.SortBy;
import com.miui.gallery.adapter.SyncStateDisplay$DisplayScene;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.picker.albumdetail.AlbumItemCheckListener;
import com.miui.gallery.picker.albumdetail.ShowSortImmersionMenuListener;
import com.miui.gallery.picker.helper.Picker;
import com.miui.gallery.picker.helper.PickerItemHolder;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.InternalContract$Cloud;
import com.miui.gallery.provider.cache.AlbumCacheManager;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.AlbumDetailSortImmersionMenuHelper;
import com.miui.gallery.ui.MicroThumbGridItem;
import com.miui.gallery.util.StringUtils;
import com.miui.gallery.widget.recyclerview.AbsSingleImageViewHolder;
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
public class PickAlbumDetailFragment extends PickAlbumDetailFragmentBase implements ShowSortImmersionMenuListener {
    public static final String SELECTION_ONLY_LOCAL = " AND " + InternalContract$Cloud.ALIAS_LOCAL_MEDIA;
    public AlbumDetailLoaderCallback mAlbumDetailLoaderCallback;
    public long mAlbumId;
    public AlbumItemCheckListener mAlbumItemCheckListener;
    public String mAlbumLocalPath;
    public SortBy mCurrentSortBy;
    public String mExtraSelection;
    public GroupedItemManager mGroupedItemManager;
    public boolean mIsCameraAlbum;
    public boolean mIsOtherSharedAlbum;
    public boolean mIsScreenRecorderAlbum;
    public boolean mIsScreenshotAlbum;
    public boolean mIsTimeGroup;
    public AlbumDetailSortImmersionMenuHelper mSortImmersionMenuHelper;
    public String mSortOrder;
    public GridItemSpacingDecoration mSpacingDecoration;
    public boolean mTagProportionChanged;
    public PickAlbumDetailAdapter mTimeLineAdapter;
    public RecyclerView.Adapter mTimeLineAdapterWrapper;
    public ProportionTagBaseAdapter<Integer> mTimeTagAdapter;

    public static /* synthetic */ void $r8$lambda$0SxkUWmlLTenRztzvS9XL2Lps_A(PickAlbumDetailFragment pickAlbumDetailFragment) {
        pickAlbumDetailFragment.lambda$updateConfiguration$0();
    }

    @Override // com.miui.gallery.picker.PickerCompatFragment
    public String getPageName() {
        return "picker_album_detail";
    }

    public PickAlbumDetailFragment() {
        super("album");
        this.mSortOrder = " DESC ";
        this.mIsTimeGroup = true;
        this.mTagProportionChanged = true;
    }

    @Override // com.miui.gallery.picker.PickerFragment, com.miui.gallery.picker.PickerCompatFragment, com.miui.gallery.picker.PickerBaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mTimeLineAdapter = new PickAlbumDetailAdapter(this.mActivity, getLifecycle());
        GroupedItemManager groupedItemManager = new GroupedItemManager();
        this.mGroupedItemManager = groupedItemManager;
        this.mTimeLineAdapterWrapper = groupedItemManager.createWrappedAdapter(this.mTimeLineAdapter);
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.picker_album_detail, viewGroup, false);
        GalleryRecyclerView galleryRecyclerView = (GalleryRecyclerView) inflate.findViewById(R.id.grid);
        this.mRecyclerView = galleryRecyclerView;
        galleryRecyclerView.setItemAnimator(null);
        this.mRecyclerView.setOnItemClickListener(getGridViewOnItemClickListener());
        this.mRecyclerView.setFastScrollEnabled(true);
        this.mRecyclerView.setFastScrollerCapsuleViewProvider(new FastScrollerCapsuleViewProvider() { // from class: com.miui.gallery.picker.PickAlbumDetailFragment.1
            @Override // com.miui.gallery.widget.recyclerview.FastScrollerCapsuleViewProvider
            public boolean isShowCapsule() {
                return true;
            }

            {
                PickAlbumDetailFragment.this = this;
            }

            @Override // com.miui.gallery.widget.recyclerview.FastScrollerCapsuleViewProvider
            public FastScrollerCapsule createFastScrollerCapsule() {
                FastScrollerTimeCapsuleView fastScrollerTimeCapsuleView = new FastScrollerTimeCapsuleView(PickAlbumDetailFragment.this.getContext());
                fastScrollerTimeCapsuleView.setStyle(R.style.FastScrollTimeCapsule);
                return fastScrollerTimeCapsuleView;
            }
        });
        this.mRecyclerView.setProportionTagAdapterProvider(new ProportionTagAdapterProvider<Integer>() { // from class: com.miui.gallery.picker.PickAlbumDetailFragment.2
            {
                PickAlbumDetailFragment.this = this;
            }

            @Override // com.miui.gallery.widget.recyclerview.ProportionTagAdapterProvider
            public boolean isShowProportionTag() {
                return (PickAlbumDetailFragment.this.getResources().getConfiguration().orientation == 2 || PickAlbumDetailFragment.this.mCurrentSortBy == SortBy.NAME || PickAlbumDetailFragment.this.mCurrentSortBy == SortBy.SIZE) ? false : true;
            }

            @Override // com.miui.gallery.widget.recyclerview.ProportionTagAdapterProvider
            public ProportionTagBaseAdapter<Integer> createTagAdapter() {
                if (PickAlbumDetailFragment.this.mTimeTagAdapter == null) {
                    PickAlbumDetailFragment pickAlbumDetailFragment = PickAlbumDetailFragment.this;
                    pickAlbumDetailFragment.mTimeTagAdapter = new ProportionStringTagAdapter(pickAlbumDetailFragment.getContext());
                }
                return PickAlbumDetailFragment.this.mTimeTagAdapter;
            }

            @Override // com.miui.gallery.widget.recyclerview.ProportionTagAdapterProvider
            public boolean isProportionTagChanged() {
                return PickAlbumDetailFragment.this.mTagProportionChanged;
            }

            @Override // com.miui.gallery.widget.recyclerview.ProportionTagAdapterProvider
            public List<ProportionTagModel<Integer>> getProportionTagModel() {
                PickAlbumDetailFragment.this.mTagProportionChanged = false;
                return PickAlbumDetailFragment.this.getAdapter().calculateTagProportionList();
            }
        });
        this.mAlbumItemCheckListener = new AlbumItemCheckListener(this, this.mPicker);
        initialSelections();
        this.mColumns = Config$ThumbConfig.get().sMicroThumbColumnsPortrait;
        this.mSpacing = getResources().getDimensionPixelSize(R.dimen.micro_thumb_vertical_spacing);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this.mActivity, this.mColumns);
        gridLayoutManager.setSpanSizeLookup(IrregularSpanSizeLookup.create(new SpanSizeProvider() { // from class: com.miui.gallery.picker.PickAlbumDetailFragment.3
            {
                PickAlbumDetailFragment.this = this;
            }

            @Override // com.miui.gallery.widget.recyclerview.SpanSizeProvider
            public int getSpanSize(int i) {
                if (GroupedItemManager.getPackedPositionChild(PickAlbumDetailFragment.this.mGroupedItemManager.getExpandablePosition(i)) == -1) {
                    return gridLayoutManager.getSpanCount();
                }
                return 1;
            }

            @Override // com.miui.gallery.widget.recyclerview.SpanSizeProvider
            public int getSpanIndex(int i, int i2) {
                int packedPositionChild = GroupedItemManager.getPackedPositionChild(PickAlbumDetailFragment.this.mGroupedItemManager.getExpandablePosition(i));
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
        updateConfiguration(getResources().getConfiguration());
        return inflate;
    }

    @Override // com.miui.gallery.picker.PickAlbumDetailFragmentBase
    public void updateConfiguration(Configuration configuration) {
        super.updateConfiguration(configuration);
        if (configuration.orientation == 2) {
            this.mColumns = Config$ThumbConfig.get().sMicroThumbColumnsLand;
        } else {
            this.mColumns = Config$ThumbConfig.get().sMicroThumbColumnsPortrait;
        }
        getAdapter().setSpanCount(this.mColumns);
        getAdapter().setSpacing(this.mSpacing);
        ((GridLayoutManager) this.mRecyclerView.getLayoutManager()).setSpanCount(this.mColumns);
        AlbumDetailSortImmersionMenuHelper albumDetailSortImmersionMenuHelper = this.mSortImmersionMenuHelper;
        if (albumDetailSortImmersionMenuHelper == null || !albumDetailSortImmersionMenuHelper.isShowing()) {
            return;
        }
        this.mSortImmersionMenuHelper.dismiss();
        this.mRecyclerView.post(new Runnable() { // from class: com.miui.gallery.picker.PickAlbumDetailFragment$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                PickAlbumDetailFragment.$r8$lambda$0SxkUWmlLTenRztzvS9XL2Lps_A(PickAlbumDetailFragment.this);
            }
        });
    }

    public /* synthetic */ void lambda$updateConfiguration$0() {
        this.mSortImmersionMenuHelper.showImmersionMenu(((PickerActivity) this.mActivity).getAppCompatActionBar().getCustomView().findViewById(R.id.more));
    }

    public final void sortOnChange(SortBy sortBy) {
        if (sortBy == SortBy.UPDATE_DATE) {
            SamplingStatHelper.recordCountEvent("picker", "sort_by_update_date");
        }
        if (sortBy == SortBy.CREATE_DATE) {
            SamplingStatHelper.recordCountEvent("picker", "sort_by_create_date");
        }
        if (sortBy == SortBy.NAME) {
            SamplingStatHelper.recordCountEvent("picker", "sort_by_name");
        }
        if (sortBy == SortBy.SIZE) {
            SamplingStatHelper.recordCountEvent("picker", "sort_by_size");
        }
        String str = " DESC ";
        if (this.mCurrentSortBy == sortBy && this.mSortOrder.equals(str)) {
            str = " ASC ";
        }
        this.mSortOrder = str;
        this.mCurrentSortBy = sortBy;
        this.mSortImmersionMenuHelper.setSortOrder(sortBy, str.equals(" ASC "));
        this.mSortImmersionMenuHelper.updateMenuItem();
        onSortByChanged();
    }

    @Override // com.miui.gallery.picker.PickerFragment
    public Uri getUri() {
        return getUri(SortBy.NONE);
    }

    @Override // com.miui.gallery.picker.albumdetail.ShowSortImmersionMenuListener
    public void onShowSortImmersionMenu(View view) {
        this.mSortImmersionMenuHelper.showImmersionMenu(view);
    }

    /* loaded from: classes2.dex */
    public class AlbumDetailLoaderCallback implements LoaderManager.LoaderCallbacks {
        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader loader) {
        }

        public AlbumDetailLoaderCallback() {
            PickAlbumDetailFragment.this = r1;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader onCreateLoader(int i, Bundle bundle) {
            CursorLoader cursorLoader = new CursorLoader(PickAlbumDetailFragment.this.mActivity);
            PickAlbumDetailFragment pickAlbumDetailFragment = PickAlbumDetailFragment.this;
            pickAlbumDetailFragment.configLoader(cursorLoader, pickAlbumDetailFragment.mCurrentSortBy);
            return cursorLoader;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader loader, Object obj) {
            PickAlbumDetailFragment.this.mTagProportionChanged = true;
            RecyclerView.Adapter adapter = PickAlbumDetailFragment.this.mRecyclerView.getAdapter();
            PickAlbumDetailFragment.this.getAdapter().setShowTimeLine(PickAlbumDetailFragment.this.needShowTimeLine());
            PickAlbumDetailFragment.this.getAdapter().setSpanCount(PickAlbumDetailFragment.this.mColumns);
            PickAlbumDetailFragment.this.getAdapter().setSpacing(PickAlbumDetailFragment.this.mSpacing);
            PickAlbumDetailFragment.this.mTimeLineAdapter.setCurrentSortBy(PickAlbumDetailFragment.this.mCurrentSortBy);
            if (adapter != PickAlbumDetailFragment.this.mTimeLineAdapterWrapper) {
                PickAlbumDetailFragment pickAlbumDetailFragment = PickAlbumDetailFragment.this;
                pickAlbumDetailFragment.mRecyclerView.setAdapter(pickAlbumDetailFragment.mTimeLineAdapterWrapper);
                PickAlbumDetailFragment pickAlbumDetailFragment2 = PickAlbumDetailFragment.this;
                pickAlbumDetailFragment2.mRecyclerView.setScrollingCalculator(pickAlbumDetailFragment2.mTimeLineAdapter);
                PickAlbumDetailFragment pickAlbumDetailFragment3 = PickAlbumDetailFragment.this;
                pickAlbumDetailFragment3.mRecyclerView.setCapsuleCalculator(pickAlbumDetailFragment3.mTimeLineAdapter);
                PickAlbumDetailFragment pickAlbumDetailFragment4 = PickAlbumDetailFragment.this;
                pickAlbumDetailFragment4.mFastScrollerMarginTop = pickAlbumDetailFragment4.getResources().getDimensionPixelOffset(R.dimen.time_line_header_height) + PickAlbumDetailFragment.this.getResources().getDimensionPixelSize(R.dimen.fast_scroller_margin_top_to_time_line);
            }
            PickAlbumDetailFragment.this.mTimeLineAdapter.swapCursor((Cursor) obj);
            PickAlbumDetailFragment.this.copy2Pick();
            PickAlbumDetailFragment pickAlbumDetailFragment5 = PickAlbumDetailFragment.this;
            pickAlbumDetailFragment5.mRecyclerView.setFastScrollerTopMargin(pickAlbumDetailFragment5.mFastScrollerMarginTop);
            PickAlbumDetailFragment.this.mRecyclerView.setClipToPadding(false);
            GalleryRecyclerView galleryRecyclerView = PickAlbumDetailFragment.this.mRecyclerView;
            galleryRecyclerView.setPadding(galleryRecyclerView.getPaddingLeft(), PickAlbumDetailFragment.this.mRecyclerView.getTop(), PickAlbumDetailFragment.this.mRecyclerView.getPaddingRight(), PickAlbumDetailFragment.this.getResources().getDimensionPixelOffset(R.dimen.safe_distance_bottom));
        }
    }

    public final boolean needShowTimeLine() {
        SortBy sortBy;
        return this.mIsTimeGroup && ((sortBy = this.mCurrentSortBy) == SortBy.UPDATE_DATE || sortBy == SortBy.CREATE_DATE);
    }

    public PickAlbumDetailAdapter getAdapter() {
        if (this.mTimeLineAdapter == null) {
            this.mTimeLineAdapter = new PickAlbumDetailAdapter(this.mActivity, getLifecycle());
        }
        return this.mTimeLineAdapter;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (getActivity() instanceof PickAlbumDetailActivity) {
            if (((PickAlbumDetailActivity) getActivity()).mIsBabyAlbum) {
                this.mCurrentSortBy = SortBy.CREATE_DATE;
                this.mIsTimeGroup = true;
            } else if (bundle != null) {
                restoreInstanceState(bundle);
            } else {
                initAlbumDetailSort();
                this.mIsTimeGroup = GalleryPreferences.Album.getAlbumDetailTimeGroup(this.mAlbumId, true);
            }
        }
        AlbumDetailSortImmersionMenuHelper albumDetailSortImmersionMenuHelper = new AlbumDetailSortImmersionMenuHelper(this.mActivity, this.mCurrentSortBy, this.mSortOrder.equals(" ASC "));
        this.mSortImmersionMenuHelper = albumDetailSortImmersionMenuHelper;
        albumDetailSortImmersionMenuHelper.setOnClickItemListener(new AlbumDetailSortImmersionMenuHelper.OnItemClickListener() { // from class: com.miui.gallery.picker.PickAlbumDetailFragment.4
            {
                PickAlbumDetailFragment.this = this;
            }

            @Override // com.miui.gallery.ui.AlbumDetailSortImmersionMenuHelper.OnItemClickListener
            public void onItemClick(SortBy sortBy) {
                PickAlbumDetailFragment.this.stopAndHideScroller();
                PickAlbumDetailFragment.this.sortOnChange(sortBy);
            }
        });
        this.mAlbumDetailLoaderCallback = new AlbumDetailLoaderCallback();
        getLoaderManager().initLoader(1, null, this.mAlbumDetailLoaderCallback);
    }

    @Override // com.miui.gallery.app.fragment.GalleryFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("is_time_group", this.mIsTimeGroup);
        bundle.putString("sort_by", this.mCurrentSortBy.name());
        bundle.putString("sort_order", this.mSortOrder);
    }

    public final void restoreInstanceState(Bundle bundle) {
        this.mIsTimeGroup = bundle.getBoolean("is_time_group", true);
        this.mCurrentSortBy = SortBy.valueOf(bundle.getString("sort_by"));
        this.mSortOrder = bundle.getString("sort_order");
    }

    public void setAlbumId(long j) {
        this.mAlbumId = j;
    }

    public void setAlbumLocalPath(String str) {
        this.mAlbumLocalPath = str;
    }

    public void setIsCameraAlbum(String str) {
        this.mIsCameraAlbum = TextUtils.equals(str, String.valueOf(1L));
    }

    public void setIsOtherSharedAlbum(boolean z) {
        this.mIsOtherSharedAlbum = z;
    }

    public void setExtraSelection(String str) {
        this.mExtraSelection = str;
    }

    public void setIsScreenshotAlbum(boolean z) {
        this.mIsScreenshotAlbum = z;
    }

    public void setIsScreenRecorderAlbum(boolean z) {
        this.mIsScreenRecorderAlbum = z;
    }

    public final void configLoader(CursorLoader cursorLoader, SortBy sortBy) {
        cursorLoader.setUri(getUri(sortBy));
        cursorLoader.setProjection(AlbumDetailAdapter.PROJECTION);
        cursorLoader.setSelection(getSelection());
        cursorLoader.setSelectionArgs(getSelectionArgs());
        cursorLoader.setSortOrder(configOrderBy(sortBy));
    }

    public final Uri getUri(SortBy sortBy) {
        if (this.mIsOtherSharedAlbum) {
            return ContentUris.withAppendedId(GalleryContract.Media.URI_OTHER_ALBUM_MEDIA, this.mAlbumId);
        }
        if (isAllPhotosAlbum()) {
            return AlbumDetailAdapter.ALL_PHOTOS_PICKER_URI.buildUpon().appendQueryParameter("generate_headers", String.valueOf(true)).build();
        }
        return GalleryContract.Media.URI_OWNER_ALBUM_DETAIL_MEDIA.buildUpon().appendQueryParameter("generate_headers", String.valueOf(true)).appendQueryParameter("remove_processing_items", String.valueOf(true)).build();
    }

    public final boolean isOnlyShowLocal() {
        return GalleryPreferences.LocalMode.isOnlyShowLocalPhoto();
    }

    public boolean isQueryMediaTypeDetail() {
        return this.mAlbumId == 2147383645;
    }

    public String getSelection() {
        StringBuilder sb = new StringBuilder();
        long j = this.mAlbumId;
        if (j == 2147483647L) {
            sb.append("serverType");
            sb.append(" = ? AND ");
            sb.append("alias_hidden");
            sb.append(" = ? AND ");
            sb.append("alias_rubbish = ? AND ");
            sb.append("localGroupId != ?");
        } else if (j == 2147483642) {
            sb.append("alias_is_favorite");
            sb.append(" = ? AND ");
            sb.append("alias_hidden");
            sb.append(" = ? AND ");
            sb.append("localGroupId");
            sb.append(" != ? AND ");
            sb.append("alias_rubbish");
            sb.append(" = ? ");
            if (getPicker().getMediaType() != Picker.MediaType.ALL) {
                sb.append(" AND ");
                sb.append("serverType");
                sb.append(" = ?");
            }
        } else if (isAllPhotosAlbum()) {
            sb.append("sha1");
            sb.append(" NOT NULL AND ");
            sb.append("alias_hidden");
            sb.append(" = ? AND ");
            sb.append("alias_rubbish");
            sb.append(" = ? AND ");
            sb.append("localGroupId");
            sb.append(" != ?");
            if (getPicker().getMediaType() != Picker.MediaType.ALL) {
                sb.append(" AND ");
                sb.append("serverType");
                sb.append(" = ?");
                if (StringUtils.isValid(getPicker().getFilterMimeTypes())) {
                    sb.append(" AND ");
                    sb.append(getFilterSelectionWithMimeType(getPicker().getFilterMimeTypes()));
                }
            }
        } else if (this.mIsOtherSharedAlbum) {
            if (getPicker().getMediaType() == Picker.MediaType.ALL) {
                return null;
            }
            sb.append("serverType =? ");
            if (StringUtils.isValid(getPicker().getFilterMimeTypes())) {
                sb.append(" AND ");
                sb.append(getFilterSelectionWithMimeType(getPicker().getFilterMimeTypes()));
            }
        } else if (isVirtualScreenshotRecorderAlbum()) {
            sb.append("localGroupId IN ?");
            appendPickerMediaType(sb);
        } else if (isQueryMediaTypeDetail()) {
            if (!TextUtils.isEmpty(this.mExtraSelection)) {
                sb.append(this.mExtraSelection);
            }
        } else {
            sb.append("localGroupId = ? ");
            appendPickerMediaType(sb);
        }
        if (isOnlyShowLocal()) {
            sb.append(SELECTION_ONLY_LOCAL);
        }
        return sb.toString();
    }

    public final void appendPickerMediaType(StringBuilder sb) {
        if (getPicker().getMediaType() != Picker.MediaType.ALL) {
            sb.append(" AND ");
            sb.append("serverType =? ");
            if (!StringUtils.isValid(getPicker().getFilterMimeTypes())) {
                return;
            }
            sb.append(" AND ");
            sb.append(getFilterSelectionWithMimeType(getPicker().getFilterMimeTypes()));
        }
    }

    public String[] getSelectionArgs() {
        String valueOf;
        String valueOf2;
        String valueOf3;
        long j = this.mAlbumId;
        if (j == 2147483647L) {
            return new String[]{String.valueOf(2), String.valueOf(0), String.valueOf(0), String.valueOf(-1000L)};
        }
        if (this.mIsOtherSharedAlbum) {
            if (getPicker().getMediaType() == Picker.MediaType.IMAGE) {
                return new String[]{String.valueOf(1)};
            }
            if (getPicker().getMediaType() != Picker.MediaType.VIDEO) {
                return null;
            }
            return new String[]{String.valueOf(2)};
        } else if (j == 2147483642) {
            Picker.MediaType mediaType = getPicker().getMediaType();
            if (mediaType == Picker.MediaType.ALL) {
                return new String[]{String.valueOf(1), String.valueOf(0), String.valueOf(-1000L), String.valueOf(0)};
            }
            String[] strArr = new String[5];
            strArr[0] = String.valueOf(1);
            strArr[1] = String.valueOf(0);
            strArr[2] = String.valueOf(-1000L);
            strArr[3] = String.valueOf(0);
            if (mediaType == Picker.MediaType.IMAGE) {
                valueOf3 = String.valueOf(1);
            } else {
                valueOf3 = String.valueOf(2);
            }
            strArr[4] = valueOf3;
            return strArr;
        } else if (isAllPhotosAlbum()) {
            Picker.MediaType mediaType2 = getPicker().getMediaType();
            if (mediaType2 == Picker.MediaType.ALL) {
                return new String[]{String.valueOf(0), String.valueOf(0), String.valueOf(-1000L)};
            }
            String[] strArr2 = new String[4];
            strArr2[0] = String.valueOf(0);
            strArr2[1] = String.valueOf(0);
            strArr2[2] = String.valueOf(-1000L);
            if (mediaType2 == Picker.MediaType.IMAGE) {
                valueOf2 = String.valueOf(1);
            } else {
                valueOf2 = String.valueOf(2);
            }
            strArr2[3] = valueOf2;
            return strArr2;
        } else if (isVirtualScreenshotRecorderAlbum()) {
            Picker.MediaType mediaType3 = getPicker().getMediaType();
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            sb.append(AlbumCacheManager.getInstance().getScreenshotsAlbumId());
            sb.append(",");
            sb.append(AlbumCacheManager.getInstance().getScreenRecordersAlbumId());
            sb.append(")");
            if (mediaType3 == Picker.MediaType.ALL) {
                return new String[]{sb.toString()};
            }
            String[] strArr3 = new String[2];
            strArr3[0] = sb.toString();
            if (mediaType3 == Picker.MediaType.IMAGE) {
                valueOf = String.valueOf(1);
            } else {
                valueOf = String.valueOf(2);
            }
            strArr3[1] = valueOf;
            return strArr3;
        } else if (isQueryMediaTypeDetail()) {
            return null;
        } else {
            return getPicker().getMediaType() == Picker.MediaType.IMAGE ? new String[]{String.valueOf(this.mAlbumId), String.valueOf(1)} : getPicker().getMediaType() == Picker.MediaType.VIDEO ? new String[]{String.valueOf(this.mAlbumId), String.valueOf(2)} : new String[]{String.valueOf(this.mAlbumId)};
        }
    }

    public final void onSortByChanged() {
        getAdapter().setCurrentSortBy(this.mCurrentSortBy);
        getAdapter().setShowTimeLine(needShowTimeLine());
        Loader loader = getLoaderManager().getLoader(1);
        configLoader((CursorLoader) loader, this.mCurrentSortBy);
        loader.forceLoad();
    }

    public final String configOrderBy(SortBy sortBy) {
        String sortByString = getSortByString(sortBy);
        return sortByString + this.mSortOrder;
    }

    /* renamed from: com.miui.gallery.picker.PickAlbumDetailFragment$5 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass5 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$adapter$SortBy;

        static {
            int[] iArr = new int[SortBy.values().length];
            $SwitchMap$com$miui$gallery$adapter$SortBy = iArr;
            try {
                iArr[SortBy.UPDATE_DATE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$adapter$SortBy[SortBy.CREATE_DATE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$adapter$SortBy[SortBy.NAME.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$adapter$SortBy[SortBy.SIZE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    public final String getSortByString(SortBy sortBy) {
        int i = AnonymousClass5.$SwitchMap$com$miui$gallery$adapter$SortBy[sortBy.ordinal()];
        return i != 1 ? i != 2 ? i != 3 ? i != 4 ? "alias_sort_time" : MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE : "title" : "alias_create_time" : "dateModified";
    }

    public void initAlbumDetailSort() {
        switch (GalleryPreferences.Album.getAlbumDetailSort(this.mAlbumId, -1)) {
            case 0:
                this.mCurrentSortBy = SortBy.UPDATE_DATE;
                this.mSortOrder = " DESC ";
                return;
            case 1:
                this.mCurrentSortBy = SortBy.UPDATE_DATE;
                this.mSortOrder = " ASC ";
                return;
            case 2:
                this.mCurrentSortBy = SortBy.CREATE_DATE;
                this.mSortOrder = " DESC ";
                return;
            case 3:
                this.mCurrentSortBy = SortBy.CREATE_DATE;
                this.mSortOrder = " ASC ";
                return;
            case 4:
                this.mCurrentSortBy = SortBy.NAME;
                this.mSortOrder = " DESC ";
                return;
            case 5:
                this.mCurrentSortBy = SortBy.NAME;
                this.mSortOrder = " ASC ";
                return;
            case 6:
                this.mCurrentSortBy = SortBy.SIZE;
                this.mSortOrder = " DESC ";
                return;
            case 7:
                this.mCurrentSortBy = SortBy.SIZE;
                this.mSortOrder = " ASC ";
                return;
            default:
                this.mCurrentSortBy = getAlbumDetailDefaultSort();
                this.mSortOrder = " DESC ";
                return;
        }
    }

    public final SortBy getAlbumDetailDefaultSort() {
        if (this.mIsCameraAlbum || this.mIsScreenshotAlbum || this.mIsScreenRecorderAlbum || Album.isUserCreateAlbum(this.mAlbumLocalPath)) {
            return SortBy.CREATE_DATE;
        }
        return SortBy.UPDATE_DATE;
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        PickAlbumDetailAdapter pickAlbumDetailAdapter = this.mTimeLineAdapter;
        if (pickAlbumDetailAdapter != null) {
            pickAlbumDetailAdapter.swapCursor(null);
        }
        this.mSortImmersionMenuHelper.dismiss();
        super.onDestroy();
    }

    /* loaded from: classes2.dex */
    public class PickAlbumDetailAdapter extends AlbumDetailAdapter {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public PickAlbumDetailAdapter(Context context, Lifecycle lifecycle) {
            super(context, SyncStateDisplay$DisplayScene.SCENE_NONE, lifecycle);
            PickAlbumDetailFragment.this = r1;
            if (isAllPhotosAlbum()) {
                setAlbumType(AlbumType.ALL_PHOTOS);
            }
        }

        @Override // com.miui.gallery.adapter.AlbumDetailAdapter, com.miui.gallery.adapter.MultiViewMediaAdapter
        public AbsSingleImageViewHolder createSingleImageViewHolder(View view, Lifecycle lifecycle) {
            return new SingleImageViewHolder(view, lifecycle);
        }

        /* loaded from: classes2.dex */
        public class SingleImageViewHolder extends AlbumDetailAdapter.BaseSingleImageViewHolder {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public SingleImageViewHolder(View view, Lifecycle lifecycle) {
                super(view, lifecycle);
                PickAlbumDetailAdapter.this = r1;
            }

            @Override // com.miui.gallery.adapter.AlbumDetailAdapter.BaseSingleImageViewHolder, com.miui.gallery.widget.recyclerview.AbsViewHolder
            public void bindData(int i, int i2, List<Object> list) {
                super.bindData(i, i2, list);
                int packDataPosition = PickAlbumDetailAdapter.this.packDataPosition(i, i2);
                PickAlbumDetailAdapter pickAlbumDetailAdapter = PickAlbumDetailAdapter.this;
                PickAlbumDetailFragment.this.bindCheckState(this.mView, pickAlbumDetailAdapter.mo1558getItem(packDataPosition));
                MicroThumbGridItem microThumbGridItem = this.mView;
                PickAlbumDetailAdapter pickAlbumDetailAdapter2 = PickAlbumDetailAdapter.this;
                PickerItemHolder.bindView(packDataPosition, microThumbGridItem, pickAlbumDetailAdapter2, PickAlbumDetailFragment.this.mAlbumItemCheckListener);
            }
        }
    }

    public final boolean isAllPhotosAlbum() {
        return this.mAlbumId == 2147483644;
    }

    public final boolean isVirtualScreenshotRecorderAlbum() {
        return this.mAlbumId == 2147483645;
    }

    @Override // com.miui.gallery.picker.PickerFragment
    public boolean supportFoldBurstItems() {
        PickAlbumDetailAdapter pickAlbumDetailAdapter = this.mTimeLineAdapter;
        if (pickAlbumDetailAdapter != null) {
            return pickAlbumDetailAdapter.supportFoldBurstItems();
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
        return this.mTimeLineAdapter.packDataPosition(packedPositionGroup, packedPositionChild);
    }
}
