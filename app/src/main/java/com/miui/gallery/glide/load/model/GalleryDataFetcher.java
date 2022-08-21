package com.miui.gallery.glide.load.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import androidx.documentfile.provider.DocumentFile;
import androidx.exifinterface.media.ExifInterface;
import ch.qos.logback.core.joran.action.Action;
import com.bumptech.glide.Priority;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ImageHeaderParserUtils;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.data.ParcelFileDescriptorRewinder;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.google.common.io.Closeables;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.glide.Utils;
import com.miui.gallery.glide.load.ExtraInfoManager;
import com.miui.gallery.glide.load.GalleryOptions;
import com.miui.gallery.glide.load.RegionConfig;
import com.miui.gallery.photosapi.PhotosOemApi;
import com.miui.gallery.provider.ProcessingMediaManager;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.constants.MIUIStorageConstants;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseBitmapUtils;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.DecodeInfoHelper;
import com.miui.gallery.util.DecodeRegionImageUtils;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.MediaStoreUtils;
import com.miui.gallery.util.Scheme;
import com.miui.gallery.util.SpecialTypeMediaUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/* loaded from: classes2.dex */
public final class GalleryDataFetcher<Data> implements DataFetcher<Data> {
    public final ArrayPool mArrayPool;
    public final BitmapPool mBitmapPool;
    public DataCallbackWrapper<? super Data> mCallbackWrapper;
    public final Class<Data> mDataClass;
    public DataHolder<Data> mDataHolder;
    public volatile DataFetcher<Data> mDelegate;
    public final ModelLoader<File, Data> mFileDelegate;
    public final int mHeight;
    public final List<ImageHeaderParser> mImageHeaderParsers;
    public volatile boolean mIsCancelled;
    public final GalleryModel mModel;
    public final GalleryModelOpener<Data> mOpener;
    public final Options mOptions;
    public final Registry mRegistry;
    public final ModelLoader<Uri, Data> mUriDelegate;
    public final int mWidth;

    public GalleryDataFetcher(GalleryModel galleryModel, GalleryModelOpener<Data> galleryModelOpener, ModelLoader<File, Data> modelLoader, ModelLoader<Uri, Data> modelLoader2, int i, int i2, Options options, Class<Data> cls, List<ImageHeaderParser> list, ArrayPool arrayPool, BitmapPool bitmapPool, Registry registry) {
        this.mModel = galleryModel;
        this.mOpener = galleryModelOpener;
        this.mFileDelegate = modelLoader;
        this.mUriDelegate = modelLoader2;
        this.mWidth = i;
        this.mHeight = i2;
        this.mOptions = options;
        this.mDataClass = cls;
        this.mImageHeaderParsers = list;
        this.mArrayPool = arrayPool;
        this.mBitmapPool = bitmapPool;
        this.mRegistry = registry;
    }

    public final DataFetcher<Data> buildDelegateFetcher(Uri uri) throws FileNotFoundException {
        ModelLoader.LoadData<Data> buildDelegateData = buildDelegateData(uri);
        if (buildDelegateData != null) {
            return buildDelegateData.fetcher;
        }
        return null;
    }

    public final ModelLoader.LoadData<Data> buildDelegateData(Uri uri) throws FileNotFoundException {
        if (Action.FILE_ATTRIBUTE.equalsIgnoreCase(uri.getScheme())) {
            DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(Scheme.FILE.crop(uri.toString()), IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag("GalleryDataFetcher", "buildDelegateData"));
            if (documentFile != null) {
                uri = documentFile.getUri();
            }
        }
        if (Action.FILE_ATTRIBUTE.equalsIgnoreCase(uri.getScheme())) {
            return this.mFileDelegate.buildLoadData(new File(Scheme.FILE.crop(Uri.decode(uri.toString()))), this.mWidth, this.mHeight, this.mOptions);
        }
        return this.mUriDelegate.buildLoadData(uri, this.mWidth, this.mHeight, this.mOptions);
    }

