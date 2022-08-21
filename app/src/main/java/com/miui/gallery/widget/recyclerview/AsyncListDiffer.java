package com.miui.gallery.widget.recyclerview;

import com.miui.gallery.widget.recyclerview.AsyncListDiffer;
import com.miui.gallery.widget.recyclerview.DiffUtil;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.MutablePropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.properties.Delegates;
import kotlin.properties.ObservableProperty;
import kotlin.properties.ReadWriteProperty;
import kotlin.reflect.KProperty;

/* compiled from: AsyncListDiffer.kt */
/* loaded from: classes3.dex */
public final class AsyncListDiffer<T> {
    public static final /* synthetic */ KProperty<Object>[] $$delegatedProperties = {Reflection.mutableProperty1(new MutablePropertyReference1Impl(AsyncListDiffer.class, "_currentList", "get_currentList()Ljava/util/List;", 0))};
    public static final Companion Companion = new Companion(null);
    public final ReadWriteProperty _currentList$delegate;
    public final AsyncDifferConfig<T> config;
    public List<? extends T> currentList;
    public List<? extends T> list;
    public final List<ListListener<T>> listeners;
    public int maxScheduledGeneration;
    public final ListUpdateCallback updateCallback;

    /* compiled from: AsyncListDiffer.kt */
    /* loaded from: classes3.dex */
    public interface ListListener<T> {
        void onCurrentListChanged(List<? extends T> list, List<? extends T> list2);
    }

    public AsyncListDiffer(ListUpdateCallback updateCallback, AsyncDifferConfig<T> config) {
        Intrinsics.checkNotNullParameter(updateCallback, "updateCallback");
        Intrinsics.checkNotNullParameter(config, "config");
        this.updateCallback = updateCallback;
        this.config = config;
        this.listeners = new CopyOnWriteArrayList();
        Delegates delegates = Delegates.INSTANCE;
        this._currentList$delegate = new ObservableProperty<List<? extends T>>(CollectionsKt__CollectionsKt.emptyList(), this) { // from class: com.miui.gallery.widget.recyclerview.AsyncListDiffer$special$$inlined$observable$1
            public final /* synthetic */ Object $initialValue;
            public final /* synthetic */ AsyncListDiffer this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(r1);
                this.$initialValue = r1;
                this.this$0 = this;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // kotlin.properties.ObservableProperty
            public void afterChange(KProperty<?> property, List<? extends T> list, List<? extends T> list2) {
                Intrinsics.checkNotNullParameter(property, "property");
                List<? extends T> list3 = list2;
                List<? extends T> list4 = list;
                AsyncListDiffer.access$setCurrentList$p(this.this$0, list3);
                for (AsyncListDiffer.ListListener listListener : AsyncListDiffer.access$getListeners$p(this.this$0)) {
                    listListener.onCurrentListChanged(list4, list3);
                }
            }
        };
        this.currentList = CollectionsKt__CollectionsKt.emptyList();
    }

    public static final /* synthetic */ List access$getListeners$p(AsyncListDiffer asyncListDiffer) {
        return asyncListDiffer.listeners;
    }

    public static final /* synthetic */ void access$setCurrentList$p(AsyncListDiffer asyncListDiffer, List list) {
        asyncListDiffer.currentList = list;
    }

    public final AsyncDifferConfig<T> getConfig() {
        return this.config;
    }

    public final void set_currentList(List<? extends T> list) {
        this._currentList$delegate.setValue(this, $$delegatedProperties[0], list);
    }

    public final List<T> getCurrentList() {
        return (List<? extends T>) this.currentList;
    }

    public static /* synthetic */ Object submitList$default(AsyncListDiffer asyncListDiffer, List list, boolean z, long j, Runnable runnable, Continuation continuation, int i, Object obj) {
        if ((i & 2) != 0) {
            z = true;
        }
        boolean z2 = z;
        if ((i & 4) != 0) {
            j = 500;
        }
        long j2 = j;
        if ((i & 8) != 0) {
            runnable = null;
        }
        return asyncListDiffer.submitList(list, z2, j2, runnable, continuation);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(12:1|(2:3|(9:5|6|(1:(4:9|10|11|12)(2:36|37))(2:38|(3:(1:41)|42|43)(1:(6:45|(1:47)(1:53)|48|(1:50)|51|52)(2:54|(4:56|(1:58)|59|60)(1:(4:62|(1:64)|65|66)(4:67|68|69|(1:71)(1:72))))))|13|14|15|(3:17|(1:19)|20)(2:24|(2:26|(1:28)))|21|22))|76|6|(0)(0)|13|14|15|(0)(0)|21|22|(1:(0))) */
    /* JADX WARN: Code restructure failed: missing block: B:143:0x00f5, code lost:
        r0 = th;
     */
    /* JADX WARN: Removed duplicated region for block: B:103:0x0053  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x010f  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x013e  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x002b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final java.lang.Object submitList(java.util.List<? extends T> r16, boolean r17, long r18, java.lang.Runnable r20, kotlin.coroutines.Continuation<? super kotlin.Unit> r21) {
        /*
            Method dump skipped, instructions count: 396
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.widget.recyclerview.AsyncListDiffer.submitList(java.util.List, boolean, long, java.lang.Runnable, kotlin.coroutines.Continuation):java.lang.Object");
    }

    public final void latchList(List<? extends T> list, DiffUtil.DiffResult diffResult, Runnable runnable) {
        this.list = list;
        Intrinsics.checkNotNull(list);
        set_currentList(list);
        diffResult.dispatchUpdatesTo(this.updateCallback);
        if (runnable == null) {
            return;
        }
        runnable.run();
    }

    public final void addListListener(ListListener<T> listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.listeners.add(listener);
    }

    /* compiled from: AsyncListDiffer.kt */
    /* loaded from: classes3.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
