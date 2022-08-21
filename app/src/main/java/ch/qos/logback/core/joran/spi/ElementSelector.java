package ch.qos.logback.core.joran.spi;

import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class ElementSelector extends ElementPath {
    public ElementSelector() {
    }

    public ElementSelector(List<String> list) {
        super(list);
    }

    public ElementSelector(String str) {
        super(str);
    }

    public boolean fullPathMatch(ElementPath elementPath) {
        if (elementPath.size() != size()) {
            return false;
        }
        int size = size();
        for (int i = 0; i < size; i++) {
            if (!equalityCheck(get(i), elementPath.get(i))) {
                return false;
            }
        }
        return true;
    }

    public int getTailMatchLength(ElementPath elementPath) {
        int i = 0;
        if (elementPath == null) {
            return 0;
        }
        int size = this.partList.size();
        int size2 = elementPath.partList.size();
        if (size != 0 && size2 != 0) {
            int i2 = size <= size2 ? size : size2;
            for (int i3 = 1; i3 <= i2 && equalityCheck(this.partList.get(size - i3), elementPath.partList.get(size2 - i3)); i3++) {
                i++;
            }
        }
        return i;
    }

    public boolean isContainedIn(ElementPath elementPath) {
        if (elementPath == null) {
            return false;
        }
        return elementPath.toStableString().contains(toStableString());
    }

    public int getPrefixMatchLength(ElementPath elementPath) {
        if (elementPath == null) {
            return 0;
        }
        int size = this.partList.size();
        int size2 = elementPath.partList.size();
        if (size == 0 || size2 == 0) {
            return 0;
        }
        if (size > size2) {
            size = size2;
        }
        int i = 0;
        for (int i2 = 0; i2 < size && equalityCheck(this.partList.get(i2), elementPath.partList.get(i2)); i2++) {
            i++;
        }
        return i;
    }

    private boolean equalityCheck(String str, String str2) {
        return str.equalsIgnoreCase(str2);
    }

    @Override // ch.qos.logback.core.joran.spi.ElementPath
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ElementSelector)) {
            return false;
        }
        ElementSelector elementSelector = (ElementSelector) obj;
        if (elementSelector.size() != size()) {
            return false;
        }
        int size = size();
        for (int i = 0; i < size; i++) {
            if (!equalityCheck(get(i), elementSelector.get(i))) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int size = size();
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            i ^= get(i2).toLowerCase(Locale.US).hashCode();
        }
        return i;
    }
}
