package com.miui.gallery.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.app.fragment.AndroidFragment;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.cloudcontrol.strategies.ComponentsStrategy;
import com.miui.gallery.concurrent.PriorityThreadFactory;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.ui.ChooserFragment;
import com.miui.gallery.ui.ShareStateRouter;
import com.miui.gallery.ui.photoPage.bars.menuitem.Send;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.DisplayUtils;
import com.miui.gallery.util.Numbers;
import com.miui.gallery.util.ReflectUtils;
import com.miui.gallery.util.SecurityShareHelper;
import com.miui.gallery.util.ShareComponentSorter;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.CircleImageView;
import com.miui.gallery.widget.ViewPager;
import com.miui.gallery.widget.ViewUtils;
import com.miui.gallery.widget.recyclerview.BlankDivider;
import com.miui.imagecleanlib.ImageCleanManager;
import com.miui.mishare.app.view.MiShareGalleryTransferView;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* loaded from: classes2.dex */
public class ChooserFragment extends AndroidFragment implements ViewPager.OnPageChangeListener {
    public boolean isBinding;
    public ResolverAdapter mAdapter;
    public FilesNotYetSetListener mFilesNotYetSetListener;
    public BlankDivider mItemDecoration;
    public MiShareGalleryTransferView mMiShareTransferView;
    public ConstraintLayout mMishareWrapper;
    public Send.ChooserObserverState mObserverState;
    public OnIntentSelectedListener mOnIntentSelectedListener;
    public OnMishareClickedListener mOnMishareClickedListener;
    public OnProjectClickedListener mOnProjectClickedListener;
    public RecyclerView mRecyclerView;
    public ScreenThrowClickListener mScreenThrowClickListener;
    public SecurityShareHelper.SecureShareProgressDialogHelper mSecureShareProgressDialogHelper = new SecurityShareHelper.SecureShareProgressDialogHelper();
    public FrameLayout mShareAndDeleteLayout;
    public CheckBox mShareAndDeleteView;
    public int mShareMode;
    public ShareStateObserver mShareStateObserver;
    public ShareComponentSorter.OnInitializedListener mSorterInitializedListener;

    /* loaded from: classes2.dex */
    public interface OnFilesProcessedListener {
        void onFilesProcessed(List<Uri> list);
    }

    /* loaded from: classes2.dex */
    public interface OnIntentSelectedListener {
        void onIntentSelected(Intent intent);
    }

    /* loaded from: classes2.dex */
    public interface OnMishareClickedListener {
        void onMishareClicked(OnFilesProcessedListener onFilesProcessedListener);
    }

    /* loaded from: classes2.dex */
    public interface OnProjectClickedListener {
        void onProjectedClicked();
    }

    public static /* synthetic */ void $r8$lambda$hL9BwbsFwjzL_A_uAict6rcxDsg(ChooserFragment chooserFragment, RelativeLayout relativeLayout) {
        chooserFragment.lambda$showShareAndDeleteView$0(relativeLayout);
    }

    @Override // com.miui.gallery.widget.ViewPager.OnPageChangeListener
    public void onPageScrollStateChanged(int i) {
    }

    @Override // com.miui.gallery.widget.ViewPager.OnPageChangeListener
    public void onPageScrolled(int i, float f, int i2) {
    }

    @Override // com.miui.gallery.widget.ViewPager.OnPageChangeListener
    public void onPageSelected(int i) {
    }

