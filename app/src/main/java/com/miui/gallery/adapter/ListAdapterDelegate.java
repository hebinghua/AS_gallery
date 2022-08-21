package com.miui.gallery.adapter;

import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.data.ClusteredList;
import com.miui.gallery.provider.cache.IRecord;
import com.miui.gallery.provider.cache.ISection;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.recyclerview.AsyncListDiffer;
import com.miui.gallery.widget.recyclerview.DiffUtil;
import com.miui.gallery.widget.recyclerview.DiffableItemKt;
import com.miui.gallery.widget.recyclerview.ListUpdateCallback;
import com.miui.os.Rom;
import java.util.List;
import java.util.Objects;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ListAdapterDelegate.kt */
/* loaded from: classes.dex */
public final class ListAdapterDelegate implements IListAdapter<IRecord>, ListUpdateCallback {
    public static final Companion Companion = new Companion(null);
    public static final boolean DEBUGGABLE = Rom.IS_DEV;
    public final DiffUtil.ItemCallback<IRecord> diffCallback;
    public final Lazy differ$delegate;
    public final RecyclerView.Adapter<?> host;

    public ListAdapterDelegate(RecyclerView.Adapter<?> host) {
        Intrinsics.checkNotNullParameter(host, "host");
        this.host = host;
        this.diffCallback = DiffableItemKt.itemDiffCallback();
        this.differ$delegate = LazyKt__LazyJVMKt.lazy(new ListAdapterDelegate$differ$2(this));
    }

    public final AsyncListDiffer<IRecord> getDiffer() {
        return (AsyncListDiffer) this.differ$delegate.mo119getValue();
    }

    public long getItemId(int i) {
        return getItem(i).getId();
    }

    public int getItemCount() {
        List<IRecord> currentList = getDiffer().getCurrentList();
        if (currentList instanceof ClusteredList) {
            return ((ClusteredList) currentList).getContentCount();
        }
        return currentList.size();
    }

    public IRecord getItem(int i) {
        List<IRecord> currentList = getDiffer().getCurrentList();
        if (currentList instanceof ClusteredList) {
            Object rawGetContent = ((ClusteredList) currentList).rawGetContent(i);
            Objects.requireNonNull(rawGetContent, "null cannot be cast to non-null type com.miui.gallery.provider.cache.IRecord");
            return (IRecord) rawGetContent;
        }
        return currentList.get(i);
    }

    public final ISection getGroup(int i) {
        List<IRecord> currentList = getDiffer().getCurrentList();
        if (currentList instanceof ClusteredList) {
            Object rawGetSection = ((ClusteredList) currentList).rawGetSection(i);
            Objects.requireNonNull(rawGetSection, "null cannot be cast to non-null type com.miui.gallery.provider.cache.ISection");
            return (ISection) rawGetSection;
        }
        int i2 = 0;
        for (IRecord iRecord : currentList) {
            if (iRecord instanceof ISection) {
                if (i2 == i) {
                    return (ISection) iRecord;
                }
                i2++;
            }
        }
        return null;
    }

    @Override // com.miui.gallery.adapter.IListAdapter
    public Object submitList(List<? extends IRecord> list, boolean z, Runnable runnable, Continuation<? super Unit> continuation) {
        Object submitList$default = AsyncListDiffer.submitList$default(getDiffer(), list, z, 0L, runnable, continuation, 4, null);
        return submitList$default == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() ? submitList$default : Unit.INSTANCE;
    }

    public List<IRecord> getCurrentList() {
        return getDiffer().getCurrentList();
    }

    @Override // com.miui.gallery.widget.recyclerview.ListUpdateCallback
    public void onDataSetChanged() {
        if (DEBUGGABLE) {
            DefaultLogger.d("ListAdapterDelegate", "onDataSetChanged");
        }
        this.host.notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.ListUpdateCallback
    public void onInserted(int i, int i2) {
        if (DEBUGGABLE) {
            DefaultLogger.d("ListAdapterDelegate", "onInserted, pos: " + i + ", count: " + i2);
        }
        this.host.notifyItemRangeInserted(i, i2);
    }

    @Override // androidx.recyclerview.widget.ListUpdateCallback
    public void onRemoved(int i, int i2) {
        if (DEBUGGABLE) {
            DefaultLogger.d("ListAdapterDelegate", "onRemoved, pos: " + i + ", count: " + i2);
        }
        this.host.notifyItemRangeRemoved(i, i2);
    }

    @Override // androidx.recyclerview.widget.ListUpdateCallback
    public void onMoved(int i, int i2) {
        if (DEBUGGABLE) {
            DefaultLogger.d("ListAdapterDelegate", "onMoved, from " + i + " to " + i2);
        }
        this.host.notifyItemMoved(i, i2);
    }

    @Override // androidx.recyclerview.widget.ListUpdateCallback
    public void onChanged(int i, int i2, Object obj) {
        if (DEBUGGABLE) {
            DefaultLogger.d("ListAdapterDelegate", "onChanged, pos: " + i + ", count: " + i2);
        }
        this.host.notifyItemRangeChanged(i, i2, obj);
    }

    /* compiled from: ListAdapterDelegate.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
