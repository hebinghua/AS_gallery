package com.miui.gallery.util;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import com.miui.gallery.error.core.ErrorCode;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.util.cloudimageloader.CloudImageLoadingListenerAdapter;
import com.miui.gallery.util.cloudimageloader.CloudImageLoadingProgressListener;
import com.miui.gallery.util.glide.CloudImageLoader;
import com.miui.gallery.util.logger.DefaultLogger;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes2.dex */
public class BulkDownloadHelper {
    public long mCurSize;
    public BulkDownloadListener mDownloadListener;
    public long mTotalSize;
    public List<BulkDownloadItem> mExecutingList = new LinkedList();
    public List<BulkDownloadItem> mSuccessList = new LinkedList();
    public List<BulkDownloadItem> mFailList = new LinkedList();
    public final ConcurrentHashMap<String, Long> mDownloadedSizeMap = new ConcurrentHashMap<>();

    /* loaded from: classes2.dex */
    public interface BulkDownloadListener {
        void onDownloadEnd(List<BulkDownloadItem> list, List<BulkDownloadItem> list2);

        void onDownloadProgress(float f);
    }

    public static /* synthetic */ long access$214(BulkDownloadHelper bulkDownloadHelper, long j) {
        long j2 = bulkDownloadHelper.mCurSize + j;
        bulkDownloadHelper.mCurSize = j2;
        return j2;
    }

    public BulkDownloadHelper() {
        reset();
    }

    public final void reset() {
        this.mExecutingList.clear();
        this.mSuccessList.clear();
        this.mFailList.clear();
        this.mDownloadedSizeMap.clear();
        this.mCurSize = 0L;
        this.mTotalSize = 0L;
        this.mDownloadListener = null;
    }

    public void download(List<BulkDownloadItem> list, boolean z, BulkDownloadListener bulkDownloadListener) {
        if (BaseMiscUtil.isValid(list)) {
            if (z) {
                cancel();
            }
            reset();
            this.mExecutingList.addAll(list);
            for (BulkDownloadItem bulkDownloadItem : this.mExecutingList) {
                this.mTotalSize += bulkDownloadItem.getSize();
            }
            this.mDownloadListener = bulkDownloadListener;
            ArrayList arrayList = new ArrayList(list.size());
            ArrayList arrayList2 = new ArrayList(list.size());
            ArrayList arrayList3 = new ArrayList(list.size());
            ArrayList arrayList4 = new ArrayList(list.size());
            for (BulkDownloadItem bulkDownloadItem2 : list) {
                if (bulkDownloadItem2.getDownloadUri() == null) {
                    DefaultLogger.w("BulkDownloadHelper", "Null download uri for item %s", bulkDownloadItem2);
                    onItemLoadingFail(bulkDownloadItem2);
                } else {
                    arrayList.add(bulkDownloadItem2.getDownloadUri());
                    arrayList2.add(bulkDownloadItem2.getType());
                    BulkItemLoadingListener generatorItemListener = generatorItemListener(bulkDownloadItem2);
                    arrayList3.add(generatorItemListener);
                    arrayList4.add(generatorItemListener);
                }
            }
            CloudImageLoader.getInstance().loadImages(arrayList, arrayList2, arrayList3, arrayList4);
        }
    }

    public final boolean contains(BulkDownloadItem bulkDownloadItem) {
        return this.mExecutingList.contains(bulkDownloadItem);
    }

    public final boolean isDownloadEnd() {
        return this.mSuccessList.size() + this.mFailList.size() == this.mExecutingList.size();
    }

    public final void onDownloadEnd() {
        BulkDownloadListener bulkDownloadListener = this.mDownloadListener;
        if (bulkDownloadListener != null) {
            bulkDownloadListener.onDownloadEnd(this.mSuccessList, this.mFailList);
        }
        reset();
    }

