package com.miui.gallery.cloud.card.network;

import com.miui.gallery.cloud.card.exception.NoResultException;
import com.miui.gallery.cloud.card.exception.RequestArgumentsNullException;
import com.miui.gallery.cloud.card.exception.TypeNotFoundException;
import com.miui.gallery.net.base.ErrorCode;
import com.miui.gallery.net.base.RequestError;
import com.miui.gallery.net.base.ResponseListener;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/* loaded from: classes.dex */
public class CommonGalleryRequestHelper<E> {
    public CommonGalleryRequest mBaseGalleryRequest;
    public Type mDataType;

    public CommonGalleryRequestHelper(RequestArguments<E> requestArguments) {
        if (requestArguments == null) {
            throw new RequestArgumentsNullException();
        }
        Type[] genericInterfaces = requestArguments.getClass().getGenericInterfaces();
        if (genericInterfaces.length > 0 && (genericInterfaces[0] instanceof ParameterizedType)) {
            Type type = ((ParameterizedType) genericInterfaces[0]).getActualTypeArguments()[0];
            if (type instanceof Class) {
                this.mDataType = type;
                this.mBaseGalleryRequest = new CommonGalleryRequest(requestArguments.getMethod(), requestArguments.getUrl(), this.mDataType);
                return;
            }
            throw new TypeNotFoundException();
        }
        throw new TypeNotFoundException();
    }

    public final void execute(final ResponseCallback<E> responseCallback) {
        this.mBaseGalleryRequest.setOnResponseListener(new ResponseListener() { // from class: com.miui.gallery.cloud.card.network.CommonGalleryRequestHelper.1
            @Override // com.miui.gallery.net.base.ResponseListener
            public void onResponse(Object... objArr) {
                Object obj = objArr[0];
                ResponseCallback responseCallback2 = responseCallback;
                if (responseCallback2 != null) {
                    responseCallback2.onResponse(obj);
                }
            }

            @Override // com.miui.gallery.net.base.ResponseListener
            public void onResponseError(ErrorCode errorCode, String str, Object obj) {
                ResponseCallback responseCallback2 = responseCallback;
                if (responseCallback2 != null) {
                    responseCallback2.onResponseError(errorCode, str, obj);
                }
            }
        });
        this.mBaseGalleryRequest.execute();
    }

    public final E executeSync() throws RequestError, NoResultException {
        Object[] executeSync = this.mBaseGalleryRequest.executeSync();
        if (executeSync != null && executeSync.length > 0) {
            return (E) executeSync[0];
        }
        throw new NoResultException();
    }

    public final CommonGalleryRequestHelper addParam(String str, String str2) {
        this.mBaseGalleryRequest.addParam(str, str2);
        return this;
    }

    public final CommonGalleryRequestHelper setUseCache(boolean z) {
        this.mBaseGalleryRequest.setUseCache(z);
        return this;
    }
}
