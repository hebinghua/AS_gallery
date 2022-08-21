package com.miui.gallery.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.miui.gallery.R;
import java.lang.ref.WeakReference;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class TimerDialog implements DialogInterface {
    public long mCheckTime;
    public long mConfirmTime;
    public boolean mContainHyperLink;
    public AlertDialog mDialog;
    public DialogCheckableView mDialogCheckableView;
    public Handler mHandler;
    public boolean mIsPositiveReverse;
    public int mNegativeButtonId;
    public DialogInterface.OnClickListener mPositiveButtonClickListener;
    public int mPositiveButtonId;
    public String mPositiveButtonText;
    public long mStartCheckTime;
    public long mStartShowTime;

    public TimerDialog(Builder builder, boolean z) {
        this.mPositiveButtonText = "";
        this.mPositiveButtonId = -1;
        this.mNegativeButtonId = -2;
        Context context = builder.mAlertDialogBuilder.getContext();
        this.mDialog = builder.mAlertDialogBuilder.create();
        this.mConfirmTime = builder.mConfirmTime - 1;
        this.mPositiveButtonClickListener = builder.mPositiveButtonClickListener;
        this.mIsPositiveReverse = builder.mIsPositiveReverse;
        this.mContainHyperLink = builder.mContainHyperLink;
        if (z) {
            this.mPositiveButtonId = -2;
            this.mNegativeButtonId = -1;
        }
        int i = builder.mCheckBoxTextId;
        boolean z2 = builder.mCheckBoxChecked;
        this.mCheckTime = builder.mCheckTime - 1;
        if (i > 0) {
            DialogCheckableView dialogCheckableView = (DialogCheckableView) LayoutInflater.from(context).inflate(R.layout.dialog_checkable_view, (ViewGroup) null);
            this.mDialogCheckableView = dialogCheckableView;
            dialogCheckableView.setChecked(z2);
            this.mDialogCheckableView.setDesc(i);
            this.mDialogCheckableView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.widget.TimerDialog.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    TimerDialog.this.mDialogCheckableView.setChecked(!TimerDialog.this.mDialogCheckableView.isChecked());
                    if (!TimerDialog.this.mDialogCheckableView.isChecked() || TimerDialog.this.mCheckTime <= 0) {
                        TimerDialog.this.mHandler.sendEmptyMessage(3);
                    } else {
                        TimerDialog.this.mHandler.sendEmptyMessage(2);
                    }
                }
            });
            getBaseAlertDialog().setView(this.mDialogCheckableView);
        }
        this.mHandler = new TimerHandler(this);
    }

    public final AlertDialog getBaseAlertDialog() {
        return this.mDialog;
    }

    public final Button getPositiveButton() {
        return getBaseAlertDialog().getButton(this.mPositiveButtonId);
    }

    public final String getRemainderTimeText(long j) {
        return this.mPositiveButtonText + "(" + (j / 1000) + "s)";
    }

    public void show() {
        getBaseAlertDialog().show();
        trigger();
    }

    public boolean isChecked() {
        DialogCheckableView dialogCheckableView = this.mDialogCheckableView;
        if (dialogCheckableView == null) {
            return false;
        }
        return dialogCheckableView.isChecked();
    }

    public final void trigger() {
        if (getBaseAlertDialog().getMessageView() != null && this.mContainHyperLink) {
            getBaseAlertDialog().getMessageView().setMovementMethod(LinkMovementMethod.getInstance());
        }
        this.mStartShowTime = System.currentTimeMillis();
        Button positiveButton = getPositiveButton();
        positiveButton.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.widget.TimerDialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DialogInterface.OnClickListener onClickListener = TimerDialog.this.mPositiveButtonClickListener;
                TimerDialog timerDialog = TimerDialog.this;
                onClickListener.onClick(timerDialog, timerDialog.mPositiveButtonId);
                TimerDialog.this.dismiss();
            }
        });
        this.mPositiveButtonText = positiveButton.getText().toString();
        this.mHandler.sendEmptyMessage(1);
    }

    public final long getRemainderTime() {
        long j = this.mConfirmTime;
        long j2 = 0;
        long currentTimeMillis = j > 0 ? j - (System.currentTimeMillis() - this.mStartShowTime) : 0L;
        if (this.mCheckTime > 0 && this.mDialogCheckableView.isChecked()) {
            j2 = this.mCheckTime - (System.currentTimeMillis() - this.mStartCheckTime);
        }
        return Math.max(j2, currentTimeMillis);
    }

    @Override // android.content.DialogInterface
    public void cancel() {
        getBaseAlertDialog().cancel();
    }

    @Override // android.content.DialogInterface
    public void dismiss() {
        getBaseAlertDialog().dismiss();
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        public AlertDialog.Builder mAlertDialogBuilder;
        public boolean mCheckBoxChecked;
        public int mCheckBoxTextId;
        public boolean mContainHyperLink;
        public boolean mIsPositiveReverse;
        public DialogInterface.OnClickListener mPositiveButtonClickListener;
        public long mConfirmTime = -1;
        public long mCheckTime = -1;

        public Builder(Context context) {
            this.mAlertDialogBuilder = new AlertDialog.Builder(context);
        }

        public Builder(Context context, boolean z) {
            this.mAlertDialogBuilder = new AlertDialog.Builder(context);
            this.mIsPositiveReverse = z;
        }

        public Builder setTitle(int i) {
            this.mAlertDialogBuilder.setTitle(i);
            return this;
        }

        public Builder setMessage(int i) {
            this.mAlertDialogBuilder.setMessage(i);
            return this;
        }

        public Builder setMessage(CharSequence charSequence, boolean z) {
            this.mAlertDialogBuilder.setMessage(charSequence);
            this.mContainHyperLink = z;
            return this;
        }

        public Builder setConfirmTime(long j) {
            this.mConfirmTime = j;
            return this;
        }

        public Builder setCheckBox(int i, boolean z, long j) {
            this.mCheckBoxTextId = i;
            this.mCheckBoxChecked = z;
            this.mCheckTime = j;
            return this;
        }

        public Builder setPositiveButton(int i, DialogInterface.OnClickListener onClickListener) {
            if (this.mIsPositiveReverse) {
                this.mAlertDialogBuilder.setNegativeButton(i, (DialogInterface.OnClickListener) null);
            } else {
                this.mAlertDialogBuilder.setPositiveButton(i, (DialogInterface.OnClickListener) null);
            }
            this.mPositiveButtonClickListener = onClickListener;
            return this;
        }

        public Builder setNegativeButton(int i, DialogInterface.OnClickListener onClickListener) {
            if (this.mIsPositiveReverse) {
                this.mAlertDialogBuilder.setPositiveButton(i, (DialogInterface.OnClickListener) null);
            } else {
                this.mAlertDialogBuilder.setNegativeButton(i, (DialogInterface.OnClickListener) null);
            }
            return this;
        }

        public TimerDialog build() {
            return new TimerDialog(this, this.mIsPositiveReverse);
        }
    }

    /* loaded from: classes2.dex */
    public static class TimerHandler extends Handler {
        public WeakReference<TimerDialog> mWeakRef;

        public TimerHandler(TimerDialog timerDialog) {
            super(Looper.getMainLooper());
            this.mWeakRef = new WeakReference<>(timerDialog);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            TimerDialog timerDialog = this.mWeakRef.get();
            if (timerDialog == null) {
                return;
            }
            int i = message.what;
            if (i == 0) {
                timerDialog.getPositiveButton().setEnabled(true);
                timerDialog.getPositiveButton().setText(timerDialog.mPositiveButtonText);
            } else if (i == 1) {
                long remainderTime = timerDialog.getRemainderTime();
                if (remainderTime > 0) {
                    timerDialog.getPositiveButton().setEnabled(false);
                    timerDialog.getPositiveButton().setText(timerDialog.getRemainderTimeText(remainderTime));
                    sendEmptyMessageDelayed(1, 100L);
                    return;
                }
                sendEmptyMessageDelayed(0, Math.max(0L, remainderTime));
            } else if (i == 2) {
                timerDialog.mStartCheckTime = System.currentTimeMillis();
                sendEmptyMessage(1);
            } else if (i == 3) {
                if (timerDialog.getRemainderTime() == 0) {
                    sendEmptyMessage(0);
                } else {
                    sendEmptyMessage(1);
                }
            } else {
                super.handleMessage(message);
            }
        }
    }
}
