package com.miui.gallery.ui.addtoalbum;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts$StartActivityForResult;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.epoxy.EpoxyModel;
import com.miui.gallery.R;
import com.miui.gallery.adapter.GallerySimpleEpoxyAdapter;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.provider.ShareAlbumHelper;
import com.miui.gallery.request.PicToPdfHelper;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.AddRemoveSecretDialogFragment;
import com.miui.gallery.ui.AlbumCreatorDialogFragment;
import com.miui.gallery.ui.BaseAlbumOperationDialogFragment;
import com.miui.gallery.ui.ConfirmDialog;
import com.miui.gallery.ui.CopyMoveDialogFragment;
import com.miui.gallery.ui.CopyOrMoveDialog;
import com.miui.gallery.ui.addtoalbum.AddToAlbumPageActivity;
import com.miui.gallery.ui.addtoalbum.style.AddToAlbumGridPageView;
import com.miui.gallery.ui.album.common.RecyclerViewItemModel;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.main.base.AbsAlbumPageView;
import com.miui.gallery.ui.album.main.base.config.AlbumPageConfig;
import com.miui.gallery.util.MediaAndAlbumOperations;
import com.miui.gallery.util.ResourceUtils;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.glide.BindImageHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.thread.ThreadManager;
import com.miui.gallery.widget.recyclerview.ItemClickSupport;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.MiStat;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: AddToAlbumPageActivity.kt */
/* loaded from: classes2.dex */
public final class AddToAlbumPageActivity extends AddToAlbumContract$V {
    public static final Companion Companion = new Companion(null);
    public GallerySimpleEpoxyAdapter<BaseViewBean<?, ?>> mAdapter;
    public boolean mIsHavePdfListener;
    public ImageView mIvSelectImg;
    public OnAddToAlbumOperationListener mListenerWrapper;
    public AbsAlbumPageView mPageView;
    public TextView mTvSelectNum;
    public View mView;

    /* compiled from: AddToAlbumPageActivity.kt */
    /* loaded from: classes2.dex */
    public interface OnAddToAlbumOperationListener {
        default boolean onAlbumSelected(long j, boolean z) {
            return false;
        }
    }

    public static final void start(ComponentActivity componentActivity, AddToAlbumPageResult addToAlbumPageResult, Bundle bundle, boolean z) {
        Companion.start(componentActivity, addToAlbumPageResult, bundle, z);
    }

    public static final void start(ComponentActivity componentActivity, boolean z, boolean z2, boolean z3, boolean z4, int i, boolean z5, long[] jArr, ArrayList<Uri> arrayList, AddToAlbumPageResult addToAlbumPageResult, boolean z6) {
        Companion.start(componentActivity, z, z2, z3, z4, i, z5, jArr, arrayList, addToAlbumPageResult, z6);
    }

    @Override // com.miui.pickdrag.base.BasePickerDragActivity
    public int[] canSlideViewIds() {
        return new int[]{R.id.headerView};
    }

