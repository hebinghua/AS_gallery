package com.miui.gallery.search.core.suggestion;

import android.database.AbstractCursor;
import android.database.ContentObserver;
import android.database.DataSetObserver;
import android.os.Bundle;
import com.miui.gallery.search.core.GroupList;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.source.Source;
import com.miui.gallery.search.core.suggestion.SuggestionCursor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public class GroupedSuggestionCursor<S extends SuggestionCursor> extends AbstractCursor implements SuggestionCursor, GroupList<S, Suggestion> {
    public final QueryInfo mQueryInfo;
    public S mSection;
    public List<S> mSections;
    public DataSetObserver mObserver = new DataSetObserver() { // from class: com.miui.gallery.search.core.suggestion.GroupedSuggestionCursor.1
        @Override // android.database.DataSetObserver
        public void onChanged() {
            ((AbstractCursor) GroupedSuggestionCursor.this).mPos = -1;
            GroupedSuggestionCursor.this.mCachedCount = null;
        }

        @Override // android.database.DataSetObserver
        public void onInvalidated() {
            ((AbstractCursor) GroupedSuggestionCursor.this).mPos = -1;
            GroupedSuggestionCursor.this.mCachedCount = null;
        }
    };
    public Integer mCachedCount = null;
    public Bundle mExtras = Bundle.EMPTY;

    public GroupedSuggestionCursor(QueryInfo queryInfo, List<S> list) {
        S s = null;
        this.mQueryInfo = queryInfo;
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Empty section value is not acceptable!");
        }
        this.mSections = new ArrayList(list.size());
        for (S s2 : list) {
            this.mSections.add(s2);
            s2.registerDataSetObserver(this.mObserver);
        }
        setSection(this.mSections.size() >= 1 ? this.mSections.get(0) : s);
    }

    public void moveToPosition(int i, int i2) {
        checkGroupPosition(i);
        int i3 = 0;
        for (int i4 = 0; i4 < i; i4++) {
            i3 += getChildrenCount(i);
        }
        moveToPosition(i3 + i2);
    }

    public final void setSection(S s) {
        if (this.mSection != s) {
            this.mSection = s;
        }
    }

    @Override // com.miui.gallery.search.core.suggestion.SuggestionCursor
    public Suggestion getCurrent() {
        return this.mSection.getCurrent();
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public Source getSuggestionSource() {
        return this.mSection.getSuggestionSource();
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public String getIntentActionURI() {
        return this.mSection.getIntentActionURI();
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public String getSuggestionIcon() {
        return this.mSection.getSuggestionIcon();
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public String getSuggestionTitle() {
        return this.mSection.getSuggestionTitle();
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public String getSuggestionSubTitle() {
        return this.mSection.getSuggestionSubTitle();
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public SuggestionExtras getSuggestionExtras() {
        return this.mSection.getSuggestionExtras();
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public List<String> getBackupIcons() {
        return this.mSection.getBackupIcons();
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public int getCount() {
        if (this.mCachedCount == null) {
            int i = 0;
            for (int i2 = 0; i2 < getGroupCount(); i2++) {
                i += getChildrenCount(i2);
            }
            this.mCachedCount = Integer.valueOf(i);
        }
        return this.mCachedCount.intValue();
    }

    @Override // android.database.AbstractCursor, android.database.CrossProcessCursor
    public boolean onMove(int i, int i2) {
        this.mSection = null;
        int i3 = 0;
        int i4 = 0;
        while (true) {
            if (i3 >= getGroupCount()) {
                i3 = -1;
                break;
            }
            int childrenCount = getChildrenCount(i3) + i4;
            if (i2 < childrenCount) {
                this.mSection = mo1334getGroup(i3);
                break;
            }
            i3++;
            i4 = childrenCount;
        }
        if (i3 >= 0) {
            return onMoveGroup(i3, i2 - i4);
        }
        return false;
    }

    public boolean onMoveGroup(int i, int i2) {
        return mo1334getGroup(i).moveToPosition(i2);
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public String getString(int i) {
        return this.mSection.getString(i);
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public short getShort(int i) {
        return this.mSection.getShort(i);
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public int getInt(int i) {
        return this.mSection.getInt(i);
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public long getLong(int i) {
        return this.mSection.getLong(i);
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public float getFloat(int i) {
        return this.mSection.getFloat(i);
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public double getDouble(int i) {
        return this.mSection.getDouble(i);
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public int getType(int i) {
        return this.mSection.getType(i);
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public boolean isNull(int i) {
        return this.mSection.isNull(i);
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public byte[] getBlob(int i) {
        return this.mSection.getBlob(i);
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public String[] getColumnNames() {
        S s = this.mSection;
        return s != null ? s.getColumnNames() : new String[0];
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public Bundle getExtras() {
        return this.mExtras;
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public void deactivate() {
        for (S s : this.mSections) {
            s.deactivate();
        }
    }

    @Override // android.database.AbstractCursor, android.database.Cursor, java.io.Closeable, java.lang.AutoCloseable, com.miui.gallery.search.core.QuietlyCloseable
    public void close() {
        for (S s : this.mSections) {
            s.close();
        }
        this.mSection = null;
        super.close();
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public void registerContentObserver(ContentObserver contentObserver) {
        for (S s : this.mSections) {
            s.registerContentObserver(contentObserver);
        }
        super.registerContentObserver(contentObserver);
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public void unregisterContentObserver(ContentObserver contentObserver) {
        for (S s : this.mSections) {
            s.unregisterContentObserver(contentObserver);
        }
        super.unregisterContentObserver(contentObserver);
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        for (S s : this.mSections) {
            s.registerDataSetObserver(dataSetObserver);
        }
        super.registerDataSetObserver(dataSetObserver);
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        for (S s : this.mSections) {
            s.unregisterDataSetObserver(dataSetObserver);
        }
        super.unregisterDataSetObserver(dataSetObserver);
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public boolean requery() {
        for (S s : this.mSections) {
            if (!s.requery()) {
                return false;
            }
        }
        return super.requery();
    }

    @Override // com.miui.gallery.search.core.GroupList
    public int getGroupCount() {
        return this.mSections.size();
    }

    public int getChildrenCount(int i) {
        checkGroupPosition(i);
        if (this.mSections.get(i) != null) {
            return this.mSections.get(i).getCount();
        }
        return 0;
    }

    @Override // com.miui.gallery.search.core.GroupList
    /* renamed from: getGroup */
    public S mo1334getGroup(int i) {
        checkGroupPosition(i);
        return this.mSections.get(i);
    }

    public void checkGroupPosition(int i) {
        if (i < 0 || i >= this.mSections.size()) {
            throw new IndexOutOfBoundsException(String.format(Locale.getDefault(), "Invalid groupIndex %d, total group count %d", Integer.valueOf(i), Integer.valueOf(this.mSections.size())));
        }
    }

    @Override // android.database.AbstractCursor, android.database.Cursor, com.miui.gallery.search.core.suggestion.SuggestionCursor
    public void setExtras(Bundle bundle) {
        if (bundle == null) {
            bundle = Bundle.EMPTY;
        }
        this.mExtras = bundle;
    }
}
