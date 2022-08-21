package com.miui.gallery.trash;

import android.accounts.Account;
import android.content.ContentValues;
import android.database.Cursor;
import com.miui.gallery.dao.base.Entity;
import com.miui.gallery.dao.base.TableColumn;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class TrashSyncTag extends Entity {
    public static int BREAK = 0;
    public static int CONTINUE = 1;
    public String mAccountName;
    public String mAccountType;
    public int mIsContinue;
    public long mSyncTag;

    public TrashSyncTag() {
    }

    public TrashSyncTag(Account account) {
        this.mAccountName = account.name;
        this.mAccountType = account.type;
    }

    public boolean isContinue() {
        return this.mIsContinue == CONTINUE;
    }

    public void setContinue(boolean z) {
        this.mIsContinue = z ? CONTINUE : BREAK;
    }

    public long getSyncTag() {
        return this.mSyncTag;
    }

    public void setSyncTag(long j) {
        this.mSyncTag = j;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public List<TableColumn> getTableColumns() {
        ArrayList arrayList = new ArrayList();
        Entity.addColumn(arrayList, "accountName", "TEXT");
        Entity.addColumn(arrayList, "accountType", "TEXT");
        Entity.addColumn(arrayList, "syncTag", "INTEGER");
        Entity.addColumn(arrayList, "isContinue", "INTEGER");
        return arrayList;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onInitFromCursor(Cursor cursor) {
        this.mAccountName = Entity.getString(cursor, "accountName");
        this.mAccountType = Entity.getString(cursor, "accountType");
        this.mSyncTag = Entity.getLong(cursor, "syncTag");
        this.mIsContinue = Entity.getInt(cursor, "isContinue");
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onConvertToContents(ContentValues contentValues) {
        contentValues.put("accountName", this.mAccountName);
        contentValues.put("accountType", this.mAccountType);
        contentValues.put("syncTag", Long.valueOf(this.mSyncTag));
        contentValues.put("isContinue", Integer.valueOf(this.mIsContinue));
    }
}
