package com.miui.gallery.ui;

import android.content.res.Configuration;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.R;
import com.miui.gallery.activity.CleanerPhotoPickActivity;
import com.miui.gallery.adapter.AlbumType;
import com.miui.gallery.adapter.PickCleanerPhotoAdapter;
import com.miui.gallery.adapter.PickSimilarPhotoAdapter;
import com.miui.gallery.adapter.SortBy;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.app.screenChange.IScreenChange;
import com.miui.gallery.cleaner.BaseScanner;
import com.miui.gallery.cleaner.ScanResult;
import com.miui.gallery.cleaner.ScannerManager;
import com.miui.gallery.cleaner.SimilarScanner;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.CleanerPhotoPickFragment;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.widget.LoadMoreLayout;
import com.miui.gallery.widget.editwrapper.EditableListViewWrapper;
import com.miui.gallery.widget.editwrapper.HeaderFooterWrapper;
import com.miui.gallery.widget.editwrapper.SimpleMultiChoiceModeListener;
import com.miui.gallery.widget.recyclerview.EditableListSpanSizeProvider;
import com.miui.gallery.widget.recyclerview.FastScrollerBar;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.miui.gallery.widget.recyclerview.GridItemSpacingDecoration;
import com.miui.gallery.widget.recyclerview.IrregularSpanSizeLookup;
import com.miui.gallery.widget.recyclerview.SimpleHeaderFooterWrapperAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import miui.gallery.support.MiuiSdkCompat;
import miuix.slidingwidget.widget.SlidingButton;

/* loaded from: classes2.dex */
public class SimilarPhotoPickFragment extends CleanerPhotoPickFragment {
    public ArrayList<Long> mClusterGroupId;
    public long mDataRefreshTime;
    public RecyclerView.AdapterDataObserver mDataSetObserver;
    public ArrayList<Integer> mGroupItemCount;
    public ArrayList<Integer> mGroupStartPos;
    public boolean mIsScrolling;
    public View mKeepBestPhotoLl;
    public SlidingButton mKeepClearCheckBox;
    public CompoundButton.OnCheckedChangeListener mKeepClearCheckListener;
    public LoadMoreLayout mLoadMoreLayout;
    public SimpleMultiChoiceModeListener mMultiChoiceModeListener;
    public boolean mNeedResetLoader;
    public BaseScanner.OnScanResultUpdateListener mOnScanResultUpdateListener;
    public RecyclerView.OnScrollListener mOnScrollListener;
    public SimilarPhotoPickLoaderCallback mSimilarPhotoPickLoaderCallbacks;
    public Runnable sRunnable;

    public static /* synthetic */ void $r8$lambda$s7DyaL4RwhNsKMmf5HHu6CHhdZ0(SimilarPhotoPickFragment similarPhotoPickFragment, int i) {
        similarPhotoPickFragment.lambda$onInflateView$1(i);
    }

    public static /* synthetic */ void $r8$lambda$t0eazx9J028IjAk8gHDoJdmFIQA(SimilarPhotoPickFragment similarPhotoPickFragment, Configuration configuration) {
        similarPhotoPickFragment.lambda$onInflateView$2(configuration);
    }

    public static /* synthetic */ AbstractHeaderFooterWrapperAdapter $r8$lambda$z5e5UdjXQACcKnHJ8STUttIeckI(SimilarPhotoPickFragment similarPhotoPickFragment, RecyclerView.Adapter adapter) {
        return similarPhotoPickFragment.lambda$onInflateView$0(adapter);
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public int getLayoutSource() {
        return R.layout.similar_photo_pick_layout;
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return "cleaner_similar_photo_pick";
    }

    @Override // com.miui.gallery.ui.CleanerPhotoPickFragment
    public boolean onKeyShortcut(int i, KeyEvent keyEvent) {
        return false;
    }

    @Override // com.miui.gallery.ui.CleanerPhotoPickFragment
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> list, Menu menu, int i) {
    }

