package com.baidu.mapsdkplatform.comapi.map;

import android.os.Message;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class s {
    private static final String a = "s";
    private r b;

    public void a(Message message) {
        if (message.what != 65289) {
            return;
        }
        int i = message.arg1;
        if (i != 12 && i != 101 && i != 102) {
            switch (i) {
                case -1:
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                    break;
                default:
                    return;
            }
        }
        r rVar = this.b;
        if (rVar == null) {
            return;
        }
        rVar.a(i, message.arg2);
    }

    public void a(r rVar) {
        this.b = rVar;
    }

    public void b(r rVar) {
        this.b = null;
    }
}
