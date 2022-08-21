package org.apache.commons.csv;

import ch.qos.logback.core.CoreConstants;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

/* loaded from: classes3.dex */
public final class CSVFormat implements Serializable {
    public static final CSVFormat DEFAULT;
    public static final CSVFormat EXCEL;
    public static final CSVFormat INFORMIX_UNLOAD;
    public static final CSVFormat INFORMIX_UNLOAD_CSV;
    public static final CSVFormat MONGODB_CSV;
    public static final CSVFormat MONGODB_TSV;
    public static final CSVFormat MYSQL;
    public static final CSVFormat ORACLE;
    public static final CSVFormat POSTGRESQL_CSV;
    public static final CSVFormat POSTGRESQL_TEXT;
    public static final CSVFormat RFC4180;
    public static final CSVFormat TDF;
    private static final long serialVersionUID = 1;
    private final boolean allowDuplicateHeaderNames;
    private final boolean allowMissingColumnNames;
    private final boolean autoFlush;
    private final Character commentMarker;
    private final String delimiter;
    private final Character escapeCharacter;
    private final String[] header;
    private final String[] headerComments;
    private final boolean ignoreEmptyLines;
    private final boolean ignoreHeaderCase;
    private final boolean ignoreSurroundingSpaces;
    private final String nullString;
    private final Character quoteCharacter;
    private final QuoteMode quoteMode;
    private final String quotedNullString;
    private final String recordSeparator;
    private final boolean skipHeaderRecord;
    private final boolean trailingDelimiter;
    private final boolean trim;

    public static boolean isLineBreak(char c) {
        return c == '\n' || c == '\r';
    }

    /* loaded from: classes3.dex */
    public static class Builder {
        public boolean allowDuplicateHeaderNames;
        public boolean allowMissingColumnNames;
        public boolean autoFlush;
        public Character commentMarker;
        public String delimiter;
        public Character escapeCharacter;
        public String[] headerComments;
        public String[] headers;
        public boolean ignoreEmptyLines;
        public boolean ignoreHeaderCase;
        public boolean ignoreSurroundingSpaces;
        public String nullString;
        public Character quoteCharacter;
        public QuoteMode quoteMode;
        public String quotedNullString;
        public String recordSeparator;
        public boolean skipHeaderRecord;
        public boolean trailingDelimiter;
        public boolean trim;

        public static Builder create(CSVFormat cSVFormat) {
            return new Builder(cSVFormat);
        }

        public Builder(CSVFormat cSVFormat) {
            this.delimiter = cSVFormat.delimiter;
            this.quoteCharacter = cSVFormat.quoteCharacter;
            this.quoteMode = cSVFormat.quoteMode;
            this.commentMarker = cSVFormat.commentMarker;
            this.escapeCharacter = cSVFormat.escapeCharacter;
            this.ignoreSurroundingSpaces = cSVFormat.ignoreSurroundingSpaces;
            this.allowMissingColumnNames = cSVFormat.allowMissingColumnNames;
            this.ignoreEmptyLines = cSVFormat.ignoreEmptyLines;
            this.recordSeparator = cSVFormat.recordSeparator;
            this.nullString = cSVFormat.nullString;
            this.headerComments = cSVFormat.headerComments;
            this.headers = cSVFormat.header;
            this.skipHeaderRecord = cSVFormat.skipHeaderRecord;
            this.ignoreHeaderCase = cSVFormat.ignoreHeaderCase;
            this.trailingDelimiter = cSVFormat.trailingDelimiter;
            this.trim = cSVFormat.trim;
            this.autoFlush = cSVFormat.autoFlush;
            this.quotedNullString = cSVFormat.quotedNullString;
            this.allowDuplicateHeaderNames = cSVFormat.allowDuplicateHeaderNames;
        }

        public CSVFormat build() {
            return new CSVFormat(this);
        }

        public Builder setAllowMissingColumnNames(boolean z) {
            this.allowMissingColumnNames = z;
            return this;
        }

