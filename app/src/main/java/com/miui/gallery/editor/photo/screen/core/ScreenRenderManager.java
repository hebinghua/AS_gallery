package com.miui.gallery.editor.photo.screen.core;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import com.miui.gallery.editor.photo.app.DraftManager;
import com.miui.gallery.editor.photo.screen.base.ScreenRenderCallback;
import com.miui.gallery.util.logger.DefaultLogger;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/* loaded from: classes2.dex */
public class ScreenRenderManager {
    public DraftManager mDraftManager;
    public Bitmap mOrigin;
    public OnOriginLoadedListener mOriginLoadedListener;
    public Bitmap mRenderBitmap;

    /* loaded from: classes2.dex */
    public interface OnOriginLoadedListener {
        void onRefresh(Bitmap bitmap);
    }

    public static /* synthetic */ void $r8$lambda$W0nv5OS79olL9X06j7Nlk2sl2Oc(ScreenRenderManager screenRenderManager, ScreenRenderCallback screenRenderCallback, boolean z, Bitmap bitmap) {
        screenRenderManager.lambda$renderBitmap$3(screenRenderCallback, z, bitmap);
    }

    /* renamed from: $r8$lambda$ZwMXoLjsFUJPJ-G47ZSUmwN_o7Q */
    public static /* synthetic */ void m921$r8$lambda$ZwMXoLjsFUJPJG47ZSUmwN_o7Q(ScreenRenderManager screenRenderManager, ObservableEmitter observableEmitter) {
        screenRenderManager.lambda$decodeOrigin$0(observableEmitter);
    }

    public static /* synthetic */ void $r8$lambda$gJMhCpxfSIU6qn70VKtrOYJSrEQ(ScreenRenderManager screenRenderManager, Bitmap bitmap) {
        screenRenderManager.lambda$decodeOrigin$1(bitmap);
    }

    public static /* synthetic */ void $r8$lambda$inJEMgY6jLSo0EwyRMIVELI0hfE(ScreenRenderManager screenRenderManager, ScreenRenderData screenRenderData, ObservableEmitter observableEmitter) {
        screenRenderManager.lambda$renderBitmap$2(screenRenderData, observableEmitter);
    }

    public ScreenRenderManager(DraftManager draftManager) {
        this.mDraftManager = draftManager;
    }

    public void decodeOrigin() {
        if (this.mDraftManager.isPreviewSameWithOrigin()) {
            Bitmap preview = this.mDraftManager.getPreview();
            this.mOrigin = preview;
            OnOriginLoadedListener onOriginLoadedListener = this.mOriginLoadedListener;
            if (onOriginLoadedListener == null) {
                return;
            }
            onOriginLoadedListener.onRefresh(preview);
            return;
        }
        Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.editor.photo.screen.core.ScreenRenderManager$$ExternalSyntheticLambda0
            @Override // io.reactivex.ObservableOnSubscribe
            public final void subscribe(ObservableEmitter observableEmitter) {
                ScreenRenderManager.m921$r8$lambda$ZwMXoLjsFUJPJG47ZSUmwN_o7Q(ScreenRenderManager.this, observableEmitter);
            }
        }).subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() { // from class: com.miui.gallery.editor.photo.screen.core.ScreenRenderManager$$ExternalSyntheticLambda2
            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                ScreenRenderManager.$r8$lambda$gJMhCpxfSIU6qn70VKtrOYJSrEQ(ScreenRenderManager.this, (Bitmap) obj);
            }
        });
    }

    public /* synthetic */ void lambda$decodeOrigin$0(ObservableEmitter observableEmitter) throws Exception {
        observableEmitter.onNext(this.mDraftManager.decodeOrigin());
    }

    public /* synthetic */ void lambda$decodeOrigin$1(Bitmap bitmap) throws Exception {
        this.mOrigin = bitmap;
        OnOriginLoadedListener onOriginLoadedListener = this.mOriginLoadedListener;
        if (onOriginLoadedListener != null) {
            onOriginLoadedListener.onRefresh(bitmap);
        }
    }

    public void renderBitmap(final boolean z, final ScreenRenderData screenRenderData, final ScreenRenderCallback screenRenderCallback) {
        DefaultLogger.d("ScreenRenderManager", "renderBitmap: start.");
        Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.editor.photo.screen.core.ScreenRenderManager$$ExternalSyntheticLambda1
            @Override // io.reactivex.ObservableOnSubscribe
            public final void subscribe(ObservableEmitter observableEmitter) {
                ScreenRenderManager.$r8$lambda$inJEMgY6jLSo0EwyRMIVELI0hfE(ScreenRenderManager.this, screenRenderData, observableEmitter);
            }
        }).subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() { // from class: com.miui.gallery.editor.photo.screen.core.ScreenRenderManager$$ExternalSyntheticLambda3
            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                ScreenRenderManager.$r8$lambda$W0nv5OS79olL9X06j7Nlk2sl2Oc(ScreenRenderManager.this, screenRenderCallback, z, (Bitmap) obj);
            }
        });
    }

    public /* synthetic */ void lambda$renderBitmap$2(ScreenRenderData screenRenderData, ObservableEmitter observableEmitter) throws Exception {
        observableEmitter.onNext(screenRenderData.apply(getOriginBitmap()));
    }

    public /* synthetic */ void lambda$renderBitmap$3(ScreenRenderCallback screenRenderCallback, boolean z, Bitmap bitmap) throws Exception {
        this.mRenderBitmap = bitmap;
        screenRenderCallback.setShareBitmap(bitmap, true);
        screenRenderCallback.onComplete(z);
    }

    public Bitmap getOriginBitmap() {
        if (this.mOrigin == null) {
            this.mOrigin = this.mDraftManager.decodeOrigin();
        }
        return this.mOrigin;
    }

    public Bitmap getRenderBitmap() {
        Bitmap bitmap = this.mRenderBitmap;
        return bitmap == null ? getOriginBitmap() : bitmap;
    }

    public void release() {
        Bitmap bitmap = this.mRenderBitmap;
        if (bitmap != null && !bitmap.isRecycled()) {
            this.mRenderBitmap.recycle();
            this.mRenderBitmap = null;
        }
        Bitmap bitmap2 = this.mOrigin;
        if (bitmap2 == null || bitmap2.isRecycled()) {
            return;
        }
        this.mOrigin.recycle();
        this.mOrigin = null;
    }

    public void setOriginLoadedListener(OnOriginLoadedListener onOriginLoadedListener) {
        this.mOriginLoadedListener = onOriginLoadedListener;
    }
}
