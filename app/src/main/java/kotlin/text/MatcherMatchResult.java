package kotlin.text;

import java.util.regex.Matcher;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;

/* compiled from: Regex.kt */
/* loaded from: classes3.dex */
public final class MatcherMatchResult implements MatchResult {
    public final MatchGroupCollection groups;
    public final CharSequence input;
    public final Matcher matcher;

    public MatcherMatchResult(Matcher matcher, CharSequence input) {
        Intrinsics.checkNotNullParameter(matcher, "matcher");
        Intrinsics.checkNotNullParameter(input, "input");
        this.matcher = matcher;
        this.input = input;
        this.groups = new MatcherMatchResult$groups$1(this);
    }

    public final java.util.regex.MatchResult getMatchResult() {
        return this.matcher;
    }

    @Override // kotlin.text.MatchResult
    public IntRange getRange() {
        IntRange range;
        range = RegexKt.range(getMatchResult());
        return range;
    }
}
