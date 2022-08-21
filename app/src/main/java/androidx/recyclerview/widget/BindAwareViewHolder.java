package androidx.recyclerview.widget;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes.dex */
public abstract class BindAwareViewHolder extends RecyclerView.ViewHolder {
    public void onBind() {
    }

    public void onUnbind() {
    }

    public BindAwareViewHolder(View view) {
        super(view);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ViewHolder
    public boolean isBound() {
        return super.isBound();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ViewHolder
    public void setFlags(int i, int i2) {
        boolean isBound = isBound();
        super.setFlags(i, i2);
        notifyBinding(isBound, isBound());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ViewHolder
    public void addFlags(int i) {
        boolean isBound = isBound();
        super.addFlags(i);
        notifyBinding(isBound, isBound());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ViewHolder
    public void clearPayload() {
        boolean isBound = isBound();
        super.clearPayload();
        notifyBinding(isBound, isBound());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ViewHolder
    public void resetInternal() {
        boolean isBound = isBound();
        super.resetInternal();
        notifyBinding(isBound, isBound());
    }

    public final void notifyBinding(boolean z, boolean z2) {
        if (z && !z2) {
            onUnbind();
        } else if (z || !z2) {
        } else {
            onBind();
        }
    }
}
