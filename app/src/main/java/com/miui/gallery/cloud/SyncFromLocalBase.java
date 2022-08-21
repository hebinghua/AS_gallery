package com.miui.gallery.cloud;

import android.accounts.Account;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.data.DBItem;
import com.miui.gallery.util.SyncLogger;

/* loaded from: classes.dex */
public abstract class SyncFromLocalBase {
    public Account mAccount;
    public Context mContext;
    public GalleryExtendedAuthToken mExtendedAuthToken;
    public RegularPagingProvider mPagingProvider = null;
    public boolean mIsFirst = true;

    /* renamed from: generateDBImage */
    public abstract DBItem mo689generateDBImage(Cursor cursor);

    public abstract Uri getBaseUri();

    public String getSortOrder() {
        return "dateModified DESC ";
    }

    public abstract void handleRequestCloudItemList() throws Exception;

    public abstract void initRequestCloudItemList();

    public abstract void putToRequestCloudItemList(DBItem dBItem);

    public void syncStart() {
    }

    public SyncFromLocalBase(Context context, Account account, GalleryExtendedAuthToken galleryExtendedAuthToken) {
        this.mContext = context;
        this.mAccount = account;
        this.mExtendedAuthToken = galleryExtendedAuthToken;
    }

    public final void sync() throws Exception {
        this.mIsFirst = true;
        long currentTimeMillis = System.currentTimeMillis();
        SyncLogger.d("SyncFromLocalBase", "sync from local start");
        while (startOrContinueBatch()) {
            SyncLogger.d("SyncFromLocalBase", "continue batch");
        }
        SyncLogger.d("SyncFromLocalBase", "sync from local finish:" + (System.currentTimeMillis() - currentTimeMillis));
    }

    public final boolean startOrContinueBatch() throws Exception {
        checkIsFirst();
        Cursor cursor = null;
        try {
            String selectionClause = getSelectionClause();
            Cursor query = this.mContext.getContentResolver().query(CloudUtils.getLimitUri(getBaseUri(), this.mPagingProvider.getQueryLimit(), this.mPagingProvider.getOffset()), CloudUtils.getProjectionAll(), selectionClause, null, getSortOrder());
            if (query == null) {
                syncEnd();
                if (query != null) {
                    query.close();
                }
                return false;
            }
            SyncLogger.d("SyncFromLocalBase", "start one batch, count=" + query.getCount());
            syncOneBatch(query);
            SyncLogger.d("SyncFromLocalBase", "end one batch, count=" + query.getCount());
            if (this.mPagingProvider.updateShouldContinue(query)) {
                query.close();
                return true;
            }
            syncEnd();
            query.close();
            return false;
        } catch (Throwable th) {
            if (0 != 0) {
                cursor.close();
            }
            throw th;
        }
    }

    public final void checkIsFirst() {
        if (this.mIsFirst) {
            this.mPagingProvider = getRegularPagingProvider();
            syncStart();
            this.mIsFirst = false;
        }
    }

    public void syncEnd() {
        this.mIsFirst = true;
        this.mPagingProvider = null;
    }

    public RegularPagingProvider getRegularPagingProvider() {
        return new RegularPagingProvider();
    }

    public String getSelectionClause() {
        return String.format(" (%s) ", "localFlag != 0");
    }

    public final void syncOneBatch(Cursor cursor) throws Exception {
        if (cursor == null) {
            SyncLogger.d("SyncFromLocalBase", "there is no item in local DB to sync.");
            return;
        }
        initRequestCloudItemList();
        while (cursor.moveToNext()) {
            putToRequestCloudItemList(mo689generateDBImage(cursor));
        }
        handleRequestCloudItemList();
    }
}
