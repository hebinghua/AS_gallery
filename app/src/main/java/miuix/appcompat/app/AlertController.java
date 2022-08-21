package miuix.appcompat.app;

import android.app.Activity;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Insets;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsAnimation;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.widget.NestedScrollView;
import java.lang.ref.WeakReference;
import java.util.List;
import miuix.androidbasewidget.widget.SingleCenterTextView;
import miuix.animation.Folme;
import miuix.appcompat.R$attr;
import miuix.appcompat.R$bool;
import miuix.appcompat.R$color;
import miuix.appcompat.R$dimen;
import miuix.appcompat.R$id;
import miuix.appcompat.R$styleable;
import miuix.appcompat.app.AlertController;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.internal.util.EasyModeHelper;
import miuix.appcompat.internal.widget.DialogButtonPanel;
import miuix.appcompat.widget.DialogAnimHelper;
import miuix.core.util.MiuixUIUtils;
import miuix.internal.util.AnimHelper;
import miuix.internal.util.AttributeResolver;
import miuix.internal.util.DeviceHelper;
import miuix.internal.util.ReflectUtil;
import miuix.internal.widget.DialogParentPanel;
import miuix.internal.widget.GroupButton;
import miuix.view.CompatViewMethod;
import miuix.view.HapticCompat;
import miuix.view.HapticFeedbackConstants;

/* loaded from: classes3.dex */
public class AlertController {
    public ListAdapter mAdapter;
    public int mAlertDialogLayout;
    public Button mButtonNegative;
    public Message mButtonNegativeMessage;
    public CharSequence mButtonNegativeText;
    public Button mButtonNeutral;
    public Message mButtonNeutralMessage;
    public CharSequence mButtonNeutralText;
    public Button mButtonPositive;
    public Message mButtonPositiveMessage;
    public CharSequence mButtonPositiveText;
    public CharSequence mCheckBoxMessage;
    public CustomComponentCallbacks mComponentCallback;
    public final Context mContext;
    public Thread mCreateThread;
    public View mCustomTitleView;
    public final AppCompatDialog mDialog;
    public View mDialogRootView;
    public View mDimBg;
    public int mFakeLandScreenMinorSize;
    public Handler mHandler;
    public boolean mHapticFeedbackEnabled;
    public Drawable mIcon;
    public boolean mIsChecked;
    public LayoutChangeListener mLayoutChangeListener;
    public int mListItemLayout;
    public int mListLayout;
    public ListView mListView;
    public CharSequence mMessage;
    public TextView mMessageView;
    public int mMultiChoiceItemLayout;
    public int mPanelAndImeMargin;
    public View mParentPanel;
    public int mParentPanelOriBottom;
    public int mParentPanelOriEnd;
    public int mParentPanelOriStart;
    public int mParentPanelOriTop;
    public Drawable mRectanglePanelBg;
    public Drawable mRoundPanelBg;
    public int mScreenHeightDp;
    public int mScreenLayout;
    public int mScreenMinorSize;
    public int mScreenOrientation;
    public Point mScreenRealSize;
    public int mScreenWidthDp;
    public NestedScrollView mScrollView;
    public AlertDialog.OnDialogShowAnimListener mShowAnimListener;
    public int mSingleChoiceItemLayout;
    public CharSequence mTitle;
    public TextView mTitleView;
    public boolean mTreatAsLandConfig;
    public View mView;
    public int mViewLayoutResId;
    public final Window mWindow;
    public WindowManager mWindowManager;
    public int mIconId = 0;
    public int mCheckedItem = -1;
    public boolean mCancelable = true;
    public boolean mCanceledOnTouchOutside = true;
    public boolean mIsEnableImmersive = true;
    public final View.OnClickListener mButtonHandler = new View.OnClickListener() { // from class: miuix.appcompat.app.AlertController.1
        {
            AlertController.this = this;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Message obtain;
            Message message;
            Message message2;
            Message message3;
            int i = HapticFeedbackConstants.MIUI_TAP_LIGHT;
            AlertController alertController = AlertController.this;
            if (view == alertController.mButtonPositive && (message3 = alertController.mButtonPositiveMessage) != null) {
                obtain = Message.obtain(message3);
                i = HapticFeedbackConstants.MIUI_TAP_NORMAL;
            } else if (view == alertController.mButtonNegative && (message2 = alertController.mButtonNegativeMessage) != null) {
                obtain = Message.obtain(message2);
            } else {
                obtain = (view != alertController.mButtonNeutral || (message = alertController.mButtonNeutralMessage) == null) ? null : Message.obtain(message);
            }
            HapticCompat.performHapticFeedback(view, i);
            if (obtain != null) {
                obtain.sendToTarget();
            }
            AlertController alertController2 = AlertController.this;
            alertController2.mHandler.obtainMessage(1, alertController2.mDialog).sendToTarget();
        }
    };
    public boolean mInsetsAnimationPlayed = false;

    public static /* synthetic */ void $r8$lambda$QUWCJ1st1jk2uhPZfesY4Q84TNY(AlertController alertController, View view) {
        alertController.lambda$setupView$0(view);
    }

    public final int parseScreenSize(int i) {
        return i & 15;
    }

    /* loaded from: classes3.dex */
    public static final class ButtonHandler extends Handler {
        private static final int MSG_DISMISS_DIALOG = 1;
        private WeakReference<DialogInterface> mDialog;

