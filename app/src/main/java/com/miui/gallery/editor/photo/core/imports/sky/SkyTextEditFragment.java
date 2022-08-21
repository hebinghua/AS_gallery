package com.miui.gallery.editor.photo.core.imports.sky;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import androidx.fragment.app.FragmentManager;
import com.miui.gallery.R;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.R$style;
import com.miui.gallery.widget.GalleryDialogFragment;

/* loaded from: classes2.dex */
public class SkyTextEditFragment extends GalleryDialogFragment implements TextWatcher, View.OnClickListener {
    public ImageView mCancel;
    public EditText mCaptionText;
    public String mContent;
    public ImageView mOk;
    public View mRootView;
    public SkyTextEditorCallback mSkyTextEditorCallback;
    public TextView mWordCount;
    public boolean mIsKeyboardShowing = false;
    public ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.miui.gallery.editor.photo.core.imports.sky.SkyTextEditFragment.3
        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            View rootView = SkyTextEditFragment.this.mRootView.getRootView();
            int exactScreenHeight = ScreenUtils.getExactScreenHeight(SkyTextEditFragment.this.getActivity());
            int height = rootView.getHeight();
            int[] iArr = new int[2];
            rootView.getLocationOnScreen(iArr);
            int i = (exactScreenHeight - height) - iArr[1];
            if (!SkyTextEditFragment.this.mIsKeyboardShowing && i > 0) {
                SkyTextEditFragment.this.mIsKeyboardShowing = true;
            }
            if (i != 0 || !SkyTextEditFragment.this.mIsKeyboardShowing) {
                return;
            }
            SkyTextEditFragment.this.dismiss();
            SkyTextEditFragment.this.mIsKeyboardShowing = false;
        }
    };

    /* loaded from: classes2.dex */
    public interface SkyTextEditorCallback {
        void onCancel();

        void onCaptionAdd(String str);

        void onCaptionUpdate(String str);

        void onClose();
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public static SkyTextEditFragment newInstance() {
        return new SkyTextEditFragment();
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
                DefaultLogger.e("SkyTextEditFragment", "null FragmentManager");
            }
        } catch (IllegalStateException e) {
            DefaultLogger.w("SkyTextEditFragment", "%s : showAllowingStateLoss ignore:%s", getClass().getSimpleName(), e);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.sky_text_edit_layout, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        getDialog().setCanceledOnTouchOutside(false);
        initView(view);
    }

    public final void initView(View view) {
        this.mRootView = view;
        this.mCaptionText = (EditText) view.findViewById(R.id.dialog_fragment);
        this.mCancel = (ImageView) view.findViewById(R.id.text_editor_btn_cancel);
        this.mOk = (ImageView) view.findViewById(R.id.text_editor_btn_ok);
        this.mWordCount = (TextView) view.findViewById(R.id.word_count);
        this.mCaptionText.addTextChangedListener(this);
        this.mCancel.setOnClickListener(this);
        this.mOk.setOnClickListener(this);
        AnimParams build = new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build();
        FolmeUtil.setCustomTouchAnim(this.mOk, build, null, null, null, true);
        FolmeUtil.setCustomTouchAnim(this.mCancel, build, null, null, null, true);
        this.mCaptionText.setFocusable(true);
        this.mCaptionText.setFocusableInTouchMode(true);
        this.mCaptionText.requestFocus();
        this.mCaptionText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.miui.gallery.editor.photo.core.imports.sky.SkyTextEditFragment.1
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return keyEvent == null || keyEvent.getKeyCode() == 66;
            }
        });
        this.mCaptionText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        this.mCaptionText.setHint(getResources().getString(R.string.filter_sky_dynamic_text_yanhua_hint, 1, 6));
        view.getViewTreeObserver().addOnGlobalLayoutListener(this.mOnGlobalLayoutListener);
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
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        this.mCaptionText.postDelayed(new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.sky.SkyTextEditFragment.2
            @Override // java.lang.Runnable
            public void run() {
                ((InputMethodManager) SkyTextEditFragment.this.mCaptionText.getContext().getSystemService("input_method")).toggleSoftInput(0, 2);
            }
        }, 100L);
        if (!TextUtils.isEmpty(this.mContent)) {
            this.mCaptionText.setText(this.mContent);
            this.mCaptionText.setSelection(Math.min(6, this.mContent.length()));
            return;
        }
        this.mCaptionText.setText("");
        this.mOk.setEnabled(false);
    }

    public void setContent(String str) {
        this.mContent = str;
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
        if (editable != null && editable.length() > 0 && editable.charAt(0) == ' ') {
            editable.delete(0, 1);
        }
        if (editable != null) {
            this.mWordCount.setText(String.valueOf(6 - editable.length()));
            if (editable.length() > 0) {
                this.mOk.setEnabled(true);
                this.mOk.setAlpha(1.0f);
                return;
            }
            this.mOk.setEnabled(false);
            this.mOk.setAlpha(0.3f);
            return;
        }
        this.mOk.setAlpha(0.3f);
        this.mOk.setEnabled(false);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.text_editor_btn_cancel) {
            dismiss();
            this.mIsKeyboardShowing = false;
            SkyTextEditorCallback skyTextEditorCallback = this.mSkyTextEditorCallback;
            if (skyTextEditorCallback == null) {
                return;
            }
            skyTextEditorCallback.onCancel();
        } else if (view.getId() != R.id.text_editor_btn_ok) {
        } else {
            String obj = this.mCaptionText.getText().toString();
            if (TextUtils.isEmpty(obj) || this.mSkyTextEditorCallback == null) {
                return;
            }
            if (TextUtils.isEmpty(this.mContent)) {
                this.mSkyTextEditorCallback.onCaptionAdd(obj);
            } else {
                this.mSkyTextEditorCallback.onCaptionUpdate(obj);
            }
            dismiss();
            this.mIsKeyboardShowing = false;
            SamplingStatHelper.recordCountEvent("photo_editor", "sky_text_yanhua_editor_ok", null);
        }
    }

    public void setCaptionEditorCallback(SkyTextEditorCallback skyTextEditorCallback) {
        this.mSkyTextEditorCallback = skyTextEditorCallback;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        SkyTextEditorCallback skyTextEditorCallback = this.mSkyTextEditorCallback;
        if (skyTextEditorCallback != null) {
            skyTextEditorCallback.onClose();
        }
        super.onDestroy();
    }
}
