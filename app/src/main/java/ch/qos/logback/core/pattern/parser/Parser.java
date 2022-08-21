package ch.qos.logback.core.pattern.parser;

import ch.qos.logback.core.pattern.Converter;
import ch.qos.logback.core.pattern.FormatInfo;
import ch.qos.logback.core.pattern.IdentityCompositeConverter;
import ch.qos.logback.core.pattern.ReplacingCompositeConverter;
import ch.qos.logback.core.pattern.util.IEscapeUtil;
import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.ScanException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class Parser<E> extends ContextAwareBase {
    public static final Map<String, String> DEFAULT_COMPOSITE_CONVERTER_MAP;
    public static final String MISSING_RIGHT_PARENTHESIS = "http://logback.qos.ch/codes.html#missingRightParenthesis";
    public static final String REPLACE_CONVERTER_WORD = "replace";
    public int pointer;
    public final List<Token> tokenList;

    static {
        HashMap hashMap = new HashMap();
        DEFAULT_COMPOSITE_CONVERTER_MAP = hashMap;
        hashMap.put(Token.BARE_COMPOSITE_KEYWORD_TOKEN.getValue().toString(), IdentityCompositeConverter.class.getName());
        hashMap.put(REPLACE_CONVERTER_WORD, ReplacingCompositeConverter.class.getName());
    }

    public Parser(TokenStream tokenStream) throws ScanException {
        this.pointer = 0;
        this.tokenList = tokenStream.tokenize();
    }

    public Parser(String str) throws ScanException {
        this(str, new RegularEscapeUtil());
    }

    public Parser(String str, IEscapeUtil iEscapeUtil) throws ScanException {
        this.pointer = 0;
        try {
            this.tokenList = new TokenStream(str, iEscapeUtil).tokenize();
        } catch (IllegalArgumentException e) {
            throw new ScanException("Failed to initialize Parser", e);
        }
    }

    public Converter<E> compile(Node node, Map<String, String> map) {
        Compiler compiler = new Compiler(node, map);
        compiler.setContext(this.context);
        return compiler.compile();
    }

    public Node parse() throws ScanException {
        return E();
    }

    public Node E() throws ScanException {
        Node T = T();
        if (T == null) {
            return null;
        }
        Node Eopt = Eopt();
        if (Eopt != null) {
            T.setNext(Eopt);
        }
        return T;
    }

    public Node Eopt() throws ScanException {
        if (getCurentToken() == null) {
            return null;
        }
        return E();
    }

    public Node T() throws ScanException {
        Token curentToken = getCurentToken();
        expectNotNull(curentToken, "a LITERAL or '%'");
        int type = curentToken.getType();
        if (type != 37) {
            if (type != 1000) {
                return null;
            }
            advanceTokenPointer();
            return new Node(0, curentToken.getValue());
        }
        advanceTokenPointer();
        Token curentToken2 = getCurentToken();
        expectNotNull(curentToken2, "a FORMAT_MODIFIER, SIMPLE_KEYWORD or COMPOUND_KEYWORD");
        if (curentToken2.getType() == 1002) {
            FormatInfo valueOf = FormatInfo.valueOf(curentToken2.getValue());
            advanceTokenPointer();
            FormattingNode C = C();
            C.setFormatInfo(valueOf);
            return C;
        }
        return C();
    }

    public FormattingNode C() throws ScanException {
        Token curentToken = getCurentToken();
        expectNotNull(curentToken, "a LEFT_PARENTHESIS or KEYWORD");
        int type = curentToken.getType();
        if (type != 1004) {
            if (type == 1005) {
                advanceTokenPointer();
                return COMPOSITE(curentToken.getValue().toString());
            }
            throw new IllegalStateException("Unexpected token " + curentToken);
        }
        return SINGLE();
    }

    public FormattingNode SINGLE() throws ScanException {
        SimpleKeywordNode simpleKeywordNode = new SimpleKeywordNode(getNextToken().getValue());
        Token curentToken = getCurentToken();
        if (curentToken != null && curentToken.getType() == 1006) {
            simpleKeywordNode.setOptions(curentToken.getOptionsList());
            advanceTokenPointer();
        }
        return simpleKeywordNode;
    }

    public FormattingNode COMPOSITE(String str) throws ScanException {
        CompositeNode compositeNode = new CompositeNode(str);
        compositeNode.setChildNode(E());
        Token nextToken = getNextToken();
        if (nextToken == null || nextToken.getType() != 41) {
            String str2 = "Expecting RIGHT_PARENTHESIS token but got " + nextToken;
            addError(str2);
            addError("See also http://logback.qos.ch/codes.html#missingRightParenthesis");
            throw new ScanException(str2);
        }
        Token curentToken = getCurentToken();
        if (curentToken != null && curentToken.getType() == 1006) {
            compositeNode.setOptions(curentToken.getOptionsList());
            advanceTokenPointer();
        }
        return compositeNode;
    }

    public Token getNextToken() {
        if (this.pointer < this.tokenList.size()) {
            List<Token> list = this.tokenList;
            int i = this.pointer;
            this.pointer = i + 1;
            return list.get(i);
        }
        return null;
    }

    public Token getCurentToken() {
        if (this.pointer < this.tokenList.size()) {
            return this.tokenList.get(this.pointer);
        }
        return null;
    }

    public void advanceTokenPointer() {
        this.pointer++;
    }

    public void expectNotNull(Token token, String str) {
        if (token != null) {
            return;
        }
        throw new IllegalStateException("All tokens consumed but was expecting " + str);
    }
}
