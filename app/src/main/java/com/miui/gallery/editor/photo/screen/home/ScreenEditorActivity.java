package com.miui.gallery.editor.photo.screen.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import com.android.internal.WindowCompat;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.agreement.core.OnAgreementInvokedListener;
import com.miui.gallery.app.StrategyContext;
import com.miui.gallery.app.activity.GalleryActivity;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureHandler;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.editor.photo.app.DraftManager;
import com.miui.gallery.editor.photo.app.ExportFragment;
import com.miui.gallery.editor.photo.app.ExportTask;
import com.miui.gallery.editor.photo.app.InitializeController;
import com.miui.gallery.editor.photo.core.SdkManager;
import com.miui.gallery.editor.photo.core.imports.doodle.DoodleManager;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.paintbrush.DoodlePen;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.paintbrush.DoodlePenManager;
import com.miui.gallery.editor.photo.penengine.Utils;
import com.miui.gallery.editor.photo.penengine.entity.Eraser;
import com.miui.gallery.editor.photo.penengine.entity.Mark;
import com.miui.gallery.editor.photo.penengine.entity.Mosaic;
import com.miui.gallery.editor.photo.penengine.entity.Pen;
import com.miui.gallery.editor.photo.penengine.entity.Shape;
import com.miui.gallery.editor.photo.penengine.entity.Text;
import com.miui.gallery.editor.photo.screen.base.ScreenRenderCallback;
import com.miui.gallery.editor.photo.screen.core.ScreenProviderManager;
import com.miui.gallery.editor.photo.screen.core.ScreenRenderData;
import com.miui.gallery.editor.photo.screen.core.ScreenRenderManager;
import com.miui.gallery.editor.photo.screen.crop.IScreenCropOperation;
import com.miui.gallery.editor.photo.screen.doodle.IScreenDoodleOperation;
import com.miui.gallery.editor.photo.screen.entity.ScreenNavigatorData;
import com.miui.gallery.editor.photo.screen.helper.ScreenEditorHelper;
import com.miui.gallery.editor.photo.screen.home.ScreenAnimatorHelper;
import com.miui.gallery.editor.photo.screen.home.ScreenDeleteDialogFragment;
import com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity;
import com.miui.gallery.editor.photo.screen.home.ScreenNavFragment;
import com.miui.gallery.editor.photo.screen.home.ScreenShareView;
import com.miui.gallery.editor.photo.screen.longcrop.ILongCropEditorController;
import com.miui.gallery.editor.photo.screen.shell.IScreenShellOperation;
import com.miui.gallery.editor.photo.screen.shell.res.ShellResourceFetcher;
import com.miui.gallery.editor.photo.screen.stat.ScreenEditorStatUtils;
import com.miui.gallery.listener.SingleClickListener;
import com.miui.gallery.net.fetch.Request;
import com.miui.gallery.permission.core.Permission;
import com.miui.gallery.permission.core.PermissionCheckCallback;
import com.miui.gallery.permission.core.PermissionInjector;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryOpenProvider;
import com.miui.gallery.scanner.core.ScannerEngine;
import com.miui.gallery.sdk.editor.Constants;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.ui.ChooserFragment;
import com.miui.gallery.ui.DeletionTask;
import com.miui.gallery.ui.ProcessTask;
import com.miui.gallery.util.BackgroundServiceHelper;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.OrientationCheckHelper;
import com.miui.gallery.util.ParcelUtils;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.SecurityShareHelper;
import com.miui.gallery.util.SystemUiUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.UriUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.market.MediaEditorInstaller;
import com.miui.gallery.widget.DebounceClickListener;
import com.miui.mediaeditor.utils.FilePermissionUtils;
import com.miui.mediaeditor.utils.MediaEditorUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import miuix.appcompat.app.ProgressDialog;

/* loaded from: classes2.dex */
public class ScreenEditorActivity extends GalleryActivity implements PermissionCheckCallback {
    public GalleryActivity mActivity;
    public ImageView mBackView;
    public FrameLayout mBottomLayoutView;
    public Fragment mChooserFragment;
    public Guideline mCommonLine;
    public Fragment mCurrentBottomFragment;
    public boolean mCurrentHasCrop;
    public ImageView mDeleteIv;
    public Disposable mDeleteSourceAndExportDisposable;
    public DraftManager mDraftManager;
    public Guideline mEditBottomLine;
    public Guideline mEditTopLine;
    public FrameLayout mEditViewLayoutView;
    public ExportTask mExportTask;
    public Handler mHandler;
    public boolean mHasExport;
    public InitializeController mInitializeController;
    public boolean mIsClickShare;
    public boolean mIsDecoderDone;
    public volatile boolean mIsDeleteLocalAndCloudExecuted;
    public boolean mIsFromLongScreen;
    public boolean mIsFromSendMode;
    public boolean mIsLongCropShow;
    public boolean mIsLongScreenMode;
    public boolean mIsMoveScreenFinished;
    public boolean mIsNoTranslucentActivity;
    public boolean mIsQuitThumbnail;
    public boolean mIsShareViewAlreadyLayout;
    public boolean mIsShared;
    public ILongCropEditorController mLongCropEditorController;
    public View mLongCropLayout;
    public Guideline mMenuTopLine;
    public MoveScreenshotTask mMoveScreenshotTask;
    public Fragment mNavFragment;
    public View mNavigationPlaceHolder;
    public ChooserFragment.OnFilesProcessedListener mOnFilesProcessedListener;
    public int mPageMode;
    public ProgressDialog mProgressDialog;
    public ResultReceiver mQuitThumbnailReceiver;
    public ImageView mRedoIv;
    public ImageView mSaveIv;
    public ScreenAnimatorHelper mScreenAnimatorHelper;
    public ScreenDeleteDialogFragment mScreenDeleteDialogFragment;
    public ScreenEditorFragment mScreenEditorFragment;
    public IScreenEditorController mScreenEditorListener;
    public ScreenRenderManager mScreenRenderManager;
    public FrameLayout mScreenRootBg;
    public ScreenShareView mScreenShareView;
    public ScreenShellExecutor mScreenShellExecutor;
    public IScreenShellOperation mScreenShellOperation;
    public Guideline mShareBottomLine;
    public ImageView mShareIv;
    public FrameLayout mShareLayoutView;
    public Intent mSharePendingIntent;
    public ConstraintLayout mShareTopLayoutView;
    public Guideline mShareTopLine;
    public ImageView mShellImageView;
    public ViewGroup mShellLayoutView;
    public TextView mShellTextViewOff;
    public TextView mShellTextViewOn;
    public ShowLoadRunnable mShowLoadRunnable;
    public View mTopLayout;
    public FrameLayout mTopLayoutContainer;
    public ImageView mUndoIv;
    public boolean mNeedExported = true;
    public boolean mIsAllowClick = true;
    public MediaEditorInstaller.Callback mMediaEditorInstallerCallback = new MediaEditorInstaller.Callback() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity.1
        {
            ScreenEditorActivity.this = this;
        }

        @Override // com.miui.gallery.util.market.MediaEditorInstaller.Callback
        public void onDialogConfirm() {
            ScreenEditorActivity.this.quitThumbnail();
            ScreenEditorActivity.this.finish();
        }