    @Override // com.miui.gallery.ui.addtoalbum.AddToAlbumContract$V, com.miui.pickdrag.base.BasePickerDragActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        parseIntent();
        View dragContentView = setDragContentView(R.layout.add_to_album_page_view);
        this.mView = dragContentView;
        Intrinsics.checkNotNull(dragContentView);
        View findViewById = dragContentView.findViewById(R.id.recyclerViewList);
        Intrinsics.checkNotNullExpressionValue(findViewById, "mView!!.findViewById(R.id.recyclerViewList)");
        RecyclerView recyclerView = (RecyclerView) findViewById;
        View view = this.mView;
        Intrinsics.checkNotNull(view);
        this.mTvSelectNum = (TextView) view.findViewById(R.id.tvSelectNum);
        View view2 = this.mView;
        Intrinsics.checkNotNull(view2);
        this.mIvSelectImg = (ImageView) view2.findViewById(R.id.ivSelectImg);
        GallerySimpleEpoxyAdapter<BaseViewBean<?, ?>> gallerySimpleEpoxyAdapter = new GallerySimpleEpoxyAdapter<>(ThreadManager.getExecutor(79), null);
        this.mAdapter = gallerySimpleEpoxyAdapter;
        recyclerView.setAdapter(gallerySimpleEpoxyAdapter);
        AlbumPageConfig.getInstance().setAddToAlbumOperation(true);
        AddToAlbumGridPageView addToAlbumGridPageView = new AddToAlbumGridPageView(this, this.mAdapter);
        this.mPageView = addToAlbumGridPageView;
        addToAlbumGridPageView.onInit(this.mView);
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() { // from class: com.miui.gallery.ui.addtoalbum.AddToAlbumPageActivity$$ExternalSyntheticLambda1
            @Override // com.miui.gallery.widget.recyclerview.ItemClickSupport.OnItemClickListener
            public final boolean onItemClick(RecyclerView recyclerView2, View view3, int i, long j, float f, float f2) {
                boolean m1576onCreate$lambda0;
                m1576onCreate$lambda0 = AddToAlbumPageActivity.m1576onCreate$lambda0(AddToAlbumPageActivity.this, recyclerView2, view3, i, j, f, f2);
                return m1576onCreate$lambda0;
            }
        });
        GallerySimpleEpoxyAdapter<BaseViewBean<?, ?>> gallerySimpleEpoxyAdapter2 = this.mAdapter;
        Intrinsics.checkNotNull(gallerySimpleEpoxyAdapter2);
        gallerySimpleEpoxyAdapter2.addEventHook(new AddToAlbumPageActivity$onCreate$2(this, RecyclerViewItemModel.VH.class));
        getPresenter().onInitData();
    }

    /* renamed from: onCreate$lambda-0 */
    public static final boolean m1576onCreate$lambda0(AddToAlbumPageActivity this$0, RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.onItemClick(i, j);
        return true;
    }

    public final void parseIntent() {
        Intent intent = getIntent();
        boolean z = false;
        if (intent != null) {
            z = intent.getBooleanExtra("have_pdf_listener", false);
        }
        this.mIsHavePdfListener = z;
        this.mListenerWrapper = new InternalListenerWrapper(this);
    }

    public final void onItemClick(int i, long j) {
        if (j == 2131361919) {
            final AlbumCreatorDialogFragment albumCreatorDialogFragment = new AlbumCreatorDialogFragment();
            albumCreatorDialogFragment.setOnAlbumOperationListener(new BaseAlbumOperationDialogFragment.OnAlbumOperationListener() { // from class: com.miui.gallery.ui.addtoalbum.AddToAlbumPageActivity$onItemClick$1
                @Override // com.miui.gallery.ui.BaseAlbumOperationDialogFragment.OnAlbumOperationListener
                public boolean onOperationFailedByConflict(final Album album) {
                    Intrinsics.checkNotNullParameter(album, "album");
                    if (AddToAlbumPageActivity.this.isDestroyed()) {
                        return true;
                    }
                    FragmentManager supportFragmentManager = AddToAlbumPageActivity.this.getSupportFragmentManager();
                    String string = ResourceUtils.getString(R.string.create_album_failed_because_dup);
                    String string2 = ResourceUtils.getString(R.string.cancel);
                    String string3 = ResourceUtils.getString(R.string.ok);
                    final AddToAlbumPageActivity addToAlbumPageActivity = AddToAlbumPageActivity.this;
                    ConfirmDialog.showConfirmDialog(supportFragmentManager, (String) null, string, string2, string3, new ConfirmDialog.ConfirmDialogInterface() { // from class: com.miui.gallery.ui.addtoalbum.AddToAlbumPageActivity$onItemClick$1$onOperationFailedByConflict$1
                        @Override // com.miui.gallery.ui.ConfirmDialog.ConfirmDialogInterface
                        public void onConfirm(DialogFragment dialog) {
                            AddToAlbumPageActivity.OnAddToAlbumOperationListener onAddToAlbumOperationListener;
                            AddToAlbumPageActivity.OnAddToAlbumOperationListener onAddToAlbumOperationListener2;
                            Intrinsics.checkNotNullParameter(dialog, "dialog");
                            dialog.dismiss();
                            onAddToAlbumOperationListener = AddToAlbumPageActivity.this.mListenerWrapper;
                            if (onAddToAlbumOperationListener != null) {
                                onAddToAlbumOperationListener2 = AddToAlbumPageActivity.this.mListenerWrapper;
                                Intrinsics.checkNotNull(onAddToAlbumOperationListener2);
                                onAddToAlbumOperationListener2.onAlbumSelected(album.getAlbumId(), true);
                            }
                        }

                        @Override // com.miui.gallery.ui.ConfirmDialog.ConfirmDialogInterface
                        public void onCancel(DialogFragment dialog) {
                            Intrinsics.checkNotNullParameter(dialog, "dialog");
                            dialog.dismiss();
                        }
                    });
                    return true;
                }

                @Override // com.miui.gallery.ui.BaseAlbumOperationDialogFragment.OnAlbumOperationListener
                public void onOperationDone(long j2, String albumName, Bundle result) {
                    boolean isShowCopyOrMoveDialog;
                    Intrinsics.checkNotNullParameter(albumName, "albumName");
                    Intrinsics.checkNotNullParameter(result, "result");
                    if (j2 > 0) {
                        AddToAlbumPageActivity addToAlbumPageActivity = AddToAlbumPageActivity.this;
                        isShowCopyOrMoveDialog = addToAlbumPageActivity.isShowCopyOrMoveDialog(j2);
                        addToAlbumPageActivity.onAlbumSelected(j2, isShowCopyOrMoveDialog);
                    }
                    albumCreatorDialogFragment.dismissAllowingStateLoss();
                }
            });
            albumCreatorDialogFragment.showAllowingStateLoss(getSupportFragmentManager(), "AlbumCreatorDialogFragment");
            SamplingStatHelper.recordCountEvent("add_to_dialog", "add_to_new_album");
            TrackController.trackClick("403.26.0.1.11241", "403.26.0.1.11240");
        } else if (j == -1000) {
            onAlbumSelected(-1000L, false);
            SamplingStatHelper.recordCountEvent("add_to_dialog", "add_to_secret");
        } else if (j == 2131361920) {
            onFinishByPdfClick();
        } else {
            onAlbumSelected(j, isShowCopyOrMoveDialog(j));
        }
    }

    public final boolean isShowCopyOrMoveDialog(long j) {
        return getIntent() == null || (!getIntent().getBooleanExtra("is_from_secret", false) && !getIntent().getBooleanExtra("is_from_other_share_album", false) && j != 2147483642);
    }

    @Override // com.miui.gallery.ui.addtoalbum.AddToAlbumContract$V
    public boolean isAddPicToPdf() {
        return PicToPdfHelper.isPicToPdfSupport() || this.mIsHavePdfListener;
    }

    @Override // com.miui.gallery.ui.addtoalbum.AddToAlbumContract$V
    public void bindHeaderInfo(Pair<String, Byte[]> pair, int i) {
        GlideOptions tinyThumbOf = GlideOptions.tinyThumbOf();
        if ((pair == null ? null : (Byte[]) pair.second) != null) {
            Object obj = pair.second;
            Intrinsics.checkNotNullExpressionValue(obj, "cover.second");
            tinyThumbOf = tinyThumbOf.secretKey(ArraysKt___ArraysKt.toByteArray((Byte[]) obj));
        }
        String str = pair == null ? null : (String) pair.first;
        ImageView imageView = this.mIvSelectImg;
        Objects.requireNonNull(tinyThumbOf, "null cannot be cast to non-null type com.bumptech.glide.request.RequestOptions");
        BindImageHelper.bindImage(str, (Uri) null, (DownloadType) null, imageView, tinyThumbOf);
        TextView textView = this.mTvSelectNum;
        Intrinsics.checkNotNull(textView);
        textView.setText(String.valueOf(i));
    }

    @Override // com.miui.gallery.ui.addtoalbum.AddToAlbumContract$V
    public void dispatchAlbums(List<? extends BaseViewBean<?, ?>> list, List<? extends EpoxyModel<?>> list2) {
        GallerySimpleEpoxyAdapter<BaseViewBean<?, ?>> gallerySimpleEpoxyAdapter = this.mAdapter;
        Intrinsics.checkNotNull(gallerySimpleEpoxyAdapter);
        gallerySimpleEpoxyAdapter.setDatasAndModels(list, list2, false);
        TimeMonitor.trackTimeMonitor("403.26.0.1.13762", AutoTracking.getRef(), list == null ? 0L : list.size());
    }

    public final void onAlbumSelected(long j, boolean z) {
        if (z && ShareAlbumHelper.isOtherShareAlbumId(j)) {
            DefaultLogger.d("AddToAlbumDialogFragment", "Is other shared album, do copy operation for default");
            z = false;
        }
        if (z) {
            showCopyOrMoveDialog(j);
            return;
        }
        OnAddToAlbumOperationListener onAddToAlbumOperationListener = this.mListenerWrapper;
        if (onAddToAlbumOperationListener != null) {
            Intrinsics.checkNotNull(onAddToAlbumOperationListener);
            if (onAddToAlbumOperationListener.onAlbumSelected(j, false)) {
                return;
            }
        }
        finish();
    }

    public final void showCopyOrMoveDialog(final long j) {
        CopyOrMoveDialog copyOrMoveDialog = new CopyOrMoveDialog();
        copyOrMoveDialog.setOnOperationSelectedListener(new CopyOrMoveDialog.OnOperationSelectedListener() { // from class: com.miui.gallery.ui.addtoalbum.AddToAlbumPageActivity$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.ui.CopyOrMoveDialog.OnOperationSelectedListener
            public final void onOperationSelected(FragmentActivity fragmentActivity, int i) {
                AddToAlbumPageActivity.m1577showCopyOrMoveDialog$lambda1(AddToAlbumPageActivity.this, j, fragmentActivity, i);
            }
        });
        copyOrMoveDialog.showAllowingStateLoss(getSupportFragmentManager(), "CopyOrMoveDialog");
    }

    /* renamed from: showCopyOrMoveDialog$lambda-1 */
    public static final void m1577showCopyOrMoveDialog$lambda1(AddToAlbumPageActivity this$0, long j, FragmentActivity fragmentActivity, int i) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        if (i == 0) {
            OnAddToAlbumOperationListener onAddToAlbumOperationListener = this$0.mListenerWrapper;
            if (onAddToAlbumOperationListener != null) {
                Intrinsics.checkNotNull(onAddToAlbumOperationListener);
                onAddToAlbumOperationListener.onAlbumSelected(j, true);
            }
            this$0.recordCopyMoveAction(true);
        } else if (i == 1) {
            OnAddToAlbumOperationListener onAddToAlbumOperationListener2 = this$0.mListenerWrapper;
            if (onAddToAlbumOperationListener2 != null) {
                Intrinsics.checkNotNull(onAddToAlbumOperationListener2);
                onAddToAlbumOperationListener2.onAlbumSelected(j, false);
            }
            this$0.recordCopyMoveAction(false);
        } else if (i == 2) {
            HashMap hashMap = new HashMap();
            hashMap.put("tip", "403.26.0.1.11244");
            hashMap.put("ref_tip", "403.26.0.1.11240");
            hashMap.put("status", "cancel");
            TrackController.trackClick(hashMap);
        } else {
            OnAddToAlbumOperationListener onAddToAlbumOperationListener3 = this$0.mListenerWrapper;
            if (onAddToAlbumOperationListener3 != null) {
                Intrinsics.checkNotNull(onAddToAlbumOperationListener3);
                onAddToAlbumOperationListener3.onAlbumSelected(j, true);
            }
            this$0.recordCopyMoveAction(true);
        }
    }

    public final void recordCopyMoveAction(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, Intrinsics.stringPlus("move", Boolean.valueOf(z)));
        hashMap.put("from", "AddToAlbumDialogFragment");
        SamplingStatHelper.recordCountEvent("organize_photos", "move_or_copy", hashMap);
    }

    @Override // com.miui.pickdrag.base.BasePickerDragActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        Intrinsics.checkNotNullParameter(newConfig, "newConfig");
        super.onConfigurationChanged(newConfig);
        AbsAlbumPageView absAlbumPageView = this.mPageView;
        if (absAlbumPageView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mPageView");
            absAlbumPageView = null;
        }
        absAlbumPageView.onConfigurationChanged(newConfig);
    }

    @Override // com.miui.pickdrag.base.BasePickerDragActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        TimeMonitor.cancelTimeMonitor("403.26.0.1.13762");
        AlbumPageConfig.getInstance().setAddToAlbumOperation(false);
        super.onDestroy();
    }

    /* compiled from: AddToAlbumPageActivity.kt */
    /* loaded from: classes2.dex */
    public static final class InternalListenerWrapper implements OnAddToAlbumOperationListener {
        public AddToAlbumPageActivity fragment;
        public final WeakReference<AddToAlbumPageActivity> mActivityRef;
        public final int mFromType;
        public final boolean mHasVideo;
        public final long[] mMediaIds;
        public final ArrayList<Uri> mMediaUris;

        public InternalListenerWrapper(AddToAlbumPageActivity fragment) {
            Intrinsics.checkNotNullParameter(fragment, "fragment");
            this.fragment = fragment;
            this.mActivityRef = new WeakReference<>(this.fragment);
            Bundle extras = this.fragment.getIntent().getExtras();
            Intrinsics.checkNotNull(extras);
            this.mMediaUris = extras.getParcelableArrayList("media_uri_array");
            long[] longArray = extras.getLongArray("media_id_array");
            this.mMediaIds = longArray == null ? new long[0] : longArray;
            this.mHasVideo = extras.getBoolean("has_video");
            this.mFromType = extras.getInt("extra_from_type");
        }

        @Override // com.miui.gallery.ui.addtoalbum.AddToAlbumPageActivity.OnAddToAlbumOperationListener
        public boolean onAlbumSelected(final long j, final boolean z) {
            boolean z2;
            if (this.mFromType == 1019) {
                FragmentActivity activity = getActivity();
                MediaAndAlbumOperations.OnCompleteListener onCompleteListener = new MediaAndAlbumOperations.OnCompleteListener() { // from class: com.miui.gallery.ui.addtoalbum.AddToAlbumPageActivity$InternalListenerWrapper$$ExternalSyntheticLambda6
                    @Override // com.miui.gallery.util.MediaAndAlbumOperations.OnCompleteListener
                    public final void onComplete(long[] jArr) {
                        AddToAlbumPageActivity.InternalListenerWrapper.m1582onAlbumSelected$lambda0(AddToAlbumPageActivity.InternalListenerWrapper.this, j, z, jArr);
                    }
                };
                long[] jArr = this.mMediaIds;
                AddRemoveSecretDialogFragment.remove(activity, onCompleteListener, j, Arrays.copyOf(jArr, jArr.length));
                return true;
            }
            boolean z3 = false;
            if (getActivity() == null) {
                return false;
            }
            if (j == -1000) {
                if (this.mMediaIds.length == 0) {
                    z3 = true;
                }
                if (!z3) {
                    FragmentActivity activity2 = getActivity();
                    MediaAndAlbumOperations.OnCompleteListener onCompleteListener2 = new MediaAndAlbumOperations.OnCompleteListener() { // from class: com.miui.gallery.ui.addtoalbum.AddToAlbumPageActivity$InternalListenerWrapper$$ExternalSyntheticLambda5
                        @Override // com.miui.gallery.util.MediaAndAlbumOperations.OnCompleteListener
                        public final void onComplete(long[] jArr2) {
                            AddToAlbumPageActivity.InternalListenerWrapper.m1583onAlbumSelected$lambda1(AddToAlbumPageActivity.InternalListenerWrapper.this, j, jArr2);
                        }
                    };
                    boolean z4 = this.mHasVideo;
                    long[] jArr2 = this.mMediaIds;
                    MediaAndAlbumOperations.addToSecretAlbum(activity2, onCompleteListener2, z4, Arrays.copyOf(jArr2, jArr2.length));
                } else {
                    MediaAndAlbumOperations.addToSecretAlbum(getActivity(), new MediaAndAlbumOperations.OnCompleteListener() { // from class: com.miui.gallery.ui.addtoalbum.AddToAlbumPageActivity$InternalListenerWrapper$$ExternalSyntheticLambda3
                        @Override // com.miui.gallery.util.MediaAndAlbumOperations.OnCompleteListener
                        public final void onComplete(long[] jArr3) {
                            AddToAlbumPageActivity.InternalListenerWrapper.m1584onAlbumSelected$lambda2(AddToAlbumPageActivity.InternalListenerWrapper.this, j, jArr3);
                        }
                    }, this.mMediaUris, this.mHasVideo);
                }
                return true;
            } else if (j == 2147483642) {
                if (this.mMediaIds.length == 0) {
                    z3 = true;
                }
                if (!z3) {
                    FragmentActivity activity3 = getActivity();
                    MediaAndAlbumOperations.OnCompleteListener onCompleteListener3 = new MediaAndAlbumOperations.OnCompleteListener() { // from class: com.miui.gallery.ui.addtoalbum.AddToAlbumPageActivity$InternalListenerWrapper$$ExternalSyntheticLambda2
                        @Override // com.miui.gallery.util.MediaAndAlbumOperations.OnCompleteListener
                        public final void onComplete(long[] jArr3) {
                            AddToAlbumPageActivity.InternalListenerWrapper.m1585onAlbumSelected$lambda3(AddToAlbumPageActivity.InternalListenerWrapper.this, j, jArr3);
                        }
                    };
                    long[] jArr3 = this.mMediaIds;
                    MediaAndAlbumOperations.addToFavoritesById(activity3, onCompleteListener3, Arrays.copyOf(jArr3, jArr3.length));
                } else {
                    MediaAndAlbumOperations.addToFavoritesByUri(getActivity(), new MediaAndAlbumOperations.OnCompleteListener() { // from class: com.miui.gallery.ui.addtoalbum.AddToAlbumPageActivity$InternalListenerWrapper$$ExternalSyntheticLambda4
                        @Override // com.miui.gallery.util.MediaAndAlbumOperations.OnCompleteListener
                        public final void onComplete(long[] jArr4) {
                            AddToAlbumPageActivity.InternalListenerWrapper.m1586onAlbumSelected$lambda4(AddToAlbumPageActivity.InternalListenerWrapper.this, j, jArr4);
                        }
                    }, this.mMediaUris);
                }
                return true;
            } else {
                if (!z || !ShareAlbumHelper.isOtherShareAlbumId(j)) {
                    z2 = z;
                } else {
                    ToastUtils.makeText(getActivity(), (int) R.string.add_to_share_album_not_delete);
                    z2 = false;
                }
                if (this.mMediaIds.length == 0) {
                    z3 = true;
                }
                if (!z3) {
                    CopyMoveDialogFragment.show(getActivity(), j, this.mMediaIds, z2, new MediaAndAlbumOperations.OnAddAlbumListener() { // from class: com.miui.gallery.ui.addtoalbum.AddToAlbumPageActivity$InternalListenerWrapper$$ExternalSyntheticLambda0
                        @Override // com.miui.gallery.util.MediaAndAlbumOperations.OnAddAlbumListener
                        public final void onComplete(long[] jArr4, boolean z5) {
                            AddToAlbumPageActivity.InternalListenerWrapper.m1587onAlbumSelected$lambda5(AddToAlbumPageActivity.InternalListenerWrapper.this, j, jArr4, z5);
                        }
                    });
                } else {
                    CopyMoveDialogFragment.show(getActivity(), j, this.mMediaUris, z2, new MediaAndAlbumOperations.OnAddAlbumListener() { // from class: com.miui.gallery.ui.addtoalbum.AddToAlbumPageActivity$InternalListenerWrapper$$ExternalSyntheticLambda1
                        @Override // com.miui.gallery.util.MediaAndAlbumOperations.OnAddAlbumListener
                        public final void onComplete(long[] jArr4, boolean z5) {
                            AddToAlbumPageActivity.InternalListenerWrapper.m1588onAlbumSelected$lambda6(AddToAlbumPageActivity.InternalListenerWrapper.this, j, jArr4, z5);
                        }
                    });
                }
                return true;
            }
        }

        /* renamed from: onAlbumSelected$lambda-0 */
        public static final void m1582onAlbumSelected$lambda0(InternalListenerWrapper this$0, long j, boolean z, long[] jArr) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this$0.snedSuccess(j, jArr, z);
        }

        /* renamed from: onAlbumSelected$lambda-1 */
        public static final void m1583onAlbumSelected$lambda1(InternalListenerWrapper this$0, long j, long[] jArr) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this$0.onAddToSecretAlbumFinish(j, jArr);
        }

        /* renamed from: onAlbumSelected$lambda-2 */
        public static final void m1584onAlbumSelected$lambda2(InternalListenerWrapper this$0, long j, long[] jArr) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this$0.onAddToSecretAlbumFinish(j, jArr);
        }

        /* renamed from: onAlbumSelected$lambda-3 */
        public static final void m1585onAlbumSelected$lambda3(InternalListenerWrapper this$0, long j, long[] jArr) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this$0.onComplete(j, jArr, false);
        }

        /* renamed from: onAlbumSelected$lambda-4 */
        public static final void m1586onAlbumSelected$lambda4(InternalListenerWrapper this$0, long j, long[] jArr) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this$0.onComplete(j, jArr, false);
        }

        /* renamed from: onAlbumSelected$lambda-5 */
        public static final void m1587onAlbumSelected$lambda5(InternalListenerWrapper this$0, long j, long[] jArr, boolean z) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this$0.onComplete(j, jArr, z);
        }

        /* renamed from: onAlbumSelected$lambda-6 */
        public static final void m1588onAlbumSelected$lambda6(InternalListenerWrapper this$0, long j, long[] jArr, boolean z) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this$0.onComplete(j, jArr, z);
        }

        public void onComplete(long j, long[] jArr, boolean z) {
            if (this.mActivityRef.get() != null) {
                snedSuccess(j, jArr, z);
                AddToAlbumPageActivity addToAlbumPageActivity = this.mActivityRef.get();
                Intrinsics.checkNotNull(addToAlbumPageActivity);
                addToAlbumPageActivity.getPresenter().onRecordLastSelectedAlbum(j);
                AddToAlbumPageActivity addToAlbumPageActivity2 = this.mActivityRef.get();
                Intrinsics.checkNotNull(addToAlbumPageActivity2);
                addToAlbumPageActivity2.finish();
            }
        }

        public final void snedSuccess(long j, long[] jArr, boolean z) {
            Intent intent = new Intent();
            intent.putExtra("album_id", j);
            intent.putExtra("result_ids", jArr);
            intent.putExtra("delete_origin", z);
            if (Album.isFavoritesAlbum(j)) {
                intent.putExtra("request_code", 67);
            }
            this.fragment.onFinishComplete(intent);
        }

        public final void onAddToSecretAlbumFinish(long j, long[] jArr) {
            boolean z = true;
            onComplete(j, jArr, true);
            ArrayList<Uri> arrayList = this.mMediaUris;
            Long valueOf = arrayList == null ? null : Long.valueOf(arrayList.size());
            long length = valueOf == null ? this.mMediaIds.length : valueOf.longValue();
            HashMap hashMap = new HashMap();
            hashMap.put("tip", "403.26.0.1.11242");
            hashMap.put("ref_tip", "403.26.0.1.11240");
            hashMap.put(MiStat.Param.COUNT, Long.valueOf(length));
            if (jArr == null || jArr.length != length) {
                z = false;
            }
            hashMap.put("success", Boolean.valueOf(z));
            TrackController.trackClick(hashMap);
            showTipIfNeed();
        }

        public final void showTipIfNeed() {
            if (this.mFromType == 1019) {
                MediaAndAlbumOperations.showTutorial(this.mActivityRef.get());
            }
        }

        public final FragmentActivity getActivity() {
            if (this.mActivityRef.get() != null) {
                return this.mActivityRef.get();
            }
            return null;
        }
    }

    /* compiled from: AddToAlbumPageActivity.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public static /* synthetic */ void $r8$lambda$nCG5f74f0VhR3tBWQrI0uwkKAZM(AddToAlbumPageResult addToAlbumPageResult, ActivityResult activityResult) {
            m1578start$lambda0(addToAlbumPageResult, activityResult);
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        public final void start(ComponentActivity activity, boolean z, boolean z2, boolean z3, boolean z4, int i, boolean z5, long[] jArr, ArrayList<Uri> arrayList, AddToAlbumPageResult addToAlbumPageResult, boolean z6) {
            Intrinsics.checkNotNullParameter(activity, "activity");
            Bundle bundle = new Bundle(8);
            bundle.putBoolean("show_share_album", z);
            bundle.putBoolean("is_from_secret", z3);
            bundle.putBoolean("show_add_secret", z2);
            bundle.putBoolean("is_from_other_share_album", z4);
            bundle.putInt("extra_from_type", i);
            bundle.putBoolean("has_video", z5);
            bundle.putLongArray("media_id_array", jArr);
            bundle.putParcelableArrayList("media_uri_array", arrayList);
            start(activity, addToAlbumPageResult, bundle, z6);
        }

        public final void start(final ComponentActivity activity, final AddToAlbumPageResult addToAlbumPageResult, Bundle bundle, boolean z) {
            Intrinsics.checkNotNullParameter(activity, "activity");
            Intrinsics.checkNotNullParameter(bundle, "bundle");
            Intent intent = new Intent("com.miui.gallery.action.ACTION_ADD_TO_ALBUM_PAGE");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.putExtras(bundle);
            intent.putExtra("have_pdf_listener", z);
            final ActivityResultLauncher register = activity.getActivityResultRegistry().register("addToAlbumPage", new ActivityResultContracts$StartActivityForResult(), new ActivityResultCallback() { // from class: com.miui.gallery.ui.addtoalbum.AddToAlbumPageActivity$Companion$$ExternalSyntheticLambda0
                @Override // androidx.activity.result.ActivityResultCallback
                public final void onActivityResult(Object obj) {
                    AddToAlbumPageActivity.Companion.$r8$lambda$nCG5f74f0VhR3tBWQrI0uwkKAZM(AddToAlbumPageActivity.AddToAlbumPageResult.this, (ActivityResult) obj);
                }
            });
            Intrinsics.checkNotNullExpressionValue(register, "activity.activityResultR…yResult(it)\n            }");
            activity.getLifecycle().addObserver(new LifecycleEventObserver() { // from class: com.miui.gallery.ui.addtoalbum.AddToAlbumPageActivity$Companion$start$1
                @Override // androidx.lifecycle.LifecycleEventObserver
                public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
                    Intrinsics.checkNotNullParameter(source, "source");
                    Intrinsics.checkNotNullParameter(event, "event");
                    if (Lifecycle.Event.ON_DESTROY == event) {
                        register.unregister();
                        activity.getLifecycle().removeObserver(this);
                    }
                }
            });
            register.launch(intent);
        }

        /* renamed from: start$lambda-0 */
        public static final void m1578start$lambda0(AddToAlbumPageResult addToAlbumPageResult, ActivityResult activityResult) {
            if (addToAlbumPageResult == null) {
                return;
            }
            addToAlbumPageResult.onActivityResult(activityResult);
        }
    }

    /* compiled from: AddToAlbumPageActivity.kt */
    /* loaded from: classes2.dex */
    public static class AddToAlbumPageResult implements ActivityResultCallback<ActivityResult> {
        public void onComplete(Long l, long[] jArr, Boolean bool) {
            throw null;
        }

        public void onPdfClick() {
        }

        @Override // androidx.activity.result.ActivityResultCallback
        public void onActivityResult(ActivityResult activityResult) {
            boolean z = true;
            if (activityResult != null && activityResult.getResultCode() == -1) {
                Intent data = activityResult.getData();
                if (data == null || data.getIntExtra("request_code", 0) != 68) {
                    z = false;
                }
                if (z) {
                    onPdfClick();
                    return;
                }
                Intent data2 = activityResult.getData();
                Boolean bool = null;
                Long valueOf = data2 == null ? null : Long.valueOf(data2.getLongExtra("album_id", 0L));
                Intent data3 = activityResult.getData();
                long[] longArrayExtra = data3 == null ? null : data3.getLongArrayExtra("result_ids");
                Intent data4 = activityResult.getData();
                if (data4 != null) {
                    bool = Boolean.valueOf(data4.getBooleanExtra("delete_origin", false));
                }
                onComplete(valueOf, longArrayExtra, bool);
            }
        }
    }

    public final void onFinishByPdfClick() {
        Intent intent = new Intent();
        intent.putExtra("request_code", 68);
        setResult(-1, intent);
        finish();
    }

    public final void onFinishComplete(Intent intent) {
        setResult(-1, intent);
        finish();
    }
}
