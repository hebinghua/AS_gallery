package com.xiaomi.clientreport.data;

import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class PerfClientReport extends a {
    public int code;
    public long perfCounts = -1;
    public long perfLatencies = -1;

    public static PerfClientReport getBlankInstance() {
        return new PerfClientReport();
    }

    @Override // com.xiaomi.clientreport.data.a
    public JSONObject toJson() {
        try {
            JSONObject json = super.toJson();
            if (json == null) {
                return null;
            }
            json.put("code", this.code);
            json.put("perfCounts", this.perfCounts);
            json.put("perfLatencies", this.perfLatencies);
            return json;
        } catch (JSONException e) {
            com.xiaomi.channel.commonutils.logger.b.a(e);
            return null;
        }
    }

    @Override // com.xiaomi.clientreport.data.a
    public String toJsonString() {
        return super.toJsonString();
    }
}
