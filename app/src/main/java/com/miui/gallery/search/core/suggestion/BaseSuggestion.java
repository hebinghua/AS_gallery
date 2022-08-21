package com.miui.gallery.search.core.suggestion;

import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.search.core.source.Source;
import java.util.List;
import java.util.Objects;

/* loaded from: classes2.dex */
public class BaseSuggestion implements Suggestion {
    public List<String> mBackupIcons;
    public String mIcon;
    public String mIntentActionURI;
    public String mSubTitle;
    public SuggestionExtras mSuggestionExtras;
    public List<String> mSuggestionIcons;
    public Source mSuggestionSource;
    public String mTitle;

    public BaseSuggestion() {
    }

    public BaseSuggestion(String str, String str2, String str3, String str4, SuggestionExtras suggestionExtras, Source source, List<String> list) {
        this.mTitle = str;
        this.mIcon = str3;
        this.mIntentActionURI = str4;
        this.mSubTitle = str2;
        this.mSuggestionExtras = suggestionExtras;
        this.mSuggestionSource = source;
        this.mBackupIcons = list;
    }

    public BaseSuggestion(String str, String str2, String str3, String str4, SuggestionExtras suggestionExtras, Source source) {
        this(str, str2, str3, str4, suggestionExtras, source, null);
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public String getIntentActionURI() {
        return this.mIntentActionURI;
    }

    public void setIntentActionURI(String str) {
        this.mIntentActionURI = str;
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public String getSuggestionIcon() {
        return this.mIcon;
    }

    public void setSuggestionIcon(String str) {
        this.mIcon = str;
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public String getSuggestionTitle() {
        return this.mTitle;
    }

    public void setSuggestionTitle(String str) {
        this.mTitle = str;
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public String getSuggestionSubTitle() {
        return this.mSubTitle;
    }

    public void setSuggestionSubTitle(String str) {
        this.mSubTitle = str;
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public Source getSuggestionSource() {
        return this.mSuggestionSource;
    }

    public void setSuggestionSource(Source source) {
        this.mSuggestionSource = source;
    }

    public void setSuggestionIcons(List<String> list) {
        this.mSuggestionIcons = list;
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public List<String> getSuggestionIcons() {
        return this.mSuggestionIcons;
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public SuggestionExtras getSuggestionExtras() {
        return this.mSuggestionExtras;
    }

    public void setSuggestionExtras(SuggestionExtras suggestionExtras) {
        this.mSuggestionExtras = suggestionExtras;
    }

    @Override // com.miui.gallery.search.core.suggestion.Suggestion
    public List<String> getBackupIcons() {
        return this.mBackupIcons;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BaseSuggestion baseSuggestion = (BaseSuggestion) obj;
        return Objects.equals(this.mBackupIcons, baseSuggestion.mBackupIcons) && Objects.equals(this.mSuggestionIcons, baseSuggestion.mSuggestionIcons) && Objects.equals(this.mTitle, baseSuggestion.mTitle) && Objects.equals(this.mIcon, baseSuggestion.mIcon) && Objects.equals(this.mIntentActionURI, baseSuggestion.mIntentActionURI) && Objects.equals(this.mSubTitle, baseSuggestion.mSubTitle) && Objects.equals(this.mSuggestionExtras, baseSuggestion.mSuggestionExtras) && Objects.equals(this.mSuggestionSource, baseSuggestion.mSuggestionSource);
    }

    public int hashCode() {
        return Objects.hash(this.mBackupIcons, this.mSuggestionIcons, this.mTitle, this.mIcon, this.mIntentActionURI, this.mSubTitle, this.mSuggestionExtras, this.mSuggestionSource);
    }

    public String toString() {
        return "BaseSuggestion{mBackupIcons=" + this.mBackupIcons + ", mSuggestionIcons=" + this.mSuggestionIcons + ", mTitle='" + this.mTitle + CoreConstants.SINGLE_QUOTE_CHAR + ", mIcon='" + this.mIcon + CoreConstants.SINGLE_QUOTE_CHAR + ", mIntentActionURI='" + this.mIntentActionURI + CoreConstants.SINGLE_QUOTE_CHAR + ", mSubTitle='" + this.mSubTitle + CoreConstants.SINGLE_QUOTE_CHAR + ", mSuggestionExtras=" + this.mSuggestionExtras + ", mSuggestionSource=" + this.mSuggestionSource + '}';
    }
}
