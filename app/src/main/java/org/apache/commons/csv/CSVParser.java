package org.apache.commons.csv;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.TreeMap;
import org.apache.commons.csv.Token;

/* loaded from: classes3.dex */
public final class CSVParser implements Iterable<CSVRecord>, Closeable {
    public final long characterOffset;
    public final CSVRecordIterator csvRecordIterator;
    public final CSVFormat format;
    public final Headers headers;
    public final Lexer lexer;
    public final List<String> recordList;
    public long recordNumber;
    public final Token reusableToken;

    /* loaded from: classes3.dex */
    public class CSVRecordIterator implements Iterator<CSVRecord> {
        public CSVRecord current;

        public CSVRecordIterator() {
        }

        public final CSVRecord getNextRecord() {
            try {
                return CSVParser.this.nextRecord();
            } catch (IOException e) {
                throw new IllegalStateException(e.getClass().getSimpleName() + " reading next record: " + e.toString(), e);
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (CSVParser.this.isClosed()) {
                return false;
            }
            if (this.current == null) {
                this.current = getNextRecord();
            }
            return this.current != null;
        }

        @Override // java.util.Iterator
        public CSVRecord next() {
            if (CSVParser.this.isClosed()) {
                throw new NoSuchElementException("CSVParser has been closed");
            }
            CSVRecord cSVRecord = this.current;
            this.current = null;
            if (cSVRecord != null || (cSVRecord = getNextRecord()) != null) {
                return cSVRecord;
            }
            throw new NoSuchElementException("No more CSV records available");
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: classes3.dex */
    public static final class Headers {
        public final Map<String, Integer> headerMap;
        public final List<String> headerNames;

        public Headers(Map<String, Integer> map, List<String> list) {
            this.headerMap = map;
            this.headerNames = list;
        }
    }

    public static CSVParser parse(InputStream inputStream, Charset charset, CSVFormat cSVFormat) throws IOException {
        Objects.requireNonNull(inputStream, "inputStream");
        Objects.requireNonNull(cSVFormat, "format");
        return parse(new InputStreamReader(inputStream, charset), cSVFormat);
    }

    public static CSVParser parse(Reader reader, CSVFormat cSVFormat) throws IOException {
        return new CSVParser(reader, cSVFormat);
    }

    public CSVParser(Reader reader, CSVFormat cSVFormat) throws IOException {
        this(reader, cSVFormat, 0L, 1L);
    }

    public CSVParser(Reader reader, CSVFormat cSVFormat, long j, long j2) throws IOException {
        this.recordList = new ArrayList();
        this.reusableToken = new Token();
        Objects.requireNonNull(reader, "reader");
        Objects.requireNonNull(cSVFormat, "format");
        this.format = cSVFormat.copy();
        this.lexer = new Lexer(cSVFormat, new ExtendedBufferedReader(reader));
        this.csvRecordIterator = new CSVRecordIterator();
        this.headers = createHeaders();
        this.characterOffset = j;
        this.recordNumber = j2 - 1;
    }

    public final void addRecordValue(boolean z) {
        String sb = this.reusableToken.content.toString();
        if (this.format.getTrim()) {
            sb = sb.trim();
        }
        if (!z || !sb.isEmpty() || !this.format.getTrailingDelimiter()) {
            this.recordList.add(handleNull(sb));
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        Lexer lexer = this.lexer;
        if (lexer != null) {
            lexer.close();
        }
    }

    public final Map<String, Integer> createEmptyHeaderMap() {
        return this.format.getIgnoreHeaderCase() ? new TreeMap(String.CASE_INSENSITIVE_ORDER) : new LinkedHashMap();
    }

    public final Headers createHeaders() throws IOException {
        Map<String, Integer> map;
        List unmodifiableList;
        String[] header = this.format.getHeader();
        ArrayList arrayList = null;
        if (header != null) {
            map = createEmptyHeaderMap();
            if (header.length == 0) {
                CSVRecord nextRecord = nextRecord();
                header = nextRecord != null ? nextRecord.values() : null;
            } else if (this.format.getSkipHeaderRecord()) {
                nextRecord();
            }
            if (header != null) {
                for (int i = 0; i < header.length; i++) {
                    String str = header[i];
                    boolean z = str == null || str.trim().isEmpty();
                    if (z && !this.format.getAllowMissingColumnNames()) {
                        throw new IllegalArgumentException("A header name is missing in " + Arrays.toString(header));
                    }
                    if ((str != null && map.containsKey(str)) && !z && !this.format.getAllowDuplicateHeaderNames()) {
                        throw new IllegalArgumentException(String.format("The header contains a duplicate name: \"%s\" in %s. If this is valid then use CSVFormat.withAllowDuplicateHeaderNames().", str, Arrays.toString(header)));
                    }
                    if (str != null) {
                        map.put(str, Integer.valueOf(i));
                        if (arrayList == null) {
                            arrayList = new ArrayList(header.length);
                        }
                        arrayList.add(str);
                    }
                }
            }
        } else {
            map = null;
        }
        if (arrayList == null) {
            unmodifiableList = Collections.emptyList();
        } else {
            unmodifiableList = Collections.unmodifiableList(arrayList);
        }
        return new Headers(map, unmodifiableList);
    }

    public long getCurrentLineNumber() {
        return this.lexer.getCurrentLineNumber();
    }

    public final String handleNull(String str) {
        boolean z = this.reusableToken.isQuoted;
        String nullString = this.format.getNullString();
        boolean isStrictQuoteMode = isStrictQuoteMode();
        if (str.equals(nullString)) {
            if (isStrictQuoteMode && z) {
                return str;
            }
            return null;
        } else if (isStrictQuoteMode && nullString == null && str.isEmpty() && !z) {
            return null;
        } else {
            return str;
        }
    }

    public boolean isClosed() {
        return this.lexer.isClosed();
    }

    public final boolean isStrictQuoteMode() {
        return this.format.getQuoteMode() == QuoteMode.ALL_NON_NULL || this.format.getQuoteMode() == QuoteMode.NON_NUMERIC;
    }

    @Override // java.lang.Iterable
    public Iterator<CSVRecord> iterator() {
        return this.csvRecordIterator;
    }

    public CSVRecord nextRecord() throws IOException {
        this.recordList.clear();
        long characterPosition = this.lexer.getCharacterPosition() + this.characterOffset;
        String str = null;
        StringBuilder sb = null;
        do {
            this.reusableToken.reset();
            this.lexer.nextToken(this.reusableToken);
            int i = AnonymousClass1.$SwitchMap$org$apache$commons$csv$Token$Type[this.reusableToken.type.ordinal()];
            if (i == 1) {
                addRecordValue(false);
            } else if (i == 2) {
                addRecordValue(true);
            } else if (i != 3) {
                if (i == 4) {
                    throw new IOException("(line " + getCurrentLineNumber() + ") invalid parse sequence");
                } else if (i == 5) {
                    if (sb == null) {
                        sb = new StringBuilder();
                    } else {
                        sb.append('\n');
                    }
                    sb.append((CharSequence) this.reusableToken.content);
                    this.reusableToken.type = Token.Type.TOKEN;
                } else {
                    throw new IllegalStateException("Unexpected Token type: " + this.reusableToken.type);
                }
            } else if (this.reusableToken.isReady) {
                addRecordValue(true);
            }
        } while (this.reusableToken.type == Token.Type.TOKEN);
        if (!this.recordList.isEmpty()) {
            this.recordNumber++;
            if (sb != null) {
                str = sb.toString();
            }
            return new CSVRecord(this, (String[]) this.recordList.toArray(Constants.EMPTY_STRING_ARRAY), str, this.recordNumber, characterPosition);
        }
        return null;
    }

    /* renamed from: org.apache.commons.csv.CSVParser$1  reason: invalid class name */
    /* loaded from: classes3.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$org$apache$commons$csv$Token$Type;

        static {
            int[] iArr = new int[Token.Type.values().length];
            $SwitchMap$org$apache$commons$csv$Token$Type = iArr;
            try {
                iArr[Token.Type.TOKEN.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$org$apache$commons$csv$Token$Type[Token.Type.EORECORD.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$org$apache$commons$csv$Token$Type[Token.Type.EOF.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$org$apache$commons$csv$Token$Type[Token.Type.INVALID.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$org$apache$commons$csv$Token$Type[Token.Type.COMMENT.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }
}
