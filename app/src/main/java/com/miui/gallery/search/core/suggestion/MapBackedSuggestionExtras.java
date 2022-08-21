package com.miui.gallery.search.core.suggestion;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class MapBackedSuggestionExtras implements SuggestionExtras {
    public Map<String, String> mExtras;

    public MapBackedSuggestionExtras(Map<String, String> map) {
        this.mExtras = map;
    }

    public MapBackedSuggestionExtras(String str, String str2) {
        HashMap hashMap = new HashMap(1);
        this.mExtras = hashMap;
        hashMap.put(str, str2);
    }

    @Override // com.miui.gallery.search.core.suggestion.SuggestionExtras
    public Collection<String> getExtraColumnNames() {
        Map<String, String> map = this.mExtras;
        if (map != null) {
            return map.keySet();
        }
        return null;
    }

    @Override // com.miui.gallery.search.core.suggestion.SuggestionExtras
    public String getExtra(String str) {
        Map<String, String> map = this.mExtras;
        if (map != null) {
            return map.get(str);
        }
        return null;
    }

    public void putExtra(String str, String str2) {
        if (this.mExtras == null) {
            this.mExtras = new HashMap(1);
        }
        this.mExtras.put(str, str2);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof SuggestionExtras)) {
            return false;
        }
        if (obj == this || (getExtraColumnNames() == null && ((SuggestionExtras) obj).getExtraColumnNames() == null)) {
            return true;
        }
        SuggestionExtras suggestionExtras = (SuggestionExtras) obj;
        if (getExtraColumnNames() == null || suggestionExtras.getExtraColumnNames() == null) {
            return false;
        }
        for (String str : getExtraColumnNames()) {
            if (!getExtra(str).equals(suggestionExtras.getExtra(str))) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        return super.toString() + ", data:" + this.mExtras;
    }
}
