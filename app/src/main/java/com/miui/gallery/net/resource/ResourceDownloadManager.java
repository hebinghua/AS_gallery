package com.miui.gallery.net.resource;

import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.net.HttpManager;
import com.miui.gallery.net.base.ErrorCode;
import com.miui.gallery.net.base.ResponseListener;
import com.miui.gallery.net.download.GalleryDownloadManager;
import com.miui.gallery.net.download.Request;
import com.miui.gallery.net.download.Verifier;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class ResourceDownloadManager {
    public List<Request> mRequestList = new ArrayList();

    public void download(Resource resource, String str, final Request.Listener listener, final boolean z) {
        long j = resource.id;
        DefaultLogger.d("ResourceDownloadManager", "downloading: %d", Long.valueOf(j));
        if (TextUtils.isEmpty(str)) {
            DefaultLogger.d("ResourceDownloadManager", "download path is empty");
            listener.onComplete(-1);
            return;
        }
        final File file = new File(str);
        if (file.exists()) {
            DefaultLogger.d("ResourceDownloadManager", "file downloaded: %s", file.getAbsolutePath());
            listener.onComplete(0);
            return;
        }
        DownloadRequest downloadRequest = new DownloadRequest(j);
        downloadRequest.setTag("ResourceDownloadManager");
        downloadRequest.execute(new ResponseListener() { // from class: com.miui.gallery.net.resource.ResourceDownloadManager.1
            @Override // com.miui.gallery.net.base.ResponseListener
            public void onResponse(Object... objArr) {
                DownloadInfo downloadInfo = (DownloadInfo) objArr[0];
                DefaultLogger.d("ResourceDownloadManager", String.format("download %s, %s", downloadInfo.url, downloadInfo.sha1));
                Request request = new Request(Uri.parse(downloadInfo.url), file);
                ResourceDownloadManager.this.mRequestList.add(request);
                request.setVerifier(new Verifier.Sha1(downloadInfo.sha1));
                request.setAllowedOverMetered(z);
                request.setListener(listener);
                GalleryDownloadManager.INSTANCE.enqueue(request);
            }

            @Override // com.miui.gallery.net.base.ResponseListener
            public void onResponseError(ErrorCode errorCode, String str2, Object obj) {
                listener.onComplete(-1);
                DefaultLogger.w("ResourceDownloadManager", "errorMessage:%s,errorCode.name:%s", str2, errorCode.name());
            }
        });
    }

    public void cancelAll() {
        HttpManager.getInstance().cancelAll("ResourceDownloadManager");
        for (Request request : this.mRequestList) {
            request.setListener(null);
        }
    }
}
