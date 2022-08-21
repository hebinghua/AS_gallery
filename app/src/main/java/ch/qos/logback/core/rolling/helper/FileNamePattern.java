package ch.qos.logback.core.rolling.helper;

import ch.qos.logback.core.Context;
import ch.qos.logback.core.pattern.Converter;
import ch.qos.logback.core.pattern.ConverterUtil;
import ch.qos.logback.core.pattern.LiteralConverter;
import ch.qos.logback.core.pattern.parser.Parser;
import ch.qos.logback.core.pattern.util.AlmostAsIsEscapeUtil;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.ScanException;
import com.xiaomi.stat.b.h;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class FileNamePattern extends ContextAwareBase {
    public static final Map<String, String> CONVERTER_MAP;
    public Converter<Object> headTokenConverter;
    public String pattern;

    static {
        HashMap hashMap = new HashMap();
        CONVERTER_MAP = hashMap;
        hashMap.put(IntegerTokenConverter.CONVERTER_KEY, IntegerTokenConverter.class.getName());
        hashMap.put("d", DateTokenConverter.class.getName());
    }

    public FileNamePattern(String str, Context context) {
        setPattern(FileFilterUtil.slashify(str));
        setContext(context);
        parse();
        ConverterUtil.startConverters(this.headTokenConverter);
    }

    public void parse() {
        try {
            Parser parser = new Parser(escapeRightParantesis(this.pattern), new AlmostAsIsEscapeUtil());
            parser.setContext(this.context);
            this.headTokenConverter = parser.compile(parser.parse(), CONVERTER_MAP);
        } catch (ScanException e) {
            addError("Failed to parse pattern \"" + this.pattern + "\".", e);
        }
    }

    public String escapeRightParantesis(String str) {
        return this.pattern.replace(")", "\\)");
    }

    public String toString() {
        return this.pattern;
    }

    public int hashCode() {
        String str = this.pattern;
        return 31 + (str == null ? 0 : str.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        FileNamePattern fileNamePattern = (FileNamePattern) obj;
        String str = this.pattern;
        if (str == null) {
            if (fileNamePattern.pattern != null) {
                return false;
            }
        } else if (!str.equals(fileNamePattern.pattern)) {
            return false;
        }
        return true;
    }

    public DateTokenConverter<Object> getPrimaryDateTokenConverter() {
        for (Converter<Object> converter = this.headTokenConverter; converter != null; converter = converter.getNext()) {
            if (converter instanceof DateTokenConverter) {
                DateTokenConverter<Object> dateTokenConverter = (DateTokenConverter) converter;
                if (dateTokenConverter.isPrimary()) {
                    return dateTokenConverter;
                }
            }
        }
        return null;
    }

    public IntegerTokenConverter getIntegerTokenConverter() {
        for (Converter<Object> converter = this.headTokenConverter; converter != null; converter = converter.getNext()) {
            if (converter instanceof IntegerTokenConverter) {
                return (IntegerTokenConverter) converter;
            }
        }
        return null;
    }

    public boolean hasIntegerTokenCOnverter() {
        return getIntegerTokenConverter() != null;
    }

    public String convertMultipleArguments(Object... objArr) {
        StringBuilder sb = new StringBuilder();
        for (Converter<Object> converter = this.headTokenConverter; converter != null; converter = converter.getNext()) {
            if (converter instanceof MonoTypedConverter) {
                MonoTypedConverter monoTypedConverter = (MonoTypedConverter) converter;
                for (Object obj : objArr) {
                    if (monoTypedConverter.isApplicable(obj)) {
                        sb.append(converter.convert(obj));
                    }
                }
            } else {
                sb.append(converter.convert(objArr));
            }
        }
        return sb.toString();
    }

    public String convert(Object obj) {
        StringBuilder sb = new StringBuilder();
        for (Converter<Object> converter = this.headTokenConverter; converter != null; converter = converter.getNext()) {
            sb.append(converter.convert(obj));
        }
        return sb.toString();
    }

    public String convertInt(int i) {
        return convert(Integer.valueOf(i));
    }

    public void setPattern(String str) {
        if (str != null) {
            this.pattern = str.trim().replace("//", h.g);
        }
    }

    public String getPattern() {
        return this.pattern;
    }

    public String toRegexForFixedDate(Date date) {
        StringBuilder sb = new StringBuilder();
        for (Converter<Object> converter = this.headTokenConverter; converter != null; converter = converter.getNext()) {
            if (converter instanceof LiteralConverter) {
                sb.append(converter.convert(null));
            } else if (converter instanceof IntegerTokenConverter) {
                sb.append(FileFinder.regexEscapePath("(\\d+)"));
            } else if (converter instanceof DateTokenConverter) {
                DateTokenConverter dateTokenConverter = (DateTokenConverter) converter;
                if (dateTokenConverter.isPrimary()) {
                    sb.append(converter.convert(date));
                } else {
                    sb.append(FileFinder.regexEscapePath(dateTokenConverter.toRegex()));
                }
            }
        }
        return sb.toString();
    }

    public String toRegex() {
        return toRegex(false, false);
    }

    public String toRegex(boolean z, boolean z2) {
        StringBuilder sb = new StringBuilder();
        for (Converter<Object> converter = this.headTokenConverter; converter != null; converter = converter.getNext()) {
            if (converter instanceof LiteralConverter) {
                sb.append(converter.convert(null));
            } else if (converter instanceof IntegerTokenConverter) {
                if (z2) {
                    sb.append(FileFinder.regexEscapePath("(\\d+)"));
                } else {
                    sb.append(FileFinder.regexEscapePath("\\d+"));
                }
            } else if (converter instanceof DateTokenConverter) {
                DateTokenConverter dateTokenConverter = (DateTokenConverter) converter;
                if (z && dateTokenConverter.isPrimary()) {
                    sb.append(FileFinder.regexEscapePath("(" + dateTokenConverter.toRegex() + ")"));
                } else {
                    sb.append(FileFinder.regexEscapePath(dateTokenConverter.toRegex()));
                }
            }
        }
        return sb.toString();
    }
}
