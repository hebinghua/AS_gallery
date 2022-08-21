package com.adobe.xmp.impl.xpath;

import ch.qos.logback.classic.spi.CallerData;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMetaFactory;
import com.adobe.xmp.impl.Utils;
import com.adobe.xmp.properties.XMPAliasInfo;

/* loaded from: classes.dex */
public final class XMPPathParser {
    public static XMPPath expandXPath(String str, String str2) throws XMPException {
        XMPPathSegment parseIndexSegment;
        if (str == null || str2 == null) {
            throw new XMPException("Parameter must not be null", 4);
        }
        XMPPath xMPPath = new XMPPath();
        PathPosition pathPosition = new PathPosition();
        pathPosition.path = str2;
        parseRootNode(str, pathPosition, xMPPath);
        while (pathPosition.stepEnd < str2.length()) {
            pathPosition.stepBegin = pathPosition.stepEnd;
            skipPathDelimiter(str2, pathPosition);
            int i = pathPosition.stepBegin;
            pathPosition.stepEnd = i;
            if (str2.charAt(i) != '[') {
                parseIndexSegment = parseStructSegment(pathPosition);
            } else {
                parseIndexSegment = parseIndexSegment(pathPosition);
            }
            if (parseIndexSegment.getKind() == 1) {
                if (parseIndexSegment.getName().charAt(0) == '@') {
                    parseIndexSegment.setName(CallerData.NA + parseIndexSegment.getName().substring(1));
                    if (!"?xml:lang".equals(parseIndexSegment.getName())) {
                        throw new XMPException("Only xml:lang allowed with '@'", 102);
                    }
                }
                if (parseIndexSegment.getName().charAt(0) == '?') {
                    pathPosition.nameStart++;
                    parseIndexSegment.setKind(2);
                }
                verifyQualName(pathPosition.path.substring(pathPosition.nameStart, pathPosition.nameEnd));
            } else if (parseIndexSegment.getKind() != 6) {
                continue;
            } else {
                if (parseIndexSegment.getName().charAt(1) == '@') {
                    parseIndexSegment.setName("[?" + parseIndexSegment.getName().substring(2));
                    if (!parseIndexSegment.getName().startsWith("[?xml:lang=")) {
                        throw new XMPException("Only xml:lang allowed with '@'", 102);
                    }
                }
                if (parseIndexSegment.getName().charAt(1) == '?') {
                    pathPosition.nameStart++;
                    parseIndexSegment.setKind(5);
                    verifyQualName(pathPosition.path.substring(pathPosition.nameStart, pathPosition.nameEnd));
                }
            }
            xMPPath.add(parseIndexSegment);
        }
        return xMPPath;
    }

    public static void skipPathDelimiter(String str, PathPosition pathPosition) throws XMPException {
        if (str.charAt(pathPosition.stepBegin) == '/') {
            int i = pathPosition.stepBegin + 1;
            pathPosition.stepBegin = i;
            if (i >= str.length()) {
                throw new XMPException("Empty XMPPath segment", 102);
            }
        }
        if (str.charAt(pathPosition.stepBegin) == '*') {
            int i2 = pathPosition.stepBegin + 1;
            pathPosition.stepBegin = i2;
            if (i2 < str.length() && str.charAt(pathPosition.stepBegin) == '[') {
                return;
            }
            throw new XMPException("Missing '[' after '*'", 102);
        }
    }

    public static XMPPathSegment parseStructSegment(PathPosition pathPosition) throws XMPException {
        pathPosition.nameStart = pathPosition.stepBegin;
        while (pathPosition.stepEnd < pathPosition.path.length() && "/[*".indexOf(pathPosition.path.charAt(pathPosition.stepEnd)) < 0) {
            pathPosition.stepEnd++;
        }
        int i = pathPosition.stepEnd;
        pathPosition.nameEnd = i;
        int i2 = pathPosition.stepBegin;
        if (i == i2) {
            throw new XMPException("Empty XMPPath segment", 102);
        }
        return new XMPPathSegment(pathPosition.path.substring(i2, i), 1);
    }

    public static XMPPathSegment parseIndexSegment(PathPosition pathPosition) throws XMPException {
        XMPPathSegment xMPPathSegment;
        int i = pathPosition.stepEnd + 1;
        pathPosition.stepEnd = i;
        if ('0' <= pathPosition.path.charAt(i) && pathPosition.path.charAt(pathPosition.stepEnd) <= '9') {
            while (pathPosition.stepEnd < pathPosition.path.length() && '0' <= pathPosition.path.charAt(pathPosition.stepEnd) && pathPosition.path.charAt(pathPosition.stepEnd) <= '9') {
                pathPosition.stepEnd++;
            }
            xMPPathSegment = new XMPPathSegment(null, 3);
        } else {
            while (pathPosition.stepEnd < pathPosition.path.length() && pathPosition.path.charAt(pathPosition.stepEnd) != ']' && pathPosition.path.charAt(pathPosition.stepEnd) != '=') {
                pathPosition.stepEnd++;
            }
            if (pathPosition.stepEnd >= pathPosition.path.length()) {
                throw new XMPException("Missing ']' or '=' for array index", 102);
            }
            if (pathPosition.path.charAt(pathPosition.stepEnd) == ']') {
                if (!"[last()".equals(pathPosition.path.substring(pathPosition.stepBegin, pathPosition.stepEnd))) {
                    throw new XMPException("Invalid non-numeric array index", 102);
                }
                xMPPathSegment = new XMPPathSegment(null, 4);
            } else {
                pathPosition.nameStart = pathPosition.stepBegin + 1;
                int i2 = pathPosition.stepEnd;
                pathPosition.nameEnd = i2;
                int i3 = i2 + 1;
                pathPosition.stepEnd = i3;
                char charAt = pathPosition.path.charAt(i3);
                if (charAt != '\'' && charAt != '\"') {
                    throw new XMPException("Invalid quote in array selector", 102);
                }
                pathPosition.stepEnd++;
                while (pathPosition.stepEnd < pathPosition.path.length()) {
                    if (pathPosition.path.charAt(pathPosition.stepEnd) == charAt) {
                        if (pathPosition.stepEnd + 1 >= pathPosition.path.length() || pathPosition.path.charAt(pathPosition.stepEnd + 1) != charAt) {
                            break;
                        }
                        pathPosition.stepEnd++;
                    }
                    pathPosition.stepEnd++;
                }
                if (pathPosition.stepEnd >= pathPosition.path.length()) {
                    throw new XMPException("No terminating quote for array selector", 102);
                }
                pathPosition.stepEnd++;
                xMPPathSegment = new XMPPathSegment(null, 6);
            }
        }
        if (pathPosition.stepEnd >= pathPosition.path.length() || pathPosition.path.charAt(pathPosition.stepEnd) != ']') {
            throw new XMPException("Missing ']' for array index", 102);
        }
        int i4 = pathPosition.stepEnd + 1;
        pathPosition.stepEnd = i4;
        xMPPathSegment.setName(pathPosition.path.substring(pathPosition.stepBegin, i4));
        return xMPPathSegment;
    }

