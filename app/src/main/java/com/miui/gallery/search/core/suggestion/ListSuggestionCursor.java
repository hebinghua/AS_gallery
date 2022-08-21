package com.miui.gallery.search.core.suggestion;

import android.database.AbstractCursor;
import android.os.Bundle;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.source.Source;
import com.miui.gallery.search.core.suggestion.Suggestion;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.xiaomi.mirror.synergy.CallMethod;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/* loaded from: classes2.dex */
public class ListSuggestionCursor<S extends Suggestion> extends AbstractCursor implements SuggestionCursor {
    public static final String[] COLUMNS = {"title", "subTitle", "actionUri", CallMethod.RESULT_ICON};
    public ArrayList<String> EMPTY_COLUMNS;
    public ArrayList<String> mExtraColumns;
    public Bundle mExtras;
    public final QueryInfo mQueryInfo;
    public final List<S> mSuggestions;

    @Override // android.database.AbstractCursor, android.database.Cursor
    public double getDouble(int i) {
        return SearchStatUtils.POW;
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public float getFloat(int i) {
        return 0.0f;
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public int getInt(int i) {
        return 0;
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public long getLong(int i) {
        return 0L;
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public short getShort(int i) {
        return (short) 0;
    }

    public ListSuggestionCursor(QueryInfo queryInfo, List<S> list) {
        this(queryInfo, list, null);
    }

    public ListSuggestionCursor(QueryInfo queryInfo, List<S> list, Bundle bundle) {
        ArrayList arrayList = new ArrayList();
        this.mSuggestions = arrayList;
        this.EMPTY_COLUMNS = new ArrayList<>(0);
        this.mExtras = Bundle.EMPTY;
        this.mQueryInfo = queryInfo;
        if (list == null) {
            return;
        }
        arrayList.addAll(list);
        setExtras(bundle);
    }

    public void add(S s) {
        if (s != null) {
            this.mSuggestions.add(s);
            onChange(true);
        }
    }

    public void add(int i, S s) {
        if (s != null) {
            this.mSuggestions.add(i, s);
            onChange(true);
        }
    }

    public void replace(int i, S s) {
        if (s != null) {
            this.mSuggestions.set(i, s);
            onChange(true);
        }
    }

    public List<S> getSuggestions() {
        return this.mSuggestions;
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public int getCount() {
        return this.mSuggestions.size();
    }

    @Override // com.miui.gallery.search.core.suggestion.SuggestionCursor
    public S getCurrent() {
        if (getPosition() < 0 || getPosition() >= getCount()) {
            return null;
        }
        return this.mSuggestions.get(getPosition());
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public Source getSuggestionSource() {
        if (getCurrent() == null) {
            return null;
        }
        return getCurrent().getSuggestionSource();
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public String getIntentActionURI() {
        if (getCurrent() == null) {
            return null;
        }
        return getCurrent().getIntentActionURI();
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public String getSuggestionIcon() {
        if (getCurrent() == null) {
            return null;
        }
        return getCurrent().getSuggestionIcon();
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public String getSuggestionTitle() {
        if (getCurrent() == null) {
            return null;
        }
        return getCurrent().getSuggestionTitle();
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public String getSuggestionSubTitle() {
        if (getCurrent() == null) {
            return null;
        }
        return getCurrent().getSuggestionSubTitle();
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public String[] getColumnNames() {
        if (getExtraColumns() != this.EMPTY_COLUMNS) {
            String[] strArr = COLUMNS;
            ArrayList arrayList = new ArrayList(strArr.length + getExtraColumns().size());
            arrayList.addAll(Arrays.asList(strArr));
            arrayList.addAll(getExtraColumns());
            return (String[]) arrayList.toArray(new String[arrayList.size()]);
        }
        return COLUMNS;
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public SuggestionExtras getSuggestionExtras() {
        if (getCurrent() == null) {
            return null;
        }
        return getCurrent().getSuggestionExtras();
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public List<String> getBackupIcons() {
        if (getCurrent() == null) {
            return null;
        }
        return getCurrent().getBackupIcons();
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public List<String> getSuggestionIcons() {
        if (getCurrent() == null) {
            return null;
        }
        return getCurrent().getSuggestionIcons();
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public int getColumnIndex(String str) {
        if ("title".equals(str)) {
            return 0;
        }
        if ("subTitle".equals(str)) {
            return 1;
        }
        if ("actionUri".equals(str)) {
            return 2;
        }
        if (!CallMethod.RESULT_ICON.equals(str)) {
            return getExtraColumns().indexOf(str) + COLUMNS.length;
        }
        return 3;
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public String getString(int i) {
        if (i != 0) {
            if (i == 1) {
                return getSuggestionSubTitle();
            }
            if (i == 2) {
                return getIntentActionURI();
            }
            if (i == 3) {
                return getSuggestionIcon();
            }
            if (getSuggestionExtras() == null) {
                return null;
            }
            return getSuggestionExtras().getExtra(getExtraColumns().get(i - COLUMNS.length));
        }
        return getSuggestionTitle();
    }

    public final ArrayList<String> getExtraColumns() {
        if (this.mExtraColumns == null) {
            Collection<String> extraColumnNames = getSuggestionExtras() != null ? getSuggestionExtras().getExtraColumnNames() : null;
            if (extraColumnNames != null && extraColumnNames.size() > 0) {
                this.mExtraColumns = new ArrayList<>(extraColumnNames);
            } else {
                this.mExtraColumns = this.EMPTY_COLUMNS;
            }
        }
        return this.mExtraColumns;
    }

    @Override // android.database.AbstractCursor, android.database.CrossProcessCursor
    public boolean onMove(int i, int i2) {
        this.mExtraColumns = null;
        return super.onMove(i, i2);
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public boolean isNull(int i) {
        return getString(i) == null;
    }

    @Override // android.database.AbstractCursor, android.database.Cursor, com.miui.gallery.search.core.suggestion.SuggestionCursor
    public void setExtras(Bundle bundle) {
        if (bundle == null) {
            bundle = Bundle.EMPTY;
        }
        this.mExtras = bundle;
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public Bundle getExtras() {
        return this.mExtras;
    }

    public String toString() {
        return super.toString() + String.format("Suggestions = %s;", this.mSuggestions);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ListSuggestionCursor listSuggestionCursor = (ListSuggestionCursor) obj;
        return Objects.equals(this.mQueryInfo, listSuggestionCursor.mQueryInfo) && Objects.equals(this.mSuggestions, listSuggestionCursor.mSuggestions) && Objects.equals(this.mExtraColumns, listSuggestionCursor.mExtraColumns) && Objects.equals(this.mExtras, listSuggestionCursor.mExtras);
    }

    public int hashCode() {
        return Objects.hash(this.mQueryInfo, this.mSuggestions, this.mExtraColumns, this.mExtras);
    }
}
