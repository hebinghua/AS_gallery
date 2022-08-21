package com.jakewharton.picnic;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: textBorder.kt */
/* loaded from: classes.dex */
public final class TextBorder {
    public final String characters;
    public static final Companion Companion = new Companion(null);
    public static final TextBorder DEFAULT = new TextBorder(" ╷╵│╶┌└├╴┐┘┤─┬┴┼");
    public static final TextBorder ROUNDED = new TextBorder(" ╷╵│╶╭╰├╴╮╯┤─┬┴┼");
    public static final TextBorder ASCII = new TextBorder("   | +++ +++-+++");

    public TextBorder(String characters) {
        Intrinsics.checkNotNullParameter(characters, "characters");
        this.characters = characters;
        if (characters.length() == 16) {
            return;
        }
        throw new IllegalArgumentException("Border string must contain exactly 16 characters".toString());
    }

    public final char getVertical() {
        return this.characters.charAt(3);
    }

    public final char getHorizontal() {
        return this.characters.charAt(12);
    }

    public final char get(boolean z, boolean z2, boolean z3, boolean z4) {
        String str = this.characters;
        int i = 0;
        int i2 = (z ? 1 : 0) | (z2 ? 2 : 0) | (z3 ? 4 : 0);
        if (z4) {
            i = 8;
        }
        return str.charAt(i2 | i);
    }

    /* compiled from: textBorder.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}
