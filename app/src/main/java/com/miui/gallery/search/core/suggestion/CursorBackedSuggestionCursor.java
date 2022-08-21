package com.miui.gallery.search.core.suggestion;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.os.Bundle;
import android.util.SparseArray;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.result.ErrorInfo;
import com.miui.gallery.search.core.source.Source;
import com.xiaomi.mirror.synergy.CallMethod;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes2.dex */
public class CursorBackedSuggestionCursor<C extends Cursor> extends CursorWrapper implements SuggestionCursor {
    public static final SuggestionExtras EMPTY_EXTRA = new SuggestionExtras() { // from class: com.miui.gallery.search.core.suggestion.CursorBackedSuggestionCursor.1
        @Override // com.miui.gallery.search.core.suggestion.SuggestionExtras
        public String getExtra(String str) {
            return null;
        }

        @Override // com.miui.gallery.search.core.suggestion.SuggestionExtras
        public Collection<String> getExtraColumnNames() {
            return null;
        }
    };
    public int mActionURIIdx;
    public Bundle mExtras;
    public int mIconIdx;
    public final QueryInfo mQueryInfo;
    public int mSubTitleIdx;
    public SparseArray<SuggestionExtras> mSuggestionExtrasArray;
    public final Source mSuggestionSource;
    public int mTitleIdx;

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public List<String> getBackupIcons() {
        return null;
    }

    @Override // com.miui.gallery.search.core.suggestion.SuggestionCursor
    public Suggestion getCurrent() {
        return this;
    }

    public CursorBackedSuggestionCursor(QueryInfo queryInfo, C c) {
        this(queryInfo, c, null);
    }

    public CursorBackedSuggestionCursor(QueryInfo queryInfo, C c, Source source) {
        this(queryInfo, c, source, null);
    }

    public CursorBackedSuggestionCursor(QueryInfo queryInfo, C c, Source source, ErrorInfo errorInfo) {
        super(c);
        this.mExtras = Bundle.EMPTY;
        this.mQueryInfo = queryInfo;
        this.mSuggestionSource = source;
        updateIndexes();
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public Source getSuggestionSource() {
        return this.mSuggestionSource;
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public String getIntentActionURI() {
        return getStringOrNull(this.mActionURIIdx);
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public String getSuggestionIcon() {
        return getStringOrNull(this.mIconIdx);
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public String getSuggestionTitle() {
        return getStringOrNull(this.mTitleIdx);
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public String getSuggestionSubTitle() {
        return getStringOrNull(this.mSubTitleIdx);
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public SuggestionExtras getSuggestionExtras() {
        String stringOrNull;
        HashMap hashMap = null;
        if (getWrappedCursor() == null) {
            return null;
        }
        if (this.mSuggestionExtrasArray == null) {
            this.mSuggestionExtrasArray = new SparseArray<>(getCount());
        }
        SuggestionExtras suggestionExtras = this.mSuggestionExtrasArray.get(getWrappedCursor().getPosition());
        SuggestionExtras suggestionExtras2 = EMPTY_EXTRA;
        if (suggestionExtras == suggestionExtras2) {
            return suggestionExtras2;
        }
        if (suggestionExtras == null) {
            String[] columnNames = getWrappedCursor().getColumnNames();
            for (int i = 0; i < columnNames.length; i++) {
                if (isExtraIndex(i) && columnNames[i] != null && (stringOrNull = getStringOrNull(i)) != null) {
                    if (hashMap == null) {
                        hashMap = new HashMap();
                    }
                    hashMap.put(columnNames[i], stringOrNull);
                }
            }
            if (hashMap != null) {
                this.mSuggestionExtrasArray.put(getPosition(), new MapBackedSuggestionExtras(hashMap));
            } else {
                this.mSuggestionExtrasArray.put(getPosition(), EMPTY_EXTRA);
            }
        }
        return this.mSuggestionExtrasArray.get(getPosition());
    }

    public final boolean isExtraIndex(int i) {
        return (i == this.mIconIdx || i == this.mActionURIIdx || i == this.mTitleIdx || i == this.mSubTitleIdx) ? false : true;
    }

    public void updateIndexes() {
        this.mTitleIdx = getColumnIndex("title");
        this.mSubTitleIdx = getColumnIndex("subTitle");
        this.mIconIdx = getColumnIndex(CallMethod.RESULT_ICON);
        this.mActionURIIdx = getColumnIndex("actionUri");
    }

    public String getStringOrNull(int i) {
        if (i == -1) {
            return null;
        }
        return getWrappedCursor().getString(i);
    }

    @Override // android.database.CursorWrapper, android.database.Cursor
    public Bundle getExtras() {
        return getWrappedCursor().getExtras() == null ? this.mExtras : getWrappedCursor().getExtras();
    }

    @Override // android.database.CursorWrapper, android.database.Cursor, com.miui.gallery.search.core.suggestion.SuggestionCursor
    public void setExtras(Bundle bundle) {
        if (bundle == null) {
            bundle = Bundle.EMPTY;
        }
        this.mExtras = bundle;
    }
}