    @Override // com.bumptech.glide.load.data.DataFetcher
    public void loadData(Priority priority, DataFetcher.DataCallback<? super Data> dataCallback) {
        RegionConfig regionConfig = (RegionConfig) this.mOptions.get(GalleryOptions.DECODE_REGION);
        if (regionConfig != null) {
            this.mCallbackWrapper = new DecodeRegionCallbackWrapper(dataCallback, regionConfig, this.mWidth, this.mImageHeaderParsers, this.mArrayPool, this.mBitmapPool, this.mRegistry);
        }
        DataCallbackWrapper<? super Data> dataCallbackWrapper = this.mCallbackWrapper;
        if (dataCallbackWrapper == null) {
            this.mCallbackWrapper = new ExtraInfoCallbackWrapper(dataCallback, this.mModel);
        } else {
            this.mCallbackWrapper = new ExtraInfoCallbackWrapper(dataCallbackWrapper, this.mModel);
        }
        if (ProcessingMediaManager.CAMERA_PROVIDER_VERSION.get(null).intValue() == 2) {
            this.mCallbackWrapper = new DecodeInfoCallbackWrapper(this.mCallbackWrapper, this.mModel);
        }
        try {
            DataHolder<Data> open = this.mOpener.open(this.mModel, this.mWidth, this.mHeight, this.mOptions);
            this.mDataHolder = open;
            if (open == null || open.data == null) {
                if (((byte[]) this.mOptions.get(GalleryOptions.SECRET_KEY)) != null) {
                    DataCallbackWrapper<? super Data> dataCallbackWrapper2 = this.mCallbackWrapper;
                    dataCallbackWrapper2.onLoadFailed(new IllegalArgumentException("Failed to load data for secret file: " + this.mModel.getPath()));
                    return;
                }
                try {
                    DataFetcher<Data> buildDelegateFetcher = buildDelegateFetcher(Utils.parseUri(this.mModel.getPath()));
                    if (buildDelegateFetcher == null) {
                        DataCallbackWrapper<? super Data> dataCallbackWrapper3 = this.mCallbackWrapper;
                        dataCallbackWrapper3.onLoadFailed(new IllegalArgumentException("Failed to build fetcher for: " + this.mModel.getPath()));
                        return;
                    }
                    this.mDelegate = buildDelegateFetcher;
                    if (this.mIsCancelled) {
                        cancel();
                        return;
                    } else {
                        buildDelegateFetcher.loadData(priority, this.mCallbackWrapper);
                        return;
                    }
                } catch (FileNotFoundException e) {
                    this.mCallbackWrapper.onLoadFailed(e);
                    return;
                }
            }
            this.mCallbackWrapper.onDataHolderReady(open);
        } catch (Exception e2) {
            if (Log.isLoggable("GalleryDataFetcher", 3)) {
                DefaultLogger.d("GalleryDataFetcher", "Failed to open file", e2);
            }
            this.mCallbackWrapper.onLoadFailed(e2);
        }
    }

    @Override // com.bumptech.glide.load.data.DataFetcher
    public void cleanup() {
        DataHolder<Data> dataHolder = this.mDataHolder;
        if (dataHolder != null) {
            try {
                this.mOpener.close(dataHolder);
            } catch (IOException unused) {
            }
        }
        DataFetcher<Data> dataFetcher = this.mDelegate;
        if (dataFetcher != null) {
            dataFetcher.cleanup();
        }
        DataCallbackWrapper<? super Data> dataCallbackWrapper = this.mCallbackWrapper;
        if (dataCallbackWrapper != null) {
            dataCallbackWrapper.close();
        }
    }

    @Override // com.bumptech.glide.load.data.DataFetcher
    public void cancel() {
        this.mIsCancelled = true;
        DataFetcher<Data> dataFetcher = this.mDelegate;
        if (dataFetcher != null) {
            dataFetcher.cancel();
        }
    }

    @Override // com.bumptech.glide.load.data.DataFetcher
    public Class<Data> getDataClass() {
        return this.mDataClass;
    }

    @Override // com.bumptech.glide.load.data.DataFetcher
    public DataSource getDataSource() {
        return DataSource.LOCAL;
    }

    /* loaded from: classes2.dex */
    public static class ExtraInfoCallbackWrapper<Data> extends DataCallbackWrapper<Data> {
        public Data mData;
        public GalleryModel mModel;

        public ExtraInfoCallbackWrapper(DataFetcher.DataCallback<Data> dataCallback, GalleryModel galleryModel) {
            super(dataCallback);
            this.mModel = galleryModel;
        }

