package ch.qos.logback.core.util;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class DatePatternToRegexUtil {
    public final String datePattern;
    public final int datePatternLength;
    public final CharSequenceToRegexMapper regexMapper = new CharSequenceToRegexMapper();

    public DatePatternToRegexUtil(String str) {
        this.datePattern = str;
        this.datePatternLength = str.length();
    }

    public String toRegex() {
        List<CharSequenceState> list = tokenize();
        StringBuilder sb = new StringBuilder();
        for (CharSequenceState charSequenceState : list) {
            sb.append(this.regexMapper.toRegex(charSequenceState));
        }
        return sb.toString();
    }

    private List<CharSequenceState> tokenize() {
        ArrayList arrayList = new ArrayList();
        CharSequenceState charSequenceState = null;
        for (int i = 0; i < this.datePatternLength; i++) {
            char charAt = this.datePattern.charAt(i);
            if (charSequenceState == null || charSequenceState.c != charAt) {
                charSequenceState = new CharSequenceState(charAt);
                arrayList.add(charSequenceState);
            } else {
                charSequenceState.incrementOccurrences();
            }
        }
        return arrayList;
    }
}
