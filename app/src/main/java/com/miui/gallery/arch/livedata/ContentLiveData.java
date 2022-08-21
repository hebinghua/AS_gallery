package com.miui.gallery.arch.livedata;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.core.content.ContentResolverCompat;
import androidx.core.os.CancellationSignal;
import com.miui.gallery.arch.livedata.ComputableLiveData;
import com.xiaomi.stat.d;
import java.util.List;
import java.util.concurrent.Executor;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsJVMKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ContentLiveData.kt */
/* loaded from: classes.dex */
public final class ContentLiveData<T> extends ComputableLiveData<List<? extends T>> {
    public static final Companion Companion = new Companion(null);
    public CancellationSignal cancellationSignal;
    public ComputableLiveData.SelfContentObserver contentObserver;
    public boolean contentObserverRegistered;
    public final ContentResolver contentResolver;
    public final Function1<Cursor, List<T>> converter;
    public final Object observerLock;
    public String[] projection;
    public String selection;
    public String[] selectionArgs;
    public String sortOrder;
    public Uri uri;

    public final void setSelection(String str) {
        this.selection = str;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public ContentLiveData(Context context, Uri uri, String[] strArr, String str, String[] strArr2, String str2, Executor executor, Function1<? super Cursor, ? extends List<? extends T>> converter) {
        super(executor);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(uri, "uri");
        Intrinsics.checkNotNullParameter(executor, "executor");
        Intrinsics.checkNotNullParameter(converter, "converter");
        this.uri = uri;
        this.projection = strArr;
        this.selection = str;
        this.selectionArgs = strArr2;
        this.sortOrder = str2;
        this.converter = converter;
        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
        Intrinsics.checkNotNullExpressionValue(contentResolver, "context.applicationContext.contentResolver");
        this.contentResolver = contentResolver;
        this.observerLock = new Object();
    }

    @Override // com.miui.gallery.arch.livedata.ComputableLiveData
    /* renamed from: compute  reason: collision with other method in class */
    public List<T> mo1205compute() {
        List<T> emptyList;
        List<Uri> listOf;
        synchronized (this) {
            this.cancellationSignal = new CancellationSignal();
            Unit unit = Unit.INSTANCE;
        }
        long currentTimeMillis = System.currentTimeMillis();
        try {
            try {
                Cursor query = ContentResolverCompat.query(this.contentResolver, this.uri, this.projection, this.selection, this.selectionArgs, this.sortOrder, this.cancellationSignal);
                if (query == null) {
                    emptyList = null;
                } else {
                    try {
                        if (Build.VERSION.SDK_INT >= 29) {
                            listOf = query.getNotificationUris();
                        } else {
                            listOf = CollectionsKt__CollectionsJVMKt.listOf(query.getNotificationUri());
                        }
                        registerContentObserver(listOf);
                        long currentTimeMillis2 = System.currentTimeMillis();
                        List<T> mo2577invoke = this.converter.mo2577invoke(query);
                        List<T> list = mo2577invoke;
                        Log.d("ContentLiveData", "convert cursor costs " + (System.currentTimeMillis() - currentTimeMillis2) + d.H);
                        emptyList = mo2577invoke;
                        CloseableKt.closeFinally(query, null);
                    } finally {
                    }
                }
                if (emptyList == null) {
                    emptyList = CollectionsKt__CollectionsKt.emptyList();
                }
                Log.d("ContentLiveData", "compute costs " + (System.currentTimeMillis() - currentTimeMillis) + d.H);
            } catch (Exception e) {
                Log.e("ContentLiveData", "got exception when compute", e);
                emptyList = CollectionsKt__CollectionsKt.emptyList();
                Log.d("ContentLiveData", "compute costs " + (System.currentTimeMillis() - currentTimeMillis) + d.H);
                synchronized (this) {
                    this.cancellationSignal = null;
                    Unit unit2 = Unit.INSTANCE;
                }
            }
            synchronized (this) {
                this.cancellationSignal = null;
            }
            return emptyList;
        } catch (Throwable th) {
            Log.d("ContentLiveData", "compute costs " + (System.currentTimeMillis() - currentTimeMillis) + d.H);
            synchronized (this) {
                this.cancellationSignal = null;
                Unit unit3 = Unit.INSTANCE;
                throw th;
            }
        }
    }

    public final void unregisterContentObserver() {
        synchronized (this.observerLock) {
            ComputableLiveData.SelfContentObserver selfContentObserver = this.contentObserver;
            if (selfContentObserver != null && this.contentObserverRegistered) {
                ContentResolver contentResolver = this.contentResolver;
                Intrinsics.checkNotNull(selfContentObserver);
                contentResolver.unregisterContentObserver(selfContentObserver);
                this.contentObserverRegistered = false;
                Log.d("ContentLiveData", "unregisterContentObserver");
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    public final void registerContentObserver(List<? extends Uri> list) {
        synchronized (this.observerLock) {
            ComputableLiveData.SelfContentObserver selfContentObserver = this.contentObserver;
            boolean z = false;
            if (selfContentObserver != null) {
                ContentResolver contentResolver = this.contentResolver;
                Intrinsics.checkNotNull(selfContentObserver);
                contentResolver.unregisterContentObserver(selfContentObserver);
                this.contentObserverRegistered = false;
            }
            if (list == null || list.isEmpty()) {
                z = true;
            }
            if (!z) {
                this.contentObserver = new ComputableLiveData.SelfContentObserver(this, this.contentResolver);
                Intrinsics.checkNotNull(list);
                for (Uri uri : list) {
                    ContentResolver contentResolver2 = this.contentResolver;
                    ComputableLiveData.SelfContentObserver selfContentObserver2 = this.contentObserver;
                    Intrinsics.checkNotNull(selfContentObserver2);
                    contentResolver2.registerContentObserver(uri, true, selfContentObserver2);
                }
                this.contentObserverRegistered = true;
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    @Override // com.miui.gallery.arch.livedata.ComputableLiveData, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        super.close();
        unregisterContentObserver();
        synchronized (this) {
            CancellationSignal cancellationSignal = this.cancellationSignal;
            if (cancellationSignal != null) {
                cancellationSignal.cancel();
                Unit unit = Unit.INSTANCE;
            }
        }
    }

    /* compiled from: ContentLiveData.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
