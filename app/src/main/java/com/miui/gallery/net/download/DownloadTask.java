package com.miui.gallery.net.download;

import android.os.AsyncTask;
import com.miui.gallery.net.download.Request;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.d.i;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/* loaded from: classes2.dex */
public class DownloadTask {
    public static final long RETRY_INTERVAL_MILLI = TimeUnit.SECONDS.toMillis(10);
    public CoreTask mCoreTask;
    public TaskInfo mTaskInfo;

    public static boolean isRetryState(int i) {
        return i == 12 || i == 11;
    }

    public static int translateErrorCode(int i) {
        if (i != 0) {
            int i2 = 1;
            if (i != 1) {
                i2 = 2;
                if (i != 2) {
                    i2 = 3;
                    if (i != 3) {
                        return 11;
                    }
                }
            }
            return i2;
        }
        return 0;
    }

    public DownloadTask(Request request) {
        this.mCoreTask = new CoreTask(request);
    }

    public boolean cancel(boolean z) {
        return this.mCoreTask.cancel(z);
    }

    public void execute(Executor executor) {
        this.mCoreTask.executeOnExecutor(executor, new Void[0]);
    }

    public int execute() {
        return this.mCoreTask.doInBackground(new Void[0]).intValue();
    }

    public boolean isDone() {
        return this.mCoreTask.getStatus() == AsyncTask.Status.FINISHED;
    }

    public final int process(Request request) {
        int tryDownload;
        DefaultLogger.d("DownloadTask", "start to download request[%s, %s]", request.getUri(), request.getDestination());
        preProcess(request);
        int maxRetryTimes = request.getMaxRetryTimes();
        int i = 0;
        do {
            tryDownload = tryDownload(request);
            if (!isRetryState(tryDownload)) {
                break;
            }
            DefaultLogger.d("DownloadTask", "retry for %d", Integer.valueOf(tryDownload));
            try {
                Thread.sleep(RETRY_INTERVAL_MILLI, 0);
                i++;
            } catch (InterruptedException unused) {
                tryDownload = 5;
            }
        } while (i <= maxRetryTimes);
        return postProcess(tryDownload);
    }

