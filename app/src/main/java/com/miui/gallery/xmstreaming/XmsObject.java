package com.miui.gallery.xmstreaming;

import java.util.HashMap;

/* loaded from: classes3.dex */
public class XmsObject {
    public long m_internalObject = 0;
    public HashMap<String, Object> m_attachmentMap = new HashMap<>();

    public void mapData() {
    }

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

    public Object getValue(String str) {
        return this.m_attachmentMap.get(str);
    }
}
