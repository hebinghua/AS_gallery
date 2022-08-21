package cn.kuaipan.android.kss.upload;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import cn.kuaipan.android.exception.ErrorHelper;
import cn.kuaipan.android.exception.KscException;
import cn.kuaipan.android.exception.KscRuntimeException;
import cn.kuaipan.android.exception.ServerException;
import cn.kuaipan.android.http.IKscDecoder;
import cn.kuaipan.android.http.IKscTransferListener;
import cn.kuaipan.android.http.KscHttpRequest;
import cn.kuaipan.android.http.KscHttpResponse;
import cn.kuaipan.android.http.KscHttpTransmitter;
import cn.kuaipan.android.http.KssTransferStopper;
import cn.kuaipan.android.kss.FileTranceListener;
import cn.kuaipan.android.kss.IKssUploadRequestResult;
import cn.kuaipan.android.kss.KssDef;
import cn.kuaipan.android.kss.RC4Encoder;
import cn.kuaipan.android.kss.upload.UploadFileInfo;
import cn.kuaipan.android.utils.ApiDataHelper;
import cn.kuaipan.android.utils.Encode;
import cn.kuaipan.android.utils.IObtainable;
import cn.kuaipan.android.utils.RandomInputStream;
import com.nexstreaming.nexeditorsdk.nexEngine;
import com.xiaomi.micloudsdk.stat.MiCloudStatManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.zip.CRC32;
import org.apache.http.HttpEntity;

/* loaded from: classes.dex */
public class KssUploader implements KssDef {
    public static volatile boolean sBreakForUT;
    public final CRC32 CRC32 = new CRC32();
    public final byte[] CRC_BUF = new byte[8192];
    public long mChunkSize = 65536;
    public final UploadTaskStore mTaskStore;
    public final KscHttpTransmitter mTransmitter;

    public KssUploader(KscHttpTransmitter kscHttpTransmitter, UploadTaskStore uploadTaskStore) {
        this.mTaskStore = uploadTaskStore;
        this.mTransmitter = kscHttpTransmitter;
    }

    public void upload(KssUploadFile kssUploadFile, IKscTransferListener iKscTransferListener, KssTransferStopper kssTransferStopper, int i, KssUploadInfo kssUploadInfo) throws KscException, InterruptedException {
        FileTranceListener fileTranceListener;
        if (iKscTransferListener != null) {
            fileTranceListener = new FileTranceListener(iKscTransferListener, true);
            iKscTransferListener.setSendTotal(kssUploadFile.fileSize);
        } else {
            fileTranceListener = null;
        }
        int i2 = 0;
        while (!Thread.interrupted()) {
            Log.d("KssUploader", "upload blockIndex: " + i2);
            uploadBlock(i, kssUploadFile, fileTranceListener, kssTransferStopper, kssUploadInfo, true, i2);
            i2++;
            if (kssUploadInfo.isCompleted()) {
                return;
            }
        }
        throw new InterruptedException();
    }

