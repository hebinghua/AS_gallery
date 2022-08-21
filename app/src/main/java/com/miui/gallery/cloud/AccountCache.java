package com.miui.gallery.cloud;

import android.accounts.Account;
import android.content.Context;
import com.miui.account.AccountHelper;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.util.SyncLogger;

/* loaded from: classes.dex */
public class AccountCache {
    public static AccountInfo sAccountInfo;

    /* loaded from: classes.dex */
    public static class AccountInfo {
        public final Account mAccount;
        public final GalleryExtendedAuthToken mToken;

        public AccountInfo(Account account, GalleryExtendedAuthToken galleryExtendedAuthToken) {
            this.mAccount = account;
            this.mToken = galleryExtendedAuthToken;
        }
    }

    public static synchronized void update(Account account, GalleryExtendedAuthToken galleryExtendedAuthToken) {
        synchronized (AccountCache.class) {
            if (account == null || galleryExtendedAuthToken == null) {
                sAccountInfo = null;
            } else {
                sAccountInfo = new AccountInfo(account, galleryExtendedAuthToken);
            }
        }
    }

    public static synchronized Account getAccount() {
        synchronized (AccountCache.class) {
            AccountInfo accountInfo = sAccountInfo;
            if (accountInfo != null) {
                return accountInfo.mAccount;
            }
            return AccountHelper.getXiaomiAccount(GalleryApp.sGetAndroidContext());
        }
    }

    public static synchronized boolean isHaveAccount() {
        boolean z;
        synchronized (AccountCache.class) {
            z = getAccount() != null;
        }
        return z;
    }

    public static AccountInfo getAccountInfo() {
        AccountInfo requestAccountInfo;
        if (sAccountInfo == null && (requestAccountInfo = requestAccountInfo()) != null) {
            update(requestAccountInfo.mAccount, requestAccountInfo.mToken);
        }
        return sAccountInfo;
    }

    public static AccountInfo requestAccountInfo() {
        if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
            SyncLogger.e("AccountCache", "requestAccount: cta not allowed");
            return null;
        }
        Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
        Account xiaomiAccount = AccountHelper.getXiaomiAccount(sGetAndroidContext);
        if (xiaomiAccount == null) {
            SyncLogger.e("AccountCache", "requestAccount: get account failed");
            return null;
        }
        GalleryExtendedAuthToken extToken = CloudUtils.getExtToken(sGetAndroidContext, xiaomiAccount);
        if (extToken == null) {
            SyncLogger.e("AccountCache", "requestAccount: get ext token failed");
            return null;
        }
        return new AccountInfo(xiaomiAccount, extToken);
    }
}
