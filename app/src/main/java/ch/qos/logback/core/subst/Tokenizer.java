package ch.qos.logback.core.subst;

import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.spi.ScanException;
import ch.qos.logback.core.subst.Token;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class Tokenizer {
    public final String pattern;
    public final int patternLength;
    public TokenizerState state = TokenizerState.LITERAL_STATE;
    public int pointer = 0;

    /* loaded from: classes.dex */
    public enum TokenizerState {
        LITERAL_STATE,
        START_STATE,
        DEFAULT_VAL_STATE
    }

    public Tokenizer(String str) {
        this.pattern = str;
        this.patternLength = str.length();
    }

    public List<Token> tokenize() throws ScanException {
        ArrayList arrayList = new ArrayList();
        StringBuilder sb = new StringBuilder();
        while (true) {
            int i = this.pointer;
            if (i >= this.patternLength) {
                break;
            }
            char charAt = this.pattern.charAt(i);
            this.pointer++;
            int i2 = AnonymousClass1.$SwitchMap$ch$qos$logback$core$subst$Tokenizer$TokenizerState[this.state.ordinal()];
            if (i2 == 1) {
                handleLiteralState(charAt, arrayList, sb);
            } else if (i2 == 2) {
                handleStartState(charAt, arrayList, sb);
            } else if (i2 == 3) {
                handleDefaultValueState(charAt, arrayList, sb);
            }
        }
        int i3 = AnonymousClass1.$SwitchMap$ch$qos$logback$core$subst$Tokenizer$TokenizerState[this.state.ordinal()];
        if (i3 == 1) {
            addLiteralToken(arrayList, sb);
        } else if (i3 == 2) {
            sb.append(CoreConstants.DOLLAR);
            addLiteralToken(arrayList, sb);
        } else if (i3 == 3) {
            sb.append(CoreConstants.COLON_CHAR);
            addLiteralToken(arrayList, sb);
        }
        return arrayList;
    }

    /* renamed from: ch.qos.logback.core.subst.Tokenizer$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$ch$qos$logback$core$subst$Tokenizer$TokenizerState;

        static {
            int[] iArr = new int[TokenizerState.values().length];
            $SwitchMap$ch$qos$logback$core$subst$Tokenizer$TokenizerState = iArr;
            try {
                iArr[TokenizerState.LITERAL_STATE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$ch$qos$logback$core$subst$Tokenizer$TokenizerState[TokenizerState.START_STATE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$ch$qos$logback$core$subst$Tokenizer$TokenizerState[TokenizerState.DEFAULT_VAL_STATE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    private void handleDefaultValueState(char c, List<Token> list, StringBuilder sb) {
        if (c == '$') {
            sb.append(CoreConstants.COLON_CHAR);
            addLiteralToken(list, sb);
            sb.setLength(0);
            this.state = TokenizerState.START_STATE;
        } else if (c == '-') {
            list.add(Token.DEFAULT_SEP_TOKEN);
            this.state = TokenizerState.LITERAL_STATE;
        } else if (c == '{') {
            sb.append(CoreConstants.COLON_CHAR);
            addLiteralToken(list, sb);
            sb.setLength(0);
            list.add(Token.CURLY_LEFT_TOKEN);
            this.state = TokenizerState.LITERAL_STATE;
        } else {
            sb.append(CoreConstants.COLON_CHAR);
            sb.append(c);
            this.state = TokenizerState.LITERAL_STATE;
        }
    }

    private void handleStartState(char c, List<Token> list, StringBuilder sb) {
        if (c == '{') {
            list.add(Token.START_TOKEN);
        } else {
            sb.append(CoreConstants.DOLLAR);
            sb.append(c);
        }
        this.state = TokenizerState.LITERAL_STATE;
    }

    private void handleLiteralState(char c, List<Token> list, StringBuilder sb) {
        if (c == '$') {
            addLiteralToken(list, sb);
            sb.setLength(0);
            this.state = TokenizerState.START_STATE;
        } else if (c == ':') {
            addLiteralToken(list, sb);
            sb.setLength(0);
            this.state = TokenizerState.DEFAULT_VAL_STATE;
        } else if (c == '{') {
            addLiteralToken(list, sb);
            list.add(Token.CURLY_LEFT_TOKEN);
            sb.setLength(0);
        } else if (c == '}') {
            addLiteralToken(list, sb);
            list.add(Token.CURLY_RIGHT_TOKEN);
            sb.setLength(0);
        } else {
            sb.append(c);
        }
    }

    private void addLiteralToken(List<Token> list, StringBuilder sb) {
        if (sb.length() == 0) {
            return;
        }
        list.add(new Token(Token.Type.LITERAL, sb.toString()));
    }
}
