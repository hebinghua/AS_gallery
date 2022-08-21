package com.miui.gallery.search.core.display.icon;

import android.content.Context;
import android.content.UriMatcher;
import android.net.Uri;
import android.widget.ImageView;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.glide.load.GalleryOptions;
import com.miui.gallery.glide.load.RegionConfig;
import com.miui.gallery.provider.cache.SearchIconItem;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.search.core.Consumer;
import com.miui.gallery.search.core.context.SearchContext;
import com.miui.gallery.search.core.display.icon.IconLoaderTask;
import com.miui.gallery.search.utils.SearchLog;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.face.FaceRegionRectF;
import com.miui.gallery.util.glide.BindImageHelper;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;

/* loaded from: classes2.dex */
public class IconImageLoader {
    public static final UriMatcher sURIMatcher;
    public Cache<String, SearchIconItem> mMemoryCache;

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static final IconImageLoader INSTANCE = new IconImageLoader();
    }

    static {
        UriMatcher uriMatcher = new UriMatcher(-1);
        sURIMatcher = uriMatcher;
        uriMatcher.addURI("web", null, 1);
    }

    public static IconImageLoader getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public IconImageLoader() {
        this.mMemoryCache = CacheBuilder.newBuilder().maximumSize(100L).recordStats().build();
    }

    public Cache<String, SearchIconItem> getMemoryCache() {
        return this.mMemoryCache;
    }

    public static void submitTask(IconLoaderTask iconLoaderTask) {
        if (iconLoaderTask.isUseDiskCache()) {
            SearchContext.getInstance().getIconLoaderCacheExecutor().submit(iconLoaderTask);
        } else {
            SearchContext.getInstance().getIconLoaderExecutor().submit(iconLoaderTask);
        }
    }

    public final void internalDisplayImage(Context context, Uri uri, DownloadType downloadType, ImageView imageView, RequestOptions requestOptions, boolean z, List<String> list) {
        boolean z2 = uri != null && "image".equals(uri.getScheme());
        if (requestOptions.getPlaceholderDrawable() != null) {
            imageView.setImageDrawable(requestOptions.getPlaceholderDrawable());
        } else if (requestOptions.getPlaceholderId() != 0) {
            imageView.setImageResource(requestOptions.getPlaceholderId());
        }
        if (!z2) {
            Glide.with(imageView).mo987load(uri).mo946apply((BaseRequestOptions<?>) requestOptions).into(imageView);
        } else if (sURIMatcher.match(uri) == 1) {
            Glide.with(imageView).mo987load(Uri.parse(uri.getQueryParameter(MapBundleKey.MapObjKey.OBJ_URL))).mo946apply((BaseRequestOptions<?>) requestOptions).into(imageView);
        } else {
            IconLoaderHolder imageHolder = IconLoaderHolder.getImageHolder(imageView);
            if (generateKey(uri, imageView).equals(imageHolder.getKey()) && imageHolder.hasRunningTask()) {
                imageHolder.displayResult();
            } else {
                cancelHolderTask(imageHolder);
            }
            imageHolder.set(imageView, uri, downloadType, requestOptions, list);
            SearchIconItem ifPresent = this.mMemoryCache.getIfPresent(uri.toString());
            if (ifPresent != null) {
                Uri uri2 = null;
                FaceRegionRectF faceRegionRectF = (ifPresent.decodeRegionW == null || ifPresent.decodeRegionH == null || ifPresent.decodeRegionX == null || ifPresent.decodeRegionY == null) ? null : new FaceRegionRectF(ifPresent.decodeRegionX.floatValue(), ifPresent.decodeRegionY.floatValue(), ifPresent.decodeRegionX.floatValue() + ifPresent.decodeRegionW.floatValue(), ifPresent.decodeRegionY.floatValue() + ifPresent.decodeRegionH.floatValue(), ifPresent.decodeRegionOrientation);
                String str = ifPresent.iconUri;
                Uri parse = str != null ? Uri.parse(str) : null;
                String str2 = ifPresent.filePath;
                String str3 = ifPresent.downloadUri;
                if (str3 != null) {
                    uri2 = Uri.parse(str3);
                }
                imageHolder.consume(new IconLoaderTask.IconLoaderResult(parse, str2, uri2, faceRegionRectF, false));
                return;
            }
            IconLoaderTask iconLoaderTask = new IconLoaderTask(context, uri, imageHolder, ThreadManager.getMainHandler(), z, z, false);
            imageHolder.setTask(iconLoaderTask);
            submitTask(iconLoaderTask);
        }
    }

    public void displayImage(Context context, Uri uri, DownloadType downloadType, ImageView imageView, RequestOptions requestOptions, List<String> list) {
        internalDisplayImage(context, uri, downloadType, imageView, requestOptions, false, list);
    }

    public void displayImageEager(Context context, Uri uri, DownloadType downloadType, ImageView imageView, RequestOptions requestOptions) {
        displayImageEager(context, uri, downloadType, imageView, requestOptions, null);
    }

    public void displayImageEager(Context context, Uri uri, DownloadType downloadType, ImageView imageView, RequestOptions requestOptions, List<String> list) {
        internalDisplayImage(context, uri, downloadType, imageView, requestOptions, true, list);
    }

    public final void cancelHolderTask(IconLoaderHolder iconLoaderHolder) {
        IconLoaderTask task = iconLoaderHolder.getTask();
        if (task != null) {
            SearchLog.d("IconImageLoader", "Cancel holder task %s", iconLoaderHolder.getKey());
            if (task.isUseDiskCache()) {
                SearchContext.getInstance().getIconLoaderCacheExecutor().cancel(task);
            } else {
                SearchContext.getInstance().getIconLoaderExecutor().cancel(task);
            }
            task.setCancelled();
            iconLoaderHolder.setTask(null);
        }
    }

    public static String generateKey(Uri uri, ImageView imageView) {
        return (uri == null || imageView == null) ? "" : uri.buildUpon().appendQueryParameter("imageView", String.valueOf(imageView.hashCode())).build().toString();
    }

    /* loaded from: classes2.dex */
    public static class IconLoaderHolder implements Consumer<IconLoaderTask.IconLoaderResult> {
        public List<String> mBackupIcons;
        public int mBackupIndex = 0;
        public RequestOptions mDisplayImageOptions;
        public DownloadType mImageType;
        public IconLoaderTask.IconLoaderResult mLastResult;
        public IconLoaderTask.IconLoaderResult mResult;
        public IconLoaderTask mTask;
        public Uri mUri;
        public WeakReference<ImageView> mViewRef;

        @Override // com.miui.gallery.search.core.Consumer
        public boolean consume(IconLoaderTask.IconLoaderResult iconLoaderResult) {
            List<String> list;
            this.mLastResult = this.mResult;
            if (iconLoaderResult == null) {
                this.mResult = null;
                this.mTask = null;
            } else if (iconLoaderResult.isValid() && iconLoaderResult.iconUri.equals(this.mUri)) {
                String queryParameter = this.mUri.getQueryParameter("serverId");
                if (queryParameter != null) {
                    SearchLog.d("IconImageLoader", "Load task finished for serverId %s, result is valid %s", queryParameter, Boolean.valueOf(iconLoaderResult.isValid()));
                } else {
                    SearchLog.d("IconImageLoader", "Load task finished for uri %s, result is valid %s", this.mUri, Boolean.valueOf(iconLoaderResult.isValid()));
                }
                this.mResult = iconLoaderResult;
            }
            if (!Objects.equals(this.mLastResult, this.mResult)) {
                displayResult();
            }
            if (this.mTask != null && iconLoaderResult != null && iconLoaderResult.isFromUnreliableCache()) {
                IconLoaderTask iconLoaderTask = new IconLoaderTask(GalleryApp.sGetAndroidContext(), this.mUri, this, ThreadManager.getMainHandler(), false, true, true);
                this.mTask = iconLoaderTask;
                IconImageLoader.submitTask(iconLoaderTask);
                return true;
            }
            IconLoaderTask.IconLoaderResult iconLoaderResult2 = this.mResult;
            if ((iconLoaderResult2 == null || (iconLoaderResult2.localFilePath == null && iconLoaderResult2.downloadUri == null)) && (list = this.mBackupIcons) != null && !list.isEmpty()) {
                tryWithBackupIcon();
                return true;
            }
            this.mTask = null;
            return true;
        }

        public final void tryWithBackupIcon() {
            List<String> list;
            if (this.mBackupIndex < 3 && (list = this.mBackupIcons) != null) {
                int size = list.size();
                int i = this.mBackupIndex;
                if (size > i && this.mBackupIcons.get(i) != null) {
                    this.mUri = Uri.parse(this.mBackupIcons.get(this.mBackupIndex));
                    IconLoaderTask iconLoaderTask = new IconLoaderTask(GalleryApp.sGetAndroidContext(), this.mUri, this, ThreadManager.getMainHandler(), false, true, true);
                    this.mTask = iconLoaderTask;
                    IconImageLoader.submitTask(iconLoaderTask);
                    this.mBackupIndex++;
                    return;
                }
            }
            this.mTask = null;
        }

        public void displayResult() {
            ImageView imageView = getImageView();
            if (imageView != null) {
                if (this.mResult != null) {
                    IconLoaderTask.IconLoaderResult iconLoaderResult = this.mResult;
                    BindImageHelper.bindImage(iconLoaderResult.localFilePath, iconLoaderResult.downloadUri, this.mImageType, imageView, this.mDisplayImageOptions.clone().mo975set(GalleryOptions.DECODE_REGION, RegionConfig.ofFace(this.mResult.facePositionRect, 2.0f)));
                } else if (this.mDisplayImageOptions.getFallbackDrawable() != null) {
                    imageView.setImageDrawable(this.mDisplayImageOptions.getFallbackDrawable());
                } else if (this.mDisplayImageOptions.getFallbackId() == 0) {
                } else {
                    imageView.setImageResource(this.mDisplayImageOptions.getFallbackId());
                }
            }
        }

        public void set(ImageView imageView, Uri uri, DownloadType downloadType, RequestOptions requestOptions, List<String> list) {
            this.mViewRef = new WeakReference<>(imageView);
            this.mUri = uri;
            this.mDisplayImageOptions = requestOptions;
            this.mImageType = downloadType;
            this.mResult = null;
            this.mBackupIndex = 0;
            this.mBackupIcons = list;
        }

        public void setTask(IconLoaderTask iconLoaderTask) {
            this.mTask = iconLoaderTask;
        }

        public IconLoaderTask getTask() {
            return this.mTask;
        }

        public boolean hasRunningTask() {
            IconLoaderTask iconLoaderTask = this.mTask;
            return iconLoaderTask != null && !iconLoaderTask.isCancelled();
        }

        public ImageView getImageView() {
            WeakReference<ImageView> weakReference = this.mViewRef;
            if (weakReference != null) {
                return weakReference.get();
            }
            return null;
        }

        public static IconLoaderHolder getImageHolder(ImageView imageView) {
            Object tag = imageView.getTag(R.id.tag_icon_loader_holder);
            if (tag == null) {
                tag = new IconLoaderHolder();
                imageView.setTag(R.id.tag_icon_loader_holder, tag);
            }
            return (IconLoaderHolder) tag;
        }

        public String getKey() {
            return IconImageLoader.generateKey(this.mUri, getImageView());
        }
    }
}
