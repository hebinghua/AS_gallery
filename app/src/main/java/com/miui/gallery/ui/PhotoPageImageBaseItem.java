package com.miui.gallery.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Size;
import androidx.documentfile.provider.DocumentFile;
import androidx.tracing.Trace;
import com.android.internal.MediaFileCompat;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.Config$BigPhotoConfig;
import com.miui.gallery.Config$TileConfig;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureListener;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.error.core.ErrorCode;
import com.miui.gallery.glide.load.resource.bitmap.GalleryDownsampleStrategy;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.ui.PhotoPageImageBaseItem;
import com.miui.gallery.ui.PhotoPageItem;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.BitmapUtils;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.ProcessingMediaHelper;
import com.miui.gallery.util.Scheme;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.photoview.BitmapRecycleCallback;
import com.miui.gallery.util.photoview.TileBitProvider;
import com.miui.gallery.util.photoview.TileBitProviderFactory;
import com.miui.gallery.util.photoview.TileReusedBitCache;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.HashMap;
import java.util.Locale;

/* loaded from: classes2.dex */
public class PhotoPageImageBaseItem extends PhotoPageItem {
    public static BitmapRecycleCallback sBitmapRecycleCallback;
    public boolean mIsImageFirstDisplay;
    public volatile boolean mIsSupportRegion;
    public RegionDecoderManager mRegionManager;

