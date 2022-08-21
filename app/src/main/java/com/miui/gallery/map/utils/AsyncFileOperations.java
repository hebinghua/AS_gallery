package com.miui.gallery.map.utils;

import android.content.Context;
import android.os.AsyncTask;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public class AsyncFileOperations extends AsyncTask<Void, Void, String> {
    public FileOperationCallback mCallback;
    public final WeakReference<Context> mContext;
    public final String mCustomStyleFileName;

    /* loaded from: classes2.dex */
    public interface FileOperationCallback {
        void onFinished(String str);
    }

    public AsyncFileOperations(Context context, String str, FileOperationCallback fileOperationCallback) {
        this.mContext = new WeakReference<>(context);
        this.mCustomStyleFileName = str;
        this.mCallback = fileOperationCallback;
    }

    public void cancelLoad() {
        this.mCallback = null;
    }

    /* JADX WARN: Removed duplicated region for block: B:48:0x00a7 A[Catch: IOException -> 0x00a3, TRY_LEAVE, TryCatch #1 {IOException -> 0x00a3, blocks: (B:44:0x009f, B:48:0x00a7), top: B:54:0x009f }] */
    /* JADX WARN: Removed duplicated region for block: B:54:0x009f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // android.os.AsyncTask
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String doInBackground(java.lang.Void... r9) {
        /*
            r8 = this;
            java.lang.String r9 = "Close stream failed: %s"
            java.lang.String r0 = "AsyncFileOperations"
            java.lang.ref.WeakReference<android.content.Context> r1 = r8.mContext
            java.lang.Object r1 = r1.get()
            r2 = 0
            if (r1 != 0) goto Le
            return r2
        Le:
            java.lang.ref.WeakReference<android.content.Context> r1 = r8.mContext
            java.lang.Object r1 = r1.get()
            android.content.Context r1 = (android.content.Context) r1
            java.io.File r1 = r1.getFilesDir()
            java.lang.String r1 = r1.getAbsolutePath()
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r1)
            java.lang.String r1 = "/"
            r3.append(r1)
            java.lang.String r1 = r8.mCustomStyleFileName
            r3.append(r1)
            java.lang.String r1 = r3.toString()
            java.io.File r3 = new java.io.File
            r3.<init>(r1)
            boolean r4 = r3.exists()
            if (r4 == 0) goto L40
            return r1
        L40:
            java.lang.ref.WeakReference<android.content.Context> r4 = r8.mContext     // Catch: java.lang.Throwable -> L7e java.io.IOException -> L81
            java.lang.Object r4 = r4.get()     // Catch: java.lang.Throwable -> L7e java.io.IOException -> L81
            android.content.Context r4 = (android.content.Context) r4     // Catch: java.lang.Throwable -> L7e java.io.IOException -> L81
            android.content.res.AssetManager r4 = r4.getAssets()     // Catch: java.lang.Throwable -> L7e java.io.IOException -> L81
            java.lang.String r5 = r8.mCustomStyleFileName     // Catch: java.lang.Throwable -> L7e java.io.IOException -> L81
            java.io.InputStream r4 = r4.open(r5)     // Catch: java.lang.Throwable -> L7e java.io.IOException -> L81
            java.io.FileOutputStream r5 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> L78 java.io.IOException -> L7b
            r5.<init>(r3)     // Catch: java.lang.Throwable -> L78 java.io.IOException -> L7b
            r3 = 4096(0x1000, float:5.74E-42)
            byte[] r3 = new byte[r3]     // Catch: java.io.IOException -> L76 java.lang.Throwable -> L9b
        L5b:
            r6 = -1
            int r7 = r4.read(r3)     // Catch: java.io.IOException -> L76 java.lang.Throwable -> L9b
            if (r6 == r7) goto L67
            r6 = 0
            r5.write(r3, r6, r7)     // Catch: java.io.IOException -> L76 java.lang.Throwable -> L9b
            goto L5b
        L67:
            r5.flush()     // Catch: java.io.IOException -> L76 java.lang.Throwable -> L9b
            r4.close()     // Catch: java.io.IOException -> L71
            r5.close()     // Catch: java.io.IOException -> L71
            goto L75
        L71:
            r2 = move-exception
            com.miui.gallery.util.logger.DefaultLogger.e(r0, r9, r2)
        L75:
            return r1
        L76:
            r1 = move-exception
            goto L84
        L78:
            r1 = move-exception
            r5 = r2
            goto L9c
        L7b:
            r1 = move-exception
            r5 = r2
            goto L84
        L7e:
            r1 = move-exception
            r5 = r2
            goto L9d
        L81:
            r1 = move-exception
            r4 = r2
            r5 = r4
        L84:
            java.lang.String r3 = "Copy custom style file failed: %s"
            com.miui.gallery.util.logger.DefaultLogger.e(r0, r3, r1)     // Catch: java.lang.Throwable -> L9b
            if (r4 == 0) goto L91
            r4.close()     // Catch: java.io.IOException -> L8f
            goto L91
        L8f:
            r1 = move-exception
            goto L97
        L91:
            if (r5 == 0) goto L9a
            r5.close()     // Catch: java.io.IOException -> L8f
            goto L9a
        L97:
            com.miui.gallery.util.logger.DefaultLogger.e(r0, r9, r1)
        L9a:
            return r2
        L9b:
            r1 = move-exception
        L9c:
            r2 = r4
        L9d:
            if (r2 == 0) goto La5
            r2.close()     // Catch: java.io.IOException -> La3
            goto La5
        La3:
            r2 = move-exception
            goto Lab
        La5:
            if (r5 == 0) goto Lae
            r5.close()     // Catch: java.io.IOException -> La3
            goto Lae
        Lab:
            com.miui.gallery.util.logger.DefaultLogger.e(r0, r9, r2)
        Lae:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.map.utils.AsyncFileOperations.doInBackground(java.lang.Void[]):java.lang.String");
    }

    @Override // android.os.AsyncTask
    public void onPostExecute(String str) {
        FileOperationCallback fileOperationCallback;
        super.onPostExecute((AsyncFileOperations) str);
        if (isCancelled() || (fileOperationCallback = this.mCallback) == null) {
            return;
        }
        fileOperationCallback.onFinished(str);
    }
}
