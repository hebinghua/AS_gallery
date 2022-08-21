package com.baidu.platform.comapi.longlink;

/* loaded from: classes.dex */
public class BNLongLink {
    private static BNLongLinkInitCallBack a;

    public static void initLongLink() {
        BNLongLinkInitCallBack bNLongLinkInitCallBack = a;
        if (bNLongLinkInitCallBack != null) {
            bNLongLinkInitCallBack.onLongLinkInit();
        }
    }

    public static void registerLongLinkInitCallBack(BNLongLinkInitCallBack bNLongLinkInitCallBack) {
        a = bNLongLinkInitCallBack;
    }

    public static void unRegisterLongLinkInitCallBack(BNLongLinkInitCallBack bNLongLinkInitCallBack) {
        a = bNLongLinkInitCallBack;
    }
}
