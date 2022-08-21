package com.miui.gallery.ui;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.adapter.BaseRecyclerAdapter;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.compat.app.ActivityCompat;
import com.miui.gallery.glide.GlideApp;
import com.miui.gallery.request.PicToPdfHelper;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.EditTextPreIme;
import com.miui.gallery.ui.PicToPdfPreviewFragment;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.ResourceUtils;
import com.miui.gallery.util.ShareUtil;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.widget.recyclerview.BaseViewHolder;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import java.io.File;
import java.util.ArrayList;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.ProgressDialog;

/* loaded from: classes2.dex */
public class PicToPdfPreviewFragment extends BaseFragment {
    public ImageView mHomeIcon;
    public TextView mHomeText;
    public View mLandMenuLayout;
    public MenuItem mMenuSave;
    public MenuItem mMenuSend;
    public PicToPdfPreviewAdapter mPicToPdfPreviewAdapter;
    public SparseBooleanArray mPicUrlCheckStatusList;
    public ArrayList<String> mPicUrlDatas;
    public GalleryRecyclerView mRecyclerView;
    public AlertDialog mRenameDialog;
    public ImageView mRenameIcon;
    public ImageView mSaveIcon;
    public ProgressDialog mSaveProgressDialog;
    public ImageView mSendIcon;
    public boolean mIsConverted = false;
    public String mOutputFileName = DateFormat.format("yyyyMMddHHmm", System.currentTimeMillis()).toString();
    public String mOutputFileType = ".pdf";
    public String mOutputPath = StorageUtils.getPathInPriorStorage(Environment.DIRECTORY_DOWNLOADS) + File.separator;

    public static /* synthetic */ void $r8$lambda$6DiSmZ8CvhPNmPcibu8i472WtUo(PicToPdfPreviewFragment picToPdfPreviewFragment, String str) {
        picToPdfPreviewFragment.lambda$doSend$6(str);
    }

    /* renamed from: $r8$lambda$DV-_YBntit9RUITzx3ohIfaSxSI */
    public static /* synthetic */ void m1555$r8$lambda$DV_YBntit9RUITzx3ohIfaSxSI(PicToPdfPreviewFragment picToPdfPreviewFragment, View view) {
        picToPdfPreviewFragment.lambda$initActionBar$2(view);
    }

    public static /* synthetic */ void $r8$lambda$IhwmorhCFQDl_F8AaDgwqvAvSwI(PicToPdfPreviewFragment picToPdfPreviewFragment, EditTextPreIme editTextPreIme) {
        picToPdfPreviewFragment.lambda$performRename$9(editTextPreIme);
    }

    public static /* synthetic */ void $r8$lambda$L8QnvPxfgjFb_i50CfqRQwiLiTE(PicToPdfPreviewFragment picToPdfPreviewFragment) {
        picToPdfPreviewFragment.lambda$notifyTitleChanged$4();
    }

    public static /* synthetic */ void $r8$lambda$Qa6qgTGpxZk3EFxCVINeQdi7j18(PicToPdfPreviewFragment picToPdfPreviewFragment, View view) {
        picToPdfPreviewFragment.lambda$initActionBar$1(view);
    }

    public static /* synthetic */ void $r8$lambda$aqfc647KqJ6lE1fib6MFdGBxggQ(PicToPdfPreviewFragment picToPdfPreviewFragment, View view) {
        picToPdfPreviewFragment.lambda$initActionBar$0(view);
    }

    public static /* synthetic */ void $r8$lambda$bedWrl6Pyyih5mjfLO1FKAezAOM(PicToPdfPreviewFragment picToPdfPreviewFragment, String str) {
        picToPdfPreviewFragment.lambda$doSave$5(str);
    }

    public static /* synthetic */ void $r8$lambda$bnE3jYowj4gBVsSdv3GruEOdwGA(PicToPdfPreviewFragment picToPdfPreviewFragment, View view) {
        picToPdfPreviewFragment.lambda$initActionBar$3(view);
    }

    /* renamed from: $r8$lambda$n35-fukSLzcw8_1gmBPJaPf_N_U */
    public static /* synthetic */ void m1556$r8$lambda$n35fukSLzcw8_1gmBPJaPf_N_U(PicToPdfPreviewFragment picToPdfPreviewFragment, DialogInterface dialogInterface, int i) {
        picToPdfPreviewFragment.lambda$onBackPressed$10(dialogInterface, i);
    }

