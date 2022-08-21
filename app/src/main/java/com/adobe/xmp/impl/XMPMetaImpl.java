package com.adobe.xmp.impl;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.XMPUtils;
import com.adobe.xmp.impl.xpath.XMPPathParser;

/* loaded from: classes.dex */
public class XMPMetaImpl implements XMPMeta {
    public String packetHeader;
    public XMPNode tree;

    public XMPMetaImpl() {
        this.packetHeader = null;
        this.tree = new XMPNode(null, null, null);
    }

    public XMPMetaImpl(XMPNode xMPNode) {
        this.packetHeader = null;
        this.tree = xMPNode;
    }

    /* JADX WARN: Code restructure failed: missing block: B:110:?, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:111:?, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x009b, code lost:
        if (r2 == null) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x00a1, code lost:
        if (r8.getChildrenLength() <= 1) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00a3, code lost:
        r8.removeChild(r2);
        r8.addChild(1, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00a9, code lost:
        r10 = com.adobe.xmp.impl.XMPNodeUtils.chooseLocalizedText(r8, r10, r11);
        r0 = ((java.lang.Integer) r10[0]).intValue();
        r10 = (com.adobe.xmp.impl.XMPNode) r10[1];
        r3 = "x-default".equals(r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00bd, code lost:
        if (r0 == 0) goto L97;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00bf, code lost:
        if (r0 == 1) goto L68;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00c2, code lost:
        if (r0 == 2) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00c5, code lost:
        if (r0 == 3) goto L59;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x00c8, code lost:
        if (r0 == 4) goto L54;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x00cb, code lost:
        if (r0 != 5) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00cd, code lost:
        com.adobe.xmp.impl.XMPNodeUtils.appendLangItem(r8, r11, r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00d0, code lost:
        if (r3 == false) goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00dd, code lost:
        throw new com.adobe.xmp.XMPException("Unexpected result from ChooseLocalizedText", 9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00de, code lost:
        if (r2 == null) goto L58;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00e4, code lost:
        if (r8.getChildrenLength() != 1) goto L58;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00e6, code lost:
        r2.setValue(r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x00e9, code lost:
        com.adobe.xmp.impl.XMPNodeUtils.appendLangItem(r8, r11, r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00ee, code lost:
        com.adobe.xmp.impl.XMPNodeUtils.appendLangItem(r8, r11, r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00f1, code lost:
        if (r3 == false) goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x00f5, code lost:
        if (r9 == false) goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00f7, code lost:
        if (r2 == r10) goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00f9, code lost:
        if (r2 == null) goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x0107, code lost:
        if (r2.getValue().equals(r10.getValue()) == false) goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0109, code lost:
        r2.setValue(r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x010c, code lost:
        r10.setValue(r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0110, code lost:
        if (r3 != false) goto L76;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x0112, code lost:
        if (r9 == false) goto L75;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0114, code lost:
        if (r2 == r10) goto L75;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0116, code lost:
        if (r2 == null) goto L75;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0124, code lost:
        if (r2.getValue().equals(r10.getValue()) == false) goto L75;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x0126, code lost:
        r2.setValue(r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x0129, code lost:
        r10.setValue(r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x012d, code lost:
        r10 = r8.iterateChildren();
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x0135, code lost:
        if (r10.hasNext() == false) goto L94;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0137, code lost:
        r11 = (com.adobe.xmp.impl.XMPNode) r10.next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x013d, code lost:
        if (r11 == r2) goto L93;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x013f, code lost:
        r0 = r11.getValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x0143, code lost:
        if (r2 == null) goto L92;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x0145, code lost:
        r3 = r2.getValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x014a, code lost:
        r3 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x014f, code lost:
        if (r0.equals(r3) != false) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x0152, code lost:
        r11.setValue(r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x0156, code lost:
        if (r2 == null) goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x0158, code lost:
        r2.setValue(r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x015c, code lost:
        com.adobe.xmp.impl.XMPNodeUtils.appendLangItem(r8, "x-default", r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x015f, code lost:
        if (r3 != false) goto L51;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x0161, code lost:
        com.adobe.xmp.impl.XMPNodeUtils.appendLangItem(r8, r11, r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x0164, code lost:
        r9 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x0165, code lost:
        if (r9 != false) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x016b, code lost:
        if (r8.getChildrenLength() != 1) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x016d, code lost:
        com.adobe.xmp.impl.XMPNodeUtils.appendLangItem(r8, "x-default", r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x0170, code lost:
        return;
     */
    @Override // com.adobe.xmp.XMPMeta
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void setLocalizedText(java.lang.String r8, java.lang.String r9, java.lang.String r10, java.lang.String r11, java.lang.String r12, com.adobe.xmp.options.PropertyOptions r13) throws com.adobe.xmp.XMPException {
        /*
            Method dump skipped, instructions count: 377
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.adobe.xmp.impl.XMPMetaImpl.setLocalizedText(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.adobe.xmp.options.PropertyOptions):void");
    }

    public Object getPropertyObject(String str, String str2, int i) throws XMPException {
        ParameterAsserts.assertSchemaNS(str);
        ParameterAsserts.assertPropName(str2);
        XMPNode findNode = XMPNodeUtils.findNode(this.tree, XMPPathParser.expandXPath(str, str2), false, null);
        if (findNode != null) {
            if (i != 0 && findNode.getOptions().isCompositeProperty()) {
                throw new XMPException("Property must be simple when a value type is requested", 102);
            }
            return evaluateNodeValue(i, findNode);
        }
        return null;
    }

    @Override // com.adobe.xmp.XMPMeta
    public Integer getPropertyInteger(String str, String str2) throws XMPException {
        return (Integer) getPropertyObject(str, str2, 2);
    }

    @Override // com.adobe.xmp.XMPMeta
    public String getPropertyString(String str, String str2) throws XMPException {
        return (String) getPropertyObject(str, str2, 0);
    }

    public void setPacketHeader(String str) {
        this.packetHeader = str;
    }

    public Object clone() {
        return new XMPMetaImpl((XMPNode) this.tree.clone());
    }

    public XMPNode getRoot() {
        return this.tree;
    }

    public final Object evaluateNodeValue(int i, XMPNode xMPNode) throws XMPException {
        String value = xMPNode.getValue();
        switch (i) {
            case 1:
                return new Boolean(XMPUtils.convertToBoolean(value));
            case 2:
                return new Integer(XMPUtils.convertToInteger(value));
            case 3:
                return new Long(XMPUtils.convertToLong(value));
            case 4:
                return new Double(XMPUtils.convertToDouble(value));
            case 5:
                return XMPUtils.convertToDate(value);
            case 6:
                return XMPUtils.convertToDate(value).getCalendar();
            case 7:
                return XMPUtils.decodeBase64(value);
            default:
                if (value == null && !xMPNode.getOptions().isCompositeProperty()) {
                    value = "";
                }
                return value;
        }
    }
}
