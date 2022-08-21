package com.miui.gallery.signature.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.signature.SignatureActivity;
import com.miui.gallery.signature.SignatureColor;
import com.miui.gallery.signature.SignatureConfig;
import com.miui.gallery.signature.core.BitmapUtil;
import com.miui.gallery.signature.core.SignatureView;
import com.miui.gallery.signature.dialog.SignatureColorAdapter;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.util.BaseScreenUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.widget.GalleryDialogFragment;
import com.miui.gallery.widget.recyclerview.BlankDivider;
import com.xiaomi.stat.MiStat;
import com.xiaomi.stat.b.h;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class SignatureDialog extends GalleryDialogFragment implements DialogInterface.OnKeyListener {
    public BlankDivider mBlankDivider;
    public Disposable mDisposable;
    public String mNewOriginPath;
    public String mOriginPath;
    public String mSavePath;
    public SignatureColorAdapter mSignatureColorAdapter;
    public ViewGroup mSignatureRootLayout;
    public SignatureView mSignatureView;
    public RecyclerView mSimpleRecyclerView;
    public View.OnClickListener mOnConfirmListener = new View.OnClickListener() { // from class: com.miui.gallery.signature.dialog.SignatureDialog$$ExternalSyntheticLambda2
        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            SignatureDialog.$r8$lambda$clZmkROy7OdhfPlG5nPD32XccS4(SignatureDialog.this, view);
        }
    };
    public boolean finish = true;

    public static /* synthetic */ void $r8$lambda$JwiFVhsknUa0bxgYNYUer9uuoq8(SignatureDialog signatureDialog) {
        signatureDialog.lambda$initView$3();
    }

    public static /* synthetic */ void $r8$lambda$ZoniE_vWVlfcV4GwcCuvCndfBJM(SignatureDialog signatureDialog, Throwable th) {
        signatureDialog.lambda$saveSignature$9(th);
    }

    public static /* synthetic */ void $r8$lambda$clZmkROy7OdhfPlG5nPD32XccS4(SignatureDialog signatureDialog, View view) {
        signatureDialog.lambda$new$6(view);
    }

    public static /* synthetic */ void $r8$lambda$jQxYYbSocOLXpeBBTGIlgYMMHx0(SignatureDialog signatureDialog, ObservableEmitter observableEmitter) {
        signatureDialog.lambda$saveSignature$7(observableEmitter);
    }

    /* renamed from: $r8$lambda$tINhVqmTY-MFxtfRFQDBjbHY2K0 */
    public static /* synthetic */ void m1392$r8$lambda$tINhVqmTYMFxtfRFQDBjbHY2K0(SignatureDialog signatureDialog) {
        signatureDialog.lambda$initView$4();
    }

    public static /* synthetic */ void $r8$lambda$uU5pQ3MliSPZVZT_nZaj8j6PMCQ(SignatureDialog signatureDialog, AlertDialog alertDialog, DialogInterface dialogInterface) {
        signatureDialog.lambda$onCreateDialog$1(alertDialog, dialogInterface);
    }

    /* renamed from: $r8$lambda$ukY2jgqRFZ9bVoRg9YFdv-fo9Ks */
    public static /* synthetic */ void m1393$r8$lambda$ukY2jgqRFZ9bVoRg9YFdvfo9Ks(SignatureDialog signatureDialog, View view) {
        signatureDialog.lambda$initView$5(view);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle arguments = getArguments();
        if (arguments == null || !arguments.containsKey("param_signature_dialog_origin_path")) {
            return;
        }
        this.mOriginPath = arguments.getString("param_signature_dialog_origin_path");
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), 2131951621);
        this.mSignatureRootLayout = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.photo_editor_signature_dialog, (ViewGroup) null, false);
        initView();
        builder.setView(this.mSignatureRootLayout).setPositiveButton(17039370, (DialogInterface.OnClickListener) null).setNegativeButton(17039360, SignatureDialog$$ExternalSyntheticLambda0.INSTANCE);
        final AlertDialog create = builder.create();
        create.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.miui.gallery.signature.dialog.SignatureDialog$$ExternalSyntheticLambda1
            @Override // android.content.DialogInterface.OnShowListener
            public final void onShow(DialogInterface dialogInterface) {
                SignatureDialog.$r8$lambda$uU5pQ3MliSPZVZT_nZaj8j6PMCQ(SignatureDialog.this, create, dialogInterface);
            }
        });
        create.setOnKeyListener(this);
        return create;
    }

    public /* synthetic */ void lambda$onCreateDialog$1(AlertDialog alertDialog, DialogInterface dialogInterface) {
        setPositiveClickListener(alertDialog);
        hideCustomPanelPadding(alertDialog);
        alertDialog.getButton(-1).setTextSize(0, getResources().getDimensionPixelSize(R.dimen.signature_dialog_manage_item_text_size));
        alertDialog.getButton(-2).setTextSize(0, getResources().getDimensionPixelSize(R.dimen.signature_dialog_manage_item_text_size));
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setFlags(1024, 1024);
        }
    }

    public final void setPositiveClickListener(AlertDialog alertDialog) {
        alertDialog.getButton(-1).setOnClickListener(this.mOnConfirmListener);
    }

    public final void hideCustomPanelPadding(AlertDialog alertDialog) {
        FrameLayout frameLayout;
        Window window = alertDialog.getWindow();
        if (window == null || (frameLayout = (FrameLayout) window.getDecorView().findViewById(R.id.customPanel)) == null) {
            return;
        }
        frameLayout.setPadding(0, frameLayout.getPaddingTop(), 0, frameLayout.getPaddingBottom());
    }

    public final void initView() {
        SignatureColor[] signatureConfigColors;
        this.mSignatureView = (SignatureView) this.mSignatureRootLayout.findViewById(R.id.signature);
        FrameLayout frameLayout = (FrameLayout) this.mSignatureRootLayout.findViewById(R.id.clear_signature);
        FolmeUtil.setCustomTouchAnim(frameLayout, new AnimParams.Builder().setTint(0.1f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build(), null, null, true);
        ArrayList arrayList = new ArrayList();
        for (SignatureColor signatureColor : SignatureColor.getSignatureConfigColors()) {
            arrayList.add(new SignatureColorAdapter.ColorData(signatureColor, getResources().getColor(R.color.signature_dialog_select_color_view_stroke_color), getResources().getDimension(R.dimen.signature_dialog_top_color_view_item_inner_radius), getResources().getDimension(R.dimen.signature_dialog_top_color_view_item_stroke_width), signatureColor.mTalkbackName));
        }
        this.mSimpleRecyclerView = (RecyclerView) this.mSignatureRootLayout.findViewById(R.id.color_recycler_view);
        this.mSignatureColorAdapter = new SignatureColorAdapter(getContext(), arrayList);
        this.mSimpleRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        this.mSimpleRecyclerView.setAdapter(this.mSignatureColorAdapter);
        BlankDivider blankDivider = new BlankDivider(getResources().getDimensionPixelSize(R.dimen.signature_dialog_top_color_view_item_gap));
        this.mBlankDivider = blankDivider;
        this.mSimpleRecyclerView.addItemDecoration(blankDivider);
        this.mSignatureColorAdapter.setOnClickListener(new SignatureColorAdapter.OnClickListener() { // from class: com.miui.gallery.signature.dialog.SignatureDialog$$ExternalSyntheticLambda4
            @Override // com.miui.gallery.signature.dialog.SignatureColorAdapter.OnClickListener
            public final void onClick(SignatureColor signatureColor2) {
                SignatureDialog.this.lambda$initView$2(signatureColor2);
            }
        });
        this.mSignatureView.post(new Runnable() { // from class: com.miui.gallery.signature.dialog.SignatureDialog$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                SignatureDialog.$r8$lambda$JwiFVhsknUa0bxgYNYUer9uuoq8(SignatureDialog.this);
            }
        });
        if (isEdit()) {
            this.mSignatureColorAdapter.setSelectionWithColor(getBitmapColor());
            this.mSignatureView.post(new Runnable() { // from class: com.miui.gallery.signature.dialog.SignatureDialog$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    SignatureDialog.m1392$r8$lambda$tINhVqmTYMFxtfRFQDBjbHY2K0(SignatureDialog.this);
                }
            });
        }
        frameLayout.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.signature.dialog.SignatureDialog$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SignatureDialog.m1393$r8$lambda$ukY2jgqRFZ9bVoRg9YFdvfo9Ks(SignatureDialog.this, view);
            }
        });
    }

    public /* synthetic */ void lambda$initView$2(SignatureColor signatureColor) {
        this.mSignatureView.changeBitmapColor(signatureColor);
    }

    public /* synthetic */ void lambda$initView$3() {
        SignatureView signatureView = this.mSignatureView;
        if (signatureView != null) {
            signatureView.changeBitmapColor(SignatureColor.SIGNATURE_COLOR_DEFAULT);
        }
    }

    public /* synthetic */ void lambda$initView$4() {
        SignatureView signatureView = this.mSignatureView;
        if (signatureView != null) {
            signatureView.restoreFromBitmap(this.mOriginPath);
            this.mSignatureView.changeBitmapColor(getBitmapColor());
        }
    }

    public /* synthetic */ void lambda$initView$5(View view) {
        this.mSignatureView.clear();
    }

    public final SignatureColor getBitmapColor() {
        return SignatureColor.getColorWithColorTag(SignatureColor.getSignatureTag(this.mOriginPath));
    }

    public /* synthetic */ void lambda$new$6(View view) {
        SignatureActivity currentActivity = getCurrentActivity();
        if (currentActivity != null) {
            if (!this.mSignatureView.isBlank()) {
                saveSignature(currentActivity);
            } else if (!TextUtils.isEmpty(this.mOriginPath)) {
                currentActivity.prepareDataAndFinish(101, this.mOriginPath);
            } else {
                dismissAllowingStateLoss();
            }
        }
    }

    public final void sampleSave() {
        File signatureFolder = SignatureConfig.getSignatureFolder(getContext());
        HashMap hashMap = new HashMap(2);
        hashMap.put(MiStat.Param.COUNT, signatureFolder.exists() ? String.valueOf(signatureFolder.list().length) : "0");
        hashMap.put("color", getCurrentColorSample());
        SamplingStatHelper.recordCountEvent("photo_editor", "signature_save_click", hashMap);
    }

    public final String getCurrentColorSample() {
        int penColor = this.mSignatureView.getPenColor();
        return penColor == getResources().getColor(R.color.signature_color_two) ? "red" : penColor == getResources().getColor(R.color.signature_color_three) ? "orange" : penColor == getResources().getColor(R.color.signature_color_four) ? "blue" : "black";
    }

    public final boolean isEdit() {
        return !TextUtils.isEmpty(this.mOriginPath);
    }

    public final void saveSignature(final SignatureActivity signatureActivity) {
        this.mDisposable = Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.signature.dialog.SignatureDialog$$ExternalSyntheticLambda5
            @Override // io.reactivex.ObservableOnSubscribe
            public final void subscribe(ObservableEmitter observableEmitter) {
                SignatureDialog.$r8$lambda$jQxYYbSocOLXpeBBTGIlgYMMHx0(SignatureDialog.this, observableEmitter);
            }
        }).subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() { // from class: com.miui.gallery.signature.dialog.SignatureDialog$$ExternalSyntheticLambda7
            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                SignatureDialog.this.lambda$saveSignature$8(signatureActivity, (String) obj);
            }
        }, new Consumer() { // from class: com.miui.gallery.signature.dialog.SignatureDialog$$ExternalSyntheticLambda6
            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                SignatureDialog.$r8$lambda$ZoniE_vWVlfcV4GwcCuvCndfBJM(SignatureDialog.this, (Throwable) obj);
            }
        });
    }

    public /* synthetic */ void lambda$saveSignature$7(ObservableEmitter observableEmitter) throws Exception {
        String saveImage;
        if (this.mSignatureView.getPenColor() == getContext().getResources().getColor(SignatureColor.SIGNATURE_COLOR_DEFAULT.mColorId)) {
            this.mSignatureView.changeBitmapColor(SignatureColor.SIGNATURE_COLOR_BLACK, false);
        }
        Bitmap buildAreaBitmap = this.mSignatureView.buildAreaBitmap(true, 0);
        if (isEdit()) {
            generateChangeColorSavePath();
            saveImage = BitmapUtil.saveImage(buildAreaBitmap, 100, this.mOriginPath, "PNG");
        } else {
            generateSavePath();
            saveImage = BitmapUtil.saveImage(buildAreaBitmap, 100, this.mSavePath, "PNG");
        }
        observableEmitter.onNext(saveImage);
    }

    public /* synthetic */ void lambda$saveSignature$8(SignatureActivity signatureActivity, String str) throws Exception {
        if (!TextUtils.isEmpty(str)) {
            if (isEdit() && !TextUtils.isEmpty(this.mNewOriginPath)) {
                StorageSolutionProvider.get().moveFile(str, this.mNewOriginPath, FileHandleRecordHelper.appendInvokerTag("SignatureDialog", "saveSignature"));
                str = this.mNewOriginPath;
            }
            sampleSave();
            signatureActivity.prepareDataAndFinish(102, str);
        }
    }

    public /* synthetic */ void lambda$saveSignature$9(Throwable th) throws Exception {
        dismissAllowingStateLoss();
    }

    public final void generateChangeColorSavePath() {
        String signatureTag = SignatureColor.getSignatureTag(this.mOriginPath);
        if (this.mSignatureView.getPenColor() != getResources().getColor(SignatureColor.getColorWithColorTag(signatureTag).mColorId)) {
            this.mNewOriginPath = this.mOriginPath.replace(signatureTag, SignatureColor.getColorTagWithColor(getContext(), this.mSignatureView.getPenColor()));
        }
    }

    public final void generateSavePath() {
        CharSequence format = DateFormat.format("yyyyMMdd_HHmmss", System.currentTimeMillis());
        this.mSavePath = SignatureConfig.getSignatureFolder(getContext()).getAbsolutePath() + h.g + "signature_file" + ((Object) format) + SignatureColor.getColorTagWithColor(getContext(), this.mSignatureView.getPenColor()) + ".png";
    }

    @Override // com.miui.gallery.widget.GalleryDialogFragment, androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        SignatureActivity currentActivity = getCurrentActivity();
        if (currentActivity == null || !this.finish) {
            return;
        }
        currentActivity.finish();
    }

    public final SignatureActivity getCurrentActivity() {
        FragmentActivity activity = getActivity();
        if (activity instanceof SignatureActivity) {
            return (SignatureActivity) activity;
        }
        return null;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        SignatureView signatureView = this.mSignatureView;
        if (signatureView != null) {
            signatureView.release();
            this.mSignatureView = null;
        }
        Disposable disposable = this.mDisposable;
        if (disposable != null) {
            if (!disposable.isDisposed()) {
                this.mDisposable.dispose();
            }
            this.mDisposable = null;
        }
    }

    @Override // android.content.DialogInterface.OnKeyListener
    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        return BaseScreenUtil.isFullScreenGestureNav(getContext()) && i == 4;
    }
}
