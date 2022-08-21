package com.miui.gallery.editor.photo.app;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.activity.AndroidActivity;
import com.miui.gallery.compat.app.ActivityCompat;
import com.miui.gallery.editor.photo.app.ExportFragment;
import com.miui.gallery.editor.photo.app.InitializeController;
import com.miui.gallery.editor.photo.app.privacy.PrivacyWatermarkHelper;
import com.miui.gallery.editor.photo.origin.EditorOriginHandler;
import com.miui.gallery.permission.core.Permission;
import com.miui.gallery.permission.core.PermissionCheckCallback;
import com.miui.gallery.permission.core.PermissionInjector;
import com.miui.gallery.util.SystemUiUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.mediaeditor.utils.FilePermissionUtils;
import com.miui.privacy.LockSettingsHelper;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.b.h;
import java.io.FileNotFoundException;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class PrivacyWatermarkActivity extends AndroidActivity implements View.OnClickListener, PermissionCheckCallback, TextWatcher {
    public Intent mActivityIntent;
    public ImageView mCancel;
    public EditText mCaptionText;
    public View mContainer;
    public DraftManager mDraftmanager;
    public EditorOriginHandler mEditorOriginHandler;
    public long mEndTime;
    public ExportTask mExportTask;
    public InitializeController mInitializeController;
    public boolean mIsInMultiWindowMode;
    public boolean mNeedConfirmPassword;
    public ImageView mOk;
    public ImageView mPreviewImage;
    public Bitmap mRawPreviewBitmap;
    public long mStartTime;
    public String mWatermarkText;
    public TextView mWordCountView;
    public int mWordLimit;
    public int mActivityResult = 0;
    public boolean mIsKeyboardShowing = false;
    public ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.miui.gallery.editor.photo.app.PrivacyWatermarkActivity.1
        {
            PrivacyWatermarkActivity.this = this;
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            PrivacyWatermarkActivity.this.mContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            if (PrivacyWatermarkActivity.this.mCaptionText == null) {
                return;
            }
            View decorView = PrivacyWatermarkActivity.this.mo546getActivity().getWindow().getDecorView();
            Rect rect = new Rect();
            decorView.getWindowVisibleDisplayFrame(rect);
            int height = decorView.getHeight() - rect.bottom;
            if (!PrivacyWatermarkActivity.this.mIsKeyboardShowing && height > 200) {
                PrivacyWatermarkActivity.this.mIsKeyboardShowing = true;
            }
            if (height >= 200 || !PrivacyWatermarkActivity.this.mIsKeyboardShowing) {
                return;
            }
            PrivacyWatermarkActivity.this.mIsKeyboardShowing = false;
            PrivacyWatermarkActivity.this.mCaptionText.clearFocus();
        }
    };
    public ExportFragment.Callbacks mExportCallbacks = new ExportFragment.Callbacks() { // from class: com.miui.gallery.editor.photo.app.PrivacyWatermarkActivity.2
        {
            PrivacyWatermarkActivity.this = this;
        }

        @Override // com.miui.gallery.editor.photo.app.ExportFragment.Callbacks
        public boolean doExport() {
            DraftManager draftManager = PrivacyWatermarkActivity.this.mDraftmanager;
            if (draftManager == null) {
                return false;
            }
            DefaultLogger.w("PrivacyWatermarkActivity", "[Export] start");
            PrivacyWatermarkActivity.this.mExportTask.prepareToExport(draftManager);
            if (!FilePermissionUtils.checkFileCreatePermission(PrivacyWatermarkActivity.this.mo546getActivity(), PrivacyWatermarkActivity.this.mExportTask.getExportUri().getPath())) {
                return false;
            }
            if (PrivacyWatermarkActivity.this.mWatermarkText != null && !PrivacyWatermarkActivity.this.mWatermarkText.isEmpty()) {
                PrivacyWatermarkActivity.this.mDraftmanager.setIsWatermarkAdded(true);
            }
            return PrivacyWatermarkActivity.this.mExportTask.onExport(draftManager, PrivacyWatermarkActivity.this.mDraftmanager.export(PrivacyWatermarkActivity.this.drawWatermark(draftManager.decodeOrigin(), PrivacyWatermarkActivity.this.mWatermarkText), PrivacyWatermarkActivity.this.mExportTask.getExportUri()));
        }

        @Override // com.miui.gallery.editor.photo.app.ExportFragment.Callbacks
        public void onCancelled(boolean z) {
            PrivacyWatermarkActivity.this.mExportTask.onCancelled(z);
        }

        @Override // com.miui.gallery.editor.photo.app.ExportFragment.Callbacks
        public void onExported(boolean z) {
            PrivacyWatermarkActivity.this.mExportTask.onPostExport(z);
            if (z) {
                if (PrivacyWatermarkActivity.this.mExportTask.isExternalCall()) {
                    return;
                }
                PrivacyWatermarkActivity.this.mEndTime = SystemClock.elapsedRealtime();
                PrivacyWatermarkActivity.this.trackExportClick();
                Intent intent = new Intent();
                intent.setDataAndType(PrivacyWatermarkActivity.this.mExportTask.getExportUri(), "image/jpeg");
                intent.putExtra("photo_secret_id", PrivacyWatermarkActivity.this.mExportTask.getSecretId());
                PrivacyWatermarkActivity.this.setActivityResult(-1, intent);
                PrivacyWatermarkActivity.this.mExportTask.closeExportDialog();
                ActivityCompat.finishAfterTransition(PrivacyWatermarkActivity.this);
                return;
            }
            PrivacyWatermarkActivity.this.mExportTask.closeExportDialog();
            ToastUtils.makeText(PrivacyWatermarkActivity.this, (int) R.string.main_save_error_msg);
        }
    };
    public InitializeController.Callbacks mDecoderCallbacks = new InitializeController.Callbacks() { // from class: com.miui.gallery.editor.photo.app.PrivacyWatermarkActivity.3
        {
            PrivacyWatermarkActivity.this = this;
        }

        @Override // com.miui.gallery.editor.photo.app.InitializeController.Callbacks
        public int doInitialize() {
            try {
                DraftManager draftManager = PrivacyWatermarkActivity.this.mDraftmanager;
                return draftManager != null ? draftManager.initializeForPreview(PrivacyWatermarkActivity.this.mEditorOriginHandler.isInMainProcess()) : false ? 3 : 2;
            } catch (FileNotFoundException e) {
                DefaultLogger.w("PrivacyWatermarkActivity", e);
                return 1;
            } catch (SecurityException e2) {
                DefaultLogger.w("PrivacyWatermarkActivity", e2);
                return 2;
            }
        }

        @Override // com.miui.gallery.editor.photo.app.InitializeController.Callbacks
        public void onDone() {
            if (PrivacyWatermarkActivity.this.mDraftmanager != null) {
                PrivacyWatermarkActivity privacyWatermarkActivity = PrivacyWatermarkActivity.this;
                privacyWatermarkActivity.mRawPreviewBitmap = privacyWatermarkActivity.mDraftmanager.getPreviewOriginal();
                PrivacyWatermarkActivity privacyWatermarkActivity2 = PrivacyWatermarkActivity.this;
                privacyWatermarkActivity2.updatePreviewImage(privacyWatermarkActivity2.mRawPreviewBitmap);
                PrivacyWatermarkActivity.this.updatePreviewWithWatermark();
            }
        }
    };

    public static /* synthetic */ void $r8$lambda$yoJd77AYAcDay8A9c0aaBtuuF_k(PrivacyWatermarkActivity privacyWatermarkActivity, View view, boolean z) {
        privacyWatermarkActivity.lambda$initView$0(view, z);
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(null);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        Uri data = intent.getData();
        if (data == null) {
            finish();
            return;
        }
        this.mDraftmanager = new DraftManager(this, data, getIntent().getExtras());
        ExportTask from = ExportTask.from(this);
        this.mExportTask = from;
        if (from == null) {
            ToastUtils.makeText(this, (int) R.string.image_invalid_path);
            return;
        }
        this.mStartTime = SystemClock.elapsedRealtime();
        SystemUiUtil.setRequestedOrientation(1, this);
        setContentView(R.layout.privacy_watermark_main);
        this.mEditorOriginHandler = new EditorOriginHandler(this, this.mExportTask.getSource());
        this.mIsInMultiWindowMode = ActivityCompat.isInMultiWindowMode(this);
        SystemUiUtil.showSystemBars(getWindow().getDecorView(), false, this.mIsInMultiWindowMode);
        initView();
        PermissionInjector.injectIfNeededIn(this, this, null);
        if (!this.mDraftmanager.isSecret()) {
            return;
        }
        getWindow().addFlags(8192);
    }

    public final void initView() {
        View findViewById = findViewById(R.id.container);
        this.mContainer = findViewById;
        findViewById.getViewTreeObserver().addOnGlobalLayoutListener(this.mOnGlobalLayoutListener);
        ImageView imageView = (ImageView) findViewById(R.id.preview_image);
        this.mPreviewImage = imageView;
        ViewCompat.setTransitionName(imageView, "tag_transition_view");
        ImageView imageView2 = (ImageView) findViewById(R.id.privacy_watermark_btn_done);
        this.mOk = imageView2;
        imageView2.setOnClickListener(this);
        ImageView imageView3 = (ImageView) findViewById(R.id.privacy_watermark_btn_cancel);
        this.mCancel = imageView3;
        imageView3.setOnClickListener(this);
        this.mCaptionText = (EditText) findViewById(R.id.privacy_watermark_edit);
        this.mWordCountView = (TextView) findViewById(R.id.privacy_watermark_count_text);
        this.mWatermarkText = getResources().getString(R.string.photo_editor_watermark_default);
        this.mCaptionText.addTextChangedListener(this);
        this.mCaptionText.requestFocus();
        this.mCaptionText.setText(this.mWatermarkText);
        this.mCaptionText.setSelection(this.mWatermarkText.length());
        this.mCaptionText.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: com.miui.gallery.editor.photo.app.PrivacyWatermarkActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnFocusChangeListener
            public final void onFocusChange(View view, boolean z) {
                PrivacyWatermarkActivity.$r8$lambda$yoJd77AYAcDay8A9c0aaBtuuF_k(PrivacyWatermarkActivity.this, view, z);
            }
        });
        setWordMaxLength(14);
        updateWordCount();
    }

    /* renamed from: showOrCloseSoftKeyboard */
    public final void lambda$initView$0(View view, boolean z) {
        if (view == null || view.getWindowToken() == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService("input_method");
        if (z) {
            inputMethodManager.showSoftInput(view, 0);
        } else {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        this.mIsKeyboardShowing = z;
    }

    @Override // com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        if (isNeedConfirmPassword()) {
            this.mNeedConfirmPassword = false;
            LockSettingsHelper.startAuthenticatePasswordActivity(this, 27);
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

    @Override // com.miui.gallery.app.activity.AndroidActivity, android.app.Activity
    public void onMultiWindowModeChanged(boolean z, Configuration configuration) {
        super.onMultiWindowModeChanged(z, configuration);
        this.mIsInMultiWindowMode = z;
        if (z) {
            SystemUiUtil.showSystemBars(getWindow().getDecorView(), this.mIsInMultiWindowMode);
        } else {
            SystemUiUtil.showSystemBars(getWindow().getDecorView(), false, this.mIsInMultiWindowMode);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.privacy_watermark_btn_cancel) {
            doCancel();
        } else if (id != R.id.privacy_watermark_btn_done) {
        } else {
            doSave();
        }
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        doCancel();
    }

    public final void doCancel() {
        this.mEndTime = SystemClock.elapsedRealtime();
        trackCancelClick();
        ActivityCompat.finishAfterTransition(this);
    }

    @Override // android.app.Activity
    public void finishAfterTransition() {
        lambda$initView$0(this.mCaptionText, false);
        super.finishAfterTransition();
    }

    public final void doSave() {
        this.mExportTask.showExportDialog();
    }

    @Override // androidx.fragment.app.FragmentActivity
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof ExportFragment) {
            ((ExportFragment) fragment).setCallbacks(this.mExportCallbacks);
        }
    }

    public final boolean isNeedConfirmPassword() {
        DraftManager draftManager = this.mDraftmanager;
        return draftManager != null && draftManager.isSecret() && this.mNeedConfirmPassword;
    }

    public final void updatePreviewImage(Bitmap bitmap) {
        this.mPreviewImage.setImageBitmap(bitmap);
    }

    public final void updateWordCount() {
        int length = this.mWatermarkText.length();
        this.mWordCountView.setText(length + h.g + this.mWordLimit);
    }

    public final void setWordMaxLength(int i) {
        EditText editText = this.mCaptionText;
        if (editText == null || i <= 0) {
            return;
        }
        this.mWordLimit = i;
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(i)});
    }

    @Override // com.miui.gallery.permission.core.PermissionCheckCallback
    public Permission[] getRuntimePermissions() {
        return new Permission[]{new Permission("android.permission.WRITE_EXTERNAL_STORAGE", getString(R.string.permission_storage_desc), true)};
    }

    @Override // com.miui.gallery.permission.core.PermissionCheckCallback
    public void onPermissionsChecked(Permission[] permissionArr, int[] iArr, boolean[] zArr) {
        InitializeController initializeController = new InitializeController(this, this.mDecoderCallbacks);
        this.mInitializeController = initializeController;
        initializeController.doInitialize();
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
        if (editable != null) {
            this.mWatermarkText = editable.toString();
            updateWordCount();
            updatePreviewWithWatermark();
            if (editable.length() > 0) {
                this.mOk.setEnabled(true);
                return;
            } else {
                this.mOk.setEnabled(false);
                return;
            }
        }
        this.mOk.setEnabled(false);
    }

    public final Bitmap drawWatermark(Bitmap bitmap, String str) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width <= 0 || height <= 0) {
            return bitmap;
        }
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        int originWidth = this.mDraftmanager.getOriginWidth();
        int originHeight = this.mDraftmanager.getOriginHeight();
        if (originHeight == height && originWidth == width) {
            PrivacyWatermarkHelper.drawWatermark(canvas, this.mWatermarkText, width, height, 0);
        } else {
            Bitmap createBitmap2 = Bitmap.createBitmap(originWidth, originHeight, Bitmap.Config.ARGB_8888);
            PrivacyWatermarkHelper.drawWatermark(new Canvas(createBitmap2), this.mWatermarkText, originWidth, originHeight, 0);
            canvas.drawBitmap(Bitmap.createScaledBitmap(createBitmap2, width, height, false), 0.0f, 0.0f, (Paint) null);
        }
        return createBitmap;
    }

    public final void updatePreviewWithWatermark() {
        String str = this.mWatermarkText;
        Bitmap bitmap = this.mRawPreviewBitmap;
        if (bitmap != null) {
            if (str == null || str.isEmpty()) {
                updatePreviewImage(bitmap);
            } else {
                updatePreviewImage(drawWatermark(bitmap, str));
            }
        }
    }

    public void setActivityResult(int i, Intent intent) {
        this.mActivityResult = i;
        this.mActivityIntent = intent;
        prepareResult(intent);
        intent.putExtra("extra_photo_edit_type", "extra_photo_editor_type_watermark");
        setResult(this.mActivityResult, this.mActivityIntent);
    }

    public final void setPhotoSecretFinishResult() {
        if (this.mActivityIntent == null) {
            this.mActivityIntent = new Intent();
        }
        prepareResult(this.mActivityIntent);
        this.mActivityIntent.putExtra("photo_secret_finish", true);
        this.mActivityIntent.putExtra("extra_photo_edit_type", "extra_photo_editor_type_watermark");
        setResult(this.mActivityResult, this.mActivityIntent);
    }

    public final void prepareResult(Intent intent) {
        DraftManager draftManager = this.mDraftmanager;
        if (draftManager != null) {
            intent.putExtra("photo_edit_exported_width", draftManager.getExportedWidth());
            intent.putExtra("photo_edit_exported_height", this.mDraftmanager.getExportedHeight());
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        EditorOriginHandler editorOriginHandler = this.mEditorOriginHandler;
        if (editorOriginHandler != null) {
            editorOriginHandler.onStart();
        }
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

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        DraftManager draftManager = this.mDraftmanager;
        if (draftManager != null) {
            draftManager.release();
            this.mDraftmanager = null;
        }
        ExportTask exportTask = this.mExportTask;
        if (exportTask != null) {
            exportTask.closeExportDialog();
        }
    }

    public final void trackCancelClick() {
        HashMap hashMap = new HashMap(4, 1.0f);
        hashMap.put("tip", "403.67.2.1.16480");
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "Cancel");
        hashMap.put("duration", Long.valueOf(this.mEndTime - this.mStartTime));
        hashMap.put("value", this.mWatermarkText);
        TrackController.trackClick(hashMap);
    }

    public final void trackExportClick() {
        HashMap hashMap = new HashMap(4, 1.0f);
        hashMap.put("tip", "403.67.2.1.16480");
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "Save");
        hashMap.put("duration", Long.valueOf(this.mEndTime - this.mStartTime));
        hashMap.put("value", this.mWatermarkText);
        TrackController.trackClick(hashMap);
    }
}
