package com.miui.gallery.editor.photo.core.imports.obsoletes;

import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import java.util.BitSet;

/* loaded from: classes2.dex */
public class EventHandler extends Handler {
    public SparseArray<Runnable> mCallbacks = new SparseArray<>();
    public BitSet mBitSet = new BitSet();

    public int register(Runnable runnable) {
        int nextClearBit = this.mBitSet.nextClearBit(0);
        this.mCallbacks.put(nextClearBit, runnable);
        this.mBitSet.set(nextClearBit);
        return nextClearBit;
    }

    public void unregister(int i) {
        this.mCallbacks.setValueAt(i, null);
        this.mBitSet.clear(i);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        Runnable runnable = this.mCallbacks.get(message.what);
        if (runnable != null) {
            runnable.run();
        }
    }
}
