package com.miui.gallery.editor.photo.core.imports.text;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.R;
import com.miui.gallery.assistant.jni.filter.BaiduSceneResult;
import com.miui.gallery.editor.photo.core.Metadata;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment;
import com.miui.gallery.editor.photo.core.common.model.TextData;
import com.miui.gallery.editor.photo.core.imports.text.TextEditorView;
import com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig;
import com.miui.gallery.editor.photo.core.imports.text.dialog.BaseDialogModel;
import com.miui.gallery.editor.photo.core.imports.text.dialog.DialogManager;
import com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog;
import com.miui.gallery.editor.photo.core.imports.text.model.DialogStatusData;
import com.miui.gallery.editor.photo.core.imports.text.signature.SignatureAppendConfig;
import com.miui.gallery.editor.photo.core.imports.text.signature.SignatureInfo;
import com.miui.gallery.editor.photo.core.imports.text.typeface.TextStyle;
import com.miui.gallery.editor.photo.core.imports.text.utils.AutoLineLayout;
import com.miui.gallery.editor.utils.EditorOrientationHelper;
import com.miui.gallery.signature.SignatureActivity;
import com.miui.gallery.signature.SignatureConfig;
import com.miui.gallery.signature.SignatureManageActivity;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class TextFragment extends AbstractEffectFragment {
    public String mBubbleText;
    public boolean mCurrentIsSignature;
    public TextStyle mCurrentTextStyle;
    public TextData mSignatureTextData;
    public TextEditDialog mTextEditDialog;
    public TextEditorView mTextEditorView;
    public TextRenderView mTextRenderView;
    public final int SIGNATURE_REQUEST_CODE = 120;
    public final int SIGNATURE_MANAGER_REQUEST_CODE = BaiduSceneResult.VISA;
    public StatusListener mStatusListener = new StatusListener();
    public SparseArray<DialogStatusData> mTextConfigDataArray = new SparseArray<>();
    public Map<String, Integer> mIndexMap = new HashMap();
    public int mCurrentIndex = 2;
    public int mSignaturePosition = -1;
    public boolean mSignatureClicked = false;
    public int mNewIndex = 2;
    public Handler mHandler = new Handler();
    public final StatisticLogger mStatisticLogger = new StatisticLogger() { // from class: com.miui.gallery.editor.photo.core.imports.text.TextFragment.1
        @Override // com.miui.gallery.editor.photo.core.imports.text.TextFragment.StatisticLogger
        public void statisticLog(String str) {
            TextFragment.this.getHostAbility().sample(str);
        }
    };
    public TextEditorView.TextEditorListener mTextEditorListener = new TextEditorView.TextEditorListener() { // from class: com.miui.gallery.editor.photo.core.imports.text.TextFragment.2
        @Override // com.miui.gallery.editor.photo.core.imports.text.TextEditorView.TextEditorListener
        public void onModify() {
        }

        @Override // com.miui.gallery.editor.photo.core.imports.text.TextEditorView.TextEditorListener
        public void onClear() {
            TextFragment.this.mCurrentIndex = -1;
            TextFragment.this.mTextEditorView.onClear();
        }

        public void onItemEdit() {
            FragmentActivity activity;
            if (!TextFragment.this.isDetached() && (activity = TextFragment.this.getActivity()) != null && !activity.isFinishing()) {
                DialogStatusData dialogStatusData = (DialogStatusData) TextFragment.this.mTextConfigDataArray.get(TextFragment.this.mCurrentIndex);
                if (!TextFragment.this.mCurrentIsSignature || dialogStatusData == null || dialogStatusData.mSignaturePath == null) {
                    if (TextFragment.this.mTextEditDialog.isShowing()) {
                        return;
                    }
                    String itemText = TextFragment.this.mTextEditorView.getItemText();
                    TextFragment.this.mTextEditDialog.setWillEditText(itemText, TextFragment.this.getString(R.string.text_append_hint).equals(itemText));
                    TextFragment textFragment = TextFragment.this;
                    DialogStatusData textConfigDataByIndex = textFragment.getTextConfigDataByIndex(textFragment.mCurrentIndex);
                    TextFragment.this.mTextEditorView.getCurrentItemStatus(textConfigDataByIndex);
                    TextFragment.this.mTextEditDialog.setInitializeData(textConfigDataByIndex);
                    TextFragment.this.mTextEditDialog.showAllowingStateLoss(TextFragment.this.getFragmentManager(), "TextEditDialog");
                    if (textConfigDataByIndex.mName == null) {
                        return;
                    }
                    TextFragment.this.mTextEditDialog.setIsShowSubstrateIcon(textConfigDataByIndex.mName.equals(DialogManager.LocalDialog.NONE.name()));
                } else if (TextFragment.this.mSignatureClicked) {
                } else {
                    TextFragment.this.startSignatureActivity(true);
                    TextFragment.this.mSignatureClicked = true;
                }
            }
        }

        @Override // com.miui.gallery.editor.photo.core.imports.text.TextEditorView.TextEditorListener
        public void onItemEdit(int i) {
            TextFragment.this.mTextEditDialog.setTabIndex(i);
            onItemEdit();
        }
    };
    public TextEditorView.CallBack mCallBack = new TextEditorView.CallBack() { // from class: com.miui.gallery.editor.photo.core.imports.text.TextFragment.3
        @Override // com.miui.gallery.editor.photo.core.imports.text.TextEditorView.CallBack
        public void onDeleteTextDialog() {
            if (TextFragment.this.mNewIndex == 0 || TextFragment.this.mNewIndex == TextFragment.this.mCurrentIndex) {
                TextFragment textFragment = TextFragment.this;
                TextFragment.this.mTextEditorView.getCurrentItemStatus(textFragment.getTextConfigDataByIndex(textFragment.mNewIndex));
            }
            if (!TextFragment.this.mTextEditorView.isEmpty() || TextFragment.this.mNewIndex != TextFragment.this.mCurrentIndex) {
                return;
            }
            TextFragment.this.mTextConfigDataArray.clear();
            TextFragment.this.onSelected(-1);
        }

        @Override // com.miui.gallery.editor.photo.core.imports.text.TextEditorView.CallBack
        public void onChangeSelection() {
            TextFragment textFragment = TextFragment.this;
            DialogStatusData textConfigDataByIndex = textFragment.getTextConfigDataByIndex(textFragment.mNewIndex);
            textConfigDataByIndex.mName = TextFragment.this.mTextEditorView.getItemTextDialogConfig().getName();
            if (TextFragment.this.mNewIndex == TextFragment.this.mCurrentIndex) {
                TextFragment.this.mTextEditorView.getCurrentItemStatus(textConfigDataByIndex);
            }
            if (textConfigDataByIndex.mName.equals(DialogManager.LocalDialog.CIRCULAR.name()) || textConfigDataByIndex.mName.equals(DialogManager.LocalDialog.RECTANGLE.name()) || textConfigDataByIndex.mName.equals(DialogManager.LocalDialog.RECT_HORIZONTAL.name()) || textConfigDataByIndex.mName.equals(DialogManager.LocalDialog.OVAL.name())) {
                int i = textConfigDataByIndex.color;
                if (i == -1) {
                    i = -16777216;
                }
                textConfigDataByIndex.color = i;
            }
            TextFragment.this.mTextEditDialog.setInitializeData(textConfigDataByIndex);
            if (TextFragment.this.mIndexMap.get(textConfigDataByIndex.mName) != null) {
                TextFragment textFragment2 = TextFragment.this;
                textFragment2.mCurrentIndex = ((Integer) textFragment2.mIndexMap.get(textConfigDataByIndex.mName)).intValue();
                TextFragment textFragment3 = TextFragment.this;
                textFragment3.onSelected(((Integer) textFragment3.mIndexMap.get(textConfigDataByIndex.mName)).intValue());
            }
        }
    };
    public TextWatcher mTextWatcher = new TextWatcher() { // from class: com.miui.gallery.editor.photo.core.imports.text.TextFragment.4
        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            TextFragment.this.mTextEditorView.setItemText(charSequence.toString());
            TextFragment.this.mStatusListener.onTextChange();
        }
    };

    /* loaded from: classes2.dex */
    public interface StatisticLogger {
        void statisticLog(String str);
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public void clear() {
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment
    public void remove(Metadata metadata) {
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment
    public void render() {
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        TextRenderView textRenderView = new TextRenderView(layoutInflater.getContext());
        this.mTextRenderView = textRenderView;
        return textRenderView;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mTextEditorView = (TextEditorView) view.findViewById(R.id.text_editor_view);
        TextEditDialog textEditDialog = new TextEditDialog();
        this.mTextEditDialog = textEditDialog;
        textEditDialog.setIsShowSubstrateIcon(true);
        this.mTextEditDialog.setConfigChangeListener(new ConfigListener());
        this.mTextEditorView.setTextEditorListener(this.mTextEditorListener);
        this.mTextEditDialog.setTextWatch(this.mTextWatcher);
        this.mTextEditDialog.setStatusListener(this.mStatusListener);
        this.mTextEditorView.setCallBack(this.mCallBack);
        this.mTextEditorView.setBitmap(getBitmap());
        this.mTextEditorView.setStatisticLogger(this.mStatisticLogger);
        this.mIndexMap.put(DialogManager.LocalDialog.NONE.name(), Integer.valueOf(this.mNewIndex));
    }

    @Override // com.miui.gallery.app.fragment.AndroidFragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mTextEditDialog.onConfigurationChanged(configuration);
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment, com.miui.gallery.editor.utils.LayoutOrientationTracker.OnLayoutOrientationChangeListener
    public void onLayoutOrientationChange() {
        super.onLayoutOrientationChange();
        EditorOrientationHelper.copyLayoutParams(new TextRenderView(getContext()), getView(), true);
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public boolean isEmpty() {
        return this.mTextEditorView.isEmpty();
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public List<String> onSample() {
        ArrayList arrayList = new ArrayList();
        ITextDialogConfig itemTextDialogConfig = this.mTextEditorView.getItemTextDialogConfig();
        if (itemTextDialogConfig != null) {
            arrayList.add(itemTextDialogConfig.getSampleName());
        }
        if (this.mCurrentTextStyle != null) {
            arrayList.add("font_" + this.mCurrentTextStyle.getName());
        }
        arrayList.add("color_" + Integer.toHexString(this.mTextEditorView.getItemColor()));
        if (this.mTextEditorView.getItemBold()) {
            arrayList.add("bold");
        }
        if (this.mTextEditorView.getItemShadow()) {
            arrayList.add("shadow");
        }
        AutoLineLayout.TextAlignment itemAlignment = this.mTextEditorView.getItemAlignment();
        if (itemAlignment != AutoLineLayout.TextAlignment.LEFT) {
            arrayList.add("align_" + itemAlignment);
        }
        float itemTransparent = this.mTextEditorView.getItemTransparent();
        if (itemTransparent > 0.0f) {
            DecimalFormat decimalFormat = new DecimalFormat(".0");
            arrayList.add("transparent_" + decimalFormat.format(itemTransparent));
        }
        if (itemTextDialogConfig instanceof SignatureAppendConfig) {
            arrayList.add("useDate_" + ((SignatureAppendConfig) itemTextDialogConfig).isShowTimeStamp());
        }
        return arrayList;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderFragment
    public RenderData onExport() {
        return new TextRenderData(this.mTextEditorView.export());
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment
    public void add(Metadata metadata, Object obj) {
        if (!isAdded()) {
            return;
        }
        int intValue = ((Integer) obj).intValue();
        this.mNewIndex = intValue;
        TextConfig textConfig = (TextConfig) metadata;
        if (intValue == this.mCurrentIndex && !this.mTextEditorView.isEmpty()) {
            return;
        }
        if (intValue != this.mSignaturePosition) {
            this.mCurrentIsSignature = false;
        }
        DialogStatusData textConfigDataByIndex = getTextConfigDataByIndex(this.mCurrentIndex);
        this.mTextEditorView.getDialogStatusData(textConfigDataByIndex);
        ITextDialogConfig itemTextDialogConfig = this.mTextEditorView.getItemTextDialogConfig();
        if (itemTextDialogConfig != null && !itemTextDialogConfig.isWatermark() && !this.mCurrentIsSignature) {
            this.mBubbleText = textConfigDataByIndex.text;
        }
        this.mTextEditorView.removeLastOperationItem();
        this.mTextEditorView.addNewItem(textConfig);
        this.mIndexMap.put(textConfig.name, Integer.valueOf(intValue));
        if (!this.mTextEditorView.isItemActivation()) {
            this.mTextEditorView.setLastItemActivation();
        }
        performSetDialog(textConfig, intValue);
        this.mTextEditDialog.setIsShowSubstrateIcon(textConfig.name.equals(DialogManager.LocalDialog.NONE.name()));
    }

    public final void performSetDialog(TextConfig textConfig, int i) {
        boolean z;
        BaseDialogModel baseDialogModel = textConfig.getBaseDialogModel();
        DialogStatusData dialogStatusData = this.mTextConfigDataArray.get(i);
        if (dialogStatusData == null) {
            dialogStatusData = getTextConfigDataByIndex(i);
            dialogStatusData.mIsWatermark = textConfig.isWatermark();
            if (textConfig.isWatermark()) {
                dialogStatusData.watermarkInitSelf(textConfig.getWatermarkInfo());
            } else if (textConfig.isSignature()) {
                dialogStatusData.configSignatureSelfByInit(textConfig.getSignatureInfo());
            } else {
                dialogStatusData.configSelfByInit(baseDialogModel);
            }
            z = true;
        } else {
            z = false;
        }
        if (!textConfig.isWatermark() && !textConfig.isSignature()) {
            dialogStatusData.text = this.mBubbleText;
        }
        if (textConfig.isSignature() && !TextUtils.isEmpty(textConfig.getSignatureInfo().path)) {
            dialogStatusData.mSignaturePath = textConfig.getSignatureInfo().path;
        }
        this.mTextEditorView.enableStatusForCurrentItem(dialogStatusData, z);
        this.mCurrentIndex = i;
        if (textConfig.isSignature() && !TextUtils.isEmpty(textConfig.getSignatureInfo().path)) {
            this.mTextEditorView.setSignaturePath(textConfig.getSignatureInfo().path);
        } else {
            this.mTextEditorView.setSignaturePath(null);
        }
        this.mTextEditorView.setItemDialogModel(baseDialogModel);
    }

    /* loaded from: classes2.dex */
    public class StatusListener implements TextEditDialog.StatusListener {
        public int mDialogBottom;

        public StatusListener() {
        }

        @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog.StatusListener
        public void onShow() {
            TextFragment.this.mTextRenderView.getTopView().setVisibility(8);
            TextFragment.this.mTextEditorView.setIsMantle(true);
            TextFragment.this.mTextEditorView.setClipCanvas(false);
        }

        @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog.StatusListener
        public void onDismiss() {
            TextFragment.this.mTextEditorView.setIsBoundBox(true);
            TextFragment.this.mTextEditorView.setIsMantle(false);
            TextFragment.this.mTextEditorView.setClipCanvas(true);
            TextFragment.this.mTextRenderView.getTopView().setVisibility(0);
            TextFragment.this.mTextEditorView.refreshTextDialog();
        }

        @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog.StatusListener
        public void onBottomChange(int i) {
            this.mDialogBottom = i;
            DefaultLogger.d("TextFragment", "onBottomChange: %d", Integer.valueOf(i));
            int activationItemBottom = TextFragment.this.mTextEditorView.getActivationItemBottom();
            DefaultLogger.d("TextFragment", "text bottom: %d", Integer.valueOf(activationItemBottom));
            if (activationItemBottom > i) {
                TextFragment.this.mTextEditorView.offsetWithAnimator(i - activationItemBottom);
            } else {
                TextFragment.this.mTextEditorView.offsetWithAnimator(0.0f);
            }
        }

        public void onTextChange() {
            onBottomChange(this.mDialogBottom);
        }
    }

    /* loaded from: classes2.dex */
    public class ConfigListener implements TextEditDialog.ConfigChangeListener {
        public int[] mColor;

        public ConfigListener() {
            this.mColor = new int[]{-1};
        }

        @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog.ConfigChangeListener
        public void onColorChange(int i) {
            TextFragment.this.mTextEditorView.setItemTextColor(i);
        }

        @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog.ConfigChangeListener
        public void onColorChange(int... iArr) {
            this.mColor = iArr;
            if (TextFragment.this.mTextEditorView.isSubstrate()) {
                TextFragment.this.mTextEditorView.setSubstrateColors(iArr);
                int i = -1;
                if (iArr[0] == -1) {
                    i = -16777216;
                }
                onColorChange(i);
            } else if (iArr.length > 1) {
                TextFragment.this.mTextEditorView.setItemTextColor(iArr[0], iArr[1]);
            } else {
                onColorChange(iArr[0]);
            }
        }

        @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog.ConfigChangeListener
        public void onTransparentChange(int i) {
            TextFragment.this.mTextEditorView.setItemTransparent(i / 100.0f);
        }

        @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog.ConfigChangeListener
        public void onShadowChange(boolean z) {
            TextFragment.this.mTextEditorView.setItemShadow(z);
        }

        @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog.ConfigChangeListener
        public void onTextAlignChange(AutoLineLayout.TextAlignment textAlignment) {
            TextFragment.this.mTextEditorView.setItemTextAlignment(textAlignment);
        }

        @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog.ConfigChangeListener
        public void onBoldChange(boolean z) {
            TextFragment.this.mTextEditorView.setItemBold(z);
        }

        @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog.ConfigChangeListener
        public void onTypefaceChange(TextStyle textStyle) {
            TextFragment.this.mCurrentTextStyle = textStyle;
            TextFragment.this.mTextEditorView.setItemTypeface(textStyle);
        }

        @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog.ConfigChangeListener
        public void onSubstrateChange(boolean z) {
            TextFragment.this.mTextEditorView.setIsSubstrate(z);
            if (!z) {
                onColorChange(this.mColor);
            }
        }

        @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog.ConfigChangeListener
        public void onStrokeChange(boolean z) {
            TextFragment.this.mTextEditorView.setIsStroke(z);
        }
    }

    public DialogStatusData getTextConfigDataByIndex(int i) {
        DialogStatusData dialogStatusData = this.mTextConfigDataArray.get(i);
        if (dialogStatusData == null) {
            DialogStatusData dialogStatusData2 = new DialogStatusData();
            dialogStatusData2.setEmpty();
            this.mTextConfigDataArray.put(i, dialogStatusData2);
            return dialogStatusData2;
        }
        return dialogStatusData;
    }

    @Override // com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment
    public void addSignature(Metadata metadata, Object obj) {
        if (metadata != null && !this.mSignatureClicked) {
            this.mSignatureTextData = (TextConfig) metadata;
            this.mSignaturePosition = ((Integer) obj).intValue();
            if (SignatureConfig.getSignatureFolder(getContext()).list().length < 1) {
                startSignatureActivity(false);
            } else {
                startSignatureManageActivity();
            }
            this.mSignatureClicked = true;
        }
    }

    public final void startSignatureActivity(boolean z) {
        Intent intent = new Intent(getActivity(), SignatureActivity.class);
        if (z) {
            String currentSignaturePath = getCurrentSignaturePath(this.mCurrentIndex);
            if (!TextUtils.isEmpty(currentSignaturePath) && new File(currentSignaturePath).exists()) {
                intent.putExtra("param_signature_path", currentSignaturePath);
            }
        }
        intent.putExtra("param_signature_is_edit", z);
        startActivityForResult(intent, 120);
    }

    public final void startSignatureManageActivity() {
        Intent intent = new Intent(getActivity(), SignatureManageActivity.class);
        String currentSignaturePath = getCurrentSignaturePath(this.mCurrentIndex);
        if (this.mCurrentIsSignature && !TextUtils.isEmpty(currentSignaturePath) && new File(currentSignaturePath).exists()) {
            intent.putExtra("param_current_signature_path", currentSignaturePath);
        }
        startActivityForResult(intent, BaiduSceneResult.VISA);
    }

    public final String getCurrentSignaturePath(int i) {
        DialogStatusData dialogStatusData = this.mTextConfigDataArray.get(i);
        if (dialogStatusData != null) {
            return dialogStatusData.mSignaturePath;
        }
        return null;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1) {
            if (i == 120) {
                int intExtra = intent.getIntExtra("result_param_add_signature_result_code", -1);
                String stringExtra = intent.getStringExtra("result_param_signature_path");
                if (intExtra == 101) {
                    deleteAndRemoveCurrentSignature(stringExtra);
                    this.mTextEditorView.clear();
                    this.mCurrentIsSignature = false;
                } else if (intExtra == 102) {
                    setSignature(stringExtra);
                }
            } else if (i == 130) {
                int intExtra2 = intent.getIntExtra("result_param_signature_result_code", -1);
                String stringExtra2 = intent.getStringExtra("result_param_signature_path");
                if (intent.getBooleanExtra("RESULT_PARAM_CURRENT_SIGNATURE_IS_DELETE", false)) {
                    this.mTextEditorView.clear();
                }
                if (intExtra2 == 1) {
                    this.mHandler.postDelayed(new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.text.TextFragment.5
                        @Override // java.lang.Runnable
                        public void run() {
                            TextFragment.this.startSignatureActivity(false);
                        }
                    }, 200L);
                } else if (intExtra2 == 2) {
                    setSignature(stringExtra2);
                }
            }
        }
        this.mSignatureClicked = false;
    }

    public final void deleteAndRemoveCurrentSignature(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.DELETE, FileHandleRecordHelper.appendInvokerTag("TextFragment", "deleteAndRemoveCurrentSignature"));
        if (documentFile == null) {
            return;
        }
        documentFile.delete();
    }

    public final void setSignature(String str) {
        TextData textData;
        TextConfig textConfig;
        SignatureInfo signatureInfo;
        if (!TextUtils.isEmpty(str) && (textData = this.mSignatureTextData) != null && (textData instanceof TextConfig) && (signatureInfo = (textConfig = (TextConfig) textData).getSignatureInfo()) != null) {
            signatureInfo.path = str;
            DialogStatusData dialogStatusData = this.mTextConfigDataArray.get(this.mSignaturePosition);
            if (dialogStatusData != null) {
                dialogStatusData.mSignaturePath = str;
            }
            if (this.mCurrentIsSignature && !this.mTextEditorView.isEmpty()) {
                this.mTextEditorView.updateItemSignature(str, this.mSignaturePosition);
                return;
            }
            this.mCurrentIsSignature = true;
            add(textConfig, Integer.valueOf(this.mSignaturePosition));
            onSelected(this.mCurrentIndex);
        }
    }
}
