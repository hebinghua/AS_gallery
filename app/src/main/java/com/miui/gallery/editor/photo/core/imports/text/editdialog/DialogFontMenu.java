package com.miui.gallery.editor.photo.core.imports.text.editdialog;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.GridLayoutManager;
import com.miui.gallery.R;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureHandler;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.editor.photo.core.imports.text.TextFontConfig;
import com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog;
import com.miui.gallery.editor.photo.core.imports.text.model.DialogStatusData;
import com.miui.gallery.editor.photo.core.imports.text.textstyle.TextEditStyleItemDecoration;
import com.miui.gallery.editor.photo.core.imports.text.typeface.DownloadCallback;
import com.miui.gallery.editor.photo.core.imports.text.typeface.FontDownloadManager;
import com.miui.gallery.editor.photo.core.imports.text.typeface.FontResourceRequest;
import com.miui.gallery.editor.photo.core.imports.text.typeface.TextStyle;
import com.miui.gallery.editor.photo.core.imports.text.typeface.TypeFaceAdapter;
import com.miui.gallery.editor.photo.core.imports.text.utils.TextTools;
import com.miui.gallery.net.base.ErrorCode;
import com.miui.gallery.net.base.ResponseListener;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.recyclerview.SingleChoiceRecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class DialogFontMenu extends DialogSubMenu<TextEditDialog.ConfigChangeListener, DialogStatusData> {
    public Context mContext;
    public TextStyle mCurrentTextStyle;
    public FontDownloadManager mFontDownloadManager;
    public FontResourceRequest mFontRequest;
    public DialogStatusData mInitializeData;
    public int mPosition;
    public SingleChoiceRecyclerView mRecyclerView;
    public TextEditStyleItemDecoration mTextEditStyleItemDecoration;
    public List<TextStyle> mTextStyles;
    public TypeFaceAdapter mTypeFaceAdapter;
    public boolean mTypeFaceInited;
    public ViewGroup mWholeView;

    /* JADX WARN: Multi-variable type inference failed */
    public DialogFontMenu(Context context, ViewGroup viewGroup, TextEditDialog.ConfigChangeListener configChangeListener) {
        super(context, viewGroup, R.string.text_edit_dialog_font, R.drawable.text_edit_dialog_tab_icon_font);
        this.mTypeFaceInited = false;
        this.mContext = context;
        this.mListener = configChangeListener;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogSubMenu
    public ViewGroup initSubMenuView(Context context, ViewGroup viewGroup) {
        TextFontConfig.init(context);
        this.mWholeView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.text_edit_dialog_options_font_panel, viewGroup, false);
        DisplayMetrics displayMetrics = StaticContext.sGetAndroidContext().getResources().getDisplayMetrics();
        this.mRecyclerView = (SingleChoiceRecyclerView) this.mWholeView.findViewById(R.id.text_edit_dialog_tab_font_recycler);
        if (Build.DEVICE.equals("cetus") || displayMetrics.widthPixels > 1080) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
            layoutParams.addRule(14);
            this.mRecyclerView.setLayoutParams(layoutParams);
        }
        TextEditStyleItemDecoration textEditStyleItemDecoration = new TextEditStyleItemDecoration(context.getResources().getInteger(R.integer.text_dialog_font_span_count), context.getResources().getDimensionPixelSize(R.dimen.text_edit_dialog_tab_font_recycler_item_spaces), context.getResources().getDimensionPixelSize(R.dimen.text_edit_dialog_tab_font_recycler_item_spaces));
        this.mTextEditStyleItemDecoration = textEditStyleItemDecoration;
        this.mRecyclerView.addItemDecoration(textEditStyleItemDecoration);
        ArrayList arrayList = new ArrayList();
        this.mTextStyles = arrayList;
        arrayList.add(TextStyle.getLocalTextStyle());
        this.mTypeFaceAdapter = new TypeFaceAdapter(context, this.mTextStyles);
        this.mRecyclerView.setLayoutManager(new GridLayoutManager(context, context.getResources().getInteger(R.integer.text_dialog_font_span_count)));
        this.mRecyclerView.setAdapter(this.mTypeFaceAdapter);
        this.mTypeFaceAdapter.setItemSelectChangeListener(new TypeFaceItemSelectChangeListener());
        loadResourceData();
        return this.mWholeView;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogSubMenu
    public void OnConfigurationChanged() {
        super.OnConfigurationChanged();
        this.mTextEditStyleItemDecoration.updateItemDecoration(this.mContext.getResources().getInteger(R.integer.text_dialog_font_span_count), this.mContext.getResources().getDimensionPixelSize(R.dimen.text_edit_dialog_tab_font_recycler_item_spaces), 0);
        this.mRecyclerView.invalidateItemDecorations();
    }

    public final void updateSelectedItemPosition(int i) {
        if (i != this.mTypeFaceAdapter.getSelectedItemPosition()) {
            this.mTypeFaceAdapter.setSelectedItemPosition(i);
            this.mTypeFaceAdapter.clearLastSelectedPostion();
        }
    }

    public final void downloadResource(Context context, final TextStyle textStyle, final int i) {
        if (this.mFontDownloadManager == null) {
            this.mFontDownloadManager = new FontDownloadManager();
        }
        this.mFontDownloadManager.downloadFontResource(context, textStyle, new DownloadCallback() { // from class: com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogFontMenu.1
            @Override // com.miui.gallery.editor.photo.core.imports.text.typeface.DownloadCallback
            public void onCompleted(boolean z) {
                DefaultLogger.d("DialogFontMenu", "%s download is : %s", textStyle.label, Boolean.valueOf(z));
                if (z) {
                    ThreadManager.getMiscPool().submit(new ThreadPool.Job<Object>() { // from class: com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogFontMenu.1.1
                        @Override // com.miui.gallery.concurrent.ThreadPool.Job
                        /* renamed from: run */
                        public Object mo1807run(ThreadPool.JobContext jobContext) {
                            String filePath = textStyle.getFilePath();
                            DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(filePath, IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag("DialogFontMenu", "onCompleted"));
                            if (documentFile != null && documentFile.exists()) {
                                textStyle.setTypeFace(Typeface.createFromFile(filePath));
                            }
                            return null;
                        }
                    }, new FutureHandler<Object>() { // from class: com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogFontMenu.1.2
                        @Override // com.miui.gallery.concurrent.FutureHandler
                        public void onPostExecute(Future<Object> future) {
                            textStyle.setState(0);
                            AnonymousClass1 anonymousClass1 = AnonymousClass1.this;
                            DialogFontMenu.this.notifyItemChanged(i);
                            DialogFontMenu dialogFontMenu = DialogFontMenu.this;
                            dialogFontMenu.updateSelectedItemPosition(dialogFontMenu.mPosition);
                            DialogFontMenu dialogFontMenu2 = DialogFontMenu.this;
                            T t = dialogFontMenu2.mListener;
                            if (t != 0) {
                                ((TextEditDialog.ConfigChangeListener) t).onTypefaceChange(dialogFontMenu2.mCurrentTextStyle);
                            }
                        }
                    });
                    return;
                }
                textStyle.setState(20);
                DialogFontMenu.this.notifyItemChanged(i);
            }

            @Override // com.miui.gallery.editor.photo.core.imports.text.typeface.DownloadCallback
            public void onStart() {
                textStyle.setState(18);
                DialogFontMenu.this.notifyItemChanged(i);
            }
        });
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogSubMenu
    public void initializeData(DialogStatusData dialogStatusData) {
        this.mInitializeData = dialogStatusData;
        if (!this.mTypeFaceInited || dialogStatusData == null) {
            return;
        }
        TextStyle textStyle = dialogStatusData.textStyle;
        Typeface typeFace = textStyle == null ? Typeface.DEFAULT : textStyle.getTypeFace();
        if (this.mTypeFaceAdapter == null) {
            return;
        }
        for (int i = 0; i < this.mTextStyles.size(); i++) {
            if (isEquals(typeFace, this.mTextStyles.get(i).getTypeFace())) {
                updateSelectedItemPosition(i);
                this.mTypeFaceAdapter.notifyDataSetChanged();
            }
        }
    }

    public final void loadResourceData() {
        if (!TextTools.isZhCNLanguage()) {
            TextTools.checkResourceExist(this.mTextStyles);
            this.mTypeFaceAdapter.notifyDataSetChanged();
            return;
        }
        DefaultLogger.d("DialogFontMenu", "loadResourceData start.");
        final long currentTimeMillis = System.currentTimeMillis();
        FontResourceRequest fontResourceRequest = new FontResourceRequest();
        this.mFontRequest = fontResourceRequest;
        fontResourceRequest.execute(new ResponseListener() { // from class: com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogFontMenu.2
            @Override // com.miui.gallery.net.base.ResponseListener
            public void onResponse(Object... objArr) {
                DefaultLogger.d("DialogFontMenu", "loadResourceData success, use time %s  ms", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                final List list = (List) objArr[0];
                if (!BaseMiscUtil.isValid(list)) {
                    return;
                }
                Collections.sort(list);
                list.add(0, TextStyle.getLocalTextStyle());
                TextTools.checkResourceExist(list);
                ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogFontMenu.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        DialogFontMenu.this.mTextStyles.clear();
                        DialogFontMenu.this.mTextStyles.addAll(list);
                        DialogFontMenu.this.mTypeFaceInited = true;
                        DialogFontMenu.this.mTypeFaceAdapter.notifyDataSetChanged();
                        DialogFontMenu dialogFontMenu = DialogFontMenu.this;
                        dialogFontMenu.initializeData(dialogFontMenu.mInitializeData);
                    }
                });
            }

            @Override // com.miui.gallery.net.base.ResponseListener
            public void onResponseError(ErrorCode errorCode, String str, Object obj) {
                DefaultLogger.d("DialogFontMenu", "loadResourceData error: %s, errorCode: %s", str, errorCode.toString());
                final ArrayList arrayList = new ArrayList();
                arrayList.add(TextStyle.getLocalTextStyle());
                TextTools.checkResourceExist(arrayList);
                ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogFontMenu.2.2
                    @Override // java.lang.Runnable
                    public void run() {
                        DialogFontMenu.this.mTextStyles.clear();
                        DialogFontMenu.this.mTextStyles.addAll(arrayList);
                        if (DialogFontMenu.this.mTypeFaceAdapter != null) {
                            DialogFontMenu.this.mTypeFaceAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogSubMenu
    public void release() {
        TextStyle next;
        FontResourceRequest fontResourceRequest = this.mFontRequest;
        if (fontResourceRequest != null) {
            fontResourceRequest.cancel();
        }
        FontDownloadManager fontDownloadManager = this.mFontDownloadManager;
        if (fontDownloadManager != null) {
            fontDownloadManager.cancelAll();
        }
        if (BaseMiscUtil.isValid(this.mTextStyles)) {
            Iterator<TextStyle> it = this.mTextStyles.iterator();
            while (it.hasNext() && (next = it.next()) != null) {
                if (next.isDownloading()) {
                    next.setState(20);
                }
            }
        }
    }

    public final boolean isEquals(Typeface typeface, Typeface typeface2) {
        return (typeface == null && typeface2 == null) || (typeface != null && typeface.equals(typeface2));
    }

    public void notifyItemChanged(int i) {
        TypeFaceAdapter typeFaceAdapter = this.mTypeFaceAdapter;
        if (typeFaceAdapter != null) {
            typeFaceAdapter.notifyItemChanged(i, 1);
        }
    }

    /* loaded from: classes2.dex */
    public class TypeFaceItemSelectChangeListener implements SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter.ItemSelectChangeListener {
        public TypeFaceItemSelectChangeListener() {
        }

        @Override // com.miui.gallery.widget.recyclerview.SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter.ItemSelectChangeListener
        public boolean onItemSelect(SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter singleChoiceRecyclerViewAdapter, int i, boolean z) {
            DialogFontMenu dialogFontMenu = DialogFontMenu.this;
            dialogFontMenu.mCurrentTextStyle = (TextStyle) dialogFontMenu.mTextStyles.get(i);
            DialogFontMenu.this.mPosition = i;
            if (DialogFontMenu.this.mCurrentTextStyle.isLocal()) {
                DialogFontMenu.this.updateSelectedItemPosition(i);
                DialogFontMenu dialogFontMenu2 = DialogFontMenu.this;
                T t = dialogFontMenu2.mListener;
                if (t == 0) {
                    return true;
                }
                ((TextEditDialog.ConfigChangeListener) t).onTypefaceChange(dialogFontMenu2.mCurrentTextStyle);
                return true;
            } else if (!DialogFontMenu.this.mCurrentTextStyle.isExtra()) {
                return true;
            } else {
                if (!DialogFontMenu.this.mCurrentTextStyle.isNeedDownload()) {
                    if (!DialogFontMenu.this.mCurrentTextStyle.isDownloaded()) {
                        return true;
                    }
                    DialogFontMenu.this.updateSelectedItemPosition(i);
                    DialogFontMenu dialogFontMenu3 = DialogFontMenu.this;
                    T t2 = dialogFontMenu3.mListener;
                    if (t2 == 0) {
                        return true;
                    }
                    ((TextEditDialog.ConfigChangeListener) t2).onTypefaceChange(dialogFontMenu3.mCurrentTextStyle);
                    return true;
                }
                DialogFontMenu dialogFontMenu4 = DialogFontMenu.this;
                dialogFontMenu4.downloadResource(dialogFontMenu4.mContext, DialogFontMenu.this.mCurrentTextStyle, i);
                return true;
            }
        }
    }
}
