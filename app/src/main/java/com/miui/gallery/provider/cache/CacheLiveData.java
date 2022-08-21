package com.miui.gallery.provider.cache;

import android.content.ContentProvider;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.core.content.ContentResolverCompat;
import androidx.core.os.CancellationSignal;
import com.miui.gallery.arch.livedata.ComputableLiveData;
import com.miui.gallery.provider.GalleryProvider;
import com.miui.gallery.provider.cache.CacheItem;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.List;
import java.util.concurrent.Executor;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsJVMKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.io.CloseableKt;
import kotlin.jdk7.AutoCloseableKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.MutablePropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.properties.Delegates;
import kotlin.properties.ObservableProperty;
import kotlin.properties.ReadWriteProperty;
import kotlin.reflect.KProperty;

/* compiled from: CacheLiveData.kt */
/* loaded from: classes2.dex */
public class CacheLiveData<T extends CacheItem, R> extends ComputableLiveData<List<? extends R>> {
    public static final /* synthetic */ KProperty<Object>[] $$delegatedProperties = {Reflection.mutableProperty1(new MutablePropertyReference1Impl(CacheLiveData.class, "visibleToUser", "getVisibleToUser()Z", 0))};
    public static final Companion Companion = new Companion(null);
    public CancellationSignal cancellationSignal;
    public ComputableLiveData.SelfContentObserver contentObserver;
    public boolean contentObserverRegistered;
    public final Object observerLock;
    public final IMediaProcessor<T, R> processor;
    public String[] projection;
    public final ContentResolver resolver;
    public String selection;
    public String[] selectionArgs;
    public String sortOrder;
    public Uri uri;
    public final ReadWriteProperty visibleToUser$delegate;

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public CacheLiveData(Context context, Uri uri, String[] strArr, String str, String[] strArr2, String str2, IMediaProcessor<? super T, R> processor) {
        this(context, uri, strArr, str, strArr2, str2, null, processor, 64, null);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(processor, "processor");
    }

    /* renamed from: access$onActive$s-455722178 */
    public static final /* synthetic */ void m1204access$onActive$s455722178(CacheLiveData cacheLiveData) {
        super.onActive();
    }

    public final String getSelection() {
        return this.selection;
    }

    public final void setSelection(String str) {
        this.selection = str;
    }

    public final String[] getSelectionArgs() {
        return this.selectionArgs;
    }

