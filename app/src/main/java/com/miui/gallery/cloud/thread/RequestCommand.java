package com.miui.gallery.cloud.thread;

import android.accounts.Account;
import android.os.SystemClock;
import com.miui.gallery.cloud.RequestCloudItem;
import com.miui.gallery.data.DBImage;

/* loaded from: classes.dex */
public class RequestCommand implements Command {
    public final Account mAccount;
    public final long mInvokeTime = SystemClock.uptimeMillis();
    public final RequestCloudItem mRequestItem;

    public RequestCommand(Account account, RequestCloudItem requestCloudItem) {
        this.mAccount = account;
        this.mRequestItem = requestCloudItem;
    }

    @Override // com.miui.gallery.cloud.thread.Command
    public int getPriority() {
        return this.mRequestItem.priority;
    }

    @Override // com.miui.gallery.cloud.thread.Command
    public String getKey() {
        return getKey(this.mRequestItem);
    }

    @Override // com.miui.gallery.cloud.thread.Command
    public long getDelay(long j) {
        return this.mRequestItem.getDelayToExecuteInMillis(j);
    }

    @Override // com.miui.gallery.cloud.thread.Command
    public boolean canMergeWith(Command command) {
        RequestCommand requestCommand = (RequestCommand) command;
        return this.mRequestItem.priority == requestCommand.mRequestItem.priority && this.mAccount.equals(requestCommand.mAccount);
    }

    @Override // com.miui.gallery.cloud.thread.Command
    public boolean needProcess() {
        return this.mRequestItem.getStatus() != 0;
    }

    public static String getKey(RequestCloudItem requestCloudItem) {
        return getKey(requestCloudItem.dbCloud, requestCloudItem.priority);
    }

    public static String getKey(DBImage dBImage, int i) {
        return dBImage.getTagId() + "#" + i;
    }
}
