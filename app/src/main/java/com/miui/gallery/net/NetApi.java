package com.miui.gallery.net;

import com.miui.gallery.net.base.ErrorCode;
import com.miui.gallery.net.base.ResponseListener;
import com.miui.gallery.net.base.VolleyRequest;
import com.miui.gallery.util.OptionalResult;
import com.miui.gallery.util.logger.DefaultLogger;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/* loaded from: classes2.dex */
public class NetApi {
    public static <T> Observable<OptionalResult<T>> create(final BaseGalleryRequest baseGalleryRequest) {
        return Observable.create(new ObservableOnSubscribe<OptionalResult<T>>() { // from class: com.miui.gallery.net.NetApi.1
            @Override // io.reactivex.ObservableOnSubscribe
            public void subscribe(final ObservableEmitter<OptionalResult<T>> observableEmitter) throws Exception {
                BaseGalleryRequest.this.execute(new ResponseListener() { // from class: com.miui.gallery.net.NetApi.1.1
                    @Override // com.miui.gallery.net.base.ResponseListener
                    public void onResponse(Object... objArr) {
                        observableEmitter.onNext(new OptionalResult(objArr[0]));
                        DefaultLogger.d("NetApi", String.format("%s onResponseSuccess", BaseGalleryRequest.this.getClass().getSimpleName()));
                    }

                    @Override // com.miui.gallery.net.base.ResponseListener
                    public void onResponseError(ErrorCode errorCode, String str, Object obj) {
                        observableEmitter.onNext(new OptionalResult(null));
                        DefaultLogger.d("NetApi", String.format("%s onResponseError", BaseGalleryRequest.this.getClass().getSimpleName()));
                    }
                });
            }
        });
    }

    public static <T> Observable<OptionalResult<T>> create(final VolleyRequest volleyRequest) {
        return Observable.create(new ObservableOnSubscribe<OptionalResult<T>>() { // from class: com.miui.gallery.net.NetApi.2
            @Override // io.reactivex.ObservableOnSubscribe
            public void subscribe(final ObservableEmitter<OptionalResult<T>> observableEmitter) throws Exception {
                VolleyRequest.this.execute(new ResponseListener() { // from class: com.miui.gallery.net.NetApi.2.1
                    @Override // com.miui.gallery.net.base.ResponseListener
                    public void onResponse(Object... objArr) {
                        observableEmitter.onNext(new OptionalResult(objArr[0]));
                        DefaultLogger.d("NetApi", String.format("%s onResponseSuccess", VolleyRequest.this.getClass().getSimpleName()));
                    }

                    @Override // com.miui.gallery.net.base.ResponseListener
                    public void onResponseError(ErrorCode errorCode, String str, Object obj) {
                        observableEmitter.onNext(new OptionalResult(null));
                        DefaultLogger.d("NetApi", String.format("%s onResponseError", VolleyRequest.this.getClass().getSimpleName()));
                    }
                });
            }
        });
    }
}
