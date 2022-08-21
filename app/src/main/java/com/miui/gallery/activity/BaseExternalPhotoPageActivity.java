package com.miui.gallery.activity;

import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Trace;
import android.text.TextUtils;
import android.util.Pair;
import android.util.Size;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import androidx.fragment.app.Fragment;
import com.android.internal.CompatHandler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.miui.display.DisplayFeatureHelper;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.app.StrategyContext;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.glide.load.model.GalleryModel;
import com.miui.gallery.glide.load.resource.bitmap.GalleryDownsampleStrategy;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.permission.core.Permission;
import com.miui.gallery.permission.core.PermissionUtils;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.PhotoPageFragment;
import com.miui.gallery.ui.album.main.utils.factory.GalleryViewCreator;
import com.miui.gallery.ui.photoPage.EnterTypeUtils;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseFeatureUtil;
import com.miui.gallery.util.BrightnessProvider;
import com.miui.gallery.util.CameraPreviewManager;
import com.miui.gallery.util.DecodeInfoHelper;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.MediaStoreUtils;
import com.miui.gallery.util.ProcessingMediaHelper;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.SystemUiUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.photoview.preload.PhotoPagePreloadHelper;
import com.miui.gallery.util.photoview.preload.PreloadHelperProvider;
import com.miui.gallery.view.BrightnessManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public abstract class BaseExternalPhotoPageActivity extends BaseActivity implements BrightnessProvider, PreloadHelperProvider {
    public static final Map<String, Pair<Boolean, Boolean>> sPRE_LOAD_SUCCESS_MAPS = new HashMap(2);
    public BrightnessManager mBrightnessManager;
    public final PhotoPagePreloadHelper mPreloadHelper = new PhotoPagePreloadHelper();
    public Runnable mRecordEventRunnable;

    public static int getScreenOrientation(int i) {
        if (i != 0) {
            if (i == 90) {
                return 8;
            }
            return i != 270 ? -1 : 0;
        }
        return 1;
    }

    public void doIfFromCamera(Intent intent, Bundle bundle) {
    }

    @Override // com.miui.gallery.activity.BaseActivity
    public int getFragmentContainerId() {
        return 16908290;
    }

    @Override // com.miui.gallery.activity.BaseActivity
    public boolean hasCustomContentView() {
        return true;
    }

    public boolean isTranslucent() {
        return false;
    }

    @Override // com.miui.gallery.util.BrightnessProvider
    public float getAutoBrightness() {
        BrightnessManager brightnessManager = this.mBrightnessManager;
        if (brightnessManager == null) {
            return -1.0f;
        }
        return brightnessManager.getAutoBrightness();
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        try {
            Trace.beginSection("BasePhotoActivityCreate");
            DefaultLogger.d("photoPageStartup", "activity onCreate");
            getLayoutInflater().setFactory2(GalleryViewCreator.getViewFactory());
            Intent intent = getIntent();
            if (intent != null && intent.getData() != null) {
                if (isCameraPreview()) {
                    prepareFromCamera();
                } else if (isCustomWidgetPreview()) {
                    prepareFromWidget();
                }
                this.mPreloadHelper.preloadPhotoPageInfo(this);
                Trace.beginSection("onCreateInternal");
                super.onCreate(bundle);
                Trace.endSection();
                if (BaseBuildUtil.isLargeScreen(this)) {
                    requestDisableStrategy(StrategyContext.DisableStrategyType.NAVIGATION_BAR);
                }
                recordCountEvent();
                return;
            }
            super.onCreate(bundle);
        } finally {
            Trace.endSection();
        }
    }

    public void recordCountEvent() {
        CompatHandler workHandler = ThreadManager.getWorkHandler();
        Runnable runnable = new Runnable() { // from class: com.miui.gallery.activity.BaseExternalPhotoPageActivity.1
            @Override // java.lang.Runnable
            public void run() {
                DefaultLogger.d("photoPageStartup", "recordEnterEvent");
                String callingPackage = IntentUtil.getCallingPackage(BaseExternalPhotoPageActivity.this);
                if (TextUtils.isEmpty(callingPackage)) {
                    callingPackage = "Unknown";
                }
                HashMap hashMap = new HashMap(1, 1.0f);
                hashMap.put("calling_package", callingPackage);
                SamplingStatHelper.recordCountEvent("photo", "external_view_photo", hashMap);
            }
        };
        this.mRecordEventRunnable = runnable;
        workHandler.post(runnable);
    }

    @Override // miuix.appcompat.app.AppCompatActivity, android.app.Activity
    public void finish() {
        finishAndRemoveTask();
    }

    @Override // android.app.Activity
    public void onActivityReenter(int i, Intent intent) {
        PhotoPageFragment photoPageFragment = (PhotoPageFragment) getSupportFragmentManager().findFragmentByTag("PhotoPageFragment");
        if (photoPageFragment != null) {
            photoPageFragment.onActivityReenter(i, intent);
        }
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.MiuiActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        PhotoPageFragment photoPageFragment;
        super.onActivityResult(i, i2, intent);
        if (getSupportFragmentManager().getBackStackEntryCount() != 0 || (photoPageFragment = (PhotoPageFragment) getSupportFragmentManager().findFragmentByTag("PhotoPageFragment")) == null) {
            return;
        }
        photoPageFragment.onActivityResult(i, i2, intent);
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.permission.core.PermissionCheckCallback
    public void onPermissionsChecked(Permission[] permissionArr, int[] iArr, boolean[] zArr) {
        final Uri data;
        try {
            Trace.beginSection("onPermissionsChecked");
            super.onPermissionsChecked(permissionArr, iArr, zArr);
            final Intent intent = getIntent();
            if (intent != null && (data = intent.getData()) != null) {
                startFragment(new BaseActivity.FragmentCreator() { // from class: com.miui.gallery.activity.BaseExternalPhotoPageActivity$$ExternalSyntheticLambda0
                    @Override // com.miui.gallery.activity.BaseActivity.FragmentCreator
                    public final Fragment create(String str) {
                        Fragment lambda$onPermissionsChecked$0;
                        lambda$onPermissionsChecked$0 = BaseExternalPhotoPageActivity.this.lambda$onPermissionsChecked$0(data, intent, str);
                        return lambda$onPermissionsChecked$0;
                    }
                }, "PhotoPageFragment", false, true);
                return;
            }
            DefaultLogger.e("BaseExternalPhotoPageActivity", "intent or uri is null.");
            finish();
            startMainActivity();
        } finally {
            Trace.endSection();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Fragment lambda$onPermissionsChecked$0(Uri uri, Intent intent, String str) {
        return PhotoPageFragment.newInstance(uri, intent.getType(), getArguments(), isTranslucent() ? 1 : 0);
    }

    public final Bundle getArguments() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            extras = new Bundle();
        }
        if (extras.getBoolean("using_deputy_screen", false)) {
            extras.putLong("support_operation_mask", 1048577L);
        }
        String callingPackage = IntentUtil.getCallingPackage(this);
        if (isCameraPreview()) {
            extras.putParcelable("photo_page_enter_type", EnterTypeUtils.EnterType.FROM_CAMERA);
        } else if (isCustomWidgetPreview()) {
            extras.putParcelable("photo_page_enter_type", EnterTypeUtils.EnterType.FROM_CUSTOM_WIDGET);
            extras.putBoolean("com.miui.gallery.extra.deleting_include_cloud", true);
        } else if (TextUtils.equals(callingPackage, "android-app://com.miui.screenrecorder")) {
            extras.putParcelable("photo_page_enter_type", EnterTypeUtils.EnterType.FROM_SCREEN_RECORDER);
        } else if (TextUtils.equals(callingPackage, "android-app://com.android.mms")) {
            extras.putParcelable("photo_page_enter_type", EnterTypeUtils.EnterType.FROM_MESSAGE);
            if (Build.VERSION.SDK_INT > 29) {
                DefaultLogger.d("BaseExternalPhotoPageActivity", "set PREVIEW_SINGLE is true by rLimit!");
                extras.putBoolean("com.miui.gallery.extra.preview_single_item", true);
            }
        } else if (TextUtils.equals(callingPackage, "android-app://com.android.fileexplorer") || TextUtils.equals(callingPackage, "android-app://com.mi.android.globalFileexplorer")) {
            extras.putParcelable("photo_page_enter_type", EnterTypeUtils.EnterType.FROM_FILE_MANAGER);
        } else if (callingPackage != null && callingPackage.contains("com.miui.notes")) {
            extras.putParcelable("photo_page_enter_type", EnterTypeUtils.EnterType.FROM_FILE_MANAGER);
        } else {
            extras.putParcelable("photo_page_enter_type", EnterTypeUtils.EnterType.FROM_COMMON_EXTERNAL);
        }
        return extras;
    }

    public final void startMainActivity() {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setPackage(getPackageName());
        intent.addFlags(67108864);
        startActivity(intent);
    }

    @Override // com.miui.gallery.activity.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        BrightnessManager brightnessManager = this.mBrightnessManager;
        if (brightnessManager != null) {
            brightnessManager.onPause();
        }
        if (isCameraPreview()) {
            setScreenEffect(false);
        }
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        try {
            Trace.beginSection("BaseExternalPhotoAc#onResume");
            super.onResume();
            Trace.beginSection("BaseExternalPhotoAc#internal");
            BrightnessManager brightnessManager = this.mBrightnessManager;
            if (brightnessManager != null) {
                brightnessManager.onResume();
            }
            if (isCameraPreview()) {
                setScreenEffect(true);
            }
            Trace.endSection();
        } finally {
            Trace.endSection();
        }
    }

    @Override // com.miui.gallery.app.activity.MiuiActivity, android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        BrightnessManager brightnessManager = this.mBrightnessManager;
        if (brightnessManager != null) {
            brightnessManager.onWindowFocusChanged(z);
        }
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        PhotoPageFragment photoPageFragment;
        if (getSupportFragmentManager().getBackStackEntryCount() != 0 || (photoPageFragment = (PhotoPageFragment) getSupportFragmentManager().findFragmentByTag("PhotoPageFragment")) == null || !photoPageFragment.isVisible() || !photoPageFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    public final void prepareFromWidget() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras == null) {
            return;
        }
        Uri data = intent.getData();
        String uri = data.toString();
        int i = extras.getInt("photo_init_position");
        intent.putExtra("photo_count", extras.getInt("photo_count"));
        intent.putExtra("photo_init_position", i);
        long longExtra = intent.getLongExtra("photo_id", -1L);
        if (longExtra == -1) {
            DefaultLogger.d("BaseExternalPhotoPageActivity", "can find the id of photo");
            return;
        }
        String mineTypeFromUri = MediaStoreUtils.getMineTypeFromUri(data);
        Size imageSize = getImageSize(false);
        intent.putExtra("photo_transition_data", new ImageLoadParams.Builder().setKey(longExtra).setFilePath(uri).setTargetSize(Config$ThumbConfig.get().sMicroTargetSize).setInitPosition(i).setMimeType(mineTypeFromUri).setRequestOptions((RequestOptions) GlideOptions.bigPhotoOf().mo978skipMemoryCache(false).mo974priority(Priority.HIGH).mo970override(Math.min(imageSize.getWidth(), imageSize.getHeight()))).build());
        if (BaseBuildUtil.isLowRamDevice()) {
            Pair<Boolean, Boolean> pair = sPRE_LOAD_SUCCESS_MAPS.get(uri);
            if (pair != null && !((Boolean) pair.first).booleanValue() && !((Boolean) pair.second).booleanValue()) {
                return;
            }
            preloadThumbnail(uri, false, false, mineTypeFromUri, -1L);
            return;
        }
        preloadThumbnail(uri, false, false, mineTypeFromUri, -1L);
    }

    public final void prepareFromCamera() {
        String str;
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras == null) {
            return;
        }
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        int screenOrientation = getScreenOrientation(extras.getInt("device_orientation", Integer.MAX_VALUE));
        intent.putExtra("enter_orientation", screenOrientation);
        if (screenOrientation != -1 && getResources().getConfiguration().orientation != screenOrientation) {
            DefaultLogger.d("BaseExternalPhotoPageActivity", "prepareFromCamera requiredScreenOrientation:" + screenOrientation);
            SystemUiUtil.setRequestedOrientation(screenOrientation, this);
        }
        Uri data = intent.getData();
        String uri = data.toString();
        boolean z = true;
        intent.putExtra("photo_count", 1);
        intent.putExtra("photo_init_position", 0);
        long parseId = ContentUris.parseId(data);
        String string = extras.getString("photo_mime_type", null);
        String mineTypeFromUri = string == null ? MediaStoreUtils.getMineTypeFromUri(data) : string;
        Size imageSize = getImageSize(ProcessingMediaHelper.getInstance().isBlurred(uri));
        int i = extras.getInt("photo_width", -1);
        int i2 = extras.getInt("photo_height", -1);
        ProcessingMediaHelper.ProcessingItem matchItem = ProcessingMediaHelper.getInstance().matchItem(uri);
        boolean z2 = matchItem != null;
        if (matchItem == null || !matchItem.isBlurred()) {
            str = uri;
            z = false;
        } else {
            str = uri;
        }
        long j = extras.getLong("extra_file_length", -1L);
        String str2 = str;
        ImageLoadParams build = new ImageLoadParams.Builder().setKey(parseId).setFilePath(str2).setFileLength(j).setTargetSize(imageSize).setInitPosition(0).setMimeType(mineTypeFromUri).setImageWidth(i).setImageHeight(i2).setRequestOptions(buildRequestOptions(z2, z, j)).build();
        intent.putExtra("photo_transition_data", build);
        DefaultLogger.v("BaseExternalPhotoPageActivity", "prepared from camera: [%s]", build);
        DecodeInfoHelper.DecodeInfo decodeInfo = new DecodeInfoHelper.DecodeInfo();
        decodeInfo.mediaUri = Uri.parse(str2);
        decodeInfo.width = i;
        decodeInfo.height = i2;
        DecodeInfoHelper.getInstance().put(decodeInfo);
        if (BaseBuildUtil.isLowRamDevice()) {
            Pair<Boolean, Boolean> pair = sPRE_LOAD_SUCCESS_MAPS.get(str2);
            if (pair == null || ((Boolean) pair.first).booleanValue() != z2 || ((Boolean) pair.second).booleanValue() != z) {
                preloadThumbnail(str2, z2, z, mineTypeFromUri, j);
            }
        } else {
            preloadThumbnail(str2, z2, z, mineTypeFromUri, j);
        }
        String str3 = "camera-brightness-manual";
        if (extras.getInt(str3, -1) == -1) {
            str3 = "camera-brightness";
        }
        this.mBrightnessManager = new BrightnessManager(this, (extras.getInt(str3, -1) * 1.0f) / 255.0f, extras.getFloat("camera-brightness-auto", -1.0f));
        doIfFromCamera(intent, extras);
    }

    public final void setScreenEffect(boolean z) {
        DisplayFeatureHelper.setScreenEffect(z);
    }

    public final boolean isCustomWidgetPreview() {
        Intent intent = getIntent();
        return intent != null && intent.getBooleanExtra("from_custom_widget", false);
    }

    public final boolean isCameraPreview() {
        Intent intent = getIntent();
        return intent != null && intent.getBooleanExtra("from_MiuiCamera", false);
    }

    public static Size getImageSize(boolean z) {
        if (z && !BaseFeatureUtil.isDisableFastBlur()) {
            return Config$ThumbConfig.get().sMicroTargetSize;
        }
        return new Size(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());
    }

    public static Target<Bitmap> preloadThumbnail(String str, String str2, long j) {
        return preloadThumbnail(str, ProcessingMediaHelper.getInstance().isMediaInProcessing(str), ProcessingMediaHelper.getInstance().isBlurred(str), str2, j);
    }

    public static Target<Bitmap> preloadThumbnail(final String str, final boolean z, final boolean z2, String str2, long j) {
        if (!PermissionUtils.checkPermission(GalleryApp.sGetAndroidContext(), "android.permission.WRITE_EXTERNAL_STORAGE")) {
            DefaultLogger.w("BaseExternalPhotoPageActivity", "Can't access external storage, relate permission is ungranted");
            return null;
        } else if ("image/x-adobe-dng".equals(str2)) {
            return null;
        } else {
            DefaultLogger.d("photoPageStartup", "preLoadThumbnail uri [%s], isTemp [%b], isBlurred [%b], mimeType [%s], fileLength [%d]", str, Boolean.valueOf(z), Boolean.valueOf(z2), str2, Long.valueOf(j));
            return Glide.with(GalleryApp.sGetAndroidContext()).mo985asBitmap().mo962load(GalleryModel.of(str)).mo946apply((BaseRequestOptions<?>) buildRequestOptions(z, z2, j)).mo945addListener(new RequestListener<Bitmap>() { // from class: com.miui.gallery.activity.BaseExternalPhotoPageActivity.2
                @Override // com.bumptech.glide.request.RequestListener
                public boolean onLoadFailed(GlideException glideException, Object obj, Target<Bitmap> target, boolean z3) {
                    DefaultLogger.e("BaseExternalPhotoPageActivity", "preload for [%s] failed, error: %s.", str, glideException != null ? glideException.getMessage() : "");
                    return false;
                }

                @Override // com.bumptech.glide.request.RequestListener
                public boolean onResourceReady(Bitmap bitmap, Object obj, Target<Bitmap> target, DataSource dataSource, boolean z3) {
                    if (BaseBuildUtil.isLowRamDevice()) {
                        BaseExternalPhotoPageActivity.sPRE_LOAD_SUCCESS_MAPS.put(str, Pair.create(Boolean.valueOf(z), Boolean.valueOf(z2)));
                    }
                    if (!z) {
                        DefaultLogger.d("BaseExternalPhotoPageActivity", "preload for [%s] success, invalidate camera preview.", str);
                        CameraPreviewManager.getInstance().invalid(str);
                        return false;
                    }
                    return false;
                }
            }).preload();
        }
    }

    public static RequestOptions buildRequestOptions(boolean z, boolean z2, long j) {
        RequestOptions mo970override;
        Size imageSize = getImageSize(z2);
        if (z2 && !BaseFeatureUtil.isDisableFastBlur()) {
            mo970override = GlideOptions.microThumbOf(j).mo974priority(Priority.HIGH).mo971override(imageSize.getWidth(), imageSize.getHeight());
        } else {
            mo970override = GlideOptions.bigPhotoOf(j).mo978skipMemoryCache(false).mo952downsample(GalleryDownsampleStrategy.CENTER_INSIDE).mo969optionalTransform((Transformation<Bitmap>) new CenterInside()).mo974priority(Priority.HIGH).mo970override(Math.min(imageSize.getWidth(), imageSize.getHeight()));
        }
        if (z) {
            mo970override = Config$ThumbConfig.markAsTemp(mo970override);
        }
        return z2 ? Config$ThumbConfig.applyProcessingOptions(mo970override) : mo970override;
    }

    @Override // com.miui.gallery.util.BrightnessProvider
    public float getManualBrightness() {
        BrightnessManager brightnessManager = this.mBrightnessManager;
        if (brightnessManager == null) {
            return -1.0f;
        }
        return brightnessManager.getManualBrightness();
    }

    @Override // com.miui.gallery.activity.BaseActivity
    public void onCtaChecked(boolean z, boolean z2) {
        PhotoPageFragment photoPageFragment = (PhotoPageFragment) getSupportFragmentManager().findFragmentByTag("PhotoPageFragment");
        if (photoPageFragment != null) {
            photoPageFragment.onCtaChecked(z);
        }
    }

    @Override // com.miui.gallery.activity.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        if (this.mRecordEventRunnable != null) {
            ThreadManager.getWorkHandler().removeCallbacks(this.mRecordEventRunnable);
        }
    }

    @Override // com.miui.gallery.util.photoview.preload.PreloadHelperProvider
    public PhotoPagePreloadHelper providePreloadHelper() {
        return this.mPreloadHelper;
    }

    @Override // com.miui.gallery.activity.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        sPRE_LOAD_SUCCESS_MAPS.clear();
        this.mPreloadHelper.release();
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> list, Menu menu, int i) {
        PhotoPageFragment photoPageFragment = (PhotoPageFragment) getSupportFragmentManager().findFragmentByTag("PhotoPageFragment");
        if (photoPageFragment != null) {
            photoPageFragment.onProvideKeyboardShortcuts(list, menu, i);
        }
        super.onProvideKeyboardShortcuts(list, menu, i);
    }

    @Override // android.app.Activity
    public boolean onKeyShortcut(int i, KeyEvent keyEvent) {
        PhotoPageFragment photoPageFragment = (PhotoPageFragment) getSupportFragmentManager().findFragmentByTag("PhotoPageFragment");
        if (photoPageFragment == null || !photoPageFragment.onKeyShortcut(i, keyEvent)) {
            return super.onKeyShortcut(i, keyEvent);
        }
        return true;
    }
}
