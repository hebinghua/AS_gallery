package com.miui.gallery.provider.cache;

import android.text.TextUtils;
import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.provider.cache.CacheItem;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes2.dex */
public abstract class Filter<T extends CacheItem> {
    public static final Pattern COMPARATOR = Pattern.compile("(?i)(<(?!=)|<=|>(?!=)|>=|=(?!=)|==|!=|(?<=\\s)LIKE(?=\\s)|(?<=\\s)IN(?=\\s|\\()|(?<=\\s)NOT\\s+IN(?=\\s|\\()|(?<=\\s)IS\\s+NULL(?=\\s|$)|(?<=\\s)NOT\\s+NULL(?=\\s|$)|(?<=\\s)&(?=\\s|$))");
    public static final CompareFilter NOT_SUPPORTED_FILTER = new CompareFilter(null, null) { // from class: com.miui.gallery.provider.cache.Filter.1
        @Override // com.miui.gallery.provider.cache.Filter
        public CacheItem filter(CacheItem cacheItem) {
            return null;
        }
    };

    /* loaded from: classes2.dex */
    public enum Compound {
        AND,
        OR
    }

    /* loaded from: classes2.dex */
    public interface FilterFactory<T extends CacheItem> {
        CompareFilter<T> getFilter(int i, Comparator comparator, String str);

        CacheItem.ColumnMapper getMapper();
    }

    public abstract T filter(T t);

    public static <T extends CacheItem> Filter<T> from(String str, String[] strArr, FilterFactory<T> filterFactory) {
        return build(insertArgs(str, strArr), filterFactory);
    }

    public static <T extends CacheItem> Filter<T> build(String str, FilterFactory<T> filterFactory) {
        Filter<T> filter;
        if (TextUtils.isEmpty(str)) {
            return new DummyFilter();
        }
        String[][] split = split(str);
        String[] strArr = split[0];
        String[] strArr2 = split[1];
        int i = 0;
        CompoundFilter compoundFilter = null;
        for (String str2 : strArr) {
            String trim = str2.trim();
            DefaultLogger.v(".provider.cache.Filter", "build for %s", trim);
            if (inBracket(trim)) {
                filter = build(trim.substring(1, trim.length() - 1), filterFactory);
            } else {
                Matcher matcher = COMPARATOR.matcher(trim);
                if (matcher.find()) {
                    CompareFilter<T> filter2 = filterFactory.getFilter(filterFactory.getMapper().getIndex(trim.substring(0, matcher.start()).trim()), Comparator.from(matcher.group()), translateParams(trim.substring(matcher.end()).trim()));
                    if (filter2 == null || filter2 == NOT_SUPPORTED_FILTER) {
                        DefaultLogger.w(".provider.cache.Filter", "Found a unrecognized column");
                    }
                    filter = filter2;
                } else {
                    DefaultLogger.w(".provider.cache.Filter", "Found a unrecognized operation");
                    filter = filterFactory.getFilter(-1, null, null);
                }
            }
            if (compoundFilter == null) {
                compoundFilter = filter;
            } else {
                i++;
                compoundFilter = new CompoundFilter(strArr2[i - 1], compoundFilter, filter);
            }
        }
        DefaultLogger.d(".provider.cache.Filter", "filter build finish: %s", compoundFilter);
        return compoundFilter;
    }

    public static String translateParams(String str) {
        return (str.length() > 1 && str.charAt(0) == '\'' && str.charAt(str.length() - 1) == '\'') ? str.substring(1, str.length() - 1) : str;
    }

    public static String[][] split(String str) {
        Compound findCompound;
        Stack stack = new Stack();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        int i = 0;
        int i2 = 0;
        while (i < str.length()) {
            char charAt = str.charAt(i);
            if (!isIdentifier(charAt)) {
                if (charAt == '(') {
                    stack.push(Integer.valueOf(i));
                } else {
                    if (str.charAt(i) == ')') {
                        stack.pop();
                    }
                    if (stack.isEmpty() && (findCompound = findCompound(i, str)) != null) {
                        arrayList.add(str.substring(i2, i));
                        arrayList2.add(findCompound.toString());
                        i = findCompound == Compound.AND ? i + 3 : i + 2;
                        i2 = i + 1;
                    }
                }
            }
            i++;
        }
        arrayList.add(str.substring(i2, str.length()));
        return new String[][]{(String[]) arrayList.toArray(new String[arrayList.size()]), (String[]) arrayList2.toArray(new String[arrayList2.size()])};
    }

    public static boolean isIdentifier(char c) {
        return c == '_' || Character.isLetterOrDigit(c);
    }

