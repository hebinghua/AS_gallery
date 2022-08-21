package com.miui.gallery.ui;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.util.SparseLongArray;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GalleryGridLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.activity.facebaby.FacePageActivity;
import com.miui.gallery.activity.facebaby.IgnorePeoplePageActivity;
import com.miui.gallery.adapter.PeoplePageAdapter;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.app.screenChange.IScreenChange;
import com.miui.gallery.cloud.base.SyncRequest;
import com.miui.gallery.cloud.base.SyncType;
import com.miui.gallery.cloud.peopleface.FaceDataManager;
import com.miui.gallery.compat.app.ActivityCompat;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.model.PeopleContactInfo;
import com.miui.gallery.people.mark.MarkMyselfViewHelper;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.preference.PreferenceHelper;
import com.miui.gallery.provider.FaceManager;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.PeopleFaceSnapshotHelper;
import com.miui.gallery.provider.deprecated.NormalPeopleFaceMediaSet;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.threadpool.GallerySchedulers;
import com.miui.gallery.ui.KeyboardShortcutGroupManager;
import com.miui.gallery.ui.PeoplePageFragment;
import com.miui.gallery.ui.PeopleRelationSetDialogFragment;
import com.miui.gallery.ui.ProcessTask;
import com.miui.gallery.ui.renameface.FaceAlbumRenameHandler;
import com.miui.gallery.ui.renameface.InputFaceNameFragment;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.deprecated.Preference;
import com.miui.gallery.util.face.PeopleCursorHelper;
import com.miui.gallery.util.face.PeopleItem;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.AntiDoubleItemClickListener;
import com.miui.gallery.widget.EmptyPage;
import com.miui.gallery.widget.editwrapper.EditableListViewWrapper;
import com.miui.gallery.widget.editwrapper.HeaderFooterWrapper;
import com.miui.gallery.widget.editwrapper.MultiChoiceModeListener;
import com.miui.gallery.widget.recyclerview.EditableListSpanSizeProvider;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.miui.gallery.widget.recyclerview.GridItemSpacingDecoration;
import com.miui.gallery.widget.recyclerview.IrregularSpanSizeLookup;
import com.miui.gallery.widget.recyclerview.ItemClickSupport;
import com.miui.gallery.widget.recyclerview.SimpleHeaderFooterWrapperAdapter;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.MiStat;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/* loaded from: classes2.dex */
public class PeoplePageFragment extends BaseFragment {
    public EditableListViewWrapper mEditableWrapper;
    public ViewStub mEmptyViewStub;
    public FaceAlbumRenameHandler mFaceAlbumRenameHandler;
    public View mFooterView;
    public SimpleHeaderFooterWrapperAdapter mFooterWrapperAdapter;
    public boolean mIsInMultiWindow;
    public int mPartialPeopleCount;
    public int mPeopleCountOfMyself;
    public PeoplePageAdapter mPeoplePageAdapter;
    public PeoplePagePhotoLoaderCallback mPeoplePagePhotoLoaderCallback;
    public GalleryRecyclerView mRecyclerView;
    public GridItemSpacingDecoration mSpacingDecoration;
    public ShowEmptyViewHelper mShowEmptyViewHelper = new ShowEmptyViewHelper(this, null);
    public PublishSubject<List<PeopleItem>> mPeopleItemPublishSubject = PublishSubject.create();
    public CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    public PeoplePageKeyboardShortcutCallback mShortcutCallback = new PeoplePageKeyboardShortcutCallback(this, null);
    public DisplayPeopleMode mDisplayPeopleMode = DisplayPeopleMode.DISPLAY_PARTIAL_PEOPLE;
    public MarkMyselfViewHelper mMarkMyselfHelper = null;
    public boolean mInMarkMode = false;
    public String mMarkName = null;
    public PeopleContactInfo.Relation mMarkRelation = null;
    public ItemClickSupport.OnItemClickListener mItemClickListener = new AnonymousClass1();
    public boolean mHaveShownSetGroupToastDialog = false;
    public boolean mFirstLoadFinish = true;
    public Handler mHandler = new Handler();
    public MultiChoiceModeListener mChoiceModeListener = new MultiChoiceModeListener() { // from class: com.miui.gallery.ui.PeoplePageFragment.3
        public ActionMode mMode;

        @Override // android.view.ActionMode.Callback
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        {
            PeoplePageFragment.this = this;
        }

        @Override // com.miui.gallery.widget.editwrapper.MultiChoiceModeListener
        public void onAllItemsCheckedStateChanged(ActionMode actionMode, boolean z) {
            enableOrDisableMenuItem(PeoplePageFragment.this.mEditableWrapper.getCheckedItemCount() > 0);
        }

        @Override // com.miui.gallery.widget.editwrapper.MultiChoiceModeListener
        public void onItemCheckedStateChanged(ActionMode actionMode, int i, long j, boolean z) {
            enableOrDisableMenuItem(PeoplePageFragment.this.mEditableWrapper.getCheckedItemCount() > 0);
        }

        @Override // android.view.ActionMode.Callback
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            this.mMode = actionMode;
            actionMode.getMenuInflater().inflate(R.menu.people_page_menu, menu);
            enableOrDisableMenuItem(false);
            PeoplePageFragment.this.mPeoplePageAdapter.enterChoiceMode();
            return true;
        }

