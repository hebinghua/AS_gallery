package com.miui.gallery.editor.photo.core.imports.text.editdialog;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.miui.gallery.R;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureHandler;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.editor.photo.core.imports.text.TextFontConfig;
import com.miui.gallery.editor.photo.core.imports.text.editdialog.TextEditDialog;
import com.miui.gallery.editor.photo.core.imports.text.model.DialogStatusData;
import com.miui.gallery.editor.photo.core.imports.text.typeface.DownloadCallback;
import com.miui.gallery.editor.photo.core.imports.text.typeface.FontDownloadManager;
import com.miui.gallery.editor.photo.core.imports.text.typeface.FontResourceRequestOld;
import com.miui.gallery.editor.photo.core.imports.text.typeface.TextStyle;
import com.miui.gallery.editor.photo.core.imports.text.typeface.TypeFaceAdapterOld;
import com.miui.gallery.editor.photo.core.imports.text.utils.TextTools;
import com.miui.gallery.net.base.ErrorCode;
import com.miui.gallery.net.base.ResponseListener;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.recyclerview.SingleChoiceRecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class DialogFontMenuOld extends DialogSubMenuOld<TextEditDialog.ConfigChangeListener, DialogStatusData> {
    public Context mContext;
    public TextStyle mCurrentTextStyle;
    public FontDownloadManager mFontDownloadManager;
    public FontResourceRequestOld mFontRequest;
    public DialogStatusData mInitializeData;
    public SingleChoiceRecyclerView mRecyclerView;
    public List<TextStyle> mTextStyles;
    public TypeFaceAdapterOld mTypeFaceAdapter;
    public boolean mTypeFaceInited;
    public ViewGroup mWholeView;

    /* JADX WARN: Multi-variable type inference failed */
    public DialogFontMenuOld(Context context, ViewGroup viewGroup, TextEditDialog.ConfigChangeListener configChangeListener) {
        super(context, viewGroup, R.string.text_edit_dialog_font, R.drawable.text_edit_dialog_tab_icon_font_old);
        this.mTypeFaceInited = false;
        this.mContext = context;
        this.mListener = configChangeListener;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogSubMenuOld
    public ViewGroup initSubMenuView(Context context, ViewGroup viewGroup) {
        TextFontConfig.init(context);
        ViewGroup viewGroup2 = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.text_edit_dialog_options_font_panel_old, viewGroup, false);
        this.mWholeView = viewGroup2;
        this.mRecyclerView = (SingleChoiceRecyclerView) viewGroup2.findViewById(R.id.text_edit_dialog_tab_font_recycler);
        ArrayList arrayList = new ArrayList();
        this.mTextStyles = arrayList;
        arrayList.add(TextStyle.getLocalTextStyle());
        this.mTypeFaceAdapter = new TypeFaceAdapterOld(context, this.mTextStyles);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        this.mRecyclerView.setAdapter(this.mTypeFaceAdapter);
        this.mTypeFaceAdapter.setItemSelectChangeListener(new TypeFaceItemSelectChangeListener());
        loadResourceData();
        return this.mWholeView;
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
        this.mFontDownloadManager.downloadFontResource(context, textStyle, new DownloadCallback() { // from class: com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogFontMenuOld.1
            @Override // com.miui.gallery.editor.photo.core.imports.text.typeface.DownloadCallback
            public void onCompleted(boolean z) {
                DefaultLogger.d("DialogFontMenu", "%s download is : %s", textStyle.label, Boolean.valueOf(z));
                if (z) {
                    ThreadManager.getMiscPool().submit(new ThreadPool.Job<Object>() { // from class: com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogFontMenuOld.1.1
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
                    }, new FutureHandler<Object>() { // from class: com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogFontMenuOld.1.2
                        @Override // com.miui.gallery.concurrent.FutureHandler
                        public void onPostExecute(Future<Object> future) {
                            textStyle.setState(0);
                            AnonymousClass1 anonymousClass1 = AnonymousClass1.this;
                            DialogFontMenuOld.this.notifyItemChanged(i);
                        }
                    });
                    return;
                }
                textStyle.setState(20);
                DialogFontMenuOld.this.notifyItemChanged(i);
            }

            @Override // com.miui.gallery.editor.photo.core.imports.text.typeface.DownloadCallback
            public void onStart() {
                textStyle.setState(18);
                DialogFontMenuOld.this.notifyItemChanged(i);
            }
        });
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogSubMenuOld
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
        FontResourceRequestOld fontResourceRequestOld = new FontResourceRequestOld();
        this.mFontRequest = fontResourceRequestOld;
        fontResourceRequestOld.execute(new ResponseListener() { // from class: com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogFontMenuOld.2
            @Override // com.miui.gallery.net.base.ResponseListener
            public void onResponse(Object... objArr) {
                DefaultLogger.d("DialogFontMenu", "loadResourceData success, use time %s  ms", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                List<TextStyle> list = (List) objArr[0];
                if (!BaseMiscUtil.isValid(list)) {
                    return;
                }
                Collections.sort(list);
                boolean isNightMode = MiscUtil.isNightMode(DialogFontMenuOld.this.mContext);
                final ArrayList arrayList = new ArrayList();
                arrayList.add(TextStyle.getLocalTextStyle());
                for (TextStyle textStyle : list) {
                    if (!isNightMode && !textStyle.isDarkModeData()) {
                        arrayList.add(textStyle);
                    }
                    if (isNightMode && textStyle.isDarkModeData()) {
                        arrayList.add(textStyle);
                    }
                }
                TextTools.checkResourceExist(arrayList);
                ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogFontMenuOld.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        DialogFontMenuOld.this.mTextStyles.clear();
                        DialogFontMenuOld.this.mTextStyles.addAll(arrayList);
                        DialogFontMenuOld.this.mTypeFaceInited = true;
                        DialogFontMenuOld.this.mTypeFaceAdapter.notifyDataSetChanged();
                        DialogFontMenuOld dialogFontMenuOld = DialogFontMenuOld.this;
                        dialogFontMenuOld.initializeData(dialogFontMenuOld.mInitializeData);
                    }
                });
            }

            @Override // com.miui.gallery.net.base.ResponseListener
            public void onResponseError(ErrorCode errorCode, String str, Object obj) {
                DefaultLogger.d("DialogFontMenu", "loadResourceData error: %s, errorCode: %s", str, errorCode.toString());
                final ArrayList arrayList = new ArrayList();
                arrayList.add(TextStyle.getLocalTextStyle());
                TextTools.checkResourceExist(arrayList);
                ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogFontMenuOld.2.2
                    @Override // java.lang.Runnable
                    public void run() {
                        DialogFontMenuOld.this.mTextStyles.clear();
                        DialogFontMenuOld.this.mTextStyles.addAll(arrayList);
                        if (DialogFontMenuOld.this.mTypeFaceAdapter != null) {
                            DialogFontMenuOld.this.mTypeFaceAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.editdialog.DialogSubMenuOld
    public void release() {
        TextStyle next;
        FontResourceRequestOld fontResourceRequestOld = this.mFontRequest;
        if (fontResourceRequestOld != null) {
            fontResourceRequestOld.cancel();
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
        TypeFaceAdapterOld typeFaceAdapterOld = this.mTypeFaceAdapter;
        if (typeFaceAdapterOld != null) {
            typeFaceAdapterOld.notifyItemChanged(i, 1);
        }
    }

    /* loaded from: classes2.dex */
    public class TypeFaceItemSelectChangeListener implements SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter.ItemSelectChangeListener {
        public TypeFaceItemSelectChangeListener() {
        }

        @Override // com.miui.gallery.widget.recyclerview.SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter.ItemSelectChangeListener
        public boolean onItemSelect(SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter singleChoiceRecyclerViewAdapter, int i, boolean z) {
            DialogFontMenuOld dialogFontMenuOld = DialogFontMenuOld.this;
            dialogFontMenuOld.mCurrentTextStyle = (TextStyle) dialogFontMenuOld.mTextStyles.get(i);
            if (DialogFontMenuOld.this.mCurrentTextStyle.isLocal()) {
                DialogFontMenuOld.this.updateSelectedItemPosition(i);
                DialogFontMenuOld dialogFontMenuOld2 = DialogFontMenuOld.this;
                T t = dialogFontMenuOld2.mListener;
                if (t == 0) {
                    return true;
                }
                ((TextEditDialog.ConfigChangeListener) t).onTypefaceChange(dialogFontMenuOld2.mCurrentTextStyle);
                return true;
            } else if (!DialogFontMenuOld.this.mCurrentTextStyle.isExtra()) {
                return true;
            } else {
                if (!DialogFontMenuOld.this.mCurrentTextStyle.isNeedDownload()) {
                    if (!DialogFontMenuOld.this.mCurrentTextStyle.isDownloaded()) {
                        return true;
                    }
                    DialogFontMenuOld.this.updateSelectedItemPosition(i);
                    DialogFontMenuOld dialogFontMenuOld3 = DialogFontMenuOld.this;
                    T t2 = dialogFontMenuOld3.mListener;
                    if (t2 == 0) {
                        return true;
                    }
                    ((TextEditDialog.ConfigChangeListener) t2).onTypefaceChange(dialogFontMenuOld3.mCurrentTextStyle);
                    return true;
                }
                DialogFontMenuOld dialogFontMenuOld4 = DialogFontMenuOld.this;
                dialogFontMenuOld4.downloadResource(dialogFontMenuOld4.mContext, DialogFontMenuOld.this.mCurrentTextStyle, i);
                return true;
            }
        }
    }
}
