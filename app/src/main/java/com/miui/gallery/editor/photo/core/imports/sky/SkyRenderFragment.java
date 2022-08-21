package com.miui.gallery.editor.photo.core.imports.sky;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.miui.gallery.R;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureListener;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.editor.photo.app.sky.res.SkyRandomRequest;
import com.miui.gallery.editor.photo.app.sky.sdk.DynamicSky;
import com.miui.gallery.editor.photo.core.Metadata;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment;
import com.miui.gallery.editor.photo.core.common.model.SkyData;
import com.miui.gallery.editor.photo.core.imports.sky.SkyEditorView;
import com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment;
import com.miui.gallery.editor.photo.core.imports.sky.SkyTextEditFragment;
import com.miui.gallery.editor.photo.utils.EditorMiscHelper;
import com.miui.gallery.editor.utils.EditorOrientationHelper;
import com.miui.gallery.net.NetApi;
import com.miui.gallery.net.hardwareauth.DeviceCredentialManager;
import com.miui.gallery.net.hardwareauth.HardwareAuthTokenManager;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.NetworkConsider;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.ProgressBarHandler;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes2.dex */
public class SkyRenderFragment extends AbstractEffectFragment implements SurfaceHolder.Callback {
    public LottieAnimationView mAudio;
    public boolean mCurrentIsScreenKeepOn;
    public SkyRenderData mCurrentRenderData;
    public long mDynamicRenderInTime;
    public DynamicSky mDynamicSky;
    public Future mFuture;
    public GLSurfaceView mGLSurfaceView;
    public SkyEditorView mImageView;
    public boolean mIsChanging;
    public boolean mIsSkyProcessing;
    public SkyRenderData mLastRenderData;
    public ConstraintLayout.LayoutParams mLayoutParams;
    public SkyRenderData mPendingStaticRenderData;
    public LottieAnimationView mProgressBar;
    public ProgressBarHandler mProgressBarHandler;
    public LottieAnimationView mRandom;
    public SkyData mSkyData;
    public Disposable mSkyRequestDispose;
    public SkyTextEditFragment mSkyTextEditFragment;
    public Bitmap mStaticSkyBitmap;
    public ImageView mText;
    public boolean mFirstDynamic = true;
    public Handler mHandler = new Handler();
    public ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new AnonymousClass1();
    public SkyRandomRequest.ISkyRandomRequestCallback mISkyRandomRequestCallback = new SkyRandomRequest.ISkyRandomRequestCallback() { // from class: com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment.2
        {
            SkyRenderFragment.this = this;
        }

        @Override // com.miui.gallery.editor.photo.app.sky.res.SkyRandomRequest.ISkyRandomRequestCallback
        public void returnRequestErrorCode(int i) {
            SkyRenderFragment.this.hideProgressBar();
            if (SkyRenderFragment.this.isAdded()) {
                if (i == -1) {
                    ToastUtils.makeText(SkyRenderFragment.this.getContext(), (int) R.string.filter_sky_download_failed_msg);
                    DefaultLogger.d("SkyRenderFragment", "download fail");
                } else {
                    ToastUtils.makeText(SkyRenderFragment.this.getContext(), String.format("%s%d", SkyRenderFragment.this.getResources().getString(R.string.filter_sky_random_download_failed), Integer.valueOf(i)));
                    DefaultLogger.d("SkyRenderFragment", "download fail cause of server");
                }
                SkyRenderFragment.this.clearAnimator();
                return;
            }
            DefaultLogger.d("SkyRenderFragment", "fragment exit");
        }

        @Override // com.miui.gallery.editor.photo.app.sky.res.SkyRandomRequest.ISkyRandomRequestCallback
        public void setSkyBgFromCloud(Bitmap bitmap, int i) {
            if (SkyRenderFragment.this.mIsChanging && SkyRenderFragment.this.isAdded()) {
                SkyRenderFragment.this.mSkyData.setFromCloud(true);
                if (!SkyRenderFragment.this.mIsSkyProcessing) {
                    SkyRenderFragment.this.mIsSkyProcessing = true;
                    SkyRenderFragment skyRenderFragment = SkyRenderFragment.this;
                    new StaticSkyTask(skyRenderFragment.mCurrentRenderData, bitmap).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                }
                DefaultLogger.d("SkyRenderFragment", "download success");
                return;
            }
            DefaultLogger.d("SkyRenderFragment", "fragment exit");
        }
    };
    public View.OnClickListener mRandomOnClickListener = new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment.3
        {
            SkyRenderFragment.this = this;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (SkyRenderFragment.this.mIsChanging || SkyRenderFragment.this.mIsSkyProcessing) {
                return;
            }
            HashMap hashMap = new HashMap(2);
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, SkyRenderFragment.this.mSkyData.getMaterialName());
            SkyRenderFragment.this.getHostAbility().sample("sky_random_click", hashMap);
            SkyRenderFragment.this.mIsChanging = true;
            SkyRenderFragment.this.downloadSkyResource();
        }
    };
    public SkyTextEditFragment.SkyTextEditorCallback mSkyTextEditorCallback = new SkyTextEditFragment.SkyTextEditorCallback() { // from class: com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment.6
        @Override // com.miui.gallery.editor.photo.core.imports.sky.SkyTextEditFragment.SkyTextEditorCallback
        public void onCancel() {
        }

        {
            SkyRenderFragment.this = this;
        }

        @Override // com.miui.gallery.editor.photo.core.imports.sky.SkyTextEditFragment.SkyTextEditorCallback
        public void onCaptionAdd(String str) {
            DefaultLogger.d("SkyRenderFragment", str);
            SkyRenderFragment.this.mDynamicSky.setSubTitleModel(0, str);
        }

        @Override // com.miui.gallery.editor.photo.core.imports.sky.SkyTextEditFragment.SkyTextEditorCallback
        public void onCaptionUpdate(String str) {
            SkyRenderFragment.this.mDynamicSky.setSubTitleModel(0, str);
        }

        @Override // com.miui.gallery.editor.photo.core.imports.sky.SkyTextEditFragment.SkyTextEditorCallback
        public void onClose() {
            SkyRenderFragment.this.mDynamicSky.resume();
            if (SkyRenderFragment.this.mCurrentRenderData != null) {
                SkyRenderFragment.this.showProgressBar();
                SkyRenderFragment.this.mDynamicSky.play(SkyRenderFragment.this.mCurrentRenderData.getMaterialId(), SkyRenderFragment.this.mCurrentRenderData.getPath(), SkyRenderFragment.this.mCurrentRenderData.getProgress());
            }
        }
    };
    public Runnable mRenderDynamicSkyTimeout = new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment.7
        {
            SkyRenderFragment.this = this;
        }

        @Override // java.lang.Runnable
        public void run() {
            SkyRenderFragment.this.onFinishProcessDynamicSky();
        }
    };
    public DynamicSky.PlayCallback mPlayCallback = new DynamicSky.PlayCallback() { // from class: com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment.8
        @Override // com.miui.gallery.editor.photo.app.sky.sdk.DynamicSky.PlayCallback
        public void onResume() {
        }

        {
            SkyRenderFragment.this = this;
        }

        @Override // com.miui.gallery.editor.photo.app.sky.sdk.DynamicSky.PlayCallback
        public void onPlay() {
            SkyRenderFragment.this.renderDynamicSkyTimeout();
        }

        @Override // com.miui.gallery.editor.photo.app.sky.sdk.DynamicSky.PlayCallback
        public void onAudioOn() {
            SkyRenderFragment.this.sampleAudioSwitch("on");
            SkyRenderFragment.this.setPlayAudio(true);
        }

        @Override // com.miui.gallery.editor.photo.app.sky.sdk.DynamicSky.PlayCallback
        public void onAudioOff() {
            SkyRenderFragment.this.sampleAudioSwitch("off");
            SkyRenderFragment.this.setPlayAudio(false);
        }

        @Override // com.miui.gallery.editor.photo.app.sky.sdk.DynamicSky.PlayCallback
        public void onPause() {
            SkyRenderFragment.this.onFinishProcessDynamicSky();
        }

        @Override // com.miui.gallery.editor.photo.app.sky.sdk.DynamicSky.PlayCallback
        public void onStop() {
            SkyRenderFragment.this.onFinishProcessDynamicSky();
        }
    };
    public View.OnClickListener mOnTextClickListener = new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment.9
        {
            SkyRenderFragment.this = this;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (SkyRenderFragment.this.mDynamicSky != null) {
                SkyRenderFragment.this.mDynamicSky.pause();
            }
            SkyRenderFragment.this.showSkyTextEditFragment();
        }
    };
    public View.OnClickListener mOnAudioClickListener = new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment.10
        {
            SkyRenderFragment.this = this;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SkyRenderFragment.this.mDynamicSky.toggleAudio();
        }
    };
    public SkyEditorView.OnLongTouchCallback mPreviewOnLongTouchCallback = new SkyEditorView.OnLongTouchCallback() { // from class: com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment.11
        {
            SkyRenderFragment.this = this;
        }

        @Override // com.miui.gallery.editor.photo.core.imports.sky.SkyEditorView.OnLongTouchCallback
        public void onLongTouchDown() {
            if (SkyRenderFragment.this.mCurrentRenderData == null || SkyRenderFragment.this.mCurrentRenderData.isNone()) {
                return;
            }
            SkyRenderFragment.this.mImageView.setImageBitmap(SkyRenderFragment.this.getBitmap());
        }

        @Override // com.miui.gallery.editor.photo.core.imports.sky.SkyEditorView.OnLongTouchCallback
        public void onLongTouchUp() {
            if (SkyRenderFragment.this.mCurrentRenderData == null || SkyRenderFragment.this.mCurrentRenderData.isNone() || SkyRenderFragment.this.mStaticSkyBitmap == null) {
                return;
            }
            SkyRenderFragment.this.mImageView.setImageBitmap(SkyRenderFragment.this.mStaticSkyBitmap);
        }
    };
    public DynamicSky.FrameCallback mFrameCallback = new DynamicSky.FrameCallback() { // from class: com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment.12
        {
            SkyRenderFragment.this = this;
        }

        @Override // com.miui.gallery.editor.photo.app.sky.sdk.DynamicSky.FrameCallback
        public void onReceiveFailed() {
            SkyRenderFragment.this.removeRenderDynamicSkyTimeout();
            SkyRenderFragment.this.onFinishProcessDynamicSky();
        }

        @Override // com.miui.gallery.editor.photo.app.sky.sdk.DynamicSky.FrameCallback
        public void onFrameCached() {
            DefaultLogger.d("DynamicSky", "dynamic render consume %d", Long.valueOf(System.currentTimeMillis() - SkyRenderFragment.this.mDynamicRenderInTime));
            SkyRenderFragment.this.mHandler.postDelayed(SkyRenderFragment.this.mChangeToDynamicUIRunnable, SkyRenderFragment.this.mFirstDynamic ? 200L : 0L);
            SkyRenderFragment.this.mFirstDynamic = false;
        }
    };
    public Runnable mChangeToDynamicUIRunnable = new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment.13
        {
            SkyRenderFragment.this = this;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (SkyRenderFragment.this.mCurrentRenderData != null && !SkyRenderFragment.this.mCurrentRenderData.isNone()) {
                DefaultLogger.d("DynamicSky", "change to dynamic ui");
                SkyRenderFragment.this.changeUi(true, false);
                SkyRenderFragment.this.operateScreenOn(true);
            } else {
                DefaultLogger.d("DynamicSky", "cant not change ui ,data is none");
            }
            SkyRenderFragment.this.removeRenderDynamicSkyTimeout();
            SkyRenderFragment.this.onFinishProcessDynamicSky();
            SkyRenderFragment.this.processPendingSkyData();
        }
    };

    public static /* synthetic */ void $r8$lambda$fVBQCfBXsprhkpgXf6Ny5q4bG1k(SkyRenderFragment skyRenderFragment, boolean z, boolean z2) {
        skyRenderFragment.lambda$downloadSkyResource$0(z, z2);
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public void clear() {
    }

    public final void enableComparison(boolean z) {
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment
    public void remove(Metadata metadata) {
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return new SkyRenderView(layoutInflater.getContext());
    }

    @Override // com.miui.gallery.app.fragment.AndroidFragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mGLSurfaceView.getViewTreeObserver().addOnGlobalLayoutListener(this.mOnGlobalLayoutListener);
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, com.miui.gallery.editor.utils.LayoutOrientationTracker.OnLayoutOrientationChangeListener
    public void onLayoutOrientationChange() {
        super.onLayoutOrientationChange();
        EditorOrientationHelper.copyLayoutParams(new SkyRenderView(getContext()), getView(), true);
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, androidx.fragment.app.Fragment
    @SuppressLint({"ClickableViewAccessibility"})
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        GLSurfaceView gLSurfaceView = (GLSurfaceView) view.findViewById(R.id.gl_surface_view);
        this.mGLSurfaceView = gLSurfaceView;
        gLSurfaceView.setZOrderOnTop(false);
        this.mImageView = (SkyEditorView) view.findViewById(R.id.image_view);
        this.mProgressBar = (LottieAnimationView) view.findViewById(R.id.progress);
        this.mAudio = (LottieAnimationView) view.findViewById(R.id.audio);
        FolmeUtil.setCustomTouchAnim(this.mAudio, new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(0.95f).build(), null, null, null, true);
        this.mAudio.setContentDescription(getResources().getString(R.string.photo_editor_talkback_sky_audio_close));
        LottieAnimationView lottieAnimationView = (LottieAnimationView) view.findViewById(R.id.random);
        this.mRandom = lottieAnimationView;
        lottieAnimationView.setOnClickListener(this.mRandomOnClickListener);
        AnimParams build = new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(0.95f).build();
        FolmeUtil.setCustomTouchAnim(this.mRandom, build, null, null, null, true);
        ImageView imageView = (ImageView) view.findViewById(R.id.text);
        this.mText = imageView;
        imageView.setOnClickListener(this.mOnTextClickListener);
        FolmeUtil.setCustomTouchAnim(this.mText, build, null, null, null, true);
        this.mText.setContentDescription(getResources().getString(R.string.photo_editor_talkback_sky_add_text));
        this.mProgressBarHandler = new ProgressBarHandler(this.mProgressBar);
        this.mAudio.setOnClickListener(this.mOnAudioClickListener);
        this.mImageView.setOnLongTouchCallback(this.mPreviewOnLongTouchCallback);
        this.mImageView.setImageBitmap(getBitmap());
        this.mGLSurfaceView.getViewTreeObserver().addOnGlobalLayoutListener(this.mOnGlobalLayoutListener);
        this.mGLSurfaceView.getHolder().addCallback(this);
        DynamicSky dynamicSky = DynamicSky.INSTANCE;
        dynamicSky.setup();
        this.mDynamicSky = dynamicSky;
        dynamicSky.setGLSurfaceView(this.mGLSurfaceView);
        this.mDynamicSky.setFrameCallback(this.mFrameCallback);
        this.mDynamicSky.setPlayCallback(this.mPlayCallback);
    }

    /* renamed from: com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements ViewTreeObserver.OnGlobalLayoutListener {
        public static /* synthetic */ void $r8$lambda$Wt3L0reQspYRAK3wLs5OPtAOE4I(AnonymousClass1 anonymousClass1) {
            anonymousClass1.lambda$onGlobalLayout$0();
        }

        public AnonymousClass1() {
            SkyRenderFragment.this = r1;
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            if (SkyRenderFragment.this.mGLSurfaceView.getViewTreeObserver().isAlive()) {
                SkyRenderFragment.this.mGLSurfaceView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
            View view = (View) SkyRenderFragment.this.mGLSurfaceView.getParent();
            SkyRenderFragment skyRenderFragment = SkyRenderFragment.this;
            skyRenderFragment.mLayoutParams = (ConstraintLayout.LayoutParams) skyRenderFragment.mGLSurfaceView.getLayoutParams();
            if (view != null) {
                if (((((view.getWidth() - view.getPaddingLeft()) - view.getPaddingRight()) - ((ViewGroup.MarginLayoutParams) SkyRenderFragment.this.mLayoutParams).leftMargin) - ((ViewGroup.MarginLayoutParams) SkyRenderFragment.this.mLayoutParams).rightMargin) / ((((view.getHeight() - view.getPaddingTop()) - view.getPaddingBottom()) - ((ViewGroup.MarginLayoutParams) SkyRenderFragment.this.mLayoutParams).topMargin) - ((ViewGroup.MarginLayoutParams) SkyRenderFragment.this.mLayoutParams).bottomMargin) > SkyRenderFragment.this.getBitmap().getWidth() / SkyRenderFragment.this.getBitmap().getHeight()) {
                    ConstraintLayout.LayoutParams layoutParams = SkyRenderFragment.this.mLayoutParams;
                    layoutParams.dimensionRatio = "W," + SkyRenderFragment.this.getBitmap().getWidth() + ":" + SkyRenderFragment.this.getBitmap().getHeight();
                } else {
                    ConstraintLayout.LayoutParams layoutParams2 = SkyRenderFragment.this.mLayoutParams;
                    layoutParams2.dimensionRatio = "H," + SkyRenderFragment.this.getBitmap().getWidth() + ":" + SkyRenderFragment.this.getBitmap().getHeight();
                }
                SkyRenderFragment.this.mGLSurfaceView.setLayoutParams(SkyRenderFragment.this.mLayoutParams);
                SkyRenderFragment.this.mGLSurfaceView.post(new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        SkyRenderFragment.AnonymousClass1.$r8$lambda$Wt3L0reQspYRAK3wLs5OPtAOE4I(SkyRenderFragment.AnonymousClass1.this);
                    }
                });
            }
        }

        public /* synthetic */ void lambda$onGlobalLayout$0() {
            if (SkyRenderFragment.this.mAudio == null || SkyRenderFragment.this.mText == null || SkyRenderFragment.this.mRandom == null) {
                return;
            }
            SkyRenderFragment skyRenderFragment = SkyRenderFragment.this;
            skyRenderFragment.configSkyBtnRelativePosition(skyRenderFragment.mGLSurfaceView.getWidth());
        }
    }

    public final void configSkyBtnRelativePosition(int i) {
        if (i <= getResources().getDimensionPixelSize(R.dimen.px_270)) {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.mAudio.getLayoutParams();
            this.mLayoutParams = layoutParams;
            layoutParams.startToStart = R.id.sky_render_view;
            layoutParams.bottomToBottom = R.id.sky_render_view;
            layoutParams.setMarginStart(getResources().getDimensionPixelSize(R.dimen.px_80));
            ((ViewGroup.MarginLayoutParams) this.mLayoutParams).bottomMargin = getResources().getDimensionPixelSize(R.dimen.px_70);
            this.mAudio.setLayoutParams(this.mLayoutParams);
            ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) this.mText.getLayoutParams();
            this.mLayoutParams = layoutParams2;
            layoutParams2.endToEnd = R.id.sky_render_view;
            layoutParams2.bottomToBottom = R.id.sky_render_view;
            layoutParams2.setMarginEnd(getResources().getDimensionPixelSize(R.dimen.px_80));
            ((ViewGroup.MarginLayoutParams) this.mLayoutParams).bottomMargin = getResources().getDimensionPixelSize(R.dimen.px_70);
            this.mText.setLayoutParams(this.mLayoutParams);
            ConstraintLayout.LayoutParams layoutParams3 = (ConstraintLayout.LayoutParams) this.mRandom.getLayoutParams();
            this.mLayoutParams = layoutParams3;
            layoutParams3.endToEnd = R.id.sky_render_view;
            layoutParams3.bottomToBottom = R.id.sky_render_view;
            layoutParams3.setMarginEnd(getResources().getDimensionPixelSize(R.dimen.px_80));
            ((ViewGroup.MarginLayoutParams) this.mLayoutParams).bottomMargin = getResources().getDimensionPixelSize(R.dimen.px_70);
            this.mRandom.setLayoutParams(this.mLayoutParams);
            return;
        }
        ConstraintLayout.LayoutParams layoutParams4 = (ConstraintLayout.LayoutParams) this.mAudio.getLayoutParams();
        this.mLayoutParams = layoutParams4;
        layoutParams4.startToStart = R.id.gl_surface_view;
        layoutParams4.bottomToBottom = R.id.gl_surface_view;
        layoutParams4.setMarginStart(getResources().getDimensionPixelSize(R.dimen.sky_audio_icon_margin));
        ((ViewGroup.MarginLayoutParams) this.mLayoutParams).bottomMargin = getResources().getDimensionPixelSize(R.dimen.sky_audio_icon_margin);
        this.mAudio.setLayoutParams(this.mLayoutParams);
        ConstraintLayout.LayoutParams layoutParams5 = (ConstraintLayout.LayoutParams) this.mText.getLayoutParams();
        this.mLayoutParams = layoutParams5;
        layoutParams5.endToEnd = R.id.gl_surface_view;
        layoutParams5.bottomToBottom = R.id.gl_surface_view;
        layoutParams5.setMarginEnd(getResources().getDimensionPixelSize(R.dimen.sky_audio_icon_margin));
        ((ViewGroup.MarginLayoutParams) this.mLayoutParams).bottomMargin = getResources().getDimensionPixelSize(R.dimen.sky_audio_icon_margin);
        this.mText.setLayoutParams(this.mLayoutParams);
        ConstraintLayout.LayoutParams layoutParams6 = (ConstraintLayout.LayoutParams) this.mRandom.getLayoutParams();
        this.mLayoutParams = layoutParams6;
        layoutParams6.endToEnd = R.id.gl_surface_view;
        layoutParams6.bottomToBottom = R.id.gl_surface_view;
        layoutParams6.setMarginEnd(getResources().getDimensionPixelSize(R.dimen.sky_audio_icon_margin));
        ((ViewGroup.MarginLayoutParams) this.mLayoutParams).bottomMargin = getResources().getDimensionPixelSize(R.dimen.sky_audio_icon_margin);
        this.mRandom.setLayoutParams(this.mLayoutParams);
    }

    public final void clearAnimator() {
        this.mIsChanging = false;
        this.mIsSkyProcessing = false;
    }

    public final void downloadSkyResource() {
        this.mRandom.playAnimation();
        if (!BaseNetworkUtils.isNetworkConnected() || !BaseGalleryPreferences.CTA.canConnectNetwork()) {
            ToastUtils.makeText(getContext(), (int) R.string.filter_sky_download_failed_msg);
            DefaultLogger.d("SkyRenderFragment", "download resource no network");
            clearAnimator();
        } else if (BaseNetworkUtils.isActiveNetworkMetered()) {
            NetworkConsider.consider(getContext(), new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
                public final void onConfirmed(boolean z, boolean z2) {
                    SkyRenderFragment.$r8$lambda$fVBQCfBXsprhkpgXf6Ny5q4bG1k(SkyRenderFragment.this, z, z2);
                }
            });
        } else {
            loadSkyResource();
        }
    }

    public /* synthetic */ void lambda$downloadSkyResource$0(boolean z, boolean z2) {
        if (z) {
            loadSkyResource();
        } else {
            clearAnimator();
        }
    }

    public final void loadSkyResource() {
        Future future = this.mFuture;
        if (future != null) {
            future.cancel();
            this.mFuture = null;
        }
        showProgressBar();
        this.mFuture = ThreadManager.getMiscPool().submit(new ThreadPool.Job<String>() { // from class: com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment.4
            {
                SkyRenderFragment.this = this;
            }

            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public String mo1807run(ThreadPool.JobContext jobContext) {
                return HardwareAuthTokenManager.getAuthTokenSync(SkyRenderFragment.this.getContext(), true, MiscUtil.getAppVersionCode(), "sky");
            }
        }, new FutureListener<String>() { // from class: com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment.5
            {
                SkyRenderFragment.this = this;
            }

            @Override // com.miui.gallery.concurrent.FutureListener
            public void onFutureDone(Future<String> future2) {
                Observable create = NetApi.create(new SkyRandomRequest(1, SkyRenderFragment.this.mCurrentRenderData.getMaterialId(), "https://api.open.ai.xiaomi.com/vision/image-gen/v1/sky", future2.get(), SkyRenderFragment.this.mISkyRandomRequestCallback));
                SkyRenderFragment.this.mSkyRequestDispose = create.subscribe();
            }
        });
    }

    public final void showSkyTextEditFragment() {
        if (this.mSkyTextEditFragment == null) {
            SkyTextEditFragment newInstance = SkyTextEditFragment.newInstance();
            this.mSkyTextEditFragment = newInstance;
            newInstance.setCaptionEditorCallback(this.mSkyTextEditorCallback);
        }
        this.mSkyTextEditFragment.setContent(this.mDynamicSky.getSubTitle());
        this.mSkyTextEditFragment.showAllowingStateLoss(getActivity().getSupportFragmentManager(), "SkyTextEditFragment");
    }

    public final void renderDynamicSkyTimeout() {
        this.mHandler.postDelayed(this.mRenderDynamicSkyTimeout, 1200L);
    }

    public final void removeRenderDynamicSkyTimeout() {
        this.mHandler.removeCallbacks(this.mRenderDynamicSkyTimeout);
    }

    public final void onFinishProcessDynamicSky() {
        this.mIsSkyProcessing = false;
        hideProgressBar();
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        hideCompareButton();
        this.mHandler.removeCallbacks(this.mChangeToDynamicUIRunnable);
        DynamicSky dynamicSky = this.mDynamicSky;
        if (dynamicSky != null) {
            dynamicSky.release();
        }
        Future future = this.mFuture;
        if (future != null) {
            future.cancel();
            this.mFuture = null;
        }
        Disposable disposable = this.mSkyRequestDispose;
        if (disposable == null || disposable.isDisposed()) {
            return;
        }
        this.mSkyRequestDispose.dispose();
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public void enterImmersive() {
        super.enterImmersive();
        EditorMiscHelper.enterImmersive(getTitleView(), this.mAudio, this.mText);
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public void exitImmersive() {
        super.exitImmersive();
        EditorMiscHelper.exitImmersive(getTitleView(), this.mAudio, this.mText);
    }

    public void setPlayAudio(boolean z) {
        String string;
        this.mAudio.setAnimation(z ? R.raw.sky_dynamic_audio_on : R.raw.sky_dynamic_audio_off);
        LottieAnimationView lottieAnimationView = this.mAudio;
        if (z) {
            string = getResources().getString(R.string.photo_editor_talkback_sky_audio_close);
        } else {
            string = getResources().getString(R.string.photo_editor_talkback_sky_audio_open);
        }
        lottieAnimationView.setContentDescription(string);
        this.mAudio.setProgress(1.0f);
        this.mAudio.playAnimation();
    }

    public final void sampleAudioSwitch(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, str);
        SamplingStatHelper.recordCountEvent("photo_editor", "sky_audio_switch", hashMap);
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        if (isDynamic()) {
            this.mDynamicSky.resume();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        DynamicSky dynamicSky;
        super.onPause();
        if (!isDynamic() || (dynamicSky = this.mDynamicSky) == null) {
            return;
        }
        dynamicSky.pause();
    }

    public final boolean isDynamic() {
        SkyRenderData skyRenderData = this.mCurrentRenderData;
        return skyRenderData != null && skyRenderData.isDynamic();
    }

    public final void setShowTextYanhua(boolean z) {
        if (z) {
            this.mText.setVisibility(0);
        } else {
            this.mText.setVisibility(8);
        }
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment
    public void add(Metadata metadata, Object obj) {
        if (!(metadata instanceof SkyData)) {
            return;
        }
        clear();
        this.mSkyData = (SkyData) metadata;
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment
    public void render() {
        if (this.mSkyData == null) {
            return;
        }
        doRender(new SkyRenderData(this.mSkyData));
    }

    public final void doRender(SkyRenderData skyRenderData) {
        boolean z = false;
        if (skyRenderData.isNone()) {
            this.mLastRenderData = this.mCurrentRenderData;
            this.mCurrentRenderData = skyRenderData;
            this.mImageView.setVisibility(0);
            this.mImageView.setImageBitmap(getBitmap());
            this.mDynamicSky.pause();
            this.mIsSkyProcessing = false;
            hideProgressBar();
            enableComparison(false);
            DefaultLogger.d("SkyRenderFragment", "render none");
        } else if (this.mIsSkyProcessing) {
            this.mPendingStaticRenderData = skyRenderData;
            DefaultLogger.d("SkyRenderFragment", "pending render data");
        } else if (skyRenderData.isDynamic()) {
            this.mLastRenderData = this.mCurrentRenderData;
            this.mCurrentRenderData = new SkyRenderData(this.mSkyData);
            SkyRenderData skyRenderData2 = this.mLastRenderData;
            if (skyRenderData2 != null && !skyRenderData2.isDynamic()) {
                this.mDynamicSky.audioOn();
                setPlayAudio(true);
            }
            enableComparison(false);
            renderDynamicSky(this.mCurrentRenderData);
            SkyRenderData skyRenderData3 = this.mCurrentRenderData;
            if (skyRenderData3 != null && skyRenderData3.isDynamicTextYanhua()) {
                z = true;
            }
            setShowTextYanhua(z);
        } else {
            this.mIsSkyProcessing = true;
            this.mLastRenderData = this.mCurrentRenderData;
            this.mCurrentRenderData = skyRenderData;
            new StaticSkyTask(this.mCurrentRenderData, null).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
            DefaultLogger.d("SkyRenderFragment", "render static sky");
        }
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        DefaultLogger.d("DynamicSky", "surfaceCreated " + surfaceHolder.getSurface().hashCode());
        DynamicSky dynamicSky = this.mDynamicSky;
        if (dynamicSky != null) {
            dynamicSky.surfaceCreated();
        }
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        DefaultLogger.d("SkyRenderFragment", "surfaceChanged, width %d x height %d", Integer.valueOf(i2), Integer.valueOf(i3));
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        DefaultLogger.d("DynamicSky", "surfaceDestroyed");
    }

    public void notifySegmentFinish(int i) {
        DynamicSky dynamicSky = this.mDynamicSky;
        if (dynamicSky != null) {
            dynamicSky.setBitmap(getBitmap());
            this.mDynamicSky.setSegmentResult(i);
        }
    }

    public void showProgressBar() {
        ProgressBarHandler progressBarHandler = this.mProgressBarHandler;
        if (progressBarHandler != null) {
            progressBarHandler.showDelay(1000);
            DefaultLogger.d("SkyRenderFragment", "show progress bar");
        }
    }

    public void hideProgressBar() {
        ProgressBarHandler progressBarHandler = this.mProgressBarHandler;
        if (progressBarHandler != null) {
            progressBarHandler.hide();
            DefaultLogger.d("SkyRenderFragment", "hide progress bar");
        }
    }

    /* loaded from: classes2.dex */
    public class StaticSkyTask extends AsyncTask<Void, Void, Bitmap> {
        public Bitmap mResBitmap;
        public SkyRenderData mSkyRenderData;

        public StaticSkyTask(SkyRenderData skyRenderData, Bitmap bitmap) {
            SkyRenderFragment.this = r1;
            this.mSkyRenderData = skyRenderData;
            this.mResBitmap = bitmap;
        }

        @Override // android.os.AsyncTask
        public void onPreExecute() {
            super.onPreExecute();
            SkyRenderFragment.this.showProgressBar();
        }

        /* JADX WARN: Removed duplicated region for block: B:109:0x00f3  */
        /* JADX WARN: Removed duplicated region for block: B:116:0x007e A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:121:? A[RETURN, SYNTHETIC] */
        @Override // android.os.AsyncTask
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public android.graphics.Bitmap doInBackground(java.lang.Void... r9) {
            /*
                r8 = this;
                com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment r9 = com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment.this
                android.graphics.Bitmap r9 = r9.getBitmap()
                r0 = 0
                if (r9 != 0) goto La
                return r0
            La:
                long r1 = java.lang.System.currentTimeMillis()
                int r3 = r9.getWidth()
                int r4 = r9.getHeight()
                android.graphics.Bitmap$Config r5 = android.graphics.Bitmap.Config.ARGB_8888
                android.graphics.Bitmap r3 = android.graphics.Bitmap.createBitmap(r3, r4, r5)
                android.graphics.Bitmap r4 = r8.mResBitmap
                if (r4 == 0) goto L87
                java.io.File r4 = new java.io.File     // Catch: java.lang.Throwable -> L68 java.io.IOException -> L6a
                java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L68 java.io.IOException -> L6a
                r5.<init>()     // Catch: java.lang.Throwable -> L68 java.io.IOException -> L6a
                com.miui.gallery.editor.photo.core.imports.sky.SkyRenderData r6 = r8.mSkyRenderData     // Catch: java.lang.Throwable -> L68 java.io.IOException -> L6a
                java.lang.String r6 = r6.getPath()     // Catch: java.lang.Throwable -> L68 java.io.IOException -> L6a
                r5.append(r6)     // Catch: java.lang.Throwable -> L68 java.io.IOException -> L6a
                java.lang.String r6 = java.io.File.separator     // Catch: java.lang.Throwable -> L68 java.io.IOException -> L6a
                r5.append(r6)     // Catch: java.lang.Throwable -> L68 java.io.IOException -> L6a
                java.lang.String r6 = "background_cloud"
                r5.append(r6)     // Catch: java.lang.Throwable -> L68 java.io.IOException -> L6a
                java.lang.String r5 = r5.toString()     // Catch: java.lang.Throwable -> L68 java.io.IOException -> L6a
                r4.<init>(r5)     // Catch: java.lang.Throwable -> L68 java.io.IOException -> L6a
                boolean r5 = r4.exists()     // Catch: java.lang.Throwable -> L68 java.io.IOException -> L6a
                if (r5 != 0) goto L51
                java.io.File r5 = r4.getParentFile()     // Catch: java.lang.Throwable -> L68 java.io.IOException -> L6a
                r5.mkdirs()     // Catch: java.lang.Throwable -> L68 java.io.IOException -> L6a
                r4.createNewFile()     // Catch: java.lang.Throwable -> L68 java.io.IOException -> L6a
            L51:
                java.io.FileOutputStream r5 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> L68 java.io.IOException -> L6a
                r5.<init>(r4)     // Catch: java.lang.Throwable -> L68 java.io.IOException -> L6a
                android.graphics.Bitmap r4 = r8.mResBitmap     // Catch: java.io.IOException -> L66 java.lang.Throwable -> L7a
                android.graphics.Bitmap$CompressFormat r6 = android.graphics.Bitmap.CompressFormat.WEBP     // Catch: java.io.IOException -> L66 java.lang.Throwable -> L7a
                r7 = 100
                r4.compress(r6, r7, r5)     // Catch: java.io.IOException -> L66 java.lang.Throwable -> L7a
                r5.flush()     // Catch: java.io.IOException -> L66 java.lang.Throwable -> L7a
                r5.close()     // Catch: java.io.IOException -> L75
                goto L87
            L66:
                r4 = move-exception
                goto L6c
            L68:
                r9 = move-exception
                goto L7c
            L6a:
                r4 = move-exception
                r5 = r0
            L6c:
                r4.printStackTrace()     // Catch: java.lang.Throwable -> L7a
                if (r5 == 0) goto L87
                r5.close()     // Catch: java.io.IOException -> L75
                goto L87
            L75:
                r4 = move-exception
                r4.printStackTrace()
                goto L87
            L7a:
                r9 = move-exception
                r0 = r5
            L7c:
                if (r0 == 0) goto L86
                r0.close()     // Catch: java.io.IOException -> L82
                goto L86
            L82:
                r0 = move-exception
                r0.printStackTrace()
            L86:
                throw r9
            L87:
                com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment r4 = com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment.this
                com.miui.gallery.editor.photo.core.imports.sky.SkyRenderData r4 = com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment.access$2200(r4)
                if (r4 == 0) goto Lcc
                com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment r4 = com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment.this
                com.miui.gallery.editor.photo.core.imports.sky.SkyRenderData r4 = com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment.access$2200(r4)
                boolean r4 = r4.isNone()
                if (r4 != 0) goto Lcc
                com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment r4 = com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment.this
                com.miui.gallery.editor.photo.core.imports.sky.SkyRenderData r4 = com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment.access$2200(r4)
                boolean r4 = r4.isDynamic()
                if (r4 != 0) goto Lcc
                com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment r4 = com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment.this
                com.miui.gallery.editor.photo.core.imports.sky.SkyRenderData r4 = com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment.access$2200(r4)
                int r4 = r4.getMaterialId()
                com.miui.gallery.editor.photo.core.imports.sky.SkyRenderData r5 = r8.mSkyRenderData
                int r5 = r5.getMaterialId()
                if (r4 != r5) goto Lcc
                com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment r4 = com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment.this
                boolean r4 = com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment.access$700(r4)
                if (r4 != 0) goto Lcc
                com.xiaomi.skytransfer.SkyTranFilter r4 = com.xiaomi.skytransfer.SkyTranFilter.getInstance()
                com.miui.gallery.editor.photo.core.imports.sky.SkyRenderData r5 = r8.mSkyRenderData
                boolean r9 = r4.transferAdjustMomentSeq(r9, r3, r5)
                goto Ld6
            Lcc:
                com.xiaomi.skytransfer.SkyTranFilter r4 = com.xiaomi.skytransfer.SkyTranFilter.getInstance()
                com.miui.gallery.editor.photo.core.imports.sky.SkyRenderData r5 = r8.mSkyRenderData
                boolean r9 = r4.transferSkyForShow(r9, r3, r5)
            Ld6:
                long r4 = java.lang.System.currentTimeMillis()
                long r4 = r4 - r1
                java.lang.Long r1 = java.lang.Long.valueOf(r4)
                java.lang.String r2 = "SkyRenderFragment"
                java.lang.String r4 = "sky render end, use time: %d"
                com.miui.gallery.util.logger.DefaultLogger.d(r2, r4, r1)
                com.miui.gallery.editor.photo.core.imports.sky.SkyRenderData r1 = r8.mSkyRenderData
                java.lang.String r1 = r1.toString()
                java.lang.String r4 = "StaticSkyTask:%s"
                com.miui.gallery.util.logger.DefaultLogger.d(r2, r4, r1)
                if (r9 == 0) goto Lf4
                r0 = r3
            Lf4:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.editor.photo.core.imports.sky.SkyRenderFragment.StaticSkyTask.doInBackground(java.lang.Void[]):android.graphics.Bitmap");
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap bitmap) {
            if (!SkyRenderFragment.this.isAdded()) {
                return;
            }
            SkyRenderFragment.this.hideProgressBar();
            SkyRenderFragment.this.mIsSkyProcessing = false;
            if (SkyRenderFragment.this.mCurrentRenderData != null && !SkyRenderFragment.this.mCurrentRenderData.isNone() && bitmap != null) {
                SkyRenderFragment.this.mStaticSkyBitmap = bitmap;
                SkyRenderFragment.this.enableComparison(true);
                SkyRenderFragment.this.mImageView.setImageBitmap(bitmap);
                SkyRenderFragment.this.changeUi(false, SkyRenderFragment.this.mCurrentRenderData.supportRandom());
                if (SkyRenderFragment.this.mLastRenderData != null && SkyRenderFragment.this.mLastRenderData.isDynamic()) {
                    SkyRenderFragment.this.mDynamicSky.stop();
                    SkyRenderFragment.this.mIsSkyProcessing = false;
                }
            }
            SkyRenderFragment.this.operateScreenOn(false);
            SkyRenderFragment.this.processPendingSkyData();
            SkyRenderFragment.this.clearAnimator();
        }
    }

    public void changeUi(boolean z, boolean z2) {
        int i = 8;
        if (z) {
            this.mImageView.setVisibility(8);
            this.mGLSurfaceView.setVisibility(0);
            this.mAudio.setVisibility(0);
        } else {
            this.mImageView.setVisibility(0);
            this.mAudio.setVisibility(8);
        }
        if (!DeviceCredentialManager.getSupportCloudCredential(getContext()) || BaseBuildUtil.isInternational()) {
            return;
        }
        LottieAnimationView lottieAnimationView = this.mRandom;
        if (z2) {
            i = 0;
        }
        lottieAnimationView.setVisibility(i);
    }

    public final void renderDynamicSky(SkyRenderData skyRenderData) {
        DefaultLogger.d("SkyRenderFragment", "render dynamic");
        SkyRenderData skyRenderData2 = this.mLastRenderData;
        if (skyRenderData2 != null && skyRenderData2.getMaterialName() != null && this.mLastRenderData.getMaterialName().equals(skyRenderData.getMaterialName())) {
            this.mDynamicSky.applyThreshold(skyRenderData.getProgress());
            return;
        }
        this.mIsSkyProcessing = true;
        this.mDynamicRenderInTime = System.currentTimeMillis();
        showProgressBar();
        this.mDynamicSky.play(skyRenderData.getMaterialId(), skyRenderData.getPath(), skyRenderData.getProgress());
    }

    public final void processPendingSkyData() {
        SkyRenderData skyRenderData = this.mPendingStaticRenderData;
        if (skyRenderData == null || skyRenderData.isNone()) {
            return;
        }
        doRender(this.mPendingStaticRenderData);
        this.mPendingStaticRenderData = null;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public boolean isEmpty() {
        SkyData skyData = this.mSkyData;
        return skyData == null || skyData.isNone();
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public List<String> onSample() {
        if (this.mSkyData == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.mSkyData.name);
        return arrayList;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public RenderData onExport() {
        return new SkyRenderData(this.mSkyData);
    }

    public final void operateScreenOn(boolean z) {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        if (z) {
            if (this.mCurrentIsScreenKeepOn) {
                return;
            }
            DefaultLogger.d("SkyRenderFragment", "open screenKeepOn...");
            activity.getWindow().addFlags(128);
            this.mCurrentIsScreenKeepOn = true;
        } else if (!this.mCurrentIsScreenKeepOn) {
        } else {
            DefaultLogger.d("SkyRenderFragment", "close screenKeepOn...");
            activity.getWindow().clearFlags(128);
            this.mCurrentIsScreenKeepOn = false;
        }
    }
}
