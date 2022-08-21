package com.miui.gallery.model.dto;

import com.miui.gallery.search.core.suggestion.SuggestionExtras;
import java.util.List;

/* loaded from: classes2.dex */
public class SuggestionData {
    public String SubTitle;
    public List<String> mBackupIcons;
    public SuggestionExtras mExtras;
    public String mIcon;
    public String mIntentActionURI;
    public String mTitle;

    public String getIntentActionURI() {
        return this.mIntentActionURI;
    }

    public void setIntentActionURI(String str) {
        this.mIntentActionURI = str;
    }

    public String getIcon() {
        return this.mIcon;
    }

    public void setIcon(String str) {
        this.mIcon = str;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setTitle(String str) {
        this.mTitle = str;
    }

    public void setSubTitle(String str) {
        this.SubTitle = str;
    }

    public void setExtras(SuggestionExtras suggestionExtras) {
        this.mExtras = suggestionExtras;
    }

    public List<String> getBackupIcons() {
        return this.mBackupIcons;
    }

    public void setBackupIcons(List<String> list) {
        this.mBackupIcons = list;
    }
}
