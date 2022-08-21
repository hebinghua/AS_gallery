package com.miui.gallery.editor.photo.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.airbnb.lottie.LottieAnimationView;
import com.android.internal.WindowCompat;
import com.miui.filtersdk.BeautificationSDK;
import com.miui.gallery.R;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.agreement.core.OnAgreementInvokedListener;
import com.miui.gallery.app.activity.AndroidActivity;
import com.miui.gallery.compat.app.ActivityCompat;
import com.miui.gallery.compat.transition.TransitionCompat;
import com.miui.gallery.editor.photo.app.AbstractNaviFragment;
import com.miui.gallery.editor.photo.app.AlertDialogFragment;
import com.miui.gallery.editor.photo.app.DraftManager;
import com.miui.gallery.editor.photo.app.ExportFragment;
import com.miui.gallery.editor.photo.app.InitializeController;
import com.miui.gallery.editor.photo.app.MenuFragment;
import com.miui.gallery.editor.photo.app.PhotoEditor;
import com.miui.gallery.editor.photo.app.PreviewFragment;
import com.miui.gallery.editor.photo.app.VideoExportFragment;
import com.miui.gallery.editor.photo.app.VideoExportManager;
import com.miui.gallery.editor.photo.app.adjust.AdjustMenuFragment;
import com.miui.gallery.editor.photo.app.adjust2.Adjust2MenuFragment;
import com.miui.gallery.editor.photo.app.beautify.BeautifyFragment;
import com.miui.gallery.editor.photo.app.crop.CropMenuFragment;
import com.miui.gallery.editor.photo.app.doodle.DoodleMenuFragment;
import com.miui.gallery.editor.photo.app.filter.Filter;
import com.miui.gallery.editor.photo.app.filter.FilterFragment;
import com.miui.gallery.editor.photo.app.frame.FrameMenuFragment;
import com.miui.gallery.editor.photo.app.longcrop.LongCropFragment;
import com.miui.gallery.editor.photo.app.mosaic.MosaicMenuFragment;
import com.miui.gallery.editor.photo.app.navigator.PhotoNaviFragment;
import com.miui.gallery.editor.photo.app.navigator.ScreenshotNaviFragment;
import com.miui.gallery.editor.photo.app.remover.Inpaint;
import com.miui.gallery.editor.photo.app.remover.RemoverMenuFragment;
import com.miui.gallery.editor.photo.app.remover2.Remover2MenuFragment;
import com.miui.gallery.editor.photo.app.remover2.sdk.Remover2CheckHelper;
import com.miui.gallery.editor.photo.app.sky.SkyFragment;
import com.miui.gallery.editor.photo.app.sky.sdk.SkyCheckHelper;
import com.miui.gallery.editor.photo.app.sticker.StickerMenuFragment;
import com.miui.gallery.editor.photo.app.text.TextMenuFragment;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.RenderFragment;
import com.miui.gallery.editor.photo.core.SdkManager;
import com.miui.gallery.editor.photo.core.SdkProvider;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractCropFragment;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractDoodleFragment;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractFrameFragment;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractLongCropFragment;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractMosaicFragment;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractRemover2Fragment;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractRemoverFragment;
import com.miui.gallery.editor.photo.core.common.model.Adjust2Data;
import com.miui.gallery.editor.photo.core.common.model.AdjustData;
import com.miui.gallery.editor.photo.core.common.model.BeautifyData;
import com.miui.gallery.editor.photo.core.common.model.CropData;
import com.miui.gallery.editor.photo.core.common.model.DoodleData;
import com.miui.gallery.editor.photo.core.common.model.FilterCategory;
import com.miui.gallery.editor.photo.core.common.model.FrameData;
import com.miui.gallery.editor.photo.core.common.model.MosaicData;
import com.miui.gallery.editor.photo.core.common.model.Remover2Data;
import com.miui.gallery.editor.photo.core.common.model.RemoverData;
import com.miui.gallery.editor.photo.core.common.model.SkyCategory;
import com.miui.gallery.editor.photo.core.common.provider.AbstractStickerProvider;
import com.miui.gallery.editor.photo.core.common.provider.AbstractTextProvider;
import com.miui.gallery.editor.photo.core.imports.adjust2.Adjust2DataImpl;
import com.miui.gallery.editor.photo.core.imports.adjust2.Adjust2RenderData;
import com.miui.gallery.editor.photo.origin.EditorOriginHandler;
import com.miui.gallery.editor.photo.utils.Callback;
import com.miui.gallery.editor.photo.utils.EditorMiscHelper;
import com.miui.gallery.editor.ui.view.EditorToast;
import com.miui.gallery.editor.utils.EditorOrientationHelper;
import com.miui.gallery.editor.utils.LayoutOrientationTracker;
import com.miui.gallery.permission.core.Permission;
import com.miui.gallery.permission.core.PermissionCheckCallback;
import com.miui.gallery.permission.core.PermissionInjector;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryOpenProvider;
import com.miui.gallery.sdk.editor.Constants;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.BuildUtil;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.OrientationCheckHelper;
import com.miui.gallery.util.ProgressBarHandler;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.SystemUiUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.photoview.ScreenSceneClassificationUtil;
import com.miui.gallery.view.BrightnessManager;
import com.miui.mediaeditor.utils.FilePermissionUtils;
import com.miui.privacy.LockSettingsHelper;
import com.xiaomi.mirror.synergy.CallMethod;
import com.xiaomi.skytransfer.SkyTranFilter;
import com.xiaomi.stat.MiStat;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import miuix.view.animation.CubicEaseInOutInterpolator;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes2.dex */
public class PhotoEditor extends AndroidActivity implements PermissionCheckCallback, HostAbility {
    public ArrayList<Integer> mActivatedEffects;
    public Intent mActivityIntent;
    public BrightnessManager mBrightnessManager;
    public long mCreateTime;
    public int mCurrentClassification;
    public View mDisplayPanel;
    public DraftManager mDraftManager;
    public EditorOriginHandler mEditorOriginHandler;
    public EditorToast mEditorToast;
    public ExportTask mExportTask;
    public FrameLayout mExtraContainer;
    public FragmentManager mFragments;
    public InitializeController mInitializeController;
    public boolean mIsBeautifyApplied;
    public boolean mIsInMultiWindowMode;
    public boolean mIsNaviProcessing;
    public View mMenuRenderLine;
    public boolean mNeedConfirmPassword;
    public boolean mNightMode;
    public PhotoGuideViewManager mPhotoGuideViewManager;
    public LottieAnimationView mProgressBar;
    public ProgressBarHandler mProgressBarHandler;
    public boolean mResumed;
    public Sampler mSampler;
    public SimpleSelectDialog mSimpleSelectDialog;
    public Effect mSingleEffect;
    public Disposable mSkySceneDisposable;
    public boolean mSuspendInputs;
    public VideoExportManager mVideoExportManager;
    public VideoExportTask mVideoExportTask;
    public FragmentData[] mFragmentData = new FragmentData[Effect.values().length];
    public String[] mSampleTags = new String[Effect.values().length];
    public TransitionConfiguration mTransitionConfig = new TransitionConfiguration(this);
    public boolean mPreviewRefreshedWhenInit = false;
    public boolean mTransitionEnd = false;
    public int mActivityResult = 0;
    public int mMode = 0;
    public PhotoNaviCallback mNavigatorCallbacks = new PhotoNaviCallback();
    public final LayoutOrientationTracker mLayoutOrientationTracker = new LayoutOrientationTracker(new LayoutOrientationTracker.OnLayoutOrientationChangeListener() { // from class: com.miui.gallery.editor.photo.app.PhotoEditor$$ExternalSyntheticLambda1
        @Override // com.miui.gallery.editor.utils.LayoutOrientationTracker.OnLayoutOrientationChangeListener
        public final void onLayoutOrientationChange() {
            PhotoEditor.$r8$lambda$hqGxTMHxFd0UKJSvPnKZVvbtcz8(PhotoEditor.this);
        }
    });
    public InitializeController.Callbacks mDecoderCallbacks = new InitializeController.Callbacks() { // from class: com.miui.gallery.editor.photo.app.PhotoEditor.1
        {
            PhotoEditor.this = this;
        }

        @Override // com.miui.gallery.editor.photo.app.InitializeController.Callbacks
        public int doInitialize() {
            SdkManager sdkManager = SdkManager.INSTANCE;
            sdkManager.onAttach(PhotoEditor.this.getApplication());
            sdkManager.onActivityCreate();
            if (PhotoEditor.this.mPhotoGuideViewManager == null) {
                PhotoEditor.this.mPhotoGuideViewManager = new PhotoGuideViewManager();
            }
            PhotoEditor.this.mPhotoGuideViewManager.init();
            boolean z = false;
            try {
                DraftManager draftManager = PhotoEditor.this.mDraftManager;
                if (draftManager != null) {
                    z = draftManager.initializeForPreview(PhotoEditor.this.mEditorOriginHandler.isInMainProcess());
                }
                return z ? 3 : 2;
            } catch (FileNotFoundException e) {
                DefaultLogger.w("PhotoEditor", e);
                return 1;
            } catch (SecurityException e2) {
                DefaultLogger.w("PhotoEditor", e2);
                return 2;
            }
        }

        @Override // com.miui.gallery.editor.photo.app.InitializeController.Callbacks
        public void onDone() {
            Fragment findFragmentByTag = PhotoEditor.this.mFragments.findFragmentByTag("preview");
            PreviewFragment previewFragment = (PreviewFragment) findFragmentByTag;
            if (findFragmentByTag != null && findFragmentByTag.isAdded()) {
                previewFragment.setLoadDone(true);
                previewFragment.showForLoadDown();
                previewFragment.closeMaskFrame(false);
                if (PhotoEditor.this.mDraftManager != null) {
                    PhotoEditor.this.mDraftManager.setOnPreviewRefreshListener(PhotoEditor.this.mOnPreviewRefreshListener);
                }
            }
            PhotoEditor.this.mTransitionConfig.onImageLoaded();
            if (PhotoEditor.this.isSingleEffectMode()) {
                PhotoEditor photoEditor = PhotoEditor.this;
                photoEditor.showSingleEffectDisplay(photoEditor.mSingleEffect);
            }
        }
    };
    public DraftManager.OnPreviewRefreshListener mOnPreviewRefreshListener = new DraftManager.OnPreviewRefreshListener() { // from class: com.miui.gallery.editor.photo.app.PhotoEditor.2
        {
            PhotoEditor.this = this;
        }

        @Override // com.miui.gallery.editor.photo.app.DraftManager.OnPreviewRefreshListener
        public void onRefresh(Bitmap bitmap) {
            PhotoEditor.this.mPreviewRefreshedWhenInit = true;
            PhotoEditor.this.tryRefreshPreview();
        }
    };
    public PreviewFragment.Callbacks mPreviewCallbacks = new PreviewFragment.Callbacks() { // from class: com.miui.gallery.editor.photo.app.PhotoEditor.3
        {
            PhotoEditor.this = this;
        }

        @Override // com.miui.gallery.editor.photo.app.PreviewFragment.Callbacks
        public Bitmap onLoadPreview() {
            if (PhotoEditor.this.mDraftManager == null) {
                return null;
            }
            return PhotoEditor.this.mDraftManager.getPreview();
        }

        @Override // com.miui.gallery.editor.photo.app.PreviewFragment.Callbacks
        public Bitmap onLoadPreviewOriginal() {
            if (PhotoEditor.this.mDraftManager == null) {
                return null;
            }
            return PhotoEditor.this.mDraftManager.getPreviewOriginal();
        }

        @Override // com.miui.gallery.editor.photo.app.PreviewFragment.Callbacks
        public boolean isPreviewChanged() {
            return PhotoEditor.this.mDraftManager != null && !PhotoEditor.this.mDraftManager.isEmpty();
        }

        @Override // com.miui.gallery.editor.photo.app.PreviewFragment.Callbacks
        public void removeWaterRender(boolean z) {
            if (PhotoEditor.this.mDraftManager != null) {
                PhotoEditor.this.mDraftManager.removeWaterRender(z, PhotoEditor.this.mReRenderCallback);
            }
        }

        @Override // com.miui.gallery.editor.photo.app.PreviewFragment.Callbacks
        public WaterMaskWrapper getWaterMaskWrapper() {
            if (PhotoEditor.this.mDraftManager != null) {
                return PhotoEditor.this.mDraftManager.getWaterMaskWrapper();
            }
            return null;
        }

        @Override // com.miui.gallery.editor.photo.app.PreviewFragment.Callbacks
        public boolean moveMaskEnable() {
            return PhotoEditor.this.mDraftManager != null && PhotoEditor.this.mDraftManager.isPreviewEnable();
        }

        @Override // com.miui.gallery.editor.photo.app.PreviewFragment.Callbacks
        public void setMaskMoved() {
            PhotoEditor.this.setExposeButtonEnable(!(PhotoEditor.this.mDraftManager == null || PhotoEditor.this.mDraftManager.isEmpty()));
        }

        @Override // com.miui.gallery.editor.photo.app.PreviewFragment.Callbacks
        public void onDiscard() {
            PhotoEditor.this.onActivityFinish(false);
            ActivityCompat.finishAfterTransition(PhotoEditor.this);
            sampleExit("cancelled");
            PhotoEditor.this.mIsBeautifyApplied = false;
        }

        @Override // com.miui.gallery.editor.photo.app.PreviewFragment.Callbacks
        public void onExport() {
            if (PhotoEditor.this.mVideoExportManager.hasExportTask()) {
                PhotoEditor.this.mVideoExportTask.showExportDialog();
            } else {
                PhotoEditor.this.mExportTask.showExportDialog();
            }
            sampleExit("exported");
            if (PhotoEditor.this.mIsBeautifyApplied) {
                HashMap hashMap = new HashMap();
                hashMap.put("effect", Integer.toString(Filter.getScene()));
                PhotoEditor.this.sample("beautify_", hashMap);
            }
            PhotoEditor.this.mIsBeautifyApplied = false;
        }

        public final void sampleExit(String str) {
            if (PhotoEditor.this.mDraftManager == null) {
                return;
            }
            HashMap hashMap = new HashMap();
            hashMap.put(MiStat.Param.COUNT, String.valueOf(PhotoEditor.this.mDraftManager.getStepCount()));
            hashMap.put("elapse_time", String.valueOf((System.currentTimeMillis() - PhotoEditor.this.mCreateTime) / 100));
            if (PhotoEditor.this.mDraftManager.isRemoveWatermarkEnable()) {
                hashMap.put(CallMethod.ARG_EXTRA_STRING, "remove_watermark" + String.valueOf(!PhotoEditor.this.mDraftManager.isWithWatermark()));
            }
            PhotoEditor.this.mSampler.recordCategory("photo_editor_home_page", str, hashMap);
        }
    };
    public PhotoNaviFragment.OnPreparePhotoNaviFragmentListener mOnPrepareListener = new PhotoNaviFragment.OnPreparePhotoNaviFragmentListener() { // from class: com.miui.gallery.editor.photo.app.PhotoEditor.4
        {
            PhotoEditor.this = this;
        }

        @Override // com.miui.gallery.editor.photo.app.navigator.PhotoNaviFragment.OnPreparePhotoNaviFragmentListener
        public boolean isBeautifyApplied() {
            return PhotoEditor.this.mIsBeautifyApplied;
        }
    };
    public MenuFragment.Callbacks mMenuCallback = new MenuFragment.Callbacks() { // from class: com.miui.gallery.editor.photo.app.PhotoEditor.5
        {
            PhotoEditor.this = this;
        }

        @Override // com.miui.gallery.editor.photo.app.MenuFragment.Callbacks
        public Bitmap onLoadPreview() {
            if (PhotoEditor.this.mDraftManager == null) {
                return null;
            }
            return PhotoEditor.this.mDraftManager.getPreview();
        }

        @Override // com.miui.gallery.editor.photo.app.MenuFragment.Callbacks
        public Bitmap onLoadOrigin() {
            if (PhotoEditor.this.mDraftManager == null) {
                return null;
            }
            return PhotoEditor.this.mDraftManager.decodeOrigin();
        }

        @Override // com.miui.gallery.editor.photo.app.MenuFragment.Callbacks
        public List<RenderData> onLoadRenderData() {
            ArrayList arrayList = new ArrayList();
            if (PhotoEditor.this.mDraftManager != null) {
                PhotoEditor.this.mDraftManager.getRenderData(arrayList);
            }
            return arrayList;
        }

        @Override // com.miui.gallery.editor.photo.app.MenuFragment.Callbacks
        public void onDiscard(MenuFragment menuFragment) {
            if (PhotoEditor.this.isSingleEffectMode()) {
                PhotoEditor.this.mPreviewCallbacks.onDiscard();
                return;
            }
            Fragment findFragmentByTag = PhotoEditor.this.mFragments.findFragmentByTag("preview");
            if (findFragmentByTag != null && ((PreviewFragment) findFragmentByTag).isRunningPreviewAnimator()) {
                return;
            }
            if (findFragmentByTag != null) {
                ((PreviewFragment) findFragmentByTag).closeMaskFrame(true);
            }
            RenderFragment renderFragment = menuFragment.getRenderFragment();
            if (renderFragment == null || !renderFragment.isAdded()) {
                return;
            }
            sample(PhotoEditor.this.mSampleTags[menuFragment.mEffect.ordinal()], renderFragment, "discard_detail");
            PhotoEditor.this.onExit(menuFragment);
        }

        @Override // com.miui.gallery.editor.photo.app.MenuFragment.Callbacks
        public void onSave(MenuFragment menuFragment) {
            if (!PhotoEditor.this.isSingleEffectMode()) {
                Fragment findFragmentByTag = PhotoEditor.this.mFragments.findFragmentByTag("preview");
                if (findFragmentByTag != null && ((PreviewFragment) findFragmentByTag).isRunningPreviewAnimator()) {
                    return;
                }
                if (findFragmentByTag != null) {
                    ((PreviewFragment) findFragmentByTag).closeMaskFrame(true);
                }
                RenderFragment renderFragment = menuFragment.getRenderFragment();
                if (renderFragment != null && renderFragment.isAdded()) {
                    RenderData export = renderFragment.export();
                    if (export != null) {
                        sample(PhotoEditor.this.mSampleTags[menuFragment.mEffect.ordinal()], renderFragment, "save_detail");
                        if (export.isVideo()) {
                            PhotoEditor.this.mVideoExportManager.setRenderData(export);
                            PhotoEditor.this.mPreviewCallbacks.onExport();
                            return;
                        } else if (PhotoEditor.this.mDraftManager == null) {
                            return;
                        } else {
                            PhotoEditor.this.mDraftManager.enqueue(PhotoEditor.this.mPreviewSaveCallback, export);
                            return;
                        }
                    }
                    PhotoEditor.this.mPreviewSaveCallback.onCancel();
                    return;
                }
                DefaultLogger.d("PhotoEditor", "no active render fragment");
                return;
            }
            RenderFragment renderFragment2 = menuFragment.getRenderFragment();
            if (renderFragment2 == null) {
                return;
            }
            RenderData export2 = renderFragment2.export();
            if (export2 != null) {
                if (export2.isVideo()) {
                    PhotoEditor.this.mVideoExportManager.setRenderData(export2);
                    PhotoEditor.this.mPreviewCallbacks.onExport();
                    return;
                } else if (PhotoEditor.this.mDraftManager == null) {
                    return;
                } else {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(export2);
                    PhotoEditor.this.mDraftManager.setRenderDataList(arrayList);
                    PhotoEditor.this.mPreviewCallbacks.onExport();
                    return;
                }
            }
            DefaultLogger.d("PhotoEditor", "saved render data is null");
        }

        public final void sample(String str, RenderFragment renderFragment, String str2) {
            List<String> sample = renderFragment.sample();
            HashMap hashMap = new HashMap();
            if (sample != null && !sample.isEmpty()) {
                for (String str3 : sample) {
                    hashMap.put("effect", str3);
                    PhotoEditor.this.mSampler.recordEvent(str, str2, hashMap);
                }
                return;
            }
            hashMap.put("effect", "*none*");
            PhotoEditor.this.mSampler.recordEvent(str, str2, hashMap);
        }
    };
    public AlertDialogFragment.Callbacks mAlertDialogCallbacks = new AlertDialogFragment.Callbacks() { // from class: com.miui.gallery.editor.photo.app.PhotoEditor.6
        @Override // com.miui.gallery.editor.photo.app.AlertDialogFragment.Callbacks
        public void onCancel(AlertDialogFragment alertDialogFragment) {
        }

        @Override // com.miui.gallery.editor.photo.app.AlertDialogFragment.Callbacks
        public void onDismiss(AlertDialogFragment alertDialogFragment) {
        }

        {
            PhotoEditor.this = this;
        }

        @Override // com.miui.gallery.editor.photo.app.AlertDialogFragment.Callbacks
        public void onClick(AlertDialogFragment alertDialogFragment, int i) {
            DefaultLogger.d("PhotoEditor", "confirm dialog from %s, event is %d", alertDialogFragment == null ? "unknown" : alertDialogFragment.getTag(), Integer.valueOf(i));
            if ("main_alert_dialog".equals(alertDialogFragment.getTag())) {
                if (i == -1) {
                    PhotoEditor.this.mPreviewCallbacks.onExport();
                } else if (i != -2) {
                } else {
                    PhotoEditor.this.mPreviewCallbacks.onDiscard();
                }
            } else if (!"menu_alert_dialog".equals(alertDialogFragment.getTag())) {
            } else {
                Fragment findFragmentById = PhotoEditor.this.mFragments.findFragmentById(R.id.menu_panel);
                if (!(findFragmentById instanceof MenuFragment)) {
                    return;
                }
                if (i == -1) {
                    PhotoEditor.this.mMenuCallback.onSave((MenuFragment) findFragmentById);
                } else if (i != -2) {
                } else {
                    PhotoEditor.this.mMenuCallback.onDiscard((MenuFragment) findFragmentById);
                }
            }
        }
    };
    public Callback mReRenderCallback = new Callback<Bitmap, Void>() { // from class: com.miui.gallery.editor.photo.app.PhotoEditor.7
        @Override // com.miui.gallery.editor.photo.utils.Callback
        public void onExecute(Bitmap bitmap) {
        }

        {
            PhotoEditor.this = this;
        }

        @Override // com.miui.gallery.editor.photo.utils.Callback
        public void onPrepare() {
            PhotoEditor.this.mSuspendInputs = true;
        }

        @Override // com.miui.gallery.editor.photo.utils.Callback
        public void onDone(Bitmap bitmap) {
            boolean z = false;
            PhotoEditor.this.mSuspendInputs = false;
            PreviewFragment previewFragment = (PreviewFragment) PhotoEditor.this.mFragments.findFragmentByTag("preview");
            if (PhotoEditor.this.mDraftManager == null || PhotoEditor.this.mDraftManager.isEmpty()) {
                z = true;
            }
            if (previewFragment != null) {
                previewFragment.refreshPreview(bitmap);
                previewFragment.enableComparison(!z);
            }
            PhotoEditor.this.setExposeButtonEnable(!z);
        }

        @Override // com.miui.gallery.editor.photo.utils.Callback
        public void onCancel() {
            PhotoEditor.this.mSuspendInputs = false;
        }

        @Override // com.miui.gallery.editor.photo.utils.Callback
        public void onError(Void r2) {
            PhotoEditor.this.mSuspendInputs = false;
        }
    };
    public Callback mPreviewSaveCallback = new Callback<Bitmap, Void>() { // from class: com.miui.gallery.editor.photo.app.PhotoEditor.8
        @Override // com.miui.gallery.editor.photo.utils.Callback
        public void onExecute(Bitmap bitmap) {
        }

        {
            PhotoEditor.this = this;
        }

        @Override // com.miui.gallery.editor.photo.utils.Callback
        public void onPrepare() {
            PhotoEditor.this.mSuspendInputs = true;
        }

        @Override // com.miui.gallery.editor.photo.utils.Callback
        public void onDone(Bitmap bitmap) {
            PreviewFragment previewFragment = (PreviewFragment) PhotoEditor.this.mFragments.findFragmentByTag("preview");
            if (previewFragment != null) {
                previewFragment.setRemoveWatermarkEnable(PhotoEditor.this.mDraftManager.checkRemoveWatermarkEnable());
            }
            PhotoEditor.this.mSuspendInputs = false;
            MenuFragment findActiveMenu = PhotoEditor.this.findActiveMenu();
            if (findActiveMenu != null) {
                findActiveMenu.hideProcessDialog();
                PhotoEditor.this.onExit(findActiveMenu);
            }
            PhotoEditor.this.mIsBeautifyApplied = false;
            if (PhotoEditor.this.mDraftManager == null || PhotoEditor.this.mDraftManager.getStepCount() <= 1) {
                PhotoEditor.this.setExposeButtonEnable(true);
            }
        }

        @Override // com.miui.gallery.editor.photo.utils.Callback
        public void onCancel() {
            PhotoEditor.this.mSuspendInputs = false;
            PhotoEditor.this.onExit(PhotoEditor.this.findActiveMenu());
        }

        @Override // com.miui.gallery.editor.photo.utils.Callback
        public void onError(Void r2) {
            PhotoEditor.this.mSuspendInputs = false;
            ToastUtils.makeText(PhotoEditor.this, (int) R.string.main_save_error_msg);
        }
    };
    public VideoExportManager.OnProgressCallback mOnProgressCallback = new VideoExportManager.OnProgressCallback() { // from class: com.miui.gallery.editor.photo.app.PhotoEditor.9
        {
            PhotoEditor.this = this;
        }

        @Override // com.miui.gallery.editor.photo.app.VideoExportManager.OnProgressCallback
        public void onProgress(int i) {
            PhotoEditor.this.mVideoExportTask.setProgress(i);
        }
    };
    public VideoExportFragment.Callbacks mVideoExportCallback = new VideoExportFragment.Callbacks() { // from class: com.miui.gallery.editor.photo.app.PhotoEditor.10
        public long mStartTime;

        {
            PhotoEditor.this = this;
        }

        @Override // com.miui.gallery.editor.photo.app.VideoExportFragment.Callbacks
        public int doExport() {
            PhotoEditor.this.mVideoExportTask.prepareToExport();
            DefaultLogger.d("PhotoEditor", "doExport start");
            this.mStartTime = System.currentTimeMillis();
            int export = PhotoEditor.this.mVideoExportManager.export(PhotoEditor.this.mDraftManager.getPreview(), PhotoEditor.this.mVideoExportTask.getExportUri());
            DefaultLogger.d("PhotoEditor", "doExport end, use time: %d", Long.valueOf(System.currentTimeMillis() - this.mStartTime));
            VideoExportTask videoExportTask = PhotoEditor.this.mVideoExportTask;
            DraftManager draftManager = PhotoEditor.this.mDraftManager;
            boolean z = true;
            if (export != 1) {
                z = false;
            }
            videoExportTask.onExport(draftManager, z);
            PhotoEditor.this.sampleExportTime("compress_finished", System.currentTimeMillis() - this.mStartTime);
            return export;
        }

        @Override // com.miui.gallery.editor.photo.app.VideoExportFragment.Callbacks
        public void onExported(boolean z) {
            PhotoEditor.this.mVideoExportTask.onPostExport(z);
            if (z) {
                if (!PhotoEditor.this.mVideoExportTask.isExternalCall()) {
                    DefaultLogger.d("PhotoEditor", "internal call, pass result");
                    Intent intent = new Intent();
                    intent.setDataAndType(GalleryOpenProvider.translateToContent(PhotoEditor.this.mVideoExportTask.getExportUri().getPath()), "video/mp4");
                    intent.putExtra("photo_secret_id", PhotoEditor.this.mVideoExportTask.getSecretId());
                    PhotoEditor.this.setActivityResult(-1, intent);
                }
                if (PhotoEditor.this.isSingleEffectMode()) {
                    PhotoEditor photoEditor = PhotoEditor.this;
                    IntentUtil.gotoSinglePhotoPage(photoEditor, photoEditor.mVideoExportTask.getExportUri());
                }
                PhotoEditor.this.mTransitionConfig.mRunTransition = false;
                PhotoEditor.this.onActivityFinish(true);
                PhotoEditor.this.finish();
                return;
            }
            PhotoEditor.this.mVideoExportTask.closeExportDialog();
            ToastUtils.makeText(PhotoEditor.this, (int) R.string.main_save_error_msg);
        }

        @Override // com.miui.gallery.editor.photo.app.VideoExportFragment.Callbacks
        public void doCancel() {
            PhotoEditor.this.mVideoExportManager.cancel();
        }

        @Override // com.miui.gallery.editor.photo.app.VideoExportFragment.Callbacks
        public void onCancelled() {
            PhotoEditor.this.mVideoExportTask.closeExportDialog();
            PhotoEditor.this.mVideoExportManager.onCancel();
        }
    };
    public ExportFragment.Callbacks mExportCallbacks = new ExportFragment.Callbacks() { // from class: com.miui.gallery.editor.photo.app.PhotoEditor.11
        public long mStartTime;

        {
            PhotoEditor.this = this;
        }

        @Override // com.miui.gallery.editor.photo.app.ExportFragment.Callbacks
        public boolean doExport() {
            DraftManager draftManager = PhotoEditor.this.mDraftManager;
            if (draftManager == null) {
                return false;
            }
            DefaultLogger.w("PhotoEditor", "[Export] start");
            this.mStartTime = System.currentTimeMillis();
            PhotoEditor.this.mExportTask.prepareToExport(draftManager);
            if (!FilePermissionUtils.checkFileCreatePermission(PhotoEditor.this.mo546getActivity(), PhotoEditor.this.mExportTask.getExportUri().getPath())) {
                return false;
            }
            boolean doExport = PhotoEditor.this.mEditorOriginHandler.doExport(draftManager, PhotoEditor.this.mExportTask.getExportUri());
            DefaultLogger.w("PhotoEditor", "[Export] decode + render + encode, time: %d", Long.valueOf(System.currentTimeMillis() - this.mStartTime));
            this.mStartTime = System.currentTimeMillis();
            boolean onExport = PhotoEditor.this.mExportTask.onExport(draftManager, doExport);
            DefaultLogger.w("PhotoEditor", "[Export] update database end, time: %d", Long.valueOf(System.currentTimeMillis() - this.mStartTime));
            PhotoEditor.this.sampleExportTime("compress_finished", System.currentTimeMillis() - this.mStartTime);
            return onExport;
        }

        @Override // com.miui.gallery.editor.photo.app.ExportFragment.Callbacks
        public void onCancelled(boolean z) {
            PhotoEditor.this.mExportTask.onCancelled(z);
            PhotoEditor.this.sampleExportTime("compress_cancelled", System.currentTimeMillis() - this.mStartTime);
        }

        @Override // com.miui.gallery.editor.photo.app.ExportFragment.Callbacks
        public void onExported(boolean z) {
            PhotoEditor.this.mExportTask.onPostExport(z);
            if (z) {
                if (!PhotoEditor.this.mExportTask.isExternalCall()) {
                    DefaultLogger.d("PhotoEditor", "internal call, pass result");
                    Intent intent = new Intent();
                    intent.setDataAndType(GalleryOpenProvider.translateToContent(PhotoEditor.this.mExportTask.getExportUri().getPath()), "image/jpeg");
                    intent.putExtra("photo_secret_id", PhotoEditor.this.mExportTask.getSecretId());
                    PhotoEditor.this.setActivityResult(-1, intent);
                }
                if (PhotoEditor.this.isSingleEffectMode()) {
                    PhotoEditor photoEditor = PhotoEditor.this;
                    IntentUtil.gotoSinglePhotoPage(photoEditor, photoEditor.mExportTask.getExportUri());
                }
                PhotoEditor.this.onActivityFinish(true);
                ActivityCompat.finishAfterTransition(PhotoEditor.this);
                return;
            }
            PhotoEditor.this.mExportTask.closeExportDialog();
            ToastUtils.makeText(PhotoEditor.this, (int) R.string.main_save_error_msg);
        }
    };

