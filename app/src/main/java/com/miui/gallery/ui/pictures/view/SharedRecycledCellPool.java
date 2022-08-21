package com.miui.gallery.ui.pictures.view;

import com.miui.gallery.util.BaseBuildUtil;

/* loaded from: classes2.dex */
public class SharedRecycledCellPool {
    public static volatile RecycledCellPoll sRecycledCellPoll;

    public static RecycledCellPoll obtain() {
        if (sRecycledCellPoll == null) {
            synchronized (SharedRecycledCellPool.class) {
                if (sRecycledCellPoll == null) {
                    sRecycledCellPoll = new SoftCellPool(BaseBuildUtil.isLowRamDevice() ? 16 : 32);
                }
            }
        }
        return sRecycledCellPoll;
    }
}
