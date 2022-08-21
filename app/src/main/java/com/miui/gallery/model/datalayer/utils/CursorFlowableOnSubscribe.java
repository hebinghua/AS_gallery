package com.miui.gallery.model.datalayer.utils;

import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import com.miui.gallery.loader.CursorConvertCallback;
import com.miui.gallery.ui.album.NoRepeatContentObserver;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Cancellable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes2.dex */
public abstract class CursorFlowableOnSubscribe<T> implements FlowableOnSubscribe<T> {
    public final Runnable changeRunnable;
    public AtomicBoolean isRegisted;
    public FlowableEmitter<T> mEmitter;
    public final ContentObserver mObserver;

    public abstract Cursor getCursor();

    public CursorConvertCallback<T> getCursorConvert(Cursor cursor) {
        return null;
    }

    public abstract void subscribe(Cursor cursor, FlowableEmitter<T> flowableEmitter);

    public CursorFlowableOnSubscribe() {
        this(220L);
    }

    public CursorFlowableOnSubscribe(long j) {
        this.isRegisted = new AtomicBoolean(false);
        this.changeRunnable = new Runnable() { // from class: com.miui.gallery.model.datalayer.utils.CursorFlowableOnSubscribe.2
            @Override // java.lang.Runnable
            public void run() {
                CursorFlowableOnSubscribe cursorFlowableOnSubscribe = CursorFlowableOnSubscribe.this;
                cursorFlowableOnSubscribe.subscribe(cursorFlowableOnSubscribe.mEmitter);
            }
        };
        this.mObserver = new NoRepeatContentObserver(ThreadManager.getMainHandler(), j, TimeUnit.MILLISECONDS) { // from class: com.miui.gallery.model.datalayer.utils.CursorFlowableOnSubscribe.1
            @Override // com.miui.gallery.ui.album.NoRepeatContentObserver
            public void onDataChange(boolean z, Uri uri) {
                com.miui.gallery.util.thread.ThreadManager.execute(31, CursorFlowableOnSubscribe.this.changeRunnable);
            }
        };
    }

    public void bindContentObserver(Cursor cursor) {
        if (cursor == null || this.isRegisted.get() || cursor.getNotificationUri() == null) {
            return;
        }
        ContentResolverUtils.registerContentObserver(cursor.getNotificationUri(), true, this.mObserver);
        this.mEmitter.setCancellable(new Cancellable() { // from class: com.miui.gallery.model.datalayer.utils.CursorFlowableOnSubscribe.3
            @Override // io.reactivex.functions.Cancellable
            public void cancel() throws Exception {
                ContentResolverUtils.unRegisterContentObserver(CursorFlowableOnSubscribe.this.mObserver);
            }
        });
        this.isRegisted.set(true);
    }

    @Override // io.reactivex.FlowableOnSubscribe
    public final void subscribe(FlowableEmitter<T> flowableEmitter) {
        Cursor cursor = null;
        try {
            try {
                cursor = getCursor();
                this.mEmitter = flowableEmitter;
                CursorConvertCallback<T> cursorConvert = getCursorConvert(cursor);
                if (cursorConvert != null) {
                    this.mEmitter.onNext(cursorConvert.mo1129convert(cursor));
                } else {
                    subscribe(cursor, flowableEmitter);
                }
                bindContentObserver(cursor);
            } catch (Exception e) {
                this.mEmitter.onError(e);
            }
        } finally {
            BaseMiscUtil.closeSilently(cursor);
        }
    }
}
