package com.google.common.base;

import ch.qos.logback.core.CoreConstants;

/* loaded from: classes.dex */
public abstract class CharMatcher {

    /* loaded from: classes.dex */
    public static abstract class FastMatcher extends CharMatcher {
    }

    public abstract boolean matches(char c);

    public static CharMatcher none() {
        return None.INSTANCE;
    }

    public static CharMatcher whitespace() {
        return Whitespace.INSTANCE;
    }

    public static CharMatcher is(char c) {
        return new Is(c);
    }

    public int indexIn(CharSequence charSequence, int i) {
        int length = charSequence.length();
        Preconditions.checkPositionIndex(i, length);
        while (i < length) {
            if (matches(charSequence.charAt(i))) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static String showCharacter(char c) {
        char[] cArr = {CoreConstants.ESCAPE_CHAR, 'u', 0, 0, 0, 0};
        for (int i = 0; i < 4; i++) {
            cArr[5 - i] = "0123456789ABCDEF".charAt(c & 15);
            c = (char) (c >> 4);
        }
        return String.copyValueOf(cArr);
    }

    /* loaded from: classes.dex */
    public static abstract class NamedFastMatcher extends FastMatcher {
        public final String description;

        public NamedFastMatcher(String str) {
            this.description = (String) Preconditions.checkNotNull(str);
        }

        public final String toString() {
            return this.description;
        }
    }

    /* loaded from: classes.dex */
    public static final class None extends NamedFastMatcher {
        public static final None INSTANCE = new None();

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char c) {
            return false;
        }

        public None() {
            super("CharMatcher.none()");
        }

        @Override // com.google.common.base.CharMatcher
        public int indexIn(CharSequence charSequence, int i) {
            Preconditions.checkPositionIndex(i, charSequence.length());
            return -1;
        }
    }

    /* loaded from: classes.dex */
    public static final class Whitespace extends NamedFastMatcher {
        public static final int SHIFT = Integer.numberOfLeadingZeros(31);
        public static final Whitespace INSTANCE = new Whitespace();

        public Whitespace() {
            super("CharMatcher.whitespace()");
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char c) {
            return "\u2002\u3000\r\u0085\u200a\u2005\u2000\u3000\u2029\u000b\u3000\u2008\u2003\u205f\u3000\u1680\t \u2006\u2001  \f\u2009\u3000\u2004\u3000\u3000\u2028\n \u3000".charAt((48906 * c) >>> SHIFT) == c;
        }
    }

    /* loaded from: classes.dex */
    public static final class Is extends FastMatcher {
        public final char match;

        public Is(char c) {
            this.match = c;
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char c) {
            return c == this.match;
        }

        public String toString() {
            String showCharacter = CharMatcher.showCharacter(this.match);
            StringBuilder sb = new StringBuilder(String.valueOf(showCharacter).length() + 18);
            sb.append("CharMatcher.is('");
            sb.append(showCharacter);
            sb.append("')");
            return sb.toString();
        }
    }
}