    public final void preProcess(final Request request) {
        this.mTaskInfo = new TaskInfo();
        if (request.getListener() != null) {
            ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.net.download.DownloadTask.1
                @Override // java.lang.Runnable
                public void run() {
                    Request.Listener listener = request.getListener();
                    if (listener != null) {
                        listener.onStart();
                    }
                }
            });
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:105:0x0142 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:97:0x014c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final int tryDownload(com.miui.gallery.net.download.Request r14) {
        /*
            Method dump skipped, instructions count: 341
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.net.download.DownloadTask.tryDownload(com.miui.gallery.net.download.Request):int");
    }

    public final void configure(HttpURLConnection httpURLConnection) {
        httpURLConnection.setConnectTimeout(10000);
        httpURLConnection.setReadTimeout(i.b);
        httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
    }

    public final void processHeader(HttpURLConnection httpURLConnection) {
        this.mTaskInfo.mContentLength = httpURLConnection.getContentLength();
        DefaultLogger.d("DownloadTask", "content length: %d", Long.valueOf(this.mTaskInfo.mContentLength));
    }

    public final void preTransferContent(Request request) {
        Verifier verifier = request.getVerifier();
        if (verifier != null) {
            DefaultLogger.d("DownloadTask", "need verify, try to get MessageDigest");
            this.mTaskInfo.mDigest = verifier.getInstance();
        }
    }

    public final void performProgressUpdate(byte[] bArr, int i) {
        int i2;
        TaskInfo taskInfo = this.mTaskInfo;
        long j = taskInfo.mDownloadSize;
        taskInfo.mDownloadSize = i + j;
        MessageDigest messageDigest = taskInfo.mDigest;
        if (messageDigest != null) {
            messageDigest.update(bArr, 0, i);
        }
        TaskInfo taskInfo2 = this.mTaskInfo;
        long j2 = taskInfo2.mContentLength;
        if (j2 <= 0 || ((int) ((j / j2) * 100.0d)) == (i2 = (int) ((taskInfo2.mDownloadSize / j2) * 100.0d))) {
            return;
        }
        this.mCoreTask.publishProgress(i2);
    }

    public final int postTransferContent() {
        if (this.mCoreTask.mRequest.getVerifier() == null || this.mCoreTask.mRequest.getVerifier().verify(this.mTaskInfo.mDigest.digest())) {
            DefaultLogger.d("DownloadTask", "verify success");
            return 0;
        }
        DefaultLogger.d("DownloadTask", "verify fail");
        return 6;
    }

    public final int postProcess(int i) {
        if (i != 0) {
            File tempFile = getTempFile(this.mCoreTask.mRequest.getDestination());
            if (tempFile.exists() && !tempFile.delete()) {
                DefaultLogger.d("DownloadTask", "delete tmp file failed %s", tempFile);
            }
        } else {
            File destination = this.mCoreTask.mRequest.getDestination();
            File tempFile2 = getTempFile(destination);
            if (!tempFile2.exists()) {
                DefaultLogger.w("DownloadTask", "downloaded file missing");
                return 9;
            } else if (!tempFile2.renameTo(destination)) {
                DefaultLogger.w("DownloadTask", "downloaded file rename failed");
                return 9;
            } else {
                DefaultLogger.d("DownloadTask", "rename tmp file success");
            }
        }
        return i;
    }

    /* loaded from: classes2.dex */
    public class CoreTask extends AsyncTask<Void, Integer, Integer> {
        public Request mRequest;

        public CoreTask(Request request) {
            this.mRequest = request;
        }

        @Override // android.os.AsyncTask
        public Integer doInBackground(Void... voidArr) {
            return Integer.valueOf(DownloadTask.this.process(this.mRequest));
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Integer num) {
            DefaultLogger.d("DownloadTask", "process download finish %d", num);
            onComplete(num.intValue());
        }

        @Override // android.os.AsyncTask
        public void onProgressUpdate(Integer... numArr) {
            Request.Listener listener = this.mRequest.getListener();
            if (listener != null) {
                listener.onProgressUpdate(numArr[0].intValue());
            }
        }

        @Override // android.os.AsyncTask
        public void onCancelled(Integer num) {
            int intValue = num != null ? num.intValue() : -2;
            DefaultLogger.d("DownloadTask", "process download cancelled %d", Integer.valueOf(intValue));
            onComplete(intValue);
        }

        public void publishProgress(int i) {
            if (this.mRequest.getListener() != null) {
                super.publishProgress(Integer.valueOf(i));
            }
        }

        public final void onComplete(int i) {
            Request request = this.mRequest;
            if (request != null) {
                Request.Listener listener = request.getListener();
                if (listener != null) {
                    listener.onComplete(i);
                }
                this.mRequest.setListener(null);
                this.mRequest = null;
            }
        }
    }

    public static File getTempFile(File file) {
        String parent = file.getParent();
        return new File(parent, file.getName() + ".download");
    }

    public static int translateResponseCode(int i) {
        if (i != 200) {
            DefaultLogger.d("DownloadTask", "processing http code %d", Integer.valueOf(i));
            int i2 = i / 100;
            if (i2 == 3) {
                return 7;
            }
            if (i2 == 4) {
                return i == 408 ? 12 : 7;
            } else if (i2 != 5) {
                return 7;
            } else {
                return i == 504 ? 12 : 8;
            }
        }
        DefaultLogger.d("DownloadTask", "http status is ok");
        return 0;
    }

    public static OutputStream openOutputStream(File file) {
        File parentFile = file.getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs()) {
            DefaultLogger.d("DownloadTask", "create folder failed");
            return null;
        }
        if (file.exists()) {
            if (file.isDirectory()) {
                DefaultLogger.d("DownloadTask", "output file is a directory");
                return null;
            }
            DefaultLogger.w("DownloadTask", "output file will be overwritten");
        }
        File tempFile = getTempFile(file);
        if (tempFile.exists()) {
            DefaultLogger.w("DownloadTask", "temp file exists, try delete");
            if (!tempFile.delete()) {
                DefaultLogger.w("DownloadTask", "temp file delete failed, will overwrite");
            }
        }
        try {
            return new FileOutputStream(tempFile);
        } catch (FileNotFoundException e) {
            DefaultLogger.w("DownloadTask", e);
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static class TaskInfo {
        public long mContentLength;
        public MessageDigest mDigest;
        public long mDownloadSize;

        public TaskInfo() {
        }
    }
}
