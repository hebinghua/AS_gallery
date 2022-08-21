package com.miui.gallery.editor.photo.app.crop;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.MenuFragment;
import com.miui.gallery.editor.photo.app.crop.AutoCropManager;
import com.miui.gallery.editor.photo.app.crop.sdk.CropLibraryLoaderHelper;
import com.miui.gallery.editor.photo.app.menu.CropView;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.SdkProvider;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractCropFragment;
import com.miui.gallery.editor.photo.core.common.model.CropData;
import com.miui.gallery.editor.photo.core.imports.crop.CropFragment;
import com.miui.gallery.editor.photo.widgets.seekbar.ScaleView;
import com.miui.gallery.editor.ui.view.EditorBlankDivider;
import com.miui.gallery.editor.utils.EditorOrientationHelper;
import com.miui.gallery.net.library.LibraryLoaderHelper;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.BuildUtil;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerView;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import miuix.animation.Folme;
import miuix.animation.IStateStyle;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.TransitionListener;
import miuix.animation.listener.UpdateInfo;
import miuix.animation.utils.EaseManager;

/* loaded from: classes2.dex */
public class CropMenuFragment extends MenuFragment<AbstractCropFragment, SdkProvider<CropData, AbstractCropFragment>> {
    public CropAdapter mAdapter;
    public IStateStyle mAnimStateStyle;
    public AutoCropData mAutoCropData;
    public View.OnClickListener mAutoCropListener;
    public AutoCropManager mAutoCropManager;
    public Runnable mAutoCropRunnable;
    public View.OnClickListener mClearListener;
    public int mCurrentIndex;
    public float mCurrentProgress;
    public List<CropData> mDataList;
    public TextView mDegreeText;
    public int mDegreeTextWidth;
    public View mDegreeView;
    public Runnable mDoAutoCropRunnable;
    public LibraryLoaderHelper.DownloadStateListener mDownloadStartListener;
    public Handler mHandler;
    public AnimConfig mHideDegreeConfig;
    public Runnable mHideDegreeShowRunnable;
    public TransitionListener mHideTransitionListener;
    public boolean mIsHideDegreeAnimationRunning;
    public boolean mIsManual;
    public boolean mIsNotShowDegreeAnim;
    public boolean mIsShowDegreeAnimationRunning;
    public ImageButton mMirrorBtn;
    public View.OnClickListener mMirrorClickListener;
    public AbstractCropFragment.OnCropStateChangedListener mOnCropStateChangedListener;
    public OnItemClickListener mOnItemClickListener;
    public ScaleView.OnScaleListener mOnScaleListener;
    public SimpleRecyclerView mRecyclerView;
    public ImageView mResetDegree;
    public ValueAnimator mResetDegreeAnimator;
    public AnimatorListenerAdapter mResetDegreeListener;
    public ValueAnimator.AnimatorUpdateListener mResetDegreeUpdateListener;
    public ImageButton mRotateBtn;
    public View.OnClickListener mRotateClickListener;
    public ScaleView mScaleView;
    public ValueAnimator mScaleViewAnimator;
    public AnimConfig mShowDegreeConfig;
    public TransitionListener mShowDegreeTransitionListener;
    public TextView mTitle;

    public static /* synthetic */ void $r8$lambda$DqN2qFqfhzNAMmI3b10Y00o7XNU(CropMenuFragment cropMenuFragment) {
        cropMenuFragment.lambda$new$3();
    }

    public static /* synthetic */ void $r8$lambda$K2_qGDTDann_giKFh1gL1wcqNHM(CropMenuFragment cropMenuFragment) {
        cropMenuFragment.addDegreeView();
    }

    public static /* synthetic */ void $r8$lambda$UgEGJIoKHVUmkRhgEoWPjT6mg1w(CropMenuFragment cropMenuFragment, View view) {
        cropMenuFragment.lambda$new$0(view);
    }

    /* renamed from: $r8$lambda$dhJWDgHRdk06Ncf-twDqI0isQhA */
    public static /* synthetic */ void m754$r8$lambda$dhJWDgHRdk06NcftwDqI0isQhA(CropMenuFragment cropMenuFragment, ValueAnimator valueAnimator) {
        cropMenuFragment.lambda$new$4(valueAnimator);
    }

