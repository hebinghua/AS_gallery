package com.miui.gallery.card;

import android.accounts.Account;
import android.content.ContentValues;
import android.database.Cursor;
import com.miui.gallery.dao.base.Entity;
import com.miui.gallery.dao.base.TableColumn;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class SyncTag extends Entity {
    public String mAccountName;
    public String mAccountType;
    public String mCardSyncInfo;
    public long mCardSyncTag;

    public SyncTag() {
    }

    public SyncTag(Account account) {
        this.mAccountName = account.name;
        this.mAccountType = account.type;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public List<TableColumn> getTableColumns() {
        ArrayList arrayList = new ArrayList();
        Entity.addColumn(arrayList, "accountName", "TEXT");
        Entity.addColumn(arrayList, "accountType", "TEXT");
        Entity.addColumn(arrayList, "cardSyncTag", "INTEGER");
        Entity.addColumn(arrayList, "cardSyncInfo", "TEXT");
        return arrayList;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onInitFromCursor(Cursor cursor) {
        this.mAccountName = Entity.getString(cursor, "accountName");
        this.mAccountType = Entity.getString(cursor, "accountType");
        this.mCardSyncTag = Entity.getLong(cursor, "cardSyncTag");
        this.mCardSyncInfo = Entity.getString(cursor, "cardSyncInfo");
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onConvertToContents(ContentValues contentValues) {
        contentValues.put("accountName", this.mAccountName);
        contentValues.put("accountType", this.mAccountType);
        contentValues.put("cardSyncTag", Long.valueOf(this.mCardSyncTag));
        contentValues.put("cardSyncInfo", this.mCardSyncInfo);
    }

    public long getCardSyncTag() {
        return this.mCardSyncTag;
    }

    public String getCardSyncInfo() {
        return this.mCardSyncInfo;
    }
}
