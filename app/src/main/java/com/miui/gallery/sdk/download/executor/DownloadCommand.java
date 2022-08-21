package com.miui.gallery.sdk.download.executor;

import android.accounts.Account;
import android.text.TextUtils;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.error.core.ErrorCode;
import com.miui.gallery.sdk.download.assist.DownloadFailReason;
import com.miui.gallery.sdk.download.assist.DownloadItem;
import com.miui.gallery.sdk.download.executor.queue.Command;

/* loaded from: classes2.dex */
public class DownloadCommand implements Command {
    public Account mAccount;
    public DownloadItem mItem;

    public DownloadCommand(Account account, DownloadItem downloadItem) {
        this.mAccount = account;
        this.mItem = downloadItem;
    }

    public DownloadCommand(DownloadCommand downloadCommand) {
        this.mAccount = downloadCommand.getAccount();
        this.mItem = new DownloadItem(downloadCommand.mItem);
    }

    @Override // com.miui.gallery.sdk.download.executor.queue.Command
    public String getKey() {
        return this.mItem.getKey();
    }

    @Override // com.miui.gallery.sdk.download.executor.queue.Command
    public int getPriority() {
        return this.mItem.getPriority();
    }

    public DownloadItem getItem() {
        return this.mItem;
    }

    public Account getAccount() {
        return this.mAccount;
    }

    public boolean equals(Object obj) {
        return obj != null && (obj instanceof DownloadCommand) && TextUtils.equals(getKey(), ((DownloadCommand) obj).getKey());
    }

    public int hashCode() {
        return getKey().hashCode();
    }

    public static boolean checkValid(DownloadCommand downloadCommand) {
        Account account = AccountCache.getAccount();
        if (account == null || downloadCommand == null || !account.equals(downloadCommand.getAccount())) {
            DownloadItem item = downloadCommand.getItem();
            if (item == null) {
                return false;
            }
            DownloadItem.callbackError(item, new DownloadFailReason(ErrorCode.NO_ACCOUNT, "illegal account", null));
            return false;
        }
        return true;
    }
}
