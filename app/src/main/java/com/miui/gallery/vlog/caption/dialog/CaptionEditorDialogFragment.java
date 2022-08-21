package com.miui.gallery.vlog.caption.dialog;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
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
import androidx.fragment.app.FragmentManager;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.vlog.R$layout;
import com.miui.gallery.vlog.R$style;
import com.miui.gallery.widget.GalleryDialogFragment;

/* loaded from: classes2.dex */
public class CaptionEditorDialogFragment extends GalleryDialogFragment implements TextWatcher, View.OnClickListener {
    public ImageView mCancel;
    public CaptionEditorCallback mCaptionEditorCallback;
    public EditText mCaptionText;
    public String mContent;
    public long mInPoint;
    public ImageView mOk;
    public View mRootView;
    public TextView mWordCount;
    public boolean mIsKeyboardShowing = false;
    public ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.miui.gallery.vlog.caption.dialog.CaptionEditorDialogFragment.3
        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            View rootView = CaptionEditorDialogFragment.this.mRootView.getRootView();
            int exactScreenHeight = ScreenUtils.getExactScreenHeight(CaptionEditorDialogFragment.this.getActivity());
            int height = rootView.getHeight();
            int[] iArr = new int[2];
            rootView.getLocationOnScreen(iArr);
            int i = (exactScreenHeight - height) - iArr[1];
            if (!CaptionEditorDialogFragment.this.mIsKeyboardShowing && i > 0) {
                CaptionEditorDialogFragment.this.mIsKeyboardShowing = true;
            }
            if (i != 0 || !CaptionEditorDialogFragment.this.mIsKeyboardShowing) {
                return;
            }
            CaptionEditorDialogFragment.this.dismiss();
            CaptionEditorDialogFragment.this.mIsKeyboardShowing = false;
        }
    };

    /* loaded from: classes2.dex */
    public interface CaptionEditorCallback {
        void onCancel();

        void onCaptionAdd(String str, long j);

        void onCaptionUpdate(String str);
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public static CaptionEditorDialogFragment newInstance() {
        return new CaptionEditorDialogFragment();
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStyle(2, R$style.Gallery_Theme_Dialog);
    }

    @Override // com.miui.gallery.widget.GalleryDialogFragment
    public void showAllowingStateLoss(FragmentManager fragmentManager, String str) {
        try {
            if (fragmentManager != null) {
                fragmentManager.beginTransaction().remove(this).commit();
                super.show(fragmentManager, str);
            } else {
                DefaultLogger.e("CaptionEditorDialogFragment", "null FragmentManager");
            }
        } catch (IllegalStateException e) {
            DefaultLogger.w("CaptionEditorDialogFragment", "%s : showAllowingStateLoss ignore:%s", getClass().getSimpleName(), e);
        }
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
        this.mCancel.setOnClickListener(this);
        this.mOk.setOnClickListener(this);
        this.mCaptionText.setFocusable(true);
        this.mCaptionText.setFocusableInTouchMode(true);
        this.mCaptionText.requestFocus();
        this.mCaptionText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.miui.gallery.vlog.caption.dialog.CaptionEditorDialogFragment.1
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i != 6) {
                    return keyEvent != null && keyEvent.getKeyCode() == 66;
                }
                CaptionEditorDialogFragment.this.performOkEvent();
                return true;
            }
        });
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
        this.mCaptionText.postDelayed(new Runnable() { // from class: com.miui.gallery.vlog.caption.dialog.CaptionEditorDialogFragment.2
            @Override // java.lang.Runnable
            public void run() {
                ((InputMethodManager) CaptionEditorDialogFragment.this.mCaptionText.getContext().getSystemService("input_method")).toggleSoftInput(0, 2);
            }
        }, 100L);
        if (!TextUtils.isEmpty(this.mContent)) {
            this.mCaptionText.setText(this.mContent);
            this.mCaptionText.setSelection(Math.min(30, this.mContent.length()));
            return;
        }
        this.mCaptionText.setText("");
        this.mOk.setEnabled(false);
    }

    public void setInPoint(long j) {
        this.mInPoint = j;
    }

    public void setContent(String str) {
        this.mContent = str;
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
        if (editable != null) {
            this.mWordCount.setText(String.valueOf(30 - editable.length()));
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

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R$id.vlog_caption_editor_btn_cancel) {
            dismiss();
            this.mIsKeyboardShowing = false;
            CaptionEditorCallback captionEditorCallback = this.mCaptionEditorCallback;
            if (captionEditorCallback == null) {
                return;
            }
            captionEditorCallback.onCancel();
        } else if (view.getId() != R$id.vlog_caption_editor_btn_ok) {
        } else {
            performOkEvent();
        }
    }

    public final void performOkEvent() {
        String obj = this.mCaptionText.getText().toString();
        if (TextUtils.isEmpty(obj) || this.mCaptionEditorCallback == null) {
            return;
        }
        if (TextUtils.isEmpty(this.mContent)) {
            this.mCaptionEditorCallback.onCaptionAdd(obj, this.mInPoint);
        } else {
            this.mCaptionEditorCallback.onCaptionUpdate(obj);
        }
        dismiss();
        this.mIsKeyboardShowing = false;
    }

    public void setCaptionEditorCallback(CaptionEditorCallback captionEditorCallback) {
        this.mCaptionEditorCallback = captionEditorCallback;
    }

    public void unRegisterCallback() {
        this.mCaptionEditorCallback = null;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
    }
}
