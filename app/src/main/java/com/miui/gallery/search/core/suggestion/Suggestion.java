package com.miui.gallery.search.core.suggestion;

import com.miui.gallery.search.core.source.Source;
import java.util.Collections;
import java.util.List;

/* loaded from: classes2.dex */
public interface Suggestion {
    List<String> getBackupIcons();

    String getIntentActionURI();

    SuggestionExtras getSuggestionExtras();

    String getSuggestionIcon();

    Source getSuggestionSource();

    String getSuggestionSubTitle();

    String getSuggestionTitle();

    default List<String> getSuggestionIcons() {
        return Collections.EMPTY_LIST;
    }
}
