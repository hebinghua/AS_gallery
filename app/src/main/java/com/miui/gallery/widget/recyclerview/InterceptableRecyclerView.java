package com.miui.gallery.widget.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.widget.recyclerview.ItemClickSupport;

/* loaded from: classes3.dex */
public class InterceptableRecyclerView extends GalleryRecyclerView {
    public ClickEventInterceptCallback mClickEventInterceptCallback;
    public ItemClickSupport.OnItemLongClickListener mDelegatedLongClickListener;

    public InterceptableRecyclerView(Context context) {
        this(context, null);
    }

    public InterceptableRecyclerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public InterceptableRecyclerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        super.setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() { // from class: com.miui.gallery.widget.recyclerview.InterceptableRecyclerView$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.widget.recyclerview.ItemClickSupport.OnItemLongClickListener
            public final boolean onItemLongClick(RecyclerView recyclerView, View view, int i2, long j, float f, float f2, boolean z) {
                boolean lambda$new$0;
                lambda$new$0 = InterceptableRecyclerView.this.lambda$new$0(recyclerView, view, i2, j, f, f2, z);
                return lambda$new$0;
            }
        });
        ItemClickSupport.from(this).setTakeOverUnhandledLongPress(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$0(RecyclerView recyclerView, View view, int i, long j, float f, float f2, boolean z) {
        ClickEventInterceptCallback clickEventInterceptCallback = this.mClickEventInterceptCallback;
        if (clickEventInterceptCallback != null) {
            int onInterceptLongClick = clickEventInterceptCallback.onInterceptLongClick(recyclerView, view, i, j, f, f2);
            if (onInterceptLongClick == 0) {
                return true;
            }
            if (onInterceptLongClick == 1) {
                return false;
            }
        }
        ItemClickSupport.OnItemLongClickListener onItemLongClickListener = this.mDelegatedLongClickListener;
        if (onItemLongClickListener != null) {
            return onItemLongClickListener.onItemLongClick(recyclerView, view, i, j, f, f2, z);
        }
        return false;
    }

    @Override // com.miui.gallery.widget.recyclerview.GalleryRecyclerView
    public final void setOnItemLongClickListener(ItemClickSupport.OnItemLongClickListener onItemLongClickListener) {
        this.mDelegatedLongClickListener = onItemLongClickListener;
    }

    public final void setClickEventInterceptCallback(ClickEventInterceptCallback clickEventInterceptCallback) {
        this.mClickEventInterceptCallback = clickEventInterceptCallback;
    }
}