    public static /* synthetic */ void $r8$lambda$8B2cOusTZbokTTrdwXGEcXWRqzg(PhotoEditor photoEditor, Disposable disposable) {
        photoEditor.lambda$startSkyScene$4(disposable);
    }

    /* renamed from: $r8$lambda$Bvf0nB23bNH1VZGyPywT9-evyOY */
    public static /* synthetic */ void m740$r8$lambda$Bvf0nB23bNH1VZGyPywT9evyOY(PhotoEditor photoEditor, Effect effect, ObservableEmitter observableEmitter) {
        photoEditor.lambda$startSkyScene$3(effect, observableEmitter);
    }

    public static /* synthetic */ void $r8$lambda$ZE6tUs3hqETShsOpgaY_KJjc0X8(PhotoEditor photoEditor, Effect effect, Object obj) {
        photoEditor.lambda$startSkyScene$5(effect, obj);
    }

    public static /* synthetic */ void $r8$lambda$hqGxTMHxFd0UKJSvPnKZVvbtcz8(PhotoEditor photoEditor) {
        photoEditor.lambda$new$0();
    }

    /* renamed from: $r8$lambda$leqa3N9N-OflPkQTw439-s-KOdo */
    public static /* synthetic */ void m741$r8$lambda$leqa3N9NOflPkQTw439sKOdo(PhotoEditor photoEditor, boolean z) {
        photoEditor.lambda$onPermissionsChecked$1(z);
    }

