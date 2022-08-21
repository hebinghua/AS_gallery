package com.miui.gallery.search.core.result;

import android.database.ContentObserver;
import android.database.DataSetObserver;
import android.os.Bundle;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.suggestion.SuggestionCursor;
import com.miui.gallery.search.utils.SearchLog;

/* loaded from: classes2.dex */
public class BaseSuggestionResult<S extends SuggestionCursor> implements SuggestionResult<S> {
    public boolean mClosed;
    public final S mData;
    public ErrorInfo mErrorInfo;
    public Bundle mExtras;
    public final QueryInfo mQueryInfo;
    public int mRefCount;

    public BaseSuggestionResult(QueryInfo queryInfo, S s) {
        this(queryInfo, s, null);
    }

    public BaseSuggestionResult(QueryInfo queryInfo, S s, ErrorInfo errorInfo) {
        this.mClosed = false;
        this.mRefCount = 0;
        this.mExtras = Bundle.EMPTY;
        this.mQueryInfo = queryInfo;
        this.mData = s;
        this.mErrorInfo = errorInfo;
    }

    @Override // com.miui.gallery.search.core.result.SuggestionResult
    public S getData() {
        S s = this.mData;
        if (s != null && !s.isClosed()) {
            return this.mData;
        }
        return null;
    }

    @Override // com.miui.gallery.search.core.result.SuggestionResult
    public ErrorInfo getErrorInfo() {
        if (isClosed()) {
            throw new IllegalStateException("getErrorMessage() when closed");
        }
        return this.mErrorInfo;
    }

    public void setErrorInfo(ErrorInfo errorInfo) {
        this.mErrorInfo = errorInfo;
    }

    @Override // com.miui.gallery.search.core.result.SuggestionResult
    public void acquire() {
        this.mRefCount++;
    }

    @Override // com.miui.gallery.search.core.result.SuggestionResult
    public void release() {
        int i = this.mRefCount - 1;
        this.mRefCount = i;
        if (i > 0 || isClosed()) {
            return;
        }
        close();
    }

    @Override // com.miui.gallery.search.core.result.SuggestionResult
    public boolean isClosed() {
        return this.mClosed;
    }

    public final void close() {
        SearchLog.d("BaseSuggestionResult", "SuggestionResult close() [" + hashCode() + "]");
        if (this.mClosed) {
            SearchLog.e("BaseSuggestionResult", "Double close()");
            return;
        }
        this.mClosed = true;
        try {
            S s = this.mData;
            if (s != null && !s.isClosed()) {
                this.mData.close();
            } else if (this.mData != null) {
                SearchLog.w("BaseSuggestionResult", this + "]'s data is already closed before result is closed!");
            }
        } catch (Exception e) {
            SearchLog.e("BaseSuggestionResult", e);
        }
    }

    @Override // com.miui.gallery.search.core.result.SuggestionResult
    public boolean isEmpty() {
        S s = this.mData;
        return s == null || s.getCount() <= 0;
    }

    public void finalize() {
        if (!isClosed()) {
            SearchLog.e("BaseSuggestionResult", "LEAK! Finalized without being closed: BaseSuggestionResult[" + getQueryInfo() + "]");
            close();
        }
    }

    @Override // com.miui.gallery.search.core.result.SuggestionResult
    public Bundle getResultExtras() {
        return this.mExtras;
    }

    @Override // com.miui.gallery.search.core.result.SuggestionResult
    public void setResultExtras(Bundle bundle) {
        if (bundle == null) {
            bundle = Bundle.EMPTY;
        }
        this.mExtras = bundle;
    }

    @Override // com.miui.gallery.search.core.result.SuggestionResult
    public QueryInfo getQueryInfo() {
        return this.mQueryInfo;
    }

    @Override // com.miui.gallery.search.core.result.SuggestionResult
    public void registerContentObserver(ContentObserver contentObserver) {
        if (getData() != null) {
            this.mData.registerContentObserver(contentObserver);
        }
    }

    @Override // com.miui.gallery.search.core.result.SuggestionResult
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        if (getData() != null) {
            this.mData.registerDataSetObserver(dataSetObserver);
        }
    }
}
