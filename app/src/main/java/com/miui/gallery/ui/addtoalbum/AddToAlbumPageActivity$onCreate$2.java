package com.miui.gallery.ui.addtoalbum;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.epoxy.EpoxyAdapter;
import com.miui.epoxy.eventhook.EventHook;
import com.miui.gallery.ui.album.common.RecyclerViewItemModel;
import com.miui.gallery.widget.recyclerview.BasicRecyclerView;
import com.miui.gallery.widget.recyclerview.ItemClickSupport;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: AddToAlbumPageActivity.kt */
/* loaded from: classes2.dex */
public final class AddToAlbumPageActivity$onCreate$2 extends EventHook<RecyclerViewItemModel.VH> {
    public final /* synthetic */ AddToAlbumPageActivity this$0;

    public static /* synthetic */ boolean $r8$lambda$25ADDbm8ta0kq11S5IRXUXQzDWY(AddToAlbumPageActivity addToAlbumPageActivity, RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
        return m1589onEvent$lambda0(addToAlbumPageActivity, recyclerView, view, i, j, f, f2);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AddToAlbumPageActivity$onCreate$2(AddToAlbumPageActivity addToAlbumPageActivity, Class<RecyclerViewItemModel.VH> cls) {
        super(cls);
        this.this$0 = addToAlbumPageActivity;
    }

    @Override // com.miui.epoxy.eventhook.EventHook
    public /* bridge */ /* synthetic */ void onEvent(View view, RecyclerViewItemModel.VH vh, EpoxyAdapter epoxyAdapter) {
        onEvent2(view, vh, (EpoxyAdapter<?>) epoxyAdapter);
    }

    /* renamed from: onEvent */
    public void onEvent2(View view, RecyclerViewItemModel.VH viewHolder, EpoxyAdapter<?> adapter) {
        Intrinsics.checkNotNullParameter(view, "view");
        Intrinsics.checkNotNullParameter(viewHolder, "viewHolder");
        Intrinsics.checkNotNullParameter(adapter, "adapter");
        if (view instanceof BasicRecyclerView) {
            final AddToAlbumPageActivity addToAlbumPageActivity = this.this$0;
            ((BasicRecyclerView) view).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() { // from class: com.miui.gallery.ui.addtoalbum.AddToAlbumPageActivity$onCreate$2$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.widget.recyclerview.ItemClickSupport.OnItemClickListener
                public final boolean onItemClick(RecyclerView recyclerView, View view2, int i, long j, float f, float f2) {
                    return AddToAlbumPageActivity$onCreate$2.$r8$lambda$25ADDbm8ta0kq11S5IRXUXQzDWY(AddToAlbumPageActivity.this, recyclerView, view2, i, j, f, f2);
                }
            });
        }
    }

    /* renamed from: onEvent$lambda-0 */
    public static final boolean m1589onEvent$lambda0(AddToAlbumPageActivity this$0, RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.onItemClick(i, j);
        return true;
    }

    @Override // com.miui.epoxy.eventhook.EventHook
    public View onBind(RecyclerViewItemModel.VH viewHolder) {
        Intrinsics.checkNotNullParameter(viewHolder, "viewHolder");
        return viewHolder.getRecyclerView();
    }
}
