package com.miui.gallery.util;

import android.content.Context;
import com.miui.gallery.R;
import com.miui.gallery.util.logger.DefaultLogger;
import java.lang.ref.WeakReference;
import miuix.appcompat.app.ProgressDialog;

/* loaded from: classes2.dex */
public abstract class GalleryProgressDisposableSubscriber<T> extends SimpleDisposableSubscriber<T> {
    public final boolean isCancelable;
    public final WeakReference<Context> mContext;
    public ProgressDialog mDialog;
    public final String mMsg;
    public final String mTitle;

    public GalleryProgressDisposableSubscriber(Context context) {
        this(context, null, context.getString(R.string.operation_in_process), false);
    }

    public GalleryProgressDisposableSubscriber(Context context, String str, String str2, boolean z) {
        this.mContext = new WeakReference<>(context);
        this.mMsg = str2;
        this.mTitle = str;
        this.isCancelable = z;
    }

    @Override // io.reactivex.subscribers.DisposableSubscriber
    public void onStart() {
        super.onStart();
        if (this.mContext.get() != null) {
            this.mDialog = DialogUtil.showProgressDialog(this.mContext.get(), this.mTitle, this.mMsg, this.isCancelable);
        }
    }

    @Override // com.miui.gallery.util.SimpleDisposableSubscriber, org.reactivestreams.Subscriber
    public void onError(Throwable th) {
        DefaultLogger.e("SimpleDisposableSubscriber", th);
        dismissDialog();
    }

    @Override // com.miui.gallery.util.SimpleDisposableSubscriber, org.reactivestreams.Subscriber
    public void onComplete() {
        dismissDialog();
    }

    public void dismissDialog() {
        ProgressDialog progressDialog = this.mDialog;
        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }
        this.mDialog.dismiss();
    }
}