        @Override // com.miui.gallery.glide.load.model.GalleryDataFetcher.DataCallbackWrapper
        public void doOnDataReady(DataFetcher.DataCallback<Data> dataCallback, Data data) {
            this.mData = data;
            Uri parseUri = Utils.parseUri(this.mModel.getPath());
            String parseMimeType = Utils.parseMimeType(parseUri);
            ExtraInfoManager.getInstance().set(this.mData, GalleryOptions.EXTRA_MIME_TYPE, parseMimeType);
            ExtraInfoManager.getInstance().set(this.mData, GalleryOptions.EXTRA_PATH, this.mModel.getPath());
            if (Build.VERSION.SDK_INT < 31 && BaseFileMimeUtil.isVideoFromMimeType(parseMimeType)) {
                boolean isHDR10 = SpecialTypeMediaUtils.isHDR10(parseUri);
                ExtraInfoManager.getInstance().set(this.mData, GalleryOptions.EXTRA_HDR10, Boolean.valueOf(isHDR10));
                if (isHDR10) {
                    ExtraInfoManager.getInstance().set(this.mData, GalleryOptions.EXTRA_HDR10_NEED_CONVERT_COLOR, Boolean.valueOf(SpecialTypeMediaUtils.isHDR10NeedConvertColor(parseUri)));
                }
            }
            dataCallback.onDataReady(data);
        }

        @Override // com.miui.gallery.glide.load.model.GalleryDataFetcher.DataCallbackWrapper
        public void doOnDataHolderReady(DataFetcher.DataCallback<Data> dataCallback, DataHolder<Data> dataHolder) {
            String str;
            this.mData = dataHolder.data;
            String path = this.mModel.getPath();
            if ((dataHolder instanceof ParcelFileDescriptorHolder) && (str = ((ParcelFileDescriptorHolder) dataHolder).linkedFilePath) != null) {
                path = str;
            }
            ExtraInfoManager.getInstance().set(this.mData, GalleryOptions.EXTRA_PATH, path);
            int i = dataHolder.requestCode;
            DefaultLogger.v("GalleryDataFetcher", "decode from %d", Integer.valueOf(i));
            if (i == 0 || i == 1) {
                ExtraInfoManager.getInstance().set(this.mData, GalleryOptions.EXTRA_MIME_TYPE, "image/*");
            } else {
                Uri parseUri = Utils.parseUri(this.mModel.getPath());
                String parseMimeType = Utils.parseMimeType(parseUri);
                if (i == 2) {
                    ExtraInfoManager.getInstance().set(this.mData, GalleryOptions.EXTRA_MIME_TYPE, "image/*");
                } else {
                    ExtraInfoManager.getInstance().set(this.mData, GalleryOptions.EXTRA_MIME_TYPE, parseMimeType);
                }
                if (Build.VERSION.SDK_INT < 31 && BaseFileMimeUtil.isVideoFromMimeType(parseMimeType)) {
                    boolean isHDR10 = SpecialTypeMediaUtils.isHDR10(parseUri);
                    ExtraInfoManager.getInstance().set(this.mData, GalleryOptions.EXTRA_HDR10, Boolean.valueOf(isHDR10));
                    if (isHDR10) {
                        ExtraInfoManager.getInstance().set(this.mData, GalleryOptions.EXTRA_HDR10_NEED_CONVERT_COLOR, Boolean.valueOf(SpecialTypeMediaUtils.isHDR10NeedConvertColor(parseUri)));
                    }
                }
            }
            dataCallback.onDataReady(this.mData);
        }

