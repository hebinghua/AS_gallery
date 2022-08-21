package org.apache.commons.csv;

import java.io.Closeable;
import java.io.IOException;
import org.apache.commons.csv.Token;

/* loaded from: classes3.dex */
public final class Lexer implements Closeable {
    public static final String CR_STRING = Character.toString('\r');
    public static final String LF_STRING = Character.toString('\n');
    public final char commentStart;
    public final char[] delimiter;
    public final char[] delimiterBuf;
    public final char escape;
    public final char[] escapeDelimiterBuf;
    public String firstEol;
    public final boolean ignoreEmptyLines;
    public final boolean ignoreSurroundingSpaces;
    public final char quoteChar;
    public final ExtendedBufferedReader reader;

    public boolean isEndOfFile(int i) {
        return i == -1;
    }

    public boolean isStartOfLine(int i) {
        return i == 10 || i == 13 || i == -2;
    }

    public Lexer(CSVFormat cSVFormat, ExtendedBufferedReader extendedBufferedReader) {
        this.reader = extendedBufferedReader;
        char[] charArray = cSVFormat.getDelimiterString().toCharArray();
        this.delimiter = charArray;
        this.escape = mapNullToDisabled(cSVFormat.getEscapeCharacter());
        this.quoteChar = mapNullToDisabled(cSVFormat.getQuoteCharacter());
        this.commentStart = mapNullToDisabled(cSVFormat.getCommentMarker());
        this.ignoreSurroundingSpaces = cSVFormat.getIgnoreSurroundingSpaces();
        this.ignoreEmptyLines = cSVFormat.getIgnoreEmptyLines();
        this.delimiterBuf = new char[charArray.length - 1];
        this.escapeDelimiterBuf = new char[(charArray.length * 2) - 1];
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.reader.close();
    }

    public long getCharacterPosition() {
        return this.reader.getPosition();
    }

    public long getCurrentLineNumber() {
        return this.reader.getCurrentLineNumber();
    }

    public boolean isClosed() {
        return this.reader.isClosed();
    }

    public boolean isCommentStart(int i) {
        return i == this.commentStart;
    }

    public boolean isDelimiter(int i) throws IOException {
        char c;
        char[] cArr = this.delimiter;
        if (i != cArr[0]) {
            return false;
        }
        if (cArr.length == 1) {
            return true;
        }
        this.reader.lookAhead(this.delimiterBuf);
        int i2 = 0;
        do {
            char[] cArr2 = this.delimiterBuf;
            if (i2 >= cArr2.length) {
                return this.reader.read(cArr2, 0, cArr2.length) != -1;
            }
            c = cArr2[i2];
            i2++;
        } while (c == this.delimiter[i2]);
        return false;
    }

    public boolean isEscape(int i) {
        return i == this.escape;
    }

    public boolean isEscapeDelimiter() throws IOException {
        this.reader.lookAhead(this.escapeDelimiterBuf);
        if (this.escapeDelimiterBuf[0] != this.delimiter[0]) {
            return false;
        }
        int i = 1;
        while (true) {
            char[] cArr = this.delimiter;
            if (i < cArr.length) {
                char[] cArr2 = this.escapeDelimiterBuf;
                int i2 = i * 2;
                if (cArr2[i2] != cArr[i] || cArr2[i2 - 1] != this.escape) {
                    break;
                }
                i++;
            } else {
                ExtendedBufferedReader extendedBufferedReader = this.reader;
                char[] cArr3 = this.escapeDelimiterBuf;
                return extendedBufferedReader.read(cArr3, 0, cArr3.length) != -1;
            }
        }
        return false;
    }

    public final boolean isMetaChar(int i) {
        return i == this.escape || i == this.quoteChar || i == this.commentStart;
    }

    public boolean isQuoteChar(int i) {
        return i == this.quoteChar;
    }

    public final char mapNullToDisabled(Character ch2) {
        if (ch2 == null) {
            return (char) 65534;
        }
        return ch2.charValue();
    }

    public Token nextToken(Token token) throws IOException {
        int lastChar = this.reader.getLastChar();
        int read = this.reader.read();
        boolean readEndOfLine = readEndOfLine(read);
        if (this.ignoreEmptyLines) {
            while (readEndOfLine && isStartOfLine(lastChar)) {
                int read2 = this.reader.read();
                readEndOfLine = readEndOfLine(read2);
                if (isEndOfFile(read2)) {
                    token.type = Token.Type.EOF;
                    return token;
                }
                int i = read;
                read = read2;
                lastChar = i;
            }
        }
        if (isEndOfFile(lastChar) || (!isDelimiter(lastChar) && isEndOfFile(read))) {
            token.type = Token.Type.EOF;
            return token;
        } else if (isStartOfLine(lastChar) && isCommentStart(read)) {
            String readLine = this.reader.readLine();
            if (readLine == null) {
                token.type = Token.Type.EOF;
                return token;
            }
            token.content.append(readLine.trim());
            token.type = Token.Type.COMMENT;
            return token;
        } else {
            while (token.type == Token.Type.INVALID) {
                if (this.ignoreSurroundingSpaces) {
                    while (Character.isWhitespace((char) read) && !isDelimiter(read) && !readEndOfLine) {
                        read = this.reader.read();
                        readEndOfLine = readEndOfLine(read);
                    }
                }
                if (isDelimiter(read)) {
                    token.type = Token.Type.TOKEN;
                } else if (readEndOfLine) {
                    token.type = Token.Type.EORECORD;
                } else if (isQuoteChar(read)) {
                    parseEncapsulatedToken(token);
                } else if (isEndOfFile(read)) {
                    token.type = Token.Type.EOF;
                    token.isReady = true;
                } else {
                    parseSimpleToken(token, read);
                }
            }
            return token;
        }
    }

