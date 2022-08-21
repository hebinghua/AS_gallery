package com.miui.gallery.card;

import android.accounts.Account;
import android.content.ContentValues;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.util.BaseMiscUtil;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class SyncTagUtil {
    public static String getAcountSelection(Account account) {
        return "accountName = '" + account.name + "' AND accountType = '" + account.type + "'";
    }

    public static SyncTag getSyncTagByAccount(Account account) {
        if (account != null) {
            List query = GalleryEntityManager.getInstance().query(SyncTag.class, getAcountSelection(account), null, null, String.format(Locale.US, "%s,%s", 0, 1));
            if (!BaseMiscUtil.isValid(query)) {
                return null;
            }
            return (SyncTag) query.get(0);
        }
        return null;
    }

    public static long getCardSyncTag(Account account) {
        SyncTag syncTagByAccount = getSyncTagByAccount(account);
        if (syncTagByAccount != null) {
            return syncTagByAccount.getCardSyncTag();
        }
        return 0L;
    }

    public static void setCardSyncTag(Account account, long j) {
        if (account == null) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("cardSyncTag", Long.valueOf(j));
        GalleryEntityManager.getInstance().update(SyncTag.class, contentValues, getAcountSelection(account), null);
    }

    public static String getCardSyncInfo(Account account) {
        SyncTag syncTagByAccount = getSyncTagByAccount(account);
        return syncTagByAccount != null ? syncTagByAccount.getCardSyncInfo() : "";
    }

    public static void setCardSyncInfo(Account account, String str) {
        if (account == null) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("cardSyncInfo", str);
        GalleryEntityManager.getInstance().update(SyncTag.class, contentValues, getAcountSelection(account), null);
    }

    public static void ensureAccount(Account account) {
        if (account == null || BaseMiscUtil.isValid(GalleryEntityManager.getInstance().query(SyncTag.class, getAcountSelection(account), null))) {
            return;
        }
        GalleryEntityManager.getInstance().insert(new SyncTag(account));
    }
}
