package com.miui.gallery.editor.photo.core.imports.text.typeface;

import android.content.Context;
import android.os.AsyncTask;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.imports.net.FontResDownloadManager;
import com.miui.gallery.net.download.Request;
import com.miui.gallery.ui.NetworkConsider;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class FontDownloadManager {
    public FontResDownloadManager mFontResDownloadManager;
    public UnzipFontTask mUnzipFontTask;

    /* renamed from: $r8$lambda$-mXYENgqlrTWo_eDCLWTnD7DwTI */
    public static /* synthetic */ void m903$r8$lambda$mXYENgqlrTWo_eDCLWTnD7DwTI(FontDownloadManager fontDownloadManager, TextStyle textStyle, DownloadCallback downloadCallback, boolean z, boolean z2) {
        fontDownloadManager.lambda$downloadFontResource$0(textStyle, downloadCallback, z, z2);
    }

    public void downloadFontResource(Context context, final TextStyle textStyle, final DownloadCallback downloadCallback) {
        if (!BaseNetworkUtils.isNetworkConnected()) {
            ToastUtils.makeText(context, (int) R.string.photo_font_download_failed_for_notwork);
            DefaultLogger.d("FontDownloadManager", "download resource no network");
        } else if (BaseNetworkUtils.isActiveNetworkMetered()) {
            NetworkConsider.consider(context, new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.editor.photo.core.imports.text.typeface.FontDownloadManager$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
                public final void onConfirmed(boolean z, boolean z2) {
                    FontDownloadManager.m903$r8$lambda$mXYENgqlrTWo_eDCLWTnD7DwTI(FontDownloadManager.this, textStyle, downloadCallback, z, z2);
                }
            });
        } else {
            downloadResource(textStyle, downloadCallback, false);
        }
    }

    public /* synthetic */ void lambda$downloadFontResource$0(TextStyle textStyle, DownloadCallback downloadCallback, boolean z, boolean z2) {
        if (z) {
            downloadResource(textStyle, downloadCallback, true);
        }
    }

    public final void downloadResource(final TextStyle textStyle, final DownloadCallback downloadCallback, boolean z) {
        if (this.mFontResDownloadManager == null) {
            this.mFontResDownloadManager = new FontResDownloadManager();
        }
        if (downloadCallback != null) {
            DefaultLogger.d("FontDownloadManager", "font is ready to download.");
            downloadCallback.onStart();
        }
        this.mFontResDownloadManager.download(textStyle, textStyle.getDownloadFilePath(), new Request.Listener() { // from class: com.miui.gallery.editor.photo.core.imports.text.typeface.FontDownloadManager.1
            {
                FontDownloadManager.this = this;
            }

            @Override // com.miui.gallery.net.download.Request.Listener
            public void onStart() {
                DefaultLogger.d("FontDownloadManager", "font start download.");
            }

            @Override // com.miui.gallery.net.download.Request.Listener
            public void onProgressUpdate(int i) {
                DefaultLogger.d("FontDownloadManager", "download progress :%d", Integer.valueOf(i));
            }

            @Override // com.miui.gallery.net.download.Request.Listener
            public void onComplete(int i) {
                DefaultLogger.d("FontDownloadManager", "download %s, resultCode: %d", textStyle.getDownloadFilePath(), Integer.valueOf(i));
                if (i == 0) {
                    FontDownloadManager.this.unZipDownloadFile(textStyle, downloadCallback);
                } else {
                    ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.text.typeface.FontDownloadManager.1.1
                        {
                            AnonymousClass1.this = this;
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            DownloadCallback downloadCallback2 = downloadCallback;
                            if (downloadCallback2 != null) {
                                downloadCallback2.onCompleted(false);
                            }
                        }
                    });
                }
            }
        }, z);
    }

    public final void unZipDownloadFile(TextStyle textStyle, DownloadCallback downloadCallback) {
        UnzipFontTask unzipFontTask = new UnzipFontTask(textStyle, downloadCallback);
        this.mUnzipFontTask = unzipFontTask;
        unzipFontTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public void cancelAll() {
        FontResDownloadManager fontResDownloadManager = this.mFontResDownloadManager;
        if (fontResDownloadManager != null) {
            fontResDownloadManager.cancelAll();
            this.mFontResDownloadManager = null;
        }
        UnzipFontTask unzipFontTask = this.mUnzipFontTask;
        if (unzipFontTask != null) {
            unzipFontTask.release();
            this.mUnzipFontTask = null;
        }
    }
}
