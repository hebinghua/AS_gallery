package com.adobe.xmp.impl;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMetaFactory;
import com.adobe.xmp.impl.xpath.XMPPath;
import com.adobe.xmp.impl.xpath.XMPPathSegment;
import com.adobe.xmp.options.PropertyOptions;
import java.util.Iterator;

/* loaded from: classes.dex */
public class XMPNodeUtils {
    public static XMPNode findSchemaNode(XMPNode xMPNode, String str, boolean z) throws XMPException {
        return findSchemaNode(xMPNode, str, null, z);
    }

    public static XMPNode findSchemaNode(XMPNode xMPNode, String str, String str2, boolean z) throws XMPException {
        XMPNode findChildByName = xMPNode.findChildByName(str);
        if (findChildByName == null && z) {
            findChildByName = new XMPNode(str, new PropertyOptions().setSchemaNode(true));
            findChildByName.setImplicit(true);
            String namespacePrefix = XMPMetaFactory.getSchemaRegistry().getNamespacePrefix(str);
            if (namespacePrefix == null) {
                if (str2 != null && str2.length() != 0) {
                    namespacePrefix = XMPMetaFactory.getSchemaRegistry().registerNamespace(str, str2);
                } else {
                    throw new XMPException("Unregistered schema namespace URI", 101);
                }
            }
            findChildByName.setValue(namespacePrefix);
            xMPNode.addChild(findChildByName);
        }
        return findChildByName;
    }

    public static XMPNode findChildNode(XMPNode xMPNode, String str, boolean z) throws XMPException {
        if (!xMPNode.getOptions().isSchemaNode() && !xMPNode.getOptions().isStruct()) {
            if (!xMPNode.isImplicit()) {
                throw new XMPException("Named children only allowed for schemas and structs", 102);
            }
            if (xMPNode.getOptions().isArray()) {
                throw new XMPException("Named children not allowed for arrays", 102);
            }
            if (z) {
                xMPNode.getOptions().setStruct(true);
            }
        }
        XMPNode findChildByName = xMPNode.findChildByName(str);
        if (findChildByName != null || !z) {
            return findChildByName;
        }
        XMPNode xMPNode2 = new XMPNode(str, new PropertyOptions());
        xMPNode2.setImplicit(true);
        xMPNode.addChild(xMPNode2);
        return xMPNode2;
    }

    public static XMPNode findNode(XMPNode xMPNode, XMPPath xMPPath, boolean z, PropertyOptions propertyOptions) throws XMPException {
        XMPNode xMPNode2;
        if (xMPPath == null || xMPPath.size() == 0) {
            throw new XMPException("Empty XMPPath", 102);
        }
        XMPNode findSchemaNode = findSchemaNode(xMPNode, xMPPath.getSegment(0).getName(), z);
        if (findSchemaNode == null) {
            return null;
        }
        if (findSchemaNode.isImplicit()) {
            findSchemaNode.setImplicit(false);
            xMPNode2 = findSchemaNode;
        } else {
            xMPNode2 = null;
        }
        for (int i = 1; i < xMPPath.size(); i++) {
            try {
                findSchemaNode = followXPathStep(findSchemaNode, xMPPath.getSegment(i), z);
                if (findSchemaNode == null) {
                    if (z) {
                        deleteNode(xMPNode2);
                    }
                    return null;
                }
                if (findSchemaNode.isImplicit()) {
                    findSchemaNode.setImplicit(false);
                    if (i == 1 && xMPPath.getSegment(i).isAlias() && xMPPath.getSegment(i).getAliasForm() != 0) {
                        findSchemaNode.getOptions().setOption(xMPPath.getSegment(i).getAliasForm(), true);
                    } else if (i < xMPPath.size() - 1 && xMPPath.getSegment(i).getKind() == 1 && !findSchemaNode.getOptions().isCompositeProperty()) {
                        findSchemaNode.getOptions().setStruct(true);
                    }
                    if (xMPNode2 == null) {
                        xMPNode2 = findSchemaNode;
                    }
                }
            } catch (XMPException e) {
                if (xMPNode2 != null) {
                    deleteNode(xMPNode2);
                }
                throw e;
            }
        }
        if (xMPNode2 != null) {
            findSchemaNode.getOptions().mergeWith(propertyOptions);
            findSchemaNode.setOptions(findSchemaNode.getOptions());
        }
        return findSchemaNode;
    }

    public static void deleteNode(XMPNode xMPNode) {
        XMPNode parent = xMPNode.getParent();
        if (xMPNode.getOptions().isQualifier()) {
            parent.removeQualifier(xMPNode);
        } else {
            parent.removeChild(xMPNode);
        }
        if (parent.hasChildren() || !parent.getOptions().isSchemaNode()) {
            return;
        }
        parent.getParent().removeChild(parent);
    }