    public static ChooserFragment newInstance(Intent intent, int i, boolean z, int i2) {
        ChooserFragment chooserFragment = new ChooserFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("key_target_intent", intent);
        bundle.putInt("key_theme", i);
        bundle.putBoolean("init_visible", z);
        bundle.putInt("share_mode", i2);
        chooserFragment.setArguments(bundle);
        return chooserFragment;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Intent intent;
        Bundle arguments = getArguments();
        if (arguments == null || (intent = (Intent) arguments.getParcelable("key_target_intent")) == null) {
            throw new IllegalArgumentException("target intent couldn't be null");
        }
        int i = arguments.getInt("key_theme", 0);
        boolean z = arguments.getBoolean("init_visible", true);
        this.mShareMode = arguments.getInt("share_mode", 1);
        View inflate = layoutInflater.inflate(R.layout.chooser_layout, viewGroup, false);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.chooser_recycler);
        this.mRecyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), 0, false));
        ResolverAdapter resolverAdapter = new ResolverAdapter(getActivity(), intent, i, z);
        this.mAdapter = resolverAdapter;
        this.mRecyclerView.setAdapter(resolverAdapter);
        BlankDivider blankDivider = new BlankDivider(getResources().getDimensionPixelSize(R.dimen.chooser_item_cell_margin), getResources().getDimensionPixelSize(R.dimen.chooser_item_cell_margin), getResources().getDimensionPixelOffset(R.dimen.chooser_item_cell_horizontal_padding), 0, 0);
        this.mItemDecoration = blankDivider;
        this.mRecyclerView.addItemDecoration(blankDivider);
        this.mAdapter.setOnIntentSelectedListener(new OnIntentSelectedListener() { // from class: com.miui.gallery.ui.ChooserFragment.1
            {
                ChooserFragment.this = this;
            }

            @Override // com.miui.gallery.ui.ChooserFragment.OnIntentSelectedListener
            public void onIntentSelected(Intent intent2) {
                if (ChooserFragment.this.mOnIntentSelectedListener != null) {
                    ChooserFragment.this.mOnIntentSelectedListener.onIntentSelected(intent2);
                }
            }
        });
        configView(inflate, this.mShareMode);
        if (!ShareComponentSorter.getInstance().initialized()) {
            DefaultLogger.d("ChooserFragment", "sorter not initialized");
            this.mSorterInitializedListener = new SortInitializeListener();
            ShareComponentSorter.getInstance().registerOnInitializedListener(this.mSorterInitializedListener);
            ShareComponentSorter.getInstance().initialize(getActivity().getApplicationContext());
        }
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof PhotoPageFragment) {
            this.mObserverState = (Send.ChooserObserverState) new ViewModelProvider(parentFragment).get(Send.ChooserObserverState.class);
        }
        onVisibilityChanged(z);
        return inflate;
    }

    /* loaded from: classes2.dex */
    public static class BaseInnerClass {
        public WeakReference<ChooserFragment> mChooserFragment;

        public BaseInnerClass(ChooserFragment chooserFragment) {
            this.mChooserFragment = new WeakReference<>(chooserFragment);
        }

        public void release() {
            WeakReference<ChooserFragment> weakReference = this.mChooserFragment;
            if (weakReference != null) {
                weakReference.clear();
                this.mChooserFragment = null;
            }
        }

        public boolean isValidWeakReference() {
            WeakReference<ChooserFragment> weakReference = this.mChooserFragment;
            return (weakReference == null || weakReference.get() == null || this.mChooserFragment.get().mMiShareTransferView == null || this.mChooserFragment.get().getActivity() == null) ? false : true;
        }
    }

    /* loaded from: classes2.dex */
    public static class ScreenThrowClickListener extends BaseInnerClass implements View.OnClickListener {
        public ScreenThrowClickListener(ChooserFragment chooserFragment) {
            super(chooserFragment);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            WeakReference<ChooserFragment> weakReference = this.mChooserFragment;
            if (weakReference == null || weakReference.get() == null || this.mChooserFragment.get().mOnProjectClickedListener == null) {
                return;
            }
            this.mChooserFragment.get().mOnProjectClickedListener.onProjectedClicked();
        }
    }

    /* loaded from: classes2.dex */
    public static class FilesNotYetSetListener extends BaseInnerClass implements MiShareGalleryTransferView.FilesNotYetSetListener {
        public CleanDoneListener mCleanDoneListener;
        public CleanProgressListener mCleanProgressListener;

        public static /* synthetic */ void $r8$lambda$nXtA1kcd6uvfgMTPTylI4_7PF2U(FilesNotYetSetListener filesNotYetSetListener, List list) {
            filesNotYetSetListener.lambda$fileNotYetSet$0(list);
        }

        public FilesNotYetSetListener(ChooserFragment chooserFragment) {
            super(chooserFragment);
        }

        @Override // com.miui.mishare.app.view.MiShareGalleryTransferView.FilesNotYetSetListener
        public void fileNotYetSet() {
            WeakReference<ChooserFragment> weakReference = this.mChooserFragment;
            if (weakReference == null || weakReference.get() == null || this.mChooserFragment.get().mOnMishareClickedListener == null) {
                return;
            }
            this.mChooserFragment.get().mOnMishareClickedListener.onMishareClicked(new OnFilesProcessedListener() { // from class: com.miui.gallery.ui.ChooserFragment$FilesNotYetSetListener$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.ui.ChooserFragment.OnFilesProcessedListener
                public final void onFilesProcessed(List list) {
                    ChooserFragment.FilesNotYetSetListener.$r8$lambda$nXtA1kcd6uvfgMTPTylI4_7PF2U(ChooserFragment.FilesNotYetSetListener.this, list);
                }
            });
        }

        public /* synthetic */ void lambda$fileNotYetSet$0(List list) {
            FragmentActivity activity = this.mChooserFragment.get().getActivity();
            if (activity == null) {
                return;
            }
            this.mCleanDoneListener = new CleanDoneListener(this.mChooserFragment.get(), list);
            CleanProgressListener cleanProgressListener = new CleanProgressListener(this.mChooserFragment.get());
            this.mCleanProgressListener = cleanProgressListener;
            SecurityShareHelper.cleanImageInfoAsync(activity, list, this.mCleanDoneListener, cleanProgressListener);
        }

        @Override // com.miui.gallery.ui.ChooserFragment.BaseInnerClass
        public void release() {
            super.release();
            CleanDoneListener cleanDoneListener = this.mCleanDoneListener;
            if (cleanDoneListener != null) {
                cleanDoneListener.release();
                this.mCleanDoneListener = null;
            }
            CleanProgressListener cleanProgressListener = this.mCleanProgressListener;
            if (cleanProgressListener != null) {
                cleanProgressListener.release();
                this.mCleanProgressListener = null;
            }
        }

        /* loaded from: classes2.dex */
        public static class CleanDoneListener extends BaseInnerClass implements SecurityShareHelper.OnCleanDoneListener {
            public final List<Uri> mUris;

            public CleanDoneListener(ChooserFragment chooserFragment, List<Uri> list) {
                super(chooserFragment);
                this.mUris = list;
            }

            @Override // com.miui.gallery.util.SecurityShareHelper.OnCleanDoneListener
            public void onCleanDone(List<Uri> list) {
                FragmentActivity activity;
                if (!BaseMiscUtil.isValid(list)) {
                    DefaultLogger.w("ChooserFragment", "no items returned by security share");
                } else if (!isValidWeakReference() || (activity = this.mChooserFragment.get().getActivity()) == null || activity.isDestroyed() || !MiShareGalleryTransferView.isServiceAvailable(activity)) {
                } else {
                    try {
                        this.mChooserFragment.get().mMiShareTransferView.sendFiles(list);
                    } catch (IllegalStateException e) {
                        DefaultLogger.d("ChooserFragment", "something should be done in main thread, we nevertheless catch it instead. this error is %s", e);
                    }
                    String packageName = activity.getPackageName();
                    List<Uri> list2 = this.mUris;
                    boolean z = false;
                    boolean z2 = list2 == list;
                    boolean z3 = list2 == list;
                    if (list.size() > 1) {
                        z = true;
                    }
                    SecurityShareHelper.analyticsImageShare(activity, packageName, z2, z3, z);
                }
            }
        }

        /* loaded from: classes2.dex */
        public static class CleanProgressListener extends BaseInnerClass implements ImageCleanManager.CleanProgressListener {
            /* renamed from: $r8$lambda$KYpm9MZcU3bOFpGNLGlrr-W78Lg */
            public static /* synthetic */ void m1435$r8$lambda$KYpm9MZcU3bOFpGNLGlrrW78Lg(CleanProgressListener cleanProgressListener, int i, int i2) {
                cleanProgressListener.lambda$onProgress$0(i, i2);
            }

            public CleanProgressListener(ChooserFragment chooserFragment) {
                super(chooserFragment);
            }

            @Override // com.miui.imagecleanlib.ImageCleanManager.CleanProgressListener
            public void onProgress(final int i, final int i2) {
                if (!isValidWeakReference() || i2 <= 10) {
                    return;
                }
                this.mChooserFragment.get().mMiShareTransferView.post(new Runnable() { // from class: com.miui.gallery.ui.ChooserFragment$FilesNotYetSetListener$CleanProgressListener$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        ChooserFragment.FilesNotYetSetListener.CleanProgressListener.m1435$r8$lambda$KYpm9MZcU3bOFpGNLGlrrW78Lg(ChooserFragment.FilesNotYetSetListener.CleanProgressListener.this, i, i2);
                    }
                });
            }

            public /* synthetic */ void lambda$onProgress$0(int i, int i2) {
                this.mChooserFragment.get().mSecureShareProgressDialogHelper.showProgressDialog(new WeakReference<>(this.mChooserFragment.get().getActivity()), i, i2);
                if (i == i2) {
                    this.mChooserFragment.get().mSecureShareProgressDialogHelper.dismissDialog();
                }
            }
        }
    }

    public void refreshView() {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.mRecyclerView.getLayoutParams();
        if (ShareStateRouter.IS_MISHARE_AVAILABLE.get(getActivity()).booleanValue()) {
            ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = getResources().getDimensionPixelSize(R.dimen.fast_share_chooser_margin_bottom);
            if (!ShareStateRouter.SHOULD_UPGRADE_NEARBY_SHARE.get(getActivity()).booleanValue()) {
                this.mMishareWrapper.setPadding(getResources().getDimensionPixelSize(R.dimen.fast_share_mishare_padding_horizontal), 0, getResources().getDimensionPixelSize(R.dimen.fast_share_mishare_padding_horizontal), getResources().getDimensionPixelSize(R.dimen.fast_share_mishare_padding_bottom));
                return;
            } else {
                this.mMishareWrapper.setPadding(getResources().getDimensionPixelSize(R.dimen.fast_share_mishare_padding_horizontal), 0, getResources().getDimensionPixelSize(R.dimen.fast_share_mishare_padding_horizontal), getResources().getDimensionPixelSize(R.dimen.fast_share_mishare_padding_bottom_with_nearby_share));
                return;
            }
        }
        ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = getResources().getDimensionPixelSize(R.dimen.fast_share_chooser_without_mishare_margin_bottom);
    }

    public void configView(View view, int i) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.mRecyclerView.getLayoutParams();
        if (ShareStateRouter.IS_MISHARE_AVAILABLE.get(getActivity()).booleanValue()) {
            this.mMiShareTransferView = (MiShareGalleryTransferView) view.findViewById(R.id.mishare);
            this.mMishareWrapper = (ConstraintLayout) view.findViewById(R.id.mishare_wrapper);
            this.mMiShareTransferView.setVisibility(0);
            this.mMiShareTransferView.setDeliveryTitle(getResources().getString(R.string.chooser_fragment_mishare_title));
            ScreenThrowClickListener screenThrowClickListener = new ScreenThrowClickListener(this);
            this.mScreenThrowClickListener = screenThrowClickListener;
            this.mMiShareTransferView.setScreenThrowClickListener(screenThrowClickListener);
            FilesNotYetSetListener filesNotYetSetListener = new FilesNotYetSetListener(this);
            this.mFilesNotYetSetListener = filesNotYetSetListener;
            this.mMiShareTransferView.setFileIfNotYet(filesNotYetSetListener);
            ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = getResources().getDimensionPixelSize(R.dimen.fast_share_chooser_margin_bottom);
            if (!ShareStateRouter.SHOULD_UPGRADE_NEARBY_SHARE.get(getActivity()).booleanValue()) {
                this.mMishareWrapper.setPadding(getResources().getDimensionPixelSize(R.dimen.fast_share_mishare_padding_horizontal), 0, getResources().getDimensionPixelSize(R.dimen.fast_share_mishare_padding_horizontal), getResources().getDimensionPixelSize(R.dimen.fast_share_mishare_padding_bottom));
            } else {
                this.mMishareWrapper.setPadding(getResources().getDimensionPixelSize(R.dimen.fast_share_mishare_padding_horizontal), 0, getResources().getDimensionPixelSize(R.dimen.fast_share_mishare_padding_horizontal), getResources().getDimensionPixelSize(R.dimen.fast_share_mishare_padding_bottom_with_nearby_share));
            }
            if (i != 2) {
                if (i != 3) {
                    return;
                }
                this.mMiShareTransferView.setMiPrintEnable(true);
                showShareAndDeleteView(view);
            }
            this.mMiShareTransferView.setScreenThrowEnable(false);
            setBindStatus(true);
            return;
        }
        ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = getResources().getDimensionPixelSize(R.dimen.fast_share_chooser_without_mishare_margin_bottom);
    }

    public final void showShareAndDeleteView(View view) {
        this.mShareAndDeleteLayout = (FrameLayout) view.findViewById(R.id.fl_share_and_delete);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_share_and_delete);
        this.mShareAndDeleteView = checkBox;
        checkBox.setChecked(GalleryPreferences.ScreenEditorPreference.isSendAndDelete());
        final RelativeLayout relativeLayout = (RelativeLayout) this.mMiShareTransferView.findViewById(R.id.rl_send_to);
        configShareAndDeleteView();
        relativeLayout.setVisibility(4);
        relativeLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.miui.gallery.ui.ChooserFragment$$ExternalSyntheticLambda0
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public final void onGlobalLayout() {
                ChooserFragment.$r8$lambda$hL9BwbsFwjzL_A_uAict6rcxDsg(ChooserFragment.this, relativeLayout);
            }
        });
    }

    public /* synthetic */ void lambda$showShareAndDeleteView$0(RelativeLayout relativeLayout) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.mShareAndDeleteLayout.getLayoutParams();
        if (((ViewGroup.MarginLayoutParams) layoutParams).height != relativeLayout.getHeight()) {
            ((ViewGroup.MarginLayoutParams) layoutParams).height = relativeLayout.getHeight();
            this.mShareAndDeleteLayout.setLayoutParams(layoutParams);
        }
    }

    public final void setBindStatus(boolean z) {
        MiShareGalleryTransferView miShareGalleryTransferView = this.mMiShareTransferView;
        if (miShareGalleryTransferView == null) {
            return;
        }
        if (z && !this.isBinding) {
            miShareGalleryTransferView.bind();
            this.isBinding = true;
        } else if (z || !this.isBinding) {
        } else {
            miShareGalleryTransferView.unbind();
            this.isBinding = false;
        }
    }

    public final void configShareAndDeleteView() {
        CheckBox checkBox = this.mShareAndDeleteView;
        if (checkBox != null) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) checkBox.getLayoutParams();
            int i = 0;
            if (this.mShareMode == 3 && BaseBuildUtil.isLargeScreenDevice()) {
                layoutParams.gravity = 8388691;
                layoutParams.setMarginStart(getResources().getDimensionPixelOffset(R.dimen.screen_share_and_delete_margin_start));
            } else if (BaseBuildUtil.isLargeScreenDevice() && BaseBuildUtil.isLargeHorizontalWindow()) {
                layoutParams.setMarginStart(getResources().getDimensionPixelOffset(R.dimen.photo_screen_share_and_delete_checkbox_margin_start));
                layoutParams.gravity = 8388691;
            } else {
                layoutParams.setMarginStart(0);
                layoutParams.gravity = 81;
            }
            this.mShareAndDeleteView.setLayoutParams(layoutParams);
            FrameLayout frameLayout = this.mShareAndDeleteLayout;
            if (DisplayUtils.isFreeformMode(getContext())) {
                i = 8;
            }
            frameLayout.setVisibility(i);
        }
    }

    public void setOnProjectClickedListener(OnProjectClickedListener onProjectClickedListener) {
        this.mOnProjectClickedListener = onProjectClickedListener;
    }

    public void setOnMishareClickedListener(OnMishareClickedListener onMishareClickedListener) {
        this.mOnMishareClickedListener = onMishareClickedListener;
    }

    public void setOnIntentSelectedListener(OnIntentSelectedListener onIntentSelectedListener) {
        this.mOnIntentSelectedListener = onIntentSelectedListener;
    }

    public OnProjectClickedListener getOnProjectClickedListener() {
        return this.mOnProjectClickedListener;
    }

    public OnMishareClickedListener getOnMishareClickedListener() {
        return this.mOnMishareClickedListener;
    }

    public OnIntentSelectedListener getOnIntentSelectedListener() {
        return this.mOnIntentSelectedListener;
    }

    public void onVisibilityChanged(boolean z) {
        setBindStatus(z);
        setStateObserver(z);
        ResolverAdapter resolverAdapter = this.mAdapter;
        if (resolverAdapter != null) {
            if (z) {
                resolverAdapter.resume();
            } else {
                resolverAdapter.pause();
            }
        }
    }

    public final void setStateObserver(boolean z) {
        MiShareGalleryTransferView miShareGalleryTransferView;
        if (z) {
            if (this.mShareStateObserver == null && (miShareGalleryTransferView = this.mMiShareTransferView) != null) {
                this.mShareStateObserver = new ShareStateObserver(miShareGalleryTransferView);
            }
            if (this.mShareStateObserver != null) {
                ShareStateRouter.getInstance().registerProjectStateObserver(this.mShareStateObserver);
                ShareStateRouter.getInstance().registerPrintStateObserver(this.mShareStateObserver);
            }
        } else if (this.mShareStateObserver != null) {
            ShareStateRouter.getInstance().removeProjectStateObserver(this.mShareStateObserver);
            ShareStateRouter.getInstance().removePrintStateObserver(this.mShareStateObserver);
            this.mShareStateObserver.release();
            this.mShareStateObserver = null;
        }
        Send.ChooserObserverState chooserObserverState = this.mObserverState;
        if (chooserObserverState != null) {
            chooserObserverState.mState.setValue(Boolean.valueOf(z));
        }
    }

    /* loaded from: classes2.dex */
    public static class ShareStateObserver implements ShareStateRouter.ProjectStateObserver, ShareStateRouter.PrintStateObserver {
        public WeakReference<MiShareGalleryTransferView> mTransferView;

        public ShareStateObserver(MiShareGalleryTransferView miShareGalleryTransferView) {
            this.mTransferView = new WeakReference<>(miShareGalleryTransferView);
        }

        @Override // com.miui.gallery.ui.ShareStateRouter.ProjectStateObserver
        public void onObserveProjectState(int i) {
            WeakReference<MiShareGalleryTransferView> weakReference = this.mTransferView;
            if (weakReference == null || weakReference.get() == null) {
                return;
            }
            if (i == 1) {
                this.mTransferView.get().setScreenThrowHighLight(false);
                this.mTransferView.get().setScreenThrowEnable(false);
            } else if (i == 2) {
                this.mTransferView.get().setScreenThrowHighLight(false);
                this.mTransferView.get().setScreenThrowEnable(true);
            } else if (i != 3) {
            } else {
                this.mTransferView.get().setScreenThrowHighLight(true);
            }
        }

        @Override // com.miui.gallery.ui.ShareStateRouter.PrintStateObserver
        public void onObservePrintState(boolean z) {
            WeakReference<MiShareGalleryTransferView> weakReference = this.mTransferView;
            if (weakReference == null || weakReference.get() == null) {
                return;
            }
            this.mTransferView.get().setMiPrintEnable(z);
        }

        public void release() {
            WeakReference<MiShareGalleryTransferView> weakReference = this.mTransferView;
            if (weakReference != null) {
                weakReference.clear();
                this.mTransferView = null;
            }
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        setStateObserver(false);
        if (this.mSorterInitializedListener != null) {
            ShareComponentSorter.getInstance().removeOnInitializedListener(this.mSorterInitializedListener);
        }
        setBindStatus(false);
        SecurityShareHelper.resetHideSettings(getContext());
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        MiShareGalleryTransferView miShareGalleryTransferView = this.mMiShareTransferView;
        if (miShareGalleryTransferView != null) {
            miShareGalleryTransferView.clearAnimation();
            try {
                Field field = ReflectUtils.getField(this.mMiShareTransferView.getClass(), "mHandler");
                field.setAccessible(true);
                ((Handler) field.get(this.mMiShareTransferView)).removeCallbacksAndMessages(null);
            } catch (IllegalAccessException unused) {
            }
        }
        ScreenThrowClickListener screenThrowClickListener = this.mScreenThrowClickListener;
        if (screenThrowClickListener != null) {
            screenThrowClickListener.release();
            this.mScreenThrowClickListener = null;
        }
        FilesNotYetSetListener filesNotYetSetListener = this.mFilesNotYetSetListener;
        if (filesNotYetSetListener != null) {
            filesNotYetSetListener.release();
            this.mFilesNotYetSetListener = null;
        }
        this.mOnIntentSelectedListener = null;
        this.mOnProjectClickedListener = null;
        this.mOnMishareClickedListener = null;
        ResolverAdapter resolverAdapter = this.mAdapter;
        if (resolverAdapter != null) {
            resolverAdapter.release();
        }
        this.mSecureShareProgressDialogHelper.dismissDialog();
        super.onDestroy();
    }

    public void requery(Intent intent) {
        this.mAdapter.requery(intent);
    }

    /* loaded from: classes2.dex */
    public static class ResolverAdapter extends RecyclerView.Adapter<CellHolder> {
        public Context mContext;
        public OnIntentSelectedListener mListener;
        public PackageManager mPm;
        public boolean mResumed;
        public Intent mTarIntent;
        public int mTheme;
        public LinkedList<WeakReference<View>> mCachedViews = new LinkedList<>();
        public List<DisplayResolvedInfo> mData = new ArrayList();
        public PriorityComparator mPriorityComparator = new PriorityComparator();
        public ResolveInfoLoader mLoader = new ResolveInfoLoader();

        public ResolverAdapter(Context context, Intent intent, int i, boolean z) {
            this.mResumed = false;
            this.mContext = context;
            this.mPm = context.getPackageManager();
            this.mTarIntent = intent;
            this.mTheme = i;
            reBuildList();
            this.mResumed = z;
        }

        public void resume() {
            if (!this.mResumed) {
                this.mResumed = true;
                notifyDataSetChanged();
            }
        }

        public void pause() {
            this.mResumed = false;
        }

        public void release() {
            this.mLoader.release();
            this.mListener = null;
        }

        public void setOnIntentSelectedListener(OnIntentSelectedListener onIntentSelectedListener) {
            this.mListener = onIntentSelectedListener;
        }

        public boolean requery(Intent intent) {
            if (intent == null || intent.filterEquals(this.mTarIntent)) {
                return false;
            }
            this.mTarIntent = intent;
            reBuildList();
            notifyDataSetChanged();
            return true;
        }

        public final void reBuildList() {
            this.mData.clear();
            long currentTimeMillis = System.currentTimeMillis();
            List<ResolveInfo> queryIntentActivities = this.mPm.queryIntentActivities(this.mTarIntent, 65536);
            if (queryIntentActivities != null) {
                filterResolveInfoList(queryIntentActivities);
                LinkedList linkedList = new LinkedList();
                addResolveListDedupe(linkedList, queryIntentActivities);
                int size = linkedList.size();
                if (size > 0) {
                    ResolveInfo resolveInfo = linkedList.get(0);
                    for (int i = 1; i < size; i++) {
                        ResolveInfo resolveInfo2 = linkedList.get(i);
                        if (resolveInfo.priority != resolveInfo2.priority || resolveInfo.isDefault != resolveInfo2.isDefault) {
                            while (i < size) {
                                linkedList.remove(i);
                                size--;
                            }
                        }
                    }
                    long currentTimeMillis2 = System.currentTimeMillis();
                    Comparator<ResolveInfo> createComparator = ShareComponentSorter.getInstance().createComparator();
                    this.mPriorityComparator.build(CloudControlStrategyHelper.getComponentPriority(), linkedList);
                    this.mPriorityComparator.mNormal = createComparator;
                    Collections.sort(linkedList, this.mPriorityComparator);
                    sortResolveList(linkedList);
                    DefaultLogger.d("ChooserFragment", "sortResolveList cost %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis2));
                    for (int i2 = 0; i2 < size; i2++) {
                        this.mData.add(new DisplayResolvedInfo(this.mTarIntent, linkedList.get(i2)));
                    }
                }
            }
            DefaultLogger.d("ChooserFragment", "reBuildList cost %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        }

        public final void filterResolveInfoList(List<ResolveInfo> list) {
            if (list == null) {
                return;
            }
            int i = 0;
            while (i < list.size()) {
                ResolveInfo resolveInfo = list.get(i);
                if (!resolveInfo.activityInfo.exported) {
                    list.remove(i);
                } else if (ShareStateRouter.SHOULD_UPGRADE_NEARBY_SHARE.get(this.mContext).booleanValue() && isNearbyShare(resolveInfo)) {
                    list.remove(i);
                } else {
                    String str = resolveInfo.activityInfo.permission;
                    if (!TextUtils.isEmpty(str) && this.mContext.checkPermission(str, Process.myPid(), Process.myUid()) != 0) {
                        list.remove(i);
                    }
                    i++;
                }
                i--;
                i++;
            }
        }

        public final void sortResolveList(List<ResolveInfo> list) {
            List<ComponentsStrategy.Component> shareComponents = CloudControlStrategyHelper.getShareComponents();
            LinkedList linkedList = new LinkedList();
            for (ComponentsStrategy.Component component : shareComponents) {
                ResolveInfo contains = contains(component, list);
                if (contains != null) {
                    linkedList.add(contains);
                    list.remove(contains);
                }
            }
            linkedList.addAll(list);
            list.clear();
            list.addAll(linkedList);
        }

        public final boolean isNearbyShare(ResolveInfo resolveInfo) {
            return TextUtils.equals("com.google.android.gms", resolveInfo.activityInfo.packageName) && TextUtils.equals("com.google.android.gms.nearby.sharing.ShareSheetActivity", resolveInfo.activityInfo.name);
        }

        public final ResolveInfo contains(ComponentsStrategy.Component component, List<ResolveInfo> list) {
            ListIterator<ResolveInfo> listIterator = list.listIterator();
            ResolveInfo resolveInfo = null;
            while (listIterator.hasNext()) {
                ResolveInfo next = listIterator.next();
                if (TextUtils.equals(next.activityInfo.packageName, component.getPackageName())) {
                    if (TextUtils.equals(next.activityInfo.name, component.getClassName())) {
                        return next;
                    }
                    resolveInfo = next;
                }
            }
            return resolveInfo;
        }

        public static boolean isSameResolvedComponent(ResolveInfo resolveInfo, ResolveInfo resolveInfo2) {
            return TextUtils.equals(resolveInfo.activityInfo.packageName, resolveInfo2.activityInfo.packageName) && TextUtils.equals(resolveInfo.activityInfo.name, resolveInfo2.activityInfo.name);
        }

        public final void addResolveListDedupe(List<ResolveInfo> list, List<ResolveInfo> list2) {
            boolean z;
            int size = list2.size();
            int size2 = list.size();
            for (int i = 0; i < size; i++) {
                ResolveInfo resolveInfo = list2.get(i);
                int i2 = 0;
                while (true) {
                    if (i2 >= size2) {
                        z = false;
                        break;
                    } else if (isSameResolvedComponent(resolveInfo, list.get(i))) {
                        z = true;
                        break;
                    } else {
                        i2++;
                    }
                }
                if (!z) {
                    list.add(resolveInfo);
                }
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* renamed from: onCreateViewHolder */
        public CellHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new CellHolder(viewGroup);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(CellHolder cellHolder, int i) {
            List<DisplayResolvedInfo> list = this.mData;
            if (list == null || list.size() == 0 || i >= this.mData.size()) {
                return;
            }
            DisplayResolvedInfo displayResolvedInfo = this.mData.get(i);
            if (cellHolder.needRefresh(displayResolvedInfo)) {
                cellHolder.mInfo = displayResolvedInfo;
                this.mLoader.loadInfo(this.mContext, cellHolder.mIcon, cellHolder.mText, displayResolvedInfo.getResolveInfo());
                return;
            }
            cellHolder.mInfo = displayResolvedInfo;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            List<DisplayResolvedInfo> list = this.mData;
            if (list != null) {
                return list.size();
            }
            return 0;
        }

        /* loaded from: classes2.dex */
        public static class ResolveInfoLoader {
            public Map<Integer, String> mCacheKey = Collections.synchronizedMap(new HashMap());
            public Map<String, LoadResult> mCacheResult = new HashMap();
            public ThreadPoolExecutor mExecutor = new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, new LinkedBlockingQueue(), new PriorityThreadFactory("thread-pool", 10));

            public void release() {
                try {
                    this.mCacheKey.clear();
                    this.mCacheResult.clear();
                    this.mExecutor.shutdownNow();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void loadInfo(Context context, ImageView imageView, TextView textView, ResolveInfo resolveInfo) {
                if (resolveInfo == null) {
                    return;
                }
                String generateKey = generateKey(resolveInfo);
                LoadingInfo loadingInfo = new LoadingInfo(imageView, textView, resolveInfo);
                this.mCacheKey.put(Integer.valueOf(loadingInfo.getViewId()), generateKey);
                LoadResult loadResult = this.mCacheResult.get(generateKey);
                if (loadResult != null) {
                    loadResult.setLoadingInfo(loadingInfo);
                    setResult(loadResult);
                    DefaultLogger.d("ChooserFragment", "load from cache");
                    return;
                }
                DefaultLogger.d("ChooserFragment", "load from file");
                submit(context, loadingInfo);
            }

            public final void submit(Context context, LoadingInfo loadingInfo) {
                new LoadTask(context).executeOnExecutor(this.mExecutor, loadingInfo);
            }

            public final void setResult(LoadResult loadResult) {
                if (needApplyResult(loadResult)) {
                    ImageView iconView = loadResult.mLoadingInfo.getIconView();
                    if (iconView != null) {
                        if (iconView instanceof CircleImageView) {
                            ((CircleImageView) iconView).setDrawableInset(-10, false);
                        }
                        iconView.setImageDrawable(loadResult.mIcon);
                        iconView.setContentDescription(loadResult.mLabel);
                    }
                    TextView labelView = loadResult.mLoadingInfo.getLabelView();
                    if (labelView != null) {
                        labelView.setText(loadResult.mLabel);
                    }
                    this.mCacheResult.remove(generateKey(loadResult.mLoadingInfo.mResolve));
                }
            }

            public final boolean needApplyResult(LoadResult loadResult) {
                LoadingInfo loadingInfo;
                if (loadResult == null || (loadingInfo = loadResult.mLoadingInfo) == null) {
                    return false;
                }
                this.mCacheResult.put(generateKey(loadingInfo.mResolve), loadResult);
                return TextUtils.equals(generateKey(loadResult.mLoadingInfo.mResolve), this.mCacheKey.get(Integer.valueOf(loadResult.mLoadingInfo.getViewId())));
            }

            public static String generateKey(ResolveInfo resolveInfo) {
                StringBuilder sb = new StringBuilder();
                if (resolveInfo != null) {
                    sb.append(resolveInfo.activityInfo.packageName);
                    sb.append("#");
                    sb.append(resolveInfo.activityInfo.name);
                }
                return sb.toString();
            }

            /* loaded from: classes2.dex */
            public static class LoadResult {
                public final Drawable mIcon;
                public final CharSequence mLabel;
                public LoadingInfo mLoadingInfo;

                public LoadResult(Drawable drawable, CharSequence charSequence) {
                    this.mIcon = drawable;
                    this.mLabel = charSequence;
                }

                public LoadResult setLoadingInfo(LoadingInfo loadingInfo) {
                    this.mLoadingInfo = loadingInfo;
                    return this;
                }
            }

            /* loaded from: classes2.dex */
            public static class LoadingInfo {
                public final WeakReference<ImageView> mIcon;
                public final WeakReference<TextView> mLabel;
                public final ResolveInfo mResolve;

                public LoadingInfo(ImageView imageView, TextView textView, ResolveInfo resolveInfo) {
                    this.mIcon = new WeakReference<>(imageView);
                    this.mLabel = new WeakReference<>(textView);
                    this.mResolve = resolveInfo;
                }

                public int getViewId() {
                    ImageView iconView = getIconView();
                    if (iconView != null) {
                        return iconView.hashCode();
                    }
                    return 0;
                }

                public ImageView getIconView() {
                    WeakReference<ImageView> weakReference = this.mIcon;
                    if (weakReference != null) {
                        return weakReference.get();
                    }
                    return null;
                }

                public TextView getLabelView() {
                    WeakReference<TextView> weakReference = this.mLabel;
                    if (weakReference != null) {
                        return weakReference.get();
                    }
                    return null;
                }
            }

            /* loaded from: classes2.dex */
            public class LoadTask extends AsyncTask<LoadingInfo, Void, LoadResult> {
                public WeakReference<Context> mContextRef;

                public LoadTask(Context context) {
                    ResolveInfoLoader.this = r1;
                    this.mContextRef = new WeakReference<>(context);
                }

                @Override // android.os.AsyncTask
                public LoadResult doInBackground(LoadingInfo... loadingInfoArr) {
                    LoadingInfo loadingInfo = loadingInfoArr[0];
                    if (loadingInfo != null) {
                        IconResult loadIcon = loadIcon(loadingInfo);
                        return new LoadResult(loadIcon.drawable, loadLabel(loadingInfo.mResolve)).setLoadingInfo(loadingInfo);
                    }
                    return null;
                }

                @Override // android.os.AsyncTask
                public void onPostExecute(LoadResult loadResult) {
                    ResolveInfoLoader.this.setResult(loadResult);
                }

                public final IconResult loadIcon(LoadingInfo loadingInfo) {
                    Drawable drawable = null;
                    try {
                        ResolveInfo resolveInfo = loadingInfo.mResolve;
                        Context context = this.mContextRef.get();
                        if (context != null) {
                            Resources resources = context.getResources();
                            ActivityInfo activityInfo = resolveInfo.activityInfo;
                            String str = activityInfo.name;
                            String str2 = activityInfo.packageName;
                            if (TextUtils.equals(str, "com.tencent.mm.ui.tools.ShareImgUI")) {
                                drawable = resources.getDrawable(R.drawable.ic_share_wechat);
                            } else if (TextUtils.equals(str, "com.tencent.mm.ui.tools.ShareToTimeLineUI")) {
                                drawable = resources.getDrawable(R.drawable.ic_share_wechat_moments);
                            } else if (TextUtils.equals(str, "com.tencent.mobileqq.activity.JumpActivity") && TextUtils.equals(str2, "com.tencent.mobileqq")) {
                                drawable = resources.getDrawable(R.drawable.ic_share_qq);
                            } else if (TextUtils.equals(str, "com.sina.weibo.composerinde.ComposerDispatchActivity")) {
                                drawable = resources.getDrawable(R.drawable.ic_share_weibo);
                            } else if (TextUtils.equals(str2, "com.qzone")) {
                                drawable = resources.getDrawable(R.drawable.ic_share_qzone);
                            }
                            if (drawable == null) {
                                drawable = resolveInfo.loadIcon(context.getPackageManager());
                            }
                        }
                    } catch (Exception e) {
                        DefaultLogger.e("ChooserFragment", e);
                    }
                    return new IconResult(drawable);
                }

                public final CharSequence loadLabel(ResolveInfo resolveInfo) {
                    int identifier;
                    Context context = this.mContextRef.get();
                    if (context != null) {
                        try {
                            Resources resources = context.getResources();
                            ActivityInfo activityInfo = resolveInfo.activityInfo;
                            String str = activityInfo.name;
                            String str2 = activityInfo.packageName;
                            if (TextUtils.equals(str, "com.tencent.mm.ui.tools.ShareImgUI")) {
                                identifier = resources.getIdentifier("com.tencent.mm_com.tencent.mm.ui.tools.ShareImgUI", "string", context.getPackageName());
                            } else if (TextUtils.equals(str, "com.tencent.mm.ui.tools.ShareToTimeLineUI")) {
                                identifier = resources.getIdentifier("com.tencent.mm_com.tencent.mm.ui.tools.ShareToTimeLineUI", "string", context.getPackageName());
                            } else if (TextUtils.equals(str, "com.tencent.mobileqq.activity.JumpActivity") && TextUtils.equals(str2, "com.tencent.mobileqq")) {
                                identifier = resources.getIdentifier("com.tencent.mobileqq_com.tencent.mobileqq.activity.JumpActivity", "string", context.getPackageName());
                            } else if (TextUtils.equals(str, "com.sina.weibo.composerinde.ComposerDispatchActivity")) {
                                identifier = resources.getIdentifier("com.sina.weibo_com.sina.weibo.composerinde.ComposerDispatchActivity", "string", context.getPackageName());
                            } else if (TextUtils.equals(str2, "com.qzone")) {
                                identifier = resources.getIdentifier("com.qzone_com.qzonex.module.operation.ui.QZonePublishMoodActivity", "string", context.getPackageName());
                            } else {
                                identifier = resources.getIdentifier(resolveInfo.activityInfo.packageName + "_" + resolveInfo.activityInfo.name, "string", context.getPackageName());
                            }
                            if (identifier != 0) {
                                return resources.getString(identifier);
                            }
                        } catch (Exception unused) {
                        }
                        return resolveInfo.loadLabel(context.getPackageManager());
                    }
                    return null;
                }
            }

            /* loaded from: classes2.dex */
            public static class IconResult {
                public final Drawable drawable;

                public IconResult(Drawable drawable) {
                    this.drawable = drawable;
                }
            }
        }

        /* loaded from: classes2.dex */
        public class CellHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public ImageView mIcon;
            public DisplayResolvedInfo mInfo;
            public TextView mText;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public CellHolder(ViewGroup viewGroup) {
                super(LayoutInflater.from(r4.mContext).inflate(R.layout.chooser_item_cell, viewGroup, false));
                ResolverAdapter.this = r4;
                this.mIcon = (ImageView) this.itemView.findViewById(R.id.chooser_icon);
                this.mText = (TextView) this.itemView.findViewById(R.id.chooser_text);
                if (r4.mTheme == 0) {
                    this.mText.setTextAppearance(r4.mContext, 2131951848);
                } else {
                    this.mText.setTextAppearance(r4.mContext, 2131951847);
                }
                ViewUtils.makeTextViewStartMarquee(this.mText);
                this.mIcon.setOnClickListener(this);
                FolmeUtil.setDefaultTouchAnim(this.mIcon, null, false, true, true);
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ShareComponentSorter.getInstance().touch(this.mInfo.mResolveInfo.activityInfo.packageName, this.mInfo.mResolveInfo.activityInfo.name);
                if (ResolverAdapter.this.mListener != null) {
                    ResolverAdapter.this.mListener.onIntentSelected(new Intent(this.mInfo.getResolvedIntent()));
                }
                ShareComponentSorter.getInstance().save();
            }

            public boolean needRefresh(DisplayResolvedInfo displayResolvedInfo) {
                return !displayResolvedInfo.equals(this.mInfo) || this.mIcon.getDrawable() == null;
            }
        }

        /* loaded from: classes2.dex */
        public static class DisplayResolvedInfo {
            public ResolveInfo mResolveInfo;
            public Intent mResolvedIntent;

            public DisplayResolvedInfo(Intent intent, ResolveInfo resolveInfo) {
                this.mResolveInfo = resolveInfo;
                Intent intent2 = new Intent(intent);
                this.mResolvedIntent = intent2;
                ActivityInfo activityInfo = this.mResolveInfo.activityInfo;
                intent2.setComponent(new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name));
            }

            public Intent getResolvedIntent() {
                return this.mResolvedIntent;
            }

            public ResolveInfo getResolveInfo() {
                return this.mResolveInfo;
            }

            public boolean equals(Object obj) {
                if (obj == null || !(obj instanceof DisplayResolvedInfo)) {
                    return false;
                }
                return ResolverAdapter.isSameResolvedComponent(getResolveInfo(), ((DisplayResolvedInfo) obj).getResolveInfo());
            }

            public int hashCode() {
                return (this.mResolveInfo.activityInfo.packageName + this.mResolveInfo.activityInfo.name).hashCode();
            }
        }
    }

    /* loaded from: classes2.dex */
    public class SortInitializeListener implements ShareComponentSorter.OnInitializedListener {
        public SortInitializeListener() {
            ChooserFragment.this = r1;
        }

        @Override // com.miui.gallery.util.ShareComponentSorter.OnInitializedListener
        public void onInitialized() {
            DefaultLogger.d("ChooserFragment", "sorter initialized, rebuild cells");
            if (ChooserFragment.this.mAdapter != null) {
                ChooserFragment.this.mAdapter.reBuildList();
                ChooserFragment.this.mAdapter.notifyDataSetChanged();
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class PriorityComparator implements Comparator<ResolveInfo> {
        public Comparator<ResolveInfo> mNormal;
        public HashMap<Component, Integer> mPriority;
        public Component mTemp;

        public PriorityComparator() {
            this.mPriority = new HashMap<>();
            this.mTemp = new Component();
        }

        @Override // java.util.Comparator
        public int compare(ResolveInfo resolveInfo, ResolveInfo resolveInfo2) {
            int compare = this.mNormal.compare(resolveInfo, resolveInfo2);
            if (compare != 0) {
                return compare;
            }
            return Integer.compare(Numbers.unbox(this.mPriority.get(this.mTemp.wrap(resolveInfo2)), 0), Numbers.unbox(this.mPriority.get(this.mTemp.wrap(resolveInfo)), 0));
        }

        public void build(List<ComponentsStrategy.Priority> list, List<ResolveInfo> list2) {
            int i;
            DefaultLogger.d("ChooserFragment", "build priority: %s", list);
            for (ResolveInfo resolveInfo : list2) {
                this.mTemp.wrap(resolveInfo);
                if (this.mPriority.get(this.mTemp) == null) {
                    Iterator<ComponentsStrategy.Priority> it = list.iterator();
                    while (true) {
                        i = 0;
                        boolean z = false;
                        if (!it.hasNext()) {
                            break;
                        }
                        ComponentsStrategy.Priority next = it.next();
                        ActivityInfo activityInfo = resolveInfo.activityInfo;
                        String str = activityInfo.packageName;
                        String str2 = activityInfo.name;
                        if ((activityInfo.applicationInfo.flags & 1) != 0) {
                            z = true;
                        }
                        if (next.match(str, str2, z)) {
                            i = next.value;
                            break;
                        }
                    }
                    this.mPriority.put(new Component(resolveInfo), Integer.valueOf(i));
                    Component component = this.mTemp;
                    DefaultLogger.d("ChooserFragment", "assign priority of %s, %s by %d", component.mPackageName, component.mClassName, Integer.valueOf(i));
                }
            }
            DefaultLogger.d("ChooserFragment", "build finish");
        }
    }

    /* loaded from: classes2.dex */
    public static class Component {
        public String mClassName;
        public int mHashCode;
        public String mPackageName;

        public Component() {
        }

        public Component(ResolveInfo resolveInfo) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            init(activityInfo.packageName, activityInfo.name);
        }

        public Component wrap(ResolveInfo resolveInfo) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            init(activityInfo.packageName, activityInfo.name);
            return this;
        }

        public final void init(String str, String str2) {
            this.mPackageName = str;
            this.mClassName = str2;
            this.mHashCode = (this.mPackageName + this.mClassName).hashCode();
        }

        public int hashCode() {
            return this.mHashCode;
        }

        public boolean equals(Object obj) {
            if (obj instanceof Component) {
                Component component = (Component) obj;
                if (this.mPackageName.equals(component.mPackageName) && this.mClassName.equals(component.mClassName)) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean getShareAndDeleteIsSelect() {
        CheckBox checkBox = this.mShareAndDeleteView;
        if (checkBox != null) {
            return checkBox.isChecked();
        }
        return false;
    }

    @Override // com.miui.gallery.app.fragment.AndroidFragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mItemDecoration.updateItemDecorationStartEnd(getResources().getDimensionPixelSize(R.dimen.chooser_item_cell_margin), getResources().getDimensionPixelSize(R.dimen.chooser_item_cell_margin));
        this.mRecyclerView.invalidateItemDecorations();
        refreshView();
        CheckBox checkBox = this.mShareAndDeleteView;
        if (checkBox == null || checkBox.getVisibility() != 0) {
            return;
        }
        configShareAndDeleteView();
    }
}