        @Override // com.miui.gallery.glide.load.model.GalleryDataFetcher.DataCallbackWrapper, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            super.close();
            ExtraInfoManager.getInstance().remove(this.mData);
        }
    }

    /* loaded from: classes2.dex */
    public static class DecodeRegionCallbackWrapper<Data> extends DataCallbackWrapper<Data> {
        public ArrayPool mArrayPool;
        public BitmapPool mBitmapPool;
        public List<ImageHeaderParser> mParsers;
        public int mPreferTargetSize;
        public RegionConfig mRegionConfig;
        public Registry mRegistry;
        public DocumentFile mTempFile;

        public DecodeRegionCallbackWrapper(DataFetcher.DataCallback<Data> dataCallback, RegionConfig regionConfig, int i, List<ImageHeaderParser> list, ArrayPool arrayPool, BitmapPool bitmapPool, Registry registry) {
            super(dataCallback);
            this.mRegionConfig = regionConfig;
            this.mPreferTargetSize = i;
            this.mParsers = list;
            this.mArrayPool = arrayPool;
            this.mBitmapPool = bitmapPool;
            this.mRegistry = registry;
        }

        @Override // com.miui.gallery.glide.load.model.GalleryDataFetcher.DataCallbackWrapper
        public void doOnDataReady(DataFetcher.DataCallback<Data> dataCallback, Data data) {
            Bitmap decodeFaceRegion;
            byte[] compressBitmapAsPng;
            Bitmap decodeFaceRegion2;
            DataRewinder rewinder = this.mRegistry.getRewinder(data);
            try {
                int i = 0;
                if (data instanceof InputStream) {
                    Object mo993rewindAndGet = rewinder.mo993rewindAndGet();
                    if (this.mRegionConfig.isFace()) {
                        try {
                            i = ImageHeaderParserUtils.getOrientation(this.mParsers, (InputStream) mo993rewindAndGet, this.mArrayPool);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mo993rewindAndGet = rewinder.mo993rewindAndGet();
                        decodeFaceRegion2 = DecodeRegionImageUtils.decodeFaceRegion(this.mRegionConfig.getRegion(), (InputStream) mo993rewindAndGet, this.mRegionConfig.getEnlargeFactor(), this.mPreferTargetSize, i);
                    } else {
                        decodeFaceRegion2 = DecodeRegionImageUtils.decodeRegion(this.mRegionConfig.getRegion(), (InputStream) mo993rewindAndGet, this.mPreferTargetSize);
                    }
                    byte[] bArr = null;
                    if (decodeFaceRegion2 != null) {
                        bArr = BaseBitmapUtils.compressBitmapAsPng(decodeFaceRegion2);
                    }
                    if (bArr != null) {
                        dataCallback.onDataReady(Utils.bytesToStream(bArr));
                    } else {
                        dataCallback.onLoadFailed(new IllegalArgumentException("decode region failed"));
                    }
                    Closeables.closeQuietly((InputStream) mo993rewindAndGet);
                    return;
                } else if (data instanceof ParcelFileDescriptor) {
                    Object mo993rewindAndGet2 = rewinder.mo993rewindAndGet();
                    if (this.mRegionConfig.isFace()) {
                        try {
                            i = ImageHeaderParserUtils.getOrientation(this.mParsers, new ParcelFileDescriptorRewinder((ParcelFileDescriptor) mo993rewindAndGet2), this.mArrayPool);
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                        decodeFaceRegion = DecodeRegionImageUtils.decodeFaceRegion(this.mRegionConfig.getRegion(), ((ParcelFileDescriptor) rewinder.mo993rewindAndGet()).getFileDescriptor(), this.mRegionConfig.getEnlargeFactor(), this.mPreferTargetSize, i);
                        if (decodeFaceRegion != null) {
                            decodeFaceRegion = TransformationUtils.rotateImageExif(this.mBitmapPool, decodeFaceRegion, i);
                        }
                    } else {
                        decodeFaceRegion = DecodeRegionImageUtils.decodeRegion(this.mRegionConfig.getRegion(), ((ParcelFileDescriptor) mo993rewindAndGet2).getFileDescriptor(), this.mPreferTargetSize);
                    }
                    if (decodeFaceRegion != null && (compressBitmapAsPng = BaseBitmapUtils.compressBitmapAsPng(decodeFaceRegion)) != null) {
                        this.mTempFile = Utils.bytes2TempFile(compressBitmapAsPng, "png");
                    }
                    if (this.mTempFile != null) {
                        dataCallback.onDataReady(StorageSolutionProvider.get().openFileDescriptor(this.mTempFile, "r"));
                        return;
                    } else {
                        dataCallback.onLoadFailed(new IllegalArgumentException("decode region failed"));
                        return;
                    }
                } else {
                    dataCallback.onLoadFailed(new IllegalArgumentException("the type of [" + data + "]is unsupported to decode region"));
                    return;
                }
            } catch (IOException e3) {
                e3.printStackTrace();
                dataCallback.onLoadFailed(new IllegalArgumentException("decode region failed"));
            }
            e3.printStackTrace();
            dataCallback.onLoadFailed(new IllegalArgumentException("decode region failed"));
        }

        @Override // com.miui.gallery.glide.load.model.GalleryDataFetcher.DataCallbackWrapper
        public void doOnDataHolderReady(DataFetcher.DataCallback<Data> dataCallback, DataHolder<Data> dataHolder) {
            doOnDataReady(dataCallback, dataHolder.data);
        }

        @Override // com.miui.gallery.glide.load.model.GalleryDataFetcher.DataCallbackWrapper, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            super.close();
            DocumentFile documentFile = this.mTempFile;
            if (documentFile == null || !documentFile.exists()) {
                return;
            }
            this.mTempFile.delete();
        }
    }

    /* loaded from: classes2.dex */
    public static class DecodeInfoCallbackWrapper<Data> extends DataCallbackWrapper<Data> {
        public Closeable mData;
        public final GalleryModel mModel;

        public DecodeInfoCallbackWrapper(DataFetcher.DataCallback<Data> dataCallback, GalleryModel galleryModel) {
            super(dataCallback);
            this.mModel = galleryModel;
        }

        @Override // com.miui.gallery.glide.load.model.GalleryDataFetcher.DataCallbackWrapper
        public void doOnDataReady(DataFetcher.DataCallback<Data> dataCallback, Data data) {
            long currentTimeMillis;
            Uri uri;
            long currentTimeMillis2;
            ExifInterface exifInterface;
            int attributeInt;
            int attributeInt2;
            int i;
            long mediaStoreId;
            long currentTimeMillis3 = System.currentTimeMillis();
            long j = currentTimeMillis3 - 1;
            try {
                DecodeInfoHelper.DecodeInfo decodeInfo = DecodeInfoHelper.getInstance().get(Utils.parseUri(this.mModel.getPath()));
                if (decodeInfo == null || ((uri = decodeInfo.fileUri) != null && !uri.toString().contains(MIUIStorageConstants.DIRECTORY_CAMERA_PATH))) {
                    currentTimeMillis = System.currentTimeMillis();
                    dataCallback.onDataReady(data);
                } else {
                    try {
                        if (data instanceof InputStream) {
                            exifInterface = new ExifInterface((InputStream) data);
                        } else if (data instanceof FileDescriptor) {
                            exifInterface = new ExifInterface((FileDescriptor) data);
                        } else if (data instanceof ParcelFileDescriptor) {
                            exifInterface = new ExifInterface(((ParcelFileDescriptor) data).getFileDescriptor());
                        } else {
                            currentTimeMillis = System.currentTimeMillis();
                            dataCallback.onDataReady(data);
                        }
                        attributeInt = exifInterface.getAttributeInt("ImageWidth", 0);
                        attributeInt2 = exifInterface.getAttributeInt("ImageLength", 0);
                        i = decodeInfo.width;
                    } catch (Exception e) {
                        currentTimeMillis2 = System.currentTimeMillis();
                        DefaultLogger.w("GalleryDataFetcher", "check valid or read data from camera failed for [%s], since [%s]", this.mModel.getPath(), e.getMessage());
                        dataCallback.onDataReady(data);
                    }
                    if ((attributeInt == i && attributeInt2 == decodeInfo.height) || (attributeInt == decodeInfo.height && attributeInt2 == i)) {
                        currentTimeMillis = System.currentTimeMillis();
                        dataCallback.onDataReady(data);
                    } else {
                        DefaultLogger.w("GalleryDataFetcher", "mismatch exif and media store metadata, try get [%s] from camera provider", this.mModel.getPath());
                        DefaultLogger.v("GalleryDataFetcher", "exif width [%d], height [%d]", Integer.valueOf(attributeInt), Integer.valueOf(attributeInt2));
                        DefaultLogger.v("GalleryDataFetcher", "media store metadata width [%d], height [%d]", Integer.valueOf(decodeInfo.width), Integer.valueOf(decodeInfo.height));
                        Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
                        ContentResolver contentResolver = GalleryApp.sGetAndroidContext().getContentResolver();
                        Uri uri2 = decodeInfo.mediaUri;
                        if (uri2 != null) {
                            mediaStoreId = ContentUris.parseId(uri2);
                        } else {
                            mediaStoreId = MediaStoreUtils.getMediaStoreId(decodeInfo.fileUri.getPath());
                        }
                        if (data instanceof InputStream) {
                            this.mData = contentResolver.openInputStream(PhotosOemApi.getQueryProcessingUri(sGetAndroidContext, mediaStoreId));
                            currentTimeMillis2 = System.currentTimeMillis();
                            dataCallback.onDataReady(this.mData);
                        } else if (data instanceof FileDescriptor) {
                            this.mData = contentResolver.openFileDescriptor(PhotosOemApi.getQueryProcessingUri(sGetAndroidContext, mediaStoreId), "r");
                            currentTimeMillis2 = System.currentTimeMillis();
                            dataCallback.onDataReady(((ParcelFileDescriptor) this.mData).getFileDescriptor());
                        } else {
                            this.mData = contentResolver.openFileDescriptor(PhotosOemApi.getQueryProcessingUri(sGetAndroidContext, mediaStoreId), "r");
                            currentTimeMillis2 = System.currentTimeMillis();
                            dataCallback.onDataReady(this.mData);
                        }
                        DefaultLogger.v("GalleryDataFetcher", "cost [%s] ms in [%s]", Long.valueOf(currentTimeMillis2 - currentTimeMillis3), getClass().getSimpleName());
                        return;
                    }
                }
                DefaultLogger.v("GalleryDataFetcher", "cost [%s] ms in [%s]", Long.valueOf(currentTimeMillis - currentTimeMillis3), getClass().getSimpleName());
            } catch (Throwable th) {
                DefaultLogger.v("GalleryDataFetcher", "cost [%s] ms in [%s]", Long.valueOf(j - currentTimeMillis3), getClass().getSimpleName());
                throw th;
            }
        }

        @Override // com.miui.gallery.glide.load.model.GalleryDataFetcher.DataCallbackWrapper
        public void doOnDataHolderReady(DataFetcher.DataCallback<Data> dataCallback, DataHolder<Data> dataHolder) {
            doOnDataReady(dataCallback, dataHolder.data);
        }

        @Override // com.miui.gallery.glide.load.model.GalleryDataFetcher.DataCallbackWrapper, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            super.close();
            BaseMiscUtil.closeSilently(this.mData);
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class DataCallbackWrapper<Data> implements DataFetcher.DataCallback<Data>, Closeable {
        public final DataFetcher.DataCallback<Data> mWrapped;

        public abstract void doOnDataHolderReady(DataFetcher.DataCallback<Data> dataCallback, DataHolder<Data> dataHolder);

        public abstract void doOnDataReady(DataFetcher.DataCallback<Data> dataCallback, Data data);

        public DataCallbackWrapper(DataFetcher.DataCallback<Data> dataCallback) {
            this.mWrapped = dataCallback;
        }

        @Override // com.bumptech.glide.load.data.DataFetcher.DataCallback
        public final void onDataReady(Data data) {
            DataFetcher.DataCallback<Data> dataCallback = this.mWrapped;
            if (dataCallback == null) {
                return;
            }
            if (data == null) {
                dataCallback.onDataReady(null);
            } else {
                doOnDataReady(dataCallback, data);
            }
        }

        public final void onDataHolderReady(DataHolder dataHolder) {
            DataFetcher.DataCallback<Data> dataCallback = this.mWrapped;
            if (dataCallback == null) {
                return;
            }
            if (dataHolder == null || dataHolder.data == null) {
                dataCallback.onDataReady(null);
            } else {
                doOnDataHolderReady(dataCallback, dataHolder);
            }
        }

        @Override // com.bumptech.glide.load.data.DataFetcher.DataCallback
        public final void onLoadFailed(Exception exc) {
            DataFetcher.DataCallback<Data> dataCallback = this.mWrapped;
            if (dataCallback != null) {
                dataCallback.onLoadFailed(exc);
            }
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            DataFetcher.DataCallback<Data> dataCallback = this.mWrapped;
            if (dataCallback instanceof DataCallbackWrapper) {
                ((DataCallbackWrapper) dataCallback).close();
            }
        }
    }
}
