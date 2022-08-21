package androidx.recyclerview.widget;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import miuix.animation.Folme;

/* loaded from: classes.dex */
public class SpringItemTouchHelper extends ItemTouchHelper {
    public boolean mSpringEnabled;

    public SpringItemTouchHelper(ItemTouchHelper.Callback callback) {
        super(callback);
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper
    public void select(RecyclerView.ViewHolder viewHolder, int i) {
        RecyclerView.ViewHolder viewHolder2;
        if (i == 2) {
            if (viewHolder != null) {
                Folme.setDraggingState(viewHolder.itemView, true);
            }
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView instanceof miuix.recyclerview.widget.RecyclerView) {
                this.mSpringEnabled = ((miuix.recyclerview.widget.RecyclerView) recyclerView).getSpringEnabled();
                ((miuix.recyclerview.widget.RecyclerView) this.mRecyclerView).setSpringEnabled(false);
            }
        } else if (i == 0 && (viewHolder2 = this.mSelected) != null) {
            Folme.setDraggingState(viewHolder2.itemView, false);
            RecyclerView recyclerView2 = this.mRecyclerView;
            if (recyclerView2 instanceof miuix.recyclerview.widget.RecyclerView) {
                ((miuix.recyclerview.widget.RecyclerView) recyclerView2).setSpringEnabled(this.mSpringEnabled);
            }
        }
        super.select(viewHolder, i);
    }
}
