package com.nexstreaming.nexeditorsdk;

import com.nexstreaming.nexeditorsdk.exception.InvalidRangeException;
import java.util.Arrays;
import java.util.List;

@Deprecated
/* loaded from: classes3.dex */
public final class nexOverlayKineMasterExpression implements Cloneable {
    public static final nexOverlayKineMasterExpression DRIFTING;
    public static final nexOverlayKineMasterExpression DROP;
    public static final nexOverlayKineMasterExpression FADE;
    public static final nexOverlayKineMasterExpression FLOATING;
    public static final nexOverlayKineMasterExpression NONE;
    public static final nexOverlayKineMasterExpression POP;
    public static final nexOverlayKineMasterExpression SCALE;
    public static final nexOverlayKineMasterExpression SLIDE;
    public static final nexOverlayKineMasterExpression SPIN;
    public static final nexOverlayKineMasterExpression SQUISHING;
    private static final nexOverlayKineMasterExpression[] values;
    private String KMinID;
    private String KMoutID;
    private String KMoverallID;
    private int KineMasterID;
    private String name;

    static {
        nexOverlayKineMasterExpression nexoverlaykinemasterexpression = new nexOverlayKineMasterExpression("None", 0, "none", "none", "none");
        NONE = nexoverlaykinemasterexpression;
        nexOverlayKineMasterExpression nexoverlaykinemasterexpression2 = new nexOverlayKineMasterExpression("Fade", 1, "FadeIn", "FadeOut", "none");
        FADE = nexoverlaykinemasterexpression2;
        nexOverlayKineMasterExpression nexoverlaykinemasterexpression3 = new nexOverlayKineMasterExpression("Pop", 2, "PopIn", "ScaleUpOut", "none");
        POP = nexoverlaykinemasterexpression3;
        nexOverlayKineMasterExpression nexoverlaykinemasterexpression4 = new nexOverlayKineMasterExpression("Slide", 3, "SlideRightIn", "SlideRightOut", "none");
        SLIDE = nexoverlaykinemasterexpression4;
        nexOverlayKineMasterExpression nexoverlaykinemasterexpression5 = new nexOverlayKineMasterExpression("Spin", 4, "SpinCCWIn", "SpinCWOut", "none");
        SPIN = nexoverlaykinemasterexpression5;
        nexOverlayKineMasterExpression nexoverlaykinemasterexpression6 = new nexOverlayKineMasterExpression("Drop", 5, "DropIn", "FadeOut", "none");
        DROP = nexoverlaykinemasterexpression6;
        nexOverlayKineMasterExpression nexoverlaykinemasterexpression7 = new nexOverlayKineMasterExpression("Scale", 6, "ScaleUpIn", "ScaleDownOut", "none");
        SCALE = nexoverlaykinemasterexpression7;
        nexOverlayKineMasterExpression nexoverlaykinemasterexpression8 = new nexOverlayKineMasterExpression("Floating", 7, "FadeIn", "FadeOut", "FloatingOverall");
        FLOATING = nexoverlaykinemasterexpression8;
        nexOverlayKineMasterExpression nexoverlaykinemasterexpression9 = new nexOverlayKineMasterExpression("Drifting", 8, "FadeIn", "FadeOut", "DriftingOverall");
        DRIFTING = nexoverlaykinemasterexpression9;
        nexOverlayKineMasterExpression nexoverlaykinemasterexpression10 = new nexOverlayKineMasterExpression("Squishing", 9, "FadeIn", "FadeOut", "SquishingOverall");
        SQUISHING = nexoverlaykinemasterexpression10;
        values = new nexOverlayKineMasterExpression[]{nexoverlaykinemasterexpression, nexoverlaykinemasterexpression2, nexoverlaykinemasterexpression3, nexoverlaykinemasterexpression4, nexoverlaykinemasterexpression5, nexoverlaykinemasterexpression6, nexoverlaykinemasterexpression7, nexoverlaykinemasterexpression8, nexoverlaykinemasterexpression9, nexoverlaykinemasterexpression10};
    }

    @Deprecated
    public static List<nexOverlayKineMasterExpression> getPresetList() {
        return Arrays.asList(values);
    }

    @Deprecated
    public static nexOverlayKineMasterExpression[] values() {
        return values;
    }

    @Deprecated
    public static nexOverlayKineMasterExpression getExpression(int i) {
        nexOverlayKineMasterExpression[] nexoverlaykinemasterexpressionArr = values;
        if (i < nexoverlaykinemasterexpressionArr.length) {
            if (i < 0) {
                throw new InvalidRangeException(i);
            }
            return nexoverlaykinemasterexpressionArr[i];
        }
        throw new InvalidRangeException(i, nexoverlaykinemasterexpressionArr.length);
    }

    @Deprecated
    public static String[] getNames() {
        String[] strArr = new String[values.length];
        int i = 0;
        while (true) {
            nexOverlayKineMasterExpression[] nexoverlaykinemasterexpressionArr = values;
            if (i < nexoverlaykinemasterexpressionArr.length) {
                strArr[i] = nexoverlaykinemasterexpressionArr[i].name;
                i++;
            } else {
                return strArr;
            }
        }
    }

    private nexOverlayKineMasterExpression(String str, int i, String str2, String str3, String str4) {
        this.name = str;
        this.KineMasterID = i;
        this.KMinID = str2;
        this.KMoutID = str3;
        this.KMoverallID = str4;
    }

    @Deprecated
    public int getID() {
        return this.KineMasterID;
    }

    public String getNames(int i) {
        if (i == 0) {
            return this.KMinID;
        }
        if (i == 1) {
            return this.KMoutID;
        }
        if (i != 2) {
            return null;
        }
        return this.KMoverallID;
    }
}
