package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.Log;
import android.util.SparseArray;
import android.util.Xml;
import com.xiaomi.stat.b.h;
import java.io.IOException;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: classes.dex */
public class ConstraintLayoutStates {
    public final ConstraintLayout mConstraintLayout;
    public ConstraintSet mDefaultConstraintSet;
    public int mCurrentStateId = -1;
    public int mCurrentConstraintNumber = -1;
    public SparseArray<State> mStateList = new SparseArray<>();
    public SparseArray<ConstraintSet> mConstraintSetMap = new SparseArray<>();

    public void setOnConstraintsChanged(ConstraintsChangedListener constraintsChangedListener) {
    }

    public ConstraintLayoutStates(Context context, ConstraintLayout layout, int resourceID) {
        this.mConstraintLayout = layout;
        load(context, resourceID);
    }

    public void updateConstraints(int id, float width, float height) {
        ConstraintSet constraintSet;
        State state;
        int findMatch;
        ConstraintSet constraintSet2;
        int i = this.mCurrentStateId;
        if (i == id) {
            if (id == -1) {
                state = this.mStateList.valueAt(0);
            } else {
                state = this.mStateList.get(i);
            }
            int i2 = this.mCurrentConstraintNumber;
            if ((i2 != -1 && state.mVariants.get(i2).match(width, height)) || this.mCurrentConstraintNumber == (findMatch = state.findMatch(width, height))) {
                return;
            }
            if (findMatch == -1) {
                constraintSet2 = this.mDefaultConstraintSet;
            } else {
                constraintSet2 = state.mVariants.get(findMatch).mConstraintSet;
            }
            if (findMatch != -1) {
                int i3 = state.mVariants.get(findMatch).mConstraintID;
            }
            if (constraintSet2 == null) {
                return;
            }
            this.mCurrentConstraintNumber = findMatch;
            constraintSet2.applyTo(this.mConstraintLayout);
            return;
        }
        this.mCurrentStateId = id;
        State state2 = this.mStateList.get(id);
        int findMatch2 = state2.findMatch(width, height);
        if (findMatch2 == -1) {
            constraintSet = state2.mConstraintSet;
        } else {
            constraintSet = state2.mVariants.get(findMatch2).mConstraintSet;
        }
        if (findMatch2 != -1) {
            int i4 = state2.mVariants.get(findMatch2).mConstraintID;
        }
        if (constraintSet == null) {
            StringBuilder sb = new StringBuilder(79);
            sb.append("NO Constraint set found ! id=");
            sb.append(id);
            sb.append(", dim =");
            sb.append(width);
            sb.append(", ");
            sb.append(height);
            Log.v("ConstraintLayoutStates", sb.toString());
            return;
        }
        this.mCurrentConstraintNumber = findMatch2;
        constraintSet.applyTo(this.mConstraintLayout);
    }

    /* loaded from: classes.dex */
    public static class State {
        public int mConstraintID;
        public ConstraintSet mConstraintSet;
        public int mId;
        public ArrayList<Variant> mVariants = new ArrayList<>();

        public State(Context context, XmlPullParser parser) {
            this.mConstraintID = -1;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(Xml.asAttributeSet(parser), R$styleable.State);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int index = obtainStyledAttributes.getIndex(i);
                if (index == R$styleable.State_android_id) {
                    this.mId = obtainStyledAttributes.getResourceId(index, this.mId);
                } else if (index == R$styleable.State_constraints) {
                    this.mConstraintID = obtainStyledAttributes.getResourceId(index, this.mConstraintID);
                    String resourceTypeName = context.getResources().getResourceTypeName(this.mConstraintID);
                    context.getResources().getResourceName(this.mConstraintID);
                    if ("layout".equals(resourceTypeName)) {
                        ConstraintSet constraintSet = new ConstraintSet();
                        this.mConstraintSet = constraintSet;
                        constraintSet.clone(context, this.mConstraintID);
                    }
                }
            }
            obtainStyledAttributes.recycle();
        }

        public void add(Variant size) {
            this.mVariants.add(size);
        }

