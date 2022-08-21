package com.miui.gallery.xmstreaming;

import com.meicam.sdk.NvsFxDescription;

/* loaded from: classes3.dex */
public class XmsAnimation extends XmsObject {
    private String paramName;
    private String value;

    public void setParamName(String str) {
        this.paramName = str;
    }

    public String getParamName() {
        return this.paramName;
    }

    public void setValue(String str) {
        this.value = str;
    }

    public String getValue() {
        return this.value;
    }

    @Override // com.miui.gallery.xmstreaming.XmsObject
    public void mapData() {
        this.m_attachmentMap.put(NvsFxDescription.ParamInfoObject.PARAM_NAME, this.paramName);
        this.m_attachmentMap.put("value", this.value);
    }
}
