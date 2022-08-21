package miuix.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceGroup;

/* loaded from: classes3.dex */
public class RadioButtonPreferenceCategory extends PreferenceCategory {
    public SingleChoiceHelper mCheckedChoice;
    public int mCheckedPosition;
    public OnPreferenceChangeInternalListener mInternalListener;

    public final void checkPreferenceByInternal(Preference preference, Object obj) {
        PreferenceGroup parent = preference.getParent() instanceof RadioSetPreferenceCategory ? preference.getParent() : preference;
        SingleChoiceHelper singleChoiceHelper = this.mCheckedChoice;
        if ((singleChoiceHelper == null || parent != singleChoiceHelper.getPreference()) && callChangeListenerByInternal(obj, parent)) {
            setCheckedPreference(preference);
        }
    }

    public final boolean callChangeListenerByInternal(Object obj, Preference preference) {
        return preference.getOnPreferenceChangeListener() == null || preference.getOnPreferenceChangeListener().onPreferenceChange(preference, obj);
    }

    public RadioButtonPreferenceCategory(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mCheckedChoice = null;
        this.mCheckedPosition = -1;
        this.mInternalListener = new OnPreferenceChangeInternalListener() { // from class: miuix.preference.RadioButtonPreferenceCategory.1
            @Override // miuix.preference.OnPreferenceChangeInternalListener
            public boolean onPreferenceChangeInternal(Preference preference, Object obj) {
                Checkable checkable = (Checkable) preference;
                Preference.OnPreferenceClickListener onPreferenceClickListener = RadioButtonPreferenceCategory.this.getOnPreferenceClickListener();
                if (onPreferenceClickListener != null) {
                    RadioButtonPreferenceCategory.this.checkPreferenceByInternal(preference, obj);
                    onPreferenceClickListener.onPreferenceClick(RadioButtonPreferenceCategory.this);
                }
                return !checkable.isChecked();
            }

            @Override // miuix.preference.OnPreferenceChangeInternalListener
            public void notifyPreferenceChangeInternal(Preference preference) {
                SingleChoiceHelper parse = RadioButtonPreferenceCategory.this.parse(preference);
                RadioButtonPreferenceCategory.this.updateCheckedPreference(parse);
                RadioButtonPreferenceCategory.this.updateCheckedPosition(parse);
            }
        };
    }