    /* renamed from: $r8$lambda$wBRJTHw6v-MWvrYT2PFZvK7XnGI */
    public static /* synthetic */ void m742$r8$lambda$wBRJTHw6vMWvrYT2PFZvK7XnGI(PhotoEditor photoEditor, Effect effect) {
        photoEditor.lambda$showSingleEffectDisplay$2(effect);
    }

    public final void onCtaChecked() {
    }

    public PhotoEditor() {
        FragmentData[] fragmentDataArr = this.mFragmentData;
        Effect<SdkProvider<BeautifyData, AbstractEffectFragment>> effect = Effect.BEAUTIFY;
        fragmentDataArr[effect.ordinal()] = new FragmentData(BeautifyFragment.class, effect, R.string.photo_editor_beautify_auto);
        FragmentData[] fragmentDataArr2 = this.mFragmentData;
        Effect<SdkProvider<BeautifyData, AbstractEffectFragment>> effect2 = Effect.BEAUTIFY2;
        fragmentDataArr2[effect2.ordinal()] = new FragmentData(BeautifyFragment.class, effect2, R.string.photo_editor_miui_beauty);
        FragmentData[] fragmentDataArr3 = this.mFragmentData;
        Effect<SdkProvider<AdjustData, AbstractEffectFragment>> effect3 = Effect.ADJUST;
        fragmentDataArr3[effect3.ordinal()] = new FragmentData(AdjustMenuFragment.class, effect3, R.string.photo_editor_adjust);
        FragmentData[] fragmentDataArr4 = this.mFragmentData;
        Effect<SdkProvider<Adjust2Data, AbstractEffectFragment>> effect4 = Effect.ADJUST2;
        fragmentDataArr4[effect4.ordinal()] = new FragmentData(Adjust2MenuFragment.class, effect4, R.string.photo_editor_adjust);
        FragmentData[] fragmentDataArr5 = this.mFragmentData;
        Effect<SdkProvider<FilterCategory, AbstractEffectFragment>> effect5 = Effect.FILTER;
        fragmentDataArr5[effect5.ordinal()] = new FragmentData(FilterFragment.class, effect5, R.string.photo_editor_filter);
        FragmentData[] fragmentDataArr6 = this.mFragmentData;
        Effect<SdkProvider<SkyCategory, AbstractEffectFragment>> effect6 = Effect.SKY;
        fragmentDataArr6[effect6.ordinal()] = new FragmentData(SkyFragment.class, effect6, R.string.photo_editor_sky);
        FragmentData[] fragmentDataArr7 = this.mFragmentData;
        Effect<SdkProvider<CropData, AbstractCropFragment>> effect7 = Effect.CROP;
        fragmentDataArr7[effect7.ordinal()] = new FragmentData(CropMenuFragment.class, effect7, R.string.photo_editor_crop_and_rotate);
        FragmentData[] fragmentDataArr8 = this.mFragmentData;
        Effect<AbstractTextProvider> effect8 = Effect.TEXT;
        fragmentDataArr8[effect8.ordinal()] = new FragmentData(TextMenuFragment.class, effect8, R.string.photo_editor_text);
        FragmentData[] fragmentDataArr9 = this.mFragmentData;
        Effect<SdkProvider<DoodleData, AbstractDoodleFragment>> effect9 = Effect.DOODLE;
        fragmentDataArr9[effect9.ordinal()] = new FragmentData(DoodleMenuFragment.class, effect9, R.string.photo_editor_doodle);
        FragmentData[] fragmentDataArr10 = this.mFragmentData;
        Effect<AbstractStickerProvider> effect10 = Effect.STICKER;
        fragmentDataArr10[effect10.ordinal()] = new FragmentData(StickerMenuFragment.class, effect10, R.string.photo_editor_sticker);
        FragmentData[] fragmentDataArr11 = this.mFragmentData;
        Effect<SdkProvider<Object, AbstractLongCropFragment>> effect11 = Effect.LONG_CROP;
        fragmentDataArr11[effect11.ordinal()] = new FragmentData(LongCropFragment.class, effect11, R.string.photo_editor_long_crop);
        FragmentData[] fragmentDataArr12 = this.mFragmentData;
        Effect<SdkProvider<MosaicData, AbstractMosaicFragment>> effect12 = Effect.MOSAIC;
        fragmentDataArr12[effect12.ordinal()] = new FragmentData(MosaicMenuFragment.class, effect12, R.string.photo_editor_mosaic);
        FragmentData[] fragmentDataArr13 = this.mFragmentData;
        Effect<SdkProvider<RemoverData, AbstractRemoverFragment>> effect13 = Effect.REMOVER;
        fragmentDataArr13[effect13.ordinal()] = new FragmentData(RemoverMenuFragment.class, effect13, R.string.photo_editor_remover);
        FragmentData[] fragmentDataArr14 = this.mFragmentData;
        Effect<SdkProvider<FrameData, AbstractFrameFragment>> effect14 = Effect.FRAME;
        fragmentDataArr14[effect14.ordinal()] = new FragmentData(FrameMenuFragment.class, effect14, R.string.photo_editor_frame);
        FragmentData[] fragmentDataArr15 = this.mFragmentData;
        Effect<SdkProvider<Remover2Data, AbstractRemover2Fragment>> effect15 = Effect.REMOVER2;
        fragmentDataArr15[effect15.ordinal()] = new FragmentData(Remover2MenuFragment.class, effect15, R.string.photo_editor_remover);
        this.mSampleTags[effect.ordinal()] = "beautify_";
        this.mSampleTags[effect2.ordinal()] = "beautify_";
        this.mSampleTags[effect3.ordinal()] = "enhance_";
        this.mSampleTags[effect4.ordinal()] = "enhance_";
        this.mSampleTags[effect5.ordinal()] = "filter_";
        this.mSampleTags[effect6.ordinal()] = "sky_";
        this.mSampleTags[effect7.ordinal()] = "crop_";
        this.mSampleTags[effect10.ordinal()] = "sticker_";
        this.mSampleTags[effect11.ordinal()] = "crop_";
        this.mSampleTags[effect8.ordinal()] = "text_";
        this.mSampleTags[effect12.ordinal()] = "mosaic_";
        this.mSampleTags[effect9.ordinal()] = "doodle_";
        this.mSampleTags[effect13.ordinal()] = "remover_";
        this.mSampleTags[effect14.ordinal()] = "frame_";
        this.mSampleTags[effect15.ordinal()] = "remover2_";
    }