    public final void uploadBlock(int i, KssUploadFile kssUploadFile, FileTranceListener fileTranceListener, KssTransferStopper kssTransferStopper, KssUploadInfo kssUploadInfo, boolean z, int i2) throws KscException, InterruptedException {
        if (kssUploadInfo == null) {
            throw new IllegalArgumentException("The KssUploadInfo can not be empty.");
        }
        verifyBlock(kssUploadFile, kssUploadInfo.getFileInfo(), i2);
        IKssUploadRequestResult.Block block = kssUploadInfo.getRequestResult().getBlock(i2);
        if (block == null) {
            throw new KscRuntimeException(500008, "Block should not be null");
        }
        if (!block.isComplete()) {
            uploadBlock(i, kssUploadFile, fileTranceListener, kssTransferStopper, kssUploadInfo, i2);
        } else if (fileTranceListener == null) {
        } else {
            fileTranceListener.setSendPos(Math.min((i2 + 1) * 4194304, kssUploadFile.fileSize));
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:68:0x01af, code lost:
        r9 = r8;
        r2 = r26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x01b6, code lost:
        if (r2.isComplete() == false) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x01b8, code lost:
        r2 = r31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x01bf, code lost:
        if (r2.needBlockRetry() == false) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x01c5, code lost:
        if (r29.decrementAndGet() <= 0) goto L76;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x01ed, code lost:
        r4 = new cn.kuaipan.android.exception.ServerMsgException(200, r2.stat);
        android.util.Log.w(r9, r1, r4);
        r38.markBroken();
        deleteUploadInfo(r34);
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x01ff, code lost:
        throw r4;
     */
    /* JADX WARN: Removed duplicated region for block: B:102:0x020e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:124:0x020a A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:130:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0075  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void uploadBlock(int r34, cn.kuaipan.android.kss.upload.KssUploadFile r35, cn.kuaipan.android.kss.FileTranceListener r36, cn.kuaipan.android.http.KssTransferStopper r37, cn.kuaipan.android.kss.upload.KssUploadInfo r38, int r39) throws cn.kuaipan.android.exception.KscException, java.lang.InterruptedException {
        /*
            Method dump skipped, instructions count: 553
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.kuaipan.android.kss.upload.KssUploader.uploadBlock(int, cn.kuaipan.android.kss.upload.KssUploadFile, cn.kuaipan.android.kss.FileTranceListener, cn.kuaipan.android.http.KssTransferStopper, cn.kuaipan.android.kss.upload.KssUploadInfo, int):void");
    }

    public final UploadChunkInfo uploadChunk(RandomInputStream randomInputStream, RC4Encoder rC4Encoder, IKscTransferListener iKscTransferListener, KssTransferStopper kssTransferStopper, KssUploadInfo kssUploadInfo, int i, UploadChunkInfo uploadChunkInfo) throws KscException, InterruptedException {
        IKssUploadRequestResult requestResult = kssUploadInfo.getRequestResult();
        String[] nodeUrls = requestResult.getNodeUrls();
        if (nodeUrls == null || nodeUrls.length <= 0) {
            throw new IllegalArgumentException("No available urls.");
        }
        UploadChunkInfo uploadChunkInfo2 = null;
        for (int i2 = 0; i2 < nodeUrls.length; i2++) {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            try {
                randomInputStream.moveToPos((i * 4194304) + uploadChunkInfo.next_pos);
                randomInputStream.mark(nexEngine.ExportHEVCMainTierLevel61);
                Uri.Builder buildUpon = Uri.parse(nodeUrls[i2] + "/upload_block_chunk").buildUpon();
                buildUpon.appendQueryParameter("chunk_pos", String.valueOf(uploadChunkInfo.next_pos));
                if (!TextUtils.isEmpty(uploadChunkInfo.upload_id)) {
                    buildUpon.appendQueryParameter("upload_id", uploadChunkInfo.upload_id);
                } else {
                    buildUpon.appendQueryParameter("file_meta", requestResult.getFileMeta());
                    buildUpon.appendQueryParameter("block_meta", requestResult.getBlock(i).meta);
                }
                UploadChunkInfo _uploadChunk = _uploadChunk(buildUpon.build(), uploadChunkInfo.next_pos, randomInputStream, rC4Encoder, iKscTransferListener, kssTransferStopper, kssUploadInfo);
                try {
                } catch (Exception e) {
                    e = e;
                }
                try {
                    kssUploadInfo.mExpectInfo = _uploadChunk.expect_info;
                    return _uploadChunk;
                } catch (Exception e2) {
                    e = e2;
                    uploadChunkInfo2 = _uploadChunk;
                    ErrorHelper.handleInterruptException(e);
                    if (ErrorHelper.isStopByCallerException(e) || i2 >= nodeUrls.length - 1) {
                        throw KscException.newException(e, "Failed when upload a kss chunk.");
                    }
                }
            } catch (Exception e3) {
                e = e3;
            }
        }
        return uploadChunkInfo2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:53:0x0158, code lost:
        return r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final cn.kuaipan.android.kss.upload.UploadChunkInfo _uploadChunk(android.net.Uri r20, long r21, cn.kuaipan.android.utils.RandomInputStream r23, cn.kuaipan.android.kss.RC4Encoder r24, cn.kuaipan.android.http.IKscTransferListener r25, cn.kuaipan.android.http.KssTransferStopper r26, cn.kuaipan.android.kss.upload.KssUploadInfo r27) throws cn.kuaipan.android.exception.KscException, java.lang.InterruptedException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 345
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.kuaipan.android.kss.upload.KssUploader._uploadChunk(android.net.Uri, long, cn.kuaipan.android.utils.RandomInputStream, cn.kuaipan.android.kss.RC4Encoder, cn.kuaipan.android.http.IKscTransferListener, cn.kuaipan.android.http.KssTransferStopper, cn.kuaipan.android.kss.upload.KssUploadInfo):cn.kuaipan.android.kss.upload.UploadChunkInfo");
    }

    public final UploadChunkInfo doUpload(Uri uri, InputStream inputStream, long j, IKscTransferListener iKscTransferListener, KssTransferStopper kssTransferStopper) throws KscException, InterruptedException {
        HttpEntity entity;
        Object obj = null;
        try {
            KscHttpRequest kscHttpRequest = new KscHttpRequest(KscHttpRequest.HttpMethod.POST, uri, (IKscDecoder) null, iKscTransferListener);
            kscHttpRequest.setPostEntity(new KssInputStreamEntity(inputStream, j));
            long currentTimeMillis = System.currentTimeMillis();
            KscHttpResponse execute = this.mTransmitter.execute(kscHttpRequest, 4, kssTransferStopper);
            long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
            long j2 = 0;
            if (execute.getResponse() != null && (entity = execute.getResponse().getEntity()) != null) {
                j2 = entity.getContentLength();
            }
            long j3 = j2;
            int statusCode = execute.getStatusCode();
            String str = "";
            if (execute.getError() != null) {
                str = execute.getError().getClass().getSimpleName();
            }
            MiCloudStatManager.getInstance().addHttpEvent(uri.toString(), currentTimeMillis2, j3, statusCode, str);
            ErrorHelper.throwError(execute);
            if (statusCode != 200) {
                ServerException serverException = new ServerException(statusCode, execute.dump());
                Log.w("KssUploader", "Exception in doUpload", serverException);
                throw serverException;
            }
            Map<String, Object> contentToMap = ApiDataHelper.contentToMap(execute);
            UploadChunkInfo uploadChunkInfo = new UploadChunkInfo(contentToMap);
            uploadChunkInfo.expect_info = ServerExpect.getServerExpect(execute);
            if (contentToMap != null && (contentToMap instanceof IObtainable)) {
                ((IObtainable) contentToMap).recycle();
            }
            return uploadChunkInfo;
        } catch (Throwable th) {
            if (0 != 0 && (obj instanceof IObtainable)) {
                null.recycle();
            }
            throw th;
        }
    }

    public final void updateUploadInfo(int i, KssUploadInfo kssUploadInfo, UploadChunkInfoPersist uploadChunkInfoPersist) {
        UploadTaskStore uploadTaskStore = this.mTaskStore;
        if (uploadTaskStore == null) {
            return;
        }
        uploadTaskStore.updateUploadInfo(i, kssUploadInfo, uploadChunkInfoPersist);
    }

    public final void deleteUploadInfo(int i) throws InterruptedException {
        UploadTaskStore uploadTaskStore = this.mTaskStore;
        if (uploadTaskStore == null) {
            return;
        }
        uploadTaskStore.removeUploadInfo(i);
    }

    public final UploadChunkInfoPersist getUploadPos(int i) throws InterruptedException {
        UploadTaskStore uploadTaskStore = this.mTaskStore;
        if (uploadTaskStore == null) {
            return null;
        }
        return uploadTaskStore.getUploadPos(i);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v4 */
    /* JADX WARN: Type inference failed for: r0v5, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r0v6 */
    public final void verifyBlock(KssUploadFile kssUploadFile, UploadFileInfo uploadFileInfo, int i) throws KscException, InterruptedException {
        UploadFileInfo.BlockInfo blockInfo = uploadFileInfo.getBlockInfo(i);
        long j = i * 4194304;
        int min = (int) Math.min(kssUploadFile.fileSize - j, 4194304L);
        if (min != blockInfo.size) {
            throw new KscException(403002, "Block size has changed.");
        }
        ?? r0 = 0;
        try {
            try {
                InputStream inputStream = kssUploadFile.getInputStream();
                try {
                    if (inputStream.skip(j) != j) {
                        throw new KscException(403002, "File size has changed.");
                    }
                    if (!TextUtils.equals(Encode.SHA1Encode(inputStream, min), blockInfo.sha1)) {
                        throw new KscException(403002, "Block has changed.");
                    }
                    try {
                        inputStream.close();
                    } catch (Throwable unused) {
                    }
                } catch (IOException e) {
                    e = e;
                    throw KscException.newException(e, null);
                }
            } catch (Throwable th) {
                th = th;
                r0 = kssUploadFile;
                try {
                    r0.close();
                } catch (Throwable unused2) {
                }
                throw th;
            }
        } catch (IOException e2) {
            e = e2;
        } catch (Throwable th2) {
            th = th2;
            r0.close();
            throw th;
        }
    }

    public static void updatePos(UploadChunkInfo uploadChunkInfo, long j, long j2, long j3) {
        if (uploadChunkInfo == null) {
            return;
        }
        if (uploadChunkInfo.isComplete()) {
            uploadChunkInfo.next_pos = j3;
            uploadChunkInfo.left_bytes = 0L;
        } else if (uploadChunkInfo.isContinue()) {
            long j4 = j + j2;
            long j5 = j3 - j4;
            if (uploadChunkInfo.next_pos == j4 && uploadChunkInfo.left_bytes == j5) {
                return;
            }
            Log.w("KssUploader", "Chunk pos is (" + uploadChunkInfo.next_pos + ", " + uploadChunkInfo.left_bytes + "), but in process is (" + j4 + ", " + j5 + ")");
            uploadChunkInfo.next_pos = j4;
            uploadChunkInfo.left_bytes = j5;
        } else {
            uploadChunkInfo.next_pos = j;
            uploadChunkInfo.left_bytes = j3 - j;
        }
    }

    public final synchronized int getCRC(InputStream inputStream, long j) throws IOException {
        this.CRC32.reset();
        while (j > 0) {
            byte[] bArr = this.CRC_BUF;
            int read = inputStream.read(bArr, 0, (int) Math.min(bArr.length, j));
            if (read < 0) {
                break;
            }
            j -= read;
            this.CRC32.update(this.CRC_BUF, 0, read);
        }
        return (int) this.CRC32.getValue();
    }
}
