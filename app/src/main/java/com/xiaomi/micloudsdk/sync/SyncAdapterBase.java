package com.xiaomi.micloudsdk.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.baidu.mapapi.UIMsg;
import com.xiaomi.micloudsdk.data.ExtendedAuthToken;
import com.xiaomi.micloudsdk.exception.CloudServerException;
import com.xiaomi.micloudsdk.exception.SyncLocalException;
import com.xiaomi.micloudsdk.request.utils.SyncRequestParam;
import com.xiaomi.micloudsdk.stat.MiCloudErrorStatReason;
import com.xiaomi.micloudsdk.sync.SyncLogInfo;
import com.xiaomi.micloudsdk.sync.utils.LegacySyncRecordUtils;
import com.xiaomi.micloudsdk.sync.utils.SyncExceptionUtils;
import com.xiaomi.micloudsdk.sync.utils.SyncTimeUtils;
import com.xiaomi.micloudsdk.utils.MiCloudConstants;
import com.xiaomi.micloudsdk.utils.ReflectUtils;
import java.io.IOException;
import java.lang.reflect.Field;
import micloud.compat.v18.sync.SyncAdapterBaseInjectorCompat;
import miui.cloud.log.PrivacyFilter;
import miui.cloud.util.SyncAutoSettingUtil;

/* loaded from: classes3.dex */
public abstract class SyncAdapterBase extends AbstractThreadedSyncAdapter {
    public boolean isForceSync;
    public boolean isIgnoreBackoff;
    public boolean isIgnoreBatteryLow;
    public boolean isIgnoreSettings;
    public boolean isIgnoreTemperature;
    public boolean isIgnoreWifiSettings;
    public boolean isManualSync;
    public Account mAccount;
    public final String mAuthType;
    public String mAuthority;
    public Context mContext;
    public ExtendedAuthToken mExtToken;
    public String mExtTokenStr;
    public String[] mNumbers;
    public ContentResolver mResolver;
    public SyncLogSender mSyncLogSender;
    public SyncResult mSyncResult;
    public String[] mTickets;

    public abstract void onPerformMiCloudSync(Bundle bundle) throws CloudServerException, SyncLocalException;

    public void onPerformMiCloudSyncComplete(MiCloudSyncResult miCloudSyncResult) {
    }

    public void onTransformExtAuthToken(ExtendedAuthToken extendedAuthToken) {
    }

    public SyncAdapterBase(Context context, boolean z, String str) {
        super(context, z);
        this.mTickets = new String[2];
        this.mNumbers = new String[2];
        this.mContext = context;
        this.mResolver = context.getContentResolver();
        this.mAuthType = str;
    }

