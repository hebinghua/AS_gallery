package com.miui.gallery.ui.pictures.view;

import android.view.View;
import miuix.core.util.Pools;

/* loaded from: classes2.dex */
public class SoftCellPool implements RecycledCellPoll {
    public Pools.SoftReferencePool<ImageCell> mPool;
    public final int mSize;

    public SoftCellPool(int i) {
        this.mSize = i;
    }

    public final void ensurePool() {
        if (this.mPool == null) {
            this.mPool = Pools.createSoftReferencePool(new Pools.Manager<ImageCell>() { // from class: com.miui.gallery.ui.pictures.view.SoftCellPool.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // miuix.core.util.Pools.Manager
                /* renamed from: createInstance */
                public ImageCell mo2624createInstance() {
                    return new ImageCell();
                }

                @Override // miuix.core.util.Pools.Manager
                public void onRelease(ImageCell imageCell) {
                    super.onRelease((AnonymousClass1) imageCell);
                }
            }, this.mSize);
        }
    }

    @Override // com.miui.gallery.ui.pictures.view.RecycledCellPoll
    public ImageCell obtainFor(View view) {
        ImageCell imageCell;
        synchronized (this) {
            ensurePool();
            imageCell = (ImageCell) this.mPool.acquire();
            imageCell.bindView(view);
        }
        return imageCell;
    }

    @Override // com.miui.gallery.ui.pictures.view.RecycledCellPoll
    public void release(ImageCell imageCell) {
        if (imageCell == null || imageCell.isActive() || !imageCell.isRecyclable()) {
            return;
        }
        imageCell.recycle();
        synchronized (this) {
            Pools.SoftReferencePool<ImageCell> softReferencePool = this.mPool;
            if (softReferencePool != null) {
                softReferencePool.release(imageCell);
            }
        }
    }
}