    public final String getSortOrder() {
        return this.sortOrder;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public /* synthetic */ CacheLiveData(android.content.Context r12, android.net.Uri r13, java.lang.String[] r14, java.lang.String r15, java.lang.String[] r16, java.lang.String r17, java.util.concurrent.Executor r18, com.miui.gallery.provider.cache.IMediaProcessor r19, int r20, kotlin.jvm.internal.DefaultConstructorMarker r21) {
        /*
            r11 = this;
            r0 = r20 & 2
            r1 = 0
            if (r0 == 0) goto L7
            r4 = r1
            goto L8
        L7:
            r4 = r13
        L8:
            r0 = r20 & 4
            if (r0 == 0) goto Le
            r5 = r1
            goto Lf
        Le:
            r5 = r14
        Lf:
            r0 = r20 & 8
            if (r0 == 0) goto L15
            r6 = r1
            goto L16
        L15:
            r6 = r15
        L16:
            r0 = r20 & 16
            if (r0 == 0) goto L1c
            r7 = r1
            goto L1e
        L1c:
            r7 = r16
        L1e:
            r0 = r20 & 32
            if (r0 == 0) goto L24
            r8 = r1
            goto L26
        L24:
            r8 = r17
        L26:
            r0 = r20 & 64
            if (r0 == 0) goto L33
            java.util.concurrent.Executor r0 = android.os.AsyncTask.THREAD_POOL_EXECUTOR
            java.lang.String r1 = "THREAD_POOL_EXECUTOR"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, r1)
            r9 = r0
            goto L35
        L33:
            r9 = r18
        L35:
            r2 = r11
            r3 = r12
            r10 = r19
            r2.<init>(r3, r4, r5, r6, r7, r8, r9, r10)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.cache.CacheLiveData.<init>(android.content.Context, android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String, java.util.concurrent.Executor, com.miui.gallery.provider.cache.IMediaProcessor, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public CacheLiveData(Context context, Uri uri, String[] strArr, String str, String[] strArr2, String str2, Executor executor, IMediaProcessor<? super T, R> processor) {
        super(executor);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(executor, "executor");
        Intrinsics.checkNotNullParameter(processor, "processor");
        this.uri = uri;
        this.projection = strArr;
        this.selection = str;
        this.selectionArgs = strArr2;
        this.sortOrder = str2;
        this.processor = processor;
        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
        Intrinsics.checkNotNullExpressionValue(contentResolver, "context.applicationContext.contentResolver");
        this.resolver = contentResolver;
        this.observerLock = new Object();
        Delegates delegates = Delegates.INSTANCE;
        this.visibleToUser$delegate = new ObservableProperty<Boolean>(Boolean.TRUE, this) { // from class: com.miui.gallery.provider.cache.CacheLiveData$special$$inlined$observable$1
            public final /* synthetic */ Object $initialValue;
            public final /* synthetic */ CacheLiveData this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(r1);
                this.$initialValue = r1;
                this.this$0 = this;
            }

            @Override // kotlin.properties.ObservableProperty
            public void afterChange(KProperty<?> property, Boolean bool, Boolean bool2) {
                Intrinsics.checkNotNullParameter(property, "property");
                boolean booleanValue = bool2.booleanValue();
                if (bool.booleanValue() == booleanValue || !booleanValue || !this.this$0.isInvalid()) {
                    return;
                }
                CacheLiveData.m1204access$onActive$s455722178(this.this$0);
            }
        };
    }

    public final ComputableLiveData.SelfContentObserver getContentObserver() {
        return this.contentObserver;
    }

    public final void setContentObserver(ComputableLiveData.SelfContentObserver selfContentObserver) {
        this.contentObserver = selfContentObserver;
    }

    public final boolean getVisibleToUser() {
        return ((Boolean) this.visibleToUser$delegate.getValue(this, $$delegatedProperties[0])).booleanValue();
    }

    public final void setVisibleToUser(boolean z) {
        this.visibleToUser$delegate.setValue(this, $$delegatedProperties[0], Boolean.valueOf(z));
    }

    @Override // com.miui.gallery.arch.livedata.ComputableLiveData
    public boolean isActiveForRefresh() {
        return getVisibleToUser() && super.isActiveForRefresh();
    }

    public final List<R> queryWithLocalProvider(Uri uri) {
        List<R> queryCachedItem;
        ContentProviderClient acquireContentProviderClient = this.resolver.acquireContentProviderClient("com.miui.gallery.cloud.provider");
        if (acquireContentProviderClient == null) {
            return null;
        }
        try {
            ContentProvider localContentProvider = acquireContentProviderClient.getLocalContentProvider();
            if (!(localContentProvider instanceof GalleryProvider)) {
                Log.w("CacheLiveData", "Acquired provider isn't " + ((Object) Reflection.getOrCreateKotlinClass(GalleryProvider.class).getSimpleName()) + ": " + localContentProvider);
                queryCachedItem = null;
            } else {
                registerContentObserver(((GalleryProvider) localContentProvider).getNotifyUris(uri));
                queryCachedItem = ((GalleryProvider) localContentProvider).queryCachedItem(uri, getSelection(), getSelectionArgs(), getSortOrder(), this.processor);
                if (queryCachedItem == null) {
                    queryCachedItem = CollectionsKt__CollectionsKt.emptyList();
                }
            }
            AutoCloseableKt.closeFinally(acquireContentProviderClient, null);
            return queryCachedItem;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                AutoCloseableKt.closeFinally(acquireContentProviderClient, th);
                throw th2;
            }
        }
    }

    public final List<R> queryWithCursor(Uri uri) {
        List<Uri> listOf;
        Cursor query = ContentResolverCompat.query(this.resolver, uri, null, this.selection, this.selectionArgs, this.sortOrder, this.cancellationSignal);
        List<R> list = null;
        if (query != null) {
            try {
                if (Build.VERSION.SDK_INT >= 29) {
                    listOf = query.getNotificationUris();
                } else {
                    listOf = CollectionsKt__CollectionsJVMKt.listOf(query.getNotificationUri());
                }
                registerContentObserver(listOf);
                List<R> processCursor = this.processor.processCursor(query);
                CloseableKt.closeFinally(query, null);
                list = processCursor;
            } catch (Throwable th) {
                try {
                    throw th;
                } catch (Throwable th2) {
                    CloseableKt.closeFinally(query, th);
                    throw th2;
                }
            }
        }
        return list == null ? CollectionsKt__CollectionsKt.emptyList() : list;
    }

    public static /* synthetic */ void updateQueryArgs$default(CacheLiveData cacheLiveData, Uri uri, String str, String[] strArr, String str2, boolean z, int i, Object obj) {
        if (obj == null) {
            if ((i & 16) != 0) {
                z = false;
            }
            cacheLiveData.updateQueryArgs(uri, str, strArr, str2, z);
            return;
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: updateQueryArgs");
    }

    public final void updateQueryArgs(Uri uri, String str, String[] strArr, String str2, boolean z) {
        Intrinsics.checkNotNullParameter(uri, "uri");
        this.uri = uri;
        this.selection = str;
        this.selectionArgs = strArr;
        this.sortOrder = str2;
        if (z) {
            invalidate();
        }
    }

    @Override // com.miui.gallery.arch.livedata.ComputableLiveData
    /* renamed from: compute */
    public List<R> mo1205compute() {
        Uri uri = this.uri;
        if (uri == null) {
            unregisterContentObserver();
            return CollectionsKt__CollectionsKt.emptyList();
        }
        synchronized (this) {
            this.cancellationSignal = new CancellationSignal();
            Unit unit = Unit.INSTANCE;
        }
        long currentTimeMillis = System.currentTimeMillis();
        try {
            try {
                List<R> queryWithLocalProvider = queryWithLocalProvider(uri);
                if (queryWithLocalProvider == null) {
                    queryWithLocalProvider = queryWithCursor(uri);
                }
                DefaultLogger.d("CacheLiveData", Intrinsics.stringPlus("compute costs: ", Long.valueOf(System.currentTimeMillis() - currentTimeMillis)));
                synchronized (this) {
                    this.cancellationSignal = null;
                }
                return queryWithLocalProvider;
            } catch (Exception e) {
                DefaultLogger.e("CacheLiveData", e);
                List<R> emptyList = CollectionsKt__CollectionsKt.emptyList();
                DefaultLogger.d("CacheLiveData", Intrinsics.stringPlus("compute costs: ", Long.valueOf(System.currentTimeMillis() - currentTimeMillis)));
                synchronized (this) {
                    this.cancellationSignal = null;
                    Unit unit2 = Unit.INSTANCE;
                    return emptyList;
                }
            }
        } catch (Throwable th) {
            DefaultLogger.d("CacheLiveData", Intrinsics.stringPlus("compute costs: ", Long.valueOf(System.currentTimeMillis() - currentTimeMillis)));
            synchronized (this) {
                this.cancellationSignal = null;
                Unit unit3 = Unit.INSTANCE;
                throw th;
            }
        }
    }

    public void unregisterContentObserver() {
        synchronized (this.observerLock) {
            if (getContentObserver() != null && this.contentObserverRegistered) {
                ContentResolver contentResolver = this.resolver;
                ComputableLiveData.SelfContentObserver contentObserver = getContentObserver();
                Intrinsics.checkNotNull(contentObserver);
                contentResolver.unregisterContentObserver(contentObserver);
                this.contentObserverRegistered = false;
                Log.d("CacheLiveData", "unregisterContentObserver");
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    public void registerContentObserver(List<? extends Uri> list) {
        synchronized (this.observerLock) {
            boolean z = false;
            if (getContentObserver() != null) {
                ContentResolver contentResolver = this.resolver;
                ComputableLiveData.SelfContentObserver contentObserver = getContentObserver();
                Intrinsics.checkNotNull(contentObserver);
                contentResolver.unregisterContentObserver(contentObserver);
                this.contentObserverRegistered = false;
            }
            if (list == null || list.isEmpty()) {
                z = true;
            }
            if (!z) {
                setContentObserver(new ComputableLiveData.SelfContentObserver(this, this.resolver));
                Intrinsics.checkNotNull(list);
                for (Uri uri : list) {
                    ContentResolver contentResolver2 = this.resolver;
                    ComputableLiveData.SelfContentObserver contentObserver2 = getContentObserver();
                    Intrinsics.checkNotNull(contentObserver2);
                    contentResolver2.registerContentObserver(uri, true, contentObserver2);
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

    /* compiled from: CacheLiveData.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
