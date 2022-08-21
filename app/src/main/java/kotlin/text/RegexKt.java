package kotlin.text;

import java.util.regex.Matcher;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt___RangesKt;

/* compiled from: Regex.kt */
/* loaded from: classes3.dex */
public final class RegexKt {
    public static final MatchResult findNext(Matcher matcher, int i, CharSequence charSequence) {
        if (!matcher.find(i)) {
            return null;
        }
        return new MatcherMatchResult(matcher, charSequence);
    }

    public static final IntRange range(java.util.regex.MatchResult matchResult) {
        return RangesKt___RangesKt.until(matchResult.start(), matchResult.end());
    }

    public static final IntRange range(java.util.regex.MatchResult matchResult, int i) {
        return RangesKt___RangesKt.until(matchResult.start(i), matchResult.end(i));
    }
}
