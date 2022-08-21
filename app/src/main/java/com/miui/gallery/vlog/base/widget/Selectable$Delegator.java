package com.miui.gallery.vlog.base.widget;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public final class Selectable$Delegator {
    public RecyclerView mParent;
    public int mSelection;

    public Selectable$Delegator(int i) {
        this(i, null);
    }

    public Selectable$Delegator(int i, Selectable$Selector selectable$Selector) {
        this.mSelection = i;
    }

    public void setSelection(int i) {
        int i2;
        RecyclerView.ViewHolder findViewHolderForAdapterPosition;
        View view;
        View view2;
        int i3 = this.mSelection;
        if (i3 == i) {
            return;
        }
        boolean z = false;
        if (i3 != -1) {
            RecyclerView.ViewHolder findViewHolderForAdapterPosition2 = this.mParent.findViewHolderForAdapterPosition(i3);
            if (findViewHolderForAdapterPosition2 != null && (view2 = findViewHolderForAdapterPosition2.itemView) != null) {
                view2.setActivated(false);
                findViewHolderForAdapterPosition2.itemView.setSelected(false);
            } else {
                i2 = this.mSelection;
                z = true;
                this.mSelection = i;
                if (i != -1 && (findViewHolderForAdapterPosition = this.mParent.findViewHolderForAdapterPosition(i)) != null && (view = findViewHolderForAdapterPosition.itemView) != null) {
                    view.setActivated(true);
                    findViewHolderForAdapterPosition.itemView.setSelected(true);
                }
                if (!z || i2 == -1) {
                }
                this.mParent.getAdapter().notifyItemChanged(i2);
                return;
            }
        }
        i2 = 1;
        this.mSelection = i;
        if (i != -1) {
            view.setActivated(true);
            findViewHolderForAdapterPosition.itemView.setSelected(true);
        }
        if (!z) {
        }
    }

    public int getSelection() {
        return this.mSelection;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        View view = viewHolder.itemView;
        if (view == null) {
            return;
        }
        boolean z = true;
        view.setActivated(i == this.mSelection);
        View view2 = viewHolder.itemView;
        if (i != this.mSelection) {
            z = false;
        }
        view2.setSelected(z);
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        if (this.mParent != null) {
            throw new IllegalStateException("already attach to a recycler view");
        }
        this.mParent = recyclerView;
    }

    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        if (this.mParent == recyclerView) {
            this.mParent = null;
        } else {
            DefaultLogger.w("Selectable.Delegator", "not the attached parent view .");
        }
    }
}
