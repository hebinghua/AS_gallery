package com.miui.gallery.ui;

import android.content.Context;
import com.miui.gallery.cloud.download.BatchDownloadManager;
import com.miui.gallery.error.BaseErrorCodeTranslator;
import com.miui.gallery.error.BatchErrorStorageNoWritePermissionTip;
import com.miui.gallery.error.core.ErrorCode;
import com.miui.gallery.error.core.ErrorCodeTranslator;
import com.miui.gallery.error.core.ErrorTip;
import com.miui.gallery.error.core.ErrorTranslateCallback;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public class DownloadManager extends SyncDownloadBaseManager implements BatchDownloadManager.OnBatchDownloadListener {
    public static LinkedList<ErrorCode> sNeedShowError;
    public ErrorCode mCurError;
    public int mDownloadState;
    public ErrorHandler mErrorHandler;
    public ErrorTip mErrorTip;
    public DownloadStatusUpdateListener mStatusListener;
    public int mSuccessSize;
    public int mTotalSize;

    /* loaded from: classes2.dex */
    public interface DownloadStatusUpdateListener {
        void onDownloadStatusUpdate(int i, int i2, int i3, ErrorTip errorTip);
    }

    static {
        LinkedList<ErrorCode> linkedList = new LinkedList<>();
        sNeedShowError = linkedList;
        linkedList.add(ErrorCode.STORAGE_FULL);
        sNeedShowError.add(ErrorCode.STORAGE_NO_WRITE_PERMISSION);
        sNeedShowError.add(ErrorCode.PRIMARY_STORAGE_WRITE_ERROR);
        sNeedShowError.add(ErrorCode.SECONDARY_STORAGE_WRITE_ERROR);
        sNeedShowError.add(ErrorCode.STORAGE_LOW);
    }

    public DownloadManager(Context context, int i) {
        super(context, i);
        this.mDownloadState = 0;
        this.mCurError = ErrorCode.NO_ERROR;
    }

    public void setStatusListener(DownloadStatusUpdateListener downloadStatusUpdateListener) {
        this.mStatusListener = downloadStatusUpdateListener;
    }

    public void setCurError(ErrorCode errorCode) {
        this.mCurError = errorCode;
    }

    public void onResume() {
        if (BatchDownloadManager.canAutoDownload()) {
            BatchDownloadManager.getInstance().startBatchDownload(getContext(), false);
        }
        BatchDownloadManager.getInstance().registerBatchDownloadListener(this);
    }

    public void onPause() {
        BatchDownloadManager.getInstance().unregisterBatchDownloadListener(this);
    }

    public ErrorHandler getErrorHandler() {
        if (this.mErrorHandler == null) {
            this.mErrorHandler = new ErrorHandler(getContext());
        }
        return this.mErrorHandler;
    }

    @Override // com.miui.gallery.cloud.download.BatchDownloadManager.OnBatchDownloadListener
    public void onDownloadComplete(List<BatchDownloadManager.BatchItem> list, List<BatchDownloadManager.BatchItem> list2, ErrorCode errorCode, String str) {
        int i = 0;
        boolean z = (errorCode == null || errorCode == ErrorCode.NO_ERROR) ? false : true;
        this.mSuccessSize = list != null ? list.size() : 0;
        if (list2 != null) {
            i = list2.size();
        }
        this.mTotalSize = i;
        this.mErrorTip = null;
        if (z) {
            this.mDownloadState = 3;
            getErrorHandler().handleError(errorCode, str, new ErrorTranslateCallback() { // from class: com.miui.gallery.ui.DownloadManager.1
                @Override // com.miui.gallery.error.core.ErrorTranslateCallback
                public void onTranslate(ErrorTip errorTip) {
                    DownloadManager.this.mErrorTip = errorTip;
                    if (DownloadManager.this.mDownloadState != 3 || DownloadManager.this.mStatusListener == null) {
                        return;
                    }
                    DownloadManager.this.mStatusListener.onDownloadStatusUpdate(DownloadManager.this.mDownloadState, DownloadManager.this.mSuccessSize, DownloadManager.this.mTotalSize, DownloadManager.this.mErrorTip);
                }
            });
            return;
        }
        this.mDownloadState = 2;
        this.mCurError = ErrorCode.NO_ERROR;
        DownloadStatusUpdateListener downloadStatusUpdateListener = this.mStatusListener;
        if (downloadStatusUpdateListener == null) {
            return;
        }
        downloadStatusUpdateListener.onDownloadStatusUpdate(2, this.mSuccessSize, i, null);
    }

    @Override // com.miui.gallery.cloud.download.BatchDownloadManager.OnBatchDownloadListener
    public void onDownloadProgress(List<BatchDownloadManager.BatchItem> list, List<BatchDownloadManager.BatchItem> list2) {
        this.mDownloadState = 1;
        this.mCurError = ErrorCode.NO_ERROR;
        ErrorHandler errorHandler = this.mErrorHandler;
        if (errorHandler != null) {
            errorHandler.clearError();
        }
        int i = 0;
        this.mSuccessSize = list != null ? list.size() : 0;
        if (list2 != null) {
            i = list2.size();
        }
        this.mTotalSize = i;
        this.mErrorTip = null;
        DownloadStatusUpdateListener downloadStatusUpdateListener = this.mStatusListener;
        if (downloadStatusUpdateListener != null) {
            downloadStatusUpdateListener.onDownloadStatusUpdate(this.mDownloadState, this.mSuccessSize, i, null);
        }
    }

    @Override // com.miui.gallery.cloud.download.BatchDownloadManager.OnBatchDownloadListener
    public void onDownloadCancelled(List<BatchDownloadManager.BatchItem> list, List<BatchDownloadManager.BatchItem> list2) {
        this.mDownloadState = 0;
        this.mCurError = ErrorCode.NO_ERROR;
        this.mSuccessSize = 0;
        this.mTotalSize = 0;
        this.mErrorTip = null;
        DownloadStatusUpdateListener downloadStatusUpdateListener = this.mStatusListener;
        if (downloadStatusUpdateListener != null) {
            downloadStatusUpdateListener.onDownloadStatusUpdate(0, 0, 0, null);
        }
    }

    public int getDownloadState() {
        return this.mDownloadState;
    }

    public int getSuccessSize() {
        return this.mSuccessSize;
    }

    public int getTotalSize() {
        return this.mTotalSize;
    }

    public ErrorTip getErrorTip() {
        return this.mErrorTip;
    }

    public boolean needShow() {
        ErrorTip errorTip;
        int i = this.mDownloadState;
        if (i == 1) {
            return true;
        }
        return i == 3 && (errorTip = getErrorHandler().getErrorTip()) != null && sNeedShowError.contains(errorTip.getCode()) && this.mCurError != errorTip.getCode();
    }

    /* loaded from: classes2.dex */
    public static class ErrorHandler {
        public Context mContext;
        public final ErrorCodeTranslator mErrorCodeTranslator = new BaseErrorCodeTranslator() { // from class: com.miui.gallery.ui.DownloadManager.ErrorHandler.1
            @Override // com.miui.gallery.error.BaseErrorCodeTranslator
            public ErrorTip translateInternal(Context context, ErrorCode errorCode, String str) {
                if (AnonymousClass2.$SwitchMap$com$miui$gallery$error$core$ErrorCode[errorCode.ordinal()] == 1) {
                    return new BatchErrorStorageNoWritePermissionTip(ErrorCode.STORAGE_NO_WRITE_PERMISSION, str);
                }
                return super.translateInternal(context, errorCode, str);
            }
        };
        public ErrorTip mErrorTip;

        public ErrorHandler(Context context) {
            this.mContext = context;
        }

        public void handleError(ErrorCode errorCode, String str, final ErrorTranslateCallback errorTranslateCallback) {
            this.mErrorCodeTranslator.translate(this.mContext, errorCode, str, new ErrorTranslateCallback() { // from class: com.miui.gallery.ui.DownloadManager.ErrorHandler.2
                @Override // com.miui.gallery.error.core.ErrorTranslateCallback
                public void onTranslate(ErrorTip errorTip) {
                    ErrorHandler.this.mErrorTip = errorTip;
                    ErrorTranslateCallback errorTranslateCallback2 = errorTranslateCallback;
                    if (errorTranslateCallback2 != null) {
                        errorTranslateCallback2.onTranslate(errorTip);
                    }
                }
            });
        }

        public ErrorTip getErrorTip() {
            return this.mErrorTip;
        }

        public void clearError() {
            this.mErrorTip = null;
        }
    }

    /* renamed from: com.miui.gallery.ui.DownloadManager$2  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass2 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$error$core$ErrorCode;

        static {
            int[] iArr = new int[ErrorCode.values().length];
            $SwitchMap$com$miui$gallery$error$core$ErrorCode = iArr;
            try {
                iArr[ErrorCode.STORAGE_NO_WRITE_PERMISSION.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }
}
