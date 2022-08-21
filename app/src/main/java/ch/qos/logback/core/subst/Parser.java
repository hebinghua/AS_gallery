package ch.qos.logback.core.subst;

import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.spi.ScanException;
import ch.qos.logback.core.subst.Node;
import ch.qos.logback.core.subst.Token;
import java.util.List;

/* loaded from: classes.dex */
public class Parser {
    public int pointer = 0;
    public final List<Token> tokenList;

    public Parser(List<Token> list) {
        this.tokenList = list;
    }

    public Node parse() throws ScanException {
        List<Token> list = this.tokenList;
        if (list == null || list.isEmpty()) {
            return null;
        }
        return E();
    }

    private Node E() throws ScanException {
        Node T = T();
        if (T == null) {
            return null;
        }
        Node Eopt = Eopt();
        if (Eopt != null) {
            T.append(Eopt);
        }
        return T;
    }

    private Node Eopt() throws ScanException {
        if (peekAtCurentToken() == null) {
            return null;
        }
        return E();
    }

    private Node T() throws ScanException {
        Token peekAtCurentToken = peekAtCurentToken();
        int i = AnonymousClass1.$SwitchMap$ch$qos$logback$core$subst$Token$Type[peekAtCurentToken.type.ordinal()];
        if (i == 1) {
            advanceTokenPointer();
            return makeNewLiteralNode(peekAtCurentToken.payload);
        } else if (i != 2) {
            if (i != 3) {
                return null;
            }
            advanceTokenPointer();
            Node V = V();
            expectCurlyRight(peekAtCurentToken());
            advanceTokenPointer();
            return V;
        } else {
            advanceTokenPointer();
            Node C = C();
            expectCurlyRight(peekAtCurentToken());
            advanceTokenPointer();
            Node makeNewLiteralNode = makeNewLiteralNode(CoreConstants.LEFT_ACCOLADE);
            makeNewLiteralNode.append(C);
            makeNewLiteralNode.append(makeNewLiteralNode(CoreConstants.RIGHT_ACCOLADE));
            return makeNewLiteralNode;
        }
    }

    /* renamed from: ch.qos.logback.core.subst.Parser$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$ch$qos$logback$core$subst$Token$Type;

        static {
            int[] iArr = new int[Token.Type.values().length];
            $SwitchMap$ch$qos$logback$core$subst$Token$Type = iArr;
            try {
                iArr[Token.Type.LITERAL.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$ch$qos$logback$core$subst$Token$Type[Token.Type.CURLY_LEFT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$ch$qos$logback$core$subst$Token$Type[Token.Type.START.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    private Node makeNewLiteralNode(String str) {
        return new Node(Node.Type.LITERAL, str);
    }

    private Node V() throws ScanException {
        Node node = new Node(Node.Type.VARIABLE, E());
        if (isDefaultToken(peekAtCurentToken())) {
            advanceTokenPointer();
            node.defaultPart = E();
        }
        return node;
    }

    private Node C() throws ScanException {
        Node E = E();
        if (isDefaultToken(peekAtCurentToken())) {
            advanceTokenPointer();
            E.append(makeNewLiteralNode(":-"));
            E.append(E());
        }
        return E;
    }

    private boolean isDefaultToken(Token token) {
        return token != null && token.type == Token.Type.DEFAULT;
    }

    public void advanceTokenPointer() {
        this.pointer++;
    }

    public void expectNotNull(Token token, String str) {
        if (token != null) {
            return;
        }
        throw new IllegalArgumentException("All tokens consumed but was expecting \"" + str + "\"");
    }

    public void expectCurlyRight(Token token) throws ScanException {
        expectNotNull(token, "}");
        if (token.type == Token.Type.CURLY_RIGHT) {
            return;
        }
        throw new ScanException("Expecting }");
    }

    public Token peekAtCurentToken() {
        if (this.pointer < this.tokenList.size()) {
            return this.tokenList.get(this.pointer);
        }
        return null;
    }
}