    @Override // android.content.AbstractThreadedSyncAdapter
    public void onPerformSync(Account account, Bundle bundle, String str, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        long j;
        Log.d("MiCloudSyncAdapterBase", "check_sync_error_refactor");
        this.mAccount = account;
        this.mAuthority = str;
        this.mSyncResult = syncResult;
        long currentTimeMillis = System.currentTimeMillis();
        String str2 = "SyncLog[" + str + "]";
        Log.d("MiCloudSyncAdapterBase", "onPerformSync: ---sync start---");
        Log.d("MiCloudSyncAdapterBase", "authority: " + str + ", extras: " + bundle.toString());
        this.isForceSync = bundle.getBoolean(MiCloudConstants.SYNC.SYNC_EXTRAS_FORCE, false);
        this.isIgnoreTemperature = bundle.getBoolean("micloud_ignore_temperature", false);
        this.isIgnoreWifiSettings = bundle.getBoolean("micloud_ignore_wifi_settings", false);
        this.isIgnoreBatteryLow = bundle.getBoolean("micloud_ignore_battery_low", false);
        this.isManualSync = bundle.getBoolean("force", false);
        this.isIgnoreBackoff = bundle.getBoolean("ignore_backoff", false);
        boolean z = bundle.getBoolean("ignore_settings", false);
        this.isIgnoreSettings = z;
        boolean z2 = this.isForceSync || this.isManualSync || z;
        SyncStatUtil.statOnSyncStart(this.mContext, z2);
        acquireLogger(str);
        syncLog(str2, new SyncLogInfo.SyncStartLogInfo(str, currentTimeMillis, bundle).generateLogInfoString());
        SyncDataInfo generateCurrentSyncDataInfo = SyncDataInfo.generateCurrentSyncDataInfo(this.mContext, str);
        syncLog(str2, "Before Sync: " + generateCurrentSyncDataInfo.toString());
        MiCloudSyncResult createSuccessResult = MiCloudSyncResult.createSuccessResult();
        if (!SyncAdapterBaseInjectorCompat.isGdprPermissionGranted(this.mContext, syncResult)) {
            Log.i("MiCloudSyncAdapterBase", "Gdpr Permission deny: " + str);
            internalSetGDPRError(syncResult);
            createSuccessResult = MiCloudSyncResult.createFailResult(new SyncLocalException(2000), 2000, "gdpr_deny");
        } else if (!SyncAutoSettingUtil.getXiaomiGlobalSyncAutomatically() || !ContentResolver.getSyncAutomatically(account, str)) {
            Log.e("MiCloudSyncAdapterBase", "onPerformSync: sync switch off. do not sync!!");
            internalSetSyncSwitchOffError(syncResult);
            createSuccessResult = MiCloudSyncResult.createFailResult(new SyncLocalException(2003), LegacySyncRecordUtils.getSyncSwitchOffErrorLegacyCode(), "switch_off");
        } else if (MiCloudResolver.isSyncPausing(this.mContext, this.mAccount, this.mAuthority)) {
            Log.e("MiCloudSyncAdapterBase", "onPerformSync: sync is set to pause. do not sync!!");
            internalSetPauseSyncError(syncResult);
            createSuccessResult = MiCloudSyncResult.createFailResult(new SyncLocalException(UIMsg.m_AppUI.MSG_APP_VERSION), LegacySyncRecordUtils.getPauseLimitErrorLegacyCode(), "pause_limit");
        } else {
            ExtTokenResult extTokenResult = getExtTokenResult(this.mContext, this.mAccount, this.mAuthority);
            this.mExtTokenStr = extTokenResult.extTokenStr;
            if (extTokenResult.error != null) {
                Log.e("MiCloudSyncAdapterBase", "onPerformSync: Tried 3 times, but get null token.");
                internalSetAuthError(syncResult);
                createSuccessResult = MiCloudSyncResult.createFailResult(extTokenResult.error, LegacySyncRecordUtils.getAuthTokenErrorLegacyCode(), "auth_token_error");
            } else {
                ExtendedAuthToken parse = ExtendedAuthToken.parse(this.mExtTokenStr);
                this.mExtToken = parse;
                onTransformExtAuthToken(parse);
                if (this.mExtToken == null) {
                    Log.e("MiCloudSyncAdapterBase", "onPerformSync: Cannot parse ext token");
                    internalSetAuthError(syncResult);
                    invalidTokenAndReSyncNowIfFirstTimeFailed(this.mContext, account, str, this.mExtTokenStr, bundle);
                    createSuccessResult = MiCloudSyncResult.createFailResult(new SyncLocalException(100), LegacySyncRecordUtils.getAuthTokenErrorLegacyCode(), "auth_token_error");
                } else {
                    boolean z3 = this.isForceSync || this.isManualSync || this.isIgnoreSettings;
                    if (z3) {
                        SyncRequestParam.incrementManualSyncCountAndRecordCurMillis();
                    }
                    try {
                        try {
                            try {
                                onPerformMiCloudSync(bundle);
                                if (z3) {
                                    SyncRequestParam.decrementManualSyncCount();
                                }
                                if (syncResult.hasError()) {
                                    Log.w("MiCloudSyncAdapterBase", "onPerformSync: Error without throw CloudException: " + SyncExceptionUtils.getErrorStringForLog(syncResult));
                                    createSuccessResult = MiCloudSyncResult.createFailResult(new SyncLocalException(syncResult.hasHardError() ? 2 : 1), LegacySyncRecordUtils.getErrorCodeBySyncResult(syncResult), MiCloudErrorStatReason.getSyncResultErrorReasonForStats(syncResult));
                                } else {
                                    Log.d("MiCloudSyncAdapterBase", "onPerformSync: No error found. ---Success---");
                                }
                            } catch (SyncLocalException e) {
                                setSyncResultByLocalException(e, syncResult);
                                String localExceptionReasonForStats = MiCloudErrorStatReason.getLocalExceptionReasonForStats(e);
                                Log.e("MiCloudSyncAdapterBase", "onPerformSync local exception, reason: " + localExceptionReasonForStats);
                                handleExceptionIfKeyBagRelated(e);
                                createSuccessResult = MiCloudSyncResult.createFailResult(e, LegacySyncRecordUtils.getErrorCodeByLocalException(e), localExceptionReasonForStats);
                                if (z3) {
                                    SyncRequestParam.decrementManualSyncCount();
                                }
                            }
                        } catch (CloudServerException e2) {
                            setSyncResultByServerException(e2, syncResult);
                            if (e2.statusCode == 401) {
                                invalidTokenAndReSyncNowIfFirstTimeFailed(this.mContext, account, str, this.mExtTokenStr, bundle);
                            }
                            long min = e2.is5xxServerException() ? Math.min(e2.get5xxServerExceptionRetryTime() / 1000, 1800L) : 0L;
                            String serverExceptionReasonForStats = MiCloudErrorStatReason.getServerExceptionReasonForStats(e2);
                            Log.e("MiCloudSyncAdapterBase", "onPerformSync server exception, reason: " + serverExceptionReasonForStats);
                            createSuccessResult = MiCloudSyncResult.createFailResult(e2, LegacySyncRecordUtils.getErrorCodeByServerException(e2), serverExceptionReasonForStats);
                            if (z3) {
                                SyncRequestParam.decrementManualSyncCount();
                            }
                            j = min;
                        }
                    } catch (Throwable th) {
                        if (z3) {
                            SyncRequestParam.decrementManualSyncCount();
                        }
                        throw th;
                    }
                }
            }
        }
        j = 0;
        SyncDataInfo generateCurrentSyncDataInfo2 = SyncDataInfo.generateCurrentSyncDataInfo(this.mContext, str);
        syncLog(str2, "After Sync: " + generateCurrentSyncDataInfo2.toString());
        long currentTimeMillis2 = System.currentTimeMillis();
        long j2 = j;
        syncLog(str2, new SyncLogInfo.SyncEndLogInfo(str, createSuccessResult.isSuccess, currentTimeMillis, currentTimeMillis2, getExtraErrorMsgForResultOrNullIfFieldNotExists(syncResult)).generateLogInfoString());
        SyncStatUtil.statOnSyncEnd(this.mContext, z2, createSuccessResult.isSuccess, createSuccessResult.errorReasonForStats, str, generateCurrentSyncDataInfo.getUnSyncedDataCount(), generateCurrentSyncDataInfo2.getUnSyncedDataCount(), currentTimeMillis, currentTimeMillis2);
        if (!createSuccessResult.isSuccess) {
            LegacySyncRecordUtils.recordSyncResult(this.mContext, str, createSuccessResult.errorCodeLegacy);
        } else {
            LegacySyncRecordUtils.recordSyncResultSuccess(this.mContext, str);
        }
        long max = Math.max(syncResult.delayUntil, 0L);
        syncResult.delayUntil = max;
        long max2 = Math.max(max, SyncTimeUtils.getGuardDelayUntilInSec(this.mContext, this.mAuthority));
        syncResult.delayUntil = max2;
        if (!createSuccessResult.isSuccess) {
            syncResult.delayUntil = Math.max(max2, j2);
        }
        onPerformMiCloudSyncComplete(createSuccessResult);
        releaseLogger();
    }

