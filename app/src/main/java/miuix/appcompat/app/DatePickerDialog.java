package miuix.appcompat.app;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import miuix.appcompat.R$id;
import miuix.appcompat.R$layout;
import miuix.pickerwidget.date.Calendar;
import miuix.pickerwidget.date.DateUtils;
import miuix.pickerwidget.widget.DatePicker;
import miuix.slidingwidget.widget.SlidingButton;

/* loaded from: classes3.dex */
public class DatePickerDialog extends AlertDialog {
    public final Calendar mCalendar;
    public final OnDateSetListener mCallBack;
    public final DatePicker mDatePicker;
    public View mLunarModePanel;
    public SlidingButton mLunarModeState;
    public DatePicker.OnDateChangedListener mOnDateChangedListener;
    public boolean mTitleNeedsUpdate;

    /* loaded from: classes3.dex */
    public interface OnDateSetListener {
        void onDateSet(DatePicker datePicker, int i, int i2, int i3);
    }

    /* renamed from: $r8$lambda$s6oVXBmi7stJdGkFIda9g-qHwLg */
    public static /* synthetic */ void m2591$r8$lambda$s6oVXBmi7stJdGkFIda9gqHwLg(DatePickerDialog datePickerDialog, CompoundButton compoundButton, boolean z) {
        datePickerDialog.lambda$new$0(compoundButton, z);
    }

    public DatePickerDialog(Context context, OnDateSetListener onDateSetListener, int i, int i2, int i3) {
        this(context, 0, onDateSetListener, i, i2, i3);
    }

    public DatePickerDialog(Context context, int i, OnDateSetListener onDateSetListener, int i2, int i3, int i4) {
        super(context, i);
        this.mTitleNeedsUpdate = true;
        this.mOnDateChangedListener = new DatePicker.OnDateChangedListener() { // from class: miuix.appcompat.app.DatePickerDialog.1
            {
                DatePickerDialog.this = this;
            }

            @Override // miuix.pickerwidget.widget.DatePicker.OnDateChangedListener
            public void onDateChanged(DatePicker datePicker, int i5, int i6, int i7, boolean z) {
                if (DatePickerDialog.this.mTitleNeedsUpdate) {
                    DatePickerDialog.this.updateTitle(i5, i6, i7);
                }
            }
        };
        this.mCallBack = onDateSetListener;
        this.mCalendar = new Calendar();
        Context context2 = getContext();
        setButton(-1, context2.getText(17039370), new DialogInterface.OnClickListener() { // from class: miuix.appcompat.app.DatePickerDialog.2
            {
                DatePickerDialog.this = this;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i5) {
                DatePickerDialog.this.tryNotifyDateSet();
            }
        });
        setButton(-2, getContext().getText(17039360), (DialogInterface.OnClickListener) null);
        setIcon(0);
        View inflate = ((LayoutInflater) context2.getSystemService("layout_inflater")).inflate(R$layout.miuix_appcompat_date_picker_dialog, (ViewGroup) null);
        setView(inflate);
        DatePicker datePicker = (DatePicker) inflate.findViewById(R$id.datePicker);
        this.mDatePicker = datePicker;
        datePicker.init(i2, i3, i4, this.mOnDateChangedListener);
        updateTitle(i2, i3, i4);
        this.mLunarModePanel = inflate.findViewById(R$id.lunarModePanel);
        SlidingButton slidingButton = (SlidingButton) inflate.findViewById(R$id.datePickerLunar);
        this.mLunarModeState = slidingButton;
        slidingButton.setOnPerformCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: miuix.appcompat.app.DatePickerDialog$$ExternalSyntheticLambda0
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                DatePickerDialog.m2591$r8$lambda$s6oVXBmi7stJdGkFIda9gqHwLg(DatePickerDialog.this, compoundButton, z);
            }
        });
    }

    public /* synthetic */ void lambda$new$0(CompoundButton compoundButton, boolean z) {
        this.mDatePicker.setLunarMode(z);
    }

    public void setLunarMode(boolean z) {
        this.mLunarModePanel.setVisibility(z ? 0 : 8);
    }

    public DatePicker getDatePicker() {
        return this.mDatePicker;
    }

    @Override // miuix.appcompat.app.AlertDialog, androidx.appcompat.app.AppCompatDialog, android.app.Dialog
    public void setTitle(CharSequence charSequence) {
        super.setTitle(charSequence);
        this.mTitleNeedsUpdate = false;
    }

    @Override // androidx.appcompat.app.AppCompatDialog, android.app.Dialog
    public void setTitle(int i) {
        super.setTitle(i);
        this.mTitleNeedsUpdate = false;
    }

    public final void tryNotifyDateSet() {
        if (this.mCallBack != null) {
            this.mDatePicker.clearFocus();
            OnDateSetListener onDateSetListener = this.mCallBack;
            DatePicker datePicker = this.mDatePicker;
            onDateSetListener.onDateSet(datePicker, datePicker.getYear(), this.mDatePicker.getMonth(), this.mDatePicker.getDayOfMonth());
        }
    }

    public final void updateTitle(int i, int i2, int i3) {
        this.mCalendar.set(1, i);
        this.mCalendar.set(5, i2);
        this.mCalendar.set(9, i3);
        super.setTitle(DateUtils.formatDateTime(getContext(), this.mCalendar.getTimeInMillis(), 14208));
    }

    @Override // android.app.Dialog
    public Bundle onSaveInstanceState() {
        Bundle onSaveInstanceState = super.onSaveInstanceState();
        onSaveInstanceState.putInt("miuix:year", this.mDatePicker.getYear());
        onSaveInstanceState.putInt("miuix:month", this.mDatePicker.getMonth());
        onSaveInstanceState.putInt("miuix:day", this.mDatePicker.getDayOfMonth());
        return onSaveInstanceState;
    }

    @Override // android.app.Dialog
    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        this.mDatePicker.init(bundle.getInt("miuix:year"), bundle.getInt("miuix:month"), bundle.getInt("miuix:day"), this.mOnDateChangedListener);
    }
}
