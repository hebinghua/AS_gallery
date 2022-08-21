package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.core.motion.utils.Easing;
import androidx.constraintlayout.motion.widget.Debug;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.xiaomi.stat.b.h;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: classes.dex */
public class ConstraintSet {
    public static final int[] VISIBILITY_FLAGS = {0, 4, 8};
    public static SparseIntArray mapToConstant = new SparseIntArray();
    public static SparseIntArray overrideMapToConstant = new SparseIntArray();
    public String derivedState = "";
    public int mRotate = 0;
    public HashMap<String, ConstraintAttribute> mSavedAttributes = new HashMap<>();
    public boolean mForceId = true;
    public HashMap<Integer, Constraint> mConstraints = new HashMap<>();

    public final String sideToString(int side) {
        switch (side) {
            case 1:
                return "left";
            case 2:
                return "right";
            case 3:
                return "top";
            case 4:
                return "bottom";
            case 5:
                return "baseline";
            case 6:
                return "start";
            case 7:
                return "end";
            default:
                return "undefined";
        }
    }

    static {
        mapToConstant.append(R$styleable.Constraint_layout_constraintLeft_toLeftOf, 25);
        mapToConstant.append(R$styleable.Constraint_layout_constraintLeft_toRightOf, 26);
        mapToConstant.append(R$styleable.Constraint_layout_constraintRight_toLeftOf, 29);
        mapToConstant.append(R$styleable.Constraint_layout_constraintRight_toRightOf, 30);
        mapToConstant.append(R$styleable.Constraint_layout_constraintTop_toTopOf, 36);
        mapToConstant.append(R$styleable.Constraint_layout_constraintTop_toBottomOf, 35);
        mapToConstant.append(R$styleable.Constraint_layout_constraintBottom_toTopOf, 4);
        mapToConstant.append(R$styleable.Constraint_layout_constraintBottom_toBottomOf, 3);
        mapToConstant.append(R$styleable.Constraint_layout_constraintBaseline_toBaselineOf, 1);
        mapToConstant.append(R$styleable.Constraint_layout_constraintBaseline_toTopOf, 91);
        mapToConstant.append(R$styleable.Constraint_layout_constraintBaseline_toBottomOf, 92);
        mapToConstant.append(R$styleable.Constraint_layout_editor_absoluteX, 6);
        mapToConstant.append(R$styleable.Constraint_layout_editor_absoluteY, 7);
        mapToConstant.append(R$styleable.Constraint_layout_constraintGuide_begin, 17);
        mapToConstant.append(R$styleable.Constraint_layout_constraintGuide_end, 18);
        mapToConstant.append(R$styleable.Constraint_layout_constraintGuide_percent, 19);
        mapToConstant.append(R$styleable.Constraint_android_orientation, 27);
        mapToConstant.append(R$styleable.Constraint_layout_constraintStart_toEndOf, 32);
        mapToConstant.append(R$styleable.Constraint_layout_constraintStart_toStartOf, 33);
        mapToConstant.append(R$styleable.Constraint_layout_constraintEnd_toStartOf, 10);
        mapToConstant.append(R$styleable.Constraint_layout_constraintEnd_toEndOf, 9);
        mapToConstant.append(R$styleable.Constraint_layout_goneMarginLeft, 13);
        mapToConstant.append(R$styleable.Constraint_layout_goneMarginTop, 16);
        mapToConstant.append(R$styleable.Constraint_layout_goneMarginRight, 14);
        mapToConstant.append(R$styleable.Constraint_layout_goneMarginBottom, 11);
        mapToConstant.append(R$styleable.Constraint_layout_goneMarginStart, 15);
        mapToConstant.append(R$styleable.Constraint_layout_goneMarginEnd, 12);
        mapToConstant.append(R$styleable.Constraint_layout_constraintVertical_weight, 40);
        mapToConstant.append(R$styleable.Constraint_layout_constraintHorizontal_weight, 39);
        mapToConstant.append(R$styleable.Constraint_layout_constraintHorizontal_chainStyle, 41);
        mapToConstant.append(R$styleable.Constraint_layout_constraintVertical_chainStyle, 42);
        mapToConstant.append(R$styleable.Constraint_layout_constraintHorizontal_bias, 20);
        mapToConstant.append(R$styleable.Constraint_layout_constraintVertical_bias, 37);
        mapToConstant.append(R$styleable.Constraint_layout_constraintDimensionRatio, 5);
        mapToConstant.append(R$styleable.Constraint_layout_constraintLeft_creator, 87);
        mapToConstant.append(R$styleable.Constraint_layout_constraintTop_creator, 87);
        mapToConstant.append(R$styleable.Constraint_layout_constraintRight_creator, 87);
        mapToConstant.append(R$styleable.Constraint_layout_constraintBottom_creator, 87);
        mapToConstant.append(R$styleable.Constraint_layout_constraintBaseline_creator, 87);
        mapToConstant.append(R$styleable.Constraint_android_layout_marginLeft, 24);
        mapToConstant.append(R$styleable.Constraint_android_layout_marginRight, 28);
        mapToConstant.append(R$styleable.Constraint_android_layout_marginStart, 31);
        mapToConstant.append(R$styleable.Constraint_android_layout_marginEnd, 8);
        mapToConstant.append(R$styleable.Constraint_android_layout_marginTop, 34);
        mapToConstant.append(R$styleable.Constraint_android_layout_marginBottom, 2);
        mapToConstant.append(R$styleable.Constraint_android_layout_width, 23);
        mapToConstant.append(R$styleable.Constraint_android_layout_height, 21);
        mapToConstant.append(R$styleable.Constraint_layout_constraintWidth, 95);
        mapToConstant.append(R$styleable.Constraint_layout_constraintHeight, 96);
        mapToConstant.append(R$styleable.Constraint_android_visibility, 22);
        mapToConstant.append(R$styleable.Constraint_android_alpha, 43);
        mapToConstant.append(R$styleable.Constraint_android_elevation, 44);
        mapToConstant.append(R$styleable.Constraint_android_rotationX, 45);
        mapToConstant.append(R$styleable.Constraint_android_rotationY, 46);
        mapToConstant.append(R$styleable.Constraint_android_rotation, 60);
        mapToConstant.append(R$styleable.Constraint_android_scaleX, 47);
        mapToConstant.append(R$styleable.Constraint_android_scaleY, 48);
        mapToConstant.append(R$styleable.Constraint_android_transformPivotX, 49);
        mapToConstant.append(R$styleable.Constraint_android_transformPivotY, 50);
        mapToConstant.append(R$styleable.Constraint_android_translationX, 51);
        mapToConstant.append(R$styleable.Constraint_android_translationY, 52);
        mapToConstant.append(R$styleable.Constraint_android_translationZ, 53);
        mapToConstant.append(R$styleable.Constraint_layout_constraintWidth_default, 54);
        mapToConstant.append(R$styleable.Constraint_layout_constraintHeight_default, 55);
        mapToConstant.append(R$styleable.Constraint_layout_constraintWidth_max, 56);
        mapToConstant.append(R$styleable.Constraint_layout_constraintHeight_max, 57);
        mapToConstant.append(R$styleable.Constraint_layout_constraintWidth_min, 58);
        mapToConstant.append(R$styleable.Constraint_layout_constraintHeight_min, 59);
        mapToConstant.append(R$styleable.Constraint_layout_constraintCircle, 61);
        mapToConstant.append(R$styleable.Constraint_layout_constraintCircleRadius, 62);
        mapToConstant.append(R$styleable.Constraint_layout_constraintCircleAngle, 63);
        mapToConstant.append(R$styleable.Constraint_animateRelativeTo, 64);
        mapToConstant.append(R$styleable.Constraint_transitionEasing, 65);
        mapToConstant.append(R$styleable.Constraint_drawPath, 66);
        mapToConstant.append(R$styleable.Constraint_transitionPathRotate, 67);
        mapToConstant.append(R$styleable.Constraint_motionStagger, 79);
        mapToConstant.append(R$styleable.Constraint_android_id, 38);
        mapToConstant.append(R$styleable.Constraint_motionProgress, 68);
        mapToConstant.append(R$styleable.Constraint_layout_constraintWidth_percent, 69);
        mapToConstant.append(R$styleable.Constraint_layout_constraintHeight_percent, 70);
        mapToConstant.append(R$styleable.Constraint_layout_wrapBehaviorInParent, 97);
        mapToConstant.append(R$styleable.Constraint_chainUseRtl, 71);
        mapToConstant.append(R$styleable.Constraint_barrierDirection, 72);
        mapToConstant.append(R$styleable.Constraint_barrierMargin, 73);
        mapToConstant.append(R$styleable.Constraint_constraint_referenced_ids, 74);
        mapToConstant.append(R$styleable.Constraint_barrierAllowsGoneWidgets, 75);
        mapToConstant.append(R$styleable.Constraint_pathMotionArc, 76);
        mapToConstant.append(R$styleable.Constraint_layout_constraintTag, 77);
        mapToConstant.append(R$styleable.Constraint_visibilityMode, 78);
        mapToConstant.append(R$styleable.Constraint_layout_constrainedWidth, 80);
        mapToConstant.append(R$styleable.Constraint_layout_constrainedHeight, 81);
        mapToConstant.append(R$styleable.Constraint_polarRelativeTo, 82);
        mapToConstant.append(R$styleable.Constraint_transformPivotTarget, 83);
        mapToConstant.append(R$styleable.Constraint_quantizeMotionSteps, 84);
        mapToConstant.append(R$styleable.Constraint_quantizeMotionPhase, 85);
        mapToConstant.append(R$styleable.Constraint_quantizeMotionInterpolator, 86);
        SparseIntArray sparseIntArray = overrideMapToConstant;
        int i = R$styleable.ConstraintOverride_layout_editor_absoluteY;
        sparseIntArray.append(i, 6);
        overrideMapToConstant.append(i, 7);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_android_orientation, 27);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_goneMarginLeft, 13);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_goneMarginTop, 16);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_goneMarginRight, 14);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_goneMarginBottom, 11);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_goneMarginStart, 15);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_goneMarginEnd, 12);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_constraintVertical_weight, 40);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_constraintHorizontal_weight, 39);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_constraintHorizontal_chainStyle, 41);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_constraintVertical_chainStyle, 42);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_constraintHorizontal_bias, 20);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_constraintVertical_bias, 37);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_constraintDimensionRatio, 5);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_constraintLeft_creator, 87);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_constraintTop_creator, 87);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_constraintRight_creator, 87);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_constraintBottom_creator, 87);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_constraintBaseline_creator, 87);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_android_layout_marginLeft, 24);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_android_layout_marginRight, 28);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_android_layout_marginStart, 31);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_android_layout_marginEnd, 8);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_android_layout_marginTop, 34);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_android_layout_marginBottom, 2);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_android_layout_width, 23);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_android_layout_height, 21);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_constraintWidth, 95);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_constraintHeight, 96);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_android_visibility, 22);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_android_alpha, 43);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_android_elevation, 44);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_android_rotationX, 45);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_android_rotationY, 46);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_android_rotation, 60);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_android_scaleX, 47);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_android_scaleY, 48);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_android_transformPivotX, 49);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_android_transformPivotY, 50);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_android_translationX, 51);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_android_translationY, 52);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_android_translationZ, 53);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_constraintWidth_default, 54);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_constraintHeight_default, 55);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_constraintWidth_max, 56);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_constraintHeight_max, 57);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_constraintWidth_min, 58);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_constraintHeight_min, 59);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_constraintCircleRadius, 62);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_constraintCircleAngle, 63);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_animateRelativeTo, 64);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_transitionEasing, 65);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_drawPath, 66);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_transitionPathRotate, 67);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_motionStagger, 79);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_android_id, 38);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_motionTarget, 98);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_motionProgress, 68);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_constraintWidth_percent, 69);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_constraintHeight_percent, 70);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_chainUseRtl, 71);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_barrierDirection, 72);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_barrierMargin, 73);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_constraint_referenced_ids, 74);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_barrierAllowsGoneWidgets, 75);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_pathMotionArc, 76);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_constraintTag, 77);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_visibilityMode, 78);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_constrainedWidth, 80);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_constrainedHeight, 81);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_polarRelativeTo, 82);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_transformPivotTarget, 83);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_quantizeMotionSteps, 84);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_quantizeMotionPhase, 85);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_quantizeMotionInterpolator, 86);
        overrideMapToConstant.append(R$styleable.ConstraintOverride_layout_wrapBehaviorInParent, 97);
    }

    public static void parseDimensionConstraints(Object data, TypedArray a, int attr, int orientation) {
        if (data == null) {
            return;
        }
        int i = a.peekValue(attr).type;
        if (i == 3) {
            parseDimensionConstraintsString(data, a.getString(attr), orientation);
            return;
        }
        int i2 = -2;
        boolean z = false;
        if (i == 5) {
            i2 = a.getDimensionPixelSize(attr, 0);
        } else {
            int i3 = a.getInt(attr, 0);
            if (i3 != -4) {
                i2 = (i3 == -3 || !(i3 == -2 || i3 == -1)) ? 0 : i3;
            } else {
                z = true;
            }
        }
        if (data instanceof ConstraintLayout.LayoutParams) {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) data;
            if (orientation == 0) {
                ((ViewGroup.MarginLayoutParams) layoutParams).width = i2;
                layoutParams.constrainedWidth = z;
                return;
            }
            ((ViewGroup.MarginLayoutParams) layoutParams).height = i2;
            layoutParams.constrainedHeight = z;
        } else if (data instanceof Layout) {
            Layout layout = (Layout) data;
            if (orientation == 0) {
                layout.mWidth = i2;
                layout.constrainedWidth = z;
                return;
            }
            layout.mHeight = i2;
            layout.constrainedHeight = z;
        } else if (!(data instanceof Constraint.Delta)) {
        } else {
            Constraint.Delta delta = (Constraint.Delta) data;
            if (orientation == 0) {
                delta.add(23, i2);
                delta.add(80, z);
                return;
            }
            delta.add(21, i2);
            delta.add(81, z);
        }
    }

    public static void parseDimensionRatioString(ConstraintLayout.LayoutParams params, String value) {
        float f = Float.NaN;
        int i = -1;
        if (value != null) {
            int length = value.length();
            int indexOf = value.indexOf(44);
            int i2 = 0;
            if (indexOf > 0 && indexOf < length - 1) {
                String substring = value.substring(0, indexOf);
                if (substring.equalsIgnoreCase("W")) {
                    i = 0;
                } else if (substring.equalsIgnoreCase("H")) {
                    i = 1;
                }
                i2 = indexOf + 1;
            }
            int indexOf2 = value.indexOf(58);
            try {
                if (indexOf2 >= 0 && indexOf2 < length - 1) {
                    String substring2 = value.substring(i2, indexOf2);
                    String substring3 = value.substring(indexOf2 + 1);
                    if (substring2.length() > 0 && substring3.length() > 0) {
                        float parseFloat = Float.parseFloat(substring2);
                        float parseFloat2 = Float.parseFloat(substring3);
                        if (parseFloat > 0.0f && parseFloat2 > 0.0f) {
                            if (i == 1) {
                                f = Math.abs(parseFloat2 / parseFloat);
                            } else {
                                f = Math.abs(parseFloat / parseFloat2);
                            }
                        }
                    }
                } else {
                    String substring4 = value.substring(i2);
                    if (substring4.length() > 0) {
                        f = Float.parseFloat(substring4);
                    }
                }
            } catch (NumberFormatException unused) {
            }
        }
        params.dimensionRatio = value;
        params.dimensionRatioValue = f;
        params.dimensionRatioSide = i;
    }

    public static void parseDimensionConstraintsString(Object data, String value, int orientation) {
        if (value == null) {
            return;
        }
        int indexOf = value.indexOf(61);
        int length = value.length();
        if (indexOf <= 0 || indexOf >= length - 1) {
            return;
        }
        String substring = value.substring(0, indexOf);
        String substring2 = value.substring(indexOf + 1);
        if (substring2.length() <= 0) {
            return;
        }
        String trim = substring.trim();
        String trim2 = substring2.trim();
        if (MapBundleKey.OfflineMapKey.OFFLINE_RATION.equalsIgnoreCase(trim)) {
            if (data instanceof ConstraintLayout.LayoutParams) {
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) data;
                if (orientation == 0) {
                    ((ViewGroup.MarginLayoutParams) layoutParams).width = 0;
                } else {
                    ((ViewGroup.MarginLayoutParams) layoutParams).height = 0;
                }
                parseDimensionRatioString(layoutParams, trim2);
                return;
            } else if (data instanceof Layout) {
                ((Layout) data).dimensionRatio = trim2;
                return;
            } else if (!(data instanceof Constraint.Delta)) {
                return;
            } else {
                ((Constraint.Delta) data).add(5, trim2);
                return;
            }
        }
        try {
            if ("weight".equalsIgnoreCase(trim)) {
                float parseFloat = Float.parseFloat(trim2);
                if (data instanceof ConstraintLayout.LayoutParams) {
                    ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) data;
                    if (orientation == 0) {
                        ((ViewGroup.MarginLayoutParams) layoutParams2).width = 0;
                        layoutParams2.horizontalWeight = parseFloat;
                    } else {
                        ((ViewGroup.MarginLayoutParams) layoutParams2).height = 0;
                        layoutParams2.verticalWeight = parseFloat;
                    }
                } else if (data instanceof Layout) {
                    Layout layout = (Layout) data;
                    if (orientation == 0) {
                        layout.mWidth = 0;
                        layout.horizontalWeight = parseFloat;
                    } else {
                        layout.mHeight = 0;
                        layout.verticalWeight = parseFloat;
                    }
                } else if (data instanceof Constraint.Delta) {
                    Constraint.Delta delta = (Constraint.Delta) data;
                    if (orientation == 0) {
                        delta.add(23, 0);
                        delta.add(39, parseFloat);
                    } else {
                        delta.add(21, 0);
                        delta.add(40, parseFloat);
                    }
                }
            } else if (!"parent".equalsIgnoreCase(trim)) {
            } else {
                float max = Math.max(0.0f, Math.min(1.0f, Float.parseFloat(trim2)));
                if (data instanceof ConstraintLayout.LayoutParams) {
                    ConstraintLayout.LayoutParams layoutParams3 = (ConstraintLayout.LayoutParams) data;
                    if (orientation == 0) {
                        ((ViewGroup.MarginLayoutParams) layoutParams3).width = 0;
                        layoutParams3.matchConstraintPercentWidth = max;
                        layoutParams3.matchConstraintDefaultWidth = 2;
                    } else {
                        ((ViewGroup.MarginLayoutParams) layoutParams3).height = 0;
                        layoutParams3.matchConstraintPercentHeight = max;
                        layoutParams3.matchConstraintDefaultHeight = 2;
                    }
                } else if (data instanceof Layout) {
                    Layout layout2 = (Layout) data;
                    if (orientation == 0) {
                        layout2.mWidth = 0;
                        layout2.widthPercent = max;
                        layout2.widthDefault = 2;
                    } else {
                        layout2.mHeight = 0;
                        layout2.heightPercent = max;
                        layout2.heightDefault = 2;
                    }
                } else if (data instanceof Constraint.Delta) {
                    Constraint.Delta delta2 = (Constraint.Delta) data;
                    if (orientation == 0) {
                        delta2.add(23, 0);
                        delta2.add(54, 2);
                    } else {
                        delta2.add(21, 0);
                        delta2.add(55, 2);
                    }
                }
            }
        } catch (NumberFormatException unused) {
        }
    }

    /* loaded from: classes.dex */
    public static class Layout {
        public static SparseIntArray mapToConstant;
        public String mConstraintTag;
        public int mHeight;
        public String mReferenceIdString;
        public int[] mReferenceIds;
        public int mWidth;
        public boolean mIsGuideline = false;
        public boolean mApply = false;
        public boolean mOverride = false;
        public int guideBegin = -1;
        public int guideEnd = -1;
        public float guidePercent = -1.0f;
        public int leftToLeft = -1;
        public int leftToRight = -1;
        public int rightToLeft = -1;
        public int rightToRight = -1;
        public int topToTop = -1;
        public int topToBottom = -1;
        public int bottomToTop = -1;
        public int bottomToBottom = -1;
        public int baselineToBaseline = -1;
        public int baselineToTop = -1;
        public int baselineToBottom = -1;
        public int startToEnd = -1;
        public int startToStart = -1;
        public int endToStart = -1;
        public int endToEnd = -1;
        public float horizontalBias = 0.5f;
        public float verticalBias = 0.5f;
        public String dimensionRatio = null;
        public int circleConstraint = -1;
        public int circleRadius = 0;
        public float circleAngle = 0.0f;
        public int editorAbsoluteX = -1;
        public int editorAbsoluteY = -1;
        public int orientation = -1;
        public int leftMargin = 0;
        public int rightMargin = 0;
        public int topMargin = 0;
        public int bottomMargin = 0;
        public int endMargin = 0;
        public int startMargin = 0;
        public int baselineMargin = 0;
        public int goneLeftMargin = Integer.MIN_VALUE;
        public int goneTopMargin = Integer.MIN_VALUE;
        public int goneRightMargin = Integer.MIN_VALUE;
        public int goneBottomMargin = Integer.MIN_VALUE;
        public int goneEndMargin = Integer.MIN_VALUE;
        public int goneStartMargin = Integer.MIN_VALUE;
        public int goneBaselineMargin = Integer.MIN_VALUE;
        public float verticalWeight = -1.0f;
        public float horizontalWeight = -1.0f;
        public int horizontalChainStyle = 0;
        public int verticalChainStyle = 0;
        public int widthDefault = 0;
        public int heightDefault = 0;
        public int widthMax = 0;
        public int heightMax = 0;
        public int widthMin = 0;
        public int heightMin = 0;
        public float widthPercent = 1.0f;
        public float heightPercent = 1.0f;
        public int mBarrierDirection = -1;
        public int mBarrierMargin = 0;
        public int mHelperType = -1;
        public boolean constrainedWidth = false;
        public boolean constrainedHeight = false;
        public boolean mBarrierAllowsGoneWidgets = true;
        public int mWrapBehavior = 0;

        public void copyFrom(Layout src) {
            this.mIsGuideline = src.mIsGuideline;
            this.mWidth = src.mWidth;
            this.mApply = src.mApply;
            this.mHeight = src.mHeight;
            this.guideBegin = src.guideBegin;
            this.guideEnd = src.guideEnd;
            this.guidePercent = src.guidePercent;
            this.leftToLeft = src.leftToLeft;
            this.leftToRight = src.leftToRight;
            this.rightToLeft = src.rightToLeft;
            this.rightToRight = src.rightToRight;
            this.topToTop = src.topToTop;
            this.topToBottom = src.topToBottom;
            this.bottomToTop = src.bottomToTop;
            this.bottomToBottom = src.bottomToBottom;
            this.baselineToBaseline = src.baselineToBaseline;
            this.baselineToTop = src.baselineToTop;
            this.baselineToBottom = src.baselineToBottom;
            this.startToEnd = src.startToEnd;
            this.startToStart = src.startToStart;
            this.endToStart = src.endToStart;
            this.endToEnd = src.endToEnd;
            this.horizontalBias = src.horizontalBias;
            this.verticalBias = src.verticalBias;
            this.dimensionRatio = src.dimensionRatio;
            this.circleConstraint = src.circleConstraint;
            this.circleRadius = src.circleRadius;
            this.circleAngle = src.circleAngle;
            this.editorAbsoluteX = src.editorAbsoluteX;
            this.editorAbsoluteY = src.editorAbsoluteY;
            this.orientation = src.orientation;
            this.leftMargin = src.leftMargin;
            this.rightMargin = src.rightMargin;
            this.topMargin = src.topMargin;
            this.bottomMargin = src.bottomMargin;
            this.endMargin = src.endMargin;
            this.startMargin = src.startMargin;
            this.baselineMargin = src.baselineMargin;
            this.goneLeftMargin = src.goneLeftMargin;
            this.goneTopMargin = src.goneTopMargin;
            this.goneRightMargin = src.goneRightMargin;
            this.goneBottomMargin = src.goneBottomMargin;
            this.goneEndMargin = src.goneEndMargin;
            this.goneStartMargin = src.goneStartMargin;
            this.goneBaselineMargin = src.goneBaselineMargin;
            this.verticalWeight = src.verticalWeight;
            this.horizontalWeight = src.horizontalWeight;
            this.horizontalChainStyle = src.horizontalChainStyle;
            this.verticalChainStyle = src.verticalChainStyle;
            this.widthDefault = src.widthDefault;
            this.heightDefault = src.heightDefault;
            this.widthMax = src.widthMax;
            this.heightMax = src.heightMax;
            this.widthMin = src.widthMin;
            this.heightMin = src.heightMin;
            this.widthPercent = src.widthPercent;
            this.heightPercent = src.heightPercent;
            this.mBarrierDirection = src.mBarrierDirection;
            this.mBarrierMargin = src.mBarrierMargin;
            this.mHelperType = src.mHelperType;
            this.mConstraintTag = src.mConstraintTag;
            int[] iArr = src.mReferenceIds;
            if (iArr != null && src.mReferenceIdString == null) {
                this.mReferenceIds = Arrays.copyOf(iArr, iArr.length);
            } else {
                this.mReferenceIds = null;
            }
            this.mReferenceIdString = src.mReferenceIdString;
            this.constrainedWidth = src.constrainedWidth;
            this.constrainedHeight = src.constrainedHeight;
            this.mBarrierAllowsGoneWidgets = src.mBarrierAllowsGoneWidgets;
            this.mWrapBehavior = src.mWrapBehavior;
        }

        static {
            SparseIntArray sparseIntArray = new SparseIntArray();
            mapToConstant = sparseIntArray;
            sparseIntArray.append(R$styleable.Layout_layout_constraintLeft_toLeftOf, 24);
            mapToConstant.append(R$styleable.Layout_layout_constraintLeft_toRightOf, 25);
            mapToConstant.append(R$styleable.Layout_layout_constraintRight_toLeftOf, 28);
            mapToConstant.append(R$styleable.Layout_layout_constraintRight_toRightOf, 29);
            mapToConstant.append(R$styleable.Layout_layout_constraintTop_toTopOf, 35);
            mapToConstant.append(R$styleable.Layout_layout_constraintTop_toBottomOf, 34);
            mapToConstant.append(R$styleable.Layout_layout_constraintBottom_toTopOf, 4);
            mapToConstant.append(R$styleable.Layout_layout_constraintBottom_toBottomOf, 3);
            mapToConstant.append(R$styleable.Layout_layout_constraintBaseline_toBaselineOf, 1);
            mapToConstant.append(R$styleable.Layout_layout_editor_absoluteX, 6);
            mapToConstant.append(R$styleable.Layout_layout_editor_absoluteY, 7);
            mapToConstant.append(R$styleable.Layout_layout_constraintGuide_begin, 17);
            mapToConstant.append(R$styleable.Layout_layout_constraintGuide_end, 18);
            mapToConstant.append(R$styleable.Layout_layout_constraintGuide_percent, 19);
            mapToConstant.append(R$styleable.Layout_android_orientation, 26);
            mapToConstant.append(R$styleable.Layout_layout_constraintStart_toEndOf, 31);
            mapToConstant.append(R$styleable.Layout_layout_constraintStart_toStartOf, 32);
            mapToConstant.append(R$styleable.Layout_layout_constraintEnd_toStartOf, 10);
            mapToConstant.append(R$styleable.Layout_layout_constraintEnd_toEndOf, 9);
            mapToConstant.append(R$styleable.Layout_layout_goneMarginLeft, 13);
            mapToConstant.append(R$styleable.Layout_layout_goneMarginTop, 16);
            mapToConstant.append(R$styleable.Layout_layout_goneMarginRight, 14);
            mapToConstant.append(R$styleable.Layout_layout_goneMarginBottom, 11);
            mapToConstant.append(R$styleable.Layout_layout_goneMarginStart, 15);
            mapToConstant.append(R$styleable.Layout_layout_goneMarginEnd, 12);
            mapToConstant.append(R$styleable.Layout_layout_constraintVertical_weight, 38);
            mapToConstant.append(R$styleable.Layout_layout_constraintHorizontal_weight, 37);
            mapToConstant.append(R$styleable.Layout_layout_constraintHorizontal_chainStyle, 39);
            mapToConstant.append(R$styleable.Layout_layout_constraintVertical_chainStyle, 40);
            mapToConstant.append(R$styleable.Layout_layout_constraintHorizontal_bias, 20);
            mapToConstant.append(R$styleable.Layout_layout_constraintVertical_bias, 36);
            mapToConstant.append(R$styleable.Layout_layout_constraintDimensionRatio, 5);
            mapToConstant.append(R$styleable.Layout_layout_constraintLeft_creator, 76);
            mapToConstant.append(R$styleable.Layout_layout_constraintTop_creator, 76);
            mapToConstant.append(R$styleable.Layout_layout_constraintRight_creator, 76);
            mapToConstant.append(R$styleable.Layout_layout_constraintBottom_creator, 76);
            mapToConstant.append(R$styleable.Layout_layout_constraintBaseline_creator, 76);
            mapToConstant.append(R$styleable.Layout_android_layout_marginLeft, 23);
            mapToConstant.append(R$styleable.Layout_android_layout_marginRight, 27);
            mapToConstant.append(R$styleable.Layout_android_layout_marginStart, 30);
            mapToConstant.append(R$styleable.Layout_android_layout_marginEnd, 8);
            mapToConstant.append(R$styleable.Layout_android_layout_marginTop, 33);
            mapToConstant.append(R$styleable.Layout_android_layout_marginBottom, 2);
            mapToConstant.append(R$styleable.Layout_android_layout_width, 22);
            mapToConstant.append(R$styleable.Layout_android_layout_height, 21);
            mapToConstant.append(R$styleable.Layout_layout_constraintWidth, 41);
            mapToConstant.append(R$styleable.Layout_layout_constraintHeight, 42);
            mapToConstant.append(R$styleable.Layout_layout_constrainedWidth, 41);
            mapToConstant.append(R$styleable.Layout_layout_constrainedHeight, 42);
            mapToConstant.append(R$styleable.Layout_layout_wrapBehaviorInParent, 97);
            mapToConstant.append(R$styleable.Layout_layout_constraintCircle, 61);
            mapToConstant.append(R$styleable.Layout_layout_constraintCircleRadius, 62);
            mapToConstant.append(R$styleable.Layout_layout_constraintCircleAngle, 63);
            mapToConstant.append(R$styleable.Layout_layout_constraintWidth_percent, 69);
            mapToConstant.append(R$styleable.Layout_layout_constraintHeight_percent, 70);
            mapToConstant.append(R$styleable.Layout_chainUseRtl, 71);
            mapToConstant.append(R$styleable.Layout_barrierDirection, 72);
            mapToConstant.append(R$styleable.Layout_barrierMargin, 73);
            mapToConstant.append(R$styleable.Layout_constraint_referenced_ids, 74);
            mapToConstant.append(R$styleable.Layout_barrierAllowsGoneWidgets, 75);
        }

        public void fillFromAttributeList(Context context, AttributeSet attrs) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R$styleable.Layout);
            this.mApply = true;
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int index = obtainStyledAttributes.getIndex(i);
                int i2 = mapToConstant.get(index);
                if (i2 == 80) {
                    this.constrainedWidth = obtainStyledAttributes.getBoolean(index, this.constrainedWidth);
                } else if (i2 == 81) {
                    this.constrainedHeight = obtainStyledAttributes.getBoolean(index, this.constrainedHeight);
                } else if (i2 != 97) {
                    switch (i2) {
                        case 1:
                            this.baselineToBaseline = ConstraintSet.lookupID(obtainStyledAttributes, index, this.baselineToBaseline);
                            continue;
                        case 2:
                            this.bottomMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.bottomMargin);
                            continue;
                        case 3:
                            this.bottomToBottom = ConstraintSet.lookupID(obtainStyledAttributes, index, this.bottomToBottom);
                            continue;
                        case 4:
                            this.bottomToTop = ConstraintSet.lookupID(obtainStyledAttributes, index, this.bottomToTop);
                            continue;
                        case 5:
                            this.dimensionRatio = obtainStyledAttributes.getString(index);
                            continue;
                        case 6:
                            this.editorAbsoluteX = obtainStyledAttributes.getDimensionPixelOffset(index, this.editorAbsoluteX);
                            continue;
                        case 7:
                            this.editorAbsoluteY = obtainStyledAttributes.getDimensionPixelOffset(index, this.editorAbsoluteY);
                            continue;
                        case 8:
                            if (Build.VERSION.SDK_INT >= 17) {
                                this.endMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.endMargin);
                                break;
                            } else {
                                continue;
                            }
                        case 9:
                            this.endToEnd = ConstraintSet.lookupID(obtainStyledAttributes, index, this.endToEnd);
                            continue;
                        case 10:
                            this.endToStart = ConstraintSet.lookupID(obtainStyledAttributes, index, this.endToStart);
                            continue;
                        case 11:
                            this.goneBottomMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.goneBottomMargin);
                            continue;
                        case 12:
                            this.goneEndMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.goneEndMargin);
                            continue;
                        case 13:
                            this.goneLeftMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.goneLeftMargin);
                            continue;
                        case 14:
                            this.goneRightMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.goneRightMargin);
                            continue;
                        case 15:
                            this.goneStartMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.goneStartMargin);
                            continue;
                        case 16:
                            this.goneTopMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.goneTopMargin);
                            continue;
                        case 17:
                            this.guideBegin = obtainStyledAttributes.getDimensionPixelOffset(index, this.guideBegin);
                            continue;
                        case 18:
                            this.guideEnd = obtainStyledAttributes.getDimensionPixelOffset(index, this.guideEnd);
                            continue;
                        case 19:
                            this.guidePercent = obtainStyledAttributes.getFloat(index, this.guidePercent);
                            continue;
                        case 20:
                            this.horizontalBias = obtainStyledAttributes.getFloat(index, this.horizontalBias);
                            continue;
                        case 21:
                            this.mHeight = obtainStyledAttributes.getLayoutDimension(index, this.mHeight);
                            continue;
                        case 22:
                            this.mWidth = obtainStyledAttributes.getLayoutDimension(index, this.mWidth);
                            continue;
                        case 23:
                            this.leftMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.leftMargin);
                            continue;
                        case 24:
                            this.leftToLeft = ConstraintSet.lookupID(obtainStyledAttributes, index, this.leftToLeft);
                            continue;
                        case 25:
                            this.leftToRight = ConstraintSet.lookupID(obtainStyledAttributes, index, this.leftToRight);
                            continue;
                        case 26:
                            this.orientation = obtainStyledAttributes.getInt(index, this.orientation);
                            continue;
                        case 27:
                            this.rightMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.rightMargin);
                            continue;
                        case 28:
                            this.rightToLeft = ConstraintSet.lookupID(obtainStyledAttributes, index, this.rightToLeft);
                            continue;
                        case 29:
                            this.rightToRight = ConstraintSet.lookupID(obtainStyledAttributes, index, this.rightToRight);
                            continue;
                        case 30:
                            if (Build.VERSION.SDK_INT >= 17) {
                                this.startMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.startMargin);
                                break;
                            } else {
                                continue;
                            }
                        case 31:
                            this.startToEnd = ConstraintSet.lookupID(obtainStyledAttributes, index, this.startToEnd);
                            continue;
                        case 32:
                            this.startToStart = ConstraintSet.lookupID(obtainStyledAttributes, index, this.startToStart);
                            continue;
                        case 33:
                            this.topMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.topMargin);
                            continue;
                        case 34:
                            this.topToBottom = ConstraintSet.lookupID(obtainStyledAttributes, index, this.topToBottom);
                            continue;
                        case 35:
                            this.topToTop = ConstraintSet.lookupID(obtainStyledAttributes, index, this.topToTop);
                            continue;
                        case 36:
                            this.verticalBias = obtainStyledAttributes.getFloat(index, this.verticalBias);
                            continue;
                        case 37:
                            this.horizontalWeight = obtainStyledAttributes.getFloat(index, this.horizontalWeight);
                            continue;
                        case 38:
                            this.verticalWeight = obtainStyledAttributes.getFloat(index, this.verticalWeight);
                            continue;
                        case 39:
                            this.horizontalChainStyle = obtainStyledAttributes.getInt(index, this.horizontalChainStyle);
                            continue;
                        case 40:
                            this.verticalChainStyle = obtainStyledAttributes.getInt(index, this.verticalChainStyle);
                            continue;
                        case 41:
                            ConstraintSet.parseDimensionConstraints(this, obtainStyledAttributes, index, 0);
                            continue;
                        case 42:
                            ConstraintSet.parseDimensionConstraints(this, obtainStyledAttributes, index, 1);
                            continue;
                        default:
                            switch (i2) {
                                case 54:
                                    this.widthDefault = obtainStyledAttributes.getInt(index, this.widthDefault);
                                    continue;
                                case 55:
                                    this.heightDefault = obtainStyledAttributes.getInt(index, this.heightDefault);
                                    continue;
                                case 56:
                                    this.widthMax = obtainStyledAttributes.getDimensionPixelSize(index, this.widthMax);
                                    continue;
                                case 57:
                                    this.heightMax = obtainStyledAttributes.getDimensionPixelSize(index, this.heightMax);
                                    continue;
                                case 58:
                                    this.widthMin = obtainStyledAttributes.getDimensionPixelSize(index, this.widthMin);
                                    continue;
                                case 59:
                                    this.heightMin = obtainStyledAttributes.getDimensionPixelSize(index, this.heightMin);
                                    continue;
                                default:
                                    switch (i2) {
                                        case 61:
                                            this.circleConstraint = ConstraintSet.lookupID(obtainStyledAttributes, index, this.circleConstraint);
                                            continue;
                                        case 62:
                                            this.circleRadius = obtainStyledAttributes.getDimensionPixelSize(index, this.circleRadius);
                                            continue;
                                        case 63:
                                            this.circleAngle = obtainStyledAttributes.getFloat(index, this.circleAngle);
                                            continue;
                                        default:
                                            switch (i2) {
                                                case 69:
                                                    this.widthPercent = obtainStyledAttributes.getFloat(index, 1.0f);
                                                    continue;
                                                case 70:
                                                    this.heightPercent = obtainStyledAttributes.getFloat(index, 1.0f);
                                                    continue;
                                                case 71:
                                                    Log.e("ConstraintSet", "CURRENTLY UNSUPPORTED");
                                                    continue;
                                                case 72:
                                                    this.mBarrierDirection = obtainStyledAttributes.getInt(index, this.mBarrierDirection);
                                                    continue;
                                                case 73:
                                                    this.mBarrierMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.mBarrierMargin);
                                                    continue;
                                                case 74:
                                                    this.mReferenceIdString = obtainStyledAttributes.getString(index);
                                                    continue;
                                                case 75:
                                                    this.mBarrierAllowsGoneWidgets = obtainStyledAttributes.getBoolean(index, this.mBarrierAllowsGoneWidgets);
                                                    continue;
                                                case 76:
                                                    String hexString = Integer.toHexString(index);
                                                    int i3 = mapToConstant.get(index);
                                                    StringBuilder sb = new StringBuilder(String.valueOf(hexString).length() + 33);
                                                    sb.append("unused attribute 0x");
                                                    sb.append(hexString);
                                                    sb.append("   ");
                                                    sb.append(i3);
                                                    Log.w("ConstraintSet", sb.toString());
                                                    continue;
                                                case 77:
                                                    this.mConstraintTag = obtainStyledAttributes.getString(index);
                                                    continue;
                                                default:
                                                    switch (i2) {
                                                        case 91:
                                                            this.baselineToTop = ConstraintSet.lookupID(obtainStyledAttributes, index, this.baselineToTop);
                                                            continue;
                                                        case 92:
                                                            this.baselineToBottom = ConstraintSet.lookupID(obtainStyledAttributes, index, this.baselineToBottom);
                                                            continue;
                                                        case 93:
                                                            this.baselineMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.baselineMargin);
                                                            continue;
                                                        case 94:
                                                            this.goneBaselineMargin = obtainStyledAttributes.getDimensionPixelSize(index, this.goneBaselineMargin);
                                                            continue;
                                                        default:
                                                            String hexString2 = Integer.toHexString(index);
                                                            int i4 = mapToConstant.get(index);
                                                            StringBuilder sb2 = new StringBuilder(String.valueOf(hexString2).length() + 34);
                                                            sb2.append("Unknown attribute 0x");
                                                            sb2.append(hexString2);
                                                            sb2.append("   ");
                                                            sb2.append(i4);
                                                            Log.w("ConstraintSet", sb2.toString());
                                                            continue;
                                                            continue;
                                                            continue;
                                                            continue;
                                                            continue;
                                                    }
                                            }
                                    }
                            }
                    }
                } else {
                    this.mWrapBehavior = obtainStyledAttributes.getInt(index, this.mWrapBehavior);
                }
            }
            obtainStyledAttributes.recycle();
        }
    }

    /* loaded from: classes.dex */
    public static class Transform {
        public static SparseIntArray mapToConstant;
        public boolean mApply = false;
        public float rotation = 0.0f;
        public float rotationX = 0.0f;
        public float rotationY = 0.0f;
        public float scaleX = 1.0f;
        public float scaleY = 1.0f;
        public float transformPivotX = Float.NaN;
        public float transformPivotY = Float.NaN;
        public int transformPivotTarget = -1;
        public float translationX = 0.0f;
        public float translationY = 0.0f;
        public float translationZ = 0.0f;
        public boolean applyElevation = false;
        public float elevation = 0.0f;

        public void copyFrom(Transform src) {
            this.mApply = src.mApply;
            this.rotation = src.rotation;
            this.rotationX = src.rotationX;
            this.rotationY = src.rotationY;
            this.scaleX = src.scaleX;
            this.scaleY = src.scaleY;
            this.transformPivotX = src.transformPivotX;
            this.transformPivotY = src.transformPivotY;
            this.transformPivotTarget = src.transformPivotTarget;
            this.translationX = src.translationX;
            this.translationY = src.translationY;
            this.translationZ = src.translationZ;
            this.applyElevation = src.applyElevation;
            this.elevation = src.elevation;
        }

        static {
            SparseIntArray sparseIntArray = new SparseIntArray();
            mapToConstant = sparseIntArray;
            sparseIntArray.append(R$styleable.Transform_android_rotation, 1);
            mapToConstant.append(R$styleable.Transform_android_rotationX, 2);
            mapToConstant.append(R$styleable.Transform_android_rotationY, 3);
            mapToConstant.append(R$styleable.Transform_android_scaleX, 4);
            mapToConstant.append(R$styleable.Transform_android_scaleY, 5);
            mapToConstant.append(R$styleable.Transform_android_transformPivotX, 6);
            mapToConstant.append(R$styleable.Transform_android_transformPivotY, 7);
            mapToConstant.append(R$styleable.Transform_android_translationX, 8);
            mapToConstant.append(R$styleable.Transform_android_translationY, 9);
            mapToConstant.append(R$styleable.Transform_android_translationZ, 10);
            mapToConstant.append(R$styleable.Transform_android_elevation, 11);
            mapToConstant.append(R$styleable.Transform_transformPivotTarget, 12);
        }

        public void fillFromAttributeList(Context context, AttributeSet attrs) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R$styleable.Transform);
            this.mApply = true;
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int index = obtainStyledAttributes.getIndex(i);
                switch (mapToConstant.get(index)) {
                    case 1:
                        this.rotation = obtainStyledAttributes.getFloat(index, this.rotation);
                        break;
                    case 2:
                        this.rotationX = obtainStyledAttributes.getFloat(index, this.rotationX);
                        break;
                    case 3:
                        this.rotationY = obtainStyledAttributes.getFloat(index, this.rotationY);
                        break;
                    case 4:
                        this.scaleX = obtainStyledAttributes.getFloat(index, this.scaleX);
                        break;
                    case 5:
                        this.scaleY = obtainStyledAttributes.getFloat(index, this.scaleY);
                        break;
                    case 6:
                        this.transformPivotX = obtainStyledAttributes.getDimension(index, this.transformPivotX);
                        break;
                    case 7:
                        this.transformPivotY = obtainStyledAttributes.getDimension(index, this.transformPivotY);
                        break;
                    case 8:
                        this.translationX = obtainStyledAttributes.getDimension(index, this.translationX);
                        break;
                    case 9:
                        this.translationY = obtainStyledAttributes.getDimension(index, this.translationY);
                        break;
                    case 10:
                        if (Build.VERSION.SDK_INT >= 21) {
                            this.translationZ = obtainStyledAttributes.getDimension(index, this.translationZ);
                            break;
                        } else {
                            break;
                        }
                    case 11:
                        if (Build.VERSION.SDK_INT >= 21) {
                            this.applyElevation = true;
                            this.elevation = obtainStyledAttributes.getDimension(index, this.elevation);
                            break;
                        } else {
                            break;
                        }
                    case 12:
                        this.transformPivotTarget = ConstraintSet.lookupID(obtainStyledAttributes, index, this.transformPivotTarget);
                        break;
                }
            }
            obtainStyledAttributes.recycle();
        }
    }

    /* loaded from: classes.dex */
    public static class PropertySet {
        public boolean mApply = false;
        public int visibility = 0;
        public int mVisibilityMode = 0;
        public float alpha = 1.0f;
        public float mProgress = Float.NaN;

        public void copyFrom(PropertySet src) {
            this.mApply = src.mApply;
            this.visibility = src.visibility;
            this.alpha = src.alpha;
            this.mProgress = src.mProgress;
            this.mVisibilityMode = src.mVisibilityMode;
        }

        public void fillFromAttributeList(Context context, AttributeSet attrs) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R$styleable.PropertySet);
            this.mApply = true;
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int index = obtainStyledAttributes.getIndex(i);
                if (index == R$styleable.PropertySet_android_alpha) {
                    this.alpha = obtainStyledAttributes.getFloat(index, this.alpha);
                } else if (index == R$styleable.PropertySet_android_visibility) {
                    this.visibility = obtainStyledAttributes.getInt(index, this.visibility);
                    this.visibility = ConstraintSet.VISIBILITY_FLAGS[this.visibility];
                } else if (index == R$styleable.PropertySet_visibilityMode) {
                    this.mVisibilityMode = obtainStyledAttributes.getInt(index, this.mVisibilityMode);
                } else if (index == R$styleable.PropertySet_motionProgress) {
                    this.mProgress = obtainStyledAttributes.getFloat(index, this.mProgress);
                }
            }
            obtainStyledAttributes.recycle();
        }
    }

    /* loaded from: classes.dex */
    public static class Motion {
        public static SparseIntArray mapToConstant;
        public boolean mApply = false;
        public int mAnimateRelativeTo = -1;
        public int mAnimateCircleAngleTo = 0;
        public String mTransitionEasing = null;
        public int mPathMotionArc = -1;
        public int mDrawPath = 0;
        public float mMotionStagger = Float.NaN;
        public int mPolarRelativeTo = -1;
        public float mPathRotate = Float.NaN;
        public float mQuantizeMotionPhase = Float.NaN;
        public int mQuantizeMotionSteps = -1;
        public String mQuantizeInterpolatorString = null;
        public int mQuantizeInterpolatorType = -3;
        public int mQuantizeInterpolatorID = -1;

        public void copyFrom(Motion src) {
            this.mApply = src.mApply;
            this.mAnimateRelativeTo = src.mAnimateRelativeTo;
            this.mTransitionEasing = src.mTransitionEasing;
            this.mPathMotionArc = src.mPathMotionArc;
            this.mDrawPath = src.mDrawPath;
            this.mPathRotate = src.mPathRotate;
            this.mMotionStagger = src.mMotionStagger;
            this.mPolarRelativeTo = src.mPolarRelativeTo;
        }

        static {
            SparseIntArray sparseIntArray = new SparseIntArray();
            mapToConstant = sparseIntArray;
            sparseIntArray.append(R$styleable.Motion_motionPathRotate, 1);
            mapToConstant.append(R$styleable.Motion_pathMotionArc, 2);
            mapToConstant.append(R$styleable.Motion_transitionEasing, 3);
            mapToConstant.append(R$styleable.Motion_drawPath, 4);
            mapToConstant.append(R$styleable.Motion_animateRelativeTo, 5);
            mapToConstant.append(R$styleable.Motion_animateCircleAngleTo, 6);
            mapToConstant.append(R$styleable.Motion_motionStagger, 7);
            mapToConstant.append(R$styleable.Motion_quantizeMotionSteps, 8);
            mapToConstant.append(R$styleable.Motion_quantizeMotionPhase, 9);
            mapToConstant.append(R$styleable.Motion_quantizeMotionInterpolator, 10);
        }

        public void fillFromAttributeList(Context context, AttributeSet attrs) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R$styleable.Motion);
            this.mApply = true;
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int index = obtainStyledAttributes.getIndex(i);
                switch (mapToConstant.get(index)) {
                    case 1:
                        this.mPathRotate = obtainStyledAttributes.getFloat(index, this.mPathRotate);
                        break;
                    case 2:
                        this.mPathMotionArc = obtainStyledAttributes.getInt(index, this.mPathMotionArc);
                        break;
                    case 3:
                        if (obtainStyledAttributes.peekValue(index).type == 3) {
                            this.mTransitionEasing = obtainStyledAttributes.getString(index);
                            break;
                        } else {
                            this.mTransitionEasing = Easing.NAMED_EASING[obtainStyledAttributes.getInteger(index, 0)];
                            break;
                        }
                    case 4:
                        this.mDrawPath = obtainStyledAttributes.getInt(index, 0);
                        break;
                    case 5:
                        this.mAnimateRelativeTo = ConstraintSet.lookupID(obtainStyledAttributes, index, this.mAnimateRelativeTo);
                        break;
                    case 6:
                        this.mAnimateCircleAngleTo = obtainStyledAttributes.getInteger(index, this.mAnimateCircleAngleTo);
                        break;
                    case 7:
                        this.mMotionStagger = obtainStyledAttributes.getFloat(index, this.mMotionStagger);
                        break;
                    case 8:
                        this.mQuantizeMotionSteps = obtainStyledAttributes.getInteger(index, this.mQuantizeMotionSteps);
                        break;
                    case 9:
                        this.mQuantizeMotionPhase = obtainStyledAttributes.getFloat(index, this.mQuantizeMotionPhase);
                        break;
                    case 10:
                        int i2 = obtainStyledAttributes.peekValue(index).type;
                        if (i2 == 1) {
                            int resourceId = obtainStyledAttributes.getResourceId(index, -1);
                            this.mQuantizeInterpolatorID = resourceId;
                            if (resourceId != -1) {
                                this.mQuantizeInterpolatorType = -2;
                                break;
                            } else {
                                break;
                            }
                        } else if (i2 == 3) {
                            String string = obtainStyledAttributes.getString(index);
                            this.mQuantizeInterpolatorString = string;
                            if (string.indexOf(h.g) > 0) {
                                this.mQuantizeInterpolatorID = obtainStyledAttributes.getResourceId(index, -1);
                                this.mQuantizeInterpolatorType = -2;
                                break;
                            } else {
                                this.mQuantizeInterpolatorType = -1;
                                break;
                            }
                        } else {
                            this.mQuantizeInterpolatorType = obtainStyledAttributes.getInteger(index, this.mQuantizeInterpolatorID);
                            break;
                        }
                }
            }
            obtainStyledAttributes.recycle();
        }
    }

    /* loaded from: classes.dex */
    public static class Constraint {
        public Delta mDelta;
        public String mTargetString;
        public int mViewId;
        public final PropertySet propertySet = new PropertySet();
        public final Motion motion = new Motion();
        public final Layout layout = new Layout();
        public final Transform transform = new Transform();
        public HashMap<String, ConstraintAttribute> mCustomConstraints = new HashMap<>();

        /* loaded from: classes.dex */
        public static class Delta {
            public int[] mTypeInt = new int[10];
            public int[] mValueInt = new int[10];
            public int mCountInt = 0;
            public int[] mTypeFloat = new int[10];
            public float[] mValueFloat = new float[10];
            public int mCountFloat = 0;
            public int[] mTypeString = new int[5];
            public String[] mValueString = new String[5];
            public int mCountString = 0;
            public int[] mTypeBoolean = new int[4];
            public boolean[] mValueBoolean = new boolean[4];
            public int mCountBoolean = 0;

            public void add(int type, int value) {
                int i = this.mCountInt;
                int[] iArr = this.mTypeInt;
                if (i >= iArr.length) {
                    this.mTypeInt = Arrays.copyOf(iArr, iArr.length * 2);
                    int[] iArr2 = this.mValueInt;
                    this.mValueInt = Arrays.copyOf(iArr2, iArr2.length * 2);
                }
                int[] iArr3 = this.mTypeInt;
                int i2 = this.mCountInt;
                iArr3[i2] = type;
                int[] iArr4 = this.mValueInt;
                this.mCountInt = i2 + 1;
                iArr4[i2] = value;
            }

            public void add(int type, float value) {
                int i = this.mCountFloat;
                int[] iArr = this.mTypeFloat;
                if (i >= iArr.length) {
                    this.mTypeFloat = Arrays.copyOf(iArr, iArr.length * 2);
                    float[] fArr = this.mValueFloat;
                    this.mValueFloat = Arrays.copyOf(fArr, fArr.length * 2);
                }
                int[] iArr2 = this.mTypeFloat;
                int i2 = this.mCountFloat;
                iArr2[i2] = type;
                float[] fArr2 = this.mValueFloat;
                this.mCountFloat = i2 + 1;
                fArr2[i2] = value;
            }

            public void add(int type, String value) {
                int i = this.mCountString;
                int[] iArr = this.mTypeString;
                if (i >= iArr.length) {
                    this.mTypeString = Arrays.copyOf(iArr, iArr.length * 2);
                    String[] strArr = this.mValueString;
                    this.mValueString = (String[]) Arrays.copyOf(strArr, strArr.length * 2);
                }
                int[] iArr2 = this.mTypeString;
                int i2 = this.mCountString;
                iArr2[i2] = type;
                String[] strArr2 = this.mValueString;
                this.mCountString = i2 + 1;
                strArr2[i2] = value;
            }

            public void add(int type, boolean value) {
                int i = this.mCountBoolean;
                int[] iArr = this.mTypeBoolean;
                if (i >= iArr.length) {
                    this.mTypeBoolean = Arrays.copyOf(iArr, iArr.length * 2);
                    boolean[] zArr = this.mValueBoolean;
                    this.mValueBoolean = Arrays.copyOf(zArr, zArr.length * 2);
                }
                int[] iArr2 = this.mTypeBoolean;
                int i2 = this.mCountBoolean;
                iArr2[i2] = type;
                boolean[] zArr2 = this.mValueBoolean;
                this.mCountBoolean = i2 + 1;
                zArr2[i2] = value;
            }
        }

        /* renamed from: clone */
        public Constraint m69clone() {
            Constraint constraint = new Constraint();
            constraint.layout.copyFrom(this.layout);
            constraint.motion.copyFrom(this.motion);
            constraint.propertySet.copyFrom(this.propertySet);
            constraint.transform.copyFrom(this.transform);
            constraint.mViewId = this.mViewId;
            constraint.mDelta = this.mDelta;
            return constraint;
        }

        public final void fillFromConstraints(ConstraintHelper helper, int viewId, Constraints.LayoutParams param) {
            fillFromConstraints(viewId, param);
            if (helper instanceof Barrier) {
                Layout layout = this.layout;
                layout.mHelperType = 1;
                Barrier barrier = (Barrier) helper;
                layout.mBarrierDirection = barrier.getType();
                this.layout.mReferenceIds = barrier.getReferencedIds();
                this.layout.mBarrierMargin = barrier.getMargin();
            }
        }

        public final void fillFromConstraints(int viewId, Constraints.LayoutParams param) {
            fillFrom(viewId, param);
            this.propertySet.alpha = param.alpha;
            Transform transform = this.transform;
            transform.rotation = param.rotation;
            transform.rotationX = param.rotationX;
            transform.rotationY = param.rotationY;
            transform.scaleX = param.scaleX;
            transform.scaleY = param.scaleY;
            transform.transformPivotX = param.transformPivotX;
            transform.transformPivotY = param.transformPivotY;
            transform.translationX = param.translationX;
            transform.translationY = param.translationY;
            transform.translationZ = param.translationZ;
            transform.elevation = param.elevation;
            transform.applyElevation = param.applyElevation;
        }

        public final void fillFrom(int viewId, ConstraintLayout.LayoutParams param) {
            this.mViewId = viewId;
            Layout layout = this.layout;
            layout.leftToLeft = param.leftToLeft;
            layout.leftToRight = param.leftToRight;
            layout.rightToLeft = param.rightToLeft;
            layout.rightToRight = param.rightToRight;
            layout.topToTop = param.topToTop;
            layout.topToBottom = param.topToBottom;
            layout.bottomToTop = param.bottomToTop;
            layout.bottomToBottom = param.bottomToBottom;
            layout.baselineToBaseline = param.baselineToBaseline;
            layout.baselineToTop = param.baselineToTop;
            layout.baselineToBottom = param.baselineToBottom;
            layout.startToEnd = param.startToEnd;
            layout.startToStart = param.startToStart;
            layout.endToStart = param.endToStart;
            layout.endToEnd = param.endToEnd;
            layout.horizontalBias = param.horizontalBias;
            layout.verticalBias = param.verticalBias;
            layout.dimensionRatio = param.dimensionRatio;
            layout.circleConstraint = param.circleConstraint;
            layout.circleRadius = param.circleRadius;
            layout.circleAngle = param.circleAngle;
            layout.editorAbsoluteX = param.editorAbsoluteX;
            layout.editorAbsoluteY = param.editorAbsoluteY;
            layout.orientation = param.orientation;
            layout.guidePercent = param.guidePercent;
            layout.guideBegin = param.guideBegin;
            layout.guideEnd = param.guideEnd;
            layout.mWidth = ((ViewGroup.MarginLayoutParams) param).width;
            layout.mHeight = ((ViewGroup.MarginLayoutParams) param).height;
            layout.leftMargin = ((ViewGroup.MarginLayoutParams) param).leftMargin;
            layout.rightMargin = ((ViewGroup.MarginLayoutParams) param).rightMargin;
            layout.topMargin = ((ViewGroup.MarginLayoutParams) param).topMargin;
            layout.bottomMargin = ((ViewGroup.MarginLayoutParams) param).bottomMargin;
            layout.baselineMargin = param.baselineMargin;
            layout.verticalWeight = param.verticalWeight;
            layout.horizontalWeight = param.horizontalWeight;
            layout.verticalChainStyle = param.verticalChainStyle;
            layout.horizontalChainStyle = param.horizontalChainStyle;
            layout.constrainedWidth = param.constrainedWidth;
            layout.constrainedHeight = param.constrainedHeight;
            layout.widthDefault = param.matchConstraintDefaultWidth;
            layout.heightDefault = param.matchConstraintDefaultHeight;
            layout.widthMax = param.matchConstraintMaxWidth;
            layout.heightMax = param.matchConstraintMaxHeight;
            layout.widthMin = param.matchConstraintMinWidth;
            layout.heightMin = param.matchConstraintMinHeight;
            layout.widthPercent = param.matchConstraintPercentWidth;
            layout.heightPercent = param.matchConstraintPercentHeight;
            layout.mConstraintTag = param.constraintTag;
            layout.goneTopMargin = param.goneTopMargin;
            layout.goneBottomMargin = param.goneBottomMargin;
            layout.goneLeftMargin = param.goneLeftMargin;
            layout.goneRightMargin = param.goneRightMargin;
            layout.goneStartMargin = param.goneStartMargin;
            layout.goneEndMargin = param.goneEndMargin;
            layout.goneBaselineMargin = param.goneBaselineMargin;
            layout.mWrapBehavior = param.wrapBehaviorInParent;
            if (Build.VERSION.SDK_INT >= 17) {
                layout.endMargin = param.getMarginEnd();
                this.layout.startMargin = param.getMarginStart();
            }
        }

        public void applyTo(ConstraintLayout.LayoutParams param) {
            Layout layout = this.layout;
            param.leftToLeft = layout.leftToLeft;
            param.leftToRight = layout.leftToRight;
            param.rightToLeft = layout.rightToLeft;
            param.rightToRight = layout.rightToRight;
            param.topToTop = layout.topToTop;
            param.topToBottom = layout.topToBottom;
            param.bottomToTop = layout.bottomToTop;
            param.bottomToBottom = layout.bottomToBottom;
            param.baselineToBaseline = layout.baselineToBaseline;
            param.baselineToTop = layout.baselineToTop;
            param.baselineToBottom = layout.baselineToBottom;
            param.startToEnd = layout.startToEnd;
            param.startToStart = layout.startToStart;
            param.endToStart = layout.endToStart;
            param.endToEnd = layout.endToEnd;
            ((ViewGroup.MarginLayoutParams) param).leftMargin = layout.leftMargin;
            ((ViewGroup.MarginLayoutParams) param).rightMargin = layout.rightMargin;
            ((ViewGroup.MarginLayoutParams) param).topMargin = layout.topMargin;
            ((ViewGroup.MarginLayoutParams) param).bottomMargin = layout.bottomMargin;
            param.goneStartMargin = layout.goneStartMargin;
            param.goneEndMargin = layout.goneEndMargin;
            param.goneTopMargin = layout.goneTopMargin;
            param.goneBottomMargin = layout.goneBottomMargin;
            param.horizontalBias = layout.horizontalBias;
            param.verticalBias = layout.verticalBias;
            param.circleConstraint = layout.circleConstraint;
            param.circleRadius = layout.circleRadius;
            param.circleAngle = layout.circleAngle;
            param.dimensionRatio = layout.dimensionRatio;
            param.editorAbsoluteX = layout.editorAbsoluteX;
            param.editorAbsoluteY = layout.editorAbsoluteY;
            param.verticalWeight = layout.verticalWeight;
            param.horizontalWeight = layout.horizontalWeight;
            param.verticalChainStyle = layout.verticalChainStyle;
            param.horizontalChainStyle = layout.horizontalChainStyle;
            param.constrainedWidth = layout.constrainedWidth;
            param.constrainedHeight = layout.constrainedHeight;
            param.matchConstraintDefaultWidth = layout.widthDefault;
            param.matchConstraintDefaultHeight = layout.heightDefault;
            param.matchConstraintMaxWidth = layout.widthMax;
            param.matchConstraintMaxHeight = layout.heightMax;
            param.matchConstraintMinWidth = layout.widthMin;
            param.matchConstraintMinHeight = layout.heightMin;
            param.matchConstraintPercentWidth = layout.widthPercent;
            param.matchConstraintPercentHeight = layout.heightPercent;
            param.orientation = layout.orientation;
            param.guidePercent = layout.guidePercent;
            param.guideBegin = layout.guideBegin;
            param.guideEnd = layout.guideEnd;
            ((ViewGroup.MarginLayoutParams) param).width = layout.mWidth;
            ((ViewGroup.MarginLayoutParams) param).height = layout.mHeight;
            String str = layout.mConstraintTag;
            if (str != null) {
                param.constraintTag = str;
            }
            param.wrapBehaviorInParent = layout.mWrapBehavior;
            if (Build.VERSION.SDK_INT >= 17) {
                param.setMarginStart(layout.startMargin);
                param.setMarginEnd(this.layout.endMargin);
            }
            param.validate();
        }
    }

    public void clone(Context context, int constraintLayoutId) {
        clone((ConstraintLayout) LayoutInflater.from(context).inflate(constraintLayoutId, (ViewGroup) null));
    }

    public void clone(ConstraintLayout constraintLayout) {
        int childCount = constraintLayout.getChildCount();
        this.mConstraints.clear();
        for (int i = 0; i < childCount; i++) {
            View childAt = constraintLayout.getChildAt(i);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) childAt.getLayoutParams();
            int id = childAt.getId();
            if (this.mForceId && id == -1) {
                throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
            }
            if (!this.mConstraints.containsKey(Integer.valueOf(id))) {
                this.mConstraints.put(Integer.valueOf(id), new Constraint());
            }
            Constraint constraint = this.mConstraints.get(Integer.valueOf(id));
            if (constraint != null) {
                constraint.mCustomConstraints = ConstraintAttribute.extractAttributes(this.mSavedAttributes, childAt);
                constraint.fillFrom(id, layoutParams);
                constraint.propertySet.visibility = childAt.getVisibility();
                int i2 = Build.VERSION.SDK_INT;
                if (i2 >= 17) {
                    constraint.propertySet.alpha = childAt.getAlpha();
                    constraint.transform.rotation = childAt.getRotation();
                    constraint.transform.rotationX = childAt.getRotationX();
                    constraint.transform.rotationY = childAt.getRotationY();
                    constraint.transform.scaleX = childAt.getScaleX();
                    constraint.transform.scaleY = childAt.getScaleY();
                    float pivotX = childAt.getPivotX();
                    float pivotY = childAt.getPivotY();
                    if (pivotX != SearchStatUtils.POW || pivotY != SearchStatUtils.POW) {
                        Transform transform = constraint.transform;
                        transform.transformPivotX = pivotX;
                        transform.transformPivotY = pivotY;
                    }
                    constraint.transform.translationX = childAt.getTranslationX();
                    constraint.transform.translationY = childAt.getTranslationY();
                    if (i2 >= 21) {
                        constraint.transform.translationZ = childAt.getTranslationZ();
                        Transform transform2 = constraint.transform;
                        if (transform2.applyElevation) {
                            transform2.elevation = childAt.getElevation();
                        }
                    }
                }
                if (childAt instanceof Barrier) {
                    Barrier barrier = (Barrier) childAt;
                    constraint.layout.mBarrierAllowsGoneWidgets = barrier.getAllowsGoneWidget();
                    constraint.layout.mReferenceIds = barrier.getReferencedIds();
                    constraint.layout.mBarrierDirection = barrier.getType();
                    constraint.layout.mBarrierMargin = barrier.getMargin();
                }
            }
        }
    }

    public void clone(Constraints constraints) {
        int childCount = constraints.getChildCount();
        this.mConstraints.clear();
        for (int i = 0; i < childCount; i++) {
            View childAt = constraints.getChildAt(i);
            Constraints.LayoutParams layoutParams = (Constraints.LayoutParams) childAt.getLayoutParams();
            int id = childAt.getId();
            if (this.mForceId && id == -1) {
                throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
            }
            if (!this.mConstraints.containsKey(Integer.valueOf(id))) {
                this.mConstraints.put(Integer.valueOf(id), new Constraint());
            }
            Constraint constraint = this.mConstraints.get(Integer.valueOf(id));
            if (constraint != null) {
                if (childAt instanceof ConstraintHelper) {
                    constraint.fillFromConstraints((ConstraintHelper) childAt, id, layoutParams);
                }
                constraint.fillFromConstraints(id, layoutParams);
            }
        }
    }

    public void applyTo(ConstraintLayout constraintLayout) {
        applyToInternal(constraintLayout, true);
        constraintLayout.setConstraintSet(null);
        constraintLayout.requestLayout();
    }

    public void applyToInternal(ConstraintLayout constraintLayout, boolean applyPostLayout) {
        View findViewById;
        int childCount = constraintLayout.getChildCount();
        HashSet hashSet = new HashSet(this.mConstraints.keySet());
        for (int i = 0; i < childCount; i++) {
            View childAt = constraintLayout.getChildAt(i);
            int id = childAt.getId();
            if (!this.mConstraints.containsKey(Integer.valueOf(id))) {
                String valueOf = String.valueOf(Debug.getName(childAt));
                Log.w("ConstraintSet", valueOf.length() != 0 ? "id unknown ".concat(valueOf) : new String("id unknown "));
            } else if (this.mForceId && id == -1) {
                throw new RuntimeException("All children of ConstraintLayout must have ids to use ConstraintSet");
            } else {
                if (id != -1) {
                    if (this.mConstraints.containsKey(Integer.valueOf(id))) {
                        hashSet.remove(Integer.valueOf(id));
                        Constraint constraint = this.mConstraints.get(Integer.valueOf(id));
                        if (constraint != null) {
                            if (childAt instanceof Barrier) {
                                constraint.layout.mHelperType = 1;
                                Barrier barrier = (Barrier) childAt;
                                barrier.setId(id);
                                barrier.setType(constraint.layout.mBarrierDirection);
                                barrier.setMargin(constraint.layout.mBarrierMargin);
                                barrier.setAllowsGoneWidget(constraint.layout.mBarrierAllowsGoneWidgets);
                                Layout layout = constraint.layout;
                                int[] iArr = layout.mReferenceIds;
                                if (iArr != null) {
                                    barrier.setReferencedIds(iArr);
                                } else {
                                    String str = layout.mReferenceIdString;
                                    if (str != null) {
                                        layout.mReferenceIds = convertReferenceString(barrier, str);
                                        barrier.setReferencedIds(constraint.layout.mReferenceIds);
                                    }
                                }
                            }
                            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) childAt.getLayoutParams();
                            layoutParams.validate();
                            constraint.applyTo(layoutParams);
                            if (applyPostLayout) {
                                ConstraintAttribute.setAttributes(childAt, constraint.mCustomConstraints);
                            }
                            childAt.setLayoutParams(layoutParams);
                            PropertySet propertySet = constraint.propertySet;
                            if (propertySet.mVisibilityMode == 0) {
                                childAt.setVisibility(propertySet.visibility);
                            }
                            int i2 = Build.VERSION.SDK_INT;
                            if (i2 >= 17) {
                                childAt.setAlpha(constraint.propertySet.alpha);
                                childAt.setRotation(constraint.transform.rotation);
                                childAt.setRotationX(constraint.transform.rotationX);
                                childAt.setRotationY(constraint.transform.rotationY);
                                childAt.setScaleX(constraint.transform.scaleX);
                                childAt.setScaleY(constraint.transform.scaleY);
                                Transform transform = constraint.transform;
                                if (transform.transformPivotTarget != -1) {
                                    if (((View) childAt.getParent()).findViewById(constraint.transform.transformPivotTarget) != null) {
                                        float top = (findViewById.getTop() + findViewById.getBottom()) / 2.0f;
                                        float left = (findViewById.getLeft() + findViewById.getRight()) / 2.0f;
                                        if (childAt.getRight() - childAt.getLeft() > 0 && childAt.getBottom() - childAt.getTop() > 0) {
                                            childAt.setPivotX(left - childAt.getLeft());
                                            childAt.setPivotY(top - childAt.getTop());
                                        }
                                    }
                                } else {
                                    if (!Float.isNaN(transform.transformPivotX)) {
                                        childAt.setPivotX(constraint.transform.transformPivotX);
                                    }
                                    if (!Float.isNaN(constraint.transform.transformPivotY)) {
                                        childAt.setPivotY(constraint.transform.transformPivotY);
                                    }
                                }
                                childAt.setTranslationX(constraint.transform.translationX);
                                childAt.setTranslationY(constraint.transform.translationY);
                                if (i2 >= 21) {
                                    childAt.setTranslationZ(constraint.transform.translationZ);
                                    Transform transform2 = constraint.transform;
                                    if (transform2.applyElevation) {
                                        childAt.setElevation(transform2.elevation);
                                    }
                                }
                            }
                        }
                    } else {
                        StringBuilder sb = new StringBuilder(43);
                        sb.append("WARNING NO CONSTRAINTS for view ");
                        sb.append(id);
                        Log.v("ConstraintSet", sb.toString());
                    }
                }
            }
        }
        Iterator it = hashSet.iterator();
        while (it.hasNext()) {
            Integer num = (Integer) it.next();
            Constraint constraint2 = this.mConstraints.get(num);
            if (constraint2 != null) {
                if (constraint2.layout.mHelperType == 1) {
                    Barrier barrier2 = new Barrier(constraintLayout.getContext());
                    barrier2.setId(num.intValue());
                    Layout layout2 = constraint2.layout;
                    int[] iArr2 = layout2.mReferenceIds;
                    if (iArr2 != null) {
                        barrier2.setReferencedIds(iArr2);
                    } else {
                        String str2 = layout2.mReferenceIdString;
                        if (str2 != null) {
                            layout2.mReferenceIds = convertReferenceString(barrier2, str2);
                            barrier2.setReferencedIds(constraint2.layout.mReferenceIds);
                        }
                    }
                    barrier2.setType(constraint2.layout.mBarrierDirection);
                    barrier2.setMargin(constraint2.layout.mBarrierMargin);
                    ConstraintLayout.LayoutParams generateDefaultLayoutParams = constraintLayout.generateDefaultLayoutParams();
                    barrier2.validateParams();
                    constraint2.applyTo(generateDefaultLayoutParams);
                    constraintLayout.addView(barrier2, generateDefaultLayoutParams);
                }
                if (constraint2.layout.mIsGuideline) {
                    View guideline = new Guideline(constraintLayout.getContext());
                    guideline.setId(num.intValue());
                    ConstraintLayout.LayoutParams generateDefaultLayoutParams2 = constraintLayout.generateDefaultLayoutParams();
                    constraint2.applyTo(generateDefaultLayoutParams2);
                    constraintLayout.addView(guideline, generateDefaultLayoutParams2);
                }
            }
        }
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt2 = constraintLayout.getChildAt(i3);
            if (childAt2 instanceof ConstraintHelper) {
                ((ConstraintHelper) childAt2).applyLayoutFeaturesInConstraintSet(constraintLayout);
            }
        }
    }

    public void center(int centerID, int firstID, int firstSide, int firstMargin, int secondId, int secondSide, int secondMargin, float bias) {
        if (firstMargin >= 0) {
            if (secondMargin < 0) {
                throw new IllegalArgumentException("margin must be > 0");
            }
            if (bias <= 0.0f || bias > 1.0f) {
                throw new IllegalArgumentException("bias must be between 0 and 1 inclusive");
            }
            if (firstSide == 1 || firstSide == 2) {
                connect(centerID, 1, firstID, firstSide, firstMargin);
                connect(centerID, 2, secondId, secondSide, secondMargin);
                Constraint constraint = this.mConstraints.get(Integer.valueOf(centerID));
                if (constraint == null) {
                    return;
                }
                constraint.layout.horizontalBias = bias;
                return;
            } else if (firstSide == 6 || firstSide == 7) {
                connect(centerID, 6, firstID, firstSide, firstMargin);
                connect(centerID, 7, secondId, secondSide, secondMargin);
                Constraint constraint2 = this.mConstraints.get(Integer.valueOf(centerID));
                if (constraint2 == null) {
                    return;
                }
                constraint2.layout.horizontalBias = bias;
                return;
            } else {
                connect(centerID, 3, firstID, firstSide, firstMargin);
                connect(centerID, 4, secondId, secondSide, secondMargin);
                Constraint constraint3 = this.mConstraints.get(Integer.valueOf(centerID));
                if (constraint3 == null) {
                    return;
                }
                constraint3.layout.verticalBias = bias;
                return;
            }
        }
        throw new IllegalArgumentException("margin must be > 0");
    }

    public void connect(int startID, int startSide, int endID, int endSide, int margin) {
        if (!this.mConstraints.containsKey(Integer.valueOf(startID))) {
            this.mConstraints.put(Integer.valueOf(startID), new Constraint());
        }
        Constraint constraint = this.mConstraints.get(Integer.valueOf(startID));
        if (constraint == null) {
            return;
        }
        switch (startSide) {
            case 1:
                if (endSide == 1) {
                    Layout layout = constraint.layout;
                    layout.leftToLeft = endID;
                    layout.leftToRight = -1;
                } else if (endSide == 2) {
                    Layout layout2 = constraint.layout;
                    layout2.leftToRight = endID;
                    layout2.leftToLeft = -1;
                } else {
                    String sideToString = sideToString(endSide);
                    StringBuilder sb = new StringBuilder(String.valueOf(sideToString).length() + 18);
                    sb.append("Left to ");
                    sb.append(sideToString);
                    sb.append(" undefined");
                    throw new IllegalArgumentException(sb.toString());
                }
                constraint.layout.leftMargin = margin;
                return;
            case 2:
                if (endSide == 1) {
                    Layout layout3 = constraint.layout;
                    layout3.rightToLeft = endID;
                    layout3.rightToRight = -1;
                } else if (endSide == 2) {
                    Layout layout4 = constraint.layout;
                    layout4.rightToRight = endID;
                    layout4.rightToLeft = -1;
                } else {
                    String sideToString2 = sideToString(endSide);
                    StringBuilder sb2 = new StringBuilder(String.valueOf(sideToString2).length() + 19);
                    sb2.append("right to ");
                    sb2.append(sideToString2);
                    sb2.append(" undefined");
                    throw new IllegalArgumentException(sb2.toString());
                }
                constraint.layout.rightMargin = margin;
                return;
            case 3:
                if (endSide == 3) {
                    Layout layout5 = constraint.layout;
                    layout5.topToTop = endID;
                    layout5.topToBottom = -1;
                    layout5.baselineToBaseline = -1;
                    layout5.baselineToTop = -1;
                    layout5.baselineToBottom = -1;
                } else if (endSide == 4) {
                    Layout layout6 = constraint.layout;
                    layout6.topToBottom = endID;
                    layout6.topToTop = -1;
                    layout6.baselineToBaseline = -1;
                    layout6.baselineToTop = -1;
                    layout6.baselineToBottom = -1;
                } else {
                    String sideToString3 = sideToString(endSide);
                    StringBuilder sb3 = new StringBuilder(String.valueOf(sideToString3).length() + 19);
                    sb3.append("right to ");
                    sb3.append(sideToString3);
                    sb3.append(" undefined");
                    throw new IllegalArgumentException(sb3.toString());
                }
                constraint.layout.topMargin = margin;
                return;
            case 4:
                if (endSide == 4) {
                    Layout layout7 = constraint.layout;
                    layout7.bottomToBottom = endID;
                    layout7.bottomToTop = -1;
                    layout7.baselineToBaseline = -1;
                    layout7.baselineToTop = -1;
                    layout7.baselineToBottom = -1;
                } else if (endSide == 3) {
                    Layout layout8 = constraint.layout;
                    layout8.bottomToTop = endID;
                    layout8.bottomToBottom = -1;
                    layout8.baselineToBaseline = -1;
                    layout8.baselineToTop = -1;
                    layout8.baselineToBottom = -1;
                } else {
                    String sideToString4 = sideToString(endSide);
                    StringBuilder sb4 = new StringBuilder(String.valueOf(sideToString4).length() + 19);
                    sb4.append("right to ");
                    sb4.append(sideToString4);
                    sb4.append(" undefined");
                    throw new IllegalArgumentException(sb4.toString());
                }
                constraint.layout.bottomMargin = margin;
                return;
            case 5:
                if (endSide == 5) {
                    Layout layout9 = constraint.layout;
                    layout9.baselineToBaseline = endID;
                    layout9.bottomToBottom = -1;
                    layout9.bottomToTop = -1;
                    layout9.topToTop = -1;
                    layout9.topToBottom = -1;
                    return;
                } else if (endSide == 3) {
                    Layout layout10 = constraint.layout;
                    layout10.baselineToTop = endID;
                    layout10.bottomToBottom = -1;
                    layout10.bottomToTop = -1;
                    layout10.topToTop = -1;
                    layout10.topToBottom = -1;
                    return;
                } else if (endSide == 4) {
                    Layout layout11 = constraint.layout;
                    layout11.baselineToBottom = endID;
                    layout11.bottomToBottom = -1;
                    layout11.bottomToTop = -1;
                    layout11.topToTop = -1;
                    layout11.topToBottom = -1;
                    return;
                } else {
                    String sideToString5 = sideToString(endSide);
                    StringBuilder sb5 = new StringBuilder(String.valueOf(sideToString5).length() + 19);
                    sb5.append("right to ");
                    sb5.append(sideToString5);
                    sb5.append(" undefined");
                    throw new IllegalArgumentException(sb5.toString());
                }
            case 6:
                if (endSide == 6) {
                    Layout layout12 = constraint.layout;
                    layout12.startToStart = endID;
                    layout12.startToEnd = -1;
                } else if (endSide == 7) {
                    Layout layout13 = constraint.layout;
                    layout13.startToEnd = endID;
                    layout13.startToStart = -1;
                } else {
                    String sideToString6 = sideToString(endSide);
                    StringBuilder sb6 = new StringBuilder(String.valueOf(sideToString6).length() + 19);
                    sb6.append("right to ");
                    sb6.append(sideToString6);
                    sb6.append(" undefined");
                    throw new IllegalArgumentException(sb6.toString());
                }
                constraint.layout.startMargin = margin;
                return;
            case 7:
                if (endSide == 7) {
                    Layout layout14 = constraint.layout;
                    layout14.endToEnd = endID;
                    layout14.endToStart = -1;
                } else if (endSide == 6) {
                    Layout layout15 = constraint.layout;
                    layout15.endToStart = endID;
                    layout15.endToEnd = -1;
                } else {
                    String sideToString7 = sideToString(endSide);
                    StringBuilder sb7 = new StringBuilder(String.valueOf(sideToString7).length() + 19);
                    sb7.append("right to ");
                    sb7.append(sideToString7);
                    sb7.append(" undefined");
                    throw new IllegalArgumentException(sb7.toString());
                }
                constraint.layout.endMargin = margin;
                return;
            default:
                String sideToString8 = sideToString(startSide);
                String sideToString9 = sideToString(endSide);
                StringBuilder sb8 = new StringBuilder(String.valueOf(sideToString8).length() + 12 + String.valueOf(sideToString9).length());
                sb8.append(sideToString8);
                sb8.append(" to ");
                sb8.append(sideToString9);
                sb8.append(" unknown");
                throw new IllegalArgumentException(sb8.toString());
        }
    }

    public void connect(int startID, int startSide, int endID, int endSide) {
        if (!this.mConstraints.containsKey(Integer.valueOf(startID))) {
            this.mConstraints.put(Integer.valueOf(startID), new Constraint());
        }
        Constraint constraint = this.mConstraints.get(Integer.valueOf(startID));
        if (constraint == null) {
            return;
        }
        switch (startSide) {
            case 1:
                if (endSide == 1) {
                    Layout layout = constraint.layout;
                    layout.leftToLeft = endID;
                    layout.leftToRight = -1;
                    return;
                } else if (endSide == 2) {
                    Layout layout2 = constraint.layout;
                    layout2.leftToRight = endID;
                    layout2.leftToLeft = -1;
                    return;
                } else {
                    String sideToString = sideToString(endSide);
                    StringBuilder sb = new StringBuilder(String.valueOf(sideToString).length() + 18);
                    sb.append("left to ");
                    sb.append(sideToString);
                    sb.append(" undefined");
                    throw new IllegalArgumentException(sb.toString());
                }
            case 2:
                if (endSide == 1) {
                    Layout layout3 = constraint.layout;
                    layout3.rightToLeft = endID;
                    layout3.rightToRight = -1;
                    return;
                } else if (endSide == 2) {
                    Layout layout4 = constraint.layout;
                    layout4.rightToRight = endID;
                    layout4.rightToLeft = -1;
                    return;
                } else {
                    String sideToString2 = sideToString(endSide);
                    StringBuilder sb2 = new StringBuilder(String.valueOf(sideToString2).length() + 19);
                    sb2.append("right to ");
                    sb2.append(sideToString2);
                    sb2.append(" undefined");
                    throw new IllegalArgumentException(sb2.toString());
                }
            case 3:
                if (endSide == 3) {
                    Layout layout5 = constraint.layout;
                    layout5.topToTop = endID;
                    layout5.topToBottom = -1;
                    layout5.baselineToBaseline = -1;
                    layout5.baselineToTop = -1;
                    layout5.baselineToBottom = -1;
                    return;
                } else if (endSide == 4) {
                    Layout layout6 = constraint.layout;
                    layout6.topToBottom = endID;
                    layout6.topToTop = -1;
                    layout6.baselineToBaseline = -1;
                    layout6.baselineToTop = -1;
                    layout6.baselineToBottom = -1;
                    return;
                } else {
                    String sideToString3 = sideToString(endSide);
                    StringBuilder sb3 = new StringBuilder(String.valueOf(sideToString3).length() + 19);
                    sb3.append("right to ");
                    sb3.append(sideToString3);
                    sb3.append(" undefined");
                    throw new IllegalArgumentException(sb3.toString());
                }
            case 4:
                if (endSide == 4) {
                    Layout layout7 = constraint.layout;
                    layout7.bottomToBottom = endID;
                    layout7.bottomToTop = -1;
                    layout7.baselineToBaseline = -1;
                    layout7.baselineToTop = -1;
                    layout7.baselineToBottom = -1;
                    return;
                } else if (endSide == 3) {
                    Layout layout8 = constraint.layout;
                    layout8.bottomToTop = endID;
                    layout8.bottomToBottom = -1;
                    layout8.baselineToBaseline = -1;
                    layout8.baselineToTop = -1;
                    layout8.baselineToBottom = -1;
                    return;
                } else {
                    String sideToString4 = sideToString(endSide);
                    StringBuilder sb4 = new StringBuilder(String.valueOf(sideToString4).length() + 19);
                    sb4.append("right to ");
                    sb4.append(sideToString4);
                    sb4.append(" undefined");
                    throw new IllegalArgumentException(sb4.toString());
                }
            case 5:
                if (endSide == 5) {
                    Layout layout9 = constraint.layout;
                    layout9.baselineToBaseline = endID;
                    layout9.bottomToBottom = -1;
                    layout9.bottomToTop = -1;
                    layout9.topToTop = -1;
                    layout9.topToBottom = -1;
                    return;
                } else if (endSide == 3) {
                    Layout layout10 = constraint.layout;
                    layout10.baselineToTop = endID;
                    layout10.bottomToBottom = -1;
                    layout10.bottomToTop = -1;
                    layout10.topToTop = -1;
                    layout10.topToBottom = -1;
                    return;
                } else if (endSide == 4) {
                    Layout layout11 = constraint.layout;
                    layout11.baselineToBottom = endID;
                    layout11.bottomToBottom = -1;
                    layout11.bottomToTop = -1;
                    layout11.topToTop = -1;
                    layout11.topToBottom = -1;
                    return;
                } else {
                    String sideToString5 = sideToString(endSide);
                    StringBuilder sb5 = new StringBuilder(String.valueOf(sideToString5).length() + 19);
                    sb5.append("right to ");
                    sb5.append(sideToString5);
                    sb5.append(" undefined");
                    throw new IllegalArgumentException(sb5.toString());
                }
            case 6:
                if (endSide == 6) {
                    Layout layout12 = constraint.layout;
                    layout12.startToStart = endID;
                    layout12.startToEnd = -1;
                    return;
                } else if (endSide == 7) {
                    Layout layout13 = constraint.layout;
                    layout13.startToEnd = endID;
                    layout13.startToStart = -1;
                    return;
                } else {
                    String sideToString6 = sideToString(endSide);
                    StringBuilder sb6 = new StringBuilder(String.valueOf(sideToString6).length() + 19);
                    sb6.append("right to ");
                    sb6.append(sideToString6);
                    sb6.append(" undefined");
                    throw new IllegalArgumentException(sb6.toString());
                }
            case 7:
                if (endSide == 7) {
                    Layout layout14 = constraint.layout;
                    layout14.endToEnd = endID;
                    layout14.endToStart = -1;
                    return;
                } else if (endSide == 6) {
                    Layout layout15 = constraint.layout;
                    layout15.endToStart = endID;
                    layout15.endToEnd = -1;
                    return;
                } else {
                    String sideToString7 = sideToString(endSide);
                    StringBuilder sb7 = new StringBuilder(String.valueOf(sideToString7).length() + 19);
                    sb7.append("right to ");
                    sb7.append(sideToString7);
                    sb7.append(" undefined");
                    throw new IllegalArgumentException(sb7.toString());
                }
            default:
                String sideToString8 = sideToString(startSide);
                String sideToString9 = sideToString(endSide);
                StringBuilder sb8 = new StringBuilder(String.valueOf(sideToString8).length() + 12 + String.valueOf(sideToString9).length());
                sb8.append(sideToString8);
                sb8.append(" to ");
                sb8.append(sideToString9);
                sb8.append(" unknown");
                throw new IllegalArgumentException(sb8.toString());
        }
    }

    public void centerHorizontally(int viewId, int toView) {
        if (toView == 0) {
            center(viewId, 0, 1, 0, 0, 2, 0, 0.5f);
        } else {
            center(viewId, toView, 2, 0, toView, 1, 0, 0.5f);
        }
    }

    public void centerVertically(int viewId, int toView) {
        if (toView == 0) {
            center(viewId, 0, 3, 0, 0, 4, 0, 0.5f);
        } else {
            center(viewId, toView, 4, 0, toView, 3, 0, 0.5f);
        }
    }

    public void clear(int viewId, int anchor) {
        Constraint constraint;
        if (!this.mConstraints.containsKey(Integer.valueOf(viewId)) || (constraint = this.mConstraints.get(Integer.valueOf(viewId))) == null) {
            return;
        }
        switch (anchor) {
            case 1:
                Layout layout = constraint.layout;
                layout.leftToRight = -1;
                layout.leftToLeft = -1;
                layout.leftMargin = -1;
                layout.goneLeftMargin = Integer.MIN_VALUE;
                return;
            case 2:
                Layout layout2 = constraint.layout;
                layout2.rightToRight = -1;
                layout2.rightToLeft = -1;
                layout2.rightMargin = -1;
                layout2.goneRightMargin = Integer.MIN_VALUE;
                return;
            case 3:
                Layout layout3 = constraint.layout;
                layout3.topToBottom = -1;
                layout3.topToTop = -1;
                layout3.topMargin = 0;
                layout3.goneTopMargin = Integer.MIN_VALUE;
                return;
            case 4:
                Layout layout4 = constraint.layout;
                layout4.bottomToTop = -1;
                layout4.bottomToBottom = -1;
                layout4.bottomMargin = 0;
                layout4.goneBottomMargin = Integer.MIN_VALUE;
                return;
            case 5:
                Layout layout5 = constraint.layout;
                layout5.baselineToBaseline = -1;
                layout5.baselineToTop = -1;
                layout5.baselineToBottom = -1;
                layout5.baselineMargin = 0;
                layout5.goneBaselineMargin = Integer.MIN_VALUE;
                return;
            case 6:
                Layout layout6 = constraint.layout;
                layout6.startToEnd = -1;
                layout6.startToStart = -1;
                layout6.startMargin = 0;
                layout6.goneStartMargin = Integer.MIN_VALUE;
                return;
            case 7:
                Layout layout7 = constraint.layout;
                layout7.endToStart = -1;
                layout7.endToEnd = -1;
                layout7.endMargin = 0;
                layout7.goneEndMargin = Integer.MIN_VALUE;
                return;
            case 8:
                Layout layout8 = constraint.layout;
                layout8.circleAngle = -1.0f;
                layout8.circleRadius = -1;
                layout8.circleConstraint = -1;
                return;
            default:
                throw new IllegalArgumentException("unknown constraint");
        }
    }

    public void setMargin(int viewId, int anchor, int value) {
        Constraint constraint = get(viewId);
        switch (anchor) {
            case 1:
                constraint.layout.leftMargin = value;
                return;
            case 2:
                constraint.layout.rightMargin = value;
                return;
            case 3:
                constraint.layout.topMargin = value;
                return;
            case 4:
                constraint.layout.bottomMargin = value;
                return;
            case 5:
                constraint.layout.baselineMargin = value;
                return;
            case 6:
                constraint.layout.startMargin = value;
                return;
            case 7:
                constraint.layout.endMargin = value;
                return;
            default:
                throw new IllegalArgumentException("unknown constraint");
        }
    }

    public void setVerticalBias(int viewId, float bias) {
        get(viewId).layout.verticalBias = bias;
    }

    public void constrainHeight(int viewId, int height) {
        get(viewId).layout.mHeight = height;
    }

    public void constrainWidth(int viewId, int width) {
        get(viewId).layout.mWidth = width;
    }

    public void setHorizontalWeight(int viewId, float weight) {
        get(viewId).layout.horizontalWeight = weight;
    }

    public void setHorizontalChainStyle(int viewId, int chainStyle) {
        get(viewId).layout.horizontalChainStyle = chainStyle;
    }

    public final Constraint get(int id) {
        if (!this.mConstraints.containsKey(Integer.valueOf(id))) {
            this.mConstraints.put(Integer.valueOf(id), new Constraint());
        }
        return this.mConstraints.get(Integer.valueOf(id));
    }

    public void load(Context context, int resourceId) {
        XmlResourceParser xml = context.getResources().getXml(resourceId);
        try {
            for (int eventType = xml.getEventType(); eventType != 1; eventType = xml.next()) {
                if (eventType == 0) {
                    xml.getName();
                    continue;
                } else if (eventType != 2) {
                    continue;
                } else {
                    String name = xml.getName();
                    Constraint fillFromAttributeList = fillFromAttributeList(context, Xml.asAttributeSet(xml), false);
                    if (name.equalsIgnoreCase("Guideline")) {
                        fillFromAttributeList.layout.mIsGuideline = true;
                    }
                    this.mConstraints.put(Integer.valueOf(fillFromAttributeList.mViewId), fillFromAttributeList);
                    continue;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:119:0x01cd, code lost:
        continue;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void load(android.content.Context r10, org.xmlpull.v1.XmlPullParser r11) {
        /*
            Method dump skipped, instructions count: 562
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.widget.ConstraintSet.load(android.content.Context, org.xmlpull.v1.XmlPullParser):void");
    }

    public static int lookupID(TypedArray a, int index, int def) {
        int resourceId = a.getResourceId(index, def);
        return resourceId == -1 ? a.getInt(index, -1) : resourceId;
    }

    public final Constraint fillFromAttributeList(Context context, AttributeSet attrs, boolean override) {
        Constraint constraint = new Constraint();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, override ? R$styleable.ConstraintOverride : R$styleable.Constraint);
        populateConstraint(context, constraint, obtainStyledAttributes, override);
        obtainStyledAttributes.recycle();
        return constraint;
    }

    public static void populateOverride(Context ctx, Constraint c, TypedArray a) {
        int indexCount = a.getIndexCount();
        Constraint.Delta delta = new Constraint.Delta();
        c.mDelta = delta;
        c.motion.mApply = false;
        c.layout.mApply = false;
        c.propertySet.mApply = false;
        c.transform.mApply = false;
        for (int i = 0; i < indexCount; i++) {
            int index = a.getIndex(i);
            switch (overrideMapToConstant.get(index)) {
                case 2:
                    delta.add(2, a.getDimensionPixelSize(index, c.layout.bottomMargin));
                    break;
                case 3:
                case 4:
                case 9:
                case 10:
                case 25:
                case 26:
                case 29:
                case 30:
                case 32:
                case 33:
                case 35:
                case 36:
                case 61:
                case 88:
                case 89:
                case 90:
                case 91:
                case 92:
                default:
                    String hexString = Integer.toHexString(index);
                    int i2 = mapToConstant.get(index);
                    StringBuilder sb = new StringBuilder(String.valueOf(hexString).length() + 34);
                    sb.append("Unknown attribute 0x");
                    sb.append(hexString);
                    sb.append("   ");
                    sb.append(i2);
                    Log.w("ConstraintSet", sb.toString());
                    break;
                case 5:
                    delta.add(5, a.getString(index));
                    break;
                case 6:
                    delta.add(6, a.getDimensionPixelOffset(index, c.layout.editorAbsoluteX));
                    break;
                case 7:
                    delta.add(7, a.getDimensionPixelOffset(index, c.layout.editorAbsoluteY));
                    break;
                case 8:
                    if (Build.VERSION.SDK_INT >= 17) {
                        delta.add(8, a.getDimensionPixelSize(index, c.layout.endMargin));
                        break;
                    } else {
                        break;
                    }
                case 11:
                    delta.add(11, a.getDimensionPixelSize(index, c.layout.goneBottomMargin));
                    break;
                case 12:
                    delta.add(12, a.getDimensionPixelSize(index, c.layout.goneEndMargin));
                    break;
                case 13:
                    delta.add(13, a.getDimensionPixelSize(index, c.layout.goneLeftMargin));
                    break;
                case 14:
                    delta.add(14, a.getDimensionPixelSize(index, c.layout.goneRightMargin));
                    break;
                case 15:
                    delta.add(15, a.getDimensionPixelSize(index, c.layout.goneStartMargin));
                    break;
                case 16:
                    delta.add(16, a.getDimensionPixelSize(index, c.layout.goneTopMargin));
                    break;
                case 17:
                    delta.add(17, a.getDimensionPixelOffset(index, c.layout.guideBegin));
                    break;
                case 18:
                    delta.add(18, a.getDimensionPixelOffset(index, c.layout.guideEnd));
                    break;
                case 19:
                    delta.add(19, a.getFloat(index, c.layout.guidePercent));
                    break;
                case 20:
                    delta.add(20, a.getFloat(index, c.layout.horizontalBias));
                    break;
                case 21:
                    delta.add(21, a.getLayoutDimension(index, c.layout.mHeight));
                    break;
                case 22:
                    delta.add(22, VISIBILITY_FLAGS[a.getInt(index, c.propertySet.visibility)]);
                    break;
                case 23:
                    delta.add(23, a.getLayoutDimension(index, c.layout.mWidth));
                    break;
                case 24:
                    delta.add(24, a.getDimensionPixelSize(index, c.layout.leftMargin));
                    break;
                case 27:
                    delta.add(27, a.getInt(index, c.layout.orientation));
                    break;
                case 28:
                    delta.add(28, a.getDimensionPixelSize(index, c.layout.rightMargin));
                    break;
                case 31:
                    if (Build.VERSION.SDK_INT >= 17) {
                        delta.add(31, a.getDimensionPixelSize(index, c.layout.startMargin));
                        break;
                    } else {
                        break;
                    }
                case 34:
                    delta.add(34, a.getDimensionPixelSize(index, c.layout.topMargin));
                    break;
                case 37:
                    delta.add(37, a.getFloat(index, c.layout.verticalBias));
                    break;
                case 38:
                    int resourceId = a.getResourceId(index, c.mViewId);
                    c.mViewId = resourceId;
                    delta.add(38, resourceId);
                    break;
                case 39:
                    delta.add(39, a.getFloat(index, c.layout.horizontalWeight));
                    break;
                case 40:
                    delta.add(40, a.getFloat(index, c.layout.verticalWeight));
                    break;
                case 41:
                    delta.add(41, a.getInt(index, c.layout.horizontalChainStyle));
                    break;
                case 42:
                    delta.add(42, a.getInt(index, c.layout.verticalChainStyle));
                    break;
                case 43:
                    delta.add(43, a.getFloat(index, c.propertySet.alpha));
                    break;
                case 44:
                    if (Build.VERSION.SDK_INT >= 21) {
                        delta.add(44, true);
                        delta.add(44, a.getDimension(index, c.transform.elevation));
                        break;
                    } else {
                        break;
                    }
                case 45:
                    delta.add(45, a.getFloat(index, c.transform.rotationX));
                    break;
                case 46:
                    delta.add(46, a.getFloat(index, c.transform.rotationY));
                    break;
                case 47:
                    delta.add(47, a.getFloat(index, c.transform.scaleX));
                    break;
                case 48:
                    delta.add(48, a.getFloat(index, c.transform.scaleY));
                    break;
                case 49:
                    delta.add(49, a.getDimension(index, c.transform.transformPivotX));
                    break;
                case 50:
                    delta.add(50, a.getDimension(index, c.transform.transformPivotY));
                    break;
                case 51:
                    delta.add(51, a.getDimension(index, c.transform.translationX));
                    break;
                case 52:
                    delta.add(52, a.getDimension(index, c.transform.translationY));
                    break;
                case 53:
                    if (Build.VERSION.SDK_INT >= 21) {
                        delta.add(53, a.getDimension(index, c.transform.translationZ));
                        break;
                    } else {
                        break;
                    }
                case 54:
                    delta.add(54, a.getInt(index, c.layout.widthDefault));
                    break;
                case 55:
                    delta.add(55, a.getInt(index, c.layout.heightDefault));
                    break;
                case 56:
                    delta.add(56, a.getDimensionPixelSize(index, c.layout.widthMax));
                    break;
                case 57:
                    delta.add(57, a.getDimensionPixelSize(index, c.layout.heightMax));
                    break;
                case 58:
                    delta.add(58, a.getDimensionPixelSize(index, c.layout.widthMin));
                    break;
                case 59:
                    delta.add(59, a.getDimensionPixelSize(index, c.layout.heightMin));
                    break;
                case 60:
                    delta.add(60, a.getFloat(index, c.transform.rotation));
                    break;
                case 62:
                    delta.add(62, a.getDimensionPixelSize(index, c.layout.circleRadius));
                    break;
                case 63:
                    delta.add(63, a.getFloat(index, c.layout.circleAngle));
                    break;
                case 64:
                    delta.add(64, lookupID(a, index, c.motion.mAnimateRelativeTo));
                    break;
                case 65:
                    if (a.peekValue(index).type == 3) {
                        delta.add(65, a.getString(index));
                        break;
                    } else {
                        delta.add(65, Easing.NAMED_EASING[a.getInteger(index, 0)]);
                        break;
                    }
                case 66:
                    delta.add(66, a.getInt(index, 0));
                    break;
                case 67:
                    delta.add(67, a.getFloat(index, c.motion.mPathRotate));
                    break;
                case 68:
                    delta.add(68, a.getFloat(index, c.propertySet.mProgress));
                    break;
                case 69:
                    delta.add(69, a.getFloat(index, 1.0f));
                    break;
                case 70:
                    delta.add(70, a.getFloat(index, 1.0f));
                    break;
                case 71:
                    Log.e("ConstraintSet", "CURRENTLY UNSUPPORTED");
                    break;
                case 72:
                    delta.add(72, a.getInt(index, c.layout.mBarrierDirection));
                    break;
                case 73:
                    delta.add(73, a.getDimensionPixelSize(index, c.layout.mBarrierMargin));
                    break;
                case 74:
                    delta.add(74, a.getString(index));
                    break;
                case 75:
                    delta.add(75, a.getBoolean(index, c.layout.mBarrierAllowsGoneWidgets));
                    break;
                case 76:
                    delta.add(76, a.getInt(index, c.motion.mPathMotionArc));
                    break;
                case 77:
                    delta.add(77, a.getString(index));
                    break;
                case 78:
                    delta.add(78, a.getInt(index, c.propertySet.mVisibilityMode));
                    break;
                case 79:
                    delta.add(79, a.getFloat(index, c.motion.mMotionStagger));
                    break;
                case 80:
                    delta.add(80, a.getBoolean(index, c.layout.constrainedWidth));
                    break;
                case 81:
                    delta.add(81, a.getBoolean(index, c.layout.constrainedHeight));
                    break;
                case 82:
                    delta.add(82, a.getInteger(index, c.motion.mAnimateCircleAngleTo));
                    break;
                case 83:
                    delta.add(83, lookupID(a, index, c.transform.transformPivotTarget));
                    break;
                case 84:
                    delta.add(84, a.getInteger(index, c.motion.mQuantizeMotionSteps));
                    break;
                case 85:
                    delta.add(85, a.getFloat(index, c.motion.mQuantizeMotionPhase));
                    break;
                case 86:
                    int i3 = a.peekValue(index).type;
                    if (i3 == 1) {
                        c.motion.mQuantizeInterpolatorID = a.getResourceId(index, -1);
                        delta.add(89, c.motion.mQuantizeInterpolatorID);
                        Motion motion = c.motion;
                        if (motion.mQuantizeInterpolatorID != -1) {
                            motion.mQuantizeInterpolatorType = -2;
                            delta.add(88, -2);
                            break;
                        } else {
                            break;
                        }
                    } else if (i3 == 3) {
                        c.motion.mQuantizeInterpolatorString = a.getString(index);
                        delta.add(90, c.motion.mQuantizeInterpolatorString);
                        if (c.motion.mQuantizeInterpolatorString.indexOf(h.g) > 0) {
                            c.motion.mQuantizeInterpolatorID = a.getResourceId(index, -1);
                            delta.add(89, c.motion.mQuantizeInterpolatorID);
                            c.motion.mQuantizeInterpolatorType = -2;
                            delta.add(88, -2);
                            break;
                        } else {
                            c.motion.mQuantizeInterpolatorType = -1;
                            delta.add(88, -1);
                            break;
                        }
                    } else {
                        Motion motion2 = c.motion;
                        motion2.mQuantizeInterpolatorType = a.getInteger(index, motion2.mQuantizeInterpolatorID);
                        delta.add(88, c.motion.mQuantizeInterpolatorType);
                        break;
                    }
                case 87:
                    String hexString2 = Integer.toHexString(index);
                    int i4 = mapToConstant.get(index);
                    StringBuilder sb2 = new StringBuilder(String.valueOf(hexString2).length() + 33);
                    sb2.append("unused attribute 0x");
                    sb2.append(hexString2);
                    sb2.append("   ");
                    sb2.append(i4);
                    Log.w("ConstraintSet", sb2.toString());
                    break;
                case 93:
                    delta.add(93, a.getDimensionPixelSize(index, c.layout.baselineMargin));
                    break;
                case 94:
                    delta.add(94, a.getDimensionPixelSize(index, c.layout.goneBaselineMargin));
                    break;
                case 95:
                    parseDimensionConstraints(delta, a, index, 0);
                    break;
                case 96:
                    parseDimensionConstraints(delta, a, index, 1);
                    break;
                case 97:
                    delta.add(97, a.getInt(index, c.layout.mWrapBehavior));
                    break;
                case 98:
                    if (MotionLayout.IS_IN_EDIT_MODE) {
                        int resourceId2 = a.getResourceId(index, c.mViewId);
                        c.mViewId = resourceId2;
                        if (resourceId2 == -1) {
                            c.mTargetString = a.getString(index);
                            break;
                        } else {
                            break;
                        }
                    } else if (a.peekValue(index).type == 3) {
                        c.mTargetString = a.getString(index);
                        break;
                    } else {
                        c.mViewId = a.getResourceId(index, c.mViewId);
                        break;
                    }
            }
        }
    }

    public final void populateConstraint(Context ctx, Constraint c, TypedArray a, boolean override) {
        if (override) {
            populateOverride(ctx, c, a);
            return;
        }
        int indexCount = a.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = a.getIndex(i);
            if (index != R$styleable.Constraint_android_id && R$styleable.Constraint_android_layout_marginStart != index && R$styleable.Constraint_android_layout_marginEnd != index) {
                c.motion.mApply = true;
                c.layout.mApply = true;
                c.propertySet.mApply = true;
                c.transform.mApply = true;
            }
            switch (mapToConstant.get(index)) {
                case 1:
                    Layout layout = c.layout;
                    layout.baselineToBaseline = lookupID(a, index, layout.baselineToBaseline);
                    break;
                case 2:
                    Layout layout2 = c.layout;
                    layout2.bottomMargin = a.getDimensionPixelSize(index, layout2.bottomMargin);
                    break;
                case 3:
                    Layout layout3 = c.layout;
                    layout3.bottomToBottom = lookupID(a, index, layout3.bottomToBottom);
                    break;
                case 4:
                    Layout layout4 = c.layout;
                    layout4.bottomToTop = lookupID(a, index, layout4.bottomToTop);
                    break;
                case 5:
                    c.layout.dimensionRatio = a.getString(index);
                    break;
                case 6:
                    Layout layout5 = c.layout;
                    layout5.editorAbsoluteX = a.getDimensionPixelOffset(index, layout5.editorAbsoluteX);
                    break;
                case 7:
                    Layout layout6 = c.layout;
                    layout6.editorAbsoluteY = a.getDimensionPixelOffset(index, layout6.editorAbsoluteY);
                    break;
                case 8:
                    if (Build.VERSION.SDK_INT >= 17) {
                        Layout layout7 = c.layout;
                        layout7.endMargin = a.getDimensionPixelSize(index, layout7.endMargin);
                        break;
                    } else {
                        break;
                    }
                case 9:
                    Layout layout8 = c.layout;
                    layout8.endToEnd = lookupID(a, index, layout8.endToEnd);
                    break;
                case 10:
                    Layout layout9 = c.layout;
                    layout9.endToStart = lookupID(a, index, layout9.endToStart);
                    break;
                case 11:
                    Layout layout10 = c.layout;
                    layout10.goneBottomMargin = a.getDimensionPixelSize(index, layout10.goneBottomMargin);
                    break;
                case 12:
                    Layout layout11 = c.layout;
                    layout11.goneEndMargin = a.getDimensionPixelSize(index, layout11.goneEndMargin);
                    break;
                case 13:
                    Layout layout12 = c.layout;
                    layout12.goneLeftMargin = a.getDimensionPixelSize(index, layout12.goneLeftMargin);
                    break;
                case 14:
                    Layout layout13 = c.layout;
                    layout13.goneRightMargin = a.getDimensionPixelSize(index, layout13.goneRightMargin);
                    break;
                case 15:
                    Layout layout14 = c.layout;
                    layout14.goneStartMargin = a.getDimensionPixelSize(index, layout14.goneStartMargin);
                    break;
                case 16:
                    Layout layout15 = c.layout;
                    layout15.goneTopMargin = a.getDimensionPixelSize(index, layout15.goneTopMargin);
                    break;
                case 17:
                    Layout layout16 = c.layout;
                    layout16.guideBegin = a.getDimensionPixelOffset(index, layout16.guideBegin);
                    break;
                case 18:
                    Layout layout17 = c.layout;
                    layout17.guideEnd = a.getDimensionPixelOffset(index, layout17.guideEnd);
                    break;
                case 19:
                    Layout layout18 = c.layout;
                    layout18.guidePercent = a.getFloat(index, layout18.guidePercent);
                    break;
                case 20:
                    Layout layout19 = c.layout;
                    layout19.horizontalBias = a.getFloat(index, layout19.horizontalBias);
                    break;
                case 21:
                    Layout layout20 = c.layout;
                    layout20.mHeight = a.getLayoutDimension(index, layout20.mHeight);
                    break;
                case 22:
                    PropertySet propertySet = c.propertySet;
                    propertySet.visibility = a.getInt(index, propertySet.visibility);
                    PropertySet propertySet2 = c.propertySet;
                    propertySet2.visibility = VISIBILITY_FLAGS[propertySet2.visibility];
                    break;
                case 23:
                    Layout layout21 = c.layout;
                    layout21.mWidth = a.getLayoutDimension(index, layout21.mWidth);
                    break;
                case 24:
                    Layout layout22 = c.layout;
                    layout22.leftMargin = a.getDimensionPixelSize(index, layout22.leftMargin);
                    break;
                case 25:
                    Layout layout23 = c.layout;
                    layout23.leftToLeft = lookupID(a, index, layout23.leftToLeft);
                    break;
                case 26:
                    Layout layout24 = c.layout;
                    layout24.leftToRight = lookupID(a, index, layout24.leftToRight);
                    break;
                case 27:
                    Layout layout25 = c.layout;
                    layout25.orientation = a.getInt(index, layout25.orientation);
                    break;
                case 28:
                    Layout layout26 = c.layout;
                    layout26.rightMargin = a.getDimensionPixelSize(index, layout26.rightMargin);
                    break;
                case 29:
                    Layout layout27 = c.layout;
                    layout27.rightToLeft = lookupID(a, index, layout27.rightToLeft);
                    break;
                case 30:
                    Layout layout28 = c.layout;
                    layout28.rightToRight = lookupID(a, index, layout28.rightToRight);
                    break;
                case 31:
                    if (Build.VERSION.SDK_INT >= 17) {
                        Layout layout29 = c.layout;
                        layout29.startMargin = a.getDimensionPixelSize(index, layout29.startMargin);
                        break;
                    } else {
                        break;
                    }
                case 32:
                    Layout layout30 = c.layout;
                    layout30.startToEnd = lookupID(a, index, layout30.startToEnd);
                    break;
                case 33:
                    Layout layout31 = c.layout;
                    layout31.startToStart = lookupID(a, index, layout31.startToStart);
                    break;
                case 34:
                    Layout layout32 = c.layout;
                    layout32.topMargin = a.getDimensionPixelSize(index, layout32.topMargin);
                    break;
                case 35:
                    Layout layout33 = c.layout;
                    layout33.topToBottom = lookupID(a, index, layout33.topToBottom);
                    break;
                case 36:
                    Layout layout34 = c.layout;
                    layout34.topToTop = lookupID(a, index, layout34.topToTop);
                    break;
                case 37:
                    Layout layout35 = c.layout;
                    layout35.verticalBias = a.getFloat(index, layout35.verticalBias);
                    break;
                case 38:
                    c.mViewId = a.getResourceId(index, c.mViewId);
                    break;
                case 39:
                    Layout layout36 = c.layout;
                    layout36.horizontalWeight = a.getFloat(index, layout36.horizontalWeight);
                    break;
                case 40:
                    Layout layout37 = c.layout;
                    layout37.verticalWeight = a.getFloat(index, layout37.verticalWeight);
                    break;
                case 41:
                    Layout layout38 = c.layout;
                    layout38.horizontalChainStyle = a.getInt(index, layout38.horizontalChainStyle);
                    break;
                case 42:
                    Layout layout39 = c.layout;
                    layout39.verticalChainStyle = a.getInt(index, layout39.verticalChainStyle);
                    break;
                case 43:
                    PropertySet propertySet3 = c.propertySet;
                    propertySet3.alpha = a.getFloat(index, propertySet3.alpha);
                    break;
                case 44:
                    if (Build.VERSION.SDK_INT >= 21) {
                        Transform transform = c.transform;
                        transform.applyElevation = true;
                        transform.elevation = a.getDimension(index, transform.elevation);
                        break;
                    } else {
                        break;
                    }
                case 45:
                    Transform transform2 = c.transform;
                    transform2.rotationX = a.getFloat(index, transform2.rotationX);
                    break;
                case 46:
                    Transform transform3 = c.transform;
                    transform3.rotationY = a.getFloat(index, transform3.rotationY);
                    break;
                case 47:
                    Transform transform4 = c.transform;
                    transform4.scaleX = a.getFloat(index, transform4.scaleX);
                    break;
                case 48:
                    Transform transform5 = c.transform;
                    transform5.scaleY = a.getFloat(index, transform5.scaleY);
                    break;
                case 49:
                    Transform transform6 = c.transform;
                    transform6.transformPivotX = a.getDimension(index, transform6.transformPivotX);
                    break;
                case 50:
                    Transform transform7 = c.transform;
                    transform7.transformPivotY = a.getDimension(index, transform7.transformPivotY);
                    break;
                case 51:
                    Transform transform8 = c.transform;
                    transform8.translationX = a.getDimension(index, transform8.translationX);
                    break;
                case 52:
                    Transform transform9 = c.transform;
                    transform9.translationY = a.getDimension(index, transform9.translationY);
                    break;
                case 53:
                    if (Build.VERSION.SDK_INT >= 21) {
                        Transform transform10 = c.transform;
                        transform10.translationZ = a.getDimension(index, transform10.translationZ);
                        break;
                    } else {
                        break;
                    }
                case 54:
                    Layout layout40 = c.layout;
                    layout40.widthDefault = a.getInt(index, layout40.widthDefault);
                    break;
                case 55:
                    Layout layout41 = c.layout;
                    layout41.heightDefault = a.getInt(index, layout41.heightDefault);
                    break;
                case 56:
                    Layout layout42 = c.layout;
                    layout42.widthMax = a.getDimensionPixelSize(index, layout42.widthMax);
                    break;
                case 57:
                    Layout layout43 = c.layout;
                    layout43.heightMax = a.getDimensionPixelSize(index, layout43.heightMax);
                    break;
                case 58:
                    Layout layout44 = c.layout;
                    layout44.widthMin = a.getDimensionPixelSize(index, layout44.widthMin);
                    break;
                case 59:
                    Layout layout45 = c.layout;
                    layout45.heightMin = a.getDimensionPixelSize(index, layout45.heightMin);
                    break;
                case 60:
                    Transform transform11 = c.transform;
                    transform11.rotation = a.getFloat(index, transform11.rotation);
                    break;
                case 61:
                    Layout layout46 = c.layout;
                    layout46.circleConstraint = lookupID(a, index, layout46.circleConstraint);
                    break;
                case 62:
                    Layout layout47 = c.layout;
                    layout47.circleRadius = a.getDimensionPixelSize(index, layout47.circleRadius);
                    break;
                case 63:
                    Layout layout48 = c.layout;
                    layout48.circleAngle = a.getFloat(index, layout48.circleAngle);
                    break;
                case 64:
                    Motion motion = c.motion;
                    motion.mAnimateRelativeTo = lookupID(a, index, motion.mAnimateRelativeTo);
                    break;
                case 65:
                    if (a.peekValue(index).type == 3) {
                        c.motion.mTransitionEasing = a.getString(index);
                        break;
                    } else {
                        c.motion.mTransitionEasing = Easing.NAMED_EASING[a.getInteger(index, 0)];
                        break;
                    }
                case 66:
                    c.motion.mDrawPath = a.getInt(index, 0);
                    break;
                case 67:
                    Motion motion2 = c.motion;
                    motion2.mPathRotate = a.getFloat(index, motion2.mPathRotate);
                    break;
                case 68:
                    PropertySet propertySet4 = c.propertySet;
                    propertySet4.mProgress = a.getFloat(index, propertySet4.mProgress);
                    break;
                case 69:
                    c.layout.widthPercent = a.getFloat(index, 1.0f);
                    break;
                case 70:
                    c.layout.heightPercent = a.getFloat(index, 1.0f);
                    break;
                case 71:
                    Log.e("ConstraintSet", "CURRENTLY UNSUPPORTED");
                    break;
                case 72:
                    Layout layout49 = c.layout;
                    layout49.mBarrierDirection = a.getInt(index, layout49.mBarrierDirection);
                    break;
                case 73:
                    Layout layout50 = c.layout;
                    layout50.mBarrierMargin = a.getDimensionPixelSize(index, layout50.mBarrierMargin);
                    break;
                case 74:
                    c.layout.mReferenceIdString = a.getString(index);
                    break;
                case 75:
                    Layout layout51 = c.layout;
                    layout51.mBarrierAllowsGoneWidgets = a.getBoolean(index, layout51.mBarrierAllowsGoneWidgets);
                    break;
                case 76:
                    Motion motion3 = c.motion;
                    motion3.mPathMotionArc = a.getInt(index, motion3.mPathMotionArc);
                    break;
                case 77:
                    c.layout.mConstraintTag = a.getString(index);
                    break;
                case 78:
                    PropertySet propertySet5 = c.propertySet;
                    propertySet5.mVisibilityMode = a.getInt(index, propertySet5.mVisibilityMode);
                    break;
                case 79:
                    Motion motion4 = c.motion;
                    motion4.mMotionStagger = a.getFloat(index, motion4.mMotionStagger);
                    break;
                case 80:
                    Layout layout52 = c.layout;
                    layout52.constrainedWidth = a.getBoolean(index, layout52.constrainedWidth);
                    break;
                case 81:
                    Layout layout53 = c.layout;
                    layout53.constrainedHeight = a.getBoolean(index, layout53.constrainedHeight);
                    break;
                case 82:
                    Motion motion5 = c.motion;
                    motion5.mAnimateCircleAngleTo = a.getInteger(index, motion5.mAnimateCircleAngleTo);
                    break;
                case 83:
                    Transform transform12 = c.transform;
                    transform12.transformPivotTarget = lookupID(a, index, transform12.transformPivotTarget);
                    break;
                case 84:
                    Motion motion6 = c.motion;
                    motion6.mQuantizeMotionSteps = a.getInteger(index, motion6.mQuantizeMotionSteps);
                    break;
                case 85:
                    Motion motion7 = c.motion;
                    motion7.mQuantizeMotionPhase = a.getFloat(index, motion7.mQuantizeMotionPhase);
                    break;
                case 86:
                    int i2 = a.peekValue(index).type;
                    if (i2 == 1) {
                        c.motion.mQuantizeInterpolatorID = a.getResourceId(index, -1);
                        Motion motion8 = c.motion;
                        if (motion8.mQuantizeInterpolatorID != -1) {
                            motion8.mQuantizeInterpolatorType = -2;
                            break;
                        } else {
                            break;
                        }
                    } else if (i2 == 3) {
                        c.motion.mQuantizeInterpolatorString = a.getString(index);
                        if (c.motion.mQuantizeInterpolatorString.indexOf(h.g) > 0) {
                            c.motion.mQuantizeInterpolatorID = a.getResourceId(index, -1);
                            c.motion.mQuantizeInterpolatorType = -2;
                            break;
                        } else {
                            c.motion.mQuantizeInterpolatorType = -1;
                            break;
                        }
                    } else {
                        Motion motion9 = c.motion;
                        motion9.mQuantizeInterpolatorType = a.getInteger(index, motion9.mQuantizeInterpolatorID);
                        break;
                    }
                case 87:
                    String hexString = Integer.toHexString(index);
                    int i3 = mapToConstant.get(index);
                    StringBuilder sb = new StringBuilder(String.valueOf(hexString).length() + 33);
                    sb.append("unused attribute 0x");
                    sb.append(hexString);
                    sb.append("   ");
                    sb.append(i3);
                    Log.w("ConstraintSet", sb.toString());
                    break;
                case 88:
                case 89:
                case 90:
                default:
                    String hexString2 = Integer.toHexString(index);
                    int i4 = mapToConstant.get(index);
                    StringBuilder sb2 = new StringBuilder(String.valueOf(hexString2).length() + 34);
                    sb2.append("Unknown attribute 0x");
                    sb2.append(hexString2);
                    sb2.append("   ");
                    sb2.append(i4);
                    Log.w("ConstraintSet", sb2.toString());
                    break;
                case 91:
                    Layout layout54 = c.layout;
                    layout54.baselineToTop = lookupID(a, index, layout54.baselineToTop);
                    break;
                case 92:
                    Layout layout55 = c.layout;
                    layout55.baselineToBottom = lookupID(a, index, layout55.baselineToBottom);
                    break;
                case 93:
                    Layout layout56 = c.layout;
                    layout56.baselineMargin = a.getDimensionPixelSize(index, layout56.baselineMargin);
                    break;
                case 94:
                    Layout layout57 = c.layout;
                    layout57.goneBaselineMargin = a.getDimensionPixelSize(index, layout57.goneBaselineMargin);
                    break;
                case 95:
                    parseDimensionConstraints(c.layout, a, index, 0);
                    break;
                case 96:
                    parseDimensionConstraints(c.layout, a, index, 1);
                    break;
                case 97:
                    Layout layout58 = c.layout;
                    layout58.mWrapBehavior = a.getInt(index, layout58.mWrapBehavior);
                    break;
            }
        }
        Layout layout59 = c.layout;
        if (layout59.mReferenceIdString == null) {
            return;
        }
        layout59.mReferenceIds = null;
    }

    public final int[] convertReferenceString(View view, String referenceIdString) {
        int i;
        Object designInformation;
        String[] split = referenceIdString.split(",");
        Context context = view.getContext();
        int[] iArr = new int[split.length];
        int i2 = 0;
        int i3 = 0;
        while (i2 < split.length) {
            String trim = split[i2].trim();
            try {
                i = R$id.class.getField(trim).getInt(null);
            } catch (Exception unused) {
                i = 0;
            }
            if (i == 0) {
                i = context.getResources().getIdentifier(trim, "id", context.getPackageName());
            }
            if (i == 0 && view.isInEditMode() && (view.getParent() instanceof ConstraintLayout) && (designInformation = ((ConstraintLayout) view.getParent()).getDesignInformation(0, trim)) != null && (designInformation instanceof Integer)) {
                i = ((Integer) designInformation).intValue();
            }
            iArr[i3] = i;
            i2++;
            i3++;
        }
        return i3 != split.length ? Arrays.copyOf(iArr, i3) : iArr;
    }
}
