package com.miui.gallery.search.core.suggestion;

import android.database.Cursor;
import android.os.Bundle;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.QuietlyCloseable;

/* loaded from: classes2.dex */
public interface SuggestionCursor extends Suggestion, QuietlyCloseable, Cursor {
    Suggestion getCurrent();

    QueryInfo getQueryInfo();

    void setExtras(Bundle bundle);
}
