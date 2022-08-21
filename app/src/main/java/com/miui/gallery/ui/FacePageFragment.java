package com.miui.gallery.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GalleryGridLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.R;
import com.miui.gallery.activity.InternalPhotoPageActivity;
import com.miui.gallery.activity.PickPeopleCoverActivity;
import com.miui.gallery.activity.facebaby.FacePageActivity;
import com.miui.gallery.activity.facebaby.RecommendFacePageActivity;
import com.miui.gallery.adapter.FacePageAdapter;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.cloud.SpaceFullHandler;
import com.miui.gallery.cloud.peopleface.FaceDataManager;
import com.miui.gallery.cloud.peopleface.PeopleFace;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureHandler;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.glide.load.RegionConfig;
import com.miui.gallery.glide.load.model.GalleryModel;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.model.PeopleContactInfo;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.FaceManager;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.deprecated.NormalPeopleFaceMediaSet;
import com.miui.gallery.provider.deprecated.PeopleRecommendMediaSet;
import com.miui.gallery.share.Path;
import com.miui.gallery.share.UIHelper;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.DeletionTask;
import com.miui.gallery.ui.KeyboardShortcutGroupManager;
import com.miui.gallery.ui.renameface.FaceAlbumHandlerBase;
import com.miui.gallery.ui.renameface.FaceAlbumRenameHandler;
import com.miui.gallery.ui.renameface.InputFaceNameFragment;
import com.miui.gallery.ui.renameface.RemoveFromFaceAlbumHandler;
import com.miui.gallery.util.DialogUtil;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.MediaAndAlbumOperations;
import com.miui.gallery.util.PhotoPageIntent;
import com.miui.gallery.util.SoundUtils;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.baby.FindFace2CreateBabyAlbum;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.deviceprovider.ApplicationHelper;
import com.miui.gallery.util.face.CheckoutRecommendPeople;
import com.miui.gallery.util.face.FaceRegionRectF;
import com.miui.gallery.util.glide.BindImageHelper;
import com.miui.gallery.util.glide.GlideRequestManagerHelper;
import com.miui.gallery.widget.ActionMenuItemView;
import com.miui.gallery.widget.PagerGridLayout;
import com.miui.gallery.widget.editwrapper.EditableListViewWrapper;
import com.miui.gallery.widget.editwrapper.HeaderFooterWrapper;
import com.miui.gallery.widget.editwrapper.MultiChoiceModeListener;
import com.miui.gallery.widget.recyclerview.EditableListSpanSizeProvider;
import com.miui.gallery.widget.recyclerview.FastScrollerBar;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.miui.gallery.widget.recyclerview.GridItemSpacingDecoration;
import com.miui.gallery.widget.recyclerview.IrregularSpanSizeLookup;
import com.miui.gallery.widget.recyclerview.ItemClickSupport;
import com.miui.gallery.widget.recyclerview.SimpleHeaderFooterWrapperAdapter;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.AppCompatActivity;

/* loaded from: classes2.dex */
public class FacePageFragment extends BaseMediaFragment implements CheckoutRecommendPeople.CheckoutStatusListener, PagerGridLayout.OnPageChangedListener {
    public FacePageAdapter mAdapter;
    public String mAlbumName;
    public CheckoutRecommendPeople mCheckoutRecommendPeopleTask;
    public Future mCoverRefreshTask;
    public EditableListViewWrapper mEditableWrapper;
    public FaceAlbumRenameHandler mFaceAlbumMergeHandler;
    public FaceAlbumRenameHandler mFaceAlbumRenameHandler;
    public View mFaceCoverHeader;
    public String mFaceCoverPath;
    public RectF mFaceCoverRectF;
    public ImageView mFaceCoverView;
    public NormalPeopleFaceMediaSet mFaceMediaSet;
    public ArrayList<NormalPeopleFaceMediaSet> mFaceMediaSetList;
    public FacePagePhotoLoaderCallback mFacePagePhotoLoaderCallback;
    public View mFooterView;
    public GridLayoutManager mGridLayoutManager;
    public boolean mHasRequestRecommendFace;
    public SimpleHeaderFooterWrapperAdapter mHeaderFooterWrapperAdapter;
    public ChoiceModeListener mListener;
    public long mLocalIdOfAlbum;
    public Future mNameRefreshTask;
    public PeopleRecommendMediaSet mPeopleRecommendMediaSet;
    public RecommendFaceGroupAdapter mRecommendFaceAdapter;
    public View mRecommendFaceButtonContainer;
    public PagerGridLayout mRecommendFaceGroup;
    public View mRecommendFaceGroupHeader;
    public String mRecommendFaceIds;
    public GalleryRecyclerView mRecyclerView;
    public int mRelationType;
    public RemoveFromFaceAlbumHandler mRemoveFromFaceAlbumHandler;
    public String mServerIdOfAlbum;
    public View mShareButtonContainer;
    public GridItemSpacingDecoration mSpacingDecoration;
    public boolean mAddFooterView = false;
    public Handler mHandler = new Handler();
    public FaceKeyboardShortcutCallback mShortcutCallback = new FaceKeyboardShortcutCallback();

    /* renamed from: $r8$lambda$CpMMbdbsmw2Gmx7D7mFO3-VOSrg */
    public static /* synthetic */ void m1447$r8$lambda$CpMMbdbsmw2Gmx7D7mFO3VOSrg(FacePageFragment facePageFragment, int i) {
        facePageFragment.lambda$onInflateView$0(i);
    }

    public static /* synthetic */ void $r8$lambda$DXjf2vBl7P3DvcWeP5EfHZ3pp4E(FacePageFragment facePageFragment, View view) {
        facePageFragment.lambda$addFaceCoverHeader$4(view);
    }

    public static /* synthetic */ void $r8$lambda$MGGDtXEfOPMkB2mkxd3dlj_h3rQ(FacePageFragment facePageFragment, View view) {
        facePageFragment.lambda$onInflateView$3(view);
    }

    public static /* synthetic */ int $r8$lambda$uQ57c858PI3YvEKoqgDCD6n0tRA(FacePageFragment facePageFragment, int i) {
        return facePageFragment.lambda$onInflateView$2(i);
    }

    /* renamed from: $r8$lambda$yZFVydfm1CS11LjZ-ttWrZqN6to */
    public static /* synthetic */ AbstractHeaderFooterWrapperAdapter m1448$r8$lambda$yZFVydfm1CS11LjZttWrZqN6to(FacePageFragment facePageFragment, RecyclerView.Adapter adapter) {
        return facePageFragment.lambda$onInflateView$1(adapter);
    }

