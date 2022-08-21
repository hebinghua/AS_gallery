package com.jakewharton.picnic;

import kotlin.jvm.functions.Function1;

/* compiled from: textRender.kt */
/* loaded from: classes.dex */
public final class TextRendering {
    public static /* synthetic */ String render$default(Table table, Function1 function1, TextBorder textBorder, int i, Object obj) {
        if ((i & 1) != 0) {
            function1 = TextRendering$renderText$1.INSTANCE;
        }
        if ((i & 2) != 0) {
            textBorder = TextBorder.DEFAULT;
        }
        return render(table, function1, textBorder);
    }

    /* JADX WARN: Code restructure failed: missing block: B:465:0x00e5, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual(r7 != null ? r7.getBorder() : null, java.lang.Boolean.TRUE) == false) goto L111;
     */
    /* JADX WARN: Code restructure failed: missing block: B:470:0x00f5, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual(r12 != null ? r12.getBorderLeft() : null, java.lang.Boolean.TRUE) != false) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:471:0x00f7, code lost:
        if (r15 > 0) goto L109;
     */
    /* JADX WARN: Code restructure failed: missing block: B:472:0x00f9, code lost:
        r7 = r30.getTableStyle();
     */
    /* JADX WARN: Code restructure failed: missing block: B:473:0x00fd, code lost:
        if (r7 == null) goto L108;
     */
    /* JADX WARN: Code restructure failed: missing block: B:474:0x00ff, code lost:
        r7 = r7.getBorderStyle();
     */
    /* JADX WARN: Code restructure failed: missing block: B:475:0x0104, code lost:
        r7 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:477:0x0107, code lost:
        if (r7 == com.jakewharton.picnic.BorderStyle.Hidden) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:478:0x0109, code lost:
        r6[r15] = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:479:0x010c, code lost:
        r15 = r15 + r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:480:0x0111, code lost:
        if (r15 != r30.getColumnCount()) goto L103;
     */
    /* JADX WARN: Code restructure failed: missing block: B:481:0x0113, code lost:
        r7 = r30.getTableStyle();
     */
    /* JADX WARN: Code restructure failed: missing block: B:482:0x0117, code lost:
        if (r7 == null) goto L102;
     */
    /* JADX WARN: Code restructure failed: missing block: B:483:0x0119, code lost:
        r7 = r7.getBorder();
     */
    /* JADX WARN: Code restructure failed: missing block: B:484:0x011e, code lost:
        r7 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:486:0x0125, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual(r7, java.lang.Boolean.TRUE) != false) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:487:0x0127, code lost:
        if (r12 == null) goto L107;
     */
    /* JADX WARN: Code restructure failed: missing block: B:488:0x0129, code lost:
        r7 = r12.getBorderRight();
     */
    /* JADX WARN: Code restructure failed: missing block: B:489:0x012e, code lost:
        r7 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:491:0x0135, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual(r7, java.lang.Boolean.TRUE) == false) goto L54;
     */
    /* JADX WARN: Code restructure failed: missing block: B:493:0x013b, code lost:
        if (r15 < r30.getColumnCount()) goto L101;
     */
    /* JADX WARN: Code restructure failed: missing block: B:494:0x013d, code lost:
        r7 = r30.getTableStyle();
     */
    /* JADX WARN: Code restructure failed: missing block: B:495:0x0141, code lost:
        if (r7 == null) goto L100;
     */
    /* JADX WARN: Code restructure failed: missing block: B:496:0x0143, code lost:
        r7 = r7.getBorderStyle();
     */
    /* JADX WARN: Code restructure failed: missing block: B:497:0x0148, code lost:
        r7 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:499:0x014b, code lost:
        if (r7 == com.jakewharton.picnic.BorderStyle.Hidden) goto L54;
     */
    /* JADX WARN: Code restructure failed: missing block: B:500:0x014d, code lost:
        r6[r15] = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:501:0x0150, code lost:
        if (r14 != 0) goto L95;
     */
    /* JADX WARN: Code restructure failed: missing block: B:502:0x0152, code lost:
        r7 = r30.getTableStyle();
     */
    /* JADX WARN: Code restructure failed: missing block: B:503:0x0156, code lost:
        if (r7 == null) goto L94;
     */
    /* JADX WARN: Code restructure failed: missing block: B:504:0x0158, code lost:
        r7 = r7.getBorder();
     */
    /* JADX WARN: Code restructure failed: missing block: B:505:0x015d, code lost:
        r7 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:507:0x0164, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual(r7, java.lang.Boolean.TRUE) != false) goto L60;
     */
    /* JADX WARN: Code restructure failed: missing block: B:508:0x0166, code lost:
        if (r12 == null) goto L99;
     */
    /* JADX WARN: Code restructure failed: missing block: B:509:0x0168, code lost:
        r7 = r12.getBorderTop();
     */
    /* JADX WARN: Code restructure failed: missing block: B:510:0x016d, code lost:
        r7 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:512:0x0174, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual(r7, java.lang.Boolean.TRUE) == false) goto L66;
     */
    /* JADX WARN: Code restructure failed: missing block: B:513:0x0176, code lost:
        if (r14 > 0) goto L93;
     */
    /* JADX WARN: Code restructure failed: missing block: B:514:0x0178, code lost:
        r7 = r30.getTableStyle();
     */
    /* JADX WARN: Code restructure failed: missing block: B:515:0x017c, code lost:
        if (r7 == null) goto L92;
     */
    /* JADX WARN: Code restructure failed: missing block: B:516:0x017e, code lost:
        r7 = r7.getBorderStyle();
     */
    /* JADX WARN: Code restructure failed: missing block: B:517:0x0183, code lost:
        r7 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:519:0x0186, code lost:
        if (r7 == com.jakewharton.picnic.BorderStyle.Hidden) goto L66;
     */
    /* JADX WARN: Code restructure failed: missing block: B:520:0x0188, code lost:
        r10[r14] = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:521:0x018b, code lost:
        r14 = r14 + r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:522:0x0190, code lost:
        if (r14 != r30.getRowCount()) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:523:0x0192, code lost:
        r4 = r30.getTableStyle();
     */
    /* JADX WARN: Code restructure failed: missing block: B:524:0x0196, code lost:
        if (r4 == null) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:525:0x0198, code lost:
        r4 = r4.getBorder();
     */
    /* JADX WARN: Code restructure failed: missing block: B:526:0x019d, code lost:
        r4 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:528:0x01a4, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual(r4, java.lang.Boolean.TRUE) != false) goto L73;
     */
    /* JADX WARN: Code restructure failed: missing block: B:529:0x01a6, code lost:
        if (r12 == null) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:530:0x01a8, code lost:
        r4 = r12.getBorderBottom();
     */
    /* JADX WARN: Code restructure failed: missing block: B:531:0x01ad, code lost:
        r4 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:533:0x01b4, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual(r4, java.lang.Boolean.TRUE) == false) goto L90;
     */
    /* JADX WARN: Code restructure failed: missing block: B:535:0x01ba, code lost:
        if (r14 < r30.getRowCount()) goto L83;
     */
    /* JADX WARN: Code restructure failed: missing block: B:536:0x01bc, code lost:
        r4 = r30.getTableStyle();
     */
    /* JADX WARN: Code restructure failed: missing block: B:537:0x01c0, code lost:
        if (r4 == null) goto L82;
     */
    /* JADX WARN: Code restructure failed: missing block: B:538:0x01c2, code lost:
        r13 = r4.getBorderStyle();
     */
    /* JADX WARN: Code restructure failed: missing block: B:539:0x01c7, code lost:
        r13 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:541:0x01ca, code lost:
        if (r13 == com.jakewharton.picnic.BorderStyle.Hidden) goto L80;
     */
    /* JADX WARN: Code restructure failed: missing block: B:542:0x01cc, code lost:
        r10[r14] = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:543:0x01cf, code lost:
        r11 = r18;
        r7 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:678:0x047d, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual(r0 != null ? r0.getBorder() : null, r10) != false) goto L416;
     */
    /* JADX WARN: Code restructure failed: missing block: B:702:0x04be, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual(r7 != null ? r7.getBorder() : null, r10) != false) goto L292;
     */
    /* JADX WARN: Code restructure failed: missing block: B:726:0x0501, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual(r10 != null ? r10.getBorder() : null, r13) != false) goto L314;
     */
    /* JADX WARN: Code restructure failed: missing block: B:750:0x0548, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual(r13 != null ? r13.getBorder() : null, r6) != false) goto L408;
     */
    /* JADX WARN: Code restructure failed: missing block: B:780:0x059d, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual(r0 != null ? r0.getBorder() : null, r5) != false) goto L360;
     */
    /* JADX WARN: Code restructure failed: missing block: B:804:0x05ed, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual(r0 != null ? r0.getBorder() : null, r5) != false) goto L385;
     */
    /* JADX WARN: Removed duplicated region for block: B:683:0x0486  */
    /* JADX WARN: Removed duplicated region for block: B:706:0x04c5  */
    /* JADX WARN: Removed duplicated region for block: B:730:0x0508  */
    /* JADX WARN: Removed duplicated region for block: B:752:0x054c  */
    /* JADX WARN: Removed duplicated region for block: B:755:0x0551 A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static final java.lang.String render(com.jakewharton.picnic.Table r30, kotlin.jvm.functions.Function1<? super com.jakewharton.picnic.Table.PositionedCell, ? extends com.jakewharton.picnic.TextLayout> r31, com.jakewharton.picnic.TextBorder r32) {
        /*
            Method dump skipped, instructions count: 1674
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jakewharton.picnic.TextRendering.render(com.jakewharton.picnic.Table, kotlin.jvm.functions.Function1, com.jakewharton.picnic.TextBorder):java.lang.String");
    }
}
