package miuix.pickerwidget.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import miuix.pickerwidget.R$attr;
import miuix.pickerwidget.R$id;
import miuix.pickerwidget.R$layout;
import miuix.pickerwidget.R$string;
import miuix.pickerwidget.R$style;
import miuix.pickerwidget.R$styleable;
import miuix.pickerwidget.date.Calendar;
import miuix.pickerwidget.date.CalendarFormatSymbols;
import miuix.pickerwidget.date.DateUtils;
import miuix.pickerwidget.widget.NumberPicker;

/* loaded from: classes3.dex */
public class DatePicker extends FrameLayout {
    public static final String LOG_TAG = DatePicker.class.getSimpleName();
    public static String[] sChineseDays;
    public static String sChineseLeapMonthMark;
    public static String[] sChineseLeapYearMonths;
    public static String[] sChineseMonths;
    public Calendar mCurrentDate;
    public Locale mCurrentLocale;
    public final DateFormat mDateFormat;
    public char[] mDateFormatOrder;
    public final NumberPicker mDaySpinner;
    public boolean mIsEnabled;
    public boolean mIsLunarMode;
    public Calendar mMaxDate;
    public Calendar mMinDate;
    public final NumberPicker mMonthSpinner;
    public int mNumberOfMonths;
    public OnDateChangedListener mOnDateChangedListener;
    public String[] mShortMonths;
    public final LinearLayout mSpinners;
    public Calendar mTempDate;
    public final NumberPicker mYearSpinner;

    /* loaded from: classes3.dex */
    public interface OnDateChangedListener {
        void onDateChanged(DatePicker datePicker, int i, int i2, int i3, boolean z);
    }

