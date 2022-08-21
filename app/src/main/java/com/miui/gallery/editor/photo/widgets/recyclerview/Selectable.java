package com.miui.gallery.editor.photo.widgets.recyclerview;

import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public interface Selectable {
    int getSelection();

    /* loaded from: classes2.dex */
    public static final class Delegator implements Selectable {
        public RecyclerView mParent;
        public int mSelection;
        public Selector mSelector;

        public Delegator(int i) {
            this(i, null);
        }

        public Delegator(int i, Selector selector) {
            this.mSelection = i;
            this.mSelector = selector;
            if (selector != null) {
                selector.mAdapter = this;
            }
        }

        public void setSelection(int i) {
            setSelection(i, true);
        }

        /* JADX WARN: Removed duplicated region for block: B:28:0x0049  */
        /* JADX WARN: Removed duplicated region for block: B:30:0x0050 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:33:0x005d  */
        /* JADX WARN: Removed duplicated region for block: B:36:? A[RETURN, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void setSelection(int r7, boolean r8) {
            /*
                r6 = this;
                int r0 = r6.mSelection
                if (r0 == r7) goto L66
                androidx.recyclerview.widget.RecyclerView r1 = r6.mParent
                if (r1 != 0) goto L9
                goto L66
            L9:
                r2 = -1
                r3 = 0
                r4 = 1
                if (r0 == r2) goto L25
                androidx.recyclerview.widget.RecyclerView$ViewHolder r0 = r1.findViewHolderForAdapterPosition(r0)
                if (r0 == 0) goto L21
                android.view.View r1 = r0.itemView
                if (r1 == 0) goto L21
                r1.setActivated(r3)
                android.view.View r0 = r0.itemView
                r0.setSelected(r3)
                goto L25
            L21:
                int r0 = r6.mSelection
                r1 = r4
                goto L27
            L25:
                r1 = r3
                r0 = r4
            L27:
                r6.mSelection = r7
                if (r8 == 0) goto L44
                if (r7 == r2) goto L44
                androidx.recyclerview.widget.RecyclerView r8 = r6.mParent
                androidx.recyclerview.widget.RecyclerView$ViewHolder r8 = r8.findViewHolderForAdapterPosition(r7)
                if (r8 == 0) goto L42
                android.view.View r5 = r8.itemView
                if (r5 == 0) goto L42
                r5.setActivated(r4)
                android.view.View r7 = r8.itemView
                r7.setSelected(r4)
                goto L44
            L42:
                r3 = r4
                goto L45
            L44:
                r7 = r4
            L45:
                com.miui.gallery.editor.photo.widgets.recyclerview.Selectable$Selector r8 = r6.mSelector
                if (r8 == 0) goto L4e
                androidx.recyclerview.widget.RecyclerView r8 = r6.mParent
                r8.invalidate()
            L4e:
                if (r1 == 0) goto L5b
                if (r0 == r2) goto L5b
                androidx.recyclerview.widget.RecyclerView r8 = r6.mParent
                androidx.recyclerview.widget.RecyclerView$Adapter r8 = r8.getAdapter()
                r8.notifyItemChanged(r0)
            L5b:
                if (r3 == 0) goto L66
                androidx.recyclerview.widget.RecyclerView r8 = r6.mParent
                androidx.recyclerview.widget.RecyclerView$Adapter r8 = r8.getAdapter()
                r8.notifyItemChanged(r7)
            L66:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.editor.photo.widgets.recyclerview.Selectable.Delegator.setSelection(int, boolean):void");
        }

        @Override // com.miui.gallery.editor.photo.widgets.recyclerview.Selectable
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
            Selector selector = this.mSelector;
            if (selector != null) {
                recyclerView.addItemDecoration(selector);
                if (this.mSelector.mRequireLayer && recyclerView.getLayerType() == 0) {
                    recyclerView.setLayerType(2, null);
                }
            }
            this.mParent = recyclerView;
        }

        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            if (this.mParent == recyclerView) {
                Selector selector = this.mSelector;
                if (selector != null) {
                    recyclerView.removeItemDecoration(selector);
                }
                this.mParent = null;
                return;
            }
            DefaultLogger.w("Selectable.Delegator", "not the attached parent view .");
        }
    }

    /* loaded from: classes2.dex */
    public static class Selector extends RecyclerView.ItemDecoration {
        public Selectable mAdapter;
        public boolean mRequireLayer;
        public Drawable mSelector;

        public Selector(Drawable drawable) {
            this.mSelector = drawable;
        }

        public Selector(int i) {
            this(createMaskDrawable(i));
            this.mRequireLayer = true;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
        public void onDrawOver(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
            View view;
            super.onDrawOver(canvas, recyclerView, state);
            RecyclerView.ViewHolder findViewHolderForAdapterPosition = recyclerView.findViewHolderForAdapterPosition(this.mAdapter.getSelection());
            if (findViewHolderForAdapterPosition == null || (view = findViewHolderForAdapterPosition.itemView) == null) {
                return;
            }
            int save = canvas.save();
            canvas.clipRect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
            this.mSelector.setBounds(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
            this.mSelector.draw(canvas);
            canvas.restoreToCount(save);
        }

        public static Drawable createMaskDrawable(int i) {
            ShapeDrawable shapeDrawable = new ShapeDrawable();
            shapeDrawable.getPaint().setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
            shapeDrawable.getPaint().setColor(i);
            return shapeDrawable;
        }
    }
}