    public final ExtTokenResult getExtTokenResult(Context context, Account account, String str) {
        Throwable e = null;
        for (int i = 0; i < 3; i++) {
            if (i != 0) {
                try {
                    Thread.sleep(1000L);
                } catch (AuthenticatorException e2) {
                    e = e2;
                    Log.e("MiCloudSyncAdapterBase", "onPerformSync getExtTokenStr AuthenticatorException: ", e);
                } catch (OperationCanceledException e3) {
                    e = e3;
                    Log.e("MiCloudSyncAdapterBase", "onPerformSync getExtTokenStr OperationCanceledException: ", e);
                } catch (IOException e4) {
                    e = e4;
                    Log.e("MiCloudSyncAdapterBase", "onPerformSync getExtTokenStr IOException: " + PrivacyFilter.filterPrivacyLog(Log.getStackTraceString(e)));
                } catch (InterruptedException e5) {
                    e = e5;
                    Log.e("MiCloudSyncAdapterBase", "onPerformSync getExtTokenStr InterruptedException: ", e);
                    Thread.currentThread().interrupt();
                }
            }
            Log.v("MiCloudSyncAdapterBase", "onPerformSync: getting auth token. authority: " + str);
            AccountManagerFuture<Bundle> authToken = AccountManager.get(context).getAuthToken(account, this.mAuthType, true, null, null);
            if (authToken == null) {
                Log.e("MiCloudSyncAdapterBase", "onPerformSync: getExtTokenStr null future.");
            } else {
                Bundle result = authToken.getResult();
                if (result == null) {
                    Log.e("MiCloudSyncAdapterBase", "onPerformSync: getExtTokenStr null future result.");
                } else {
                    String string = result.getString("authtoken");
                    if (TextUtils.isEmpty(string)) {
                        Log.e("MiCloudSyncAdapterBase", "onPerformSync: getExtTokenStr future result textEmpty.");
                    } else {
                        return new ExtTokenResult(string, null);
                    }
                }
            }
        }
        if (e == null) {
            e = new SyncLocalException(100);
        }
        return new ExtTokenResult(null, e);
    }

