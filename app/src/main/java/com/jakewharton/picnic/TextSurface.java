package com.jakewharton.picnic;

import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringsKt;

/* compiled from: textSurface.kt */
/* loaded from: classes.dex */
public final class TextSurface implements TextCanvas {
    public final int height;
    public final StringBuilder[] rowBuilders;
    public final int width;

    public TextSurface(int i, int i2) {
        this.width = i;
        this.height = i2;
        int height = getHeight();
        StringBuilder[] sbArr = new StringBuilder[height];
        for (int i3 = 0; i3 < height; i3++) {
            StringBuilder sb = new StringBuilder(getWidth());
            int width = getWidth();
            for (int i4 = 0; i4 < width; i4++) {
                sb.append(' ');
            }
            sbArr[i3] = sb;
        }
        this.rowBuilders = sbArr;
    }

    @Override // com.jakewharton.picnic.TextCanvas
    public int getWidth() {
        return this.width;
    }

    @Override // com.jakewharton.picnic.TextCanvas
    public int getHeight() {
        return this.height;
    }

    public void write(int i, int i2, char c) {
        StringBuilder sb = this.rowBuilders[i];
        sb.setCharAt(TextKt.visualIndex(sb, i2), c);
    }

    @Override // com.jakewharton.picnic.TextCanvas
    public void write(int i, int i2, String string) {
        Intrinsics.checkNotNullParameter(string, "string");
        int i3 = 0;
        for (Object obj : StringsKt__StringsKt.split$default(string, new char[]{'\n'}, false, 0, 6, null)) {
            int i4 = i3 + 1;
            if (i3 < 0) {
                CollectionsKt__CollectionsKt.throwIndexOverflow();
            }
            String str = (String) obj;
            StringBuilder sb = this.rowBuilders[i3 + i];
            sb.replace(TextKt.visualIndex(sb, i2), TextKt.visualIndex(sb, TextKt.getVisualCodePointCount(str) + i2), str);
            i3 = i4;
        }
    }

    public String toString() {
        int i = 0;
        int i2 = 0;
        for (StringBuilder sb : this.rowBuilders) {
            i2 += sb.length();
        }
        StringBuilder sb2 = new StringBuilder(i2 + (getHeight() - 1));
        StringBuilder[] sbArr = this.rowBuilders;
        int length = sbArr.length;
        int i3 = 0;
        while (i < length) {
            StringBuilder sb3 = sbArr[i];
            int i4 = i3 + 1;
            if (i3 > 0) {
                sb2.append('\n');
            }
            sb2.append((CharSequence) sb3);
            i++;
            i3 = i4;
        }
        String sb4 = sb2.toString();
        Intrinsics.checkNotNullExpressionValue(sb4, "StringBuilder(capacity).…builderAction).toString()");
        return sb4;
    }
}