    @Override // com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        BaseGalleryPreferences.CTA.setToAllowUseOnOfflineGlobal(getIntent().getBooleanExtra("allow_use_on_offline_global", false));
        this.mCurrentClassification = getIntent().getIntExtra("extra_photopagefragment_screen_scene_code", 0);
        super.onCreate(null);
        WindowCompat.setCutoutModeShortEdges(getWindow());
        this.mTransitionConfig.onActivityCreate(bundle != null);
        if (!OrientationCheckHelper.isSupportOrientationChange()) {
            if (getResources().getConfiguration().orientation == 2) {
                this.mTransitionConfig.mRunTransition = false;
            }
            SystemUiUtil.setRequestedOrientation(1, this);
        }
        if (getIntent().getExtras() != null) {
            this.mMode = getIntent().getExtras().getInt("editor_mode", 0);
        }
        Uri data = getIntent().getData();
        if (data == null) {
            finish();
            return;
        }
        DraftManager draftManager = new DraftManager(this, data, getIntent().getExtras());
        this.mDraftManager = draftManager;
        draftManager.setSingleEffectMode(isSingleEffectMode());
        this.mExportTask = ExportTask.from(this);
        this.mVideoExportTask = VideoExportTask.from(this);
        VideoExportManager videoExportManager = new VideoExportManager();
        this.mVideoExportManager = videoExportManager;
        videoExportManager.setOnProgressCallback(this.mOnProgressCallback);
        this.mEditorToast = new EditorToast(this);
        if (this.mExportTask == null || this.mVideoExportTask == null) {
            ActivityCompat.finishAfterTransition(this);
            ToastUtils.makeText(this, (int) R.string.image_invalid_path);
            return;
        }
        setContentView(R.layout.photo_editor_main);
        this.mSampler = Sampler.from(getIntent().getExtras());
        this.mEditorOriginHandler = new EditorOriginHandler(this, this.mExportTask.getSource());
        this.mFragments = getSupportFragmentManager();
        PermissionInjector.injectIfNeededIn(this, this, null);
        this.mIsInMultiWindowMode = ActivityCompat.isInMultiWindowMode(this);
        this.mNightMode = BaseMiscUtil.isNightMode(this);
        this.mExtraContainer = (FrameLayout) findViewById(R.id.extra_container);
        this.mMenuRenderLine = findViewById(R.id.menu_render_line);
        this.mDisplayPanel = findViewById(R.id.display_panel);
        getWindow().setBackgroundDrawable(new ColorDrawable(-16777216));
        this.mLayoutOrientationTracker.onConfigurationChange(getResources().getConfiguration());
        LottieAnimationView lottieAnimationView = (LottieAnimationView) findViewById(R.id.progress);
        this.mProgressBar = lottieAnimationView;
        this.mProgressBarHandler = new ProgressBarHandler(lottieAnimationView);
        if (!this.mDraftManager.isSecret()) {
            return;
        }
        getWindow().addFlags(8192);
    }

    public /* synthetic */ void lambda$new$0() {
        DefaultLogger.d("PhotoEditor", "layout orientation change");
        View findViewById = findViewById(R.id.activity_main);
        EditorOrientationHelper.copyLayoutParams(getLayoutInflater().inflate(R.layout.photo_editor_main, (ViewGroup) findViewById.getParent(), false), findViewById, true);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof LayoutOrientationTracker.OnLayoutOrientationChangeListener) {
                ((LayoutOrientationTracker.OnLayoutOrientationChangeListener) fragment).onLayoutOrientationChange();
            }
        }
    }

    @Override // com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mLayoutOrientationTracker.onConfigurationChange(configuration);
    }

    @Override // com.miui.gallery.editor.photo.app.HostAbility
    public void showInnerToast(String str) {
        if (EditorOrientationHelper.isLayoutPortrait(this)) {
            this.mEditorToast.show(str, this.mMenuRenderLine, 80, (ScreenUtils.getScreenHorizontal(this) - this.mEditorToast.getToastWidth(str)) / 2, (-this.mEditorToast.getToastHeight()) - getResources().getDimensionPixelSize(R.dimen.editor_inner_toast_margin_bottom));
            return;
        }
        int screenVertical = ScreenUtils.getScreenVertical(this);
        int width = ((this.mDisplayPanel.getWidth() - getResources().getDimensionPixelSize(R.dimen.editor_confirm_menu_height)) / 2) + (this.mEditorToast.getToastWidth(str) / 2);
        EditorToast editorToast = this.mEditorToast;
        editorToast.show(str, this.mMenuRenderLine, 48, -width, (screenVertical / 2) - (editorToast.getToastHeight() / 2));
    }

    @Override // com.miui.gallery.editor.photo.app.HostAbility
    public void hideInnerToast() {
        this.mEditorToast.dismiss();
    }

    @Override // com.miui.gallery.editor.photo.app.HostAbility
    public void addViewToExtraContainer(View view, FrameLayout.LayoutParams layoutParams) {
        FrameLayout frameLayout = this.mExtraContainer;
        if (frameLayout == null) {
            return;
        }
        frameLayout.removeAllViews();
        this.mExtraContainer.addView(view, layoutParams);
    }

    @Override // com.miui.gallery.editor.photo.app.HostAbility
    public void clearExtraContainer() {
        FrameLayout frameLayout = this.mExtraContainer;
        if (frameLayout == null) {
            return;
        }
        frameLayout.removeAllViews();
    }

    @Override // com.miui.gallery.editor.photo.app.HostAbility
    public ViewGroup getExtraContainer() {
        return this.mExtraContainer;
    }

    @Override // com.miui.gallery.editor.photo.app.HostAbility
    public void sample(String str) {
        sample(str, null);
    }

    @Override // com.miui.gallery.editor.photo.app.HostAbility
    public void sample(String str, Map<String, String> map) {
        this.mSampler.recordCategory("photo_editor", str, map);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onAttachedToWindow() {
        if (!WindowCompat.isNotch(this) || this.mIsInMultiWindowMode) {
            return;
        }
        SystemUiUtil.extendToStatusBar(getWindow().getDecorView());
    }

    @Override // com.miui.gallery.permission.core.PermissionCheckCallback
    public void onPermissionsChecked(Permission[] permissionArr, int[] iArr, boolean[] zArr) {
        if (BaseGalleryPreferences.CTA.allowUseOnOfflineGlobal() || BaseGalleryPreferences.CTA.canConnectNetwork()) {
            onCtaChecked();
        } else {
            AgreementsUtils.showUserAgreements(this, new OnAgreementInvokedListener() { // from class: com.miui.gallery.editor.photo.app.PhotoEditor$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.agreement.core.OnAgreementInvokedListener
                public final void onAgreementInvoked(boolean z) {
                    PhotoEditor.m741$r8$lambda$leqa3N9NOflPkQTw439sKOdo(PhotoEditor.this, z);
                }
            });
        }
        InitializeController initializeController = new InitializeController(this, this.mDecoderCallbacks);
        this.mInitializeController = initializeController;
        initializeController.doInitialize();
        this.mCreateTime = System.currentTimeMillis();
        this.mSampler.recordCategory("photo_editor_home_page", "enter");
        BeautificationSDK.init(getApplicationContext());
        this.mTransitionConfig.postActivityCreate();
        initScreenBrightness();
        PreviewFragment previewFragment = new PreviewFragment();
        previewFragment.setLoadDone(false);
        this.mFragments.beginTransaction().add(R.id.display_panel, previewFragment, "preview").commitAllowingStateLoss();
        if (this.mMode == 0) {
            prepareNavigator();
            return;
        }
        Effect<SdkProvider<SkyCategory, AbstractEffectFragment>> effect = Effect.SKY;
        this.mSingleEffect = effect;
        showSingleEffectMenu(effect);
    }

    public /* synthetic */ void lambda$onPermissionsChecked$1(boolean z) {
        onCtaChecked();
    }

    public final boolean isSingleEffectMode() {
        return this.mMode != 0;
    }

    @Override // com.miui.gallery.app.activity.AndroidActivity, android.app.Activity
    public void onMultiWindowModeChanged(boolean z, Configuration configuration) {
        super.onMultiWindowModeChanged(z, configuration);
        this.mIsInMultiWindowMode = z;
        if (z) {
            SystemUiUtil.showSystemBars(getWindow().getDecorView(), this.mIsInMultiWindowMode);
        } else {
            SystemUiUtil.hideSystemBars(getWindow().getDecorView(), !this.mNightMode, false, false);
        }
    }

    public final void showSingleEffectMenu(Effect effect) {
        FragmentData fragmentData = this.mFragmentData[effect.ordinal()];
        MenuFragment newMenu = fragmentData.newMenu();
        Bundle bundle = new Bundle();
        bundle.putBoolean("single_effect_mode", true);
        newMenu.setArguments(bundle);
        this.mFragments.beginTransaction().add(R.id.menu_panel, newMenu, fragmentData.menuTag).commitAllowingStateLoss();
    }

    public final void showSingleEffectDisplay(final Effect effect) {
        FragmentData fragmentData = this.mFragmentData[effect.ordinal()];
        RenderFragment createRenderFragment = fragmentData.newMenu().createRenderFragment(fragmentData.titleResId);
        getSupportFragmentManager().beginTransaction().detach(createRenderFragment).add(R.id.display_panel, createRenderFragment, fragmentData.renderTag).hide(createRenderFragment).commitAllowingStateLoss();
        getSupportFragmentManager().executePendingTransactions();
        Fragment findFragmentById = this.mFragments.findFragmentById(R.id.menu_panel);
        MenuFragment menuFragment = (MenuFragment) findFragmentById;
        if (findFragmentById != null && findFragmentById.isAdded()) {
            menuFragment.setRenderFragmentInSingleMode(createRenderFragment);
        }
        getWindow().getDecorView().post(new Runnable() { // from class: com.miui.gallery.editor.photo.app.PhotoEditor$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                PhotoEditor.m742$r8$lambda$wBRJTHw6vMWvrYT2PFZvK7XnGI(PhotoEditor.this, effect);
            }
        });
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        EditorOriginHandler editorOriginHandler = this.mEditorOriginHandler;
        if (editorOriginHandler != null) {
            editorOriginHandler.onStart();
        }
    }

    public final void initScreenBrightness() {
        Intent intent = getIntent();
        if (intent != null) {
            float floatExtra = intent.getFloatExtra("photo-brightness-manual", -1.0f);
            float floatExtra2 = intent.getFloatExtra("photo-brightness-auto", -1.0f);
            if (floatExtra < 0.0f && floatExtra2 < 0.0f) {
                return;
            }
            BrightnessManager brightnessManager = new BrightnessManager(this, floatExtra, floatExtra2);
            this.mBrightnessManager = brightnessManager;
            if (!this.mResumed) {
                return;
            }
            brightnessManager.onResume();
        }
    }

    @Override // com.miui.gallery.permission.core.PermissionCheckCallback
    public Permission[] getRuntimePermissions() {
        return new Permission[]{new Permission("android.permission.WRITE_EXTERNAL_STORAGE", getString(R.string.permission_storage_desc), true)};
    }

    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    @SuppressLint({"MissingSuperCall"})
    public void onSaveInstanceState(Bundle bundle) {
        DefaultLogger.d("PhotoEditor", "onSaveInstanceState");
    }

    @Override // com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        ScreenSceneClassificationUtil.setScreenSceneClassification(this.mCurrentClassification);
        BrightnessManager brightnessManager = this.mBrightnessManager;
        if (brightnessManager != null) {
            brightnessManager.onResume();
        }
        this.mSampler.recordPageStart(this);
        this.mResumed = true;
        if (isNeedConfirmPassword()) {
            this.mNeedConfirmPassword = false;
            LockSettingsHelper.startAuthenticatePasswordActivity(this, 27);
        }
        if (this.mIsInMultiWindowMode) {
            SystemUiUtil.showSystemBars(getWindow().getDecorView(), this.mIsInMultiWindowMode);
        }
        SystemUiUtil.setWindowFullScreenFlag(this);
    }

    @Override // com.miui.gallery.app.activity.AndroidActivity
    public boolean useDefaultScreenSceneMode() {
        return this.mCurrentClassification == 0;
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        this.mNeedConfirmPassword = true;
        super.onStop();
        EditorOriginHandler editorOriginHandler = this.mEditorOriginHandler;
        if (editorOriginHandler != null) {
            editorOriginHandler.onDestroy();
        }
    }

    @Override // com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 27) {
            if (i2 != -1) {
                setPhotoSecretFinishResult();
                finish();
            } else {
                this.mNeedConfirmPassword = false;
            }
        }
        super.onActivityResult(i, i2, intent);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        this.mResumed = false;
        super.onPause();
        BrightnessManager brightnessManager = this.mBrightnessManager;
        if (brightnessManager != null) {
            brightnessManager.onPause();
        }
        this.mSampler.recordPageEnd(this);
    }

    @Override // com.miui.gallery.app.activity.AndroidActivity, android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        BrightnessManager brightnessManager = this.mBrightnessManager;
        if (brightnessManager != null) {
            brightnessManager.onWindowFocusChanged(z);
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        disposeDisposable(this.mSkySceneDisposable);
        SimpleSelectDialog simpleSelectDialog = this.mSimpleSelectDialog;
        if (simpleSelectDialog != null) {
            simpleSelectDialog.dismissSafely();
            this.mSimpleSelectDialog = null;
        }
        DraftManager draftManager = this.mDraftManager;
        if (draftManager != null) {
            draftManager.release();
            this.mDraftManager = null;
        }
        ExportTask exportTask = this.mExportTask;
        if (exportTask != null) {
            exportTask.closeExportDialog();
        }
        VideoExportManager videoExportManager = this.mVideoExportManager;
        if (videoExportManager != null) {
            videoExportManager.release();
            this.mVideoExportManager = null;
        }
        VideoExportTask videoExportTask = this.mVideoExportTask;
        if (videoExportTask != null) {
            videoExportTask.closeExportDialog();
        }
        SkyTranFilter.getInstance().releaseSeqAsync();
    }

    @Override // androidx.fragment.app.FragmentActivity
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof MenuFragment) {
            ((MenuFragment) fragment).mCallbacks = this.mMenuCallback;
        } else if (fragment instanceof AbstractNaviFragment) {
            ((AbstractNaviFragment) fragment).mCallbacks = this.mNavigatorCallbacks;
        } else if (fragment instanceof PreviewFragment) {
            ((PreviewFragment) fragment).mCallbacks = this.mPreviewCallbacks;
        } else if (fragment instanceof ExportFragment) {
            ((ExportFragment) fragment).setCallbacks(this.mExportCallbacks);
        } else if (fragment instanceof AlertDialogFragment) {
            ((AlertDialogFragment) fragment).setCallbacks(this.mAlertDialogCallbacks);
        } else if (!(fragment instanceof VideoExportFragment)) {
        } else {
            ((VideoExportFragment) fragment).setCallbacks(this.mVideoExportCallback);
        }
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (isSingleEffectMode()) {
            this.mPreviewCallbacks.onDiscard();
            return;
        }
        disposeDisposable(this.mSkySceneDisposable);
        Fragment findFragmentById = this.mFragments.findFragmentById(R.id.menu_panel);
        if (findFragmentById instanceof AbstractNaviFragment) {
            DefaultLogger.d("PhotoEditor", "back pressed on navigator");
            DraftManager draftManager = this.mDraftManager;
            if (draftManager != null && !draftManager.isEmpty()) {
                DefaultLogger.d("PhotoEditor", "have pending operation");
                new AlertDialogFragment.Builder().setMessage(R.string.main_discard_confirm_message).setPositiveButton(R.string.main_discard_positive_btn).setNegativeButton(R.string.main_discard_negative_btn).setCancellable(true).build().showAllowingStateLoss(this.mFragments, "main_alert_dialog");
                return;
            }
        } else if (findFragmentById instanceof MenuFragment) {
            DefaultLogger.d("PhotoEditor", "back pressed on menu");
            MenuFragment menuFragment = (MenuFragment) findFragmentById;
            if (menuFragment.getRenderFragment() != null && !menuFragment.getRenderFragment().isEmpty()) {
                DefaultLogger.d("PhotoEditor", "menu has pending operation");
                new AlertDialogFragment.Builder().setMessage(R.string.sub_discard_confirm_message).setPositiveButton(R.string.sub_discard_positive_btn).setNegativeButton(R.string.sub_discard_negative_btn).setCancellable(true).build().showAllowingStateLoss(this.mFragments, "menu_alert_dialog");
                return;
            }
            onExit(menuFragment);
            return;
        }
        DefaultLogger.d("PhotoEditor", "neither handled back press event, just call default");
        onActivityFinish(false);
        super.onBackPressed();
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        return this.mSuspendInputs || super.dispatchTouchEvent(motionEvent);
    }

    @Override // androidx.core.app.ComponentActivity, android.app.Activity, android.view.Window.Callback
    @SuppressLint({"RestrictedApi"})
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return this.mSuspendInputs || super.dispatchKeyEvent(keyEvent);
    }

    public final void onActivityFinish(boolean z) {
        this.mTransitionConfig.onExit(z);
        Fragment findFragmentById = this.mFragments.findFragmentById(R.id.display_panel);
        if (findFragmentById instanceof PreviewFragment) {
            ((PreviewFragment) findFragmentById).onExit(z);
        }
        if (isNeedConfirmPassword()) {
            setPhotoSecretFinishResult();
        }
    }

    public void setActivityResult(int i, Intent intent) {
        this.mActivityResult = i;
        this.mActivityIntent = intent;
        prepareResult(intent);
        intent.putExtra("extra_photo_edit_type", "extra_photo_editor_type_common");
        setResult(this.mActivityResult, this.mActivityIntent);
    }

    public final void setPhotoSecretFinishResult() {
        if (this.mActivityIntent == null) {
            this.mActivityIntent = new Intent();
        }
        prepareResult(this.mActivityIntent);
        this.mActivityIntent.putExtra("photo_secret_finish", true);
        this.mActivityIntent.putExtra("extra_photo_edit_type", "extra_photo_editor_type_common");
        setResult(this.mActivityResult, this.mActivityIntent);
    }

    public final void prepareResult(Intent intent) {
        DraftManager draftManager = this.mDraftManager;
        if (draftManager != null) {
            intent.putExtra("photo_edit_exported_width", draftManager.getExportedWidth());
            intent.putExtra("photo_edit_exported_height", this.mDraftManager.getExportedHeight());
        }
    }

    public final boolean isNeedConfirmPassword() {
        DraftManager draftManager = this.mDraftManager;
        return draftManager != null && draftManager.isSecret() && this.mNeedConfirmPassword;
    }

    public final void setExposeButtonEnable(boolean z) {
        PreviewFragment previewFragment = (PreviewFragment) this.mFragments.findFragmentByTag("preview");
        if (previewFragment == null || !previewFragment.isAdded() || previewFragment.getView() == null) {
            return;
        }
        previewFragment.setExportEnabled(z);
    }

    public final void onEnterTransitionEnd() {
        this.mTransitionEnd = true;
        tryRefreshPreview();
        PreviewFragment previewFragment = (PreviewFragment) this.mFragments.findFragmentByTag("preview");
        if (previewFragment != null) {
            previewFragment.setRemoveWatermarkEnable(this.mDraftManager.isRemoveWatermarkEnable());
            previewFragment.playDownloadMediaEditorAppAnimation();
        }
        PhotoGuideViewManager photoGuideViewManager = this.mPhotoGuideViewManager;
        if (photoGuideViewManager != null) {
            photoGuideViewManager.tryShowFilterGuideView(this.mFragments);
        }
    }

    public final void tryRefreshPreview() {
        PreviewFragment previewFragment;
        if (!this.mPreviewRefreshedWhenInit || !this.mTransitionEnd || (previewFragment = (PreviewFragment) this.mFragments.findFragmentByTag("preview")) == null) {
            return;
        }
        previewFragment.refreshPreview(this.mDraftManager.getPreview());
    }

    public final void prepareNavigator() {
        ArrayList<Integer> resolveEffects = resolveEffects(getIntent().getExtras());
        this.mActivatedEffects = resolveEffects;
        Effect[] values = Effect.values();
        Iterator it = ((ArrayList) resolveEffects.clone()).iterator();
        while (it.hasNext()) {
            int intValue = ((Integer) it.next()).intValue();
            if (SdkManager.INSTANCE.getProvider(values[intValue]) == null) {
                DefaultLogger.d("PhotoEditor", "%s not supported, skip", values[intValue]);
                this.mActivatedEffects.remove(Integer.valueOf(intValue));
            }
        }
        showNavigator();
    }

    public final void showNavigator() {
        AbstractNaviFragment resolveNavigator = resolveNavigator(getIntent().getExtras());
        Bundle bundle = new Bundle();
        bundle.putIntegerArrayList(MiStat.Param.CONTENT, this.mActivatedEffects);
        resolveNavigator.setArguments(bundle);
        this.mFragments.beginTransaction().setTransition(8194).add(R.id.menu_panel, resolveNavigator, "navigator").commitAllowingStateLoss();
    }

    /* renamed from: showEditFragment */
    public void lambda$showSingleEffectDisplay$2(Effect effect) {
        boolean z;
        FragmentData fragmentData = this.mFragmentData[effect.ordinal()];
        RenderFragment renderFragment = (RenderFragment) this.mFragments.findFragmentByTag(fragmentData.renderTag);
        Fragment findFragmentByTag = this.mFragments.findFragmentByTag(fragmentData.gestureTag);
        boolean z2 = true;
        if (renderFragment != null) {
            this.mFragments.beginTransaction().show(renderFragment).commitAllowingStateLoss();
            z = true;
        } else {
            z = false;
        }
        if (findFragmentByTag != null) {
            this.mFragments.beginTransaction().show(findFragmentByTag).commitAllowingStateLoss();
        } else {
            z2 = z;
        }
        if (z2) {
            this.mFragments.executePendingTransactions();
        }
        Fragment findFragmentByTag2 = this.mFragments.findFragmentByTag("preview");
        if (findFragmentByTag2 != null) {
            this.mFragments.beginTransaction().detach(findFragmentByTag2).commitAllowingStateLoss();
            this.mFragments.executePendingTransactions();
        }
    }

    public final void startSkyScene(final Effect effect) {
        this.mSkySceneDisposable = Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.editor.photo.app.PhotoEditor$$ExternalSyntheticLambda2
            @Override // io.reactivex.ObservableOnSubscribe
            public final void subscribe(ObservableEmitter observableEmitter) {
                PhotoEditor.m740$r8$lambda$Bvf0nB23bNH1VZGyPywT9evyOY(PhotoEditor.this, effect, observableEmitter);
            }
        }).doOnSubscribe(new Consumer() { // from class: com.miui.gallery.editor.photo.app.PhotoEditor$$ExternalSyntheticLambda3
            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                PhotoEditor.$r8$lambda$8B2cOusTZbokTTrdwXGEcXWRqzg(PhotoEditor.this, (Disposable) obj);
            }
        }).subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() { // from class: com.miui.gallery.editor.photo.app.PhotoEditor$$ExternalSyntheticLambda4
            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                PhotoEditor.$r8$lambda$ZE6tUs3hqETShsOpgaY_KJjc0X8(PhotoEditor.this, effect, obj);
            }
        });
    }

    public /* synthetic */ void lambda$startSkyScene$3(Effect effect, ObservableEmitter observableEmitter) throws Exception {
        if (this.mDraftManager != null) {
            SkyTranFilter.getInstance().skyScene(this.mDraftManager.getPreview());
            observableEmitter.onNext(effect);
        }
    }

    public /* synthetic */ void lambda$startSkyScene$4(Disposable disposable) throws Exception {
        this.mIsNaviProcessing = true;
        this.mProgressBarHandler.showDelay(1000);
    }

    public /* synthetic */ void lambda$startSkyScene$5(Effect effect, Object obj) throws Exception {
        this.mProgressBarHandler.hide();
        this.mNavigatorCallbacks.postNavigate(effect);
        this.mIsNaviProcessing = false;
    }

    public static void disposeDisposable(Disposable disposable) {
        if (disposable == null || disposable.isDisposed()) {
            return;
        }
        disposable.dispose();
    }

    /* loaded from: classes2.dex */
    public class PhotoNaviCallback implements AbstractNaviFragment.Callbacks {
        public PhotoNaviCallback() {
            PhotoEditor.this = r1;
        }

        @Override // com.miui.gallery.editor.photo.app.AbstractNaviFragment.Callbacks
        public void onNavigate(Effect effect) {
            if (PhotoEditor.this.mDraftManager == null || !PhotoEditor.this.mDraftManager.isPreviewEnable() || PhotoEditor.this.mIsNaviProcessing) {
                DefaultLogger.w("PhotoEditor", "has not load preview when click");
            } else if (effect == Effect.SKY) {
                PhotoEditor.this.startSkyScene(effect);
            } else if (effect == Effect.BEAUTIFY2) {
                if (PhotoEditor.this.mIsBeautifyApplied) {
                    PhotoEditor.this.mDraftManager.enqueue(PhotoEditor.this.mReRenderCallback);
                    PhotoEditor photoEditor = PhotoEditor.this;
                    photoEditor.mIsBeautifyApplied = !photoEditor.mIsBeautifyApplied;
                    return;
                }
                Adjust2RenderData adjust2RenderData = new Adjust2RenderData(Arrays.asList(new Adjust2DataImpl(0)));
                adjust2RenderData.mType = effect;
                PhotoEditor.this.mDraftManager.enqueue(PhotoEditor.this.mReRenderCallback, adjust2RenderData);
                PhotoEditor photoEditor2 = PhotoEditor.this;
                photoEditor2.mIsBeautifyApplied = !photoEditor2.mIsBeautifyApplied;
            } else {
                postNavigate(effect);
            }
        }

        public final void sampleNavigate(Effect effect) {
            PhotoEditor.this.mSampler.recordEvent(PhotoEditor.this.mSampleTags[effect.ordinal()], "enter");
        }

        public final void postNavigate(Effect effect) {
            DefaultLogger.d("PhotoEditor", "click effect %s", effect.name());
            List<Fragment> fragments = PhotoEditor.this.mFragments.getFragments();
            if (fragments != null) {
                for (Fragment fragment : fragments) {
                    String tag = fragment.getTag();
                    if (tag != null && tag.contains(":menu")) {
                        return;
                    }
                }
            }
            if (SdkManager.INSTANCE.getProvider(effect).initialized()) {
                if (PhotoEditor.this.mFragments.getBackStackEntryCount() != 0) {
                    DefaultLogger.d("PhotoEditor", "last effect editor is still active");
                    return;
                }
                DefaultLogger.d("PhotoEditor", "navigate effect %s", effect);
                FragmentData fragmentData = PhotoEditor.this.mFragmentData[effect.ordinal()];
                RenderFragment renderFragment = (RenderFragment) PhotoEditor.this.mFragments.findFragmentByTag(fragmentData.renderTag);
                Fragment findFragmentByTag = PhotoEditor.this.mFragments.findFragmentByTag(fragmentData.gestureTag);
                MenuFragment newMenu = fragmentData.newMenu();
                boolean z = false;
                if (renderFragment == null) {
                    renderFragment = newMenu.createRenderFragment(fragmentData.titleResId);
                    PhotoEditor.this.mFragments.beginTransaction().detach(renderFragment).add(R.id.display_panel, renderFragment, fragmentData.renderTag).hide(renderFragment).commitAllowingStateLoss();
                    z = true;
                }
                if (!renderFragment.isSupportBitmap(PhotoEditor.this.mDraftManager.getPreview())) {
                    ToastUtils.makeText(PhotoEditor.this, renderFragment.getUnSupportStringRes());
                    return;
                }
                if (findFragmentByTag == null && (findFragmentByTag = newMenu.createGestureFragment()) != null) {
                    PhotoEditor.this.mFragments.beginTransaction().detach(findFragmentByTag).add(R.id.display_panel, findFragmentByTag, fragmentData.gestureTag).hide(findFragmentByTag).commitAllowingStateLoss();
                    z = true;
                }
                if (z) {
                    PhotoEditor.this.mFragments.executePendingTransactions();
                }
                Bundle bundle = new Bundle();
                PhotoEditor.this.mFragments.putFragment(bundle, "MenuFragment:display_fragment", renderFragment);
                if (findFragmentByTag != null) {
                    PhotoEditor.this.mFragments.putFragment(bundle, "MenuFragment:gesture_fragment", findFragmentByTag);
                }
                newMenu.setArguments(bundle);
                Fragment findFragmentByTag2 = PhotoEditor.this.mFragments.findFragmentByTag("navigator");
                if (findFragmentByTag2 != null) {
                    PhotoEditor.this.mFragments.beginTransaction().detach(findFragmentByTag2).commitAllowingStateLoss();
                    PhotoEditor.this.mFragments.executePendingTransactions();
                }
                PhotoEditor.this.mFragments.beginTransaction().add(R.id.menu_panel, newMenu, fragmentData.menuTag).commitAllowingStateLoss();
                PhotoEditor.this.mFragments.executePendingTransactions();
                if (renderFragment.isSupportAnimation()) {
                    Fragment findFragmentByTag3 = PhotoEditor.this.mFragments.findFragmentByTag("preview");
                    if (findFragmentByTag3 != null) {
                        PhotoEditor.this.mSuspendInputs = true;
                        ((PreviewFragment) findFragmentByTag3).prepareShowEditFragment(effect, new PreviewFragment.OnPrepareEditFragmentListener() { // from class: com.miui.gallery.editor.photo.app.PhotoEditor$PhotoNaviCallback$$ExternalSyntheticLambda0
                            @Override // com.miui.gallery.editor.photo.app.PreviewFragment.OnPrepareEditFragmentListener
                            public final void showEditFragment(Effect effect2) {
                                PhotoEditor.PhotoNaviCallback.this.lambda$postNavigate$0(effect2);
                            }
                        });
                    }
                } else {
                    PhotoEditor.this.lambda$showSingleEffectDisplay$2(effect);
                }
                sampleNavigate(effect);
                return;
            }
            DefaultLogger.w("PhotoEditor", "SdkProvider: %s has not initialized when click", effect.name());
        }

        public /* synthetic */ void lambda$postNavigate$0(Effect effect) {
            PhotoEditor.this.mSuspendInputs = false;
            PhotoEditor.this.lambda$showSingleEffectDisplay$2(effect);
        }
    }

    public final void onExit(MenuFragment menuFragment) {
        Fragment findFragmentByTag = this.mFragments.findFragmentByTag("preview");
        if (findFragmentByTag != null) {
            this.mFragments.beginTransaction().attach(findFragmentByTag).commitNowAllowingStateLoss();
        }
        FragmentTransaction beginTransaction = this.mFragments.beginTransaction();
        RenderFragment renderFragment = menuFragment.getRenderFragment();
        if (renderFragment != null) {
            beginTransaction.remove(renderFragment);
            if (renderFragment.isSupportAnimation() && findFragmentByTag != null) {
                ((PreviewFragment) findFragmentByTag).hideEditFragment();
            }
        }
        Fragment gestureFragment = menuFragment.getGestureFragment();
        if (gestureFragment != null) {
            beginTransaction.remove(gestureFragment);
        }
        beginTransaction.remove(menuFragment);
        Fragment findFragmentByTag2 = this.mFragments.findFragmentByTag("navigator");
        if (findFragmentByTag2 != null) {
            beginTransaction.attach(findFragmentByTag2);
        }
        beginTransaction.commitAllowingStateLoss();
    }

    public final void sampleExportTime(String str, long j) {
        HashMap hashMap = new HashMap();
        hashMap.put("cost_time", String.valueOf(j));
        this.mSampler.recordCategory("photo_editor_home_page", str, hashMap);
    }

    /* loaded from: classes2.dex */
    public static class FragmentData {
        public final String gestureTag;
        public final Class<? extends MenuFragment> menu;
        public final String menuTag;
        public final String renderTag;
        public final int titleResId;

        public FragmentData(Class<? extends MenuFragment> cls, Effect effect, int i) {
            this.menu = cls;
            this.menuTag = effect.name() + ":menu";
            this.renderTag = effect.name() + ":render";
            this.gestureTag = effect.name() + ":gesture";
            this.titleResId = i;
        }

        public MenuFragment newMenu() {
            try {
                return this.menu.newInstance();
            } catch (IllegalAccessException e) {
                DefaultLogger.w("PhotoEditor", e);
                throw new IllegalStateException(e);
            } catch (InstantiationException e2) {
                DefaultLogger.w("PhotoEditor", e2);
                throw new IllegalStateException(e2);
            }
        }
    }

    public final MenuFragment findActiveMenu() {
        Fragment findFragmentById = this.mFragments.findFragmentById(R.id.menu_panel);
        if (findFragmentById != null && findFragmentById.isAdded()) {
            if (findFragmentById instanceof MenuFragment) {
                return (MenuFragment) findFragmentById;
            }
            DefaultLogger.w("PhotoEditor", "not menu in menu panel: %s", findFragmentById);
        }
        DefaultLogger.w("PhotoEditor", "no active menu fragment found");
        return null;
    }

    public final ArrayList<Integer> resolveEffects(Bundle bundle) {
        if (bundle != null && bundle.getBoolean(Constants.EXTRA_IS_SCREENSHOT)) {
            if (bundle.getBoolean(Constants.EXTRA_IS_LONG_SCREENSHOT)) {
                return new ArrayList<>(Arrays.asList(Integer.valueOf(Effect.LONG_CROP.ordinal()), Integer.valueOf(Effect.DOODLE.ordinal()), Integer.valueOf(Effect.MOSAIC.ordinal())));
            }
            return new ArrayList<>(Arrays.asList(Integer.valueOf(Effect.CROP.ordinal()), Integer.valueOf(Effect.DOODLE.ordinal()), Integer.valueOf(Effect.TEXT.ordinal()), Integer.valueOf(Effect.MOSAIC.ordinal())));
        }
        Effect<AbstractStickerProvider> effect = Effect.STICKER;
        ArrayList<Integer> arrayList = new ArrayList<>(Arrays.asList(Integer.valueOf(Effect.CROP.ordinal()), Integer.valueOf(Effect.FILTER.ordinal()), Integer.valueOf(effect.ordinal()), Integer.valueOf(Effect.DOODLE.ordinal()), Integer.valueOf(Effect.TEXT.ordinal()), Integer.valueOf(Effect.FRAME.ordinal()), Integer.valueOf(Effect.MOSAIC.ordinal())));
        if (!BuildUtil.isEditorProcess()) {
            arrayList.add(0, Integer.valueOf(Effect.BEAUTIFY2.ordinal()));
            arrayList.add(3, Integer.valueOf(Effect.ADJUST2.ordinal()));
        }
        if (SkyCheckHelper.isSkyEnable()) {
            arrayList.add(arrayList.indexOf(Integer.valueOf(effect.ordinal())), Integer.valueOf(Effect.SKY.ordinal()));
        }
        int indexOf = arrayList.indexOf(Integer.valueOf(effect.ordinal()));
        if (Remover2CheckHelper.isRemover2Support()) {
            arrayList.add(indexOf, Integer.valueOf(Effect.REMOVER2.ordinal()));
        } else if (Inpaint.isAvailable()) {
            arrayList.add(indexOf, Integer.valueOf(Effect.REMOVER.ordinal()));
        }
        return arrayList;
    }

    public final AbstractNaviFragment resolveNavigator(Bundle bundle) {
        if (bundle != null && bundle.getBoolean(Constants.EXTRA_IS_SCREENSHOT)) {
            return new ScreenshotNaviFragment();
        }
        PhotoNaviFragment photoNaviFragment = new PhotoNaviFragment();
        photoNaviFragment.setListener(this.mOnPrepareListener);
        return photoNaviFragment;
    }

    @Override // android.app.Activity
    public void finishAfterTransition() {
        if (this.mTransitionConfig.mRunTransition) {
            super.finishAfterTransition();
        } else {
            finish();
        }
    }

    /* loaded from: classes2.dex */
    public static class TransitionConfiguration {
        public PhotoEditor mEditor;
        public int mEnterDuration;
        public int mExitDuration;
        public int mImageHeight;
        public int mImageWidth;
        public Matrix mMatrix;
        public int mMenuOffset;
        public String mPhotoViewName;
        public boolean mRunTransition;
        public Transition.TransitionListener mTransitionListener = new Transition.TransitionListener() { // from class: com.miui.gallery.editor.photo.app.PhotoEditor.TransitionConfiguration.2
            @Override // android.transition.Transition.TransitionListener
            public void onTransitionPause(Transition transition) {
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionResume(Transition transition) {
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionStart(Transition transition) {
            }

            {
                TransitionConfiguration.this = this;
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionEnd(Transition transition) {
                TransitionConfiguration.this.mEditor.onEnterTransitionEnd();
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionCancel(Transition transition) {
                TransitionConfiguration.this.mEditor.onEnterTransitionEnd();
            }
        };

        public TransitionConfiguration(PhotoEditor photoEditor) {
            this.mEditor = photoEditor;
        }

        public void onActivityCreate(boolean z) {
            this.mRunTransition = com.miui.gallery.compat.view.WindowCompat.requestActivityTransition(this.mEditor.getWindow()) && this.mEditor.getIntent().getBooleanExtra("extra_custom_transition", false) && !z;
            SystemUiUtil.setDrawSystemBarBackground(this.mEditor.getWindow());
            if (this.mEditor.isSingleEffectMode()) {
                this.mRunTransition = false;
            }
        }

        public void postActivityCreate() {
            if (!this.mRunTransition) {
                return;
            }
            Intent intent = this.mEditor.getIntent();
            this.mImageWidth = intent.getIntExtra("extra_image_width", 0);
            this.mImageHeight = intent.getIntExtra("extra_image_height", 0);
            float[] floatArrayExtra = intent.getFloatArrayExtra("extra_image_matrix");
            Matrix matrix = new Matrix();
            this.mMatrix = matrix;
            matrix.setValues(floatArrayExtra);
            Resources resources = this.mEditor.getResources();
            this.mMenuOffset = resources.getDimensionPixelOffset(R.dimen.photo_editor_transition_menu_offset);
            this.mPhotoViewName = resources.getString(R.string.photo_editor_transition_image_view);
            this.mEnterDuration = resources.getInteger(R.integer.photo_editor_enter_transition_duration);
            this.mExitDuration = resources.getInteger(R.integer.photo_editor_exit_transition_duration);
            configureEnterTransition();
            ActivityCompat.postponeEnterTransition(this.mEditor);
        }

        public void onImageLoaded() {
            if (!this.mRunTransition) {
                this.mEditor.onEnterTransitionEnd();
            } else {
                ActivityCompat.startPostponedEnterTransition(this.mEditor);
            }
        }

        public void onExit(boolean z) {
            PreviewFragment previewFragment = (PreviewFragment) this.mEditor.mFragments.findFragmentByTag("preview");
            boolean z2 = false;
            if (previewFragment != null) {
                previewFragment.setRemoveWatermarkEnable(false);
            }
            if (OrientationCheckHelper.isSupportOrientationChange()) {
                ImageView photoView = EditorMiscHelper.getPhotoView();
                if (photoView != null) {
                    z2 = true;
                }
                this.mRunTransition = z2;
                if (z2) {
                    this.mMatrix = photoView.getImageMatrix();
                    Drawable drawable = photoView.getDrawable();
                    if (drawable != null) {
                        this.mImageWidth = drawable.getIntrinsicWidth();
                        this.mImageHeight = drawable.getIntrinsicHeight();
                    }
                }
            }
            if (!this.mRunTransition) {
                this.mEditor.mExportTask.closeExportDialog();
                this.mEditor.mVideoExportTask.closeExportDialog();
                return;
            }
            configureExitTransition(z);
            ActivityCompat.setEnterSharedElementCallback(this.mEditor, new ActivityCompat.SharedElementCallback() { // from class: com.miui.gallery.editor.photo.app.PhotoEditor.TransitionConfiguration.1
                {
                    TransitionConfiguration.this = this;
                }

                @Override // com.miui.gallery.compat.app.ActivityCompat.SharedElementCallback
                public void onSharedElementStart() {
                    TransitionConfiguration.this.mEditor.mExportTask.closeExportDialog();
                    TransitionConfiguration.this.mEditor.mVideoExportTask.closeExportDialog();
                }
            });
        }

        public final void configureEnterTransition() {
            TransitionSet transitionSet = new TransitionSet();
            ImageTransition imageTransition = new ImageTransition(true, this.mMatrix, this.mImageWidth, this.mImageHeight);
            imageTransition.setInterpolator(new CubicEaseInOutInterpolator());
            TransitionCompat.addTarget(imageTransition, this.mPhotoViewName);
            MenuTransition menuTransition = new MenuTransition(BaseMiscUtil.isRTLDirection() ? -this.mMenuOffset : this.mMenuOffset, true, this.mEditor.getResources());
            menuTransition.setInterpolator(new CubicEaseInOutInterpolator());
            menuTransition.addTarget(R.id.menu_panel);
            transitionSet.addTransition(imageTransition).addTransition(menuTransition).setDuration(this.mEnterDuration).addListener(this.mTransitionListener);
            com.miui.gallery.compat.view.WindowCompat.setSharedElementEnterTransition(this.mEditor.getWindow(), transitionSet);
        }

        public final void configureExitTransition(boolean z) {
            TransitionSet transitionSet = new TransitionSet();
            Transition changeBounds = z ? new ChangeBounds() : new ImageTransition(false, this.mMatrix, this.mImageWidth, this.mImageHeight);
            changeBounds.setInterpolator(new CubicEaseOutInterpolator());
            changeBounds.setDuration(this.mExitDuration);
            TransitionCompat.addTarget(changeBounds, this.mPhotoViewName);
            MenuTransition menuTransition = new MenuTransition(BaseMiscUtil.isRTLDirection() ? -this.mMenuOffset : this.mMenuOffset, false, this.mEditor.getResources());
            menuTransition.setInterpolator(new CubicEaseOutInterpolator());
            menuTransition.addTarget(R.id.menu_panel);
            transitionSet.addTransition(changeBounds).addTransition(menuTransition);
            com.miui.gallery.compat.view.WindowCompat.setSharedElementReturnTransition(this.mEditor.getWindow(), transitionSet);
        }
    }

    /* loaded from: classes2.dex */
    public static class PhotoGuideViewManager {
        public int mTypeResId;

        public final boolean isShowPortraitGuideView() {
            return false;
        }

        public final boolean isShowSkyGuideView() {
            return false;
        }

        public PhotoGuideViewManager() {
        }

        public void init() {
            if (isShowPortraitGuideView()) {
                this.mTypeResId = R.string.filter_portrait_hint;
            } else if (isShowSkyGuideView()) {
                this.mTypeResId = R.string.filter_sky_hint;
            } else {
                this.mTypeResId = -1;
            }
        }

        public void tryShowFilterGuideView(FragmentManager fragmentManager) {
            if (this.mTypeResId == -1) {
                return;
            }
            Fragment findFragmentByTag = fragmentManager.findFragmentByTag("navigator");
            if (!(findFragmentByTag instanceof PhotoNaviFragment)) {
                return;
            }
            ((PhotoNaviFragment) findFragmentByTag).showItemGuideView(Effect.FILTER, 16, this.mTypeResId);
            int i = this.mTypeResId;
            if (i == R.string.filter_portrait_hint) {
                GalleryPreferences.PhotoFilterPortraitColorGuide.setPhotoFilterPortraitColorGuided(true);
            } else if (i != R.string.filter_sky_hint) {
            } else {
                GalleryPreferences.PhotoFilterSkyGuide.setPhotoFilterSkyGuided(true);
            }
        }
    }
}
