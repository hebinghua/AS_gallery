package com.miui.gallery.vlog.caption.dialog;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.vlog.R$layout;
import com.miui.gallery.vlog.R$style;

/* loaded from: classes2.dex */
public class HeaderTailSingleDialogFragment extends DialogFragment implements TextWatcher, View.OnClickListener, TextView.OnEditorActionListener {
    public ImageView mCancel;
    public EditText mCaptionText;
    public String mContent;
    public long mInPoint;
    public ImageView mOk;
    public View mRootView;
    public EditorCallback mTitleEditorCallback;
    public TextView mWordCount;
    public boolean mIsKeyboardShowing = false;
    public int mWordLimit = 0;
    public ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.miui.gallery.vlog.caption.dialog.HeaderTailSingleDialogFragment.2
        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            View rootView = HeaderTailSingleDialogFragment.this.mRootView.getRootView();
            int exactScreenHeight = ScreenUtils.getExactScreenHeight(HeaderTailSingleDialogFragment.this.getActivity());
            int height = rootView.getHeight();
            int[] iArr = new int[2];
            rootView.getLocationOnScreen(iArr);
            int i = (exactScreenHeight - height) - iArr[1];
            if (!HeaderTailSingleDialogFragment.this.mIsKeyboardShowing && i > 0) {
                HeaderTailSingleDialogFragment.this.mIsKeyboardShowing = true;
            }
            if (i != 0 || !HeaderTailSingleDialogFragment.this.mIsKeyboardShowing) {
                return;
            }
            HeaderTailSingleDialogFragment.this.dismiss();
            HeaderTailSingleDialogFragment.this.mIsKeyboardShowing = false;
        }
    };

    /* loaded from: classes2.dex */
    public interface EditorCallback {
        void onCancel();

        void onTitleSingleEditorFinished(String str, long j);

        void onTitleSingleUpdateFinished(String str);
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public static HeaderTailSingleDialogFragment newInstance() {
        return new HeaderTailSingleDialogFragment();
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStyle(2, R$style.Gallery_Theme_Dialog);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R$layout.vlog_caption_text_editor_layout, viewGroup, false);
    }

    public final void initView(View view) {
        this.mRootView = view;
        this.mCaptionText = (EditText) view.findViewById(R$id.et_dialog_fragment);
        this.mCancel = (ImageView) view.findViewById(R$id.vlog_caption_editor_btn_cancel);
        this.mOk = (ImageView) view.findViewById(R$id.vlog_caption_editor_btn_ok);
        this.mWordCount = (TextView) view.findViewById(R$id.word_count);
        this.mCaptionText.addTextChangedListener(this);
        this.mCaptionText.setOnEditorActionListener(this);
        this.mCancel.setOnClickListener(this);
        this.mOk.setOnClickListener(this);
        this.mCaptionText.setFocusable(true);
        this.mCaptionText.setFocusableInTouchMode(true);
        this.mCaptionText.requestFocus();
        if (!TextUtils.isEmpty(this.mContent)) {
            this.mCaptionText.setText(this.mContent);
            this.mWordCount.setText(String.valueOf(this.mWordLimit - this.mContent.length()));
        } else {
            this.mWordCount.setText(String.valueOf(this.mWordLimit));
        }
        this.mCaptionText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(this.mWordLimit)});
        view.getViewTreeObserver().addOnGlobalLayoutListener(this.mOnGlobalLayoutListener);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        getDialog().setCanceledOnTouchOutside(false);
        initView(view);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setGravity(119);
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = -1;
            attributes.height = -2;
            attributes.dimAmount = 0.0f;
            window.setAttributes(attributes);
            window.setBackgroundDrawable(new ColorDrawable(0));
            window.setSoftInputMode(16);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() { // from class: com.miui.gallery.vlog.caption.dialog.HeaderTailSingleDialogFragment.1
            @Override // java.lang.Runnable
            public void run() {
                ((InputMethodManager) HeaderTailSingleDialogFragment.this.mCaptionText.getContext().getSystemService("input_method")).toggleSoftInput(0, 2);
            }
        }, 100L);
        if (!TextUtils.isEmpty(this.mContent)) {
            this.mCaptionText.setText(this.mContent);
            this.mCaptionText.setSelection(Math.min(this.mWordLimit, this.mContent.length()));
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void setUserVisibleHint(boolean z) {
        super.setUserVisibleHint(z);
    }

    public void setContent(String str) {
        this.mContent = str;
    }

    public void setWordLimit(int i) {
        this.mWordLimit = i;
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
        if (editable != null) {
            this.mWordCount.setText(String.valueOf(this.mWordLimit - editable.length()));
            if (editable.length() > 0) {
                this.mOk.setEnabled(true);
                return;
            } else {
                this.mOk.setEnabled(false);
                return;
            }
        }
        this.mOk.setEnabled(false);
    }

    @Override // androidx.fragment.app.DialogFragment
    public void dismiss() {
        this.mCaptionText.setText("");
        this.mIsKeyboardShowing = false;
        if (isStateSaved() || !isResumed()) {
            return;
        }
        super.dismiss();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R$id.vlog_caption_editor_btn_cancel) {
            this.mCaptionText.setText("");
            dismiss();
            this.mIsKeyboardShowing = false;
            this.mTitleEditorCallback.onCancel();
        } else if (view.getId() != R$id.vlog_caption_editor_btn_ok) {
        } else {
            performOkEvent();
        }
    }

    public final void performOkEvent() {
        String obj = this.mCaptionText.getText().toString();
        if (TextUtils.isEmpty(obj) || this.mTitleEditorCallback == null) {
            return;
        }
        if (TextUtils.isEmpty(this.mContent)) {
            this.mTitleEditorCallback.onTitleSingleEditorFinished(obj, this.mInPoint);
        } else {
            this.mTitleEditorCallback.onTitleSingleUpdateFinished(obj);
        }
        this.mCaptionText.setText("");
        dismiss();
        this.mIsKeyboardShowing = false;
    }

    public void setTitleSingleEditorCallback(EditorCallback editorCallback) {
        this.mTitleEditorCallback = editorCallback;
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 6) {
            performOkEvent();
            return true;
        }
        return false;
    }
}
