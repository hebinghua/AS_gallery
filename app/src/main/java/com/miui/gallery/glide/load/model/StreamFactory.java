package com.miui.gallery.glide.load.model;

import android.content.Context;
import android.net.Uri;
import ch.qos.logback.core.joran.action.Action;
import com.bumptech.glide.load.Options;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.glide.Utils;
import com.miui.gallery.glide.load.GalleryOptions;
import com.miui.gallery.photosapi.PhotosOemApi;
import com.miui.gallery.provider.ProcessingMediaManager;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.CryptoUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.ProcessingMediaHelper;
import com.miui.gallery.util.SecretAlbumCryptoUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public class StreamFactory extends Factory<InputStream> {
    public static final List<ISourceProvider> SOURCE_PROVIDERS;

    /* loaded from: classes2.dex */
    public interface ISourceProvider {
        InputStreamHolder acquire(Uri uri, String str, Options options) throws IOException;

        boolean handles(Options options);
    }

    static {
        LinkedList linkedList = new LinkedList();
        SOURCE_PROVIDERS = linkedList;
        linkedList.add(new ProcessingMediaSourceProvider());
        linkedList.add(new SecretFileSourceProvider());
        linkedList.add(new AccompanyFileSourceProvider());
    }

    public StreamFactory(Context context) {
        super(context, new GalleryModelOpener<InputStream>() { // from class: com.miui.gallery.glide.load.model.StreamFactory.1
            @Override // com.miui.gallery.glide.load.model.GalleryModelOpener
            public DataHolder<InputStream> open(GalleryModel galleryModel, int i, int i2, Options options) {
                String path = galleryModel.getPath();
                Uri parseUri = Utils.parseUri(path);
                if (parseUri != null && parseUri.getPath() != null) {
                    for (ISourceProvider iSourceProvider : StreamFactory.SOURCE_PROVIDERS) {
                        if (iSourceProvider.handles(options)) {
                            try {
                                InputStreamHolder acquire = iSourceProvider.acquire(parseUri, path, options);
                                if (acquire != null) {
                                    return acquire;
                                }
                            } catch (IOException | RuntimeException e) {
                                DefaultLogger.v("StreamFactory", e);
                            }
                        }
                    }
                    return null;
                }
                throw new IllegalArgumentException("Invalid path: " + path);
            }

            @Override // com.miui.gallery.glide.load.model.GalleryModelOpener
            public void close(DataHolder<InputStream> dataHolder) throws IOException {
                dataHolder.data.close();
            }
        }, InputStream.class);
    }

    /* loaded from: classes2.dex */
    public static class ProcessingMediaSourceProvider implements ISourceProvider {
        public ProcessingMediaSourceProvider() {
        }

        @Override // com.miui.gallery.glide.load.model.StreamFactory.ISourceProvider
        public boolean handles(Options options) {
            return ProcessingMediaManager.CAMERA_PROVIDER_VERSION.get(null).intValue() >= 2;
        }

        @Override // com.miui.gallery.glide.load.model.StreamFactory.ISourceProvider
        public InputStreamHolder acquire(Uri uri, String str, Options options) throws FileNotFoundException {
            ProcessingMediaHelper.ProcessingItem matchItem = ProcessingMediaHelper.getInstance().matchItem(uri.toString());
            if (matchItem == null) {
                return null;
            }
            Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
            Uri queryProcessingUri = PhotosOemApi.getQueryProcessingUri(sGetAndroidContext, matchItem.getMediaStoreId());
            DefaultLogger.v("StreamFactory", "[%s] is processing media, convert to [%s]", uri, queryProcessingUri);
            return new InputStreamHolder(sGetAndroidContext.getContentResolver().openInputStream(queryProcessingUri));
        }
    }

    /* loaded from: classes2.dex */
    public static class SecretFileSourceProvider implements ISourceProvider {
        public SecretFileSourceProvider() {
        }

        @Override // com.miui.gallery.glide.load.model.StreamFactory.ISourceProvider
        public boolean handles(Options options) {
            return options.get(GalleryOptions.SECRET_KEY) != null;
        }

        @Override // com.miui.gallery.glide.load.model.StreamFactory.ISourceProvider
        public InputStreamHolder acquire(Uri uri, String str, Options options) throws FileNotFoundException {
            byte[] bArr = (byte[]) options.get(GalleryOptions.SECRET_KEY);
            if (!Action.FILE_ATTRIBUTE.equalsIgnoreCase(uri.getScheme())) {
                throw new FileNotFoundException("Secret file only support file scheme, path: " + str);
            }
            String path = uri.getPath();
            String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("StreamFactory", "acquire");
            if (CloudUtils.SecretAlbumUtils.isEncryptedVideoByPath(path)) {
                Uri decryptVideo2CacheFolder = SecretAlbumCryptoUtils.decryptVideo2CacheFolder(Uri.fromFile(new File(path)), bArr, -1L);
                if (decryptVideo2CacheFolder == null || decryptVideo2CacheFolder.getPath() == null) {
                    throw new FileNotFoundException("Failed to decrypt video, path: " + str);
                }
                return new InputStreamHolder(StorageSolutionProvider.get().openInputStream(StorageSolutionProvider.get().getDocumentFile(decryptVideo2CacheFolder.getPath(), IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag)));
            } else if (CloudUtils.SecretAlbumUtils.isUnencryptedVideoByPath(path)) {
                return new InputStreamHolder(StorageSolutionProvider.get().openInputStream(StorageSolutionProvider.get().getDocumentFile(path, IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag)));
            } else {
                if (CloudUtils.SecretAlbumUtils.isUnencryptedImageByPath(path)) {
                    return new InputStreamHolder(StorageSolutionProvider.get().openInputStream(StorageSolutionProvider.get().getDocumentFile(path, IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag)));
                }
                return new InputStreamHolder(CryptoUtil.getDecryptCipherInputStream(StorageSolutionProvider.get().openInputStream(StorageSolutionProvider.get().getDocumentFile(path, IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag)), bArr));
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class AccompanyFileSourceProvider implements ISourceProvider {
        public AccompanyFileSourceProvider() {
        }

        @Override // com.miui.gallery.glide.load.model.StreamFactory.ISourceProvider
        public boolean handles(Options options) {
            return !((Boolean) options.get(GalleryOptions.SKIP_ACCOMPANY_FILE)).booleanValue();
        }

        @Override // com.miui.gallery.glide.load.model.StreamFactory.ISourceProvider
        public InputStreamHolder acquire(Uri uri, String str, Options options) throws IOException {
            InputStream inputStream;
            String parseMimeType = Utils.parseMimeType(uri, options);
            int i = 1;
            if (!Action.FILE_ATTRIBUTE.equalsIgnoreCase(uri.getScheme()) || !((Boolean) options.get(GalleryOptions.SMALL_SIZE)).booleanValue() || !BaseFileMimeUtil.isRawFromMimeType(parseMimeType)) {
                i = -1;
                inputStream = null;
            } else {
                inputStream = (InputStream) ThumbFetcherManager.request(InputStream.class, str, 1);
            }
            if (inputStream != null) {
                return new InputStreamHolder(inputStream, i);
            }
            return null;
        }
    }
}