        @Override // com.miui.gallery.util.market.MediaEditorInstaller.Callback
        public void onDialogCancel() {
            ScreenEditorActivity.this.quitThumbnail();
            ScreenEditorActivity.this.finish();
        }
    };
    public View.OnClickListener mOnClickListener = new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity.5
        {
            ScreenEditorActivity.this = this;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            ScreenEditorActivity.this.mScreenShellExecutor.mDoShellInterrupt = true;
            int id = view.getId();
            if (id == R.id.action_back) {
                if (!ScreenEditorActivity.this.isFromNormalShare()) {
                    ScreenEditorActivity.this.switchPageMode(0);
                } else {
                    ScreenEditorActivity.this.finish();
                }
            } else if (id == R.id.redo_iv) {
                ScreenEditorActivity.this.handleRedoClickEvent();
            } else if (id != R.id.undo_iv) {
            } else {
                ScreenEditorActivity.this.handleUndoClickEvent();
            }
        }
    };
    public DebounceClickListener mAntiDoubleClickListener = new DebounceClickListener() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity.6
        {
            ScreenEditorActivity.this = this;
        }

        @Override // com.miui.gallery.widget.DebounceClickListener
        public void onClickConfirmed(View view) {
            if (view.getId() != R.id.screen_editor_shell_layout) {
                return;
            }
            if (!ScreenEditorActivity.this.mCurrentHasCrop) {
                ScreenEditorActivity.this.mScreenShellExecutor.doShell(ScreenEditorActivity.this);
            } else {
                ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.screen_editor_shell_disallowed_with_crop);
            }
        }
    };
    public ScreenShareView.OnClickShareViewListener mOnClickShareViewListener = new ScreenShareView.OnClickShareViewListener() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity$$ExternalSyntheticLambda6
        @Override // com.miui.gallery.editor.photo.screen.home.ScreenShareView.OnClickShareViewListener
        public final void onClick() {
            ScreenEditorActivity.$r8$lambda$9i0ZuJQhnZZDI2BlrebZpFFJN20(ScreenEditorActivity.this);
        }
    };
    public SingleClickListener mSingleClickListener = new SingleClickListener() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity.7
        {
            ScreenEditorActivity.this = this;
        }

        @Override // com.miui.gallery.listener.SingleClickListener
        public void onSingleClick(View view) {
            ScreenEditorActivity.this.mScreenShellExecutor.mDoShellInterrupt = true;
            int id = view.getId();
            if (id == R.id.delete_iv) {
                if (ScreenEditorActivity.this.mScreenDeleteDialogFragment == null) {
                    ScreenEditorActivity.this.mScreenDeleteDialogFragment = new ScreenDeleteDialogFragment();
                }
                ScreenEditorActivity.this.mScreenDeleteDialogFragment.setDialogClickListener(ScreenEditorActivity.this.mDeleteDialogClickListener);
                ScreenEditorActivity.this.mScreenDeleteDialogFragment.showAllowingStateLoss(ScreenEditorActivity.this.getSupportFragmentManager(), "ScreenDeleteDialogFragment");
            } else if (id == R.id.save_iv) {
                ScreenEditorActivity.this.handleSaveClickEvent();
            } else if (id != R.id.share_iv) {
            } else {
                ScreenEditorActivity.this.handleShareClickEvent();
            }
        }
    };
    public ScreenDeleteDialogFragment.DialogClickListener mDeleteDialogClickListener = new ScreenDeleteDialogFragment.DialogClickListener() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity.8
        {
            ScreenEditorActivity.this = this;
        }

        @Override // com.miui.gallery.editor.photo.screen.home.ScreenDeleteDialogFragment.DialogClickListener
        public void onDelete() {
            ScreenEditorActivity.this.deleteFileWithJudgeHasExport();
            ScreenEditorStatUtils.statBtnDeletelClick(ScreenEditorActivity.this.mIsLongScreenMode);
        }
    };
    public final ProcessTask.OnCompleteListener<long[]> mCompleteListener = new ProcessTask.OnCompleteListener() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity$$ExternalSyntheticLambda7
        @Override // com.miui.gallery.ui.ProcessTask.OnCompleteListener
        public final void onCompleteProcess(Object obj) {
            ScreenEditorActivity.$r8$lambda$vRynr6ElHy7pzYOfEOyYp1mouE0(ScreenEditorActivity.this, (long[]) obj);
        }
    };
    public OnScreenCropStatusChangeListener mOnScreenCropStatusChangeListener = new OnScreenCropStatusChangeListener() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity$$ExternalSyntheticLambda3
        @Override // com.miui.gallery.editor.photo.screen.home.OnScreenCropStatusChangeListener
        public final void onChanged(boolean z) {
            ScreenEditorActivity.$r8$lambda$kEih_prxCjgWgZy_PazvpHQAY5E(ScreenEditorActivity.this, z);
        }
    };
    public InitializeController.Callbacks mDecoderCallbacks = new InitializeController.Callbacks() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity.10
        {
            ScreenEditorActivity.this = this;
        }

        @Override // com.miui.gallery.editor.photo.app.InitializeController.Callbacks
        public int doInitialize() {
            SdkManager sdkManager = SdkManager.INSTANCE;
            sdkManager.onAttach(ScreenEditorActivity.this.getApplication());
            sdkManager.onActivityCreate();
            try {
                return ScreenEditorActivity.this.mDraftManager.initializeForPreview(false) ? 3 : 2;
            } catch (FileNotFoundException e) {
                DefaultLogger.w("ScreenEditorActivity_", e);
                return 1;
            } catch (SecurityException e2) {
                DefaultLogger.w("ScreenEditorActivity_", e2);
                return 2;
            }
        }

        @Override // com.miui.gallery.editor.photo.app.InitializeController.Callbacks
        public void onDone() {
            ScreenEditorActivity.this.mIsDecoderDone = true;
            ScreenEditorActivity.this.mScreenEditorListener.setPreviewBitmap(ScreenEditorActivity.this.mDraftManager.getPreview());
            ScreenEditorActivity.this.mScreenShareView.setShareBitmap(ScreenEditorActivity.this.mDraftManager.getPreview(), false);
            ScreenEditorActivity.this.doAnimators();
        }
    };
    public ExportFragment.Callbacks mExportCallbacks = new ExportFragment.Callbacks() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity.11
        public long mStartTime;

        {
            ScreenEditorActivity.this = this;
        }

        @Override // com.miui.gallery.editor.photo.app.ExportFragment.Callbacks
        public boolean doExport() {
            DefaultLogger.w("ScreenEditorActivity_", "[Export] start");
            this.mStartTime = System.currentTimeMillis();
            ScreenEditorActivity.this.mExportTask.prepareToExport(ScreenEditorActivity.this.mDraftManager);
            boolean export = ScreenEditorActivity.this.mDraftManager.export(ScreenEditorActivity.this.mScreenRenderManager.getRenderBitmap(), ScreenEditorActivity.this.mExportTask.getExportUri());
            DefaultLogger.w("ScreenEditorActivity_", "[Export] decode + render + encode, time: %d", Long.valueOf(System.currentTimeMillis() - this.mStartTime));
            this.mStartTime = System.currentTimeMillis();
            boolean onExport = ScreenEditorActivity.this.mExportTask.onExport(ScreenEditorActivity.this.mDraftManager, export);
            DefaultLogger.w("ScreenEditorActivity_", "[Export] update database end, time: %d", Long.valueOf(System.currentTimeMillis() - this.mStartTime));
            return onExport;
        }

        @Override // com.miui.gallery.editor.photo.app.ExportFragment.Callbacks
        public void onCancelled(boolean z) {
            ScreenEditorActivity.this.mExportTask.onCancelled(z);
        }

        @Override // com.miui.gallery.editor.photo.app.ExportFragment.Callbacks
        public void onExported(boolean z) {
            ScreenEditorActivity.this.mExportTask.onPostExport(z);
            ScreenEditorActivity.this.mExportTask.closeExportDialog();
            if (z) {
                ScreenEditorActivity.this.mHasExport = true;
                ScreenEditorActivity.this.mNeedExported = false;
                if (!ScreenEditorActivity.this.isSharePage()) {
                    ScreenEditorActivity.this.deleteSourceAndExportAndFinish(true, false, false, false);
                    return;
                }
                ScreenEditorActivity screenEditorActivity = ScreenEditorActivity.this;
                screenEditorActivity.parseUriAndShare(screenEditorActivity.mExportTask.getExportUri());
                return;
            }
            ToastUtils.makeText(ScreenEditorActivity.this, (int) R.string.main_save_error_msg);
        }
    };
    public ScreenAnimatorHelper.AnimatorViewCallback mAnimatorViewCallback = new ScreenAnimatorHelper.AnimatorViewCallback() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity.12
        {
            ScreenEditorActivity.this = this;
        }

        @Override // com.miui.gallery.editor.photo.screen.home.ScreenAnimatorHelper.AnimatorViewCallback
        public View getBottomLayoutView() {
            return ScreenEditorActivity.this.mBottomLayoutView;
        }

        @Override // com.miui.gallery.editor.photo.screen.home.ScreenAnimatorHelper.AnimatorViewCallback
        public View getEditTopView() {
            return ScreenEditorActivity.this.mTopLayoutContainer;
        }

        @Override // com.miui.gallery.editor.photo.screen.home.ScreenAnimatorHelper.AnimatorViewCallback
        public View getShareTopView() {
            return ScreenEditorActivity.this.mShareTopLayoutView;
        }

        @Override // com.miui.gallery.editor.photo.screen.home.ScreenAnimatorHelper.AnimatorViewCallback
        public View getScreenEditorBgView() {
            return ScreenEditorActivity.this.mScreenRootBg;
        }

        @Override // com.miui.gallery.editor.photo.screen.home.ScreenAnimatorHelper.AnimatorViewCallback
        public Guideline getShareBottomLine() {
            return ScreenEditorActivity.this.mShareBottomLine;
        }

        @Override // com.miui.gallery.editor.photo.screen.home.ScreenAnimatorHelper.AnimatorViewCallback
        public Guideline getShareTopLine() {
            return ScreenEditorActivity.this.mShareTopLine;
        }

        @Override // com.miui.gallery.editor.photo.screen.home.ScreenAnimatorHelper.AnimatorViewCallback
        public Guideline getCommonChangeLine() {
            return ScreenEditorActivity.this.mCommonLine;
        }

        @Override // com.miui.gallery.editor.photo.screen.home.ScreenAnimatorHelper.AnimatorViewCallback
        public Guideline getEditBottomLine() {
            return ScreenEditorActivity.this.mEditBottomLine;
        }

        @Override // com.miui.gallery.editor.photo.screen.home.ScreenAnimatorHelper.AnimatorViewCallback
        public Guideline getEditTopLine() {
            return ScreenEditorActivity.this.mEditTopLine;
        }
    };
    public ScreenNavFragment.Callback mScreenEditBottomCallback = new ScreenNavFragment.Callback() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity.13
        {
            ScreenEditorActivity.this = this;
        }

        @Override // com.miui.gallery.editor.photo.screen.home.ScreenNavFragment.Callback
        public boolean onPenSelect(Pen pen) {
            if (ScreenEditorActivity.this.mLongCropEditorController != null) {
                ScreenEditorActivity.this.mScreenEditorListener.setLongCropEntry(ScreenEditorActivity.this.mLongCropEditorController.onExport());
                ScreenEditorActivity.this.mIsLongCropShow = false;
            }
            ScreenEditorStatUtils.statNavItemClick(ScreenEditorActivity.this.mIsLongScreenMode, 9, false);
            DoodlePen normal = DoodlePenManager.INSTANCE.getNormal();
            normal.setSize(pen.getSize());
            normal.setAlpha(pen.getAlpha());
            normal.setColorInt(pen.getColor());
            ScreenEditorActivity.this.mScreenEditorListener.setDoodlePen(normal);
            boolean currentScreenEditor = ScreenEditorActivity.this.mScreenEditorListener.setCurrentScreenEditor(9);
            ((IScreenDoodleOperation) ScreenEditorActivity.this.mScreenEditorFragment.getScreenOperation(IScreenDoodleOperation.class)).setDoodleData(DoodleManager.getScreenDoodlePenPathData(), 0);
            ScreenEditorActivity.this.configEditModeView();
            return currentScreenEditor;
        }

        @Override // com.miui.gallery.editor.photo.screen.home.ScreenNavFragment.Callback
        public boolean onMarkSelect(Mark mark) {
            ScreenEditorStatUtils.statNavItemClick(ScreenEditorActivity.this.mIsLongScreenMode, 10, false);
            if (ScreenEditorActivity.this.mLongCropEditorController != null) {
                ScreenEditorActivity.this.mScreenEditorListener.setLongCropEntry(ScreenEditorActivity.this.mLongCropEditorController.onExport());
                ScreenEditorActivity.this.mIsLongCropShow = false;
            }
            ScreenEditorStatUtils.statNavItemClick(ScreenEditorActivity.this.mIsLongScreenMode, 10, false);
            DoodlePen markPen = DoodlePenManager.INSTANCE.getMarkPen();
            markPen.setSize(mark.getSize());
            markPen.setAlpha(mark.getAlpha());
            markPen.setColorInt(mark.getColor());
            ScreenEditorActivity.this.mScreenEditorListener.setDoodlePen(markPen);
            boolean currentScreenEditor = ScreenEditorActivity.this.mScreenEditorListener.setCurrentScreenEditor(10);
            ((IScreenDoodleOperation) ScreenEditorActivity.this.mScreenEditorFragment.getScreenOperation(IScreenDoodleOperation.class)).setDoodleData(DoodleManager.getScreenDoodlePenPathData(), 0);
            ScreenEditorActivity.this.configEditModeView();
            return currentScreenEditor;
        }

        @Override // com.miui.gallery.editor.photo.screen.home.ScreenNavFragment.Callback
        public boolean onMosaicSelect(Mosaic mosaic, ScreenNavigatorData screenNavigatorData) {
            if (ScreenEditorActivity.this.mLongCropEditorController != null) {
                ScreenEditorActivity.this.mScreenEditorListener.setLongCropEntry(ScreenEditorActivity.this.mLongCropEditorController.onExport());
                ScreenEditorActivity.this.mIsLongCropShow = false;
            }
            ScreenEditorStatUtils.statNavItemClick(ScreenEditorActivity.this.mIsLongScreenMode, screenNavigatorData, false);
            boolean currentScreenEditor = ScreenEditorActivity.this.mScreenEditorListener.setCurrentScreenEditor(4);
            ScreenEditorActivity.this.configEditModeView();
            return currentScreenEditor;
        }

        @Override // com.miui.gallery.editor.photo.screen.home.ScreenNavFragment.Callback
        public boolean onTextSelect(Text text) {
            DefaultLogger.d("ScreenEditorActivity_", "onTextSelect: ");
            if (ScreenEditorActivity.this.mLongCropEditorController != null) {
                ScreenEditorActivity.this.mScreenEditorListener.setLongCropEntry(ScreenEditorActivity.this.mLongCropEditorController.onExport());
                ScreenEditorActivity.this.mIsLongCropShow = false;
            }
            boolean currentScreenEditor = ScreenEditorActivity.this.mScreenEditorListener.setCurrentScreenEditor(3);
            ScreenEditorStatUtils.statNavItemClick(ScreenEditorActivity.this.mIsLongScreenMode, 3, false);
            ScreenEditorActivity.this.configEditModeView();
            return currentScreenEditor;
        }

        @Override // com.miui.gallery.editor.photo.screen.home.ScreenNavFragment.Callback
        public boolean onShapeSelect(Shape shape) {
            DefaultLogger.d("ScreenEditorActivity_", "onShapeSelect: ");
            if (ScreenEditorActivity.this.mLongCropEditorController != null) {
                ScreenEditorActivity.this.mScreenEditorListener.setLongCropEntry(ScreenEditorActivity.this.mLongCropEditorController.onExport());
                ScreenEditorActivity.this.mIsLongCropShow = false;
            }
            boolean currentScreenEditor = ScreenEditorActivity.this.mScreenEditorListener.setCurrentScreenEditor(8);
            ScreenEditorStatUtils.statNavItemClick(ScreenEditorActivity.this.mIsLongScreenMode, 8, false);
            ScreenEditorActivity.this.configEditModeView();
            return currentScreenEditor;
        }

        @Override // com.miui.gallery.editor.photo.screen.home.ScreenNavFragment.Callback
        public boolean onEraserSelect(Eraser eraser) {
            if (ScreenEditorActivity.this.mLongCropEditorController != null) {
                ScreenEditorActivity.this.mScreenEditorListener.setLongCropEntry(ScreenEditorActivity.this.mLongCropEditorController.onExport());
                ScreenEditorActivity.this.mIsLongCropShow = false;
            }
            ScreenEditorStatUtils.statNavItemClick(ScreenEditorActivity.this.mIsLongScreenMode, 11, false);
            DoodlePen eraser2 = DoodlePenManager.INSTANCE.getEraser();
            eraser2.setSize(eraser.getSize());
            ScreenEditorActivity.this.mScreenEditorListener.setDoodlePen(eraser2);
            boolean currentScreenEditor = ScreenEditorActivity.this.mScreenEditorListener.setCurrentScreenEditor(11);
            ((IScreenDoodleOperation) ScreenEditorActivity.this.mScreenEditorFragment.getScreenOperation(IScreenDoodleOperation.class)).setDoodleData(DoodleManager.getScreenDoodlePenPathData(), 0);
            ScreenEditorActivity.this.configEditModeView();
            return currentScreenEditor;
        }

        @Override // com.miui.gallery.editor.photo.screen.home.ScreenNavFragment.Callback
        public void onSaveClick() {
            ScreenEditorActivity.this.handleSaveClickEvent();
        }

        @Override // com.miui.gallery.editor.photo.screen.home.ScreenNavFragment.Callback
        public void onUndoClick() {
            ScreenEditorActivity.this.handleUndoClickEvent();
        }

        @Override // com.miui.gallery.editor.photo.screen.home.ScreenNavFragment.Callback
        public void onRedoClick() {
            ScreenEditorActivity.this.handleRedoClickEvent();
        }
    };
    public ChooserFragment.OnIntentSelectedListener mOnIntentSelectedListener = new ChooserFragment.OnIntentSelectedListener() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity.14
        {
            ScreenEditorActivity.this = this;
        }

        @Override // com.miui.gallery.ui.ChooserFragment.OnIntentSelectedListener
        public void onIntentSelected(Intent intent) {
            if (intent == null) {
                return;
            }
            ScreenEditorActivity.this.mIsClickShare = true;
            GalleryPreferences.ScreenEditorPreference.setSendAndDelete(ScreenEditorActivity.this.checkIsShareAndDelete());
            ScreenEditorActivity.this.mSharePendingIntent = intent;
            ScreenEditorActivity.this.doExportTask();
        }
    };
    public ChooserFragment.OnMishareClickedListener mOnMishareClickedListener = new ChooserFragment.OnMishareClickedListener() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity.15
        {
            ScreenEditorActivity.this = this;
        }

        @Override // com.miui.gallery.ui.ChooserFragment.OnMishareClickedListener
        public void onMishareClicked(ChooserFragment.OnFilesProcessedListener onFilesProcessedListener) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.miui.mishare.connectivity", "MiShareGalleryTransferView"));
            ScreenEditorActivity.this.mOnFilesProcessedListener = onFilesProcessedListener;
            ScreenEditorActivity.this.mOnIntentSelectedListener.onIntentSelected(intent);
        }
    };
    public MoveScreenshotTask.Callback mMoveScreenshotCallback = new MoveScreenshotTask.Callback() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity$$ExternalSyntheticLambda5
        @Override // com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity.MoveScreenshotTask.Callback
        public final void onFinish(boolean z, String str) {
            ScreenEditorActivity.$r8$lambda$IZYl2M7__M7nWKPLEpw7v3JDZWY(ScreenEditorActivity.this, z, str);
        }
    };
    public ScreenRenderCallback mScreenRenderCallback = new ScreenRenderCallback() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity.17
        {
            ScreenEditorActivity.this = this;
        }

        @Override // com.miui.gallery.editor.photo.screen.base.ScreenRenderCallback
        public void setShareBitmap(Bitmap bitmap, boolean z) {
            ScreenEditorActivity.this.mScreenShareView.setShareBitmap(bitmap, false);
        }

        @Override // com.miui.gallery.editor.photo.screen.base.ScreenRenderCallback
        public void onComplete(boolean z) {
            if (z) {
                if (ScreenEditorActivity.this.mNeedExported) {
                    ScreenEditorActivity.this.mExportTask.showExportDialog();
                    ScreenEditorStatUtils.statShowExportFragment(ScreenEditorActivity.this.getRenderData(), ScreenEditorActivity.this.mIsLongScreenMode);
                    return;
                } else if (!ScreenEditorActivity.this.isSharePage()) {
                    ScreenEditorActivity.this.deleteSourceAndExportAndFinish(true, false, false, false);
                    return;
                } else {
                    ScreenEditorActivity screenEditorActivity = ScreenEditorActivity.this;
                    screenEditorActivity.parseUriAndShare(screenEditorActivity.mExportTask.getExportUri());
                    return;
                }
            }
            ScreenEditorActivity.this.configShareModeView();
            ScreenEditorActivity.this.mScreenAnimatorHelper.startSharePageEnterAnimator(ScreenEditorActivity.this.mScreenShellExecutor.getShellMarginTop(), ScreenEditorActivity.this.mScreenShellExecutor.getShellMarginBottom());
        }
    };

    public static /* synthetic */ void $r8$lambda$9i0ZuJQhnZZDI2BlrebZpFFJN20(ScreenEditorActivity screenEditorActivity) {
        screenEditorActivity.lambda$new$1();
    }

    /* renamed from: $r8$lambda$9u7Qb_ihJh2ZGpOQh-MxlPGMK0Q */
    public static /* synthetic */ void m926$r8$lambda$9u7Qb_ihJh2ZGpOQhMxlPGMK0Q(ScreenEditorActivity screenEditorActivity, boolean z, boolean z2, boolean z3) {
        screenEditorActivity.lambda$onAttachFragment$6(z, z2, z3);
    }

    /* renamed from: $r8$lambda$IWkZAUX_CNdCoXt-RlauNI3QeCc */
    public static /* synthetic */ void m927$r8$lambda$IWkZAUX_CNdCoXtRlauNI3QeCc(ScreenEditorActivity screenEditorActivity, boolean z) {
        screenEditorActivity.lambda$onPermissionsChecked$5(z);
    }

    public static /* synthetic */ void $r8$lambda$IZYl2M7__M7nWKPLEpw7v3JDZWY(ScreenEditorActivity screenEditorActivity, boolean z, String str) {
        screenEditorActivity.lambda$new$8(z, str);
    }

    public static /* synthetic */ void $r8$lambda$fir2LFdwjD1Jmb8xpzvP6gSHqDA(ScreenEditorActivity screenEditorActivity, Bitmap bitmap) {
        screenEditorActivity.lambda$initView$0(bitmap);
    }

    public static /* synthetic */ void $r8$lambda$gXRFqqTMbE_uhxzpsTg2UnyCWcE(ScreenEditorActivity screenEditorActivity, boolean z, boolean z2, boolean z3, boolean z4, ObservableEmitter observableEmitter) {
        screenEditorActivity.lambda$deleteSourceAndExportAndFinish$2(z, z2, z3, z4, observableEmitter);
    }

    public static /* synthetic */ void $r8$lambda$kEih_prxCjgWgZy_PazvpHQAY5E(ScreenEditorActivity screenEditorActivity, boolean z) {
        screenEditorActivity.lambda$new$4(z);
    }

    public static /* synthetic */ String $r8$lambda$uRB5keTB42fY07s1k18_lM6ehKM(ScreenEditorActivity screenEditorActivity, Uri uri, ThreadPool.JobContext jobContext) {
        return screenEditorActivity.lambda$parseUriAndShare$7(uri, jobContext);
    }

    public static /* synthetic */ void $r8$lambda$vRynr6ElHy7pzYOfEOyYp1mouE0(ScreenEditorActivity screenEditorActivity, long[] jArr) {
        screenEditorActivity.lambda$new$3(jArr);
    }

    public final void onCtaChecked() {
    }

    @Override // com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!MediaEditorInstaller.getInstance().installIfNotExist(this, this.mMediaEditorInstallerCallback, false)) {
            return;
        }
        if (MediaEditorUtils.isMediaEditorAvailable() && MediaEditorUtils.isSecurityCenterSupportMediaEditor(this) && MediaEditorUtils.isMiShareSupportMediaEditor(this)) {
            Intent intent = getIntent();
            intent.setComponent(new ComponentName("com.miui.mediaeditor", "com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity"));
            QuitThumbnailResultReceiver quitThumbnailResultReceiver = new QuitThumbnailResultReceiver(this, new Handler());
            this.mQuitThumbnailReceiver = quitThumbnailResultReceiver;
            intent.putExtra("extra_result_receiver", ParcelUtils.getParcelableResultReceiver(quitThumbnailResultReceiver));
            startActivity(intent);
            finish();
            return;
        }
        BaseGalleryPreferences.CTA.setToAllowUseOnOfflineGlobal(getIntent().getBooleanExtra("allow_use_on_offline_global", false));
        requestDisableStrategy(StrategyContext.DisableStrategyType.NAVIGATION_BAR);
        SystemUiUtil.hideSystemBars(getWindow().getDecorView(), true, false, false);
        getWindow().getDecorView().setBackgroundResource(R.color.screen_editor_view_background);
        init();
    }

    /* loaded from: classes2.dex */
    public static class QuitThumbnailResultReceiver extends ResultReceiver {
        public final WeakReference<ScreenEditorActivity> mActivity;

        public QuitThumbnailResultReceiver(ScreenEditorActivity screenEditorActivity, Handler handler) {
            super(handler);
            this.mActivity = new WeakReference<>(screenEditorActivity);
        }

        @Override // android.os.ResultReceiver
        public void onReceiveResult(int i, Bundle bundle) {
            ScreenEditorActivity screenEditorActivity;
            super.onReceiveResult(i, bundle);
            if (i != -1 || (screenEditorActivity = this.mActivity.get()) == null || screenEditorActivity.isDestroyed() || screenEditorActivity.isFinishing()) {
                return;
            }
            screenEditorActivity.quitThumbnail();
        }
    }

    public final void quitThumbnail() {
        Intent intent = new Intent(this, ScreenShotService.class);
        intent.putExtra("quit_thumnail", true);
        BackgroundServiceHelper.startForegroundServiceIfNeed(this, intent);
        this.mIsQuitThumbnail = true;
    }

    public void init() {
        if (!OrientationCheckHelper.isSupportOrientationChange() || !BaseBuildUtil.isLargeScreenIndependentOrientation()) {
            if (!this.mIsNoTranslucentActivity && !SystemUiUtil.setRequestedOrientation(1, this)) {
                Intent intent = getIntent();
                intent.setClass(this, ScreenEditorNoTranslucentActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        } else {
            SystemUiUtil.setRequestedOrientation(4, this);
        }
        WindowCompat.setCutoutModeShortEdges(getWindow());
        setContentView(R.layout.screen_editor_activity);
        this.mActivity = this;
        this.mHandler = new Handler();
        Intent intent2 = getIntent();
        this.mIsLongScreenMode = intent2.getBooleanExtra(Constants.EXTRA_IS_LONG_SCREENSHOT, false);
        this.mIsFromLongScreen = intent2.getBooleanExtra("FromLongScreenshot", false);
        this.mIsFromSendMode = intent2.getBooleanExtra("is_from_send", false);
        DefaultLogger.d("ScreenEditorActivity_", "init: " + this.mIsLongScreenMode + this.mIsFromLongScreen + this.mIsFromSendMode);
        if (intent2.getData() == null) {
            finish();
            return;
        }
        this.mDraftManager = new DraftManager(this, intent2.getData(), intent2.getExtras());
        this.mExportTask = ExportTask.from(this);
        this.mPageMode = this.mIsFromSendMode ? 1 : 0;
        this.mScreenShellExecutor = new ScreenShellExecutor();
        this.mScreenAnimatorHelper = new ScreenAnimatorHelper(this, getIntent().getIntArrayExtra("ThumbnailRect"), this.mAnimatorViewCallback);
        this.mScreenRenderManager = new ScreenRenderManager(this.mDraftManager);
        initView();
        initScreenAnimatorHelper();
        PermissionInjector.injectIfNeededIn(this, this, null);
        ScreenProviderManager.INSTANCE.onActivityCreate(this);
    }

    public final void initScreenAnimatorHelper() {
        if (isFromNormalEdit()) {
            this.mScreenAnimatorHelper.setViewsAlpha(0.0f, this.mTopLayoutContainer, this.mScreenRootBg, this.mBottomLayoutView);
        } else if (!isFromNormalShare()) {
        } else {
            this.mScreenAnimatorHelper.setViewsAlpha(0.0f, this.mTopLayoutContainer, this.mScreenRootBg, this.mBottomLayoutView, this.mShareTopLayoutView);
        }
    }

    public final void updateUndoView(boolean z) {
        if (Utils.isLargeScreen(this)) {
            Fragment fragment = this.mNavFragment;
            if (fragment == null || !(fragment instanceof ScreenNavFragment)) {
                return;
            }
            ((ScreenNavFragment) fragment).setUndoEnable(z);
            return;
        }
        this.mUndoIv.setEnabled(z);
    }

    public final void updateRedoView(boolean z) {
        if (Utils.isLargeScreen(this)) {
            Fragment fragment = this.mNavFragment;
            if (fragment == null || !(fragment instanceof ScreenNavFragment)) {
                return;
            }
            ((ScreenNavFragment) fragment).setRedoEnable(z);
            return;
        }
        this.mRedoIv.setEnabled(z);
    }

    public final void initNavigation() {
        int navBarHeight = ScreenUtils.getNavBarHeight(this);
        if (navBarHeight == 0) {
            navBarHeight = getResources().getDimensionPixelSize(R.dimen.screen_editor_activity_navigation_height);
        }
        ViewGroup.LayoutParams layoutParams = this.mNavigationPlaceHolder.getLayoutParams();
        layoutParams.height = navBarHeight;
        this.mNavigationPlaceHolder.setLayoutParams(layoutParams);
    }

    public final void initView() {
        this.mTopLayoutContainer = (FrameLayout) findViewById(R.id.screen_edit_top_layout_container);
        if (Utils.isLargeScreen(this)) {
            View inflate = View.inflate(this, R.layout.screen_edit_top_layout_pad, this.mTopLayoutContainer);
            this.mTopLayout = inflate;
            ImageView imageView = (ImageView) inflate.findViewById(R.id.delete_iv);
            this.mDeleteIv = imageView;
            imageView.setOnClickListener(this.mSingleClickListener);
            ImageView imageView2 = (ImageView) this.mTopLayout.findViewById(R.id.share_iv);
            this.mShareIv = imageView2;
            imageView2.setOnClickListener(this.mSingleClickListener);
        } else {
            View inflate2 = View.inflate(this, R.layout.screen_edit_top_layout_phone, this.mTopLayoutContainer);
            this.mTopLayout = inflate2;
            ImageView imageView3 = (ImageView) inflate2.findViewById(R.id.delete_iv);
            this.mDeleteIv = imageView3;
            imageView3.setOnClickListener(this.mSingleClickListener);
            ImageView imageView4 = (ImageView) this.mTopLayout.findViewById(R.id.save_iv);
            this.mSaveIv = imageView4;
            imageView4.setOnClickListener(this.mSingleClickListener);
            ImageView imageView5 = (ImageView) this.mTopLayout.findViewById(R.id.share_iv);
            this.mShareIv = imageView5;
            imageView5.setOnClickListener(this.mSingleClickListener);
            ImageView imageView6 = (ImageView) this.mTopLayout.findViewById(R.id.undo_iv);
            this.mUndoIv = imageView6;
            imageView6.setOnClickListener(this.mOnClickListener);
            ImageView imageView7 = (ImageView) this.mTopLayout.findViewById(R.id.redo_iv);
            this.mRedoIv = imageView7;
            imageView7.setOnClickListener(this.mOnClickListener);
        }
        this.mShellImageView = (ImageView) this.mTopLayout.findViewById(R.id.screen_editor_shell_image);
        this.mShellTextViewOn = (TextView) findViewById(R.id.screen_editor_shell_text_on);
        this.mShellTextViewOff = (TextView) findViewById(R.id.screen_editor_shell_text_off);
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.screen_editor_shell_layout);
        this.mShellLayoutView = viewGroup;
        viewGroup.setOnClickListener(this.mAntiDoubleClickListener);
        this.mNavigationPlaceHolder = findViewById(R.id.navigation_placeholder);
        this.mScreenRootBg = (FrameLayout) findViewById(R.id.screen_root_layout_bg);
        this.mBottomLayoutView = (FrameLayout) findViewById(R.id.bottom_area_layout);
        this.mEditViewLayoutView = (FrameLayout) findViewById(R.id.screen_editor_layout);
        this.mLongCropLayout = findViewById(R.id.screen_longcrop_layout);
        ScreenShareView screenShareView = (ScreenShareView) findViewById(R.id.share_view);
        this.mScreenShareView = screenShareView;
        screenShareView.setOnClickShareViewListener(this.mOnClickShareViewListener);
        this.mShareTopLayoutView = (ConstraintLayout) findViewById(R.id.top_send_layout);
        this.mShareLayoutView = (FrameLayout) findViewById(R.id.share_view_layout);
        ImageView imageView8 = (ImageView) findViewById(R.id.action_back);
        this.mBackView = imageView8;
        imageView8.setOnClickListener(this.mOnClickListener);
        this.mShareTopLine = (Guideline) findViewById(R.id.share_top_guide_line);
        this.mShareBottomLine = (Guideline) findViewById(R.id.share_bottom_guide_line);
        this.mCommonLine = (Guideline) findViewById(R.id.common_base_guide_line);
        this.mMenuTopLine = (Guideline) findViewById(R.id.menu_top_guide_line);
        this.mEditBottomLine = (Guideline) findViewById(R.id.bottom_guide_line);
        this.mEditTopLine = (Guideline) findViewById(R.id.top_guide_line);
        initNavigation();
        updateUndoView(false);
        updateRedoView(false);
        this.mScreenShareView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity.2
            {
                ScreenEditorActivity.this = this;
            }

            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                ScreenEditorActivity.this.mScreenShareView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ScreenEditorActivity.this.mIsShareViewAlreadyLayout = true;
                ScreenEditorActivity.this.doAnimators();
            }
        });
        initShellView();
        this.mScreenEditorFragment = new ScreenEditorFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.screen_editor_layout, this.mScreenEditorFragment, "fragment_tag_editor").commit();
        this.mScreenRenderManager.setOriginLoadedListener(new ScreenRenderManager.OnOriginLoadedListener() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity$$ExternalSyntheticLambda2
            @Override // com.miui.gallery.editor.photo.screen.core.ScreenRenderManager.OnOriginLoadedListener
            public final void onRefresh(Bitmap bitmap) {
                ScreenEditorActivity.$r8$lambda$fir2LFdwjD1Jmb8xpzvP6gSHqDA(ScreenEditorActivity.this, bitmap);
            }
        });
        this.mScreenRenderManager.decodeOrigin();
        showBottomViewWithMode(this.mPageMode);
    }

    public /* synthetic */ void lambda$initView$0(Bitmap bitmap) {
        this.mScreenEditorFragment.setOriginBitmap(bitmap);
    }

    @Override // com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (!OrientationCheckHelper.isSupportOrientationChange() || !BaseBuildUtil.isLargeScreenIndependentOrientation()) {
            SystemUiUtil.setRequestedOrientation(1, this);
        } else {
            SystemUiUtil.setRequestedOrientation(4, this);
        }
        if (!isSharePage() || this.mScreenShareView == null) {
            return;
        }
        configShareModeView();
    }

    public final void initShellView() {
        if (this.mScreenShellExecutor.isShellFuncEnable()) {
            this.mShellLayoutView.setVisibility(0);
        } else {
            this.mShellLayoutView.setVisibility(8);
        }
    }

    public final void doAnimators() {
        if (!this.mIsDecoderDone || !this.mIsShareViewAlreadyLayout) {
            return;
        }
        if (this.mIsFromLongScreen) {
            if (!this.mIsLongScreenMode) {
                this.mScreenAnimatorHelper.startEnterAnimator();
                return;
            }
            this.mScreenEditorListener.setLongCrop(true);
            this.mScreenEditorListener.startScreenThumbnailAnimate(this.mScreenAnimatorHelper.mThumbnailAnimatorCallback);
        } else if (isFromNormalEdit()) {
            this.mScreenEditorListener.startScreenThumbnailAnimate(this.mScreenAnimatorHelper.mThumbnailAnimatorCallback);
        } else {
            this.mScreenShareView.startShareViewAnimator(this.mScreenAnimatorHelper.mShareViewAnimatorCallback);
        }
    }

    public void changeWithMode(final int i) {
        if (i == 0) {
            this.mScreenAnimatorHelper.startSharePageExitAnimator(this.mScreenShellExecutor.getShellMarginTop(), this.mScreenShellExecutor.getShellMarginBottom(), new AnimatorListenerAdapter() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity.3
                {
                    ScreenEditorActivity.this = this;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    ScreenEditorActivity.this.mScreenEditorListener.checkTextEditor(false);
                    ScreenEditorActivity.this.showBottomViewWithMode(i);
                    ScreenEditorActivity.this.configEditModeView();
                    ScreenEditorActivity.this.mScreenAnimatorHelper.startEditPageEnterAnimator();
                }
            });
        } else if (i != 1) {
        } else {
            this.mScreenAnimatorHelper.startEditPageExitAnimator(new AnimatorListenerAdapter() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity.4
                {
                    ScreenEditorActivity.this = this;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    ScreenEditorActivity.this.mScreenEditorListener.checkTextEditor(true);
                    ScreenEditorActivity.this.showBottomViewWithMode(i);
                    ScreenEditorActivity.this.checkAndDoRender(false);
                }
            });
        }
    }

    public void configShareModeView() {
        this.mTopLayoutContainer.setVisibility(8);
        this.mShareLayoutView.setVisibility(0);
        this.mShareTopLayoutView.setVisibility(0);
        this.mLongCropLayout.setVisibility(8);
        this.mEditViewLayoutView.setVisibility(8);
        this.mShareBottomLine.setGuidelineEnd(this.mScreenAnimatorHelper.getShareModeBottomGuidelineEnd());
        this.mCommonLine.setGuidelineEnd(this.mScreenAnimatorHelper.getScreenEditorBaseGuidelineEnd());
        FrameLayout frameLayout = this.mBottomLayoutView;
        if (frameLayout != null) {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) frameLayout.getLayoutParams();
            ((ViewGroup.MarginLayoutParams) layoutParams).height = this.mScreenAnimatorHelper.getScreenEditorBaseGuidelineEnd();
            layoutParams.topToBottom = R.id.common_base_guide_line;
            this.mBottomLayoutView.setLayoutParams(layoutParams);
        }
        if (!isFromNormalShare()) {
            this.mShareLayoutView.setPadding(getResources().getDimensionPixelSize(R.dimen.screen_editor_share_view_padding_start), 0, getResources().getDimensionPixelSize(R.dimen.screen_editor_share_view_padding_end), 0);
        }
        getWindow().setNavigationBarColor(getResources().getColor(R.color.screen_editor_view_background));
        SystemUiUtil.setTraditionNavigationBarIconMode(this.mActivity, BaseMiscUtil.isNightMode(getWindow().getContext()));
    }

    public void configEditModeView() {
        this.mShareTopLayoutView.setVisibility(8);
        this.mShareLayoutView.setVisibility(8);
        this.mTopLayoutContainer.setVisibility(0);
        if (this.mIsLongScreenMode && this.mIsLongCropShow) {
            this.mLongCropLayout.setVisibility(0);
            this.mEditViewLayoutView.setVisibility(8);
        } else {
            this.mLongCropLayout.setVisibility(8);
            this.mEditViewLayoutView.setVisibility(0);
        }
        getWindow().setNavigationBarColor(getResources().getColor(R.color.screen_editor_share_bg));
    }

    public void showBottomViewWithMode(int i) {
        Fragment fragment;
        String str;
        if (i == 0) {
            if (this.mNavFragment == null) {
                this.mNavFragment = ScreenNavFragment.newInstance();
            }
            fragment = this.mNavFragment;
            str = "NavFragment_";
        } else if (i == 1) {
            if (this.mChooserFragment == null) {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("image/*");
                this.mChooserFragment = ChooserFragment.newInstance(intent, 0, true, 3);
            }
            ((ChooserFragment) this.mChooserFragment).setOnIntentSelectedListener(this.mOnIntentSelectedListener);
            ((ChooserFragment) this.mChooserFragment).setOnMishareClickedListener(this.mOnMishareClickedListener);
            fragment = this.mChooserFragment;
            str = "ChooserFragment";
        } else {
            fragment = null;
            str = "";
        }
        if (fragment == null) {
            return;
        }
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment2 = this.mCurrentBottomFragment;
        if (fragment2 == null) {
            beginTransaction.add(R.id.bottom_area_layout, fragment, str).commitAllowingStateLoss();
        } else if (fragment2 != fragment) {
            if (fragment2 instanceof ChooserFragment) {
                if (!fragment.isAdded() && getSupportFragmentManager().findFragmentByTag(str) == null) {
                    beginTransaction.remove(this.mCurrentBottomFragment).add(R.id.bottom_area_layout, fragment, str).commitAllowingStateLoss();
                } else {
                    beginTransaction.remove(this.mCurrentBottomFragment).show(fragment).commitAllowingStateLoss();
                }
            } else if (!fragment.isAdded() && getSupportFragmentManager().findFragmentByTag(str) == null) {
                beginTransaction.hide(this.mCurrentBottomFragment).add(R.id.bottom_area_layout, fragment, str).commitAllowingStateLoss();
            } else {
                beginTransaction.hide(this.mCurrentBottomFragment).show(fragment).commitAllowingStateLoss();
            }
        }
        this.mCurrentBottomFragment = fragment;
    }

    public /* synthetic */ void lambda$new$1() {
        switchPageMode(0);
    }

    public final void updateLongScreenPreviewShow() {
        ILongCropEditorController iLongCropEditorController = this.mLongCropEditorController;
        if (iLongCropEditorController != null) {
            iLongCropEditorController.setScreenDrawEntry(this.mScreenEditorListener.onExport().mDrawEntry);
        }
    }

    public final void handleShareClickEvent() {
        if (this.mIsLongScreenMode || this.mScreenEditorFragment.isCanDoSaveOperation()) {
            switchPageMode(1);
        }
    }

    public final boolean checkOutFileCreatePermission() {
        this.mExportTask.prepareToExport(this.mDraftManager);
        return FilePermissionUtils.checkFileCreatePermission(this, UriUtils.getFilePathWithUri(GalleryApp.sGetAndroidContext(), this.mExportTask.getExportUri()));
    }

    public final boolean checkSourceFileDeletePermission() {
        return FilePermissionUtils.checkFileDeletePermission(this, UriUtils.getFilePathWithUri(GalleryApp.sGetAndroidContext(), this.mExportTask.getSource()));
    }

    public final boolean checkOutFileDeletePermission() {
        return FilePermissionUtils.checkFileDeletePermission(this, UriUtils.getFilePathWithUri(GalleryApp.sGetAndroidContext(), this.mExportTask.getExportUri()));
    }

    public final void handleSaveClickEvent() {
        if (this.mIsLongScreenMode || this.mScreenEditorFragment.isCanDoSaveOperation()) {
            this.mScreenEditorListener.export();
            doExportTask();
            ScreenEditorStatUtils.statBtnSavelClick(this.mIsLongScreenMode);
        }
    }

    public final void deleteFileWithJudgeHasExport() {
        if (!checkSourceFileDeletePermission()) {
            return;
        }
        if (this.mHasExport) {
            if (!checkOutFileDeletePermission()) {
                return;
            }
            deleteSourceAndExportAndFinish(true, true, false, true);
            return;
        }
        deleteSourceAndExportAndFinish(true, false, true, false);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (!this.mIsAllowClick) {
            return true;
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public final void deleteSourceAndExportAndFinish(final boolean z, final boolean z2, final boolean z3, final boolean z4) {
        this.mIsAllowClick = false;
        Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity$$ExternalSyntheticLambda8
            @Override // io.reactivex.ObservableOnSubscribe
            public final void subscribe(ObservableEmitter observableEmitter) {
                ScreenEditorActivity.$r8$lambda$gXRFqqTMbE_uhxzpsTg2UnyCWcE(ScreenEditorActivity.this, z, z3, z2, z4, observableEmitter);
            }
        }).subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Pair<String, String>>() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity.9
            @Override // io.reactivex.Observer
            public void onComplete() {
            }

            {
                ScreenEditorActivity.this = this;
            }

            @Override // io.reactivex.Observer
            public void onSubscribe(Disposable disposable) {
                ScreenEditorActivity.this.mDeleteSourceAndExportDisposable = disposable;
            }

            @Override // io.reactivex.Observer
            public void onNext(Pair<String, String> pair) {
                DefaultLogger.d("ScreenEditorActivity_", "start deleteFileLocalAndCloud, path : %s ", pair.toString());
                boolean z5 = z;
                if (z5 && z2) {
                    boolean z6 = z3;
                    if (z6 && z4) {
                        ScreenEditorActivity.this.deleteFileLocalAndCloud((String) pair.first, (String) pair.second);
                    } else if (z6) {
                        ScreenEditorActivity.this.deleteFileLocalAndCloud((String) pair.first);
                    } else if (z4) {
                        ScreenEditorActivity.this.deleteFileLocalAndCloud((String) pair.second);
                    } else {
                        ScreenEditorActivity.this.finish();
                    }
                } else if (z5 && z3) {
                    ScreenEditorActivity.this.deleteFileLocalAndCloud((String) pair.first);
                } else if (z2 && z4) {
                    ScreenEditorActivity.this.deleteFileLocalAndCloud((String) pair.second);
                } else {
                    ScreenEditorActivity.this.finish();
                }
                ScreenEditorActivity.this.mIsDeleteLocalAndCloudExecuted = true;
            }

            @Override // io.reactivex.Observer
            public void onError(Throwable th) {
                ScreenEditorActivity.this.mIsAllowClick = true;
            }
        });
    }

    public /* synthetic */ void lambda$deleteSourceAndExportAndFinish$2(boolean z, boolean z2, boolean z3, boolean z4, ObservableEmitter observableEmitter) throws Exception {
        String str;
        String str2 = "";
        if (z) {
            str = UriUtils.getFilePathWithUri(GalleryApp.sGetAndroidContext(), this.mExportTask.getSource());
            if (!z2) {
                deleteFileThorough(str);
            }
        } else {
            str = str2;
        }
        if (z3) {
            str2 = UriUtils.getFilePathWithUri(GalleryApp.sGetAndroidContext(), this.mExportTask.getExportUri());
            if (!z4) {
                deleteFileThorough(str2);
            }
        }
        DefaultLogger.d("ScreenEditorActivity_", "deleteFileThorough finish");
        observableEmitter.onNext(Pair.create(str, str2));
    }

    public final void deleteFileThorough(String str) {
        if (!TextUtils.isEmpty(str) && FilePermissionUtils.checkFileDeletePermission(this, str)) {
            DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.DELETE, FileHandleRecordHelper.appendInvokerTag("ScreenEditorActivity_", "deleteFileThorough"));
            if (documentFile == null) {
                return;
            }
            documentFile.delete();
            ScreenEditorHelper.updateLocalDBNotShowInRecycleBin(str);
            ScannerEngine.getInstance().cleanFile(GalleryApp.sGetAndroidContext(), str, 13);
        }
    }

    public final void deleteFileLocalAndCloud(String... strArr) {
        if (strArr == null || strArr.length < 1) {
            return;
        }
        DeletionTask deletionTask = new DeletionTask();
        DeletionTask.Param param = new DeletionTask.Param(strArr, 0, 57);
        deletionTask.setFragmentActivityForStoragePermissionMiss(this);
        deletionTask.setCompleteListener(this.mCompleteListener);
        deletionTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, param);
    }

    public /* synthetic */ void lambda$new$3(long[] jArr) {
        DefaultLogger.d("ScreenEditorActivity_", "delete Screenshots file success!");
        finish();
    }

    public /* synthetic */ void lambda$new$4(boolean z) {
        this.mCurrentHasCrop = z;
        this.mScreenShellExecutor.setViewStatus(z);
    }

    public final void doShare(Uri uri) {
        this.mIsShared = false;
        Intent intent = this.mSharePendingIntent;
        if (intent == null || uri == null) {
            DefaultLogger.e("ScreenEditorActivity_", "share error.");
            return;
        }
        String packageName = intent.getComponent().getPackageName();
        if (GalleryOpenProvider.needReturnContentUri(this.mActivity, this.mSharePendingIntent)) {
            uri = GalleryOpenProvider.translateToContent(uri.getPath());
            if (!TextUtils.isEmpty(packageName)) {
                this.mActivity.grantUriPermission(packageName, uri, 1);
                this.mActivity.grantUriPermission(SecurityShareHelper.getSharePackageName(getApplicationContext()), uri, 1);
            }
        }
        if ("com.miui.mishare.connectivity".equals(packageName)) {
            this.mOnFilesProcessedListener.onFilesProcessed(Arrays.asList(uri));
            this.mSharePendingIntent = null;
            return;
        }
        this.mSharePendingIntent.setAction("android.intent.action.SEND");
        this.mSharePendingIntent.putExtra("android.intent.extra.STREAM", uri);
        this.mSharePendingIntent.addFlags(268435456);
        this.mSharePendingIntent.addFlags(134742016);
        startActivityForResult(this.mSharePendingIntent, 1);
    }

    @Override // com.miui.gallery.permission.core.PermissionCheckCallback
    public Permission[] getRuntimePermissions() {
        return new Permission[]{new Permission("android.permission.WRITE_EXTERNAL_STORAGE", getString(R.string.permission_storage_desc), true)};
    }

    @Override // com.miui.gallery.permission.core.PermissionCheckCallback
    public void onPermissionsChecked(Permission[] permissionArr, int[] iArr, boolean[] zArr) {
        if (BaseGalleryPreferences.CTA.allowUseOnOfflineGlobal() || BaseGalleryPreferences.CTA.canConnectNetwork()) {
            onCtaChecked();
        } else if (!BaseBuildUtil.isInternational() && BaseGalleryPreferences.CTA.hasShownNetworkingAgreements() && !BaseGalleryPreferences.CTA.remindConnectNetworkEveryTime()) {
            onCtaChecked();
        } else {
            AgreementsUtils.showUserAgreements(this, new OnAgreementInvokedListener() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.agreement.core.OnAgreementInvokedListener
                public final void onAgreementInvoked(boolean z) {
                    ScreenEditorActivity.m927$r8$lambda$IWkZAUX_CNdCoXtRlauNI3QeCc(ScreenEditorActivity.this, z);
                }
            });
        }
        doInitialize();
    }

    public /* synthetic */ void lambda$onPermissionsChecked$5(boolean z) {
        onCtaChecked();
    }

    public final void doInitialize() {
        InitializeController initializeController = new InitializeController(this, this.mDecoderCallbacks);
        this.mInitializeController = initializeController;
        initializeController.doInitialize();
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onAttachedToWindow() {
        if (this.mShareTopLayoutView == null) {
            return;
        }
        if (isFromNormalShare()) {
            configShareModeView();
        } else {
            configEditModeView();
        }
        if (!WindowCompat.isNotch(this)) {
            return;
        }
        SystemUiUtil.extendToStatusBar(getWindow().getDecorView());
    }

    @Override // androidx.fragment.app.FragmentActivity
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        DefaultLogger.d("ScreenEditorActivity_", "onAttachFragment: " + fragment.getTag());
        if (fragment instanceof ExportFragment) {
            ((ExportFragment) fragment).setCallbacks(this.mExportCallbacks);
        } else if (fragment instanceof IScreenEditorController) {
            IScreenEditorController iScreenEditorController = (IScreenEditorController) fragment;
            this.mScreenEditorListener = iScreenEditorController;
            iScreenEditorController.setOnCropStatusChangeListener(this.mOnScreenCropStatusChangeListener);
            this.mScreenEditorListener.setOperationUpdateListener(new OperationUpdateListener() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity$$ExternalSyntheticLambda4
                @Override // com.miui.gallery.editor.photo.screen.home.OperationUpdateListener
                public final void onOperationUpdate(boolean z, boolean z2, boolean z3) {
                    ScreenEditorActivity.m926$r8$lambda$9u7Qb_ihJh2ZGpOQhMxlPGMK0Q(ScreenEditorActivity.this, z, z2, z3);
                }
            });
            this.mScreenEditorListener.setLongCrop(this.mIsLongCropShow);
        } else if (!(fragment instanceof ScreenNavFragment)) {
        } else {
            ((ScreenNavFragment) fragment).setCallback(this.mScreenEditBottomCallback);
        }
    }

    public /* synthetic */ void lambda$onAttachFragment$6(boolean z, boolean z2, boolean z3) {
        updateUndoView(z2);
        updateRedoView(z3);
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        if (this.mIsClickShare && checkIsShareAndDelete()) {
            deleteFileWithJudgeHasExport();
        }
        super.onStop();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        Disposable disposable;
        super.onDestroy();
        hideProcessDialog();
        DefaultLogger.d("ScreenEditorActivity_", "mIsDeleteLocalAndCloudExecuted = %b", Boolean.valueOf(this.mIsDeleteLocalAndCloudExecuted));
        if (this.mIsDeleteLocalAndCloudExecuted && (disposable = this.mDeleteSourceAndExportDisposable) != null) {
            if (!disposable.isDisposed()) {
                this.mDeleteSourceAndExportDisposable.dispose();
            }
            this.mDeleteSourceAndExportDisposable = null;
        }
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            this.mHandler = null;
        }
        ScreenAnimatorHelper screenAnimatorHelper = this.mScreenAnimatorHelper;
        if (screenAnimatorHelper != null) {
            screenAnimatorHelper.release();
        }
        DraftManager draftManager = this.mDraftManager;
        if (draftManager != null) {
            draftManager.release();
        }
        ExportTask exportTask = this.mExportTask;
        if (exportTask != null) {
            exportTask.closeExportDialog();
        }
        ScreenProviderManager.INSTANCE.onActivityDestroy();
        ScreenRenderManager screenRenderManager = this.mScreenRenderManager;
        if (screenRenderManager != null) {
            screenRenderManager.release();
        }
        ScreenDeleteDialogFragment screenDeleteDialogFragment = this.mScreenDeleteDialogFragment;
        if (screenDeleteDialogFragment != null) {
            screenDeleteDialogFragment.dismissSafely();
            this.mScreenDeleteDialogFragment = null;
        }
        this.mOnFilesProcessedListener = null;
        MoveScreenshotTask moveScreenshotTask = this.mMoveScreenshotTask;
        if (moveScreenshotTask != null) {
            moveScreenshotTask.cancel();
            this.mMoveScreenshotTask = null;
        }
        ScreenShellExecutor screenShellExecutor = this.mScreenShellExecutor;
        if (screenShellExecutor != null) {
            screenShellExecutor.release();
        }
        if (this.mQuitThumbnailReceiver != null) {
            if (!this.mIsQuitThumbnail) {
                quitThumbnail();
            }
            this.mQuitThumbnailReceiver = null;
        }
    }

    public ScreenRenderData getRenderData() {
        ScreenRenderData onExport = this.mScreenEditorListener.onExport();
        ILongCropEditorController iLongCropEditorController = this.mLongCropEditorController;
        if (iLongCropEditorController != null && iLongCropEditorController.onExport() != null) {
            onExport.setLongCropEntry(this.mLongCropEditorController.onExport());
        }
        return onExport;
    }

    public final void handleRedoClickEvent() {
        this.mScreenEditorListener.doRevert();
        updateLongScreenPreviewShow();
        ScreenEditorStatUtils.statRevertClick(this.mIsLongScreenMode);
    }

    public final void handleUndoClickEvent() {
        this.mScreenEditorListener.doRevoke();
        updateLongScreenPreviewShow();
        ScreenEditorStatUtils.statRevokeClick(this.mIsLongScreenMode);
    }

    public final void checkAndDoRender(boolean z) {
        IScreenShellOperation iScreenShellOperation;
        ILongCropEditorController iLongCropEditorController = this.mLongCropEditorController;
        boolean z2 = false;
        boolean z3 = iLongCropEditorController != null && iLongCropEditorController.isModifiedBaseLast();
        if (!z && (iScreenShellOperation = this.mScreenShellOperation) != null && iScreenShellOperation.isShellStatusChangedForLastRequest()) {
            z2 = true;
        }
        if (this.mScreenEditorListener.isModifiedBaseLast() || z3 || z2) {
            this.mNeedExported = true;
            this.mScreenRenderManager.renderBitmap(z, getRenderData(), this.mScreenRenderCallback);
            return;
        }
        this.mScreenRenderCallback.onComplete(z);
    }

    public final void doExportTask() {
        if (hasEdited()) {
            if (!checkOutFileCreatePermission()) {
                return;
            }
            checkAndDoRender(true);
        } else if (isSharePage()) {
            parseUriAndShare(this.mExportTask.getSource());
        } else {
            finish();
        }
    }

    public final void parseUriAndShare(final Uri uri) {
        if (!this.mIsShared) {
            this.mIsShared = true;
            if (checkIsShareAndDelete()) {
                delayShowLoading();
                submitMoveTask(uri);
                return;
            }
            ThreadManager.getMiscPool().submit(new ThreadPool.Job() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity$$ExternalSyntheticLambda1
                @Override // com.miui.gallery.concurrent.ThreadPool.Job
                /* renamed from: run */
                public final Object mo1807run(ThreadPool.JobContext jobContext) {
                    return ScreenEditorActivity.$r8$lambda$uRB5keTB42fY07s1k18_lM6ehKM(ScreenEditorActivity.this, uri, jobContext);
                }
            }, new FutureHandler<String>() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity.16
                {
                    ScreenEditorActivity.this = this;
                }

                @Override // com.miui.gallery.concurrent.FutureHandler
                public void onPostExecute(Future<String> future) {
                    if (!TextUtils.isEmpty(future.get())) {
                        ScreenEditorActivity.this.doShare(Uri.parse(future.get()));
                    }
                }
            });
        }
    }

    public /* synthetic */ String lambda$parseUriAndShare$7(Uri uri, ThreadPool.JobContext jobContext) {
        return UriUtils.getFilePathWithUri(this.mActivity, uri);
    }

    public final boolean checkIsShareAndDelete() {
        Fragment fragment = this.mChooserFragment;
        if (fragment == null || !(fragment instanceof ChooserFragment)) {
            return false;
        }
        return ((ChooserFragment) fragment).getShareAndDeleteIsSelect();
    }

    public void delayShowLoading() {
        if (this.mHandler == null) {
            this.mHandler = new Handler();
        }
        ShowLoadRunnable showLoadRunnable = this.mShowLoadRunnable;
        if (showLoadRunnable != null) {
            this.mHandler.removeCallbacks(showLoadRunnable);
        }
        ShowLoadRunnable showLoadRunnable2 = new ShowLoadRunnable();
        this.mShowLoadRunnable = showLoadRunnable2;
        this.mHandler.postDelayed(showLoadRunnable2, 1000L);
    }

    /* loaded from: classes2.dex */
    public class ShowLoadRunnable implements Runnable {
        public ShowLoadRunnable() {
            ScreenEditorActivity.this = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (!ScreenEditorActivity.this.checkIsShareAndDelete() || ScreenEditorActivity.this.mIsMoveScreenFinished) {
                return;
            }
            ScreenEditorActivity.this.showProcessDialog();
        }
    }

    public final void showProcessDialog() {
        if (this.mProgressDialog == null) {
            ProgressDialog progressDialog = new ProgressDialog(this.mActivity);
            this.mProgressDialog = progressDialog;
            progressDialog.setMessage(this.mActivity.getString(R.string.remover_menu_processing));
            this.mProgressDialog.setCanceledOnTouchOutside(false);
            this.mProgressDialog.setCancelable(false);
            this.mProgressDialog.setIndeterminate(true);
        }
        this.mProgressDialog.show();
    }

    public final void hideProcessDialog() {
        ProgressDialog progressDialog = this.mProgressDialog;
        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }
        this.mProgressDialog.dismiss();
    }

    /* loaded from: classes2.dex */
    public static class MoveScreenshotTask {
        public Callback mCallback;
        public Future mFuture;

        /* loaded from: classes2.dex */
        public interface Callback {
            void onFinish(boolean z, String str);
        }

        public static /* synthetic */ String $r8$lambda$_WwTgBUwzF7WFWTljr7YhFuBwuY(Uri uri, ThreadPool.JobContext jobContext) {
            return lambda$execute$0(uri, jobContext);
        }

        public MoveScreenshotTask(Callback callback) {
            this.mCallback = callback;
        }

        public void execute(final Uri uri) {
            Future future = this.mFuture;
            if (future != null) {
                future.cancel();
            }
            this.mFuture = ThreadManager.getMiscPool().submit(new ThreadPool.Job() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity$MoveScreenshotTask$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.concurrent.ThreadPool.Job
                /* renamed from: run */
                public final Object mo1807run(ThreadPool.JobContext jobContext) {
                    return ScreenEditorActivity.MoveScreenshotTask.$r8$lambda$_WwTgBUwzF7WFWTljr7YhFuBwuY(uri, jobContext);
                }
            }, new FutureHandler<String>() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity.MoveScreenshotTask.1
                {
                    MoveScreenshotTask.this = this;
                }

                @Override // com.miui.gallery.concurrent.FutureHandler
                public void onPostExecute(Future<String> future2) {
                    if (MoveScreenshotTask.this.mCallback != null) {
                        MoveScreenshotTask.this.mCallback.onFinish(future2.isCancelled(), future2.get());
                    }
                }
            });
        }

        public static /* synthetic */ String lambda$execute$0(Uri uri, ThreadPool.JobContext jobContext) {
            return ScreenEditorHelper.copyScreenFileToCache(UriUtils.getFilePathWithUri(GalleryApp.sGetAndroidContext(), uri));
        }

        public void cancel() {
            Future future = this.mFuture;
            if (future != null) {
                future.cancel();
                this.mFuture = null;
            }
            this.mCallback = null;
        }
    }

    public final void submitMoveTask(Uri uri) {
        if (this.mMoveScreenshotTask == null) {
            this.mMoveScreenshotTask = new MoveScreenshotTask(this.mMoveScreenshotCallback);
        }
        this.mIsMoveScreenFinished = false;
        this.mMoveScreenshotTask.execute(uri);
    }

    public /* synthetic */ void lambda$new$8(boolean z, String str) {
        this.mIsMoveScreenFinished = true;
        hideProcessDialog();
        if (!z && !TextUtils.isEmpty(str)) {
            doShare(Uri.parse(str));
        }
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        this.mScreenShellExecutor.mDoShellInterrupt = true;
        if (this.mPageMode == 0 && this.mIsFromSendMode) {
            switchPageMode(1);
        } else if (isFromNormalShare()) {
            if (this.mIsClickShare && checkIsShareAndDelete()) {
                deleteFileWithJudgeHasExport();
            } else {
                finish();
            }
        } else if (this.mPageMode == 0) {
            if (this.mIsClickShare && checkIsShareAndDelete()) {
                deleteFileWithJudgeHasExport();
            } else if (this.mHasExport) {
                deleteSourceAndExportAndFinish(true, false, false, false);
            } else {
                finish();
            }
        } else {
            switchPageMode(0);
        }
    }

    @Override // com.miui.gallery.app.activity.MiuiActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1) {
            this.mNeedExported = false;
        }
    }

    public final void switchPageMode(int i) {
        this.mPageMode = i;
        changeWithMode(i);
    }

    public final boolean hasEdited() {
        ILongCropEditorController iLongCropEditorController = this.mLongCropEditorController;
        boolean z = (iLongCropEditorController == null || iLongCropEditorController.onExport() == null || !this.mLongCropEditorController.onExport().isModified()) ? false : true;
        IScreenEditorController iScreenEditorController = this.mScreenEditorListener;
        return z || (iScreenEditorController != null && iScreenEditorController.isModified());
    }

    public boolean isSharePage() {
        return this.mPageMode == 1;
    }

    public final boolean isFromNormalShare() {
        return !this.mIsFromLongScreen && this.mIsFromSendMode;
    }

    public final boolean isFromNormalEdit() {
        return !this.mIsFromLongScreen && !this.mIsFromSendMode;
    }

    public ScreenShellExecutor getScreenShellExecutor() {
        return this.mScreenShellExecutor;
    }

    /* loaded from: classes2.dex */
    public class ScreenShellExecutor {
        public boolean mCurrentIsShellStatus;
        public boolean mDoShellInterrupt;
        public Request.Listener mShellLoadedListener = new Request.Listener() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity.ScreenShellExecutor.1
            @Override // com.miui.gallery.net.fetch.Request.Listener
            public void onFail() {
            }

            @Override // com.miui.gallery.net.fetch.Request.Listener
            public void onStart() {
            }

            {
                ScreenShellExecutor.this = this;
            }

            @Override // com.miui.gallery.net.fetch.Request.Listener
            public void onSuccess() {
                if (!ScreenShellExecutor.this.mDoShellInterrupt) {
                    ScreenShellExecutor screenShellExecutor = ScreenShellExecutor.this;
                    screenShellExecutor.doShell(ScreenEditorActivity.this);
                }
            }
        };

        public ScreenShellExecutor() {
            ScreenEditorActivity.this = r1;
        }

        public void release() {
            ShellResourceFetcher.INSTANCE.cancelAll();
        }

        public void doShell(FragmentActivity fragmentActivity) {
            if (ShellResourceFetcher.isResExist()) {
                if (ScreenEditorActivity.this.mScreenShellOperation == null) {
                    ScreenEditorActivity screenEditorActivity = ScreenEditorActivity.this;
                    screenEditorActivity.mScreenShellOperation = (IScreenShellOperation) screenEditorActivity.mScreenEditorListener.getScreenOperation(IScreenShellOperation.class);
                }
                ((IScreenCropOperation) ScreenEditorActivity.this.mScreenEditorListener.getScreenOperation(IScreenCropOperation.class)).resetCanvas(null);
                ScreenEditorActivity.this.mScreenEditorListener.setCurrentScreenEditor(7);
                boolean isWithShell = ScreenEditorActivity.this.mScreenShellOperation.isWithShell();
                ScreenEditorActivity.this.mDraftManager.setIsNeedSaveAsPng(isWithShell);
                ScreenEditorActivity.this.mScreenShellExecutor.mCurrentIsShellStatus = true;
                if (isWithShell) {
                    ScreenEditorActivity.this.mShellTextViewOn.setVisibility(4);
                    ScreenEditorActivity.this.mShellTextViewOff.setVisibility(0);
                    ScreenEditorActivity.this.mShellTextViewOn.setSelected(false);
                    ScreenEditorActivity.this.mShellTextViewOff.setSelected(true);
                    return;
                }
                ScreenEditorActivity.this.mShellTextViewOn.setVisibility(0);
                ScreenEditorActivity.this.mShellTextViewOff.setVisibility(4);
                ScreenEditorActivity.this.mShellTextViewOff.setSelected(false);
                ScreenEditorActivity.this.mShellTextViewOn.setSelected(true);
                return;
            }
            this.mDoShellInterrupt = false;
            ShellResourceFetcher.INSTANCE.checkFetch(fragmentActivity, this.mShellLoadedListener);
        }

        public void setViewStatus(boolean z) {
            if (z) {
                ScreenEditorActivity.this.mShellLayoutView.setBackgroundResource(R.drawable.screen_editor_shell_bg);
                ScreenEditorActivity.this.mShellImageView.setAlpha(0.6f);
                ScreenEditorActivity.this.mShellTextViewOn.setAlpha(0.6f);
                ScreenEditorActivity.this.mShellTextViewOff.setAlpha(0.6f);
                return;
            }
            ScreenEditorActivity.this.mShellLayoutView.setBackgroundResource(R.drawable.screen_editor_shell_background);
            ScreenEditorActivity.this.mShellImageView.setAlpha(1.0f);
            ScreenEditorActivity.this.mShellTextViewOn.setAlpha(1.0f);
            ScreenEditorActivity.this.mShellTextViewOff.setAlpha(1.0f);
        }

        public int getShellMarginBottom() {
            if (ScreenEditorActivity.this.mScreenShellOperation == null || ScreenEditorActivity.this.mScreenShellOperation.getShellFitMargin() == null || !ScreenEditorActivity.this.mScreenShellOperation.isWithShell()) {
                return 0;
            }
            return (int) ScreenEditorActivity.this.mScreenShellOperation.getShellFitMargin().bottom;
        }

        public int getShellMarginTop() {
            if (ScreenEditorActivity.this.mScreenShellOperation == null || ScreenEditorActivity.this.mScreenShellOperation.getShellFitMargin() == null || !ScreenEditorActivity.this.mScreenShellOperation.isWithShell()) {
                return 0;
            }
            return (int) ScreenEditorActivity.this.mScreenShellOperation.getShellFitMargin().top;
        }

        public boolean isShellFuncEnable() {
            boolean booleanExtra = ScreenEditorActivity.this.getIntent().getBooleanExtra("from_partial_screenshot", false);
            int[] intArrayExtra = ScreenEditorActivity.this.getIntent().getIntArrayExtra("ThumbnailRect");
            return ShellResourceFetcher.hasShellRes() && !ScreenEditorActivity.this.mIsLongScreenMode && (intArrayExtra != null && intArrayExtra.length == 4 && intArrayExtra[2] < intArrayExtra[3]) && !booleanExtra;
        }
    }
}
