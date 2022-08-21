package com.xiaomi.onetrack.a;

import ch.qos.logback.core.CoreConstants;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class k {
    public String a;
    public long b;
    public long c;
    public String d;
    public JSONObject e;

    public String toString() {
        return "ConfigModel{appId='" + this.a + CoreConstants.SINGLE_QUOTE_CHAR + ", commonSample=" + this.b + ", timeStamp=" + this.c + ", eventsHash='" + this.d + CoreConstants.SINGLE_QUOTE_CHAR + ", appData='" + this.e + CoreConstants.SINGLE_QUOTE_CHAR + '}';
    }
}
