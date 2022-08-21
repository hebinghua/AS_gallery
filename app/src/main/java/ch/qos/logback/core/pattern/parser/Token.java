package ch.qos.logback.core.pattern.parser;

import java.util.List;

/* loaded from: classes.dex */
class Token {
    public static final int COMPOSITE_KEYWORD = 1005;
    public static final int CURLY_LEFT = 123;
    public static final int CURLY_RIGHT = 125;
    public static final int DOT = 46;
    public static final int EOF = Integer.MAX_VALUE;
    public static final int FORMAT_MODIFIER = 1002;
    public static final int LITERAL = 1000;
    public static final int MINUS = 45;
    public static final int OPTION = 1006;
    public static final int PERCENT = 37;
    public static final int RIGHT_PARENTHESIS = 41;
    public static final int SIMPLE_KEYWORD = 1004;
    private final List<String> optionsList;
    private final int type;
    private final String value;
    public static Token EOF_TOKEN = new Token(Integer.MAX_VALUE, "EOF");
    public static Token RIGHT_PARENTHESIS_TOKEN = new Token(41);
    public static Token BARE_COMPOSITE_KEYWORD_TOKEN = new Token(1005, "BARE");
    public static Token PERCENT_TOKEN = new Token(37);

    public Token(int i) {
        this(i, null, null);
    }

    public Token(int i, String str) {
        this(i, str, null);
    }

    public Token(int i, List<String> list) {
        this(i, null, list);
    }

    public Token(int i, String str, List<String> list) {
        this.type = i;
        this.value = str;
        this.optionsList = list;
    }

    public int getType() {
        return this.type;
    }

    public String getValue() {
        return this.value;
    }

    public List<String> getOptionsList() {
        return this.optionsList;
    }

    public String toString() {
        String str;
        int i = this.type;
        if (i == 37) {
            str = "%";
        } else if (i == 41) {
            str = "RIGHT_PARENTHESIS";
        } else if (i == 1000) {
            str = "LITERAL";
        } else if (i != 1002) {
            switch (i) {
                case 1004:
                    str = "SIMPLE_KEYWORD";
                    break;
                case 1005:
                    str = "COMPOSITE_KEYWORD";
                    break;
                case 1006:
                    str = "OPTION";
                    break;
                default:
                    str = "UNKNOWN";
                    break;
            }
        } else {
            str = "FormatModifier";
        }
        if (this.value == null) {
            return "Token(" + str + ")";
        }
        return "Token(" + str + ", \"" + this.value + "\")";
    }

    public int hashCode() {
        int i = this.type * 29;
        String str = this.value;
        return i + (str != null ? str.hashCode() : 0);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Token)) {
            return false;
        }
        Token token = (Token) obj;
        if (this.type != token.type) {
            return false;
        }
        String str = this.value;
        String str2 = token.value;
        return str == null ? str2 == null : str.equals(str2);
    }
}
