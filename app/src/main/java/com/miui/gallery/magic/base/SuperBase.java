package com.miui.gallery.magic.base;

/* loaded from: classes2.dex */
public abstract class SuperBase<CONTRACT> {
    public CONTRACT contract;

    /* renamed from: initContract */
    public abstract CONTRACT mo1070initContract();

    public CONTRACT getContract() {
        if (this.contract == null) {
            this.contract = mo1070initContract();
        }
        return this.contract;
    }
}