        public Builder setDelimiter(char c) {
            return setDelimiter(String.valueOf(c));
        }

        public Builder setDelimiter(String str) {
            if (CSVFormat.containsLineBreak(str)) {
                throw new IllegalArgumentException("The delimiter cannot be a line break");
            }
            this.delimiter = str;
            return this;
        }

        public Builder setEscape(char c) {
            setEscape(Character.valueOf(c));
            return this;
        }

        public Builder setEscape(Character ch2) {
            if (CSVFormat.isLineBreak(ch2)) {
                throw new IllegalArgumentException("The escape character cannot be a line break");
            }
            this.escapeCharacter = ch2;
            return this;
        }

        public Builder setIgnoreEmptyLines(boolean z) {
            this.ignoreEmptyLines = z;
            return this;
        }

        public Builder setIgnoreSurroundingSpaces(boolean z) {
            this.ignoreSurroundingSpaces = z;
            return this;
        }

        public Builder setNullString(String str) {
            this.nullString = str;
            this.quotedNullString = this.quoteCharacter + str + this.quoteCharacter;
            return this;
        }

        public Builder setQuote(Character ch2) {
            if (CSVFormat.isLineBreak(ch2)) {
                throw new IllegalArgumentException("The quoteChar cannot be a line break");
            }
            this.quoteCharacter = ch2;
            return this;
        }

        public Builder setQuoteMode(QuoteMode quoteMode) {
            this.quoteMode = quoteMode;
            return this;
        }

        public Builder setRecordSeparator(char c) {
            this.recordSeparator = String.valueOf(c);
            return this;
        }

        public Builder setRecordSeparator(String str) {
            this.recordSeparator = str;
            return this;
        }

        public Builder setSkipHeaderRecord(boolean z) {
            this.skipHeaderRecord = z;
            return this;
        }

        public Builder setTrim(boolean z) {
            this.trim = z;
            return this;
        }
    }

    static {
        Character ch2 = Constants.DOUBLE_QUOTE_CHAR;
        CSVFormat cSVFormat = new CSVFormat(",", ch2, null, null, null, false, true, "\r\n", null, null, null, false, false, false, false, false, false, true);
        DEFAULT = cSVFormat;
        EXCEL = cSVFormat.builder().setIgnoreEmptyLines(false).setAllowMissingColumnNames(true).build();
        INFORMIX_UNLOAD = cSVFormat.builder().setDelimiter('|').setEscape(CoreConstants.ESCAPE_CHAR).setQuote(ch2).setRecordSeparator('\n').build();
        INFORMIX_UNLOAD_CSV = cSVFormat.builder().setDelimiter(",").setQuote(ch2).setRecordSeparator('\n').build();
        Builder quote = cSVFormat.builder().setDelimiter(",").setEscape(ch2).setQuote(ch2);
        QuoteMode quoteMode = QuoteMode.MINIMAL;
        MONGODB_CSV = quote.setQuoteMode(quoteMode).setSkipHeaderRecord(false).build();
        MONGODB_TSV = cSVFormat.builder().setDelimiter('\t').setEscape(ch2).setQuote(ch2).setQuoteMode(quoteMode).setSkipHeaderRecord(false).build();
        Builder nullString = cSVFormat.builder().setDelimiter('\t').setEscape(CoreConstants.ESCAPE_CHAR).setIgnoreEmptyLines(false).setQuote(null).setRecordSeparator('\n').setNullString("\\N");
        QuoteMode quoteMode2 = QuoteMode.ALL_NON_NULL;
        MYSQL = nullString.setQuoteMode(quoteMode2).build();
        ORACLE = cSVFormat.builder().setDelimiter(",").setEscape(CoreConstants.ESCAPE_CHAR).setIgnoreEmptyLines(false).setQuote(ch2).setNullString("\\N").setTrim(true).setRecordSeparator(System.lineSeparator()).setQuoteMode(quoteMode).build();
        POSTGRESQL_CSV = cSVFormat.builder().setDelimiter(",").setEscape(ch2).setIgnoreEmptyLines(false).setQuote(ch2).setRecordSeparator('\n').setNullString("").setQuoteMode(quoteMode2).build();
        POSTGRESQL_TEXT = cSVFormat.builder().setDelimiter('\t').setEscape(CoreConstants.ESCAPE_CHAR).setIgnoreEmptyLines(false).setQuote(ch2).setRecordSeparator('\n').setNullString("\\N").setQuoteMode(quoteMode2).build();
        RFC4180 = cSVFormat.builder().setIgnoreEmptyLines(false).build();
        TDF = cSVFormat.builder().setDelimiter('\t').setIgnoreSurroundingSpaces(true).build();
    }

