package com.miui.gallery.search.core.source.server;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.model.PersistentResponse;
import com.miui.gallery.net.base.ErrorCode;
import com.miui.gallery.net.base.RequestError;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.result.BaseSourceResult;
import com.miui.gallery.search.core.result.ErrorInfo;
import com.miui.gallery.search.core.result.SourceResult;
import com.miui.gallery.search.core.source.InterceptableSource;
import com.miui.gallery.search.core.source.server.ServerSearchRequest;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.search.utils.SearchLog;
import com.miui.gallery.search.utils.SearchUtils;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.PersistentResponseHelper;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public abstract class ServerSource extends InterceptableSource {
    public abstract boolean canCarryLog();

    public int getMethod(QueryInfo queryInfo, String str) {
        return 1001;
    }

    @Override // com.miui.gallery.search.core.source.Source
    public int getPriority(QueryInfo queryInfo) {
        return 2;
    }

    public abstract String getQueryAppendPath(QueryInfo queryInfo);

    public boolean isPersistable(QueryInfo queryInfo) {
        return false;
    }

    public abstract SourceResult onResponse(QueryInfo queryInfo, ServerSearchRequest serverSearchRequest, Object obj);

    public boolean useCache(QueryInfo queryInfo) {
        return false;
    }

    public ServerSource(Context context) {
        super(context);
    }

    public String getQueryPathPrefix(QueryInfo queryInfo, String str) {
        return HostManager.Search.getSearchUrlHost();
    }

    public Map<String, String> getQueryParams(QueryInfo queryInfo) {
        return new HashMap(queryInfo.getParams());
    }

    public String getQueryUserPath(String str) {
        return ServerSearchRequest.Builder.getDefaultUserPath(str);
    }

    @Override // com.miui.gallery.search.core.source.InterceptableSource
    public SourceResult getPrejudgeResult(QueryInfo queryInfo) {
        Object fromJson;
        SourceResult prejudgeResult = super.getPrejudgeResult(queryInfo);
        if (prejudgeResult != null) {
            return prejudgeResult;
        }
        boolean z = false;
        boolean z2 = !BaseNetworkUtils.isNetworkConnected() && isFatalCondition(queryInfo, 1);
        boolean z3 = queryInfo != null && Boolean.valueOf(queryInfo.getParam("use_persistent_response")).booleanValue();
        if ((z2 || z3) && isPersistable(queryInfo)) {
            try {
                ServerSearchRequest build = getRequestBuilder(queryInfo).build();
                Map<String, String> params = build.getParams();
                SearchUtils.removeExtraParamsForPersistence(params);
                PersistentResponse find = PersistentResponseHelper.find(build.getUrl(), params);
                if (find != null && !TextUtils.isEmpty(find.getResponse()) && (fromJson = HttpUtils.createGson().fromJson(find.getResponse(), getResponseDataType(queryInfo))) != null) {
                    SourceResult onResponse = onResponse(queryInfo, build, fromJson);
                    if (onResponse != null) {
                        boolean z4 = find.getUpdateTime() + find.getExpireMillis() < System.currentTimeMillis();
                        Bundle resultExtras = onResponse.getResultExtras();
                        if (resultExtras == null || resultExtras == Bundle.EMPTY) {
                            resultExtras = new Bundle();
                            onResponse.setResultExtras(resultExtras);
                        }
                        if (!z2 && z4) {
                            z = true;
                        }
                        resultExtras.putBoolean("need_requery", z);
                        resultExtras.putString("result_hash_code", SearchUtils.generateResultHashCode(find.getResponse()));
                    }
                    return onResponse;
                }
            } catch (Exception e) {
                SearchLog.e("ServerSource", e);
            }
        }
        if (!z2) {
            return prejudgeResult;
        }
        BaseSourceResult generateDefaultResult = generateDefaultResult(queryInfo, null);
        generateDefaultResult.setErrorInfo(new ErrorInfo(1));
        return generateDefaultResult;
    }

    @Override // com.miui.gallery.search.core.source.InterceptableSource
    public boolean isFatalCondition(QueryInfo queryInfo, int i) {
        if (i == 1 || i == 3 || i == 10 || i == 13) {
            return true;
        }
        return super.isFatalCondition(queryInfo, i);
    }

    public SourceResult onResponseError(QueryInfo queryInfo, ServerSearchRequest serverSearchRequest, RequestError requestError) {
        return onResponseError(queryInfo, new ErrorInfo(getSearchErrorCodeFromNetErrorCode(requestError.getErrorCode()), requestError.getMessage()));
    }

    public SourceResult onResponseError(QueryInfo queryInfo, ErrorInfo errorInfo) {
        SearchLog.w("ServerSource", "onResponseError for query %s, error %s", queryInfo, errorInfo);
        BaseSourceResult generateDefaultResult = generateDefaultResult(queryInfo, null);
        generateDefaultResult.setErrorInfo(errorInfo);
        return generateDefaultResult;
    }

    /* renamed from: com.miui.gallery.search.core.source.server.ServerSource$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$net$base$ErrorCode;

        static {
            int[] iArr = new int[ErrorCode.values().length];
            $SwitchMap$com$miui$gallery$net$base$ErrorCode = iArr;
            try {
                iArr[ErrorCode.NETWORK_NOT_CONNECTED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$net$base$ErrorCode[ErrorCode.SERVER_ERROR.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$net$base$ErrorCode[ErrorCode.NET_ERROR.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$net$base$ErrorCode[ErrorCode.BODY_EMPTY.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$net$base$ErrorCode[ErrorCode.PARSE_ERROR.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$miui$gallery$net$base$ErrorCode[ErrorCode.HANDLE_ERROR.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$miui$gallery$net$base$ErrorCode[ErrorCode.DATA_BIND_ERROR.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    public int getSearchErrorCodeFromNetErrorCode(ErrorCode errorCode) {
        switch (AnonymousClass1.$SwitchMap$com$miui$gallery$net$base$ErrorCode[errorCode.ordinal()]) {
            case 1:
                return 1;
            case 2:
                return 6;
            case 3:
            case 4:
                return 5;
            case 5:
            case 6:
            case 7:
                return 7;
            default:
                return 9;
        }
    }

    @Override // com.miui.gallery.search.core.source.InterceptableSource
    public SourceResult doGetSuggestions(QueryInfo queryInfo) {
        String pullCachedEvent;
        ServerSearchRequest build = getRequestBuilder(queryInfo).build();
        if (canCarryLog() && (pullCachedEvent = SearchStatUtils.pullCachedEvent()) != null) {
            build.addParam("logData", pullCachedEvent);
        }
        try {
            Object[] executeSync = build.executeSync();
            String str = null;
            if (executeSync != null && executeSync.length > 0) {
                if (isPersistable(queryInfo)) {
                    long j = -1;
                    if (build.getResponseData() instanceof Cacheable) {
                        j = ((Cacheable) build.getResponseData()).getExpireMills();
                    }
                    Map<String, String> params = build.getParams();
                    SearchUtils.removeExtraParamsForPersistence(params);
                    PersistentResponseHelper.saveAsync(build.getUrl(), params, build.getResponseRawData(), j);
                    str = SearchUtils.generateResultHashCode(build.getResponseRawData());
                }
                SourceResult onResponse = onResponse(queryInfo, build, executeSync[0]);
                if (str != null && onResponse != null) {
                    Bundle resultExtras = onResponse.getResultExtras();
                    if (resultExtras == null || resultExtras == Bundle.EMPTY) {
                        resultExtras = new Bundle();
                        onResponse.setResultExtras(resultExtras);
                    }
                    resultExtras.putString("result_hash_code", str);
                }
                return onResponse;
            }
            throw new RequestError(ErrorCode.BODY_EMPTY, "Execute result should not be null", null);
        } catch (RequestError e) {
            return onResponseError(queryInfo, build, e);
        } catch (Exception unused) {
            return onResponseError(queryInfo, new ErrorInfo(7));
        }
    }

    public Type getResponseDataType(QueryInfo queryInfo) {
        return SectionedResponseData.class;
    }

    public ServerSearchRequest.Builder getRequestBuilder(QueryInfo queryInfo) {
        String xiaomiId = SearchUtils.getXiaomiId();
        return new ServerSearchRequest.Builder().setQueryPathPrefix(getQueryPathPrefix(queryInfo, xiaomiId)).setResultDataType(getResponseDataType(queryInfo)).setMethod(getMethod(queryInfo, xiaomiId)).setUserPath(getQueryUserPath(xiaomiId)).setUserId(xiaomiId).setQueryAppendPath(getQueryAppendPath(queryInfo)).addQueryParams(getQueryParams(queryInfo)).setUseCache(useCache(queryInfo));
    }
}