        public ButtonHandler(DialogInterface dialogInterface) {
            this.mDialog = new WeakReference<>(dialogInterface);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == -3 || i == -2 || i == -1) {
                ((DialogInterface.OnClickListener) message.obj).onClick(this.mDialog.get(), message.what);
            } else if (i != 1) {
            } else {
                ((DialogInterface) message.obj).dismiss();
            }
        }
    }

    public AlertController(Context context, AppCompatDialog appCompatDialog, Window window) {
        initScreenMinorSize(context);
        this.mContext = context;
        this.mDialog = appCompatDialog;
        this.mWindow = window;
        this.mHandler = new ButtonHandler(appCompatDialog);
        this.mComponentCallback = new CustomComponentCallbacks(this);
        this.mLayoutChangeListener = new LayoutChangeListener(this);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(null, R$styleable.AlertDialog, 16842845, 0);
        this.mAlertDialogLayout = obtainStyledAttributes.getResourceId(R$styleable.AlertDialog_layout, 0);
        this.mListLayout = obtainStyledAttributes.getResourceId(R$styleable.AlertDialog_listLayout, 0);
        this.mMultiChoiceItemLayout = obtainStyledAttributes.getResourceId(R$styleable.AlertDialog_multiChoiceItemLayout, 0);
        this.mSingleChoiceItemLayout = obtainStyledAttributes.getResourceId(R$styleable.AlertDialog_singleChoiceItemLayout, 0);
        this.mListItemLayout = obtainStyledAttributes.getResourceId(R$styleable.AlertDialog_listItemLayout, 0);
        obtainStyledAttributes.recycle();
        appCompatDialog.supportRequestWindowFeature(1);
        if (Build.VERSION.SDK_INT < 28 && isNotch()) {
            ReflectUtil.callObjectMethod(window, "addExtraFlags", new Class[]{Integer.TYPE}, 768);
        }
        this.mTreatAsLandConfig = context.getResources().getBoolean(R$bool.treat_as_land);
        this.mCreateThread = Thread.currentThread();
    }

    public final boolean isNotch() {
        Class<?> cls = ReflectUtil.getClass("android.os.SystemProperties");
        Class cls2 = Integer.TYPE;
        return ((Integer) ReflectUtil.callStaticObjectMethod(cls, cls2, "getInt", new Class[]{String.class, cls2}, "ro.miui.notch", 0)).intValue() == 1;
    }

    public static boolean canTextInput(View view) {
        if (view.onCheckIsTextEditor()) {
            return true;
        }
        if (!(view instanceof ViewGroup)) {
            return false;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount();
        while (childCount > 0) {
            childCount--;
            if (canTextInput(viewGroup.getChildAt(childCount))) {
                return true;
            }
        }
        return false;
    }

    public void installContent() {
        this.mDialog.setContentView(this.mAlertDialogLayout);
        setupWindow();
        setupView();
        checkUpdatePanelBackground();
    }

    public final void checkUpdatePanelBackground() {
        int screenOrientation = getScreenOrientation();
        if (this.mContext.getResources().getConfiguration().orientation != screenOrientation || (DeviceHelper.isFoldDevice() && screenOrientation == 1)) {
            adjustDialogPanelBackground(screenOrientation);
        }
    }

    public void setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
        TextView textView = this.mTitleView;
        if (textView != null) {
            textView.setText(charSequence);
        }
    }

    public void setCustomTitle(View view) {
        this.mCustomTitleView = view;
    }

    public void setMessage(CharSequence charSequence) {
        this.mMessage = charSequence;
        TextView textView = this.mMessageView;
        if (textView != null) {
            textView.setText(charSequence);
            centerIfSingleLine(this.mMessageView, charSequence);
        }
    }

    public TextView getMessageView() {
        return this.mMessageView;
    }

    public void setView(int i) {
        this.mView = null;
        this.mViewLayoutResId = i;
    }

    public void setView(View view) {
        this.mView = view;
        this.mViewLayoutResId = 0;
    }

    public void setButton(int i, CharSequence charSequence, DialogInterface.OnClickListener onClickListener, Message message) {
        if (message == null && onClickListener != null) {
            message = this.mHandler.obtainMessage(i, onClickListener);
        }
        if (i == -3) {
            this.mButtonNeutralText = charSequence;
            this.mButtonNeutralMessage = message;
        } else if (i == -2) {
            this.mButtonNegativeText = charSequence;
            this.mButtonNegativeMessage = message;
        } else if (i == -1) {
            this.mButtonPositiveText = charSequence;
            this.mButtonPositiveMessage = message;
        } else {
            throw new IllegalArgumentException("Button does not exist");
        }
    }

    public void setIcon(int i) {
        this.mIcon = null;
        this.mIconId = i;
    }

    public void setIcon(Drawable drawable) {
        this.mIcon = drawable;
        this.mIconId = 0;
    }

    public int getIconAttributeResId(int i) {
        TypedValue typedValue = new TypedValue();
        this.mContext.getTheme().resolveAttribute(i, typedValue, true);
        return typedValue.resourceId;
    }

    public ListView getListView() {
        return this.mListView;
    }

    public Button getButton(int i) {
        if (i != -3) {
            if (i == -2) {
                return this.mButtonNegative;
            }
            if (i == -1) {
                return this.mButtonPositive;
            }
            return null;
        }
        return this.mButtonNeutral;
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return keyEvent.getKeyCode() == 82;
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        NestedScrollView nestedScrollView = this.mScrollView;
        return nestedScrollView != null && nestedScrollView.executeKeyEvent(keyEvent);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        NestedScrollView nestedScrollView = this.mScrollView;
        return nestedScrollView != null && nestedScrollView.executeKeyEvent(keyEvent);
    }

    public void setCancelable(boolean z) {
        this.mCancelable = z;
    }

    public void setCanceledOnTouchOutside(boolean z) {
        this.mCanceledOnTouchOutside = z;
    }

    public final boolean isCancelable() {
        return this.mCancelable;
    }

    public final boolean isCanceledOnTouchOutside() {
        return this.mCanceledOnTouchOutside;
    }

    public final void hideSoftIME() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.mContext.getSystemService(InputMethodManager.class);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(this.mParentPanel.getWindowToken(), 0);
        }
    }

    public final void setupView() {
        ListAdapter listAdapter;
        this.mDialogRootView = this.mWindow.findViewById(R$id.dialog_root_view);
        this.mParentPanel = this.mWindow.findViewById(R$id.parentPanel);
        this.mDimBg = this.mWindow.findViewById(R$id.dialog_dim_bg);
        if (isDialogImmersive()) {
            this.mDimBg.setOnClickListener(new View.OnClickListener() { // from class: miuix.appcompat.app.AlertController$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    AlertController.$r8$lambda$QUWCJ1st1jk2uhPZfesY4Q84TNY(AlertController.this, view);
                }
            });
            this.mParentPanelOriStart = this.mParentPanel.getPaddingStart();
            this.mParentPanelOriEnd = this.mParentPanel.getPaddingEnd();
            this.mParentPanelOriTop = this.mParentPanel.getPaddingTop();
            this.mParentPanelOriBottom = this.mParentPanel.getPaddingBottom();
            adaptPaddingBottom();
            setupDialogPanel();
        } else {
            this.mDimBg.setVisibility(8);
        }
        ViewGroup viewGroup = (ViewGroup) this.mParentPanel.findViewById(R$id.topPanel);
        ViewGroup viewGroup2 = (ViewGroup) this.mParentPanel.findViewById(R$id.contentPanel);
        ViewGroup viewGroup3 = (ViewGroup) this.mParentPanel.findViewById(R$id.buttonPanel);
        ViewGroup viewGroup4 = (ViewGroup) this.mParentPanel.findViewById(R$id.customPanel);
        if (viewGroup4 != null) {
            setupCustomContent(viewGroup4);
        }
        if (viewGroup2 != null) {
            setupContent(viewGroup2);
        }
        if (viewGroup3 != null) {
            setupButtons(viewGroup3);
        }
        if (viewGroup != null) {
            setupTitle(viewGroup);
        }
        boolean z = (viewGroup == null || viewGroup.getVisibility() == 8) ? false : true;
        if (viewGroup3 != null) {
            viewGroup3.getVisibility();
        }
        if (z) {
            NestedScrollView nestedScrollView = this.mScrollView;
            if (nestedScrollView != null) {
                nestedScrollView.setClipToPadding(true);
            }
            View view = null;
            if (this.mMessage != null || this.mListView != null) {
                view = viewGroup.findViewById(R$id.titleDividerNoCustom);
            }
            if (view != null) {
                view.setVisibility(0);
            }
            if (this.mListView != null && viewGroup2 != null) {
                viewGroup2.setPadding(viewGroup2.getPaddingLeft(), 0, viewGroup2.getPaddingRight(), viewGroup2.getPaddingBottom());
            }
        }
        ListView listView = this.mListView;
        if (listView != null && (listAdapter = this.mAdapter) != null) {
            listView.setAdapter(listAdapter);
            int i = this.mCheckedItem;
            if (i > -1) {
                listView.setItemChecked(i, true);
                listView.setSelection(i);
            }
        }
        CheckBox checkBox = (CheckBox) this.mParentPanel.findViewById(16908289);
        if (checkBox != null) {
            setupCheckbox(checkBox);
        }
        handMarginBetweenBtnAndContent();
        setupWindowInsetsAnimation();
    }

    public /* synthetic */ void lambda$setupView$0(View view) {
        if (!isCancelable() || !isCanceledOnTouchOutside()) {
            return;
        }
        hideSoftIME();
        this.mDialog.cancel();
    }

    public final void setupCustomContent(ViewGroup viewGroup) {
        View view = this.mView;
        boolean z = false;
        if (view == null) {
            view = this.mViewLayoutResId != 0 ? LayoutInflater.from(this.mContext).inflate(this.mViewLayoutResId, viewGroup, false) : null;
        }
        if (view != null) {
            z = true;
        }
        if (!z || !canTextInput(view)) {
            this.mWindow.setFlags(131072, 131072);
        }
        if (z) {
            ((FrameLayout) this.mWindow.findViewById(16908331)).addView(view, new ViewGroup.LayoutParams(-1, -1));
            if (this.mListView == null) {
                return;
            }
            ((LinearLayout.LayoutParams) viewGroup.getLayoutParams()).weight = 0.0f;
            return;
        }
        viewGroup.setVisibility(8);
    }

    public final void setupTitle(ViewGroup viewGroup) {
        if (this.mCustomTitleView != null) {
            viewGroup.addView(this.mCustomTitleView, 0, new ViewGroup.LayoutParams(-1, -2));
            this.mWindow.findViewById(R$id.alertTitle).setVisibility(8);
        } else if (!TextUtils.isEmpty(this.mTitle)) {
            TextView textView = (TextView) this.mWindow.findViewById(R$id.alertTitle);
            this.mTitleView = textView;
            textView.setText(this.mTitle);
            Drawable drawable = this.mIcon;
            if (drawable != null) {
                this.mTitleView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, (Drawable) null, (Drawable) null, (Drawable) null);
            }
            int i = this.mIconId;
            if (i != 0) {
                this.mTitleView.setCompoundDrawablesRelativeWithIntrinsicBounds(i, 0, 0, 0);
            }
            if (this.mMessage == null || viewGroup.getVisibility() == 8) {
                return;
            }
            changeTitlePadding(this.mTitleView);
        } else {
            this.mWindow.findViewById(R$id.alertTitle).setVisibility(8);
            viewGroup.setVisibility(8);
        }
    }

    public final void setupContent(ViewGroup viewGroup) {
        NestedScrollView nestedScrollView = (NestedScrollView) this.mWindow.findViewById(R$id.scrollView);
        this.mScrollView = nestedScrollView;
        nestedScrollView.setFocusable(false);
        this.mScrollView.setNestedScrollingEnabled(false);
        TextView textView = (TextView) viewGroup.findViewById(R$id.message);
        this.mMessageView = textView;
        if (textView == null) {
            return;
        }
        CharSequence charSequence = this.mMessage;
        if (charSequence != null) {
            textView.setText(charSequence);
            centerIfSingleLine(this.mMessageView, this.mMessage);
            return;
        }
        textView.setVisibility(8);
        this.mScrollView.removeView(this.mMessageView);
        if (this.mListView != null) {
            viewGroup.removeAllViews();
            viewGroup.addView(this.mListView, new ViewGroup.LayoutParams(-1, -1));
            return;
        }
        viewGroup.setVisibility(8);
    }

    public final void relayoutButtons(DialogButtonPanel dialogButtonPanel) {
        dialogButtonPanel.setOrientation(1);
        dialogButtonPanel.removeAllViews();
        Button button = this.mButtonPositive;
        if (button != null) {
            dialogButtonPanel.addView(button);
        }
        Button button2 = this.mButtonNeutral;
        if (button2 != null) {
            dialogButtonPanel.addView(button2);
        }
        Button button3 = this.mButtonNegative;
        if (button3 != null) {
            dialogButtonPanel.addView(button3);
        }
    }

    public final boolean isEllipsized(TextView textView, int i) {
        return ((int) textView.getPaint().measureText(textView.getText().toString())) > (i - textView.getPaddingStart()) - textView.getPaddingEnd();
    }

    public final void handMarginBetweenBtnAndContent() {
        View findViewById = this.mParentPanel.findViewById(R$id.buttonPanel);
        View findViewById2 = this.mParentPanel.findViewById(R$id.contentPanel);
        boolean z = findViewById != null && findViewById.getVisibility() == 0;
        if (findViewById2 == null || findViewById2.getVisibility() != 0) {
            return;
        }
        injectMargin((ViewGroup) findViewById2.findViewById(R$id.contentView), z);
    }

    public final void injectMargin(ViewGroup viewGroup, boolean z) {
        if (viewGroup == null) {
            return;
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) viewGroup.getLayoutParams();
        if (z) {
            marginLayoutParams.bottomMargin = this.mContext.getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_dialog_content_margin_bottom);
        } else {
            marginLayoutParams.bottomMargin = 0;
        }
    }

    public final void disableForceDark(View view) {
        CompatViewMethod.setForceDarkAllowed(view, false);
    }

    public final void setupButtons(ViewGroup viewGroup) {
        int i;
        Button button = (Button) viewGroup.findViewById(16908313);
        this.mButtonPositive = button;
        button.setOnClickListener(this.mButtonHandler);
        EasyModeHelper.updateTextViewSize(this.mButtonPositive);
        if (TextUtils.isEmpty(this.mButtonPositiveText)) {
            this.mButtonPositive.setVisibility(8);
            i = 0;
        } else {
            this.mButtonPositive.setText(this.mButtonPositiveText);
            this.mButtonPositive.setVisibility(0);
            disableForceDark(this.mButtonPositive);
            addPressAnimInternal(this.mButtonPositive);
            i = 1;
        }
        int i2 = i;
        Button button2 = (Button) viewGroup.findViewById(16908314);
        this.mButtonNegative = button2;
        button2.setOnClickListener(this.mButtonHandler);
        EasyModeHelper.updateTextViewSize(this.mButtonNegative);
        if (TextUtils.isEmpty(this.mButtonNegativeText)) {
            this.mButtonNegative.setVisibility(8);
        } else {
            this.mButtonNegative.setText(this.mButtonNegativeText);
            this.mButtonNegative.setVisibility(0);
            i |= 2;
            i2++;
            disableForceDark(this.mButtonNegative);
            addPressAnimInternal(this.mButtonNegative);
        }
        Button button3 = (Button) viewGroup.findViewById(16908315);
        this.mButtonNeutral = button3;
        button3.setOnClickListener(this.mButtonHandler);
        EasyModeHelper.updateTextViewSize(this.mButtonNeutral);
        if (TextUtils.isEmpty(this.mButtonNeutralText)) {
            this.mButtonNeutral.setVisibility(8);
        } else {
            this.mButtonNeutral.setText(this.mButtonNeutralText);
            this.mButtonNeutral.setVisibility(0);
            i |= 4;
            i2++;
            disableForceDark(this.mButtonNeutral);
            addPressAnimInternal(this.mButtonNeutral);
        }
        if (!(i != 0)) {
            viewGroup.setVisibility(8);
            return;
        }
        DialogButtonPanel dialogButtonPanel = (DialogButtonPanel) viewGroup.findViewById(R$id.buttonGroup);
        if (i2 > 2) {
            relayoutButtons(dialogButtonPanel);
        } else if (i2 == 1) {
            dialogButtonPanel.clearVisibleChildMargins();
        } else {
            int i3 = this.mParentPanel.getLayoutParams().width;
            if (i3 <= 0) {
                i3 = this.mContext.getResources().getDisplayMetrics().widthPixels;
            }
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) viewGroup.getLayoutParams();
            int marginStart = ((i3 - (marginLayoutParams.getMarginStart() + marginLayoutParams.getMarginEnd())) - this.mContext.getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_dialog_btn_margin_horizontal)) / 2;
            boolean z = false;
            for (int i4 = 0; i4 < dialogButtonPanel.getChildCount(); i4++) {
                TextView textView = (TextView) dialogButtonPanel.getChildAt(i4);
                if (textView.getVisibility() == 0) {
                    z = isEllipsized(textView, marginStart);
                }
                if (z) {
                    break;
                }
            }
            if (!z) {
                return;
            }
            relayoutButtons(dialogButtonPanel);
        }
    }

    public final void setupWindowInsetsAnimation() {
        if (Build.VERSION.SDK_INT < 30 || !isDialogImmersive()) {
            return;
        }
        if (this.mWindow.getAttributes().softInputMode == 32) {
            this.mWindow.setSoftInputMode(48);
        }
        this.mWindow.getDecorView().setWindowInsetsAnimationCallback(new WindowInsetsAnimation.Callback(1) { // from class: miuix.appcompat.app.AlertController.2
            {
                AlertController.this = this;
            }

            @Override // android.view.WindowInsetsAnimation.Callback
            public WindowInsets onProgress(WindowInsets windowInsets, List<WindowInsetsAnimation> list) {
                if (windowInsets.isVisible(WindowInsets.Type.ime())) {
                    int dialogPanelExtraBottomPadding = windowInsets.getInsets(WindowInsets.Type.ime()).bottom - (AlertController.this.mTreatAsLandConfig ? AlertController.this.mPanelAndImeMargin : AlertController.this.getDialogPanelExtraBottomPadding());
                    if (dialogPanelExtraBottomPadding < 0) {
                        dialogPanelExtraBottomPadding = 0;
                    }
                    AlertController.this.translateDialogPanel(-dialogPanelExtraBottomPadding);
                }
                return windowInsets;
            }

            @Override // android.view.WindowInsetsAnimation.Callback
            public WindowInsetsAnimation.Bounds onStart(WindowInsetsAnimation windowInsetsAnimation, WindowInsetsAnimation.Bounds bounds) {
                if (AlertController.this.mTreatAsLandConfig) {
                    AlertController alertController = AlertController.this;
                    alertController.mPanelAndImeMargin = (int) (alertController.getDialogPanelMargin() + AlertController.this.mParentPanel.getTranslationY());
                    if (AlertController.this.mPanelAndImeMargin <= 0) {
                        AlertController.this.mPanelAndImeMargin = 0;
                    }
                }
                return super.onStart(windowInsetsAnimation, bounds);
            }

            @Override // android.view.WindowInsetsAnimation.Callback
            public void onEnd(WindowInsetsAnimation windowInsetsAnimation) {
                super.onEnd(windowInsetsAnimation);
                AlertController.this.mInsetsAnimationPlayed = true;
                WindowInsets rootWindowInsets = AlertController.this.mWindow.getDecorView().getRootWindowInsets();
                if (rootWindowInsets == null || rootWindowInsets.getInsets(WindowInsets.Type.ime()).bottom > 0 || AlertController.this.mParentPanel.getTranslationY() >= 0.0f) {
                    return;
                }
                AlertController.this.translateDialogPanel(0);
            }

            @Override // android.view.WindowInsetsAnimation.Callback
            public void onPrepare(WindowInsetsAnimation windowInsetsAnimation) {
                super.onPrepare(windowInsetsAnimation);
                DialogAnimHelper.cancelAnimator();
                AlertController.this.mInsetsAnimationPlayed = false;
            }
        });
        this.mWindow.getDecorView().setOnApplyWindowInsetsListener(new AnonymousClass3());
    }

    /* renamed from: miuix.appcompat.app.AlertController$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 implements View.OnApplyWindowInsetsListener {
        /* renamed from: $r8$lambda$H0KWMhB12WdM-1eRVq52TIsmsgE */
        public static /* synthetic */ void m2590$r8$lambda$H0KWMhB12WdM1eRVq52TIsmsgE(AnonymousClass3 anonymousClass3, WindowInsets windowInsets) {
            anonymousClass3.lambda$onApplyWindowInsets$0(windowInsets);
        }

        public AnonymousClass3() {
            AlertController.this = r1;
        }

        public /* synthetic */ void lambda$onApplyWindowInsets$0(WindowInsets windowInsets) {
            AlertController.this.checkTranslateDialogPanel(windowInsets);
        }

        @Override // android.view.View.OnApplyWindowInsetsListener
        public WindowInsets onApplyWindowInsets(View view, final WindowInsets windowInsets) {
            view.post(new Runnable() { // from class: miuix.appcompat.app.AlertController$3$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AlertController.AnonymousClass3.m2590$r8$lambda$H0KWMhB12WdM1eRVq52TIsmsgE(AlertController.AnonymousClass3.this, windowInsets);
                }
            });
            return WindowInsets.CONSUMED;
        }
    }

    public final int getDialogPanelMargin() {
        int[] iArr = new int[2];
        this.mParentPanel.getLocationOnScreen(iArr);
        this.mWindowManager.getDefaultDisplay().getRealSize(this.mScreenRealSize);
        return this.mScreenRealSize.y - (iArr[1] + this.mParentPanel.getHeight());
    }

    public boolean isChecked() {
        boolean isChecked = ((CheckBox) this.mWindow.findViewById(16908289)).isChecked();
        this.mIsChecked = isChecked;
        return isChecked;
    }

    public void setCheckBox(boolean z, CharSequence charSequence) {
        this.mIsChecked = z;
        this.mCheckBoxMessage = charSequence;
    }

    public final void initScreenMinorSize(Context context) {
        this.mScreenRealSize = new Point();
        this.mWindowManager = (WindowManager) context.getSystemService("window");
        updateMinorScreenSize(context.getResources().getConfiguration());
        this.mFakeLandScreenMinorSize = context.getResources().getDimensionPixelSize(R$dimen.fake_landscape_screen_minor_size);
    }

    public final void updateMinorScreenSize(Configuration configuration) {
        int min = (int) (Math.min(configuration.screenWidthDp, configuration.screenHeightDp) * (configuration.densityDpi / 160.0f));
        if (min > 0) {
            this.mScreenMinorSize = min;
            return;
        }
        Point point = new Point();
        this.mWindowManager.getDefaultDisplay().getSize(point);
        this.mScreenMinorSize = Math.min(point.x, point.y);
    }

    public final void setupCheckbox(CheckBox checkBox) {
        if (this.mCheckBoxMessage != null) {
            checkBox.setVisibility(0);
            checkBox.setChecked(this.mIsChecked);
            checkBox.setText(this.mCheckBoxMessage);
            return;
        }
        checkBox.setVisibility(8);
    }

    public final void adaptPaddingBottom() {
        int navigationBarHeight;
        if (!isLandscape()) {
            if (MiuixUIUtils.isInMultiWindowMode(this.mContext)) {
                navigationBarHeight = this.mLayoutChangeListener.hasNavigationBarHeightInMultiWindowMode() ? MiuixUIUtils.getNavigationBarHeight(this.mContext) : 0;
            } else {
                navigationBarHeight = MiuixUIUtils.getNavigationBarHeight(this.mContext);
            }
            this.mParentPanel.setPaddingRelative(this.mParentPanelOriStart, this.mParentPanelOriTop, this.mParentPanelOriEnd, this.mParentPanelOriBottom + navigationBarHeight);
        } else if (getDialogPanelExtraBottomPadding() <= 0) {
        } else {
            this.mParentPanel.setPaddingRelative(this.mParentPanelOriStart, this.mParentPanelOriTop, this.mParentPanelOriEnd, this.mParentPanelOriBottom);
        }
    }

    public final int getDialogPanelExtraBottomPadding() {
        return Math.max(0, this.mParentPanel.getPaddingBottom() - this.mParentPanelOriBottom);
    }

    public void setEnableImmersive(boolean z) {
        this.mIsEnableImmersive = z;
    }

    public boolean isDialogImmersive() {
        return this.mIsEnableImmersive && Build.VERSION.SDK_INT >= 29;
    }

    public final void setupDialogPanel() {
        setupDialogPanel(this.mContext.getResources().getConfiguration());
    }

    public final boolean isLandscape() {
        return isLandscape(getScreenOrientation());
    }

    public final boolean isInPcMode() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), "synergy_mode", 0) == 1;
    }

    public final boolean isLandscape(int i) {
        boolean z = i == 2;
        if (z && isInPcMode()) {
            this.mWindowManager.getDefaultDisplay().getRealSize(this.mScreenRealSize);
            Point point = this.mScreenRealSize;
            z = point.x > point.y;
        }
        return z || this.mTreatAsLandConfig || DeviceHelper.isTablet(this.mContext);
    }

    public final void addPressAnimInternal(View view) {
        Drawable buttonSelectorBackground;
        if (AnimHelper.isDialogDebugInAndroidUIThreadEnabled()) {
            if (!(view instanceof GroupButton) || (buttonSelectorBackground = ((GroupButton) view).getButtonSelectorBackground()) == null) {
                return;
            }
            view.setBackground(buttonSelectorBackground);
            return;
        }
        AnimHelper.addPressAnim(view);
    }

    public final int getPanelWidth(boolean z) {
        if (z) {
            return this.mTreatAsLandConfig ? this.mFakeLandScreenMinorSize : this.mScreenMinorSize;
        }
        return -1;
    }

    public final void setupDialogPanel(Configuration configuration) {
        updateConfiguration(configuration);
        boolean isLandscape = isLandscape(this.mScreenOrientation);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mParentPanel.getLayoutParams();
        layoutParams.gravity = isLandscape ? 17 : 80;
        layoutParams.width = getPanelWidth(isLandscape);
        layoutParams.height = -2;
        this.mParentPanel.setLayoutParams(layoutParams);
    }

    public final void updateConfiguration(Configuration configuration) {
        this.mScreenOrientation = getScreenOrientation();
        this.mScreenLayout = parseScreenSize(configuration.screenLayout);
        this.mScreenWidthDp = configuration.screenWidthDp;
        this.mScreenHeightDp = configuration.screenHeightDp;
    }

    public final void setupWindow() {
        if (isDialogImmersive()) {
            setupImmersiveWindow();
        } else {
            setupUnImmersiveWindow(this.mContext.getResources().getConfiguration());
        }
    }

    public final void setupImmersiveWindow() {
        this.mWindow.setLayout(-1, -1);
        this.mWindow.setBackgroundDrawableResource(R$color.miuix_appcompat_transparent);
        this.mWindow.setDimAmount(0.0f);
        this.mWindow.addFlags(-2147481344);
        int i = Build.VERSION.SDK_INT;
        if (i > 28) {
            Activity associatedActivity = ((AlertDialog) this.mDialog).getAssociatedActivity();
            if (associatedActivity != null) {
                this.mWindow.getAttributes().layoutInDisplayCutoutMode = getCutoutMode(getScreenOrientation(), associatedActivity.getWindow().getAttributes().layoutInDisplayCutoutMode);
            } else {
                this.mWindow.getAttributes().layoutInDisplayCutoutMode = 1;
            }
        }
        clearFitSystemWindow(this.mWindow.getDecorView());
        if (i >= 30) {
            this.mWindow.getAttributes().setFitInsetsSides(0);
            Activity associatedActivity2 = ((AlertDialog) this.mDialog).getAssociatedActivity();
            if (associatedActivity2 == null || (associatedActivity2.getWindow().getAttributes().flags & 1024) != 0) {
                return;
            }
            this.mWindow.clearFlags(1024);
        }
    }

    public final void setupUnImmersiveWindow(Configuration configuration) {
        updateConfiguration(configuration);
        boolean isLandscape = isLandscape(this.mScreenOrientation);
        this.mWindow.setGravity(isLandscape ? 17 : 80);
        this.mWindow.addFlags(2);
        this.mWindow.setDimAmount(0.5f);
        this.mWindow.setLayout(getPanelWidth(isLandscape), -2);
        this.mWindow.setBackgroundDrawableResource(R$color.miuix_appcompat_transparent);
    }

    public final void clearFitSystemWindow(View view) {
        if ((view instanceof DialogParentPanel) || view == null) {
            return;
        }
        int i = 0;
        view.setFitsSystemWindows(false);
        if (!(view instanceof ViewGroup)) {
            return;
        }
        while (true) {
            ViewGroup viewGroup = (ViewGroup) view;
            if (i >= viewGroup.getChildCount()) {
                return;
            }
            clearFitSystemWindow(viewGroup.getChildAt(i));
            i++;
        }
    }

    public final void reInitLandConfig(Configuration configuration) {
        this.mTreatAsLandConfig = this.mContext.getApplicationContext().getResources().getBoolean(R$bool.treat_as_land);
        this.mFakeLandScreenMinorSize = this.mContext.getApplicationContext().getResources().getDimensionPixelSize(R$dimen.fake_landscape_screen_minor_size);
        updateMinorScreenSize(configuration);
    }

    public void onConfigurationChanged(Configuration configuration) {
        if (!checkThread()) {
            Log.w("AlertController", "dialog is created in thread:" + this.mCreateThread + ", but onConfigurationChanged is called from different thread:" + Thread.currentThread() + ", so this onConfigurationChanged call should be ignore");
        } else if (!this.mWindow.getDecorView().isAttachedToWindow()) {
        } else {
            int screenOrientation = getScreenOrientation();
            if (this.mDialog.getOwnerActivity() == null) {
                checkUpdateDialog(configuration);
                return;
            }
            int diff = configuration.diff(this.mContext.getResources().getConfiguration());
            boolean z = false;
            boolean z2 = ((diff & 1024) == 0 && this.mScreenWidthDp == configuration.screenWidthDp && this.mScreenHeightDp == configuration.screenHeightDp) ? false : true;
            if ((diff & 128) != 0 || this.mScreenOrientation != screenOrientation) {
                z = true;
            }
            if (!z && !z2) {
                return;
            }
            updateDialog(configuration, screenOrientation);
        }
    }

    public final void checkUpdateDialog(Configuration configuration) {
        boolean z = (this.mScreenLayout == parseScreenSize(configuration.screenLayout) && this.mScreenWidthDp == configuration.screenWidthDp && this.mScreenHeightDp == configuration.screenHeightDp) ? false : true;
        int screenOrientation = getScreenOrientation();
        if (this.mScreenOrientation != screenOrientation || z) {
            updateDialog(configuration, screenOrientation);
        }
    }

    public final void updateDialog(Configuration configuration, int i) {
        reInitLandConfig(configuration);
        if (isDialogImmersive()) {
            updateWindowCutoutMode(i);
            setupDialogPanel(configuration);
        } else {
            setupUnImmersiveWindow(configuration);
        }
        adjustDialogPanelBackground(i);
    }

    public final void updateWindowCutoutMode(int i) {
        Activity associatedActivity;
        int cutoutMode;
        if (Build.VERSION.SDK_INT <= 28 || !isNotch() || this.mScreenOrientation == i || (associatedActivity = ((AlertDialog) this.mDialog).getAssociatedActivity()) == null || this.mWindow.getAttributes().layoutInDisplayCutoutMode == (cutoutMode = getCutoutMode(i, associatedActivity.getWindow().getAttributes().layoutInDisplayCutoutMode))) {
            return;
        }
        this.mWindow.getAttributes().layoutInDisplayCutoutMode = cutoutMode;
        if (!this.mDialog.isShowing()) {
            return;
        }
        this.mWindowManager.updateViewLayout(this.mWindow.getDecorView(), this.mWindow.getAttributes());
    }

    public final int getCutoutMode(int i, int i2) {
        return (!isNotch() || i2 != 0) ? i2 : i == 2 ? 2 : 1;
    }

    public final int getScreenOrientation() {
        WindowManager windowManager = this.mWindowManager;
        if (windowManager == null) {
            return 0;
        }
        int rotation = windowManager.getDefaultDisplay().getRotation();
        return (rotation == 1 || rotation == 3) ? 2 : 1;
    }

    public final void adjustDialogPanelBackground(int i) {
        if (this.mRectanglePanelBg == null) {
            this.mRectanglePanelBg = AttributeResolver.resolveDrawable(this.mContext, 16842836);
        }
        if (this.mRoundPanelBg == null) {
            this.mRoundPanelBg = AttributeResolver.resolveDrawable(this.mContext, R$attr.miuixDialogRoundWindowBg);
        }
        if (isLandscape(i)) {
            this.mParentPanel.setBackground(this.mRoundPanelBg);
        } else {
            this.mParentPanel.setBackground(this.mRectanglePanelBg);
        }
    }

    public void setShowAnimListener(AlertDialog.OnDialogShowAnimListener onDialogShowAnimListener) {
        this.mShowAnimListener = onDialogShowAnimListener;
    }

    public void onStart() {
        this.mContext.registerComponentCallbacks(this.mComponentCallback);
        if (isDialogImmersive()) {
            checkUpdateDialog(this.mContext.getResources().getConfiguration());
            DialogAnimHelper.executeShowAnim(this.mParentPanel, this.mDimBg, isLandscape(), this.mShowAnimListener);
            this.mWindow.getDecorView().addOnLayoutChangeListener(this.mLayoutChangeListener);
        }
    }

    public void onStop() {
        this.mContext.unregisterComponentCallbacks(this.mComponentCallback);
        if (isDialogImmersive()) {
            this.mWindow.getDecorView().removeOnLayoutChangeListener(this.mLayoutChangeListener);
        }
    }

    public final void checkAndClearFocus() {
        View currentFocus = this.mWindow.getCurrentFocus();
        if (currentFocus != null) {
            currentFocus.clearFocus();
            hideSoftIME();
        }
    }

    public final boolean checkThread() {
        return this.mCreateThread == Thread.currentThread();
    }

    public void onDetachedFromWindow() {
        if (!AnimHelper.isDialogDebugInAndroidUIThreadEnabled()) {
            Folme.clean(this.mParentPanel, this.mDimBg);
        }
    }

    public void dismiss(DialogAnimHelper.OnDismiss onDismiss) {
        View view = this.mParentPanel;
        if (view == null) {
            if (onDismiss == null) {
                return;
            }
            onDismiss.end();
        } else if (view.isAttachedToWindow()) {
            checkAndClearFocus();
            DialogAnimHelper.executeDismissAnim(this.mParentPanel, this.mDimBg, onDismiss);
        } else {
            Log.d("AlertController", "dialog is not attached to window when dismiss is invoked");
            try {
                ((AlertDialog) this.mDialog).realDismiss();
            } catch (IllegalArgumentException e) {
                Log.wtf("AlertController", "Not catch the dialog will throw the illegalArgumentException (In Case cause the crash , we expect it should be caught)", e);
            }
        }
    }

    public final void centerIfSingleLine(TextView textView, CharSequence charSequence) {
        if (this.mView == null && this.mCheckBoxMessage == null && (textView instanceof SingleCenterTextView)) {
            ((SingleCenterTextView) textView).setEnableSingleCenter(true);
        }
    }

    public final void changeTitlePadding(TextView textView) {
        textView.setPadding(textView.getPaddingLeft(), textView.getPaddingTop(), textView.getPaddingRight(), 0);
    }

    public final void translateDialogPanel(int i) {
        this.mParentPanel.setTranslationY(i);
    }

    /* loaded from: classes3.dex */
    public static class AlertParams {
        public ListAdapter mAdapter;
        public CharSequence mCheckBoxMessage;
        public boolean[] mCheckedItems;
        public final Context mContext;
        public Cursor mCursor;
        public View mCustomTitleView;
        public boolean mForceInverseBackground;
        public boolean mHapticFeedbackEnabled;
        public Drawable mIcon;
        public final LayoutInflater mInflater;
        public boolean mIsChecked;
        public String mIsCheckedColumn;
        public boolean mIsMultiChoice;
        public boolean mIsSingleChoice;
        public CharSequence[] mItems;
        public String mLabelColumn;
        public CharSequence mMessage;
        public DialogInterface.OnClickListener mNegativeButtonListener;
        public CharSequence mNegativeButtonText;
        public DialogInterface.OnClickListener mNeutralButtonListener;
        public CharSequence mNeutralButtonText;
        public DialogInterface.OnCancelListener mOnCancelListener;
        public DialogInterface.OnMultiChoiceClickListener mOnCheckboxClickListener;
        public DialogInterface.OnClickListener mOnClickListener;
        public AlertDialog.OnDialogShowAnimListener mOnDialogShowAnimListener;
        public DialogInterface.OnDismissListener mOnDismissListener;
        public AdapterView.OnItemSelectedListener mOnItemSelectedListener;
        public DialogInterface.OnKeyListener mOnKeyListener;
        public OnPrepareListViewListener mOnPrepareListViewListener;
        public DialogInterface.OnShowListener mOnShowListener;
        public DialogInterface.OnClickListener mPositiveButtonListener;
        public CharSequence mPositiveButtonText;
        public CharSequence mTitle;
        public View mView;
        public int mViewLayoutResId;
        public int mIconId = 0;
        public int mIconAttrId = 0;
        public int mCheckedItem = -1;
        public boolean mCancelable = true;
        public boolean mEnableDialogImmersive = true;

        /* loaded from: classes3.dex */
        public interface OnPrepareListViewListener {
            void onPrepareListView(ListView listView);
        }

        public AlertParams(Context context) {
            this.mContext = context;
            this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        }

        public void apply(AlertController alertController) {
            View view = this.mCustomTitleView;
            if (view != null) {
                alertController.setCustomTitle(view);
            } else {
                CharSequence charSequence = this.mTitle;
                if (charSequence != null) {
                    alertController.setTitle(charSequence);
                }
                Drawable drawable = this.mIcon;
                if (drawable != null) {
                    alertController.setIcon(drawable);
                }
                int i = this.mIconId;
                if (i != 0) {
                    alertController.setIcon(i);
                }
                int i2 = this.mIconAttrId;
                if (i2 != 0) {
                    alertController.setIcon(alertController.getIconAttributeResId(i2));
                }
            }
            CharSequence charSequence2 = this.mMessage;
            if (charSequence2 != null) {
                alertController.setMessage(charSequence2);
            }
            CharSequence charSequence3 = this.mPositiveButtonText;
            if (charSequence3 != null) {
                alertController.setButton(-1, charSequence3, this.mPositiveButtonListener, null);
            }
            CharSequence charSequence4 = this.mNegativeButtonText;
            if (charSequence4 != null) {
                alertController.setButton(-2, charSequence4, this.mNegativeButtonListener, null);
            }
            CharSequence charSequence5 = this.mNeutralButtonText;
            if (charSequence5 != null) {
                alertController.setButton(-3, charSequence5, this.mNeutralButtonListener, null);
            }
            if (this.mItems != null || this.mCursor != null || this.mAdapter != null) {
                createListView(alertController);
            }
            View view2 = this.mView;
            if (view2 != null) {
                alertController.setView(view2);
            } else {
                int i3 = this.mViewLayoutResId;
                if (i3 != 0) {
                    alertController.setView(i3);
                }
            }
            CharSequence charSequence6 = this.mCheckBoxMessage;
            if (charSequence6 != null) {
                alertController.setCheckBox(this.mIsChecked, charSequence6);
            }
            alertController.mHapticFeedbackEnabled = this.mHapticFeedbackEnabled;
            alertController.setEnableImmersive(this.mEnableDialogImmersive);
        }

        private void createListView(final AlertController alertController) {
            int i;
            ListAdapter listAdapter;
            final ListView listView = (ListView) this.mInflater.inflate(alertController.mListLayout, (ViewGroup) null);
            if (this.mIsMultiChoice) {
                if (this.mCursor == null) {
                    listAdapter = new ArrayAdapter<CharSequence>(this.mContext, alertController.mMultiChoiceItemLayout, 16908308, this.mItems) { // from class: miuix.appcompat.app.AlertController.AlertParams.1
                        {
                            AlertParams.this = this;
                        }

                        @Override // android.widget.ArrayAdapter, android.widget.Adapter
                        public View getView(int i2, View view, ViewGroup viewGroup) {
                            View view2 = super.getView(i2, view, viewGroup);
                            boolean[] zArr = AlertParams.this.mCheckedItems;
                            if (zArr != null && zArr[i2]) {
                                listView.setItemChecked(i2, true);
                            }
                            CompatViewMethod.setForceDarkAllowed(view2, false);
                            if (view == null) {
                                AnimHelper.addPressAnim(view2);
                            }
                            EasyModeHelper.updateTextViewSize((TextView) view2.findViewById(16908308));
                            return view2;
                        }
                    };
                } else {
                    listAdapter = new CursorAdapter(this.mContext, this.mCursor, false) { // from class: miuix.appcompat.app.AlertController.AlertParams.2
                        private final int mIsCheckedIndex;
                        private final int mLabelIndex;

                        {
                            AlertParams.this = this;
                            Cursor cursor = getCursor();
                            this.mLabelIndex = cursor.getColumnIndexOrThrow(this.mLabelColumn);
                            this.mIsCheckedIndex = cursor.getColumnIndexOrThrow(this.mIsCheckedColumn);
                        }

                        @Override // android.widget.CursorAdapter
                        public void bindView(View view, Context context, Cursor cursor) {
                            CheckedTextView checkedTextView = (CheckedTextView) view.findViewById(16908308);
                            checkedTextView.setText(cursor.getString(this.mLabelIndex));
                            ListView listView2 = listView;
                            int position = cursor.getPosition();
                            boolean z = true;
                            if (cursor.getInt(this.mIsCheckedIndex) != 1) {
                                z = false;
                            }
                            listView2.setItemChecked(position, z);
                            EasyModeHelper.updateTextViewSize(checkedTextView);
                        }

                        @Override // android.widget.CursorAdapter
                        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
                            View inflate = AlertParams.this.mInflater.inflate(alertController.mMultiChoiceItemLayout, viewGroup, false);
                            AnimHelper.addPressAnim(inflate);
                            CompatViewMethod.setForceDarkAllowed(inflate, false);
                            return inflate;
                        }
                    };
                }
            } else {
                if (this.mIsSingleChoice) {
                    i = alertController.mSingleChoiceItemLayout;
                } else {
                    i = alertController.mListItemLayout;
                }
                int i2 = i;
                if (this.mCursor != null) {
                    listAdapter = new SimpleCursorAdapter(this.mContext, i2, this.mCursor, new String[]{this.mLabelColumn}, new int[]{16908308}) { // from class: miuix.appcompat.app.AlertController.AlertParams.3
                        {
                            AlertParams.this = this;
                        }

                        @Override // android.widget.CursorAdapter, android.widget.Adapter
                        public View getView(int i3, View view, ViewGroup viewGroup) {
                            View view2 = super.getView(i3, view, viewGroup);
                            if (view == null) {
                                AnimHelper.addPressAnim(view2);
                            }
                            EasyModeHelper.updateTextViewSize((TextView) view2.findViewById(16908308));
                            return view2;
                        }
                    };
                } else {
                    listAdapter = this.mAdapter;
                    if (listAdapter == null) {
                        listAdapter = new CheckedItemAdapter(this.mContext, i2, 16908308, this.mItems);
                    }
                }
            }
            OnPrepareListViewListener onPrepareListViewListener = this.mOnPrepareListViewListener;
            if (onPrepareListViewListener != null) {
                onPrepareListViewListener.onPrepareListView(listView);
            }
            alertController.mAdapter = listAdapter;
            alertController.mCheckedItem = this.mCheckedItem;
            if (this.mOnClickListener != null) {
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: miuix.appcompat.app.AlertController.AlertParams.4
                    {
                        AlertParams.this = this;
                    }

                    @Override // android.widget.AdapterView.OnItemClickListener
                    public void onItemClick(AdapterView<?> adapterView, View view, int i3, long j) {
                        AlertParams.this.mOnClickListener.onClick(alertController.mDialog, i3);
                        if (!AlertParams.this.mIsSingleChoice) {
                            alertController.mDialog.dismiss();
                        }
                    }
                });
            } else if (this.mOnCheckboxClickListener != null) {
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: miuix.appcompat.app.AlertController.AlertParams.5
                    {
                        AlertParams.this = this;
                    }

                    @Override // android.widget.AdapterView.OnItemClickListener
                    public void onItemClick(AdapterView<?> adapterView, View view, int i3, long j) {
                        boolean[] zArr = AlertParams.this.mCheckedItems;
                        if (zArr != null) {
                            zArr[i3] = listView.isItemChecked(i3);
                        }
                        AlertParams.this.mOnCheckboxClickListener.onClick(alertController.mDialog, i3, listView.isItemChecked(i3));
                    }
                });
            }
            AdapterView.OnItemSelectedListener onItemSelectedListener = this.mOnItemSelectedListener;
            if (onItemSelectedListener != null) {
                listView.setOnItemSelectedListener(onItemSelectedListener);
            }
            if (this.mIsSingleChoice) {
                listView.setChoiceMode(1);
            } else if (this.mIsMultiChoice) {
                listView.setChoiceMode(2);
            }
            alertController.mListView = listView;
        }
    }

    /* loaded from: classes3.dex */
    public static class CheckedItemAdapter extends ArrayAdapter<CharSequence> {
        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public boolean hasStableIds() {
            return true;
        }

        public CheckedItemAdapter(Context context, int i, int i2, CharSequence[] charSequenceArr) {
            super(context, i, i2, charSequenceArr);
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view2 = super.getView(i, view, viewGroup);
            if (view == null) {
                AnimHelper.addPressAnim(view2);
            }
            EasyModeHelper.updateTextViewSize((TextView) view2.findViewById(16908308));
            return view2;
        }
    }

    /* loaded from: classes3.dex */
    public static class CustomComponentCallbacks implements ComponentCallbacks {
        private WeakReference<AlertController> mHost;

        @Override // android.content.ComponentCallbacks
        public void onLowMemory() {
        }

        public CustomComponentCallbacks(AlertController alertController) {
            this.mHost = new WeakReference<>(alertController);
        }

        @Override // android.content.ComponentCallbacks
        public void onConfigurationChanged(Configuration configuration) {
            if (this.mHost.get() != null) {
                this.mHost.get().onConfigurationChanged(configuration);
            }
        }
    }

    public final boolean isFreeFormMode(Rect rect) {
        if (isLandscape() || !MiuixUIUtils.isInMultiWindowMode(this.mContext)) {
            return false;
        }
        this.mWindowManager.getDefaultDisplay().getRealSize(this.mScreenRealSize);
        if (rect.top != 0 || rect.left != 0) {
            return false;
        }
        int i = rect.right;
        Point point = this.mScreenRealSize;
        return i == point.x && rect.bottom < point.y;
    }

    public final void checkTranslateDialogPanel(WindowInsets windowInsets) {
        if (Build.VERSION.SDK_INT < 30 || !isDialogImmersive()) {
            return;
        }
        if (!this.mInsetsAnimationPlayed && !MiuixUIUtils.isInMultiWindowMode(this.mContext)) {
            return;
        }
        Insets insets = windowInsets.getInsets(WindowInsets.Type.ime());
        if (insets.bottom > 0) {
            int dialogPanelMargin = (int) (getDialogPanelMargin() + this.mParentPanel.getTranslationY());
            this.mPanelAndImeMargin = dialogPanelMargin;
            if (dialogPanelMargin <= 0) {
                this.mPanelAndImeMargin = 0;
            }
            if (this.mTreatAsLandConfig || DeviceHelper.isTablet(this.mContext)) {
                int i = this.mPanelAndImeMargin;
                int i2 = insets.bottom;
                if (i < i2) {
                    translateDialogPanel(i - i2);
                    return;
                } else {
                    translateDialogPanel(0);
                    return;
                }
            }
            translateDialogPanel(getDialogPanelExtraBottomPadding() - insets.bottom);
        } else if (this.mParentPanel.getTranslationY() >= 0.0f) {
        } else {
            translateDialogPanel(0);
        }
    }

    /* loaded from: classes3.dex */
    public static class LayoutChangeListener implements View.OnLayoutChangeListener {
        private WeakReference<AlertController> mHost;
        private Rect mWindowVisibleFrame = new Rect();

        public LayoutChangeListener(AlertController alertController) {
            this.mHost = new WeakReference<>(alertController);
        }

        @Override // android.view.View.OnLayoutChangeListener
        public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            AlertController alertController = this.mHost.get();
            if (alertController != null) {
                view.getWindowVisibleDisplayFrame(this.mWindowVisibleFrame);
                alertController.adaptPaddingBottom();
                handleMultiWindowLandscapeChange(alertController, i3);
                if (Build.VERSION.SDK_INT >= 30) {
                    return;
                }
                if (view.findFocus() != null) {
                    if (alertController.isFreeFormMode(this.mWindowVisibleFrame)) {
                        return;
                    }
                    handleImeChange(view, this.mWindowVisibleFrame, alertController);
                } else if (alertController.mParentPanel.getTranslationY() >= 0.0f) {
                } else {
                    alertController.translateDialogPanel(0);
                }
            }
        }

        private void handleImeChange(View view, Rect rect, AlertController alertController) {
            int i;
            int height = (view.getHeight() - alertController.getDialogPanelExtraBottomPadding()) - rect.bottom;
            if (height > 0) {
                i = -height;
                DialogAnimHelper.cancelAnimator();
            } else {
                i = 0;
            }
            alertController.translateDialogPanel(i);
        }

        private void changeViewPadding(View view, int i, int i2) {
            view.setPadding(i, 0, i2, 0);
        }

        private void handleMultiWindowLandscapeChange(AlertController alertController, int i) {
            if (MiuixUIUtils.isInMultiWindowMode(alertController.mContext)) {
                if (this.mWindowVisibleFrame.left > 0) {
                    int i2 = i - alertController.mContext.getResources().getDisplayMetrics().widthPixels;
                    if (this.mWindowVisibleFrame.right == i) {
                        changeViewPadding(alertController.mDialogRootView, i2, 0);
                        return;
                    } else {
                        changeViewPadding(alertController.mDialogRootView, 0, i2);
                        return;
                    }
                }
                changeViewPadding(alertController.mDialogRootView, 0, 0);
            }
        }

        public boolean hasNavigationBarHeightInMultiWindowMode() {
            this.mHost.get().mWindowManager.getDefaultDisplay().getRealSize(this.mHost.get().mScreenRealSize);
            Rect rect = this.mWindowVisibleFrame;
            return (rect.left == 0 && rect.right == this.mHost.get().mScreenRealSize.x && this.mWindowVisibleFrame.top <= MiuixUIUtils.getStatusBarHeight(this.mHost.get().mContext)) ? false : true;
        }
    }
}
