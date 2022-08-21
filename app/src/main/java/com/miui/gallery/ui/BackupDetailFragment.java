package com.miui.gallery.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.MatrixCursor;
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
import androidx.recyclerview.widget.GalleryGridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ch.qos.logback.core.util.FileSize;
import com.google.common.collect.Lists;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.R;
import com.miui.gallery.activity.InternalPhotoPageActivity;
import com.miui.gallery.adapter.AlbumDetailAdapter;
import com.miui.gallery.adapter.AlbumType;
import com.miui.gallery.adapter.SortBy;
import com.miui.gallery.adapter.SyncStateDisplay$DisplayScene;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.cloud.syncstate.OnSyncStateChangeObserver;
import com.miui.gallery.cloud.syncstate.SyncStateInfo;
import com.miui.gallery.cloud.syncstate.SyncStateManager;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.sdk.SyncStatus;
import com.miui.gallery.sdk.uploadstatus.ItemType;
import com.miui.gallery.sdk.uploadstatus.SyncProxy;
import com.miui.gallery.sdk.uploadstatus.UploadStatusItem;
import com.miui.gallery.sdk.uploadstatus.UploadStatusProxy;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.photoPage.EnterTypeUtils;
import com.miui.gallery.util.PhotoPageIntent;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.miui.gallery.widget.recyclerview.GridItemSpacingDecoration;
import com.miui.gallery.widget.recyclerview.IrregularSpanSizeLookup;
import com.miui.gallery.widget.recyclerview.ItemClickSupport;
import com.miui.gallery.widget.recyclerview.SpanSizeProvider;
import com.miui.gallery.widget.recyclerview.grouped.GroupedItemManager;
import com.miui.gallery.widget.tsd.DrawerState;
import com.miui.gallery.widget.tsd.INestedTwoStageDrawer;
import com.miui.gallery.widget.tsd.InestedScrollerStateListener;
import com.xiaomi.stat.MiStat;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes2.dex */
public class BackupDetailFragment extends PhotoListFragmentBase<AlbumDetailAdapter> implements UploadStatusProxy.UploadStatusChangedListener, OnSyncStateChangeObserver {
    public BackupTitle mBackUpTitle;
    public BackupDetailAdapter mBackupDetailAdapter;
    public GroupedItemManager mGroupedItemManager;
    public boolean mIsFirstLoaded;
    public LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback;
    public List<String> mUploadedItems = Lists.newArrayList();

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public int getLayoutSource() {
        return R.layout.backup_detail;
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return "backup_detail";
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public String[] getSelectionArgs() {
        return null;
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public long getSupportOperationMask() {
        return FileSize.KB_COEFFICIENT;
    }

    public final int getSyncStateDisplayOptions() {
        return 12;
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public void onDataLoaded(int i) {
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase, miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        final View onInflateView = super.onInflateView(layoutInflater, viewGroup, bundle);
        ViewGroup viewGroup2 = (ViewGroup) onInflateView.findViewById(R.id.backup_bar);
        BackupTitle backupTitle = (BackupTitle) LayoutInflater.from(this.mActivity).inflate(R.layout.backup_title, viewGroup2, false);
        this.mBackUpTitle = backupTitle;
        viewGroup2.addView(backupTitle);
        BackupDetailAdapter backupDetailAdapter = new BackupDetailAdapter(this.mActivity, SyncStateDisplay$DisplayScene.SCENE_ALWAYS, getSyncStateDisplayOptions(), getLifecycle());
        this.mBackupDetailAdapter = backupDetailAdapter;
        backupDetailAdapter.setCurrentSortBy(SortBy.CREATE_DATE);
        this.mBackupDetailAdapter.setAlbumType(AlbumType.NORMAL);
        this.mRecyclerView.setOnItemClickListener(getGridViewOnItemClickListener());
        final GalleryGridLayoutManager galleryGridLayoutManager = new GalleryGridLayoutManager(this.mActivity, Config$ThumbConfig.get().sMicroThumbColumnsPortrait);
        this.mRecyclerView.setLayoutManager(galleryGridLayoutManager);
        galleryGridLayoutManager.setSpanSizeLookup(IrregularSpanSizeLookup.create(new SpanSizeProvider() { // from class: com.miui.gallery.ui.BackupDetailFragment.1
            @Override // com.miui.gallery.widget.recyclerview.SpanSizeProvider
            public int getSpanSize(int i) {
                if (GroupedItemManager.getPackedPositionChild(BackupDetailFragment.this.mGroupedItemManager.getExpandablePosition(i)) == -1) {
                    return galleryGridLayoutManager.getSpanCount();
                }
                return 1;
            }

            @Override // com.miui.gallery.widget.recyclerview.SpanSizeProvider
            public int getSpanIndex(int i, int i2) {
                int packedPositionChild = GroupedItemManager.getPackedPositionChild(BackupDetailFragment.this.mGroupedItemManager.getExpandablePosition(i));
                if (packedPositionChild == -1) {
                    return 0;
                }
                return packedPositionChild % i2;
            }
        }));
        GroupedItemManager groupedItemManager = new GroupedItemManager();
        this.mGroupedItemManager = groupedItemManager;
        this.mRecyclerView.setAdapter(groupedItemManager.createWrappedAdapter(this.mBackupDetailAdapter));
        this.mSpacing = getResources().getDimensionPixelSize(R.dimen.micro_thumb_vertical_spacing);
        GalleryRecyclerView galleryRecyclerView = this.mRecyclerView;
        galleryRecyclerView.addItemDecoration(new GridItemSpacingDecoration(galleryRecyclerView, false, getResources().getDimensionPixelSize(R.dimen.micro_thumb_horizontal_spacing), this.mSpacing));
        this.mRecyclerView.setScrollingCalculator(this.mBackupDetailAdapter);
        this.mRecyclerView.setFastScrollEnabled(true);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.fast_scroller_margin_top_to_time_line);
        this.mFastScrollerMarginTop = dimensionPixelSize;
        this.mRecyclerView.setFastScrollerTopMargin(dimensionPixelSize);
        onInflateView.post(new Runnable() { // from class: com.miui.gallery.ui.BackupDetailFragment.2
            @Override // java.lang.Runnable
            public void run() {
                ((INestedTwoStageDrawer) onInflateView).setDrawerState(DrawerState.OPEN, false, null);
            }
        });
        ((INestedTwoStageDrawer) onInflateView).addScrollerStateListener(new InestedScrollerStateListener() { // from class: com.miui.gallery.ui.BackupDetailFragment.3
            @Override // com.miui.gallery.widget.tsd.InestedScrollerStateListener
            public void onScrollerUpdate(DrawerState drawerState, int i, int i2) {
            }

            @Override // com.miui.gallery.widget.tsd.InestedScrollerStateListener
            public void onScrollerStateChanged(DrawerState drawerState, int i) {
                if (drawerState != DrawerState.CLOSE) {
                    BackupDetailFragment.this.mRecyclerView.hideScrollerBar();
                }
            }
        });
        onConfigurationChanged(getResources().getConfiguration());
        return onInflateView;
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase, com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mBackupDetailAdapter.setSpanCount(this.mColumns);
        this.mBackupDetailAdapter.setSpacing(this.mSpacing);
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, com.miui.gallery.ui.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        SyncStateManager.getInstance().registerSyncStateObserver(this.mActivity, this);
        HashMap hashMap = new HashMap();
        hashMap.put("tip", "403.28.2.1.11289");
        hashMap.put("ref_tip", AutoTracking.getRef());
        BackupDetailAdapter backupDetailAdapter = this.mBackupDetailAdapter;
        hashMap.put(MiStat.Param.COUNT, String.valueOf(backupDetailAdapter == null ? 0 : backupDetailAdapter.getItemCount()));
        BackupTitle backupTitle = this.mBackUpTitle;
        if (backupTitle != null && !backupTitle.isNormalSyncStatus(backupTitle.getSyncState())) {
            hashMap.put("error", this.mBackUpTitle.getSyncState().name());
        }
        AutoTracking.trackView(hashMap);
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, com.miui.gallery.ui.BaseFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        SyncStateManager.getInstance().unregisterSyncStateObserver(this.mActivity, this);
    }

    @Override // com.miui.gallery.cloud.syncstate.OnSyncStateChangeObserver
    public void onSyncStateChanged(SyncStateInfo syncStateInfo) {
        SyncStateManager.getInstance().trackSyncStateChanged(syncStateInfo);
        this.mBackUpTitle.refreshSyncState(syncStateInfo);
    }

    @Override // com.miui.gallery.ui.BaseFragment, androidx.fragment.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        SyncProxy.getInstance().getUploadStatusProxy().addUploadStatusChangedListener(this);
    }

    @Override // com.miui.gallery.ui.BaseFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        SyncProxy.getInstance().getUploadStatusProxy().removeUploadStatusChangedListener(this);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    /* renamed from: getAdapter */
    public AlbumDetailAdapter mo1564getAdapter() {
        return this.mBackupDetailAdapter;
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public Uri getUri() {
        if (this.mIsFirstLoaded) {
            return GalleryContract.Media.URI_ALL;
        }
        return GalleryContract.Media.URI;
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public String getSelection() {
        return "alias_sync_state = 3 OR alias_sync_state = 2 OR (alias_sync_state = 0 AND _id IN (" + TextUtils.join(",", this.mUploadedItems) + "))";
    }

    @Override // com.miui.gallery.sdk.uploadstatus.UploadStatusProxy.UploadStatusChangedListener
    public void onUploadStatusChanged(UploadStatusItem uploadStatusItem) {
        if (uploadStatusItem == null || uploadStatusItem.mStatus != SyncStatus.STATUS_SUCCESS) {
            return;
        }
        if (uploadStatusItem.getItemType() != ItemType.OWNER && uploadStatusItem.getItemType() != ItemType.SHARER) {
            return;
        }
        refreshOnUploadStatusChanged(uploadStatusItem);
    }

    public final void refreshOnUploadStatusChanged(final UploadStatusItem uploadStatusItem) {
        ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.ui.BackupDetailFragment.4
            @Override // java.lang.Runnable
            public void run() {
                Loader loader;
                if (!BackupDetailFragment.this.mIsFirstLoaded) {
                    return;
                }
                if (uploadStatusItem != null) {
                    String localId = SyncProxy.getInstance().getUriAdapter().getLocalId(uploadStatusItem.getUserUri());
                    if (uploadStatusItem.getItemType() == ItemType.OWNER) {
                        BackupDetailFragment.this.mUploadedItems.add(localId);
                    } else {
                        BackupDetailFragment.this.mUploadedItems.add(String.valueOf(Long.parseLong(localId) + 16777215));
                    }
                }
                if (!BackupDetailFragment.this.isAdded() || (loader = LoaderManager.getInstance(BackupDetailFragment.this).getLoader(1)) == null) {
                    return;
                }
                BackupDetailFragment.this.configLoader((CursorLoader) loader);
                loader.onContentChanged();
            }
        });
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public LoaderManager.LoaderCallbacks<Cursor> getLoaderCallback() {
        if (this.mLoaderCallback == null) {
            this.mLoaderCallback = new BackupLoaderCallBack();
        }
        return this.mLoaderCallback;
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public String[] getProjection() {
        return BackupDetailAdapter.PROJECTION;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i2 == -1 && i == 36) {
            new PhotoPageIntent.Builder(this, InternalPhotoPageActivity.class).setUri(getUri()).setCount(this.mBackupDetailAdapter.getSecretCount()).setSelection("(" + getSelection() + ") AND (localGroupId = -1000)").setSelectionArgs(getSelectionArgs()).setOrderBy(getCurrentSortOrder()).setAlbumId(this.mAlbumId).setAlbumName(this.mAlbumName).setOperationMask(getSupportOperationMask()).setUnfoldBurst(!mo1564getAdapter().supportFoldBurstItems()).build().gotoPhotoPage();
            HashMap hashMap = new HashMap();
            hashMap.put("from", getPageName());
            hashMap.put("position", 0);
            SamplingStatHelper.recordCountEvent("photo", "click_micro_thumb", hashMap);
        }
    }

    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public ItemClickSupport.OnItemClickListener getGridViewOnItemClickListener() {
        return new ItemClickSupport.OnItemClickListener() { // from class: com.miui.gallery.ui.BackupDetailFragment.5
            @Override // com.miui.gallery.widget.recyclerview.ItemClickSupport.OnItemClickListener
            public boolean onItemClick(RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
                int unwrapPosition = BackupDetailFragment.this.unwrapPosition(i);
                int i2 = 0;
                if (unwrapPosition == -1) {
                    return false;
                }
                if (BackupDetailFragment.this.mBackupDetailAdapter.getItemViewType(unwrapPosition) == 1) {
                    AuthenticatePrivacyPasswordFragment.startAuthenticatePrivacyPassword(BackupDetailFragment.this);
                } else {
                    String str = "(" + BackupDetailFragment.this.getSelection() + ") AND (localGroupId != -1000)";
                    ImageLoadParams build = new ImageLoadParams.Builder().setKey(BackupDetailFragment.this.mBackupDetailAdapter.getItemKey(unwrapPosition)).setFilePath(BackupDetailFragment.this.mBackupDetailAdapter.getBindImagePath(unwrapPosition)).setTargetSize(Config$ThumbConfig.get().sMicroTargetSize).setRegionRect(BackupDetailFragment.this.mBackupDetailAdapter.getItemDecodeRectF(unwrapPosition)).setInitPosition(unwrapPosition).setMimeType(BackupDetailFragment.this.mBackupDetailAdapter.getMimeType(unwrapPosition)).setSecretKey(BackupDetailFragment.this.mBackupDetailAdapter.getItemSecretKey(unwrapPosition)).setFileLength(BackupDetailFragment.this.mBackupDetailAdapter.getFileLength(unwrapPosition)).setImageWidth(BackupDetailFragment.this.mBackupDetailAdapter.getImageWidth(unwrapPosition)).setImageHeight(BackupDetailFragment.this.mBackupDetailAdapter.getImageHeight(unwrapPosition)).setCreateTime(BackupDetailFragment.this.mBackupDetailAdapter.getCreateTime(unwrapPosition)).setLocation(BackupDetailFragment.this.mBackupDetailAdapter.getLocation(unwrapPosition)).build();
                    PhotoPageIntent.Builder initPosition = new PhotoPageIntent.Builder(BackupDetailFragment.this, InternalPhotoPageActivity.class).setAdapterView(recyclerView).setUri(BackupDetailFragment.this.getUri()).setInitPosition(unwrapPosition);
                    int itemCount = BackupDetailFragment.this.mBackupDetailAdapter.getItemCount();
                    if (BackupDetailFragment.this.mBackupDetailAdapter.getSecretCount() > 0) {
                        i2 = 1;
                    }
                    initPosition.setCount(itemCount - i2).setSelection(str).setSelectionArgs(BackupDetailFragment.this.getSelectionArgs()).setOrderBy(BackupDetailFragment.this.getCurrentSortOrder()).setImageLoadParams(build).setAlbumId(BackupDetailFragment.this.mAlbumId).setAlbumName(BackupDetailFragment.this.mAlbumName).setOperationMask(BackupDetailFragment.this.getSupportOperationMask()).setUnfoldBurst(!BackupDetailFragment.this.mo1564getAdapter().supportFoldBurstItems()).setEnterType(EnterTypeUtils.EnterType.FROM_BACKUP).build().gotoPhotoPage();
                    HashMap hashMap = new HashMap();
                    hashMap.put("from", BackupDetailFragment.this.getPageName());
                    hashMap.put("position", Integer.valueOf(unwrapPosition));
                    SamplingStatHelper.recordCountEvent("photo", "click_micro_thumb", hashMap);
                }
                return true;
            }
        };
    }

    public int unwrapPosition(int i) {
        long expandablePosition = this.mGroupedItemManager.getExpandablePosition(i);
        int packedPositionGroup = GroupedItemManager.getPackedPositionGroup(expandablePosition);
        int packedPositionChild = GroupedItemManager.getPackedPositionChild(expandablePosition);
        if (packedPositionChild == -1) {
            return -1;
        }
        return mo1564getAdapter().packDataPosition(packedPositionGroup, packedPositionChild);
    }

    /* loaded from: classes2.dex */
    public static class BackupDetailAdapter extends AlbumDetailAdapter {
        public static final int COLUMN_INDEX_ALBUM_ID;
        public static String[] PROJECTION;

        public final int mergeSyncState(int i, int i2) {
            return (i2 == 0 || i2 == 1) ? (i < i2 || i == Integer.MAX_VALUE) ? i2 : i : i2 != 2 ? i2 != 3 ? (i2 != 4 || i == 2 || i == 3) ? i : i2 : i != 2 ? i2 : i : i2;
        }

        @Override // com.miui.gallery.adapter.AlbumDetailAdapter, com.miui.gallery.adapter.IMediaAdapter
        public boolean supportFoldBurstItems() {
            return false;
        }

        static {
            String[] strArr = AlbumDetailAdapter.PROJECTION_INTERNAL;
            COLUMN_INDEX_ALBUM_ID = strArr.length;
            String[] strArr2 = new String[strArr.length + 1];
            PROJECTION = strArr2;
            System.arraycopy(strArr, 0, strArr2, 0, strArr.length);
            PROJECTION[strArr.length] = "localGroupId";
        }

        public BackupDetailAdapter(Context context, SyncStateDisplay$DisplayScene syncStateDisplay$DisplayScene, int i, Lifecycle lifecycle) {
            super(context, syncStateDisplay$DisplayScene, i, lifecycle);
            setShowTimeLine(false);
        }

        public int getSecretCount() {
            if (getCursor() != null) {
                getCursor().moveToLast();
                if (getCursor().getLong(0) == -1000) {
                    return getCursor().getInt(COLUMN_INDEX_ALBUM_ID);
                }
            }
            return 0;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return -1000 == getItemId(i) ? 1 : 0;
        }

        public final boolean isSecretItem(Cursor cursor) {
            return cursor.getLong(COLUMN_INDEX_ALBUM_ID) == -1000;
        }

        public Cursor classifyCursor(Cursor cursor) {
            String[] strArr = PROJECTION;
            MatrixCursor matrixCursor = new MatrixCursor(strArr);
            if (cursor != null && cursor.moveToFirst()) {
                int length = strArr.length;
                Object[] objArr = new Object[length];
                int i = Integer.MAX_VALUE;
                int i2 = 0;
                int i3 = 0;
                do {
                    if (isSecretItem(cursor)) {
                        int syncState = getSyncState(cursor);
                        i = mergeSyncState(i, syncState);
                        if (syncState == 0) {
                            i2++;
                        }
                        i3++;
                    } else {
                        for (int i4 = 0; i4 < length; i4++) {
                            int type = cursor.getType(i4);
                            if (type == 0) {
                                objArr[i4] = null;
                            } else if (type == 1) {
                                objArr[i4] = Long.valueOf(cursor.getLong(i4));
                            } else if (type == 2) {
                                objArr[i4] = Double.valueOf(cursor.getDouble(i4));
                            } else if (type == 3) {
                                objArr[i4] = cursor.getString(i4);
                            }
                        }
                        matrixCursor.addRow(objArr);
                    }
                } while (cursor.moveToNext());
                if (i3 > 0) {
                    objArr[0] = -1000L;
                    objArr[8] = Integer.valueOf(i2);
                    objArr[11] = Integer.valueOf(i3);
                    objArr[12] = Integer.valueOf(i);
                    matrixCursor.addRow(objArr);
                }
            }
            return matrixCursor;
        }
    }

    /* loaded from: classes2.dex */
    public class BackupLoaderCallBack implements LoaderManager.LoaderCallbacks<Cursor> {
        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader<Cursor> loader) {
        }

        public BackupLoaderCallBack() {
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            CursorLoader cursorLoader = new CursorLoader(BackupDetailFragment.this.mActivity);
            BackupDetailFragment.this.configLoader(cursorLoader);
            return cursorLoader;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            BackupDetailFragment.this.mo1564getAdapter().swapCursor(BackupDetailFragment.this.mBackupDetailAdapter.classifyCursor(cursor));
            if (BackupDetailFragment.this.mo1564getAdapter().getItemCount() == 0) {
                BackupDetailFragment.this.setEmptyViewVisibility(0);
                BackupDetailFragment backupDetailFragment = BackupDetailFragment.this;
                backupDetailFragment.mRecyclerView.setEmptyView(backupDetailFragment.getEmptyView());
            } else {
                BackupDetailFragment.this.setEmptyViewVisibility(8);
            }
            if (!BackupDetailFragment.this.mIsFirstLoaded) {
                BackupDetailFragment.this.mIsFirstLoaded = true;
                BackupDetailFragment.this.refreshOnUploadStatusChanged(null);
            }
        }
    }
}
