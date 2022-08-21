package com.miui.gallery.editor.photo.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import androidx.documentfile.provider.DocumentFile;
import com.bumptech.glide.Glide;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.RenderEngine;
import com.miui.gallery.editor.photo.utils.BigBitmapLoadUtils;
import com.miui.gallery.editor.photo.utils.Callback;
import com.miui.gallery.glide.load.model.PreloadModel;
import com.miui.gallery.glide.util.GlideLoadingUtils;
import com.miui.gallery.model.SecretInfo;
import com.miui.gallery.provider.CloudUtils;
import com.miui.gallery.sdk.editor.Constants;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.Bitmaps;
import com.miui.gallery.util.CryptoUtil;
import com.miui.gallery.util.ExifUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.FileUtils;
import com.miui.gallery.util.IoUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery3d.exif.ExifInterface;
import com.miui.gallery3d.exif.ExifTag;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class DraftManager {
    public Bundle mBundle;
    public Context mContext;
    public float mDownSampleSize;
    public RenderEngine[] mEngines;
    public ExifInterface mExif;
    public int mExportedHeight;
    public int mExportedWidth;
    public boolean mIsFavorite;
    public boolean mIsNeedSaveAsPng;
    public boolean mIsPreviewSameWithOrigin;
    public boolean mIsScreenshot;
    public boolean mIsSingleEffectMode;
    public boolean mIsWatermarkAdded;
    public String mMimeType;
    public OnPreviewRefreshListener mOnPreviewRefreshListener;
    public int mOriginHeight;
    public int mOriginWidth;
    public int mPreferHeight;
    public int mPreferWidth;
    public Bitmap mPreview;
    public volatile boolean mPreviewEnable;
    public int mPreviewHeight;
    public Bitmap mPreviewOriginal;
    public int mPreviewWidth;
    public List<RenderData> mRenderDataList;
    public int mRotationDegree;
    public SecretInfo mSecretInfo;
    public Uri mSource;
    public androidx.exifinterface.media.ExifInterface mSupportExif;
    public volatile boolean mWithWatermark;
    public XmpExtraManager mXmpExtraManager;

    /* loaded from: classes2.dex */
    public interface OnPreviewRefreshListener {
        void onRefresh(Bitmap bitmap);
    }

    public DraftManager(Context context, Uri uri, Bundle bundle) {
        this(context, uri, bundle, true);
    }

    public DraftManager(Context context, Uri uri, Bundle bundle, boolean z) {
        DisplayMetrics displayMetrics;
        this.mEngines = new RenderEngine[Effect.values().length];
        this.mRenderDataList = new ArrayList();
        this.mXmpExtraManager = new XmpExtraManager();
        boolean z2 = true;
        this.mWithWatermark = true;
        this.mIsWatermarkAdded = false;
        this.mContext = context;
        this.mSource = uri;
        this.mBundle = bundle;
        this.mWithWatermark = z;
        Context context2 = this.mContext;
        if (context2 instanceof Activity) {
            displayMetrics = new DisplayMetrics();
            ((Activity) this.mContext).getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
        } else {
            displayMetrics = context2.getResources().getDisplayMetrics();
        }
        this.mPreferWidth = displayMetrics.widthPixels;
        this.mPreferHeight = displayMetrics.heightPixels - this.mContext.getResources().getDimensionPixelSize(R.dimen.photo_editor_menu_panel_height);
        DefaultLogger.d("DraftManager", "prefer width %d, prefer height %d", Integer.valueOf(this.mPreferWidth), Integer.valueOf(this.mPreferHeight));
        if (!FileUtils.isScreenShot(this.mSource) && (bundle == null || !bundle.getBoolean(Constants.EXTRA_IS_SCREENSHOT))) {
            z2 = false;
        }
        this.mIsScreenshot = z2;
        SecretInfo secretInfo = new SecretInfo();
        this.mSecretInfo = secretInfo;
        secretInfo.mSecretPath = this.mSource.getPath();
        if (bundle != null) {
            this.mSecretInfo.mIsSecret = bundle.getBoolean("extra_is_secret");
            this.mSecretInfo.mSecretKey = bundle.getByteArray("extra_secret_key");
            this.mSecretInfo.mSecretId = bundle.getLong("photo_secret_id");
            this.mIsFavorite = bundle.getBoolean("photo_is_favorite");
        }
    }

    public final Bitmap decodePreviewBitmap() throws FileNotFoundException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = Integer.highestOneBit((int) this.mDownSampleSize);
        options.inMutable = true;
        Bitmap decodeBitmap = decodeBitmap(options, this.mRotationDegree);
        if (decodeBitmap == null || decodeBitmap.getWidth() <= this.mPreviewWidth) {
            return decodeBitmap;
        }
        long currentTimeMillis = System.currentTimeMillis();
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(decodeBitmap, this.mPreviewWidth, this.mPreviewHeight, false);
        DefaultLogger.d("DraftManager", "scale preview bitmap consume %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        return createScaledBitmap;
    }

    public boolean initializeForPreview(boolean z) throws FileNotFoundException, SecurityException {
        Bitmap blockingLoad;
        long currentTimeMillis = System.currentTimeMillis();
        initForBitmapInfo();
        float max = Math.max(1.0f, Math.max(this.mOriginHeight / this.mPreferHeight, this.mOriginWidth / this.mPreferWidth));
        this.mDownSampleSize = max;
        this.mPreviewWidth = Math.round(this.mOriginWidth / max);
        this.mPreviewHeight = Math.round(this.mOriginHeight / this.mDownSampleSize);
        DefaultLogger.d("DraftManager", "result preview size width %d, height %d, down sample %f", Integer.valueOf(this.mPreviewWidth), Integer.valueOf(this.mPreviewHeight), Float.valueOf(this.mDownSampleSize));
        if (z && (blockingLoad = GlideLoadingUtils.blockingLoad(Glide.with(this.mContext), PreloadModel.of(this.mSource.toString()))) != null && !blockingLoad.isRecycled()) {
            if ((blockingLoad.getWidth() >= this.mPreferWidth && blockingLoad.getHeight() >= this.mPreferHeight) || !shouldLoadFromSourceFile()) {
                this.mPreviewEnable = true;
            }
            DefaultLogger.d("DraftManager", "load preview from cache");
            this.mPreview = Bitmaps.copyBitmapInCaseOfRecycle(blockingLoad);
        }
        if (this.mPreview == null) {
            this.mPreview = decodePreviewBitmap();
            this.mPreviewEnable = true;
        }
        Bitmap bitmap = this.mPreview;
        if (bitmap != null && bitmap.getWidth() == this.mOriginWidth && this.mPreview.getHeight() == this.mOriginHeight) {
            this.mIsPreviewSameWithOrigin = true;
        }
        this.mPreviewOriginal = this.mPreview;
        DefaultLogger.d("DraftManager", "initialize costs %dms same:%b, previewEnable:%b", Long.valueOf(System.currentTimeMillis() - currentTimeMillis), Boolean.valueOf(this.mIsPreviewSameWithOrigin), Boolean.valueOf(this.mPreviewEnable));
        if (!this.mPreviewEnable) {
            new LoadPreviewTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        } else if (this.mXmpExtraManager.isMoveWatermaskEnable()) {
            Bitmap copy = this.mPreview.copy(Bitmap.Config.ARGB_8888, true);
            this.mPreview = copy;
            this.mXmpExtraManager.sweepImage(copy, getInputStream());
        }
        return this.mPreview != null;
    }

    /* loaded from: classes2.dex */
    public class LoadPreviewTask extends AsyncTask<Void, Void, Bitmap> {
        public LoadPreviewTask() {
        }

        @Override // android.os.AsyncTask
        public Bitmap doInBackground(Void... voidArr) {
            Bitmap bitmap = null;
            try {
                bitmap = DraftManager.this.decodePreviewBitmap();
                if (DraftManager.this.mXmpExtraManager.isMoveWatermaskEnable()) {
                    if (!bitmap.isMutable()) {
                        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                    }
                    DraftManager.this.mXmpExtraManager.sweepImage(bitmap, DraftManager.this.getInputStream());
                }
            } catch (FileNotFoundException e) {
                DefaultLogger.w("DraftManager", e);
            } catch (SecurityException e2) {
                DefaultLogger.w("DraftManager", e2);
            }
            return bitmap;
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap bitmap) {
            super.onPostExecute((LoadPreviewTask) bitmap);
            DraftManager.this.mPreviewEnable = true;
            if (bitmap != null) {
                DraftManager.this.mPreview = bitmap;
                if (DraftManager.this.mOnPreviewRefreshListener == null) {
                    return;
                }
                DraftManager.this.mOnPreviewRefreshListener.onRefresh(DraftManager.this.mPreview);
            }
        }
    }

    public final InputStream getInputStream() throws FileNotFoundException {
        if (isSecret()) {
            checkSecretInfo();
            InputStream openInputStream = StorageSolutionProvider.get().openInputStream(StorageSolutionProvider.get().getDocumentFile(this.mSecretInfo.mSecretPath, IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag("DraftManager", "getInputStream")));
            byte[] bArr = this.mSecretInfo.mSecretKey;
            return bArr != null ? CryptoUtil.getDecryptCipherInputStream(openInputStream, bArr) : openInputStream;
        }
        return IoUtils.openInputStream(this.mContext, this.mSource);
    }

    public final Bitmap decodeBitmap(BitmapFactory.Options options, int i) throws FileNotFoundException {
        return Bitmaps.setConfig(Bitmaps.joinExif(Bitmaps.decodeStream(getInputStream(), options), i, options));
    }

    public boolean checkRemoveWatermarkEnable() {
        if (this.mXmpExtraManager.isRemoveWatermarkEnable() || this.mXmpExtraManager.isMoveWatermaskEnable()) {
            this.mXmpExtraManager.isRemoveWatermarkShow(this.mPreviewOriginal, this.mRenderDataList);
            return this.mXmpExtraManager.isRemoveWatermarkEnable() || this.mXmpExtraManager.isMoveWatermaskEnable();
        }
        return false;
    }

    public boolean isRemoveWatermarkEnable() {
        return this.mXmpExtraManager.isRemoveWatermarkEnable();
    }

    public boolean isPreviewSameWithOrigin() {
        return this.mIsPreviewSameWithOrigin;
    }

    public Bundle getBundle() {
        return this.mBundle;
    }

    public Bitmap getPreview() {
        return this.mPreview;
    }

    public boolean isPreviewEnable() {
        return this.mPreviewEnable;
    }

    public String getExportFileSuffix() {
        return isSavedAsPng() ? "png" : "jpg";
    }

    public boolean isSavedAsPng() {
        return (!this.mIsScreenshot && "image/png".equals(this.mMimeType)) || isNeedSaveAsPng();
    }

    public int getExportedWidth() {
        return this.mExportedWidth;
    }

    public int getExportedHeight() {
        return this.mExportedHeight;
    }

    public List<RenderData> getRenderDataList() {
        return this.mRenderDataList;
    }

    public void setRenderDataList(List<RenderData> list) {
        this.mRenderDataList = list;
    }

    public boolean isWithWatermark() {
        return this.mWithWatermark;
    }

    public boolean isNeedSaveAsPng() {
        return this.mIsNeedSaveAsPng;
    }

    public void setIsNeedSaveAsPng(boolean z) {
        this.mIsNeedSaveAsPng = z;
    }

    public void enqueue(Callback callback, RenderData renderData) {
        new PreviewRenderTask(callback, renderData).execute(new RenderData[0]);
    }

    public void enqueue(Callback callback) {
        new RemoveBeautifyRenderTask(callback).execute(new Void[0]);
    }

    public void release() {
        RenderEngine[] renderEngineArr;
        for (RenderData renderData : this.mRenderDataList) {
            renderData.release();
        }
        for (RenderEngine renderEngine : this.mEngines) {
            if (renderEngine != null) {
                renderEngine.release();
            }
        }
    }

    public void setIsWatermarkAdded(boolean z) {
        this.mIsWatermarkAdded = z;
    }

    public int getStepCount() {
        return this.mRenderDataList.size();
    }

    public boolean isEmpty() {
        return this.mRenderDataList.isEmpty() && this.mWithWatermark && !this.mXmpExtraManager.waterChanged();
    }

    public boolean export(Uri uri) {
        Bitmap bitmap;
        DefaultLogger.d("DraftManager", "exporting");
        if (isEmpty()) {
            return false;
        }
        if (this.mOriginHeight == 0 || this.mOriginWidth == 0) {
            try {
                initForBitmapInfo();
            } catch (FileNotFoundException e) {
                DefaultLogger.w("DraftManager", e);
                return false;
            } catch (SecurityException e2) {
                DefaultLogger.w("DraftManager", e2);
                return false;
            }
        }
        boolean z = true;
        if (this.mIsPreviewSameWithOrigin) {
            bitmap = this.mPreview;
            if (this.mIsSingleEffectMode && !this.mRenderDataList.isEmpty()) {
                bitmap = RenderEngine.render(this.mContext, bitmap, this.mRenderDataList, this.mEngines, true);
            }
            if (this.mWithWatermark && this.mXmpExtraManager.isMoveWatermaskEnable()) {
                this.mXmpExtraManager.saveWaterMask(bitmap);
            }
            if (bitmap != null) {
                z = false;
            }
            DefaultLogger.d("DraftManager", "origin is preview,bmp is empty:%b", Boolean.valueOf(z));
        } else {
            Bitmap decodeOrigin = decodeOrigin();
            if (!this.mWithWatermark || this.mXmpExtraManager.isMoveWatermaskEnable()) {
                InputStream inputStream = null;
                try {
                    try {
                        inputStream = getInputStream();
                        this.mXmpExtraManager.sweepImage(decodeOrigin, inputStream);
                    } catch (FileNotFoundException e3) {
                        e3.printStackTrace();
                    }
                } finally {
                    IoUtils.close(inputStream);
                }
            }
            if (!this.mRenderDataList.isEmpty()) {
                decodeOrigin = RenderEngine.render(this.mContext, decodeOrigin, this.mRenderDataList, this.mEngines, true);
            }
            bitmap = decodeOrigin;
            if (this.mWithWatermark && this.mXmpExtraManager.isMoveWatermaskEnable()) {
                this.mXmpExtraManager.saveWaterMask(bitmap);
            }
        }
        if (bitmap != null) {
            return export(bitmap, uri);
        }
        return false;
    }

    public boolean export(Bitmap bitmap, Uri uri) {
        boolean z = false;
        if (bitmap == null || uri == null) {
            return false;
        }
        this.mExportedWidth = bitmap.getWidth();
        this.mExportedHeight = bitmap.getHeight();
        OutputStream outputStream = getOutputStream(uri);
        if (outputStream == null) {
            return false;
        }
        ExifInterface exifInterface = this.mExif;
        if (exifInterface != null) {
            List<ExifTag> allTags = exifInterface.getAllTags();
            if (allTags == null || allTags.isEmpty()) {
                DefaultLogger.i("DraftManager", "no exif tags found in exif");
            } else {
                DefaultLogger.d("DraftManager", "filter exif");
                ExifInterface exifInterface2 = new ExifInterface();
                for (ExifTag exifTag : allTags) {
                    short tagId = exifTag.getTagId();
                    if (tagId != ExifInterface.getTrueTagKey(ExifInterface.TAG_ORIENTATION) && tagId != ExifInterface.getTrueTagKey(ExifInterface.TAG_IMAGE_WIDTH) && tagId != ExifInterface.getTrueTagKey(ExifInterface.TAG_IMAGE_LENGTH) && tagId != ExifInterface.getTrueTagKey(ExifInterface.TAG_XIAOMI_COMMENT) && tagId != ExifInterface.getTrueTagKey(ExifInterface.TAG_USER_COMMENT) && tagId != -30576 && tagId != -30568 && tagId != -30569 && tagId != -23293 && tagId != -30567) {
                        exifInterface2.setTag(exifTag);
                    } else {
                        DefaultLogger.d("DraftManager", "skip user comment");
                    }
                }
                exifInterface2.setTag(exifInterface2.buildTag(ExifInterface.TAG_ORIENTATION, Short.valueOf(ExifInterface.getOrientationValueForRotation(0))));
                exifInterface2.setTag(exifInterface2.buildTag(ExifInterface.TAG_IMAGE_WIDTH, Integer.valueOf(bitmap.getWidth())));
                exifInterface2.setTag(exifInterface2.buildTag(ExifInterface.TAG_IMAGE_LENGTH, Integer.valueOf(bitmap.getHeight())));
                if (this.mIsWatermarkAdded) {
                    exifInterface2.setTag(exifInterface2.buildTag(ExifInterface.TAG_WATERMARK_ADDED, 1));
                }
                outputStream = exifInterface2.getExifWriterStream(outputStream);
            }
        } else if (this.mSupportExif != null) {
            DefaultLogger.i("DraftManager", "add exif tags from support exif");
            ExifInterface exifInterface3 = new ExifInterface();
            long dateTime = ExifUtil.getDateTime(this.mSupportExif, true);
            if (dateTime > 0) {
                ExifUtil.setDateTime(exifInterface3, dateTime, true);
            }
            exifInterface3.setTag(exifInterface3.buildTag(ExifInterface.TAG_ORIENTATION, Short.valueOf(ExifInterface.getOrientationValueForRotation(0))));
            exifInterface3.setTag(exifInterface3.buildTag(ExifInterface.TAG_IMAGE_WIDTH, Integer.valueOf(bitmap.getWidth())));
            exifInterface3.setTag(exifInterface3.buildTag(ExifInterface.TAG_IMAGE_LENGTH, Integer.valueOf(bitmap.getHeight())));
            if (this.mIsWatermarkAdded) {
                exifInterface3.setTag(exifInterface3.buildTag(ExifInterface.TAG_WATERMARK_ADDED, 1));
            }
            outputStream = exifInterface3.getExifWriterStream(outputStream);
        } else {
            DefaultLogger.i("DraftManager", "no exif found in source image");
        }
        int compressQuality = getCompressQuality(bitmap.getWidth(), bitmap.getHeight());
        boolean isSavedAsPng = isSavedAsPng();
        long currentTimeMillis = System.currentTimeMillis();
        try {
            try {
                boolean compress = bitmap.compress(isSavedAsPng ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG, compressQuality, outputStream);
                if (compress) {
                    outputStream.flush();
                }
                IoUtils.close("DraftManager", outputStream);
                z = compress;
            } catch (IOException e) {
                DefaultLogger.w("DraftManager", e);
                IoUtils.close("DraftManager", outputStream);
            }
            DefaultLogger.d("DraftManager", "saved as png %b, quality %d, compress cost %d", Boolean.valueOf(isSavedAsPng), Integer.valueOf(compressQuality), Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            return z;
        } catch (Throwable th) {
            IoUtils.close("DraftManager", outputStream);
            throw th;
        }
    }

    public final int getCompressQuality(int i, int i2) {
        return BigBitmapLoadUtils.isHR108(i, i2) ? 90 : 97;
    }

    public final void initForBitmapInfo() throws FileNotFoundException {
        DefaultLogger.d("DraftManager", "decoding bitmap size:%d*%d", Integer.valueOf(this.mOriginHeight), Integer.valueOf(this.mOriginWidth));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmaps.decodeStream(getInputStream(), options);
        this.mMimeType = options.outMimeType;
        boolean isSourceFileSupportGallery3DExif = isSourceFileSupportGallery3DExif();
        if (isSecret()) {
            checkSecretInfo();
            if (isSourceFileSupportGallery3DExif) {
                this.mExif = (ExifInterface) ExifUtil.createExifInterface(this.mSource.getPath(), this.mSecretInfo.mSecretKey, ExifUtil.sGallery3DExifCreator);
            } else if (this.mSecretInfo.mSecretKey != null) {
                this.mSupportExif = (androidx.exifinterface.media.ExifInterface) ExifUtil.createExifInterfaceByDecryptFile(this.mSource.getPath(), this.mSecretInfo.mSecretKey, ExifUtil.sSupportExifCreator);
            } else {
                this.mSupportExif = (androidx.exifinterface.media.ExifInterface) ExifUtil.createExifInterface(getInputStream(), ExifUtil.sSupportExifCreator);
            }
        } else if (isSourceFileSupportGallery3DExif) {
            this.mExif = ExifUtil.sGallery3DExifCreator.mo1691create(getInputStream());
        } else {
            this.mSupportExif = (androidx.exifinterface.media.ExifInterface) ExifUtil.createExifInterface(getInputStream(), ExifUtil.sSupportExifCreator);
        }
        Object obj = this.mExif;
        if (obj == null) {
            obj = this.mSupportExif;
        }
        ExifUtil.ExifInfo parseRotationInfo = ExifUtil.parseRotationInfo(obj);
        if (parseRotationInfo != null) {
            this.mRotationDegree = parseRotationInfo.rotation;
        }
        int i = options.outWidth;
        int i2 = options.outHeight;
        InputStream inputStream = null;
        Bitmaps.joinExif(null, this.mRotationDegree, options);
        this.mOriginWidth = options.outWidth;
        int i3 = options.outHeight;
        this.mOriginHeight = i3;
        DefaultLogger.d("DraftManager", "decoding bitmap size:%d*%d", Integer.valueOf(i3), Integer.valueOf(this.mOriginWidth));
        long currentTimeMillis = System.currentTimeMillis();
        try {
            inputStream = getInputStream();
            this.mXmpExtraManager.decodeXmpData(inputStream, i, i2, this.mRotationDegree);
            this.mXmpExtraManager.checkSubImage(getInputStream());
            this.mXmpExtraManager.initDeviceMask(getInputStream());
            this.mXmpExtraManager.initTimeMask(getInputStream());
            IoUtils.close("DraftManager", inputStream);
            DefaultLogger.d("DraftManager", "decodeXmpData coast : %s", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        } catch (Throwable th) {
            IoUtils.close("DraftManager", inputStream);
            throw th;
        }
    }

    public final void checkSecretInfo() {
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(this.mSecretInfo.mSecretPath, IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag("DraftManager", "checkSecretInfo"));
        if (documentFile == null || !documentFile.exists()) {
            Context context = this.mContext;
            SecretInfo secretInfo = this.mSecretInfo;
            this.mSecretInfo = CloudUtils.getSecretInfo(context, secretInfo.mSecretId, secretInfo);
        }
    }

    public Bitmap decodeOrigin() {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            options.inSampleSize = BigBitmapLoadUtils.calculateInSampleSize(this.mContext, this.mOriginWidth, this.mOriginHeight);
            return decodeBitmap(options, this.mRotationDegree);
        } catch (FileNotFoundException e) {
            DefaultLogger.w("DraftManager", e);
            return null;
        } catch (SecurityException e2) {
            DefaultLogger.w("DraftManager", e2);
            return null;
        }
    }

    public void getRenderData(List<RenderData> list) {
        list.addAll(this.mRenderDataList);
    }

    public final OutputStream getOutputStream(Uri uri) {
        try {
            return IoUtils.openOutputStream("DraftManager", this.mContext, uri);
        } catch (Exception e) {
            DefaultLogger.w("DraftManager", e);
            IoUtils.close(null);
            return null;
        }
    }

    public boolean isSecret() {
        return this.mSecretInfo.mIsSecret;
    }

    public boolean isFavorite() {
        return this.mIsFavorite;
    }

    public final boolean shouldLoadFromSourceFile() {
        return !isRaw() && (!BigBitmapLoadUtils.isHR108(this.mOriginWidth, this.mOriginHeight) || !isHeif());
    }

    public final boolean isSourceFileSupportGallery3DExif() {
        return !isRaw() && !isHeif();
    }

    public final boolean isRaw() {
        return BaseFileMimeUtil.isRawFromMimeType(this.mMimeType);
    }

    public final boolean isHeif() {
        return BaseFileMimeUtil.isHeifMimeType(this.mMimeType);
    }

    public void removeWaterRender(boolean z, Callback<Bitmap, Void> callback) {
        this.mWithWatermark = z;
        if (!this.mXmpExtraManager.isMoveWatermaskEnable()) {
            new ReRenderTask(callback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Boolean.valueOf(z));
        }
    }

    public WaterMaskWrapper getWaterMaskWrapper() {
        return this.mXmpExtraManager.getWaterMaskWrapper();
    }

    /* loaded from: classes2.dex */
    public class ReRenderTask extends AsyncTask<Boolean, Void, Bitmap> {
        public Callback<Bitmap, Void> mCallback;

        public ReRenderTask(Callback<Bitmap, Void> callback) {
            this.mCallback = callback;
        }

        @Override // android.os.AsyncTask
        public void onPreExecute() {
            this.mCallback.onPrepare();
        }

        @Override // android.os.AsyncTask
        public Bitmap doInBackground(Boolean... boolArr) {
            boolean booleanValue = boolArr[0].booleanValue();
            InputStream inputStream = null;
            this.mCallback.onExecute(null);
            Bitmap copy = DraftManager.this.mPreviewOriginal.copy(Bitmap.Config.ARGB_8888, true);
            if (!booleanValue) {
                try {
                    try {
                        inputStream = DraftManager.this.getInputStream();
                        DraftManager.this.mXmpExtraManager.sweepImage(copy, inputStream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } finally {
                    IoUtils.close("DraftManager", inputStream);
                }
            }
            return RenderEngine.render(DraftManager.this.mContext, copy, DraftManager.this.mRenderDataList, DraftManager.this.mEngines);
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                DraftManager.this.mPreview = bitmap;
                this.mCallback.onDone(DraftManager.this.mPreview);
                return;
            }
            this.mCallback.onError(null);
        }
    }

    /* loaded from: classes2.dex */
    public class RemoveBeautifyRenderTask extends AsyncTask<Void, Void, Bitmap> {
        public Callback<Bitmap, Void> mCallback;

        public RemoveBeautifyRenderTask(Callback<Bitmap, Void> callback) {
            this.mCallback = callback;
        }

        @Override // android.os.AsyncTask
        public void onPreExecute() {
            this.mCallback.onPrepare();
            RenderData renderData = (RenderData) DraftManager.this.mRenderDataList.get(DraftManager.this.mRenderDataList.size() - 1);
            if (renderData.mType != Effect.BEAUTIFY2) {
                return;
            }
            DraftManager.this.mRenderDataList.remove(renderData);
        }

        @Override // android.os.AsyncTask
        public Bitmap doInBackground(Void... voidArr) {
            InputStream inputStream = null;
            this.mCallback.onExecute(null);
            Bitmap copy = DraftManager.this.mPreviewOriginal.copy(Bitmap.Config.ARGB_8888, true);
            if (!DraftManager.this.mWithWatermark || DraftManager.this.mXmpExtraManager.isMoveWatermaskEnable()) {
                try {
                    try {
                        inputStream = DraftManager.this.getInputStream();
                        DraftManager.this.mXmpExtraManager.sweepImage(copy, inputStream);
                    } catch (FileNotFoundException e) {
                        DefaultLogger.e("DraftManager", "DraftManager#RemoveBeautifyRenderTask#doInBackground=%s", e);
                    }
                } finally {
                    IoUtils.close(inputStream);
                }
            }
            for (RenderData renderData : DraftManager.this.mRenderDataList) {
                copy = RenderEngine.findEngine(DraftManager.this.mContext, renderData, DraftManager.this.mEngines).render(copy, renderData, false);
                if (copy == null) {
                    break;
                }
            }
            return copy;
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap bitmap) {
            if (DraftManager.this.mRenderDataList.isEmpty() || bitmap != null) {
                DraftManager.this.mPreview = bitmap;
                this.mCallback.onDone(DraftManager.this.mPreview);
                return;
            }
            this.mCallback.onError(null);
        }

        @Override // android.os.AsyncTask
        public void onCancelled(Bitmap bitmap) {
            this.mCallback.onCancel();
        }
    }

    /* loaded from: classes2.dex */
    public class PreviewRenderTask extends AsyncTask<RenderData, Void, Bitmap> {
        public Callback<Bitmap, Void> mCallback;
        public RenderData mRenderData;

        public PreviewRenderTask(Callback<Bitmap, Void> callback, RenderData renderData) {
            this.mCallback = callback;
            this.mRenderData = renderData;
        }

        @Override // android.os.AsyncTask
        public void onPreExecute() {
            this.mCallback.onPrepare();
        }

        @Override // android.os.AsyncTask
        public Bitmap doInBackground(RenderData... renderDataArr) {
            this.mCallback.onExecute(null);
            if (DraftManager.this.mPreviewOriginal == DraftManager.this.mPreview) {
                DefaultLogger.d("DraftManager", "copy process preview start");
                DraftManager draftManager = DraftManager.this;
                draftManager.mPreview = draftManager.mPreview.copy(Bitmap.Config.ARGB_8888, true);
                DefaultLogger.d("DraftManager", "copy process preview done");
            }
            Bitmap render = RenderEngine.findEngine(DraftManager.this.mContext, this.mRenderData, DraftManager.this.mEngines).render(DraftManager.this.mPreview, this.mRenderData, false);
            DraftManager.this.mXmpExtraManager.updateMaskInfo(this.mRenderData);
            return render;
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                DraftManager.this.mPreview = bitmap;
                DraftManager.this.mRenderDataList.add(this.mRenderData);
                this.mCallback.onDone(DraftManager.this.mPreview);
                return;
            }
            this.mCallback.onError(null);
        }

        @Override // android.os.AsyncTask
        public void onCancelled(Bitmap bitmap) {
            this.mCallback.onCancel();
        }
    }

    public Bitmap getPreviewOriginal() {
        return this.mPreviewOriginal;
    }

    public void setOnPreviewRefreshListener(OnPreviewRefreshListener onPreviewRefreshListener) {
        this.mOnPreviewRefreshListener = onPreviewRefreshListener;
    }

    public void setSingleEffectMode(boolean z) {
        this.mIsSingleEffectMode = z;
    }

    public int getOriginWidth() {
        return this.mOriginWidth;
    }

    public int getOriginHeight() {
        return this.mOriginHeight;
    }
}