    public static PropertyOptions verifySetOptions(PropertyOptions propertyOptions, Object obj) throws XMPException {
        if (propertyOptions == null) {
            propertyOptions = new PropertyOptions();
        }
        if (propertyOptions.isArrayAltText()) {
            propertyOptions.setArrayAlternate(true);
        }
        if (propertyOptions.isArrayAlternate()) {
            propertyOptions.setArrayOrdered(true);
        }
        if (propertyOptions.isArrayOrdered()) {
            propertyOptions.setArray(true);
        }
        if (propertyOptions.isCompositeProperty() && obj != null && obj.toString().length() > 0) {
            throw new XMPException("Structs and arrays can't have values", 103);
        }
        propertyOptions.assertConsistency(propertyOptions.getOptions());
        return propertyOptions;
    }

    public static XMPNode followXPathStep(XMPNode xMPNode, XMPPathSegment xMPPathSegment, boolean z) throws XMPException {
        int lookupQualSelector;
        int kind = xMPPathSegment.getKind();
        if (kind == 1) {
            return findChildNode(xMPNode, xMPPathSegment.getName(), z);
        }
        if (kind == 2) {
            return findQualifierNode(xMPNode, xMPPathSegment.getName().substring(1), z);
        }
        if (!xMPNode.getOptions().isArray()) {
            throw new XMPException("Indexing applied to non-array", 102);
        }
        if (kind == 3) {
            lookupQualSelector = findIndexedItem(xMPNode, xMPPathSegment.getName(), z);
        } else if (kind == 4) {
            lookupQualSelector = xMPNode.getChildrenLength();
        } else if (kind == 6) {
            String[] splitNameAndValue = Utils.splitNameAndValue(xMPPathSegment.getName());
            lookupQualSelector = lookupFieldSelector(xMPNode, splitNameAndValue[0], splitNameAndValue[1]);
        } else if (kind == 5) {
            String[] splitNameAndValue2 = Utils.splitNameAndValue(xMPPathSegment.getName());
            lookupQualSelector = lookupQualSelector(xMPNode, splitNameAndValue2[0], splitNameAndValue2[1], xMPPathSegment.getAliasForm());
        } else {
            throw new XMPException("Unknown array indexing step in FollowXPathStep", 9);
        }
        if (1 <= lookupQualSelector && lookupQualSelector <= xMPNode.getChildrenLength()) {
            return xMPNode.getChild(lookupQualSelector);
        }
        return null;
    }

    public static XMPNode findQualifierNode(XMPNode xMPNode, String str, boolean z) throws XMPException {
        XMPNode findQualifierByName = xMPNode.findQualifierByName(str);
        if (findQualifierByName != null || !z) {
            return findQualifierByName;
        }
        XMPNode xMPNode2 = new XMPNode(str, null);
        xMPNode2.setImplicit(true);
        xMPNode.addQualifier(xMPNode2);
        return xMPNode2;
    }

    public static int findIndexedItem(XMPNode xMPNode, String str, boolean z) throws XMPException {
        try {
            int parseInt = Integer.parseInt(str.substring(1, str.length() - 1));
            if (parseInt < 1) {
                throw new XMPException("Array index must be larger than zero", 102);
            }
            if (z && parseInt == xMPNode.getChildrenLength() + 1) {
                XMPNode xMPNode2 = new XMPNode("[]", null);
                xMPNode2.setImplicit(true);
                xMPNode.addChild(xMPNode2);
            }
            return parseInt;
        } catch (NumberFormatException unused) {
            throw new XMPException("Array index not digits.", 102);
        }
    }

    public static int lookupFieldSelector(XMPNode xMPNode, String str, String str2) throws XMPException {
        int i = -1;
        for (int i2 = 1; i2 <= xMPNode.getChildrenLength() && i < 0; i2++) {
            XMPNode child = xMPNode.getChild(i2);
            if (!child.getOptions().isStruct()) {
                throw new XMPException("Field selector must be used on array of struct", 102);
            }
            int i3 = 1;
            while (true) {
                if (i3 <= child.getChildrenLength()) {
                    XMPNode child2 = child.getChild(i3);
                    if (str.equals(child2.getName()) && str2.equals(child2.getValue())) {
                        i = i2;
                        break;
                    }
                    i3++;
                }
            }
        }
        return i;
    }

