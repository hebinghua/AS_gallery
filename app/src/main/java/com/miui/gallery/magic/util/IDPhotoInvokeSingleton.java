package com.miui.gallery.magic.util;

import com.miui.gallery.magic.IDPhotoInvoker;

/* loaded from: classes2.dex */
public class IDPhotoInvokeSingleton {
    public int initTimes;
    public boolean isInit;
    public IDPhotoInvoker mIDPhotoInvoker;

    /* loaded from: classes2.dex */
    public static class SingletonClassInstance {
        public static final IDPhotoInvokeSingleton instance = new IDPhotoInvokeSingleton();
    }

    public IDPhotoInvokeSingleton() {
        this.mIDPhotoInvoker = null;
    }

    public static IDPhotoInvokeSingleton getInstance() {
        return SingletonClassInstance.instance;
    }

    public IDPhotoInvoker getIDPhotoInvoker() {
        if (this.mIDPhotoInvoker == null) {
            this.mIDPhotoInvoker = new IDPhotoInvoker();
        }
        if (!this.isInit) {
            this.mIDPhotoInvoker.idInit();
            MagicLog.INSTANCE.showLog("MagicLogger", "mIDPhotoInvoker idInit");
            this.isInit = true;
        }
        return this.mIDPhotoInvoker;
    }

    public void createIDPhotoInvoker() {
        this.initTimes++;
        MagicLog.INSTANCE.showLog("MagicLogger", " createIDPhotoInvoker initTimes " + this.initTimes);
    }

    public void destroyIDPhotoInvoker() {
        IDPhotoInvoker iDPhotoInvoker;
        this.initTimes--;
        MagicLog.INSTANCE.showLog("MagicLogger", " destroyIDPhotoInvoker initTimes " + this.initTimes);
        if (this.initTimes != 0 || !this.isInit || (iDPhotoInvoker = this.mIDPhotoInvoker) == null) {
            return;
        }
        iDPhotoInvoker.idDestory();
        MagicLog.INSTANCE.showLog("MagicLogger", "mIDPhotoInvoker idDestroy");
        this.isInit = false;
        this.mIDPhotoInvoker = null;
    }
}