    /* renamed from: $r8$lambda$viqdYbwnbswD2V6xEsa2-UiS98A */
    public static /* synthetic */ void m755$r8$lambda$viqdYbwnbswD2V6xEsa2UiS98A(CropMenuFragment cropMenuFragment, View view) {
        cropMenuFragment.lambda$new$1(view);
    }

    public static /* synthetic */ void $r8$lambda$zmUgCp3ofRYd_dWrbw6F3s7lsOM(CropMenuFragment cropMenuFragment, View view) {
        cropMenuFragment.lambda$addDegreeView$2(view);
    }

    public CropMenuFragment() {
        super(Effect.CROP);
        this.mCurrentProgress = 0.0f;
        this.mAutoCropData = new AutoCropData();
        this.mMirrorClickListener = new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.app.crop.CropMenuFragment$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                CropMenuFragment.$r8$lambda$UgEGJIoKHVUmkRhgEoWPjT6mg1w(CropMenuFragment.this, view);
            }
        };
        this.mRotateClickListener = new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.app.crop.CropMenuFragment$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                CropMenuFragment.m755$r8$lambda$viqdYbwnbswD2V6xEsa2UiS98A(CropMenuFragment.this, view);
            }
        };
        this.mOnScaleListener = new ScaleView.OnScaleListener() { // from class: com.miui.gallery.editor.photo.app.crop.CropMenuFragment.1
            {
                CropMenuFragment.this = this;
            }

            @Override // com.miui.gallery.editor.photo.widgets.seekbar.ScaleView.OnScaleListener
            public void onChange(float f) {
                float decimalsDegree = CropMenuFragment.this.getDecimalsDegree(f);
                if (decimalsDegree == 0.0f) {
                    CropMenuFragment.this.mScaleView.setContentDescription(CropMenuFragment.this.getResources().getString(R.string.photo_editor_talkback_crop_scale_default_value));
                    LinearMotorHelper.performHapticFeedback(CropMenuFragment.this.mScaleView, LinearMotorHelper.HAPTIC_MESH_HEAVY);
                } else {
                    int i = CropMenuFragment.this.mCurrentIndex;
                    CropMenuFragment.this.mCurrentIndex = (int) (f / 2.25f);
                    if (i != CropMenuFragment.this.mCurrentIndex) {
                        LinearMotorHelper.performHapticFeedback(CropMenuFragment.this.mScaleView, LinearMotorHelper.HAPTIC_MESH_LIGHT);
                    }
                }
                if (decimalsDegree == CropMenuFragment.this.mCurrentProgress) {
                    return;
                }
                CropMenuFragment.this.mCurrentProgress = decimalsDegree;
                CropMenuFragment.this.mDegreeText.setText(CropMenuFragment.this.getResources().getString(R.string.photo_editor_crop_rotation_format, Integer.valueOf(Math.round(decimalsDegree))));
                ((AbstractCropFragment) CropMenuFragment.this.getRenderFragment()).tuning(decimalsDegree);
                CropMenuFragment.this.setTitleToReset();
                CropMenuFragment.this.showDegree();
                CropMenuFragment.this.mDegreeView.setContentDescription(CropMenuFragment.this.getResources().getQuantityString(R.plurals.photo_editor_talkback_crop_scale_value, Math.abs(Math.round(decimalsDegree)), Integer.valueOf(Math.round(decimalsDegree))));
                CropMenuFragment.this.mScaleView.setContentDescription(CropMenuFragment.this.getResources().getQuantityString(R.plurals.photo_editor_talkback_crop_scale_value, Math.abs(Math.round(decimalsDegree)), Integer.valueOf(Math.round(decimalsDegree))));
            }

            @Override // com.miui.gallery.editor.photo.widgets.seekbar.ScaleView.OnScaleListener
            public void onStartTrackingTouch(ScaleView scaleView) {
                ((AbstractCropFragment) CropMenuFragment.this.getRenderFragment()).prepareTuning();
                CropMenuFragment.this.showDegree();
            }

            @Override // com.miui.gallery.editor.photo.widgets.seekbar.ScaleView.OnScaleListener
            public void onStopTrackingTouch(ScaleView scaleView) {
                ((AbstractCropFragment) CropMenuFragment.this.getRenderFragment()).finishTuning();
                CropMenuFragment.this.hideDegree(1300);
            }

            @Override // com.miui.gallery.editor.photo.widgets.seekbar.ScaleView.OnScaleListener
            public void onReset(ScaleView scaleView) {
                CropMenuFragment.this.hideDegree(1300);
                ((AbstractCropFragment) CropMenuFragment.this.getRenderFragment()).finishTuning();
            }
        };
        this.mShowDegreeTransitionListener = new TransitionListener() { // from class: com.miui.gallery.editor.photo.app.crop.CropMenuFragment.2
            {
                CropMenuFragment.this = this;
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onUpdate(Object obj, Collection<UpdateInfo> collection) {
                super.onUpdate(obj, collection);
                float floatValue = UpdateInfo.findByName(collection, "progress").getFloatValue();
                CropMenuFragment.this.getHostAbility().getExtraContainer().setAlpha(floatValue);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) CropMenuFragment.this.mDegreeView.getLayoutParams();
                layoutParams.width = (int) (CropMenuFragment.this.mDegreeTextWidth * floatValue);
                CropMenuFragment.this.mDegreeView.setLayoutParams(layoutParams);
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onBegin(Object obj, Collection<UpdateInfo> collection) {
                CropMenuFragment.this.mIsShowDegreeAnimationRunning = true;
                CropMenuFragment.this.setDegreeLayoutVisible(0);
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onComplete(Object obj) {
                if (CropMenuFragment.this.mScaleView.getState() != ScaleView.STATE.FLING) {
                    CropMenuFragment.this.getHostAbility().getExtraContainer().setAlpha(1.0f);
                    CropMenuFragment.this.getHostAbility().getExtraContainer().setVisibility(0);
                }
                CropMenuFragment.this.mIsShowDegreeAnimationRunning = false;
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onCancel(Object obj) {
                CropMenuFragment.this.mIsShowDegreeAnimationRunning = false;
            }
        };
        this.mHideTransitionListener = new TransitionListener() { // from class: com.miui.gallery.editor.photo.app.crop.CropMenuFragment.3
            {
                CropMenuFragment.this = this;
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onBegin(Object obj, Collection<UpdateInfo> collection) {
                CropMenuFragment.this.mIsHideDegreeAnimationRunning = true;
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onUpdate(Object obj, Collection<UpdateInfo> collection) {
                super.onUpdate(obj, collection);
                CropMenuFragment.this.getHostAbility().getExtraContainer().setAlpha(1.0f - UpdateInfo.findByName(collection, "progress").getFloatValue());
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onComplete(Object obj) {
                CropMenuFragment.this.setDegreeLayoutVisible(8);
                CropMenuFragment.this.mIsHideDegreeAnimationRunning = false;
                CropMenuFragment.this.mIsNotShowDegreeAnim = false;
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onCancel(Object obj) {
                if (CropMenuFragment.this.mScaleView.getState() != ScaleView.STATE.FLING) {
                    CropMenuFragment.this.getHostAbility().getExtraContainer().setAlpha(1.0f);
                    CropMenuFragment.this.setDegreeLayoutVisible(0);
                }
                CropMenuFragment.this.mIsHideDegreeAnimationRunning = false;
                CropMenuFragment.this.mIsNotShowDegreeAnim = false;
            }
        };
        this.mHideDegreeShowRunnable = new Runnable() { // from class: com.miui.gallery.editor.photo.app.crop.CropMenuFragment$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                CropMenuFragment.$r8$lambda$DqN2qFqfhzNAMmI3b10Y00o7XNU(CropMenuFragment.this);
            }
        };
        this.mResetDegreeListener = new AnimatorListenerAdapter() { // from class: com.miui.gallery.editor.photo.app.crop.CropMenuFragment.4
            {
                CropMenuFragment.this = this;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                CropMenuFragment.this.mScaleView.reset();
                ((AbstractCropFragment) CropMenuFragment.this.getRenderFragment()).hideGuideLines();
            }
        };
        this.mResetDegreeUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.photo.app.crop.CropMenuFragment$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                CropMenuFragment.m754$r8$lambda$dhJWDgHRdk06NcftwDqI0isQhA(CropMenuFragment.this, valueAnimator);
            }
        };
        this.mOnCropStateChangedListener = new AbstractCropFragment.OnCropStateChangedListener() { // from class: com.miui.gallery.editor.photo.app.crop.CropMenuFragment.5
            @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractCropFragment.OnCropStateChangedListener
            public void changeRotationState(boolean z) {
            }

            {
                CropMenuFragment.this = this;
            }

            @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractCropFragment.OnCropStateChangedListener
            public void onCropped() {
                CropMenuFragment.this.setTitleToReset();
            }

            @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractCropFragment.OnCropStateChangedListener
            public void onRestored() {
                CropMenuFragment.this.setNormalTitle();
                CropMenuFragment.this.mScaleView.setOnScaleListener(null);
                CropMenuFragment.this.mScaleView.setValue(0.0f);
                CropMenuFragment.this.mScaleView.setOnScaleListener(CropMenuFragment.this.mOnScaleListener);
                CropMenuFragment.this.mDegreeText.setText(CropMenuFragment.this.getResources().getString(R.string.photo_editor_crop_rotation_format, 0));
                CropMenuFragment.this.mAdapter.setSelection(0);
            }

            @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractCropFragment.OnCropStateChangedListener
            public void onAutoCropFinished() {
                CropMenuFragment.this.getHostAbility().showInnerToast(CropMenuFragment.this.getResources().getString(R.string.photo_editor_crop_auto_tips));
            }
        };
        this.mClearListener = new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.app.crop.CropMenuFragment.6
            {
                CropMenuFragment.this = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ((AbstractCropFragment) CropMenuFragment.this.getRenderFragment()).restore();
                CropMenuFragment.this.mScaleView.setOnScaleListener(null);
                CropMenuFragment.this.mScaleView.setValue(0.0f);
                CropMenuFragment.this.mScaleView.setOnScaleListener(CropMenuFragment.this.mOnScaleListener);
                CropMenuFragment.this.hideDegree(0);
                CropMenuFragment.this.mScaleView.setOnScaleListener(null);
                CropMenuFragment.this.mScaleView.setValue(0.0f);
                CropMenuFragment.this.mScaleView.setOnScaleListener(CropMenuFragment.this.mOnScaleListener);
                ((AbstractCropFragment) CropMenuFragment.this.getRenderFragment()).hideGuideLines();
                CropMenuFragment.this.setNormalTitle();
                SamplingStatHelper.recordCountEvent("photo_editor", "crop_restore_click");
            }
        };
        this.mAutoCropListener = new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.app.crop.CropMenuFragment.7
            {
                CropMenuFragment.this = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CropMenuFragment.this.mIsManual = true;
                CropMenuFragment.this.doAutoCrop();
                SamplingStatHelper.recordCountEvent("photo_editor", "crop_auto_click");
            }
        };
        this.mOnItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.editor.photo.app.crop.CropMenuFragment.8
            {
                CropMenuFragment.this = this;
            }

            @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
            public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
                ((SimpleRecyclerView) recyclerView).setSpringEnabled(false);
                if (((CropData) CropMenuFragment.this.mDataList.get(i)) instanceof CropData.AspectRatio) {
                    CropMenuFragment.this.mAdapter.setSelection(i);
                    CropMenuFragment.this.hideDegree(0);
                    ((AbstractCropFragment) CropMenuFragment.this.getRenderFragment()).setAspectRatio((CropData.AspectRatio) CropMenuFragment.this.mAdapter.getSelectedItem());
                    HashMap hashMap = new HashMap(1);
                    hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, ((CropData.AspectRatio) CropMenuFragment.this.mAdapter.getSelectedItem()).name);
                    CropMenuFragment.this.getHostAbility().sample("crop_change_ratio", hashMap);
                }
                recyclerView.smoothScrollToPosition(i);
                return true;
            }
        };
        this.mDoAutoCropRunnable = new Runnable() { // from class: com.miui.gallery.editor.photo.app.crop.CropMenuFragment.10
            {
                CropMenuFragment.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                CropMenuFragment.this.mIsManual = true;
                CropMenuFragment.this.doAutoCrop();
            }
        };
        this.mAutoCropRunnable = new Runnable() { // from class: com.miui.gallery.editor.photo.app.crop.CropMenuFragment.11
            {
                CropMenuFragment.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (!CropMenuFragment.this.isAdded()) {
                    return;
                }
                if (!CropMenuFragment.this.mIsManual) {
                    CropMenuFragment.this.mTitle.setText(R.string.photo_editor_crop_reset);
                    CropMenuFragment.this.mTitle.setOnClickListener(CropMenuFragment.this.mClearListener);
                }
                CropMenuFragment.this.hideDegree(1300);
                CropMenuFragment.this.mDegreeText.setText(CropMenuFragment.this.getResources().getString(R.string.photo_editor_crop_rotation_format, Integer.valueOf((int) CropMenuFragment.this.mAutoCropData.getDegree())));
                CropMenuFragment.this.mScaleView.setEnabled(true);
            }
        };
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, com.miui.gallery.editor.photo.app.EditorFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ArrayList arrayList = new ArrayList();
        this.mDataList = arrayList;
        arrayList.addAll(this.mSdkProvider.list());
        this.mAutoCropManager = new AutoCropManager();
        processAutoCrop();
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, com.miui.gallery.editor.utils.LayoutOrientationTracker.OnLayoutOrientationChangeListener
    public void onLayoutOrientationChange() {
        super.onLayoutOrientationChange();
        EditorOrientationHelper.copyLayoutParams(new CropView(getContext()), getView(), true);
        View view = this.mDegreeView;
        if (view == null || view.getParent() == null) {
            return;
        }
        this.mDegreeView.setLayoutParams(getDegreeViewLayoutParam());
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return new CropView(viewGroup.getContext());
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        getRenderFragment().setOnCropChangedListener(this.mOnCropStateChangedListener);
        ScaleView scaleView = (ScaleView) view.findViewById(R.id.scale_view);
        this.mScaleView = scaleView;
        scaleView.initViewParam(0.0f, -45.0f, 45.0f, 2.25f);
        this.mScaleView.setOnScaleListener(this.mOnScaleListener);
        this.mMirrorBtn = (ImageButton) view.findViewById(R.id.mirror_btn);
        FolmeUtil.setCustomTouchAnim(this.mMirrorBtn, new AnimParams.Builder().setAlpha(0.6f).setScale(1.0f).build(), null, null, true);
        this.mMirrorBtn.setOnClickListener(this.mMirrorClickListener);
        this.mRotateBtn = (ImageButton) view.findViewById(R.id.rotate_btn);
        FolmeUtil.setCustomTouchAnim(this.mRotateBtn, new AnimParams.Builder().setAlpha(0.6f).setScale(1.0f).build(), null, null, true);
        this.mRotateBtn.setOnClickListener(this.mRotateClickListener);
        TextView textView = (TextView) View.inflate(getActivity(), R.layout.seekbar_bubble_indicator_text, null);
        SimpleRecyclerView simpleRecyclerView = (SimpleRecyclerView) view.findViewById(R.id.recycler_view);
        this.mRecyclerView = simpleRecyclerView;
        simpleRecyclerView.setEnableItemClickWhileSettling(true);
        int dimension = (int) getResources().getDimension(R.dimen.editor_crop_menu_recyclerview_margin_start);
        this.mRecyclerView.addItemDecoration(new EditorBlankDivider(dimension, dimension, (int) getResources().getDimension(R.dimen.editor_crop_menu_item_gap), 0, 0));
        CropAdapter cropAdapter = new CropAdapter(getActivity(), this.mDataList, null);
        this.mAdapter = cropAdapter;
        this.mRecyclerView.setAdapter(cropAdapter);
        this.mAdapter.setOnItemClickListener(this.mOnItemClickListener);
        this.mTitle = (TextView) view.findViewById(R.id.title);
        FolmeUtil.setCustomTouchAnim(this.mTitle, new AnimParams.Builder().setAlpha(0.6f).setScale(1.0f).build(), null, null, true);
        this.mTitle.setVisibility(4);
        this.mTitle.post(new Runnable() { // from class: com.miui.gallery.editor.photo.app.crop.CropMenuFragment$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                CropMenuFragment.$r8$lambda$K2_qGDTDann_giKFh1gL1wcqNHM(CropMenuFragment.this);
            }
        });
        this.mHandler = new Handler();
    }

    public /* synthetic */ void lambda$new$0(View view) {
        hideDegree(0);
        getRenderFragment().doMirror();
    }

    public /* synthetic */ void lambda$new$1(View view) {
        hideDegree(0);
        getRenderFragment().doRotate();
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment
    public void notifySave() {
        AbstractCropFragment renderFragment = getRenderFragment();
        if (!(renderFragment instanceof CropFragment) || !((CropFragment) renderFragment).isCanDoSaveOperation()) {
            return;
        }
        super.notifySave();
    }

    public final void processAutoCrop() {
        if (!BuildUtil.isEditorProcess()) {
            if (CropLibraryLoaderHelper.getInstance().checkHasDownload()) {
                new AutoCropTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
            } else if (BaseNetworkUtils.isActiveNetworkMetered()) {
            } else {
                CropLibraryLoaderHelper.getInstance().checkAbleOrDownload(getActivity());
            }
        }
    }

    public final void addDegreeView() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.photo_editor_crop_degree_show_item, getHostAbility().getExtraContainer(), false);
        this.mDegreeView = inflate;
        this.mDegreeText = (TextView) inflate.findViewById(R.id.degree);
        ImageView imageView = (ImageView) this.mDegreeView.findViewById(R.id.reset_degree);
        this.mResetDegree = imageView;
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.app.crop.CropMenuFragment$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                CropMenuFragment.$r8$lambda$zmUgCp3ofRYd_dWrbw6F3s7lsOM(CropMenuFragment.this, view);
            }
        });
        this.mDegreeText.setText(getResources().getString(R.string.photo_editor_crop_rotation_format, Integer.valueOf(Math.round(this.mCurrentProgress))));
        getHostAbility().addViewToExtraContainer(this.mDegreeView, getDegreeViewLayoutParam());
        getHostAbility().getExtraContainer().setAlpha(0.0f);
    }

    public /* synthetic */ void lambda$addDegreeView$2(View view) {
        this.mIsNotShowDegreeAnim = true;
        resetDegreeAnimator();
        hideDegree(0);
        getHostAbility().sample("crop_reset_degree");
    }

    public final FrameLayout.LayoutParams getDegreeViewLayoutParam() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        if (isLayoutPortrait()) {
            layoutParams.gravity = 81;
            this.mDegreeTextWidth = (int) getResources().getDimension(R.dimen.editor_crop_menu_degree_show_width);
            layoutParams.width = 0;
            layoutParams.height = (int) getResources().getDimension(R.dimen.editor_crop_menu_degree_show_height);
            layoutParams.bottomMargin = (int) getResources().getDimension(R.dimen.editor_crop_menu_degree_show_bottom_margin);
        } else {
            this.mDegreeTextWidth = (int) getResources().getDimension(R.dimen.editor_crop_menu_degree_show_width);
            layoutParams.width = 0;
            layoutParams.height = (int) getResources().getDimension(R.dimen.editor_crop_menu_degree_show_height);
            layoutParams.setMarginEnd((int) getResources().getDimension(R.dimen.editor_crop_menu_degree_show_end_margin));
            layoutParams.gravity = 8388629;
        }
        return layoutParams;
    }

    public final void setDegreeLayoutVisible(int i) {
        getHostAbility().getExtraContainer().setVisibility(i);
    }

    public final void showDegree() {
        if (this.mIsNotShowDegreeAnim) {
            return;
        }
        this.mHandler.removeCallbacks(this.mHideDegreeShowRunnable);
        IStateStyle iStateStyle = this.mAnimStateStyle;
        if (iStateStyle != null && this.mIsHideDegreeAnimationRunning) {
            iStateStyle.cancel();
        }
        if (this.mIsShowDegreeAnimationRunning || Math.round(getHostAbility().getExtraContainer().getAlpha()) == 1) {
            return;
        }
        if (this.mShowDegreeConfig == null) {
            AnimConfig ease = new AnimConfig().setEase(EaseManager.getStyle(-2, 0.9f, 0.3f));
            this.mShowDegreeConfig = ease;
            ease.setMinDuration(200L);
            this.mShowDegreeConfig.addListeners(this.mShowDegreeTransitionListener);
        }
        Folme.clean(getHostAbility().getExtraContainer());
        Folme.useValue(new Object[0]).fromTo(new AnimState("showFrom").add("progress", SearchStatUtils.POW), new AnimState("showTo").add("progress", 1.0d), this.mShowDegreeConfig);
    }

    public final void hideDegree(int i) {
        this.mHandler.removeCallbacks(this.mHideDegreeShowRunnable);
        this.mHandler.postDelayed(this.mHideDegreeShowRunnable, i);
    }

    public /* synthetic */ void lambda$new$3() {
        if (this.mIsHideDegreeAnimationRunning) {
            return;
        }
        Folme.clean(getHostAbility().getExtraContainer());
        if (this.mHideDegreeConfig == null) {
            AnimConfig ease = new AnimConfig().setEase(EaseManager.getStyle(-2, 0.9f, 0.3f));
            this.mHideDegreeConfig = ease;
            ease.setMinDuration(200L);
            this.mHideDegreeConfig.addListeners(this.mHideTransitionListener);
        }
        this.mAnimStateStyle = Folme.useValue(new Object[0]).fromTo(new AnimState("hideFrom").add("progress", SearchStatUtils.POW), new AnimState("hideTo").add("progress", 1.0d), this.mHideDegreeConfig);
    }

    public final void resetDegreeAnimator() {
        if (this.mResetDegreeAnimator == null) {
            ValueAnimator valueAnimator = new ValueAnimator();
            this.mResetDegreeAnimator = valueAnimator;
            valueAnimator.addUpdateListener(this.mResetDegreeUpdateListener);
            this.mResetDegreeAnimator.addListener(this.mResetDegreeListener);
        }
        this.mResetDegreeAnimator.setFloatValues(this.mCurrentProgress, 0.0f);
        this.mResetDegreeAnimator.setDuration(200L);
        this.mResetDegreeAnimator.start();
    }

    public /* synthetic */ void lambda$new$4(ValueAnimator valueAnimator) {
        this.mScaleView.setOnScaleListener(null);
        this.mScaleView.setValue(((Float) valueAnimator.getAnimatedValue()).floatValue());
        this.mScaleView.setOnScaleListener(this.mOnScaleListener);
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        getRenderFragment().setOnCropChangedListener(null);
        this.mHandler.removeCallbacks(this.mHideDegreeShowRunnable);
        this.mHandler.removeCallbacks(this.mAutoCropRunnable);
        this.mHandler.removeCallbacks(this.mDoAutoCropRunnable);
        getHostAbility().clearExtraContainer();
        ValueAnimator valueAnimator = this.mScaleViewAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mScaleViewAnimator.cancel();
        }
        if (this.mDownloadStartListener != null) {
            CropLibraryLoaderHelper.getInstance().removeDownloadStateListener(this.mDownloadStartListener);
            this.mDownloadStartListener = null;
        }
    }

    public final void setNormalTitle() {
        if (this.mAutoCropData.canAutoCropOrRotation()) {
            this.mTitle.setText(R.string.photo_editor_crop_auto);
            this.mTitle.setTextColor(getResources().getColor(R.color.photo_editor_white_color));
            this.mTitle.setOnClickListener(this.mAutoCropListener);
            return;
        }
        this.mTitle.setText("");
        this.mTitle.setOnClickListener(null);
    }

    public final float getDecimalsDegree(float f) {
        return (float) new BigDecimal(f).setScale(1, 4).doubleValue();
    }

    public final void setTitleToReset() {
        this.mTitle.setText(R.string.photo_editor_crop_reset);
        this.mTitle.setTextColor(getResources().getColor(R.color.photo_editor_white_color));
        this.mTitle.setOnClickListener(this.mClearListener);
    }

    public final void doAutoCrop() {
        this.mTitle.setOnClickListener(null);
        if (getRenderFragment() == null) {
            return;
        }
        getRenderFragment().autoCrop(this.mAutoCropData);
        doScaleAnimator(0.0f, this.mAutoCropData.getDegree());
        if (this.mIsManual) {
            this.mTitle.setText(R.string.photo_editor_crop_auto);
            this.mTitle.setTextColor(getResources().getColor(R.color.photo_editor_highlight_color));
            this.mTitle.setOnClickListener(this.mClearListener);
        }
        this.mScaleView.setEnabled(false);
        this.mHandler.postDelayed(this.mAutoCropRunnable, 400L);
    }

    public final void doScaleAnimator(float f, float f2) {
        if (this.mScaleViewAnimator == null) {
            ValueAnimator valueAnimator = new ValueAnimator();
            this.mScaleViewAnimator = valueAnimator;
            valueAnimator.setDuration(200L);
            this.mScaleViewAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.photo.app.crop.CropMenuFragment.9
                {
                    CropMenuFragment.this = this;
                }

                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    CropMenuFragment.this.mScaleView.setOnScaleListener(null);
                    CropMenuFragment.this.mScaleView.setValue(((Float) valueAnimator2.getAnimatedValue()).floatValue());
                    CropMenuFragment.this.mScaleView.setOnScaleListener(CropMenuFragment.this.mOnScaleListener);
                }
            });
        }
        if (this.mScaleViewAnimator.isRunning()) {
            this.mScaleViewAnimator.cancel();
        }
        this.mScaleViewAnimator.setFloatValues(f, f2);
        this.mScaleViewAnimator.start();
    }

    /* loaded from: classes2.dex */
    public class AutoCropTask extends AsyncTask<Void, Void, Void> {
        public AutoCropTask() {
            CropMenuFragment.this = r1;
        }

        @Override // android.os.AsyncTask
        public Void doInBackground(Void... voidArr) {
            try {
                try {
                    if (CropMenuFragment.this.getPreview() != null && AutoCropJni.isAvailable()) {
                        CropMenuFragment.this.mAutoCropManager.create();
                        if (CropMenuFragment.this.mAutoCropManager.init() == AutoCropManager.AutoCropError.AUTO_CROP_ERROR_SUCCESS.ordinal()) {
                            DefaultLogger.d("CropMenuFragment", "auto crop sdk version = " + CropMenuFragment.this.mAutoCropManager.getVersion());
                            CropMenuFragment.this.mAutoCropData.rotationResult = CropMenuFragment.this.mAutoCropManager.getCropParams(CropMenuFragment.this.getPreview(), CropMenuFragment.this.mAutoCropData.angles, CropMenuFragment.this.mAutoCropData.mBox);
                            DefaultLogger.d("CropMenuFragment", "auto crop sdk response: angle = " + CropMenuFragment.this.mAutoCropData.getAngle() + "  \n BBox = " + CropMenuFragment.this.mAutoCropData.mBox.toString());
                            HashMap hashMap = new HashMap();
                            hashMap.put("result", String.valueOf(CropMenuFragment.this.mAutoCropData.getAngle()));
                            SamplingStatHelper.recordCountEvent("photo_editor", "crop_auto_rotation_result", hashMap);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                CropMenuFragment.this.mAutoCropManager.release();
                CropMenuFragment.this.mAutoCropManager.destroy();
                return null;
            } catch (Throwable th) {
                CropMenuFragment.this.mAutoCropManager.release();
                CropMenuFragment.this.mAutoCropManager.destroy();
                throw th;
            }
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Void r2) {
            if (CropMenuFragment.this.isAdded() && !CropMenuFragment.this.isRemoving()) {
                CropMenuFragment.this.mTitle.setVisibility(0);
                CropMenuFragment.this.setNormalTitle();
                return;
            }
            DefaultLogger.w("CropMenuFragment", "AutoCrop fragment isRemoved");
        }
    }
}
