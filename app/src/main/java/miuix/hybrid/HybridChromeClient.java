package miuix.hybrid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.KeyEvent;
import miuix.hybrid.GeolocationPermissions;
import miuix.internal.hybrid.HybridManager;

/* loaded from: classes3.dex */
public class HybridChromeClient {
    private HybridManager mManager;

    public void onGeolocationPermissionsShowPrompt(String str, GeolocationPermissions.Callback callback) {
    }

    public void onReceivedTitle(HybridView hybridView, String str) {
    }

    public void setHybridManager(HybridManager hybridManager) {
        this.mManager = hybridManager;
    }

    public boolean onJsAlert(HybridView hybridView, String str, String str2, final JsResult jsResult) {
        new AlertDialog.Builder(this.mManager.getActivity()).setMessage(str2).setPositiveButton(17039370, new DialogInterface.OnClickListener() { // from class: miuix.hybrid.HybridChromeClient.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                jsResult.confirm();
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: miuix.hybrid.HybridChromeClient.2
            @Override // android.content.DialogInterface.OnCancelListener
            public void onCancel(DialogInterface dialogInterface) {
                jsResult.cancel();
            }
        }).setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: miuix.hybrid.HybridChromeClient.1
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == 4) {
                    jsResult.confirm();
                    return false;
                }
                return true;
            }
        }).show();
        return true;
    }

    public boolean onJsConfirm(HybridView hybridView, String str, String str2, final JsResult jsResult) {
        new AlertDialog.Builder(this.mManager.getActivity()).setMessage(str2).setPositiveButton(17039370, new DialogInterface.OnClickListener() { // from class: miuix.hybrid.HybridChromeClient.7
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                jsResult.confirm();
            }
        }).setNegativeButton(17039360, new DialogInterface.OnClickListener() { // from class: miuix.hybrid.HybridChromeClient.6
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                jsResult.cancel();
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: miuix.hybrid.HybridChromeClient.5
            @Override // android.content.DialogInterface.OnCancelListener
            public void onCancel(DialogInterface dialogInterface) {
                jsResult.cancel();
            }
        }).setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: miuix.hybrid.HybridChromeClient.4
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == 4) {
                    jsResult.confirm();
                    return false;
                }
                return true;
            }
        }).show();
        return true;
    }

    public void onProgressChanged(HybridView hybridView, int i) {
        hybridView.setProgress(i);
    }

    public void openFileChooser(ValueCallback<Uri> valueCallback, String str, String str2) {
        valueCallback.onReceiveValue(null);
    }
}