    public final void onItemLoadingFail(BulkDownloadItem bulkDownloadItem) {
        this.mTotalSize -= bulkDownloadItem.getSize();
        this.mFailList.add(bulkDownloadItem);
        if (isDownloadEnd()) {
            onDownloadEnd();
        }
    }

    public final BulkItemLoadingListener generatorItemListener(BulkDownloadItem bulkDownloadItem) {
        return new BulkItemLoadingListener(bulkDownloadItem) { // from class: com.miui.gallery.util.BulkDownloadHelper.1
            @Override // com.miui.gallery.util.BulkDownloadHelper.BulkItemLoadingListener
            public void onLoadingFailed(BulkDownloadItem bulkDownloadItem2, ErrorCode errorCode) {
                DefaultLogger.i("BulkDownloadHelper", "onLoadingFailed %s", bulkDownloadItem2);
                if (BulkDownloadHelper.this.contains(bulkDownloadItem2)) {
                    BulkDownloadHelper.this.onItemLoadingFail(bulkDownloadItem2);
                }
            }

            @Override // com.miui.gallery.util.BulkDownloadHelper.BulkItemLoadingListener
            public void onLoadingSuccess(BulkDownloadItem bulkDownloadItem2) {
                DefaultLogger.d("BulkDownloadHelper", "onLoadingSuccess %s", bulkDownloadItem2);
                if (BulkDownloadHelper.this.contains(bulkDownloadItem2)) {
                    BulkDownloadHelper.access$214(BulkDownloadHelper.this, bulkDownloadItem2.getSize());
                    BulkDownloadHelper.this.mSuccessList.add(bulkDownloadItem2);
                    if (!BulkDownloadHelper.this.isDownloadEnd()) {
                        return;
                    }
                    BulkDownloadHelper.this.onDownloadEnd();
                }
            }

            @Override // com.miui.gallery.util.BulkDownloadHelper.BulkItemLoadingListener
            public void onLoadingProgress(BulkDownloadItem bulkDownloadItem2, long j, long j2) {
                if (!BulkDownloadHelper.this.contains(bulkDownloadItem2) || BulkDownloadHelper.this.mDownloadListener == null) {
                    return;
                }
                BulkDownloadHelper.this.mDownloadListener.onDownloadProgress(BulkDownloadHelper.this.computeTotalDownloadProgress(bulkDownloadItem2, j));
            }
        };
    }

    public final synchronized float computeTotalDownloadProgress(BulkDownloadItem bulkDownloadItem, long j) {
        long j2;
        long j3;
        this.mDownloadedSizeMap.put(bulkDownloadItem.getDownloadUri().toString(), Long.valueOf(j));
        j2 = 0;
        for (Map.Entry<String, Long> entry : this.mDownloadedSizeMap.entrySet()) {
            Long value = entry.getValue();
            j2 += value == null ? 0L : value.longValue();
        }
        j3 = this.mTotalSize;
        return j3 == 0 ? 0.0f : (float) (j2 / j3);
    }

    public void cancel() {
        if (this.mExecutingList.size() > 0) {
            for (BulkDownloadItem bulkDownloadItem : this.mExecutingList) {
                CloudImageLoader.getInstance().cancel(bulkDownloadItem.getDownloadUri(), bulkDownloadItem.getType());
            }
        }
        reset();
    }

