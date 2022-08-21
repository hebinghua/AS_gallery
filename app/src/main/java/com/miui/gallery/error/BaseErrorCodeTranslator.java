package com.miui.gallery.error;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;
import com.miui.gallery.error.core.ErrorCode;
import com.miui.gallery.error.core.ErrorCodeTranslator;
import com.miui.gallery.error.core.ErrorTip;
import com.miui.gallery.error.core.ErrorTranslateCallback;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class BaseErrorCodeTranslator implements ErrorCodeTranslator {
    public AsyncTask<Pair<ErrorCode, String>, Void, ErrorTip> mTask;

    /* renamed from: com.miui.gallery.error.BaseErrorCodeTranslator$2  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass2 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$error$core$ErrorCode;

        static {
            int[] iArr = new int[ErrorCode.values().length];
            $SwitchMap$com$miui$gallery$error$core$ErrorCode = iArr;
            try {
                iArr[ErrorCode.NO_CTA_PERMISSION.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$error$core$ErrorCode[ErrorCode.NO_NETWORK.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$error$core$ErrorCode[ErrorCode.NO_WIFI_CONNECTED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$error$core$ErrorCode[ErrorCode.SYNC_OFF.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$error$core$ErrorCode[ErrorCode.STORAGE_FULL.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$miui$gallery$error$core$ErrorCode[ErrorCode.STORAGE_LOW.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$miui$gallery$error$core$ErrorCode[ErrorCode.STORAGE_NO_WRITE_PERMISSION.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$miui$gallery$error$core$ErrorCode[ErrorCode.PRIMARY_STORAGE_WRITE_ERROR.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$miui$gallery$error$core$ErrorCode[ErrorCode.SECONDARY_STORAGE_WRITE_ERROR.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$miui$gallery$error$core$ErrorCode[ErrorCode.DECODE_ERROR.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$miui$gallery$error$core$ErrorCode[ErrorCode.NETWORK_RESTRICT.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$miui$gallery$error$core$ErrorCode[ErrorCode.NO_ACCOUNT.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$com$miui$gallery$error$core$ErrorCode[ErrorCode.CONNECT_TIMEOUT.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$com$miui$gallery$error$core$ErrorCode[ErrorCode.SOCKET_TIMEOUT.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$com$miui$gallery$error$core$ErrorCode[ErrorCode.OVER_QUOTA.ordinal()] = 15;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                $SwitchMap$com$miui$gallery$error$core$ErrorCode[ErrorCode.THUMBNAIL_BUILD_ERROR.ordinal()] = 16;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                $SwitchMap$com$miui$gallery$error$core$ErrorCode[ErrorCode.NOT_SYNCED.ordinal()] = 17;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                $SwitchMap$com$miui$gallery$error$core$ErrorCode[ErrorCode.WRITE_EXIF_ERROR.ordinal()] = 18;
            } catch (NoSuchFieldError unused18) {
            }
            try {
                $SwitchMap$com$miui$gallery$error$core$ErrorCode[ErrorCode.SERVER_INVALID.ordinal()] = 19;
            } catch (NoSuchFieldError unused19) {
            }
        }
    }

    public ErrorTip translateInternal(Context context, ErrorCode errorCode, String str) {
        List<String> mountedVolumePaths;
        switch (AnonymousClass2.$SwitchMap$com$miui$gallery$error$core$ErrorCode[errorCode.ordinal()]) {
            case 1:
                return new ErrorNoCTAPermissionTip(ErrorCode.NO_CTA_PERMISSION);
            case 2:
                return new ErrorNoNetworkTip(ErrorCode.NO_NETWORK);
            case 3:
                return new ErrorNoWifiTip(ErrorCode.NO_WIFI_CONNECTED);
            case 4:
                return new ErrorSyncOffTip(ErrorCode.SYNC_OFF);
            case 5:
                boolean z = false;
                if (StorageUtils.hasExternalSDCard(context) && (mountedVolumePaths = StorageUtils.getMountedVolumePaths(context)) != null && mountedVolumePaths.size() > 0) {
                    Iterator<String> it = mountedVolumePaths.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            if (StorageUtils.getAvailableBytes(it.next()) > 104857600) {
                                z = true;
                            }
                        }
                    }
                }
                return z ? new ErrorChangeStorageTip(ErrorCode.STORAGE_FULL) : new ErrorStorageFullTip(ErrorCode.STORAGE_FULL);
            case 6:
                return new ErrorStorageFullTip(ErrorCode.STORAGE_LOW);
            case 7:
                return new ErrorStorageNoWritePermissionTip(ErrorCode.STORAGE_NO_WRITE_PERMISSION, str);
            case 8:
                return new ErrorPrimaryStorageWriteTip(ErrorCode.PRIMARY_STORAGE_WRITE_ERROR);
            case 9:
                return new ErrorSecondaryStorageWriteTip(ErrorCode.SECONDARY_STORAGE_WRITE_ERROR);
            case 10:
                return new ErrorDecodeTip(ErrorCode.DECODE_ERROR);
            case 11:
                return new ErrorNetworkRestrictTip(ErrorCode.NETWORK_RESTRICT);
            case 12:
                return new ErrorNoAccountTip(ErrorCode.NO_ACCOUNT);
            case 13:
                return new ErrorConnectTimeoutTip(ErrorCode.CONNECT_TIMEOUT);
            case 14:
                return new ErrorSocketTimeoutTip(ErrorCode.SOCKET_TIMEOUT);
            case 15:
                return new ErrorOverQuotaTip(ErrorCode.OVER_QUOTA);
            case 16:
                return new ErrorBuildThumbnailTip(ErrorCode.THUMBNAIL_BUILD_ERROR);
            case 17:
                return new ErrorNotSyncedTip(ErrorCode.NOT_SYNCED);
            case 18:
                return new ErrorWriteExifTip(ErrorCode.WRITE_EXIF_ERROR);
            case 19:
                return new ErrorDownloadFileServerInvalid(ErrorCode.SERVER_INVALID);
            default:
                return new ErrorUnknownTip(ErrorCode.UNKNOWN);
        }
    }

    @Override // com.miui.gallery.error.core.ErrorCodeTranslator
    public void translate(final Context context, final ErrorCode errorCode, final String str, final ErrorTranslateCallback errorTranslateCallback) {
        ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.error.BaseErrorCodeTranslator.1
            @Override // java.lang.Runnable
            public void run() {
                if (BaseErrorCodeTranslator.this.mTask != null) {
                    BaseErrorCodeTranslator.this.mTask.cancel(true);
                    BaseErrorCodeTranslator.this.mTask = null;
                }
                BaseErrorCodeTranslator.this.mTask = new AsyncTask<Pair<ErrorCode, String>, Void, ErrorTip>() { // from class: com.miui.gallery.error.BaseErrorCodeTranslator.1.1
                    @Override // android.os.AsyncTask
                    public ErrorTip doInBackground(Pair<ErrorCode, String>... pairArr) {
                        AnonymousClass1 anonymousClass1 = AnonymousClass1.this;
                        return BaseErrorCodeTranslator.this.translateInternal(context, (ErrorCode) pairArr[0].first, (String) pairArr[0].second);
                    }

                    @Override // android.os.AsyncTask
                    public void onPostExecute(ErrorTip errorTip) {
                        ErrorTranslateCallback errorTranslateCallback2;
                        if (isCancelled() || (errorTranslateCallback2 = errorTranslateCallback) == null) {
                            return;
                        }
                        errorTranslateCallback2.onTranslate(errorTip);
                    }
                }.execute(new Pair<>(errorCode, str));
            }
        });
    }
}