    @SafeVarargs
    public static <T> T[] clone(T... tArr) {
        if (tArr == null) {
            return null;
        }
        return (T[]) ((Object[]) tArr.clone());
    }

    public static boolean contains(String str, char c) {
        Objects.requireNonNull(str, "source");
        return str.indexOf(c) >= 0;
    }

    public static boolean containsLineBreak(String str) {
        return contains(str, '\r') || contains(str, '\n');
    }

    public static boolean isLineBreak(Character ch2) {
        return ch2 != null && isLineBreak(ch2.charValue());
    }

    public static String[] toStringArray(Object[] objArr) {
        if (objArr == null) {
            return null;
        }
        String[] strArr = new String[objArr.length];
        for (int i = 0; i < objArr.length; i++) {
            strArr[i] = Objects.toString(objArr[i], null);
        }
        return strArr;
    }

    public CSVFormat(Builder builder) {
        this.delimiter = builder.delimiter;
        this.quoteCharacter = builder.quoteCharacter;
        this.quoteMode = builder.quoteMode;
        this.commentMarker = builder.commentMarker;
        this.escapeCharacter = builder.escapeCharacter;
        this.ignoreSurroundingSpaces = builder.ignoreSurroundingSpaces;
        this.allowMissingColumnNames = builder.allowMissingColumnNames;
        this.ignoreEmptyLines = builder.ignoreEmptyLines;
        this.recordSeparator = builder.recordSeparator;
        this.nullString = builder.nullString;
        this.headerComments = builder.headerComments;
        this.header = builder.headers;
        this.skipHeaderRecord = builder.skipHeaderRecord;
        this.ignoreHeaderCase = builder.ignoreHeaderCase;
        this.trailingDelimiter = builder.trailingDelimiter;
        this.trim = builder.trim;
        this.autoFlush = builder.autoFlush;
        this.quotedNullString = builder.quotedNullString;
        this.allowDuplicateHeaderNames = builder.allowDuplicateHeaderNames;
        validate();
    }

    public CSVFormat(String str, Character ch2, QuoteMode quoteMode, Character ch3, Character ch4, boolean z, boolean z2, String str2, String str3, Object[] objArr, String[] strArr, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7, boolean z8, boolean z9) {
        this.delimiter = str;
        this.quoteCharacter = ch2;
        this.quoteMode = quoteMode;
        this.commentMarker = ch3;
        this.escapeCharacter = ch4;
        this.ignoreSurroundingSpaces = z;
        this.allowMissingColumnNames = z4;
        this.ignoreEmptyLines = z2;
        this.recordSeparator = str2;
        this.nullString = str3;
        this.headerComments = toStringArray(objArr);
        this.header = (String[]) clone(strArr);
        this.skipHeaderRecord = z3;
        this.ignoreHeaderCase = z5;
        this.trailingDelimiter = z7;
        this.trim = z6;
        this.autoFlush = z8;
        this.quotedNullString = ch2 + str3 + ch2;
        this.allowDuplicateHeaderNames = z9;
        validate();
    }

    public Builder builder() {
        return Builder.create(this);
    }

