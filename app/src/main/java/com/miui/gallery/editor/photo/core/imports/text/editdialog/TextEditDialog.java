package com.miui.gallery.editor.photo.core.imports.text.editdialog;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.baidu.platform.comapi.UIMsg;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.compat.app.ActivityCompat;
import com.miui.gallery.editor.photo.app.HostAbility;
import com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog;
import com.miui.gallery.editor.photo.core.imports.text.model.DialogStatusData;
import com.miui.gallery.editor.photo.core.imports.text.typeface.TextStyle;
import com.miui.gallery.editor.photo.core.imports.text.utils.AutoLineLayout;
import com.miui.gallery.editor.photo.core.imports.text.utils.ColorUtils;
import com.miui.gallery.editor.photo.widgets.ColorSelector.ColorSelectorView;
import com.miui.gallery.editor.ui.view.EditorToast;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.GalleryDialogFragment;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class TextEditDialog extends GalleryDialogFragment implements HostAbility {
    public String[] mColorData;
    public ColorSelectorView mColorSelectorView;
    public ConfigChangeListener mConfigChangeListener;
    public FrameLayout mContainerView;
    public EditText mEditText;
    public ViewGroup mEditView;
    public EditorToast mEditorToast;
    public DialogStatusData mInitializeData;
    public Boolean mIsInMultiWindowMode;
    public boolean mIsShowSubstrateIcon;
    public LinearLayout mNavigationContainer;
    public StatusListener mStatusListener;
    public ImageView mSubstrateIv;
    public FrameLayout mTabContainer;
    public TextWatcher mTextWatcher;
    public ViewGroup mWholeView;
    public String mWillEditText;
    public boolean mWillSelection = false;
    public int mScreenHeight = 0;
    public Tab mCurrentTab = Tab.KEYBOARD;
    public List<DialogSubMenu> mDialogSubMenuList = new ArrayList();
    public int mCurrentHeight = 0;
    public int mKeyBoardHeight = 0;
    public int mTabIndex = -1;
    public String mBlack = "#FF000000";
    public OnItemClickListener mOnItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog.1
        {
            TextEditDialog.this = this;
        }

        @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
        public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
            String str = TextEditDialog.this.mColorSelectorView.getColorTexts()[i];
            if (i == 1) {
                str = TextEditDialog.this.mBlack;
            }
            String[] split = str.split(",");
            TextEditDialog.this.mConfigChangeListener.onColorChange(ColorUtils.colorStringToInt(split));
            TextEditDialog.this.mColorSelectorView.setSelection(i, true);
            if (TextEditDialog.this.mInitializeData.isSubstrate) {
                TextEditDialog.this.mInitializeData.mSubstrateColors = ColorUtils.colorStringToInt(split);
                return true;
            }
            TextEditDialog.this.mInitializeData.color = Color.parseColor(split[0]);
            if (split.length > 1) {
                TextEditDialog.this.mInitializeData.gradientsColor = Color.parseColor(split[1]);
            }
            return true;
        }
    };
    public View.OnClickListener mNavigationClickListener = new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog.5
        {
            TextEditDialog.this = this;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            int ordinal = TextEditDialog.this.mCurrentTab.ordinal();
            for (int i = 0; i < TextEditDialog.this.mDialogSubMenuList.size(); i++) {
                DialogSubMenu dialogSubMenu = (DialogSubMenu) TextEditDialog.this.mDialogSubMenuList.get(i);
                if (view != dialogSubMenu.getNavigationButton()) {
                    dialogSubMenu.setChecked(false);
                } else if (ordinal != i) {
                    dialogSubMenu.setChecked(true);
                    if (i == 0) {
                        TextEditDialog.this.showOrHideKeyboard();
                        TextEditDialog.this.mTabIndex = -1;
                    } else {
                        if (TextEditDialog.this.mCurrentTab == Tab.KEYBOARD) {
                            TextEditDialog.this.mCurrentTab = Tab.values()[i];
                            TextEditDialog.this.hideKeyboard();
                        }
                        ViewGroup subMenuView = dialogSubMenu.getSubMenuView();
                        TextEditDialog.this.mTabContainer.removeAllViews();
                        TextEditDialog.this.mTabContainer.addView(subMenuView, new FrameLayout.LayoutParams(-1, -1));
                    }
                    TextEditDialog.this.mCurrentTab = Tab.values()[i];
                    DefaultLogger.d("TextEditDialog", "current click index : %d", Integer.valueOf(i));
                }
            }
        }
    };

    /* loaded from: classes2.dex */
    public interface ConfigChangeListener {
        void onBoldChange(boolean z);

        void onColorChange(int i);

        default void onColorChange(int... iArr) {
        }

        void onShadowChange(boolean z);

        default void onStrokeChange(boolean z) {
        }

        default void onSubstrateChange(boolean z) {
        }

        void onTextAlignChange(AutoLineLayout.TextAlignment textAlignment);

        void onTransparentChange(int i);

        void onTypefaceChange(TextStyle textStyle);
    }

    /* loaded from: classes2.dex */
    public interface StatusListener {
        void onBottomChange(int i);

        void onDismiss();

        void onShow();
    }

    /* renamed from: $r8$lambda$P-vucjKg0ixGS_W-vSQKKFTo_TQ */
    public static /* synthetic */ void m888$r8$lambda$PvucjKg0ixGS_WvSQKKFTo_TQ(TextEditDialog textEditDialog, DialogInterface dialogInterface) {
        textEditDialog.lambda$onCreateDialog$3(dialogInterface);
    }

    public static /* synthetic */ void $r8$lambda$SmFPE7XDyorPugaYDjE9AHsHXq8(TextEditDialog textEditDialog, View view) {
        textEditDialog.lambda$initView$2(view);
    }

    /* renamed from: $r8$lambda$a2uR1lsmvWfMEW7Pm-5AmHOH_G8 */
    public static /* synthetic */ void m889$r8$lambda$a2uR1lsmvWfMEW7Pm5AmHOH_G8(TextEditDialog textEditDialog, View view) {
        textEditDialog.lambda$onCreateView$0(view);
    }

    /* renamed from: $r8$lambda$pHa1Gl8wIxEc-SgijeXqsBgjl4c */
    public static /* synthetic */ void m890$r8$lambda$pHa1Gl8wIxEcSgijeXqsBgjl4c(TextEditDialog textEditDialog, View view) {
        textEditDialog.lambda$initView$1(view);
    }

    @Override // com.miui.gallery.editor.photo.app.HostAbility
    public void addViewToExtraContainer(View view, FrameLayout.LayoutParams layoutParams) {
    }

    @Override // com.miui.gallery.editor.photo.app.HostAbility
    public void clearExtraContainer() {
    }

    @Override // com.miui.gallery.editor.photo.app.HostAbility
    public ViewGroup getExtraContainer() {
        return null;
    }

    @Override // com.miui.gallery.editor.photo.app.HostAbility
    public void hideInnerToast() {
    }

    /* renamed from: com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog$6 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass6 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$editor$photo$core$imports$text$editdialog$TextEditDialog$Tab;

        static {
            int[] iArr = new int[Tab.values().length];
            $SwitchMap$com$miui$gallery$editor$photo$core$imports$text$editdialog$TextEditDialog$Tab = iArr;
            try {
                iArr[Tab.KEYBOARD.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$text$editdialog$TextEditDialog$Tab[Tab.STYLE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$text$editdialog$TextEditDialog$Tab[Tab.FONT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    /* loaded from: classes2.dex */
    public enum Tab {
        KEYBOARD,
        STYLE,
        FONT;

        public DialogSubMenu getSubMenu(Context context, ConfigChangeListener configChangeListener, ViewGroup viewGroup, HostAbility hostAbility) {
            int i = AnonymousClass6.$SwitchMap$com$miui$gallery$editor$photo$core$imports$text$editdialog$TextEditDialog$Tab[ordinal()];
            if (i != 1) {
                if (i == 2) {
                    return new DialogStyleMenu(context, viewGroup, configChangeListener, hostAbility);
                }
                if (i == 3) {
                    return new DialogFontMenu(context, viewGroup, configChangeListener);
                }
                return null;
            }
            return new DialogKeyboardMenu(context);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        FrameLayout frameLayout = new FrameLayout(getActivity());
        this.mContainerView = frameLayout;
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        this.mContainerView.getViewTreeObserver().addOnGlobalLayoutListener(new LayoutListener());
        this.mIsInMultiWindowMode = Boolean.valueOf(ActivityCompat.isInMultiWindowMode(getActivity()));
        if (this.mWholeView == null) {
            initView(layoutInflater);
            this.mContainerView.setAlpha(0.0f);
        }
        ImageView imageView = this.mSubstrateIv;
        if (imageView != null) {
            imageView.setSelected(this.mInitializeData.isSubstrate);
        }
        this.mContainerView.addView(this.mWholeView);
        refreshColorSelectorSelection();
        if (Build.DEVICE.equals("cetus") || this.mIsInMultiWindowMode.booleanValue()) {
            ViewGroup.LayoutParams layoutParams = this.mWholeView.getLayoutParams();
            layoutParams.height = -1;
            this.mWholeView.setLayoutParams(layoutParams);
            this.mWholeView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    TextEditDialog.m889$r8$lambda$a2uR1lsmvWfMEW7Pm5AmHOH_G8(TextEditDialog.this, view);
                }
            });
        }
        return this.mContainerView;
    }

    public /* synthetic */ void lambda$onCreateView$0(View view) {
        dismissSafely();
    }

    @Override // com.miui.gallery.widget.GalleryDialogFragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.mDialogSubMenuList != null) {
            for (int i = 0; i < this.mDialogSubMenuList.size(); i++) {
                this.mDialogSubMenuList.get(i).OnConfigurationChanged();
            }
        }
    }

    public final void initView(LayoutInflater layoutInflater) {
        Tab[] values;
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.text_edit_text_dialog, (ViewGroup) null);
        this.mWholeView = viewGroup;
        this.mSubstrateIv = (ImageView) viewGroup.findViewById(R.id.iv_substrate);
        this.mColorSelectorView = (ColorSelectorView) this.mWholeView.findViewById(R.id.recyclerView);
        if (this.mColorData == null) {
            this.mColorData = getContext().getResources().getStringArray(R.array.color_value);
        }
        this.mColorSelectorView.init(this.mColorData);
        this.mColorSelectorView.setOnItemClickListener(this.mOnItemClickListener);
        this.mConfigChangeListener.onColorChange(this.mInitializeData.color);
        this.mSubstrateIv.setVisibility(this.mIsShowSubstrateIcon ? 0 : 8);
        this.mSubstrateIv.setSelected(this.mInitializeData.isSubstrate);
        this.mSubstrateIv.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TextEditDialog.m890$r8$lambda$pHa1Gl8wIxEcSgijeXqsBgjl4c(TextEditDialog.this, view);
            }
        });
        this.mNavigationContainer = (LinearLayout) this.mWholeView.findViewById(R.id.text_append_edit_text_tab_group);
        this.mTabContainer = (FrameLayout) this.mWholeView.findViewById(R.id.text_append_edit_text_tab_container);
        this.mEditView = (ViewGroup) this.mWholeView.findViewById(R.id.text_append_edit_text_layout);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2);
        layoutParams.weight = 1.0f;
        for (Tab tab : Tab.values()) {
            DialogSubMenu subMenu = tab.getSubMenu(getActivity(), this.mConfigChangeListener, this.mTabContainer, this);
            if (tab.ordinal() == 0) {
                subMenu.setChecked(true);
            }
            View navigationButton = subMenu.getNavigationButton();
            navigationButton.setOnClickListener(this.mNavigationClickListener);
            this.mNavigationContainer.addView(navigationButton, layoutParams);
            this.mDialogSubMenuList.add(subMenu);
        }
        this.mEditText = (EditText) this.mWholeView.findViewById(R.id.text_append_edit_text);
        this.mWholeView.findViewById(R.id.text_append_edit_text_submit).setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TextEditDialog.$r8$lambda$SmFPE7XDyorPugaYDjE9AHsHXq8(TextEditDialog.this, view);
            }
        });
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.enableTransitionType(4);
        this.mWholeView.setLayoutTransition(layoutTransition);
        this.mEditorToast = new EditorToast(this.mWholeView.getContext());
    }

    public /* synthetic */ void lambda$initView$1(View view) {
        DialogStatusData dialogStatusData = this.mInitializeData;
        if (dialogStatusData.isStroke) {
            showInnerToast(this.mWholeView.getContext().getResources().getString(R.string.both_stroke_and_substrate_are_not_supported));
            return;
        }
        ConfigChangeListener configChangeListener = this.mConfigChangeListener;
        boolean z = !dialogStatusData.isSubstrate;
        dialogStatusData.isSubstrate = z;
        configChangeListener.onSubstrateChange(z);
        view.setSelected(this.mInitializeData.isSubstrate);
        String str = this.mColorSelectorView.getColorTexts()[this.mColorSelectorView.getSelection()];
        DialogStatusData dialogStatusData2 = this.mInitializeData;
        if (!dialogStatusData2.isSubstrate) {
            return;
        }
        dialogStatusData2.mSubstrateColors = ColorUtils.colorStringToInt(str.split(","));
    }

    public /* synthetic */ void lambda$initView$2(View view) {
        dismissSafely();
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        Dialog dialog = new Dialog(getActivity(), 2131952154);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnShowListener
            public final void onShow(DialogInterface dialogInterface) {
                TextEditDialog.m888$r8$lambda$PvucjKg0ixGS_WvSQKKFTo_TQ(TextEditDialog.this, dialogInterface);
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
            if (!Build.DEVICE.equals("cetus") && !this.mIsInMultiWindowMode.booleanValue()) {
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
        for (DialogSubMenu dialogSubMenu : this.mDialogSubMenuList) {
            dialogSubMenu.initializeData(this.mInitializeData);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        dismissSafely();
        super.onPause();
    }

    public void setIsShowSubstrateIcon(boolean z) {
        this.mIsShowSubstrateIcon = z;
        ImageView imageView = this.mSubstrateIv;
        if (imageView != null) {
            imageView.setVisibility(z ? 0 : 8);
        }
    }

    public void setTextWatch(TextWatcher textWatcher) {
        this.mTextWatcher = textWatcher;
    }

    public void setWillEditText(String str, boolean z) {
        this.mWillEditText = str;
        this.mWillSelection = z;
    }

    public void setConfigChangeListener(ConfigChangeListener configChangeListener) {
        this.mConfigChangeListener = configChangeListener;
    }

    public final void hideKeyboard() {
        this.mEditText.postDelayed(new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog.3
            {
                TextEditDialog.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (!TextEditDialog.this.isAdded()) {
                    return;
                }
                DefaultLogger.d("TextEditDialog", "hideKeyboard");
                ((InputMethodManager) TextEditDialog.this.getActivity().getSystemService("input_method")).hideSoftInputFromWindow(TextEditDialog.this.mEditText.getWindowToken(), 0);
            }
        }, 100L);
    }

    public final void showOrHideKeyboard() {
        this.mEditText.postDelayed(new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog.4
            {
                TextEditDialog.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (!TextEditDialog.this.isAdded()) {
                    return;
                }
                ((InputMethodManager) GalleryApp.sGetAndroidContext().getSystemService("input_method")).toggleSoftInput(0, 2);
            }
        }, 100L);
    }

    /* loaded from: classes2.dex */
    public class LayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        public Rect mRect;

        public static /* synthetic */ void $r8$lambda$0OQDtMxds4ZQKh8O9eKnWiTJzC8(LayoutListener layoutListener, View view) {
            layoutListener.lambda$onGlobalLayout$0(view);
        }

        public LayoutListener() {
            TextEditDialog.this = r1;
            this.mRect = new Rect();
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            Dialog dialog = TextEditDialog.this.getDialog();
            String str = Build.DEVICE;
            if (str.equals("cetus") || TextEditDialog.this.mIsInMultiWindowMode.booleanValue()) {
                ViewGroup.LayoutParams layoutParams = TextEditDialog.this.mWholeView.getLayoutParams();
                layoutParams.height = -1;
                TextEditDialog.this.mWholeView.setLayoutParams(layoutParams);
                TextEditDialog.this.mWholeView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog$LayoutListener$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        TextEditDialog.LayoutListener.$r8$lambda$0OQDtMxds4ZQKh8O9eKnWiTJzC8(TextEditDialog.LayoutListener.this, view);
                    }
                });
            }
            if (dialog == null || dialog.getWindow() == null) {
                return;
            }
            int heightDifference = TextEditDialog.getHeightDifference(TextEditDialog.this.mContainerView, TextEditDialog.this.mScreenHeight, this.mRect);
            DefaultLogger.d("TextEditDialog", "heightDifference : %d:%d:%d", Integer.valueOf(heightDifference), Integer.valueOf(TextEditDialog.this.mKeyBoardHeight), Integer.valueOf(TextEditDialog.this.mScreenHeight));
            if (heightDifference != 0) {
                if (TextEditDialog.this.mKeyBoardHeight < heightDifference) {
                    TextEditDialog.this.mKeyBoardHeight = heightDifference;
                }
                if (heightDifference <= 0) {
                    TextEditDialog.this.mKeyBoardHeight = 0;
                } else {
                    TextEditDialog.this.mKeyBoardHeight = Math.max(heightDifference, (int) ScreenUtils.dpToPixel(200.0f));
                }
                if (TextEditDialog.this.mCurrentHeight <= 0 && TextEditDialog.this.mCurrentHeight != heightDifference) {
                    TextEditDialog.this.mCurrentHeight = heightDifference;
                    TextEditDialog textEditDialog = TextEditDialog.this;
                    textEditDialog.checkMenuIndex(textEditDialog.mTabIndex);
                }
                TextEditDialog.this.notifyKeyboardHeightChange();
            } else {
                if (TextEditDialog.this.mCurrentHeight > 0 && TextEditDialog.this.mCurrentTab == Tab.KEYBOARD && (!str.equals("cetus") || !TextEditDialog.this.mIsInMultiWindowMode.booleanValue())) {
                    DefaultLogger.d("TextEditDialog", "onGlobalLayout dismiss");
                    TextEditDialog.this.mKeyBoardHeight = 0;
                    TextEditDialog.this.dismissSafely();
                }
                TextEditDialog.this.mCurrentHeight = heightDifference;
                if (TextEditDialog.this.mTabIndex == Tab.KEYBOARD.ordinal()) {
                    TextEditDialog textEditDialog2 = TextEditDialog.this;
                    textEditDialog2.checkMenuIndex(textEditDialog2.mTabIndex);
                    TextEditDialog.this.mTabIndex = -1;
                }
            }
            TextEditDialog textEditDialog3 = TextEditDialog.this;
            textEditDialog3.notifyHeightChange(textEditDialog3.mKeyBoardHeight);
            DefaultLogger.d("TextEditDialog", "onGlobalLayout mCurrentHeight %d", Integer.valueOf(TextEditDialog.this.mCurrentHeight));
        }

        public /* synthetic */ void lambda$onGlobalLayout$0(View view) {
            TextEditDialog.this.dismissSafely();
        }
    }

    public static int getHeightDifference(View view, int i, Rect rect) {
        view.getWindowVisibleDisplayFrame(rect);
        return i - (rect.bottom - rect.top);
    }

    public final void checkMenuIndex(int i) {
        if (i == -1) {
            return;
        }
        this.mNavigationClickListener.onClick(this.mDialogSubMenuList.get(i).getNavigationButton());
    }

    public final void notifyKeyboardHeightChange() {
        if (this.mKeyBoardHeight == 0) {
            if (!Build.DEVICE.equals("cetus") && !this.mIsInMultiWindowMode.booleanValue()) {
                return;
            }
            this.mTabContainer.setVisibility(0);
            this.mTabContainer.setLayoutParams(new LinearLayout.LayoutParams(-1, (int) UIMsg.MSG_MAP_PANO_DATA));
            this.mContainerView.setAlpha(1.0f);
        } else if (this.mTabContainer.getVisibility() == 0 && this.mTabContainer.getHeight() == this.mKeyBoardHeight) {
        } else {
            this.mTabContainer.setVisibility(0);
            this.mTabContainer.setLayoutParams(new LinearLayout.LayoutParams(-1, this.mKeyBoardHeight));
            this.mContainerView.setAlpha(1.0f);
        }
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        for (DialogSubMenu dialogSubMenu : this.mDialogSubMenuList) {
            if (dialogSubMenu != null) {
                dialogSubMenu.release();
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
        StatusListener statusListener = this.mStatusListener;
        if (statusListener != null) {
            statusListener.onShow();
        }
        refreshColorSelectorSelection();
    }

    public final void notifyDismiss() {
        StatusListener statusListener = this.mStatusListener;
        if (statusListener != null) {
            statusListener.onDismiss();
        }
    }

    public final void notifyHeightChange(int i) {
        if (this.mStatusListener != null) {
            int height = this.mNavigationContainer.getHeight() + ((LinearLayout.LayoutParams) this.mEditView.getLayoutParams()).topMargin + this.mEditView.getHeight();
            int curDisplayHeight = ScreenUtils.getCurDisplayHeight(getActivity());
            this.mScreenHeight = curDisplayHeight;
            int i2 = (curDisplayHeight - i) - height;
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

    public void setStatusListener(StatusListener statusListener) {
        this.mStatusListener = statusListener;
    }

    public boolean isShowing() {
        Dialog dialog = getDialog();
        return dialog != null && dialog.isShowing();
    }

    public void setTabIndex(int i) {
        this.mTabIndex = i;
    }

    @Override // com.miui.gallery.editor.photo.app.HostAbility
    public void showInnerToast(String str) {
        this.mEditorToast.show(str, this.mWholeView, 80, (ScreenUtils.getCurDisplayWidth((Activity) getContext()) - this.mEditorToast.getToastWidth(str)) / 2, (-this.mEditorToast.getToastHeight()) - getResources().getDimensionPixelSize(R.dimen.editor_inner_toast_margin_bottom));
    }

    public void refreshColorSelectorSelection() {
        int i;
        DialogStatusData dialogStatusData = this.mInitializeData;
        int i2 = dialogStatusData.isSubstrate ? dialogStatusData.mSubstrateColors[0] : dialogStatusData.color;
        if (i2 == -16777216) {
            this.mColorSelectorView.setSelection(1, false);
            return;
        }
        int i3 = 0;
        while (true) {
            String[] strArr = this.mColorData;
            if (i3 >= strArr.length) {
                return;
            }
            String[] split = strArr[i3].split(",");
            if (split.length == 1 && Color.parseColor(this.mColorData[i3]) == i2) {
                this.mColorSelectorView.setSelection(i3, false);
                return;
            }
            DialogStatusData dialogStatusData2 = this.mInitializeData;
            if (dialogStatusData2.isSubstrate) {
                int[] iArr = dialogStatusData2.mSubstrateColors;
                if (iArr.length > 1) {
                    i = iArr[1];
                    if (Color.parseColor(split[0]) != i2 && Color.parseColor(split[1]) == i) {
                        this.mColorSelectorView.setSelection(i3, false);
                        return;
                    }
                    i3++;
                }
            }
            i = dialogStatusData2.gradientsColor;
            if (Color.parseColor(split[0]) != i2) {
            }
            i3++;
        }
    }
}