    public static void setSyncResultByServerException(CloudServerException cloudServerException, SyncResult syncResult) {
        int i = cloudServerException.statusCode;
        if (i == -10001) {
            setSyncResultByLocalExceptionCode(cloudServerException.code, syncResult);
        } else if (i == 403 || i == 406 || i == 400) {
            internalSetRequestError(syncResult);
        } else if (i == 401) {
            internalSetAuthError(syncResult);
        } else if (cloudServerException.code == 52003) {
            internalSetGDPRError(syncResult);
        } else if (cloudServerException.is5xxServerException()) {
            internalSetRequestError(syncResult);
        } else {
            internalSetUnknownError(syncResult);
        }
    }

    public static void setSyncResultByLocalException(SyncLocalException syncLocalException, SyncResult syncResult) {
        setSyncResultByLocalExceptionCode(syncLocalException.getErrorCode(), syncResult);
    }

    public static void setSyncResultByLocalExceptionCode(int i, SyncResult syncResult) {
        switch (i) {
            case 1000:
                internalSetCTAError(syncResult);
                return;
            case 1001:
                internalSetSimActivatedError(syncResult);
                return;
            case 1002:
                internalSetPermissionLimit(syncResult);
                return;
            case 1003:
                internalSetSecureSpaceLimitError(syncResult);
                return;
            default:
                switch (i) {
                    case 2000:
                        internalSetGDPRError(syncResult);
                        return;
                    case 2001:
                        internalSetCloudStorageFullError(syncResult);
                        return;
                    case 2002:
                        internalSetWlanOnlyError(syncResult);
                        return;
                    default:
                        internalSetUnknownError(syncResult);
                        return;
                }
        }
    }

    public static void invalidTokenAndReSyncNowIfFirstTimeFailed(Context context, Account account, String str, String str2, Bundle bundle) {
        Bundle bundle2 = new Bundle(bundle);
        if (bundle2.getBoolean("token_invalid_retry", false)) {
            Log.e("MiCloudSyncAdapterBase", "Already retry with invalid token but still failed");
            return;
        }
        bundle2.putBoolean("token_invalid_retry", true);
        Log.w("MiCloudSyncAdapterBase", "AuthToken expired. Invalid and retry now");
        AccountManager.get(context).invalidateAuthToken(account.type, str2);
        ContentResolver.requestSync(account, str, bundle2);
    }

    public static void internalSetRequestError(SyncResult syncResult) {
        syncResult.stats.numIoExceptions++;
        setExtraErrorMsgForResultIfFieldExists(syncResult, "request_error");
    }

    public static void internalSetUnknownError(SyncResult syncResult) {
        syncResult.stats.numConflictDetectedExceptions++;
    }

    public static void internalSetAuthError(SyncResult syncResult) {
        syncResult.stats.numAuthExceptions++;
    }

    public static void internalSetGDPRError(SyncResult syncResult) {
        syncResult.stats.numConflictDetectedExceptions++;
        setExtraErrorMsgForResultIfFieldExists(syncResult, "gdpr_error");
    }

    public static void internalSetSyncSwitchOffError(SyncResult syncResult) {
        syncResult.stats.numConflictDetectedExceptions++;
        setExtraErrorMsgForResultIfFieldExists(syncResult, "switch_off");
    }

    public static void internalSetPauseSyncError(SyncResult syncResult) {
        syncResult.stats.numConflictDetectedExceptions++;
        setExtraErrorMsgForResultIfFieldExists(syncResult, "pause_limit");
    }

    public static void internalSetCloudStorageFullError(SyncResult syncResult) {
        syncResult.stats.numConflictDetectedExceptions++;
        setExtraErrorMsgForResultIfFieldExists(syncResult, "cloud_storage_full");
    }

    public static void internalSetCTAError(SyncResult syncResult) {
        syncResult.stats.numConflictDetectedExceptions++;
        setExtraErrorMsgForResultIfFieldExists(syncResult, "permission_error");
    }

