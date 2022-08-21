package ch.qos.logback.classic.pattern;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.util.CachingDateFormatter;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/* loaded from: classes.dex */
public class DateConverter extends ClassicConverter {
    public CachingDateFormatter cachingDateFormatter = null;

    @Override // ch.qos.logback.core.pattern.DynamicConverter, ch.qos.logback.core.spi.LifeCycle
    public void start() {
        String firstOption = getFirstOption();
        if (firstOption == null) {
            firstOption = CoreConstants.ISO8601_PATTERN;
        }
        if (firstOption.equals(CoreConstants.ISO8601_STR)) {
            firstOption = CoreConstants.ISO8601_PATTERN;
        }
        TimeZone timeZone = TimeZone.getDefault();
        Locale locale = Locale.ENGLISH;
        List<String> optionList = getOptionList();
        if (optionList != null) {
            if (optionList.size() > 1) {
                timeZone = TimeZone.getTimeZone(optionList.get(1));
            }
            if (optionList.size() > 2) {
                locale = parseLocale(optionList.get(2));
            }
        }
        try {
            this.cachingDateFormatter = new CachingDateFormatter(firstOption, locale);
        } catch (IllegalArgumentException e) {
            addWarn("Could not instantiate SimpleDateFormat with pattern " + firstOption, e);
            this.cachingDateFormatter = new CachingDateFormatter(CoreConstants.ISO8601_PATTERN, locale);
        }
        this.cachingDateFormatter.setTimeZone(timeZone);
    }

    private Locale parseLocale(String str) {
        String[] split = str.split(",");
        if (split.length > 1) {
            return new Locale(split[0], split[1]);
        }
        return new Locale(split[0]);
    }

    @Override // ch.qos.logback.core.pattern.Converter
    public String convert(ILoggingEvent iLoggingEvent) {
        return this.cachingDateFormatter.format(iLoggingEvent.getTimeStamp());
    }
}
