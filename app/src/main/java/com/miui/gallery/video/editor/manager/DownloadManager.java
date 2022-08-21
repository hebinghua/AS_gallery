package com.miui.gallery.video.editor.manager;

import android.net.Uri;
import com.miui.gallery.net.HttpManager;
import com.miui.gallery.net.base.ErrorCode;
import com.miui.gallery.net.base.ResponseListener;
import com.miui.gallery.net.download.GalleryDownloadManager;
import com.miui.gallery.net.download.Request;
import com.miui.gallery.net.download.Verifier;
import com.miui.gallery.net.resource.DownloadInfo;
import com.miui.gallery.net.resource.DownloadRequest;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.video.editor.DownloadCommand;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class DownloadManager {
    public List<Request> mRequestList = new ArrayList();

    public void download(DownloadCommand downloadCommand, final Request.Listener listener) {
        if (downloadCommand == null) {
            DefaultLogger.w("DownloadManager", "resource is null, download is fail.");
            return;
        }
        final String zipPath = downloadCommand.getZipPath();
        DownloadRequest downloadRequest = new DownloadRequest(downloadCommand.getId());
        downloadRequest.setTag("DownloadManager");
        downloadRequest.execute(new ResponseListener() { // from class: com.miui.gallery.video.editor.manager.DownloadManager.1
            @Override // com.miui.gallery.net.base.ResponseListener
            public void onResponse(Object... objArr) {
                DownloadInfo downloadInfo = (DownloadInfo) objArr[0];
                DefaultLogger.d("DownloadManager", String.format("download %s, %s", downloadInfo.url, downloadInfo.sha1));
                File file = new File(zipPath);
                if (file.exists()) {
                    DefaultLogger.d("DownloadManager", "the file already exist and its path is : %s", file.getAbsolutePath());
                    file.delete();
                }
                Request request = new Request(Uri.parse(downloadInfo.url), file);
                DownloadManager.this.mRequestList.add(request);
                request.setVerifier(new Verifier.Sha1(downloadInfo.sha1));
                request.setAllowedOverMetered(BaseNetworkUtils.isActiveNetworkMetered());
                request.setListener(listener);
                GalleryDownloadManager.INSTANCE.enqueue(request);
            }

            @Override // com.miui.gallery.net.base.ResponseListener
            public void onResponseError(ErrorCode errorCode, String str, Object obj) {
                listener.onComplete(-1);
                DefaultLogger.w("DownloadManager", "errorMessage:%s,errorCode.name:5s", str, errorCode.name());
            }
        });
    }

    public void cancelAll() {
        HttpManager.getInstance().cancelAll("DownloadManager");
        for (Request request : this.mRequestList) {
            request.setListener(null);
        }
        this.mRequestList.clear();
    }
}