    public DatePicker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.datePickerStyle);
    }

    public DatePicker(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        String str;
        this.mDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        this.mIsEnabled = true;
        this.mIsLunarMode = false;
        initChineseDaysIfNeeded();
        this.mTempDate = new Calendar();
        this.mMinDate = new Calendar();
        this.mMaxDate = new Calendar();
        this.mCurrentDate = new Calendar();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.DatePicker, i, R$style.Widget_DatePicker);
        boolean z = obtainStyledAttributes.getBoolean(R$styleable.DatePicker_spinnersShown, true);
        int i2 = obtainStyledAttributes.getInt(R$styleable.DatePicker_startYear, 1900);
        int i3 = obtainStyledAttributes.getInt(R$styleable.DatePicker_endYear, 2100);
        String string = obtainStyledAttributes.getString(R$styleable.DatePicker_minDate);
        String string2 = obtainStyledAttributes.getString(R$styleable.DatePicker_maxDate);
        int i4 = R$layout.miuix_appcompat_date_picker;
        this.mIsLunarMode = obtainStyledAttributes.getBoolean(R$styleable.DatePicker_lunarCalendar, false);
        boolean z2 = obtainStyledAttributes.getBoolean(R$styleable.DatePicker_showYear, true);
        boolean z3 = obtainStyledAttributes.getBoolean(R$styleable.DatePicker_showMonth, true);
        boolean z4 = obtainStyledAttributes.getBoolean(R$styleable.DatePicker_showDay, true);
        obtainStyledAttributes.recycle();
        setCurrentLocale(Locale.getDefault());
        ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(i4, (ViewGroup) this, true);
        NumberPicker.OnValueChangeListener onValueChangeListener = new NumberPicker.OnValueChangeListener() { // from class: miuix.pickerwidget.widget.DatePicker.1
            @Override // miuix.pickerwidget.widget.NumberPicker.OnValueChangeListener
            public void onValueChange(NumberPicker numberPicker, int i5, int i6) {
                DatePicker.this.mTempDate.setTimeInMillis(DatePicker.this.mCurrentDate.getTimeInMillis());
                if (numberPicker == DatePicker.this.mDaySpinner) {
                    DatePicker.this.mTempDate.add(DatePicker.this.mIsLunarMode ? 10 : 9, i6 - i5);
                } else if (numberPicker == DatePicker.this.mMonthSpinner) {
                    DatePicker.this.mTempDate.add(DatePicker.this.mIsLunarMode ? 6 : 5, i6 - i5);
                } else if (numberPicker == DatePicker.this.mYearSpinner) {
                    DatePicker.this.mTempDate.set(DatePicker.this.mIsLunarMode ? 2 : 1, i6);
                } else {
                    throw new IllegalArgumentException();
                }
                DatePicker datePicker = DatePicker.this;
                datePicker.setDate(datePicker.mTempDate.get(1), DatePicker.this.mTempDate.get(5), DatePicker.this.mTempDate.get(9));
                if (numberPicker == DatePicker.this.mYearSpinner) {
                    DatePicker.this.resetMonthsDisplayedValues();
                }
                DatePicker.this.updateSpinners();
                DatePicker.this.notifyDateChanged();
            }
        };
        this.mSpinners = (LinearLayout) findViewById(R$id.pickers);
        NumberPicker numberPicker = (NumberPicker) findViewById(R$id.day);
        this.mDaySpinner = numberPicker;
        numberPicker.setOnLongPressUpdateInterval(100L);
        numberPicker.setOnValueChangedListener(onValueChangeListener);
        if (!z4) {
            numberPicker.setVisibility(8);
        }
        NumberPicker numberPicker2 = (NumberPicker) findViewById(R$id.month);
        this.mMonthSpinner = numberPicker2;
        numberPicker2.setMinValue(0);
        numberPicker2.setMaxValue(this.mNumberOfMonths - 1);
        numberPicker2.setDisplayedValues(this.mShortMonths);
        numberPicker2.setOnLongPressUpdateInterval(200L);
        numberPicker2.setOnValueChangedListener(onValueChangeListener);
        if (!z3) {
            numberPicker2.setVisibility(8);
        }
        NumberPicker numberPicker3 = (NumberPicker) findViewById(R$id.year);
        this.mYearSpinner = numberPicker3;
        numberPicker3.setOnLongPressUpdateInterval(100L);
        numberPicker3.setOnValueChangedListener(onValueChangeListener);
        if (!z2) {
            numberPicker3.setVisibility(8);
        }
        updateFormatters();
        if (!z) {
            setSpinnersShown(true);
        } else {
            setSpinnersShown(z);
        }
        this.mTempDate.setTimeInMillis(0L);
        if (!TextUtils.isEmpty(string)) {
            if (!parseDate(string, this.mTempDate)) {
                str = string2;
                this.mTempDate.set(i2, 0, 1, 0, 0, 0, 0);
            } else {
                str = string2;
            }
        } else {
            str = string2;
            this.mTempDate.set(i2, 0, 1, 0, 0, 0, 0);
        }
        setMinDate(this.mTempDate.getTimeInMillis());
        this.mTempDate.setTimeInMillis(0L);
        if (!TextUtils.isEmpty(str)) {
            if (!parseDate(str, this.mTempDate)) {
                this.mTempDate.set(i3, 11, 31, 0, 0, 0, 0);
            }
        } else {
            this.mTempDate.set(i3, 11, 31, 0, 0, 0, 0);
        }
        setMaxDate(this.mTempDate.getTimeInMillis());
        this.mCurrentDate.setTimeInMillis(System.currentTimeMillis());
        init(this.mCurrentDate.get(1), this.mCurrentDate.get(5), this.mCurrentDate.get(9), null);
        reorderSpinners();
        if (getImportantForAccessibility() == 0) {
            setImportantForAccessibility(1);
        }
    }

    public final void initChineseDaysIfNeeded() {
        String[] strArr;
        if (sChineseDays == null) {
            sChineseDays = CalendarFormatSymbols.getOrCreate(getContext()).getChineseDays();
        }
        if (sChineseMonths == null) {
            sChineseMonths = CalendarFormatSymbols.getOrCreate(getContext()).getChineseMonths();
            Resources resources = getContext().getResources();
            int i = 0;
            while (true) {
                strArr = sChineseMonths;
                if (i >= strArr.length) {
                    break;
                }
                StringBuilder sb = new StringBuilder();
                String[] strArr2 = sChineseMonths;
                sb.append(strArr2[i]);
                sb.append(resources.getString(R$string.chinese_month));
                strArr2[i] = sb.toString();
                i++;
            }
            sChineseLeapYearMonths = new String[strArr.length + 1];
        }
        if (sChineseLeapMonthMark == null) {
            sChineseLeapMonthMark = CalendarFormatSymbols.getOrCreate(getContext()).getChineseLeapMonths()[1];
        }
    }

    public final void updateFormatters() {
        NumberPicker numberPicker = this.mDaySpinner;
        if (numberPicker == null || this.mYearSpinner == null) {
            return;
        }
        numberPicker.setFormatter(NumberPicker.TWO_DIGIT_FORMATTER);
        this.mYearSpinner.setFormatter(new NumberPicker.NumberFormatter());
    }

    public long getMinDate() {
        return this.mMinDate.getTimeInMillis();
    }

    public void setMinDate(long j) {
        this.mTempDate.setTimeInMillis(j);
        if (this.mTempDate.get(1) != this.mMinDate.get(1) || this.mTempDate.get(12) == this.mMinDate.get(12)) {
            this.mMinDate.setTimeInMillis(j);
            if (this.mCurrentDate.before(this.mMinDate)) {
                this.mCurrentDate.setTimeInMillis(this.mMinDate.getTimeInMillis());
            }
            updateSpinners();
        }
    }

    public long getMaxDate() {
        return this.mMaxDate.getTimeInMillis();
    }

    public void setMaxDate(long j) {
        this.mTempDate.setTimeInMillis(j);
        if (this.mTempDate.get(1) != this.mMaxDate.get(1) || this.mTempDate.get(12) == this.mMaxDate.get(12)) {
            this.mMaxDate.setTimeInMillis(j);
            if (this.mCurrentDate.after(this.mMaxDate)) {
                this.mCurrentDate.setTimeInMillis(this.mMaxDate.getTimeInMillis());
            }
            updateSpinners();
        }
    }

    @Override // android.view.View
    public void setEnabled(boolean z) {
        if (this.mIsEnabled == z) {
            return;
        }
        super.setEnabled(z);
        this.mDaySpinner.setEnabled(z);
        this.mMonthSpinner.setEnabled(z);
        this.mYearSpinner.setEnabled(z);
        this.mIsEnabled = z;
    }

    @Override // android.view.View
    public boolean isEnabled() {
        return this.mIsEnabled;
    }

    @Override // android.view.View
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        onPopulateAccessibilityEvent(accessibilityEvent);
        return true;
    }

    @Override // android.view.View
    public void onPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onPopulateAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.getText().add(DateUtils.formatDateTime(getContext(), this.mCurrentDate.getTimeInMillis(), 896));
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName(DatePicker.class.getName());
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(DatePicker.class.getName());
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        setCurrentLocale(configuration.locale);
    }

    public boolean getSpinnersShown() {
        return this.mSpinners.isShown();
    }

    public void setSpinnersShown(boolean z) {
        this.mSpinners.setVisibility(z ? 0 : 8);
    }

    public void setLunarMode(boolean z) {
        if (z != this.mIsLunarMode) {
            this.mIsLunarMode = z;
            resetMonthsDisplayedValues();
            updateSpinners();
        }
    }

    private void setCurrentLocale(Locale locale) {
        if (locale.equals(this.mCurrentLocale)) {
            return;
        }
        this.mCurrentLocale = locale;
        this.mNumberOfMonths = this.mTempDate.getActualMaximum(5) + 1;
        resetMonthsDisplayedValues();
        updateFormatters();
    }

    public final void resetMonthsDisplayedValues() {
        int i = 0;
        if (this.mIsLunarMode) {
            int chineseLeapMonth = this.mCurrentDate.getChineseLeapMonth();
            if (chineseLeapMonth < 0) {
                this.mShortMonths = sChineseMonths;
                return;
            }
            String[] strArr = sChineseLeapYearMonths;
            this.mShortMonths = strArr;
            int i2 = chineseLeapMonth + 1;
            System.arraycopy(sChineseMonths, 0, strArr, 0, i2);
            String[] strArr2 = sChineseMonths;
            System.arraycopy(strArr2, chineseLeapMonth, this.mShortMonths, i2, strArr2.length - chineseLeapMonth);
            String[] strArr3 = this.mShortMonths;
            strArr3[i2] = sChineseLeapMonthMark + this.mShortMonths[i2];
        } else if ("en".equals(this.mCurrentLocale.getLanguage().toLowerCase())) {
            this.mShortMonths = CalendarFormatSymbols.getOrCreate(getContext()).getShortMonths();
        } else {
            this.mShortMonths = new String[12];
            while (true) {
                String[] strArr4 = this.mShortMonths;
                if (i >= strArr4.length) {
                    return;
                }
                int i3 = i + 1;
                strArr4[i] = NumberPicker.TWO_DIGIT_FORMATTER.format(i3);
                i = i3;
            }
        }
    }

    public final void reorderSpinners() {
        this.mSpinners.removeAllViews();
        char[] cArr = this.mDateFormatOrder;
        if (cArr == null) {
            cArr = android.text.format.DateFormat.getDateFormatOrder(getContext());
        }
        int length = cArr.length;
        for (int i = 0; i < length; i++) {
            char c = cArr[i];
            if (c == 'M') {
                this.mSpinners.addView(this.mMonthSpinner);
                setImeOptions(this.mMonthSpinner, length, i);
            } else if (c == 'd') {
                this.mSpinners.addView(this.mDaySpinner);
                setImeOptions(this.mDaySpinner, length, i);
            } else if (c == 'y') {
                this.mSpinners.addView(this.mYearSpinner);
                setImeOptions(this.mYearSpinner, length, i);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchRestoreInstanceState(SparseArray<Parcelable> sparseArray) {
        dispatchThawSelfOnly(sparseArray);
    }

    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        return new SavedState(super.onSaveInstanceState(), this.mCurrentDate.get(1), this.mCurrentDate.get(5), this.mCurrentDate.get(9), this.mIsLunarMode);
    }

    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        setDate(savedState.mYear, savedState.mMonth, savedState.mDay);
        this.mIsLunarMode = savedState.mIsLunar;
        updateSpinners();
    }

    public void init(int i, int i2, int i3, OnDateChangedListener onDateChangedListener) {
        setDate(i, i2, i3);
        updateSpinners();
        this.mOnDateChangedListener = onDateChangedListener;
    }

    public final boolean parseDate(String str, Calendar calendar) {
        try {
            calendar.setTimeInMillis(this.mDateFormat.parse(str).getTime());
            return true;
        } catch (ParseException unused) {
            String str2 = LOG_TAG;
            Log.w(str2, "Date: " + str + " not in format: MM/dd/yyyy");
            return false;
        }
    }

    public final void setDate(int i, int i2, int i3) {
        this.mCurrentDate.set(i, i2, i3, 0, 0, 0, 0);
        if (this.mCurrentDate.before(this.mMinDate)) {
            this.mCurrentDate.setTimeInMillis(this.mMinDate.getTimeInMillis());
        } else if (!this.mCurrentDate.after(this.mMaxDate)) {
        } else {
            this.mCurrentDate.setTimeInMillis(this.mMaxDate.getTimeInMillis());
        }
    }

    public final void updateSpinners() {
        int i;
        if (this.mIsLunarMode) {
            this.mDaySpinner.setLabel(null);
            this.mMonthSpinner.setLabel(null);
            this.mYearSpinner.setLabel(null);
        } else {
            this.mDaySpinner.setLabel(getContext().getString(R$string.date_picker_label_day));
            this.mMonthSpinner.setLabel(getContext().getString(R$string.date_picker_label_month));
            this.mYearSpinner.setLabel(getContext().getString(R$string.date_picker_label_year));
        }
        this.mDaySpinner.setDisplayedValues(null);
        this.mDaySpinner.setMinValue(1);
        this.mDaySpinner.setMaxValue(this.mIsLunarMode ? this.mCurrentDate.getActualMaximum(10) : this.mCurrentDate.getActualMaximum(9));
        this.mDaySpinner.setWrapSelectorWheel(true);
        this.mMonthSpinner.setDisplayedValues(null);
        boolean z = false;
        this.mMonthSpinner.setMinValue(0);
        NumberPicker numberPicker = this.mMonthSpinner;
        int i2 = 11;
        if (this.mIsLunarMode && this.mCurrentDate.getChineseLeapMonth() >= 0) {
            i2 = 12;
        }
        numberPicker.setMaxValue(i2);
        this.mMonthSpinner.setWrapSelectorWheel(true);
        int i3 = this.mIsLunarMode ? 2 : 1;
        if (this.mCurrentDate.get(i3) == this.mMinDate.get(i3)) {
            this.mMonthSpinner.setMinValue(this.mIsLunarMode ? this.mMinDate.get(6) : this.mMinDate.get(5));
            this.mMonthSpinner.setWrapSelectorWheel(false);
            int i4 = this.mIsLunarMode ? 6 : 5;
            if (this.mCurrentDate.get(i4) == this.mMinDate.get(i4)) {
                this.mDaySpinner.setMinValue(this.mIsLunarMode ? this.mMinDate.get(10) : this.mMinDate.get(9));
                this.mDaySpinner.setWrapSelectorWheel(false);
            }
        }
        if (this.mCurrentDate.get(i3) == this.mMaxDate.get(i3)) {
            this.mMonthSpinner.setMaxValue(this.mIsLunarMode ? this.mMinDate.get(6) : this.mMaxDate.get(5));
            this.mMonthSpinner.setWrapSelectorWheel(false);
            this.mMonthSpinner.setDisplayedValues(null);
            int i5 = this.mIsLunarMode ? 6 : 5;
            if (this.mCurrentDate.get(i5) == this.mMaxDate.get(i5)) {
                this.mDaySpinner.setMaxValue(this.mIsLunarMode ? this.mMaxDate.get(10) : this.mMaxDate.get(9));
                this.mDaySpinner.setWrapSelectorWheel(false);
            }
        }
        this.mMonthSpinner.setDisplayedValues((String[]) Arrays.copyOfRange(this.mShortMonths, this.mMonthSpinner.getMinValue(), this.mShortMonths.length));
        if (this.mIsLunarMode) {
            this.mDaySpinner.setDisplayedValues((String[]) Arrays.copyOfRange(sChineseDays, this.mDaySpinner.getMinValue() - 1, sChineseDays.length));
        }
        int i6 = isLunarMode() ? 2 : 1;
        this.mYearSpinner.setMinValue(this.mMinDate.get(i6));
        this.mYearSpinner.setMaxValue(this.mMaxDate.get(i6));
        this.mYearSpinner.setWrapSelectorWheel(false);
        int chineseLeapMonth = this.mCurrentDate.getChineseLeapMonth();
        if (chineseLeapMonth >= 0 && (this.mCurrentDate.isChineseLeapMonth() || this.mCurrentDate.get(6) > chineseLeapMonth)) {
            z = true;
        }
        this.mYearSpinner.setValue(this.mIsLunarMode ? this.mCurrentDate.get(2) : this.mCurrentDate.get(1));
        NumberPicker numberPicker2 = this.mMonthSpinner;
        if (this.mIsLunarMode) {
            Calendar calendar = this.mCurrentDate;
            i = z ? calendar.get(6) + 1 : calendar.get(6);
        } else {
            i = this.mCurrentDate.get(5);
        }
        numberPicker2.setValue(i);
        this.mDaySpinner.setValue(this.mIsLunarMode ? this.mCurrentDate.get(10) : this.mCurrentDate.get(9));
    }

    public int getYear() {
        return this.mCurrentDate.get(this.mIsLunarMode ? 2 : 1);
    }

    public int getMonth() {
        if (this.mIsLunarMode) {
            return this.mCurrentDate.isChineseLeapMonth() ? this.mCurrentDate.get(6) + 12 : this.mCurrentDate.get(6);
        }
        return this.mCurrentDate.get(5);
    }

    public int getDayOfMonth() {
        return this.mCurrentDate.get(this.mIsLunarMode ? 10 : 9);
    }

    public boolean isLunarMode() {
        return this.mIsLunarMode;
    }

    public final void notifyDateChanged() {
        sendAccessibilityEvent(4);
        OnDateChangedListener onDateChangedListener = this.mOnDateChangedListener;
        if (onDateChangedListener != null) {
            onDateChangedListener.onDateChanged(this, getYear(), getMonth(), getDayOfMonth(), this.mIsLunarMode);
        }
    }

    public final void setImeOptions(NumberPicker numberPicker, int i, int i2) {
        ((TextView) numberPicker.findViewById(R$id.number_picker_input)).setImeOptions(i2 < i + (-1) ? 5 : 6);
    }

    public void setDateFormatOrder(char[] cArr) {
        this.mDateFormatOrder = cArr;
        reorderSpinners();
    }

    /* loaded from: classes3.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: miuix.pickerwidget.widget.DatePicker.SavedState.1
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        public final int mDay;
        public final boolean mIsLunar;
        public final int mMonth;
        public final int mYear;

        public SavedState(Parcelable parcelable, int i, int i2, int i3, boolean z) {
            super(parcelable);
            this.mYear = i;
            this.mMonth = i2;
            this.mDay = i3;
            this.mIsLunar = z;
        }

        public SavedState(Parcel parcel) {
            super(parcel);
            this.mYear = parcel.readInt();
            this.mMonth = parcel.readInt();
            this.mDay = parcel.readInt();
            this.mIsLunar = parcel.readInt() != 1 ? false : true;
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.mYear);
            parcel.writeInt(this.mMonth);
            parcel.writeInt(this.mDay);
            parcel.writeInt(this.mIsLunar ? 1 : 0);
        }
    }
}