        public int findMatch(float width, float height) {
            for (int i = 0; i < this.mVariants.size(); i++) {
                if (this.mVariants.get(i).match(width, height)) {
                    return i;
                }
            }
            return -1;
        }
    }

    /* loaded from: classes.dex */
    public static class Variant {
        public int mConstraintID;
        public ConstraintSet mConstraintSet;
        public float mMaxHeight;
        public float mMaxWidth;
        public float mMinHeight;
        public float mMinWidth;

        public Variant(Context context, XmlPullParser parser) {
            this.mMinWidth = Float.NaN;
            this.mMinHeight = Float.NaN;
            this.mMaxWidth = Float.NaN;
            this.mMaxHeight = Float.NaN;
            this.mConstraintID = -1;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(Xml.asAttributeSet(parser), R$styleable.Variant);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int index = obtainStyledAttributes.getIndex(i);
                if (index == R$styleable.Variant_constraints) {
                    this.mConstraintID = obtainStyledAttributes.getResourceId(index, this.mConstraintID);
                    String resourceTypeName = context.getResources().getResourceTypeName(this.mConstraintID);
                    context.getResources().getResourceName(this.mConstraintID);
                    if ("layout".equals(resourceTypeName)) {
                        ConstraintSet constraintSet = new ConstraintSet();
                        this.mConstraintSet = constraintSet;
                        constraintSet.clone(context, this.mConstraintID);
                    }
                } else if (index == R$styleable.Variant_region_heightLessThan) {
                    this.mMaxHeight = obtainStyledAttributes.getDimension(index, this.mMaxHeight);
                } else if (index == R$styleable.Variant_region_heightMoreThan) {
                    this.mMinHeight = obtainStyledAttributes.getDimension(index, this.mMinHeight);
                } else if (index == R$styleable.Variant_region_widthLessThan) {
                    this.mMaxWidth = obtainStyledAttributes.getDimension(index, this.mMaxWidth);
                } else if (index == R$styleable.Variant_region_widthMoreThan) {
                    this.mMinWidth = obtainStyledAttributes.getDimension(index, this.mMinWidth);
                } else {
                    Log.v("ConstraintLayoutStates", "Unknown tag");
                }
            }
            obtainStyledAttributes.recycle();
        }

        public boolean match(float widthDp, float heightDp) {
            if (Float.isNaN(this.mMinWidth) || widthDp >= this.mMinWidth) {
                if (!Float.isNaN(this.mMinHeight) && heightDp < this.mMinHeight) {
                    return false;
                }
                if (!Float.isNaN(this.mMaxWidth) && widthDp > this.mMaxWidth) {
                    return false;
                }
                return Float.isNaN(this.mMaxHeight) || heightDp <= this.mMaxHeight;
            }
            return false;
        }
    }

    public final void load(Context context, int resourceId) {
        XmlResourceParser xml = context.getResources().getXml(resourceId);
        State state = null;
        try {
            int eventType = xml.getEventType();
            while (true) {
                boolean z = true;
                if (eventType == 1) {
                    return;
                }
                if (eventType == 0) {
                    xml.getName();
                    continue;
                } else if (eventType != 2) {
                    continue;
                } else {
                    String name = xml.getName();
                    switch (name.hashCode()) {
                        case -1349929691:
                            if (name.equals("ConstraintSet")) {
                                z = true;
                                break;
                            }
                            z = true;
                            break;
                        case 80204913:
                            if (name.equals("State")) {
                                z = true;
                                break;
                            }
                            z = true;
                            break;
                        case 1382829617:
                            if (name.equals("StateSet")) {
                                break;
                            }
                            z = true;
                            break;
                        case 1657696882:
                            if (name.equals("layoutDescription")) {
                                z = false;
                                break;
                            }
                            z = true;
                            break;
                        case 1901439077:
                            if (name.equals("Variant")) {
                                z = true;
                                break;
                            }
                            z = true;
                            break;
                        default:
                            z = true;
                            break;
                    }
                    if (z) {
                        state = new State(context, xml);
                        this.mStateList.put(state.mId, state);
                        continue;
                    } else if (z) {
                        Variant variant = new Variant(context, xml);
                        if (state != null) {
                            state.add(variant);
                            continue;
                        } else {
                            continue;
                        }
                    } else if (!z) {
                        continue;
                    } else {
                        parseConstraintSet(context, xml);
                        continue;
                    }
                }
                eventType = xml.next();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
    }

    public final void parseConstraintSet(Context context, XmlPullParser parser) {
        ConstraintSet constraintSet = new ConstraintSet();
        int attributeCount = parser.getAttributeCount();
        for (int i = 0; i < attributeCount; i++) {
            String attributeName = parser.getAttributeName(i);
            String attributeValue = parser.getAttributeValue(i);
            if (attributeName != null && attributeValue != null && "id".equals(attributeName)) {
                int identifier = attributeValue.contains(h.g) ? context.getResources().getIdentifier(attributeValue.substring(attributeValue.indexOf(47) + 1), "id", context.getPackageName()) : -1;
                if (identifier == -1) {
                    if (attributeValue.length() > 1) {
                        identifier = Integer.parseInt(attributeValue.substring(1));
                    } else {
                        Log.e("ConstraintLayoutStates", "error in parsing id");
                    }
                }
                constraintSet.load(context, parser);
                this.mConstraintSetMap.put(identifier, constraintSet);
                return;
            }
        }
    }
}