        @Override // android.view.ActionMode.Callback
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            if (menuItem.getItemId() != 16908313 && menuItem.getItemId() != 16908314) {
                LinearMotorHelper.performHapticFeedback(PeoplePageFragment.this.getContext(), LinearMotorHelper.HAPTIC_TAP_LIGHT);
            }
            int itemId = menuItem.getItemId();
            if (itemId == R.id.action_ignore_face_album) {
                PeoplePageFragment peoplePageFragment = PeoplePageFragment.this;
                peoplePageFragment.showIgnoreFaceAlbumAlert(peoplePageFragment.mEditableWrapper.getCheckedItemIds(), this.mMode);
                TrackController.trackClick("403.47.2.1.11261", AutoTracking.getRef());
                return true;
            } else if (itemId == R.id.action_merge_face_album) {
                PeoplePageFragment peoplePageFragment2 = PeoplePageFragment.this;
                peoplePageFragment2.mergePeople(peoplePageFragment2.mEditableWrapper.getCheckedItemIds(), this.mMode);
                TrackController.trackClick("403.47.2.1.11260", AutoTracking.getRef());
                return true;
            } else if (itemId != R.id.action_set_group) {
                return false;
            } else {
                PeoplePageFragment peoplePageFragment3 = PeoplePageFragment.this;
                peoplePageFragment3.showAndSetRelationDialog(peoplePageFragment3.mEditableWrapper.getCheckedItemIds(), getDefaultPeopleRelationText(), this.mMode);
                TrackController.trackClick("403.47.2.1.11262", AutoTracking.getRef());
                return true;
            }
        }

        public final String getDefaultPeopleRelationText() {
            if (PeoplePageFragment.this.mEditableWrapper.getCheckedItemCount() > 1) {
                return null;
            }
            List<Integer> checkedPositions = PeoplePageFragment.this.mEditableWrapper.getCheckedPositions();
            if (checkedPositions.size() <= 0) {
                return null;
            }
            int intValue = checkedPositions.get(0).intValue();
            int relationTypeOfItem = PeoplePageFragment.this.mPeoplePageAdapter.getRelationTypeOfItem(intValue);
            if (PeopleContactInfo.isUserDefineRelation(relationTypeOfItem)) {
                return PeoplePageFragment.this.mPeoplePageAdapter.getRelationTextOfItem(intValue);
            }
            return PeopleContactInfo.getRelationValue(relationTypeOfItem);
        }

        @Override // android.view.ActionMode.Callback
        public void onDestroyActionMode(ActionMode actionMode) {
            this.mMode = null;
            PeoplePageFragment.this.mPeoplePageAdapter.exitChoiceMode();
        }

        public final void enableOrDisableMenuItem(boolean z) {
            Menu menu = this.mMode.getMenu();
            MenuItem findItem = menu.findItem(R.id.action_merge_face_album);
            if (findItem != null) {
                findItem.setEnabled(z);
            }
            MenuItem findItem2 = menu.findItem(R.id.action_ignore_face_album);
            if (findItem2 != null) {
                findItem2.setEnabled(z);
            }
            MenuItem findItem3 = menu.findItem(R.id.action_set_group);
            if (findItem3 != null) {
                findItem3.setEnabled(z);
            }
        }
    };

    /* loaded from: classes2.dex */
    public enum DisplayPeopleMode {
        NOT_DISTINGUISH,
        DISPLAY_ALL_PEOPLE,
        DISPLAY_PARTIAL_PEOPLE
    }

    /* renamed from: $r8$lambda$Jef4x6-vRM3AqHBtLL8ZBs-Cua4 */
    public static /* synthetic */ void m1529$r8$lambda$Jef4x6vRM3AqHBtLL8ZBsCua4(PeoplePageFragment peoplePageFragment, View view) {
        peoplePageFragment.lambda$onInflateView$0(view);
    }

    public static /* synthetic */ void $r8$lambda$QPHMEdmsIlAj0W9jJHCqlxKRKoA(PeoplePageFragment peoplePageFragment, Configuration configuration) {
        peoplePageFragment.lambda$onInflateView$2(configuration);
    }

    public static /* synthetic */ void $r8$lambda$fBZLf_Fkf9lwEZVDM5R6cx76i9U(PeoplePageFragment peoplePageFragment) {
        peoplePageFragment.lambda$loadPeoplePage$3();
    }

    public static /* synthetic */ AbstractHeaderFooterWrapperAdapter $r8$lambda$fQxpZKYueqEPLwlEkExXTY6co_4(PeoplePageFragment peoplePageFragment, RecyclerView.Adapter adapter) {
        return peoplePageFragment.lambda$onInflateView$1(adapter);
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return "people";
    }

    public static /* synthetic */ int access$1908(PeoplePageFragment peoplePageFragment) {
        int i = peoplePageFragment.mPeopleCountOfMyself;
        peoplePageFragment.mPeopleCountOfMyself = i + 1;
        return i;
    }

    /* loaded from: classes2.dex */
    public class ShowEmptyViewHelper {
        public EmptyPage mEmptyView;

        public ShowEmptyViewHelper() {
            PeoplePageFragment.this = r1;
            this.mEmptyView = null;
        }

        public /* synthetic */ ShowEmptyViewHelper(PeoplePageFragment peoplePageFragment, AnonymousClass1 anonymousClass1) {
            this();
        }

        public void resume() {
            EmptyPage emptyPage = this.mEmptyView;
            if (emptyPage != null) {
                emptyPage.resumeMaml();
            }
        }

        public void pause() {
            EmptyPage emptyPage = this.mEmptyView;
            if (emptyPage != null) {
                emptyPage.pauseMaml();
            }
        }

        public void destroy() {
            EmptyPage emptyPage = this.mEmptyView;
            if (emptyPage != null) {
                emptyPage.destroyMaml();
            }
        }

        public final View initializeEmptyView(ViewStub viewStub, boolean z) {
            if (z) {
                if (this.mEmptyView == null) {
                    this.mEmptyView = (EmptyPage) viewStub.inflate();
                    setupEmptyView();
                }
                updateEmptyView();
            }
            return this.mEmptyView;
        }

        public final void updateEmptyView() {
            if (this.mEmptyView == null) {
                return;
            }
            if (Preference.sIsFirstSynced()) {
                if (!AIAlbumStatusHelper.isFaceAlbumEnabled()) {
                    showSwitchClosedTips();
                    return;
                } else if (GalleryPreferences.Face.isFirstSyncCompleted()) {
                    showFaceEmptyTips();
                    return;
                }
            }
            if (!BaseNetworkUtils.isNetworkConnected()) {
                showNoWifiTips();
            } else if (!SyncUtil.isGalleryCloudSyncable(PeoplePageFragment.this.mActivity)) {
                showSyncOffTips();
            } else {
                showSyncingTips();
            }
        }

        public final void showFaceEmptyTips() {
            this.mEmptyView.setTitle(R.string.face_album_empty);
            this.mEmptyView.setActionButtonVisible(false);
        }

        public final void showSyncingTips() {
            this.mEmptyView.setTitle(R.string.face_album_empty_syncing);
            this.mEmptyView.setActionButtonVisible(false);
        }

        public final void showSyncOffTips() {
            this.mEmptyView.setTitle(R.string.face_album_use_tip);
            this.mEmptyView.setActionButtonText(R.string.search_backup_now);
            this.mEmptyView.setActionButtonVisible(true);
            this.mEmptyView.setOnActionButtonClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.PeoplePageFragment.ShowEmptyViewHelper.1
                {
                    ShowEmptyViewHelper.this = this;
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    IntentUtil.gotoTurnOnSyncSwitchInner(PeoplePageFragment.this.mActivity);
                }
            });
        }

        public final void showNoWifiTips() {
            this.mEmptyView.setTitle(R.string.face_album_empty_syncing_when_connect_wifi);
            this.mEmptyView.setActionButtonText(R.string.setup_network_connection);
            this.mEmptyView.setActionButtonVisible(true);
            this.mEmptyView.setOnActionButtonClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.PeoplePageFragment.ShowEmptyViewHelper.2
                {
                    ShowEmptyViewHelper.this = this;
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    ShowEmptyViewHelper.this.setupNetworkConnection();
                }
            });
        }

        public final void showSwitchClosedTips() {
            this.mEmptyView.setTitle(R.string.face_album_use_tip);
            this.mEmptyView.setActionButtonText(R.string.start_to_use_face_albumset);
            this.mEmptyView.setActionButtonVisible(true);
            this.mEmptyView.setOnActionButtonClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.PeoplePageFragment.ShowEmptyViewHelper.3
                {
                    ShowEmptyViewHelper.this = this;
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    AIAlbumStatusHelper.setFaceAlbumStatus(PeoplePageFragment.this.mActivity, true);
                    ShowEmptyViewHelper.this.updateEmptyView();
                    if (PeoplePageFragment.this.getLoaderManager().getLoader(2) == null) {
                        PeoplePageFragment.this.getLoaderManager().initLoader(2, null, PeoplePageFragment.this.mPeoplePagePhotoLoaderCallback);
                    }
                    SyncUtil.requestSync(PeoplePageFragment.this.mActivity, new SyncRequest.Builder().setSyncType(SyncType.NORMAL).setSyncReason(8L).build());
                    SamplingStatHelper.recordCountEvent("people", "people_open_switch");
                }
            });
        }

        public final void setupEmptyView() {
            this.mEmptyView.setActionButtonVisible(false);
        }

        public final void setupNetworkConnection() {
            if (PeoplePageFragment.this.getActivity() != null) {
                PeoplePageFragment.this.getActivity().startActivity(new Intent("android.settings.SETTINGS"));
            }
        }
    }

    @Override // com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Observable<List<PeopleItem>> observeOn = this.mPeopleItemPublishSubject.observeOn(GallerySchedulers.misc());
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        this.mCompositeDisposable.add(observeOn.delay(350L, timeUnit, GallerySchedulers.misc()).throttleLatest(3000L, timeUnit, GallerySchedulers.misc(), true).map(PeoplePageFragment$$ExternalSyntheticLambda3.INSTANCE).subscribe());
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mIsInMultiWindow = ActivityCompat.isInMultiWindowMode(this.mActivity) && !BaseBuildUtil.isLargeHorizontalWindow();
        View inflate = layoutInflater.inflate(R.layout.people_page, viewGroup, false);
        GalleryRecyclerView galleryRecyclerView = (GalleryRecyclerView) inflate.findViewById(R.id.grid);
        this.mRecyclerView = galleryRecyclerView;
        this.mSpacingDecoration = new GridItemSpacingDecoration(galleryRecyclerView, false, this.mActivity.getResources().getDimensionPixelSize(R.dimen.people_face_horizontal_spacing), this.mActivity.getResources().getDimensionPixelSize(R.dimen.people_face_vertical_spacing));
        this.mRecyclerView.invalidateItemDecorations();
        this.mRecyclerView.addItemDecoration(this.mSpacingDecoration);
        this.mRecyclerView.setItemAnimator(null);
        this.mPeoplePageAdapter = new PeoplePageAdapter(this.mActivity);
        View inflate2 = LayoutInflater.from(this.mActivity).inflate(R.layout.see_more_people_view, (ViewGroup) null, false);
        this.mFooterView = inflate2;
        FolmeUtil.setDefaultTouchAnim(inflate2, null, false, false, true);
        this.mFooterView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.PeoplePageFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PeoplePageFragment.m1529$r8$lambda$Jef4x6vRM3AqHBtLL8ZBsCua4(PeoplePageFragment.this, view);
            }
        });
        EditableListViewWrapper editableListViewWrapper = new EditableListViewWrapper(this.mRecyclerView);
        this.mEditableWrapper = editableListViewWrapper;
        editableListViewWrapper.setAdapter(this.mPeoplePageAdapter, new HeaderFooterWrapper() { // from class: com.miui.gallery.ui.PeoplePageFragment$$ExternalSyntheticLambda2
            @Override // com.miui.gallery.widget.editwrapper.HeaderFooterWrapper
            public final AbstractHeaderFooterWrapperAdapter wrap(RecyclerView.Adapter adapter) {
                return PeoplePageFragment.$r8$lambda$fQxpZKYueqEPLwlEkExXTY6co_4(PeoplePageFragment.this, adapter);
            }
        });
        this.mEditableWrapper.setHandleTouchAnimItemType(PeoplePageGridItem.class.getSimpleName());
        this.mEditableWrapper.setOnItemClickListener(this.mItemClickListener);
        this.mEditableWrapper.enableChoiceMode(true);
        this.mEditableWrapper.enterChoiceModeWithLongClick(true);
        this.mEditableWrapper.setMultiChoiceModeListener(this.mChoiceModeListener);
        GalleryGridLayoutManager galleryGridLayoutManager = new GalleryGridLayoutManager(this.mActivity, getResources().getInteger(R.integer.people_face_grid_view_columns));
        galleryGridLayoutManager.setSpanSizeLookup(IrregularSpanSizeLookup.create(new EditableListSpanSizeProvider(this.mEditableWrapper, galleryGridLayoutManager)));
        this.mRecyclerView.setLayoutManager(galleryGridLayoutManager);
        this.mEmptyViewStub = (ViewStub) inflate.findViewById(R.id.empty_view);
        updateConfiguration(getResources().getConfiguration());
        addScreenChangeListener(new IScreenChange.OnScreenLayoutSizeChangeListener() { // from class: com.miui.gallery.ui.PeoplePageFragment$$ExternalSyntheticLambda1
            @Override // com.miui.gallery.app.screenChange.IScreenChange.OnScreenLayoutSizeChangeListener
            public final void onScreenLayoutSizeChange(Configuration configuration) {
                PeoplePageFragment.$r8$lambda$QPHMEdmsIlAj0W9jJHCqlxKRKoA(PeoplePageFragment.this, configuration);
            }
        });
        return inflate;
    }

    public /* synthetic */ void lambda$onInflateView$0(View view) {
        onFooterClick();
    }

    public /* synthetic */ AbstractHeaderFooterWrapperAdapter lambda$onInflateView$1(RecyclerView.Adapter adapter) {
        SimpleHeaderFooterWrapperAdapter simpleHeaderFooterWrapperAdapter = new SimpleHeaderFooterWrapperAdapter(adapter);
        this.mFooterWrapperAdapter = simpleHeaderFooterWrapperAdapter;
        return simpleHeaderFooterWrapperAdapter;
    }

    public /* synthetic */ void lambda$onInflateView$2(Configuration configuration) {
        int dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.people_page_list_padding_left_right);
        this.mRecyclerView.setPadding(dimensionPixelOffset, 0, dimensionPixelOffset, 0);
        this.mSpacingDecoration.setHorizontalSpacing(getResources().getDimensionPixelOffset(R.dimen.people_face_horizontal_spacing));
    }

    public final void onFooterClick() {
        DisplayPeopleMode displayPeopleMode = this.mDisplayPeopleMode;
        DisplayPeopleMode displayPeopleMode2 = DisplayPeopleMode.DISPLAY_PARTIAL_PEOPLE;
        if (displayPeopleMode == displayPeopleMode2) {
            this.mDisplayPeopleMode = DisplayPeopleMode.DISPLAY_ALL_PEOPLE;
            this.mEditableWrapper.getCheckedItemIds();
            TrackController.trackClick("403.47.2.1.11258", AutoTracking.getRef());
        } else if (displayPeopleMode == DisplayPeopleMode.DISPLAY_ALL_PEOPLE) {
            this.mDisplayPeopleMode = displayPeopleMode2;
        }
        getLoaderManager().getLoader(2).forceLoad();
        HashMap hashMap = new HashMap();
        hashMap.put("state", this.mDisplayPeopleMode == DisplayPeopleMode.DISPLAY_ALL_PEOPLE ? "all" : "part");
        SamplingStatHelper.recordCountEvent("people", "people_list_display_mode", hashMap);
    }

    /* renamed from: com.miui.gallery.ui.PeoplePageFragment$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends AntiDoubleItemClickListener {
        /* renamed from: $r8$lambda$5nSPvxek0g884ccpq4-j1EtRm8Y */
        public static /* synthetic */ Boolean m1530$r8$lambda$5nSPvxek0g884ccpq4j1EtRm8Y(AnonymousClass1 anonymousClass1, String str, Void[] voidArr) {
            return anonymousClass1.lambda$onAntiDoubleItemClick$0(str, voidArr);
        }

        public AnonymousClass1() {
            PeoplePageFragment.this = r1;
        }

        @Override // com.miui.gallery.widget.AntiDoubleItemClickListener
        public void onAntiDoubleItemClick(RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
            final String peopleIdOfItem = PeoplePageFragment.this.mPeoplePageAdapter.getPeopleIdOfItem(i);
            final String peopleLocalIdOfItem = PeoplePageFragment.this.mPeoplePageAdapter.getPeopleLocalIdOfItem(i);
            final String name = ((PeoplePageGridItem) view).getName();
            if (PeoplePageFragment.this.mInMarkMode) {
                if (PeoplePageFragment.this.mMarkRelation == null) {
                    if (TextUtils.isEmpty(PeoplePageFragment.this.mMarkName)) {
                        return;
                    }
                    NormalPeopleFaceMediaSet normalPeopleFaceMediaSet = new NormalPeopleFaceMediaSet(Long.parseLong(peopleLocalIdOfItem), peopleIdOfItem, TextUtils.isEmpty(PeoplePageFragment.this.mMarkName) ? name : PeoplePageFragment.this.mMarkName);
                    PeoplePageFragment peoplePageFragment = PeoplePageFragment.this;
                    peoplePageFragment.mFaceAlbumRenameHandler = new FaceAlbumRenameHandler(peoplePageFragment.mActivity, normalPeopleFaceMediaSet, new FaceAlbumRenameHandler.ConfirmListener() { // from class: com.miui.gallery.ui.PeoplePageFragment.1.2
                        {
                            AnonymousClass1.this = this;
                        }

                        @Override // com.miui.gallery.ui.renameface.FaceAlbumRenameHandler.ConfirmListener
                        public void onConfirm(String str, boolean z) {
                            PeoplePageFragment peoplePageFragment2 = PeoplePageFragment.this;
                            peoplePageFragment2.finishWithMarkSuccessResult(peopleLocalIdOfItem, peopleIdOfItem, str, name, peoplePageFragment2.mMarkName);
                        }
                    });
                    PeoplePageFragment.this.mFaceAlbumRenameHandler.show();
                    return;
                }
                ProcessTask processTask = new ProcessTask(new ProcessTask.ProcessCallback() { // from class: com.miui.gallery.ui.PeoplePageFragment$1$$ExternalSyntheticLambda0
                    @Override // com.miui.gallery.ui.ProcessTask.ProcessCallback
                    public final Object doProcess(Object[] objArr) {
                        return PeoplePageFragment.AnonymousClass1.m1530$r8$lambda$5nSPvxek0g884ccpq4j1EtRm8Y(PeoplePageFragment.AnonymousClass1.this, peopleLocalIdOfItem, (Void[]) objArr);
                    }
                });
                processTask.setCompleteListener(new ProcessTask.OnCompleteListener<Boolean>() { // from class: com.miui.gallery.ui.PeoplePageFragment.1.1
                    {
                        AnonymousClass1.this = this;
                    }

                    @Override // com.miui.gallery.ui.ProcessTask.OnCompleteListener
                    public void onCompleteProcess(Boolean bool) {
                        if (bool != null && bool.booleanValue()) {
                            PeoplePageFragment peoplePageFragment2 = PeoplePageFragment.this;
                            peoplePageFragment2.finishWithMarkSuccessResult(peopleLocalIdOfItem, peopleIdOfItem, name, peoplePageFragment2.mMarkName, PeoplePageFragment.this.mMarkRelation.getRelationType());
                            HashMap hashMap = new HashMap();
                            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, String.valueOf(PeoplePageFragment.this.mMarkRelation.getRelationType()));
                            SamplingStatHelper.recordCountEvent("people_mark", "mark_relation_in_people_page", hashMap);
                            return;
                        }
                        ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.mark_operation_failed);
                    }
                });
                PeoplePageFragment peoplePageFragment2 = PeoplePageFragment.this;
                processTask.showProgress(peoplePageFragment2.mActivity, peoplePageFragment2.getString(R.string.mark_operation_processing));
                processTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                return;
            }
            TrackController.trackClick("403.47.2.1.11257", AutoTracking.getRef());
            TimeMonitor.createNewTimeMonitor("403.17.0.1.13786");
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("server_id_of_album", peopleIdOfItem);
            bundle.putString("local_id_of_album", peopleLocalIdOfItem);
            bundle.putString("album_name", name);
            bundle.putInt("relationType", PeoplePageFragment.this.mPeoplePageAdapter.getRelationTypeOfItem(i));
            bundle.putString("face_album_cover", PeoplePageFragment.this.mPeoplePageAdapter.getThumbFilePath(i));
            bundle.putParcelable("face_position_rect", PeoplePageFragment.this.mPeoplePageAdapter.getFaceRegionRectF(i));
            intent.putExtras(bundle);
            intent.setClass(PeoplePageFragment.this.mActivity, FacePageActivity.class);
            PeoplePageFragment.this.startActivity(intent);
        }

        public /* synthetic */ Boolean lambda$onAntiDoubleItemClick$0(String str, Void[] voidArr) {
            String relationValue = PeopleContactInfo.getRelationValue(PeoplePageFragment.this.mMarkRelation);
            return Boolean.valueOf(NormalPeopleFaceMediaSet.moveFaceToRelationGroup(new long[]{Long.parseLong(str)}, relationValue, relationValue));
        }
    }

    /* loaded from: classes2.dex */
    public class PeoplePagePhotoLoaderCallback implements LoaderManager.LoaderCallbacks {
        public Future mChangeToVisibleFuture;
        public SparseBooleanArray mIsManualLoad;
        public final ArrayList<String> mLastLoadVisibilityUndefinedIds;
        public SparseLongArray mLoaderCreateTime;

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader loader) {
        }

        public PeoplePagePhotoLoaderCallback() {
            PeoplePageFragment.this = r1;
            this.mLastLoadVisibilityUndefinedIds = new ArrayList<>();
            this.mLoaderCreateTime = new SparseLongArray();
            this.mIsManualLoad = new SparseBooleanArray();
        }

        public /* synthetic */ PeoplePagePhotoLoaderCallback(PeoplePageFragment peoplePageFragment, AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader onCreateLoader(int i, Bundle bundle) {
            this.mIsManualLoad.put(i, true);
            this.mLoaderCreateTime.put(i, System.currentTimeMillis());
            CursorLoader cursorLoader = new CursorLoader(PeoplePageFragment.this.mActivity);
            if (i == 1) {
                cursorLoader.setUri(GalleryContract.PeopleFace.PEOPLE_SNAPSHOT_URI);
                cursorLoader.setProjection(PeopleItem.COMPAT_PROJECTION);
            } else {
                cursorLoader.setUri(GalleryContract.PeopleFace.PERSONS_URI);
                cursorLoader.setProjection(PeopleCursorHelper.PROJECTION);
            }
            return cursorLoader;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader loader, Object obj) {
            if (obj == null) {
                DefaultLogger.d("PeoplePageFragment", "empty load result");
                return;
            }
            long currentTimeMillis = System.currentTimeMillis();
            if (!initMarkMyselfHelper() && PeoplePageFragment.this.mFirstLoadFinish && shouldShowSetGroupToastDialog()) {
                initialSetGroupToastDialog((Cursor) obj);
            }
            PeoplePageFragment.this.mFirstLoadFinish = false;
            ArrayList<String> arrayList = new ArrayList<>();
            HashMap<String, Integer> hashMap = new HashMap<>();
            Cursor cursor = (Cursor) obj;
            Cursor wrapCursorByDisplayMode = wrapCursorByDisplayMode(cursor, arrayList, hashMap);
            PeopleContactInfo.UserDefineRelation.setUserDefineRelations(arrayList);
            PeoplePageFragment.this.mPeoplePageAdapter.setUserDefineRelationMap(hashMap);
            PeoplePageFragment.this.mPeoplePageAdapter.changeCursor(wrapCursorByDisplayMode);
            int id = loader.getId();
            if (id != 2) {
                PeoplePageFragment.this.loadPeoplePage();
            } else {
                boolean z = true;
                if (PeoplePageFragment.this.getLoaderManager().getLoader(1) != null) {
                    PeoplePageFragment.this.destroySnapshotLoader();
                }
                PeoplePageFragment.this.mPeopleItemPublishSubject.onNext(PeopleFaceSnapshotHelper.cursor2Entities(wrapCursorByDisplayMode));
                boolean z2 = cursor.getCount() == 0;
                PeoplePageFragment.this.mRecyclerView.setEmptyView(PeoplePageFragment.this.mShowEmptyViewHelper.initializeEmptyView(PeoplePageFragment.this.mEmptyViewStub, z2));
                PeoplePageFragment.this.mShowEmptyViewHelper.resume();
                doResetFooterAfterReload(z2);
                ArrayList<String> visibilityTypeUndefinedIds = getVisibilityTypeUndefinedIds(cursor);
                synchronized (this.mLastLoadVisibilityUndefinedIds) {
                    if (this.mLastLoadVisibilityUndefinedIds.containsAll(visibilityTypeUndefinedIds) && visibilityTypeUndefinedIds.containsAll(this.mLastLoadVisibilityUndefinedIds)) {
                        z = false;
                    }
                }
                if (z) {
                    changeUndefinedToVisible(visibilityTypeUndefinedIds);
                }
                TimeMonitor.trackTimeMonitor("403.47.0.1.13783", cursor.getCount());
            }
            if (!this.mIsManualLoad.get(id)) {
                return;
            }
            this.mIsManualLoad.put(id, false);
            long j = currentTimeMillis - this.mLoaderCreateTime.get(id);
            DefaultLogger.d("PeoplePageFragment", "loader : %s, people count : %d, costs %d", loaderId2Name(id), Integer.valueOf(cursor.getCount()), Long.valueOf(j));
            statLoadTime(loaderId2Name(id), j, cursor.getCount());
        }

        public final Cursor wrapCursorByDisplayMode(Cursor cursor, ArrayList<String> arrayList, HashMap hashMap) {
            if (cursor != null && cursor.getCount() != 0) {
                cursor.moveToFirst();
                int i = 0;
                int i2 = 0;
                int i3 = 0;
                int i4 = 0;
                while (!cursor.isAfterLast() && i2 <= 18) {
                    int relationType = PeopleCursorHelper.getRelationType(cursor);
                    if (PeopleContactInfo.isUserDefineRelation(relationType)) {
                        String relationText = PeopleCursorHelper.getRelationText(cursor);
                        if (relationText != null && !hashMap.containsKey(relationText)) {
                            hashMap.put(relationText, Integer.valueOf(arrayList.size()));
                            arrayList.add(relationText);
                        }
                    } else if (PeopleContactInfo.isUnKnownRelation(relationType)) {
                        i3++;
                        if (TextUtils.isEmpty(PeopleCursorHelper.getPeopleName(cursor))) {
                            i2++;
                        }
                    } else if (PeopleContactInfo.isMyselfRelation(relationType)) {
                        PeoplePageFragment.access$1908(PeoplePageFragment.this);
                    }
                    i4++;
                    if (i4 >= 18 && i2 > 0 && i3 % ((GridLayoutManager) PeoplePageFragment.this.mRecyclerView.getLayoutManager()).getSpanCount() == 0) {
                        break;
                    }
                    cursor.moveToNext();
                }
                PeoplePageFragment.this.mPartialPeopleCount = i4;
                if (i4 != cursor.getCount()) {
                    if (PeoplePageFragment.this.mDisplayPeopleMode == DisplayPeopleMode.DISPLAY_PARTIAL_PEOPLE) {
                        MatrixCursor matrixCursor = new MatrixCursor(PeopleCursorHelper.PROJECTION);
                        cursor.moveToFirst();
                        while (!cursor.isAfterLast() && i < i4) {
                            PeopleCursorHelper.add2MatrixCursor(matrixCursor, cursor);
                            i++;
                            cursor.moveToNext();
                        }
                        return matrixCursor;
                    }
                } else {
                    PeoplePageFragment.this.mDisplayPeopleMode = DisplayPeopleMode.NOT_DISTINGUISH;
                    return cursor;
                }
            }
            return cursor;
        }

        public final void doResetFooterAfterReload(boolean z) {
            String string;
            if (PeoplePageFragment.this.mDisplayPeopleMode == DisplayPeopleMode.NOT_DISTINGUISH) {
                PeoplePageFragment.this.mFooterWrapperAdapter.removeFooter(PeoplePageFragment.this.mFooterView);
            } else if (z) {
                PeoplePageFragment.this.mFooterWrapperAdapter.removeFooter(PeoplePageFragment.this.mFooterView);
            } else {
                PeoplePageFragment.this.mFooterWrapperAdapter.removeFooter(PeoplePageFragment.this.mFooterView);
                PeoplePageFragment.this.mFooterWrapperAdapter.addFooter(PeoplePageFragment.this.mFooterView);
                if (PeoplePageFragment.this.mDisplayPeopleMode == DisplayPeopleMode.DISPLAY_PARTIAL_PEOPLE) {
                    string = PeoplePageFragment.this.mActivity.getString(R.string.expand_people);
                } else {
                    string = PeoplePageFragment.this.mActivity.getString(R.string.collaps_people);
                }
                ((TextView) PeoplePageFragment.this.mFooterView.findViewById(R.id.see_more_people_text)).setText(string);
            }
        }

        public final boolean shouldShowSetGroupToastDialog() {
            if (!PeoplePageFragment.this.mInMarkMode) {
                return (PeoplePageFragment.this.mMarkMyselfHelper == null || !PeoplePageFragment.this.mMarkMyselfHelper.isSuggestionDialogVisible()) && !PeoplePageFragment.this.mHaveShownSetGroupToastDialog && !PreferenceHelper.getBoolean(GalleryPreferences.PrefKeys.FACE_HAS_TOAST_SET_GROUP, false);
            }
            return false;
        }

        public final void initialSetGroupToastDialog(Cursor cursor) {
            String string;
            String string2;
            if (cursor == null || cursor.getCount() == 0) {
                return;
            }
            if (seeIfHasNamedPeople(cursor)) {
                string = PeoplePageFragment.this.mActivity.getString(R.string.set_face_group_toast_title_old_user);
                string2 = PeoplePageFragment.this.mActivity.getString(R.string.set_face_group_toast_msg_old_user);
            } else {
                string = PeoplePageFragment.this.mActivity.getString(R.string.set_face_group_toast_title_new_user);
                string2 = PeoplePageFragment.this.mActivity.getString(R.string.set_face_group_toast_msg_new_user);
            }
            DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.PeoplePageFragment.PeoplePagePhotoLoaderCallback.1
                {
                    PeoplePagePhotoLoaderCallback.this = this;
                }

                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    PreferenceHelper.putBoolean(GalleryPreferences.PrefKeys.FACE_HAS_TOAST_SET_GROUP, true);
                }
            };
            if (PeoplePageFragment.this.mHaveShownSetGroupToastDialog) {
                return;
            }
            ToastDialogFragment newInstance = ToastDialogFragment.newInstance(string, string2, R.string.have_known);
            newInstance.setConfirmListener(onClickListener);
            newInstance.showAllowingStateLoss(PeoplePageFragment.this.getActivity().getSupportFragmentManager(), "PeoplePageFragment");
            PeoplePageFragment.this.mHaveShownSetGroupToastDialog = true;
        }

        public final boolean seeIfHasNamedPeople(Cursor cursor) {
            if (cursor != null) {
                cursor.moveToFirst();
                while (cursor.moveToNext()) {
                    if (!TextUtils.isEmpty(PeopleCursorHelper.getPeopleName(cursor))) {
                        return true;
                    }
                }
            }
            return false;
        }

        public final ArrayList<String> getVisibilityTypeUndefinedIds(Cursor cursor) {
            ArrayList<String> arrayList = new ArrayList<>();
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    if (PeopleCursorHelper.getVisibilityType(cursor) == 0) {
                        arrayList.add(PeopleCursorHelper.getPeopleLocalId(cursor));
                    }
                }
            }
            return arrayList;
        }

        public final void changeUndefinedToVisible(final ArrayList<String> arrayList) {
            Future future = this.mChangeToVisibleFuture;
            if (future != null) {
                future.cancel();
                this.mChangeToVisibleFuture = null;
            }
            this.mChangeToVisibleFuture = ThreadManager.getMiscPool().submit(new ThreadPool.Job<Void>() { // from class: com.miui.gallery.ui.PeoplePageFragment.PeoplePagePhotoLoaderCallback.2
                {
                    PeoplePagePhotoLoaderCallback.this = this;
                }

                @Override // com.miui.gallery.concurrent.ThreadPool.Job
                /* renamed from: run */
                public Void mo1807run(ThreadPool.JobContext jobContext) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("visibilityType", (Integer) 1);
                    FaceManager.safeUpdatePeopleFaceByIds(contentValues, arrayList);
                    synchronized (PeoplePagePhotoLoaderCallback.this.mLastLoadVisibilityUndefinedIds) {
                        PeoplePagePhotoLoaderCallback.this.mLastLoadVisibilityUndefinedIds.clear();
                        PeoplePagePhotoLoaderCallback.this.mLastLoadVisibilityUndefinedIds.addAll(arrayList);
                    }
                    return null;
                }
            });
        }

        public final boolean initMarkMyselfHelper() {
            if (PeoplePageFragment.this.mInMarkMode) {
                return false;
            }
            if (PeoplePageFragment.this.mMarkMyselfHelper == null) {
                PeoplePageFragment peoplePageFragment = PeoplePageFragment.this;
                peoplePageFragment.mMarkMyselfHelper = new MarkMyselfViewHelper(peoplePageFragment);
            }
            return PeoplePageFragment.this.mMarkMyselfHelper.onStart();
        }

        public final void statLoadTime(String str, long j, int i) {
            HashMap hashMap = new HashMap();
            hashMap.put("loader", str);
            hashMap.put("cost_time", String.valueOf(j));
            hashMap.put(MiStat.Param.COUNT, String.valueOf(i));
            SamplingStatHelper.recordCountEvent("people", "people_load_time", hashMap);
        }

        public final String loaderId2Name(int i) {
            return i != 1 ? i != 2 ? String.valueOf(i) : "people_page_photos" : "people_page_snapshot";
        }
    }

    public final void loadPeoplePage() {
        ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.ui.PeoplePageFragment$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                PeoplePageFragment.$r8$lambda$fBZLf_Fkf9lwEZVDM5R6cx76i9U(PeoplePageFragment.this);
            }
        });
    }

    public /* synthetic */ void lambda$loadPeoplePage$3() {
        this.mDisplayPeopleMode = DisplayPeopleMode.DISPLAY_PARTIAL_PEOPLE;
        getLoaderManager().initLoader(2, null, this.mPeoplePagePhotoLoaderCallback);
    }

    public final void destroySnapshotLoader() {
        ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.ui.PeoplePageFragment.2
            {
                PeoplePageFragment.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (PeoplePageFragment.this.getLoaderManager().getLoader(1) != null) {
                    PeoplePageFragment.this.getLoaderManager().destroyLoader(1);
                }
            }
        });
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        String string;
        super.onActivityCreated(bundle);
        this.mPeoplePagePhotoLoaderCallback = new PeoplePagePhotoLoaderCallback(this, null);
        if (!AIAlbumStatusHelper.isFaceAlbumEnabled()) {
            this.mShowEmptyViewHelper.initializeEmptyView(this.mEmptyViewStub, true).setVisibility(0);
            this.mRecyclerView.setVisibility(8);
        } else {
            getLoaderManager().initLoader(1, null, this.mPeoplePagePhotoLoaderCallback);
        }
        Intent intent = this.mActivity.getIntent();
        if (intent.getData() != null) {
            Uri data = intent.getData();
            boolean booleanQueryParameter = data.getBooleanQueryParameter("markMode", false);
            this.mInMarkMode = booleanQueryParameter;
            if (!booleanQueryParameter) {
                return;
            }
            this.mMarkName = data.getQueryParameter("markName");
            String queryParameter = data.getQueryParameter("markRelation");
            if (!TextUtils.isEmpty(queryParameter)) {
                PeopleContactInfo.Relation relation = PeopleContactInfo.getRelation(PeopleContactInfo.getRelationType(queryParameter));
                this.mMarkRelation = relation;
                if (relation == PeopleContactInfo.Relation.unknown) {
                    DefaultLogger.w("PeoplePageFragment", "Do not support mark unknown group type");
                    this.mMarkRelation = null;
                }
            }
            if (TextUtils.isEmpty(this.mMarkName) && this.mMarkRelation == null) {
                DefaultLogger.e("PeoplePageFragment", "Couldn't find valid mark arguments!");
                finish();
            }
            if (!TextUtils.isEmpty(this.mMarkName)) {
                string = getString(R.string.people_mark_mode_title, this.mMarkName);
            } else {
                PeopleContactInfo.Relation relation2 = this.mMarkRelation;
                string = relation2 != null ? getString(R.string.people_mark_mode_title, PeopleContactInfo.getRelationName(relation2)) : getString(R.string.people_mark_mode_title_no_mark_name);
            }
            this.mActivity.getAppCompatActionBar().setTitle(string);
        }
    }

    @Override // com.miui.gallery.ui.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        this.mShowEmptyViewHelper.resume();
    }

    @Override // com.miui.gallery.ui.BaseFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        this.mShowEmptyViewHelper.pause();
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        MarkMyselfViewHelper markMyselfViewHelper = this.mMarkMyselfHelper;
        if (markMyselfViewHelper != null) {
            markMyselfViewHelper.onStop();
        }
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        this.mShowEmptyViewHelper.destroy();
        super.onDestroyView();
    }

    @Override // androidx.fragment.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != R.id.action_see_ignore_faces) {
            return false;
        }
        TrackController.trackClick("403.47.2.1.11259", AutoTracking.getRef());
        this.mActivity.startActivity(new Intent(this.mActivity, IgnorePeoplePageActivity.class));
        return true;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 16 || i == 17 || i == 19) {
            PeopleContactInfo peopleContactInfo = null;
            if (intent != null && i2 == -1) {
                peopleContactInfo = InputFaceNameFragment.getContactInfo(this.mActivity, intent);
            }
            this.mFaceAlbumRenameHandler.finishWhenGetContact(peopleContactInfo);
        } else if (i == 41) {
            MarkMyselfViewHelper markMyselfViewHelper = this.mMarkMyselfHelper;
            if (markMyselfViewHelper == null) {
                return;
            }
            markMyselfViewHelper.setLoadMoreMarkResult(i2 == -1);
        } else {
            super.onActivityResult(i, i2, intent);
        }
    }

    public final void ignoreFaceAlbum(long[] jArr, ActionMode actionMode) {
        ArrayList arrayList = new ArrayList();
        for (long j : jArr) {
            arrayList.add(Long.valueOf(j));
        }
        FaceDataManager.safeIgnorePeopleByIds(arrayList);
        actionMode.finish();
    }

    public final void showIgnoreFaceAlbumAlert(final long[] jArr, final ActionMode actionMode) {
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.PeoplePageFragment.4
            {
                PeoplePageFragment.this = this;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                PeoplePageFragment.this.ignoreFaceAlbum(jArr, actionMode);
                SamplingStatHelper.recordCountEvent("people", "people_ignore_album");
            }
        };
        DialogInterface.OnClickListener onClickListener2 = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.PeoplePageFragment.5
            {
                PeoplePageFragment.this = this;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                actionMode.finish();
            }
        };
        FaceAlbumIgnoreTipFragment faceAlbumIgnoreTipFragment = new FaceAlbumIgnoreTipFragment();
        faceAlbumIgnoreTipFragment.setConfirmAndCancelListener(onClickListener, onClickListener2);
        faceAlbumIgnoreTipFragment.showAllowingStateLoss(getFragmentManager(), "FaceAlbumIgnoreTipFragment");
    }

    public final void mergePeople(long[] jArr, final ActionMode actionMode) {
        ArrayList<FaceManager.BasicPeopleInfo> peopleBasicInfoByIds = FaceManager.getPeopleBasicInfoByIds(jArr);
        if (peopleBasicInfoByIds == null || peopleBasicInfoByIds.isEmpty()) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        Iterator<FaceManager.BasicPeopleInfo> it = peopleBasicInfoByIds.iterator();
        while (it.hasNext()) {
            FaceManager.BasicPeopleInfo next = it.next();
            arrayList.add(new NormalPeopleFaceMediaSet(next.id, next.serverId, next.name));
        }
        FaceAlbumRenameHandler faceAlbumRenameHandler = new FaceAlbumRenameHandler(this.mActivity, arrayList, new FaceAlbumRenameHandler.ConfirmListener() { // from class: com.miui.gallery.ui.PeoplePageFragment.6
            {
                PeoplePageFragment.this = this;
            }

            @Override // com.miui.gallery.ui.renameface.FaceAlbumRenameHandler.ConfirmListener
            public void onConfirm(String str, boolean z) {
                PeoplePageFragment.this.mHandler.post(new Runnable() { // from class: com.miui.gallery.ui.PeoplePageFragment.6.1
                    {
                        AnonymousClass6.this = this;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        actionMode.finish();
                    }
                });
                SamplingStatHelper.recordCountEvent("people", "people_merge_album");
            }
        });
        this.mFaceAlbumRenameHandler = faceAlbumRenameHandler;
        faceAlbumRenameHandler.show();
    }

    public final void showAndSetRelationDialog(final long[] jArr, String str, final ActionMode actionMode) {
        PeopleRelationSetDialogFragment.createRelationSetDialog(this.mActivity, this.mActivity.getString(R.string.set_group), str, jArr.length, this.mPeopleCountOfMyself, new PeopleRelationSetDialogFragment.RelationSelectedListener() { // from class: com.miui.gallery.ui.PeoplePageFragment.7
            {
                PeoplePageFragment.this = this;
            }

            @Override // com.miui.gallery.ui.PeopleRelationSetDialogFragment.RelationSelectedListener
            public void onRelationSelected(final String str2, final String str3) {
                actionMode.finish();
                ThreadManager.getMiscPool().submit(new ThreadPool.Job<Void>() { // from class: com.miui.gallery.ui.PeoplePageFragment.7.1
                    {
                        AnonymousClass7.this = this;
                    }

                    @Override // com.miui.gallery.concurrent.ThreadPool.Job
                    /* renamed from: run */
                    public Void mo1807run(ThreadPool.JobContext jobContext) {
                        NormalPeopleFaceMediaSet.moveFaceToRelationGroup(jArr, str2, str3);
                        return null;
                    }
                });
            }
        });
    }

    @Override // com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateConfiguration(configuration);
        if (this.mDisplayPeopleMode != DisplayPeopleMode.DISPLAY_PARTIAL_PEOPLE || getLoaderManager().getLoader(2) == null) {
            return;
        }
        getLoaderManager().getLoader(2).forceLoad();
    }

    public final void updateConfiguration(Configuration configuration) {
        this.mIsInMultiWindow = ActivityCompat.isInMultiWindowMode(this.mActivity) && !BaseBuildUtil.isLargeHorizontalWindow();
        int findFirstVisibleItemPosition = this.mRecyclerView.findFirstVisibleItemPosition();
        int dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.people_page_list_padding_left_right);
        this.mRecyclerView.setPadding(dimensionPixelOffset, 0, dimensionPixelOffset, 0);
        this.mSpacingDecoration.setHorizontalSpacing(getResources().getDimensionPixelOffset(R.dimen.people_face_horizontal_spacing));
        this.mSpacingDecoration.setVerticalSpacing(getResources().getDimensionPixelOffset(R.dimen.people_face_vertical_spacing));
        this.mRecyclerView.invalidateItemDecorations();
        if (configuration.orientation == 1 || this.mIsInMultiWindow) {
            ((GridLayoutManager) this.mRecyclerView.getLayoutManager()).setSpanCount(getResources().getInteger(R.integer.people_face_grid_view_columns));
        } else {
            ((GridLayoutManager) this.mRecyclerView.getLayoutManager()).setSpanCount(getResources().getInteger(R.integer.people_face_grid_view_columns_land));
        }
        this.mRecyclerView.scrollToPosition(findFirstVisibleItemPosition);
    }

    public final void finishWithMarkSuccessResult(String str, String str2, String str3, String str4, String str5) {
        ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.mark_operation_succeeded);
        Intent intent = new Intent();
        intent.putExtra("server_id_of_album", str2);
        intent.putExtra("local_id_of_album", str);
        intent.putExtra("album_name", str3);
        intent.putExtra("origin_album_name", str4);
        intent.putExtra("mark_album_name", str5);
        this.mActivity.setResult(-1, intent);
        this.mActivity.finish();
        this.mActivity.overridePendingTransition(0, 0);
    }

    public final void finishWithMarkSuccessResult(String str, String str2, String str3, String str4, int i) {
        ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.mark_operation_succeeded);
        Intent intent = new Intent();
        intent.putExtra("server_id_of_album", str2);
        intent.putExtra("local_id_of_album", str);
        intent.putExtra("mark_relation", i);
        intent.putExtra("origin_album_name", str3);
        intent.putExtra("mark_album_name", str4);
        this.mActivity.setResult(-1, intent);
        this.mActivity.finish();
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        PeoplePageAdapter peoplePageAdapter = this.mPeoplePageAdapter;
        if (peoplePageAdapter != null) {
            peoplePageAdapter.swapCursor(null);
        }
        this.mCompositeDisposable.dispose();
        super.onDestroy();
    }

    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> list, Menu menu, int i) {
        ArrayList arrayList = new ArrayList();
        if (this.mEditableWrapper.isInActionMode()) {
            arrayList.add(KeyboardShortcutGroupManager.getInstance().getSelectAllShortcutInfo());
        }
        list.add(new KeyboardShortcutGroup(getPageName(), arrayList));
    }

    public boolean onKeyShortcut(int i, KeyEvent keyEvent) {
        return KeyboardShortcutGroupManager.getInstance().onKeyShortcut(i, keyEvent, this.mShortcutCallback);
    }

    /* loaded from: classes2.dex */
    public class PeoplePageKeyboardShortcutCallback implements KeyboardShortcutGroupManager.OnKeyShortcutCallback {
        public PeoplePageKeyboardShortcutCallback() {
            PeoplePageFragment.this = r1;
        }

        public /* synthetic */ PeoplePageKeyboardShortcutCallback(PeoplePageFragment peoplePageFragment, AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onSelectAllPressed() {
            if (PeoplePageFragment.this.mEditableWrapper.isInActionMode()) {
                PeoplePageFragment.this.mEditableWrapper.setAllItemsCheckState(true);
            }
            return true;
        }
    }
}
