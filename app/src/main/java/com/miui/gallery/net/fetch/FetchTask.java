package com.miui.gallery.net.fetch;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.net.base.RequestError;
import com.miui.gallery.net.download.GalleryDownloadManager;
import com.miui.gallery.net.download.Verifier;
import com.miui.gallery.net.resource.DownloadInfo;
import com.miui.gallery.net.resource.DownloadRequest;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.ZipUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/* loaded from: classes2.dex */
public class FetchTask {
    public Future mFuture;
    public Request mRequest;
    public volatile int mStatus = 0;
    public Handler mHandler = new Handler(Looper.getMainLooper());

    public FetchTask(Request request) {
        this.mRequest = request;
    }

    public void cancel() {
        Future future = this.mFuture;
        if (future != null) {
            future.cancel(true);
        }
    }

    public int getStatus() {
        return this.mStatus;
    }

    public void execute(ExecutorService executorService) {
        this.mStatus = 1;
        if (this.mRequest.getListener() != null) {
            this.mRequest.getListener().onStart();
        }
        this.mFuture = executorService.submit(new Runnable() { // from class: com.miui.gallery.net.fetch.FetchTask.1
            @Override // java.lang.Runnable
            public void run() {
                FetchTask.this.postResult(FetchTask.this.process());
            }
        });
    }

    public final void postResult(final boolean z) {
        this.mStatus = z ? 2 : 3;
        this.mHandler.post(new Runnable() { // from class: com.miui.gallery.net.fetch.FetchTask.2
            @Override // java.lang.Runnable
            public void run() {
                if (z) {
                    if (FetchTask.this.mRequest.getListener() == null) {
                        return;
                    }
                    FetchTask.this.mRequest.getListener().onSuccess();
                } else if (FetchTask.this.mRequest.getListener() == null) {
                } else {
                    FetchTask.this.mRequest.getListener().onFail();
                }
            }
        });
    }

    public static File getTempFile(File file) {
        String parent = file.getParent();
        return new File(parent, file.getName() + "_download");
    }

    public final boolean downloadAndZip(File file, File file2, File file3) {
        try {
            Object[] executeSync = new DownloadRequest(this.mRequest.getId()).executeSync();
            if (executeSync != null && executeSync.length != 0) {
                DefaultLogger.d("FetchTask", "get resource data info");
                DownloadInfo downloadInfo = (DownloadInfo) executeSync[0];
                com.miui.gallery.net.download.Request request = new com.miui.gallery.net.download.Request(Uri.parse(downloadInfo.url), file);
                request.setVerifier(new Verifier.Sha1(downloadInfo.sha1));
                request.setAllowedOverMetered(true);
                if (GalleryDownloadManager.INSTANCE.download(request) != 0) {
                    return false;
                }
                DefaultLogger.d("FetchTask", "complete download resource data");
                ZipUtils.unzip(file, file2);
                DefaultLogger.d("FetchTask", "unzip resource to %s", file2.getPath());
                if (!file2.renameTo(file3)) {
                    DefaultLogger.d("FetchTask", "rename dest dir fail %s", file3.getPath());
                    return false;
                }
                DefaultLogger.d("FetchTask", "rename %s to %s", file2, file3);
                return true;
            }
            return false;
        } catch (RequestError e) {
            e.printStackTrace();
            return false;
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
            return false;
        } catch (IOException e3) {
            e3.printStackTrace();
            return false;
        }
    }

    public boolean process() {
        DocumentFile documentFile;
        this.mRequest.deleteHistoricVersion();
        File destDir = this.mRequest.destDir();
        if (destDir.exists() && (documentFile = StorageSolutionProvider.get().getDocumentFile(destDir.getAbsolutePath(), IStoragePermissionStrategy.Permission.DELETE, FileHandleRecordHelper.appendInvokerTag("FetchTask", "process"))) != null) {
            documentFile.delete();
        }
        File tempFile = getTempFile(destDir);
        if (tempFile.exists()) {
            DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(tempFile.getAbsolutePath(), IStoragePermissionStrategy.Permission.DELETE, FileHandleRecordHelper.appendInvokerTag("FetchTask", "process"));
            if (documentFile2 == null || !documentFile2.delete()) {
                return false;
            }
        } else if (!tempFile.mkdirs()) {
            return false;
        }
        File zipFile = this.mRequest.zipFile();
        if (!zipFile.exists() || zipFile.delete()) {
            if (!downloadAndZip(zipFile, tempFile, destDir)) {
                tempFile.delete();
                zipFile.delete();
                destDir.delete();
                return false;
            }
            return zipFile.delete();
        }
        return false;
    }
}