    public final Token parseEncapsulatedToken(Token token) throws IOException {
        int read;
        token.isQuoted = true;
        long currentLineNumber = getCurrentLineNumber();
        while (true) {
            int read2 = this.reader.read();
            if (isEscape(read2)) {
                if (isEscapeDelimiter()) {
                    token.content.append(this.delimiter);
                } else {
                    int readEscape = readEscape();
                    if (readEscape == -1) {
                        StringBuilder sb = token.content;
                        sb.append((char) read2);
                        sb.append((char) this.reader.getLastChar());
                    } else {
                        token.content.append((char) readEscape);
                    }
                }
            } else if (isQuoteChar(read2)) {
                if (isQuoteChar(this.reader.lookAhead())) {
                    token.content.append((char) this.reader.read());
                } else {
                    do {
                        read = this.reader.read();
                        if (isDelimiter(read)) {
                            token.type = Token.Type.TOKEN;
                            return token;
                        } else if (isEndOfFile(read)) {
                            token.type = Token.Type.EOF;
                            token.isReady = true;
                            return token;
                        } else if (readEndOfLine(read)) {
                            token.type = Token.Type.EORECORD;
                            return token;
                        }
                    } while (Character.isWhitespace((char) read));
                    throw new IOException("(line " + getCurrentLineNumber() + ") invalid char between encapsulated token and delimiter");
                }
            } else if (isEndOfFile(read2)) {
                throw new IOException("(startline " + currentLineNumber + ") EOF reached before encapsulated token finished");
            } else {
                token.content.append((char) read2);
            }
        }
    }

    public final Token parseSimpleToken(Token token, int i) throws IOException {
        while (true) {
            if (readEndOfLine(i)) {
                token.type = Token.Type.EORECORD;
                break;
            } else if (isEndOfFile(i)) {
                token.type = Token.Type.EOF;
                token.isReady = true;
                break;
            } else if (isDelimiter(i)) {
                token.type = Token.Type.TOKEN;
                break;
            } else if (isEscape(i)) {
                if (isEscapeDelimiter()) {
                    token.content.append(this.delimiter);
                } else {
                    int readEscape = readEscape();
                    if (readEscape == -1) {
                        StringBuilder sb = token.content;
                        sb.append((char) i);
                        sb.append((char) this.reader.getLastChar());
                    } else {
                        token.content.append((char) readEscape);
                    }
                }
                i = this.reader.read();
            } else {
                token.content.append((char) i);
                i = this.reader.read();
            }
        }
        if (this.ignoreSurroundingSpaces) {
            trimTrailingSpaces(token.content);
        }
        return token;
    }

    public boolean readEndOfLine(int i) throws IOException {
        if (i == 13 && this.reader.lookAhead() == 10) {
            i = this.reader.read();
            if (this.firstEol == null) {
                this.firstEol = "\r\n";
            }
        }
        if (this.firstEol == null) {
            if (i == 10) {
                this.firstEol = LF_STRING;
            } else if (i == 13) {
                this.firstEol = CR_STRING;
            }
        }
        return i == 10 || i == 13;
    }

    public int readEscape() throws IOException {
        int read = this.reader.read();
        if (read != -1) {
            if (read == 98) {
                return 8;
            }
            if (read == 102) {
                return 12;
            }
            if (read == 110) {
                return 10;
            }
            if (read == 114) {
                return 13;
            }
            if (read == 116) {
                return 9;
            }
            if (read != 12 && read != 13) {
                switch (read) {
                    case 8:
                    case 9:
                    case 10:
                        break;
                    default:
                        if (!isMetaChar(read)) {
                            return -1;
                        }
                        return read;
                }
            }
            return read;
        }
        throw new IOException("EOF whilst processing escape sequence");
    }

    public void trimTrailingSpaces(StringBuilder sb) {
        int length = sb.length();
        while (length > 0) {
            int i = length - 1;
            if (!Character.isWhitespace(sb.charAt(i))) {
                break;
            }
            length = i;
        }
        if (length != sb.length()) {
            sb.setLength(length);
        }
    }
}
