package miuix.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import miuix.animation.Folme;
import miuix.animation.ITouchStyle;
import miuix.animation.base.AnimConfig;
import miuix.appcompat.adapter.SpinnerDoubleLineContentAdapter;
import miuix.appcompat.internal.adapter.SpinnerCheckableArrayAdapter;
import miuix.appcompat.widget.Spinner;

/* loaded from: classes3.dex */
public class DropDownPreference extends Preference {
    public static final Class<?>[] ADAPTER_CONSTRUCTOR_SIGNATURE = {Context.class, AttributeSet.class};
    public static final CharSequence[] EMPTY = new CharSequence[0];
    public ArrayAdapter mAdapter;
    public ArrayAdapter mContentAdapter;
    public CharSequence[] mEntries;
    public Drawable[] mEntryIcons;
    public CharSequence[] mEntryValues;
    public final AdapterView.OnItemSelectedListener mItemSelectedListener;
    public Handler mNotifyHandler;
    public Spinner mSpinner;
    public String mValue;
    public boolean mValueSet;

    public DropDownPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.dropdownPreferenceStyle);
    }

    public DropDownPreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public DropDownPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mNotifyHandler = new Handler();
        this.mItemSelectedListener = new AdapterView.OnItemSelectedListener() { // from class: miuix.preference.DropDownPreference.1
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i3, long j) {
                if (i3 >= 0) {
                    final String str = (String) DropDownPreference.this.mEntryValues[i3];
                    DropDownPreference.this.mNotifyHandler.post(new Runnable() { // from class: miuix.preference.DropDownPreference.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (str.equals(DropDownPreference.this.getValue()) || !DropDownPreference.this.callChangeListener(str)) {
                                return;
                            }
                            DropDownPreference.this.setValue(str);
                        }
                    });
                }
            }
        };
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.DropDownPreference, i, i2);
        String string = obtainStyledAttributes.getString(R$styleable.DropDownPreference_adapter);
        obtainStyledAttributes.recycle();
        if (!TextUtils.isEmpty(string)) {
            this.mContentAdapter = initAdapter(context, attributeSet, string);
        } else {
            this.mContentAdapter = new DropDownLayoutAdapter(context, attributeSet, i, i2);
        }
        this.mAdapter = createAdapter();
        constructEntries();
    }

    public final void constructEntries() {
        ArrayAdapter arrayAdapter = this.mContentAdapter;
        if (arrayAdapter instanceof DropDownLayoutAdapter) {
            this.mEntries = ((DropDownLayoutAdapter) arrayAdapter).getEntries();
            this.mEntryValues = ((DropDownLayoutAdapter) this.mContentAdapter).getEntryValues();
            this.mEntryIcons = ((DropDownLayoutAdapter) this.mContentAdapter).getEntryIcons();
            return;
        }
        int count = arrayAdapter.getCount();
        this.mEntries = new CharSequence[this.mContentAdapter.getCount()];
        for (int i = 0; i < count; i++) {
            this.mEntries[i] = this.mContentAdapter.getItem(i).toString();
        }
        this.mEntryValues = this.mEntries;
        this.mEntryIcons = null;
    }

    public ArrayAdapter createAdapter() {
        Context context = getContext();
        ArrayAdapter arrayAdapter = this.mContentAdapter;
        return new SpinnerCheckableArrayAdapter(context, arrayAdapter, new PreferenceCheckedProvider(this, arrayAdapter));
    }

    public final ArrayAdapter initAdapter(Context context, AttributeSet attributeSet, String str) {
        try {
            Constructor constructor = context.getClassLoader().loadClass(str).asSubclass(ArrayAdapter.class).getConstructor(ADAPTER_CONSTRUCTOR_SIGNATURE);
            Object[] objArr = {context, attributeSet};
            constructor.setAccessible(true);
            return (ArrayAdapter) constructor.newInstance(objArr);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Can't find Adapter: " + str, e);
        } catch (IllegalAccessException e2) {
            throw new IllegalStateException("Can't access non-public constructor " + str, e2);
        } catch (InstantiationException e3) {
            e = e3;
            throw new IllegalStateException("Could not instantiate the Adapter: " + str, e);
        } catch (NoSuchMethodException e4) {
            throw new IllegalStateException("Error creating Adapter " + str, e4);
        } catch (InvocationTargetException e5) {
            e = e5;
            throw new IllegalStateException("Could not instantiate the Adapter: " + str, e);
        }
    }

    @Override // androidx.preference.Preference
    public Object onGetDefaultValue(TypedArray typedArray, int i) {
        return typedArray.getString(i);
    }

    public void setValue(String str) {
        boolean z = !TextUtils.equals(this.mValue, str);
        if (z || !this.mValueSet) {
            this.mValue = str;
            this.mValueSet = true;
            persistString(str);
            if (!z) {
                return;
            }
            notifyChanged();
        }
    }

    public String getValue() {
        return this.mValue;
    }

    @Override // androidx.preference.Preference
    public void onSetInitialValue(Object obj) {
        setValue(getPersistedString((String) obj));
    }

    @Override // androidx.preference.Preference
    public Parcelable onSaveInstanceState() {
        Parcelable onSaveInstanceState = super.onSaveInstanceState();
        if (isPersistent()) {
            return onSaveInstanceState;
        }
        SavedState savedState = new SavedState(onSaveInstanceState);
        savedState.mValue = getValue();
        return savedState;
    }

    @Override // androidx.preference.Preference
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable == null || !parcelable.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        setValue(savedState.mValue);
    }

    @Override // androidx.preference.Preference
    public void notifyChanged() {
        super.notifyChanged();
        if (this.mAdapter != null) {
            this.mNotifyHandler.post(new Runnable() { // from class: miuix.preference.DropDownPreference.2
                @Override // java.lang.Runnable
                public void run() {
                    DropDownPreference.this.mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override // androidx.preference.Preference
    public void performClick(View view) {
        Spinner spinner = this.mSpinner;
        if (spinner != null) {
            spinner.performClick();
            Log.d("DropDownPreference", "trigger from perform click");
        }
    }

    public final void disableSpinnerClick(Spinner spinner) {
        spinner.setClickable(false);
        spinner.setLongClickable(false);
        if (Build.VERSION.SDK_INT >= 23) {
            spinner.setContextClickable(false);
        }
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(final PreferenceViewHolder preferenceViewHolder) {
        if (this.mAdapter.getCount() > 0) {
            Spinner spinner = (Spinner) preferenceViewHolder.itemView.findViewById(R$id.spinner);
            this.mSpinner = spinner;
            spinner.setImportantForAccessibility(2);
            disableSpinnerClick(this.mSpinner);
            this.mSpinner.setAdapter((SpinnerAdapter) this.mAdapter);
            this.mSpinner.setOnItemSelectedListener(null);
            this.mSpinner.setSelection(findSpinnerIndexOfValue(getValue()));
            this.mSpinner.post(new Runnable() { // from class: miuix.preference.DropDownPreference.3
                @Override // java.lang.Runnable
                public void run() {
                    DropDownPreference.this.mSpinner.setOnItemSelectedListener(DropDownPreference.this.mItemSelectedListener);
                }
            });
            this.mSpinner.setOnSpinnerDismissListener(new Spinner.OnSpinnerDismissListener() { // from class: miuix.preference.DropDownPreference.4
                @Override // miuix.appcompat.widget.Spinner.OnSpinnerDismissListener
                public void onSpinnerDismiss() {
                    Folme.useAt(preferenceViewHolder.itemView).touch().touchUp(new AnimConfig[0]);
                }
            });
            preferenceViewHolder.itemView.setOnTouchListener(new View.OnTouchListener() { // from class: miuix.preference.DropDownPreference.5
                @Override // android.view.View.OnTouchListener
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    int action = motionEvent.getAction();
                    if (action == 0) {
                        Folme.useAt(view).touch().setScale(1.0f, new ITouchStyle.TouchType[0]).touchDown(new AnimConfig[0]);
                    } else if (action == 1) {
                        float rawX = motionEvent.getRawX();
                        float rawY = motionEvent.getRawY();
                        DropDownPreference.this.mSpinner.setFenceXFromView(view);
                        DropDownPreference.this.mSpinner.performClick(rawX, rawY);
                    } else if (action == 3) {
                        Folme.useAt(view).touch().touchUp(new AnimConfig[0]);
                    }
                    return true;
                }
            });
        }
        super.onBindViewHolder(preferenceViewHolder);
    }

    public final int findSpinnerIndexOfValue(String str) {
        int i = 0;
        while (true) {
            CharSequence[] charSequenceArr = this.mEntryValues;
            if (i < charSequenceArr.length) {
                if (TextUtils.equals(charSequenceArr[i], str)) {
                    return i;
                }
                i++;
            } else {
                return -1;
            }
        }
    }

    /* loaded from: classes3.dex */
    public static class SavedState extends Preference.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: miuix.preference.DropDownPreference.SavedState.1
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        public String mValue;

        public SavedState(Parcel parcel) {
            super(parcel);
            this.mValue = parcel.readString();
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override // android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeString(this.mValue);
        }
    }

    /* loaded from: classes3.dex */
    public static class PreferenceCheckedProvider implements SpinnerCheckableArrayAdapter.CheckedStateProvider {
        public ArrayAdapter mAdapter;
        public DropDownPreference mPreference;

        public PreferenceCheckedProvider(DropDownPreference dropDownPreference, ArrayAdapter arrayAdapter) {
            this.mPreference = dropDownPreference;
            this.mAdapter = arrayAdapter;
        }

        @Override // miuix.appcompat.internal.adapter.SpinnerCheckableArrayAdapter.CheckedStateProvider
        public boolean isChecked(int i) {
            return TextUtils.equals(this.mPreference.getValue(), this.mPreference.mEntryValues[i]);
        }
    }

    /* loaded from: classes3.dex */
    public static class DropDownLayoutAdapter extends SpinnerDoubleLineContentAdapter {
        public CharSequence[] mValues;

        public DropDownLayoutAdapter(Context context, AttributeSet attributeSet, int i, int i2) {
            super(context, 0);
            int[] iArr;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.DropDownPreference, i, i2);
            this.mEntries = TypedArrayUtils.getTextArray(obtainStyledAttributes, R$styleable.DropDownPreference_entries, 0);
            this.mValues = TypedArrayUtils.getTextArray(obtainStyledAttributes, R$styleable.DropDownPreference_entryValues, 0);
            this.mSummaries = TypedArrayUtils.getTextArray(obtainStyledAttributes, R$styleable.DropDownPreference_entrySummaries, 0);
            int resourceId = obtainStyledAttributes.getResourceId(R$styleable.DropDownPreference_entryIcons, -1);
            obtainStyledAttributes.recycle();
            if (resourceId > 0) {
                TypedArray obtainTypedArray = context.getResources().obtainTypedArray(resourceId);
                iArr = new int[obtainTypedArray.length()];
                for (int i3 = 0; i3 < obtainTypedArray.length(); i3++) {
                    iArr[i3] = obtainTypedArray.getResourceId(i3, 0);
                }
                obtainTypedArray.recycle();
            } else {
                iArr = null;
            }
            setEntryIcons(iArr);
        }

        public CharSequence[] getEntryValues() {
            return this.mValues;
        }
    }
}
