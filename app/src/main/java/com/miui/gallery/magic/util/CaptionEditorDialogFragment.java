package com.miui.gallery.magic.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.fragment.app.FragmentManager;
import com.miui.gallery.magic.R$array;
import com.miui.gallery.magic.R$color;
import com.miui.gallery.magic.R$dimen;
import com.miui.gallery.magic.R$drawable;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$layout;
import com.miui.gallery.magic.R$style;
import com.miui.gallery.magic.idphoto.bean.PhotoStyle;
import com.miui.gallery.magic.idphoto.menu.CertificatesMenuPresenter;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.GalleryDialogFragment;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class CaptionEditorDialogFragment extends GalleryDialogFragment implements View.OnClickListener {
    public CertificatesMenuPresenter.MyPhotoStyleCallBack callBack;
    public RelativeLayout editTextRl;
    public RelativeLayout editTextRlH;
    public String editText_h_mm;
    public String editText_h_px;
    public String editText_w_mm;
    public String editText_w_px;
    public TextView errorText;
    public EditText h;
    public RelativeLayout mCancel;
    public View mContentView;
    public AlertDialog mDialog;
    public Drawable mHDrawable;
    public PopupWindow mIndicator;
    public RelativeLayout mOk;
    public Drawable mWDrawable;
    public TextView popHText;
    public LinearLayout popHeight;
    public TextView popWText;
    public LinearLayout popWidth;
    public RelativeLayout rlMM;
    public RelativeLayout rlPx;
    public TextView tvCancel;
    public TextView tvOk;
    public EditText w;
    public float alpha70 = 0.7f;
    public float alphaAll = 1.0f;
    public boolean isWidthEditext = false;
    public boolean isHeightEditext = false;
    public int SELECTED_PX_OR_MM = 0;
    public ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.miui.gallery.magic.util.CaptionEditorDialogFragment.7
        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
        }
    };

    public static CaptionEditorDialogFragment newInstance(Bundle bundle) {
        CaptionEditorDialogFragment captionEditorDialogFragment = new CaptionEditorDialogFragment();
        captionEditorDialogFragment.setArguments(bundle);
        return captionEditorDialogFragment;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStyle(2, R$style.Magic_Gallery_Theme_Dialog);
        this.callBack = (CertificatesMenuPresenter.MyPhotoStyleCallBack) getArguments().getParcelable(CertificatesMenuPresenter.PHOTOSTYLECALLBACK);
        bgAlpha(this.alpha70);
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View inflate = View.inflate(getActivity(), R$layout.ts_vlog_caption_text_editor_layout, null);
        initView(inflate);
        this.errorText.setVisibility(8);
        builder.setView(inflate);
        AlertDialog create = builder.create();
        this.mDialog = create;
        create.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.miui.gallery.magic.util.CaptionEditorDialogFragment.1
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface dialogInterface) {
                BaseMiscUtil.showInputMethod(CaptionEditorDialogFragment.this.w);
            }
        });
        return this.mDialog;
    }

    public final void initView(View view) {
        this.popWidth = (LinearLayout) view.findViewById(R$id.pop_w_ll);
        this.popHeight = (LinearLayout) view.findViewById(R$id.pop_h_ll);
        this.popHText = (TextView) view.findViewById(R$id.tv_h_text);
        this.popWText = (TextView) view.findViewById(R$id.tv_w_text);
        this.editTextRl = (RelativeLayout) view.findViewById(R$id.rl_edit_w);
        this.editTextRlH = (RelativeLayout) view.findViewById(R$id.rl_edit_h);
        this.tvCancel = (TextView) view.findViewById(R$id.tv_cancel);
        this.tvOk = (TextView) view.findViewById(R$id.tv_ok);
        this.w = (EditText) view.findViewById(R$id.magic_idp_dialog_w);
        this.h = (EditText) view.findViewById(R$id.magic_idp_dialog_h);
        this.errorText = (TextView) view.findViewById(R$id.module_tv_hint_text);
        this.mCancel = (RelativeLayout) view.findViewById(R$id.vlog_caption_editor_btn_cancel);
        this.mOk = (RelativeLayout) view.findViewById(R$id.vlog_caption_editor_btn_ok);
        AnimParams build = new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build();
        FolmeUtil.setCustomTouchAnim(this.mCancel, build, null, null, null, true);
        FolmeUtil.setCustomTouchAnim(this.mOk, build, null, null, null, true);
        Context context = getContext();
        int i = R$drawable.magic_id_photo_edit_blue_shape;
        this.mWDrawable = context.getDrawable(i);
        this.mHDrawable = getContext().getDrawable(R$drawable.magic_id_photo_edit_shape);
        this.w.setFocusable(true);
        this.w.setFocusableInTouchMode(true);
        this.editTextRl.setBackground(getContext().getDrawable(i));
        this.popHeight.setOnClickListener(this);
        this.popWidth.setOnClickListener(this);
        this.mCancel.setOnClickListener(this);
        this.mOk.setOnClickListener(this);
        this.w.addTextChangedListener(new wTextWatcher());
        this.h.addTextChangedListener(new HTextWatcher());
        this.w.requestFocus();
        this.w.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: com.miui.gallery.magic.util.CaptionEditorDialogFragment.2
            @Override // android.view.View.OnFocusChangeListener
            public void onFocusChange(View view2, boolean z) {
                if (z) {
                    CaptionEditorDialogFragment.this.editTextRl.setBackground(CaptionEditorDialogFragment.this.getContext().getDrawable(R$drawable.magic_id_photo_edit_blue_shape));
                } else {
                    CaptionEditorDialogFragment.this.editTextRl.setBackground(CaptionEditorDialogFragment.this.getContext().getDrawable(R$drawable.magic_id_photo_edit_shape));
                }
                CaptionEditorDialogFragment captionEditorDialogFragment = CaptionEditorDialogFragment.this;
                captionEditorDialogFragment.mWDrawable = captionEditorDialogFragment.editTextRl.getBackground();
            }
        });
        this.h.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: com.miui.gallery.magic.util.CaptionEditorDialogFragment.3
            @Override // android.view.View.OnFocusChangeListener
            public void onFocusChange(View view2, boolean z) {
                if (z) {
                    CaptionEditorDialogFragment.this.editTextRlH.setBackground(CaptionEditorDialogFragment.this.getContext().getDrawable(R$drawable.magic_id_photo_edit_blue_shape));
                } else {
                    CaptionEditorDialogFragment.this.editTextRlH.setBackground(CaptionEditorDialogFragment.this.getContext().getDrawable(R$drawable.magic_id_photo_edit_shape));
                }
                CaptionEditorDialogFragment captionEditorDialogFragment = CaptionEditorDialogFragment.this;
                captionEditorDialogFragment.mHDrawable = captionEditorDialogFragment.editTextRlH.getBackground();
            }
        });
        String str = "px";
        if (this.callBack.getCurrent() == null) {
            this.SELECTED_PX_OR_MM = 0;
            this.editText_w_px = "";
            this.editText_h_px = "";
            this.editText_w_mm = "";
            this.editText_h_mm = "";
            isHaveTextColorAndOkBg(false);
        } else {
            if (TextUtils.equals(str, this.callBack.getCurrent().getSizeType())) {
                this.SELECTED_PX_OR_MM = 0;
                this.editText_w_px = this.callBack.getCurrent().getWidth() + "";
                this.editText_h_px = this.callBack.getCurrent().getHeight() + "";
                this.editText_w_mm = "";
                this.editText_h_mm = "";
            } else {
                this.SELECTED_PX_OR_MM = 1;
                this.editText_w_px = "";
                this.editText_h_px = "";
                this.editText_w_mm = this.callBack.getCurrent().getWidth() + "";
                this.editText_h_mm = this.callBack.getCurrent().getHeight() + "";
            }
            EditText editText = this.w;
            editText.setText(this.callBack.getCurrent().getWidth() + "");
            EditText editText2 = this.h;
            editText2.setText(this.callBack.getCurrent().getHeight() + "");
            isHaveTextColorAndOkBg(true);
        }
        if (this.SELECTED_PX_OR_MM != 0) {
            str = "mm";
        }
        this.popHText.setText(str);
        this.popWText.setText(str);
        view.getViewTreeObserver().addOnGlobalLayoutListener(this.mOnGlobalLayoutListener);
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R$id.vlog_caption_editor_btn_cancel) {
            dismiss();
            hintKbTwo(this.w, getContext());
        } else if (view.getId() == R$id.vlog_caption_editor_btn_ok) {
            int i = getInt(this.w.getText().toString());
            int i2 = getInt(this.h.getText().toString());
            PhotoStyle photoStyle = new PhotoStyle(i, i2);
            String str = this.SELECTED_PX_OR_MM == 0 ? "px" : "mm";
            if (str.equals("px") && (photoStyle.getWidth() > 1024 || photoStyle.getWidth() < 100)) {
                this.editTextRl.setBackground(getContext().getDrawable(R$drawable.magic_id_photo_edit_red_shape));
                this.errorText.setVisibility(0);
            }
            if (str.equals("px") && (photoStyle.getHeight() > 1024 || photoStyle.getHeight() < 100)) {
                this.editTextRlH.setBackground(getContext().getDrawable(R$drawable.magic_id_photo_edit_red_shape));
                this.errorText.setVisibility(0);
            }
            if (str.equals("mm") && (photoStyle.getWidth() > 120 || photoStyle.getWidth() < 10)) {
                this.editTextRl.setBackground(getContext().getDrawable(R$drawable.magic_id_photo_edit_red_shape));
                this.errorText.setVisibility(0);
            }
            if (str.equals("mm") && (photoStyle.getHeight() > 120 || photoStyle.getHeight() < 10)) {
                this.editTextRlH.setBackground(getContext().getDrawable(R$drawable.magic_id_photo_edit_red_shape));
                this.errorText.setVisibility(0);
            }
            if (this.errorText.getVisibility() == 0) {
                return;
            }
            if (str.equals("mm")) {
                photoStyle.setwMM(i);
                photoStyle.sethMM(i2);
                photoStyle.useDpi();
            }
            photoStyle.setSizeType(str);
            this.callBack.dismissCallBack(photoStyle);
            dismiss();
            hintKbTwo(this.w, getContext());
        } else if (view.getId() == R$id.pop_w_ll) {
            int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.pop_w_height);
            int dimensionPixelSize2 = getResources().getDimensionPixelSize(R$dimen.magic_w_width);
            if (BaseMiscUtil.isRTLDirection()) {
                initPopupWindow(this.editTextRl, this.SELECTED_PX_OR_MM, -dimensionPixelSize2, -dimensionPixelSize);
            } else {
                initPopupWindow(this.editTextRl, this.SELECTED_PX_OR_MM, dimensionPixelSize2, -dimensionPixelSize);
            }
        } else if (view.getId() != R$id.pop_h_ll) {
        } else {
            int dimensionPixelSize3 = getResources().getDimensionPixelSize(R$dimen.pop_w_height);
            int dimensionPixelSize4 = getResources().getDimensionPixelSize(R$dimen.magic_w_width);
            if (BaseMiscUtil.isRTLDirection()) {
                initPopupWindow(this.editTextRlH, this.SELECTED_PX_OR_MM, -dimensionPixelSize4, -dimensionPixelSize3);
            } else {
                initPopupWindow(this.editTextRlH, this.SELECTED_PX_OR_MM, dimensionPixelSize4, -dimensionPixelSize3);
            }
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        bgAlpha(this.alphaAll);
        this.popWidth.setEnabled(true);
        this.popHeight.setEnabled(true);
    }

    public static void hintKbTwo(EditText editText, Context context) {
        ((InputMethodManager) context.getSystemService("input_method")).hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static int getInt(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        return Integer.valueOf(str).intValue();
    }

    public final void initPopupWindow(View view, int i, int i2, int i3) {
        View inflate = LayoutInflater.from(getContext()).inflate(R$layout.ts_pop_edit_dialog, (ViewGroup) null);
        this.mContentView = inflate;
        this.rlPx = (RelativeLayout) inflate.findViewById(R$id.rl_pop_item_px);
        this.rlMM = (RelativeLayout) this.mContentView.findViewById(R$id.rl_pop_item_mm);
        Log.e("DBCXXX--initPopupWindow", i + "");
        PopupWindow popupWindow = new PopupWindow(this.mContentView, getResources().getDimensionPixelSize(R$dimen.magic_idp_pop_width), getResources().getDimensionPixelSize(R$dimen.magic_w_width_pop_height), false);
        this.mIndicator = popupWindow;
        popupWindow.setAnimationStyle(R$style.magic_seek_bar_bubble_animation);
        this.mIndicator.setOutsideTouchable(true);
        this.mIndicator.showAsDropDown(view, i2, i3);
        if (i == 0) {
            this.rlPx.setSelected(true);
            this.rlMM.setSelected(false);
        } else {
            this.rlMM.setSelected(true);
            this.rlPx.setSelected(false);
        }
        this.rlPx.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.magic.util.CaptionEditorDialogFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                CaptionEditorDialogFragment.this.mIndicator.dismiss();
                CaptionEditorDialogFragment.this.clickPopDealData(0);
                CaptionEditorDialogFragment.this.popWidth.setEnabled(true);
                CaptionEditorDialogFragment.this.popHeight.setEnabled(true);
            }
        });
        this.rlMM.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.magic.util.CaptionEditorDialogFragment.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                CaptionEditorDialogFragment.this.mIndicator.dismiss();
                CaptionEditorDialogFragment.this.clickPopDealData(1);
                CaptionEditorDialogFragment.this.popWidth.setEnabled(true);
                CaptionEditorDialogFragment.this.popHeight.setEnabled(true);
            }
        });
    }

    public void clickPopDealData(int i) {
        if (getContext() != null) {
            String str = i == 0 ? "px" : "mm";
            this.popHText.setText(str);
            this.popWText.setText(str);
            String[] stringArray = getContext().getResources().getStringArray(i == 0 ? R$array.magic_id_photo_edit_hint_px_array : R$array.magic_id_photo_edit_hint_mm_array);
            this.SELECTED_PX_OR_MM = i;
            if (i == 0) {
                if (TextUtils.isEmpty(this.editText_w_px)) {
                    this.w.setText("");
                    this.w.setHint(stringArray[0]);
                } else {
                    this.w.setText(this.editText_w_px);
                }
                if (TextUtils.isEmpty(this.editText_h_px)) {
                    this.h.setText("");
                    this.h.setHint(stringArray[0]);
                } else {
                    this.h.setText(this.editText_h_px);
                }
            }
            if (i == 1) {
                if (TextUtils.isEmpty(this.editText_w_mm)) {
                    this.w.setText("");
                    this.w.setHint(stringArray[0]);
                } else {
                    this.w.setText(this.editText_w_mm);
                }
                if (TextUtils.isEmpty(this.editText_h_mm)) {
                    this.h.setText("");
                    this.h.setHint(stringArray[0]);
                } else {
                    this.h.setText(this.editText_h_mm);
                }
            }
            this.w.setHint(stringArray[0]);
            this.h.setHint(stringArray[0]);
        }
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        bgAlpha(this.alphaAll);
    }

    /* loaded from: classes2.dex */
    public class wTextWatcher implements TextWatcher {
        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public wTextWatcher() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            if (CaptionEditorDialogFragment.this.errorText.getVisibility() == 0) {
                CaptionEditorDialogFragment.this.errorText.setVisibility(8);
            }
            CaptionEditorDialogFragment.this.editTextRl.setBackground(CaptionEditorDialogFragment.this.mWDrawable);
            CaptionEditorDialogFragment.this.editTextRlH.setBackground(CaptionEditorDialogFragment.this.mHDrawable);
            if (CaptionEditorDialogFragment.this.SELECTED_PX_OR_MM == 0) {
                CaptionEditorDialogFragment captionEditorDialogFragment = CaptionEditorDialogFragment.this;
                captionEditorDialogFragment.editText_w_px = captionEditorDialogFragment.w.getText().toString();
            } else {
                CaptionEditorDialogFragment captionEditorDialogFragment2 = CaptionEditorDialogFragment.this;
                captionEditorDialogFragment2.editText_w_mm = captionEditorDialogFragment2.w.getText().toString();
            }
            if (editable == null) {
                CaptionEditorDialogFragment.this.isWidthEditext = false;
                CaptionEditorDialogFragment.this.isHaveTextColorAndOkBg(false);
            } else if (editable.length() > 0) {
                CaptionEditorDialogFragment.this.isWidthEditext = true;
                if (CaptionEditorDialogFragment.this.isHeightEditext) {
                    CaptionEditorDialogFragment.this.isHaveTextColorAndOkBg(true);
                } else {
                    CaptionEditorDialogFragment.this.isHaveTextColorAndOkBg(false);
                }
            } else {
                CaptionEditorDialogFragment.this.isWidthEditext = false;
                CaptionEditorDialogFragment.this.isHaveTextColorAndOkBg(false);
            }
        }
    }

    /* loaded from: classes2.dex */
    public class HTextWatcher implements TextWatcher {
        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public HTextWatcher() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            if (CaptionEditorDialogFragment.this.errorText.getVisibility() == 0) {
                CaptionEditorDialogFragment.this.errorText.setVisibility(8);
            }
            CaptionEditorDialogFragment.this.editTextRlH.setBackground(CaptionEditorDialogFragment.this.mHDrawable);
            CaptionEditorDialogFragment.this.editTextRl.setBackground(CaptionEditorDialogFragment.this.mWDrawable);
            if (CaptionEditorDialogFragment.this.SELECTED_PX_OR_MM == 0) {
                CaptionEditorDialogFragment.this.editText_h_px = editable.toString();
            } else {
                CaptionEditorDialogFragment.this.editText_h_mm = editable.toString();
            }
            if (editable == null) {
                CaptionEditorDialogFragment.this.isHeightEditext = false;
                CaptionEditorDialogFragment.this.isHaveTextColorAndOkBg(false);
            } else if (editable.length() > 0) {
                CaptionEditorDialogFragment.this.isHeightEditext = true;
                if (CaptionEditorDialogFragment.this.isWidthEditext) {
                    CaptionEditorDialogFragment.this.isHaveTextColorAndOkBg(true);
                } else {
                    CaptionEditorDialogFragment.this.isHaveTextColorAndOkBg(false);
                }
            } else {
                CaptionEditorDialogFragment.this.isHeightEditext = false;
                CaptionEditorDialogFragment.this.isHaveTextColorAndOkBg(false);
            }
        }
    }

    public void isHaveTextColorAndOkBg(boolean z) {
        if (z) {
            this.mOk.setEnabled(z);
            this.tvOk.setTextColor(getContext().getColor(R$color.dialog_zidingyi_white));
            return;
        }
        this.mOk.setEnabled(z);
        this.tvOk.setTextColor(getContext().getColor(R$color.dialog_zidingyi_textcolor_30));
    }

    public final void bgAlpha(float f) {
        WindowManager.LayoutParams attributes = getActivity().getWindow().getAttributes();
        attributes.alpha = f;
        getActivity().getWindow().setAttributes(attributes);
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
}
