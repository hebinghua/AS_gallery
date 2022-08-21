package com.meicam.sdk;

import java.util.HashMap;

/* loaded from: classes.dex */
public class NvsObject {
    public long m_internalObject = 0;
    private HashMap<String, Object> m_attachmentMap = new HashMap<>();

    public void setAttachment(String str, Object obj) {
        this.m_attachmentMap.put(str, obj);
    }

    public Object getAttachment(String str) {
        return this.m_attachmentMap.get(str);
    }

    public void setInternalObject(long j) {
        this.m_internalObject = j;
    }

    public long getInternalObject() {
        return this.m_internalObject;
    }
}
