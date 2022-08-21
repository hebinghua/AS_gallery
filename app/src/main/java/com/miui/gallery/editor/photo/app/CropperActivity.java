package com.miui.gallery.editor.photo.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import ch.qos.logback.core.joran.action.Action;
import com.edmodo.cropper.CropImageView;
import com.miui.gallery.R;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.app.StrategyContext;
import com.miui.gallery.content.ExtendedAsyncTaskLoader;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.OrientationCheckHelper;
import com.miui.gallery.util.SystemUiUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.GalleryDialogFragment;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import miuix.appcompat.app.ProgressDialog;

/* loaded from: classes2.dex */
public class CropperActivity extends BaseActivity {
    public View mCancelButton;
    public CropImageView mCropView;
    public Uri mData;
    public Uri mOutput;
    public float mOutputX;
    public float mOutputY;
    public boolean mReturnData;
    public View mSaveButton;
    public View.OnClickListener mOnClickListener = new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.app.CropperActivity.1
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view != CropperActivity.this.mCancelButton) {
                if (view != CropperActivity.this.mSaveButton) {
                    return;
                }
                new ExportFragment().showAllowingStateLoss(CropperActivity.this.getSupportFragmentManager(), "ExportFragment");
                return;
            }
            CropperActivity.this.finish();
        }
    };
    public LoaderManager.LoaderCallbacks<DecodeResult> mCallbacks = new LoaderManager.LoaderCallbacks<DecodeResult>() { // from class: com.miui.gallery.editor.photo.app.CropperActivity.2
        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader<DecodeResult> loader) {
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public /* bridge */ /* synthetic */ void onLoadFinished(Loader<DecodeResult> loader, DecodeResult decodeResult) {
            onLoadFinished2((Loader) loader, decodeResult);
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader<DecodeResult> onCreateLoader(int i, Bundle bundle) {
            CropperActivity cropperActivity = CropperActivity.this;
            return new PrepareLoader(cropperActivity, cropperActivity.mData);
        }

        /* renamed from: onLoadFinished  reason: avoid collision after fix types in other method */
        public void onLoadFinished2(Loader loader, DecodeResult decodeResult) {
            if (decodeResult.mBitmap != null) {
                CropperActivity.this.mCropView.setImageBitmap(decodeResult.mBitmap);
                CropperActivity.this.mSaveButton.setEnabled(true);
                return;
            }
            Exception exc = decodeResult.mException;
            if (exc != null) {
                DefaultLogger.w("CropperActivity", exc);
            }
            Toast.makeText(CropperActivity.this, (int) R.string.image_decode_failed, 0).show();
            CropperActivity.this.finish();
        }
    };

    @Override // com.miui.gallery.activity.BaseActivity
    public boolean needShowUserAgreements() {
        return true;
    }

    @Override // com.miui.gallery.app.activity.GalleryActivity, miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    @SuppressLint({"MissingSuperCall"})
    public void onSaveInstanceState(Bundle bundle) {
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        Object obj;
        super.onCreate(bundle);
        if (!OrientationCheckHelper.isSupportOrientationChange()) {
            SystemUiUtil.setRequestedOrientation(1, this);
        }
        requestDisableStrategy(StrategyContext.DisableStrategyType.NAVIGATION_BAR);
        setContentView(R.layout.crop_activity);
        this.mSaveButton = findViewById(R.id.ok);
        this.mCancelButton = findViewById(R.id.cancel);
        this.mSaveButton.setOnClickListener(this.mOnClickListener);
        this.mCancelButton.setOnClickListener(this.mOnClickListener);
        this.mCropView = (CropImageView) findViewById(R.id.crop_view);
        this.mSaveButton.setEnabled(false);
        Intent intent = getIntent();
        this.mData = intent.getData();
        this.mOutput = (Uri) intent.getParcelableExtra("output");
        this.mReturnData = intent.getBooleanExtra("return-data", false);
        this.mOutputX = intent.getIntExtra("outputX", -1);
        this.mOutputY = intent.getIntExtra("outputY", -1);
        Uri uri = this.mData;
        if (uri == null || (obj = this.mOutput) == null) {
            DefaultLogger.e("CropperActivity", "src or des missed, src: %s, des: %s", uri, this.mOutput);
            finish();
            return;
        }
        if (obj == null) {
            obj = "bytes";
        }
        DefaultLogger.d("CropperActivity", "cropper's input: %s, output: %s", uri, obj);
        int intExtra = intent.getIntExtra("aspectX", -1);
        int intExtra2 = intent.getIntExtra("aspectY", -1);
        if (intExtra > 0 && intExtra2 > 0) {
            this.mCropView.setFixedAspectRatio(intent.getBooleanExtra("fixed_aspect_ratio", true));
            this.mCropView.setAspectRatio(intExtra, intExtra2);
        }
        getSupportLoaderManager().initLoader(0, null, this.mCallbacks);
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.MiuiActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 100) {
            setResult(i2);
            finish();
        }
    }

    public final Bitmap createOutput() {
        long currentTimeMillis = System.currentTimeMillis();
        Bitmap croppedImage = this.mCropView.getCroppedImage();
        if (this.mOutputX > 0.0f && this.mOutputY > 0.0f) {
            Matrix matrix = new Matrix();
            matrix.setScale(this.mOutputX / croppedImage.getWidth(), this.mOutputY / croppedImage.getHeight());
            croppedImage = Bitmap.createBitmap(croppedImage, 0, 0, croppedImage.getWidth(), croppedImage.getHeight(), matrix, true);
        }
        DefaultLogger.d("CropperActivity", "create output costs %dms", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        return croppedImage;
    }

    /* loaded from: classes2.dex */
    public static class DecodeResult {
        public Bitmap mBitmap;
        public Exception mException;

        public DecodeResult(Bitmap bitmap, Exception exc) {
            this.mBitmap = bitmap;
            this.mException = exc;
        }
    }

    /* loaded from: classes2.dex */
    public static class PrepareLoader extends ExtendedAsyncTaskLoader<DecodeResult> {
        public DecodeResult mDecodeResult;
        public Uri mUri;

        public PrepareLoader(Context context, Uri uri) {
            super(context);
            this.mUri = uri;
        }

        @Override // androidx.loader.content.AsyncTaskLoader
        /* renamed from: loadInBackground */
        public DecodeResult mo1444loadInBackground() {
            InputStream inputStream;
            Throwable th;
            InputStream inputStream2;
            InputStream openInputStream;
            try {
                try {
                    openInputStream = getContext().getContentResolver().openInputStream(this.mUri);
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (FileNotFoundException e) {
                e = e;
                inputStream2 = null;
            } catch (SecurityException e2) {
                e = e2;
                inputStream2 = null;
            } catch (Throwable th3) {
                inputStream = null;
                th = th3;
            }
            try {
                if (openInputStream == null) {
                    DefaultLogger.d("CropperActivity", "no stream return.");
                    DecodeResult decodeResult = new DecodeResult(null, null);
                    if (openInputStream != null) {
                        try {
                            openInputStream.close();
                        } catch (IOException e3) {
                            DefaultLogger.w("CropperActivity", e3);
                        }
                    }
                    return decodeResult;
                }
                inputStream2 = new BufferedInputStream(openInputStream);
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = sampleSize(this.mUri);
                    Bitmap decodeStream = BitmapFactory.decodeStream(inputStream2, null, options);
                    int photoRotation = getPhotoRotation(this.mUri);
                    if (photoRotation != 0) {
                        DefaultLogger.d("CropperActivity", "rotate image by %d", Integer.valueOf(photoRotation));
                        decodeStream = rotateBitmap(photoRotation, decodeStream);
                    }
                    DefaultLogger.d("CropperActivity", "sample size is %dx%d", Integer.valueOf(options.outWidth), Integer.valueOf(options.outHeight));
                    DecodeResult decodeResult2 = new DecodeResult(decodeStream, null);
                    try {
                        inputStream2.close();
                    } catch (IOException e4) {
                        DefaultLogger.w("CropperActivity", e4);
                    }
                    return decodeResult2;
                } catch (FileNotFoundException e5) {
                    e = e5;
                    DefaultLogger.e("CropperActivity", e);
                    DecodeResult decodeResult3 = new DecodeResult(null, e);
                    if (inputStream2 != null) {
                        try {
                            inputStream2.close();
                        } catch (IOException e6) {
                            DefaultLogger.w("CropperActivity", e6);
                        }
                    }
                    return decodeResult3;
                } catch (SecurityException e7) {
                    e = e7;
                    DefaultLogger.w("CropperActivity", e);
                    DecodeResult decodeResult4 = new DecodeResult(null, e);
                    if (inputStream2 != null) {
                        try {
                            inputStream2.close();
                        } catch (IOException e8) {
                            DefaultLogger.w("CropperActivity", e8);
                        }
                    }
                    return decodeResult4;
                }
            } catch (FileNotFoundException e9) {
                inputStream2 = openInputStream;
                e = e9;
            } catch (SecurityException e10) {
                inputStream2 = openInputStream;
                e = e10;
            } catch (Throwable th4) {
                th = th4;
                inputStream = openInputStream;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e11) {
                        DefaultLogger.w("CropperActivity", e11);
                    }
                }
                throw th;
            }
        }

        public final int sampleSize(Uri uri) {
            InputStream openInputStream;
            BufferedInputStream bufferedInputStream;
            BufferedInputStream bufferedInputStream2 = null;
            try {
                try {
                    try {
                        openInputStream = getContext().getContentResolver().openInputStream(uri);
                    } catch (IOException e) {
                        DefaultLogger.w("CropperActivity", e);
                    }
                } catch (FileNotFoundException e2) {
                    e = e2;
                }
                if (openInputStream == null) {
                    if (openInputStream != null) {
                        openInputStream.close();
                    }
                    return 1;
                }
                try {
                    bufferedInputStream = new BufferedInputStream(openInputStream);
                } catch (FileNotFoundException e3) {
                    bufferedInputStream2 = openInputStream;
                    e = e3;
                } catch (Throwable th) {
                    bufferedInputStream2 = openInputStream;
                    th = th;
                }
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(bufferedInputStream, null, options);
                    int max = Math.max(options.outHeight / 2048, options.outWidth / 2048);
                    try {
                        bufferedInputStream.close();
                    } catch (IOException e4) {
                        DefaultLogger.w("CropperActivity", e4);
                    }
                    return max;
                } catch (FileNotFoundException e5) {
                    e = e5;
                    bufferedInputStream2 = bufferedInputStream;
                    DefaultLogger.w("CropperActivity", e);
                    if (bufferedInputStream2 != null) {
                        bufferedInputStream2.close();
                    }
                    return 1;
                } catch (Throwable th2) {
                    th = th2;
                    bufferedInputStream2 = bufferedInputStream;
                    if (bufferedInputStream2 != null) {
                        try {
                            bufferedInputStream2.close();
                        } catch (IOException e6) {
                            DefaultLogger.w("CropperActivity", e6);
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:41:0x0053 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Type inference failed for: r5v0, types: [android.net.Uri] */
        /* JADX WARN: Type inference failed for: r5v4 */
        /* JADX WARN: Type inference failed for: r5v7, types: [java.io.InputStream] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final int getPhotoRotation(android.net.Uri r5) {
            /*
                r4 = this;
                java.lang.String r0 = "CropperActivity"
                r1 = 0
                android.content.Context r2 = r4.getContext()     // Catch: java.lang.Throwable -> L38 java.io.FileNotFoundException -> L3d
                android.content.ContentResolver r2 = r2.getContentResolver()     // Catch: java.lang.Throwable -> L38 java.io.FileNotFoundException -> L3d
                java.io.InputStream r1 = r2.openInputStream(r5)     // Catch: java.lang.Throwable -> L38 java.io.FileNotFoundException -> L3d
                if (r1 == 0) goto L2d
                java.io.BufferedInputStream r5 = new java.io.BufferedInputStream     // Catch: java.lang.Throwable -> L38 java.io.FileNotFoundException -> L3d
                r5.<init>(r1)     // Catch: java.lang.Throwable -> L38 java.io.FileNotFoundException -> L3d
                com.miui.gallery.util.ExifUtil$ExifCreator<androidx.exifinterface.media.ExifInterface> r1 = com.miui.gallery.util.ExifUtil.sSupportExifCreator     // Catch: java.io.FileNotFoundException -> L2b java.lang.Throwable -> L50
                java.lang.Object r1 = r1.mo1691create(r5)     // Catch: java.io.FileNotFoundException -> L2b java.lang.Throwable -> L50
                androidx.exifinterface.media.ExifInterface r1 = (androidx.exifinterface.media.ExifInterface) r1     // Catch: java.io.FileNotFoundException -> L2b java.lang.Throwable -> L50
                int r1 = com.miui.gallery.util.ExifUtil.getRotationDegrees(r1)     // Catch: java.io.FileNotFoundException -> L2b java.lang.Throwable -> L50
                r5.close()     // Catch: java.io.IOException -> L26
                goto L2a
            L26:
                r5 = move-exception
                com.miui.gallery.util.logger.DefaultLogger.w(r0, r5)
            L2a:
                return r1
            L2b:
                r1 = move-exception
                goto L41
            L2d:
                java.lang.String r5 = "no stream opened"
                com.miui.gallery.util.logger.DefaultLogger.e(r0, r5)     // Catch: java.lang.Throwable -> L38 java.io.FileNotFoundException -> L3d
                if (r1 == 0) goto L4e
                r1.close()     // Catch: java.io.IOException -> L4a
                goto L4e
            L38:
                r5 = move-exception
                r3 = r1
                r1 = r5
                r5 = r3
                goto L51
            L3d:
                r5 = move-exception
                r3 = r1
                r1 = r5
                r5 = r3
            L41:
                com.miui.gallery.util.logger.DefaultLogger.w(r0, r1)     // Catch: java.lang.Throwable -> L50
                if (r5 == 0) goto L4e
                r5.close()     // Catch: java.io.IOException -> L4a
                goto L4e
            L4a:
                r5 = move-exception
                com.miui.gallery.util.logger.DefaultLogger.w(r0, r5)
            L4e:
                r5 = 0
                return r5
            L50:
                r1 = move-exception
            L51:
                if (r5 == 0) goto L5b
                r5.close()     // Catch: java.io.IOException -> L57
                goto L5b
            L57:
                r5 = move-exception
                com.miui.gallery.util.logger.DefaultLogger.w(r0, r5)
            L5b:
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.editor.photo.app.CropperActivity.PrepareLoader.getPhotoRotation(android.net.Uri):int");
        }

        public final Bitmap rotateBitmap(int i, Bitmap bitmap) {
            Matrix matrix = new Matrix();
            matrix.preRotate(i);
            try {
                return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            } catch (OutOfMemoryError unused) {
                DefaultLogger.e("CropperActivity", "rotateBitmap OutOfMemoryError");
                return null;
            }
        }

        @Override // com.miui.gallery.content.ExtendedAsyncTaskLoader, androidx.loader.content.Loader
        public void onStartLoading() {
            super.onStartLoading();
            DecodeResult decodeResult = this.mDecodeResult;
            if (decodeResult == null) {
                forceLoad();
            } else {
                deliverResult(decodeResult);
            }
        }

        @Override // androidx.loader.content.Loader
        public void deliverResult(DecodeResult decodeResult) {
            if (!isReset()) {
                this.mDecodeResult = decodeResult;
            }
            if (isStarted()) {
                super.deliverResult((PrepareLoader) decodeResult);
            }
        }

        @Override // androidx.loader.content.Loader
        public void onReset() {
            super.onReset();
            this.mDecodeResult = null;
        }
    }

    /* loaded from: classes2.dex */
    public static class ExportLoader extends ExtendedAsyncTaskLoader<Boolean> {
        public WeakReference<CropperActivity> mActivityWeakReference;
        public Uri mOut;
        public Boolean mResult;

        public ExportLoader(CropperActivity cropperActivity, Uri uri) {
            super(cropperActivity);
            this.mOut = uri;
            this.mActivityWeakReference = new WeakReference<>(cropperActivity);
        }

        @Override // androidx.loader.content.AsyncTaskLoader
        /* renamed from: loadInBackground */
        public Boolean mo1444loadInBackground() {
            Bitmap.CompressFormat compressFormat;
            DefaultLogger.d("CropperActivity", "start export in background.");
            CropperActivity cropperActivity = this.mActivityWeakReference.get();
            if (cropperActivity == null) {
                return Boolean.FALSE;
            }
            Bitmap createOutput = cropperActivity.createOutput();
            if (createOutput == null) {
                return Boolean.FALSE;
            }
            if (Action.FILE_ATTRIBUTE.equals(this.mOut.getScheme())) {
                compressFormat = GalleryUtils.convertExtensionToCompressFormat(BaseFileUtils.getExtension(this.mOut.getPath()));
            } else {
                String type = getContext().getContentResolver().getType(this.mOut);
                compressFormat = "image/png".equals(type) ? Bitmap.CompressFormat.PNG : "image/webp".equals(type) ? Bitmap.CompressFormat.WEBP : Bitmap.CompressFormat.JPEG;
            }
            BufferedOutputStream bufferedOutputStream = null;
            try {
                try {
                    try {
                        OutputStream openOutputStream = getContext().getContentResolver().openOutputStream(this.mOut);
                        if (openOutputStream != null) {
                            BufferedOutputStream bufferedOutputStream2 = new BufferedOutputStream(openOutputStream);
                            try {
                                Boolean valueOf = Boolean.valueOf(createOutput.compress(compressFormat, 100, bufferedOutputStream2));
                                try {
                                    bufferedOutputStream2.close();
                                } catch (IOException e) {
                                    DefaultLogger.w("CropperActivity", e);
                                } catch (Exception e2) {
                                    DefaultLogger.e("CropperActivity", e2);
                                }
                                return valueOf;
                            } catch (FileNotFoundException e3) {
                                e = e3;
                                bufferedOutputStream = bufferedOutputStream2;
                                DefaultLogger.w("CropperActivity", e);
                                if (bufferedOutputStream != null) {
                                    bufferedOutputStream.close();
                                }
                                return Boolean.FALSE;
                            } catch (SecurityException e4) {
                                e = e4;
                                bufferedOutputStream = bufferedOutputStream2;
                                DefaultLogger.w("CropperActivity", e);
                                if (bufferedOutputStream != null) {
                                    bufferedOutputStream.close();
                                }
                                return Boolean.FALSE;
                            } catch (Exception e5) {
                                e = e5;
                                bufferedOutputStream = bufferedOutputStream2;
                                DefaultLogger.e("CropperActivity", e);
                                if (bufferedOutputStream != null) {
                                    bufferedOutputStream.close();
                                }
                                return Boolean.FALSE;
                            } catch (Throwable th) {
                                th = th;
                                bufferedOutputStream = bufferedOutputStream2;
                                if (bufferedOutputStream != null) {
                                    try {
                                        bufferedOutputStream.close();
                                    } catch (IOException e6) {
                                        DefaultLogger.w("CropperActivity", e6);
                                    } catch (Exception e7) {
                                        DefaultLogger.e("CropperActivity", e7);
                                    }
                                }
                                throw th;
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                    }
                } catch (FileNotFoundException e8) {
                    e = e8;
                } catch (SecurityException e9) {
                    e = e9;
                } catch (Exception e10) {
                    e = e10;
                }
            } catch (IOException e11) {
                DefaultLogger.w("CropperActivity", e11);
            } catch (Exception e12) {
                DefaultLogger.e("CropperActivity", e12);
            }
            return Boolean.FALSE;
        }

        @Override // com.miui.gallery.content.ExtendedAsyncTaskLoader, androidx.loader.content.Loader
        public void onStartLoading() {
            super.onStartLoading();
            Boolean bool = this.mResult;
            if (bool == null) {
                forceLoad();
            } else {
                deliverResult(bool);
            }
        }

        @Override // androidx.loader.content.Loader
        public void deliverResult(Boolean bool) {
            super.deliverResult((ExportLoader) bool);
            this.mResult = bool;
            if (isStarted()) {
                super.deliverResult((ExportLoader) bool);
            }
        }

        @Override // androidx.loader.content.Loader
        public void onReset() {
            super.onReset();
        }
    }

    /* loaded from: classes2.dex */
    public static class ExportFragment extends GalleryDialogFragment {
        public LoaderManager.LoaderCallbacks<Boolean> mCallbacks = new LoaderManager.LoaderCallbacks<Boolean>() { // from class: com.miui.gallery.editor.photo.app.CropperActivity.ExportFragment.1
            @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
            public void onLoaderReset(Loader<Boolean> loader) {
            }

            @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
            public /* bridge */ /* synthetic */ void onLoadFinished(Loader<Boolean> loader, Boolean bool) {
                onLoadFinished2((Loader) loader, bool);
            }

            @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
            public Loader<Boolean> onCreateLoader(int i, Bundle bundle) {
                return new ExportLoader(ExportFragment.this.mCropper, ExportFragment.this.mCropper.mOutput);
            }

            /* renamed from: onLoadFinished  reason: avoid collision after fix types in other method */
            public void onLoadFinished2(Loader loader, Boolean bool) {
                if (!bool.booleanValue()) {
                    Toast.makeText(ExportFragment.this.mCropper, (int) R.string.main_save_error_msg, 0).show();
                } else {
                    Intent intent = new Intent();
                    intent.setData(((ExportLoader) loader).mOut);
                    ExportFragment.this.mCropper.setResult(-1, intent);
                }
                ExportFragment.this.mCropper.finish();
            }
        };
        public CropperActivity mCropper;

        @Override // androidx.fragment.app.Fragment
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            if (activity instanceof CropperActivity) {
                this.mCropper = (CropperActivity) activity;
                return;
            }
            throw new IllegalStateException("can't attach to install of " + activity.getClass().getSimpleName());
        }

        @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            getLoaderManager().initLoader(0, null, this.mCallbacks);
        }

        @Override // androidx.fragment.app.DialogFragment
        /* renamed from: onCreateDialog */
        public Dialog mo1072onCreateDialog(Bundle bundle) {
            ProgressDialog progressDialog = new ProgressDialog(this.mCropper);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage(getActivity().getString(R.string.photo_editor_saving));
            return progressDialog;
        }

        @Override // androidx.fragment.app.Fragment
        public void onDestroy() {
            super.onDestroy();
            LoaderManager loaderManager = getLoaderManager();
            if (loaderManager == null || !loaderManager.hasRunningLoaders()) {
                return;
            }
            loaderManager.destroyLoader(0);
        }
    }

    @Override // com.miui.gallery.activity.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        LoaderManager supportLoaderManager = getSupportLoaderManager();
        if (supportLoaderManager == null || !supportLoaderManager.hasRunningLoaders()) {
            return;
        }
        supportLoaderManager.destroyLoader(0);
    }
}