    public static void internalSetSimActivatedError(SyncResult syncResult) {
        syncResult.stats.numConflictDetectedExceptions++;
        setExtraErrorMsgForResultIfFieldExists(syncResult, "sim_activated_error");
    }

    public static void internalSetPermissionLimit(SyncResult syncResult) {
        syncResult.stats.numConflictDetectedExceptions++;
        setExtraErrorMsgForResultIfFieldExists(syncResult, "permission_limit");
    }

    public static void internalSetSecureSpaceLimitError(SyncResult syncResult) {
        syncResult.stats.numConflictDetectedExceptions++;
        setExtraErrorMsgForResultIfFieldExists(syncResult, "secure_space_limit");
    }

    public static void internalSetWlanOnlyError(SyncResult syncResult) {
        syncResult.stats.numConflictDetectedExceptions++;
        setExtraErrorMsgForResultIfFieldExists(syncResult, "wlan_only");
    }

    public final void acquireLogger(String str) {
        SyncLogSender newSyncLogSender = SyncLogSenderFactory.newSyncLogSender(this.mContext, str);
        this.mSyncLogSender = newSyncLogSender;
        newSyncLogSender.openSyncLog();
    }

    public final void releaseLogger() {
        this.mSyncLogSender.release();
        this.mSyncLogSender = null;
    }

    public void syncLog(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            throw new IllegalArgumentException("tag or msg is null!");
        }
        this.mSyncLogSender.sendLog(str, str2);
    }

    public final void handleExceptionIfKeyBagRelated(SyncLocalException syncLocalException) {
        if (syncLocalException.getErrorCode() == 3000) {
            this.mContext.sendBroadcast(new Intent().setPackage("com.miui.cloudservice").setAction("com.miui.cloudservice.keybag.UPDATE"));
        } else if (syncLocalException.getErrorCode() != 3001) {
        } else {
            this.mContext.sendBroadcast(new Intent().setPackage("com.miui.cloudservice").setAction("com.miui.cloudservice.keybag.DOWNLOAD"));
        }
    }

    public static void setExtraErrorMsgForResultIfFieldExists(SyncResult syncResult, String str) {
        ResultMessage resultMessageOrNullIfFieldNotExists = getResultMessageOrNullIfFieldNotExists(syncResult);
        if (resultMessageOrNullIfFieldNotExists == null) {
            return;
        }
        resultMessageOrNullIfFieldNotExists.set(str);
    }

    public static String getExtraErrorMsgForResultOrNullIfFieldNotExists(SyncResult syncResult) {
        ResultMessage resultMessageOrNullIfFieldNotExists = getResultMessageOrNullIfFieldNotExists(syncResult);
        if (resultMessageOrNullIfFieldNotExists == null) {
            return null;
        }
        return resultMessageOrNullIfFieldNotExists.get();
    }

    public static ResultMessage getResultMessageOrNullIfFieldNotExists(SyncResult syncResult) {
        Field field = ReflectUtils.getField(syncResult.getClass(), "miSyncResult");
        if (field == null) {
            return null;
        }
        try {
            Object obj = field.get(syncResult);
            Field field2 = ReflectUtils.getField(obj.getClass(), "resultMessage");
            if (field2 == null) {
                throw new RuntimeException("resultMessageField is null: Please file a bug to CloudService!!");
            }
            return new ResultMessage(obj, field2);
        } catch (IllegalAccessException unused) {
            throw new RuntimeException("get miSyncResultField: Please file a bug to CloudService!!");
        }
    }

    /* loaded from: classes3.dex */
    public static class ResultMessage {
        public final Object miSyncResult;
        public final Field resultMessageField;

        public ResultMessage(Object obj, Field field) {
            this.miSyncResult = obj;
            this.resultMessageField = field;
        }

        public void set(String str) {
            try {
                this.resultMessageField.set(this.miSyncResult, str);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("set miSyncResult: Please file a bug to CloudService!!", e);
            }
        }

        public String get() {
            try {
                return (String) this.resultMessageField.get(this.miSyncResult);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("get resultMessageField: Please file a bug to CloudService!!", e);
            }
        }
    }

    /* loaded from: classes3.dex */
    public class ExtTokenResult {
        public final Throwable error;
        public final String extTokenStr;

        public ExtTokenResult(String str, Throwable th) {
            this.extTokenStr = str;
            this.error = th;
        }
    }
}