    public static /* synthetic */ void $r8$lambda$nphC0YYHNQA5l8_rB8fU2UIQ6Es(PicToPdfPreviewFragment picToPdfPreviewFragment, EditTextPreIme editTextPreIme, DialogInterface dialogInterface) {
        picToPdfPreviewFragment.lambda$performRename$8(editTextPreIme, dialogInterface);
    }

    /* renamed from: $r8$lambda$tBd-PX3Q7YCKcJlYPEPuTyLumlU */
    public static /* synthetic */ void m1557$r8$lambda$tBdPX3Q7YCKcJlYPEPuTyLumlU(PicToPdfPreviewFragment picToPdfPreviewFragment, EditTextPreIme editTextPreIme, View view) {
        picToPdfPreviewFragment.lambda$performRename$7(editTextPreIme, view);
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return "PicToPdfPreviewFragment";
    }

    public static PicToPdfPreviewFragment newInstance(Bundle bundle) {
        PicToPdfPreviewFragment picToPdfPreviewFragment = new PicToPdfPreviewFragment();
        picToPdfPreviewFragment.setArguments(bundle);
        return picToPdfPreviewFragment;
    }

    @Override // com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.pic_to_pdf_preview, viewGroup, false);
        GalleryRecyclerView galleryRecyclerView = (GalleryRecyclerView) inflate.findViewById(R.id.grid);
        this.mRecyclerView = galleryRecyclerView;
        galleryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        initActionBar();
        return inflate;
    }

    public final void initActionBar() {
        View inflate = getActivity().getLayoutInflater().inflate(R.layout.pic_to_pdf_title, (ViewGroup) null);
        inflate.setLayoutParams(new ActionBar.LayoutParams(-1, -1));
        if (!MiscUtil.isLandMode(getActivity())) {
            setMenuVisibility(true);
            View view = this.mLandMenuLayout;
            if (view != null && view.getVisibility() == 0) {
                this.mLandMenuLayout.setVisibility(8);
            }
        } else if (MiscUtil.isLandMode(getActivity()) && ActivityCompat.isInMultiWindowMode(getActivity())) {
            setMenuVisibility(true);
            if (this.mLandMenuLayout == null) {
                this.mLandMenuLayout = inflate.findViewById(R.id.menu_layout);
            }
            View view2 = this.mLandMenuLayout;
            if (view2 != null && view2.getVisibility() == 0) {
                this.mLandMenuLayout.setVisibility(8);
            }
        } else {
            View findViewById = inflate.findViewById(R.id.menu_layout);
            this.mLandMenuLayout = findViewById;
            findViewById.setVisibility(0);
            this.mRenameIcon = (ImageView) inflate.findViewById(R.id.menu_rename);
            this.mSaveIcon = (ImageView) inflate.findViewById(R.id.menu_save);
            this.mSendIcon = (ImageView) inflate.findViewById(R.id.menu_send);
            setMenuVisibility(false);
            this.mRenameIcon.setImageResource(R.drawable.action_button_rename_light);
            this.mRenameIcon.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.PicToPdfPreviewFragment$$ExternalSyntheticLambda4
                @Override // android.view.View.OnClickListener
                public final void onClick(View view3) {
                    PicToPdfPreviewFragment.$r8$lambda$aqfc647KqJ6lE1fib6MFdGBxggQ(PicToPdfPreviewFragment.this, view3);
                }
            });
            this.mSaveIcon.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.PicToPdfPreviewFragment$$ExternalSyntheticLambda3
                @Override // android.view.View.OnClickListener
                public final void onClick(View view3) {
                    PicToPdfPreviewFragment.$r8$lambda$Qa6qgTGpxZk3EFxCVINeQdi7j18(PicToPdfPreviewFragment.this, view3);
                }
            });
            this.mSendIcon.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.PicToPdfPreviewFragment$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view3) {
                    PicToPdfPreviewFragment.m1555$r8$lambda$DV_YBntit9RUITzx3ohIfaSxSI(PicToPdfPreviewFragment.this, view3);
                }
            });
        }
        ImageView imageView = (ImageView) inflate.findViewById(R.id.home_arrow);
        this.mHomeIcon = imageView;
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.PicToPdfPreviewFragment$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view3) {
                PicToPdfPreviewFragment.$r8$lambda$bnE3jYowj4gBVsSdv3GruEOdwGA(PicToPdfPreviewFragment.this, view3);
            }
        });
        this.mHomeText = (TextView) inflate.findViewById(R.id.home_text);
        notifyTitleChanged();
        getAppCompatActivity().getAppCompatActionBar().setDisplayOptions(16, 16);
        getAppCompatActivity().getAppCompatActionBar().setCustomView(inflate);
    }

    public /* synthetic */ void lambda$initActionBar$0(View view) {
        doRename();
    }

    public /* synthetic */ void lambda$initActionBar$1(View view) {
        doSave();
    }

    public /* synthetic */ void lambda$initActionBar$2(View view) {
        doSend();
    }

    public /* synthetic */ void lambda$initActionBar$3(View view) {
        onBackPressed();
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        ArrayList<String> stringArrayList = getArguments().getStringArrayList("pic_to_pdf_data");
        this.mPicUrlDatas = stringArrayList;
        if (stringArrayList != null && stringArrayList.size() > 0) {
            this.mPicUrlCheckStatusList = new SparseBooleanArray(this.mPicUrlDatas.size());
            for (int i = 0; i < this.mPicUrlDatas.size(); i++) {
                this.mPicUrlCheckStatusList.put(i, true);
            }
        }
        GalleryRecyclerView galleryRecyclerView = this.mRecyclerView;
        PicToPdfPreviewAdapter picToPdfPreviewAdapter = new PicToPdfPreviewAdapter();
        this.mPicToPdfPreviewAdapter = picToPdfPreviewAdapter;
        galleryRecyclerView.setAdapter(picToPdfPreviewAdapter);
    }

    public final void notifyTitleChanged() {
        if (TextUtils.isEmpty(this.mOutputFileName)) {
            return;
        }
        this.mHomeText.post(new Runnable() { // from class: com.miui.gallery.ui.PicToPdfPreviewFragment$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                PicToPdfPreviewFragment.$r8$lambda$L8QnvPxfgjFb_i50CfqRQwiLiTE(PicToPdfPreviewFragment.this);
            }
        });
    }

    public /* synthetic */ void lambda$notifyTitleChanged$4() {
        if (TextUtils.isEmpty(this.mOutputFileName)) {
            return;
        }
        String str = this.mOutputFileName;
        boolean z = false;
        while (true) {
            if (this.mHomeText.getPaint().measureText(str + "..." + this.mOutputFileType) <= this.mHomeText.getWidth() || TextUtils.isEmpty(str)) {
                break;
            }
            str = str.substring(0, str.length() - 1);
            z = true;
        }
        if (z) {
            this.mHomeText.setText(str + "..." + this.mOutputFileType);
            return;
        }
        this.mHomeText.setText(this.mOutputFileName + this.mOutputFileType);
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        PicToPdfHelper.getInstance().dispose();
        dismissDialog(this.mSaveProgressDialog);
        dismissDialog(this.mRenameDialog);
    }

    public final void dismissDialog(AlertDialog alertDialog) {
        if (alertDialog == null || !alertDialog.isShowing()) {
            return;
        }
        alertDialog.dismiss();
    }

    @Override // com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        initActionBar();
        super.onConfigurationChanged(configuration);
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.pic_to_pdf_preview_menu, menu);
        this.mMenuSave = menu.findItem(R.id.menu_save);
        this.mMenuSend = menu.findItem(R.id.menu_send);
    }

    public final void doRename() {
        TrackController.trackClick("403.71.2.1.16984", AutoTracking.getRef());
        SamplingStatHelper.recordCountEvent("pic_to_pdf", "pic_to_pdf_rename_click");
        performRename();
    }

    public final void doSave() {
        TrackController.trackClick("403.71.3.1.16985", AutoTracking.getRef());
        SamplingStatHelper.recordCountEvent("pic_to_pdf", "pic_to_pdf_save_click");
        if (this.mIsConverted) {
            ToastUtils.makeText(getContext(), ResourceUtils.getString(R.string.pic_to_pdf_saved_in_fileexplorer));
        } else {
            performSavePdf(new PicToPdfHelper.OnSavePdfCompleteListener() { // from class: com.miui.gallery.ui.PicToPdfPreviewFragment$$ExternalSyntheticLambda8
                @Override // com.miui.gallery.request.PicToPdfHelper.OnSavePdfCompleteListener
                public final void onSavePdfComplete(String str) {
                    PicToPdfPreviewFragment.$r8$lambda$bedWrl6Pyyih5mjfLO1FKAezAOM(PicToPdfPreviewFragment.this, str);
                }
            });
        }
    }

    public /* synthetic */ void lambda$doSave$5(String str) {
        onPicToPdfComplete(str);
        if (!TextUtils.isEmpty(str)) {
            getActivity().finish();
        }
    }

    public final void doSend() {
        TrackController.trackClick("403.71.4.1.16986", AutoTracking.getRef());
        SamplingStatHelper.recordCountEvent("pic_to_pdf", "pic_to_pdf_send_click");
        if (this.mIsConverted) {
            ShareUtil.share(getActivity(), getOutputFilePath());
        } else {
            performSavePdf(new PicToPdfHelper.OnSavePdfCompleteListener() { // from class: com.miui.gallery.ui.PicToPdfPreviewFragment$$ExternalSyntheticLambda7
                @Override // com.miui.gallery.request.PicToPdfHelper.OnSavePdfCompleteListener
                public final void onSavePdfComplete(String str) {
                    PicToPdfPreviewFragment.$r8$lambda$6DiSmZ8CvhPNmPcibu8i472WtUo(PicToPdfPreviewFragment.this, str);
                }
            });
        }
    }

    public /* synthetic */ void lambda$doSend$6(String str) {
        onPicToPdfComplete(str);
        if (!TextUtils.isEmpty(str)) {
            ShareUtil.share(getActivity(), getOutputFilePath());
        }
    }

    @Override // androidx.fragment.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_rename /* 2131362911 */:
                doRename();
                return true;
            case R.id.menu_render_line /* 2131362912 */:
            case R.id.menu_replace_album_cover /* 2131362913 */:
            default:
                return true;
            case R.id.menu_save /* 2131362914 */:
                doSave();
                return true;
            case R.id.menu_send /* 2131362915 */:
                doSend();
                return true;
        }
    }

    public final void onPicToPdfComplete(String str) {
        dismissDialog(this.mSaveProgressDialog);
        if (!TextUtils.isEmpty(str)) {
            this.mIsConverted = true;
            String name = new File(str).getName();
            this.mOutputFileName = name.substring(0, name.lastIndexOf(this.mOutputFileType));
            ToastUtils.makeText(getContext(), ResourceUtils.getString(R.string.pic_to_pdf_saved_in_fileexplorer));
            notifyTitleChanged();
            return;
        }
        ToastUtils.makeText(getContext(), ResourceUtils.getString(R.string.burst_save_video_fail));
    }

    public final void performRename() {
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.edit_text_dialog, (ViewGroup) null, false);
        final EditTextPreIme editTextPreIme = (EditTextPreIme) inflate.findViewById(R.id.edit_text);
        editTextPreIme.setText(this.mOutputFileName);
        editTextPreIme.selectAll();
        editTextPreIme.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        editTextPreIme.addTextChangedListener(new TextWatcher() { // from class: com.miui.gallery.ui.PicToPdfPreviewFragment.1
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            {
                PicToPdfPreviewFragment.this = this;
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                PicToPdfPreviewFragment.this.mRenameDialog.getButton(-1).setEnabled(!TextUtils.isEmpty(editable));
            }
        });
        editTextPreIme.setOnBackKeyListener(new EditTextPreIme.OnBackKeyListener() { // from class: com.miui.gallery.ui.PicToPdfPreviewFragment.2
            {
                PicToPdfPreviewFragment.this = this;
            }

            @Override // com.miui.gallery.ui.EditTextPreIme.OnBackKeyListener
            public void onClose() {
                if (PicToPdfPreviewFragment.this.mRenameDialog == null || !PicToPdfPreviewFragment.this.mRenameDialog.isShowing()) {
                    return;
                }
                PicToPdfPreviewFragment.this.mRenameDialog.dismiss();
            }
        });
        FragmentActivity activity = getActivity();
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            return;
        }
        AlertDialog create = new AlertDialog.Builder(activity).setView(inflate).setTitle(getString(R.string.operation_rename)).setPositiveButton(17039370, (DialogInterface.OnClickListener) null).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).create();
        this.mRenameDialog = create;
        create.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.miui.gallery.ui.PicToPdfPreviewFragment$$ExternalSyntheticLambda1
            @Override // android.content.DialogInterface.OnShowListener
            public final void onShow(DialogInterface dialogInterface) {
                PicToPdfPreviewFragment.$r8$lambda$nphC0YYHNQA5l8_rB8fU2UIQ6Es(PicToPdfPreviewFragment.this, editTextPreIme, dialogInterface);
            }
        });
        this.mRenameDialog.show();
        editTextPreIme.postDelayed(new Runnable() { // from class: com.miui.gallery.ui.PicToPdfPreviewFragment$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                PicToPdfPreviewFragment.$r8$lambda$IhwmorhCFQDl_F8AaDgwqvAvSwI(PicToPdfPreviewFragment.this, editTextPreIme);
            }
        }, 100L);
    }

    public /* synthetic */ void lambda$performRename$8(final EditTextPreIme editTextPreIme, DialogInterface dialogInterface) {
        this.mRenameDialog.getButton(-1).setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.PicToPdfPreviewFragment$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PicToPdfPreviewFragment.m1557$r8$lambda$tBdPX3Q7YCKcJlYPEPuTyLumlU(PicToPdfPreviewFragment.this, editTextPreIme, view);
            }
        });
    }

    public /* synthetic */ void lambda$performRename$7(EditTextPreIme editTextPreIme, View view) {
        Editable mo49getText = editTextPreIme.mo49getText();
        if (mo49getText == null) {
            return;
        }
        String obj = mo49getText.toString();
        if (!verifyAlbumName(obj)) {
            return;
        }
        String[] list = new File(this.mOutputPath).list();
        if (list != null && list.length > 0) {
            for (String str : list) {
                if (str.contentEquals(obj + this.mOutputFileType)) {
                    ToastUtils.makeText(getContext(), ResourceUtils.getString(R.string.toast_rename_exit));
                    return;
                }
            }
        }
        this.mIsConverted = false;
        this.mOutputFileName = obj;
        notifyTitleChanged();
        this.mRenameDialog.dismiss();
    }

    public /* synthetic */ void lambda$performRename$9(EditTextPreIme editTextPreIme) {
        InputMethodManager inputMethodManager;
        if (isRemoving() || isDetached() || (inputMethodManager = (InputMethodManager) GalleryApp.sGetAndroidContext().getSystemService("input_method")) == null) {
            return;
        }
        inputMethodManager.showSoftInput(editTextPreIme, 0);
    }

    public final boolean verifyAlbumName(String str) {
        if ("._".indexOf(str.charAt(0)) >= 0) {
            ToastUtils.makeText(getActivity(), (int) R.string.toast_invalid_prefix);
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if ("/\\:@*?<>\r\n\t".indexOf(charAt) >= 0) {
                if ("\r\n\t".indexOf(charAt) >= 0) {
                    charAt = ' ';
                }
                ToastUtils.makeText(getActivity(), getString(R.string.toast_invalid_char, Character.valueOf(charAt)));
                return false;
            }
        }
        return true;
    }

    public final void performSavePdf(PicToPdfHelper.OnSavePdfCompleteListener onSavePdfCompleteListener) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        this.mSaveProgressDialog = progressDialog;
        progressDialog.setMessage(GalleryApp.sGetAndroidContext().getString(R.string.photo_editor_saving));
        this.mSaveProgressDialog.setCanceledOnTouchOutside(false);
        this.mSaveProgressDialog.setCancelable(false);
        this.mSaveProgressDialog.show();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.mPicUrlDatas.size(); i++) {
            if (this.mPicUrlCheckStatusList.get(i)) {
                arrayList.add(this.mPicUrlDatas.get(i));
            }
        }
        PicToPdfHelper.getInstance().onSavePdf(arrayList, getOutputFilePath(), onSavePdfCompleteListener);
    }

    public final String getOutputFilePath() {
        return this.mOutputPath + this.mOutputFileName + this.mOutputFileType;
    }

    public void onBackPressed() {
        if (!this.mIsConverted) {
            new AlertDialog.Builder(getActivity()).setMessage(R.string.pic_to_pdf_exit_notice).setPositiveButton(17039370, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.PicToPdfPreviewFragment$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    PicToPdfPreviewFragment.m1556$r8$lambda$n35fukSLzcw8_1gmBPJaPf_N_U(PicToPdfPreviewFragment.this, dialogInterface, i);
                }
            }).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).create().show();
        } else {
            getActivity().finish();
        }
    }

    public /* synthetic */ void lambda$onBackPressed$10(DialogInterface dialogInterface, int i) {
        getActivity().finish();
    }

    /* loaded from: classes2.dex */
    public class PicToPdfPreviewAdapter extends BaseRecyclerAdapter<String, PicToPdfPreviewHolder> {
        public static /* synthetic */ void $r8$lambda$8Evfd7Crgta1WpTnafnT9gGNjZw(PicToPdfPreviewAdapter picToPdfPreviewAdapter, int i, boolean z, View view) {
            picToPdfPreviewAdapter.lambda$onBindViewHolder$0(i, z, view);
        }

        public PicToPdfPreviewAdapter() {
            PicToPdfPreviewFragment.this = r1;
        }

        @Override // com.miui.gallery.adapter.BaseRecyclerAdapter, com.miui.gallery.adapter.IBaseRecyclerAdapter
        /* renamed from: getItem */
        public String mo1558getItem(int i) {
            return (String) PicToPdfPreviewFragment.this.mPicUrlDatas.get(i);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* renamed from: onCreateViewHolder */
        public PicToPdfPreviewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new PicToPdfPreviewHolder(BaseViewHolder.getView(viewGroup, R.layout.pic_to_pdf_preview_item));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(PicToPdfPreviewHolder picToPdfPreviewHolder, final int i) {
            GlideApp.with(PicToPdfPreviewFragment.this.getActivity()).mo990load((String) PicToPdfPreviewFragment.this.mPicUrlDatas.get(i)).into(picToPdfPreviewHolder.mPicToPdfPreview);
            if (PicToPdfPreviewFragment.this.mPicUrlDatas.size() > 1) {
                picToPdfPreviewHolder.mPicToPdfIndex.setVisibility(0);
                picToPdfPreviewHolder.mCheckSelect.setVisibility(0);
                picToPdfPreviewHolder.mPicToPdfIndex.setText(String.valueOf(i + 1));
                final boolean z = PicToPdfPreviewFragment.this.mPicUrlCheckStatusList.get(i);
                picToPdfPreviewHolder.mCheckSelect.setChecked(z);
                picToPdfPreviewHolder.mPicToPdfPreview.setAlpha(z ? 1.0f : 0.3f);
                picToPdfPreviewHolder.mCheckSelect.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.PicToPdfPreviewFragment$PicToPdfPreviewAdapter$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        PicToPdfPreviewFragment.PicToPdfPreviewAdapter.$r8$lambda$8Evfd7Crgta1WpTnafnT9gGNjZw(PicToPdfPreviewFragment.PicToPdfPreviewAdapter.this, i, z, view);
                    }
                });
                return;
            }
            picToPdfPreviewHolder.mPicToPdfIndex.setVisibility(8);
            picToPdfPreviewHolder.mCheckSelect.setVisibility(8);
        }

        public /* synthetic */ void lambda$onBindViewHolder$0(int i, boolean z, View view) {
            boolean z2 = true;
            PicToPdfPreviewFragment.this.mPicUrlCheckStatusList.put(i, !z);
            int i2 = 0;
            for (int i3 = 0; i3 < PicToPdfPreviewFragment.this.mPicUrlCheckStatusList.size(); i3++) {
                if (PicToPdfPreviewFragment.this.mPicUrlCheckStatusList.get(i3)) {
                    i2++;
                }
            }
            if (PicToPdfPreviewFragment.this.mMenuSave != null) {
                PicToPdfPreviewFragment.this.mMenuSave.setEnabled(i2 > 0);
            }
            if (PicToPdfPreviewFragment.this.mMenuSend != null) {
                MenuItem menuItem = PicToPdfPreviewFragment.this.mMenuSend;
                if (i2 <= 0) {
                    z2 = false;
                }
                menuItem.setEnabled(z2);
            }
            PicToPdfPreviewFragment.this.mIsConverted = false;
            notifyItemChanged(i);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            if (PicToPdfPreviewFragment.this.mPicUrlDatas == null) {
                return 0;
            }
            return PicToPdfPreviewFragment.this.mPicUrlDatas.size();
        }
    }

    /* loaded from: classes2.dex */
    public static class PicToPdfPreviewHolder extends BaseViewHolder {
        public CheckBox mCheckSelect;
        public TextView mPicToPdfIndex;
        public ImageView mPicToPdfPreview;

        public PicToPdfPreviewHolder(View view) {
            super(view);
            this.mPicToPdfPreview = (ImageView) view.findViewById(R.id.pic_to_pdf_preview);
            this.mPicToPdfIndex = (TextView) view.findViewById(R.id.pic_to_pdf_index);
            this.mCheckSelect = (CheckBox) view.findViewById(R.id.check_select);
        }
    }
}
