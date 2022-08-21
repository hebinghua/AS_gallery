package com.miui.gallery.ui.album;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import com.miui.gallery.util.thread.RxGalleryExecutors;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.subscribers.DisposableSubscriber;
import java.util.concurrent.TimeUnit;

@Deprecated
/* loaded from: classes2.dex */
public abstract class NoRepeatContentObserver extends ContentObserver {
    public ContentOnSubscribe<MyContentSubscribeBean> mContentOnSubscribe;
    public MyContentSubscribeBean mContentSubscribeBean;
    public Disposable mDisposable;

    public abstract void onDataChange(boolean z, Uri uri);

    public NoRepeatContentObserver(Handler handler, long j, TimeUnit timeUnit) {
        super(handler);
        this.mContentOnSubscribe = new ContentOnSubscribe<>();
        this.mContentSubscribeBean = new MyContentSubscribeBean();
        this.mDisposable = (Disposable) Flowable.create(this.mContentOnSubscribe, BackpressureStrategy.BUFFER).throttleLatest(j, timeUnit, RxGalleryExecutors.getInstance().getUserThreadScheduler()).doOnCancel(new Action() { // from class: com.miui.gallery.ui.album.NoRepeatContentObserver.2
            @Override // io.reactivex.functions.Action
            public void run() throws Exception {
                NoRepeatContentObserver.this.mContentOnSubscribe = null;
                NoRepeatContentObserver.this.mContentSubscribeBean = null;
            }
        }).subscribeWith(new DisposableSubscriber<MyContentSubscribeBean>() { // from class: com.miui.gallery.ui.album.NoRepeatContentObserver.1
            @Override // org.reactivestreams.Subscriber
            public void onComplete() {
            }

            @Override // org.reactivestreams.Subscriber
            public void onError(Throwable th) {
            }

            @Override // org.reactivestreams.Subscriber
            public void onNext(MyContentSubscribeBean myContentSubscribeBean) {
                if (myContentSubscribeBean == null) {
                    return;
                }
                NoRepeatContentObserver.this.onDataChange(myContentSubscribeBean.isSelfChange, myContentSubscribeBean.mUri);
            }
        });
    }

    @Override // android.database.ContentObserver
    public void onChange(boolean z, Uri uri) {
        MyContentSubscribeBean myContentSubscribeBean = this.mContentSubscribeBean;
        if (myContentSubscribeBean == null) {
            return;
        }
        myContentSubscribeBean.setUri(uri);
        this.mContentSubscribeBean.setSelfChange(z);
        this.mContentOnSubscribe.getEmitter().onNext(this.mContentSubscribeBean);
    }

    public void dispose() {
        Disposable disposable = this.mDisposable;
        if (disposable != null) {
            disposable.dispose();
        }
    }

    /* loaded from: classes2.dex */
    public static class MyContentSubscribeBean {
        public boolean isSelfChange;
        public Uri mUri;

        public MyContentSubscribeBean() {
        }

        public void setSelfChange(boolean z) {
            this.isSelfChange = z;
        }

        public void setUri(Uri uri) {
            this.mUri = uri;
        }
    }

    /* loaded from: classes2.dex */
    public static final class ContentOnSubscribe<T extends MyContentSubscribeBean> implements FlowableOnSubscribe<T> {
        public FlowableEmitter<T> mEmitter;

        public ContentOnSubscribe() {
        }

        public FlowableEmitter<T> getEmitter() {
            return this.mEmitter;
        }

        @Override // io.reactivex.FlowableOnSubscribe
        public void subscribe(FlowableEmitter<T> flowableEmitter) throws Exception {
            this.mEmitter = flowableEmitter;
        }
    }
}