    public CSVFormat copy() {
        return builder().build();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || CSVFormat.class != obj.getClass()) {
            return false;
        }
        CSVFormat cSVFormat = (CSVFormat) obj;
        return this.allowDuplicateHeaderNames == cSVFormat.allowDuplicateHeaderNames && this.allowMissingColumnNames == cSVFormat.allowMissingColumnNames && this.autoFlush == cSVFormat.autoFlush && Objects.equals(this.commentMarker, cSVFormat.commentMarker) && Objects.equals(this.delimiter, cSVFormat.delimiter) && Objects.equals(this.escapeCharacter, cSVFormat.escapeCharacter) && Arrays.equals(this.header, cSVFormat.header) && Arrays.equals(this.headerComments, cSVFormat.headerComments) && this.ignoreEmptyLines == cSVFormat.ignoreEmptyLines && this.ignoreHeaderCase == cSVFormat.ignoreHeaderCase && this.ignoreSurroundingSpaces == cSVFormat.ignoreSurroundingSpaces && Objects.equals(this.nullString, cSVFormat.nullString) && Objects.equals(this.quoteCharacter, cSVFormat.quoteCharacter) && this.quoteMode == cSVFormat.quoteMode && Objects.equals(this.quotedNullString, cSVFormat.quotedNullString) && Objects.equals(this.recordSeparator, cSVFormat.recordSeparator) && this.skipHeaderRecord == cSVFormat.skipHeaderRecord && this.trailingDelimiter == cSVFormat.trailingDelimiter && this.trim == cSVFormat.trim;
    }

    public boolean getAllowDuplicateHeaderNames() {
        return this.allowDuplicateHeaderNames;
    }

    public boolean getAllowMissingColumnNames() {
        return this.allowMissingColumnNames;
    }

    public Character getCommentMarker() {
        return this.commentMarker;
    }

    public String getDelimiterString() {
        return this.delimiter;
    }

    public Character getEscapeCharacter() {
        return this.escapeCharacter;
    }

    public String[] getHeader() {
        String[] strArr = this.header;
        if (strArr != null) {
            return (String[]) strArr.clone();
        }
        return null;
    }

    public boolean getIgnoreEmptyLines() {
        return this.ignoreEmptyLines;
    }

    public boolean getIgnoreHeaderCase() {
        return this.ignoreHeaderCase;
    }

    public boolean getIgnoreSurroundingSpaces() {
        return this.ignoreSurroundingSpaces;
    }

    public String getNullString() {
        return this.nullString;
    }

    public Character getQuoteCharacter() {
        return this.quoteCharacter;
    }

    public QuoteMode getQuoteMode() {
        return this.quoteMode;
    }

    public boolean getSkipHeaderRecord() {
        return this.skipHeaderRecord;
    }

    public boolean getTrailingDelimiter() {
        return this.trailingDelimiter;
    }

    public boolean getTrim() {
        return this.trim;
    }

    public int hashCode() {
        return ((((Arrays.hashCode(this.header) + 31) * 31) + Arrays.hashCode(this.headerComments)) * 31) + Objects.hash(Boolean.valueOf(this.allowDuplicateHeaderNames), Boolean.valueOf(this.allowMissingColumnNames), Boolean.valueOf(this.autoFlush), this.commentMarker, this.delimiter, this.escapeCharacter, Boolean.valueOf(this.ignoreEmptyLines), Boolean.valueOf(this.ignoreHeaderCase), Boolean.valueOf(this.ignoreSurroundingSpaces), this.nullString, this.quoteCharacter, this.quoteMode, this.quotedNullString, this.recordSeparator, Boolean.valueOf(this.skipHeaderRecord), Boolean.valueOf(this.trailingDelimiter), Boolean.valueOf(this.trim));
    }

    public boolean isCommentMarkerSet() {
        return this.commentMarker != null;
    }

    public boolean isEscapeCharacterSet() {
        return this.escapeCharacter != null;
    }

    public boolean isNullStringSet() {
        return this.nullString != null;
    }

    public boolean isQuoteCharacterSet() {
        return this.quoteCharacter != null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Delimiter=<");
        sb.append(this.delimiter);
        sb.append('>');
        if (isEscapeCharacterSet()) {
            sb.append(' ');
            sb.append("Escape=<");
            sb.append(this.escapeCharacter);
            sb.append('>');
        }
        if (isQuoteCharacterSet()) {
            sb.append(' ');
            sb.append("QuoteChar=<");
            sb.append(this.quoteCharacter);
            sb.append('>');
        }
        if (this.quoteMode != null) {
            sb.append(' ');
            sb.append("QuoteMode=<");
            sb.append(this.quoteMode);
            sb.append('>');
        }
        if (isCommentMarkerSet()) {
            sb.append(' ');
            sb.append("CommentStart=<");
            sb.append(this.commentMarker);
            sb.append('>');
        }
        if (isNullStringSet()) {
            sb.append(' ');
            sb.append("NullString=<");
            sb.append(this.nullString);
            sb.append('>');
        }
        if (this.recordSeparator != null) {
            sb.append(' ');
            sb.append("RecordSeparator=<");
            sb.append(this.recordSeparator);
            sb.append('>');
        }
        if (getIgnoreEmptyLines()) {
            sb.append(" EmptyLines:ignored");
        }
        if (getIgnoreSurroundingSpaces()) {
            sb.append(" SurroundingSpaces:ignored");
        }
        if (getIgnoreHeaderCase()) {
            sb.append(" IgnoreHeaderCase:ignored");
        }
        sb.append(" SkipHeaderRecord:");
        sb.append(this.skipHeaderRecord);
        if (this.headerComments != null) {
            sb.append(' ');
            sb.append("HeaderComments:");
            sb.append(Arrays.toString(this.headerComments));
        }
        if (this.header != null) {
            sb.append(' ');
            sb.append("Header:");
            sb.append(Arrays.toString(this.header));
        }
        return sb.toString();
    }

    public final void validate() throws IllegalArgumentException {
        String[] strArr;
        if (containsLineBreak(this.delimiter)) {
            throw new IllegalArgumentException("The delimiter cannot be a line break");
        }
        Character ch2 = this.quoteCharacter;
        if (ch2 != null && contains(this.delimiter, ch2.charValue())) {
            throw new IllegalArgumentException("The quoteChar character and the delimiter cannot be the same ('" + this.quoteCharacter + "')");
        }
        Character ch3 = this.escapeCharacter;
        if (ch3 != null && contains(this.delimiter, ch3.charValue())) {
            throw new IllegalArgumentException("The escape character and the delimiter cannot be the same ('" + this.escapeCharacter + "')");
        }
        Character ch4 = this.commentMarker;
        if (ch4 != null && contains(this.delimiter, ch4.charValue())) {
            throw new IllegalArgumentException("The comment start character and the delimiter cannot be the same ('" + this.commentMarker + "')");
        }
        Character ch5 = this.quoteCharacter;
        if (ch5 != null && ch5.equals(this.commentMarker)) {
            throw new IllegalArgumentException("The comment start character and the quoteChar cannot be the same ('" + this.commentMarker + "')");
        }
        Character ch6 = this.escapeCharacter;
        if (ch6 != null && ch6.equals(this.commentMarker)) {
            throw new IllegalArgumentException("The comment start and the escape character cannot be the same ('" + this.commentMarker + "')");
        } else if (this.escapeCharacter == null && this.quoteMode == QuoteMode.NONE) {
            throw new IllegalArgumentException("No quotes mode set but no escape character is set");
        } else {
            if (this.header == null || this.allowDuplicateHeaderNames) {
                return;
            }
            HashSet hashSet = new HashSet();
            for (String str : this.header) {
                if (!hashSet.add(str)) {
                    throw new IllegalArgumentException("The header contains a duplicate entry: '" + str + "' in " + Arrays.toString(this.header));
                }
            }
        }
    }
}
