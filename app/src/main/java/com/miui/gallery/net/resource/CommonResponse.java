package com.miui.gallery.net.resource;

import com.miui.gallery.net.base.ErrorCode;
import com.miui.gallery.net.base.ResponseListener;
import com.miui.gallery.net.resource.LocalResource;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.List;

/* loaded from: classes2.dex */
public class CommonResponse<T extends LocalResource> implements ResponseListener {
    public Callback mCallback;
    public boolean mIsResponsed;

    /* loaded from: classes2.dex */
    public interface Callback<T> {
        void onFail();

        void onSuccess(List<T> list);
    }

    public static /* synthetic */ void $r8$lambda$5bHWJ0YLvseZ52gVd26MxzmTJ5k(CommonResponse commonResponse, ErrorCode errorCode, String str) {
        commonResponse.lambda$onResponseError$1(errorCode, str);
    }

    /* renamed from: $r8$lambda$Pj-KtRmTDdLthJE5V2mzo2g69UM */
    public static /* synthetic */ void m1164$r8$lambda$PjKtRmTDdLthJE5V2mzo2g69UM(CommonResponse commonResponse, Object[] objArr) {
        commonResponse.lambda$onResponse$0(objArr);
    }

    public CommonResponse(Callback callback) {
        this.mCallback = callback;
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    @Override // com.miui.gallery.net.base.ResponseListener
    public void onResponse(final Object... objArr) {
        ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.net.resource.CommonResponse$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                CommonResponse.m1164$r8$lambda$PjKtRmTDdLthJE5V2mzo2g69UM(CommonResponse.this, objArr);
            }
        });
    }

    public /* synthetic */ void lambda$onResponse$0(Object[] objArr) {
        List list = (List) objArr[0];
        this.mIsResponsed = true;
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onSuccess(list);
        }
    }

    @Override // com.miui.gallery.net.base.ResponseListener
    public void onResponseError(final ErrorCode errorCode, final String str, Object obj) {
        ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.net.resource.CommonResponse$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                CommonResponse.$r8$lambda$5bHWJ0YLvseZ52gVd26MxzmTJ5k(CommonResponse.this, errorCode, str);
            }
        });
    }

    public /* synthetic */ void lambda$onResponseError$1(ErrorCode errorCode, String str) {
        this.mIsResponsed = true;
        DefaultLogger.e("CommonResponse", "errorCode: %s , errorMessage: %s. ", errorCode, str);
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onFail();
        }
    }
}
