package com.miui.gallery.editor.photo.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.airbnb.lottie.LottieAnimationView;
import com.miui.gallery.R;
import com.miui.gallery.app.fragment.AndroidFragment;
import com.miui.gallery.compat.view.ViewCompat;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.widgets.PreviewImageView;
import com.miui.gallery.editor.utils.EditorOrientationHelper;
import com.miui.gallery.editor.utils.FolmeUtilsEditor;
import com.miui.gallery.editor.utils.LayoutOrientationTracker;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.SystemUiUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.market.MediaEditorInstaller;
import com.miui.mediaeditor.utils.MediaEditorUtils;
import com.xiaomi.stat.a;
import java.util.HashMap;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes2.dex */
public class PreviewFragment extends AndroidFragment implements LayoutOrientationTracker.OnLayoutOrientationChangeListener {
    public int mAnimatorDuration;
    public Callbacks mCallbacks;
    public int mCompareButtonDelay;
    public View mDiscardView;
    public LottieAnimationView mDownloadMediaEditorAppView;
    public View mExportView;
    public boolean mExportable;
    public HostAbility mHostAbility;
    public PreviewImageView mImageView;
    public long mLastClickTime;
    public boolean mOverwriteBackground;
    public Bitmap mPreviewBitmap;
    public View mProgressView;
    public Button mRemoverBtn;
    public ValueAnimator mValueAnimator;
    public boolean mLoadDone = true;
    public boolean mIsOnExit = false;
    public boolean mCompareEnable = false;
    public boolean mRemoveWatermarkEnable = false;
    public boolean mRemoveWatermarkSelected = false;
    public Handler mHandler = new Handler();
    public MediaEditorInstaller.Callback mInstallMediaEditorCallback = new MediaEditorInstaller.Callback() { // from class: com.miui.gallery.editor.photo.app.PreviewFragment.1
        {
            PreviewFragment.this = this;
        }

        @Override // com.miui.gallery.util.market.MediaEditorInstaller.Callback
        public void onInstallSuccess() {
            if (PreviewFragment.this.mDownloadMediaEditorAppView != null) {
                PreviewFragment.this.mDownloadMediaEditorAppView.setVisibility(MediaEditorUtils.isMediaEditorAvailable() ? 8 : 0);
            }
        }
    };
    public View.OnClickListener mClickListener = new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.app.PreviewFragment.2
        {
            PreviewFragment.this = this;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - PreviewFragment.this.mLastClickTime < 100) {
                return;
            }
            PreviewFragment.this.mLastClickTime = currentTimeMillis;
            if (view.getId() == R.id.export) {
                LinearMotorHelper.performHapticFeedback(view, LinearMotorHelper.HAPTIC_TAP_NORMAL);
                PreviewFragment.this.mCallbacks.onExport();
            } else if (view.getId() == R.id.discard) {
                LinearMotorHelper.performHapticFeedback(view, LinearMotorHelper.HAPTIC_TAP_NORMAL);
                PreviewFragment.this.mCallbacks.onDiscard();
            } else {
                throw new IllegalStateException("not support view");
            }
        }
    };
    public View.OnClickListener mRemoveWatermarkListener = new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.app.PreviewFragment.3
        {
            PreviewFragment.this = this;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            boolean isSelected = view.isSelected();
            view.setSelected(!isSelected);
            PreviewFragment.this.mCallbacks.removeWaterRender(isSelected);
            PreviewFragment.this.mCallbacks.setMaskMoved();
            PreviewFragment.this.mRemoveWatermarkSelected = view.isSelected();
            PreviewFragment.this.mRemoverBtn.setText(PreviewFragment.this.mRemoveWatermarkSelected ? R.string.photo_editor_remove_watermark_on : R.string.photo_editor_remove_watermark);
            PreviewFragment.this.mImageView.setMaskShow(!PreviewFragment.this.mRemoveWatermarkSelected);
        }
    };
    public Runnable mCompareRunnable = new Runnable() { // from class: com.miui.gallery.editor.photo.app.PreviewFragment.4
        {
            PreviewFragment.this = this;
        }

        @Override // java.lang.Runnable
        public void run() {
            PreviewFragment.this.mHandler.removeCallbacks(this);
            PreviewFragment.this.mImageView.setImageBitmap(PreviewFragment.this.mCallbacks.onLoadPreviewOriginal());
            HashMap hashMap = new HashMap();
            hashMap.put("page", a.d);
            SamplingStatHelper.recordCountEvent("photo_editor", "compare_button_touch", hashMap);
        }
    };
    public PreviewImageView.PreviewCallback mPreviewCallback = new PreviewImageView.PreviewCallback() { // from class: com.miui.gallery.editor.photo.app.PreviewFragment.6
        @Override // com.miui.gallery.editor.photo.widgets.PreviewImageView.PreviewCallback
        public void removerButtonShow(boolean z) {
        }

        {
            PreviewFragment.this = this;
        }

        @Override // com.miui.gallery.editor.photo.widgets.PreviewImageView.PreviewCallback
        public void setPreviewBitmap() {
            PreviewFragment.this.mImageView.setImageBitmap(PreviewFragment.this.mPreviewBitmap);
        }

        @Override // com.miui.gallery.editor.photo.widgets.PreviewImageView.PreviewCallback
        public void setCompareBitmap() {
            PreviewFragment.this.mImageView.setImageBitmap(PreviewFragment.this.mCallbacks.onLoadPreviewOriginal());
        }

        @Override // com.miui.gallery.editor.photo.widgets.PreviewImageView.PreviewCallback
        public void setMaskMoved() {
            PreviewFragment.this.mCallbacks.setMaskMoved();
        }
    };

    /* loaded from: classes2.dex */
    public interface Callbacks {
        WaterMaskWrapper getWaterMaskWrapper();

        boolean isPreviewChanged();

        boolean moveMaskEnable();

        void onDiscard();

        void onExport();

        Bitmap onLoadPreview();

        Bitmap onLoadPreviewOriginal();

        void removeWaterRender(boolean z);

        void setMaskMoved();
    }

    /* loaded from: classes2.dex */
    public interface OnPrepareEditFragmentListener {
        void showEditFragment(Effect effect);
    }

    public static /* synthetic */ void $r8$lambda$APHK1lqhQ2rkNRtX5pr72E9xN70(PreviewFragment previewFragment, int i, int i2, int i3, ConstraintLayout.LayoutParams layoutParams, int i4, ValueAnimator valueAnimator) {
        previewFragment.lambda$prepareShowEditFragment$1(i, i2, i3, layoutParams, i4, valueAnimator);
    }

    public static /* synthetic */ void $r8$lambda$YOIanA6Y1ymyK9AEC_wtuUY5v9w(PreviewFragment previewFragment, int i, int i2, int i3, ConstraintLayout.LayoutParams layoutParams, int i4, ValueAnimator valueAnimator) {
        previewFragment.lambda$hideEditFragment$2(i, i2, i3, layoutParams, i4, valueAnimator);
    }

    /* renamed from: $r8$lambda$lCApj_1wCCf8XFT6px-awIklQno */
    public static /* synthetic */ void m743$r8$lambda$lCApj_1wCCf8XFT6pxawIklQno(PreviewFragment previewFragment, View view) {
        previewFragment.lambda$onViewCreated$0(view);
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof HostAbility) {
            this.mHostAbility = (HostAbility) activity;
        }
    }

    @Override // com.miui.gallery.app.fragment.AndroidFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mAnimatorDuration = getResources().getInteger(R.integer.photo_editor_sub_editor_sub_menu_appear_duration);
        this.mCompareButtonDelay = getResources().getInteger(R.integer.compare_button_delay);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.preview_container, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    @SuppressLint({"ClickableViewAccessibility"})
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        PreviewImageView previewImageView = (PreviewImageView) view.findViewById(R.id.preview);
        this.mImageView = previewImageView;
        int i = 0;
        previewImageView.setStrokeVisible(false);
        this.mRemoverBtn = (Button) view.findViewById(R.id.remove_btn);
        this.mProgressView = view.findViewById(R.id.progress_bar);
        this.mExportView = view.findViewById(R.id.export);
        this.mDiscardView = view.findViewById(R.id.discard);
        LottieAnimationView lottieAnimationView = (LottieAnimationView) view.findViewById(R.id.download_mediaeditor_app_view);
        this.mDownloadMediaEditorAppView = lottieAnimationView;
        if (MediaEditorUtils.isMediaEditorAvailable()) {
            i = 8;
        }
        lottieAnimationView.setVisibility(i);
        this.mDownloadMediaEditorAppView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.app.PreviewFragment$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                PreviewFragment.m743$r8$lambda$lCApj_1wCCf8XFT6pxawIklQno(PreviewFragment.this, view2);
            }
        });
        ViewCompat.setTransitionName(this.mImageView, R.string.photo_editor_transition_image_view);
        if (getArguments() != null) {
            this.mOverwriteBackground = getArguments().getBoolean("overwrite_background");
        }
        if (this.mOverwriteBackground) {
            TypedValue typedValue = new TypedValue();
            getActivity().getTheme().resolveAttribute(16842801, typedValue, true);
            this.mImageView.setBackgroundColor(typedValue.data);
        }
        this.mRemoverBtn.setOnClickListener(this.mRemoveWatermarkListener);
        this.mExportView.setEnabled(this.mExportable);
        this.mExportView.setOnClickListener(this.mClickListener);
        this.mDiscardView.setOnClickListener(this.mClickListener);
        showForLoadDown();
        refreshRemoveButtonStatus();
        this.mRemoverBtn.setOnClickListener(this.mRemoveWatermarkListener);
        this.mImageView.setPreviewCallback(this.mPreviewCallback);
        this.mImageView.setOverwriteBackground(this.mOverwriteBackground);
        FolmeUtilsEditor.animButton(this.mDiscardView);
        FolmeUtilsEditor.animButton(this.mExportView);
        FolmeUtilsEditor.animButton(this.mRemoverBtn);
    }

    public /* synthetic */ void lambda$onViewCreated$0(View view) {
        MediaEditorInstaller.getInstance().installIfNotExist(getActivity(), this.mInstallMediaEditorCallback, true);
    }

    @Override // com.miui.gallery.editor.utils.LayoutOrientationTracker.OnLayoutOrientationChangeListener
    public void onLayoutOrientationChange() {
        View view = getView();
        EditorOrientationHelper.copyLayoutParams(getLayoutInflater().inflate(R.layout.preview_container, (ViewGroup) view.getParent(), false), view, true);
    }

    public void playDownloadMediaEditorAppAnimation() {
        this.mDownloadMediaEditorAppView.playAnimation();
    }

    public void closeMaskFrame(boolean z) {
        this.mImageView.closeMaskFrame(z);
    }

    public void showForLoadDown() {
        if (this.mLoadDone) {
            this.mImageView.setVisibility(0);
            Bitmap onLoadPreview = this.mCallbacks.onLoadPreview();
            this.mPreviewBitmap = onLoadPreview;
            this.mImageView.setImageBitmap(onLoadPreview);
            if (this.mCallbacks.moveMaskEnable()) {
                this.mImageView.setWaterMask(this.mCallbacks.getWaterMaskWrapper());
            }
            enableComparison(this.mCallbacks.isPreviewChanged());
            this.mProgressView.setVisibility(8);
        }
    }

    public final void refreshRemoveButtonStatus() {
        if (this.mRemoverBtn != null) {
            this.mRemoveWatermarkSelected = !this.mImageView.isShowMask();
            this.mRemoverBtn.setVisibility(this.mRemoveWatermarkEnable ? 0 : 8);
            this.mRemoverBtn.setSelected(this.mRemoveWatermarkSelected);
            this.mRemoverBtn.setText(this.mRemoveWatermarkSelected ? R.string.photo_editor_remove_watermark_on : R.string.photo_editor_remove_watermark);
        }
    }

    public void enableComparison(boolean z) {
        HostAbility hostAbility;
        this.mCompareEnable = z;
        this.mImageView.enableComparison(z);
        if (!this.mCompareEnable || !GalleryPreferences.PhotoEditor.shouldCompareTipShow() || (hostAbility = this.mHostAbility) == null) {
            return;
        }
        hostAbility.showInnerToast(getResources().getString(R.string.photo_editor_compare_tip));
        GalleryPreferences.PhotoEditor.setCompareTipShow();
    }

    public void refreshPreview(Bitmap bitmap) {
        if (!isAdded() || this.mIsOnExit) {
            return;
        }
        this.mPreviewBitmap = bitmap;
        this.mImageView.setImageBitmap(bitmap);
        this.mImageView.setWaterMask(this.mCallbacks.getWaterMaskWrapper());
    }

    public boolean isRunningPreviewAnimator() {
        ValueAnimator valueAnimator = this.mValueAnimator;
        if (valueAnimator != null) {
            return valueAnimator.isRunning();
        }
        return false;
    }

    public void setExportEnabled(boolean z) {
        View view = this.mExportView;
        if (view != null) {
            view.setEnabled(z);
            this.mExportView.setClickable(z);
        } else {
            DefaultLogger.w("PreviewFragment", "shouldn't call when not visible");
        }
        this.mExportable = z;
    }

    public void onExit(boolean z) {
        this.mIsOnExit = true;
        PreviewImageView previewImageView = this.mImageView;
        if (previewImageView != null) {
            previewImageView.setMaskShow(false);
            if (z) {
                return;
            }
            Bitmap onLoadPreview = this.mCallbacks.onLoadPreview();
            Bitmap onLoadPreviewOriginal = this.mCallbacks.onLoadPreviewOriginal();
            if (onLoadPreview == null || onLoadPreviewOriginal == null || onLoadPreview.getWidth() != onLoadPreviewOriginal.getWidth() || onLoadPreview.getHeight() != onLoadPreviewOriginal.getHeight()) {
                return;
            }
            this.mImageView.setImageBitmap(onLoadPreviewOriginal);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        PreviewImageView previewImageView = this.mImageView;
        if (previewImageView != null) {
            previewImageView.setImageBitmap(null);
        }
        ValueAnimator valueAnimator = this.mValueAnimator;
        if (valueAnimator != null && valueAnimator.isStarted()) {
            this.mValueAnimator.cancel();
        }
        this.mValueAnimator = null;
        MediaEditorInstaller.getInstance().removeInstallListener();
    }

    public boolean isLayoutPortrait() {
        return EditorOrientationHelper.isLayoutPortrait(getContext());
    }

    public void prepareShowEditFragment(final Effect effect, final OnPrepareEditFragmentListener onPrepareEditFragmentListener) {
        final int dimensionPixelSize;
        int i;
        Rect cropWindowPaddingRect = getCropWindowPaddingRect();
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(R.dimen.editor_menu_crop_preview_content_padding_start_end);
        final int dimensionPixelSize3 = getResources().getDimensionPixelSize(R.dimen.editor_menu_crop_preview_content_padding_top) + cropWindowPaddingRect.top;
        final int dimensionPixelSize4 = getResources().getDimensionPixelSize(R.dimen.editor_menu_crop_preview_content_padding_bottom) + cropWindowPaddingRect.bottom;
        if (isLayoutPortrait()) {
            i = SystemUiUtil.isWaterFallScreen() ? getResources().getDimensionPixelSize(R.dimen.editor_waterfall_screen_horizontal_protect_size) : 0;
            dimensionPixelSize = 0;
        } else {
            dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.photo_editor_landscape_vertical_protect_size);
            i = 0;
        }
        final int i2 = dimensionPixelSize2 - i;
        ValueAnimator ofInt = ValueAnimator.ofInt(0, 1);
        this.mValueAnimator = ofInt;
        ofInt.setDuration(this.mAnimatorDuration);
        this.mValueAnimator.setInterpolator(new CubicEaseOutInterpolator());
        final ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.mImageView.getLayoutParams();
        this.mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.photo.app.PreviewFragment$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                PreviewFragment.$r8$lambda$APHK1lqhQ2rkNRtX5pr72E9xN70(PreviewFragment.this, i2, dimensionPixelSize3, dimensionPixelSize4, layoutParams, dimensionPixelSize, valueAnimator);
            }
        });
        final boolean isShowMask = this.mImageView.isShowMask();
        this.mValueAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.editor.photo.app.PreviewFragment.5
            {
                PreviewFragment.this = this;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (PreviewFragment.this.mImageView != null) {
                    PreviewFragment.this.mImageView.setMaskShow(isShowMask);
                }
                if (PreviewFragment.this.mCallbacks != null) {
                    onPrepareEditFragmentListener.showEditFragment(effect);
                }
            }
        });
        PreviewImageView previewImageView = this.mImageView;
        if (previewImageView != null) {
            previewImageView.setMaskShow(false);
        }
        this.mValueAnimator.start();
        updateRemoveVisibility(false);
    }

    public /* synthetic */ void lambda$prepareShowEditFragment$1(int i, int i2, int i3, ConstraintLayout.LayoutParams layoutParams, int i4, ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        int i5 = (int) (i * animatedFraction);
        int i6 = (int) (i4 * (1.0f - animatedFraction));
        ((ViewGroup.MarginLayoutParams) layoutParams).topMargin = i6;
        ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = i6;
        this.mImageView.setPadding(i5, (int) (i2 * animatedFraction), i5, (int) (i3 * animatedFraction));
    }

    public void hideEditFragment() {
        final int dimensionPixelSize;
        Rect cropWindowPaddingRect = getCropWindowPaddingRect();
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(R.dimen.editor_menu_crop_preview_content_padding_start_end);
        final int dimensionPixelSize3 = getResources().getDimensionPixelSize(R.dimen.editor_menu_crop_preview_content_padding_top) + cropWindowPaddingRect.top;
        final int dimensionPixelSize4 = getResources().getDimensionPixelSize(R.dimen.editor_menu_crop_preview_content_padding_bottom) + cropWindowPaddingRect.bottom;
        int i = 0;
        if (isLayoutPortrait()) {
            dimensionPixelSize = 0;
            i = SystemUiUtil.isWaterFallScreen() ? getResources().getDimensionPixelSize(R.dimen.editor_waterfall_screen_horizontal_protect_size) : 0;
        } else {
            dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.photo_editor_landscape_vertical_protect_size);
        }
        final int i2 = dimensionPixelSize2 - i;
        final ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.mImageView.getLayoutParams();
        ValueAnimator ofInt = ValueAnimator.ofInt(1, 0);
        this.mValueAnimator = ofInt;
        ofInt.setDuration(this.mAnimatorDuration);
        this.mValueAnimator.setInterpolator(new CubicEaseOutInterpolator());
        this.mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.photo.app.PreviewFragment$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                PreviewFragment.$r8$lambda$YOIanA6Y1ymyK9AEC_wtuUY5v9w(PreviewFragment.this, i2, dimensionPixelSize3, dimensionPixelSize4, layoutParams, dimensionPixelSize, valueAnimator);
            }
        });
        this.mValueAnimator.start();
    }

    public /* synthetic */ void lambda$hideEditFragment$2(int i, int i2, int i3, ConstraintLayout.LayoutParams layoutParams, int i4, ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        float f = 1.0f - animatedFraction;
        int i5 = (int) (i * f);
        int i6 = (int) (i4 * animatedFraction);
        ((ViewGroup.MarginLayoutParams) layoutParams).topMargin = i6;
        ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = i6;
        this.mImageView.setPadding(i5, (int) (i2 * f), i5, (int) (i3 * f));
    }

    public final Rect getCropWindowPaddingRect() {
        Rect rect = new Rect();
        getResources().getDrawable(R.drawable.crop_window_new).getPadding(rect);
        return rect;
    }

    public void setRemoveWatermarkEnable(boolean z) {
        this.mRemoveWatermarkEnable = z;
        Button button = this.mRemoverBtn;
        if (button != null) {
            button.setVisibility(z ? 0 : 8);
        }
    }

    public final void updateRemoveVisibility(boolean z) {
        if (z) {
            if (!this.mRemoveWatermarkEnable) {
                return;
            }
            this.mRemoverBtn.setVisibility(0);
            return;
        }
        this.mRemoverBtn.setVisibility(4);
    }

    public void setLoadDone(boolean z) {
        this.mLoadDone = z;
    }
}