    /* loaded from: classes2.dex */
    public static class BulkDownloadItem implements Parcelable {
        public static final Parcelable.Creator<BulkDownloadItem> CREATOR = new Parcelable.Creator<BulkDownloadItem>() { // from class: com.miui.gallery.util.BulkDownloadHelper.BulkDownloadItem.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public BulkDownloadItem mo1670createFromParcel(Parcel parcel) {
                return new BulkDownloadItem(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public BulkDownloadItem[] mo1671newArray(int i) {
                return new BulkDownloadItem[i];
            }
        };
        public String mDownloadPath;
        public Uri mDownloadUri;
        public ErrorCode mErrorCode;
        public String mErrorDesc;
        public long mSize;
        public DownloadType mType;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public BulkDownloadItem(Uri uri, DownloadType downloadType, long j) {
            this.mDownloadUri = uri;
            this.mType = downloadType;
            this.mSize = j;
        }

        public BulkDownloadItem(Parcel parcel) {
            this.mDownloadUri = (Uri) parcel.readParcelable(Uri.class.getClassLoader());
            this.mType = (DownloadType) parcel.readParcelable(DownloadType.class.getClassLoader());
            this.mSize = parcel.readLong();
        }

        public void setDownloadPath(String str) {
            this.mDownloadPath = str;
        }

        public void setErrorCode(ErrorCode errorCode) {
            this.mErrorCode = errorCode;
        }

        public void setErrorDesc(String str) {
            this.mErrorDesc = str;
        }

        public Uri getDownloadUri() {
            return this.mDownloadUri;
        }

        public DownloadType getType() {
            return this.mType;
        }

        public long getSize() {
            return this.mSize;
        }

        public String getDownloadPath() {
            return this.mDownloadPath;
        }

        public ErrorCode getErrorCode() {
            return this.mErrorCode;
        }

        public String getErrorDesc() {
            return this.mErrorDesc;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeParcelable(this.mDownloadUri, i);
            parcel.writeParcelable(this.mType, i);
            parcel.writeLong(this.mSize);
        }

        public String toString() {
            return String.format(Locale.US, "Uri[%s], ImageType[%s]", this.mDownloadUri, this.mType.name());
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class BulkItemLoadingListener extends CloudImageLoadingListenerAdapter implements CloudImageLoadingProgressListener {
        public WeakReference<BulkDownloadItem> mDownloadItemRef;

        public abstract void onLoadingFailed(BulkDownloadItem bulkDownloadItem, ErrorCode errorCode);

        public abstract void onLoadingProgress(BulkDownloadItem bulkDownloadItem, long j, long j2);

        public abstract void onLoadingSuccess(BulkDownloadItem bulkDownloadItem);

        public BulkItemLoadingListener(BulkDownloadItem bulkDownloadItem) {
            this.mDownloadItemRef = new WeakReference<>(bulkDownloadItem);
        }

        public final boolean isValidate() {
            WeakReference<BulkDownloadItem> weakReference = this.mDownloadItemRef;
            return (weakReference == null || weakReference.get() == null) ? false : true;
        }

        @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
        public void onDownloadComplete(Uri uri, DownloadType downloadType, View view, String str) {
            if (isValidate()) {
                this.mDownloadItemRef.get().setDownloadPath(str);
            }
        }

        @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
        public void onLoadingFailed(Uri uri, DownloadType downloadType, View view, ErrorCode errorCode, String str) {
            if (isValidate()) {
                this.mDownloadItemRef.get().setErrorCode(errorCode);
                this.mDownloadItemRef.get().setErrorDesc(str);
                onLoadingFailed(this.mDownloadItemRef.get(), errorCode);
                return;
            }
            DefaultLogger.i("BulkDownloadHelper", "onLoadingFailed not validate");
        }

        @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListenerAdapter, com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
        public void onLoadingComplete(Uri uri, DownloadType downloadType, View view, Bitmap bitmap) {
            if (isValidate()) {
                onLoadingSuccess(this.mDownloadItemRef.get());
            } else {
                DefaultLogger.d("BulkDownloadHelper", "onLoadingComplete not validate");
            }
        }

        @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingProgressListener
        public void onProgressUpdate(Uri uri, DownloadType downloadType, View view, int i, int i2) {
            if (!isValidate()) {
                DefaultLogger.d("BulkDownloadHelper", "onProgressUpdate not validate");
            } else if (i > i2) {
            } else {
                long size = this.mDownloadItemRef.get().getSize();
                onLoadingProgress(this.mDownloadItemRef.get(), ((float) size) * ((i * 1.0f) / i2), size);
            }
        }
    }
}
