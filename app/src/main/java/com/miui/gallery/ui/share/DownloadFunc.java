package com.miui.gallery.ui.share;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.error.BaseErrorCodeTranslator;
import com.miui.gallery.error.core.ErrorCode;
import com.miui.gallery.error.core.ErrorTip;
import com.miui.gallery.error.core.ErrorTranslateCallback;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.sdk.download.ImageDownloader;
import com.miui.gallery.sdk.download.assist.DownloadFailReason;
import com.miui.gallery.sdk.download.assist.DownloadedItem;
import com.miui.gallery.sdk.download.listener.DownloadListener;
import com.miui.gallery.sdk.download.listener.DownloadProgressListener;
import com.miui.gallery.ui.share.DownloadFunc;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public class DownloadFunc implements PrepareFunc<DownloadItem> {
    public final WeakReference<FragmentActivity> mRef;

    @Override // com.miui.gallery.ui.share.PrepareFunc
    public void release() {
    }

    @Override // com.miui.gallery.ui.share.PrepareFunc
    public /* bridge */ /* synthetic */ Uri prepare(DownloadItem downloadItem, PrepareProgressCallback<DownloadItem> prepareProgressCallback) {
        return prepare2(downloadItem, (PrepareProgressCallback) prepareProgressCallback);
    }

    public DownloadFunc(WeakReference<FragmentActivity> weakReference) {
        this.mRef = weakReference;
    }

    /* renamed from: prepare */
    public Uri prepare2(final DownloadItem downloadItem, final PrepareProgressCallback prepareProgressCallback) {
        DownloadedItem loadSync = ImageDownloader.getInstance().loadSync(downloadItem.getPreparedUriInLastStep(), downloadItem.getDownloadType(), null, prepareProgressCallback != null ? new DownloadProgressListener() { // from class: com.miui.gallery.ui.share.DownloadFunc.1
            {
                DownloadFunc.this = this;
            }

            @Override // com.miui.gallery.sdk.download.listener.DownloadProgressListener
            public void onDownloadProgress(Uri uri, DownloadType downloadType, long j, long j2) {
                prepareProgressCallback.onPreparing(downloadItem, (((float) j) * 1.0f) / ((float) j2));
            }
        } : null, new AnonymousClass2());
        if (loadSync == null || TextUtils.isEmpty(loadSync.getFilePath())) {
            return null;
        }
        return Uri.fromFile(new File(loadSync.getFilePath()));
    }

    /* renamed from: com.miui.gallery.ui.share.DownloadFunc$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements DownloadListener {
        public static /* synthetic */ void $r8$lambda$ZaDfOvkDXbSgbNcXplGct5d349s(AnonymousClass2 anonymousClass2, ErrorTip errorTip) {
            anonymousClass2.lambda$onDownloadFail$0(errorTip);
        }

        public AnonymousClass2() {
            DownloadFunc.this = r1;
        }

        @Override // com.miui.gallery.sdk.download.listener.DownloadListener
        public void onDownloadFail(Uri uri, DownloadType downloadType, DownloadFailReason downloadFailReason) {
            DefaultLogger.d("DownloadFunc", "onDownloadFail, uri=%s, type=%s, reason=%s", uri, downloadType, downloadFailReason);
            if (downloadFailReason == null) {
                return;
            }
            new BaseErrorCodeTranslator().translate((Context) DownloadFunc.this.mRef.get(), downloadFailReason.getCode(), downloadFailReason.getDesc(), new ErrorTranslateCallback() { // from class: com.miui.gallery.ui.share.DownloadFunc$2$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.error.core.ErrorTranslateCallback
                public final void onTranslate(ErrorTip errorTip) {
                    DownloadFunc.AnonymousClass2.$r8$lambda$ZaDfOvkDXbSgbNcXplGct5d349s(DownloadFunc.AnonymousClass2.this, errorTip);
                }
            });
        }

        public /* synthetic */ void lambda$onDownloadFail$0(ErrorTip errorTip) {
            if (errorTip.getCode() == ErrorCode.STORAGE_NO_WRITE_PERMISSION) {
                errorTip.action((Context) DownloadFunc.this.mRef.get(), null);
            }
        }
    }
}