    public static void parseRootNode(String str, PathPosition pathPosition, XMPPath xMPPath) throws XMPException {
        while (pathPosition.stepEnd < pathPosition.path.length() && "/[*".indexOf(pathPosition.path.charAt(pathPosition.stepEnd)) < 0) {
            pathPosition.stepEnd++;
        }
        int i = pathPosition.stepEnd;
        int i2 = pathPosition.stepBegin;
        if (i == i2) {
            throw new XMPException("Empty initial XMPPath step", 102);
        }
        String verifyXPathRoot = verifyXPathRoot(str, pathPosition.path.substring(i2, i));
        XMPAliasInfo findAlias = XMPMetaFactory.getSchemaRegistry().findAlias(verifyXPathRoot);
        if (findAlias == null) {
            xMPPath.add(new XMPPathSegment(str, Integer.MIN_VALUE));
            xMPPath.add(new XMPPathSegment(verifyXPathRoot, 1));
            return;
        }
        xMPPath.add(new XMPPathSegment(findAlias.getNamespace(), Integer.MIN_VALUE));
        XMPPathSegment xMPPathSegment = new XMPPathSegment(verifyXPathRoot(findAlias.getNamespace(), findAlias.getPropName()), 1);
        xMPPathSegment.setAlias(true);
        xMPPathSegment.setAliasForm(findAlias.getAliasForm().getOptions());
        xMPPath.add(xMPPathSegment);
        if (findAlias.getAliasForm().isArrayAltText()) {
            XMPPathSegment xMPPathSegment2 = new XMPPathSegment("[?xml:lang='x-default']", 5);
            xMPPathSegment2.setAlias(true);
            xMPPathSegment2.setAliasForm(findAlias.getAliasForm().getOptions());
            xMPPath.add(xMPPathSegment2);
        } else if (!findAlias.getAliasForm().isArray()) {
        } else {
            XMPPathSegment xMPPathSegment3 = new XMPPathSegment("[1]", 3);
            xMPPathSegment3.setAlias(true);
            xMPPathSegment3.setAliasForm(findAlias.getAliasForm().getOptions());
            xMPPath.add(xMPPathSegment3);
        }
    }

    public static void verifyQualName(String str) throws XMPException {
        int indexOf = str.indexOf(58);
        if (indexOf > 0) {
            String substring = str.substring(0, indexOf);
            if (Utils.isXMLNameNS(substring)) {
                if (XMPMetaFactory.getSchemaRegistry().getNamespaceURI(substring) == null) {
                    throw new XMPException("Unknown namespace prefix for qualified name", 102);
                }
                return;
            }
        }
        throw new XMPException("Ill-formed qualified name", 102);
    }

    public static void verifySimpleXMLName(String str) throws XMPException {
        if (Utils.isXMLName(str)) {
            return;
        }
        throw new XMPException("Bad XML name", 102);
    }

    public static String verifyXPathRoot(String str, String str2) throws XMPException {
        if (str == null || str.length() == 0) {
            throw new XMPException("Schema namespace URI is required", 101);
        }
        if (str2.charAt(0) == '?' || str2.charAt(0) == '@') {
            throw new XMPException("Top level name must not be a qualifier", 102);
        }
        if (str2.indexOf(47) >= 0 || str2.indexOf(91) >= 0) {
            throw new XMPException("Top level name must be simple", 102);
        }
        String namespacePrefix = XMPMetaFactory.getSchemaRegistry().getNamespacePrefix(str);
        if (namespacePrefix == null) {
            throw new XMPException("Unregistered schema namespace URI", 101);
        }
        int indexOf = str2.indexOf(58);
        if (indexOf < 0) {
            verifySimpleXMLName(str2);
            return namespacePrefix + str2;
        }
        verifySimpleXMLName(str2.substring(0, indexOf));
        verifySimpleXMLName(str2.substring(indexOf));
        String substring = str2.substring(0, indexOf + 1);
        String namespacePrefix2 = XMPMetaFactory.getSchemaRegistry().getNamespacePrefix(str);
        if (namespacePrefix2 == null) {
            throw new XMPException("Unknown schema namespace prefix", 101);
        }
        if (!substring.equals(namespacePrefix2)) {
            throw new XMPException("Schema namespace URI and prefix mismatch", 101);
        }
        return str2;
    }
}
