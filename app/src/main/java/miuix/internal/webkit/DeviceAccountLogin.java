package miuix.internal.webkit;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import java.lang.ref.WeakReference;

/* loaded from: classes3.dex */
public class DeviceAccountLogin {
    public AccountManager mAccountManager;
    public Activity mActivity;
    public AccountManagerCallback<Bundle> mCallback = new LoginCallback(this);

    public void onLoginCancel() {
    }

    public void onLoginFail() {
    }

    public void onLoginPageFinished() {
    }

    public void onLoginStart() {
    }

    public void onLoginSuccess(String str) {
    }

    public DeviceAccountLogin(Activity activity) {
        this.mActivity = activity;
        this.mAccountManager = AccountManager.get(activity.getApplicationContext());
    }

    public void login(String str, String str2, String str3) {
        Account account;
        Account[] accountsByType = this.mAccountManager.getAccountsByType(str);
        if (accountsByType.length == 0) {
            onLoginCancel();
            return;
        }
        Account account2 = null;
        if (TextUtils.isEmpty(str2)) {
            account2 = accountsByType[0];
        } else {
            for (Account account3 : accountsByType) {
                if (account3.name.equals(str2)) {
                    account = account3;
                    break;
                }
            }
        }
        account = account2;
        if (account != null) {
            onLoginStart();
            this.mAccountManager.getAuthToken(account, "weblogin:" + str3, (Bundle) null, (Activity) null, this.mCallback, (Handler) null);
            return;
        }
        onLoginCancel();
    }

    /* loaded from: classes3.dex */
    public static class LoginCallback implements AccountManagerCallback<Bundle> {
        public WeakReference<DeviceAccountLogin> mLoginRef;

        public LoginCallback(DeviceAccountLogin deviceAccountLogin) {
            this.mLoginRef = new WeakReference<>(deviceAccountLogin);
        }

        @Override // android.accounts.AccountManagerCallback
        public void run(AccountManagerFuture<Bundle> accountManagerFuture) {
            DeviceAccountLogin deviceAccountLogin = this.mLoginRef.get();
            if (deviceAccountLogin == null) {
                return;
            }
            try {
                String string = accountManagerFuture.getResult().getString("authtoken");
                if (string == null) {
                    deviceAccountLogin.onLoginFail();
                } else {
                    deviceAccountLogin.onLoginSuccess(string);
                }
            } catch (Exception e) {
                Log.e("DeviceAccountLogin", "Fail to login", e);
                deviceAccountLogin.onLoginFail();
            }
        }
    }
}
