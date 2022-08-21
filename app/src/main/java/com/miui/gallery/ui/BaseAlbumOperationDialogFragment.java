package com.miui.gallery.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.miui.gallery.R;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.cloudcontrol.strategies.ServerReservedAlbumNamesStrategy;
import com.miui.gallery.cloudcontrol.strategies.ServerUnModifyAlbumsStrategy;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.ui.ConfirmDialog;
import com.miui.gallery.ui.album.callback.OnAlbumCreateHandler;
import com.miui.gallery.util.ActionURIHandler;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.DebugUtil;
import com.miui.gallery.util.ResourceUtils;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.lang.ref.WeakReference;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public abstract class BaseAlbumOperationDialogFragment extends GalleryInputDialogFragment {
    public View.OnClickListener mConfirmListener = new View.OnClickListener() { // from class: com.miui.gallery.ui.BaseAlbumOperationDialogFragment.3
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            BaseAlbumOperationDialogFragment.this.confirmAlbumOperation(true);
        }
    };
    public OnAlbumOperationListenerWrapper mOnAlbumOperationListenerWrapper;
    public ServerReservedAlbumNamesStrategy mServerReservedAlbumNamesStrategy;
    public ServerUnModifyAlbumsStrategy mServerUnModifyAlbumsStrategy;

    /* loaded from: classes2.dex */
    public interface OnAlbumOperationListener {
        void onOperationDone(long j, String str, Bundle bundle);

        default boolean onOperationFailedByConflict(Album album) {
            return false;
        }
    }

    public abstract Bundle execAlbumOperation(Context context, String str);

    public abstract int getDialogTitle();

    @Override // com.miui.gallery.ui.GalleryInputDialogFragment
    public int getLayoutId() {
        return R.layout.album_edit_text_dialog;
    }

    public abstract int getOperationFailedString();

    public abstract int getOperationSucceededString();

    public abstract String getOperationTag();

    public boolean isHideSoftInputForLand() {
        return false;
    }

    public void parseArguments() {
    }

    @Override // com.miui.gallery.ui.GalleryInputDialogFragment, androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        parseArguments();
        this.mInputView.setHint(R.string.album_name);
        this.mInputView.setText(this.mDefaultName);
        this.mInputView.selectAll();
        this.mServerReservedAlbumNamesStrategy = CloudControlStrategyHelper.getServerReservedAlbumNamesStrategy();
        this.mServerUnModifyAlbumsStrategy = CloudControlStrategyHelper.getServerUnModifyAlbumsStrategy();
        this.mInputView.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.miui.gallery.ui.BaseAlbumOperationDialogFragment.1
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == 6 && BaseAlbumOperationDialogFragment.this.mInputView.mo49getText() != null) {
                    if (!TextUtils.isEmpty(BaseAlbumOperationDialogFragment.this.mInputView.mo49getText().toString().trim())) {
                        BaseAlbumOperationDialogFragment.this.confirmAlbumOperation(false);
                    } else {
                        BaseAlbumOperationDialogFragment.this.mInputView.selectAll();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public final long parseResultId(Bundle bundle) {
        if (bundle == null) {
            return -101L;
        }
        return bundle.getLong("id", -1L);
    }

    public final long parseConflictAlbumId(Bundle bundle) {
        if (bundle == null) {
            return -101L;
        }
        return bundle.getLong("conflict_album_id", -1L);
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        AlertDialog create = new AlertDialog.Builder(getActivity()).setView(this.mCustomView).setTitle(getDialogTitle()).setPositiveButton(17039370, (DialogInterface.OnClickListener) null).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).create();
        ((GalleryInputDialogFragment) this).mDialog = create;
        create.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.miui.gallery.ui.BaseAlbumOperationDialogFragment.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface dialogInterface) {
                BaseAlbumOperationDialogFragment baseAlbumOperationDialogFragment = BaseAlbumOperationDialogFragment.this;
                baseAlbumOperationDialogFragment.mConfirmButton = ((GalleryInputDialogFragment) baseAlbumOperationDialogFragment).mDialog.getButton(-1);
                BaseAlbumOperationDialogFragment baseAlbumOperationDialogFragment2 = BaseAlbumOperationDialogFragment.this;
                baseAlbumOperationDialogFragment2.mConfirmButton.setEnabled(!TextUtils.isEmpty(baseAlbumOperationDialogFragment2.mInputView.mo49getText()));
                BaseAlbumOperationDialogFragment baseAlbumOperationDialogFragment3 = BaseAlbumOperationDialogFragment.this;
                baseAlbumOperationDialogFragment3.mConfirmButton.setOnClickListener(baseAlbumOperationDialogFragment3.mConfirmListener);
                BaseAlbumOperationDialogFragment.this.checkAndSetSoftInputMode();
            }
        });
        return ((GalleryInputDialogFragment) this).mDialog;
    }

    public final void checkAndSetSoftInputMode() {
        FragmentActivity activity = getActivity();
        if (!(activity != null && activity.getResources().getConfiguration().orientation == 2) || !isHideSoftInputForLand()) {
            BaseMiscUtil.showInputMethod(this.mInputView);
        }
    }

    public final void selectAll() {
        EditTextPreIme editTextPreIme = this.mInputView;
        editTextPreIme.setText(editTextPreIme.mo49getText());
        this.mInputView.selectAll();
    }

    public final void confirmAlbumOperation(boolean z) {
        String obj = this.mInputView.mo49getText().toString();
        if (!verifyAlbumName(obj)) {
            return;
        }
        if (z) {
            this.mConfirmButton.setEnabled(false);
        }
        DebugUtil.logEventTime("operationTrace", getOperationTag(), false);
        new AlbumOperateTask(this).execute(obj);
    }

    public final boolean verifyAlbumName(String str) {
        char c = ' ';
        if (TextUtils.isEmpty(str.trim())) {
            this.mInputView.selectAll();
            ToastUtils.makeText(getActivity(), getString(R.string.new_album_invalid_char, ' '));
            return false;
        } else if ("._".indexOf(str.charAt(0)) >= 0) {
            ToastUtils.makeText(getActivity(), (int) R.string.new_album_invalid_prefix);
            return false;
        } else if (str.getBytes().length > 255) {
            ToastUtils.makeText(getActivity(), (int) R.string.new_album_invalid_byte);
            return false;
        } else {
            for (int i = 0; i < str.length(); i++) {
                char charAt = str.charAt(i);
                if ("/\\:@*?<>\"|\r\n\t".indexOf(charAt) >= 0) {
                    if ("\r\n\t".indexOf(charAt) < 0) {
                        c = charAt;
                    }
                    ToastUtils.makeText(getActivity(), getString(R.string.new_album_invalid_char, Character.valueOf(c)));
                    return false;
                }
            }
            if (!this.mServerReservedAlbumNamesStrategy.containsName(str) && !this.mServerUnModifyAlbumsStrategy.containsName(str)) {
                return true;
            }
            ToastUtils.makeText(getActivity(), getString(R.string.new_album_invalid_name, str));
            selectAll();
            return false;
        }
    }

    public final void onOperationDone(long j, String str, Bundle bundle) {
        if (getActivity() == null) {
            return;
        }
        this.mConfirmButton.setEnabled(true);
        OnAlbumOperationListenerWrapper onAlbumOperationListenerWrapper = this.mOnAlbumOperationListenerWrapper;
        if (onAlbumOperationListenerWrapper != null) {
            onAlbumOperationListenerWrapper.onOperationDone(j, str, bundle);
        } else if (getActivity() instanceof OnAlbumCreateHandler) {
            ((OnAlbumCreateHandler) getActivity()).handleAlbumCreated(j, str, bundle);
        }
        if (j == -103 || j == -105) {
            long parseConflictAlbumId = parseConflictAlbumId(bundle);
            if (parseConflictAlbumId >= 0) {
                if (bundle.containsKey("album_source")) {
                    onAlbumConflict((Album) bundle.getParcelable("album_source"));
                } else {
                    Album album = new Album(parseConflictAlbumId);
                    album.setAlbumName(str);
                    onAlbumConflict(album);
                }
            } else {
                ToastUtils.makeText(getActivity(), getString(R.string.album_already_exists_msg, str));
            }
            selectAll();
        } else if (j < 0) {
            ToastUtils.makeText(getActivity(), getOperationFailedString());
            selectAll();
        }
        if (j <= 0) {
            return;
        }
        ((GalleryInputDialogFragment) this).mDialog.dismiss();
        if (getOperationSucceededString() <= 0) {
            return;
        }
        ToastUtils.makeText(getActivity(), getOperationSucceededString());
    }

    public void showAlbumConflictDialog(Album album, String str, String str2, String str3, OnAlbumConflictDialogButtonCallback onAlbumConflictDialogButtonCallback) {
        Context context = getContext();
        if (album == null || context == null) {
            return;
        }
        FragmentManager fragmentManager = getFragmentManager();
        if (onAlbumConflictDialogButtonCallback == null) {
            onAlbumConflictDialogButtonCallback = new OnAlbumConflictDialogButtonCallback(album);
        }
        ConfirmDialog.showConfirmDialog(fragmentManager, (String) null, str, str2, str3, onAlbumConflictDialogButtonCallback);
    }

    /* loaded from: classes2.dex */
    public class OnAlbumConflictDialogButtonCallback implements ConfirmDialog.ConfirmDialogInterface {
        public Album mConflictAlbum;

        public OnAlbumConflictDialogButtonCallback(Album album) {
            this.mConflictAlbum = album;
        }

        @Override // com.miui.gallery.ui.ConfirmDialog.ConfirmDialogInterface
        public void onConfirm(DialogFragment dialogFragment) {
            AlertDialog alertDialog = ((GalleryInputDialogFragment) BaseAlbumOperationDialogFragment.this).mDialog;
            if (alertDialog != null && alertDialog.isShowing()) {
                ((GalleryInputDialogFragment) BaseAlbumOperationDialogFragment.this).mDialog.dismiss();
            }
            dialogFragment.dismiss();
            Bundle bundle = new Bundle();
            bundle.putParcelable("album_source", this.mConflictAlbum);
            ActionURIHandler.handleUri(BaseAlbumOperationDialogFragment.this.getActivity(), GalleryContract.Common.URI_ALBUM_PAGE, bundle);
        }

        @Override // com.miui.gallery.ui.ConfirmDialog.ConfirmDialogInterface
        public void onCancel(DialogFragment dialogFragment) {
            dialogFragment.dismiss();
        }
    }

    public void onAlbumConflict(Album album) {
        OnAlbumOperationListenerWrapper onAlbumOperationListenerWrapper = this.mOnAlbumOperationListenerWrapper;
        if (onAlbumOperationListenerWrapper == null || !onAlbumOperationListenerWrapper.onOperationFailedByConflict(album)) {
            showAlbumConflictDialog(album, ResourceUtils.getString(R.string.album_already_exists_msg, album.getAlbumName()), ResourceUtils.getString(R.string.cancel), ResourceUtils.getString(R.string.album_already_exists_check, album.getAlbumName()), new OnAlbumConflictDialogButtonCallback(album));
        }
    }

    public void setOnAlbumOperationListener(OnAlbumOperationListener onAlbumOperationListener) {
        this.mOnAlbumOperationListenerWrapper = new OnAlbumOperationListenerWrapper(onAlbumOperationListener);
    }

    /* loaded from: classes2.dex */
    public static class AlbumOperateTask extends AsyncTask<String, Void, Bundle> {
        public String mAlbumName;
        public WeakReference<BaseAlbumOperationDialogFragment> mHostRef;

        public AlbumOperateTask(BaseAlbumOperationDialogFragment baseAlbumOperationDialogFragment) {
            this.mHostRef = new WeakReference<>(baseAlbumOperationDialogFragment);
        }

        @Override // android.os.AsyncTask
        public Bundle doInBackground(String... strArr) {
            this.mAlbumName = strArr[0];
            BaseAlbumOperationDialogFragment ensureHost = ensureHost();
            if (ensureHost != null) {
                return ensureHost.execAlbumOperation(StaticContext.sGetAndroidContext(), this.mAlbumName);
            }
            DefaultLogger.w("BaseAlbumOperationDialogFragment", "host was cleared, abort operate(%d)", strArr[0]);
            return Bundle.EMPTY;
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Bundle bundle) {
            BaseAlbumOperationDialogFragment ensureHost = ensureHost();
            if (ensureHost != null) {
                ensureHost.onOperationDone(ensureHost.parseResultId(bundle), this.mAlbumName, bundle);
                DebugUtil.logEventTime("operationTrace", ensureHost.getOperationTag(), true);
                return;
            }
            DefaultLogger.w("BaseAlbumOperationDialogFragment", "host was cleared, drop result");
        }

        public final BaseAlbumOperationDialogFragment ensureHost() {
            BaseAlbumOperationDialogFragment baseAlbumOperationDialogFragment = this.mHostRef.get();
            if (baseAlbumOperationDialogFragment == null || baseAlbumOperationDialogFragment.isDetached()) {
                return null;
            }
            return baseAlbumOperationDialogFragment;
        }
    }

    /* loaded from: classes2.dex */
    public static class OnAlbumOperationListenerWrapper implements OnAlbumOperationListener {
        public final OnAlbumOperationListener mBase;

        public OnAlbumOperationListenerWrapper(OnAlbumOperationListener onAlbumOperationListener) {
            this.mBase = onAlbumOperationListener;
        }

        @Override // com.miui.gallery.ui.BaseAlbumOperationDialogFragment.OnAlbumOperationListener
        public void onOperationDone(long j, String str, Bundle bundle) {
            this.mBase.onOperationDone(j, str, bundle);
        }

        @Override // com.miui.gallery.ui.BaseAlbumOperationDialogFragment.OnAlbumOperationListener
        public boolean onOperationFailedByConflict(Album album) {
            return this.mBase.onOperationFailedByConflict(album);
        }
    }
}
