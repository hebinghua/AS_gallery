package com.miui.gallery.editor.photo.core.imports.text.editdialog;

import android.animation.LayoutTransition;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.baidu.platform.comapi.UIMsg;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.compat.app.ActivityCompat;
import com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog;
import com.miui.gallery.editor.photo.core.imports.text.model.DialogStatusData;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.GalleryDialogFragment;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class TextEditDialogOld extends GalleryDialogFragment {
    public TextEditDialog.ConfigChangeListener mConfigChangeListener;
    public FrameLayout mContainerView;
    public EditText mEditText;
    public RelativeLayout mEditView;
    public DialogStatusData mInitializeData;
    public Boolean mIsInMultiWindowMode;
    public LinearLayout mNavigationContainer;
    public TextEditDialog.StatusListener mStatusListener;
    public FrameLayout mTabContainer;
    public TextWatcher mTextWatcher;
    public ViewGroup mWholeView;
    public String mWillEditText;
    public boolean mWillSelection = false;
    public int mScreenHeight = 0;
    public Tab mCurrentTab = Tab.KEYBOARD;
    public List<DialogSubMenuOld> mDialogSubMenuList = new ArrayList();
    public int mCurrentHeight = 0;
    public int mKeyBoardHeight = 0;
    public View.OnClickListener mNavigationClickListener = new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialogOld.3
        {
            TextEditDialogOld.this = this;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            int ordinal = TextEditDialogOld.this.mCurrentTab.ordinal();
            for (int i = 0; i < TextEditDialogOld.this.mDialogSubMenuList.size(); i++) {
                DialogSubMenuOld dialogSubMenuOld = (DialogSubMenuOld) TextEditDialogOld.this.mDialogSubMenuList.get(i);
                if (view != dialogSubMenuOld.getNavigationButton()) {
                    dialogSubMenuOld.setChecked(false);
                } else if (ordinal != i) {
                    dialogSubMenuOld.setChecked(true);
                    if (i == 0) {
                        TextEditDialogOld.this.showOrHideKeyboard();
                    } else {
                        if (TextEditDialogOld.this.mCurrentTab == Tab.KEYBOARD) {
                            TextEditDialogOld.this.mCurrentTab = Tab.values()[i];
                            TextEditDialogOld.this.hideKeyboard();
                        }
                        ViewGroup subMenuView = dialogSubMenuOld.getSubMenuView();
                        TextEditDialogOld.this.mTabContainer.removeAllViews();
                        TextEditDialogOld.this.mTabContainer.addView(subMenuView, new FrameLayout.LayoutParams(-1, -1));
                    }
                    TextEditDialogOld.this.mCurrentTab = Tab.values()[i];
                    DefaultLogger.d("TextEditDialog", "current click index : %d", Integer.valueOf(i));
                }
            }
        }
    };

    /* renamed from: com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialogOld$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements Runnable {
    }

    public static /* synthetic */ void $r8$lambda$0GxB4QoZAqx1ZOWOtbVrgDH_Z6M(TextEditDialogOld textEditDialogOld, View view) {
        textEditDialogOld.lambda$onCreateView$0(view);
    }

    public static /* synthetic */ void $r8$lambda$AkufhFHzrtAWJjyNBhaXt7gDVvo(TextEditDialogOld textEditDialogOld, View view) {
        textEditDialogOld.lambda$initView$2(view);
    }

    /* renamed from: $r8$lambda$C3btrwGk6OIQVEQ9cj-Tn6G0OcQ */
    public static /* synthetic */ void m891$r8$lambda$C3btrwGk6OIQVEQ9cjTn6G0OcQ(TextEditDialogOld textEditDialogOld, View view) {
        textEditDialogOld.lambda$initView$1(view);
    }

    /* renamed from: $r8$lambda$KsHYG7MUuqqlJPykX-JcZa8tTnM */
    public static /* synthetic */ void m892$r8$lambda$KsHYG7MUuqqlJPykXJcZa8tTnM(TextEditDialogOld textEditDialogOld, DialogInterface dialogInterface) {
        textEditDialogOld.lambda$onCreateDialog$3(dialogInterface);
    }

    /* renamed from: com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialogOld$4 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass4 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$editor$photo$core$imports$text$editdialog$TextEditDialogOld$Tab;

        static {
            int[] iArr = new int[Tab.values().length];
            $SwitchMap$com$miui$gallery$editor$photo$core$imports$text$editdialog$TextEditDialogOld$Tab = iArr;
            try {
                iArr[Tab.KEYBOARD.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$text$editdialog$TextEditDialogOld$Tab[Tab.STYLE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$text$editdialog$TextEditDialogOld$Tab[Tab.FONT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    /* loaded from: classes2.dex */
    public enum Tab {
        KEYBOARD,
        STYLE,
        FONT;

        public DialogSubMenuOld getSubMenu(Context context, TextEditDialog.ConfigChangeListener configChangeListener, ViewGroup viewGroup) {
            DialogSubMenuOld dialogStyleMenuOld;
            int i = AnonymousClass4.$SwitchMap$com$miui$gallery$editor$photo$core$imports$text$editdialog$TextEditDialogOld$Tab[ordinal()];
            if (i != 1) {
                if (i == 2) {
                    dialogStyleMenuOld = new DialogStyleMenuOld(context, viewGroup, configChangeListener);
                } else if (i != 3) {
                    return null;
                } else {
                    dialogStyleMenuOld = new DialogFontMenuOld(context, viewGroup, configChangeListener);
                }
                return dialogStyleMenuOld;
            }
            return new DialogKeyboardMenuOld(context);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        FrameLayout frameLayout = new FrameLayout(getActivity());
        this.mContainerView = frameLayout;
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        this.mContainerView.getViewTreeObserver().addOnGlobalLayoutListener(new LayoutListener(this, null));
        this.mIsInMultiWindowMode = Boolean.valueOf(ActivityCompat.isInMultiWindowMode(getActivity()));
        if (this.mWholeView == null) {
            initView(layoutInflater);
        }
        this.mContainerView.addView(this.mWholeView);
        if (Build.DEVICE.equals("cetus") || this.mIsInMultiWindowMode.booleanValue()) {
            ViewGroup.LayoutParams layoutParams = this.mWholeView.getLayoutParams();
            layoutParams.height = -1;
            this.mWholeView.setLayoutParams(layoutParams);
            this.mWholeView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialogOld$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    TextEditDialogOld.$r8$lambda$0GxB4QoZAqx1ZOWOtbVrgDH_Z6M(TextEditDialogOld.this, view);
                }
            });
        }
        return this.mContainerView;
    }

    public /* synthetic */ void lambda$onCreateView$0(View view) {
        dismissSafely();
    }

    public final void initView(LayoutInflater layoutInflater) {
        Tab[] values;
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.text_edit_text_dialog_old, (ViewGroup) null);
        this.mWholeView = viewGroup;
        this.mNavigationContainer = (LinearLayout) viewGroup.findViewById(R.id.text_append_edit_text_tab_group);
        this.mTabContainer = (FrameLayout) this.mWholeView.findViewById(R.id.text_append_edit_text_tab_container);
        this.mEditView = (RelativeLayout) this.mWholeView.findViewById(R.id.text_append_edit_text_layout);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2);
        layoutParams.weight = 1.0f;
        for (Tab tab : Tab.values()) {
            DialogSubMenuOld subMenu = tab.getSubMenu(getActivity(), this.mConfigChangeListener, this.mTabContainer);
            if (tab.ordinal() == 0) {
                subMenu.setChecked(true);
            }
            View navigationButton = subMenu.getNavigationButton();
            navigationButton.setOnClickListener(this.mNavigationClickListener);
            this.mNavigationContainer.addView(navigationButton, layoutParams);
            this.mDialogSubMenuList.add(subMenu);
        }
        this.mEditText = (EditText) this.mWholeView.findViewById(R.id.text_append_edit_text);
        this.mWholeView.findViewById(R.id.text_append_edit_text_delete).setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialogOld$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TextEditDialogOld.m891$r8$lambda$C3btrwGk6OIQVEQ9cjTn6G0OcQ(TextEditDialogOld.this, view);
            }
        });
        this.mWholeView.findViewById(R.id.text_append_edit_text_submit).setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialogOld$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TextEditDialogOld.$r8$lambda$AkufhFHzrtAWJjyNBhaXt7gDVvo(TextEditDialogOld.this, view);
            }
        });
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.enableTransitionType(4);
        this.mWholeView.setLayoutTransition(layoutTransition);
    }

    public /* synthetic */ void lambda$initView$1(View view) {
        this.mEditText.setText("");
    }

    public /* synthetic */ void lambda$initView$2(View view) {
        dismissSafely();
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        Dialog dialog = new Dialog(getActivity(), 2131952154);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialogOld$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnShowListener
            public final void onShow(DialogInterface dialogInterface) {
                TextEditDialogOld.m892$r8$lambda$KsHYG7MUuqqlJPykXJcZa8tTnM(TextEditDialogOld.this, dialogInterface);
            }
        });
        Window window = dialog.getWindow();
        if (window != null) {
            Point point = new Point();
            window.getWindowManager().getDefaultDisplay().getSize(point);
            this.mScreenHeight = point.y;
            window.getAttributes().windowAnimations = 2131952154;
        }
        return dialog;
    }

    public /* synthetic */ void lambda$onCreateDialog$3(DialogInterface dialogInterface) {
        DefaultLogger.d("TextEditDialog", "onShow");
        this.mEditText.setFocusable(true);
        this.mEditText.setFocusableInTouchMode(true);
        this.mEditText.requestFocus();
        if (this.mCurrentTab == Tab.KEYBOARD) {
            showOrHideKeyboard();
        }
        String str = this.mWillEditText;
        if (str != null) {
            this.mEditText.setText(str);
            if (this.mWillSelection) {
                this.mEditText.setSelection(0, this.mWillEditText.length());
            } else {
                this.mEditText.setSelection(this.mWillEditText.length());
            }
        }
        TextWatcher textWatcher = this.mTextWatcher;
        if (textWatcher != null) {
            this.mEditText.addTextChangedListener(textWatcher);
        }
        notifyShow();
        notifyHeightChange(this.mKeyBoardHeight);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setGravity(80);
            WindowManager.LayoutParams attributes = window.getAttributes();
            int i = -1;
            attributes.width = -1;
            if (!Build.DEVICE.equals("cetus")) {
                i = -2;
            }
            attributes.height = i;
            attributes.dimAmount = 0.0f;
            window.setAttributes(attributes);
            window.setBackgroundDrawable(new ColorDrawable(0));
        }
        Window window2 = getActivity().getWindow();
        if (window2 != null) {
            window2.setSoftInputMode(48);
        }
        this.mCurrentHeight = 0;
        DefaultLogger.d("TextEditDialog", "onStart");
        notifyKeyboardHeightChange();
        for (DialogSubMenuOld dialogSubMenuOld : this.mDialogSubMenuList) {
            dialogSubMenuOld.initializeData(this.mInitializeData);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        dismissSafely();
        super.onPause();
    }

    public void setTextWatch(TextWatcher textWatcher) {
        this.mTextWatcher = textWatcher;
    }

    public void setWillEditText(String str, boolean z) {
        this.mWillEditText = str;
        this.mWillSelection = z;
    }

    public void setConfigChangeListener(TextEditDialog.ConfigChangeListener configChangeListener) {
        this.mConfigChangeListener = configChangeListener;
    }

    public final void hideKeyboard() {
        if (!isAdded()) {
            return;
        }
        DefaultLogger.d("TextEditDialog", "hideKeyboard");
        ((InputMethodManager) getActivity().getSystemService("input_method")).hideSoftInputFromWindow(this.mEditText.getWindowToken(), 0);
    }

    public final void showOrHideKeyboard() {
        this.mEditText.postDelayed(new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialogOld.2
            {
                TextEditDialogOld.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (!TextEditDialogOld.this.isAdded()) {
                    return;
                }
                ((InputMethodManager) GalleryApp.sGetAndroidContext().getSystemService("input_method")).toggleSoftInput(0, 2);
            }
        }, 100L);
    }

    /* loaded from: classes2.dex */
    public class LayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        public Rect mRect;

        public LayoutListener() {
            TextEditDialogOld.this = r1;
            this.mRect = new Rect();
        }

        public /* synthetic */ LayoutListener(TextEditDialogOld textEditDialogOld, AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            Dialog dialog = TextEditDialogOld.this.getDialog();
            if (dialog == null || dialog.getWindow() == null) {
                return;
            }
            int heightDifference = TextEditDialogOld.getHeightDifference(TextEditDialogOld.this.mContainerView, TextEditDialogOld.this.mScreenHeight, this.mRect);
            DefaultLogger.d("TextEditDialog", "heightDifference : %d:%d:%d", Integer.valueOf(heightDifference), Integer.valueOf(TextEditDialogOld.this.mKeyBoardHeight), Integer.valueOf(TextEditDialogOld.this.mScreenHeight));
            if (heightDifference != 0) {
                if (TextEditDialogOld.this.mKeyBoardHeight < heightDifference) {
                    TextEditDialogOld.this.mKeyBoardHeight = heightDifference;
                }
                if (heightDifference <= 0) {
                    TextEditDialogOld.this.mKeyBoardHeight = 0;
                } else {
                    TextEditDialogOld.this.mKeyBoardHeight = Math.max(heightDifference, (int) ScreenUtils.dpToPixel(200.0f));
                }
                if (TextEditDialogOld.this.mCurrentHeight <= 0 && TextEditDialogOld.this.mCurrentHeight != heightDifference) {
                    TextEditDialogOld.this.mCurrentHeight = heightDifference;
                    TextEditDialogOld.this.checkMenuIndex(Tab.KEYBOARD.ordinal());
                }
                TextEditDialogOld.this.notifyKeyboardHeightChange();
            } else {
                if (TextEditDialogOld.this.mCurrentHeight > 0 && TextEditDialogOld.this.mCurrentTab == Tab.KEYBOARD) {
                    DefaultLogger.d("TextEditDialog", "onGlobalLayout dismiss");
                    TextEditDialogOld.this.dismissSafely();
                }
                TextEditDialogOld.this.mCurrentHeight = heightDifference;
            }
            TextEditDialogOld textEditDialogOld = TextEditDialogOld.this;
            textEditDialogOld.notifyHeightChange(textEditDialogOld.mKeyBoardHeight);
            DefaultLogger.d("TextEditDialog", "onGlobalLayout mCurrentHeight %d", Integer.valueOf(TextEditDialogOld.this.mCurrentHeight));
        }
    }

    public static int getHeightDifference(View view, int i, Rect rect) {
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        view.getWindowVisibleDisplayFrame(rect);
        return (i - rect.bottom) - ((i - iArr[1]) - view.getHeight());
    }

    public final void checkMenuIndex(int i) {
        if (i <= 0 || i >= this.mDialogSubMenuList.size()) {
            this.mNavigationClickListener.onClick(this.mDialogSubMenuList.get(i).getNavigationButton());
        }
    }

    public final void notifyKeyboardHeightChange() {
        if (this.mKeyBoardHeight == 0) {
            if (!Build.DEVICE.equals("cetus") && !this.mIsInMultiWindowMode.booleanValue()) {
                return;
            }
            this.mTabContainer.setVisibility(0);
            this.mTabContainer.setLayoutParams(new LinearLayout.LayoutParams(-1, (int) UIMsg.MSG_MAP_PANO_DATA));
        } else if (this.mTabContainer.getVisibility() == 0 && this.mTabContainer.getHeight() == this.mKeyBoardHeight) {
        } else {
            this.mTabContainer.setVisibility(0);
            this.mTabContainer.setLayoutParams(new LinearLayout.LayoutParams(-1, this.mKeyBoardHeight));
        }
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        for (DialogSubMenuOld dialogSubMenuOld : this.mDialogSubMenuList) {
            if (dialogSubMenuOld != null) {
                dialogSubMenuOld.release();
            }
        }
        this.mContainerView.removeAllViews();
        super.onDestroyView();
    }

    @Override // com.miui.gallery.widget.GalleryDialogFragment, androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        notifyHeightChange(0);
        notifyDismiss();
        TextWatcher textWatcher = this.mTextWatcher;
        if (textWatcher != null) {
            this.mEditText.removeTextChangedListener(textWatcher);
        }
    }

    public void setInitializeData(DialogStatusData dialogStatusData) {
        this.mInitializeData = dialogStatusData;
    }

    public final void notifyShow() {
        TextEditDialog.StatusListener statusListener = this.mStatusListener;
        if (statusListener != null) {
            statusListener.onShow();
        }
    }

    public final void notifyDismiss() {
        TextEditDialog.StatusListener statusListener = this.mStatusListener;
        if (statusListener != null) {
            statusListener.onDismiss();
        }
    }

    public final void notifyHeightChange(int i) {
        if (this.mStatusListener != null) {
            int height = this.mNavigationContainer.getHeight() + ((LinearLayout.LayoutParams) this.mEditView.getLayoutParams()).topMargin + this.mEditView.getHeight();
            int screenHeight = ScreenUtils.getScreenHeight();
            this.mScreenHeight = screenHeight;
            int i2 = (screenHeight - i) - height;
            DefaultLogger.d("TextEditDialog", "navigation: %d height:%d,bottom:%d,%d", Integer.valueOf(height), Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(this.mScreenHeight));
            if (i == 0) {
                this.mStatusListener.onBottomChange(this.mScreenHeight);
                DefaultLogger.d("TextEditDialog", "notifyHeightChange: %d", Integer.valueOf(this.mScreenHeight));
                return;
            }
            this.mStatusListener.onBottomChange(i2);
            DefaultLogger.d("TextEditDialog", "notifyHeightChange: %d", Integer.valueOf(i2));
        }
    }

    public void setStatusListener(TextEditDialog.StatusListener statusListener) {
        this.mStatusListener = statusListener;
    }

    public boolean isShowing() {
        Dialog dialog = getDialog();
        return dialog != null && dialog.isShowing();
    }
}