    public static int lookupQualSelector(XMPNode xMPNode, String str, String str2, int i) throws XMPException {
        if ("xml:lang".equals(str)) {
            int lookupLanguageItem = lookupLanguageItem(xMPNode, Utils.normalizeLangValue(str2));
            if (lookupLanguageItem >= 0 || (i & 4096) <= 0) {
                return lookupLanguageItem;
            }
            XMPNode xMPNode2 = new XMPNode("[]", null);
            xMPNode2.addQualifier(new XMPNode("xml:lang", "x-default", null));
            xMPNode.addChild(1, xMPNode2);
            return 1;
        }
        for (int i2 = 1; i2 < xMPNode.getChildrenLength(); i2++) {
            Iterator iterateQualifier = xMPNode.getChild(i2).iterateQualifier();
            while (iterateQualifier.hasNext()) {
                XMPNode xMPNode3 = (XMPNode) iterateQualifier.next();
                if (str.equals(xMPNode3.getName()) && str2.equals(xMPNode3.getValue())) {
                    return i2;
                }
            }
        }
        return -1;
    }

    public static void normalizeLangArray(XMPNode xMPNode) {
        if (!xMPNode.getOptions().isArrayAltText()) {
            return;
        }
        for (int i = 2; i <= xMPNode.getChildrenLength(); i++) {
            XMPNode child = xMPNode.getChild(i);
            if (child.hasQualifier() && "x-default".equals(child.getQualifier(1).getValue())) {
                try {
                    xMPNode.removeChild(i);
                    xMPNode.addChild(1, child);
                } catch (XMPException unused) {
                }
                if (i != 2) {
                    return;
                }
                xMPNode.getChild(2).setValue(child.getValue());
                return;
            }
        }
    }

    public static void detectAltText(XMPNode xMPNode) {
        if (!xMPNode.getOptions().isArrayAlternate() || !xMPNode.hasChildren()) {
            return;
        }
        boolean z = false;
        Iterator iterateChildren = xMPNode.iterateChildren();
        while (true) {
            if (iterateChildren.hasNext()) {
                if (((XMPNode) iterateChildren.next()).getOptions().getHasLanguage()) {
                    z = true;
                    break;
                }
            } else {
                break;
            }
        }
        if (!z) {
            return;
        }
        xMPNode.getOptions().setArrayAltText(true);
        normalizeLangArray(xMPNode);
    }

    public static void appendLangItem(XMPNode xMPNode, String str, String str2) throws XMPException {
        XMPNode xMPNode2 = new XMPNode("[]", str2, null);
        XMPNode xMPNode3 = new XMPNode("xml:lang", str, null);
        xMPNode2.addQualifier(xMPNode3);
        if (!"x-default".equals(xMPNode3.getValue())) {
            xMPNode.addChild(xMPNode2);
        } else {
            xMPNode.addChild(1, xMPNode2);
        }
    }

    public static Object[] chooseLocalizedText(XMPNode xMPNode, String str, String str2) throws XMPException {
        if (!xMPNode.getOptions().isArrayAltText()) {
            throw new XMPException("Localized text array is not alt-text", 102);
        }
        XMPNode xMPNode2 = null;
        if (!xMPNode.hasChildren()) {
            return new Object[]{new Integer(0), null};
        }
        Iterator iterateChildren = xMPNode.iterateChildren();
        XMPNode xMPNode3 = null;
        int i = 0;
        while (iterateChildren.hasNext()) {
            XMPNode xMPNode4 = (XMPNode) iterateChildren.next();
            if (xMPNode4.getOptions().isCompositeProperty()) {
                throw new XMPException("Alt-text array item is not simple", 102);
            }
            if (!xMPNode4.hasQualifier() || !"xml:lang".equals(xMPNode4.getQualifier(1).getName())) {
                throw new XMPException("Alt-text array item has no language qualifier", 102);
            }
            String value = xMPNode4.getQualifier(1).getValue();
            if (str2.equals(value)) {
                return new Object[]{new Integer(1), xMPNode4};
            }
            if (str != null && value.startsWith(str)) {
                if (xMPNode2 == null) {
                    xMPNode2 = xMPNode4;
                }
                i++;
            } else if ("x-default".equals(value)) {
                xMPNode3 = xMPNode4;
            }
        }
        return i == 1 ? new Object[]{new Integer(2), xMPNode2} : i > 1 ? new Object[]{new Integer(3), xMPNode2} : xMPNode3 != null ? new Object[]{new Integer(4), xMPNode3} : new Object[]{new Integer(5), xMPNode.getChild(1)};
    }

    public static int lookupLanguageItem(XMPNode xMPNode, String str) throws XMPException {
        if (!xMPNode.getOptions().isArray()) {
            throw new XMPException("Language item must be used on array", 102);
        }
        for (int i = 1; i <= xMPNode.getChildrenLength(); i++) {
            XMPNode child = xMPNode.getChild(i);
            if (child.hasQualifier() && "xml:lang".equals(child.getQualifier(1).getName()) && str.equals(child.getQualifier(1).getValue())) {
                return i;
            }
        }
        return -1;
    }
}
