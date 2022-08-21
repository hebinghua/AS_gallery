package com.miui.gallery.hybrid.hybridclient;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import com.miui.account.AccountHelper;
import com.miui.gallery.hybrid.hybridclient.HybridClient;

/* loaded from: classes2.dex */
public class LoginHybridClient extends DeviceIdHybridClient {
    public LoginHybridClient(Context context, String str) {
        super(context, str);
    }

    @Override // com.miui.gallery.hybrid.hybridclient.GalleryHybridClient, com.miui.gallery.hybrid.hybridclient.HybridClient
    public void getActualPath(final HybridClient.ActualPathCallback actualPathCallback) {
        if (actualPathCallback == null) {
            return;
        }
        if (this.mContext == null) {
            actualPathCallback.onGetActualPath(null);
            return;
        }
        String str = "weblogin:" + this.mUrl;
        Account xiaomiAccount = AccountHelper.getXiaomiAccount(this.mContext);
        if (xiaomiAccount == null) {
            return;
        }
        AccountManager.get(this.mContext).getAuthToken(xiaomiAccount, str, (Bundle) null, false, new AccountManagerCallback<Bundle>() { // from class: com.miui.gallery.hybrid.hybridclient.LoginHybridClient.1
            @Override // android.accounts.AccountManagerCallback
            public void run(AccountManagerFuture<Bundle> accountManagerFuture) {
                try {
                    actualPathCallback.onGetActualPath(accountManagerFuture.getResult().getString("authtoken"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, (Handler) null);
    }
}
