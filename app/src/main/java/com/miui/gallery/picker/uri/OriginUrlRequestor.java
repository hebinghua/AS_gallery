package com.miui.gallery.picker.uri;

import android.net.Uri;
import android.os.AsyncTask;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.cloud.CloudGalleryOwnerRequestor;
import com.miui.gallery.cloud.CloudGallerySharerRequestor;
import com.miui.gallery.cloud.CloudUrlProvider;
import com.miui.gallery.cloud.RequestCloudItem;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.sdk.download.adapter.BaseUriAdapter;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.micloudsdk.file.MiCloudFileRequestor;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class OriginUrlRequestor {
    public ProgressListener mProgressListener;
    public AsyncTask mRequestAsyncTask;
    public final List<OriginUrlRequestTask> mTasks;

    /* loaded from: classes2.dex */
    public interface ProgressListener {
        void onCancelled();

        void onEnd(ArrayList<OriginInfo> arrayList, boolean z);

        void onStart(int i);

        void onUpdate(int i);
    }

    public OriginUrlRequestor(ArrayList<OriginUrlRequestTask> arrayList, ProgressListener progressListener) {
        this.mTasks = (List) arrayList.clone();
        this.mProgressListener = progressListener;
    }

    public void start(Uri[] uriArr, String[] strArr) {
        final ArrayList<OriginInfo> arrayList = new ArrayList<>(uriArr != null ? uriArr.length : 0);
        if (uriArr == null || strArr == null || uriArr.length != strArr.length) {
            ProgressListener progressListener = this.mProgressListener;
            if (progressListener == null) {
                return;
            }
            progressListener.onEnd(arrayList, false);
            return;
        }
        for (int i = 0; i < uriArr.length; i++) {
            arrayList.add(new OriginInfo(uriArr[i], strArr[i]));
        }
        this.mProgressListener.onStart(this.mTasks.size());
        this.mRequestAsyncTask = new AsyncTask<Void, Integer, Boolean>() { // from class: com.miui.gallery.picker.uri.OriginUrlRequestor.1
            @Override // android.os.AsyncTask
            public Boolean doInBackground(Void... voidArr) {
                MiCloudFileRequestor miCloudFileRequestor;
                if (OriginUrlRequestor.this.mTasks.isEmpty()) {
                    return Boolean.TRUE;
                }
                if (!BaseNetworkUtils.isNetworkConnected() || !BaseGalleryPreferences.CTA.canConnectNetwork()) {
                    return Boolean.FALSE;
                }
                AccountCache.AccountInfo accountInfo = AccountCache.getAccountInfo();
                if (accountInfo == null) {
                    return Boolean.FALSE;
                }
                BaseUriAdapter baseUriAdapter = new BaseUriAdapter();
                MiCloudFileRequestor cloudGalleryOwnerRequestor = new CloudGalleryOwnerRequestor(accountInfo.mAccount, CloudUrlProvider.getUrlProvider(false, false));
                MiCloudFileRequestor cloudGallerySharerRequestor = new CloudGallerySharerRequestor(accountInfo.mAccount, CloudUrlProvider.getUrlProvider(true, false));
                MiCloudFileRequestor cloudGalleryOwnerRequestor2 = new CloudGalleryOwnerRequestor(accountInfo.mAccount, CloudUrlProvider.getUrlProvider(false, true));
                MiCloudFileRequestor cloudGallerySharerRequestor2 = new CloudGallerySharerRequestor(accountInfo.mAccount, CloudUrlProvider.getUrlProvider(true, true));
                for (OriginUrlRequestTask originUrlRequestTask : OriginUrlRequestor.this.mTasks) {
                    if (isCancelled()) {
                        return Boolean.FALSE;
                    }
                    if (originUrlRequestTask.mMediaType != 0) {
                        DBImage dBItemForUri = baseUriAdapter.getDBItemForUri(originUrlRequestTask.mDownloadUri);
                        RequestCloudItem requestCloudItem = new RequestCloudItem(DownloadType.ORIGIN.getPriority(), dBItemForUri);
                        if (dBItemForUri.isShareItem()) {
                            miCloudFileRequestor = originUrlRequestTask.mMediaType == 1 ? cloudGallerySharerRequestor : cloudGallerySharerRequestor2;
                        } else {
                            miCloudFileRequestor = originUrlRequestTask.mMediaType == 1 ? cloudGalleryOwnerRequestor : cloudGalleryOwnerRequestor2;
                        }
                        try {
                            JSONObject requestDownload = miCloudFileRequestor.requestDownload(requestCloudItem);
                            if (requestDownload != null && requestDownload.optString("result").equals("ok")) {
                                ((OriginInfo) arrayList.get(originUrlRequestTask.mPosition)).setOriginDownloadInfo(requestDownload.getJSONObject("data"), originUrlRequestTask.mOriginHeight, originUrlRequestTask.mOriginWidth);
                            }
                        } catch (Exception e) {
                            DefaultLogger.e("OriginUrlRequestor", "request origin download info error" + e);
                        }
                        publishProgress(Integer.valueOf(OriginUrlRequestor.this.mTasks.indexOf(originUrlRequestTask) + 1));
                    }
                }
                return Boolean.TRUE;
            }

            @Override // android.os.AsyncTask
            public void onPostExecute(Boolean bool) {
                if (OriginUrlRequestor.this.mProgressListener != null) {
                    OriginUrlRequestor.this.mProgressListener.onEnd(arrayList, bool.booleanValue());
                }
            }

            @Override // android.os.AsyncTask
            public void onProgressUpdate(Integer... numArr) {
                if (OriginUrlRequestor.this.mProgressListener != null) {
                    OriginUrlRequestor.this.mProgressListener.onUpdate(numArr[0].intValue());
                }
            }

            @Override // android.os.AsyncTask
            public void onCancelled() {
                if (OriginUrlRequestor.this.mProgressListener != null) {
                    OriginUrlRequestor.this.mProgressListener.onCancelled();
                }
            }
        }.execute(new Void[0]);
    }

    public void cancel() {
        AsyncTask asyncTask = this.mRequestAsyncTask;
        if (asyncTask != null) {
            asyncTask.cancel(true);
        }
    }

    public void destroy() {
        AsyncTask asyncTask = this.mRequestAsyncTask;
        if (asyncTask != null) {
            asyncTask.cancel(true);
            this.mRequestAsyncTask = null;
        }
        if (this.mProgressListener != null) {
            this.mProgressListener = null;
        }
    }

    /* loaded from: classes2.dex */
    public static class OriginInfo {
        public static String FILE_SHA1 = "sha1";
        public static String FILE_URI = "file_uri";
        public static String ORIGIN_DOWNLOAD_INFO = "origin_download_info";
        public static String ORIGIN_HEIGHT = "origin_height";
        public static String ORIGIN_WIDTH = "origin_width";
        public Uri mFileUri;
        public int mHeight;
        public JSONObject mOriginDownloadInfo;
        public String mSha1;
        public int mWidth;

        public OriginInfo(Uri uri, String str) {
            this.mFileUri = uri;
            this.mSha1 = str;
        }

        public void setOriginDownloadInfo(JSONObject jSONObject, int i, int i2) {
            this.mOriginDownloadInfo = jSONObject;
            this.mHeight = i;
            this.mWidth = i2;
        }

        public String toJson() {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put(FILE_URI, this.mFileUri);
                jSONObject.put(FILE_SHA1, this.mSha1);
                JSONObject jSONObject2 = this.mOriginDownloadInfo;
                if (jSONObject2 != null) {
                    jSONObject.put(ORIGIN_DOWNLOAD_INFO, jSONObject2);
                    jSONObject.put(ORIGIN_HEIGHT, this.mHeight);
                    jSONObject.put(ORIGIN_WIDTH, this.mWidth);
                }
                return jSONObject.toString();
            } catch (Exception unused) {
                DefaultLogger.e("OriginUrlRequestor", "originInfo to json error");
                return null;
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class OriginUrlRequestTask {
        public Uri mDownloadUri;
        public int mMediaType;
        public int mOriginHeight;
        public int mOriginWidth;
        public int mPosition;

        public OriginUrlRequestTask(int i, Uri uri, int i2, int i3, int i4) {
            this.mPosition = i;
            this.mDownloadUri = uri;
            this.mOriginHeight = i2;
            this.mOriginWidth = i3;
            this.mMediaType = i4;
        }
    }
}