    @Override // com.miui.gallery.ui.CleanerPhotoPickFragment
    public void recordCancelSelectAllEvent() {
    }

    @Override // com.miui.gallery.ui.CleanerPhotoPickFragment
    public void recordSelectAllEvent() {
    }

    @Override // com.miui.gallery.ui.CleanerPhotoPickFragment
    public void recordSelectGroupEvent() {
    }

    public SimilarPhotoPickFragment() {
        super(3);
        this.mIsScrolling = false;
        this.mDataSetObserver = new RecyclerView.AdapterDataObserver() { // from class: com.miui.gallery.ui.SimilarPhotoPickFragment.1
            {
                SimilarPhotoPickFragment.this = this;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
            public void onChanged() {
                SimilarPhotoPickFragment similarPhotoPickFragment = SimilarPhotoPickFragment.this;
                if (!similarPhotoPickFragment.mIsFirstLoadFinish || similarPhotoPickFragment.mo1564getAdapter().getItemCount() <= 0) {
                    return;
                }
                SimilarPhotoPickFragment similarPhotoPickFragment2 = SimilarPhotoPickFragment.this;
                similarPhotoPickFragment2.mIsFirstLoadFinish = false;
                if (!similarPhotoPickFragment2.mKeepClearCheckBox.isChecked()) {
                    return;
                }
                SimilarPhotoPickFragment.this.keepClearPhotos();
            }
        };
        this.mMultiChoiceModeListener = new SimpleMultiChoiceModeListener() { // from class: com.miui.gallery.ui.SimilarPhotoPickFragment.2
            {
                SimilarPhotoPickFragment.this = this;
            }

            @Override // com.miui.gallery.widget.editwrapper.MultiChoiceModeListener
            public void onAllItemsCheckedStateChanged(ActionMode actionMode, boolean z) {
                SimilarPhotoPickFragment.this.onItemSelectedChanged();
            }

            @Override // com.miui.gallery.widget.editwrapper.MultiChoiceModeListener
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long j, boolean z) {
                SimilarPhotoPickFragment.this.onItemSelectedChanged();
            }
        };
        this.mKeepClearCheckListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.miui.gallery.ui.SimilarPhotoPickFragment.3
            {
                SimilarPhotoPickFragment.this = this;
            }

            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    SimilarPhotoPickFragment.this.keepClearPhotos();
                } else {
                    SimilarPhotoPickFragment.this.mEditableWrapper.setAllItemsCheckState(false);
                }
                if (!z) {
                    SamplingStatHelper.recordCountEvent("cleaner", "similar_keep_clear_cancel");
                }
                TrackController.trackClick("403.27.6.1.11334", AutoTracking.getRef(), z ? "true" : "false");
            }
        };
        this.mOnScrollListener = new RecyclerView.OnScrollListener() { // from class: com.miui.gallery.ui.SimilarPhotoPickFragment.4
            {
                SimilarPhotoPickFragment.this = this;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                if (SimilarPhotoPickFragment.this.getActivity() == null) {
                    return;
                }
                if (i == 0) {
                    SimilarPhotoPickFragment.this.mIsScrolling = false;
                    if (!SimilarPhotoPickFragment.this.mNeedResetLoader) {
                        return;
                    }
                    SimilarPhotoPickFragment.this.postRestartLoader(true);
                    return;
                }
                SimilarPhotoPickFragment.this.mIsScrolling = true;
            }
        };
        this.mOnScanResultUpdateListener = new BaseScanner.OnScanResultUpdateListener() { // from class: com.miui.gallery.ui.SimilarPhotoPickFragment.5
            {
                SimilarPhotoPickFragment.this = this;
            }

            @Override // com.miui.gallery.cleaner.BaseScanner.OnScanResultUpdateListener
            public void onUpdate(int i, long j, ScanResult scanResult) {
                if (SimilarPhotoPickFragment.this.getActivity() != null && i == 3) {
                    SimilarPhotoPickFragment.this.postRestartLoader(true);
                }
            }
        };
        this.sRunnable = new Runnable() { // from class: com.miui.gallery.ui.SimilarPhotoPickFragment.6
            {
                SimilarPhotoPickFragment.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (SimilarPhotoPickFragment.this.mIsScrolling) {
                    SimilarPhotoPickFragment.this.mNeedResetLoader = true;
                } else {
                    SimilarPhotoPickFragment.this.restartLoader();
                }
            }
        };
    }

    @Override // com.miui.gallery.ui.CleanerPhotoPickFragment, com.miui.gallery.ui.PhotoListFragmentBase, miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(getLayoutSource(), viewGroup, false);
        this.mKeepBestPhotoLl = inflate.findViewById(R.id.keep_best_photo);
        Button button = (Button) inflate.findViewById(R.id.delete);
        this.mDeleteButton = button;
        button.setOnClickListener(this.mDeleteButtonClickListener);
        this.mDeleteButton.setEnabled(false);
        Button selectAllButton = ((CleanerPhotoPickActivity) this.mActivity).getSelectAllButton();
        this.mSelectButton = selectAllButton;
        MiuiSdkCompat.setEditActionModeButton(this.mActivity, selectAllButton, 0);
        this.mSelectButton.setOnClickListener(this.mSelectListener);
        SlidingButton slidingButton = (SlidingButton) inflate.findViewById(R.id.keep_clear_check_box);
        this.mKeepClearCheckBox = slidingButton;
        slidingButton.setOnPerformCheckedChangeListener(this.mKeepClearCheckListener);
        this.mKeepClearCheckBox.setEnabled(true);
        this.mKeepClearCheckBox.setChecked(false);
        this.mRecyclerView = (GalleryRecyclerView) inflate.findViewById(R.id.grid);
        this.mSpacing = getResources().getDimensionPixelSize(R.dimen.micro_thumb_vertical_spacing);
        this.mColumns = Config$ThumbConfig.get().sMicroThumbColumnsPortrait;
        GridItemSpacingDecoration gridItemSpacingDecoration = new GridItemSpacingDecoration(this.mRecyclerView, false, getResources().getDimensionPixelSize(R.dimen.micro_thumb_horizontal_spacing), this.mSpacing);
        this.mSpacingDecoration = gridItemSpacingDecoration;
        this.mRecyclerView.addItemDecoration(gridItemSpacingDecoration);
        setEmptyViewVisibility(8);
        PickCleanerPhotoAdapter mo1564getAdapter = mo1564getAdapter();
        mo1564getAdapter.setCurrentSortBy(SortBy.CREATE_DATE);
        mo1564getAdapter.setAlbumType(AlbumType.NORMAL);
        mo1564getAdapter.registerAdapterDataObserver(this.mDataSetObserver);
        this.mEditableWrapper = new EditableListViewWrapper(this.mRecyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.mActivity, this.mColumns);
        gridLayoutManager.setSpanSizeLookup(IrregularSpanSizeLookup.create(new EditableListSpanSizeProvider(this.mEditableWrapper, gridLayoutManager)));
        this.mEditableWrapper.setHandleTouchAnimItemType(AlbumDetailGridItem.class.getSimpleName());
        this.mEditableWrapper.setLayoutManager(gridLayoutManager);
        this.mLoadMoreLayout = (LoadMoreLayout) layoutInflater.inflate(R.layout.load_more_layout, (ViewGroup) this.mRecyclerView, false);
        this.mEditableWrapper.setAdapter(mo1564getAdapter, new HeaderFooterWrapper() { // from class: com.miui.gallery.ui.SimilarPhotoPickFragment$$ExternalSyntheticLambda1
            @Override // com.miui.gallery.widget.editwrapper.HeaderFooterWrapper
            public final AbstractHeaderFooterWrapperAdapter wrap(RecyclerView.Adapter adapter) {
                return SimilarPhotoPickFragment.$r8$lambda$z5e5UdjXQACcKnHJ8STUttIeckI(SimilarPhotoPickFragment.this, adapter);
            }
        });
        this.mEditableWrapper.setOnItemClickListener(getGridViewOnItemClickListener());
        this.mEditableWrapper.setMultiChoiceModeListener(this.mMultiChoiceModeListener);
        this.mEditableWrapper.disableScaleImageViewAniWhenInActionMode();
        this.mEditableWrapper.startChoiceMode();
        this.mScanner = ScannerManager.getInstance().getScanner(this.mType);
        this.mActivity.getAppCompatActionBar().setTitle(R.string.cleaner_similar_title);
        ((CleanerPhotoPickActivity) this.mActivity).getSelectAllButton().setVisibility(8);
        this.mRecyclerView.addOnScrollListener(this.mOnScrollListener);
        this.mRecyclerView.setFastScrollEnabled(true);
        this.mRecyclerView.setOnFastScrollerStateChangedListener(new FastScrollerBar.OnStateChangedListener() { // from class: com.miui.gallery.ui.SimilarPhotoPickFragment$$ExternalSyntheticLambda2
            @Override // com.miui.gallery.widget.recyclerview.FastScrollerBar.OnStateChangedListener
            public final void onStateChanged(int i) {
                SimilarPhotoPickFragment.$r8$lambda$s7DyaL4RwhNsKMmf5HHu6CHhdZ0(SimilarPhotoPickFragment.this, i);
            }
        });
        this.mRecyclerView.setFastScrollerTopMargin(this.mFastScrollerMarginTop);
        this.mScanner.addListener(this.mOnScanResultUpdateListener);
        addScreenChangeListener(new IScreenChange.OnScreenLayoutSizeChangeListener() { // from class: com.miui.gallery.ui.SimilarPhotoPickFragment$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.app.screenChange.IScreenChange.OnScreenLayoutSizeChangeListener
            public final void onScreenLayoutSizeChange(Configuration configuration) {
                SimilarPhotoPickFragment.$r8$lambda$t0eazx9J028IjAk8gHDoJdmFIQA(SimilarPhotoPickFragment.this, configuration);
            }
        });
        resetScanResult();
        updateConfiguration();
        return inflate;
    }

    public /* synthetic */ AbstractHeaderFooterWrapperAdapter lambda$onInflateView$0(RecyclerView.Adapter adapter) {
        return new SimpleHeaderFooterWrapperAdapter(adapter, null, Collections.singletonList(this.mLoadMoreLayout));
    }

    public /* synthetic */ void lambda$onInflateView$1(int i) {
        EditableListViewWrapper editableListViewWrapper = this.mEditableWrapper;
        if (editableListViewWrapper != null) {
            editableListViewWrapper.reductionTouchView();
        }
    }

    public /* synthetic */ void lambda$onInflateView$2(Configuration configuration) {
        int dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.similar_photo_pick_bar_margin_horizontal);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.mKeepBestPhotoLl.getLayoutParams();
        layoutParams.setMarginStart(dimensionPixelOffset);
        layoutParams.setMarginEnd(dimensionPixelOffset);
    }

    @Override // com.miui.gallery.ui.CleanerPhotoPickFragment
    public void resetCheckState() {
        super.resetCheckState();
        this.mKeepClearCheckBox.setChecked(false);
    }

    @Override // com.miui.gallery.ui.CleanerPhotoPickFragment
    public void recordDeleteEvent(int i) {
        TrackController.trackClick("403.27.6.1.11333", AutoTracking.getRef(), i);
    }

    @Override // com.miui.gallery.ui.CleanerPhotoPickFragment
    public void resetScanResult() {
        super.resetScanResult();
        this.mGroupStartPos = ((SimilarScanner) this.mScanner).getGroupStartPos();
        this.mGroupItemCount = ((SimilarScanner) this.mScanner).getGroupItemCount();
        this.mClusterGroupId = ((SimilarScanner) this.mScanner).getClusterGroupId();
    }

    @Override // com.miui.gallery.ui.CleanerPhotoPickFragment
    public void onItemSelectedChanged() {
        boolean z = false;
        if (mo1564getAdapter().getItemCount() > 0) {
            Button button = this.mDeleteButton;
            if (this.mEditableWrapper.getCheckedItemCount() > 0) {
                z = true;
            }
            button.setEnabled(z);
            return;
        }
        this.mDeleteButton.setEnabled(false);
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, com.miui.gallery.ui.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        if (this.mIsScrolling || !this.mNeedResetLoader) {
            return;
        }
        postRestartLoader(false);
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        ThreadManager.getMainHandler().removeCallbacks(this.sRunnable);
        ((SimilarScanner) this.mScanner).removeSingleItemGroups();
        this.mScanner.removeListener(this.mOnScanResultUpdateListener);
    }

    @Override // com.miui.gallery.ui.CleanerPhotoPickFragment, com.miui.gallery.ui.PhotoListFragmentBase, com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (configuration.orientation == 2) {
            this.mColumns = getResources().getInteger(R.integer.thumbnail_similar_photo_pick_columns_land);
        } else {
            this.mColumns = getResources().getInteger(R.integer.thumbnail_similar_photo_pick_columns);
        }
        this.mRecyclerView.scrollToPosition(this.mRecyclerView.findFirstVisibleItemPosition());
        updateConfiguration();
    }

    private void updateConfiguration() {
        mo1564getAdapter().setSpanCount(this.mColumns);
        mo1564getAdapter().setSpacing(this.mSpacing);
    }

    public final void postRestartLoader(boolean z) {
        if (System.currentTimeMillis() - this.mDataRefreshTime >= 5000) {
            z = false;
        }
        ThreadManager.getMainHandler().removeCallbacks(this.sRunnable);
        ThreadManager.getMainHandler().postDelayed(this.sRunnable, z ? 2000 : 0);
        this.mNeedResetLoader = false;
    }

    public final void restartLoader() {
        Loader loader;
        resetScanResult();
        if (isAdded() && (loader = getLoaderManager().getLoader(1)) != null) {
            configLoader((CursorLoader) loader);
            loader.forceLoad();
        }
        this.mDataRefreshTime = System.currentTimeMillis();
        this.mNeedResetLoader = false;
    }

    @Override // com.miui.gallery.ui.CleanerPhotoPickFragment, com.miui.gallery.ui.PhotoListFragmentBase
    /* renamed from: getAdapter */
    public PickCleanerPhotoAdapter mo1564getAdapter() {
        if (this.mAdapter == null) {
            this.mAdapter = new PickSimilarPhotoAdapter(this.mActivity, this.mRecyclerView, getLifecycle());
        }
        return this.mAdapter;
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public LoaderManager.LoaderCallbacks getLoaderCallback() {
        if (this.mSimilarPhotoPickLoaderCallbacks == null) {
            this.mSimilarPhotoPickLoaderCallbacks = new SimilarPhotoPickLoaderCallback();
        }
        this.mDataRefreshTime = System.currentTimeMillis();
        return this.mSimilarPhotoPickLoaderCallbacks;
    }

    @Override // com.miui.gallery.ui.CleanerPhotoPickFragment
    public CleanerPhotoPickFragment.DeleteMessage getDeleteMessage() {
        return new CleanerPhotoPickFragment.DeleteMessage.Builder().setReason(48).setCleanerSubEvent("cleaner_similar_used").build();
    }

    /* loaded from: classes2.dex */
    public class SimilarPhotoPickLoaderCallback implements LoaderManager.LoaderCallbacks {
        public CursorLoader mLoader;

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader loader) {
        }

        public SimilarPhotoPickLoaderCallback() {
            SimilarPhotoPickFragment.this = r1;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader onCreateLoader(int i, Bundle bundle) {
            CursorLoader cursorLoader = new CursorLoader(SimilarPhotoPickFragment.this.mActivity);
            this.mLoader = cursorLoader;
            SimilarPhotoPickFragment.this.configLoader(cursorLoader);
            return this.mLoader;
        }

        /* JADX WARN: Removed duplicated region for block: B:107:0x0134  */
        /* JADX WARN: Removed duplicated region for block: B:108:0x013a  */
        /* JADX WARN: Removed duplicated region for block: B:111:0x014b  */
        /* JADX WARN: Removed duplicated region for block: B:112:0x0155  */
        /* JADX WARN: Removed duplicated region for block: B:74:0x0058  */
        /* JADX WARN: Removed duplicated region for block: B:98:0x00e4  */
        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void onLoadFinished(androidx.loader.content.Loader r13, java.lang.Object r14) {
            /*
                Method dump skipped, instructions count: 351
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.ui.SimilarPhotoPickFragment.SimilarPhotoPickLoaderCallback.onLoadFinished(androidx.loader.content.Loader, java.lang.Object):void");
        }
    }

    public final void keepClearPhotos() {
        PickSimilarPhotoAdapter pickSimilarPhotoAdapter = (PickSimilarPhotoAdapter) mo1564getAdapter();
        List<Integer> headerPositions = pickSimilarPhotoAdapter.getHeaderPositions();
        if (BaseMiscUtil.isValid(headerPositions)) {
            for (int i = 0; i < headerPositions.size(); i++) {
                int intValue = headerPositions.get(i).intValue();
                int childCount = pickSimilarPhotoAdapter.getChildCount(i);
                this.mEditableWrapper.setItemChecked(intValue, false);
                for (int i2 = 1; i2 < childCount; i2++) {
                    this.mEditableWrapper.setItemChecked(intValue + i2, true);
                }
            }
        }
        onItemSelectedChanged();
    }

    /* loaded from: classes2.dex */
    public static class SortCursor extends CursorWrapper {
        public int mPos;
        public ArrayList<Long> mResults;
        public int[] sortList;

        public SortCursor(Cursor cursor, ArrayList<Long> arrayList) {
            super(cursor);
            this.mPos = 0;
            this.sortList = new int[getCount()];
            this.mResults = arrayList;
            initSortList();
        }

        public final void initSortList() {
            Cursor wrappedCursor = getWrappedCursor();
            for (int i = 0; i < getCount(); i++) {
                if (wrappedCursor.moveToPosition(i)) {
                    long j = wrappedCursor.getLong(0);
                    int i2 = 0;
                    while (true) {
                        if (i2 < this.mResults.size() && i2 < getCount()) {
                            if (j == this.mResults.get(i2).longValue()) {
                                this.sortList[i2] = i;
                                break;
                            }
                            i2++;
                        }
                    }
                }
            }
        }

        @Override // android.database.CursorWrapper, android.database.Cursor
        public boolean moveToPosition(int i) {
            if (i >= 0 && i < this.sortList.length) {
                this.mPos = i;
                return getWrappedCursor().moveToPosition(this.sortList[i]);
            } else if (i < 0) {
                this.mPos = -1;
                return getWrappedCursor().moveToPosition(-1);
            } else {
                int[] iArr = this.sortList;
                if (i < iArr.length) {
                    return false;
                }
                this.mPos = iArr.length;
                return getWrappedCursor().moveToPosition(getWrappedCursor().getCount());
            }
        }

        @Override // android.database.CursorWrapper, android.database.Cursor
        public boolean moveToFirst() {
            return moveToPosition(0);
        }

        @Override // android.database.CursorWrapper, android.database.Cursor
        public boolean moveToLast() {
            return moveToPosition(getCount() - 1);
        }

        @Override // android.database.CursorWrapper, android.database.Cursor
        public boolean moveToNext() {
            return moveToPosition(this.mPos + 1);
        }

        @Override // android.database.CursorWrapper, android.database.Cursor
        public boolean moveToPrevious() {
            return moveToPosition(this.mPos - 1);
        }

        @Override // android.database.CursorWrapper, android.database.Cursor
        public boolean move(int i) {
            return moveToPosition(this.mPos + i);
        }

        @Override // android.database.CursorWrapper, android.database.Cursor
        public int getPosition() {
            return this.mPos;
        }
    }
}
