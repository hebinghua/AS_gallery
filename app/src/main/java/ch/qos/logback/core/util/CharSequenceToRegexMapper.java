package ch.qos.logback.core.util;

import java.text.DateFormatSymbols;

/* loaded from: classes.dex */
class CharSequenceToRegexMapper {
    public DateFormatSymbols symbols = DateFormatSymbols.getInstance();

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public String toRegex(CharSequenceState charSequenceState) {
        int i = charSequenceState.occurrences;
        char c = charSequenceState.c;
        if (c != 'y') {
            if (c == 'z') {
                return ".*";
            }
            switch (c) {
                case '\'':
                    if (i != 1) {
                        throw new IllegalStateException("Too many single quotes");
                    }
                    return "";
                case '.':
                    return "\\.";
                case 'K':
                case 'S':
                case 'W':
                case 'd':
                case 'h':
                case 'k':
                case 'm':
                case 's':
                case 'w':
                    break;
                case 'M':
                    if (i <= 2) {
                        return number(i);
                    }
                    if (i == 3) {
                        return getRegexForShortMonths();
                    }
                    return getRegexForLongMonths();
                case 'Z':
                    return "(\\+|-)\\d{4}";
                case '\\':
                    throw new IllegalStateException("Forward slashes are not allowed");
                case 'a':
                    return getRegexForAmPms();
                default:
                    switch (c) {
                        case 'D':
                        case 'F':
                        case 'H':
                            break;
                        case 'E':
                            if (i >= 4) {
                                return getRegexForLongDaysOfTheWeek();
                            }
                            return getRegexForShortDaysOfTheWeek();
                        case 'G':
                            return ".*";
                        default:
                            if (i == 1) {
                                return "" + c;
                            }
                            return c + "{" + i + "}";
                    }
            }
        }
        return number(i);
    }

    private String number(int i) {
        return "\\d{" + i + "}";
    }

    private String getRegexForAmPms() {
        return symbolArrayToRegex(this.symbols.getAmPmStrings());
    }

    private String getRegexForLongDaysOfTheWeek() {
        return symbolArrayToRegex(this.symbols.getWeekdays());
    }

    private String getRegexForShortDaysOfTheWeek() {
        return symbolArrayToRegex(this.symbols.getShortWeekdays());
    }

    private String getRegexForLongMonths() {
        return symbolArrayToRegex(this.symbols.getMonths());
    }

    public String getRegexForShortMonths() {
        return symbolArrayToRegex(this.symbols.getShortMonths());
    }

    private String symbolArrayToRegex(String[] strArr) {
        int[] findMinMaxLengthsInSymbols = findMinMaxLengthsInSymbols(strArr);
        return ".{" + findMinMaxLengthsInSymbols[0] + "," + findMinMaxLengthsInSymbols[1] + "}";
    }

    public static int[] findMinMaxLengthsInSymbols(String[] strArr) {
        int i = Integer.MAX_VALUE;
        int i2 = 0;
        for (String str : strArr) {
            int length = str.length();
            if (length != 0) {
                i = Math.min(i, length);
                i2 = Math.max(i2, length);
            }
        }
        return new int[]{i, i2};
    }
}
