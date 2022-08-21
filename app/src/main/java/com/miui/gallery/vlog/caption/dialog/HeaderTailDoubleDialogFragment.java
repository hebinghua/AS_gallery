package com.miui.gallery.vlog.caption.dialog;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.vlog.R$drawable;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.vlog.R$layout;
import com.miui.gallery.vlog.R$style;

/* loaded from: classes2.dex */
public class HeaderTailDoubleDialogFragment extends DialogFragment implements View.OnFocusChangeListener, View.OnClickListener, TextWatcher {
    public ImageView mCancel;
    public String mContent;
    public int mFocusId;
    public long mInPoint;
    public EditText mMainTitle;
    public int mMainTitleLimit;
    public ImageView mOk;
    public View mRootView;
    public String mSub;
    public EditText mSubTitle;
    public int mSubTitleLimit;
    public EditorCallback mTitleEditorCallback;
    public TextView mWordCount;
    public boolean mIsKeyboardShowing = false;
    public ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.miui.gallery.vlog.caption.dialog.HeaderTailDoubleDialogFragment.2
        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            View rootView = HeaderTailDoubleDialogFragment.this.mRootView.getRootView();
            int exactScreenHeight = ScreenUtils.getExactScreenHeight(HeaderTailDoubleDialogFragment.this.getActivity());
            int height = rootView.getHeight();
            int[] iArr = new int[2];
            rootView.getLocationOnScreen(iArr);
            int i = (exactScreenHeight - height) - iArr[1];
            if (!HeaderTailDoubleDialogFragment.this.mIsKeyboardShowing && i > 0) {
                HeaderTailDoubleDialogFragment.this.mIsKeyboardShowing = true;
            }
            if (i != 0 || !HeaderTailDoubleDialogFragment.this.mIsKeyboardShowing) {
                return;
            }
            HeaderTailDoubleDialogFragment.this.dismiss();
            HeaderTailDoubleDialogFragment.this.mIsKeyboardShowing = false;
        }
    };

    /* loaded from: classes2.dex */
    public interface EditorCallback {
        void onCancel();

        void onHeaderTailEditorFinished(String str, long j);

        void onHeaderTailUpdateFinished(String str, String str2);
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public static HeaderTailDoubleDialogFragment newInstance() {
        return new HeaderTailDoubleDialogFragment();
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStyle(2, R$style.Gallery_Theme_Dialog);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R$layout.vlog_header_tail_double_editor_layout, viewGroup, false);
    }

    public final void initView(View view) {
        this.mRootView = view;
        this.mMainTitle = (EditText) view.findViewById(R$id.et_main_title);
        this.mSubTitle = (EditText) view.findViewById(R$id.et_sub_title);
        this.mCancel = (ImageView) view.findViewById(R$id.vlog_caption_editor_btn_cancel);
        this.mOk = (ImageView) view.findViewById(R$id.vlog_caption_editor_btn_ok);
        this.mWordCount = (TextView) view.findViewById(R$id.word_count);
        this.mMainTitle.setOnFocusChangeListener(this);
        this.mSubTitle.setOnFocusChangeListener(this);
        this.mMainTitle.addTextChangedListener(this);
        this.mSubTitle.addTextChangedListener(this);
        this.mCancel.setOnClickListener(this);
        this.mOk.setOnClickListener(this);
        this.mMainTitle.setFocusable(true);
        this.mMainTitle.setFocusableInTouchMode(true);
        this.mMainTitle.requestFocus();
        if (!TextUtils.isEmpty(this.mContent)) {
            this.mMainTitle.setText(this.mContent);
        }
        this.mMainTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(this.mMainTitleLimit)});
        this.mSubTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(this.mSubTitleLimit)});
        view.getViewTreeObserver().addOnGlobalLayoutListener(this.mOnGlobalLayoutListener);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        getDialog().setCanceledOnTouchOutside(false);
        initView(view);
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        WindowManager.LayoutParams attributes = getDialog().getWindow().getAttributes();
        ((ViewGroup.LayoutParams) attributes).width = -1;
        ((ViewGroup.LayoutParams) attributes).height = -1;
        getDialog().getWindow().setAttributes(attributes);
        super.onResume();
        new Handler().postDelayed(new Runnable() { // from class: com.miui.gallery.vlog.caption.dialog.HeaderTailDoubleDialogFragment.1
            @Override // java.lang.Runnable
            public void run() {
                ((InputMethodManager) HeaderTailDoubleDialogFragment.this.mMainTitle.getContext().getSystemService("input_method")).toggleSoftInput(0, 2);
            }
        }, 100L);
        if (!TextUtils.isEmpty(this.mSub)) {
            this.mSubTitle.setText(this.mSub);
        }
        if (!TextUtils.isEmpty(this.mContent)) {
            this.mMainTitle.setText(this.mContent);
            this.mMainTitle.setSelection(Math.min(this.mMainTitleLimit, this.mContent.length()));
        }
    }

    public void setContent(String str) {
        this.mContent = str;
    }

    public void setSub(String str) {
        this.mSub = str;
    }

    public void setWordLimit(int i, int i2) {
        this.mMainTitleLimit = i;
        this.mSubTitleLimit = i2;
    }

    @Override // android.view.View.OnFocusChangeListener
    public void onFocusChange(View view, boolean z) {
        if (z) {
            int id = view.getId();
            int i = R$id.et_main_title;
            if (id == i) {
                this.mMainTitle.setBackgroundResource(R$drawable.vlog_title_background_focus);
                this.mSubTitle.setBackgroundResource(R$drawable.vlog_title_background_unfocus);
                this.mWordCount.setText(String.valueOf(this.mMainTitleLimit));
                String obj = this.mMainTitle.getText().toString();
                if (!TextUtils.isEmpty(obj)) {
                    this.mWordCount.setText(String.valueOf(this.mMainTitleLimit - obj.length()));
                }
                this.mFocusId = i;
                return;
            }
            int id2 = view.getId();
            int i2 = R$id.et_sub_title;
            if (id2 != i2) {
                return;
            }
            this.mMainTitle.setBackgroundResource(R$drawable.vlog_title_background_unfocus);
            this.mSubTitle.setBackgroundResource(R$drawable.vlog_title_background_focus);
            this.mWordCount.setText(String.valueOf(this.mSubTitleLimit));
            String obj2 = this.mSubTitle.getText().toString();
            if (!TextUtils.isEmpty(obj2)) {
                this.mWordCount.setText(String.valueOf(this.mSubTitleLimit - obj2.length()));
            }
            this.mFocusId = i2;
        }
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        if (charSequence != null) {
            if (this.mFocusId == R$id.et_main_title) {
                this.mWordCount.setText(String.valueOf(this.mMainTitleLimit - (this.mMainTitle.getText() == null ? 0 : this.mMainTitle.getText().length())));
            } else {
                this.mWordCount.setText(String.valueOf(this.mSubTitleLimit - (this.mSubTitle.getText() == null ? 0 : this.mSubTitle.getText().length())));
            }
            if (this.mMainTitle.getText().length() > 0 && this.mSubTitle.getText().length() > 0) {
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
            this.mMainTitle.setText("");
            dismiss();
            this.mIsKeyboardShowing = false;
            this.mTitleEditorCallback.onCancel();
        } else if (view.getId() != R$id.vlog_caption_editor_btn_ok) {
        } else {
            String obj = this.mMainTitle.getText().toString();
            String obj2 = this.mSubTitle.getText().toString();
            if (TextUtils.isEmpty(obj) || TextUtils.isEmpty(obj2) || this.mTitleEditorCallback == null) {
                return;
            }
            if (TextUtils.isEmpty(this.mContent)) {
                this.mTitleEditorCallback.onHeaderTailEditorFinished(obj, this.mInPoint);
            } else {
                this.mTitleEditorCallback.onHeaderTailUpdateFinished(obj, obj2);
            }
            dismiss();
            this.mIsKeyboardShowing = false;
        }
    }

    public void setTitleSubEditorCallback(EditorCallback editorCallback) {
        this.mTitleEditorCallback = editorCallback;
    }
}
