package com.miui.gallery.collage;

import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.airbnb.lottie.LottieAnimationView;
import com.android.internal.WindowCompat;
import com.miui.gallery.R;
import com.miui.gallery.activity.ExternalPhotoPageActivity;
import com.miui.gallery.collage.BitmapManager;
import com.miui.gallery.collage.core.CollagePresenter;
import com.miui.gallery.collage.core.Effect;
import com.miui.gallery.collage.core.ViewInterface;
import com.miui.gallery.collage.core.stitching.StitchingPresenter;
import com.miui.gallery.picker.PickGalleryActivity;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.ClickUtils;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.OrientationCheckHelper;
import com.miui.gallery.util.SystemUiUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.market.MediaEditorInstaller;
import com.miui.mediaeditor.utils.MediaEditorUtils;
import java.io.File;
import java.util.ArrayList;
import miuix.appcompat.app.ProgressDialog;

/* loaded from: classes.dex */
public class CollageActivity extends FragmentActivity implements ViewInterface {
    public BitmapManager mBitmapManager;
    public TextView mCancel;
    public CollagePresenter mCurrentPresenter;
    public LottieAnimationView mDownloadMediaEditorAppView;
    public View mLoadingProgressView;
    public CollagePresenter[] mPresenters;
    public RadioGroup mRadioGroup;
    public View mRenderContainer;
    public Bitmap mReplaceBitmapFrom;
    public ViewGroup mRootView;
    public TextView mSave;
    public ProgressDialog mSaveProgressDialog;
    public int mCurrentImageSize = 1;
    public Rect mRectTemp = new Rect();
    public AnimParams downParams = new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build();
    public CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.miui.gallery.collage.CollageActivity.2
        {
            CollageActivity.this = this;
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            if (z) {
                CollageActivity.this.onSelectRadio(compoundButton);
            }
        }
    };
    public MediaEditorInstaller.Callback mInstallMediaEditorCallback = new MediaEditorInstaller.Callback() { // from class: com.miui.gallery.collage.CollageActivity.3
        {
            CollageActivity.this = this;
        }

        @Override // com.miui.gallery.util.market.MediaEditorInstaller.Callback
        public void onInstallSuccess() {
            if (CollageActivity.this.mDownloadMediaEditorAppView != null) {
                CollageActivity.this.mDownloadMediaEditorAppView.setVisibility(MediaEditorUtils.isMediaEditorAvailable() ? 8 : 0);
            }
        }
    };
    public View.OnClickListener mCancelListener = new View.OnClickListener() { // from class: com.miui.gallery.collage.CollageActivity.4
        {
            CollageActivity.this = this;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            LinearMotorHelper.performHapticFeedback(view, LinearMotorHelper.HAPTIC_TAP_NORMAL);
            CollageActivity.this.finish();
        }
    };
    public View.OnClickListener mSaveListener = new View.OnClickListener() { // from class: com.miui.gallery.collage.CollageActivity.5
        {
            CollageActivity.this = this;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            LinearMotorHelper.performHapticFeedback(view, LinearMotorHelper.HAPTIC_TAP_NORMAL);
            if (ClickUtils.isDoubleClick()) {
                DefaultLogger.d("CollageActivity", "click save btn, doubleClick ，time : " + System.currentTimeMillis());
                return;
            }
            DefaultLogger.d("CollageActivity", "click save btn, normal click ，time  : " + System.currentTimeMillis());
            if (CollageActivity.this.mCurrentPresenter == null || CollageActivity.this.mCurrentPresenter.isActivating()) {
                return;
            }
            CollageActivity.this.mCurrentPresenter.doSave(CollageActivity.this.mBitmapManager);
        }
    };
    public BitmapManager.BitmapLoaderListener mBitmapLoaderListener = new BitmapManager.BitmapLoaderListener() { // from class: com.miui.gallery.collage.CollageActivity.6
        {
            CollageActivity.this = this;
        }

        @Override // com.miui.gallery.collage.BitmapManager.BitmapLoaderListener
        public void onBitmapLoad(Bitmap[] bitmapArr) {
            CollageActivity.this.onBitmapDecodeFinishAfterChoose();
        }

        @Override // com.miui.gallery.collage.BitmapManager.BitmapLoaderListener
        public void onBitmapReplace(Bitmap bitmap, Bitmap bitmap2) {
            CollageActivity.this.notifyBitmapReplace(bitmap, bitmap2);
        }
    };

    /* loaded from: classes.dex */
    public interface ReplaceImageListener {
        void onReplace(Bitmap bitmap);
    }

    public static /* synthetic */ void $r8$lambda$67mOGAB1UoaIFvqpAhSCas2cwso(CollageActivity collageActivity, View view) {
        collageActivity.lambda$findViews$0(view);
    }

    @Override // com.miui.gallery.collage.core.ViewInterface
    public Context getContext() {
        return this;
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(null);
        if (!MediaEditorInstaller.getInstance().installIfNotExist(this, new MediaEditorInstaller.Callback() { // from class: com.miui.gallery.collage.CollageActivity.1
            {
                CollageActivity.this = this;
            }

            @Override // com.miui.gallery.util.market.MediaEditorInstaller.Callback
            public void onDialogConfirm() {
                CollageActivity.this.finish();
            }

            @Override // com.miui.gallery.util.market.MediaEditorInstaller.Callback
            public void onDialogCancel() {
                CollageActivity.this.finish();
            }
        }, false)) {
            return;
        }
        if (MediaEditorUtils.isMediaEditorAvailable()) {
            Intent intent = getIntent();
            intent.setComponent(new ComponentName("com.miui.mediaeditor", "com.miui.gallery.collage.CollageActivity"));
            intent.setPackage("com.miui.mediaeditor");
            startActivity(intent);
            finish();
        }
        WindowCompat.setCutoutModeShortEdges(getWindow());
        SystemUiUtil.setDrawSystemBarBackground(getWindow());
        SystemUiUtil.hideSystemBars(getWindow().getDecorView(), true, false, false);
        if (!OrientationCheckHelper.isSupportOrientationChange()) {
            SystemUiUtil.setRequestedOrientation(1, this);
        }
        setContentView(R.layout.collage_main);
        init();
        findViews();
        resolveIntent();
        initPresenter();
        initView();
    }

    public final void init() {
        this.mBitmapManager = new BitmapManager(this, this.mBitmapLoaderListener);
    }

    public final void initPresenter() {
        int length = Effect.values().length;
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < length; i++) {
            CollagePresenter generatePresenter = Effect.values()[i].generatePresenter();
            if (generatePresenter.supportImageSize(this.mCurrentImageSize)) {
                generatePresenter.setImageSize(this.mCurrentImageSize);
                arrayList.add(generatePresenter);
            }
        }
        CollagePresenter[] collagePresenterArr = new CollagePresenter[arrayList.size()];
        this.mPresenters = collagePresenterArr;
        arrayList.toArray(collagePresenterArr);
    }

    public final void initView() {
        CollagePresenter[] collagePresenterArr;
        for (CollagePresenter collagePresenter : this.mPresenters) {
            RadioButton radioButton = (RadioButton) LayoutInflater.from(this).inflate(R.layout.collage_radio_item, (ViewGroup) this.mRadioGroup, false);
            this.mRadioGroup.addView(radioButton);
            radioButton.setText(collagePresenter.getTitle());
            radioButton.setHapticFeedbackEnabled(false);
            FolmeUtil.setCustomTouchAnim(radioButton, this.downParams, null, null, null, true);
            radioButton.setContentDescription(radioButton.getText());
            radioButton.setOnCheckedChangeListener(this.mCheckedChangeListener);
        }
        RadioGroup radioGroup = this.mRadioGroup;
        radioGroup.check(radioGroup.getChildAt(0).getId());
        this.mCancel.setOnClickListener(this.mCancelListener);
        this.mSave.setOnClickListener(this.mSaveListener);
        getWindow().setNavigationBarColor(getResources().getColor(R.color.collage_background));
        this.mRenderContainer.setSystemUiVisibility(16);
    }

    @Override // androidx.fragment.app.FragmentActivity
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        CollagePresenter collagePresenter = this.mCurrentPresenter;
        if (collagePresenter != null) {
            collagePresenter.onAttachFragment(fragment);
        }
    }

    public final void onSelectRadio(View view) {
        onSelectPresenter(this.mRadioGroup.indexOfChild(view));
    }

    public final void onSelectPresenter(int i) {
        CollagePresenter collagePresenter = this.mPresenters[i];
        collagePresenter.attach(this);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
        CollagePresenter collagePresenter2 = this.mCurrentPresenter;
        if (collagePresenter2 != null) {
            beginTransaction.hide(supportFragmentManager.findFragmentByTag(collagePresenter2.getMenuFragmentTag()));
            beginTransaction.hide(supportFragmentManager.findFragmentByTag(this.mCurrentPresenter.getRenderFragmentTag()));
        }
        this.mCurrentPresenter = collagePresenter;
        String menuFragmentTag = collagePresenter.getMenuFragmentTag();
        String renderFragmentTag = collagePresenter.getRenderFragmentTag();
        Fragment findFragmentByTag = supportFragmentManager.findFragmentByTag(menuFragmentTag);
        Fragment findFragmentByTag2 = supportFragmentManager.findFragmentByTag(renderFragmentTag);
        if (findFragmentByTag2 == null) {
            beginTransaction.add(R.id.render_fragment_container, collagePresenter.getRenderFragment(), renderFragmentTag);
        } else {
            beginTransaction.show(findFragmentByTag2);
        }
        if (findFragmentByTag == null) {
            beginTransaction.add(R.id.menu_fragment_container, collagePresenter.getMenuFragment(), menuFragmentTag);
        } else {
            beginTransaction.show(findFragmentByTag);
        }
        beginTransaction.commitAllowingStateLoss();
        supportFragmentManager.executePendingTransactions();
    }

    public final void notifyBitmapsReceive() {
        if (this.mBitmapManager.isEmpty()) {
            return;
        }
        dismissLoading();
        for (CollagePresenter collagePresenter : this.mPresenters) {
            collagePresenter.notifyReceiveBitmaps();
        }
        enableButton();
    }

    public final void dismissLoading() {
        View view = this.mLoadingProgressView;
        if (view != null) {
            this.mRootView.removeView(view);
            this.mLoadingProgressView = null;
        }
    }

    public final void notifyBitmapReplace(Bitmap bitmap, Bitmap bitmap2) {
        for (CollagePresenter collagePresenter : this.mPresenters) {
            collagePresenter.notifyBitmapReplace(bitmap, bitmap2);
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        SystemUiUtil.hideSystemBars(getWindow().getDecorView(), true, false, false);
    }

    public final void resolveIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            SamplingStatHelper.recordCountEvent("collage", "collage_enter");
            receiveImages(intent.getClipData());
        }
    }

    public final Uri resolveUri(Uri uri) {
        return (uri == null || !uri.getAuthority().equals("com.miui.gallery.open")) ? uri : Uri.fromFile(new File(uri.getLastPathSegment()));
    }

    public final void findViews() {
        this.mRadioGroup = (RadioGroup) findViewById(R.id.collage_title_parent);
        this.mSave = (TextView) findViewById(R.id.collage_save);
        this.mCancel = (TextView) findViewById(R.id.collage_back);
        FolmeUtil.setCustomTouchAnim(this.mSave, this.downParams, null, null, null, true);
        FolmeUtil.setCustomTouchAnim(this.mCancel, this.downParams, null, null, null, true);
        this.mRenderContainer = findViewById(R.id.render_fragment_container);
        this.mLoadingProgressView = findViewById(R.id.progress_view);
        this.mRootView = (ViewGroup) findViewById(R.id.collage_root);
        LottieAnimationView lottieAnimationView = (LottieAnimationView) findViewById(R.id.download_mediaeditor_app_view);
        this.mDownloadMediaEditorAppView = lottieAnimationView;
        lottieAnimationView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.collage.CollageActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                CollageActivity.$r8$lambda$67mOGAB1UoaIFvqpAhSCas2cwso(CollageActivity.this, view);
            }
        });
        setDownloadMediaEditorAppViewAnimation();
    }

    public /* synthetic */ void lambda$findViews$0(View view) {
        MediaEditorInstaller.getInstance().installIfNotExist(this, this.mInstallMediaEditorCallback, true);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.mRenderContainer.getLayoutParams();
        ((ViewGroup.MarginLayoutParams) layoutParams).topMargin = getResources().getDimensionPixelSize(R.dimen.collage_render_margin_top);
        ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = getResources().getDimensionPixelSize(R.dimen.collage_render_margin_bottom);
        this.mRenderContainer.setLayoutParams(layoutParams);
    }

    public final void setDownloadMediaEditorAppViewAnimation() {
        this.mDownloadMediaEditorAppView.setVisibility(MediaEditorUtils.isMediaEditorAvailable() ? 8 : 0);
        this.mDownloadMediaEditorAppView.setAnimation(MiscUtil.isNightMode(getContext()) ? R.raw.download_mediaeditor_app_black : R.raw.download_mediaeditor_app_white);
        this.mDownloadMediaEditorAppView.playAnimation();
    }

    public final void startPicker(int i, int i2) {
        Intent intent = new Intent(this, PickGalleryActivity.class);
        intent.setType("image/*");
        intent.putExtra("pick-upper-bound", i2);
        startActivityForResult(intent, i);
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        DefaultLogger.d("CollageActivity", "onActivityResult");
        StorageSolutionProvider.get().onHandleRequestPermissionResult(this, i, i2, intent);
        if (i == 0) {
            if (i2 == -1) {
                receiveImages(intent.getClipData());
            } else {
                finish();
            }
        } else if (i != 1 || i2 != -1) {
        } else {
            Uri resolveUri = resolveUri(intent.getData());
            if (unSupportFile(resolveUri)) {
                ToastUtils.makeText(this, (int) R.string.secret_reason_type_not_support);
            } else {
                this.mBitmapManager.replaceBitmap(this.mReplaceBitmapFrom, resolveUri);
            }
        }
    }

    public final boolean unSupportFile(Uri uri) {
        return !CollageUtils.isMimeTypeSupport(BaseFileMimeUtil.getMimeType(uri.getPath()));
    }

    @Override // com.miui.gallery.collage.core.ViewInterface
    public void onSaving() {
        showProgressDialog();
    }

    @Override // com.miui.gallery.collage.core.ViewInterface
    public void onSaveFinish(String str, boolean z) {
        DefaultLogger.d("CollageActivity", "save bitmap finished ， outPath : " + str + " ，success : " + z);
        dismissSaveProgressDialog();
        if (z) {
            if (!TextUtils.isEmpty(str)) {
                gotoPhotoPage(str);
            }
            setResult(-1);
            finish();
        } else if (this.mCurrentPresenter instanceof StitchingPresenter) {
            ToastUtils.makeText(this, getResources().getString(R.string.save_error_try_a_different_template_msg));
        } else {
            ToastUtils.makeText(this, getResources().getString(R.string.main_save_error_msg));
        }
    }

    @Override // com.miui.gallery.collage.core.ViewInterface
    public void onReplaceBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        DefaultLogger.d("CollageActivity", "replace bitmap : %s", bitmap);
        this.mReplaceBitmapFrom = bitmap;
        startPicker(1, 1);
    }

    @Override // com.miui.gallery.collage.core.ViewInterface
    public Bitmap[] getBitmaps() {
        return this.mBitmapManager.data();
    }

    public final void showProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        this.mSaveProgressDialog = progressDialog;
        progressDialog.setMessage(getString(R.string.photo_editor_saving));
        this.mSaveProgressDialog.setCancelable(false);
        this.mSaveProgressDialog.setCanceledOnTouchOutside(false);
        this.mSaveProgressDialog.setIndeterminate(true);
        this.mSaveProgressDialog.show();
    }

    public final void gotoPhotoPage(String str) {
        Intent intent = new Intent(this, ExternalPhotoPageActivity.class);
        intent.setData(Uri.fromFile(new File(str)));
        intent.putExtra("com.miui.gallery.extra.deleting_include_cloud", true);
        startActivity(intent);
    }

    public final void receiveImages(ClipData clipData) {
        int min = Math.min(clipData.getItemCount(), 6);
        Uri[] uriArr = new Uri[min];
        for (int i = 0; i < min; i++) {
            uriArr[i] = resolveUri(clipData.getItemAt(i).getUri());
        }
        this.mCurrentImageSize = min;
        decodeBitmapAsync(uriArr);
    }

    public final void decodeBitmapAsync(Uri[] uriArr) {
        this.mBitmapManager.loadBitmapAsync(uriArr);
    }

    public final void onBitmapDecodeFinishAfterChoose() {
        int min = Math.min(getIntent().getClipData().getItemCount(), 6);
        if (min <= 0 || min != this.mBitmapManager.size()) {
            ToastUtils.makeText(this, (int) R.string.secret_reason_type_not_support);
            finish();
            return;
        }
        this.mCurrentImageSize = this.mBitmapManager.size();
        notifyBitmapsReceive();
    }

    public final void enableButton() {
        this.mSave.setEnabled(true);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        View view;
        if (motionEvent.getAction() == 0 && (view = this.mRenderContainer) != null) {
            view.getGlobalVisibleRect(this.mRectTemp);
            if (!this.mRectTemp.contains(Math.round(motionEvent.getX()), Math.round(motionEvent.getY()))) {
                this.mCurrentPresenter.dismissControlWindow();
            }
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public final void dismissSaveProgressDialog() {
        ProgressDialog progressDialog = this.mSaveProgressDialog;
        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }
        this.mSaveProgressDialog.dismiss();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        dismissSaveProgressDialog();
        CollagePresenter[] collagePresenterArr = this.mPresenters;
        if (collagePresenterArr != null) {
            for (CollagePresenter collagePresenter : collagePresenterArr) {
                collagePresenter.detach();
            }
        }
        BitmapManager bitmapManager = this.mBitmapManager;
        if (bitmapManager != null) {
            bitmapManager.setBitmapLoaderListener(null);
        }
        MediaEditorInstaller.getInstance().removeInstallListener();
        super.onDestroy();
    }
}