    public final String getOrderBy() {
        return "dateTaken DESC ";
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return "face";
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.face_page, viewGroup, false);
        this.mAdapter = new FacePageAdapter(this.mActivity, getLifecycle());
        GalleryRecyclerView galleryRecyclerView = (GalleryRecyclerView) inflate.findViewById(R.id.grid);
        this.mRecyclerView = galleryRecyclerView;
        galleryRecyclerView.setItemAnimator(null);
        GridItemSpacingDecoration gridItemSpacingDecoration = new GridItemSpacingDecoration(this.mRecyclerView, false, this.mActivity.getResources().getDimensionPixelSize(R.dimen.micro_thumb_horizontal_spacing), this.mActivity.getResources().getDimensionPixelSize(R.dimen.micro_thumb_vertical_spacing));
        this.mSpacingDecoration = gridItemSpacingDecoration;
        this.mRecyclerView.addItemDecoration(gridItemSpacingDecoration);
        this.mRecyclerView.setFastScrollEnabled(true);
        this.mRecyclerView.setOnFastScrollerStateChangedListener(new FastScrollerBar.OnStateChangedListener() { // from class: com.miui.gallery.ui.FacePageFragment$$ExternalSyntheticLambda3
            @Override // com.miui.gallery.widget.recyclerview.FastScrollerBar.OnStateChangedListener
            public final void onStateChanged(int i) {
                FacePageFragment.m1447$r8$lambda$CpMMbdbsmw2Gmx7D7mFO3VOSrg(FacePageFragment.this, i);
            }
        });
        this.mEditableWrapper = new EditableListViewWrapper(this.mRecyclerView);
        GalleryGridLayoutManager galleryGridLayoutManager = new GalleryGridLayoutManager(this.mActivity, Config$ThumbConfig.get().sMicroThumbColumnsPortrait);
        this.mGridLayoutManager = galleryGridLayoutManager;
        galleryGridLayoutManager.setSpanSizeLookup(IrregularSpanSizeLookup.create(new EditableListSpanSizeProvider(this.mEditableWrapper, galleryGridLayoutManager)));
        this.mEditableWrapper.setLayoutManager(this.mGridLayoutManager);
        this.mEditableWrapper.setHandleTouchAnimItemType(MicroThumbGridItem.class.getSimpleName());
        this.mEditableWrapper.setAdapter(this.mAdapter, new HeaderFooterWrapper() { // from class: com.miui.gallery.ui.FacePageFragment$$ExternalSyntheticLambda2
            @Override // com.miui.gallery.widget.editwrapper.HeaderFooterWrapper
            public final AbstractHeaderFooterWrapperAdapter wrap(RecyclerView.Adapter adapter) {
                return FacePageFragment.m1448$r8$lambda$yZFVydfm1CS11LjZttWrZqN6to(FacePageFragment.this, adapter);
            }
        });
        this.mRecyclerView.setAdapterPos2ViewPosConverter(new GalleryRecyclerView.AdapterPos2ViewPosConverter() { // from class: com.miui.gallery.ui.FacePageFragment$$ExternalSyntheticLambda4
            @Override // com.miui.gallery.widget.recyclerview.GalleryRecyclerView.AdapterPos2ViewPosConverter
            public final int convert(int i) {
                return FacePageFragment.$r8$lambda$uQ57c858PI3YvEKoqgDCD6n0tRA(FacePageFragment.this, i);
            }
        });
        this.mEditableWrapper.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() { // from class: com.miui.gallery.ui.FacePageFragment.1
            {
                FacePageFragment.this = this;
            }

            @Override // com.miui.gallery.widget.recyclerview.ItemClickSupport.OnItemClickListener
            public boolean onItemClick(RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
                FacePageAdapter facePageAdapter = FacePageFragment.this.mAdapter;
                new PhotoPageIntent.Builder(FacePageFragment.this, InternalPhotoPageActivity.class).setAdapterView(recyclerView).setUri(GalleryContract.PeopleFace.ONE_PERSON_URI).setInitPosition(i).setCount(facePageAdapter.getItemCount()).setSelectionArgs(FacePageFragment.this.getSelectionArgs()).setOrderBy(FacePageFragment.this.getOrderBy()).setImageLoadParams(new ImageLoadParams.Builder().setKey(facePageAdapter.getItemKey(i)).setFilePath(facePageAdapter.getBindImagePath(i)).setTargetSize(Config$ThumbConfig.get().sMicroTargetSize).setInitPosition(i).setMimeType(facePageAdapter.getMimeType(i)).setFileLength(facePageAdapter.getFileLength(i)).setCreateTime(facePageAdapter.getCreateTime(i)).setLocation(facePageAdapter.getLocation(i)).fromFace(true).build()).setAlbumName(FacePageFragment.this.mAlbumName).setUnfoldBurst(true).build().gotoPhotoPage();
                HashMap hashMap = new HashMap();
                hashMap.put("from", FacePageFragment.this.getPageName());
                hashMap.put("position", Integer.valueOf(i));
                SamplingStatHelper.recordCountEvent("photo", "click_micro_thumb", hashMap);
                return true;
            }
        });
        View inflate2 = LayoutInflater.from(this.mActivity).inflate(R.layout.see_more_recommend_face_view, (ViewGroup) this.mRecyclerView, false);
        this.mFooterView = inflate2;
        View findViewById = inflate2.findViewById(R.id.see_more_recommend_face_container);
        this.mRecommendFaceButtonContainer = findViewById;
        findViewById.findViewById(R.id.see_more_recommend_face).setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.FacePageFragment$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                FacePageFragment.$r8$lambda$MGGDtXEfOPMkB2mkxd3dlj_h3rQ(FacePageFragment.this, view);
            }
        });
        int intExtra = this.mActivity.getIntent().getIntExtra("relationType", -1);
        this.mRelationType = intExtra;
        if (PeopleContactInfo.isBabyRelation(intExtra) && ApplicationHelper.supportShare()) {
            View findViewById2 = inflate.findViewById(R.id.share_container);
            this.mShareButtonContainer = findViewById2;
            findViewById2.setVisibility(0);
            final ActionMenuItemView actionMenuItemView = (ActionMenuItemView) this.mShareButtonContainer.findViewById(R.id.share_button);
            FolmeUtil.setCustomTouchAnim(actionMenuItemView, new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build(), null, null, true);
            actionMenuItemView.findViewById(R.id.share_button).setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.FacePageFragment.2
                {
                    FacePageFragment.this = this;
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    long isHasEverNotCreateBabyAlbumFromThis = FacePageFragment.this.getIsHasEverNotCreateBabyAlbumFromThis();
                    if (isHasEverNotCreateBabyAlbumFromThis == -1) {
                        FacePageFragment.this.toast2CreateBabyAlbumBeforeShare();
                        return;
                    }
                    LinearMotorHelper.performHapticFeedback(actionMenuItemView, LinearMotorHelper.HAPTIC_TAP_LIGHT);
                    UIHelper.showAlbumShareInfo(FacePageFragment.this.mActivity, new Path(isHasEverNotCreateBabyAlbumFromThis, false, true), 0);
                    SamplingStatHelper.recordCountEvent("face", "face_enter_album_share");
                }
            });
        }
        this.mEditableWrapper.enableChoiceMode(true);
        this.mEditableWrapper.enterChoiceModeWithLongClick(true);
        ChoiceModeListener choiceModeListener = new ChoiceModeListener();
        this.mListener = choiceModeListener;
        this.mEditableWrapper.setMultiChoiceModeListener(choiceModeListener);
        updateConfiguration(getResources().getConfiguration());
        return inflate;
    }

    public /* synthetic */ void lambda$onInflateView$0(int i) {
        EditableListViewWrapper editableListViewWrapper = this.mEditableWrapper;
        if (editableListViewWrapper != null) {
            editableListViewWrapper.reductionTouchView();
        }
    }

    public /* synthetic */ AbstractHeaderFooterWrapperAdapter lambda$onInflateView$1(RecyclerView.Adapter adapter) {
        SimpleHeaderFooterWrapperAdapter simpleHeaderFooterWrapperAdapter = new SimpleHeaderFooterWrapperAdapter(adapter);
        this.mHeaderFooterWrapperAdapter = simpleHeaderFooterWrapperAdapter;
        return simpleHeaderFooterWrapperAdapter;
    }

    public /* synthetic */ int lambda$onInflateView$2(int i) {
        return this.mEditableWrapper.getRawPosition(i);
    }

    public /* synthetic */ void lambda$onInflateView$3(View view) {
        this.mPeopleRecommendMediaSet.refreshRecommendInfo();
        Intent intent = new Intent(this.mActivity, RecommendFacePageActivity.class);
        intent.putExtra("server_id_of_album", getServerIdOfAlbum());
        intent.putExtra("local_id_of_album", this.mLocalIdOfAlbum);
        intent.putExtra("album_name", getAlbumName());
        intent.putExtra("server_ids_of_faces", this.mPeopleRecommendMediaSet.getServerIdsIn());
        startActivityForResult(intent, 21);
        SamplingStatHelper.recordCountEvent("face", "face_enter_recommend");
    }

    @Override // com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateConfiguration(configuration);
        this.mRecyclerView.scrollToPosition(this.mRecyclerView.findFirstVisibleItemPosition());
    }

    public final void updateConfiguration(Configuration configuration) {
        int i;
        if (configuration.orientation == 2) {
            i = Config$ThumbConfig.get().sMicroThumbColumnsLand;
        } else {
            i = Config$ThumbConfig.get().sMicroThumbColumnsPortrait;
        }
        ((GridLayoutManager) this.mRecyclerView.getLayoutManager()).setSpanCount(i);
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment
    public List<Loader<?>> getLoaders() {
        return Collections.singletonList(LoaderManager.getInstance(this).getLoader(1));
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, com.miui.gallery.ui.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        refreshFaceNameIfNeeded();
        seeIfHasRecommendFace();
        PagerGridLayout pagerGridLayout = this.mRecommendFaceGroup;
        if (pagerGridLayout != null) {
            pagerGridLayout.freshCurrentPage();
        }
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        CheckoutRecommendPeople checkoutRecommendPeople = this.mCheckoutRecommendPeopleTask;
        if (checkoutRecommendPeople != null) {
            checkoutRecommendPeople.clearListener();
        }
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        RecommendFaceGroupAdapter recommendFaceGroupAdapter = this.mRecommendFaceAdapter;
        if (recommendFaceGroupAdapter != null) {
            recommendFaceGroupAdapter.setOnLoadingCompleteListener(null);
        }
        cancelTask(this.mNameRefreshTask);
        cancelTask(this.mCoverRefreshTask);
        FacePageAdapter facePageAdapter = this.mAdapter;
        if (facePageAdapter != null) {
            facePageAdapter.swapCursor(null);
        }
    }

    public final void changeVisibilityOfShareContainer(int i) {
        View view = this.mShareButtonContainer;
        if (view != null) {
            view.setVisibility(i);
        }
    }

    public final void toast2CreateBabyAlbumBeforeShare() {
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.FacePageFragment.3
            {
                FacePageFragment.this = this;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (SpaceFullHandler.isOwnerSpaceFull()) {
                    ToastUtils.makeText(FacePageFragment.this.mActivity, (int) R.string.backup_cloud_space_low_title);
                    return;
                }
                FindFace2CreateBabyAlbum.gotoFillBabyAlbumInfo(FacePageFragment.this.mActivity, new NormalPeopleFaceMediaSet(FacePageFragment.this.mLocalIdOfAlbum, FacePageFragment.this.getServerIdOfAlbum(), FacePageFragment.this.getAlbumName()), FacePageFragment.this.mAdapter.getFirstFaceServerId());
                SamplingStatHelper.recordCountEvent("face", "face_create_baby_album");
            }
        };
        try {
            new AlertDialog.Builder(this.mActivity).setCancelable(true).setIconAttribute(16843605).setMessage(R.string.begin_share_toast).setPositiveButton(this.mActivity.getString(17039370), onClickListener).setNegativeButton(this.mActivity.getString(17039360), (DialogInterface.OnClickListener) null).setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.miui.gallery.ui.FacePageFragment.4
                @Override // android.content.DialogInterface.OnCancelListener
                public void onCancel(DialogInterface dialogInterface) {
                }

                {
                    FacePageFragment.this = this;
                }
            }).create().show();
        } catch (Exception unused) {
        }
    }

    public final void seeIfHasRecommendFace() {
        if (TextUtils.isEmpty(this.mServerIdOfAlbum)) {
            return;
        }
        ThreadManager.getMiscPool().submit(new ThreadPool.Job<Void>() { // from class: com.miui.gallery.ui.FacePageFragment.5
            {
                FacePageFragment.this = this;
            }

            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public Void mo1807run(ThreadPool.JobContext jobContext) {
                if (!FacePageFragment.this.mHasRequestRecommendFace) {
                    FacePageFragment facePageFragment = FacePageFragment.this;
                    facePageFragment.mCheckoutRecommendPeopleTask = new CheckoutRecommendPeople(null, facePageFragment);
                    FacePageFragment.this.mCheckoutRecommendPeopleTask.getRecommendPeopleFromNet(FacePageFragment.this.getServerIdOfAlbum());
                    FacePageFragment.this.mHasRequestRecommendFace = true;
                }
                return null;
            }
        });
    }

    @Override // com.miui.gallery.util.face.CheckoutRecommendPeople.CheckoutStatusListener
    public void onFinishCheckoutPeopleFace(int i) {
        this.mPeopleRecommendMediaSet.refreshRecommendInfo();
    }

    public final void freshFacePhotoCount() {
        int itemCount = this.mAdapter.getItemCount();
        ((TextView) this.mFaceCoverHeader.findViewById(R.id.photo_count)).setText(this.mActivity.getResources().getQuantityString(R.plurals.face_count, itemCount, Integer.valueOf(itemCount)));
    }

    public void changeDisplayMode() {
        this.mAdapter.changeDisplayMode();
    }

    public boolean isFaceDisplayMode() {
        return this.mAdapter.isFaceDisplayMode();
    }

    public final String[] getSelectionArgs() {
        return new String[]{this.mServerIdOfAlbum, String.valueOf(this.mLocalIdOfAlbum)};
    }

    /* loaded from: classes2.dex */
    public class FacePagePhotoLoaderCallback implements LoaderManager.LoaderCallbacks {
        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader loader) {
        }

        public FacePagePhotoLoaderCallback() {
            FacePageFragment.this = r1;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader onCreateLoader(int i, Bundle bundle) {
            CursorLoader cursorLoader = new CursorLoader(FacePageFragment.this.mActivity);
            cursorLoader.setUri(GalleryContract.PeopleFace.ONE_PERSON_URI.buildUpon().appendQueryParameter("generate_headers", String.valueOf(true)).build());
            cursorLoader.setProjection(FacePageAdapter.PROJECTION);
            cursorLoader.setSelectionArgs(FacePageFragment.this.getSelectionArgs());
            cursorLoader.setSortOrder(FacePageFragment.this.getOrderBy());
            return cursorLoader;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader loader, Object obj) {
            FacePageFragment.this.mAdapter.swapCursor((Cursor) obj);
            FacePageFragment.this.freshFacePhotoCount();
            FacePageFragment.this.refreshFaceCover();
            if (FacePageFragment.this.mAddFooterView) {
                FacePageFragment.this.mHeaderFooterWrapperAdapter.addFooter(FacePageFragment.this.mFooterView);
                FacePageFragment.this.mAddFooterView = false;
            }
        }
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        Bundle extras = this.mActivity.getIntent().getExtras();
        this.mServerIdOfAlbum = extras.getString("server_id_of_album");
        this.mLocalIdOfAlbum = Long.parseLong(extras.getString("local_id_of_album"));
        this.mFaceCoverPath = extras.getString("face_album_cover");
        this.mFaceCoverRectF = (RectF) extras.getParcelable("face_position_rect");
        this.mAlbumName = extras.getString("album_name");
        this.mFacePagePhotoLoaderCallback = new FacePagePhotoLoaderCallback();
        getLoaderManager().initLoader(1, null, this.mFacePagePhotoLoaderCallback);
        addHeaderView();
        setTitle();
        if (bundle != null) {
            NormalPeopleFaceMediaSet normalPeopleFaceMediaSet = (NormalPeopleFaceMediaSet) bundle.getParcelable("NormalPeopleFaceMediaset");
            this.mFaceMediaSet = normalPeopleFaceMediaSet;
            if (normalPeopleFaceMediaSet != null) {
                this.mFaceAlbumRenameHandler = new FaceAlbumRenameHandler(this.mActivity, normalPeopleFaceMediaSet, confirmListener(), true ^ PeopleContactInfo.isUnKnownRelation(this.mRelationType));
            }
            ArrayList<NormalPeopleFaceMediaSet> parcelableArrayList = bundle.getParcelableArrayList("NormalPeopleFaceMediasetList");
            this.mFaceMediaSetList = parcelableArrayList;
            if (parcelableArrayList == null) {
                return;
            }
            this.mFaceAlbumMergeHandler = new FaceAlbumRenameHandler(this.mActivity, parcelableArrayList, confirmListener());
        }
    }

    @Override // com.miui.gallery.app.fragment.GalleryFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        NormalPeopleFaceMediaSet normalPeopleFaceMediaSet = this.mFaceMediaSet;
        if (normalPeopleFaceMediaSet != null) {
            bundle.putParcelable("NormalPeopleFaceMediaset", normalPeopleFaceMediaSet);
        }
        ArrayList<NormalPeopleFaceMediaSet> arrayList = this.mFaceMediaSetList;
        if (arrayList != null) {
            bundle.putParcelableArrayList("NormalPeopleFaceMediasetList", arrayList);
        }
    }

    public final void setTitle() {
        AppCompatActivity appCompatActivity = this.mActivity;
        if (appCompatActivity != null) {
            appCompatActivity.getAppCompatActionBar().setTitle(getString(R.string.face_album_title, this.mAlbumName));
        }
        ((TextView) this.mFaceCoverHeader.findViewById(R.id.face_name)).setText(this.mAlbumName);
    }

    public final void addHeaderView() {
        addFaceCoverHeader();
        addRecommendGroupHeader();
        this.mRecyclerView.postDelayed(new Runnable() { // from class: com.miui.gallery.ui.FacePageFragment.6
            {
                FacePageFragment.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (FacePageFragment.this.mFaceCoverHeader != null) {
                    FacePageFragment facePageFragment = FacePageFragment.this;
                    facePageFragment.mFastScrollerMarginTop += facePageFragment.mFaceCoverHeader.getHeight();
                }
                if (FacePageFragment.this.mRecommendFaceGroupHeader != null) {
                    FacePageFragment facePageFragment2 = FacePageFragment.this;
                    facePageFragment2.mFastScrollerMarginTop += facePageFragment2.mRecommendFaceGroupHeader.getHeight();
                }
                FacePageFragment.this.mRecyclerView.setFastScrollerTopMargin(FacePageFragment.this.mFastScrollerMarginTop);
            }
        }, 500L);
    }

    public final void addFaceCoverHeader() {
        View inflate = LayoutInflater.from(this.mActivity).inflate(R.layout.face_page_face_cover_header, (ViewGroup) this.mRecyclerView, false);
        this.mFaceCoverHeader = inflate;
        this.mFaceCoverView = (ImageView) inflate.findViewById(R.id.face_cover);
        displayFaceCover();
        TextView textView = (TextView) this.mFaceCoverHeader.findViewById(R.id.face_name_edit);
        FolmeUtil.setDefaultTouchAnim(textView, null, false, false, true);
        textView.setText(getString(R.string.face_name_input));
        textView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.FacePageFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                FacePageFragment.$r8$lambda$DXjf2vBl7P3DvcWeP5EfHZ3pp4E(FacePageFragment.this, view);
            }
        });
        this.mHeaderFooterWrapperAdapter.addHeader(this.mFaceCoverHeader);
        this.mFaceCoverView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.FacePageFragment.7
            {
                FacePageFragment.this = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("server_id_of_album", FacePageFragment.this.mServerIdOfAlbum);
                bundle.putLong("local_id_of_album", FacePageFragment.this.mLocalIdOfAlbum);
                intent.putExtras(bundle);
                intent.setClass(FacePageFragment.this.mActivity, PickPeopleCoverActivity.class);
                FacePageFragment.this.startActivityForResult(intent, 55);
                SamplingStatHelper.recordCountEvent("face", "face_album_pick_cover");
                TrackController.trackClick("403.47.1.1.11252", AutoTracking.getRef());
            }
        });
    }

    public /* synthetic */ void lambda$addFaceCoverHeader$4(View view) {
        showRenameHandler();
    }

    public final void displayFaceCover() {
        RequestManager safeGet;
        GlideOptions decodeRegion;
        if (this.mFaceCoverPath == null || !isAdded() || (safeGet = GlideRequestManagerHelper.safeGet(this.mFaceCoverView)) == null) {
            return;
        }
        RequestBuilder<Bitmap> mo962load = safeGet.mo985asBitmap().mo962load(GalleryModel.of(this.mFaceCoverPath));
        RectF rectF = this.mFaceCoverRectF;
        if (rectF instanceof FaceRegionRectF) {
            decodeRegion = GlideOptions.peopleFaceOf((FaceRegionRectF) rectF);
        } else {
            decodeRegion = GlideOptions.microThumbOf().decodeRegion(RegionConfig.of(this.mFaceCoverRectF));
        }
        mo962load.mo946apply((BaseRequestOptions<?>) decodeRegion).mo945addListener(new RequestListener<Bitmap>() { // from class: com.miui.gallery.ui.FacePageFragment.8
            @Override // com.bumptech.glide.request.RequestListener
            public boolean onLoadFailed(GlideException glideException, Object obj, Target<Bitmap> target, boolean z) {
                return false;
            }

            {
                FacePageFragment.this = this;
            }

            @Override // com.bumptech.glide.request.RequestListener
            public boolean onResourceReady(Bitmap bitmap, Object obj, Target<Bitmap> target, DataSource dataSource, boolean z) {
                FacePageFragment.this.invalidateFaceGridView();
                return false;
            }
        }).into(this.mFaceCoverView);
    }

    public final void invalidateFaceGridView() {
        GalleryRecyclerView galleryRecyclerView = this.mRecyclerView;
        if (galleryRecyclerView != null) {
            galleryRecyclerView.postInvalidate();
            TimeMonitor.trackTimeMonitor("403.17.0.1.13786", this.mAdapter.getItemCount());
        }
    }

    public final void refreshFaceCover() {
        if (this.mServerIdOfAlbum != null || this.mLocalIdOfAlbum >= 0) {
            cancelTask(this.mCoverRefreshTask);
            this.mCoverRefreshTask = ThreadManager.getMiscPool().submit(new ThreadPool.Job<Boolean>() { // from class: com.miui.gallery.ui.FacePageFragment.9
                {
                    FacePageFragment.this = this;
                }

                @Override // com.miui.gallery.concurrent.ThreadPool.Job
                /* renamed from: run */
                public Boolean mo1807run(ThreadPool.JobContext jobContext) {
                    Boolean bool = Boolean.FALSE;
                    FaceRegionRectF[] faceRegionRectFArr = new FaceRegionRectF[1];
                    String queryCoverImageOfOnePerson = FaceManager.queryCoverImageOfOnePerson(FacePageFragment.this.mServerIdOfAlbum, FacePageFragment.this.mLocalIdOfAlbum, faceRegionRectFArr);
                    if (TextUtils.isEmpty(queryCoverImageOfOnePerson) || queryCoverImageOfOnePerson.equals(FacePageFragment.this.mFaceCoverPath)) {
                        return bool;
                    }
                    FacePageFragment.this.mFaceCoverPath = queryCoverImageOfOnePerson;
                    FacePageFragment.this.mFaceCoverRectF = faceRegionRectFArr[0];
                    return Boolean.TRUE;
                }
            }, new FutureHandler<Boolean>() { // from class: com.miui.gallery.ui.FacePageFragment.10
                {
                    FacePageFragment.this = this;
                }

                @Override // com.miui.gallery.concurrent.FutureHandler
                public void onPostExecute(Future<Boolean> future) {
                    Boolean bool = future == null ? null : future.get();
                    if (bool == null || !bool.booleanValue()) {
                        return;
                    }
                    FacePageFragment.this.displayFaceCover();
                }
            });
        }
    }

    public final void refreshFaceNameIfNeeded() {
        cancelTask(this.mNameRefreshTask);
        this.mNameRefreshTask = ThreadManager.getMiscPool().submit(new ThreadPool.Job<String>() { // from class: com.miui.gallery.ui.FacePageFragment.11
            {
                FacePageFragment.this = this;
            }

            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public String mo1807run(ThreadPool.JobContext jobContext) {
                return FaceManager.queryPersonName(FacePageFragment.this.mLocalIdOfAlbum);
            }
        }, new FutureHandler<String>() { // from class: com.miui.gallery.ui.FacePageFragment.12
            {
                FacePageFragment.this = this;
            }

            @Override // com.miui.gallery.concurrent.FutureHandler
            public void onPostExecute(Future<String> future) {
                if (FacePageFragment.this.isAdded() && future != null) {
                    String str = future.get();
                    if (str != null && !str.equals(FacePageFragment.this.mAlbumName)) {
                        FacePageFragment.this.mAlbumName = str;
                    }
                    ((TextView) FacePageFragment.this.mFaceCoverHeader.findViewById(R.id.face_name_edit)).setText(FacePageFragment.this.getString(R.string.face_name_input));
                    FacePageFragment.this.setTitle();
                    FacePageFragment.this.invalidateFaceGridView();
                }
            }
        });
    }

    public final void cancelTask(Future future) {
        if (future == null || future.isCancelled()) {
            return;
        }
        future.cancel();
    }

    public final void addRecommendGroupHeader() {
        PeopleRecommendMediaSet peopleRecommendMediaSet = new PeopleRecommendMediaSet(new NormalPeopleFaceMediaSet(this.mLocalIdOfAlbum, getServerIdOfAlbum(), getAlbumName()));
        this.mPeopleRecommendMediaSet = peopleRecommendMediaSet;
        peopleRecommendMediaSet.refreshRecommendInfo();
        if (this.mPeopleRecommendMediaSet.getActualNeedRecommendPeopleFacePhotoNumber() > 0) {
            if (!GalleryPreferences.Face.isFaceRecommendGroupHidden(getServerIdOfAlbum())) {
                this.mRecommendFaceIds = this.mPeopleRecommendMediaSet.getServerIdsIn();
                View inflate = LayoutInflater.from(this.mActivity).inflate(R.layout.face_page_recommend_group_header, (ViewGroup) this.mRecyclerView, false);
                this.mRecommendFaceGroupHeader = inflate;
                this.mHeaderFooterWrapperAdapter.addHeader(inflate);
                SamplingStatHelper.recordCountEvent("face", "face_show_recommend_panel");
                PagerGridLayout pagerGridLayout = (PagerGridLayout) this.mRecommendFaceGroupHeader.findViewById(R.id.face_recommend_group);
                this.mRecommendFaceGroup = pagerGridLayout;
                pagerGridLayout.setOnPageChangedListener(this);
                RecommendFaceGroupAdapter recommendFaceGroupAdapter = new RecommendFaceGroupAdapter(this, this.mServerIdOfAlbum, Long.valueOf(this.mLocalIdOfAlbum)) { // from class: com.miui.gallery.ui.FacePageFragment.13
                    @Override // com.miui.gallery.widget.PagerGridLayout.BaseAdapter
                    public int getColumnsCount() {
                        return 4;
                    }

                    @Override // com.miui.gallery.ui.RecommendFaceGroupAdapter
                    public int getLayoutId() {
                        return R.layout.recommend_face_cover_item_small;
                    }

                    @Override // com.miui.gallery.widget.PagerGridLayout.BaseAdapter
                    public int getRowsCount() {
                        return 1;
                    }

                    {
                        FacePageFragment.this = this;
                    }
                };
                this.mRecommendFaceAdapter = recommendFaceGroupAdapter;
                recommendFaceGroupAdapter.setOnLoadingCompleteListener(new BindImageHelper.OnImageLoadingCompleteListener() { // from class: com.miui.gallery.ui.FacePageFragment.14
                    @Override // com.miui.gallery.util.glide.BindImageHelper.OnImageLoadingCompleteListener
                    public void onLoadingFailed() {
                    }

                    {
                        FacePageFragment.this = this;
                    }

                    @Override // com.miui.gallery.util.glide.BindImageHelper.OnImageLoadingCompleteListener
                    public void onLoadingComplete() {
                        FacePageFragment.this.invalidateFaceGridView();
                    }
                });
                getLoaderManager().initLoader(2, null, new FaceRecommendPhotoLoaderCallback());
                this.mRecommendFaceGroupHeader.findViewById(R.id.confirm_recommend).setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.FacePageFragment.15
                    {
                        FacePageFragment.this = this;
                    }

                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        if (GalleryPreferences.Face.isShowRecommendConfirmDialog()) {
                            int mergeFaceCount = FacePageFragment.this.mRecommendFaceAdapter.getMergeFaceCount();
                            GalleryPreferences.Face.setShowRecommendConfirmDialog(false);
                            new AlertDialog.Builder(FacePageFragment.this.mActivity).setPositiveButton(R.string.confirm_hidden_recommend_group, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.FacePageFragment.15.1
                                {
                                    AnonymousClass15.this = this;
                                }

                                @Override // android.content.DialogInterface.OnClickListener
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    FacePageFragment.this.changeToNextPage();
                                }
                            }).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).setTitle(FacePageFragment.this.getString(R.string.remind_face_recommend_confirm)).setMessage(FacePageFragment.this.mActivity.getResources().getQuantityString(R.plurals.remind_face_recommend_confirm_text, mergeFaceCount, Integer.valueOf(mergeFaceCount))).create().show();
                            return;
                        }
                        FacePageFragment.this.changeToNextPage();
                    }
                });
                this.mRecommendFaceGroupHeader.findViewById(R.id.face_recommend_hidden).setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.FacePageFragment.16
                    {
                        FacePageFragment.this = this;
                    }

                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        AlertDialog create = new AlertDialog.Builder(FacePageFragment.this.mActivity).setPositiveButton(R.string.confirm_hidden_recommend_group, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.FacePageFragment.16.1
                            {
                                AnonymousClass16.this = this;
                            }

                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i) {
                                GalleryPreferences.Face.setFaceRecommendGroupHidden(FacePageFragment.this.getServerIdOfAlbum(), true);
                                FacePageFragment.this.mHeaderFooterWrapperAdapter.removeHeader(FacePageFragment.this.mRecommendFaceGroupHeader);
                                FacePageFragment.this.mHeaderFooterWrapperAdapter.addFooter(FacePageFragment.this.mFooterView);
                                SamplingStatHelper.recordCountEvent("face", "face_close_recommend_panel");
                            }
                        }).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).setTitle(FacePageFragment.this.getString(R.string.confirm_hidden_recommend_group_title)).setMessage(FacePageFragment.this.getString(R.string.confirm_hidden_recommend_group_message)).create();
                        create.show();
                        create.getButton(-1).setTextColor(FacePageFragment.this.getResources().getColor(R.color.remove_recommend_header_dialog_button));
                    }
                });
                return;
            }
            this.mAddFooterView = true;
        }
    }

    /* loaded from: classes2.dex */
    public class FaceRecommendPhotoLoaderCallback implements LoaderManager.LoaderCallbacks {
        public final String getOrderBy() {
            return "dateTaken DESC ";
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader loader) {
        }

        public FaceRecommendPhotoLoaderCallback() {
            FacePageFragment.this = r1;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader onCreateLoader(int i, Bundle bundle) {
            CursorLoader cursorLoader = new CursorLoader(FacePageFragment.this.getActivity());
            cursorLoader.setUri(getUri());
            cursorLoader.setProjection(RecommendFaceGroupAdapter.PROJECTION);
            cursorLoader.setSelectionArgs(new String[]{FacePageFragment.this.mRecommendFaceIds});
            cursorLoader.setSortOrder(getOrderBy());
            return cursorLoader;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader loader, Object obj) {
            FacePageFragment.this.mRecommendFaceAdapter.swapCursor((Cursor) obj);
            FacePageFragment.this.mRecommendFaceGroup.setAdapter(FacePageFragment.this.mRecommendFaceAdapter);
        }

        public final Uri getUri() {
            return GalleryContract.PeopleFace.RECOMMEND_FACES_OF_ONE_PERSON_URI;
        }
    }

    public void changeToNextPage() {
        PagerGridLayout pagerGridLayout = this.mRecommendFaceGroup;
        if (pagerGridLayout != null) {
            pagerGridLayout.changeToNextPage();
        }
    }

    @Override // com.miui.gallery.widget.PagerGridLayout.OnPageChangedListener
    public void onPageChanged(int i, int i2, boolean z) {
        if (z) {
            this.mHeaderFooterWrapperAdapter.removeHeader(this.mRecommendFaceGroupHeader);
            return;
        }
        TextView textView = (TextView) this.mRecommendFaceGroupHeader.findViewById(R.id.face_recommend_group_number);
        if (i2 == 1) {
            this.mRecommendFaceGroupHeader.findViewById(R.id.group_divider).setVisibility(8);
        } else {
            textView.setText(getString(R.string.face_recommend_group_number, Integer.valueOf(i + 1), Integer.valueOf(i2)));
        }
    }

    public String getServerIdOfAlbum() {
        return this.mServerIdOfAlbum;
    }

    public final String getAlbumName() {
        return (this.mActivity != null && !TextUtils.isEmpty(this.mAlbumName) && !this.mActivity.getString(R.string.people_page_unname).equalsIgnoreCase(this.mAlbumName)) ? this.mAlbumName : "";
    }

    public long getIsHasEverNotCreateBabyAlbumFromThis() {
        return FaceManager.queryBabyAlbumByPeopleId(this.mServerIdOfAlbum);
    }

    @Override // androidx.fragment.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_change_mode_to_face /* 2131361864 */:
                changeDisplayMode();
                recordDisplayModeCountEvent("face");
                TrackController.trackClick("403.47.1.1.11254", AutoTracking.getRef());
                return true;
            case R.id.action_change_mode_to_photo /* 2131361865 */:
                changeDisplayMode();
                recordDisplayModeCountEvent("photo");
                return true;
            case R.id.action_ignore /* 2131361879 */:
                shoeIgnoreAlert();
                TrackController.trackClick("403.47.2.1.11256", AutoTracking.getRef());
                return true;
            case R.id.action_merge_to /* 2131361888 */:
                showMergeHandler();
                TrackController.trackClick("403.47.2.1.11255", AutoTracking.getRef());
                return true;
            default:
                return false;
        }
    }

    public final void showMergeHandler() {
        if (this.mFaceAlbumMergeHandler == null) {
            NormalPeopleFaceMediaSet normalPeopleFaceMediaSet = new NormalPeopleFaceMediaSet(this.mLocalIdOfAlbum, this.mServerIdOfAlbum, this.mAlbumName);
            ArrayList<NormalPeopleFaceMediaSet> arrayList = new ArrayList<>();
            this.mFaceMediaSetList = arrayList;
            arrayList.add(normalPeopleFaceMediaSet);
            this.mFaceAlbumMergeHandler = new FaceAlbumRenameHandler(this.mActivity, this.mFaceMediaSetList, confirmListener());
        }
        this.mFaceAlbumMergeHandler.show();
    }

    public final boolean shoeIgnoreAlert() {
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.FacePageFragment.17
            {
                FacePageFragment.this = this;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                final ArrayList arrayList = new ArrayList();
                arrayList.add(Long.valueOf(FacePageFragment.this.mLocalIdOfAlbum));
                ThreadManager.getMiscPool().submit(new ThreadPool.Job<Void>() { // from class: com.miui.gallery.ui.FacePageFragment.17.1
                    {
                        AnonymousClass17.this = this;
                    }

                    @Override // com.miui.gallery.concurrent.ThreadPool.Job
                    /* renamed from: run */
                    public Void mo1807run(ThreadPool.JobContext jobContext) {
                        FaceDataManager.safeIgnorePeopleByIds(arrayList);
                        FacePageFragment.this.mActivity.finish();
                        return null;
                    }
                });
            }
        };
        AppCompatActivity appCompatActivity = this.mActivity;
        DialogUtil.showConfirmAlertWithCancel(appCompatActivity, onClickListener, null, "", Html.fromHtml(appCompatActivity.getString(R.string.ignore_alert_title)), this.mActivity.getString(17039370), 17039360);
        return true;
    }

    public final void recordDisplayModeCountEvent(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, str);
        SamplingStatHelper.recordCountEvent("face", "face_change_display_mode", hashMap);
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        PeopleContactInfo peopleContactInfo = null;
        if (i == 16) {
            PeopleContactInfo contactInfo = (intent == null || i2 != -1) ? null : InputFaceNameFragment.getContactInfo(this.mActivity, intent);
            FaceAlbumRenameHandler faceAlbumRenameHandler = this.mFaceAlbumRenameHandler;
            if (faceAlbumRenameHandler != null) {
                faceAlbumRenameHandler.finishWhenGetContact(contactInfo);
            }
            this.mFaceMediaSet = null;
        } else if (i == 17) {
            if (intent != null && i2 == -1) {
                peopleContactInfo = InputFaceNameFragment.getContactInfo(this.mActivity, intent);
            }
            RemoveFromFaceAlbumHandler removeFromFaceAlbumHandler = this.mRemoveFromFaceAlbumHandler;
            if (removeFromFaceAlbumHandler == null) {
                return;
            }
            removeFromFaceAlbumHandler.finishWhenGetContact(peopleContactInfo);
        } else if (i == 19) {
            PeopleContactInfo contactInfo2 = (intent == null || i2 != -1) ? null : InputFaceNameFragment.getContactInfo(this.mActivity, intent);
            FaceAlbumRenameHandler faceAlbumRenameHandler2 = this.mFaceAlbumMergeHandler;
            if (faceAlbumRenameHandler2 != null) {
                faceAlbumRenameHandler2.finishWhenGetContact(contactInfo2);
            }
            this.mFaceMediaSetList = null;
        } else if (i == 21) {
            if (intent == null || i2 != -1 || !intent.getBooleanExtra("all_faces_confirmed", false)) {
                return;
            }
            this.mHeaderFooterWrapperAdapter.removeFooter(this.mFooterView);
        } else if (i != 55) {
            super.onActivityResult(i, i2, intent);
        } else if (intent == null || i2 != -1) {
        } else {
            if (intent.getBooleanExtra("pick_people_cover_success", false)) {
                this.mFaceCoverPath = intent.getStringExtra("face_album_cover");
                this.mFaceCoverRectF = (RectF) intent.getParcelableExtra("face_position_rect");
                displayFaceCover();
                ToastUtils.makeText(this.mActivity, (int) R.string.pick_people_cover_success);
                return;
            }
            ToastUtils.makeText(this.mActivity, (int) R.string.pick_people_cover_fail);
        }
    }

    /* loaded from: classes2.dex */
    public class ChoiceModeListener implements MultiChoiceModeListener {
        public ActionMode mMode;
        public FaceAlbumHandlerBase.FaceAlbumHandlerListener mRemoveFromFaceAlbumListener;

        public ChoiceModeListener() {
            FacePageFragment.this = r1;
            this.mRemoveFromFaceAlbumListener = new FaceAlbumHandlerBase.FaceAlbumHandlerListener() { // from class: com.miui.gallery.ui.FacePageFragment.ChoiceModeListener.2
                {
                    ChoiceModeListener.this = this;
                }

                @Override // com.miui.gallery.ui.renameface.FaceAlbumHandlerBase.FaceAlbumHandlerListener
                public void onGetFolderItem(final FaceAlbumHandlerBase.FaceFolderItem faceFolderItem) {
                    Spanned fromHtml;
                    String str;
                    String str2;
                    if (FacePageFragment.this.mEditableWrapper.getCheckedItemIds().length > 0) {
                        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.FacePageFragment.ChoiceModeListener.2.1
                            {
                                AnonymousClass2.this = this;
                            }

                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i) {
                                doRemove(faceFolderItem);
                            }
                        };
                        DialogInterface.OnClickListener onClickListener2 = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.FacePageFragment.ChoiceModeListener.2.2
                            {
                                AnonymousClass2.this = this;
                            }

                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        };
                        if (faceFolderItem == null) {
                            str2 = FacePageFragment.this.mActivity.getString(R.string.remove_from_album_title);
                            str = FacePageFragment.this.mActivity.getString(R.string.operation_remove_face);
                            fromHtml = Html.fromHtml(FacePageFragment.this.mActivity.getResources().getString(R.string.remove_from_album_message));
                        } else {
                            String string = FacePageFragment.this.mActivity.getString(17039370);
                            fromHtml = Html.fromHtml(FacePageFragment.this.mActivity.getString(R.string.confirm_merge_many_face_albums, new Object[]{faceFolderItem.getName()}));
                            str = string;
                            str2 = "";
                        }
                        DialogUtil.showConfirmAlertWithCancel(FacePageFragment.this.mActivity, onClickListener, onClickListener2, str2, fromHtml, str, 17039360);
                    }
                }

                public final void doRemove(FaceAlbumHandlerBase.FaceFolderItem faceFolderItem) {
                    NormalPeopleFaceMediaSet.doMoveFacesToAnother(faceFolderItem, FacePageFragment.this.mEditableWrapper.getCheckedItemIds());
                    ChoiceModeListener.this.mMode.finish();
                    SamplingStatHelper.recordCountEvent("face", "face_remove");
                }
            };
        }

        @Override // android.view.ActionMode.Callback
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            this.mMode = actionMode;
            actionMode.getMenuInflater().inflate(R.menu.face_page_action_menu, menu);
            enableOrDisableMenuItem(false);
            menu.findItem(R.id.action_produce).setVisible(GalleryPreferences.Assistant.isCreativityFunctionOn());
            FacePageFragment.this.changeVisibilityOfShareContainer(8);
            return true;
        }

        @Override // android.view.ActionMode.Callback
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            FacePageFragment.this.changeVisibilityOfShareContainer(8);
            return false;
        }

        @Override // com.miui.gallery.widget.editwrapper.MultiChoiceModeListener
        public void onItemCheckedStateChanged(ActionMode actionMode, int i, long j, boolean z) {
            enableOrDisableMenuItem(FacePageFragment.this.mEditableWrapper.getCheckedItemCount() > 0);
        }

        public final void enableOrDisableMenuItem(boolean z) {
            Menu menu;
            ActionMode actionMode = this.mMode;
            if (actionMode == null || (menu = actionMode.getMenu()) == null) {
                return;
            }
            menu.findItem(R.id.action_remove_from_face_album).setEnabled(z);
            menu.findItem(R.id.action_delete).setEnabled(z);
            menu.findItem(R.id.action_produce).setEnabled(z);
            menu.findItem(R.id.action_send).setEnabled(z);
        }

        @Override // android.view.ActionMode.Callback
        public boolean onActionItemClicked(final ActionMode actionMode, MenuItem menuItem) {
            if (menuItem.getItemId() != 16908313 && menuItem.getItemId() != 16908314) {
                LinearMotorHelper.performHapticFeedback(FacePageFragment.this.mActivity, LinearMotorHelper.HAPTIC_TAP_LIGHT);
            }
            switch (menuItem.getItemId()) {
                case R.id.action_delete /* 2131361872 */:
                    FacePageFragment.this.doDelete(this.mMode, getSelectedPhotoIds());
                    return true;
                case R.id.action_produce /* 2131361895 */:
                    MediaAndAlbumOperations.doProduceCreation(FacePageFragment.this.mActivity, new MediaAndAlbumOperations.OnCompleteListener() { // from class: com.miui.gallery.ui.FacePageFragment.ChoiceModeListener.1
                        {
                            ChoiceModeListener.this = this;
                        }

                        @Override // com.miui.gallery.util.MediaAndAlbumOperations.OnCompleteListener
                        public void onComplete(long[] jArr) {
                            actionMode.finish();
                        }
                    }, FacePageFragment.this.mEditableWrapper.getCheckedItems());
                    SamplingStatHelper.recordCountEvent("face", "produce");
                    return true;
                case R.id.action_remove_from_face_album /* 2131361897 */:
                    FacePageFragment.this.startRemoveFromFaceAlbum(this.mRemoveFromFaceAlbumListener);
                    return true;
                case R.id.action_send /* 2131361903 */:
                    List<Integer> checkedPositions = FacePageFragment.this.mEditableWrapper.getCheckedPositions();
                    ArrayList arrayList = new ArrayList(checkedPositions.size());
                    int i = Integer.MAX_VALUE;
                    for (int i2 = 0; i2 < checkedPositions.size(); i2++) {
                        int intValue = checkedPositions.get(i2).intValue();
                        arrayList.add(Long.valueOf(FacePageFragment.this.mAdapter.getItemKey(intValue)));
                        if (intValue < i) {
                            i = intValue;
                        }
                    }
                    int[] iArr = new int[checkedPositions.size()];
                    long[] jArr = new long[arrayList.size()];
                    for (int i3 = 0; i3 < checkedPositions.size(); i3++) {
                        iArr[i3] = checkedPositions.get(i3).intValue();
                        jArr[i3] = ((Long) arrayList.get(i3)).longValue();
                    }
                    int i4 = i == Integer.MAX_VALUE ? 0 : i;
                    ImageLoadParams build = new ImageLoadParams.Builder().setKey(FacePageFragment.this.mAdapter.getItemKey(i4)).setFilePath(FacePageFragment.this.mAdapter.getBindImagePath(i4)).setTargetSize(Config$ThumbConfig.get().sMicroTargetSize).setRegionRect(FacePageFragment.this.mAdapter.getItemDecodeRectF(i4)).setInitPosition(i4).setMimeType(FacePageFragment.this.mAdapter.getMimeType(i4)).setFileLength(FacePageFragment.this.mAdapter.getFileLength(i4)).setCreateTime(FacePageFragment.this.mAdapter.getCreateTime(i4)).setLocation(FacePageFragment.this.mAdapter.getLocation(i4)).build();
                    FacePageFragment facePageFragment = FacePageFragment.this;
                    IntentUtil.gotoPreviewSelectPage(facePageFragment, GalleryContract.PeopleFace.ONE_PERSON_URI, i4, facePageFragment.mAdapter.getItemCount(), null, FacePageFragment.this.getSelectionArgs(), FacePageFragment.this.getOrderBy(), build, jArr, iArr);
                    actionMode.finish();
                    SamplingStatHelper.recordCountEvent("face", "face_send");
                    return true;
                default:
                    return false;
            }
        }

        @Override // android.view.ActionMode.Callback
        public void onDestroyActionMode(ActionMode actionMode) {
            this.mMode = null;
            FacePageFragment.this.changeVisibilityOfShareContainer(0);
        }

        public ActionMode getMode() {
            return this.mMode;
        }

        @Override // com.miui.gallery.widget.editwrapper.MultiChoiceModeListener
        public void onAllItemsCheckedStateChanged(ActionMode actionMode, boolean z) {
            enableOrDisableMenuItem(FacePageFragment.this.mEditableWrapper.getCheckedItemCount() > 0);
        }

        public final long[] getSelectedPhotoIds() {
            List<Integer> checkedPositions = FacePageFragment.this.mEditableWrapper.getCheckedPositions();
            ArrayList arrayList = new ArrayList(checkedPositions.size());
            for (int i = 0; i < checkedPositions.size(); i++) {
                arrayList.add(Long.valueOf(FacePageFragment.this.mAdapter.getItemPhotoId(checkedPositions.get(i).intValue())));
            }
            long[] jArr = new long[arrayList.size()];
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                jArr[i2] = ((Long) arrayList.get(i2)).longValue();
            }
            return jArr;
        }
    }

    public final void doDelete(final ActionMode actionMode, long[] jArr) {
        MediaAndAlbumOperations.delete(this.mActivity, "FacePageFragmentDeleteMediaDialogFragment", new DeletionTask.OnDeletionCompleteListener() { // from class: com.miui.gallery.ui.FacePageFragment.18
            {
                FacePageFragment.this = this;
            }

            @Override // com.miui.gallery.ui.DeletionTask.OnDeletionCompleteListener
            public void onDeleted(int i, long[] jArr2) {
                FacePageFragment facePageFragment = FacePageFragment.this;
                if (facePageFragment.mActivity == null) {
                    return;
                }
                NormalPeopleFaceMediaSet.ignoreFaces(facePageFragment.mEditableWrapper.getCheckedItemIds());
                FacePageFragment facePageFragment2 = FacePageFragment.this;
                ToastUtils.makeText(facePageFragment2.mActivity, facePageFragment2.getResources().getQuantityString(R.plurals.delete_finish_format, i, Integer.valueOf(i)));
                if (i > 0) {
                    SoundUtils.playSoundForOperation(FacePageFragment.this.mActivity, 0);
                }
                actionMode.finish();
                SamplingStatHelper.recordCountEvent("face", "face_delete");
            }
        }, null, this.mLocalIdOfAlbum, this.mAlbumName, 28, jArr);
    }

    public final void showRenameHandler() {
        if (this.mFaceAlbumRenameHandler == null) {
            NormalPeopleFaceMediaSet normalPeopleFaceMediaSet = new NormalPeopleFaceMediaSet(this.mLocalIdOfAlbum, getServerIdOfAlbum(), getAlbumName());
            this.mFaceMediaSet = normalPeopleFaceMediaSet;
            this.mFaceAlbumRenameHandler = new FaceAlbumRenameHandler(this.mActivity, normalPeopleFaceMediaSet, confirmListener(), !PeopleContactInfo.isUnKnownRelation(this.mRelationType));
        }
        this.mFaceAlbumRenameHandler.show();
    }

    public final FaceAlbumRenameHandler.ConfirmListener confirmListener() {
        return new FaceAlbumRenameHandler.ConfirmListener() { // from class: com.miui.gallery.ui.FacePageFragment.19
            {
                FacePageFragment.this = this;
            }

            @Override // com.miui.gallery.ui.renameface.FaceAlbumRenameHandler.ConfirmListener
            public void onConfirm(final String str, boolean z) {
                if (!z) {
                    FacePageFragment.this.mHandler.post(new Runnable() { // from class: com.miui.gallery.ui.FacePageFragment.19.1
                        {
                            AnonymousClass19.this = this;
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            if (FacePageFragment.this.getActivity() == null) {
                                return;
                            }
                            if (TextUtils.isEmpty(FacePageFragment.this.getAlbumName())) {
                                ((TextView) FacePageFragment.this.mFaceCoverHeader.findViewById(R.id.face_name_edit)).setText(FacePageFragment.this.getString(R.string.face_name_edit));
                            }
                            FacePageFragment.this.mAlbumName = str;
                            FacePageFragment.this.setTitle();
                        }
                    });
                } else {
                    ThreadManager.getMiscPool().submit(new ThreadPool.Job<Void>() { // from class: com.miui.gallery.ui.FacePageFragment.19.2
                        {
                            AnonymousClass19.this = this;
                        }

                        @Override // com.miui.gallery.concurrent.ThreadPool.Job
                        /* renamed from: run */
                        public Void mo1807run(ThreadPool.JobContext jobContext) {
                            PeopleFace groupByPeopleName = FaceDataManager.getGroupByPeopleName(FacePageFragment.this.mActivity, str);
                            if (groupByPeopleName != null) {
                                Intent intent = new Intent(FacePageFragment.this.mActivity, FacePageActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("server_id_of_album", groupByPeopleName.serverId);
                                bundle.putString("local_id_of_album", groupByPeopleName.getId());
                                bundle.putString("album_name", str);
                                bundle.putInt("relationType", groupByPeopleName.relationType);
                                FaceRegionRectF[] faceRegionRectFArr = new FaceRegionRectF[1];
                                bundle.putString("face_album_cover", FaceManager.queryCoverImageOfOnePerson(groupByPeopleName.serverId, Long.parseLong(groupByPeopleName.getId()), faceRegionRectFArr));
                                bundle.putParcelable("face_position_rect", faceRegionRectFArr[0]);
                                intent.putExtras(bundle);
                                FacePageFragment.this.mActivity.startActivity(intent);
                                FacePageFragment.this.mActivity.finish();
                                return null;
                            }
                            return null;
                        }
                    });
                }
                SamplingStatHelper.recordCountEvent("face", "face_album_rename");
            }
        };
    }

    public final void startRemoveFromFaceAlbum(FaceAlbumHandlerBase.FaceAlbumHandlerListener faceAlbumHandlerListener) {
        if (this.mRemoveFromFaceAlbumHandler == null) {
            this.mRemoveFromFaceAlbumHandler = new RemoveFromFaceAlbumHandler(this.mActivity, new NormalPeopleFaceMediaSet(this.mLocalIdOfAlbum, getServerIdOfAlbum(), getAlbumName()), faceAlbumHandlerListener);
        }
        this.mRemoveFromFaceAlbumHandler.show();
    }

    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> list, Menu menu, int i) {
        ArrayList arrayList = new ArrayList();
        if (this.mEditableWrapper.isInActionMode()) {
            arrayList.add(KeyboardShortcutGroupManager.getInstance().getSelectAllShortcutInfo());
            arrayList.addAll(KeyboardShortcutGroupManager.getInstance().getDeleteShortcutInfo());
        }
        list.add(new KeyboardShortcutGroup(getPageName(), arrayList));
    }

    public boolean onKeyShortcut(int i, KeyEvent keyEvent) {
        return KeyboardShortcutGroupManager.getInstance().onKeyShortcut(i, keyEvent, this.mShortcutCallback);
    }

    /* loaded from: classes2.dex */
    public class FaceKeyboardShortcutCallback implements KeyboardShortcutGroupManager.OnKeyShortcutCallback {
        public FaceKeyboardShortcutCallback() {
            FacePageFragment.this = r1;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onSelectAllPressed() {
            if (FacePageFragment.this.mEditableWrapper.isInActionMode()) {
                FacePageFragment.this.mEditableWrapper.setAllItemsCheckState(true);
            }
            return true;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onDeletePressed() {
            if (FacePageFragment.this.mEditableWrapper.isInActionMode()) {
                FacePageFragment facePageFragment = FacePageFragment.this;
                facePageFragment.doDelete(facePageFragment.mListener.getMode(), FacePageFragment.this.mListener.getSelectedPhotoIds());
                return true;
            }
            return true;
        }
    }
}
