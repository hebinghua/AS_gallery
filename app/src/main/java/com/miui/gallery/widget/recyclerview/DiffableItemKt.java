package com.miui.gallery.widget.recyclerview;

import com.miui.gallery.widget.recyclerview.DiffUtil;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: DiffableItem.kt */
/* loaded from: classes3.dex */
public final class DiffableItemKt {
    public static final <T extends DiffableItem<?>> DiffUtil.ItemCallback<T> itemDiffCallback() {
        return (DiffUtil.ItemCallback<T>) new DiffUtil.ItemCallback<T>() { // from class: com.miui.gallery.widget.recyclerview.DiffableItemKt$itemDiffCallback$1
            /* JADX WARN: Incorrect types in method signature: (TT;TT;)Z */
            @Override // com.miui.gallery.widget.recyclerview.DiffUtil.ItemCallback
            public boolean areItemsTheSame(DiffableItem oldItem, DiffableItem newItem) {
                Intrinsics.checkNotNullParameter(oldItem, "oldItem");
                Intrinsics.checkNotNullParameter(newItem, "newItem");
                return oldItem.genericItemSameAs(newItem);
            }

            /* JADX WARN: Incorrect types in method signature: (TT;TT;)Z */
            @Override // com.miui.gallery.widget.recyclerview.DiffUtil.ItemCallback
            public boolean areContentsTheSame(DiffableItem oldItem, DiffableItem newItem) {
                Intrinsics.checkNotNullParameter(oldItem, "oldItem");
                Intrinsics.checkNotNullParameter(newItem, "newItem");
                return oldItem.genericContentSameAs(newItem);
            }
        };
    }
}
