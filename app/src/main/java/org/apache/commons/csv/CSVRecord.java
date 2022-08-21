package org.apache.commons.csv;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes3.dex */
public final class CSVRecord implements Serializable, Iterable<String> {
    private static final long serialVersionUID = 1;
    private final long characterPosition;
    private final String comment;
    public final transient CSVParser parser;
    private final long recordNumber;
    private final String[] values;

    public CSVRecord(CSVParser cSVParser, String[] strArr, String str, long j, long j2) {
        this.recordNumber = j;
        this.values = strArr == null ? Constants.EMPTY_STRING_ARRAY : strArr;
        this.parser = cSVParser;
        this.comment = str;
        this.characterPosition = j2;
    }

    public String get(int i) {
        return this.values[i];
    }

    @Override // java.lang.Iterable
    public Iterator<String> iterator() {
        return toList().iterator();
    }

    public int size() {
        return this.values.length;
    }

    public List<String> toList() {
        return Arrays.asList(this.values);
    }

    public String toString() {
        return "CSVRecord [comment='" + this.comment + "', recordNumber=" + this.recordNumber + ", values=" + Arrays.toString(this.values) + "]";
    }

    public String[] values() {
        return this.values;
    }
}