    public static Compound findCompound(int i, String str) {
        int i2 = i + 4;
        if (i2 >= str.length()) {
            return null;
        }
        int i3 = i + 1;
        if (str.charAt(i3) == 'a' || str.charAt(i3) == 'A') {
            int i4 = i + 2;
            if (str.charAt(i4) == 'n' || str.charAt(i4) == 'N') {
                int i5 = i + 3;
                if ((str.charAt(i5) == 'd' || str.charAt(i5) == 'D') && (Character.isSpaceChar(str.charAt(i2)) || str.charAt(i2) == '(')) {
                    return Compound.AND;
                }
            }
        }
        if (str.charAt(i3) == 'o' || str.charAt(i3) == 'O') {
            int i6 = i + 2;
            if (str.charAt(i6) == 'r' || str.charAt(i6) == 'R') {
                int i7 = i + 3;
                if (Character.isSpaceChar(str.charAt(i7)) || str.charAt(i7) == '(') {
                    return Compound.OR;
                }
            }
        }
        return null;
    }

    public static boolean inBracket(String str) {
        return str.charAt(0) == '(' && str.charAt(str.length() - 1) == ')';
    }

    public static String insertArgs(String str, String[] strArr) {
        if (strArr == null || strArr.length < 1) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (int i2 = 0; i2 < str.length(); i2++) {
            char charAt = str.charAt(i2);
            if (charAt == '?') {
                sb.append(strArr[i]);
                i++;
            } else {
                sb.append(charAt);
            }
        }
        return sb.toString();
    }

    /* loaded from: classes2.dex */
    public enum Comparator {
        GREATER,
        GREATER_OR_EQUAL,
        LESS,
        LESS_OR_EQUAL,
        EQUALS,
        NOT_EQUALS,
        IN,
        NOT_IN,
        IS_NULL,
        NOT_NULL,
        LIKE,
        BITWISE_AND;

        public static Comparator from(String str) {
            char charAt = str.charAt(0);
            char charAt2 = str.charAt(str.length() - 1);
            if (charAt == '<' && charAt2 == '=') {
                return LESS_OR_EQUAL;
            }
            if (charAt == '<') {
                return LESS;
            }
            if (charAt == '>' && charAt2 == '=') {
                return GREATER_OR_EQUAL;
            }
            if (charAt == '>') {
                return GREATER;
            }
            if (charAt == '=') {
                return EQUALS;
            }
            if (charAt == '!') {
                return NOT_EQUALS;
            }
            if (charAt2 == 'n' || charAt2 == 'N') {
                if (str.length() > 2) {
                    return NOT_IN;
                }
                return IN;
            } else if (charAt2 == 'l' || charAt2 == 'L') {
                if (charAt == 'i' || charAt == 'I') {
                    return IS_NULL;
                }
                return NOT_NULL;
            } else if ((charAt == 'l' || charAt == 'L') && (charAt2 == 'E' || charAt2 == 'e')) {
                return LIKE;
            } else {
                if (charAt != '&') {
                    return null;
                }
                return BITWISE_AND;
            }
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class CompareFilter<T extends CacheItem> extends Filter<T> {
        public String mArgument;
        public Comparator mComparator;

        public CompareFilter(Comparator comparator, String str) {
            this.mComparator = comparator;
            this.mArgument = str;
        }

        public String toString() {
            return "CompareFilter{, mComparator=" + this.mComparator + ", mArgument='" + this.mArgument + CoreConstants.SINGLE_QUOTE_CHAR + '}';
        }
    }

    /* loaded from: classes2.dex */
    public static class CompoundFilter<T extends CacheItem> extends Filter<T> {
        public final Compound mCompound;
        public final Filter<T> mLeft;
        public final Filter<T> mRight;

        public CompoundFilter(String str, Filter<T> filter, Filter<T> filter2) {
            this.mLeft = filter;
            this.mRight = filter2;
            this.mCompound = Compound.valueOf(str);
        }

        public CompoundFilter(Compound compound, Filter<T> filter, Filter<T> filter2) {
            this.mLeft = filter;
            this.mRight = filter2;
            this.mCompound = compound;
        }

        @Override // com.miui.gallery.provider.cache.Filter
        public T filter(T t) {
            if (this.mCompound == Compound.AND) {
                if (this.mLeft.filter(t) == null) {
                    return null;
                }
            } else if (this.mLeft.filter(t) != null) {
                return t;
            }
            return this.mRight.filter(t);
        }

        public String toString() {
            return "CompoundFilter{mFilters=[" + this.mLeft + ", " + this.mRight + "], mCompound=" + this.mCompound + '}';
        }
    }

    /* loaded from: classes2.dex */
    public static class DummyFilter<T extends CacheItem> extends Filter<T> {
        @Override // com.miui.gallery.provider.cache.Filter
        public T filter(T t) {
            return t;
        }

        public String toString() {
            return "DummyFilter";
        }

        public DummyFilter() {
        }
    }
}
