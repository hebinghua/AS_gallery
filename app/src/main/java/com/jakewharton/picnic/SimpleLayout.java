package com.jakewharton.picnic;

import com.jakewharton.picnic.Table;
import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.NoWhenBranchMatchedException;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringsKt;

/* compiled from: textLayout.kt */
/* loaded from: classes.dex */
public final class SimpleLayout implements TextLayout {
    public final Table.PositionedCell cell;
    public final int leftPadding;
    public final int topPadding;

    /* loaded from: classes.dex */
    public final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;
        public static final /* synthetic */ int[] $EnumSwitchMapping$1;

        static {
            int[] iArr = new int[TextAlignment.values().length];
            $EnumSwitchMapping$0 = iArr;
            TextAlignment textAlignment = TextAlignment.TopLeft;
            iArr[textAlignment.ordinal()] = 1;
            TextAlignment textAlignment2 = TextAlignment.TopCenter;
            iArr[textAlignment2.ordinal()] = 2;
            TextAlignment textAlignment3 = TextAlignment.TopRight;
            iArr[textAlignment3.ordinal()] = 3;
            TextAlignment textAlignment4 = TextAlignment.MiddleLeft;
            iArr[textAlignment4.ordinal()] = 4;
            TextAlignment textAlignment5 = TextAlignment.MiddleCenter;
            iArr[textAlignment5.ordinal()] = 5;
            TextAlignment textAlignment6 = TextAlignment.MiddleRight;
            iArr[textAlignment6.ordinal()] = 6;
            TextAlignment textAlignment7 = TextAlignment.BottomLeft;
            iArr[textAlignment7.ordinal()] = 7;
            TextAlignment textAlignment8 = TextAlignment.BottomCenter;
            iArr[textAlignment8.ordinal()] = 8;
            TextAlignment textAlignment9 = TextAlignment.BottomRight;
            iArr[textAlignment9.ordinal()] = 9;
            int[] iArr2 = new int[TextAlignment.values().length];
            $EnumSwitchMapping$1 = iArr2;
            iArr2[textAlignment.ordinal()] = 1;
            iArr2[textAlignment4.ordinal()] = 2;
            iArr2[textAlignment7.ordinal()] = 3;
            iArr2[textAlignment2.ordinal()] = 4;
            iArr2[textAlignment5.ordinal()] = 5;
            iArr2[textAlignment8.ordinal()] = 6;
            iArr2[textAlignment3.ordinal()] = 7;
            iArr2[textAlignment6.ordinal()] = 8;
            iArr2[textAlignment9.ordinal()] = 9;
        }
    }

    public SimpleLayout(Table.PositionedCell cell) {
        Integer paddingTop;
        Integer paddingLeft;
        Intrinsics.checkNotNullParameter(cell, "cell");
        this.cell = cell;
        CellStyle canonicalStyle = cell.getCanonicalStyle();
        int i = 0;
        this.leftPadding = (canonicalStyle == null || (paddingLeft = canonicalStyle.getPaddingLeft()) == null) ? 0 : paddingLeft.intValue();
        CellStyle canonicalStyle2 = cell.getCanonicalStyle();
        if (canonicalStyle2 != null && (paddingTop = canonicalStyle2.getPaddingTop()) != null) {
            i = paddingTop.intValue();
        }
        this.topPadding = i;
    }

    @Override // com.jakewharton.picnic.TextLayout
    public int measureWidth() {
        Integer paddingRight;
        int i = this.leftPadding;
        CellStyle canonicalStyle = this.cell.getCanonicalStyle();
        int intValue = i + ((canonicalStyle == null || (paddingRight = canonicalStyle.getPaddingRight()) == null) ? 0 : paddingRight.intValue());
        Iterator it = StringsKt__StringsKt.split$default(this.cell.getCell().getContent(), new char[]{'\n'}, false, 0, 6, null).iterator();
        if (it.hasNext()) {
            int visualCodePointCount = TextKt.getVisualCodePointCount((String) it.next());
            while (it.hasNext()) {
                int visualCodePointCount2 = TextKt.getVisualCodePointCount((String) it.next());
                if (visualCodePointCount < visualCodePointCount2) {
                    visualCodePointCount = visualCodePointCount2;
                }
            }
            return intValue + visualCodePointCount;
        }
        throw new NoSuchElementException();
    }

    @Override // com.jakewharton.picnic.TextLayout
    public int measureHeight() {
        Integer paddingBottom;
        int i = this.topPadding + 1;
        CellStyle canonicalStyle = this.cell.getCanonicalStyle();
        int intValue = i + ((canonicalStyle == null || (paddingBottom = canonicalStyle.getPaddingBottom()) == null) ? 0 : paddingBottom.intValue());
        String content = this.cell.getCell().getContent();
        int i2 = 0;
        for (int i3 = 0; i3 < content.length(); i3++) {
            if (content.charAt(i3) == '\n') {
                i2++;
            }
        }
        return intValue + i2;
    }

    @Override // com.jakewharton.picnic.TextLayout
    public void draw(TextCanvas canvas) {
        TextAlignment textAlignment;
        int i;
        int height;
        int i2;
        int i3;
        int width;
        int i4;
        Integer paddingRight;
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        int measureHeight = measureHeight();
        CellStyle canonicalStyle = this.cell.getCanonicalStyle();
        if (canonicalStyle == null || (textAlignment = canonicalStyle.getAlignment()) == null) {
            textAlignment = TextAlignment.TopLeft;
        }
        switch (WhenMappings.$EnumSwitchMapping$0[textAlignment.ordinal()]) {
            case 1:
            case 2:
            case 3:
                i = this.topPadding;
                break;
            case 4:
            case 5:
            case 6:
                height = (canvas.getHeight() - measureHeight) / 2;
                i2 = this.topPadding;
                i = height + i2;
                break;
            case 7:
            case 8:
            case 9:
                height = canvas.getHeight() - measureHeight;
                i2 = this.topPadding;
                i = height + i2;
                break;
            default:
                throw new NoWhenBranchMatchedException();
        }
        int i5 = 0;
        for (Object obj : StringsKt__StringsKt.split$default(this.cell.getCell().getContent(), new char[]{'\n'}, false, 0, 6, null)) {
            int i6 = i5 + 1;
            if (i5 < 0) {
                CollectionsKt__CollectionsKt.throwIndexOverflow();
            }
            String str = (String) obj;
            int i7 = this.leftPadding;
            CellStyle canonicalStyle2 = this.cell.getCanonicalStyle();
            int intValue = i7 + ((canonicalStyle2 == null || (paddingRight = canonicalStyle2.getPaddingRight()) == null) ? 0 : paddingRight.intValue()) + TextKt.getVisualCodePointCount(str);
            switch (WhenMappings.$EnumSwitchMapping$1[textAlignment.ordinal()]) {
                case 1:
                case 2:
                case 3:
                    i3 = this.leftPadding;
                    continue;
                    canvas.write(i5 + i, i3, str);
                    i5 = i6;
                case 4:
                case 5:
                case 6:
                    width = (canvas.getWidth() - intValue) / 2;
                    i4 = this.leftPadding;
                    break;
                case 7:
                case 8:
                case 9:
                    width = canvas.getWidth() - intValue;
                    i4 = this.leftPadding;
                    break;
                default:
                    throw new NoWhenBranchMatchedException();
            }
            i3 = width + i4;
            canvas.write(i5 + i, i3, str);
            i5 = i6;
        }
    }
}
