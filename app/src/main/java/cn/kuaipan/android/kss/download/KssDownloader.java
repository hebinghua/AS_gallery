package cn.kuaipan.android.kss.download;

import cn.kuaipan.android.exception.KscRuntimeException;
import cn.kuaipan.android.http.KscHttpResponse;
import cn.kuaipan.android.http.KscHttpTransmitter;
import cn.kuaipan.android.kss.KssDef;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes.dex */
public class KssDownloader implements KssDef {
    public final KscHttpTransmitter mTransmitter;

    public KssDownloader(KscHttpTransmitter kscHttpTransmitter) {
        this.mTransmitter = kscHttpTransmitter;
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0054 A[Catch: IOException -> 0x00be, all -> 0x00c6, TRY_LEAVE, TryCatch #7 {IOException -> 0x00be, blocks: (B:15:0x0043, B:17:0x0054), top: B:75:0x0043, outer: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0063 A[Catch: all -> 0x00c6, TRY_LEAVE, TryCatch #3 {all -> 0x00c6, blocks: (B:5:0x0019, B:7:0x001f, B:9:0x0030, B:12:0x0038, B:14:0x003e, B:15:0x0043, B:17:0x0054, B:18:0x0057, B:19:0x005d, B:21:0x0063, B:24:0x006f, B:27:0x0077, B:29:0x007d, B:31:0x0083, B:32:0x0089, B:34:0x008b, B:35:0x0091, B:36:0x0092, B:37:0x0097, B:38:0x0098, B:40:0x00a2, B:48:0x00bf, B:49:0x00c5), top: B:68:0x0019, inners: #7, #8 }] */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0098 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void download(cn.kuaipan.android.kss.download.KssDownloadFile r11, boolean r12, cn.kuaipan.android.http.IKscTransferListener r13, cn.kuaipan.android.http.KssTransferStopper r14, cn.kuaipan.android.kss.IKssDownloadRequestResult r15) throws cn.kuaipan.android.exception.KscException, java.lang.InterruptedException {
        /*
            Method dump skipped, instructions count: 235
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.kuaipan.android.kss.download.KssDownloader.download(cn.kuaipan.android.kss.download.KssDownloadFile, boolean, cn.kuaipan.android.http.IKscTransferListener, cn.kuaipan.android.http.KssTransferStopper, cn.kuaipan.android.kss.IKssDownloadRequestResult):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:105:0x01df  */
    /* JADX WARN: Removed duplicated region for block: B:106:0x01e7  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x00f0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:145:0x01cb A[ADDED_TO_REGION, EDGE_INSN: B:145:0x01cb->B:96:0x01cb ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00ca A[Catch: all -> 0x00ae, Exception -> 0x00b2, TRY_ENTER, TRY_LEAVE, TryCatch #14 {Exception -> 0x00b2, all -> 0x00ae, blocks: (B:30:0x009d, B:32:0x00a7, B:42:0x00ca), top: B:138:0x009d }] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00d5  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0123 A[Catch: Exception -> 0x015f, all -> 0x0163, TryCatch #1 {all -> 0x0163, blocks: (B:47:0x00eb, B:49:0x00f0, B:51:0x0107, B:53:0x0113, B:57:0x0149, B:59:0x014e, B:56:0x0123), top: B:120:0x00eb }] */
    /* JADX WARN: Removed duplicated region for block: B:88:0x01ab A[Catch: all -> 0x01d9, TRY_LEAVE, TryCatch #15 {all -> 0x01d9, blocks: (B:86:0x01a2, B:88:0x01ab, B:99:0x01d4, B:100:0x01d8, B:98:0x01d0), top: B:130:0x01a2 }] */
    /* JADX WARN: Removed duplicated region for block: B:97:0x01cd  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x01d0 A[Catch: all -> 0x01d9, TryCatch #15 {all -> 0x01d9, blocks: (B:86:0x01a2, B:88:0x01ab, B:99:0x01d4, B:100:0x01d8, B:98:0x01d0), top: B:130:0x01a2 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void download(cn.kuaipan.android.kss.IKssDownloadRequestResult r33, cn.kuaipan.android.kss.download.KssAccessor r34, cn.kuaipan.android.kss.download.LoadMap r35, cn.kuaipan.android.http.KssTransferStopper r36, java.util.concurrent.atomic.AtomicInteger r37) throws java.lang.InterruptedException, java.security.InvalidKeyException, cn.kuaipan.android.exception.KscException {
        /*
            Method dump skipped, instructions count: 523
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.kuaipan.android.kss.download.KssDownloader.download(cn.kuaipan.android.kss.IKssDownloadRequestResult, cn.kuaipan.android.kss.download.KssAccessor, cn.kuaipan.android.kss.download.LoadMap, cn.kuaipan.android.http.KssTransferStopper, java.util.concurrent.atomic.AtomicInteger):void");
    }

    public final void save(KscHttpResponse kscHttpResponse, KssAccessor kssAccessor, LoadRecorder loadRecorder, AtomicInteger atomicInteger) throws IllegalStateException, IOException {
        boolean z = false;
        try {
            InputStream content = kscHttpResponse.getStatusCode() == 200 ? kscHttpResponse.getContent() : null;
            if (content == null) {
                throw new KscRuntimeException(500008, "Not meet exception, but no response.\n" + kscHttpResponse.dump());
            }
            byte[] bArr = new byte[8192];
            boolean z2 = false;
            while (true) {
                try {
                    int read = content.read(bArr);
                    if (read < 0) {
                        break;
                    }
                    z2 = true;
                    if (read > 0 && kssAccessor.write(bArr, 0, read, loadRecorder) < read) {
                        break;
                    }
                } catch (Throwable th) {
                    th = th;
                    z = z2;
                    if (z) {
                        atomicInteger.set(3);
                    }
                    throw th;
                }
            }
            if (!z2) {
                return;
            }
            atomicInteger.set(3);
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public final void releaseResponse(KscHttpResponse kscHttpResponse) throws InterruptedException {
        InterruptedException isInterrupted;
        if (kscHttpResponse != null) {
            try {
                kscHttpResponse.release();
            } finally {
                if (isInterrupted == null) {
                }
            }
        }
    }
}