    public PhotoPageImageBaseItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mIsImageFirstDisplay = true;
        this.mIsSupportRegion = true;
    }

    @Override // com.miui.gallery.ui.PhotoPageItem, android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mRegionManager = new RegionDecoderManager();
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void onImageLoadFinish(ErrorCode errorCode) {
        super.onImageLoadFinish(errorCode);
        this.mPhotoView.setRegionDecodeEnable(needRegionDecode() && !isMediaInProcessing());
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void doRelease() {
        this.mRegionManager.release();
        super.doRelease();
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void doOnSelected(boolean z, boolean z2, boolean z3) {
        super.doOnSelected(z, z2, z3);
        this.mRegionManager.onSelected();
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void doOnUnSelected(boolean z, int i) {
        super.doOnUnSelected(z, i);
        this.mRegionManager.onUnSelected();
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void swapItem(BaseDataItem baseDataItem) {
        if (baseDataItem == null || !baseDataItem.equals(this.mDataItem)) {
            this.mIsImageFirstDisplay = true;
            this.mIsSupportRegion = true;
        }
        super.swapItem(baseDataItem);
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public RequestOptions getRequestOptions(BaseDataItem baseDataItem, boolean z) {
        RequestOptions requestOptions = super.getRequestOptions(baseDataItem, z);
        if (!this.mIsSupportRegion) {
            requestOptions = requestOptions.mo952downsample(GalleryDownsampleStrategy.AT_LEAST).encodeFormat(Bitmap.CompressFormat.PNG);
        }
        if (!z) {
            if (isMediaInProcessing()) {
                Config$BigPhotoConfig.markAsTemp(requestOptions);
            }
            return isBlurredForProcessing() ? Config$BigPhotoConfig.applyProcessingOptions(requestOptions) : requestOptions;
        }
        return requestOptions;
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public Size getImageSize(boolean z) {
        if (!z && !this.mIsSupportRegion) {
            int fullScreenHeight = ScreenUtils.getFullScreenHeight(getContext());
            return new Size(fullScreenHeight, fullScreenHeight);
        }
        return super.getImageSize(z);
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public void doDisplayImage(String str, RequestOptions requestOptions) {
        this.mPhotoView.setRegionDecodeEnable(false);
        super.doDisplayImage(str, requestOptions);
        if (isPagerSelected() && !this.mIsImageFirstDisplay) {
            this.mRegionManager.resetRegionDecoderIfNeeded();
        }
        this.mIsImageFirstDisplay = false;
    }

    public static BitmapRecycleCallback getBitmapRecycleCallback() {
        if (sBitmapRecycleCallback == null) {
            sBitmapRecycleCallback = PhotoPageImageBaseItem$$ExternalSyntheticLambda0.INSTANCE;
        }
        return sBitmapRecycleCallback;
    }

    public static /* synthetic */ void lambda$getBitmapRecycleCallback$0(Bitmap bitmap) {
        TileReusedBitCache.getInstance().put(bitmap);
    }

    @Override // com.miui.gallery.ui.PhotoPageItem
    public PhotoPageItem.DownloadManager createDownloadManager() {
        return new BaseImageDownloadManager();
    }

    /* loaded from: classes2.dex */
    public class BaseImageDownloadManager extends PhotoPageItem.DownloadManager {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public BaseImageDownloadManager() {
            super();
            PhotoPageImageBaseItem.this = r1;
        }

        @Override // com.miui.gallery.ui.PhotoPageItem.DownloadManager
        public void doOnDownloaded(DownloadType downloadType, String str) {
            super.doOnDownloaded(downloadType, str);
            if (PhotoPageImageBaseItem.this.isPagerSelected()) {
                PhotoPageImageBaseItem.this.mRegionManager.onSelected();
            }
        }
    }

    /* loaded from: classes2.dex */
    public class RegionDecoderManager implements FutureListener<TileBitProvider> {
        public TileBitProvider mDecoderProvider;
        public String mFileKey;
        public Future mRegionCreateFuture;

        public static /* synthetic */ void $r8$lambda$Wh9DnKAMS1XkigQ94rwajz6cUGQ(RegionDecoderManager regionDecoderManager) {
            regionDecoderManager.lambda$onFutureDone$0();
        }

        public RegionDecoderManager() {
            PhotoPageImageBaseItem.this = r1;
        }

        public final boolean isFileChanged() {
            BaseDataItem baseDataItem = PhotoPageImageBaseItem.this.mDataItem;
            if (baseDataItem == null) {
                return true;
            }
            String pathDisplayBetter = baseDataItem.getPathDisplayBetter();
            return TextUtils.isEmpty(pathDisplayBetter) || !TextUtils.equals(genFileKey(pathDisplayBetter, PhotoPageImageBaseItem.this.mDataItem.getDisplayBetterFileSize()), this.mFileKey);
        }

        public void resetRegionDecoderIfNeeded() {
            if (!isFileChanged()) {
                return;
            }
            PhotoPageImageBaseItem.this.post(new Runnable() { // from class: com.miui.gallery.ui.PhotoPageImageBaseItem.RegionDecoderManager.1
                {
                    RegionDecoderManager.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    RegionDecoderManager.this.create();
                }
            });
        }

        public void onSelected() {
            create();
        }

        public final void create() {
            release();
            if (canSupportRegion()) {
                this.mRegionCreateFuture = ThreadManager.getTileProviderPool().submit(new RegionCreateJob(PhotoPageImageBaseItem.this.mDataItem.getPathDisplayBetter(), PhotoPageImageBaseItem.this.mDataItem.getSecretKey(), PhotoPageImageBaseItem.this.mDataItem.getMimeType()), this);
            }
        }

        public final void release() {
            Trace.beginSection("baseImageItemRelease");
            Trace.beginSection("cancel");
            Future future = this.mRegionCreateFuture;
            if (future != null) {
                future.cancel();
                this.mRegionCreateFuture = null;
            }
            Trace.endSection();
            Trace.beginSection("RegionReleaseJob");
            new RegionReleaseJob(this.mDecoderProvider).execute();
            this.mDecoderProvider = null;
            Trace.endSection();
            Trace.beginSection("releaseTile");
            PhotoPageImageBaseItem.this.mPhotoView.releaseTile();
            Trace.endSection();
            Trace.endSection();
        }

        public void onUnSelected() {
            release();
        }

        public final boolean canSupportRegion() {
            BaseDataItem baseDataItem;
            return PhotoPageImageBaseItem.this.mIsSupportRegion && (baseDataItem = PhotoPageImageBaseItem.this.mDataItem) != null && BaseFileMimeUtil.isImageFromMimeType(baseDataItem.getMimeType()) && !BaseFileMimeUtil.isGifFromMimeType(PhotoPageImageBaseItem.this.mDataItem.getMimeType()) && !BaseFileMimeUtil.isRawFromMimeType(PhotoPageImageBaseItem.this.mDataItem.getMimeType()) && PhotoPageImageBaseItem.this.mDataItem.getOriginalPath() != null && !ProcessingMediaHelper.getInstance().isMediaInProcessing(Scheme.FILE.wrap(PhotoPageImageBaseItem.this.mDataItem.getOriginalPath()));
        }

        @Override // com.miui.gallery.concurrent.FutureListener
        public void onFutureDone(final Future<TileBitProvider> future) {
            final TileBitProvider tileBitProvider = future.get();
            if (tileBitProvider != null) {
                if (tileBitProvider.getImageWidth() > 0) {
                    PhotoPageImageBaseItem.this.mIsSupportRegion = true;
                    PhotoPageImageBaseItem.this.post(new Runnable() { // from class: com.miui.gallery.ui.PhotoPageImageBaseItem.RegionDecoderManager.2
                        {
                            RegionDecoderManager.this = this;
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            if (!PhotoPageImageBaseItem.this.isPagerSelected() || future.isCancelled()) {
                                tileBitProvider.release();
                                return;
                            }
                            DefaultLogger.d("PhotoPageImageBaseItem", "setup region");
                            RegionDecoderManager.this.mDecoderProvider = tileBitProvider;
                            PhotoPageImageBaseItem.this.mPhotoView.setupTile(tileBitProvider, PhotoPageImageBaseItem.getBitmapRecycleCallback(), PhotoPageImageBaseItem.this.getTrimMemoryCallback());
                        }
                    });
                    return;
                }
                PhotoPageImageBaseItem.this.mIsSupportRegion = false;
                BaseDataItem baseDataItem = PhotoPageImageBaseItem.this.mDataItem;
                String originalPath = baseDataItem != null ? baseDataItem.getOriginalPath() : null;
                if (TextUtils.isEmpty(originalPath)) {
                    return;
                }
                DefaultLogger.w("PhotoPageImageBaseItem", "not support region %s", MediaFileCompat.getMimeTypeForFile(originalPath));
                PhotoPageImageBaseItem.this.post(new Runnable() { // from class: com.miui.gallery.ui.PhotoPageImageBaseItem$RegionDecoderManager$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        PhotoPageImageBaseItem.RegionDecoderManager.$r8$lambda$Wh9DnKAMS1XkigQ94rwajz6cUGQ(PhotoPageImageBaseItem.RegionDecoderManager.this);
                    }
                });
                HashMap hashMap = new HashMap();
                hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, MediaFileCompat.getMimeTypeForFile(originalPath));
                SamplingStatHelper.recordCountEvent("photo", "photo_not_support_region", hashMap);
            }
        }

        public /* synthetic */ void lambda$onFutureDone$0() {
            PhotoPageImageBaseItem photoPageImageBaseItem = PhotoPageImageBaseItem.this;
            photoPageImageBaseItem.displayImage(photoPageImageBaseItem.mDataItem);
        }

        public final String genFileKey(String str, long j) {
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            return String.format(Locale.US, "%s_%d", str, Long.valueOf(j));
        }

        /* loaded from: classes2.dex */
        public class RegionCreateJob implements ThreadPool.Job<TileBitProvider> {
            public String mMimeType;
            public String mModel;
            public byte[] mSecretKey;

            public RegionCreateJob(String str, byte[] bArr, String str2) {
                RegionDecoderManager.this = r1;
                this.mModel = str;
                this.mSecretKey = bArr;
                this.mMimeType = str2;
            }

            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public TileBitProvider mo1807run(ThreadPool.JobContext jobContext) {
                Uri parse;
                int i;
                Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
                DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(this.mModel, IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag("PhotoPageImageBaseItem", "run"));
                if (documentFile != null && documentFile.exists()) {
                    parse = documentFile.getUri();
                } else {
                    parse = Uri.parse(this.mModel);
                }
                Uri uri = parse;
                if (!uri.isAbsolute()) {
                    DefaultLogger.i("PhotoPageImageBaseItem", "illegal model for region decode [%s]", this.mModel);
                    return null;
                } else if (jobContext.isCancelled()) {
                    return null;
                } else {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapUtils.safeDecodeBitmap(sGetAndroidContext.getContentResolver(), uri, options, this.mSecretKey);
                    int i2 = options.outWidth;
                    if (i2 > 0 && (i = options.outHeight) > 0 && !Config$TileConfig.needUseTile(i2, i)) {
                        DefaultLogger.i("PhotoPageImageBaseItem", "not need use tile [width %d, height %d]", Integer.valueOf(options.outWidth), Integer.valueOf(options.outHeight));
                        return null;
                    } else if (jobContext.isCancelled()) {
                        return null;
                    } else {
                        TileBitProvider create = TileBitProviderFactory.create(sGetAndroidContext.getContentResolver(), uri, this.mMimeType, options.outWidth, options.outHeight, this.mSecretKey);
                        if (jobContext.isCancelled()) {
                            create.release();
                            RegionDecoderManager.this.mFileKey = null;
                            return null;
                        }
                        RegionDecoderManager regionDecoderManager = RegionDecoderManager.this;
                        String str = this.mModel;
                        regionDecoderManager.mFileKey = regionDecoderManager.genFileKey(str, BaseFileUtils.getFileSize(str));
                        return create;
                    }
                }
            }
        }

        /* loaded from: classes2.dex */
        public class RegionReleaseJob implements ThreadPool.Job<Void> {
            public TileBitProvider mToBeReleasedProvider;

            public static /* synthetic */ void $r8$lambda$onWuEtGHv1w9G4YmvkFExRLkQ6Q(RegionReleaseJob regionReleaseJob) {
                regionReleaseJob.lambda$execute$0();
            }

            public RegionReleaseJob(TileBitProvider tileBitProvider) {
                RegionDecoderManager.this = r1;
                this.mToBeReleasedProvider = tileBitProvider;
            }

            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public Void mo1807run(ThreadPool.JobContext jobContext) {
                TileBitProvider tileBitProvider = this.mToBeReleasedProvider;
                if (tileBitProvider == null) {
                    return null;
                }
                tileBitProvider.release();
                DefaultLogger.d("PhotoPageImageBaseItem", "release region");
                this.mToBeReleasedProvider = null;
                return null;
            }

            public void execute() {
                if (this.mToBeReleasedProvider == null) {
                    return;
                }
                ThreadManager.getWorkHandler().post(new Runnable() { // from class: com.miui.gallery.ui.PhotoPageImageBaseItem$RegionDecoderManager$RegionReleaseJob$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        PhotoPageImageBaseItem.RegionDecoderManager.RegionReleaseJob.$r8$lambda$onWuEtGHv1w9G4YmvkFExRLkQ6Q(PhotoPageImageBaseItem.RegionDecoderManager.RegionReleaseJob.this);
                    }
                });
            }

            public /* synthetic */ void lambda$execute$0() {
                if (this.mToBeReleasedProvider == null) {
                    return;
                }
                ThreadManager.getTileProviderPool().submit(this);
            }
        }
    }
}