    public RadioButtonPreferenceCategory(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.preferenceCategoryRadioStyle);
    }

    @Override // androidx.preference.PreferenceGroup
    public boolean addPreference(Preference preference) {
        SingleChoiceHelper parse = parse(preference);
        boolean addPreference = super.addPreference(preference);
        if (addPreference) {
            parse.setOnPreferenceChangeInternalListener(this.mInternalListener);
        }
        if (parse.isChecked()) {
            if (this.mCheckedChoice != null) {
                throw new IllegalStateException("Already has a checked item, please check state of new add preference");
            }
            this.mCheckedChoice = parse;
        }
        return addPreference;
    }

    @Override // androidx.preference.PreferenceGroup
    public void removeAll() {
        super.removeAll();
        this.mCheckedPosition = -1;
        this.mCheckedChoice = null;
    }

    @Override // androidx.preference.PreferenceGroup
    public boolean removePreference(Preference preference) {
        SingleChoiceHelper parse = parse(preference);
        boolean removePreference = super.removePreference(preference);
        if (removePreference) {
            parse.setOnPreferenceChangeInternalListener(null);
            if (parse.isChecked()) {
                parse.setChecked(false);
                this.mCheckedPosition = -1;
                this.mCheckedChoice = null;
            }
        }
        return removePreference;
    }

    public void setCheckedPreference(Preference preference) {
        if (preference == null) {
            clearChecked();
            return;
        }
        SingleChoiceHelper parse = parse(preference);
        if (parse.isChecked()) {
            return;
        }
        setCheckedPreferenceInternal(parse);
        updateCheckedPreference(parse);
        updateCheckedPosition(parse);
    }

    public final void clearChecked() {
        SingleChoiceHelper singleChoiceHelper = this.mCheckedChoice;
        if (singleChoiceHelper != null) {
            singleChoiceHelper.setChecked(false);
        }
        this.mCheckedChoice = null;
        this.mCheckedPosition = -1;
    }

    public final void setCheckedPreferenceInternal(SingleChoiceHelper singleChoiceHelper) {
        singleChoiceHelper.setChecked(true);
    }

    public final void updateCheckedPreference(SingleChoiceHelper singleChoiceHelper) {
        if (singleChoiceHelper.isChecked()) {
            SingleChoiceHelper singleChoiceHelper2 = this.mCheckedChoice;
            if (singleChoiceHelper2 != null && singleChoiceHelper2.getPreference() != singleChoiceHelper.getPreference()) {
                this.mCheckedChoice.setChecked(false);
            }
            this.mCheckedChoice = singleChoiceHelper;
        }
    }

    public final void updateCheckedPosition(SingleChoiceHelper singleChoiceHelper) {
        if (singleChoiceHelper.isChecked()) {
            int preferenceCount = getPreferenceCount();
            for (int i = 0; i < preferenceCount; i++) {
                if (getPreference(i) == singleChoiceHelper.getPreference()) {
                    this.mCheckedPosition = i;
                    return;
                }
            }
        }
    }

    public final SingleChoiceHelper parse(Preference preference) {
        if (preference instanceof RadioButtonPreference) {
            if (preference.getParent() instanceof RadioSetPreferenceCategory) {
                return new CategorySingleChoiceHelper((RadioSetPreferenceCategory) preference.getParent());
            }
            return new PreferenceSingleChoiceHelper((RadioButtonPreference) preference);
        } else if (preference instanceof RadioSetPreferenceCategory) {
            return new CategorySingleChoiceHelper((RadioSetPreferenceCategory) preference);
        } else {
            throw new IllegalArgumentException("Only RadioButtonPreference or RadioSetPreferenceCategory can be added to RadioButtonPreferenceCategory");
        }
    }

    /* loaded from: classes3.dex */
    public class PreferenceSingleChoiceHelper extends SingleChoiceHelper {
        public RadioButtonPreference mPreference;

        public PreferenceSingleChoiceHelper(RadioButtonPreference radioButtonPreference) {
            super(radioButtonPreference);
            this.mPreference = radioButtonPreference;
        }

        @Override // miuix.preference.RadioButtonPreferenceCategory.SingleChoiceHelper
        public void setOnPreferenceChangeInternalListener(OnPreferenceChangeInternalListener onPreferenceChangeInternalListener) {
            this.mPreference.setOnPreferenceChangeInternalListener(onPreferenceChangeInternalListener);
        }

        @Override // miuix.preference.RadioButtonPreferenceCategory.SingleChoiceHelper
        public Preference getPreference() {
            return this.mPreference;
        }
    }

    /* loaded from: classes3.dex */
    public class CategorySingleChoiceHelper extends SingleChoiceHelper {
        public RadioSetPreferenceCategory mCategory;

        public CategorySingleChoiceHelper(RadioSetPreferenceCategory radioSetPreferenceCategory) {
            super(radioSetPreferenceCategory);
            this.mCategory = radioSetPreferenceCategory;
        }

        @Override // miuix.preference.RadioButtonPreferenceCategory.SingleChoiceHelper
        public void setOnPreferenceChangeInternalListener(OnPreferenceChangeInternalListener onPreferenceChangeInternalListener) {
            this.mCategory.setOnPreferenceChangeInternalListener(onPreferenceChangeInternalListener);
        }

        @Override // miuix.preference.RadioButtonPreferenceCategory.SingleChoiceHelper
        public Preference getPreference() {
            return this.mCategory;
        }

        @Override // miuix.preference.RadioButtonPreferenceCategory.SingleChoiceHelper, android.widget.Checkable
        public void setChecked(boolean z) {
            super.setChecked(z);
            if (this.mCategory.getPrimaryPreference() != null) {
                this.mCategory.getPrimaryPreference().setChecked(z);
            }
        }
    }

    /* loaded from: classes3.dex */
    public abstract class SingleChoiceHelper implements Checkable {
        public Checkable mCheckable;

        public abstract Preference getPreference();

        public abstract void setOnPreferenceChangeInternalListener(OnPreferenceChangeInternalListener onPreferenceChangeInternalListener);

        public SingleChoiceHelper(Checkable checkable) {
            this.mCheckable = checkable;
        }

        @Override // android.widget.Checkable
        public void setChecked(boolean z) {
            this.mCheckable.setChecked(z);
        }

        @Override // android.widget.Checkable
        public boolean isChecked() {
            return this.mCheckable.isChecked();
        }

        @Override // android.widget.Checkable
        public void toggle() {
            setChecked(!isChecked());
        }
    }
}
