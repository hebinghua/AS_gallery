package com.miui.gallery.reddot;

import java.util.List;

/* loaded from: classes2.dex */
public class RedDotGroup extends RedDot {
    public int mMaxDisplayNum;
    public List<RedDot> mPriorityGroup;

    @Override // com.miui.gallery.reddot.Rules
    public void onSaw() {
    }

    @Override // com.miui.gallery.reddot.Rules
    public void onUpdate() {
    }

    public RedDotGroup(String str, List<RedDot> list, int i) {
        super(str);
        this.mMaxDisplayNum = 0;
        this.mPriorityGroup = list;
        this.mMaxDisplayNum = i;
    }

    @Override // com.miui.gallery.reddot.Rules
    public void onClick() {
        for (RedDot redDot : this.mPriorityGroup) {
            redDot.onSaw();
        }
    }

    @Override // com.miui.gallery.reddot.Rules
    public boolean processDisplayStatus() {
        int i = 0;
        for (RedDot redDot : this.mPriorityGroup) {
            if (i >= this.mMaxDisplayNum || !redDot.processDisplayStatus() || !DisplayStatusManager.reddenDot(redDot.getKey())) {
                DisplayStatusManager.unreddenDot(redDot.getKey());
                if (redDot instanceof RedDotGroup) {
                    for (RedDot redDot2 : ((RedDotGroup) redDot).mPriorityGroup) {
                        DisplayStatusManager.unreddenDot(redDot2.getKey());
                    }
                }
            } else {
                i++;
            }
        }
        return i > 0;
    }
}
