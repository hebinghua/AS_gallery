package com.adobe.xmp;

import com.adobe.xmp.impl.XMPMetaParser;
import com.adobe.xmp.impl.XMPSchemaRegistryImpl;
import com.adobe.xmp.options.ParseOptions;

/* loaded from: classes.dex */
public final class XMPMetaFactory {
    public static XMPSchemaRegistry schema = new XMPSchemaRegistryImpl();
    public static XMPVersionInfo versionInfo = null;

    public static XMPSchemaRegistry getSchemaRegistry() {
        return schema;
    }

    public static XMPMeta parseFromBuffer(byte[] bArr) throws XMPException {
        return parseFromBuffer(bArr, null);
    }

    public static XMPMeta parseFromBuffer(byte[] bArr, ParseOptions parseOptions) throws XMPException {
        return XMPMetaParser.parse(bArr, parseOptions);
    }
}
